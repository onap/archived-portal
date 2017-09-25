package org.openecomp.portalapp.portal.service;

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
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPAppRoleFunction;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.domain.ExternalRoleDetails;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.BulkUploadRoleFunction;
import org.openecomp.portalapp.portal.transport.BulkUploadUserRoles;
import org.openecomp.portalapp.portal.transport.CentralApp;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.transport.CentralUser;
import org.openecomp.portalapp.portal.transport.CentralUserApp;
import org.openecomp.portalapp.portal.transport.EcompUserRoles;
import org.openecomp.portalapp.portal.transport.ExternalAccessPerms;
import org.openecomp.portalapp.portal.transport.ExternalAccessPermsDetail;
import org.openecomp.portalapp.portal.transport.ExternalAccessRole;
import org.openecomp.portalapp.portal.transport.ExternalAccessRolePerms;
import org.openecomp.portalapp.portal.transport.ExternalAccessUser;
import org.openecomp.portalapp.portal.transport.ExternalRoleDescription;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.restful.domain.EcompRole;
import org.openecomp.portalsdk.core.restful.domain.EcompUser;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Service("externalAccessRolesService")
@EnableAspectJAutoProxy
@EPMetricsLog
@EPAuditLog
public class ExternalAccessRolesServiceImpl implements ExternalAccessRolesService {

	private static final String AND_FUNCTION_CD_EQUALS = " and function_cd = '";

	private static final String OWNER = ".owner";

	private static final String ADMIN = ".admin";

	private static final String ACCOUNT_ADMINISTRATOR = ".Account_Administrator";

	private static final String FUNCTION_CD_LIKE_CLAUSE = " and function_cd like '%";

	private static final String FUNCTION_PIPE = "|";

	private static final String IS_NULL_STRING = "null";

	private static final String EXTERNAL_AUTH_PERMS = "perms";

	private static final String EXTERNAL_AUTH_ROLE_DESCRIPTION = "description";

	private static final String WHERE_APP_ID_EQUALS = " where app_id = ";

	private static final String IS_EMPTY_JSON_STRING = "{}";

	private static final String CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE = "Connecting to External Auth system";

	private static final String WHERE_ROLE_ID_EQUALS = " where role_id = ";

	private static final String APP_ROLE_ID = "appRoleId";

	private static final String APP_ID = "appId";

	private static final String PRIORITY = "priority";

	private static final String ACTIVE = "active";

	private static final String ROLE_NAME = "name";

	private static final String ID = "id";

	private static final String WHERE_ROLE_NAME_EQUALS = " where role_name = '";

	private static final String APP_ID_EQUALS = " app_id = ";

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	@Autowired
	private SessionFactory sessionFactory;

	RestTemplate template = new RestTemplate();

	@SuppressWarnings("unchecked")
	public List<EPRole> getAppRoles(Long appId) throws Exception {
		List<EPRole> applicationRoles = null;
		String filter = null;
		try {
			if (appId == 1) {
				filter = " where app_id is null";
			} else {
				filter = WHERE_APP_ID_EQUALS + appId;
			}
			applicationRoles = dataAccessService.getList(EPRole.class, filter, null, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles: failed", e);
			throw new Exception(e.getMessage());
		}
		return applicationRoles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getApp(String uebkey) throws Exception {
		List<EPApp> app = null;
		try {
			app = (List<EPApp>) dataAccessService.getList(EPApp.class, " where ueb_key = '" + uebkey + "'", null, null);
			if(!app.get(0).getEnabled() && !app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)){
				throw new Exception("Application:"+app.get(0).getName()+" is Unavailable");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getApp: failed", e);
			throw new Exception(e.getMessage());
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
		logger.debug(EELFLoggerDelegate.debugLogger, "getSingleAppRole: Connecting to External Auth system");
		ResponseEntity<String> response = template
				.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
								+ app.getNameSpace() + "." + addRole.replaceAll(" ", "_"),
						HttpMethod.GET, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getSingleAppRole: Finished GET app role from External Auth system and status code: {} ", response.getStatusCode().value());
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
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		delResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role?force=true",
				HttpMethod.DELETE, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleInExternalSystem: Finished DELETE operation in the External Auth system and status code: {} ", delResponse.getStatusCode().value());
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
	private boolean updateRoleInExternalSystem(Role updateExtRole, EPApp app) throws Exception {
		boolean response = false;
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<String> deleteResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		ExternalAccessRolePerms extRolePerms = null;
		ExternalAccessPerms extPerms = null;
		List<EPRole> epRoleList = null;
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
			epRoleList = dataAccessService.getList(EPRole.class,
					WHERE_ROLE_ID_EQUALS + updateExtRole.getId() + " and app_id is null", null, null);
		} else {
			epRoleList = dataAccessService.getList(EPRole.class,
					" where app_role_id = " + updateExtRole.getId() + " and app_id = " + app.getId(), null, null);
		}		
		String appRole = getSingleAppRole(epRoleList.get(0).getName(), app);
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
					throw new Exception(deleteResponse.getBody());
				}
				addRole(updateExtRole, app.getUebKey());
			} else {
				String desc = extRole.getJSONObject(0).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
				String name = extRole.getJSONObject(0).getString(ROLE_NAME);
				List<ExternalAccessPerms> list = null;
				if (extRole.getJSONObject(0).has(EXTERNAL_AUTH_PERMS)) {
					JSONArray perms = extRole.getJSONObject(0).getJSONArray(EXTERNAL_AUTH_PERMS);
					list = mapper.readValue(perms.toString(), TypeFactory.defaultInstance()
							.constructCollectionType(List.class, ExternalAccessPerms.class));
				}
				ExternalRoleDescription sysRoleList = mapper.readValue(desc, ExternalRoleDescription.class);
				// If role name or role functions are updated then delete record in External System and add new record to avoid conflicts
				Boolean existingRoleActive;
				boolean isActiveValueChanged;
				// check role active status
				existingRoleActive = new Boolean(sysRoleList.getActive());
				isActiveValueChanged = existingRoleActive.equals(updateExtRole.getActive());
				if (!sysRoleList.getName().equals(updateExtRole.getName())) {
					Map<String, String> delRoleKeyMapper = new HashMap<>();
					delRoleKeyMapper.put(ROLE_NAME, name);
					String delRoleKeyValue = mapper.writeValueAsString(delRoleKeyMapper);	
					deleteResponse = deleteRoleInExternalSystem(delRoleKeyValue);
					if (deleteResponse.getStatusCode().value() != 200) {
						logger.error(EELFLoggerDelegate.errorLogger, "updateRoleInExternalSystem:  Failed to delete role in external system due to {} ", deleteResponse.getBody());
						throw new Exception(deleteResponse.getBody());
					}
					addRole(updateExtRole, app.getUebKey());
					addRoleFunctionsInExternalSystem(updateExtRole, mapper, app);
				}
				boolean checkPriorityStatus = StringUtils.equals(String.valueOf(sysRoleList.getPriority()),String.valueOf(updateExtRole.getPriority()));
				ExternalAccessRole updateRole = new ExternalAccessRole();
				if (!isActiveValueChanged
						|| !checkPriorityStatus
						|| sysRoleList.getId().equals(IS_NULL_STRING)
						|| !sysRoleList.getId().equals(String.valueOf(epRoleList.get(0).getId()))) {
					String updateDesc = "";
					String appId = (app.getId().equals(PortalConstants.PORTAL_APP_ID)) ? " app_id is null"
							: APP_ID_EQUALS + app.getId();
					List<EPRole> getRole = dataAccessService.getList(EPRole.class,
							WHERE_ROLE_NAME_EQUALS + updateExtRole.getName() + "' and " + appId, null, null);
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
						extSystemUpdateRoleJsonMapper.put(APP_ROLE_ID, String.valueOf(getRole.get(0).getAppRoleId()));

					}
					updateDesc = mapper.writeValueAsString(extSystemUpdateRoleJsonMapper);
					updateRole.setName(app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
					updateRole.setDescription(updateDesc);
					String updateRoleDesc = mapper.writeValueAsString(updateRole);
					HttpEntity<String> entity = new HttpEntity<>(updateRoleDesc, headers);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"updateRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
					ResponseEntity<String> updatePermsResponse = template.exchange(
							SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
							HttpMethod.PUT, entity, String.class);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"updateRoleInExternalSystem: Finished updating in External Auth system and status code: {} ",
							updatePermsResponse.getStatusCode().value());
				}
				List<RoleFunction> roleFunctionListNew = convertSetToListOfRoleFunctions(updateExtRole);
				Map<String, RoleFunction> updateRoleFunc = new HashMap<>();
				for (RoleFunction addPerm : roleFunctionListNew) {
					updateRoleFunc.put(addPerm.getCode(), addPerm);
				}
				final Map<String, ExternalAccessPerms> extRolePermMap = new HashMap<>();
				// Update permissions in the ExternalAccess System
				if (list != null) {
					for (ExternalAccessPerms perm : list) {
						if (!updateRoleFunc.containsKey(perm.getInstance())) {
							removePermForRole(perm, mapper, name, headers);
						}
						extRolePermMap.put(perm.getInstance(), perm);
					}
				}
				response = true;
				if (!roleFunctionListNew.isEmpty()) {
					for (RoleFunction roleFunc : roleFunctionListNew) {
						if (!extRolePermMap.containsKey(roleFunc.getCode())) {
							String checkType = roleFunc.getCode().contains("menu") ? "menu" : "url";
							extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, roleFunc.getCode(),
									"*");
							extRolePerms = new ExternalAccessRolePerms(extPerms,
									app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
							String updateRolePerms = mapper.writeValueAsString(extRolePerms);
							HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
							logger.debug(EELFLoggerDelegate.debugLogger, "updateRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
							ResponseEntity<String> addResponse = template.exchange(
									SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
											+ "role/perm",
									HttpMethod.POST, entity, String.class);
							if (addResponse.getStatusCode().value() != 201) {
								response = false;
								logger.debug(EELFLoggerDelegate.debugLogger,
										"updateRoleInExternalSystem: Connected to External Auth system but something went wrong! due to {} and statuscode: {}",
										addResponse.getStatusCode().getReasonPhrase(),
										addResponse.getStatusCode().value());
							} else {
								response = true;
								logger.debug(EELFLoggerDelegate.debugLogger, "updateRoleInExternalSystem: Finished adding permissions to roles in External Auth system and status code: {} ", addResponse.getStatusCode().value());
							}
						}
					}
				}
			}
		} else {
			// It seems like role exists in local DB but not in External Access system
			addRole(updateExtRole, app.getUebKey());
			List<RoleFunction> roleFunctionListUpdate = convertSetToListOfRoleFunctions(updateExtRole);
			response = true;
			if (!roleFunctionListUpdate.isEmpty()) {
				addRoleFunctionsInExternalSystem(updateExtRole, mapper, app);
			}
		}
		return response;
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
			String checkType = roleFunc.getCode().contains("menu") ? "menu" : "url";
			extAddPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, roleFunc.getCode(), "*");
			extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
					app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
			String updateRolePerms = addPermsMapper.writeValueAsString(extAddRolePerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
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
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: Finished adding permissions to roles in External Auth system and status code: {} ", addResponse.getStatusCode().value());
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
	 * @throws Exception
	 */
	private void removePermForRole(ExternalAccessPerms perm, ObjectMapper permMapper, String name, HttpHeaders headers)
			throws Exception {
		ExternalAccessRolePerms extAccessRolePerms = new ExternalAccessRolePerms(perm, name);
		String permDetails = permMapper.writeValueAsString(extAccessRolePerms);
		HttpEntity<String> deleteEntity = new HttpEntity<>(permDetails, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "removePermForRole: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> deletePermResponse = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/"
						+ name + "/perm", HttpMethod.DELETE, deleteEntity, String.class);
		if (deletePermResponse.getStatusCode().value() != 200) {
			throw new Exception(deletePermResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "removePermForRole: Finished deleting permission to role in External Auth system and status code: {}",
				deletePermResponse.getStatusCode().value());
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
	private boolean addNewRoleInExternalSystem(List<EPRole> newRole, EPApp app) throws Exception {
		boolean response = false;
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
		extRole.setName(app.getNameSpace() + "." + newRole.get(0).getName().replaceAll(" ", "_"));
		extRole.setDescription(addDesc);
		addNewRole = mapper.writeValueAsString(extRole);
		HttpEntity<String> deleteEntity = new HttpEntity<>(addNewRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "addNewRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> addNewRoleInExternalSystem = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
				HttpMethod.POST, deleteEntity, String.class);
		if (addNewRoleInExternalSystem.getStatusCode().value() != 201) {
			throw new Exception(addNewRoleInExternalSystem.getBody());
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, "addNewRoleInExternalSystem: Finished adding into External Auth system and status code: {}",
					addNewRoleInExternalSystem.getStatusCode().value());
			response = true;
		}
		return response;
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
	@SuppressWarnings("unchecked")
	private String updateExistingRoleInExternalSystem(Role addRole, EPApp app) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String addNewRole = "";
		ExternalAccessRole extRole = new ExternalAccessRole();
		List<EPRole> role = null;
		String addDesc = null;
		Map<String, String> extSystemUpdateRole = new LinkedHashMap<>();
		if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
			role = dataAccessService.getList(EPRole.class, WHERE_ROLE_ID_EQUALS + addRole.getId() + " and app_id is null", null, null);
		} else {
			role = dataAccessService.getList(EPRole.class, " where app_role_id = " + addRole.getId() + " and app_id ="+app.getId(), null, null);
		}
		extSystemUpdateRole.put(ID, String.valueOf(role.get(0).getId()));
		extSystemUpdateRole.put(ROLE_NAME, String.valueOf(addRole.getName()));
		extSystemUpdateRole.put(ACTIVE, String.valueOf(role.get(0).getActive()));
		extSystemUpdateRole.put(PRIORITY, String.valueOf(role.get(0).getPriority()));
		extSystemUpdateRole.put(APP_ID, String.valueOf(role.get(0).getAppId()));
		extSystemUpdateRole.put(APP_ROLE_ID, String.valueOf(role.get(0).getAppRoleId()));
		addDesc = mapper.writeValueAsString(extSystemUpdateRole);
		extRole.setName(app.getNameSpace() + "." + addRole.getName().replaceAll(" ", "_"));
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
	@Transactional
	private boolean addRoleInEcompDB(Role addRoleInDB, EPApp app) throws Exception {
		boolean result = false;
		List<EPRole> applicationRoles = null;
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
				checkIfRoleExitsInExternalSystem(addRoleInDB, app);
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
				if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					List<EPRole> roleCreated = dataAccessService.getList(EPRole.class,
							WHERE_ROLE_NAME_EQUALS + addRoleInDB.getName() + "' and app_id = " + app.getId(), null,
							null);
					EPRole epUpdateRole = roleCreated.get(0);
					epUpdateRole.setAppRoleId(epUpdateRole.getId());
					dataAccessService.saveDomainObject(epUpdateRole, null);
					getRoleCreated = dataAccessService.getList(EPRole.class,
							WHERE_ROLE_NAME_EQUALS + addRoleInDB.getName() + "' and app_id = " + app.getId(), null,
							null);
				} else {
					getRoleCreated = dataAccessService.getList(EPRole.class,
							WHERE_ROLE_NAME_EQUALS + addRoleInDB.getName() + "' and app_id is null", null, null);
				}
				// Add role in External Auth system
				result = addNewRoleInExternalSystem(getRoleCreated, app);
			} else { // if role already exists then update it
				if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					applicationRoles = dataAccessService.getList(EPRole.class,
							" where app_id is null " + " and role_id = " + addRoleInDB.getId(), null, null);
				} else {
					applicationRoles = dataAccessService.getList(EPRole.class,
							WHERE_APP_ID_EQUALS + app.getId() + " and app_role_id = " + addRoleInDB.getId(), null, null);
				}
				if (applicationRoles.isEmpty()) {
					applicationRoles = dataAccessService.getList(EPRole.class,
							WHERE_APP_ID_EQUALS + app.getId() + " and role_name = '" + addRoleInDB.getName() + "'", null,
							null);
				}
				updateRoleInExternalSystem(addRoleInDB, app);
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

				saveRoleFunction(listWithoutDuplicates, app, applicationRoles);
				result = true;
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleInEcompDB is failed", e);
			throw new Exception(e.getMessage());
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
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		String roleName = app.getNameSpace() + "." + checkRole.getName().replaceAll(" ", "_");
		HttpEntity<String> checkRoleEntity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "checkIfRoleExitsInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> checkRoleInExternalSystem = template
				.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
						+ roleName, HttpMethod.GET, checkRoleEntity, String.class);
		if (!checkRoleInExternalSystem.getBody().equals(IS_EMPTY_JSON_STRING)) {
			logger.debug("checkIfRoleExitsInExternalSystem: Role already exists in external system {} and status code: {} ", checkRoleInExternalSystem.getBody(), checkRoleInExternalSystem.getStatusCode().value());
			throw new Exception("Role already exists in external system");
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
	private void saveRoleFunction(List<RoleFunction> roleFunctionListNew, EPApp app, List<EPRole> applicationRoles)
			throws Exception {
		for (RoleFunction roleFunc : roleFunctionListNew) {
			EPAppRoleFunction appRoleFunc = new EPAppRoleFunction();
			appRoleFunc.setAppId(app.getId());
			appRoleFunc.setRoleId(applicationRoles.get(0).getId());
			// query to check if function code is different for safe operation
			List<CentralRoleFunction> roleFunction = dataAccessService.getList(CentralRoleFunction.class,
					WHERE_APP_ID_EQUALS+ app.getId()+FUNCTION_CD_LIKE_CLAUSE + roleFunc.getCode() + "'", null, null);
			appRoleFunc.setCode(roleFunction.get(0).getCode());
			dataAccessService.saveDomainObject(appRoleFunc, null);
		}
	}
	
	/**
	 * It deletes all EPAppRoleFunction records in the portal
	 * 
	 * @param app
	 * @param role
	 */
	@SuppressWarnings("unchecked")
	private void deleteRoleFunction(EPApp app, List<EPRole> role) {
		List<EPAppRoleFunction> appRoleFunctionList = dataAccessService.getList(EPAppRoleFunction.class,
				WHERE_APP_ID_EQUALS + app.getId() + " and role_id = " + role.get(0).getId(), null, null);
		if (!appRoleFunctionList.isEmpty()) {
			for (EPAppRoleFunction approleFunction : appRoleFunctionList) {
				dataAccessService.deleteDomainObject(approleFunction, null);
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EPUser> getUser(String loginId){
		return (List<EPUser>) dataAccessService.getList(EPUser.class,
				" where org_user_id = '" + loginId + "'", null, null);
	}

	@Override
	public String getUserWithRoles(String loginId, String uebkey) throws Exception {
		final Map<String, String> params = new HashMap<>();
		List<EPUser> userList = null;
		CentralUser cenUser = null;
		EPApp app = null;
		String result = null;
		try {
			params.put("orgUserIdValue", loginId);
			List<EPApp> appList = getApp(uebkey);
			if (!appList.isEmpty()) {
				app = appList.get(0);
				userList = getUser(loginId);
				if (!userList.isEmpty()) {
					EPUser user = userList.get(0);
					ObjectMapper mapper = new ObjectMapper();
					Set<EPUserApp> userAppSet = user.getEPUserApps();
					cenUser = createEPUser(user, userAppSet, app);
					result = mapper.writeValueAsString(cenUser);
				} else if (userList.isEmpty()) {
					throw new Exception("User not found");
				}
			} else {
				throw new Exception("Application not found");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUser: failed", e);
			throw new Exception(e.getMessage());
		}
		return result;
	}

	@Override
	public List<CentralRole> getRolesForApp(String uebkey) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Entering into getRolesForApp");
		List<CentralRole> roleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		try {
			List<EPApp> app = getApp(uebkey);
			List<EPRole> appRolesList = getAppRoles(app.get(0).getId());
			createCentralRoleObject(app, appRolesList, roleList, params);
		} catch (Exception e) {
			throw new Exception("getRolesForApp Failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Finished getRolesForApp");
		return roleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentralRoleFunction> getRoleFuncList(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<CentralRoleFunction> finalRoleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		params.put(APP_ID, app.getId());
		// Sync all functions from external system into Ecomp portal DB
		logger.debug(EELFLoggerDelegate.debugLogger, "getRoleFuncList: Entering into syncRoleFunctionFromExternalAccessSystem");
		syncRoleFunctionFromExternalAccessSystem(app);
		logger.debug(EELFLoggerDelegate.debugLogger, "getRoleFuncList: Finished syncRoleFunctionFromExternalAccessSystem");
		List<CentralRoleFunction> getRoleFuncList = dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null);
		for(CentralRoleFunction roleFuncItem : getRoleFuncList ){
			if(roleFuncItem.getCode().contains(FUNCTION_PIPE)){
				String code = "";
				int count = StringUtils.countMatches(roleFuncItem.getCode(), FUNCTION_PIPE);
				if (count == 2)
					code = roleFuncItem.getCode().substring(
							roleFuncItem.getCode().indexOf(FUNCTION_PIPE) + 1,
							roleFuncItem.getCode().lastIndexOf(FUNCTION_PIPE));
				else
					code = roleFuncItem.getCode()
							.substring(roleFuncItem.getCode().lastIndexOf(FUNCTION_PIPE) + 1);
				
				roleFuncItem.setCode(code);
				finalRoleList.add(roleFuncItem);
			} else{
				finalRoleList.add(roleFuncItem);
			}
		}
		return finalRoleList;
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
	private CentralUser createEPUser(EPUser userInfo, Set<EPUserApp> userAppSet, EPApp app) throws Exception {

		final Map<String, Long> params = new HashMap<>();
		CentralUser userAppList = new CentralUser();
		CentralUser user1 = null;
		try {
			userAppList.userApps = new TreeSet<CentralUserApp>();
			for (EPUserApp userApp : userAppSet) {
				if (userApp.getRole().getActive()) {
					EPApp epApp = userApp.getApp();
					String globalRole = userApp.getRole().getName().toLowerCase();
					if (((epApp.getId().equals(app.getId()))
							&& (!userApp.getRole().getId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID)))
							|| ((epApp.getId().equals(PortalConstants.PORTAL_APP_ID))
									&& (globalRole.toLowerCase().startsWith("global_")))) {
						CentralUserApp cua = new CentralUserApp();
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
						List<CentralRoleFunction> appRoleFunctionList = dataAccessService
								.executeNamedQuery("getAppRoleFunctionList", params, null);
						SortedSet<CentralRoleFunction> roleFunctionSet = new TreeSet<>();
						for (CentralRoleFunction roleFunc : appRoleFunctionList) {
							String functionCode = "";
							if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
								int count = StringUtils.countMatches(roleFunc.getCode(), FUNCTION_PIPE);
								String finalFunctionCodeVal;
								if (count == 2)
									finalFunctionCodeVal = roleFunc.getCode().substring(
											roleFunc.getCode().indexOf(FUNCTION_PIPE) + 1,
											roleFunc.getCode().lastIndexOf(FUNCTION_PIPE));
								else
									finalFunctionCodeVal = roleFunc.getCode()
											.substring(roleFunc.getCode().lastIndexOf(FUNCTION_PIPE) + 1);

								functionCode = finalFunctionCodeVal;
							} else {
								functionCode = roleFunc.getCode();
							}
							CentralRoleFunction cenRoleFunc = new CentralRoleFunction(roleFunc.getId(), functionCode,
									roleFunc.getName(), null, null);
							roleFunctionSet.add(cenRoleFunc);
						}
						Long userRoleId = null;
						if (globalRole.toLowerCase().startsWith("global_")
								&& epApp.getId().equals(PortalConstants.PORTAL_APP_ID)) {
							userRoleId = userApp.getRole().getId();
						} else {
							userRoleId = userApp.getRole().getAppRoleId();
						}
						CentralRole cenRole = new CentralRole(userRoleId, userApp.getRole().getCreated(),
								userApp.getRole().getModified(), userApp.getRole().getCreatedId(),
								userApp.getRole().getModifiedId(), userApp.getRole().getRowNum(),
								userApp.getRole().getName(), userApp.getRole().getActive(),
								userApp.getRole().getPriority(), roleFunctionSet, null, null);
						cua.setRole(cenRole);

						userAppList.userApps.add(cua);
					}
				}
			}

			user1 = new CentralUser(null, userInfo.getCreated(), userInfo.getModified(), userInfo.getCreatedId(),
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
					userInfo.isOnline(), userInfo.getChatId(), userAppList.userApps, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "createEPUser: createEPUser failed", e);
			throw new Exception(e.getMessage());
		}

		return user1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CentralRole getRoleInfo(Long roleId, String uebkey) throws Exception {
		final Map<String, Long> params = new HashMap<>();
		List<CentralRole> roleList = new ArrayList<>();
		CentralRole cenRole = new CentralRole();
		List<EPRole> roleInfo = null;
		List<EPApp> app = null;
		try {
			app = getApp(uebkey);
			if (app.isEmpty()) {
				throw new Exception("Application not found");
			}
			String filter = null;
			if (app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
				filter = WHERE_ROLE_ID_EQUALS + roleId + " and app_id is null ";
			} else {
				filter = " where app_role_id = " + roleId + " and app_id = " + app.get(0).getId();

			}
			roleInfo = dataAccessService.getList(EPRole.class, filter, null, null);
			roleList = createCentralRoleObject(app, roleInfo, roleList, params);
			if (roleList.isEmpty()) {
				return cenRole;
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo: failed", e);
			throw new Exception(e.getMessage());

		}
		return roleList.get(0);
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
	 */
	@SuppressWarnings("unchecked")
	private List<CentralRole> createCentralRoleObject(List<EPApp> app, List<EPRole> roleInfo,
			List<CentralRole> roleList, Map<String, Long> params) {
		for (EPRole role : roleInfo) {
			params.put("roleId", role.getId());
			params.put(APP_ID, app.get(0).getId());
			List<CentralRoleFunction> cenRoleFuncList = dataAccessService.executeNamedQuery("getAppRoleFunctionList",
					params, null);
			SortedSet<CentralRoleFunction> roleFunctionSet = new TreeSet<>();
			for (CentralRoleFunction roleFunc : cenRoleFuncList) {
				String functionCode = "";
				if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
					int count = StringUtils.countMatches(roleFunc.getCode(), FUNCTION_PIPE);
					String finalFunctionCodeVal;
					if (count == 2)
						finalFunctionCodeVal = roleFunc.getCode().substring(
								roleFunc.getCode().indexOf(FUNCTION_PIPE) + 1,
								roleFunc.getCode().lastIndexOf(FUNCTION_PIPE));
					else
						finalFunctionCodeVal = roleFunc.getCode()
								.substring(roleFunc.getCode().lastIndexOf(FUNCTION_PIPE) + 1);
					functionCode = finalFunctionCodeVal;
				} else {
					functionCode = roleFunc.getCode();
				}
				CentralRoleFunction cenRoleFunc = new CentralRoleFunction(role.getId(), functionCode,
						roleFunc.getName(), null, null);
				roleFunctionSet.add(cenRoleFunc);
			}
			SortedSet<CentralRole> childRoles = new TreeSet<>();
			CentralRole cenRole = null;
			if (role.getAppRoleId() == null) {
				cenRole = new CentralRole(role.getId(), role.getCreated(), role.getModified(), role.getCreatedId(),
						role.getModifiedId(), role.getRowNum(), role.getName(), role.getActive(), role.getPriority(),
						roleFunctionSet, childRoles, null);
			} else {
				cenRole = new CentralRole(role.getAppRoleId(), role.getCreated(), role.getModified(),
						role.getCreatedId(), role.getModifiedId(), role.getRowNum(), role.getName(), role.getActive(),
						role.getPriority(), roleFunctionSet, childRoles, null);
			}
			roleList.add(cenRole);
		}
		return roleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CentralRoleFunction getRoleFunction(String functionCode, String uebkey) throws Exception {
		CentralRoleFunction roleFunc = null;
		EPApp app = getApp(uebkey).get(0);
		List<CentralRoleFunction> getRoleFuncList = null;
		final Map<String, String> params = new HashMap<>();
		try {
			params.put("functionCode", functionCode);
			params.put(APP_ID, String.valueOf(app.getId()));
			getRoleFuncList = dataAccessService.executeNamedQuery("getRoleFunction", params, null);
			if (getRoleFuncList.isEmpty()) {
				return roleFunc;
			} else{
				String functionCodeFormat = getRoleFuncList.get(0).getCode();
				if(functionCodeFormat.contains(FUNCTION_PIPE)){
					String newfunctionCodeFormat = functionCodeFormat.substring(functionCodeFormat.lastIndexOf(FUNCTION_PIPE)+1); 
					roleFunc = new CentralRoleFunction(getRoleFuncList.get(0).getId(), newfunctionCodeFormat, getRoleFuncList.get(0).getName(), getRoleFuncList.get(0).getAppId(), getRoleFuncList.get(0).getEditUrl());
				} else{
					roleFunc = new CentralRoleFunction(getRoleFuncList.get(0).getId(), functionCodeFormat, getRoleFuncList.get(0).getName(), getRoleFuncList.get(0).getAppId(), getRoleFuncList.get(0).getEditUrl());
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction: failed", e);
			throw new Exception("getRoleFunction failed", e);
		}
		return roleFunc;
	}

	@Override
	public boolean saveCentralRoleFunction(CentralRoleFunction domainCentralRoleFunction, EPApp app) throws Exception {
		boolean saveOrUpdateFunction = false;
		try {
			addRoleFunctionInExternalSystem(domainCentralRoleFunction, app);
			dataAccessService.saveDomainObject(domainCentralRoleFunction, null);
			saveOrUpdateFunction = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveCentralRoleFunction: failed", e);
			saveOrUpdateFunction = false;
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
	@SuppressWarnings("unchecked")
	private void addRoleFunctionInExternalSystem(CentralRoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ExternalAccessPerms extPerms = new ExternalAccessPerms();
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		List<CentralRoleFunction> appRoleFunc = dataAccessService.getList(CentralRoleFunction.class,
				WHERE_APP_ID_EQUALS + app.getId() + AND_FUNCTION_CD_EQUALS + domainCentralRoleFunction.getCode() + "'", null, null);
		String roleFuncName = null;
		if (!appRoleFunc.isEmpty()) {
			roleFuncName = appRoleFunc.get(0).getCode();
		} else {
			roleFuncName = domainCentralRoleFunction.getCode();
		}
		String checkType = domainCentralRoleFunction.getCode().contains("menu") ? "menu" : "url";
		HttpEntity<String> getSinglePermEntity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} ", CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> getResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perms/"
						+ app.getNameSpace() + "." + checkType + "/" + roleFuncName + "/*",
				HttpMethod.GET, getSinglePermEntity, String.class);
		if (getResponse.getStatusCode().value() != 200) {
			EPLogUtil.logExternalAuthAccessAlarm(logger, getResponse.getStatusCode());
			throw new Exception(getResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: Finished GET permission from External Auth system and response: {} ", getResponse.getBody());
		String res = getResponse.getBody();
		if (res.equals(IS_EMPTY_JSON_STRING)) {
			try {
				extPerms.setAction("*");
				extPerms.setInstance(domainCentralRoleFunction.getCode());
				extPerms.setType(app.getNameSpace() + "." + checkType);
				extPerms.setDescription(domainCentralRoleFunction.getName());
				String updateRole = mapper.writeValueAsString(extPerms);
				HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
				ResponseEntity<String> addPermResponse= template.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
						HttpMethod.POST, entity, String.class);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: Finished adding permission in  and status code: {} ", addPermResponse.getStatusCode().value());
			} catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			}catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionInExternalSystem: Failed to add fucntion in external central auth system",
						e);
			}
		} else {
			try {
				extPerms.setAction("*");
				extPerms.setInstance(domainCentralRoleFunction.getCode());
				extPerms.setType(app.getNameSpace() + "." + checkType);
				extPerms.setDescription(domainCentralRoleFunction.getName());
				String updateRole = mapper.writeValueAsString(extPerms);
				HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
				ResponseEntity<String> updatePermResponse = template.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
						HttpMethod.PUT, entity, String.class);
				logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: Finished updating permission in External Auth system and response: {} ", updatePermResponse.getBody());
			} catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionInExternalSystem: Failed to update function in external central auth system",
						e);
			}
		}
	}

	@Override
	@Transactional
	public boolean deleteCentralRoleFunction(String code, EPApp app) {
		boolean deleteFunctionResponse = false;
		try {
			final Map<String, String> params = new HashMap<>();
			params.put("functionCode", code);
			params.put(APP_ID, String.valueOf(app.getId()));
			CentralRoleFunction domainCentralRoleFunction = (CentralRoleFunction) dataAccessService
					.executeNamedQuery("getRoleFunction", params, null).get(0);
			deleteRoleFunctionInExternalSystem(domainCentralRoleFunction, app);
			// Delete role function dependency records
			deleteAppRoleFunctions(code, app);
			dataAccessService.deleteDomainObject(domainCentralRoleFunction, null);
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
				APP_ID_EQUALS + app.getId() + FUNCTION_CD_LIKE_CLAUSE + code + "'", null);
	}
	
	/**
	 * 
	 * It deletes permission in the external auth system  
	 * 
	 * @param domainCentralRoleFunction
	 * @param app
	 * @throws Exception
	 */
	private void deleteRoleFunctionInExternalSystem(CentralRoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ExternalAccessPerms extPerms = new ExternalAccessPerms();
			String instanceValue = "";
			if(domainCentralRoleFunction.getCode().contains(FUNCTION_PIPE)){
				instanceValue = domainCentralRoleFunction.getCode().substring(domainCentralRoleFunction.getCode().lastIndexOf(FUNCTION_PIPE)+1);
			}else{
				instanceValue = domainCentralRoleFunction.getCode();
			}
			String checkType = instanceValue.contains("menu") ? "menu" : "url";
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			extPerms.setAction("*");
			extPerms.setInstance(domainCentralRoleFunction.getCode());
			extPerms.setType(app.getNameSpace() + "." + checkType);
			extPerms.setDescription(domainCentralRoleFunction.getName());
			String updateRole = mapper.writeValueAsString(extPerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
			logger.debug(EELFLoggerDelegate.debugLogger,"deleteRoleFunctionInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
			ResponseEntity<String> delPermResponse = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
					+ "perm?force=true", HttpMethod.DELETE, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleFunctionInExternalSystem: Finished deleting permission in External Auth system and status code: {} ", delPermResponse.getStatusCode().value());
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
	public boolean saveRoleForApplication(Role saveRole, String uebkey) throws Exception {
		boolean addRoleResponse = false;
		try {
			EPApp app = getApp(uebkey).get(0);
			addRoleInEcompDB(saveRole, app);
			addRoleResponse = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleForApplication failed", e);
		}
		return addRoleResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteRoleForApplication(String deleteRole, String uebkey) throws Exception {
		Session localSession = sessionFactory.openSession();
		Transaction transaction = null;
		boolean result = false;
		try {
			transaction = localSession.beginTransaction();

			List<EPRole> epRoleList = null;
			ResponseEntity<String> deleteResponse = null;
			EPApp app = getApp(uebkey).get(0);
			if (app.getId() == 1) {
				epRoleList = dataAccessService.getList(EPRole.class,
						" where app_id is null " + "and role_name = '" + deleteRole + "'", null, null);
			} else {
				epRoleList = dataAccessService.getList(EPRole.class,
						WHERE_APP_ID_EQUALS + app.getId() + " and role_name = '" + deleteRole + "'", null, null);
			}
			// Delete app role functions before deleting role
			deleteRoleFunction(app, epRoleList);
			if (app.getId() == 1) {
				// Delete fn_user_ role
				dataAccessService.deleteDomainObjects(EPUserApp.class,
						APP_ID_EQUALS + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);

				deleteRoleDependencyRecords(localSession, epRoleList.get(0).getId(), app.getId());
			}
			// Delete Role in External System
			String deleteRoleKey = "{\"name\":\"" + app.getNameSpace() + "."
					+ epRoleList.get(0).getName().replaceAll(" ", "_") + "\"}";
			deleteResponse = deleteRoleInExternalSystem(deleteRoleKey);
			if (deleteResponse.getStatusCode().value() != 200 || deleteResponse.getStatusCode().value() != 404) {
				EPLogUtil.logExternalAuthAccessAlarm(logger, deleteResponse.getStatusCode());
				logger.error(EELFLoggerDelegate.errorLogger,
						"deleteRoleForApplication: Failed to delete role in external auth system! due to {} ",
						deleteResponse.getBody());
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleForApplication: about to commit the transaction");
			transaction.commit();
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleForApplication: committed the transaction");
			dataAccessService.deleteDomainObject(epRoleList.get(0), null);
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
		logger.debug(EELFLoggerDelegate.debugLogger,"deleteUserRoleInExternalSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
		ResponseEntity<String> getResponse = template
				.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole/"
								+ LoginId
								+ SystemProperties
										.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
								+ "/" + app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"),
						HttpMethod.GET, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: Finished GET user roles from External Auth system and response: {} ", getResponse.getBody());
		if (getResponse.getStatusCode().value() != 200) {
			throw new Exception(getResponse.getBody());
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
									+ "/" + app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"),
							HttpMethod.DELETE, userRoleentity, String.class);
			if (deleteResponse.getStatusCode().value() != 200) {
				throw new Exception("Failed to delete user role");
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: Finished deleting user role in External Auth system and status code: {} ", deleteResponse.getStatusCode().value());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentralRole> getActiveRoles(String uebkey) throws Exception {
		List<CentralRole> roleList = new ArrayList<>();
		try {
			List<EPApp> app = getApp(uebkey);
			final Map<String, Long> params = new HashMap<>();
			// check if portal
			Long appId = null;
			if (!app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
				appId = app.get(0).getId();
			}
			List<EPRole> epRole = dataAccessService.getList(EPRole.class,
					WHERE_APP_ID_EQUALS + appId + " and active_yn = 'Y'", null, null);
			roleList = createCentralRoleObject(app, epRole, roleList, params);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles: failed", e);
			throw new Exception(e.getMessage());
		}
		return roleList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteDependencyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception {
		boolean result = false;
		Session localSession = sessionFactory.openSession();
		Transaction transaction = null;
		EPApp app = null;
		try {
			transaction = localSession.beginTransaction();
			List<EPRole> epRoleList = null;
			app = getApp(uebkey).get(0);
			epRoleList = dataAccessService.getList(EPRole.class,
					WHERE_APP_ID_EQUALS + app.getId() + " and app_role_id = " + roleId, null, null);
			if (epRoleList.isEmpty()) {
				epRoleList = dataAccessService.getList(EPRole.class,
						WHERE_APP_ID_EQUALS + app.getId() + " and role_id = " + roleId, null, null);
			}
			// Delete User Role in External System before deleting role
			deleteUserRoleInExternalSystem(epRoleList.get(0), app, LoginId);
			// Delete user app roles
			dataAccessService.deleteDomainObjects(EPUserApp.class,
					APP_ID_EQUALS + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);

			deleteRoleDependencyRecords(localSession, epRoleList.get(0).getId(), app.getId());
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteDependencyRoleRecord: about to commit the transaction");
			transaction.commit();
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteDependencyRoleRecord: committed the transaction");
			result = true;
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to deleteRoleDependeciesRecord", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction, "deleteDependencyRoleRecord: rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			result = false;
		} finally {
			localSession.close();
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void syncRoleFunctionFromExternalAccessSystem(EPApp app) {
		try {
			ResponseEntity<String> response = null;
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			HttpEntity<String> entity = new HttpEntity<>(headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: {} " , CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
			response = template
					.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "perms/ns/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);

			String res = response.getBody();
			logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Finished GET permissions from External Auth system and response: {} ", response.getBody());
			JSONObject jsonObj = new JSONObject(res);
			JSONArray extPerms = jsonObj.getJSONArray("perm");
			for (int i = 0; i < extPerms.length(); i++) {
				if (extPerms.getJSONObject(i).getString("type").equals(app.getNameSpace() + ".access")) {
					extPerms.remove(i);
					i--;
				}
			}
			ExternalAccessPermsDetail permDetails = null;
			List<ExternalAccessPermsDetail> permsDetailList = new ArrayList<>();
			for (int i = 0; i < extPerms.length(); i++) {
				String description = null;
				if(extPerms.getJSONObject(i).has("description")){
					description = extPerms.getJSONObject(i).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
				} else{
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

			final Map<String, Long> params = new HashMap<>();
			final Map<String, CentralRoleFunction> roleFuncMap = new HashMap<>();
			params.put(APP_ID, app.getId());
			List<CentralRoleFunction> appFunctions = dataAccessService.executeNamedQuery("getAllRoleFunctions", params,
					null);
			if (!appFunctions.isEmpty()) {
				for (CentralRoleFunction roleFunc : appFunctions) {
					roleFuncMap.put(roleFunc.getCode(), roleFunc);
				}
			}
			// delete all application role functions
			dataAccessService.deleteDomainObjects(EPAppRoleFunction.class, APP_ID_EQUALS + app.getId(), null);
			// Add if new functions and app role functions were added in external auth system
			for (ExternalAccessPermsDetail permsDetail : permsDetailList) {
				String code = permsDetail.getInstance();
				CentralRoleFunction getFunctionCodeKey = roleFuncMap.get(permsDetail.getInstance());
				if (null == getFunctionCodeKey) {
					String finalFunctionCodeVal = "";
					if (permsDetail.getInstance().contains(FUNCTION_PIPE)) {
						int count = StringUtils.countMatches(permsDetail.getInstance(), FUNCTION_PIPE);
						if (count == 2)
							finalFunctionCodeVal = permsDetail.getInstance().substring(
									permsDetail.getInstance().indexOf(FUNCTION_PIPE) + 1,
									permsDetail.getInstance().lastIndexOf(FUNCTION_PIPE));
						else
							finalFunctionCodeVal = permsDetail.getInstance()
									.substring(permsDetail.getInstance().lastIndexOf(FUNCTION_PIPE) + 1);
					} else {
						finalFunctionCodeVal = permsDetail.getInstance();
					}
					CentralRoleFunction checkIfCodeStillExits = roleFuncMap.get(finalFunctionCodeVal);
					if (null == checkIfCodeStillExits) {
						logger.debug(EELFLoggerDelegate.debugLogger,
								"syncRoleFunctionFromExternalAccessSystem: Adding function: {} ", code);
						addFunctionInEcompDB(app, permsDetail, code);
						logger.debug(EELFLoggerDelegate.debugLogger,
								"syncRoleFunctionFromExternalAccessSystem: Finished adding function: {} ", code);

					}
				} 				
				List<EPRole> epRolesList = null;
				List<String> roles = permsDetail.getRoles();
				if (roles != null) {
					for (String roleList : roles) {
						if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
							epRolesList = dataAccessService.getList(EPRole.class,
									" where app_id is null " + " and role_name = '"
											+ roleList.substring(app.getNameSpace().length() + 1).replaceAll("_", " ")
											+ "'",
									null, null);
						} else {
							epRolesList = dataAccessService.getList(EPRole.class,
									WHERE_APP_ID_EQUALS + app.getId() + " and role_name = '"
											+ roleList.substring(app.getNameSpace().length() + 1).replaceAll("_", " ")
											+ "'",
									null, null);
						}
						if (epRolesList.isEmpty()) {
							if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
								epRolesList = dataAccessService.getList(EPRole.class,
										" where app_id is null " + " and role_name = '"
												+ roleList.substring(app.getNameSpace().length() + 1) + "'",
										null, null);
							} else {
								epRolesList = dataAccessService.getList(EPRole.class,
										WHERE_APP_ID_EQUALS + app.getId() + " and role_name = '"
												+ roleList.substring(app.getNameSpace().length() + 1) + "'",
										null, null);
							}
							// Adding new role thats does not exits in Local but exists in external access system
							if (epRolesList.isEmpty()) {
								Role role = addRoleInDBIfDoesNotExists(app, roleList.substring(app.getNameSpace().length() + 1));
								addIfRoleDescriptionNotExitsInExtSystem(role, app);
								epRolesList = dataAccessService.getList(EPRole.class,
										WHERE_APP_ID_EQUALS + app.getId() + " and role_name = '"
												+ role.getName() + "'",
										null, null);
							}
						}
						// save all application role functions
						if (!epRolesList.isEmpty()) {
							try {
								List<CentralRoleFunction> roleFunctionList = null;
								String functionCode = "";
								if (permsDetail.getInstance().contains(FUNCTION_PIPE)) {
									int count = StringUtils.countMatches(permsDetail.getInstance(), FUNCTION_PIPE);
									String finalFunctionCodeVal;
									if (count == 2)
										finalFunctionCodeVal = permsDetail.getInstance().substring(
												permsDetail.getInstance().indexOf(FUNCTION_PIPE) + 1,
												permsDetail.getInstance().lastIndexOf(FUNCTION_PIPE));
									else
										finalFunctionCodeVal = permsDetail.getInstance()
												.substring(permsDetail.getInstance().lastIndexOf(FUNCTION_PIPE) + 1);

									functionCode = finalFunctionCodeVal;
								}
								roleFunctionList = dataAccessService.getList(CentralRoleFunction.class,
										" where app_id = " + app.getId() + AND_FUNCTION_CD_EQUALS + functionCode + "'",
										null, null);
								if (roleFunctionList.isEmpty()) {
									roleFunctionList = dataAccessService.getList(CentralRoleFunction.class,
											" where app_id = " + app.getId() + AND_FUNCTION_CD_EQUALS + code + "'",
											null, null);
								}
								if (!roleFunctionList.isEmpty()) {
									EPAppRoleFunction addAppRoleFunc = new EPAppRoleFunction();
									addAppRoleFunc.setAppId(app.getId());
									addAppRoleFunc.setCode(roleFunctionList.get(0).getCode());
									addAppRoleFunc.setRoleId(epRolesList.get(0).getId());
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
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Finished syncRoleFunctionFromExternalAccessSystem");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "syncRoleFunctionFromExternalAccessSystem: Failed syncRoleFunctionFromExternalAccessSystem", e);

		}
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
		CentralRoleFunction addFunction = new CentralRoleFunction();
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
		EPRole epRoleNew = new EPRole();
		try {
			epRoleNew.setActive(true);
			epRoleNew.setName(role);
			if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				epRoleNew.setAppId(null);
			} else {
				epRoleNew.setAppId(app.getId());
			}
			dataAccessService.saveDomainObject(epRoleNew, null);
			List<EPRole> getRoleCreated = null;
			if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				List<EPRole> roleCreated = dataAccessService.getList(EPRole.class,
						WHERE_ROLE_NAME_EQUALS + role + "' and app_id = " + app.getId(), null, null);
				EPRole epUpdateRole = roleCreated.get(0);
				epUpdateRole.setAppRoleId(epUpdateRole.getId());
				dataAccessService.saveDomainObject(epUpdateRole, null);
				getRoleCreated = dataAccessService.getList(EPRole.class,
						WHERE_ROLE_NAME_EQUALS + role + "' and app_id = " + app.getId(), null, null);
			} else {
				getRoleCreated = dataAccessService.getList(EPRole.class,
						WHERE_ROLE_NAME_EQUALS + role + "' and app_id is null", null, null);
			}
			EPRole roleObject = getRoleCreated.get(0);
			setNewRole.setId(roleObject.getId());
			setNewRole.setName(roleObject.getName());
			setNewRole.setActive(roleObject.getActive());
			setNewRole.setPriority(roleObject.getPriority());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleInDBIfDoesNotExists: Failed", e);
		}
		return setNewRole;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer bulkUploadFunctions(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<RoleFunction> roleFuncList = null;
		roleFuncList = dataAccessService.getList(RoleFunction.class, null);
		CentralRoleFunction cenRoleFunc = null;
		Integer functionsAdded = 0;
		try {
			for (RoleFunction roleFunc : roleFuncList) {
				cenRoleFunc = new CentralRoleFunction(roleFunc.getCode(), roleFunc.getName());
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
		List<CentralRole> cenRoleList = new ArrayList<>();
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
			throw new Exception(e.getMessage());
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
					app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"));
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
		CentralRoleFunction cenRoleFunc = null;
		for (RoleFunction roleFunction : roleFunctionsList) {
			cenRoleFunc = new CentralRoleFunction(roleFunction.getCode(), roleFunction.getName());
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
							app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"));
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
							e.getMessage());
				}
			}

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void syncApplicationRolesWithEcompDB(EPApp app) {
		try {
			ResponseEntity<String> response = null;
			List<EPRole> finalRoleList = new ArrayList<>();
			ExternalRoleDescription ApplicationRole = new ExternalRoleDescription();
			ExternalAccessPerms externalAccessPerms = null;
			List<String> functionCodelist = new ArrayList<>();
			List<ExternalRoleDetails> externalRoleDetailsList = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
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
			dataAccessService.deleteDomainObjects(EPAppRoleFunction.class, APP_ID_EQUALS + app.getId(), null);
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
						externalAccessPerms = new ExternalAccessPerms(perms.getString("type"),
								perms.getString("instance"), perms.getString("action"));
						ePAppRoleFunction.setCode(externalAccessPerms.getInstance());
						functionCodelist.add(ePAppRoleFunction.getCode());
						externalAccessPermsOfRole.add(externalAccessPerms);
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

				if (!externalAccessPermsOfRole.isEmpty()) {
					// Adding functions to roles  
					for (ExternalAccessPerms externalpermission : externalAccessPermsOfRole) {
						try {
							logger.debug(EELFLoggerDelegate.debugLogger,
									"SyncApplicationRolesWithEcompDB: Adding function to the role: {}",
									externalpermission.getInstance());
							List<CentralRoleFunction> roleFunction = null;
							roleFunction = dataAccessService.getList(
									CentralRoleFunction.class, " where function_cd = '"
											+ externalpermission.getInstance() + "' and " + APP_ID_EQUALS + app.getId(),
									null, null);
							if (roleFunction.isEmpty()) {
								String funcCode = externalpermission.getType()
										.substring(app.getNameSpace().length() + 1) + FUNCTION_PIPE
										+ externalAccessPerms.getInstance();
								roleFunction = dataAccessService.getList(CentralRoleFunction.class,
										" where function_cd = '" + funcCode + "' and " + APP_ID_EQUALS + app.getId(), null,
										null);
							}
							if(!roleFunction.isEmpty()){
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
				externalRoleDetailsList.add(externalRoleDetail);
			}
			for (ExternalRoleDetails externalRole : externalRoleDetailsList) {
				EPRole ecompRole = convertExternalRoleDetailstoEpRole(externalRole);
				finalRoleList.add(ecompRole);
			}

			List<EPRole> applicationRolesList = new ArrayList<>();
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

			// Check if roles exits in external Access system and make it inactive
			final Map<String, EPRole> checkRolesInactive = new HashMap<>();
			for (EPRole extrole : finalRoleList) {
				checkRolesInactive.put(extrole.getName(), extrole);
			}
			for (EPRole role : applicationRolesList) {
				try {
					final Map<String, String> extRoleParams = new HashMap<>();
					List<EPRole> roleList = null;
					extRoleParams.put("appRoleName", role.getName());
					if (!checkRolesInactive.containsKey(role.getName())) {
						if (app.getId() == 1) {
							roleList = dataAccessService.executeNamedQuery("getPortalAppRoles", extRoleParams, null);
						} else {
							extRoleParams.put(APP_ID, app.getId().toString());
							roleList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", extRoleParams, null);
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

			// It checks properties in the external auth system app role description and updates role in local
			for (EPRole roleItem : finalRoleList) {
				final Map<String, String> roleParams = new HashMap<>();
				List<EPRole> currentList = null;
				roleParams.put("appRoleName", roleItem.getName());
				if (app.getId() == 1) {
					currentList = dataAccessService.executeNamedQuery("getPortalAppRoles", roleParams, null);
				} else {
					roleParams.put(APP_ID, app.getId().toString());
					currentList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", roleParams, null);
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

			EPRole roleToBeAddedInEcompDB = new EPRole();
			for (int i = 0; i < roleListToBeAddInEcompDB.size(); i++) {
				try {
					roleToBeAddedInEcompDB = roleListToBeAddInEcompDB.get(i);
					if (app.getId() == 1) {
						roleToBeAddedInEcompDB.setAppRoleId(null);
					}
					dataAccessService.saveDomainObject(roleToBeAddedInEcompDB, null);
					List<EPRole> getRoleCreatedInSync = null;
					if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						getRoleCreatedInSync = dataAccessService.getList(EPRole.class,
								WHERE_ROLE_NAME_EQUALS + roleToBeAddedInEcompDB.getName() + "' and app_id = "+app.getId(), null, null);
						EPRole epUpdateRole = getRoleCreatedInSync.get(0);
						epUpdateRole.setAppRoleId(epUpdateRole.getId());
						dataAccessService.saveDomainObject(epUpdateRole, null);
					}
					List<EPRole> roleList = new ArrayList<>();
					final Map<String, String> params = new HashMap<>();

					params.put("appRoleName", roleToBeAddedInEcompDB.getName());
					if (app.getId() == 1) {
						roleList = dataAccessService.executeNamedQuery("getPortalAppRoles", params, null);
					} else {
						params.put(APP_ID, app.getId().toString());
						roleList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", params, null);
					}
					EPRole role = roleList.get(0);
					Role aaFrole = new Role();
					aaFrole.setId(role.getId());
					aaFrole.setActive(role.getActive());
					aaFrole.setPriority(role.getPriority());
					aaFrole.setName(role.getName());
					updateRoleInExternalSystem(aaFrole, app);
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"SyncApplicationRolesWithEcompDB: Failed to add or update role in external auth system", e);
				}
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "syncApplicationRolesWithEcompDB: Finished");
		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to SyncApplicationRolesWithEcompDB", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "syncApplicationRolesWithEcompDB: Failed ", e);
		}
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
				addUserRoleInExternalSystem(userRolesUpload);
				userRolesAdded++;
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
					userRolesUpload.getAppNameSpace() + "." + userRolesUpload.getRoleName().replaceAll(" ", "_"));
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
				logger.error(EELFLoggerDelegate.errorLogger, "addUserRoleInExternalSystem: Failed to addUserRoleInExternalSystem", e.getMessage());
			}
		}
	}

	@Override
	public void deleteRoleDependencyRecords(Session localSession, Long roleId, Long appId) throws Exception {
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

			// Delete form fn_menu_functional_roles
			sql = "DELETE FROM fn_menu_functional_roles WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleDependeciesRecord: failed ", e);
			throw new Exception("delete Failed" + e.getMessage());
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
}
