/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modification Copyright © 2020 IBM.
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
package org.onap.portalapp.portal.controller;


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
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompAuditLog;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.exceptions.InvalidRoleException;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalsdk.core.service.UserService;
import org.onap.portalapp.portal.transport.CentralRole;
import org.onap.portalapp.portal.transport.CentralRoleFunction;
import org.onap.portalapp.portal.transport.CentralUser;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalapp.validation.SecureString;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.service.AuditService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auxapi")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ExternalAccessRolesController implements BasicAuthenticationController {
	private static final String ROLE_INVALID_CHARS = "%=():,\"\"";
	private static final String SUCCESSFULLY_DELETED = "Successfully Deleted";
	private static final String INVALID_UEB_KEY = "Invalid credentials!";
	private static final String LOGIN_ID = "LoginId";
	private static final String UEBKEY = "uebkey";

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesController.class);
	private static final DataValidator DATA_VALIDATOR = new DataValidator();

	@Autowired
	private AuditService auditService;

	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	@Autowired
	private UserService userservice =  new UserServiceCentalizedImpl();

	@ApiOperation(value = "Gets user role for an application.", response = CentralUser.class, responseContainer="List")
	@GetMapping(value = {
			"/user/{loginId}" }, produces = "application/json")
	public CentralUser getUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginId") String loginId) throws Exception {
		if (!DATA_VALIDATOR.isValid(new SecureString(loginId))){
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
	
	@ApiOperation(value = "Gets user roles for an application which is upgraded to newer version.", response = String.class, responseContainer="List")
	@GetMapping(value = {
			"/v1/user/{loginId}" }, produces = "application/json")
	public String getV2UserList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginId") String loginId) throws Exception {
		if (!DATA_VALIDATOR.isValid(new SecureString(loginId))){
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
	
	@ApiOperation(value = "Gets roles for an application.", response = CentralRole.class, responseContainer="Json")
	@GetMapping(value = {
			"/roles" }, produces = "application/json")
	public List<CentralRole> getRolesForApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRolesForApp");
		List<CentralV2Role> v2CenRole = null;
		List<CentralRole> cenRole = null;
		try {
			fieldsValidation(request);	
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			// Sync all roles from external system into ONAP portal DB
			logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Entering into syncApplicationRolesWithEcompDB");
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
	
	@ApiOperation(value = "Gets roles for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer="Json")
	@GetMapping(value = {
			"/v1/roles" }, produces = "application/json")
	public List<CentralV2Role> getV2RolesForApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getV2RolesForApp");
		List<CentralV2Role> answer = null;
		try {
			fieldsValidation(request);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			// Sync all roles from external system into ONAP portal DB
			logger.debug(EELFLoggerDelegate.debugLogger, "getV2RolesForApp: Entering into syncApplicationRolesWithEcompDB");
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

	@ApiOperation(value = "Gets all role functions for an application for older version.", response = CentralRoleFunction.class, responseContainer="Json")
	@GetMapping(value = {
			"/functions" }, produces = "application/json")
	public List<CentralRoleFunction> getRoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<CentralV2RoleFunction> answer = null;
		List<CentralRoleFunction> roleFuncList = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRoleFunctionsList");
		try {
			fieldsValidation(request);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			// Sync all functions from external system into ONAP portal DB
			logger.debug(EELFLoggerDelegate.debugLogger, "getRoleFunctionsList: Entering into syncRoleFunctionFromExternalAccessSystem");
			externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
			logger.debug(EELFLoggerDelegate.debugLogger, "getRoleFunctionsList: Finished syncRoleFunctionFromExternalAccessSystem");
			answer = externalAccessRolesService.getRoleFuncList(request.getHeader(UEBKEY));
			roleFuncList = externalAccessRolesService.convertCentralRoleFunctionToRoleFunctionObject(answer);		
		} catch (Exception e) {
			sendErrorResponse(response, e);
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunctionsList failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRoleFunctionsList");
		return roleFuncList;
	}	
	
	@ApiOperation(value = "Gets all role functions for an application which is upgraded to newer version.", response = CentralV2RoleFunction.class, responseContainer="Json")
	@GetMapping(value = {
			"/v1/functions" }, produces = "application/json")
	public List<CentralV2RoleFunction> getV2RoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<CentralV2RoleFunction> cenRoleFuncList = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getV2RoleFunctionsList");
		try {
			fieldsValidation(request);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			// Sync all functions from external system into ONAP portal DB
			logger.debug(EELFLoggerDelegate.debugLogger, "getV2RoleFunctionsList: Entering into syncRoleFunctionFromExternalAccessSystem");
			externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
			logger.debug(EELFLoggerDelegate.debugLogger, "getV2RoleFunctionsList: Finished syncRoleFunctionFromExternalAccessSystem");
			cenRoleFuncList = externalAccessRolesService.getRoleFuncList(request.getHeader(UEBKEY));	
		} catch (Exception e) {
			sendErrorResponse(response, e);
			logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleFunctionsList failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getV2RoleFunctionsList");
		return cenRoleFuncList;
	}	
	

	@ApiOperation(value = "Gets role information for an application.", response = CentralRole.class, responseContainer="Json")
	@GetMapping(value = {
			"/role/{role_id}" }, produces = "application/json")
	public CentralRole getRoleInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("role_id") Long roleId) throws Exception {
		CentralV2Role answer = null;
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
	
	@ApiOperation(value = "Gets v2 role information for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer="Json")
	@GetMapping(value = {
			"/v1/role/{role_id}" }, produces = "application/json")
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
	@GetMapping(value = { "/function/{code}" }, produces = "application/json")
	public CentralRoleFunction getRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) throws Exception {
		CentralV2RoleFunction centralV2RoleFunction = null;
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		if(!DATA_VALIDATOR.isValid(new SecureString(code))){
			sendErrorResponse(response, new Exception("Data is not valid"));
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", new Exception("Data is not valid"));
		}
		try {
			fieldsValidation(request);
			centralV2RoleFunction = externalAccessRolesService.getRoleFunction(code, request.getHeader(UEBKEY));
			if(centralV2RoleFunction != null && EcompPortalUtils.getFunctionCode(centralV2RoleFunction.getCode()).equals(code)) {
				BeanUtils.copyProperties(centralV2RoleFunction, centralRoleFunction, "type","action");
			}
		} catch (Exception e) {
			sendErrorResponse(response, e);
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", e);
		}
		return centralRoleFunction;
	}
	
	@ApiOperation(value = "Gets role information for an application provided by function code.", response = CentralV2RoleFunction.class, responseContainer = "Json")
	@GetMapping(value = { "/v1/function/{code}" }, produces = "application/json")
	public CentralV2RoleFunction getV2RoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) throws Exception {
		CentralV2RoleFunction centralV2RoleFunction = null;
		if(!DATA_VALIDATOR.isValid(new SecureString(code))){
			sendErrorResponse(response, new Exception("Data is not valid"));
			logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleFunction failed", new Exception("Data is not valid"));
		}
		try {
			fieldsValidation(request);
			centralV2RoleFunction = externalAccessRolesService.getRoleFunction(code, request.getHeader(UEBKEY));
			if(centralV2RoleFunction == null || !EcompPortalUtils.getFunctionCode(centralV2RoleFunction.getCode()).equals(code)) {
				centralV2RoleFunction = new CentralV2RoleFunction();
			}
		} catch (Exception e) {
			sendErrorResponse(response, e);
			logger.error(EELFLoggerDelegate.errorLogger, "getV2RoleFunction failed", e);
		}
		return centralV2RoleFunction;
	}

	@ApiOperation(value = "Saves role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/roleFunction" }, produces = "application/json")
	public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String roleFunc) {
		String status = "Successfully saved!";
		if(!DATA_VALIDATOR.isValid(new SecureString(roleFunc))){
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed");
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
				"Failed to roleFunc, not valid data.", "Failed");
		}
		try {
			fieldsValidation(request);
               ObjectMapper mapper = new ObjectMapper();
			List<EPApp> applicationList = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
			EPApp requestedApp = applicationList.get(0);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CentralV2RoleFunction availableRoleFunction = mapper.readValue(roleFunc, CentralV2RoleFunction.class);
			CentralV2RoleFunction domainRoleFunction = null;
			boolean isCentralV2Version = false;
			if(availableRoleFunction.getType()!=null && availableRoleFunction.getAction()!= null) {
				isCentralV2Version = true;
			}
			if(isCentralV2Version) {
				String code = availableRoleFunction.getType()+"|"+availableRoleFunction.getCode()+"|"+availableRoleFunction.getAction();
				domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
						requestedApp.getUebKey());
			} else {
				domainRoleFunction = externalAccessRolesService.getRoleFunction(availableRoleFunction.getCode(),
						requestedApp.getUebKey());
			}
	
			boolean saveOrUpdateResponse = false;
			if (domainRoleFunction != null && isCentralV2Version && domainRoleFunction.getCode().equals(availableRoleFunction.getCode())
					&& domainRoleFunction.getType().equals(availableRoleFunction.getType())
					&& domainRoleFunction.getAction().equals(availableRoleFunction.getAction())) {
				domainRoleFunction.setName(availableRoleFunction.getName());
				saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(domainRoleFunction,
						requestedApp);
			} else {
				availableRoleFunction.setAppId(requestedApp.getId());
				saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(availableRoleFunction,
						requestedApp);
			}		
		
			if(domainRoleFunction != null) {
				status = "Successfully updated!";
			}
			if (saveOrUpdateResponse) {
				EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
				EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
				String activityCode = (!status.equals("Successfully updated!"))
						? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION
						: EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION;
				logger.info(EELFLoggerDelegate.applicationLogger, "saveRoleFunction: succeeded for app {}, function {}",
						app.getId(), availableRoleFunction.getCode());
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(activityCode);
				auditLog.setComments(
						EcompPortalUtils.truncateString(
								"saveRoleFunction role for app:" + app.getId() + " and function:'"
										+ availableRoleFunction.getCode() + "'",
								PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				auditLog.setAffectedRecordId(user.getOrgUserId());
				auditService.logActivity(auditLog, null);
				String auditMessageInfo = EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRoleFunction", activityCode,
						String.valueOf(user.getId()), user.getOrgUserId(), availableRoleFunction.getCode());		
				EPLogUtil.logAuditMessage(logger, auditMessageInfo);	
				
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed");
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                        "Failed to saveRoleFunction for '" + availableRoleFunction.getCode() + "'", "Failed");
			}
		} catch (Exception e) {
			if (e.getMessage() == null ||e.getMessage().contains(INVALID_UEB_KEY)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, status, "Success");
	}
	
	@ApiOperation(value = "Deletes role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@DeleteMapping(value = { "/roleFunction/{code}" }, produces = "application/json")
	public PortalRestResponse<String> deleteRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) {
		if(!DATA_VALIDATOR.isValid(new SecureString(code))){
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed");
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
				"Failed to deleteRoleFunction, not valid data.", "Failed");
		}
		try {
			fieldsValidation(request);
			EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
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
				String auditMessageInfo = EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRoleFunction",
						EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION, String.valueOf(user.getId()),
						user.getOrgUserId(), code);		
				EPLogUtil.logAuditMessage(logger, auditMessageInfo);	
				
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
	@PostMapping(value = { "/role" }, produces = "application/json")
	public PortalRestResponse<String> saveRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Role role) {
		try {
			fieldsValidation(request);
			ExternalRequestFieldsValidator saveRoleResult = null;
			EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			if(role.getId()!=null && StringUtils.containsAny(role.getName(), ROLE_INVALID_CHARS)) {
				throw new InvalidRoleException("Invalid role name found for '" + role.getName() + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed");
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
				String auditMessageInfo = EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRole", activityCode,
						String.valueOf(user.getId()), user.getOrgUserId(), role.getName());		
				EPLogUtil.logAuditMessage(logger, auditMessageInfo);	
				
			} else {
				if(saveRoleResult.getDetailMessage().contains("406")){
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,						
					"Failed to create a role for '" + role.getName() + "'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed"
						, "Failed");
				} else{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
							"Failed to saveRole for '" + role.getName() + "'", "Failed");
				}
			}
		} catch (Exception e) {
			if (e.getMessage().contains(INVALID_UEB_KEY)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully Saved", "Success");
	}
	
	@ApiOperation(value = "Deletes role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@DeleteMapping(value = { "/deleteRole/{code}" }, produces = "application/json")
	public  PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String code) {
		if(!DATA_VALIDATOR.isValid(new SecureString(code))){
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed");
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
				"Failed to deleteRole, not valid data.", "Failed");
		}
		try {
			fieldsValidation(request);
			boolean deleteResponse = externalAccessRolesService.deleteRoleForApplication(code,
					request.getHeader(UEBKEY));
			if (deleteResponse) {
				EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
				EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
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
				String auditMessageInfo = EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRole",
						EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE, String.valueOf(user.getId()),
						user.getOrgUserId(), code);		
				EPLogUtil.logAuditMessage(logger, auditMessageInfo);	
				
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
	@GetMapping(value = { "/activeRoles" }, produces = "application/json")
	public  List<CentralRole> getActiveRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<CentralRole> roles = null;
		try {
			fieldsValidation(request);
			List<CentralV2Role> cenRoles= externalAccessRolesService.getActiveRoles(request.getHeader(UEBKEY));
			roles = externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(cenRoles);
		} catch (Exception e) {
			sendErrorResponse(response, e);		
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
		}
		return roles;
		
	}
	
	@ApiOperation(value = "Gets active roles for an application.", response = CentralV2Role.class, responseContainer = "Json")
	@GetMapping(value = { "/v1/activeRoles" }, produces = "application/json")
	public  List<CentralV2Role> getV2ActiveRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	@DeleteMapping(value = { "/deleteDependcyRoleRecord/{roleId}" }, produces = "application/json")
	public PortalRestResponse<String> deleteDependencyRoleRecord(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("roleId") Long roleId) {
		ExternalRequestFieldsValidator removeResult = null;
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
	@DeleteMapping(value = { "/v2/deleteRole/{roleId}" }, produces = "application/json")
	public PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("roleId") Long roleId) {
		ExternalRequestFieldsValidator removeResult = null;
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
	@PostMapping(value = { "/upload/portal/functions" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadFunctions(HttpServletRequest request, HttpServletResponse response) {
		Integer result = 0;
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
	@PostMapping(value = { "/upload/portal/roles" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadRoles(HttpServletRequest request, HttpServletResponse response) {
		Integer result = 0;
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
	@PostMapping(value = { "/upload/portal/roleFunctions" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadRoleFunctions(HttpServletRequest request, HttpServletResponse response) {
		Integer result = 0;
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
	@PostMapping(value = { "/upload/portal/userRoles" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadUserRoles(HttpServletRequest request, HttpServletResponse response) {
		Integer result = 0;
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
	@PostMapping(value = { "/upload/portal/userRole/{roleId}" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadUsersSingleRole(HttpServletRequest request, HttpServletResponse response, @PathVariable Long roleId) {
		Integer result = 0;
		try {
			String roleName = request.getHeader("RoleName");
			result = externalAccessRolesService.bulkUploadUsersSingleRole(request.getHeader(UEBKEY), roleId, roleName);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUsersSingleRole failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUsersSingleRole", "Failed");
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully added: " + result, "Success");
	}
	
	@ApiOperation(value = "Bulk upload functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/upload/partner/functions" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerFunctions(HttpServletRequest request, HttpServletResponse response) {
		Integer addedFunctions = 0;
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
	@PostMapping(value = { "/upload/partner/roles" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Role> upload) {
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
	@PostMapping(value = { "/upload/partner/roleFunctions" }, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerRoleFunctions(HttpServletRequest request, HttpServletResponse response) {
		Integer addedRoleFunctions = 0;
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
	@GetMapping(value = { "/menuFunctions" }, produces = "application/json")
	public  List<String> getMenuFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	@GetMapping(value = { "/users" }, produces = "application/json")
	public  List<EcompUser> getUsersOfApplication(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	
	/**
	 * 
	 * It checks the input uebkey of the application and throws exception if it is invalid key
	 * 
	 * @param request
	 * @throws Exception
	 */
	private void  fieldsValidation(HttpServletRequest request) throws Exception{
		List<EPApp> app = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
		if(app.isEmpty()){
			throw new Exception(INVALID_UEB_KEY);
		}
		if(!app.isEmpty() && app.get(0).getRolesInAAF()){
			ResponseEntity<String> response = externalAccessRolesService.getNameSpaceIfExists(app.get(0));
			if (response.getStatusCode().value() == HttpServletResponse.SC_NOT_FOUND)
				throw new Exception("Invalid NameSpace");
		}
	}
	
	/**
	 * 
	 * It returns http response with appropriate message
	 * 
	 * @param response
	 * @param e
	 * @throws IOException
	 */
	private void sendErrorResponse(HttpServletResponse response, Exception e) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		final Map<String,String> uebkeyResponse = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		String reason = "";
		if (e.getMessage().contains(INVALID_UEB_KEY)) {
			uebkeyResponse.put("error",INVALID_UEB_KEY);
			reason = mapper.writeValueAsString(uebkeyResponse);
			response.getWriter().write(reason);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			uebkeyResponse.put("error",e.getMessage());
			reason = mapper.writeValueAsString(uebkeyResponse);
			response.getWriter().write(reason);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Gets ecompUser of an application.", response = CentralUser.class, responseContainer = "List")
	@GetMapping(value = { "/v2/user/{loginId}" }, produces = "application/json")
	public String getEcompUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginId") String loginId) throws Exception {
		if(!DATA_VALIDATOR.isValid(new SecureString(loginId))){
			sendErrorResponse(response, new Exception("getEcompUser failed"));
			logger.error(EELFLoggerDelegate.errorLogger, "getEcompUser failed", new Exception("getEcompUser failed"));
		}
		EcompUser user = new EcompUser();
		ObjectMapper mapper = new ObjectMapper();
		String answer = null;
		try {
			fieldsValidation(request);
			
			answer = externalAccessRolesService.getV2UserWithRoles(loginId, request.getHeader(UEBKEY));
			if (answer != null) {
                User ecompUser = userservice.userMapper(answer);
				user = UserUtils.convertToEcompUser(ecompUser);
			    List<EcompRole> missingRolesOfUser = externalAccessRolesService.missingUserApplicationRoles(request.getHeader(UEBKEY), loginId, user.getRoles());
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
	@GetMapping(value = { "/v2/roles" }, produces = "application/json")
	public List<EcompRole> getEcompRolesOfApplication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<EcompRole> ecompRoles = null;
		ObjectMapper mapper = new ObjectMapper();
		List<CentralV2Role> cenRole = null;
		try {
			fieldsValidation(request);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
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
			for (Role role : roles)
				ecompRoles.add(UserUtils.convertToEcompRole(role));
			logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getEcompRolesOfApplication");
		}
		return ecompRoles;
	}
}
