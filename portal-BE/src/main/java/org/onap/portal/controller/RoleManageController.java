/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.management.InvalidApplicationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.ecomp.CentralizedApp;
import org.onap.portal.domain.dto.ecomp.EcompAuditLog;
import org.onap.portal.domain.dto.ecomp.UploadRoleFunctionExtSystem;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.exception.DuplicateRecordException;
import org.onap.portal.exception.InvalidRoleException;
import org.onap.portal.exception.NonCentralizedAppException;
import org.onap.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.AdminRolesService;
import org.onap.portal.service.CentralizedAppService;
import org.onap.portal.service.ExternalAccessRolesService;
import org.onap.portal.service.app.FnAppService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portal.validation.SecureString;
import org.onap.portalapp.controller.core.RoleController;
import org.onap.portalapp.controller.core.RoleListController;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.AuditServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.JsonMessage;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Configuration
public class RoleManageController {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final String PIPE = "|";
    private static final String ROLE_INVALID_CHARS = "%=():,\"\"";

    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RoleManageController.class);

    private RoleListController roleListController = new RoleListController();
    private RoleController roleController = new RoleController();
    private final AuditService auditService = new AuditServiceImpl();

    private final CentralizedAppService centralizedAppService;
    private final FnUserService fnUserService;
    private final FnAppService fnAppService;
    private final AdminRolesService adminRolesService;
    private final ExternalAccessRolesService externalAccessRolesService;

    @Autowired
    public RoleManageController(CentralizedAppService centralizedAppService, FnUserService fnUserService,
        FnAppService fnAppService,
        AdminRolesService adminRolesService,
        ExternalAccessRolesService externalAccessRolesService) {
        this.centralizedAppService = centralizedAppService;
        this.fnUserService = fnUserService;
        this.fnAppService = fnAppService;
        this.adminRolesService = adminRolesService;
        this.externalAccessRolesService = externalAccessRolesService;
    }

    @RequestMapping(value = {"/portalApi/get_roles/{appId}"}, method = RequestMethod.GET)
    public void getRoles(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId) {
        try {
            FnUser user = fnUserService.loadUserByUsername(principal.getName());
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral()) {
                    Map<String, Object> model = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    List<CentralV2Role> answer = externalAccessRolesService.getRolesForApp(requestedApp.getUebKey());
                    model.put("availableRoles", answer);
                    JsonMessage msg = new JsonMessage(mapper.writeValueAsString(model));
                    JSONObject j = new JSONObject(msg);
                    response.getWriter().write(j.toString());
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName());
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.getRoles, Unauthorized user");
                SendErrorForUnauthorizedUser(response, user);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRoles failed", e);
        }
    }

    @RequestMapping(value = {"/portalApi/role_list/toggleRole/{appId}/{roleId}"}, method = RequestMethod.POST)
    public Map<String, Object> toggleRole(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId, @PathVariable("roleId") Long roleId) throws Exception {
        FnApp requestedApp;
        String restcallStatus;
        HashMap<String, Object> responseMap = new HashMap<>();
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        try {
            requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CentralV2Role domainRole = externalAccessRolesService.getRoleInfo(roleId, requestedApp.getUebKey());
                boolean active = domainRole.isActive();
                domainRole.setActive(!active);
                String result = mapper.writeValueAsString(domainRole);
                Role newRole = externalAccessRolesService.convertCentralRoleToRole(result);
                ExternalRequestFieldsValidator externalRequestFieldsValidator = externalAccessRolesService
                    .saveRoleForApplication(newRole, requestedApp.getUebKey());
                boolean getAddResponse = externalRequestFieldsValidator.isResult();
                if (getAddResponse) {
                    restcallStatus = "Success";
                    logger.info(EELFLoggerDelegate.auditLogger, "Toggle active status for role " + domainRole.getId());
                } else {
                    restcallStatus = "Toggle Role Failed";
                    logger.info(EELFLoggerDelegate.auditLogger, "Toggle Role Failed " + domainRole.getId());
                }
                responseMap.put("restcallStatus", restcallStatus);
                responseMap.put("availableRoles", externalAccessRolesService.getRolesForApp(requestedApp.getUebKey()));
            } else {
                logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.toggleRole, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                responseMap.put("restcallStatus", " Unauthorized user");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "toggleRole failed", e);
            throw e;
        }
        return responseMap;
    }

    @RequestMapping(value = {"/portalApi/role_list/removeRole/{appId}/{roleId}"}, method = RequestMethod.POST)
    public Map<String, Object> removeRole(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId, @PathVariable("roleId") Long roleId) throws Exception {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        FnApp requestedApp;
        String restCallStatus;
        HashMap<String, Object> responseMap = new HashMap<>();
        ExternalRequestFieldsValidator externalRequestFieldsValidator;
        try {
            requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral()) {
                    externalRequestFieldsValidator = externalAccessRolesService.deleteDependencyRoleRecord(roleId,
                        requestedApp.getUebKey(), user.getOrgUserId());
                    boolean deleteResponse = externalRequestFieldsValidator.isResult();
                    if (deleteResponse) {
                        restCallStatus = "Success";
                        FnUser requestedUser = (FnUser) externalAccessRolesService.getUser(user.getOrgUserId()).get(0);
                        FnApp app = (FnApp) externalAccessRolesService.getApp(requestedApp.getUebKey()).get(0);
                        logger.info(EELFLoggerDelegate.applicationLogger, "deleteRole: succeeded for app {}, role {}",
                            app.getId(), roleId);
                        String activityCode = EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE;
                        AuditLog auditLog = getAuditInfo(requestedUser, activityCode);
                        auditLog.setComments(EcompPortalUtils.truncateString(
                            "Deleted role for app:" + app.getId() + " and role:'" + roleId + "'",
                            PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                        auditService.logActivity(auditLog, null);
                        MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        EcompPortalUtils.calculateDateTimeDifferenceForLog(
                            MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                            MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                        logger.info(EELFLoggerDelegate.auditLogger,
                            EPLogUtil.formatAuditLogMessage("RoleManageController.removeRole",
                                EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE,
                                String.valueOf(requestedUser.getId()), requestedUser.getOrgUserId(),
                                roleId.toString()));
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                        MDC.remove(SystemProperties.MDC_TIMER);
                    } else {
                        restCallStatus = "Remove Role failed";
                        responseMap.put("error", externalRequestFieldsValidator.getDetailMessage());
                        logger.error(EELFLoggerDelegate.errorLogger, "removeRole failed");
                    }
                    responseMap.put("restCallStatus", restCallStatus);
                    responseMap.put("availableRoles",
                        externalAccessRolesService.getRolesForApp(requestedApp.getUebKey()));
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName());
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.removeRole, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                responseMap.put("restCallStatus", " Unauthorized user");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "removeRole failed", e);
            throw e;
        }
        return responseMap;
    }

    @RequestMapping(value = {"/portalApi/role/saveRole/{appId}"}, method = RequestMethod.POST)
    public Map<String, Object> saveRole(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        String responseString = null;
        HashMap<String, Object> responseMap = new HashMap<>();
        try {
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral().equals(true)) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JsonNode root = mapper.readTree(request.getReader());
                    CentralV2Role role = mapper.readValue(root.get("role").toString(), CentralV2Role.class);
                    List<CentralV2Role> childRoles = mapper.readValue(root.get("childRoles").toString(),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, CentralV2Role.class));
                    List<EpAppFunction> roleFunctions = mapper.readValue(root.get("roleFunctions").toString(),
                        TypeFactory.defaultInstance().constructCollectionType(List.class,
                            EpAppFunction.class));
                    if (role.getId() != null && StringUtils.containsAny(role.getName(), ROLE_INVALID_CHARS)) {
                        throw new InvalidRoleException("Invalid role name found for '" + role.getName()
                            + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed");
                    }
                    CentralV2Role domainRole;
                    if (role.getId() != null) {
                        domainRole = externalAccessRolesService.getRoleInfo(role.getId(), requestedApp.getUebKey());
                        domainRole.setName(role.getName());
                        domainRole.setPriority(role.getPriority());
                    } else {
                        List<CentralV2Role> roles = externalAccessRolesService.getRolesForApp(requestedApp.getUebKey());
                        for (CentralV2Role existRole : roles) {
                            if (existRole.getName().equalsIgnoreCase(role.getName())) {
                                throw new DuplicateRecordException("Role already exists: " + existRole.getName());
                            }
                        }
                        domainRole = CentralV2Role.builder().build();
                        domainRole.setName(role.getName());
                        domainRole.setPriority(role.getPriority());
                        domainRole.setActive(role.isActive());
                        if (role.getChildRoles() != null && role.getChildRoles().size() > 0) {
                            for (Object childRole : childRoles) {
                                domainRole.addChildRole((CentralV2Role) childRole);
                            }
                        }
                    }
                    if (role.getRoleFunctions() != null && role.getRoleFunctions().size() > 0) {
                        domainRole.setRoleFunctions(new TreeSet<>());
                        for (EpAppFunction roleFunction : roleFunctions) {
                            if (roleFunction.getType() == null && roleFunction.getAction() == null) {
                                throw new InvalidRoleException("Invalid role function type:" + roleFunction.getType()
                                    + " and action: " + roleFunction.getAction() + " found while saving!");
                            }
                            if (EcompPortalUtils.checkFunctionCodeHasEncodePattern(roleFunction.getFunctionCd())) {
                                roleFunction.setFunctionCd(roleFunction.getType() + PIPE
                                    + EcompPortalUtils.encodeFunctionCode(roleFunction.getFunctionCd()) + PIPE
                                    + roleFunction.getAction());
                            } else {
                                roleFunction
                                    .setFunctionCd(roleFunction.getType() + PIPE + roleFunction.getFunctionCd() + PIPE
                                        + roleFunction.getAction());
                            }
                            domainRole.addRoleFunction(roleFunction);
                        }
                    } else {
                        domainRole.setRoleFunctions(new TreeSet<>());
                    }
                    String result = mapper.writeValueAsString(domainRole);
                    Role newRole = externalAccessRolesService.convertCentralRoleToRole(result);
                    ExternalRequestFieldsValidator externalRequestFieldsValidator = externalAccessRolesService
                        .saveRoleForApplication(newRole, requestedApp.getUebKey());
                    boolean getAddResponse = externalRequestFieldsValidator.isResult();
                    if (getAddResponse) {
                        String activityCode = (role.getId() == null) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_ROLE
                            : EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_ROLE_AND_FUNCTION;
                        logger.info(EELFLoggerDelegate.applicationLogger, "saveRole: succeeded for app {}, role {}",
                            requestedApp.getId(), role.getName());
                        AuditLog auditLog = new AuditLog();
                        auditLog.setUserId(user.getId());
                        auditLog.setActivityCode(activityCode);
                        auditLog.setComments(EcompPortalUtils.truncateString(
                            "saveRole role for app:" + requestedApp.getId() + " and role:'" + role.getName() + "'",
                            PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                        auditLog.setAffectedRecordId(user.getOrgUserId());
                        auditService.logActivity(auditLog, null);
                        MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        EcompPortalUtils.calculateDateTimeDifferenceForLog(
                            MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                            MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                        logger.info(EELFLoggerDelegate.auditLogger,
                            EPLogUtil.formatAuditLogMessage("RoleManageController.saveRole", activityCode,
                                String.valueOf(user.getId()), user.getOrgUserId(), role.getName()));
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                        MDC.remove(SystemProperties.MDC_TIMER);
                        responseMap.put("status", "Success");
                        responseMap.put("role", domainRole);
                    } else {
                        if (externalRequestFieldsValidator.getDetailMessage().contains("406")) {
                            externalRequestFieldsValidator.setDetailMessage("Failed to save role for '" + role.getName()
                                + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed");
                        }
                        responseMap.put("status", "SaveRole Failed");
                        responseMap.put("role", responseString);
                        responseMap.put("error", externalRequestFieldsValidator.getDetailMessage());
                        logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed");
                    }
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.saveRole, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                responseMap.put("error", " Unauthorized user");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed", e);
            responseMap.put("error", e.getMessage());
        }
        return responseMap;
    }

    @RequestMapping(value = {"/portalApi/role/removeRoleFunction"}, method = RequestMethod.POST)
    public ModelAndView removeRoleRoleFunction(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return getRoleController().removeRoleFunction(request, response);
    }

    @RequestMapping(value = {"/portalApi/role/addRoleFunction"}, method = RequestMethod.POST)
    public ModelAndView addRoleRoRoleFunction(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return getRoleController().addRoleFunction(request, response);
    }

    @RequestMapping(value = {"/portalApi/role/removeChildRole"}, method = RequestMethod.POST)
    public ModelAndView removeChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getRoleController().removeChildRole(request, response);
    }

    @RequestMapping(value = {"/portalApi/role/addChildRole"}, method = RequestMethod.POST)
    public ModelAndView addChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getRoleController().addChildRole(request, response);
    }

    @RequestMapping(value = {"/portalApi/get_role/{appId}/{roleId}"}, method = RequestMethod.GET)
    public void getRole(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId,
        @PathVariable("roleId") Long roleId) throws Exception {
        try {
            FnUser user = fnUserService.loadUserByUsername(principal.getName());
            ObjectMapper mapper = new ObjectMapper();
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral()) {
                    CentralV2Role answer = externalAccessRolesService.getRoleInfo(roleId, requestedApp.getUebKey());
                    logger.info(EELFLoggerDelegate.applicationLogger, "role_id" + roleId);
                    Map<String, Object> model = new HashMap<>();
                    model.put("availableRoleFunctions", mapper
                        .writeValueAsString(externalAccessRolesService.getRoleFuncList(requestedApp.getUebKey())));
                    model.put("availableRoles",
                        mapper.writeValueAsString(getAvailableChildRoles(requestedApp.getUebKey(), roleId)));
                    model.put("role", mapper.writeValueAsString(answer));
                    JsonMessage msg = new JsonMessage(mapper.writeValueAsString(model));
                    JSONObject j = new JSONObject(msg);
                    response.getWriter().write(j.toString());
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName());
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.getRoleFunctionList, Unauthorized user");
                SendErrorForUnauthorizedUser(response, user);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRole failed", e);
            throw e;
        }
    }

    @RequestMapping(value = {"/portalApi/get_role_functions/{appId}"}, method = RequestMethod.GET)
    public void getRoleFunctionList(Principal principal, HttpServletRequest request, HttpServletResponse response,
        @PathVariable("appId") Long appId) throws Exception {
        try {
            FnUser user = fnUserService.loadUserByUsername(principal.getName());
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral()) {
                    List<EpAppFunction> answer = null;
                    Map<String, Object> model = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    answer = externalAccessRolesService.getRoleFuncList(requestedApp.getUebKey());
                    model.put("availableRoleFunctions", answer);
                    JsonMessage msg = new JsonMessage(mapper.writeValueAsString(model));
                    JSONObject j = new JSONObject(msg);
                    response.getWriter().write(j.toString());
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName());
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.getRoleFunctionList, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                response.getWriter().write("Unauthorized User");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunctionList failed", e);
            throw e;
        }
    }

    @RequestMapping(value = {"/portalApi/role_function_list/saveRoleFunction/{appId}"}, method = RequestMethod.POST)
    public PortalRestResponse<String> saveRoleFunction(Principal principal, HttpServletRequest request,
        HttpServletResponse response, @Valid @RequestBody EpAppFunction roleFunc, @PathVariable("appId") Long appId) {
        if (roleFunc != null) {
            Validator validator = VALIDATOR_FACTORY.getValidator();
            Set<ConstraintViolation<EpAppFunction>> constraintViolations = validator.validate(roleFunc);

            if (!constraintViolations.isEmpty()) {
                logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction: Failed");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Data is not valid", "ERROR");
            }
        }

        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        boolean saveOrUpdateResponse;
        try {
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral() && roleFunc != null) {
                    String code = roleFunc.getType() + PIPE + roleFunc.getFunctionCd() + PIPE + roleFunc.getAction();
                    EpAppFunction domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
                        requestedApp.getUebKey());
                    if (domainRoleFunction != null
                        && (domainRoleFunction.getType() == null || domainRoleFunction.getAction() == null)) {
                        addIfTypeActionDoesNotExits(domainRoleFunction);
                    }
                    boolean isSave = true;
                    if (domainRoleFunction != null && domainRoleFunction.getFunctionCd()
                        .equals(roleFunc.getFunctionCd())
                        && domainRoleFunction.getType().equals(roleFunc.getType())
                        && domainRoleFunction.getAction().equals(roleFunc.getAction())) {
                        domainRoleFunction.setFunctionName(roleFunc.getFunctionName());
                        saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(domainRoleFunction,
                            requestedApp);
                        isSave = false;
                    } else {
                        roleFunc.setAppId(requestedApp);
                        saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(roleFunc,
                            requestedApp);
                    }
                    if (saveOrUpdateResponse) {
                        FnUser requestedUser = externalAccessRolesService.getUser(user.getOrgUserId()).get(0);
                        FnApp app = externalAccessRolesService.getApp(requestedApp.getUebKey()).get(0);
                        String activityCode = (isSave) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION
                            : EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION;
                        logExterlaAuthRoleFunctionActivity(code, requestedUser, app, activityCode);
                    }
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName() + " is not Centralized Application");
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.saveRoleFunction, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Unauthorized User", "Failure");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction: Failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failure");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Saved Successfully!", "Success");
    }

    private void logExterlaAuthRoleFunctionActivity(String code, FnUser requestedUser, FnApp app, String activityCode) {
        logger.info(EELFLoggerDelegate.applicationLogger, "saveRoleFunction: succeeded for app {}, function {}",
            app.getId(), code);
        AuditLog auditLog = getAuditInfo(requestedUser, activityCode);
        auditLog.setComments(EcompPortalUtils.truncateString(
            "saveRoleFunction role for app:" + app.getId() + " and function:'" + code + "'",
            PortalConstants.AUDIT_LOG_COMMENT_SIZE));
        auditService.logActivity(auditLog, null);
        MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
        MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
        EcompPortalUtils.calculateDateTimeDifferenceForLog(MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
            MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
        logger.info(EELFLoggerDelegate.auditLogger,
            EPLogUtil.formatAuditLogMessage("RoleManageController.saveRoleFunction", activityCode,
                String.valueOf(requestedUser.getId()), requestedUser.getOrgUserId(), code));
        MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
        MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
        MDC.remove(SystemProperties.MDC_TIMER);
    }

    private void addIfTypeActionDoesNotExits(EpAppFunction domainRoleFunction) {
        if (domainRoleFunction.getFunctionCd().contains(PIPE)) {
            String newfunctionCodeFormat = EcompPortalUtils.getFunctionCode(domainRoleFunction.getFunctionCd());
            String newfunctionTypeFormat = EcompPortalUtils.getFunctionType(domainRoleFunction.getFunctionCd());
            String newfunctionActionFormat = EcompPortalUtils.getFunctionAction(domainRoleFunction.getFunctionCd());
            domainRoleFunction.setType(newfunctionTypeFormat);
            domainRoleFunction.setAction(newfunctionActionFormat);
            domainRoleFunction.setFunctionCd(newfunctionCodeFormat);
        } else {
            String type = externalAccessRolesService.getFunctionCodeType(domainRoleFunction.getFunctionCd());
            String action = externalAccessRolesService.getFunctionCodeAction(domainRoleFunction.getFunctionCd());
            domainRoleFunction.setType(type);
            domainRoleFunction.setAction(action);
        }
    }

    @RequestMapping(value = {"/portalApi/role_function_list/removeRoleFunction/{appId}"}, method = RequestMethod.POST)
    public PortalRestResponse<String> removeRoleFunction(Principal principal,
        HttpServletRequest request, HttpServletResponse response,
        @RequestBody String roleFunc, @PathVariable("appId") Long appId) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        if (roleFunc != null) {
            SecureString secureString = new SecureString(roleFunc);

            Validator validator = VALIDATOR_FACTORY.getValidator();
            Set<ConstraintViolation<SecureString>> constraintViolations = validator.validate(secureString);

            if (!constraintViolations.isEmpty()) {
                logger.error(EELFLoggerDelegate.errorLogger, "removeRoleFunction: Failed");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Data is not valid", "ERROR");
            }
        }

        try {
            FnApp requestedApp = fnAppService.getById(appId);
            if (isAuthorizedUser(user, requestedApp)) {
                fieldsValidation(requestedApp);
                if (requestedApp.getAuthCentral()) {
                    ObjectMapper mapper = new ObjectMapper();
                    boolean getDelFuncResponse;
                    EpAppFunction availableRoleFunction = mapper.readValue(roleFunc, EpAppFunction.class);
                    String code = availableRoleFunction.getType() + PIPE + availableRoleFunction.getFunctionCd() + PIPE
                        + availableRoleFunction.getAction();
                    EpAppFunction domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
                        requestedApp.getUebKey());
                    getDelFuncResponse = externalAccessRolesService
                        .deleteCentralRoleFunction(domainRoleFunction.getFunctionCd(), requestedApp);
                    if (getDelFuncResponse) {
                        logger.info(EELFLoggerDelegate.applicationLogger,
                            "deleteRoleFunction: succeeded for app {}, role {}", requestedApp.getId(),
                            domainRoleFunction.getFunctionCd());
                        String activityCode = EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION;
                        AuditLog auditLog = getAuditInfo(user, activityCode);
                        auditLog.setComments(
                            EcompPortalUtils.truncateString(
                                "Deleted function for app:" + requestedApp.getId() + " and function code:'"
                                    + domainRoleFunction.getFunctionCd() + "'",
                                PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                        auditService.logActivity(auditLog, null);
                        MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
                            EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                        EcompPortalUtils.calculateDateTimeDifferenceForLog(
                            MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                            MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                        logger.info(EELFLoggerDelegate.auditLogger,
                            EPLogUtil.formatAuditLogMessage("RoleManageController.removeRoleFunction",
                                EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION,
                                String.valueOf(user.getId()), user.getOrgUserId(),
                                domainRoleFunction.getFunctionCd()));
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                        MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                        MDC.remove(SystemProperties.MDC_TIMER);
                        logger.info(EELFLoggerDelegate.auditLogger,
                            "Remove role function " + domainRoleFunction.getFunctionName());
                    }
                } else {
                    throw new NonCentralizedAppException(requestedApp.getAppName() + " is not Centralized Application");
                }
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.removeRoleFunction, Unauthorized user");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Unauthorized User", "Failure");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "removeRoleFunction failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failure");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Deleted Successfully!", "Success");
    }

    @RequestMapping(value = {"/portalApi/centralizedApps"}, method = RequestMethod.GET)
    public List<CentralizedApp> getCentralizedAppRoles(Principal principal, HttpServletRequest request,
        HttpServletResponse response,
        String userId) {
        if (userId != null) {
            SecureString secureString = new SecureString(userId);

            Validator validator = VALIDATOR_FACTORY.getValidator();
            Set<ConstraintViolation<SecureString>> constraintViolations = validator.validate(secureString);

            if (!constraintViolations.isEmpty()) {
                logger.error(EELFLoggerDelegate.errorLogger, "removeRoleFunction: Failed");
                return null;
            }
        }

        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        List<CentralizedApp> applicationsList = null;
        if (adminRolesService.isAccountAdmin(user.getId(), user.getOrgUserId(), user.getUserApps()) || adminRolesService
            .isSuperAdmin(user.getLoginId())
            || adminRolesService.isRoleAdmin(user.getId())) {
            applicationsList = centralizedAppService.getCentralizedAppsOfUser(userId);
        } else {
            logger.info(EELFLoggerDelegate.auditLogger,
                "RoleManageController.getCentralizedAppRoles, Unauthorized user");
            EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
        }
        return applicationsList;
    }

    public List<CentralizedApp> getCentralizedAppsOfUser(String userId) {
        List<CentralizedApp> centralizedAppsList = new ArrayList<>();
        try {
            centralizedAppsList = centralizedAppService.getCentralizedAppsOfUser(userId);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getCentralizedAppsOfUser failed", e);
        }
        return centralizedAppsList;
    }

    public RoleListController getRoleListController() {
        return roleListController;
    }

    public void setRoleListController(RoleListController roleListController) {
        this.roleListController = roleListController;
    }

    public RoleController getRoleController() {
        return roleController;
    }

    public void setRoleController(RoleController roleController) {
        this.roleController = roleController;
    }

    @RequestMapping(value = {"/portalApi/syncRoles"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> syncRoles(Principal principal, HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody Long appId) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        try {
            FnApp app = fnAppService.getById(appId);
            if (isAuthorizedUser(user, app)) {
                fieldsValidation(app);
                externalAccessRolesService.syncApplicationRolesWithEcompDB(app);
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.syncRoles, Unauthorized user:{}", user != null ? user.getOrgUserId() : "");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Unauthorized User", "Failure");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "failed syncRoles", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Sync roles completed successfully!", "Success");
    }

    @RequestMapping(value = {"/portalApi/syncFunctions"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> syncFunctions(Principal principal, HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody Long appId) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        try {
            FnApp app = fnAppService.getById(appId);
            if (isAuthorizedUser(user, app)) {
                fieldsValidation(app);
                externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.syncFunctions, Unauthorized user:{}",
                    user != null ? user.getOrgUserId() : "");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Unauthorized User", "Failure");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "failed syncFunctions", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Sync Functions completed successfully!", "Success");
    }

    public List<CentralV2Role> getAvailableChildRoles(String uebKey, Long roleId) throws Exception {
        List<CentralV2Role> availableChildRoles = externalAccessRolesService.getRolesForApp(uebKey);
        if (roleId == null || roleId == 0) {
            return availableChildRoles;
        }
        CentralV2Role currentRole = externalAccessRolesService.getRoleInfo(roleId, uebKey);
        Set<CentralV2Role> allParentRoles = new TreeSet<>();
        getAllParentRolesAsList(currentRole, allParentRoles);
        availableChildRoles
            .removeIf(role -> !role.isActive() || allParentRoles.contains(role) || role.getId().equals(roleId));
        return availableChildRoles;
    }

    private void getAllParentRolesAsList(CentralV2Role role, Set<CentralV2Role> allParentRoles) {
        Set<CentralV2Role> parentRoles = role.getParentRoles();
        allParentRoles.addAll(parentRoles);
        for (CentralV2Role parentRole : parentRoles) {
            getAllParentRolesAsList(parentRole, allParentRoles);
        }
    }

    public AuditLog getAuditInfo(FnUser user, String activityCode) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(user.getId());
        auditLog.setActivityCode(activityCode);
        auditLog.setAffectedRecordId(user.getOrgUserId());

        return auditLog;
    }

    private void fieldsValidation(FnApp app) throws Exception {
        List<FnApp> appInfo = externalAccessRolesService.getApp(app.getUebKey());
        if (appInfo.isEmpty()) {
            throw new InvalidApplicationException("Invalid credentials");
        }
        if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()
            && appInfo.get(0).getAuthCentral()) {
            ResponseEntity<String> response = externalAccessRolesService.getNameSpaceIfExists(appInfo.get(0));
            if (response.getStatusCode().value() == HttpServletResponse.SC_NOT_FOUND) {
                throw new InvalidApplicationException("Invalid NameSpace");
            }
        }
    }

    private boolean isAuthorizedUser(FnUser user, FnApp requestedApp) {
        return user != null && (adminRolesService.isAccountAdminOfApplication(user.getId(), requestedApp)
            || (adminRolesService.isSuperAdmin(user.getLoginId()) && requestedApp.getId()
            .equals(PortalConstants.PORTAL_APP_ID)));
    }

    private void SendErrorForUnauthorizedUser(HttpServletResponse response, FnUser user) throws IOException {
        EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
        response.getWriter().write("Unauthorized User");
    }

    @RequestMapping(value = {
        "/portalApi/uploadRoleFunction/{appId}"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> bulkUploadRoleFunc(Principal principal, HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody UploadRoleFunctionExtSystem data, @PathVariable("appId") Long appId) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        try {
            FnApp app = fnAppService.getById(appId);
            if (isAuthorizedUser(user, app)) {
                fieldsValidation(app);
                externalAccessRolesService.bulkUploadRoleFunc(data, app);
                String activityCode = EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_ROLE_AND_FUNCTION;
                String code = data.getName() + "," + data.getType() + PIPE + data.getInstance() + PIPE
                    + data.getAction();
                logExterlaAuthRoleFunctionActivity(code, user, app, activityCode);
            } else {
                logger.info(EELFLoggerDelegate.auditLogger,
                    "RoleManageController.syncRoles, Unauthorized user:{}", user != null ? user.getOrgUserId() : "");
                EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Unauthorized User", "Failure");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "Failed bulkUploadRoleFunc!", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Uploaded Role Function successfully!", "Success");
    }
}
