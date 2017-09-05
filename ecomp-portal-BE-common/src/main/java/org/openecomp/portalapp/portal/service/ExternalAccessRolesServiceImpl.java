package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.BulkUploadRoleFunction;
import org.openecomp.portalapp.portal.transport.BulkUploadUserRoles;
import org.openecomp.portalapp.portal.transport.CentralApp;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.transport.CentralUser;
import org.openecomp.portalapp.portal.transport.CentralUserApp;
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
public class ExternalAccessRolesServiceImpl implements ExternalAccessRolesService {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	
	@Autowired
	private SessionFactory sessionFactory;


	RestTemplate template = new RestTemplate();

	@SuppressWarnings("unchecked")
	public List<EPRole> getAppRoles(Long appId, Boolean extRequestValue) throws Exception {
		List<EPRole> applicationRoles = null;
		String filter = null;
		try {
			if (appId == 1) {
				filter = " where app_id is null";
			} else {
				filter = " where app_id = " + appId;
			}
			applicationRoles = dataAccessService.getList(EPRole.class, filter, null, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles is failed", e);
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
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getApp is failed", e);
			throw new Exception(e.getMessage());
		}
		return app;
	}

	public String getSingleAppRole(String addRole, EPApp app) throws Exception {
		String response = "";
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		response = template
				.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
								+ app.getNameSpace() + "." + addRole.replaceAll(" ", "_"),
						HttpMethod.GET, entity, String.class)
				.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");

		return response;
	}

	@Override
	public boolean addRole(Role addRole, String uebkey) throws Exception {
		boolean response = false;
		ResponseEntity<String> addResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		EPApp app = getApp(uebkey).get(0);
		String newRole = createNewRoleInExternalSystem(addRole, app);
		HttpEntity<String> entity = new HttpEntity<>(newRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		addResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
				HttpMethod.POST, entity, String.class);
		if (addResponse.getStatusCode().value() == 201) {
			response = true;
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
		}
		if (addResponse.getStatusCode().value() == 406) {
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system but something went wrong!");
			throw new Exception("Failed to create role");
		}
		return response;
	}

	@Override
	public void updateRole(Role addRole, EPApp app) throws Exception {
		boolean addResponse = updateRoleInExternalSystem(addRole, app);
		if (!addResponse) {
			throw new Exception("Failed to update a role");
		}
	}

	private ResponseEntity<String> deleteRoleInExternalSystem(String delRole) throws Exception {
		ResponseEntity<String> delResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(delRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		delResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role?force=true",
				HttpMethod.DELETE, entity, String.class);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
		return delResponse;
	}

	@SuppressWarnings("unchecked")
	private boolean updateRoleInExternalSystem(Role updateExtRole, EPApp app) throws Exception {
		boolean response = false;
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<String> deleteResponse = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		ExternalAccessRolePerms extRolePerms = null;
		ExternalAccessPerms extPerms = null;
		List<EPRole> epRoleList = null;
			epRoleList = dataAccessService.getList(EPRole.class,
				" where role_id = " + updateExtRole.getId(), null, null);
		String appRole = getSingleAppRole(epRoleList.get(0).getName(), app);
		if (!appRole.equals("{}")) {
			JSONObject jsonObj = new JSONObject(appRole);
			JSONArray extRole = jsonObj.getJSONArray("role");
			if (!extRole.getJSONObject(0).has("description")) {
				String roleName = extRole.getJSONObject(0).getString("name");
				String delRoleKey = "{\"name\":\"" + roleName + "\"}";
				deleteResponse = deleteRoleInExternalSystem(delRoleKey);
				if (deleteResponse.getStatusCode().value() != 200) {
					throw new Exception("Failed to delete role in external access system!");
				}
				addRole(updateExtRole, app.getUebKey());
			} else {
				String desc = extRole.getJSONObject(0).getString("description");
				String name = extRole.getJSONObject(0).getString("name");
				List<ExternalAccessPerms> list = null;
				if (extRole.getJSONObject(0).has("perms")) {
					JSONArray perms = extRole.getJSONObject(0).getJSONArray("perms");
					ObjectMapper permsMapper = new ObjectMapper();
					list = permsMapper.readValue(perms.toString(), TypeFactory.defaultInstance()
							.constructCollectionType(List.class, ExternalAccessPerms.class));
				}
				ObjectMapper roleMapper = new ObjectMapper();
				ExternalRoleDescription sysRoleList = roleMapper.readValue(desc, ExternalRoleDescription.class);
				// If role name or role functions are updated then delete record in External System and add new record to avoid conflicts
				Boolean existingRoleActive;
				boolean res;
				// check role active status
				existingRoleActive = new Boolean(sysRoleList.getActive());
				res = existingRoleActive.equals(updateExtRole.getActive());
				if (!sysRoleList.getName().equals(updateExtRole.getName())) {
					String deleteRoleKey = "{\"name\":\"" + name + "\"}";
					deleteResponse = deleteRoleInExternalSystem(deleteRoleKey);
					if (deleteResponse.getStatusCode().value() != 200) {
						throw new Exception("Failed to delete role in external access system!");
					}
					response = addRole(updateExtRole, app.getUebKey());
					ObjectMapper addPermsMapper = new ObjectMapper();
					response = addRoleFunctionsInExternalSystem(updateExtRole, addPermsMapper, app);
				}
				ExternalAccessRole updateRole = new ExternalAccessRole();
				if (!res || !sysRoleList.getPriority().equals(String.valueOf(updateExtRole.getPriority())) || 
						sysRoleList.getId().equals("null")) {
					String updateDesc = "";
					List<EPRole> getRole = dataAccessService.getList(EPRole.class,
							" where role_name = '" + updateExtRole.getName() + "'", null, null);
					if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						updateDesc = "{\"id\":\"" + getRole.get(0).getId() + "\",\"name\":\"" + updateExtRole.getName()
								+ "\",\"active\":\"" + updateExtRole.getActive() + "\",\"priority\":\""
								+ updateExtRole.getPriority() + "\",\"appId\":\"null\",\"appRoleId\":\"null\"}";

					} else {
						updateDesc = "{\"id\":\"" + getRole.get(0).getId() + "\",\"name\":\"" + updateExtRole.getName()
								+ "\",\"active\":\"" + updateExtRole.getActive() + "\",\"priority\":\""
								+ updateExtRole.getPriority() + "\",\"appId\":\"" + app.getId() + "\",\"appRoleId\":\""
								+ getRole.get(0).getAppRoleId() + "\"}";

					}
					updateRole.setName(app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
					updateRole.setDescription(updateDesc);
					String updateRoleDesc = mapper.writeValueAsString(updateRole);
					HttpEntity<String> entity = new HttpEntity<>(updateRoleDesc, headers);
					logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
					template.exchange(
							SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
							HttpMethod.PUT, entity, String.class);
					logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
				}
				List<RoleFunction> roleFunctionListNew = convertSetToListOfRoleFunctions(updateExtRole);
				Map<String, RoleFunction> updateRoleFunc = new HashMap<>();
				for (RoleFunction addPerm : roleFunctionListNew) {
					updateRoleFunc.put(addPerm.getCode(), addPerm);
				}
				final Map<String, ExternalAccessPerms> extRolePermMap = new HashMap<>();
				// Update permissions in the ExternalAccess System
				ObjectMapper permMapper = new ObjectMapper();
				if (list != null) {
					for (ExternalAccessPerms perm : list) {
						if (!updateRoleFunc.containsKey(perm.getInstance())) {
							removePermForRole(perm, permMapper, name, headers);
						}
						extRolePermMap.put(perm.getInstance(), perm);
					}
				}
				response = true;
				if (!roleFunctionListNew.isEmpty() || roleFunctionListNew.size() > 0) {
					for (RoleFunction roleFunc : roleFunctionListNew) {
						if (!extRolePermMap.containsKey(roleFunc.getCode())) {
							String checkType = roleFunc.getCode().contains("menu") ? "menu" : "url";
							extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, roleFunc.getCode(),
									"*");
							extRolePerms = new ExternalAccessRolePerms(extPerms,
									app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
							String updateRolePerms = mapper.writeValueAsString(extRolePerms);
							HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
							logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
							ResponseEntity<String> addResponse = template.exchange(
									SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
											+ "role/perm",
									HttpMethod.POST, entity, String.class);
							if (addResponse.getStatusCode().value() != 201) {
								response = false;
								logger.debug(EELFLoggerDelegate.debugLogger,
										"Connected to External Access system but something went wrong! due to {} and statuscode: {}", addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
							} else {
								response = true;
								logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
							}
						}
					}
				}
			}
		} else {
			// It seems like role exists in local DB but not in External Access  system
			addRole(updateExtRole, app.getUebKey());
			List<RoleFunction> roleFunctionListUpdate = convertSetToListOfRoleFunctions(updateExtRole);
			response = true;
			if (!roleFunctionListUpdate.isEmpty() || roleFunctionListUpdate.size() > 0) {
				ObjectMapper addPermsMapper = new ObjectMapper();
				addRoleFunctionsInExternalSystem(updateExtRole, addPermsMapper, app);
			}
		}
		return response;
	}
	
	private boolean addRoleFunctionsInExternalSystem(Role updateExtRole, ObjectMapper addPermsMapper, EPApp app) throws Exception {
		boolean response = false;
		ExternalAccessRolePerms extAddRolePerms = null;
		ExternalAccessPerms extAddPerms = null;
		List<RoleFunction> roleFunctionListAdd = convertSetToListOfRoleFunctions(updateExtRole);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		for (RoleFunction roleFunc : roleFunctionListAdd) {
			String checkType = roleFunc.getCode().contains("menu") ? "menu" : "url";
			extAddPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, roleFunc.getCode(),
					"*");
			extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
					app.getNameSpace() + "." + updateExtRole.getName().replaceAll(" ", "_"));
			String updateRolePerms = addPermsMapper.writeValueAsString(extAddRolePerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
			ResponseEntity<String> addResponse = template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "role/perm",
					HttpMethod.POST, entity, String.class);
			if (addResponse.getStatusCode().value() != 201) {
				response = false;
				logger.debug(EELFLoggerDelegate.debugLogger,
						"Connected to External Access system but something went wrong! due to {} and statuscode: {}", addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
			} else {
				response = true;
				logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
			}
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private List<RoleFunction> convertSetToListOfRoleFunctions(Role updateExtRole){
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
	
	private void removePermForRole(ExternalAccessPerms perm, ObjectMapper permMapper,String name, HttpHeaders headers) throws Exception {
		ExternalAccessRolePerms extAccessRolePerms = new ExternalAccessRolePerms(perm, name);
		String permDetails = permMapper.writeValueAsString(extAccessRolePerms);
		HttpEntity<String> deleteEntity = new HttpEntity<>(permDetails, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		ResponseEntity<String> deletePermResponse = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "role/"+name+"/perm", HttpMethod.DELETE, deleteEntity, String.class);
		if (deletePermResponse.getStatusCode().value() != 200) {
			throw new Exception("Failed to delete role function");
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
	}

	private boolean addNewRoleInExternalSystem(List<EPRole> newRole, EPApp app) throws Exception {
		boolean response = false;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		ObjectMapper mapper = new ObjectMapper();
		String addNewRole = "";
		ExternalAccessRole extRole = new ExternalAccessRole();
		String addDesc = null;
		addDesc = "{\"id\":\"" + newRole.get(0).getId() + "\",\"name\":\"" + newRole.get(0).getName() + "\",\"active\":\""
					+ newRole.get(0).getActive() + "\",\"priority\":\"" +newRole.get(0).getPriority() + "\",\"appId\":\""
					+ newRole.get(0).getAppId() + "\",\"appRoleId\":\"" + newRole.get(0).getAppRoleId() + "\"}";

		extRole.setName(app.getNameSpace() + "." + newRole.get(0).getName().replaceAll(" ", "_"));
		extRole.setDescription(addDesc);
		addNewRole = mapper.writeValueAsString(extRole);
		HttpEntity<String> deleteEntity = new HttpEntity<>(addNewRole, headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		ResponseEntity<String> addNewRoleInExternalSystem = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "role", HttpMethod.POST, deleteEntity, String.class);
		if (addNewRoleInExternalSystem.getStatusCode().value() != 201) {
			throw new Exception("Failed to add Role in External System");
		} else{
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
			response = true;
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private String createNewRoleInExternalSystem(Role addRole, EPApp app) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String addNewRole = "";
		ExternalAccessRole extRole = new ExternalAccessRole();
		List<EPRole> role = null;
		String addDesc = null;
		if(app.getId().equals(PortalConstants.PORTAL_APP_ID)){
			role = dataAccessService.getList(EPRole.class,
					" where role_id = " + addRole.getId(), null, null);	
			addDesc = "{\"id\":\"" + role.get(0).getId() + "\",\"name\":\"" + addRole.getName() + "\",\"active\":\""
					+ role.get(0).getActive() + "\",\"priority\":\"" + role.get(0).getPriority()
					+ "\",\"appId\":\"null\",\"appRoleId\":\"null\"}";
		} else{
			role = dataAccessService.getList(EPRole.class,
					" where app_role_id = " + addRole.getId(), null, null);	
			addDesc = "{\"id\":\"" + role.get(0).getId() + "\",\"name\":\"" + addRole.getName() + "\",\"active\":\""
					+ role.get(0).getActive() + "\",\"priority\":\"" + addRole.getPriority() + "\",\"appId\":\""
					+ app.getId() + "\",\"appRoleId\":\"" + role.get(0).getAppRoleId() + "\"}";
		}
		extRole.setName(app.getNameSpace() + "." + addRole.getName().replaceAll(" ", "_"));
		extRole.setDescription(addDesc);
		addNewRole = mapper.writeValueAsString(extRole);
		return addNewRole;
	}

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
					List <EPRole> getRoleCreated = null;
					if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						List<EPRole> roleCreated =  dataAccessService.getList(EPRole.class,
								" where role_name = '" + addRoleInDB.getName() +"' and app_id = "+ app.getId(), null, null);	
						EPRole epUpdateRole = roleCreated.get(0);
						epUpdateRole.setAppRoleId(epUpdateRole.getId());
						dataAccessService.saveDomainObject(epUpdateRole, null);
						getRoleCreated =  dataAccessService.getList(EPRole.class,
								" where role_name = '" + addRoleInDB.getName() +"' and app_id = "+ app.getId() , null, null);	
					} else{
						getRoleCreated =  dataAccessService.getList(EPRole.class,
								" where role_name = '" + addRoleInDB.getName() +"' and app_id is null", null, null);	
					}
				// Add role in External Access system
				boolean response = addNewRoleInExternalSystem(getRoleCreated, app);
				
				if (!response) {
					throw new Exception("Failed to add role!");
				}
			} else { // if role already exists then update it
				if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
					applicationRoles = dataAccessService.getList(EPRole.class,
							" where app_id is null " + " and role_id = " + addRoleInDB.getId(), null, null);
				} else {
					applicationRoles = dataAccessService.getList(EPRole.class,
							" where app_id = " + app.getId() + " and app_role_id = " + addRoleInDB.getId(), null, null);
				}
				if(applicationRoles.isEmpty() && !app.getId().equals(PortalConstants.PORTAL_APP_ID)){
					applicationRoles = dataAccessService.getList(EPRole.class,
							" where app_id = " + app.getId() + " and role_id = " + addRoleInDB.getId(), null, null);
				}
				updateRoleInExternalSystem(addRoleInDB, app);
				deleteRoleFunction(app, applicationRoles);
				if (applicationRoles.size() > 0 || !applicationRoles.isEmpty()) {
					epRole = applicationRoles.get(0);
					epRole.setName(addRoleInDB.getName());
					epRole.setPriority(addRoleInDB.getPriority());
					epRole.setActive(addRoleInDB.getActive());
					if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						epRole.setAppId(null);
						epRole.setAppRoleId(null);
					} else if(!app.getId().equals(PortalConstants.PORTAL_APP_ID) && applicationRoles.get(0).getAppRoleId() == null){
						epRole.setAppRoleId(epRole.getId());
					}
					dataAccessService.saveDomainObject(epRole, null);
				}
				
				saveRoleFunction(listWithoutDuplicates, app, applicationRoles);
			}
			result = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addRoleInEcompDB is failed", e);
			throw new Exception(e.getMessage());
		}
		return result;
	}

	private void checkIfRoleExitsInExternalSystem(Role checkRole, EPApp app) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		String roleName = app.getNameSpace()+"."+checkRole.getName().replaceAll(" ", "_");
		HttpEntity<String> checkRoleEntity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		ResponseEntity<String> checkRoleInExternalSystem = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
						+ "roles/"+roleName, HttpMethod.GET, checkRoleEntity, String.class);
		if(!checkRoleInExternalSystem.getBody().equals("{}")){
			logger.debug("Role already exists in external system ", checkRoleInExternalSystem.getBody());
			throw new Exception("Role already exists in external system");
		}		
	}

	private void saveRoleFunction(List<RoleFunction> roleFunctionListNew, EPApp app, List<EPRole> applicationRoles)	throws Exception {
		for (RoleFunction roleFunc : roleFunctionListNew) {
			EPAppRoleFunction appRoleFunc = new EPAppRoleFunction();
			appRoleFunc.setAppId(app.getId());
			appRoleFunc.setRoleId(applicationRoles.get(0).getId());
			appRoleFunc.setCode(roleFunc.getCode());
			dataAccessService.saveDomainObject(appRoleFunc, null);
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteRoleFunction(EPApp app, List<EPRole> role) {
		List<EPAppRoleFunction> appRoleFunctionList = dataAccessService.getList(EPAppRoleFunction.class,
				" where app_id = " + app.getId() + " and role_id = " + role.get(0).getId(), null, null);
		if (!appRoleFunctionList.isEmpty() || appRoleFunctionList.size() > 0) {
			for (EPAppRoleFunction approleFunction : appRoleFunctionList) {
				dataAccessService.deleteDomainObject(approleFunction, null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String getUser(String loginId, String uebkey) throws Exception {
		final Map<String, String> params = new HashMap<>();
		List<EPUser> userList = null;
		CentralUser cenUser = null;
		EPApp app = null;
		String result = null;
		try {
			params.put("orgUserIdValue", loginId);
			List<EPApp> appList = (List<EPApp>) getApp(uebkey);
			if (appList.size() > 0) {
				app = appList.get(0);
				userList = (List<EPUser>) dataAccessService.getList(EPUser.class,
						" where org_user_id = '" + loginId + "'", null, null);
				if (userList.size() > 0) {
					EPUser user = userList.get(0);
					ObjectMapper mapper = new ObjectMapper();
					Set<EPUserApp> userAppSet = user.getEPUserApps();
					cenUser = createEPUser(user, userAppSet, app);
					result = mapper.writeValueAsString(cenUser);
				} else if (userList.size() == 0) {
					throw new Exception("User not found");
				}
			} else {
				throw new Exception("Application not found");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUser is failed", e);
			throw new Exception(e.getMessage());
		}
		return result;
	}
	
	@Override
	public List<CentralRole> getRolesForApp(String uebkey) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "Entering into getRolesForApp");
		List<CentralRole> roleList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		try {
			List<EPApp> app = getApp(uebkey);
			List<EPRole> appRolesList = getAppRoles(app.get(0).getId(), null);
			createCentralRoleObject(app, appRolesList, roleList, params);
		} catch (Exception e) {
			throw new Exception("getRolesForApp Failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished getRolesForApp");
		return roleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentralRoleFunction> getRoleFuncList(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<CentralRoleFunction> getRoleFuncList = null;
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", app.getId());
		//Sync all functions from external system into Ecomp portal DB
		logger.debug(EELFLoggerDelegate.debugLogger, "Entering into syncRoleFunctionFromExternalAccessSystem");
		syncRoleFunctionFromExternalAccessSystem(app);
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished syncRoleFunctionFromExternalAccessSystem");
		getRoleFuncList = dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null);
		return getRoleFuncList;
	}

	@SuppressWarnings("unchecked")
	public CentralUser createEPUser(EPUser userInfo, Set<EPUserApp> userAppSet, EPApp app) throws Exception {

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
						params.put("appId", userApp.getApp().getId());
						List<CentralRoleFunction> appRoleFunctionList = dataAccessService
								.executeNamedQuery("getAppRoleFunctionList", params, null);
						SortedSet<CentralRoleFunction> roleFunctionSet = new TreeSet<CentralRoleFunction>();
						for (CentralRoleFunction roleFunc : appRoleFunctionList) {
							CentralRoleFunction cenRoleFunc = new CentralRoleFunction(roleFunc.getId(),
									roleFunc.getCode(), roleFunc.getName(), null, null);
							roleFunctionSet.add(cenRoleFunc);
						}
						Long userRoleId = null;
						if(globalRole.toLowerCase().startsWith("global_") && epApp.getId().equals(PortalConstants.PORTAL_APP_ID)){
							userRoleId = userApp.getRole().getId();
						} else{
							userRoleId = userApp.getRole().getAppRoleId();
						}
						CentralRole cenRole = new CentralRole(userRoleId,
								userApp.getRole().getCreated(), userApp.getRole().getModified(),
								userApp.getRole().getCreatedId(), userApp.getRole().getModifiedId(),
								userApp.getRole().getRowNum(), userApp.getRole().getName(),
								userApp.getRole().getActive(), userApp.getRole().getPriority(), roleFunctionSet, null,
								null);
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
			logger.error(EELFLoggerDelegate.errorLogger, "createEPUser failed", e);
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
			if (app.isEmpty() || app.size() == 0) {
				throw new Exception("Application not found");
			}
			String filter = null;
			if (app.get(0).getId() == PortalConstants.PORTAL_APP_ID) {
				filter = " where role_id = " + roleId + " and app_id is null ";
			} else {
				filter = " where app_role_id = " + roleId + " and app_id = " + app.get(0).getId();

			}
			roleInfo = dataAccessService.getList(EPRole.class, filter, null, null);
			roleList = createCentralRoleObject(app, roleInfo, roleList, params);
			if (roleList.isEmpty()) {
				return cenRole;
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo failed", e);
			throw new Exception(e.getMessage());

		}
		return roleList.get(0);
	}

	@SuppressWarnings("unchecked")
	private List<CentralRole> createCentralRoleObject(List<EPApp> app, List<EPRole> roleInfo,
			List<CentralRole> roleList, Map<String, Long> params) {
		for (EPRole role : roleInfo) {
			params.put("roleId", role.getId());
			params.put("appId", app.get(0).getId());
			List<CentralRoleFunction> cenRoleFuncList = dataAccessService.executeNamedQuery("getAppRoleFunctionList",
					params, null);
			SortedSet<CentralRoleFunction> roleFunctionSet = new TreeSet<CentralRoleFunction>();
			for (CentralRoleFunction roleFunc : cenRoleFuncList) {
				CentralRoleFunction cenRoleFunc = new CentralRoleFunction(role.getId(), roleFunc.getCode(),
						roleFunc.getName(), null, null);
				roleFunctionSet.add(cenRoleFunc);
			}
			SortedSet<CentralRole> childRoles = new TreeSet<CentralRole>();
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
			params.put("appId", String.valueOf(app.getId()));
			getRoleFuncList = dataAccessService.executeNamedQuery("getRoleFunction", params, null);
			if (getRoleFuncList.isEmpty() | getRoleFuncList.size() == 0) {
				return roleFunc;
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", e);
			throw new Exception("getRoleFunction failed", e);
		}
		return getRoleFuncList.get(0);
	}

	@Override
	public void saveCentralRoleFunction(CentralRoleFunction domainCentralRoleFunction, EPApp app) throws Exception {
		try {
			addRoleFunctionInExternalSystem(domainCentralRoleFunction, app);
			dataAccessService.saveDomainObject(domainCentralRoleFunction, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveCentralRoleFunction failed", e);
			throw new Exception(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void addRoleFunctionInExternalSystem(CentralRoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		final Map<String, String> params = new HashMap<>();
		params.put("functionCd", domainCentralRoleFunction.getCode());
		params.put("appId", String.valueOf(app.getId()));
		ExternalAccessPerms extPerms = new ExternalAccessPerms();
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		List<CentralRoleFunction> appRoleFunc = dataAccessService.executeNamedQuery("getAppFunctionDetails", params,
				null);
		String roleFuncName = null;
		if (!appRoleFunc.isEmpty()) {
			roleFuncName = appRoleFunc.get(0).getCode();
		} else {
			roleFuncName = domainCentralRoleFunction.getCode();
		}
		String checkType = domainCentralRoleFunction.getCode().contains("menu") ? "menu" : "url";
		HttpEntity<String> getSinglePermEntity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		ResponseEntity<String> getResponse = template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perms/"
						+ app.getNameSpace() + "." + checkType + "/" + roleFuncName + "/*",
				HttpMethod.GET, getSinglePermEntity, String.class);
		if (getResponse.getStatusCode().value() != 200) {
			EPLogUtil.logExternalAuthAccessAlarm(logger, getResponse.getStatusCode());
			throw new Exception(getResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
		String res = getResponse.getBody();
		if (res.equals("{}")) {
			try{
			extPerms.setAction("*");
			extPerms.setInstance(domainCentralRoleFunction.getCode());
			extPerms.setType(app.getNameSpace() + "." + checkType);
			extPerms.setDescription(domainCentralRoleFunction.getName());
			String updateRole = mapper.writeValueAsString(extPerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
					HttpMethod.POST, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
			}catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			}catch(Exception e){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to add function in external central auth system", e);
			}
		} else {
			try{
			extPerms.setAction("*");
			extPerms.setInstance(domainCentralRoleFunction.getCode());
			extPerms.setType(app.getNameSpace() + "." + checkType);
			extPerms.setDescription(domainCentralRoleFunction.getName());
			String updateRole = mapper.writeValueAsString(extPerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
			template.exchange(
					SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
					HttpMethod.PUT, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
			}catch(HttpClientErrorException e){
				logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to add function in external central auth system", e);
				EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
			}catch(Exception e){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to add function in external central auth system", e);

			}
		}
	}

	@Override
	@Transactional
	public void deleteCentralRoleFunction(String code, String uebkey) {
		try {
			EPApp app = getApp(uebkey).get(0);
			final Map<String, String> params = new HashMap<>();
			params.put("functionCd", code);
			params.put("appId", String.valueOf(app.getId()));
			CentralRoleFunction domainCentralRoleFunction = (CentralRoleFunction) dataAccessService.executeNamedQuery("getAppFunctionDetails", params, null).get(0);
 			deleteRoleFunctionInExternalSystem(domainCentralRoleFunction, app);
 			//Delete role function dependecy records
 			deleteAppRoleFunctions(code, app);
			dataAccessService.deleteDomainObject(domainCentralRoleFunction, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteCentralRoleFunction failed", e);
		}
	}

	private void deleteAppRoleFunctions(String code, EPApp app) {
			dataAccessService.deleteDomainObjects(EPAppRoleFunction.class, " app_id = "+app.getId()+" and function_cd = '"+ code +"'", null);
	}

	private void deleteRoleFunctionInExternalSystem(CentralRoleFunction domainCentralRoleFunction, EPApp app)
			throws Exception {
		try{
		ObjectMapper mapper = new ObjectMapper();
		ExternalAccessPerms extPerms = new ExternalAccessPerms();
		String checkType = domainCentralRoleFunction.getCode().contains("menu") ? "menu" : "url";
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		extPerms.setAction("*");
		extPerms.setInstance(domainCentralRoleFunction.getCode());
		extPerms.setType(app.getNameSpace() + "." + checkType);
		extPerms.setDescription(domainCentralRoleFunction.getName());
		String updateRole = mapper.writeValueAsString(extPerms);
		HttpEntity<String> entity = new HttpEntity<>(updateRole, headers);
		template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm?force=true",
				HttpMethod.DELETE, entity, String.class);
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to delete functions in External System", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("404 Not Found")){
			logger.debug(EELFLoggerDelegate.debugLogger, " It seems like function is already deleted in external central auth system  but exists in local DB", e.getMessage());
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to delete functions in External System", e);
			}
		}
	}

	@Override
	public void saveRoleForApplication(Role saveRole, String uebkey) throws Exception {
		try {
			EPApp app = getApp(uebkey).get(0);
			addRoleInEcompDB(saveRole, app);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleForApplication failed", e);
			throw new Exception(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteRoleForApplication(String deleteRole, String uebkey) throws Exception {
		Session localSession = null;
		Transaction transaction = null;
		boolean result = false;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();

			List<EPRole> epRoleList = null;
			ResponseEntity<String> deleteResponse = null;
			EPApp app = getApp(uebkey).get(0);
			if(app.getId() == 1)
			{
				epRoleList = dataAccessService.getList(EPRole.class,
						" where app_id is null " + "and role_name = '" + deleteRole +"'", null, null);
			}
			else{
			epRoleList = dataAccessService.getList(EPRole.class,
					" where app_id = " + app.getId() + " and role_name = '" + deleteRole +"'", null, null);
			}
			// Delete app role functions before deleting role
			deleteRoleFunction(app, epRoleList);
			if(app.getId() == 1)
			{
				// Delete fn_user_ role
				dataAccessService.deleteDomainObjects(EPUserApp.class,
						" app_id = " + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);
				
				deleteRoleDependeciesRecord(localSession, epRoleList.get(0).getId());
			}
			// Delete Role in External System
			String deleteRoleKey = "{\"name\":\"" + app.getNameSpace() + "."
					+ epRoleList.get(0).getName().replaceAll(" ", "_") + "\"}";
			deleteResponse = deleteRoleInExternalSystem(deleteRoleKey);
			if (deleteResponse.getStatusCode().value() != 200) {
				EPLogUtil.logExternalAuthAccessAlarm(logger, deleteResponse.getStatusCode());
				throw new Exception("Failed to delete role in external access system!");
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "about to commit the transaction");
			transaction.commit();
			logger.debug(EELFLoggerDelegate.debugLogger, "committed the transaction");
			dataAccessService.deleteDomainObject(epRoleList.get(0), null);
			result = true;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleForApplication failed", e);
			throw new Exception(e.getMessage());
		}finally {
			localSession.close();
			if (!result) {
				throw new Exception(
						"Exception occurred in deleteRoleForApplication while closing database session for role: '" + deleteRole + "'.");
			}
		}
	}

	private void deleteUserRoleInExternalSystem(EPRole role, EPApp app, String LoginId) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		ResponseEntity<String> getResponse = template
				.exchange(
						SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole/"
								+ LoginId
								+ SystemProperties
										.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
								+ "/" + app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"),
						HttpMethod.GET, entity, String.class);
		if (getResponse.getStatusCode().value() != 200) {
			throw new Exception(getResponse.getBody());
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
		String res = getResponse.getBody();
		if (!res.equals("{}")) {
			HttpEntity<String> userRoleentity = new HttpEntity<>(headers);
			logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
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
			logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
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
					" where app_id = " + appId + " and active_yn = 'Y'", null, null);
			roleList = createCentralRoleObject(app, epRole, roleList, params);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
			throw new Exception(e.getMessage());
		}
		return roleList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDependcyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception {
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		EPApp app = null;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			List<EPRole> epRoleList = null;
			app = getApp(uebkey).get(0);
			epRoleList = dataAccessService.getList(EPRole.class,
					" where app_id = " + app.getId() + " and app_role_id = " + roleId, null, null);
			if(epRoleList.isEmpty()){
				epRoleList = dataAccessService.getList(EPRole.class,
						" where app_id = " + app.getId() + " and role_id = " + roleId, null, null);
			}
			// Delete User Role in External System before deleting role
			deleteUserRoleInExternalSystem(epRoleList.get(0), app, LoginId);
			// Delete user app roles
			dataAccessService.deleteDomainObjects(EPUserApp.class,
					" app_id = " + app.getId() + " and role_id = " + epRoleList.get(0).getId(), null);
			
			deleteRoleDependeciesRecord(localSession, epRoleList.get(0).getId());
			logger.debug(EELFLoggerDelegate.debugLogger, "about to commit the transaction");
			transaction.commit();
			logger.debug(EELFLoggerDelegate.debugLogger, "committed the transaction");
			result = true;
		}catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to deleteRoleDependeciesRecord", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		}catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction,
					"deleteDependcyRoleRecord rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}finally {
			localSession.close();
			if (!result) {
				throw new Exception(
						"Exception occurred in syncAppRoles while closing database session for role: '" + app.getId() + "'.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void syncRoleFunctionFromExternalAccessSystem(EPApp app){
		try{
		ResponseEntity<String> response = null;
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		response = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
				+ "perms/ns/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);

		String res = response.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system and the result is :", res);
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
			if (extPerms.getJSONObject(i).has("roles")) {
				ObjectMapper rolesListMapper = new ObjectMapper();
				JSONArray resRoles = extPerms.getJSONObject(i).getJSONArray("roles");
				List<String> list = rolesListMapper.readValue(resRoles.toString(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
				permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
						extPerms.getJSONObject(i).getString("instance"), extPerms.getJSONObject(i).getString("action"),
						list, extPerms.getJSONObject(i).getString("description"));
				permsDetailList.add(permDetails);
			} else {
				permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
						extPerms.getJSONObject(i).getString("instance"), extPerms.getJSONObject(i).getString("action"),
						extPerms.getJSONObject(i).getString("description"));
				permsDetailList.add(permDetails);
			}
		}

		final Map<String, Long> params = new HashMap<>();
		final Map<String, CentralRoleFunction> roleFuncMap = new HashMap<>();
		params.put("appId", app.getId());
		List<CentralRoleFunction> appFunctions = dataAccessService.executeNamedQuery("getAllRoleFunctions", params,
				null);
		if (appFunctions.size() > 0) {
			for (CentralRoleFunction roleFunc : appFunctions) {
				roleFuncMap.put(roleFunc.getCode(), roleFunc);
			}
		}
		// delete all application role functions
		dataAccessService.deleteDomainObjects(EPAppRoleFunction.class, " app_id = " + app.getId(), null);
		
		// Add if new functions and app role functions were added in Externalsystem
		for (ExternalAccessPermsDetail permsDetail : permsDetailList) {
			if (!roleFuncMap.containsKey(permsDetail.getInstance())) {
				CentralRoleFunction addFunction = new CentralRoleFunction();
				addFunction.setAppId(app.getId());
				addFunction.setCode(permsDetail.getInstance());
				addFunction.setName(permsDetail.getDescription());
				dataAccessService.saveDomainObject(addFunction, null);
			}
				List<EPRole> epRolesList = null;
				List<String> roles = permsDetail.getRoles();
				if (roles != null) {
				for (String roleList : roles) {
					if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
						epRolesList = dataAccessService.getList(EPRole.class,
								" where app_id is null " + " and role_name = '"
										+ roleList.substring(app.getNameSpace().length() + 1).replaceAll("_", " ") +"'",
								null, null);
					} else {
						epRolesList = dataAccessService.getList(EPRole.class,
								" where app_id = " + app.getId() + " and role_name = '"
										+ roleList.substring(app.getNameSpace().length() + 1).replaceAll("_", " ") +"'",
								null, null);
					}
					if(epRolesList.isEmpty()){
						if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
							epRolesList = dataAccessService.getList(EPRole.class,
									" where app_id is null " + " and role_name = '"
											+ roleList.substring(app.getNameSpace().length() + 1)
											+ "'",
									null, null);
						} else {
							epRolesList = dataAccessService.getList(EPRole.class,
									" where app_id = " + app.getId() + " and role_name = '"
											+ roleList.substring(app.getNameSpace().length() + 1)+"'",
									null, null);
						}
					}
					// save all application role functions
					if (epRolesList.size() > 0 || !epRolesList.isEmpty()) {
						EPAppRoleFunction addAppRoleFunc = new EPAppRoleFunction();
						addAppRoleFunc.setAppId(app.getId());
						addAppRoleFunc.setCode(permsDetail.getInstance());
						addAppRoleFunc.setRoleId(epRolesList.get(0).getId());
						dataAccessService.saveDomainObject(addAppRoleFunc, null);
					}
				}
			}
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished syncRoleFunctionFromExternalAccessSystem");
		} catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed syncRoleFunctionFromExternalAccessSystem", e);

		}
	}
	
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
		}catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - bulkUploadFunctions failed", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		}catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e.getMessage(), e);
		}
		return functionsAdded;
	}

	public Integer bulkUploadRoles(String uebkey) throws Exception {
		List<EPApp> app = getApp(uebkey);
		List<EPRole> roles = getAppRoles(app.get(0).getId(), null);
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
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
			throw new Exception(e.getMessage());
		}
		return rolesListAdded;
	}
	
	private void addRoleInExternalSystem(Role role, EPApp app) throws Exception {
		String addRoleNew = createNewRoleInExternalSystem(role, app);
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		try{
		HttpEntity<String> entity = new HttpEntity<>(addRoleNew, headers);
		template.exchange(
				SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
				HttpMethod.POST, entity, String.class);
		}catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addRoleInExternalSystem", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		}catch(Exception e){
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.error(EELFLoggerDelegate.errorLogger, "Role already exits but does not break functionality");
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to addRoleInExternalSystem", e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Integer bulkUploadRolesFunctions(String uebkey) throws Exception {
		EPApp app = getApp(uebkey).get(0);
		List<EPRole> roles = getAppRoles(app.getId(), null);
		final Map<String, Long> params = new HashMap<>();
		Integer roleFunctions = 0;
		try {
			for (EPRole role : roles) {
				params.put("roleId", role.getId());
				List<BulkUploadRoleFunction> appRoleFunc = dataAccessService.executeNamedQuery("uploadAllRoleFunctions", params, null);
				if(!appRoleFunc.isEmpty()){
					for(BulkUploadRoleFunction addRoleFunc : appRoleFunc){
						addRoleFunctionsInExternalSystem(addRoleFunc, role, app);
						roleFunctions++;
					}
				}
			}
		} catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to bulkUploadRolesFunctions", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRolesFunctions failed", e);
		}
		return roleFunctions;
	}

	private void addRoleFunctionsInExternalSystem(BulkUploadRoleFunction addRoleFunc, EPRole role, EPApp app){
			String checkType = addRoleFunc.getFunctionCd().contains("menu") ? "menu" : "url";
			ExternalAccessRolePerms extRolePerms = null;
			ExternalAccessPerms extPerms = null;
			ObjectMapper mapper = new ObjectMapper();
			try{
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();

			extPerms = new ExternalAccessPerms(app.getNameSpace() + "." + checkType, addRoleFunc.getFunctionCd(), "*", addRoleFunc.getFunctionName());
			extRolePerms = new ExternalAccessRolePerms(extPerms,
					app.getNameSpace() + "." + role.getName().replaceAll(" ", "_"));
			String updateRolePerms = mapper.writeValueAsString(extRolePerms);
			HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
			template
					.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "role/perm", HttpMethod.POST, entity, String.class);
			} catch(Exception e){
				if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
					logger.error(EELFLoggerDelegate.errorLogger, "RoleFunction already exits but does not break functionality",e);
				} else {
					logger.error(EELFLoggerDelegate.errorLogger, "Failed to addRoleFunctionsInExternalSystem", e.getMessage());
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
							"RoleFunction already exits but does not break functionality");
				} else {
					logger.error(EELFLoggerDelegate.errorLogger, "Failed to addRoleFunctionsInExternalSystem",
							e.getMessage());
				}
			}

		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public void SyncApplicationRolesWithEcompDB(EPApp app){
		try{
		ResponseEntity<String> response = null;
		List<EPRole> finalRoleList = new ArrayList<>();
		ExternalRoleDescription ApplicationRole = new ExternalRoleDescription();
		ExternalAccessPerms externalAccessPerms = null;
		List<String> functionCodelist = new ArrayList<>();
		List<ExternalRoleDetails> externalRoleDetailsList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
		response = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
				+ "roles/ns/" + app.getNameSpace(), HttpMethod.GET, entity, String.class);
		String res = response.getBody();
		logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system and the result is :", res);
		JSONObject jsonObj = new JSONObject(res);
		JSONArray extRole = jsonObj.getJSONArray("role");
		for (int i = 0; i < extRole.length(); i++) {
			if (extRole.getJSONObject(i).getString("name").equals(app.getNameSpace() + ".admin")
					|| extRole.getJSONObject(i).getString("name").equals(app.getNameSpace() + ".owner")
					) {
				extRole.remove(i);
				i--;
			}
			if(!app.getId().equals(PortalConstants.PORTAL_APP_ID) && extRole.getJSONObject(i).get("name").equals(app.getNameSpace()+"."+PortalConstants.ADMIN_ROLE.replaceAll(" ", "_"))){
				extRole.remove(i);
				i--;
			}
		}
		List<EPAppRoleFunction> applicationRoleFunctionList = new ArrayList<>();
		for (int i = 0; i < extRole.length(); i++) {
			ExternalRoleDetails externalRoleDetail = new ExternalRoleDetails();
			EPAppRoleFunction ePAppRoleFunction = new EPAppRoleFunction();
			JSONObject Role = (JSONObject) extRole.get(i);
			if(!extRole.getJSONObject(i).has("description"))
			{
				ApplicationRole.setActive("true");
					ApplicationRole.setAppId("null");
				ApplicationRole.setPriority("null");
				ApplicationRole.setAppRoleId("null");
				String roleName =extRole.getJSONObject(i).getString("name");
				ApplicationRole.setName(roleName.substring(app.getNameSpace().length()+1));
			}
			else {
			String desc = extRole.getJSONObject(i).getString("description");
			ApplicationRole = mapper.readValue(desc, ExternalRoleDescription.class);
			}
			

			SortedSet<ExternalAccessPerms> externalAccessPermsOfRole = new TreeSet<>();
			if (extRole.getJSONObject(i).has("perms")) {
				JSONArray extPerm = (JSONArray) Role.get("perms");
				for (int j = 0; j < extPerm.length(); j++) {
					JSONObject perms = extPerm.getJSONObject(j);
					externalAccessPerms = new ExternalAccessPerms(perms.getString("type"), perms.getString("instance"),
							perms.getString("action"));
					ePAppRoleFunction.setCode(externalAccessPerms.getInstance());
					functionCodelist.add(ePAppRoleFunction.getCode());
					externalAccessPermsOfRole.add(externalAccessPerms);
				}
			}

			if (ApplicationRole.getActive().equals("null")) {
				externalRoleDetail.setActive(false);
			} else {
				externalRoleDetail.setActive(Boolean.parseBoolean(ApplicationRole.getActive().toString()));
			}
			externalRoleDetail.setName(ApplicationRole.getName());

			if (ApplicationRole.getAppId().equals("null") && app.getId() == 1) {
				externalRoleDetail.setAppId(null);
			} else if(ApplicationRole.getAppId().equals("null")){
				externalRoleDetail.setAppId(app.getId());
			}else {
				externalRoleDetail.setAppId(Long.parseLong(ApplicationRole.getAppId().toString()));
			}

			if (ApplicationRole.getPriority().equals("null")) {
				externalRoleDetail.setPriority(null);
			} else {
				externalRoleDetail.setPriority(Integer.parseInt(ApplicationRole.getPriority().toString()));
			}

			if (ApplicationRole.getAppRoleId().equals("null") && app.getId() == 1) {
				externalRoleDetail.setAppRoleId(null);
			}

			if (!externalAccessPermsOfRole.isEmpty() || externalAccessPermsOfRole.size() > 0) {
				for (ExternalAccessPerms externalpermission : externalAccessPermsOfRole) {
					EPAppRoleFunction apRoleFunction = new EPAppRoleFunction();
					apRoleFunction.setAppId(app.getId());
					apRoleFunction.setRoleId(Long.parseLong(ApplicationRole.getId()));
					apRoleFunction.setCode(externalpermission.getInstance());
					applicationRoleFunctionList.add(apRoleFunction);
				}
			}
			externalRoleDetailsList.add(externalRoleDetail);
		}
			
		for (ExternalRoleDetails externalRole : externalRoleDetailsList) {
			EPRole ecompRole = new EPRole();
			ecompRole = convertExternalRoleDetailstoEpRole(externalRole);
			finalRoleList.add(ecompRole);
		}

		List<EPRole> applicationRolesList = new ArrayList<>();
		applicationRolesList = getAppRoles(app.getId(), null);
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
		for(EPRole extrole : finalRoleList){
			checkRolesInactive.put(extrole.getName(), extrole);
		}
			for (EPRole role : applicationRolesList) {
				final Map<String, String> extRoleParams = new HashMap<>();
				List<EPRole> roleList = new ArrayList<>();
				extRoleParams.put("appRoleName", role.getName());
				if (!checkRolesInactive.containsKey(role.getName())) {
					if (app.getId() == 1) {
						roleList = dataAccessService.executeNamedQuery("getPortalAppRoles", extRoleParams, null);
					} else {
						extRoleParams.put("appId", app.getId().toString());
						roleList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", extRoleParams, null);
					}
					EPRole updateRoleInactive = roleList.get(0);
					updateRoleInactive.setActive(false);
					dataAccessService.saveDomainObject(updateRoleInactive, null);					
				}
			}
		
			for (EPRole roleItem : finalRoleList) {
				final Map<String, String> roleParams = new HashMap<>();
				List<EPRole> currentList = new ArrayList<>();
				roleParams.put("appRoleName", roleItem.getName());
				if (app.getId() == 1) {
					currentList = dataAccessService.executeNamedQuery("getPortalAppRoles", roleParams, null);
				} else {
					roleParams.put("appId", app.getId().toString());
					currentList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", roleParams, null);
				}

				if (!currentList.isEmpty()) {
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
				}
			}

		EPRole roleToBeAddedInEcompDB = new EPRole();
		for (int i = 0; i < roleListToBeAddInEcompDB.size(); i++) {
			roleToBeAddedInEcompDB = roleListToBeAddInEcompDB.get(i);
			if(app.getId() == 1)
			{
				roleToBeAddedInEcompDB.setAppRoleId(null);
			}
			dataAccessService.saveDomainObject(roleToBeAddedInEcompDB, null);
			List <EPRole> getRoleCreatedInSync = null;
			if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
				getRoleCreatedInSync =  dataAccessService.getList(EPRole.class,
						" where role_name = '" + roleToBeAddedInEcompDB.getName() +"'", null, null);	
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
				params.put("appId", app.getId().toString());
				roleList = dataAccessService.executeNamedQuery("getRoletoUpdateAAF", params, null);
			}
			EPRole role = roleList.get(0);
			Role aaFrole = new Role();
			aaFrole.setId(role.getId());
			aaFrole.setActive(role.getActive());
			aaFrole.setPriority(role.getPriority());
			aaFrole.setName(role.getName());
			updateRoleInExternalSystem(aaFrole, app);
		 }
			dataAccessService.deleteDomainObjects(EPAppRoleFunction.class, " app_id = " + app.getId(), null);
			for (EPAppRoleFunction rolefun : applicationRoleFunctionList) {
				dataAccessService.saveDomainObject(rolefun, null);
			}
		
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished SyncApplicationRolesWithEcompDB");
		}catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to SyncApplicationRolesWithEcompDB", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to SyncApplicationRolesWithEcompDB", e);
		}
	}

	public EPRole convertExternalRoleDetailstoEpRole(ExternalRoleDetails externalRoleDetails) {
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

	private void addUserRoleInExternalSystem(BulkUploadUserRoles userRolesUpload){
		try{
		String name = "";
		ObjectMapper mapper = new ObjectMapper();
		if (EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
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
		}catch(HttpClientErrorException e){
			logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addUserRoleInExternalSystem", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
		}catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.error(EELFLoggerDelegate.errorLogger, "UserRole already exits but does not break functionality");
			} else {
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to addUserRoleInExternalSystem", e.getMessage());
			}
		}
	}

	@Override
	public void deleteRoleDependeciesRecord(Session localSession, Long roleId) throws Exception {
		try {
			// Delete from fn_role_function
			String sql = "DELETE FROM fn_role_function WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			Query query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
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
			
			// Delete from fn_role_composite
			sql = "DELETE FROM fn_role_composite WHERE parent_role_id=" + roleId + " OR child_role_id="
					+ roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Delete from fn_user_pseudo_role
			sql = "DELETE FROM fn_user_pseudo_role WHERE pseudo_role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			//Delete form EP_WIDGET_CATALOG_ROLE
			sql = "DELETE FROM EP_WIDGET_CATALOG_ROLE WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			//Delete form EP_WIDGET_CATALOG_ROLE
			sql = "DELETE FROM ep_user_roles_request_det WHERE requested_role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			//Delete form fn_menu_functional_roles
			sql = "DELETE FROM fn_menu_functional_roles WHERE role_id=" + roleId;
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleDependeciesRecord failed " , e);
			throw new Exception("delete Failed"+ e.getMessage());
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMenuFunctionsList(String uebkey) throws Exception {
		List<String> appMenuFunctionsList = null;
		try{
		EPApp app = getApp(uebkey).get(0);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		appMenuFunctionsList = dataAccessService.executeNamedQuery("getMenuFunctions", appParams, null);
		} catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed getMenuFunctionsList", e);
			return appMenuFunctionsList;
		}
		return appMenuFunctionsList;
	}
}
