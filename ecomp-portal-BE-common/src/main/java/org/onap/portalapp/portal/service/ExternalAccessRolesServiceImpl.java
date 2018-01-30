/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPAppRoleFunction;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.ExternalRoleDetails;
import org.onap.portalapp.portal.exceptions.DeleteDomainObjectFailedException;
import org.onap.portalapp.portal.exceptions.ExternalAuthSystemException;
import org.onap.portalapp.portal.exceptions.InactiveApplicationException;
import org.onap.portalapp.portal.exceptions.InvalidApplicationException;
import org.onap.portalapp.portal.exceptions.InvalidUserException;
import org.onap.portalapp.portal.exceptions.RoleFunctionException;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.transport.BulkUploadRoleFunction;
import org.onap.portalapp.portal.transport.BulkUploadUserRoles;
import org.onap.portalapp.portal.transport.CentralApp;
import org.onap.portalapp.portal.transport.CentralRole;
import org.onap.portalapp.portal.transport.CentralRoleFunction;
import org.onap.portalapp.portal.transport.CentralUser;
import org.onap.portalapp.portal.transport.CentralUserApp;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.CentralV2User;
import org.onap.portalapp.portal.transport.CentralV2UserApp;
import org.onap.portalapp.portal.transport.EcompUserRoles;
import org.onap.portalapp.portal.transport.ExternalAccessPerms;
import org.onap.portalapp.portal.transport.ExternalAccessPermsDetail;
import org.onap.portalapp.portal.transport.ExternalAccessRole;
import org.onap.portalapp.portal.transport.ExternalAccessRolePerms;
import org.onap.portalapp.portal.transport.ExternalAccessUser;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.transport.ExternalRoleDescription;
import org.onap.portalapp.portal.transport.GlobalRoleWithApplicationRoleFunction;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Service("externalAccessRolesService")
@EnableAspectJAutoProxy
@EPMetricsLog
@EPAuditLog
public class ExternalAccessRolesServiceImpl implements ExternalAccessRolesService {

	private static final String APP_ROLE_NAME_PARAM = "appRoleName";

	private static final String GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM = "getRoletoUpdateInExternalAuthSystem";

	private static final String GET_PORTAL_APP_ROLES_QUERY = "getPortalAppRoles";

	private static final String GET_ROLE_FUNCTION_QUERY = "getRoleFunction";

	private static final String FUNCTION_CODE_PARAMS = "functionCode";

	private static final String AND_FUNCTION_CD_EQUALS = " and function_cd = '";

	private static final String OWNER = ".owner";

	private static final String ADMIN = ".admin";

	private static final String ACCOUNT_ADMINISTRATOR = ".Account_Administrator";

	private static final String FUNCTION_PIPE = "|";

	private static final String IS_NULL_STRING = "null";

	private static final String EXTERNAL_AUTH_PERMS = "perms";

	private static final String EXTERNAL_AUTH_ROLE_DESCRIPTION = "description";

	private static final String IS_EMPTY_JSON_STRING = "{}";

	private static final String CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE = "Connecting to External Auth system";

	private static final String APP_ROLE_ID = "appRoleId";

	private static final String APP_ID = "appId";

	private static final String PRIORITY = "priority";

	private static final String ACTIVE = "active";

	private static final String ROLE_NAME = "name";

	private static final String ID = "id";

	private static final String APP_ID_EQUALS = " app_id = ";
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	
	@Autowired
	private EPAppService epAppService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	EPRoleService ePRoleService;

	RestTemplate template = new RestTemplate();
	
	
	// These decode values are based on HexDecoder
	static final String decodeValueOfForwardSlash = "2f";
	static final String decodeValueOfHiphen = "2d";
	static final String decodeValueOfStar = "2a";

	@SuppressWarnings("unchecked")
	public List<EPRole> getAppRoles(Long appId) throws Exception {
		List<EPRole> applicationRoles = null;
		final Map<String, Long> appParams = new HashMap<>();
		try {
			if (appId == 1) {
				applicationRoles = dataAccessService.executeNamedQuery("getPortalAppRolesList", null, null);
			} else {
				appParams.put("appId", appId);
				applicationRoles = dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles: failed", e);
			throw e;
		}
		return applicationRoles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getApp(String uebkey) throws Exception {
		List<EPApp> app = null;
		try {
			final Map<String, String> appUebkeyParams = new HashMap<>();
			appUebkeyParams.put("appKey", uebkey);
			app = dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null);
			if(!app.isEmpty() && !app.get(0).getEnabled() && !app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)){
				throw new InactiveApplicationException("Application:"+app.get(0).getName()+" is Unavailable");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getApp: failed", e);
			throw e;
		}
		return app;
	}

	/**
	 * It returns  single application role from external auth system 
	 * @param addRole
	 * @param app
	 * @return JSON string which contains application role details
	 * @throws Exception
	 */
	private String getSingleAppRole(String addRole, EPApp app) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "getSingleAppRole: Connecting to External Auth system");
		response = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
						+ app.getNameSpace()
						+ "." + addRole
								.replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
				HttpMethod.GET, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getSingleAppRole: Finished GET app role from External Auth system and status code: {} ",
				response.getStatusCode().value());
		return response.getBody();
	}

	@Override
	public boolean addRole(Role addRole, String uebkey) throws Exception {
		boolean response = false;
		ResponseEntity<String> addResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		EPApp app = getApp(uebkey).get(0);
		String newRole = updateExistingRoleInExternalSystem(addRole, app);
		HttpEntity<String> entity = new HttpEntity<>(newRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "addRole: Connecting to External Auth system");
		addResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
				HttpMethod.POST, entity, String.class);
		if (addResponse.getStatusCode().value() == 201) {
			response = true;
			logger.debug(EELFLoggerDelegate.debugLogger, "addRole: Finished adding role in the External Auth system  and response code: {} ", addResponse.getStatusCode().value());
		}
		if (addResponse.getStatusCode().value() == 406) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"addRole: Failed to add in the External Auth system due to {} and status code: {}", addResponse.getBody(), addResponse.getStatusCode().value());
		}
		return response;
	}

	/**
	 * 
	 * It deletes record in external auth system
	 * 
	 * @param delRole
	 * @return JSON String which has status code and response body 
	 * @throws Exception
	 */
	private ResponseEntity<String> deleteRoleInExternalSystem(String delRole) throws Exception {
		ResponseEntity<String> delResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(delRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleInExternalSystem: {} for DELETE: {}" , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, delRole);
		delResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role?force=true",
				HttpMethod.DELETE, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleInExternalSystem: Finished DELETE operation in the External Auth system {} and status code: {} ", delRole, delResponse.getStatusCode().value());
		return delResponse;
	}

	/**
	 * It updates role in external auth system
	 * 
	 * @param updateExtRole
	 * @param app
	 * @return true if success else false
	 * @throws Exception
	 * 					If updateRoleInExternalSystem fails we catch it in logger for detail message
	 */
	@SuppressWarnings("unchecked")
	private boolean updateRoleInExternalSystem(Role updateExtRole, EPApp app, boolean isGlobalRole) throws Exception {
		boolean response = false;
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<String> deleteResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		List<EPRole> epRoleList = null;
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)
				|| (isGlobalRole && !app.getId().equals(PortalConstants.PORTAL_APP_ID))) {
			epRoleList = getPortalAppRoleInfo(updateExtRole.getId());
		} else {
			epRoleList = getPartnerAppRoleInfo(updateExtRole.getId(), app);
		}

		// Assigning functions to global role
		if ((isGlobalRole && !app.getId().equals(PortalConstants.PORTAL_APP_ID))) {
			List<RoleFunction> globalRoleFunctionListNew = convertSetToListOfRoleFunctions(updateExtRole);
			EPApp portalAppInfo = epAppService.getApp(PortalConstants.PORTAL_APP_ID);
			addFunctionsTOGlobalRole(epRoleList, updateExtRole, globalRoleFunctionListNew, mapper, app, portalAppInfo);
			response = true;
		} else {
			String appRole = getSingleAppRole(epRoleList.get(0).getName(), app);
			List<RoleFunction> roleFunctionListNew = convertSetToListOfRoleFunctions(updateExtRole);
			if (!appRole.equals(IS_EMPTY_JSON_STRING)) {
				JSONObject jsonObj = new JSONObject(appRole);
				JSONArray extRole = jsonObj.getJSONArray("role");
				if (!extRole.getJSONObject(0).has(EXTERNAL_AUTH_ROLE_DESCRIPTION)) {
					String roleName = extRole.getJSONObject(0).getString(ROLE_NAME);
					Map<String, String> delRoleKeyMapper = new HashMap<>();
					delRoleKeyMapper.put(ROLE_NAME, roleName);
					String delRoleKeyValue = mapper.writeValueAsString(delRoleKeyMapper);
					deleteResponse = deleteRoleInExternalSystem(delRoleKeyValue);
					if (deleteResponse.getStatusCode().value() != 200) {
						throw new ExternalAuthSystemException(deleteResponse.getBody());
					}
					addRole(updateExtRole, app.getUebKey());
				} else {
					String desc = extRole.getJSONObject(0).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
					String name = extRole.getJSONObject(0).getString(ROLE_NAME);
					List<ExternalAccessPerms> list = new ArrayList<>();
					if (extRole.getJSONObject(0).has(EXTERNAL_AUTH_PERMS)) {
						JSONArray perms = extRole.getJSONObject(0).getJSONArray(EXTERNAL_AUTH_PERMS);
						list = mapper.readValue(perms.toString(), TypeFactory.defaultInstance()
								.constructCollectionType(List.class, ExternalAccessPerms.class));
					}
					ExternalRoleDescription sysRoleList = mapper.readValue(desc, ExternalRoleDescription.class);
					// If role name or role functions are updated then delete
					// record in External System and add new record to avoid
					// conflicts
					Boolean existingRoleActive;
					boolean isActiveValueChanged;
					// check role active status
					existingRoleActive = new Boolean(sysRoleList.getActive());
					isActiveValueChanged = existingRoleActive.equals(updateExtRole.getActive());
					boolean isRoleNameChanged = false;
					if (!sysRoleList.getName().equals(updateExtRole.getName())) {
						isRoleNameChanged = true;
						Map<String, String> delRoleKeyMapper = new HashMap<>();
						delRoleKeyMapper.put(ROLE_NAME, name);
						String delRoleKeyValue = mapper.writeValueAsString(delRoleKeyMapper);
						deleteResponse = deleteRoleInExternalSystem(delRoleKeyValue);
						if (deleteResponse.getStatusCode().value() != 200) {
							logger.error(EELFLoggerDelegate.errorLogger,
									"updateRoleInExternalSystem:  Failed to delete role in external system due to {} ",
									deleteResponse.getBody());
							throw new ExternalAuthSystemException(deleteResponse.getBody());
						}
						addRole(updateExtRole, app.getUebKey());
						// add partner functions to the global role in External Auth System
						if(!list.isEmpty() && isGlobalRole){
							addPartnerHasRoleFunctionsToGlobalRole(list, mapper, app, updateExtRole);	
						}
						list.removeIf(perm -> EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getNameSpace()));
						// if role name is changes please ignore the previous functions in External Auth and update with user requested functions
						addRemoveFunctionsToRole(updateExtRole, app, mapper, roleFunctionListNew, name,
								list);
					}
					boolean checkPriorityStatus = StringUtils.equals(String.valueOf(sysRoleList.getPriority()),
							String.valueOf(updateExtRole.getPriority()));
					ExternalAccessRole updateRole = new ExternalAccessRole();
					if (!isActiveValueChanged || !checkPriorityStatus || sysRoleList.getId().equals(IS_NULL_STRING)
							|| !sysRoleList.getId().equals(String.valueOf(epRoleList.get(0).getId()))) {
						String updateDesc = "";
						List<EPRole> getRole;
						final Map<String, String> getAppRoleByName =  new HashMap<>();
						getAppRoleByName.put(APP_ROLE_NAME_PARAM, updateExtRole.getName());
						if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
							getRole = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, getAppRoleByName,
									null);
						} else {
							getAppRoleByName.put("appId", String.valueOf(app.getId()));
							getRole = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM,
									getAppRoleByName, null);
						}
						Map<String, String> extSystemUpdateRoleJsonMapper = new LinkedHashMap<>();
						extSystemUpdateRoleJsonMapper.put(ID, String.valueOf(getRole.get(0).getId()));
						extSystemUpdateRoleJsonMapper.put(ROLE_NAME, String.valueOf(updateExtRole.getName()));
						extSystemUpdateRoleJsonMapper.put(ACTIVE, String.valueOf(updateExtRole.getActive()));
						extSystemUpdateRoleJsonMapper.put(PRIORITY, String.valueOf(updateExtRole.getPriority()));
						if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
							extSystemUpdateRoleJsonMapper.put(APP_ID, "null");
							extSystemUpdateRoleJsonMapper.put(APP_ROLE_ID, "null");
						} else {
							extSystemUpdateRoleJsonMapper.put(APP_ID, String.valueOf(app.getId()));
							extSystemUpdateRoleJsonMapper.put(APP_ROLE_ID,
									String.valueOf(getRole.get(0).getAppRoleId()));

						}
						updateDesc = mapper.writeValueAsString(extSystemUpdateRoleJsonMapper);
						updateRole.setName(app.getNameSpace() + "." + updateExtRole.getName().replaceAll(
								EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
						updateRole.setDescription(updateDesc);
						String updateRoleDesc = mapper.writeValueAsString(updateRole);
						HttpEntity<String> entity = new HttpEntity<>(updateRoleDesc, headers);
						logger.debug(EELFLoggerDelegate.debugLogger, "updateRoleInExternalSystem: {} for PUT: {}",
								CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRoleDesc);
						ResponseEntity<String> updatePermsResponse = template.exchange(
								SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
										+ "role",
								HttpMethod.PUT, entity, String.class);
						logger.debug(EELFLoggerDelegate.debugLogger,
								"updateRoleInExternalSystem: Finished updating in External Auth system {} and status code: {} ",
								updateRoleDesc, updatePermsResponse.getStatusCode().value());
					}
					if(!isRoleNameChanged) {
						response = addRemoveFunctionsToRole(updateExtRole, app, mapper, roleFunctionListNew, name,
								list);
					}
				}
			} else {
				// It seems like role exists in local DB but not in External
				// Access system
				addRole(updateExtRole, app.getUebKey());
				List<RoleFunction> roleFunctionListUpdate = convertSetToListOfRoleFunctions(updateExtRole);
				response = true;
				if (!roleFunctionListUpdate.isEmpty()) {
					addRoleFunctionsInExternalSystem(updateExtRole, mapper, app);
				}
			}
		}
		return response;
	}

	private boolean addRemoveFunctionsToRole(Role updateExtRole, EPApp app, ObjectMapper mapper,
			List<RoleFunction> roleFunctionListNew, String name, List<ExternalAccessPerms> list) throws Exception {
		boolean response;
		Map<String, RoleFunction> updateRoleFunc = new HashMap<>();
		for (RoleFunction addPerm : roleFunctionListNew) {
			updateRoleFunc.put(addPerm.getCode(), addPerm);
		}
		final Map<String, ExternalAccessPerms> extRolePermMap = new HashMap<>();
		final Map<String, ExternalAccessPerms> extRolePermMapPipes = new HashMap<>();
		list.removeIf(perm -> !EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getNameSpace()));
		// Update permissions in the ExternalAccess System
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		if (!list.isEmpty()) {
			for (ExternalAccessPerms perm : list) {
				RoleFunction roleFunc =  updateRoleFunc.get(perm.getType().substring(app.getNameSpace().length()+1) + FUNCTION_PIPE + perm.getInstance() + FUNCTION_PIPE + perm.getAction());	
				if (roleFunc==null) {
					RoleFunction roleFuncPipeFilter =  updateRoleFunc.get(perm.getInstance());
					if(roleFuncPipeFilter == null)
					removePermForRole(perm, mapper, name, headers);
				}
				extRolePermMap.put(perm.getInstance(), perm);
				extRolePermMapPipes.put(
						perm.getType().substring(app.getNameSpace().length()+1) + FUNCTION_PIPE + perm.getInstance() + FUNCTION_PIPE + perm.getAction(), perm);
			}
		}
		response = true;
		if (!roleFunctionListNew.isEmpty()) {
			for (RoleFunction roleFunc : roleFunctionListNew) {
				if(roleFunc.getCode().contains(FUNCTION_PIPE)) {
					ExternalAccessPerms perm = extRolePermMapPipes.get(roleFunc.getCode());
					if (perm == null) {
						response = addFunctionsToRoleInExternalAuthSystem(updateExtRole, app, mapper, headers,
								roleFunc);
					}
				} else {
					if (!extRolePermMap.containsKey(EcompPortalUtils.getFunctionCode(roleFunc.getCode()))) {
						response = addFunctionsToRoleInExternalAuthSystem(updateExtRole, app, mapper, headers,
								roleFunc);
					}
				}
			}
		}
		return response;
	}
	
	/*
	 * Adds function to the role in the external auth system while editing a role or updating new functions to a role 
	 *
	 */
	private boolean addFunctionsToRoleInExternalAuthSystem(Role updateExtRole, EPApp app, ObjectMapper mapper,
			HttpHeaders headers, RoleFunction roleFunc) throws JsonProcessingException {
		boolean response;
		ExternalAccessRolePerms extRolePerms;
		ExternalAccessPerms extPerms;
		String code = "";
		String type = "";
		String action = "";
		if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
			code = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
			type = getFunctionType(roleFunc.getCode());
			action = getFunctionAction(roleFunc.getCode());
		} else {
			code = roleFunc.getCode();
			type = roleFunc.getCode().contains("menu") ? "menu" : "url";
			action = "*";
		}
		extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + type, code, action);
		extRolePerms = new ExternalAccessRolePerms(extPerms,
				app.getNameSpace() + "."
						+ updateExtRole.getName().replaceAll(
								EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS,
								"_"));
		String updateRolePerms = mapper.writeValueAsString(extRolePerms);
		HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "updateRoleInExternalSystem: {} for POST: {}",
				CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRolePerms);
		ResponseEntity<String> addResponse = template.exchange(
				SystemProperties.getProperty(
						EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
				HttpMethod.POST, entity, String.class);
		if (addResponse.getStatusCode().value() != 201 && addResponse.getStatusCode().value()!= 409) {
			response = false;
			logger.debug(EELFLoggerDelegate.debugLogger,
					"updateRoleInExternalSystem: Connected to External Auth system but something went wrong! due to {} and statuscode: {}",
					addResponse.getStatusCode().getReasonPhrase(),
					addResponse.getStatusCode().value());
		} else {
			response = true;
			logger.debug(EELFLoggerDelegate.debugLogger,
					"updateRoleInExternalSystem: Finished adding permissions to roles in External Auth system {} and status code: {} ",
					updateRolePerms, addResponse.getStatusCode().value());
		}
		return response;
	}
	
	private void addPartnerHasRoleFunctionsToGlobalRole(List<ExternalAccessPerms> permslist, ObjectMapper mapper,
			EPApp app, Role updateExtRole) throws Exception {
		for (ExternalAccessPerms perm : permslist) {
			if (!EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getNameSpace())) {
				ExternalAccessRolePerms extAddGlobalRolePerms = null;
				ExternalAccessPerms extAddPerms = null;
				extAddPerms = new ExternalAccessPerms(perm.getType(), perm.getInstance(), perm.getAction());
				extAddGlobalRolePerms = new ExternalAccessRolePerms(extAddPerms,
						app.getNameSpace() + "." + updateExtRole.getName().replaceAll(
								EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
				String addPerms = mapper.writeValueAsString(extAddGlobalRolePerms);
				HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
				HttpEntity<String> entity = new HttpEntity<>(addPerms, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addPartnerHasRoleFunctionsToGlobalRole: {} ",
						CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
				try {
					ResponseEntity<String> addResponse = template
							.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
									+ "role/perm", HttpMethod.POST, entity, String.class);
					if (addResponse.getStatusCode().value() != 201) {
						logger.debug(EELFLoggerDelegate.debugLogger,
								"addPartnerHasRoleFunctionsToGlobalRole: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
								addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
					} else {
						logger.debug(EELFLoggerDelegate.debugLogger,
								"addPartnerHasRoleFunctionsToGlobalRole: Finished adding permissions to roles in External Auth system and status code: {} ",
								addResponse.getStatusCode().value());
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "addPartnerHasRoleFunctionsToGlobalRole: Failed for POST request: {} due to ",
							addPerms, e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addFunctionsTOGlobalRole(List<EPRole> epRoleList, Role updateExtRole, List<RoleFunction> roleFunctionListNew, ObjectMapper mapper, EPApp app, EPApp portalAppInfo)
			throws Exception {
		try {
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addFunctionsTOGlobalRole");
			//GET Permissions from External Auth System
			JSONArray extPerms = getExtAuthPermissions(app);
			List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPerms);
			final Map<String, ExternalAccessPermsDetail> existingPermsWithRoles = new HashMap<>();
			final Map<String, ExternalAccessPermsDetail> existingPermsWithRolesWithPipes = new HashMap<>();
			final Map<String, RoleFunction> userRquestedFunctionsMap = new HashMap<>();
			final Map<String, RoleFunction> userRquestedFunctionsMapPipesFilter = new HashMap<>();
			for (ExternalAccessPermsDetail permDetail : permsDetailList) {
				existingPermsWithRoles.put(EcompPortalUtils.getFunctionCode(permDetail.getInstance()), permDetail);
				existingPermsWithRolesWithPipes.put(permDetail.getInstance(), permDetail);

			}
			// Add If function does not exists for role in External Auth System
			for (RoleFunction roleFunc : roleFunctionListNew) {
				String roleFuncCode = "";
				ExternalAccessPermsDetail permsDetail;
				if(roleFunc.getCode().contains(FUNCTION_PIPE)) {
					roleFuncCode = roleFunc.getCode();
					permsDetail = existingPermsWithRolesWithPipes.get(roleFunc.getCode());
				} else {
					roleFuncCode = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
					permsDetail = existingPermsWithRoles.get(roleFuncCode);
				}
				if (null == permsDetail.getRoles() || !permsDetail.getRoles()
						.contains(portalAppInfo.getNameSpace() + FUNCTION_PIPE + epRoleList.get(0).getName().replaceAll(
								EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"))) {
					addRoleFunctionsToGlobalRoleInExternalSystem(roleFunc, updateExtRole, mapper, app, portalAppInfo);
				}
				userRquestedFunctionsMap.put(roleFuncCode, roleFunc);
				userRquestedFunctionsMapPipesFilter.put(EcompPortalUtils.getFunctionCode(roleFuncCode), roleFunc);
			}			
			// Delete functions if exists in External Auth System but not in incoming request
			final Map<String, Long> epAppRoleFuncParams =  new HashMap<>();
			epAppRoleFuncParams.put("requestedAppId", app.getId());
			epAppRoleFuncParams.put("roleId",updateExtRole.getId());
			List<GlobalRoleWithApplicationRoleFunction> globalRoleFunctionList = dataAccessService.executeNamedQuery("getGlobalRoleForRequestedApp", epAppRoleFuncParams, null);
			for(GlobalRoleWithApplicationRoleFunction globalRoleFunc: globalRoleFunctionList){
				String globalRoleFuncWithoutPipes = "";
				RoleFunction roleFunc = null;
				if(globalRoleFunc.getFunctionCd().contains(FUNCTION_PIPE)) {
					globalRoleFuncWithoutPipes = globalRoleFunc.getFunctionCd();
					roleFunc = userRquestedFunctionsMap.get(globalRoleFuncWithoutPipes);
				}else {
					globalRoleFuncWithoutPipes  = EcompPortalUtils.getFunctionCode(globalRoleFunc.getFunctionCd());
					roleFunc = userRquestedFunctionsMapPipesFilter.get(globalRoleFuncWithoutPipes);
				}
				if(roleFunc == null){
					ExternalAccessPermsDetail permDetailFromMap = globalRoleFunc.getFunctionCd().contains(FUNCTION_PIPE) ? existingPermsWithRolesWithPipes.get(globalRoleFuncWithoutPipes) : existingPermsWithRoles.get(globalRoleFuncWithoutPipes);
					ExternalAccessPerms perm = new ExternalAccessPerms(permDetailFromMap.getType(), EcompPortalUtils.getFunctionCode(permDetailFromMap.getInstance()), permDetailFromMap.getAction());
					String roleName = portalAppInfo.getNameSpace()+"."+globalRoleFunc.getRoleName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_");
					HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
					removePermForRole(perm, mapper, roleName, headers);
				}
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "Finished addFunctionsTOGlobalRole");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addFunctionsTOGlobalRole: Failed",e);
			throw e;
		}
	}

	private void addRoleFunctionsToGlobalRoleInExternalSystem(RoleFunction addFunction, Role globalRole, ObjectMapper mapper, EPApp app,
			EPApp portalAppInfo) throws Exception {
		try {
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addRoleFunctionsToGlobalRoleInExternalSystem");
			ExternalAccessRolePerms extAddRolePerms = null;
			ExternalAccessPerms extAddPerms = null;
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
				String code = "";
				String type = "";
				String action = "";
				if (addFunction.getCode().contains(FUNCTION_PIPE)) {
					code = EcompPortalUtils.getFunctionCode(addFunction.getCode());
					type = getFunctionType(addFunction.getCode());
					action = getFunctionAction(addFunction.getCode());
				} else {
					code = addFunction.getCode();
					type = addFunction.getCode().contains("menu") ? "menu" : "url";
					action = "*";
				}
				extAddPerms = new ExternalAccessPerms(app.getNameSpace() + "." + type, code, action);
				extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
						portalAppInfo.getNameSpace() + "." + globalRole.getName().replaceAll(
								EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
				String updateRolePerms = mapper.writeValueAsString(extAddRolePerms);
				HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: {} ",
						CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
				ResponseEntity<String> addResponse = template
						.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
								+ "role/perm", HttpMethod.POST, entity, String.class);
				if (addResponse.getStatusCode().value() != 201) {
					logger.debug(EELFLoggerDelegate.debugLogger,
							"addRoleFunctionsInExternalSystem: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
							addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
				} else {
					logger.debug(EELFLoggerDelegate.debugLogger,
							"addRoleFunctionsInExternalSystem: Finished adding permissions to roles in External Auth system and status code: {} ",
							addResponse.getStatusCode().value());
				}
			logger.debug(EELFLoggerDelegate.debugLogger, "Finished addRoleFunctionsToGlobalRoleInExternalSystem");
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionsToGlobalRoleInExternalSystem: Failed",e);
			throw e;
		}
	}

	/**
	 * 
	 * It adds functions to the role in external auth system 
	 * 
	 * @param updateExtRole
	 * @param addPermsMapper
	 * @param app
	 * @return true if success else false
	 * @throws Exception
	 */
	private boolean addRoleFunctionsInExternalSystem(Role updateExtRole, ObjectMapper addPermsMapper, EPApp app)
			throws Exception {
		boolean response = false;
		ExternalAccessRolePerms extAddRolePerms = null;
		ExternalAccessPerms extAddPerms = null;
		List<RoleFunction> roleFunctionListAdd = convertSetToListOfRoleFunctions(updateExtRole);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		for (RoleFunction roleFunc : roleFunctionListAdd) {
			String code = "";
			String type= "";
			String action = "";
			if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
				code = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
				type = getFunctionType(roleFunc.getCode());
				action = getFunctionAction(roleFunc.getCode());
			} else {
				code = roleFunc.getCode();
				type = roleFunc.getCode().contains("menu") ? "menu" : "url";
				action = "*";
			}
			extAddPerms = new ExternalAccessPerms(app.getNameSpace() + "." + type, code, action);
			extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
					app.getNameSpace() + "." + updateExtRole.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
			String updateRolePerms = addPermsMapper.writeValueAsString(extAddRolePerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: {} for POST: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRolePerms);
			ResponseEntity<String> addResponse = template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
					HttpMethod.POST, entity, String.class);
			if (addResponse.getStatusCode().value() != 201) {
				response = false;
				logger.debug(EELFLoggerDelegate.debugLogger,
						"addRoleFunctionsInExternalSystem: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
						addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
			} else {
				response = true;
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: Finished adding permissions to roles in External Auth system {} and status code: {} ", updateRolePerms, addResponse.getStatusCode().value());
			}
		}
		return response;
	}

	/**
	 * 
	 * It converts list of functions in updateExtRole parameter to the RoleFunction object
	 * 
	 * @param updateExtRole
	 * @return list of functions 
	 */
	@SuppressWarnings("unchecked")
	private List<RoleFunction> convertSetToListOfRoleFunctions(Role updateExtRole) {
		Set<RoleFunction> roleFunctionSetList = updateExtRole.getRoleFunctions();
		List<RoleFunction> roleFunctionList = new ArrayList<>();
		ObjectMapper roleFuncMapper = new ObjectMapper();
		Iterator<RoleFunction> itetaror = roleFunctionSetList.iterator();
		while (itetaror.hasNext()) {
			Object nextValue = itetaror.next();
			RoleFunction roleFunction = roleFuncMapper.convertValue(nextValue, RoleFunction.class);
			roleFunctionList.add(roleFunction);
		}
		return roleFunctionList.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * It delete permissions/functions in the external auth system
	 * 
	 * @param perm
	 * @param permMapper
	 * @param name
	 * @param headers
	 * @throws JsonProcessingException 
	 * @throws Exception
	 */
	private void removePermForRole(ExternalAccessPerms perm, ObjectMapper permMapper, String name, HttpHeaders headers)
			throws ExternalAuthSystemException, JsonProcessingException {
		ExternalAccessRolePerms extAccessRolePerms = new ExternalAccessRolePerms(perm, name);
		String permDetails = permMapper.writeValueAsString(extAccessRolePerms);
		try{
		HttpEntity<String> deleteEntity = new HttpEntity<>(permDetails, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "removePermForRole: {} for DELETE: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, permDetails);
		ResponseEntity<String> deletePermResponse = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/"
						+ name + "/perm", HttpMethod.DELETE, deleteEntity, String.class);
		if (deletePermResponse.getStatusCode().value() != 200) {
			throw new ExternalAuthSystemException(deletePermResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "removePermForRole: Finished deleting permission to role in External Auth system: {} and status code: {}",
				permDetails, deletePermResponse.getStatusCode().value());
		} catch(Exception e){
			if(e.getMessage().contains("404")){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to add role for DELETE request: {} due to {}", permDetails, e.getMessage());				
			} else{
				throw e;
			}
		}
	}

	/**
	 * It will create new role in the External Auth System
	 * 
	 * @param newRole
	 * @param app
	 * @return true if successfully added in the system else false
	 * @throws Exception
	 *             If fails to add role in the system
	 */
	private void addNewRoleInExternalSystem(List<EPRole> newRole, EPApp app) throws Exception, HttpClientErrorException {
		try{
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		ObjectMapper mapper = new ObjectMapper();
		String addNewRole = "";
		ExternalAccessRole extRole = new ExternalAccessRole();
		String addDesc = null;
		Map<String, String> extSystemJsonMapper = new LinkedHashMap<>();
		extSystemJsonMapper.put(ID, String.valueOf(newRole.get(0).getId()));
		extSystemJsonMapper.put(ROLE_NAME, String.valueOf(newRole.get(0).getName()));
		extSystemJsonMapper.put(ACTIVE, String.valueOf(newRole.get(0).getActive()));
		extSystemJsonMapper.put(PRIORITY, String.valueOf(newRole.get(0).getPriority()));
		extSystemJsonMapper.put(APP_ID, String.valueOf(newRole.get(0).getAppId()));
		extSystemJsonMapper.put(APP_ROLE_ID, String.valueOf(newRole.get(0).getAppRoleId()));
		addDesc = mapper.writeValueAsString(extSystemJsonMapper);
		extRole.setName(app.getNameSpace() + "." + newRole.get(0).getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
		extRole.setDescription(addDesc);
		addNewRole = mapper.writeValueAsString(extRole);
		HttpEntity<String> postEntity = new HttpEntity<>(addNewRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "addNewRoleInExternalSystem: {} for POST: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, addNewRole);
		ResponseEntity<String> addNewRoleInExternalSystem = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
				HttpMethod.POST, postEntity, String.class);
			if (addNewRoleInExternalSystem.getStatusCode().value() == 201) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"addNewRoleInExternalSystem: Finished adding into External Auth system for POST: {} and status code: {}",
						addNewRole, addNewRoleInExternalSystem.getStatusCode().value());
			}
		}catch(HttpClientErrorException ht){
			dataAccessService.deleteDomainObjects(EPRole.class, " role_id = "+ newRole.get(0).getId(), null);
			logger.error(EELFLoggerDelegate.debugLogger, "addNewRoleInExternalSystem: Failed to add in External Auth system and status code: {}",
					ht);
			throw new HttpClientErrorException(ht.getStatusCode());
		}
	}

	/**
	 * 
	 * It updates existing role in the External Auth System
	 * 
	 * @param addRole
	 *            It Contains role information
	 * @param app
	 * @return string which is formatted to match with the external auth system
	 * @throws JsonProcessingException
	 */
	private String updateExistingRoleInExternalSystem(Role addRole, EPApp app) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String addNewRole = "";
		ExternalAccessRole extRole = new ExternalAccessRole();
		List<EPRole> role = null;
		String addDesc = null;
		Map<String, String> extSystemUpdateRole = new LinkedHashMap<>();
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
			role = getPortalAppRoleInfo(addRole.getId());
		} else {
			role = getPartnerAppRoleInfo(addRole.getId(), app);
		}
		extSystemUpdateRole.put(ID, String.valueOf(role.get(0).getId()));
		extSystemUpdateRole.put(ROLE_NAME, String.valueOf(addRole.getName()));
		extSystemUpdateRole.put(ACTIVE, String.valueOf(role.get(0).getActive()));
		extSystemUpdateRole.put(PRIORITY, String.valueOf(role.get(0).getPriority()));
		extSystemUpdateRole.put(APP_ID, String.valueOf(role.get(0).getAppId()));
		extSystemUpdateRole.put(APP_ROLE_ID, String.valueOf(role.get(0).getAppRoleId()));
		addDesc = mapper.writeValueAsString(extSystemUpdateRole);
		extRole.setName(app.getNameSpace() + "." + addRole.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
		extRole.setDescription(addDesc);
		addNewRole = mapper.writeValueAsString(extRole);
		return addNewRole;
	}

	/**
	 * It create a role in the external auth system and then in our local 
	 * 
	 * @param addRoleInDB
	 * @param app
	 * @return true else false
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	private boolean addRoleInEcompDB(Role addRoleInDB, EPApp app) throws Exception {		
		boolean result = false;
		EPRole epRole = null;
		Set<RoleFunction> roleFunctionList = addRoleInDB.getRoleFunctions();
		List<RoleFunction> roleFunctionListNew = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		Iterator<RoleFunction> itetaror = roleFunctionList.iterator();
		while (itetaror.hasNext()) {
			Object nextValue = itetaror.next();
			RoleFunction roleFunction = mapper.convertValue(nextValue, RoleFunction.class);
			roleFunctionListNew.add(roleFunction);
		}
		List<RoleFunction> listWithoutDuplicates = roleFunctionListNew.stream().distinct().collect(Collectors.toList());
		try {
			if (addRoleInDB.getId() == null) { // check if it is new role
				if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
					checkIfRoleExitsInExternalSystem(addRoleInDB, app);
				}
				EPRole epRoleNew = new EPRole();
				epRoleNew.setActive(addRoleInDB.getActive());
				epRoleNew.setName(addRoleInDB.getName());
				epRoleNew.setPriority(addRoleInDB.getPriority());
				if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					epRoleNew.setAppId(null);
				} else {
					epRoleNew.setAppId(app.getId());
				}
				dataAccessService.saveDomainObject(epRoleNew, null);
				List<EPRole> getRoleCreated = null;
				final Map<String, String> epAppRoleParams =  new HashMap<>();
				final Map<String, String> epAppPortalRoleParams =  new HashMap<>();
				if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					epAppRoleParams.put("appId", String.valueOf(app.getId()));
					epAppRoleParams.put(APP_ROLE_NAME_PARAM, addRoleInDB.getName());
					List<EPRole> roleCreated = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, epAppRoleParams, null);
					EPRole epUpdateRole = roleCreated.get(0);
					epUpdateRole.setAppRoleId(epUpdateRole.getId());
					dataAccessService.saveDomainObject(epUpdateRole, null);
					getRoleCreated = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, epAppRoleParams, null);
				} else {
					epAppPortalRoleParams.put(APP_ROLE_NAME_PARAM, addRoleInDB.getName());
					getRoleCreated = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, epAppPortalRoleParams, null);
				}
				// Add role in External Auth system
				if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
					addNewRoleInExternalSystem(getRoleCreated, app);
				}
			} else { // if role already exists then update it
				EPRole globalRole = null;
				List<EPRole> applicationRoles;
				List<EPRole> globalRoleList = getGlobalRolesOfPortal();
				boolean isGlobalRole = false;
				if (!globalRoleList.isEmpty()) {
					EPRole role = globalRoleList.stream().filter(x -> addRoleInDB.getId().equals(x.getId())).findAny()
							.orElse(null);
					if (role != null) {
						globalRole = role;
						isGlobalRole = true;
					}
				}
				if (app.getId().equals(PortalConstants.PORTAL_APP_ID)
						|| (globalRole != null && app.getId() != globalRole.getAppId())) {
					applicationRoles = getPortalAppRoleInfo(addRoleInDB.getId());
				} else {
					applicationRoles = getPartnerAppRoleInfo(addRoleInDB.getId(), app);
				}
				if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
					updateRoleInExternalSystem(addRoleInDB, app, isGlobalRole);
					// Add all user to the re-named role in external auth system
					if (!applicationRoles.isEmpty()
							&& !addRoleInDB.getName().equals(applicationRoles.get(0).getName())) {
						bulkUploadUsersSingleRole(app.getUebKey(), applicationRoles.get(0).getId(),
								addRoleInDB.getName());
					}
				}
				deleteRoleFunction(app, applicationRoles);
				if (!applicationRoles.isEmpty()) {
					epRole = applicationRoles.get(0);
					epRole.setName(addRoleInDB.getName());
					epRole.setPriority(addRoleInDB.getPriority());
					epRole.setActive(addRoleInDB.getActive());
					if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						epRole.setAppId(null);
						epRole.setAppRoleId(null);
					} else if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)
							&& applicationRoles.get(0).getAppRoleId() == null) {
						epRole.setAppRoleId(epRole.getId());
					}
					dataAccessService.saveDomainObject(epRole, null);
				}
				Long roleAppId = null;
				if (globalRole != null && !app.getId().equals(globalRole.getAppId()))
					roleAppId = PortalConstants.PORTAL_APP_ID;
				saveRoleFunction(listWithoutDuplicates, app, applicationRoles, roleAppId);
				result = true;
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleInEcompDB is failed", e);
			throw e;
		}
		return result;
	}

	/**
	 * 
	 * It validates whether role exists in external auth system
	 * 
	 * @param checkRole
	 * @param app
	 * @throws Exception
	 * 					If role exits
	 */
	private void checkIfRoleExitsInExternalSystem(Role checkRole, EPApp app) throws Exception {
		getNameSpaceIfExists(app);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		String roleName = app.getNameSpace() + "." + checkRole.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_");
		HttpEntity<String> checkRoleEntity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "checkIfRoleExitsInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> checkRoleInExternalSystem = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
						+ roleName, HttpMethod.GET, checkRoleEntity, String.class);
		if (!checkRoleInExternalSystem.getBody().equals(IS_EMPTY_JSON_STRING)) {
			logger.debug("checkIfRoleExitsInExternalSystem: Role already exists in external system {} and status code: {} ", checkRoleInExternalSystem.getBody(), checkRoleInExternalSystem.getStatusCode().value());
			throw new ExternalAuthSystemException(" Role already exists in external system");
		}
	}

	/**
	 * It saves list of functions to the role in portal
	 * 
	 * @param roleFunctionListNew
	 * @param app
	 * @param applicationRoles
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void saveRoleFunction(List<RoleFunction> roleFunctionListNew, EPApp app, List<EPRole> applicationRoles ,Long roleAppId)
			throws Exception {	
		final Map<String, String> getAppFunctionParams = new HashMap<>(); 

		for (RoleFunction roleFunc : roleFunctionListNew) {
			String code = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
			EPAppRoleFunction appRoleFunc = new EPAppRoleFunction();
			appRoleFunc.setAppId(app.getId());
			appRoleFunc.setRoleId(applicationRoles.get(0).getId());
			appRoleFunc.setRoleAppId(String.valueOf(roleAppId));
			getAppFunctionParams.put("appId", String.valueOf(app.getId()));
			getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
			// query to check if function code has pipes
			List<CentralV2RoleFunction> roleFunction = dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null);
			if(roleFunction.isEmpty()){
				getAppFunctionParams.put(FUNCTION_CODE_PARAMS, code);
				roleFunction = dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null);
			}
			if(roleFunction.size() > 1){
				CentralV2RoleFunction getExactFunctionCode = appFunctionListFilter(code, roleFunction);
				appRoleFunc.setCode(getExactFunctionCode.getCode());
			} else{
				appRoleFunc.setCode(roleFunction.get(0).getCode());
			}
			
			dataAccessService.saveDomainObject(appRoleFunc, null);
		}
	}

	/**
	 * 
	 * It filters the app functions which starts with similar name in the result set
	 * 
	 * @param roleFunc
	 * @param roleFunction
	 * @return CentralRoleFunction 
	 */
	private CentralV2RoleFunction appFunctionListFilter(String roleFuncCode, List<CentralV2RoleFunction> roleFunction) {
		final Map<String, CentralV2RoleFunction> appFunctionsFilter = new HashMap<>(); 
		final Map<String, CentralV2RoleFunction> appFunctionsFilterPipes = new HashMap<>(); 
		CentralV2RoleFunction getExactFunctionCode = null;
		for(CentralV2RoleFunction cenRoleFunction : roleFunction){
			appFunctionsFilter.put(cenRoleFunction.getCode(), cenRoleFunction);
			appFunctionsFilterPipes.put(EcompPortalUtils.getFunctionCode(cenRoleFunction.getCode()), cenRoleFunction);
		}
		getExactFunctionCode = appFunctionsFilter.get(roleFuncCode);
		if(getExactFunctionCode == null){
			getExactFunctionCode = appFunctionsFilterPipes.get(roleFuncCode);
		}
		return getExactFunctionCode;
	}
	
	/**
	 * It deletes all EPAppRoleFunction records in the portal
	 * 
	 * @param app
	 * @param role
	 */
	@SuppressWarnings("unchecked")
	private void deleteRoleFunction(EPApp app, List<EPRole> role) {
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", role.get(0).getId());
		List<EPAppRoleFunction> appRoleFunctionList =  dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null);
		if (!appRoleFunctionList.isEmpty()) {
			for (EPAppRoleFunction approleFunction : appRoleFunctionList) {
				dataAccessService.deleteDomainObject(approleFunction, null);
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EPUser> getUser(String loginId){
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", loginId);
		return (List<EPUser>) dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null);
	}

	@Override
	public String getV2UserWithRoles(String loginId, String uebkey) throws Exception {
		final Map<String, String> params = new HashMap<>();
		List<EPUser> userList = null;
		CentralV2User cenV2User = null;
		String result = null;
		try {
			params.put("orgUserIdValue", loginId);
			List<EPApp> appList = getApp(uebkey);
			if (!appList.isEmpty()) {
				userList = getUser(loginId);
				if (!userList.isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					cenV2User = getV2UserAppRoles(loginId, uebkey);
					result = mapper.writeValueAsString(cenV2User);
				} else if (userList.isEmpty()) {
					throw new InvalidUserException("User not found");
				}
			} else {
				throw new InactiveApplicationException("Application not found");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUser: failed", e);
			throw e;
		}
		return result;
	}

	@Override
	public List<CentralV2Role> getRolesForApp(String uebkey) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Entering into getRolesForApp");
		List<CentralV2Role> roleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		try {
			List<EPApp> app = getApp(uebkey);
			List<EPRole> appRolesList = getAppRoles(app.get(0).getId());
			roleList = createCentralRoleObject(app, appRolesList, roleList, params);
			if(app.get(0).getId() != PortalConstants.PORTAL_APP_ID){
			    List<CentralV2Role> globalRoleList = getGlobalRolesOfApplication(app.get(0).getId());
				List<EPRole> globalRolesList = getGlobalRolesOfPortal();
			    List<CentralV2Role> portalsGlobalRolesFinlaList = new ArrayList<>();
				if (!globalRolesList.isEmpty()) {
					for (EPRole eprole : globalRolesList) {
						CentralV2Role cenRole = convertRoleToCentralV2Role(eprole);
						portalsGlobalRolesFinlaList.add(cenRole);
					}
					roleList.addAll(globalRoleList);
					for (CentralV2Role role : portalsGlobalRolesFinlaList) {
						CentralV2Role result = roleList.stream()
									.filter(x -> role.getId().equals(x.getId())).findAny().orElse(null);
							if (result == null)
								roleList.add(role);
					}
				} else {
					for (EPRole role : globalRolesList) {
						CentralV2Role cenRole = convertRoleToCentralV2Role(role);
						roleList.add(cenRole);
					}
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRolesForApp: Failed!", e);
			throw e;
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Finished!");
		return roleList.stream().distinct().collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentralV2RoleFunction> getRoleFuncList(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<CentralV2RoleFunction> finalRoleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		params.put(APP_ID, app.getId());
		List<CentralV2RoleFunction> getRoleFuncList = dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null);
		for (CentralV2RoleFunction roleFuncItem : getRoleFuncList) {
			String code = EcompPortalUtils.getFunctionCode(roleFuncItem.getCode());
			String type = getFunctionType(roleFuncItem.getCode());
			String action = getFunctionAction(roleFuncItem.getCode());
			roleFuncItem.setCode(EPUserUtils.decodeFunctionCode(code));
			roleFuncItem.setType(type);
			roleFuncItem.setAction(action);
			finalRoleList.add(roleFuncItem);
		}
		return finalRoleList;
	}


	/**
	 * It return function action
	 * 
	 * @param roleFuncItem
	 * @return String action
	 */
	private String getFunctionAction(String roleFuncItem) {
		return (!roleFuncItem.contains(FUNCTION_PIPE)) ? "*"
				: EcompPortalUtils.getFunctionAction(roleFuncItem);
	}

	/**
	 * 
	 * It check function code has any pipes, if found return function type
	 * 
	 * @param roleFuncItem
	 * @param type
	 * @return function type
	 */
	private String getFunctionType(String roleFuncItem) {
		String type = null;
		if ((roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("menu"))
				|| (!roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("menu"))) {
			type = "menu";
		} else if (checkIfCodeHasNoPipesAndHasTypeUrl(roleFuncItem)
				||checkIfCodeHasPipesAndHasTypeUrl(roleFuncItem)
				||checkIfCodeHasNoPipesAndHasNoTypeUrl(roleFuncItem)) {
			type = "url";
		} else if (roleFuncItem.contains(FUNCTION_PIPE)
				&& (!roleFuncItem.contains("menu") || roleFuncItem.contains("url"))) {
			type = EcompPortalUtils.getFunctionType(roleFuncItem);
		}
		return type;
	}

	/**
	 * 
	 * It check whether function code has no pipes and no url string in it
	 * 
	 * @param roleFuncItem
	 * @return true or false
	 */
	private boolean checkIfCodeHasNoPipesAndHasNoTypeUrl(String roleFuncItem) {
		return !roleFuncItem.contains(FUNCTION_PIPE) && !roleFuncItem.contains("url");
	}
	
	/**
	 * 
	 * It check whether function code has pipes and url string in it  
	 * 
	 * @param roleFuncItem
	 * @return true or false
	 */
	private boolean checkIfCodeHasPipesAndHasTypeUrl(String roleFuncItem) {
		return roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("url");
	}

	/**
	 * 
	 * It check whether function code has no pipes and has url string in it 
	 * 
	 * @param roleFuncItem
	 * @return true or false
	 */
	private boolean checkIfCodeHasNoPipesAndHasTypeUrl(String roleFuncItem) {
		return !roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("url");
	}

	/**
	 * It returns user detail information which is deep copy of EPUser.class object
	 * 
	 * @param userInfo
	 * @param userAppSet
	 * @param app
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private CentralV2User createEPUser(EPUser userInfo, Set<EPUserApp> userAppSet, EPApp app) throws Exception {

		final Map<String, Long> params = new HashMap<>();
		CentralV2User userAppList = new CentralV2User();
		CentralV2User user1 = null;
		try {
			userAppList.setUserApps(new TreeSet<CentralV2UserApp>());
			for (EPUserApp userApp : userAppSet) {
				if (userApp.getRole().getActive()) {
					EPApp epApp = userApp.getApp();
					String globalRole = userApp.getRole().getName().toLowerCase();
					if (((epApp.getId().equals(app.getId()))
							&& (!userApp.getRole().getId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID)))
							|| ((epApp.getId().equals(PortalConstants.PORTAL_APP_ID))
									&& (globalRole.toLowerCase().startsWith("global_")))) {
						CentralV2UserApp cua = new CentralV2UserApp();
						cua.setUserId(null);
						CentralApp cenApp = new CentralApp(1L, epApp.getCreated(), epApp.getModified(),
								epApp.getCreatedId(), epApp.getModifiedId(), epApp.getRowNum(), epApp.getName(),
								epApp.getImageUrl(), epApp.getDescription(), epApp.getNotes(), epApp.getUrl(),
								epApp.getAlternateUrl(), epApp.getAppRestEndpoint(), epApp.getMlAppName(),
								epApp.getMlAppAdminId(), String.valueOf(epApp.getMotsId()), epApp.getAppPassword(),
								String.valueOf(epApp.getOpen()), String.valueOf(epApp.getEnabled()),
								epApp.getThumbnail(), epApp.getUsername(), epApp.getUebKey(), epApp.getUebSecret(),
								epApp.getUebTopicName());
						cua.setApp(cenApp);
						params.put("roleId", userApp.getRole().getId());
						params.put(APP_ID, userApp.getApp().getId());
						CentralV2Role centralRole;
						List<EPRole> globalRoleList;
						globalRoleList = getGlobalRolesOfPortal();
						EPRole result = globalRoleList.stream().filter(x -> userApp.getRole().getId().equals(x.getId()))
								.findAny().orElse(null);
						if (result != null && userApp.getApp().getId() != app.getId()) {
							userApp.getRole().setId(result.getId());
							centralRole = getGlobalRoleForRequestedApp(app.getId(), userApp.getRole().getId());
							cua.setRole(centralRole);
						} else {
							List<CentralV2RoleFunction> appRoleFunctionList = dataAccessService
									.executeNamedQuery("getAppRoleFunctionList", params, null);
							SortedSet<CentralV2RoleFunction> roleFunctionSet = new TreeSet<>();
							for (CentralV2RoleFunction roleFunc : appRoleFunctionList) {
								String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
								CentralV2RoleFunction cenRoleFunc = new CentralV2RoleFunction(roleFunc.getId(),
										functionCode, roleFunc.getName(), null, null);
								roleFunctionSet.add(cenRoleFunc);
							}
							Long userRoleId = null;
							if (globalRole.toLowerCase().startsWith("global_")
									&& epApp.getId().equals(PortalConstants.PORTAL_APP_ID)) {
								userRoleId = userApp.getRole().getId();
							} else {
								userRoleId = userApp.getRole().getAppRoleId();
							}
							CentralV2Role cenRole = new CentralV2Role(userRoleId, userApp.getRole().getCreated(),
									userApp.getRole().getModified(), userApp.getRole().getCreatedId(),
									userApp.getRole().getModifiedId(), userApp.getRole().getRowNum(),
									userApp.getRole().getName(), userApp.getRole().getActive(),
									userApp.getRole().getPriority(), roleFunctionSet, null, null);
							cua.setRole(cenRole);
						}
						userAppList.getUserApps().add(cua);
					}
				}
			}

			user1 = new CentralV2User(null, userInfo.getCreated(), userInfo.getModified(), userInfo.getCreatedId(),
					userInfo.getModifiedId(), userInfo.getRowNum(), userInfo.getOrgId(), userInfo.getManagerId(),
					userInfo.getFirstName(), userInfo.getMiddleInitial(), userInfo.getLastName(), userInfo.getPhone(),
					userInfo.getFax(), userInfo.getCellular(), userInfo.getEmail(), userInfo.getAddressId(),
					userInfo.getAlertMethodCd(), userInfo.getHrid(), userInfo.getOrgUserId(), userInfo.getOrgCode(),
					userInfo.getAddress1(), userInfo.getAddress2(), userInfo.getCity(), userInfo.getState(),
					userInfo.getZipCode(), userInfo.getCountry(), userInfo.getOrgManagerUserId(),
					userInfo.getLocationClli(), userInfo.getBusinessCountryCode(), userInfo.getBusinessCountryName(),
					userInfo.getBusinessUnit(), userInfo.getBusinessUnitName(), userInfo.getDepartment(),
					userInfo.getDepartmentName(), userInfo.getCompanyCode(), userInfo.getCompany(),
					userInfo.getZipCodeSuffix(), userInfo.getJobTitle(), userInfo.getCommandChain(),
					userInfo.getSiloStatus(), userInfo.getCostCenter(), userInfo.getFinancialLocCode(),
					userInfo.getLoginId(), userInfo.getLoginPwd(), userInfo.getLastLoginDate(), userInfo.getActive(),
					userInfo.getInternal(), userInfo.getSelectedProfileId(), userInfo.getTimeZoneId(),
					userInfo.isOnline(), userInfo.getChatId(), userAppList.getUserApps(), null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "createEPUser: createEPUser failed", e);
			throw e;
		}
		return user1;
	}

	@Override
	public CentralV2Role getRoleInfo(Long roleId, String uebkey) throws Exception {
		final Map<String, Long> params = new HashMap<>();
		List<CentralV2Role> roleList = new ArrayList<>();
		CentralV2Role cenRole = new CentralV2Role();
		List<EPRole> roleInfo = null;
		List<EPApp> app = null;
		try {
			app = getApp(uebkey);
			if (app.isEmpty()) {
				throw new InactiveApplicationException("Application not found");
			}
			if (app.get(0).getId() != PortalConstants.PORTAL_APP_ID) {
				List<EPRole> globalRoleList = new ArrayList<>();
				globalRoleList = getGlobalRolesOfPortal();
				if (globalRoleList.size() > 0) {
					EPRole result = globalRoleList.stream().filter(x -> roleId.equals(x.getId())).findAny()
							.orElse(null);
					if (result != null)
						return getGlobalRoleForRequestedApp(app.get(0).getId(), roleId);
				}
			}
			if (app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
				roleInfo = getPortalAppRoleInfo(roleId);
			} else {
				roleInfo = getPartnerAppRoleInfo(roleId, app.get(0));
			}
			roleList = createCentralRoleObject(app, roleInfo, roleList, params);
			if (roleList.isEmpty()) {
				return cenRole;
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo: failed", e);
			throw e;

		}
		return roleList.get(0);
	}

	@SuppressWarnings("unchecked")
	private List<EPRole> getPartnerAppRoleInfo(Long roleId, EPApp app) {
		List<EPRole> roleInfo;
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", roleId);
		getPartnerAppRoleParams.put("appId", app.getId());				
		roleInfo = dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null);
		if(roleInfo.isEmpty()) {
			getPartnerAppRoleParams.put("appRoleId", roleId);
			roleInfo = dataAccessService.executeNamedQuery("getPartnerAppRoleById", getPartnerAppRoleParams, null);
		}
		return roleInfo;
	}

	@SuppressWarnings("unchecked")
	private List<EPRole> getPortalAppRoleInfo(Long roleId) {
		List<EPRole> roleInfo;
		final Map<String, Long> getPortalAppRoleParams = new HashMap<>();
		getPortalAppRoleParams.put("roleId", roleId);
		roleInfo = dataAccessService.executeNamedQuery("getPortalAppRoleByRoleId", getPortalAppRoleParams, null);
		return roleInfo;
	}
	
	/**
	 * 
	 * It returns list of app roles along with role functions and which went through deep copy
	 * 
	 * @param app
	 * @param roleInfo
	 * @param roleList
	 * @param params
	 * @return
	 * @throws DecoderException 
	 */
	@SuppressWarnings("unchecked")
	private List<CentralV2Role> createCentralRoleObject(List<EPApp> app, List<EPRole> roleInfo,
			List<CentralV2Role> roleList, Map<String, Long> params) throws RoleFunctionException {
		for (EPRole role : roleInfo) {
			params.put("roleId", role.getId());
			params.put(APP_ID, app.get(0).getId());
			List<CentralV2RoleFunction> cenRoleFuncList = dataAccessService.executeNamedQuery("getAppRoleFunctionList",
					params, null);
			SortedSet<CentralV2RoleFunction> roleFunctionSet = new TreeSet<>();
			for (CentralV2RoleFunction roleFunc : cenRoleFuncList) {
				String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
				functionCode = EPUserUtils.decodeFunctionCode(functionCode);
				String type = getFunctionType(roleFunc.getCode());
				String action = getFunctionAction(roleFunc.getCode());
				CentralV2RoleFunction cenRoleFunc = new CentralV2RoleFunction(role.getId(), functionCode,
						roleFunc.getName(), null, type, action, null);
				roleFunctionSet.add(cenRoleFunc);
			}
			SortedSet<CentralV2Role> childRoles = new TreeSet<>();
			SortedSet<CentralV2Role> parentRoles = new TreeSet<>();
			CentralV2Role cenRole = null;
			if (role.getAppRoleId() == null) {
				cenRole = new CentralV2Role(role.getId(), role.getCreated(), role.getModified(), role.getCreatedId(),
						role.getModifiedId(), role.getRowNum(), role.getName(), role.getActive(), role.getPriority(),
						roleFunctionSet, childRoles, parentRoles);
			} else {
				cenRole = new CentralV2Role(role.getAppRoleId(), role.getCreated(), role.getModified(),
						role.getCreatedId(), role.getModifiedId(), role.getRowNum(), role.getName(), role.getActive(),
						role.getPriority(), roleFunctionSet, childRoles, parentRoles);
			}
			roleList.add(cenRole);
		}
		return roleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CentralV2RoleFunction getRoleFunction(String functionCode, String uebkey) throws Exception {
		if (functionCode.contains("|"))
			functionCode = EcompPortalUtils.getFunctionCode(functionCode);
		functionCode = encodeFunctionCode(functionCode);
		CentralV2RoleFunction roleFunc = null;
		EPApp app = getApp(uebkey).get(0);
		List<CentralV2RoleFunction> getRoleFuncList = null;
		final Map<String, String> params = new HashMap<>();
		try {
			params.put(FUNCTION_CODE_PARAMS, functionCode);
			params.put(APP_ID, String.valueOf(app.getId()));
			getRoleFuncList = dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, params, null);
			if (getRoleFuncList.isEmpty()) {
				return roleFunc;
			} else {
				if (getRoleFuncList.size() > 1) {
					CentralV2RoleFunction cenV2RoleFunction = appFunctionListFilter(functionCode, getRoleFuncList);
					if(cenV2RoleFunction == null)
						return roleFunc;
					roleFunc = checkIfPipesExitsInFunctionCode(cenV2RoleFunction);
				} else {
					roleFunc = checkIfPipesExitsInFunctionCode(getRoleFuncList.get(0));
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction: failed", e);
			throw e;
		}
		return roleFunc;
	}

	private CentralV2RoleFunction checkIfPipesExitsInFunctionCode(CentralV2RoleFunction getRoleFuncList) {
		CentralV2RoleFunction roleFunc;
		String functionCodeFormat = getRoleFuncList.getCode();
		if (functionCodeFormat.contains(FUNCTION_PIPE)) {
			String newfunctionCodeFormat = EcompPortalUtils.getFunctionCode(functionCodeFormat);
			String newfunctionTypeFormat = EcompPortalUtils.getFunctionType(functionCodeFormat);
			String newfunctionActionFormat = EcompPortalUtils.getFunctionAction(functionCodeFormat);
			roleFunc = new CentralV2RoleFunction(getRoleFuncList.getId(), newfunctionCodeFormat,
					getRoleFuncList.getName(), getRoleFuncList.getAppId(), newfunctionTypeFormat, newfunctionActionFormat,
					getRoleFuncList.getEditUrl());
		} else {
			roleFunc = new CentralV2RoleFunction(getRoleFuncList.getId(), functionCodeFormat,
					getRoleFuncList.getName(), getRoleFuncList.getAppId(),
					getRoleFuncList.getEditUrl());
		}
		return roleFunc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveCentralRoleFunction(CentralV2RoleFunction domainCentralRoleFunction, EPApp app) throws Exception {
		boolean saveOrUpdateFunction = false;
		try {
			domainCentralRoleFunction.setCode(encodeFunctionCode(domainCentralRoleFunction.getCode()));
			final Map<String, String> functionParams = new HashMap<>();
			functionParams.put("appId", String.valueOf(app.getId()));
			List<CentralV2RoleFunction> appRoleFuncWithPipe = new ArrayList<>();
			// If request coming from portal application we use type, instance/code and action to fetch record
			if(domainCentralRoleFunction.getType()!=null && domainCentralRoleFunction.getAction()!=null){
				functionParams.put(FUNCTION_CODE_PARAMS, domainCentralRoleFunction.getType()+FUNCTION_PIPE
						+domainCentralRoleFunction.getCode()+FUNCTION_PIPE+domainCentralRoleFunction.getAction());
				appRoleFuncWithPipe =  dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, functionParams, null);
				if(appRoleFuncWithPipe.isEmpty()){
					functionParams.put(FUNCTION_CODE_PARAMS, domainCentralRoleFunction.getCode());
					appRoleFuncWithPipe =  dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, functionParams, null);
				}
			} 
			// If request coming from SDK applications we use just function code to fetch record
			else{
				functionParams.put(FUNCTION_CODE_PARAMS, domainCentralRoleFunction.getCode());
			}		
			CentralV2RoleFunction appFunctionCode = null;
			if(!appRoleFuncWithPipe.isEmpty()){
				// Make sure we extract correct record if similar records are found as query uses like condition 
			   appFunctionCode = appFunctionListFilter(domainCentralRoleFunction.getCode(), appRoleFuncWithPipe);	
			   if(appFunctionCode == null){
				   appFunctionCode = domainCentralRoleFunction;
			   }
			} else{
				appFunctionCode = domainCentralRoleFunction;
			}
			appFunctionCode.setName(domainCentralRoleFunction.getName());
			if(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
				addRoleFunctionInExternalSystem(appFunctionCode, app);			
			}
			if(domainCentralRoleFunction.getType() != null && domainCentralRoleFunction.getAction() != null){
				appFunctionCode.setCode(domainCentralRoleFunction.getType()+
					FUNCTION_PIPE+domainCentralRoleFunction.getCode()+FUNCTION_PIPE+domainCentralRoleFunction.getAction());
			}
			appFunctionCode.setAppId(app.getId());
			dataAccessService.saveDomainObject(appFunctionCode, null);
			saveOrUpdateFunction = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveCentralRoleFunction: failed", e);
			throw e;
		}
		return saveOrUpdateFunction;
	}
	
	/**
	 * It creates application permission in external auth system
	 * 
	 * @param domainCentralRoleFunction
	 * @param app
	 * @throws Exception
	 */
	private void addRoleFunctionInExternalSystem(CentralV2RoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ExternalAccessPerms extPerms = new ExternalAccessPerms();
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth(); 
		String type = "";
		String instance = "";
		String action = "";
		if((domainCentralRoleFunction.getType()!=null && domainCentralRoleFunction.getAction()!=null) || domainCentralRoleFunction.getCode().contains(FUNCTION_PIPE)){
			type =  domainCentralRoleFunction.getCode().contains(FUNCTION_PIPE) ? EcompPortalUtils.getFunctionType(domainCentralRoleFunction.getCode()) : domainCentralRoleFunction.getType(); 
			instance =  domainCentralRoleFunction.getCode().contains(FUNCTION_PIPE) ?  EcompPortalUtils.getFunctionCode(domainCentralRoleFunction.getCode()) : domainCentralRoleFunction.getCode();
			action =  domainCentralRoleFunction.getCode().contains(FUNCTION_PIPE) ? EcompPortalUtils.getFunctionAction(domainCentralRoleFunction.getCode()) : domainCentralRoleFunction.getAction();
		} else{
			type = domainCentralRoleFunction.getCode().contains("menu") ? "menu" : "url";
			instance = domainCentralRoleFunction.getCode();
			action = "*"; 
		}		
		// get Permissions from External Auth System
		JSONArray extPermsList = getExtAuthPermissions(app);
		List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPermsList);
		String requestedPerm = type+FUNCTION_PIPE+instance+FUNCTION_PIPE+action;
		boolean checkIfFunctionsExits = permsDetailList.stream().anyMatch(permsDetail -> permsDetail.getInstance().equals(requestedPerm));
		if (!checkIfFunctionsExits) {
			try {
				extPerms.setAction(action);
				extPerms.setInstance(instance);
				extPerms.setType(app.getNameSpace() + "." + type);
				extPerms.setDescription(domainCentralRoleFunction.getName());
				String addFunction = mapper.writeValueAsString(extPerms);
				HttpEntity<String> entity = new HttpEntity<>(addFunction, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} for POST: {}" , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, addFunction);
				ResponseEntity<String> addPermResponse= template.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
						HttpMethod.POST, entity, String.class);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: Finished adding permission for POST: {} and status code: {} ", addPermResponse.getStatusCode().value(), addFunction);
			} catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
				throw e;
			}catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionInExternalSystem: Failed to add fucntion in external central auth system",
						e);
				throw e;
			}
		} else {
			try {
				extPerms.setAction(action);
				extPerms.setInstance(instance);
				extPerms.setType(app.getNameSpace() + "." + type);
				extPerms.setDescription(domainCentralRoleFunction.getName());
				String updateRoleFunction = mapper.writeValueAsString(extPerms);
				HttpEntity<String> entity = new HttpEntity<>(updateRoleFunction, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} for PUT: {}" , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRoleFunction);
				ResponseEntity<String> updatePermResponse = template.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
						HttpMethod.PUT, entity, String.class);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: Finished updating permission in External Auth system {} and response: {} ", updateRoleFunction, updatePermResponse.getStatusCode().value());
			} catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
				throw e;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionInExternalSystem: Failed to update function in external central auth system",e);
				throw e;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteCentralRoleFunction(String code, EPApp app) {
		boolean deleteFunctionResponse = false;
		try {
			final Map<String, String> params = new HashMap<>();
			params.put(FUNCTION_CODE_PARAMS, code);
			params.put(APP_ID, String.valueOf(app.getId()));
			List<CentralV2RoleFunction> domainCentralRoleFunction = dataAccessService
					.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, params, null);
			CentralV2RoleFunction appFunctionCode = appFunctionListFilter(code, domainCentralRoleFunction);
			if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
				deleteRoleFunctionInExternalSystem(appFunctionCode, app);
				// Delete role function dependency records
				deleteAppRoleFunctions(appFunctionCode.getCode(), app);
			}
			dataAccessService.deleteDomainObject(appFunctionCode, null);
			deleteFunctionResponse = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteCentralRoleFunction: failed", e);
		}
		return deleteFunctionResponse;
	}

	/**
	 * It deletes app function record in portal 
	 * 
	 * @param code
	 * @param app
	 */
	private void deleteAppRoleFunctions(String code, EPApp app) {
		dataAccessService.deleteDomainObjects(EPAppRoleFunction.class,
				APP_ID_EQUALS + app.getId() + AND_FUNCTION_CD_EQUALS + code + "'", null);
	}
	
	/**
	 * 
	 * It deletes permission in the external auth system  
	 * 
	 * @param domainCentralRoleFunction
	 * @param app
	 * @throws Exception
	 */
	private void deleteRoleFunctionInExternalSystem(CentralV2RoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ExternalAccessPerms extPerms = new ExternalAccessPerms();
			String instanceValue = EcompPortalUtils.getFunctionCode(domainCentralRoleFunction.getCode());
			String checkType = getFunctionType(domainCentralRoleFunction.getCode());
			String actionValue = getFunctionAction(domainCentralRoleFunction.getCode());
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			extPerms.setAction(actionValue);
			extPerms.setInstance(instanceValue);
			extPerms.setType(app.getNameSpace() + "." + checkType);
			extPerms.setDescription(domainCentralRoleFunction.getName());
			String deleteRoleFunction = mapper.writeValueAsString(extPerms);
			HttpEntity<String> entity = new HttpEntity<>(deleteRoleFunction, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleFunctionInExternalSystem: {} for DELETE: {} ",
					CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, deleteRoleFunction);
			ResponseEntity<String> delPermResponse = template
					.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "perm?force=true", HttpMethod.DELETE, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger,
					"deleteRoleFunctionInExternalSystem: Finished deleting permission in External Auth system {} and status code: {} ",
					deleteRoleFunction, delPermResponse.getStatusCode().value());
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to delete functions in External System", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						" deleteRoleFunctionInExternalSystem: It seems like function is already deleted in external central auth system  but exists in local DB",
						e.getMessage());
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunctionInExternalSystem: Failed to delete functions in External System", e);
			}
		}
	}

	@Override
	public ExternalRequestFieldsValidator saveRoleForApplication(Role saveRole, String uebkey) throws Exception {
		boolean response = false;
		String message = "";
		try {
			EPApp app = getApp(uebkey).get(0);
			addRoleInEcompDB(saveRole, app);
			response = true;
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleForApplication failed", e);
		}
		return new ExternalRequestFieldsValidator(response,message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteRoleForApplication(String deleteRole, String uebkey) throws Exception {
		Session localSession = sessionFactory.openSession();
		Transaction transaction = null;
		boolean result = false;
		try {
			List<EPRole> epRoleList = null;
			EPApp app = getApp(uebkey).get(0);
			final Map<String, String> deleteRoleParams = new HashMap<>();
			deleteRoleParams.put(APP_ROLE_NAME_PARAM, deleteRole);
			if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				epRoleList = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, deleteRoleParams, null);
			} else {
				deleteRoleParams.put(APP_ID, String.valueOf(app.getId()));
				epRoleList = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, deleteRoleParams, null);
			}
			if (!epRoleList.isEmpty()) {
				transaction = localSession.beginTransaction();
				// Delete app role functions before deleting role
				deleteRoleFunction(app, epRoleList);
				if (app.getId() == 1) {
					// Delete fn_user_ role
					dataAccessService.deleteDomainObjects(EPUserApp.class,
							APP_ID_EQUALS + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);
					boolean isPortalRequest = false;
					deleteRoleDependencyRecords(localSession, epRoleList.get(0).getId(), app.getId(), isPortalRequest);
				}
				deleteRoleInExternalAuthSystem(epRoleList, app);
				transaction.commit();
				logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleForApplication: committed the transaction");
				dataAccessService.deleteDomainObject(epRoleList.get(0), null);
			}
			result = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleForApplication: failed", e);
			result = false;
		} finally {
			localSession.close();
		}
		return result;
	}
	
	/**
	 * 
	 * It deletes role for application in external auth system 
	 * 
	 * @param epRoleList contains role information
	 * @param app contains application information
	 * @throws Exception
	 */
	private void deleteRoleInExternalAuthSystem(List<EPRole> epRoleList, EPApp app) throws Exception {
		ResponseEntity<String> deleteResponse;
		ResponseEntity<String> res = getNameSpaceIfExists(app);
		if (res.getStatusCode() == HttpStatus.OK) {
		// Delete Role in External System
		String deleteRoleKey = "{\"name\":\"" + app.getNameSpace() + "." + epRoleList.get(0).getName()
				.replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_") + "\"}";
		deleteResponse = deleteRoleInExternalSystem(deleteRoleKey);
		if (deleteResponse.getStatusCode().value() != 200 || deleteResponse.getStatusCode().value() != 404) {
			EPLogUtil.logExternalAuthAccessAlarm(logger, deleteResponse.getStatusCode());
			logger.error(EELFLoggerDelegate.errorLogger,
					"deleteRoleForApplication: Failed to delete role in external auth system! due to {} ",
					deleteResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger,
				"deleteRoleForApplication: about to commit the transaction");
		}
	}

	/**
	 * 
	 * It deletes application user role in external auth system
	 * 
	 * @param role
	 * @param app
	 * @param LoginId
	 * @throws Exception
	 */
	private void deleteUserRoleInExternalSystem(EPRole role, EPApp app, String LoginId) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		getNameSpaceIfExists(app);
		logger.debug(EELFLoggerDelegate.debugLogger,"deleteUserRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> getResponse = template
				.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole/"
								+ LoginId
								+ SystemProperties
										.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
								+ "/" + app.getNameSpace() + "." + role.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
						HttpMethod.GET, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: Finished GET user roles from External Auth system and response: {} ", getResponse.getBody());
		if (getResponse.getStatusCode().value() != 200) {
			throw new ExternalAuthSystemException(getResponse.getBody());
		}
		String res = getResponse.getBody();
		if (!res.equals(IS_EMPTY_JSON_STRING)) {
			HttpEntity<String> userRoleentity = new HttpEntity<>(headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
			ResponseEntity<String> deleteResponse = template
					.exchange(
							SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
									+ "userRole/" + LoginId
									+ SystemProperties
											.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
									+ "/" + app.getNameSpace() + "." + role.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
							HttpMethod.DELETE, userRoleentity, String.class);
			if (deleteResponse.getStatusCode().value() != 200) {
				throw new ExternalAuthSystemException("Failed to delete user role");
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: Finished deleting user role in External Auth system and status code: {} ", deleteResponse.getStatusCode().value());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentralV2Role> getActiveRoles(String uebkey) throws Exception {
		List<CentralV2Role> roleList = new ArrayList<>();
		try {
			List<EPApp> app = getApp(uebkey);
			final Map<String, Long> params = new HashMap<>();
			// check if portal
			Long appId = null;
			if (!app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
				appId = app.get(0).getId();
			}
			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion active_ynCrt = Restrictions.eq("active", Boolean.TRUE);
			Criterion appIdCrt;
			if (appId == null)
				appIdCrt = Restrictions.isNull("appId");
			else
				appIdCrt = Restrictions.eq("appId", appId);
			Criterion andCrit = Restrictions.and(active_ynCrt, appIdCrt);
			restrictionsList.add(andCrit);
			List<EPRole> epRole = (List<EPRole>) dataAccessService.getList(EPRole.class, null, restrictionsList, null);
			roleList = createCentralRoleObject(app, epRole, roleList, params);
			List<CentralV2Role> globalRoleList = getGlobalRolesOfApplication(app.get(0).getId());
			if (globalRoleList.size() > 0)
				roleList.addAll(globalRoleList);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles: failed", e);
			throw e;
		}
		return roleList;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExternalRequestFieldsValidator deleteDependencyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception {
		Session localSession = sessionFactory.openSession();
		String message = "";
		Transaction transaction = null;
		boolean response = false;
		EPApp app = null;
		try {
			transaction = localSession.beginTransaction();
			List<EPRole> epRoleList = null;
			app = getApp(uebkey).get(0);
			if(app.getId().equals(PortalConstants.PORTAL_APP_ID)){
				epRoleList = getPortalAppRoleInfo(roleId);
			} else{
				epRoleList = getPartnerAppRoleInfo(roleId, app);
			}
			if(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
				// Delete User Role in External System before deleting role
				deleteUserRoleInExternalSystem(epRoleList.get(0), app, LoginId);	
			}
			// Delete user app roles
			dataAccessService.deleteDomainObjects(EPUserApp.class,
					APP_ID_EQUALS + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);
			boolean isPortalRequest = false;
			deleteRoleDependencyRecords(localSession, epRoleList.get(0).getId(), app.getId(), isPortalRequest);
			transaction.commit();
			if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
				// Final call to delete role once all dependencies has been deleted
				deleteRoleInExternalAuthSystem(epRoleList, app);
			}
			dataAccessService.deleteDomainObjects(EPRole.class, " role_id = "+ epRoleList.get(0).getId(), null);		
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteDependencyRoleRecord: committed the transaction");
			response = true;
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord: HttpClientErrorException", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			message = e.getMessage();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord failed", e);
			EcompPortalUtils.rollbackTransaction(transaction,
					"deleteDependencyRoleRecord rollback, exception = " + e.toString());
			message = e.getMessage();
		} finally {
			localSession.close();
		}
		return new ExternalRequestFieldsValidator(response,message);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void syncRoleFunctionFromExternalAccessSystem(EPApp app) {
		try {

			// get Permissions from External Auth System
			JSONArray extPerms = getExtAuthPermissions(app);
			List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPerms);

			// get functions in DB
			final Map<String, Long> params = new HashMap<>();
			final Map<String, CentralV2RoleFunction> roleFuncMap = new HashMap<>();
			params.put(APP_ID, app.getId());
			List<CentralV2RoleFunction> appFunctions = dataAccessService.executeNamedQuery("getAllRoleFunctions", params,
					null);
			if (!appFunctions.isEmpty()) {
				for (CentralV2RoleFunction roleFunc : appFunctions) {
					roleFuncMap.put(roleFunc.getCode(), roleFunc);
				}
			}
			
			// get Roles for portal in DB
			List<EPRole> portalRoleList = getGlobalRolesOfPortal();
			final Map<String, EPRole> existingPortalRolesMap = new HashMap<>();
			for(EPRole epRole : portalRoleList){
				existingPortalRolesMap.put(epRole.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), epRole);
			}
			
			// get Roles in DB
			final Map<String, EPRole> currentRolesInDB = getCurrentRolesInDB(app);
			
			// store External Permissions with Pipe and without Pipe (just instance)
			final Map<String, ExternalAccessPermsDetail> extAccessPermsContainsPipeMap = new HashMap<>();
			final Map<String, ExternalAccessPermsDetail> extAccessPermsMap = new HashMap<>();
			for (ExternalAccessPermsDetail permsDetailInfoWithPipe : permsDetailList) {
				extAccessPermsContainsPipeMap.put(permsDetailInfoWithPipe.getInstance(), permsDetailInfoWithPipe);
				String finalFunctionCodeVal = EcompPortalUtils.getFunctionCode(permsDetailInfoWithPipe.getInstance());
				extAccessPermsMap.put(finalFunctionCodeVal, permsDetailInfoWithPipe);
			}

			// Add if new functions and app role functions were added in
			// external auth system
			for (ExternalAccessPermsDetail permsDetail : permsDetailList) {
				String code = permsDetail.getInstance();
				CentralV2RoleFunction getFunctionCodeKey = roleFuncMap.get(permsDetail.getInstance());
				List<CentralV2RoleFunction> roleFunctionList = addGetLocalFunction(app, roleFuncMap, permsDetail, code,
						getFunctionCodeKey);
				List<String> roles = permsDetail.getRoles();
				if (roles != null) {
					// Check if function has any roles and which does not exist
					// in External Auth System. If exists delete in local
					addRemoveIfFunctionsRolesIsSyncWithExternalAuth(app, currentRolesInDB, roleFunctionList, roles, existingPortalRolesMap);
				}
			}

			// Check if function does exits in External Auth System but exits in
			// local then delete function and its dependencies
			for (CentralV2RoleFunction roleFunc : appFunctions) {
				try {
					ExternalAccessPermsDetail getFunctionCodeContainsPipeKey = extAccessPermsContainsPipeMap
							.get(roleFunc.getCode());
					if (null == getFunctionCodeContainsPipeKey) {
						ExternalAccessPermsDetail getFunctionCodeKey = extAccessPermsMap.get(roleFunc.getCode());
						if (null == getFunctionCodeKey) {
							deleteAppRoleFuncDoesNotExitsInExtSystem(app, roleFunc);
						}
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"syncRoleFunctionFromExternalAccessSystem: Failed to delete function", e);

				}
			}

			logger.debug(EELFLoggerDelegate.debugLogger,
					"syncRoleFunctionFromExternalAccessSystem: Finished syncRoleFunctionFromExternalAccessSystem");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"syncRoleFunctionFromExternalAccessSystem: Failed syncRoleFunctionFromExternalAccessSystem", e);

		}
	}

	@SuppressWarnings("unchecked")
	private void addRemoveIfFunctionsRolesIsSyncWithExternalAuth(EPApp app, final Map<String, EPRole> currentRolesInDB,
			List<CentralV2RoleFunction> roleFunctionList, List<String> roles, Map<String, EPRole> existingPortalRolesMap)
			throws Exception {
		if (!roleFunctionList.isEmpty()) {
			final Map<String, String> appRoleFuncParams = new HashMap<>();
			final Map<String, LocalRole> currentAppRoleFunctionsMap = new HashMap<>();
			final Map<String, String> currentRolesInExtSystem = new HashMap<>();
			appRoleFuncParams.put("functionCd", roleFunctionList.get(0).getCode());
			appRoleFuncParams.put("appId", String.valueOf(app.getId()));
			List<LocalRole> localRoleList = dataAccessService.executeNamedQuery("getCurrentAppRoleFunctions",
					appRoleFuncParams, null);
			for (LocalRole localRole : localRoleList) {
				currentAppRoleFunctionsMap.put(localRole.getRolename().replaceAll(
						EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), localRole);
			}
			for (String addRole : roles) {
				currentRolesInExtSystem.put(addRole.substring(addRole.indexOf(FUNCTION_PIPE)+1), addRole);
			}
			for (String extAuthrole : roles) {
				String roleNameSpace = extAuthrole.substring(0, extAuthrole.indexOf(FUNCTION_PIPE));
				boolean isNameSpaceMatching = EcompPortalUtils.checkNameSpaceMatching(roleNameSpace, app.getNameSpace());
				if (isNameSpaceMatching) {
					if (!currentAppRoleFunctionsMap
							.containsKey(extAuthrole.substring(app.getNameSpace().length() + 1))) {
						EPRole localAddFuntionRole = currentRolesInDB
								.get(extAuthrole.substring(app.getNameSpace().length() + 1));
						if (localAddFuntionRole == null) {
							checkAndAddRoleInDB(app, currentRolesInDB, roleFunctionList, extAuthrole);
						} else {
							EPAppRoleFunction addAppRoleFunc = new EPAppRoleFunction();
							addAppRoleFunc.setAppId(app.getId());
							addAppRoleFunc.setCode(roleFunctionList.get(0).getCode());
							addAppRoleFunc.setRoleId(localAddFuntionRole.getId());
							dataAccessService.saveDomainObject(addAppRoleFunc, null);
						}
					}
					// This block is to save global role function if exists
				} else {
					String extAuthAppRoleName = extAuthrole.substring(extAuthrole.indexOf(FUNCTION_PIPE) + 1);
					boolean checkIfGlobalRoleExists = existingPortalRolesMap.containsKey(extAuthAppRoleName);
					if (checkIfGlobalRoleExists) {
						final Map<String, Long> params = new HashMap<>();
						EPRole role = existingPortalRolesMap.get(extAuthAppRoleName);
						EPAppRoleFunction addGlobalRoleFunctions = new EPAppRoleFunction();
						params.put("appId", app.getId());
						params.put("roleId", role.getId());
						List<EPAppRoleFunction> currentGlobalRoleFunctionsList = dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", params, null);				
						boolean checkIfRoleFunctionExists = currentGlobalRoleFunctionsList.stream().anyMatch(currentGlobalRoleFunction -> currentGlobalRoleFunction.getCode().equals(roleFunctionList.get(0).getCode()));
						if (role != null && !checkIfRoleFunctionExists) {
							addGlobalRoleFunctions.setAppId(app.getId());
							addGlobalRoleFunctions.setRoleId(role.getId());
							if (!app.getId().equals(role.getAppRoleId())) {
								addGlobalRoleFunctions.setRoleAppId((PortalConstants.PORTAL_APP_ID).toString());
							} else {
								addGlobalRoleFunctions.setRoleAppId(null);
							}
							addGlobalRoleFunctions.setCode(roleFunctionList.get(0).getCode());
							dataAccessService.saveDomainObject(addGlobalRoleFunctions, null);
						}
					}
				}
			}
			for (LocalRole localRoleDelete : localRoleList) {
				if (!currentRolesInExtSystem.containsKey(localRoleDelete.getRolename()
						.replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"))) {
					dataAccessService.deleteDomainObjects(EPAppRoleFunction.class,
							APP_ID_EQUALS + app.getId() + AND_FUNCTION_CD_EQUALS + roleFunctionList.get(0).getCode()
									+ "'" + " and role_id = " + localRoleDelete.getRoleId().longValue(),
							null);
				}
			}
		}
	}

	private void deleteAppRoleFuncDoesNotExitsInExtSystem(EPApp app, CentralV2RoleFunction roleFunc) {
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncRoleFunctionFromExternalAccessSystem: Deleting app role function {}",
				roleFunc.getCode());
		dataAccessService.deleteDomainObjects(EPAppRoleFunction.class,
				APP_ID_EQUALS + app.getId() + AND_FUNCTION_CD_EQUALS + roleFunc.getCode() +"'", null);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncRoleFunctionFromExternalAccessSystem: Deleted app role function {}",
				roleFunc.getCode());

		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncRoleFunctionFromExternalAccessSystem: Deleting app function {}",
				roleFunc.getCode());
		dataAccessService.deleteDomainObjects(CentralV2RoleFunction.class,
				APP_ID_EQUALS + app.getId() + AND_FUNCTION_CD_EQUALS + roleFunc.getCode() +"'", null);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncRoleFunctionFromExternalAccessSystem: Deleted app function {}",
				roleFunc.getCode());
	}

	private void checkAndAddRoleInDB(EPApp app, final Map<String, EPRole> currentRolesInDB,
			List<CentralV2RoleFunction> roleFunctionList, String roleList) throws Exception {
		if (!currentRolesInDB.containsKey(
				roleList.substring(app.getNameSpace().length() + 1))) {
			Role role = addRoleInDBIfDoesNotExists(app,
					roleList.substring(app.getNameSpace().length() + 1));
			addIfRoleDescriptionNotExitsInExtSystem(role, app);
			if (!roleFunctionList.isEmpty()) {
				try {
					if (!roleFunctionList.isEmpty()) {
						EPAppRoleFunction addAppRoleFunc = new EPAppRoleFunction();
						addAppRoleFunc.setAppId(app.getId());
						addAppRoleFunc.setCode(roleFunctionList.get(0).getCode());
						addAppRoleFunc.setRoleId(role.getId());
						dataAccessService.saveDomainObject(addAppRoleFunc, null);
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"syncRoleFunctionFromExternalAccessSystem: Failed to save app role function ",
							e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<CentralV2RoleFunction> addGetLocalFunction(EPApp app, final Map<String, CentralV2RoleFunction> roleFuncMap,
			ExternalAccessPermsDetail permsDetail, String code, CentralV2RoleFunction getFunctionCodeKey) {
		String finalFunctionCodeVal = addToLocalIfFunctionNotExists(app, roleFuncMap, permsDetail, code,
				getFunctionCodeKey);
		final Map<String, String> appSyncFuncsParams = new HashMap<>();
		appSyncFuncsParams.put("appId", String.valueOf(app.getId()));
		appSyncFuncsParams.put("functionCd", finalFunctionCodeVal);
		List<CentralV2RoleFunction> roleFunctionList = null;
		roleFunctionList = dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
				null);
		if (roleFunctionList.isEmpty()) {
			appSyncFuncsParams.put("functionCd", code);
			roleFunctionList = dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
					null);
		}
		return roleFunctionList;
	}

	private String addToLocalIfFunctionNotExists(EPApp app, final Map<String, CentralV2RoleFunction> roleFuncMap,
			ExternalAccessPermsDetail permsDetail, String code, CentralV2RoleFunction getFunctionCodeKey
			) {
		String finalFunctionCodeVal = "";	
		if (null == getFunctionCodeKey) {
			finalFunctionCodeVal = EcompPortalUtils.getFunctionCode(permsDetail.getInstance());
			CentralV2RoleFunction checkIfCodeStillExits = roleFuncMap.get(finalFunctionCodeVal);
			// If function does not exist in local then add!
			if (null == checkIfCodeStillExits) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"syncRoleFunctionFromExternalAccessSystem: Adding function: {} ", code);
				addFunctionInEcompDB(app, permsDetail, code);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"syncRoleFunctionFromExternalAccessSystem: Finished adding function: {} ", code);
			}
		}
		return finalFunctionCodeVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, EPRole> getCurrentRolesInDB(EPApp app) {
		final Map<String, EPRole> currentRolesInDB = new HashMap<>();
		List<EPRole> getCurrentRoleList = null;
		final Map<String, Long> appParams = new HashMap<>();
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
			getCurrentRoleList = dataAccessService.executeNamedQuery("getPortalAppRolesList", null, null);
		} else {
			appParams.put("appId", app.getId());
			getCurrentRoleList = dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null);
		}
		for (EPRole role : getCurrentRoleList) {
			currentRolesInDB.put(role.getName()
					.replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), role);
		}
		return currentRolesInDB;
	}

	private List<ExternalAccessPermsDetail> getExtAuthPerrmissonList(EPApp app, JSONArray extPerms)
			throws IOException{
		ExternalAccessPermsDetail permDetails = null;
		List<ExternalAccessPermsDetail> permsDetailList = new ArrayList<>();
		for (int i = 0; i < extPerms.length(); i++) {
			String description = null;
			if (extPerms.getJSONObject(i).has("description")) {
				description = extPerms.getJSONObject(i).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
			} else {
				description = extPerms.getJSONObject(i).getString("instance");
			}
			if (extPerms.getJSONObject(i).has("roles")) {
				ObjectMapper rolesListMapper = new ObjectMapper();
				JSONArray resRoles = extPerms.getJSONObject(i).getJSONArray("roles");
				List<String> list = rolesListMapper.readValue(resRoles.toString(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
				permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
						extPerms.getJSONObject(i).getString("type").substring(app.getNameSpace().length() + 1)
								+ FUNCTION_PIPE + extPerms.getJSONObject(i).getString("instance") + FUNCTION_PIPE
								+ extPerms.getJSONObject(i).getString("action"),
						extPerms.getJSONObject(i).getString("action"), list, description);
				permsDetailList.add(permDetails);
			} else {
				permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
						extPerms.getJSONObject(i).getString("type").substring(app.getNameSpace().length() + 1)
								+ FUNCTION_PIPE + extPerms.getJSONObject(i).getString("instance") + FUNCTION_PIPE
								+ extPerms.getJSONObject(i).getString("action"),
						extPerms.getJSONObject(i).getString("action"), description);
				permsDetailList.add(permDetails);
			}
		}
		return permsDetailList;
	}

	private JSONArray getExtAuthPermissions(EPApp app) throws Exception {
		ResponseEntity<String> response = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: {} ",
				CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		response = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "perms/ns/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);

		String res = response.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncRoleFunctionFromExternalAccessSystem: Finished GET permissions from External Auth system and response: {} ",
				response.getBody());
		JSONObject jsonObj = new JSONObject(res);
		JSONArray extPerms = jsonObj.getJSONArray("perm");
		for (int i = 0; i < extPerms.length(); i++) {
			if (extPerms.getJSONObject(i).getString("type").equals(app.getNameSpace() + ".access")) {
				extPerms.remove(i);
				i--;
			}
		}
		return extPerms;
	}
	
	/**
	 * 
	 * Add function into local DB
	 * 
	 * @param app
	 * @param permsDetail
	 * @param code
	 */
	private void addFunctionInEcompDB(EPApp app, ExternalAccessPermsDetail permsDetail, String code) {
		try{
		CentralV2RoleFunction addFunction = new CentralV2RoleFunction();
		addFunction.setAppId(app.getId());
		addFunction.setCode(code);
		addFunction.setName(permsDetail.getDescription());
		dataAccessService.saveDomainObject(addFunction, null);
		} catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "addFunctionInEcompDB: Failed to add function", e);
		}
	}

	/**
	 * 
	 * It updates description of a role in external auth system
	 * 
	 * @param role
	 * @param app
	 * @throws Exception
	 */
	private void addIfRoleDescriptionNotExitsInExtSystem(Role role, EPApp app) throws Exception {
		String addRoleNew = updateExistingRoleInExternalSystem(role, app);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		try {
			HttpEntity<String> entity = new HttpEntity<>(addRoleNew, headers);
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
					HttpMethod.PUT, entity, String.class);
		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addIfRoleDescriptionNotExitsInExtSystem",
					e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addIfRoleDescriptionNotExitsInExtSystem: Failed",
					e);
		}
	}

	/**
	 * 
	 * While sync functions form external auth system if new role found we should add in local and return Role.class object
	 * 
	 * @param app
	 * @param role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Role addRoleInDBIfDoesNotExists(EPApp app, String role) {
		Role setNewRole = new Role();
		try {
			// functions can have new role created in External Auth System prevent
			// duplication here
			boolean isCreated = checkIfRoleExitsElseCreateInSyncFunctions(role, app);
			final Map<String, String> getRoleByNameParams = new HashMap<>();
			List<EPRole> getRoleCreated = null;
			getRoleByNameParams.put(APP_ROLE_NAME_PARAM, role);
			if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				getRoleByNameParams.put("appId", String.valueOf(app.getId()));
				List<EPRole> roleCreated = dataAccessService
						.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, getRoleByNameParams, null);
				if (!isCreated) {
					EPRole epUpdateRole = roleCreated.get(0);
					epUpdateRole.setAppRoleId(epUpdateRole.getId());
					dataAccessService.saveDomainObject(epUpdateRole, null);
					getRoleCreated = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM,
							getRoleByNameParams, null);
				} else {
					getRoleCreated = roleCreated;
				}
			} else {
				getRoleCreated = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, getRoleByNameParams,
						null);
			}
			if (getRoleCreated != null && !getRoleCreated.isEmpty()) {
				EPRole roleObject = getRoleCreated.get(0);
				setNewRole.setId(roleObject.getId());
				setNewRole.setName(roleObject.getName());
				setNewRole.setActive(roleObject.getActive());
				setNewRole.setPriority(roleObject.getPriority());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleInDBIfDoesNotExists: Failed", e);
		}
		return setNewRole;
	}

	@SuppressWarnings("unchecked")
	private boolean checkIfRoleExitsElseCreateInSyncFunctions(String role, EPApp app) {
		boolean isCreated = false;
		final Map<String, String> roleParams = new HashMap<>();
		roleParams.put(APP_ROLE_NAME_PARAM, role);
		List<EPRole> roleCreated = null;
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
			roleCreated = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams,
					null);
		} else {
			roleParams.put("appId", String.valueOf(app.getId()));
			roleCreated = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams,
					null);
		}
		if (roleCreated == null || roleCreated.isEmpty()) {
			roleParams.put("appId", String.valueOf(app.getId()));
			EPRole epRoleNew = new EPRole();
			epRoleNew.setActive(true);
			epRoleNew.setName(role);
			if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				epRoleNew.setAppId(null);
			} else {
				epRoleNew.setAppId(app.getId());
			}
			dataAccessService.saveDomainObject(epRoleNew, null);
			isCreated = false;
		} else {
			isCreated = true;
		}
		return isCreated;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer bulkUploadFunctions(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<RoleFunction> roleFuncList = dataAccessService.executeNamedQuery("getAllFunctions", null, null);
		CentralV2RoleFunction cenRoleFunc = null;
		Integer functionsAdded = 0;
		try {
			for (RoleFunction roleFunc : roleFuncList) {
				cenRoleFunc = new CentralV2RoleFunction(roleFunc.getCode(), roleFunc.getName());
				addRoleFunctionInExternalSystem(cenRoleFunc, app);
				functionsAdded++;
			}
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - bulkUploadFunctions failed", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions: failed", e.getMessage(), e);
		}
		return functionsAdded;
	}

	@Override
	public Integer bulkUploadRoles(String uebkey) throws Exception {
		List<EPApp> app = getApp(uebkey);
		List<EPRole> roles = getAppRoles(app.get(0).getId());
		List<CentralV2Role> cenRoleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		Integer rolesListAdded = 0;
		try {
			cenRoleList = createCentralRoleObject(app, roles, cenRoleList, params);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			String roleList = mapper.writeValueAsString(cenRoleList);
			List<Role> roleObjectList = mapper.readValue(roleList,
					TypeFactory.defaultInstance().constructCollectionType(List.class, Role.class));
			for (Role role : roleObjectList) {
				addRoleInExternalSystem(role, app.get(0));
				rolesListAdded++;
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles: failed", e);
			throw e;
		}
		return rolesListAdded;
	}

	/**
	 * It creating new role in external auth system while doing bulk upload
	 * 
	 * @param role
	 * @param app
	 * @throws Exception
	 */
	private void addRoleInExternalSystem(Role role, EPApp app) throws Exception {
		String addRoleNew = updateExistingRoleInExternalSystem(role, app);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		try {
			HttpEntity<String> entity = new HttpEntity<>(addRoleNew, headers);
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
					HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addRoleInExternalSystem", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleInExternalSystem: Role already exits but does not break functionality", e);
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleInExternalSystem: Failed to addRoleInExternalSystem", e.getMessage());
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer bulkUploadRolesFunctions(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<EPRole> roles = getAppRoles(app.getId());
		final Map<String, Long> params = new HashMap<>();
		Integer roleFunctions = 0;
		try {
			for (EPRole role : roles) {
				params.put("roleId", role.getId());
				List<BulkUploadRoleFunction> appRoleFunc = dataAccessService.executeNamedQuery("uploadAllRoleFunctions",
						params, null);
				if (!appRoleFunc.isEmpty()) {
					for (BulkUploadRoleFunction addRoleFunc : appRoleFunc) {
						addRoleFunctionsInExternalSystem(addRoleFunc, role, app);
						roleFunctions++;
					}
				}
			}
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to bulkUploadRolesFunctions", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRolesFunctions: failed", e);
		}
		return roleFunctions;
	}
	
	/**
	 * Its adding a role function while doing bulk upload
	 * 
	 * @param addRoleFunc
	 * @param role
	 * @param app
	 */
	private void addRoleFunctionsInExternalSystem(BulkUploadRoleFunction addRoleFunc, EPRole role, EPApp app) {
		String checkType = addRoleFunc.getFunctionCd().contains("menu") ? "menu" : "url";
		ExternalAccessRolePerms extRolePerms = null;
		ExternalAccessPerms extPerms = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();

			extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, addRoleFunc.getFunctionCd(), "*",
					addRoleFunc.getFunctionName());
			extRolePerms = new ExternalAccessRolePerms(extPerms,
					app.getNameSpace() + "." + role.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
			String updateRolePerms = mapper.writeValueAsString(extRolePerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
					HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"addRoleFunctionsInExternalSystem: RoleFunction already exits but does not break functionality", e);
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionsInExternalSystem: Failed to addRoleFunctionsInExternalSystem",
						e.getMessage());
			}
		}
	}

	@Override
	public void bulkUploadPartnerFunctions(String uebkey, List<RoleFunction> roleFunctionsList) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		CentralV2RoleFunction cenRoleFunc = null;
		for (RoleFunction roleFunction : roleFunctionsList) {
			cenRoleFunc = new CentralV2RoleFunction(roleFunction.getCode(), roleFunction.getName());
			addRoleFunctionInExternalSystem(cenRoleFunc, app);
		}
	}

	@Override
	public void bulkUploadPartnerRoles(String uebkey, List<Role> roleList) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		for (Role role : roleList) {
			addRoleInExternalSystem(role, app);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bulkUploadPartnerRoleFunctions(String uebkey, List<Role> roleList) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		for (Role role : roleList) {
			try {
				Set<RoleFunction> roleFunctionList = role.getRoleFunctions();
				List<RoleFunction> roleFunctionListNew = new ArrayList<>();
				ObjectMapper roleFunctionsMapper = new ObjectMapper();
				Iterator<RoleFunction> itetaror = roleFunctionList.iterator();
				while (itetaror.hasNext()) {
					Object nextValue = itetaror.next();
					RoleFunction roleFunction = roleFunctionsMapper.convertValue(nextValue, RoleFunction.class);
					roleFunctionListNew.add(roleFunction);
				}
				List<RoleFunction> listWithoutDuplicates = roleFunctionListNew.stream().distinct()
						.collect(Collectors.toList());
				for (RoleFunction roleFunction : listWithoutDuplicates) {
					String checkType = roleFunction.getCode().contains("menu") ? "menu" : "url";
					ExternalAccessRolePerms extRolePerms = null;
					ExternalAccessPerms extPerms = null;
					ObjectMapper mapper = new ObjectMapper();
					extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, roleFunction.getCode(),
							"*");
					extRolePerms = new ExternalAccessRolePerms(extPerms,
							app.getNameSpace() + "." + role.getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
					String updateRolePerms = mapper.writeValueAsString(extRolePerms);
					HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
					template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "role/perm", HttpMethod.PUT, entity, String.class);
				}
			} catch (Exception e) {
				if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"bulkUploadPartnerRoleFunctions: RoleFunction already exits but does not break functionality");
				} else {
					logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerRoleFunctions: Failed to addRoleFunctionsInExternalSystem",
							e);
				}
			}

		}
	}

	@Override
	@Transactional
	public void syncApplicationRolesWithEcompDB(EPApp app) {
		try {
			logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Started");
			//Sync functions and roles assigned to it which also creates new roles if does not exits in portal
			syncRoleFunctionFromExternalAccessSystem(app);
			logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Finished");	
			
			ObjectMapper mapper = new ObjectMapper();
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering to getAppRolesJSONFromExtAuthSystem");
			// Get Permissions from External Auth System
			JSONArray extRole = getAppRolesJSONFromExtAuthSystem(app);
			
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into getExternalRoleDetailsList");
			List<ExternalRoleDetails> externalRoleDetailsList = getExternalRoleDetailsList(app,
					mapper, extRole);
			
			List<EPRole> finalRoleList = new ArrayList<>();
			for (ExternalRoleDetails externalRole : externalRoleDetailsList) {
				EPRole ecompRole = convertExternalRoleDetailstoEpRole(externalRole);
				finalRoleList.add(ecompRole);
			}

			List<EPRole> applicationRolesList;
			applicationRolesList = getAppRoles(app.getId());
			List<String> applicationRoleIdList = new ArrayList<>();
			for (EPRole applicationRole : applicationRolesList) {
				applicationRoleIdList.add(applicationRole.getName());
			}

			List<EPRole> roleListToBeAddInEcompDB = new ArrayList<>();
			for (EPRole aafRole : finalRoleList) {
				if (!applicationRoleIdList.contains(aafRole.getName())) {
					roleListToBeAddInEcompDB.add(aafRole);
				}
			}

			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into inactiveRolesNotInExternalAuthSystem");
			// Check if roles exits in external Access system and if not make inactive in DB
			inactiveRolesNotInExternalAuthSystem(app, finalRoleList, applicationRolesList);

			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into checkAndUpdateRoleInDB");
			// It checks properties in the external auth system app role description and updates role in local
			checkAndUpdateRoleInDB(app, finalRoleList);

			logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addNewRoleInEcompDBUpdateDescInExtAuthSystem");
			// Add new roles in DB and updates role description in External Auth System 
			addNewRoleInEcompDBUpdateDescInExtAuthSystem(app, roleListToBeAddInEcompDB);
			logger.debug(EELFLoggerDelegate.debugLogger, "syncApplicationRolesWithEcompDB: Finished");
		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "syncApplicationRolesWithEcompDB: Failed due to the External Auth System", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "syncApplicationRolesWithEcompDB: Failed ", e);
		}
	}

	/**
	 * 
	 * It adds new roles in DB and updates description in External Auth System
	 * 
	 * @param app
	 * @param roleListToBeAddInEcompDB
	 */
	@SuppressWarnings("unchecked")
	private void addNewRoleInEcompDBUpdateDescInExtAuthSystem(EPApp app, List<EPRole> roleListToBeAddInEcompDB) {
		EPRole roleToBeAddedInEcompDB;
		for (int i = 0; i < roleListToBeAddInEcompDB.size(); i++) {
			try {
				roleToBeAddedInEcompDB = roleListToBeAddInEcompDB.get(i);
				if (app.getId() == 1) {
					roleToBeAddedInEcompDB.setAppRoleId(null);
				}
				dataAccessService.saveDomainObject(roleToBeAddedInEcompDB, null);
				List<EPRole> getRoleCreatedInSync = null;
				if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					final Map<String, String> globalRoleParams = new HashMap<>();
					globalRoleParams.put("appId", String.valueOf(app.getId()));
					globalRoleParams.put("appRoleName", roleToBeAddedInEcompDB.getName());
					getRoleCreatedInSync = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, globalRoleParams, null);
					EPRole epUpdateRole = getRoleCreatedInSync.get(0);
					epUpdateRole.setAppRoleId(epUpdateRole.getId());
					dataAccessService.saveDomainObject(epUpdateRole, null);
				}
				List<EPRole> roleList = new ArrayList<>();
				final Map<String, String> params = new HashMap<>();

				params.put(APP_ROLE_NAME_PARAM, roleToBeAddedInEcompDB.getName());
				boolean isPortalRole = false;
				if (app.getId() == 1) {
					isPortalRole = true;
					roleList = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, params, null);
				} else {
					isPortalRole = false;
					params.put(APP_ID, app.getId().toString());
					roleList = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, params, null);
				}
				EPRole role = roleList.get(0);
				Role aaFrole = new Role();
				aaFrole.setId(role.getId());
				aaFrole.setActive(role.getActive());
				aaFrole.setPriority(role.getPriority());
				aaFrole.setName(role.getName());
				updateRoleInExternalSystem(aaFrole, app, isPortalRole);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"SyncApplicationRolesWithEcompDB: Failed to add or update role in external auth system", e);
			}
		}
	}

	/**
	 * 
	 * It checks description in External Auth System if found any changes updates in DB
	 * 
	 * @param app
	 * @param finalRoleList contains list of External Auth System roles list which is converted to EPRole
	 */
	@SuppressWarnings("unchecked")
	private void checkAndUpdateRoleInDB(EPApp app, List<EPRole> finalRoleList) {
		for (EPRole roleItem : finalRoleList) {
			final Map<String, String> roleParams = new HashMap<>();
			List<EPRole> currentList = null;
			roleParams.put(APP_ROLE_NAME_PARAM, roleItem.getName());
			if (app.getId() == 1) {
				currentList = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, roleParams, null);
			} else {
				roleParams.put(APP_ID, app.getId().toString());
				currentList = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams, null);
			}

			if (!currentList.isEmpty()) {
				try {
					Boolean aafRoleActive;
					Boolean localRoleActive;
					boolean result;
					aafRoleActive = Boolean.valueOf(roleItem.getActive());
					localRoleActive = Boolean.valueOf(currentList.get(0).getActive());
					result = aafRoleActive.equals(localRoleActive);
					EPRole updateRole = currentList.get(0);

					if (!result) {
						updateRole.setActive(roleItem.getActive());
						dataAccessService.saveDomainObject(updateRole, null);
					}
					if (roleItem.getPriority() != null
							&& !currentList.get(0).getPriority().equals(roleItem.getPriority())) {
						updateRole.setPriority(roleItem.getPriority());
						dataAccessService.saveDomainObject(updateRole, null);
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"syncApplicationRolesWithEcompDB: Failed to update role ", e);
				}
			}
		}
	}
	/**
	 * 
	 * It de-activates application roles in DB if not present in External Auth system  
	 * 
	 * @param app
	 * @param finalRoleList contains list of current roles present in External Auth System
	 * @param applicationRolesList contains list of current roles present in DB
	 */
	@SuppressWarnings("unchecked")
	private void inactiveRolesNotInExternalAuthSystem(EPApp app, List<EPRole> finalRoleList,
			List<EPRole> applicationRolesList) {
		final Map<String, EPRole> checkRolesInactive = new HashMap<>();
		for (EPRole extrole : finalRoleList) {
			checkRolesInactive.put(extrole.getName(), extrole);
		}
		for (EPRole role : applicationRolesList) {
			try {
				final Map<String, String> extRoleParams = new HashMap<>();
				List<EPRole> roleList = null;
				extRoleParams.put(APP_ROLE_NAME_PARAM, role.getName());
				if (!checkRolesInactive.containsKey(role.getName())) {
					if (app.getId() == 1) {
						roleList = dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, extRoleParams, null);
					} else {
						extRoleParams.put(APP_ID, app.getId().toString());
						roleList = dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, extRoleParams, null);
					}
					EPRole updateRoleInactive = roleList.get(0);
					updateRoleInactive.setActive(false);
					dataAccessService.saveDomainObject(updateRoleInactive, null);
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"syncApplicationRolesWithEcompDB: Failed to de-activate role ", e);
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ExternalRoleDetails> getExternalRoleDetailsList(EPApp app,
			ObjectMapper mapper, JSONArray extRole)
			throws IOException {
		List<ExternalRoleDetails> externalRoleDetailsList = new ArrayList<>();
		ExternalRoleDescription ApplicationRole = new ExternalRoleDescription();
		ExternalAccessPerms externalAccessPerms = new ExternalAccessPerms(); 
		List<String> functionCodelist = new ArrayList<>();
		for (int i = 0; i < extRole.length(); i++) {
			ExternalRoleDetails externalRoleDetail = new ExternalRoleDetails();
			EPAppRoleFunction ePAppRoleFunction = new EPAppRoleFunction();
			JSONObject Role = (JSONObject) extRole.get(i);
			if (!extRole.getJSONObject(i).has(EXTERNAL_AUTH_ROLE_DESCRIPTION)) {
				ApplicationRole.setActive("true");
				ApplicationRole.setAppId(IS_NULL_STRING);
				ApplicationRole.setPriority(IS_NULL_STRING);
				ApplicationRole.setAppRoleId(IS_NULL_STRING);
				String roleName = extRole.getJSONObject(i).getString(ROLE_NAME);
				ApplicationRole.setName(roleName.substring(app.getNameSpace().length() + 1));
			} else {
				String desc = extRole.getJSONObject(i).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
				ApplicationRole = mapper.readValue(desc, ExternalRoleDescription.class);
			}

			SortedSet<ExternalAccessPerms> externalAccessPermsOfRole = new TreeSet<>();
			if (extRole.getJSONObject(i).has(EXTERNAL_AUTH_PERMS)) {
				JSONArray extPerm = (JSONArray) Role.get(EXTERNAL_AUTH_PERMS);
				for (int j = 0; j < extPerm.length(); j++) {
					JSONObject perms = extPerm.getJSONObject(j);
					boolean isNamespaceMatching = EcompPortalUtils.checkNameSpaceMatching(perms.getString("type"), app.getNameSpace());
					if (isNamespaceMatching) {
						externalAccessPerms = new ExternalAccessPerms(perms.getString("type"),
								perms.getString("instance"), perms.getString("action"));
						ePAppRoleFunction.setCode(externalAccessPerms.getInstance());
						functionCodelist.add(ePAppRoleFunction.getCode());
						externalAccessPermsOfRole.add(externalAccessPerms);
					}

				}
			}

			if (ApplicationRole.getActive().equals(IS_NULL_STRING)) {
				externalRoleDetail.setActive(false);
			} else {
				externalRoleDetail.setActive(Boolean.parseBoolean(ApplicationRole.getActive()));
			}
			externalRoleDetail.setName(ApplicationRole.getName());

			if (ApplicationRole.getAppId().equals(IS_NULL_STRING) && app.getId() == 1) {
				externalRoleDetail.setAppId(null);
			} else if (ApplicationRole.getAppId().equals(IS_NULL_STRING)) {
				externalRoleDetail.setAppId(app.getId());
			} else {
				externalRoleDetail.setAppId(Long.parseLong(ApplicationRole.getAppId()));
			}

			if (ApplicationRole.getPriority().equals(IS_NULL_STRING)) {
				externalRoleDetail.setPriority(null);
			} else {
				externalRoleDetail.setPriority(Integer.parseInt(ApplicationRole.getPriority()));
			}

			if (ApplicationRole.getAppRoleId().equals(IS_NULL_STRING) && app.getId() == 1) {
				externalRoleDetail.setAppRoleId(null);
			}

			// get role functions from DB
			final Map<String, EPAppRoleFunction> roleFunctionsMap = new HashMap<>();
			if (!ApplicationRole.getId().equals(IS_NULL_STRING)) {
				final Map<String, Long> appRoleFuncsParams = new  HashMap<>();
				appRoleFuncsParams.put("appId", app.getId());
				appRoleFuncsParams.put("roleId", Long.valueOf(ApplicationRole.getId()));
				List<EPAppRoleFunction> appRoleFunctions = dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null);
				if (!appRoleFunctions.isEmpty()) {
					for (EPAppRoleFunction roleFunc : appRoleFunctions) {
						roleFunctionsMap.put(roleFunc.getCode(), roleFunc);
					}
				}
			}

			if (!externalAccessPermsOfRole.isEmpty()) {
				// Adding functions to role
				for (ExternalAccessPerms externalpermission : externalAccessPermsOfRole) {
					EPAppRoleFunction checkRoleFunctionExits = roleFunctionsMap.get(externalpermission.getInstance());
					if (checkRoleFunctionExits == null) {
						String funcCode = externalpermission.getType().substring(app.getNameSpace().length() + 1)
								+ FUNCTION_PIPE + externalAccessPerms.getInstance() + FUNCTION_PIPE
								+ externalAccessPerms.getAction();
						EPAppRoleFunction checkRoleFunctionPipeExits = roleFunctionsMap.get(funcCode);
						if (checkRoleFunctionPipeExits == null) {
							try {
								final Map<String, String> appFuncsParams = new  HashMap<>();
								appFuncsParams.put("appId", String.valueOf(app.getId()));
								appFuncsParams.put("functionCd", externalpermission.getInstance());
								logger.debug(EELFLoggerDelegate.debugLogger,
										"SyncApplicationRolesWithEcompDB: Adding function to the role: {}",
										externalpermission.getInstance());
								List<CentralV2RoleFunction> roleFunction = null;
								roleFunction = dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null);
								if (roleFunction.isEmpty()) {
									appFuncsParams.put("functionCd", funcCode);
									roleFunction = dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null);
								}
								if (!roleFunction.isEmpty()) {
									EPAppRoleFunction apRoleFunction = new EPAppRoleFunction();
									apRoleFunction.setAppId(app.getId());
									apRoleFunction.setRoleId(Long.parseLong(ApplicationRole.getId()));
									apRoleFunction.setCode(roleFunction.get(0).getCode());
									dataAccessService.saveDomainObject(apRoleFunction, null);
								}
							} catch (Exception e) {
								logger.error(EELFLoggerDelegate.errorLogger,
										"SyncApplicationRolesWithEcompDB: Failed to add role function", e);
							}
						}
					}
				}
			}
			externalRoleDetailsList.add(externalRoleDetail);
		}
		return externalRoleDetailsList;
	}

	@Override
	public JSONArray getAppRolesJSONFromExtAuthSystem(EPApp app) throws Exception {
		ResponseEntity<String> response = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "syncApplicationRolesWithEcompDB: {} ",
				CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		response = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "roles/ns/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);
		String res = response.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncApplicationRolesWithEcompDB: Finished GET roles from External Auth system and the result is :",
				res);
		JSONObject jsonObj = new JSONObject(res);
		JSONArray extRole = jsonObj.getJSONArray("role");
		for (int i = 0; i < extRole.length(); i++) {
			if (extRole.getJSONObject(i).getString(ROLE_NAME).equals(app.getNameSpace() + ADMIN)
					|| extRole.getJSONObject(i).getString(ROLE_NAME).equals(app.getNameSpace() + OWNER)
					|| (extRole.getJSONObject(i).getString(ROLE_NAME).equals(app.getNameSpace() + ACCOUNT_ADMINISTRATOR)
							&& !app.getId().equals(PortalConstants.PORTAL_APP_ID))) {
				extRole.remove(i);
				i--;
			}			
		}
		return extRole;
	}
	
	@Override
	public JSONArray getAllUsersByRole(String roleName) throws Exception{
		ResponseEntity<String> response = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "getAllUsersByRole: {} ",
				CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		response = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "userRoles/role/" + roleName, HttpMethod.GET, entity, String.class);
		String res = response.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger,
				"syncApplicationRolesWithEcompDB: Finished GET roles from External Auth system and the result is :",
				res);
		JSONObject jsonObj = new JSONObject(res);
		JSONArray extRole = jsonObj.getJSONArray("userRole");
		
		return extRole;
	}

	/**
	 * 
	 * It converts from ExternalRoleDetails.class object to EPRole.class object
	 * 
	 * @param externalRoleDetails
	 * @return EPRole object
	 */
	private EPRole convertExternalRoleDetailstoEpRole(ExternalRoleDetails externalRoleDetails) {
		EPRole role = new EPRole();
		role.setActive(externalRoleDetails.isActive());
		role.setAppId(externalRoleDetails.getAppId());
		role.setAppRoleId(externalRoleDetails.getAppRoleId());
		role.setName(externalRoleDetails.getName());
		role.setPriority(externalRoleDetails.getPriority());
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer bulkUploadUserRoles(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		final Map<String, String> params = new HashMap<>();
		params.put("uebKey", app.getUebKey());
		List<BulkUploadUserRoles> userRolesList = null;
		Integer userRolesAdded = 0;
		if (app.getCentralAuth()) {
			userRolesList = dataAccessService.executeNamedQuery("getBulkUserRoles", params, null);
			for (BulkUploadUserRoles userRolesUpload : userRolesList) {
				if(!userRolesUpload.getOrgUserId().equals("su1234")){
					addUserRoleInExternalSystem(userRolesUpload);
					userRolesAdded++;
				}
			}
		}
		return userRolesAdded;
	}

	/**
	 * Its adding a user role in external auth system while doing bulk upload 
	 * 
	 * @param userRolesUpload
	 */
	private void addUserRoleInExternalSystem(BulkUploadUserRoles userRolesUpload) {
		try {
			String name = "";
			ObjectMapper mapper = new ObjectMapper();
			if (EPCommonSystemProperties
					.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
				name = userRolesUpload.getOrgUserId()
						+ SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
			}
			ExternalAccessUser extUser = new ExternalAccessUser(name,
					userRolesUpload.getAppNameSpace() + "." + userRolesUpload.getRoleName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
			String userRole = mapper.writeValueAsString(extUser);
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			HttpEntity<String> entity = new HttpEntity<>(userRole, headers);
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole",
					HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addUserRoleInExternalSystem", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.error(EELFLoggerDelegate.errorLogger, "addUserRoleInExternalSystem: UserRole already exits but does not break functionality");
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "addUserRoleInExternalSystem: Failed to addUserRoleInExternalSystem", e);
			}
		}
	}

	@Override
	public void deleteRoleDependencyRecords(Session localSession, Long roleId, Long appId, boolean isPortalRequest) throws Exception {
		try {
			String sql = ""; 
			Query query = null;
			
			//It should delete only when it portal's roleId
			if(appId.equals(PortalConstants.PORTAL_APP_ID)){
			// Delete from fn_role_function
			sql = "DELETE FROM fn_role_function WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			// Delete from fn_role_composite
			sql = "DELETE FROM fn_role_composite WHERE parent_role_id=" + roleId + " OR child_role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			}
			
			// Delete from ep_app_role_function
			sql = "DELETE FROM ep_app_role_function WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Delete from ep_role_notification
			sql = "DELETE FROM ep_role_notification WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			// Delete from fn_user_pseudo_role
			sql = "DELETE FROM fn_user_pseudo_role WHERE pseudo_role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Delete form EP_WIDGET_CATALOG_ROLE
			sql = "DELETE FROM EP_WIDGET_CATALOG_ROLE WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Delete form EP_WIDGET_CATALOG_ROLE
			sql = "DELETE FROM ep_user_roles_request_det WHERE requested_role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			if(!isPortalRequest) {
				// Delete form fn_menu_functional_roles
				sql = "DELETE FROM fn_menu_functional_roles WHERE role_id=" + roleId;
				logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
				query = localSession.createSQLQuery(sql);
				query.executeUpdate();	
			}
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleDependeciesRecord: failed ", e);
			throw new DeleteDomainObjectFailedException("delete Failed" + e.getMessage());
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMenuFunctionsList(String uebkey) throws Exception {
		List<String> appMenuFunctionsList = null;
		try {
			EPApp app = getApp(uebkey).get(0);
			final Map<String, Long> appParams = new HashMap<>();
			appParams.put(APP_ID, app.getId());
			appMenuFunctionsList = dataAccessService.executeNamedQuery("getMenuFunctions", appParams, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuFunctionsList: Failed", e);
			return appMenuFunctionsList;
		}
		return appMenuFunctionsList;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<EcompUser> getAllAppUsers(String uebkey) throws Exception {
		List<String> usersList = new ArrayList<>();
		List<EcompUser> usersfinalList = new ArrayList<>();
		try {
			EPApp app = getApp(uebkey).get(0);
			final Map<String, Long> appParams = new HashMap<>();
			appParams.put("appId", app.getId());
			List<EcompUserRoles> userList = (List<EcompUserRoles>) dataAccessService
					.executeNamedQuery("ApplicationUserRoles", appParams, null);
			for (EcompUserRoles ecompUserRole : userList) {
				boolean found = false;
				Set<EcompRole> roles = null;
				for (EcompUser user : usersfinalList) {
					if (user.getOrgUserId().equals(ecompUserRole.getOrgUserId())) {
						EcompRole ecompRole = new EcompRole();
						ecompRole.setId(ecompUserRole.getRoleId());
						ecompRole.setName(ecompUserRole.getRoleName());
						roles = user.getRoles();
						roles.add(ecompRole);
						user.setRoles(roles);
						found = true;
						break;
					}
				}

				if (!found) {
					EcompUser epUser = new EcompUser();
					epUser.setOrgId(ecompUserRole.getOrgId());
					epUser.setManagerId(ecompUserRole.getManagerId());
					epUser.setFirstName(ecompUserRole.getFirstName());
					epUser.setLastName(ecompUserRole.getLastName());
					epUser.setPhone(ecompUserRole.getPhone());
					epUser.setEmail(ecompUserRole.getEmail());
					epUser.setOrgUserId(ecompUserRole.getOrgUserId());
					epUser.setOrgCode(ecompUserRole.getOrgCode());
					epUser.setOrgManagerUserId(ecompUserRole.getOrgManagerUserId());
					epUser.setJobTitle(ecompUserRole.getJobTitle());
					epUser.setLoginId(ecompUserRole.getLoginId());
					epUser.setActive(true);
					roles = new HashSet<>();
					EcompRole ecompRole = new EcompRole();
					ecompRole.setId(ecompUserRole.getRoleId());
					ecompRole.setName(ecompUserRole.getRoleName());
					roles.add(ecompRole);
					epUser.setRoles(roles);
					usersfinalList.add(epUser);
				}
			}
			ObjectMapper mapper = new ObjectMapper();

			for (EcompUser u1 : usersfinalList) {
				String str = mapper.writeValueAsString(u1);
				usersList.add(str);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAllUsers failed", e);
			throw e;
		}
		return usersfinalList;
	}
	

	@Override
	public Role ConvertCentralRoleToRole(String result) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Role newRole = new Role();
		try {
			newRole = mapper.readValue(result, Role.class);
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to convert the result to Role Object", e);
		}
		if (newRole.getRoleFunctions() != null) {
			@SuppressWarnings("unchecked")
			Set<RoleFunction> roleFunctionList = newRole.getRoleFunctions();
			Set<RoleFunction> roleFunctionListNew = new HashSet<>();
			Iterator<RoleFunction> itetaror = roleFunctionList.iterator();
			while (itetaror.hasNext()) {
				Object nextValue = itetaror.next();
				RoleFunction roleFun = mapper.convertValue(nextValue, RoleFunction.class);
				roleFunctionListNew.add(roleFun);
			}
			newRole.setRoleFunctions(roleFunctionListNew);
		}
		return newRole;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CentralizedApp> getCentralizedAppsOfUser(String userId) {
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		List<CentralizedApp> centralizedAppsList = new ArrayList<>();
		try{
			centralizedAppsList =  dataAccessService
					.executeNamedQuery("getCentralizedAppsOfUser", params, null);
		}catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getCentralizedAppsOfUser failed", e);
		}
		return centralizedAppsList;
	}

	@SuppressWarnings("unchecked")
	public List<CentralV2Role> getGlobalRolesOfApplication(Long appId) {
		Map<String, Long> params = new HashMap<>();
		params.put("appId", appId);
		List<GlobalRoleWithApplicationRoleFunction> globalRoles = new ArrayList<>();
		try {
			globalRoles = dataAccessService.executeNamedQuery("getGlobalRoleWithApplicationRoleFunctions", params,
					null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getCentralizedAppsOfUser failed", e);
		}
		List<CentralV2Role> rolesfinalList = new ArrayList<>();
		if (globalRoles.size() > 0)
			rolesfinalList = finalListOfCentralRoles(globalRoles);
		return rolesfinalList;
	}

	@SuppressWarnings("unchecked")
	private CentralV2Role getGlobalRoleForRequestedApp(long requestedAppId, long roleId) {
		CentralV2Role finalGlobalrole = null;
		List<GlobalRoleWithApplicationRoleFunction> roleWithApplicationRoleFucntions = new ArrayList<>();
		Map<String, Long> params = new HashMap<>();
		params.put("roleId", roleId);
		params.put("requestedAppId", requestedAppId);
		try {
			roleWithApplicationRoleFucntions = dataAccessService.executeNamedQuery("getGlobalRoleForRequestedApp",
					params, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getGlobalRoleForRequestedApp failed", e);
		}
		if (roleWithApplicationRoleFucntions.size() > 0) {
			List<CentralV2Role> rolesfinalList = finalListOfCentralRoles(roleWithApplicationRoleFucntions);
			finalGlobalrole = rolesfinalList.get(0);
		} else {
			List<EPRole> roleList = getPortalAppRoleInfo(roleId);
			finalGlobalrole = convertRoleToCentralV2Role(roleList.get(0));
		}
		return finalGlobalrole;
	}

	private List<CentralV2Role> finalListOfCentralRoles(List<GlobalRoleWithApplicationRoleFunction> globalRoles) {
		List<CentralV2Role> rolesfinalList = new ArrayList<>();
		for (GlobalRoleWithApplicationRoleFunction role : globalRoles) {
			boolean found = false;
			for (CentralV2Role cenRole : rolesfinalList) {
				if (role.getRoleId().equals(cenRole.getId())) {
					SortedSet<CentralV2RoleFunction> roleFunctions = cenRole.getRoleFunctions();
					CentralV2RoleFunction cenRoleFun = createCentralRoleFunctionForGlobalRole(role);
					roleFunctions.add(cenRoleFun);
					cenRole.setRoleFunctions(roleFunctions);
					found = true;
					break;
				}
			}
			if (!found) {
				CentralV2Role cenrole = new CentralV2Role();
				cenrole.setName(role.getRoleName());
				cenrole.setId(role.getRoleId());
				cenrole.setActive(role.isActive());
				cenrole.setPriority(role.getPriority());
				SortedSet<CentralV2RoleFunction> roleFunctions = new TreeSet<>();
				CentralV2RoleFunction cenRoleFun = createCentralRoleFunctionForGlobalRole(role);
				roleFunctions.add(cenRoleFun);
				cenrole.setRoleFunctions(roleFunctions);
				rolesfinalList.add(cenrole);
			}
		}
		return rolesfinalList;
	}

	private CentralV2RoleFunction createCentralRoleFunctionForGlobalRole(GlobalRoleWithApplicationRoleFunction role) {
		String instance;
		String type;
		String action;
		CentralV2RoleFunction cenRoleFun;
		if(role.getFunctionCd().contains(FUNCTION_PIPE)){
			instance = EcompPortalUtils.getFunctionCode(role.getFunctionCd());
			type = EcompPortalUtils.getFunctionType(role.getFunctionCd());
			action = EcompPortalUtils.getFunctionAction(role.getFunctionCd());
			cenRoleFun = new CentralV2RoleFunction(null, instance, role.getFunctionName(), null, type, action, null);
		} else{
			type = getFunctionType(role.getFunctionCd());
			action = getFunctionAction(role.getFunctionCd());
			cenRoleFun = new CentralV2RoleFunction(null, role.getFunctionCd(), role.getFunctionName(), null, type, action, null);
		}
		return cenRoleFun;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPRole> getGlobalRolesOfPortal() {
		List<EPRole> globalRoles = new ArrayList<>();
		try {
			globalRoles = dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getGlobalRolesOfPortal failed", e);
		}
		return globalRoles;
	}

	private CentralV2Role convertRoleToCentralV2Role(EPRole role) {
	 return new CentralV2Role(role.getId(), role.getCreated(), role.getModified(), role.getCreatedId(),
				role.getModifiedId(), role.getRowNum(), role.getName(), role.getActive(), role.getPriority(),
				new TreeSet<>(), new TreeSet<>(), new TreeSet<>());
		
	}
	
	@Override
	public List<RoleFunction> convertCentralRoleFunctionToRoleFunctionObject(List<CentralV2RoleFunction> answer) {
		List<RoleFunction> addRoleFuncList = new ArrayList<>();
		for(CentralV2RoleFunction cenRoleFunc : answer){
			RoleFunction setRoleFunc = new RoleFunction();
			setRoleFunc.setCode(cenRoleFunc.getCode());
			setRoleFunc.setName(cenRoleFunc.getName());
			addRoleFuncList.add(setRoleFunc);
		}		
		return addRoleFuncList;
	}

	@Override
	public CentralUser getUserRoles(String loginId, String uebkey) throws Exception {
		CentralUser sendUserRoles = null;

		try {
			CentralV2User cenV2User = getV2UserAppRoles(loginId, uebkey);
			sendUserRoles = convertV2UserRolesToOlderVersion(cenV2User);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserRoles: failed", e);
		}
		return sendUserRoles;
	}

	/**
	 * 
	 * It returns V2 CentralUser object if user has any roles and permissions
	 * 
	 * @param loginId
	 * @param uebkey
	 * @return CentralUser object
	 * @throws Exception
	 */
	private CentralV2User getV2UserAppRoles(String loginId, String uebkey) throws Exception {
		EPApp app;
		List<EPUser> epUserList;
		List<EPApp> appList = getApp(uebkey);
		app = appList.get(0);
		epUserList = getUser(loginId);
		EPUser user = epUserList.get(0);
		Set<EPUserApp> userAppSet = user.getEPUserApps();
		return createEPUser(user, userAppSet, app);
	}

	/**
	 * It converts V2 CentralUser object to old version CentralUser object
	 * 
	 * @param cenV2User
	 * @return EPUser object
	 */
	private CentralUser convertV2UserRolesToOlderVersion(CentralV2User cenV2User) {
			Set<CentralV2UserApp> userV2Apps = cenV2User.getUserApps();
			Set<CentralUserApp> userApps = new TreeSet<>();
			for(CentralV2UserApp userApp : userV2Apps){				
				CentralApp app  = userApp.getApp();
				CentralUserApp cua = new CentralUserApp();
				cua.setUserId(null);
				cua.setApp(app);
				SortedSet<CentralRoleFunction> cenRoleFunction = new TreeSet<>();
				for(CentralV2RoleFunction  cenV2RoleFunc : userApp.getRole().getRoleFunctions() ){					
					CentralRoleFunction cenRoleFunc = new CentralRoleFunction(cenV2RoleFunc.getCode(), cenV2RoleFunc.getName());								
					cenRoleFunction.add(cenRoleFunc);
				}
				CentralRole role = new CentralRole(userApp.getRole().getId(), userApp.getRole().getName(), userApp.getRole().isActive(), userApp.getRole().getPriority(),
						cenRoleFunction);
				cua.setRole(role);
				userApps.add(cua);
			}
			return new CentralUser(cenV2User.getId(), cenV2User.getCreated(), cenV2User.getModified(), 
					cenV2User.getCreatedId(),cenV2User.getModifiedId(), 
					cenV2User.getRowNum(), cenV2User.getOrgId(), cenV2User.getManagerId(), cenV2User.getFirstName(), 
					cenV2User.getMiddleInitial(), cenV2User.getLastName(), cenV2User.getPhone(), cenV2User.getFax(), 
					cenV2User.getCellular(),cenV2User.getEmail(),cenV2User.getAddressId(),cenV2User.getAlertMethodCd(),
					cenV2User.getHrid(),cenV2User.getOrgUserId(),cenV2User.getOrgCode(),cenV2User.getAddress1(), 
					cenV2User.getAddress2(),cenV2User.getCity(),cenV2User.getState(),cenV2User.getZipCode(),cenV2User.getCountry(), 
					cenV2User.getOrgManagerUserId(),cenV2User.getLocationClli(),cenV2User.getBusinessCountryCode(), 
					cenV2User.getBusinessCountryName(),cenV2User.getBusinessUnit(),cenV2User.getBusinessUnitName(), 
					cenV2User.getDepartment(),cenV2User.getDepartmentName(),cenV2User.getCompanyCode(), 
					cenV2User.getCompany(),cenV2User.getZipCodeSuffix(),cenV2User.getJobTitle(), 
					cenV2User.getCommandChain(),cenV2User.getSiloStatus(),cenV2User.getCostCenter(),
					cenV2User.getFinancialLocCode(),cenV2User.getLoginId(),cenV2User.getLoginPwd(), 
					cenV2User.getLastLoginDate(),cenV2User.isActive(),cenV2User.isInternal(),cenV2User.getSelectedProfileId(),cenV2User.getTimeZoneId(),
					cenV2User.isOnline(),cenV2User.getChatId(), 
					userApps);
	}

	@Override
	public List<CentralRole> convertV2CentralRoleListToOldVerisonCentralRoleList(List<CentralV2Role> v2CenRoleList) {
		List<CentralRole> cenRoleList = new ArrayList<>();
		SortedSet<CentralRoleFunction> cenRoleFuncList = new TreeSet<>();
			for(CentralV2Role v2CenRole : v2CenRoleList){
				for(CentralV2RoleFunction v2CenRoleFunc: v2CenRole.getRoleFunctions()){
					CentralRoleFunction roleFunc = new CentralRoleFunction(v2CenRoleFunc.getCode(), v2CenRoleFunc.getName());
					cenRoleFuncList.add(roleFunc);
				}
				CentralRole role = new CentralRole(v2CenRole.getId(), v2CenRole.getName(), v2CenRole.isActive(), v2CenRole.getPriority(), cenRoleFuncList);
				cenRoleList.add(role);
			}		
		return cenRoleList;
	}
	
	@Override
	public ResponseEntity<String> getNameSpaceIfExists(EPApp app) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists: Connecting to External Auth system");
		ResponseEntity<String> response = null;
		try {
			response = template
					.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "nss/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists: Finished ",
					response.getStatusCode().value());
		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkIfNameSpaceExists failed", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new InvalidApplicationException("Invalid NameSpace");
			else
				throw e;
		}
		return response;
	}
	
	@Override
	public CentralRole convertV2CentralRoleToOldVerisonCentralRole(CentralV2Role v2CenRole) {
		SortedSet<CentralRoleFunction> cenRoleFuncList = new TreeSet<>();
		for (CentralV2RoleFunction v2CenRoleFunc : v2CenRole.getRoleFunctions()) {
			CentralRoleFunction roleFunc = new CentralRoleFunction(v2CenRoleFunc.getCode(), v2CenRoleFunc.getName());
			cenRoleFuncList.add(roleFunc);
		}
		return new CentralRole(v2CenRole.getId(), v2CenRole.getName(), v2CenRole.isActive(), v2CenRole.getPriority(),
				cenRoleFuncList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer bulkUploadUsersSingleRole(String uebkey, Long roleId, String modifiedRoleName) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		final Map<String, String> params = new HashMap<>();
		params.put("uebKey", app.getUebKey());
		params.put("roleId", String.valueOf(roleId));
		List<BulkUploadUserRoles> userRolesList = null;
		Integer userRolesAdded = 0;
		if (app.getCentralAuth()) {
			userRolesList = dataAccessService.executeNamedQuery("getBulkUsersForSingleRole", params, null);
			for (BulkUploadUserRoles userRolesUpload : userRolesList) {
				userRolesUpload.setRoleName(modifiedRoleName);
				if(!userRolesUpload.getOrgUserId().equals("su1234")){
					addUserRoleInExternalSystem(userRolesUpload);
					userRolesAdded++;
				}
			}
		}
		return userRolesAdded;
	}	
	
	@Override
	public String encodeFunctionCode(String funCode){
		String encodedString = funCode;
		List<Pattern> encodingList = new ArrayList<>();
		encodingList.add(Pattern.compile("/"));
		encodingList.add(Pattern.compile("-"));
		for (Pattern xssInputPattern : encodingList) {
			encodedString = xssInputPattern.matcher(encodedString)
					.replaceAll("%" + Hex.encodeHexString(xssInputPattern.toString().getBytes()));
		}		
		encodedString = encodedString.replaceAll("\\*", "%"+ Hex.encodeHexString("*".getBytes()));
		return encodedString;
	}
}
