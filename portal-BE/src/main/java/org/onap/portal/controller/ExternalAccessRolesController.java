/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 *
 */

package org.onap.portal.controller;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.ecomp.EcompAuditLog;
import org.onap.portal.domain.dto.transport.CentralRole;
import org.onap.portal.domain.dto.transport.CentralRoleFunction;
import org.onap.portal.domain.dto.transport.CentralUser;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.exception.InvalidRoleException;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.ExternalAccessRolesService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portal.validation.DataValidator;
import org.onap.portal.validation.SecureString;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.AuditServiceImpl;
import org.onap.portalsdk.core.service.UserServiceCentalizedImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auxapi")
@EnableAspectJAutoProxy
@EPAuditLog
public class ExternalAccessRolesController {

    private static final String ROLE_INVALID_CHARS = "%=():,\"\"";
    private static final String SUCCESSFULLY_DELETED = "Successfully Deleted";
    private static final String INVALID_UEB_KEY = "Invalid credentials!";
    private static final String LOGIN_ID = "LoginId";
    private static final String UEBKEY = "uebkey";

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesController.class);
    private static final UserServiceCentalizedImpl userService = new UserServiceCentalizedImpl();
    private static final AuditService auditService = new AuditServiceImpl();

    private final ExternalAccessRolesService externalAccessRolesService;
    private final FnUserService fnUserService;
    private final DataValidator dataValidator;

    @Autowired
    public ExternalAccessRolesController(FnUserService fnUserService,
        DataValidator dataValidator, ExternalAccessRolesService externalAccessRolesService) {
        this.fnUserService = fnUserService;
        this.dataValidator = dataValidator;
        this.externalAccessRolesService = externalAccessRolesService;
    }

    @ApiOperation(value = "Gets user role for an application.", response = CentralUser.class, responseContainer = "List")
    @RequestMapping(value = {
        "/user/{loginId}"}, method = RequestMethod.GET, produces = "application/json")
    public CentralUser getUser(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("loginId") String loginId) throws Exception {
        if (!dataValidator.isValid(new SecureString(loginId))) {
            sendErrorResponse(response, new Exception("Data is not valid"));
            logger.error(EELFLoggerDelegate.errorLogger, "getUser not valid data");
            return null;
        }
        CentralUser answer = null;
        try {
            fieldsValidation(request);
            answer = externalAccessRolesService.getUserRoles(loginId, request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getUser failed", e);
        }
        return answer;
    }

    @ApiOperation(value = "Gets user roles for an application which is upgraded to newer version.", response = String.class, responseContainer = "List")
    @RequestMapping(value = {
        "/v1/user/{loginId}"}, method = RequestMethod.GET, produces = "application/json")
    public String getV2UserList(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("loginId") String loginId) throws Exception {
        if (!dataValidator.isValid(new SecureString(loginId))) {
            sendErrorResponse(response, new Exception("Data is not valid"));
            logger.error(EELFLoggerDelegate.errorLogger, "getV2UserList not valid data");
            return "Data is not valid";
        }
        String answer = null;
        try {
            fieldsValidation(request);
            answer = externalAccessRolesService.getV2UserWithRoles(loginId, request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2UserList failed", e);
        }
        return answer;
    }

    @ApiOperation(value = "Gets roles for an application.", response = CentralRole.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/roles"}, method = RequestMethod.GET, produces = "application/json")
    public List<CentralRole> getRolesForApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRolesForApp");
        List<CentralV2Role> v2CenRole;
        List<CentralRole> cenRole = null;
        try {
            fieldsValidation(request);
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            logger
                .debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Entering into syncApplicationRolesWithEcompDB");
            externalAccessRolesService.syncApplicationRolesWithEcompDB(app);
            logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Finished syncApplicationRolesWithEcompDB");
            v2CenRole = externalAccessRolesService.getRolesForApp(request.getHeader(UEBKEY));
            cenRole = externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(v2CenRole);
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getRolesForApp failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRolesForApp");
        return cenRole;
    }

    @ApiOperation(value = "Gets roles for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/v1/roles"}, method = RequestMethod.GET, produces = "application/json")
    public List<CentralV2Role> getV2RolesForApp(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getV2RolesForApp");
        List<CentralV2Role> answer = null;
        try {
            fieldsValidation(request);
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            // Sync all roles from external system into ONAP portal DB
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getV2RolesForApp: Entering into syncApplicationRolesWithEcompDB");
            externalAccessRolesService.syncApplicationRolesWithEcompDB(app);
            logger.debug(EELFLoggerDelegate.debugLogger, "getV2RolesForApp: Finished syncApplicationRolesWithEcompDB");
            answer = externalAccessRolesService.getRolesForApp(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2RolesForApp failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getV2RolesForApp");
        return answer;
    }

    @ApiOperation(value = "Gets all role functions for an application for older version.", response = CentralRoleFunction.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/functions"}, method = RequestMethod.GET, produces = "application/json")
    public List<CentralRoleFunction> getRoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        List<EpAppFunction> answer;
        List<CentralRoleFunction> roleFuncList = null;
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRoleFunctionsList");
        try {
            fieldsValidation(request);
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            // Sync all functions from external system into ONAP portal DB
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getRoleFunctionsList: Entering into syncRoleFunctionFromExternalAccessSystem");
            externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getRoleFunctionsList: Finished syncRoleFunctionFromExternalAccessSystem");
            answer = externalAccessRolesService.getRoleFuncList(request.getHeader(UEBKEY));
            roleFuncList = externalAccessRolesService.convertCentralRoleFunctionToRoleFunctionObject(answer);
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunctionsList failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRoleFunctionsList");
        return roleFuncList;
    }

    @ApiOperation(value = "Gets all role functions for an application which is upgraded to newer version.", response = EpAppFunction.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/v1/functions"}, method = RequestMethod.GET, produces = "application/json")
    public List<EpAppFunction> getV2RoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        List<EpAppFunction> cenRoleFuncList = null;
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getV2RoleFunctionsList");
        try {
            fieldsValidation(request);
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            // Sync all functions from external system into ONAP portal DB
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getV2RoleFunctionsList: Entering into syncRoleFunctionFromExternalAccessSystem");
            externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getV2RoleFunctionsList: Finished syncRoleFunctionFromExternalAccessSystem");
            cenRoleFuncList = externalAccessRolesService.getRoleFuncList(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleFunctionsList failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getV2RoleFunctionsList");
        return cenRoleFuncList;
    }


    @ApiOperation(value = "Gets role information for an application.", response = CentralRole.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/role/{role_id}"}, method = RequestMethod.GET, produces = "application/json")
    public CentralRole getRoleInfo(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("role_id") Long roleId) throws Exception {
        CentralV2Role answer;
        CentralRole cenRole = null;
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRoleInfo");
        try {
            fieldsValidation(request);
            answer = externalAccessRolesService.getRoleInfo(roleId, request.getHeader(UEBKEY));
            cenRole = externalAccessRolesService.convertV2CentralRoleToOldVerisonCentralRole(answer);
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRoleInfo");
        return cenRole;
    }

    @ApiOperation(value = "Gets v2 role information for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/v1/role/{role_id}"}, method = RequestMethod.GET, produces = "application/json")
    public CentralV2Role getV2RoleInfo(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("role_id") Long roleId) throws Exception {
        CentralV2Role answer = null;
        logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getV2RoleInfo");
        try {
            fieldsValidation(request);
            answer = externalAccessRolesService.getRoleInfo(roleId, request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleInfo failed", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getV2RoleInfo");
        return answer;
    }

    @ApiOperation(value = "Gets role information for an application provided by function code.", response = CentralRoleFunction.class, responseContainer = "Json")
    @RequestMapping(value = {"/function/{code}"}, method = RequestMethod.GET, produces = "application/json")
    public CentralRoleFunction getRoleFunction(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("code") String code) throws Exception {
        EpAppFunction EpAppFunction;
        CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
        if (!dataValidator.isValid(new SecureString(code))) {
            sendErrorResponse(response, new Exception("Data is not valid"));
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", new Exception("Data is not valid"));
        }
        try {
            fieldsValidation(request);
            EpAppFunction = externalAccessRolesService.getRoleFunction(code, request.getHeader(UEBKEY));
            if (EpAppFunction != null && EcompPortalUtils.getFunctionCode(EpAppFunction.getFunctionCd()).equals(code)) {
                BeanUtils.copyProperties(EpAppFunction, centralRoleFunction, "type", "action");
            }
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", e);
        }
        return centralRoleFunction;
    }

    @ApiOperation(value = "Gets role information for an application provided by function code.", response = EpAppFunction.class, responseContainer = "Json")
    @RequestMapping(value = {"/v1/function/{code}"}, method = RequestMethod.GET, produces = "application/json")
    public EpAppFunction getV2RoleFunction(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("code") String code) throws Exception {
        EpAppFunction EpAppFunction = null;
        if (!dataValidator.isValid(new SecureString(code))) {
            sendErrorResponse(response, new Exception("Data is not valid"));
            logger
                .error(EELFLoggerDelegate.errorLogger, "getV2RoleFunction failed", new Exception("Data is not valid"));
        }
        try {
            fieldsValidation(request);
            EpAppFunction = externalAccessRolesService.getRoleFunction(code, request.getHeader(UEBKEY));
            if (EpAppFunction == null || !EcompPortalUtils.getFunctionCode(EpAppFunction.getFunctionCd())
                .equals(code)) {
                EpAppFunction = new EpAppFunction();
            }
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleFunction failed", e);
        }
        return EpAppFunction;
    }

    @ApiOperation(value = "Saves role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/roleFunction"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response,
        @RequestBody String roleFunc) {
        String status = "Successfully saved!";
        if (!dataValidator.isValid(new SecureString(roleFunc))) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed");
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                "Failed to roleFunc, not valid data.", "Failed");
        }
        try {
            fieldsValidation(request);
            ObjectMapper mapper = new ObjectMapper();
            List<FnApp> applicationList = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
            FnApp requestedApp = applicationList.get(0);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            EpAppFunction availableRoleFunction = mapper.readValue(roleFunc, EpAppFunction.class);
            EpAppFunction domainRoleFunction;
            boolean isCentralV2Version = false;
            if (availableRoleFunction.getType() != null && availableRoleFunction.getAction() != null) {
                isCentralV2Version = true;
            }
            if (isCentralV2Version) {
                String code = availableRoleFunction.getType() + "|" + availableRoleFunction.getFunctionCd() + "|"
                    + availableRoleFunction.getAction();
                domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
                    requestedApp.getUebKey());
            } else {
                domainRoleFunction = externalAccessRolesService.getRoleFunction(availableRoleFunction.getFunctionCd(),
                    requestedApp.getUebKey());
            }

            boolean saveOrUpdateResponse;
            if (domainRoleFunction != null && isCentralV2Version && domainRoleFunction.getFunctionCd()
                .equals(availableRoleFunction.getFunctionCd())
                && domainRoleFunction.getType().equals(availableRoleFunction.getType())
                && domainRoleFunction.getAction().equals(availableRoleFunction.getAction())) {
                domainRoleFunction.setFunctionName(availableRoleFunction.getFunctionName());
                saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(domainRoleFunction,
                    requestedApp);
            } else {
                availableRoleFunction.setAppId(requestedApp);
                saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(availableRoleFunction,
                    requestedApp);
            }

            if (domainRoleFunction != null) {
                status = "Successfully updated!";
            }
            if (saveOrUpdateResponse) {
                FnUser user = fnUserService.loadUserByUsername(request.getHeader(LOGIN_ID));
                FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
                String activityCode = (!status.equals("Successfully updated!"))
                    ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION
                    : EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION;
                logger.info(EELFLoggerDelegate.applicationLogger, "saveRoleFunction: succeeded for app {}, function {}",
                    app.getId(), availableRoleFunction.getFunctionCd());
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(activityCode);
                auditLog.setComments(
                    EcompPortalUtils.truncateString(
                        "saveRoleFunction role for app:" + app.getId() + " and function:'"
                            + availableRoleFunction.getFunctionCd() + "'",
                        PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                auditLog.setAffectedRecordId(user.getOrgUserId());
                auditService.logActivity(auditLog, null);
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRoleFunction", activityCode,
                        String.valueOf(user.getId()), user.getOrgUserId(), availableRoleFunction.getFunctionCd()));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            } else {
                logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    "Failed to saveRoleFunction for '" + availableRoleFunction.getFunctionCd() + "'", "Failed");
            }
        } catch (Exception e) {
            if (e.getMessage() == null || e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, status, "Success");
    }

    @ApiOperation(value = "Deletes role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/roleFunction/{code}"}, method = RequestMethod.DELETE, produces = "application/json")
    public PortalRestResponse<String> deleteRoleFunction(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("code") String code) {
        if (!dataValidator.isValid(new SecureString(code))) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed");
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                "Failed to deleteRoleFunction, not valid data.", "Failed");
        }
        try {
            fieldsValidation(request);
            FnUser user = fnUserService.loadUserByUsername(request.getHeader(LOGIN_ID));
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            boolean getDelFuncResponse = externalAccessRolesService.deleteCentralRoleFunction(code, app);
            if (getDelFuncResponse) {
                logger.info(EELFLoggerDelegate.applicationLogger, "deleteRoleFunction: succeeded for app {}, role {}",
                    app.getId(), code);
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION);
                auditLog.setComments(EcompPortalUtils.truncateString(
                    "Deleted function for app:" + app.getId() + " and function code:'" + code + "'",
                    PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                auditLog.setAffectedRecordId(user.getOrgUserId());
                auditService.logActivity(auditLog, null);
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRoleFunction",
                        EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION, String.valueOf(user.getId()),
                        user.getOrgUserId(), code));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            } else {
                logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    "Failed to deleteRoleFunction for '" + code + "'", "Failed");
            }
        } catch (Exception e) {
            if (e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESSFULLY_DELETED, "Success");

    }

    @ApiOperation(value = "Saves role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/role"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> saveRole(HttpServletRequest request, HttpServletResponse response,
        @RequestBody Role role) {
        try {
            fieldsValidation(request);
            ExternalRequestFieldsValidator saveRoleResult;
            FnUser user = fnUserService.loadUserByUsername(request.getHeader(LOGIN_ID));
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            if (role.getId() != null && StringUtils.containsAny(role.getName(), ROLE_INVALID_CHARS)) {
                throw new InvalidRoleException("Invalid role name found for '" + role.getName()
                    + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed");
            }
            saveRoleResult = externalAccessRolesService.saveRoleForApplication(role, request.getHeader(UEBKEY));
            if (saveRoleResult.isResult()) {
                String activityCode = (role.getId() == null) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_ROLE
                    : EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_ROLE_AND_FUNCTION;
                logger.info(EELFLoggerDelegate.applicationLogger, "saveRole: succeeded for app {}, role {}",
                    app.getId(), role.getName());
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(activityCode);
                auditLog.setComments(EcompPortalUtils.truncateString(
                    "saveRole role for app:" + app.getId() + " and role:'" + role.getName() + "'",
                    PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                auditLog.setAffectedRecordId(user.getOrgUserId());
                auditService.logActivity(auditLog, null);
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRole", activityCode,
                        String.valueOf(user.getId()), user.getOrgUserId(), role.getName()));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            } else {
                if (saveRoleResult.getDetailMessage().contains("406")) {
                    response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                        "Failed to create a role for '" + role.getName()
                            + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed"
                        , "Failed");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                        "Failed to saveRole for '" + role.getName() + "'", "Failed");
                }
            }
        } catch (Exception e) {
            if (e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully Saved", "Success");
    }

    @ApiOperation(value = "Deletes role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/deleteRole/{code}"}, method = RequestMethod.DELETE, produces = "application/json")
    public PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String code) {
        if (!dataValidator.isValid(new SecureString(code))) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed");
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                "Failed to deleteRole, not valid data.", "Failed");
        }
        try {
            fieldsValidation(request);
            boolean deleteResponse = externalAccessRolesService.deleteRoleForApplication(code,
                request.getHeader(UEBKEY));
            if (deleteResponse) {
                FnUser user = fnUserService.loadUserByUsername(request.getHeader(LOGIN_ID));
                FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
                logger.info(EELFLoggerDelegate.applicationLogger, "deleteRole: succeeded for app {}, role {}",
                    app.getId(), code);
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE);
                auditLog.setComments(EcompPortalUtils.truncateString(
                    "Deleted role for app:" + app.getId() + " and role:'" + code + "'",
                    PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                auditLog.setAffectedRecordId(user.getOrgUserId());
                auditService.logActivity(auditLog, null);
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRole",
                        EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE, String.valueOf(user.getId()),
                        user.getOrgUserId(), code));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            } else {
                logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    "Failed to deleteRole for '" + code + "'", "Failed");
            }
        } catch (Exception e) {
            if (e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESSFULLY_DELETED, "Success");
    }

    @ApiOperation(value = "Gets active roles for an application.", response = CentralRole.class, responseContainer = "Json")
    @RequestMapping(value = {"/activeRoles"}, method = RequestMethod.GET, produces = "application/json")
    public List<CentralRole> getActiveRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CentralRole> roles = null;
        try {
            fieldsValidation(request);
            List<CentralV2Role> cenRoles = externalAccessRolesService.getActiveRoles(request.getHeader(UEBKEY));
            roles = externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(cenRoles);
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
        }
        return roles;

    }

    @ApiOperation(value = "Gets active roles for an application.", response = CentralV2Role.class, responseContainer = "Json")
    @RequestMapping(value = {"/v1/activeRoles"}, method = RequestMethod.GET, produces = "application/json")
    public List<CentralV2Role> getV2ActiveRoles(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        List<CentralV2Role> cenRole = null;
        try {
            fieldsValidation(request);
            cenRole = externalAccessRolesService.getActiveRoles(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getV2ActiveRoles failed", e);
        }
        return cenRole;

    }

    @ApiOperation(value = "deletes user roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/deleteDependcyRoleRecord/{roleId}"}, method = RequestMethod.DELETE, produces = "application/json")
    public PortalRestResponse<String> deleteDependencyRoleRecord(HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("roleId") Long roleId) {
        ExternalRequestFieldsValidator removeResult;
        try {
            fieldsValidation(request);
            removeResult = externalAccessRolesService.deleteDependencyRoleRecord(roleId,
                request.getHeader(UEBKEY), request.getHeader(LOGIN_ID));
            if (!removeResult.isResult()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to deleteDependencyRoleRecord",
                    "Failed");
            }
        } catch (Exception e) {
            if (e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESSFULLY_DELETED, "Success");
    }


    @ApiOperation(value = "deletes  roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/v2/deleteRole/{roleId}"}, method = RequestMethod.DELETE, produces = "application/json")
    public PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("roleId") Long roleId) {
        ExternalRequestFieldsValidator removeResult;
        try {
            fieldsValidation(request);
            removeResult = externalAccessRolesService.deleteDependencyRoleRecord(roleId,
                request.getHeader(UEBKEY), request.getHeader(LOGIN_ID));
            if (!removeResult.isResult()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to deleteRole",
                    "Failed");
            }
        } catch (Exception e) {
            if (e.getMessage().contains(INVALID_UEB_KEY)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESSFULLY_DELETED, "Success");
    }


    @ApiOperation(value = "Bulk upload functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/upload/portal/functions"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadFunctions(HttpServletRequest request, HttpServletResponse response) {
        Integer result;
        try {
            result = externalAccessRolesService.bulkUploadFunctions(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadFunctions", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
    }

    @ApiOperation(value = "Bulk upload roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/upload/portal/roles"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadRoles(HttpServletRequest request, HttpServletResponse response) {
        Integer result;
        try {
            result = externalAccessRolesService.bulkUploadRoles(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
    }

    @ApiOperation(value = "Bulk upload role functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/upload/portal/roleFunctions"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadRoleFunctions(HttpServletRequest request,
        HttpServletResponse response) {
        Integer result;
        try {
            result = externalAccessRolesService.bulkUploadRolesFunctions(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoleFunctions failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoleFunctions", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
    }

    @ApiOperation(value = "Bulk upload user roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/upload/portal/userRoles"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadUserRoles(HttpServletRequest request, HttpServletResponse response) {
        Integer result;
        try {
            result = externalAccessRolesService.bulkUploadUserRoles(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUserRoles failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUserRoles", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
    }

    @ApiOperation(value = "Bulk upload users for renamed role of an application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/upload/portal/userRole/{roleId}"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadUsersSingleRole(HttpServletRequest request,
        HttpServletResponse response, @PathVariable Long roleId) {
        Integer result;
        try {
            String roleName = request.getHeader("RoleName");
            result = externalAccessRolesService.bulkUploadUsersSingleRole(request.getHeader(UEBKEY), roleId, roleName);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUsersSingleRole failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUsersSingleRole",
                "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
    }

    @ApiOperation(value = "Bulk upload functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/upload/partner/functions"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadPartnerFunctions(HttpServletRequest request,
        HttpServletResponse response) {
        Integer addedFunctions;
        try {
            addedFunctions = externalAccessRolesService.bulkUploadPartnerFunctions(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadFunctions", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK,
            "Successfully added: '" + addedFunctions + "' functions", "Success");
    }

    @ApiOperation(value = "Bulk upload roles for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {"/upload/partner/roles"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadPartnerRoles(HttpServletRequest request, HttpServletResponse response,
        @RequestBody List<Role> upload) {
        try {
            externalAccessRolesService.bulkUploadPartnerRoles(request.getHeader(UEBKEY), upload);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added", "Success");
    }

    @ApiOperation(value = "Bulk upload role functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
    @RequestMapping(value = {
        "/upload/partner/roleFunctions"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadPartnerRoleFunctions(HttpServletRequest request,
        HttpServletResponse response) {
        Integer addedRoleFunctions;
        try {
            addedRoleFunctions = externalAccessRolesService.bulkUploadPartnerRoleFunctions(request.getHeader(UEBKEY));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerRoleFunctions failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadPartnerRoleFunctions",
                "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK,
            "Successfully added: '" + addedRoleFunctions + "' role functions", "Success");
    }

    @ApiOperation(value = "Gets all functions along with global functions", response = List.class, responseContainer = "Json")
    @RequestMapping(value = {"/menuFunctions"}, method = RequestMethod.GET, produces = "application/json")
    public List<String> getMenuFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> functionsList = null;
        try {
            fieldsValidation(request);
            functionsList = externalAccessRolesService.getMenuFunctionsList(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getMenuFunctions failed", e);
        }
        return functionsList;
    }

    @ApiOperation(value = "Gets all active Users of application", response = String.class, responseContainer = "Json")
    @RequestMapping(value = {"/users"}, method = RequestMethod.GET, produces = "application/json")
    public List<EcompUser> getUsersOfApplication(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        List<EcompUser> users = null;
        try {
            fieldsValidation(request);
            users = externalAccessRolesService.getAllAppUsers(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getUsersOfApplication failed", e);
        }
        return users;
    }

    @ApiOperation(value = "Gets ecompUser of an application.", response = CentralUser.class, responseContainer = "List")
    @RequestMapping(value = {"/v2/user/{loginId}"}, method = RequestMethod.GET, produces = "application/json")
    public String getEcompUser(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("loginId") String loginId) throws Exception {
        if (!dataValidator.isValid(new SecureString(loginId))) {
            sendErrorResponse(response, new Exception("getEcompUser failed"));
            logger.error(EELFLoggerDelegate.errorLogger, "getEcompUser failed", new Exception("getEcompUser failed"));
        }
        EcompUser user = new EcompUser();
        ObjectMapper mapper = new ObjectMapper();
        String answer;
        try {
            fieldsValidation(request);

            answer = externalAccessRolesService.getV2UserWithRoles(loginId, request.getHeader(UEBKEY));
            if (answer != null) {
                User ecompUser = userService.userMapper(answer);
                user = UserUtils.convertToEcompUser(ecompUser);
                List<EcompRole> missingRolesOfUser = externalAccessRolesService
                    .missingUserApplicationRoles(request.getHeader(UEBKEY), loginId, user.getRoles());
                if (missingRolesOfUser.size() > 0) {
                    Set<EcompRole> roles = new TreeSet<>(missingRolesOfUser);
                    user.getRoles().addAll(roles);
                }
            }
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getEcompUser failed", e);
        }
        return mapper.writeValueAsString(user);
    }

    @ApiOperation(value = "Gets user ecomp role for an application.", response = CentralUser.class, responseContainer = "List")
    @RequestMapping(value = {"/v2/roles"}, method = RequestMethod.GET, produces = "application/json")
    public List<EcompRole> getEcompRolesOfApplication(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        List<EcompRole> ecompRoles = null;
        ObjectMapper mapper = new ObjectMapper();
        List<CentralV2Role> cenRole = null;
        try {
            fieldsValidation(request);
            FnApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
            // Sync all roles from external system into ONAP portal DB
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getRolesForApp: Entering into syncApplicationRolesWithEcompDB");
            externalAccessRolesService.syncApplicationRolesWithEcompDB(app);
            logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Finished syncApplicationRolesWithEcompDB");
            cenRole = externalAccessRolesService.getActiveRoles(request.getHeader(UEBKEY));
        } catch (Exception e) {
            sendErrorResponse(response, e);
            logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
        }
        if (cenRole != null) {
            String res = mapper.writeValueAsString(cenRole);
            ecompRoles = new ArrayList<>();
            List<Role> roles = mapper.readValue(res,
                TypeFactory.defaultInstance().constructCollectionType(List.class, Role.class));
            for (Role role : roles) {
                ecompRoles.add(UserUtils.convertToEcompRole(role));
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getEcompRolesOfApplication");
        }
        return ecompRoles;
    }

    private void sendErrorResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final Map<String, String> uebkeyResponse = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String reason;
        if (e.getMessage().contains(INVALID_UEB_KEY)) {
            uebkeyResponse.put("error", INVALID_UEB_KEY);
            reason = mapper.writeValueAsString(uebkeyResponse);
            response.getWriter().write(reason);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            uebkeyResponse.put("error", e.getMessage());
            reason = mapper.writeValueAsString(uebkeyResponse);
            response.getWriter().write(reason);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void fieldsValidation(HttpServletRequest request) throws Exception {
        List<FnApp> app = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
        if (app.isEmpty()) {
            throw new Exception(INVALID_UEB_KEY);
        }
        if (app.get(0).getAuthCentral()) {
            ResponseEntity<String> response = externalAccessRolesService.getNameSpaceIfExists(app.get(0));
            if (response.getStatusCode().value() == HttpServletResponse.SC_NOT_FOUND) {
                throw new Exception("Invalid NameSpace");
            }
        }
    }
}
