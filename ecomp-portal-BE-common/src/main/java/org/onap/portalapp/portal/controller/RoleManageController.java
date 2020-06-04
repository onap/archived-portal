/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung 
 * ===================================================================
 * Modifications Copyright (c) 2020 IBM
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.controller.core.RoleController;
import org.onap.portalapp.controller.core.RoleListController;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompAuditLog;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.ecomp.model.UploadRoleFunctionExtSystem;
import org.onap.portalapp.portal.exceptions.DuplicateRecordException;
import org.onap.portalapp.portal.exceptions.InvalidApplicationException;
import org.onap.portalapp.portal.exceptions.InvalidRoleException;
import org.onap.portalapp.portal.exceptions.NonCentralizedAppException;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.validation.SecureString;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.JsonMessage;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Proxies REST calls to role-management functions that arrive on paths
 * /portalApi/* over to controller methods provided by the SDK-Core library.
 * Those controller methods are mounted on paths not exposed by the Portal FE.
 */
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class RoleManageController extends EPRestrictedBaseController {
	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

	private static final String PIPE = "|";

	private static final String ROLE_INVALID_CHARS = "%=():,\"\"";

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RoleManageController.class);

	@Autowired
	private RoleController roleController;

	@Autowired
	private RoleListController roleListController;

	@Autowired
	private EPAppService appService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	@Autowired
	private AdminRolesService adminRolesService;

	/**
	 * Calls an SDK-Core library method that gets the available roles and writes
	 * them to the request object. Portal specifies a Hibernate mappings from the
	 * Role class to the fn_role_v view, which ensures that only Portal (app_id is
	 * null) roles are fetched.
	 * 
	 * Any method declared void (no return value) or returning null causes the audit
	 * log aspect method to declare failure. TODO: should return a JSON string.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@GetMapping(value = { "/portalApi/get_roles/{appId}" })
	public void getRoles(HttpServletRequest request, HttpServletResponse response, @PathVariable("appId") Long appId)
			throws Exception {
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth()) {
					List<CentralV2Role> answer = null;
					Map<String, Object> model = new HashMap<>();
					ObjectMapper mapper = new ObjectMapper();
					answer = externalAccessRolesService.getRolesForApp(requestedApp.getUebKey());
					model.put("availableRoles", answer);
					JsonMessage msg = new JsonMessage(mapper.writeValueAsString(model));
					JSONObject j = new JSONObject(msg);
					response.getWriter().write(j.toString());
				} else
					throw new NonCentralizedAppException(requestedApp.getName());
			} else {
				logger.info(EELFLoggerDelegate.auditLogger, "RoleManageController.getRoles, Unauthorized user");
				SendErrorForUnauthorizedUser(response, user);
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoles failed", e);
		}
	}

	@PostMapping(value = { "/portalApi/role_list/toggleRole/{appId}/{roleId}" })
	public Map<String, Object> toggleRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId, @PathVariable("roleId") Long roleId) throws Exception {
		EPApp requestedApp = null;
		String restcallStatus = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				CentralV2Role domainRole = externalAccessRolesService.getRoleInfo(roleId, requestedApp.getUebKey());
				// role. toggle active ind
				boolean active = domainRole.getActive();
				domainRole.setActive(!active);

				String result = mapper.writeValueAsString(domainRole);
				Role newRole = externalAccessRolesService.ConvertCentralRoleToRole(result);
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

	@PostMapping(value = { "/portalApi/role_list/removeRole/{appId}/{roleId}" })
	public Map<String, Object> removeRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId, @PathVariable("roleId") Long roleId) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		EPApp requestedApp = null;
		String restCallStatus = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		ExternalRequestFieldsValidator externalRequestFieldsValidator = null;
		try {
			requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth()) {
					externalRequestFieldsValidator = externalAccessRolesService.deleteDependencyRoleRecord(roleId,
							requestedApp.getUebKey(), user.getOrgUserId());
					boolean deleteResponse = externalRequestFieldsValidator.isResult();
					if (deleteResponse) {
						restCallStatus = "Success";
						EPUser requestedUser = (EPUser) externalAccessRolesService.getUser(user.getOrgUserId()).get(0);
						EPApp app = (EPApp) externalAccessRolesService.getApp(requestedApp.getUebKey()).get(0);
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
				} else
					throw new NonCentralizedAppException(requestedApp.getName());
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

	@PostMapping(value = { "/portalApi/role/saveRole/{appId}" })
	public Map<String, Object> saveRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		String responseString = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		try {
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp != null && requestedApp.getCentralAuth().equals(true)) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					JsonNode root = mapper.readTree(request.getReader());
					CentralV2Role role = mapper.readValue(root.get("role").toString(), CentralV2Role.class);

					List<CentralV2Role> childRoles = mapper.readValue(root.get("childRoles").toString(),
							TypeFactory.defaultInstance().constructCollectionType(List.class, CentralV2Role.class));
					List<CentralV2RoleFunction> roleFunctions = mapper.readValue(root.get("roleFunctions").toString(),
							TypeFactory.defaultInstance().constructCollectionType(List.class,
									CentralV2RoleFunction.class));
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
						// check for existing role of same name
						List<CentralV2Role> roles = externalAccessRolesService.getRolesForApp(requestedApp.getUebKey());
						for (CentralV2Role existRole : roles)
							if (existRole.getName().equalsIgnoreCase(role.getName()))
								throw new DuplicateRecordException("Role already exists: " + existRole.getName());

                        domainRole = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
						domainRole.setName(role.getName());
						domainRole.setPriority(role.getPriority());
						domainRole.setActive(role.getActive());
						if (role.getChildRoles() != null && role.getChildRoles().size() > 0) {
							for (Object childRole : childRoles) {
								domainRole.addChildRole((CentralV2Role) childRole);
							}
						}
					}
					if (role.getRoleFunctions() != null && role.getRoleFunctions().size() > 0) {
						domainRole.setRoleFunctions(new TreeSet<CentralV2RoleFunction>());
						for (CentralV2RoleFunction roleFunction : roleFunctions) {
							if (roleFunction.getType() == null && roleFunction.getAction() == null) {
								throw new InvalidRoleException("Invalid role function type:" + roleFunction.getType()
										+ " and action: " + roleFunction.getAction() + " found while saving!");
							}
							if (EcompPortalUtils.checkFunctionCodeHasEncodePattern(roleFunction.getCode()))
								roleFunction.setCode(roleFunction.getType() + PIPE
										+ EcompPortalUtils.encodeFunctionCode(roleFunction.getCode()) + PIPE
										+ roleFunction.getAction());
							else
								roleFunction.setCode(roleFunction.getType() + PIPE + roleFunction.getCode() + PIPE
										+ roleFunction.getAction());
							domainRole.addRoleFunction((CentralV2RoleFunction) roleFunction);
						}
					} else {
						domainRole.setRoleFunctions(new TreeSet<>());
					}
					String result = mapper.writeValueAsString(domainRole);
					Role newRole = externalAccessRolesService.ConvertCentralRoleToRole(result);
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

	@PostMapping(value = { "/portalApi/role/removeRoleFunction" })
	public ModelAndView removeRoleRoleFunction(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getRoleController().removeRoleFunction(request, response);
	}

	@PostMapping(value = { "/portalApi/role/addRoleFunction" })
	public ModelAndView addRoleRoRoleFunction(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getRoleController().addRoleFunction(request, response);
	}

	@PostMapping(value = { "/portalApi/role/removeChildRole" })
	public ModelAndView removeChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleController().removeChildRole(request, response);
	}

	@PostMapping(value = { "/portalApi/role/addChildRole" })
	public ModelAndView addChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleController().addChildRole(request, response);
	}

	@GetMapping(value = { "/portalApi/get_role/{appId}/{roleId}" })
	public void getRole(HttpServletRequest request, HttpServletResponse response, @PathVariable("appId") Long appId,
			@PathVariable("roleId") Long roleId) throws Exception {
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			ObjectMapper mapper = new ObjectMapper();
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth()) {
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
				} else
					throw new NonCentralizedAppException(requestedApp.getName());
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

	@GetMapping(value = { "/portalApi/get_role_functions/{appId}" })
	public void getRoleFunctionList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId) throws Exception {
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth()) {
					List<CentralV2RoleFunction> answer = null;
					Map<String, Object> model = new HashMap<>();
					ObjectMapper mapper = new ObjectMapper();
					answer = externalAccessRolesService.getRoleFuncList(requestedApp.getUebKey());
					model.put("availableRoleFunctions", answer);
					JsonMessage msg = new JsonMessage(mapper.writeValueAsString(model));
					JSONObject j = new JSONObject(msg);
					response.getWriter().write(j.toString());
				} else
					throw new NonCentralizedAppException(requestedApp.getName());
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

	@PostMapping(value = { "/portalApi/role_function_list/saveRoleFunction/{appId}" })
	public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody CentralV2RoleFunction roleFunc,
			@PathVariable("appId") Long appId) throws Exception {
		if (roleFunc!=null) {
			Validator validator = VALIDATOR_FACTORY.getValidator();
			Set<ConstraintViolation<CentralV2RoleFunction>> constraintViolations = validator.validate(roleFunc);

			if(!constraintViolations.isEmpty()){
				logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction: Failed");
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Data is not valid", "ERROR");
			}
		}

		EPUser user = EPUserUtils.getUserSession(request);
		boolean saveOrUpdateResponse = false;
		try {
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth() && roleFunc!=null) {
					String code = roleFunc.getType() + PIPE + roleFunc.getCode() + PIPE + roleFunc.getAction();
					CentralV2RoleFunction domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
							requestedApp.getUebKey());
					if (domainRoleFunction != null
							&& (domainRoleFunction.getType() == null || domainRoleFunction.getAction() == null)) {
						addIfTypeActionDoesNotExits(domainRoleFunction);
					}
					boolean isSave = true;
					if (domainRoleFunction != null && domainRoleFunction.getCode().equals(roleFunc.getCode())
							&& domainRoleFunction.getType().equals(roleFunc.getType())
							&& domainRoleFunction.getAction().equals(roleFunc.getAction())) {
						domainRoleFunction.setName(roleFunc.getName());
						saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(domainRoleFunction,
								requestedApp);
						isSave = false;
					} else {
						roleFunc.setAppId(requestedApp.getId());
						saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(roleFunc,
								requestedApp);
					}
					if (saveOrUpdateResponse) {
						EPUser requestedUser = externalAccessRolesService.getUser(user.getOrgUserId()).get(0);
						EPApp app = externalAccessRolesService.getApp(requestedApp.getUebKey()).get(0);
						String activityCode = (isSave) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION
								: EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION;
						logExterlaAuthRoleFunctionActivity(code, requestedUser, app, activityCode);
					}
				} else
					throw new NonCentralizedAppException(requestedApp.getName() + " is not Centralized Application");
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

	private void logExterlaAuthRoleFunctionActivity(String code, EPUser requestedUser, EPApp app, String activityCode) {
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

	private void addIfTypeActionDoesNotExits(CentralV2RoleFunction domainRoleFunction) {
		if (domainRoleFunction.getCode().contains(PIPE)) {
			String newfunctionCodeFormat = EcompPortalUtils.getFunctionCode(domainRoleFunction.getCode());
			String newfunctionTypeFormat = EcompPortalUtils.getFunctionType(domainRoleFunction.getCode());
			String newfunctionActionFormat = EcompPortalUtils.getFunctionAction(domainRoleFunction.getCode());
			domainRoleFunction.setType(newfunctionTypeFormat);
			domainRoleFunction.setAction(newfunctionActionFormat);
			domainRoleFunction.setCode(newfunctionCodeFormat);
		} else {
			String type = externalAccessRolesService.getFunctionCodeType(domainRoleFunction.getCode());
			String action = externalAccessRolesService.getFunctionCodeAction(domainRoleFunction.getCode());
			domainRoleFunction.setType(type);
			domainRoleFunction.setAction(action);
		}
	}

	@PostMapping(value = { "/portalApi/role_function_list/removeRoleFunction/{appId}" })
	public PortalRestResponse<String> removeRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String roleFunc, @PathVariable("appId") Long appId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);

		if (roleFunc!=null) {
			SecureString secureString = new SecureString(roleFunc);

			Validator validator = VALIDATOR_FACTORY.getValidator();
			Set<ConstraintViolation<SecureString>> constraintViolations = validator.validate(secureString);

			if(!constraintViolations.isEmpty()){
				logger.error(EELFLoggerDelegate.errorLogger, "removeRoleFunction: Failed");
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Data is not valid", "ERROR");
			}
		}

		try {
			EPApp requestedApp = appService.getApp(appId);
			if (isAuthorizedUser(user, requestedApp)) {
				fieldsValidation(requestedApp);
				if (requestedApp.getCentralAuth()) {
					ObjectMapper mapper = new ObjectMapper();
					String data = roleFunc;
					boolean getDelFuncResponse = false;
					CentralV2RoleFunction availableRoleFunction = mapper.readValue(data, CentralV2RoleFunction.class);
					String code = availableRoleFunction.getType() + PIPE + availableRoleFunction.getCode() + PIPE
							+ availableRoleFunction.getAction();
					CentralV2RoleFunction domainRoleFunction = externalAccessRolesService.getRoleFunction(code,
							requestedApp.getUebKey());
					getDelFuncResponse = externalAccessRolesService
							.deleteCentralRoleFunction(domainRoleFunction.getCode(), requestedApp);
					if (getDelFuncResponse) {
						logger.info(EELFLoggerDelegate.applicationLogger,
								"deleteRoleFunction: succeeded for app {}, role {}", requestedApp.getId(),
								domainRoleFunction.getCode());
						String activityCode = EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION;
						AuditLog auditLog = getAuditInfo(user, activityCode);
						auditLog.setComments(
								EcompPortalUtils.truncateString(
										"Deleted function for app:" + requestedApp.getId() + " and function code:'"
												+ domainRoleFunction.getCode() + "'",
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
										domainRoleFunction.getCode()));
						MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
						MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
						MDC.remove(SystemProperties.MDC_TIMER);
						logger.info(EELFLoggerDelegate.auditLogger,
								"Remove role function " + domainRoleFunction.getName());
					}
				} else
					throw new NonCentralizedAppException(requestedApp.getName() + " is not Centralized Application");
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

	@GetMapping(value = { "/portalApi/centralizedApps" })
	public List<CentralizedApp> getCentralizedAppRoles(HttpServletRequest request, HttpServletResponse response, String userId) {
		if(userId!=null) {
			SecureString secureString = new SecureString(userId);

			Validator validator = VALIDATOR_FACTORY.getValidator();
			Set<ConstraintViolation<SecureString>> constraintViolations = validator.validate(secureString);

			if(!constraintViolations.isEmpty()){
				logger.error(EELFLoggerDelegate.errorLogger, "removeRoleFunction: Failed");
				return null;
			}
		}

		EPUser user = EPUserUtils.getUserSession(request);
		List<CentralizedApp> applicationsList = null;
		if (adminRolesService.isAccountAdmin(user) || adminRolesService.isSuperAdmin(user)
				|| adminRolesService.isRoleAdmin(user)) {
			applicationsList = externalAccessRolesService.getCentralizedAppsOfUser(userId);
		} else {
			logger.info(EELFLoggerDelegate.auditLogger,
					"RoleManageController.getCentralizedAppRoles, Unauthorized user");
			EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
		}
		return applicationsList;
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

	@PostMapping(value = { "/portalApi/syncRoles" }, produces = "application/json")
	public PortalRestResponse<String> syncRoles(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Long appId) {
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			EPApp app = appService.getApp(appId);
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

	@PostMapping(value = { "/portalApi/syncFunctions" }, produces = "application/json")
	public PortalRestResponse<String> syncFunctions(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Long appId) {
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			EPApp app = appService.getApp(appId);
			if (isAuthorizedUser(user, app)) {
				fieldsValidation(app);
				externalAccessRolesService.syncRoleFunctionFromExternalAccessSystem(app);
			} else {
				logger.info(EELFLoggerDelegate.auditLogger,
						"RoleManageController.syncFunctions, Unauthorized user:{}", user != null ? user.getOrgUserId() : "");
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
		allParentRoles = getAllParentRolesAsList(currentRole, allParentRoles);
		Iterator<CentralV2Role> availableChildRolesIterator = availableChildRoles.iterator();
		while (availableChildRolesIterator.hasNext()) {
			CentralV2Role role = availableChildRolesIterator.next();
			if (!role.getActive() || allParentRoles.contains(role) || role.getId().equals(roleId)) {
				availableChildRolesIterator.remove();
			}
		}
		return availableChildRoles;
	}

	private Set<CentralV2Role> getAllParentRolesAsList(CentralV2Role role, Set<CentralV2Role> allParentRoles) {
		Set<CentralV2Role> parentRoles = role.getParentRoles();
		allParentRoles.addAll(parentRoles);
		Iterator<CentralV2Role> parentRolesIterator = parentRoles.iterator();
		while (parentRolesIterator.hasNext()) {
			getAllParentRolesAsList(parentRolesIterator.next(), allParentRoles);
		}
		return allParentRoles;
	}

	public AuditLog getAuditInfo(EPUser user, String activityCode) {
		AuditLog auditLog = new AuditLog();
		auditLog.setUserId(user.getId());
		auditLog.setActivityCode(activityCode);
		auditLog.setAffectedRecordId(user.getOrgUserId());

		return auditLog;
	}

	private void fieldsValidation(EPApp app) throws Exception {
		app.getUebKey();
		List<EPApp> appInfo = externalAccessRolesService.getApp(app.getUebKey());
		if (appInfo.isEmpty()) {
			throw new InvalidApplicationException("Invalid credentials");
		}
		if (!appInfo.isEmpty() && EcompPortalUtils.checkIfRemoteCentralAccessAllowed()
				&& appInfo.get(0).getCentralAuth()) {
			ResponseEntity<String> response = externalAccessRolesService.getNameSpaceIfExists(appInfo.get(0));
			if (response.getStatusCode().value() == HttpServletResponse.SC_NOT_FOUND)
				throw new InvalidApplicationException("Invalid NameSpace");
		}
	}

	private boolean isAuthorizedUser(EPUser user, EPApp requestedApp) {
		if (user != null && (adminRolesService.isAccountAdminOfApplication(user, requestedApp)
				|| (adminRolesService.isSuperAdmin(user) && requestedApp.getId().equals(PortalConstants.PORTAL_APP_ID))))
			return true;
		return false;
	}

	private void SendErrorForUnauthorizedUser(HttpServletResponse response, EPUser user) throws IOException {
		EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
		response.getWriter().write("Unauthorized User");
	}

	@PostMapping(value = {
			"/portalApi/uploadRoleFunction/{appId}" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadRoleFunc(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UploadRoleFunctionExtSystem data, @PathVariable("appId") Long appId) {
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			EPApp app = appService.getApp(appId);
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
