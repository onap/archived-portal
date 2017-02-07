/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.transport.http.HTTPException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuRole;
import org.openecomp.portalapp.portal.transport.RemoteUserWithRoles;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;
import org.openecomp.portalapp.portal.transport.RolesInAppForUser;
import org.openecomp.portalapp.portal.transport.UserApplicationRoles;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.restful.domain.EcompRole;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.UserProfileService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("userRolesService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class UserRolesServiceImpl implements UserRolesService {
	private static Long ACCOUNT_ADMIN_ROLE_ID = 999L;

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRolesServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	SearchService searchService;
	@Autowired
	EPAppService appsService;
	@Autowired
	EPLdapService ldapService;
	@Autowired
	ApplicationsRestClientService applicationsRestClientService;
	@Autowired
	EPRoleService epRoleService;
	@Autowired
	UserProfileService userProfileService;
	
	@PostConstruct
	private void init() {
		try {
			ACCOUNT_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
	}
	
	private static HashMap<Long, EcompRole> hashMapFromEcompRoles(EcompRole[] ecompRoles) {
		HashMap<Long, EcompRole> result = new HashMap<Long, EcompRole>();
		if (ecompRoles!=null) {
			for (int i = 0; i < ecompRoles.length; i++) {
				if (ecompRoles[i].getId() != null) {
					result.put(ecompRoles[i].getId(), ecompRoles[i]);
				}
			}
		}
		return result;
	}

	private void createLocalUserIfNecessary(String orgUserId) {
		if (StringUtils.isEmpty(orgUserId)) {
			logger.error(EELFLoggerDelegate.errorLogger, "createLocalUserIfNecessary : empty orgUserId!");
			return;
		}
		Session localSession = null;
		Transaction transaction = null;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			@SuppressWarnings("unchecked")
			List<EPUser> userList = localSession.createQuery("from " + EPUser.class.getName() + " where org_user_id='" + orgUserId + "'").list();
			if (userList.size() == 0) {
				EPUser client = searchService.searchUserByUserId(orgUserId);
				if (client == null) {
					String msg = "cannot create user " + orgUserId + ", because he cannot be found in phonebook";
					logger.error(EELFLoggerDelegate.errorLogger, msg);
				} else {
					client.setLoginId(orgUserId);
					client.setActive(true);
					localSession.save(client);
				}
			}
			transaction.commit();
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			EcompPortalUtils.rollbackTransaction(transaction, "searchOrCreateUser rollback, exception = " + e);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "searchOrCreateUser");
		}
	}

	private static void syncUserRoles(SessionFactory sessionFactory, String orgUserId, Long appId, EcompRole[] userAppRoles) throws Exception {
		HashMap<Long, EcompRole> newUserAppRolesMap = hashMapFromEcompRoles(userAppRoles);
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			@SuppressWarnings("unchecked")
			List<EPUser> userList = localSession.createQuery("from " + EPUser.class.getName() + " where org_user_id='" + orgUserId + "'").list();
			if (userList.size() > 0) {
				EPUser client = userList.get(0);
				@SuppressWarnings("unchecked")
				List<EPUserApp> userRoles = localSession
						.createQuery("from " + EPUserApp.class.getName() + " where app.id=" + appId + " and userId=" + client.getId()).list();
				for (EPUserApp userRole : userRoles) {
						if (! userRole.getRoleId().equals(ACCOUNT_ADMIN_ROLE_ID)) {
					
							Long userAppRoleId = userRole.getAppRoleId(); 
							if (!newUserAppRolesMap.containsKey(userAppRoleId)) {
								localSession.delete(userRole);
							} else {
								newUserAppRolesMap.remove(userAppRoleId);
							}
						}
				}
				Collection<EcompRole> newRolesToAdd = newUserAppRolesMap.values();
				if (newRolesToAdd.size() > 0) {
					EPApp app = (EPApp) localSession.get(EPApp.class, appId);
					@SuppressWarnings("unchecked")
					List<EPRole> roles = localSession.createQuery("from " + EPRole.class.getName() + " where appId=" + appId).list();
					HashMap<Long, EPRole> rolesMap = new HashMap<Long, EPRole>();
					for (EPRole role : roles) {
						rolesMap.put(role.getAppRoleId(), role);
					}
					for (EcompRole userRole : newRolesToAdd) {
						EPUserApp userApp = new EPUserApp();
						userApp.setUserId(client.getId());
						userApp.setApp(app);
						userApp.setRole(rolesMap.get(userRole.getId()));
						localSession.save(userApp);
					}
				}
			}
			transaction.commit();
			result = true;
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			EcompPortalUtils.rollbackTransaction(transaction, "Exception occurred in syncUserRoles, Details: " + EcompPortalUtils.getStackTrace(e));
		} finally {
			localSession.close();
			if (!result) {
				throw new Exception("Exception occurred in syncUserRoles while closing database session for app: '" + appId + "'.");
			}
		}
	}

	// Called when getting the list of roles for the user
	private List<RoleInAppForUser> constructRolesInAppForUserGet(EcompRole[] appRoles, EcompRole[] userAppRoles) {
		List<RoleInAppForUser> rolesInAppForUser = new ArrayList<RoleInAppForUser>();

		Set<Long> userAppRolesMap = new HashSet<Long>();
		if (userAppRoles!=null) {
			for (EcompRole ecompRole : userAppRoles) {
				userAppRolesMap.add(ecompRole.getId());
			}
		} else {
			String message = String.format("UserRolesServiceImpl.constructRolesInAppForUserGet has received userAppRoles list empty.");
			logger.info(EELFLoggerDelegate.errorLogger, message);
		}

		if (appRoles!=null) {
			for (EcompRole ecompRole : appRoles) {
				RoleInAppForUser roleForUser = new RoleInAppForUser(ecompRole.getId(), ecompRole.getName());
				roleForUser.isApplied = userAppRolesMap.contains(ecompRole.getId());
				rolesInAppForUser.add(roleForUser);
			}
		} else {
			String message = String.format("UserRolesServiceImpl.constructRolesInAppForUser has received appRoles list empty.");
			logger.info(EELFLoggerDelegate.errorLogger, message);
		}
		return rolesInAppForUser;
	}
	public List<RoleInAppForUser> getAppRolesForUser(Long appId, String orgUserId) {
		List<RoleInAppForUser> rolesInAppForUser = null;
		try {
			EcompRole[] appRoles = applicationsRestClientService.get(EcompRole[].class, appId, "/roles");

			// Test this error case, for generating an internal Ecomp Portal error
//			EcompRole[] appRoles = null;
			// If there is an exception in the rest client api, then null will be returned.
			if (appRoles != null) {
				syncAppRoles(sessionFactory, appId, appRoles);
				EcompRole[] userAppRoles;
				try {
					userAppRoles = applicationsRestClientService.get(EcompRole[].class, appId, String.format("/user/%s/roles", orgUserId));
					if (userAppRoles == null) {
						if (EcompPortalUtils.getExternalAppResponseCode() == 400) {
						    EcompPortalUtils.setExternalAppResponseCode(200);
						    logger.error(EELFLoggerDelegate.errorLogger, "400 returned from /user/{userid}/roles, assuming user doesn't exist, app is framework SDK based, and things are ok. Overriding to 200 until framework SDK returns a useful response.");
						    logger.debug(EELFLoggerDelegate.debugLogger, "400 returned from /user/{userid}/roles, assuming user doesn't exist, app is framework SDK based, and things are ok. Overriding to 200 until framework SDK returns a useful response.");
						}
					}
					// If the remote application isn't down we MUST to sync user roles here in case we have this user here!
					syncUserRoles(sessionFactory, orgUserId, appId, userAppRoles);
				} catch (Exception e) {
					// TODO: we may need to check if user exists, maybe remote app is down.
					logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
					logger.error(EELFLoggerDelegate.errorLogger, "LR: user " + orgUserId + " does not exist in remote application: " + appId + ".");
					userAppRoles = new EcompRole[0];
				}
				rolesInAppForUser = constructRolesInAppForUserGet(appRoles, userAppRoles);
				// Test this error case, for generating an external app error
//				EcompPortalUtils.setResponseCode(404);
			}
		} catch (Exception e) {
			String message = String.format("Received an exception while performing getAppRolesForUser for the User %s, and for the AppId %s, Details: %s", 
											orgUserId, Long.toString(appId), EcompPortalUtils.getStackTrace(e));
			logger.error(EELFLoggerDelegate.errorLogger, message);
		}
		return rolesInAppForUser;

	}
	
	// copies of methods in GetAppsWithUserRoleState
	private void syncAppRoles(SessionFactory sessionFactory, Long appId, EcompRole[] appRoles) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "entering syncAppRoles for appId: "+appId);
		HashMap<Long, EcompRole> newRolesMap = hashMapFromEcompRoles(appRoles);
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			// Attention! All roles from remote application supposed to be active!
			@SuppressWarnings("unchecked")
			List<EPRole> currentAppRoles = localSession.createQuery("from " + EPRole.class.getName() + " where appId=" + appId).list();
			List<EPRole> obsoleteRoles = new ArrayList<EPRole>();
			for (int i = 0; i < currentAppRoles.size(); i++) {
				EPRole oldAppRole = currentAppRoles.get(i);
				if (oldAppRole.getAppRoleId() != null) {
					EcompRole role = null;
					role = newRolesMap.get(oldAppRole.getAppRoleId());
					if (role != null) {
						if (!(role.getName() == null || oldAppRole.getName().equals(role.getName()))) {
							oldAppRole.setName(role.getName());
							localSession.update(oldAppRole);
						}
						newRolesMap.remove(oldAppRole.getAppRoleId());
					} else {
						obsoleteRoles.add(oldAppRole);
					}
				} else {
					obsoleteRoles.add(oldAppRole);
				}
			}
			Collection<EcompRole> newRolesToAdd = newRolesMap.values();
			for (EcompRole role : newRolesToAdd) {
				logger.debug(EELFLoggerDelegate.debugLogger, "about to add missing role: "+role.toString());
				EPRole newRole = new EPRole();
				// Attention! All roles from remote application supposed to be active!
				newRole.setActive(true);
				newRole.setName(role.getName());
				newRole.setAppId(appId);
				newRole.setAppRoleId(role.getId());
				localSession.save(newRole);
			}
			if (obsoleteRoles.size() > 0) {
				logger.debug(EELFLoggerDelegate.debugLogger, "we have obsolete roles to delete");
				for (EPRole role : obsoleteRoles) {
					logger.debug(EELFLoggerDelegate.debugLogger, "obsolete role: "+role.toString());
					Long roleId = role.getId();
				     // delete obsolete roles here
					// Must delete all records with foreign key constraints on fn_role:
					// fn_user_role, fn_role_composite, fn_role_function, fn_user_pseudo_role, fn_menu_functional_roles.
					// And for fn_menu_functional, if no other roles for that menu item, remove the url.
					
					// Delete from fn_user_role
					 @SuppressWarnings("unchecked")
					List<EPUserApp> userRoles = localSession
								.createQuery("from " + EPUserApp.class.getName() + " where app.id=" + appId + " and role_id=" + roleId).list();
					
					logger.debug(EELFLoggerDelegate.debugLogger, "number of userRoles to delete: "+userRoles.size());
					for (EPUserApp userRole : userRoles) {
						logger.debug(EELFLoggerDelegate.debugLogger, "about to delete userRole: "+userRole.toString());
						localSession.delete(userRole);
						logger.debug(EELFLoggerDelegate.debugLogger, "finished deleting userRole: "+userRole.toString());
					}
					
					// Delete from fn_menu_functional_roles
					@SuppressWarnings("unchecked")
					List<FunctionalMenuRole> funcMenuRoles = localSession
						.createQuery("from " + FunctionalMenuRole.class.getName() + " where roleId=" + roleId).list();
					int numMenuRoles = funcMenuRoles.size();
					logger.debug(EELFLoggerDelegate.debugLogger, "number of funcMenuRoles for roleId: "+roleId+": "+numMenuRoles);
					for (FunctionalMenuRole funcMenuRole : funcMenuRoles) {
						Long menuId = funcMenuRole.menuId;
						// If this is the only role for this menu item, then the app and roles will be gone,
						// so must null out the url too, to be consistent
						@SuppressWarnings("unchecked")
						List<FunctionalMenuRole> funcMenuRoles2 = localSession
							.createQuery("from " + FunctionalMenuRole.class.getName() + " where menuId=" + menuId).list();
						int numMenuRoles2 = funcMenuRoles2.size();
						logger.debug(EELFLoggerDelegate.debugLogger, "number of funcMenuRoles for menuId: "+menuId+": "+numMenuRoles2);
						localSession.delete(funcMenuRole);
						if (numMenuRoles2 == 1) {
							// If this is the only role for this menu item, then the app and roles will be gone,
							// so must null out the url too, to be consistent
							logger.debug(EELFLoggerDelegate.debugLogger, "There is exactly 1 menu item for this role, so emptying the url");
							@SuppressWarnings("unchecked")
							List<FunctionalMenuItem> funcMenuItems = localSession
									.createQuery("from " + FunctionalMenuItem.class.getName() + " where menuId=" + menuId).list();
							if (funcMenuItems.size() > 0) {
								logger.debug(EELFLoggerDelegate.debugLogger, "got the menu item");
								FunctionalMenuItem funcMenuItem = funcMenuItems.get(0);
								funcMenuItem.url = "";
								localSession.update(funcMenuItem);
							}
						}
					}
					
					// Delete from fn_role_function
					String sql = "DELETE FROM fn_role_function WHERE role_id="+roleId;
					logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
					Query query = localSession.createSQLQuery(sql);
					query.executeUpdate();

					// Delete from fn_role_composite
					sql = "DELETE FROM fn_role_composite WHERE parent_role_id="+roleId+" OR child_role_id="+roleId;
					logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
					query = localSession.createSQLQuery(sql);
					query.executeUpdate();

					// Delete from fn_user_pseudo_role
					sql = "DELETE FROM fn_user_pseudo_role WHERE pseudo_role_id="+roleId;
					logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
					query = localSession.createSQLQuery(sql);
					query.executeUpdate();
					
					logger.debug(EELFLoggerDelegate.debugLogger, "about to delete the role: "+role.toString());
					localSession.delete(role);
					logger.debug(EELFLoggerDelegate.debugLogger, "deleted the role");
				}
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "about to commit the transaction");
			transaction.commit();
			logger.debug(EELFLoggerDelegate.debugLogger, "committed the transaction");
			result = true;
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			EcompPortalUtils.rollbackTransaction(transaction, "Exception occurred in syncAppRoles, Details: " + EcompPortalUtils.getStackTrace(e));
		} finally {
			localSession.close();
			if (!result) {
				throw new Exception("Exception occurred in syncAppRoles while closing database session for app: '" + appId + "'.");
			}
		}
	}

	// Called when updating the list of roles for the user
	private RolesInAppForUser constructRolesInAppForUserUpdate(String orgUserId, Long appId, Set<EcompRole> userRolesInRemoteApp) {
		RolesInAppForUser result;
		result = new RolesInAppForUser();
		result.appId = appId;
		result.orgUserId = orgUserId;
		for (EcompRole role : userRolesInRemoteApp) {
			RoleInAppForUser roleInAppForUser = new RoleInAppForUser();
			roleInAppForUser.roleId = role.getId();
			roleInAppForUser.roleName = role.getName();
			roleInAppForUser.isApplied = new Boolean(true);
			result.roles.add(roleInAppForUser);
		}
		return result;
	}

	private EPUser getUserFromRemoteApp(String orgUserId, EPApp app, ApplicationsRestClientService applicationsRestClientService) throws HTTPException {
		EPUser user = applicationsRestClientService.get(EPUser.class, app.getId(), String.format("/user/%s", orgUserId));
		return user;
	}

	private boolean remoteUserShouldBeCreated(List<RoleInAppForUser> roleInAppForUserList) {
		for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
			if (roleInAppForUser.isApplied.booleanValue()) {
				return true;
			}
		}
		return false;
	}

	private Set<EcompRole> postUsersRolesToRemoteApp(List<RoleInAppForUser> roleInAppForUserList, ObjectMapper mapper,
			ApplicationsRestClientService applicationsRestClientService, Long appId, String orgUserId) throws JsonProcessingException, HTTPException {
		Set<EcompRole> updatedUserRoles = constructUsersEcompRoles(roleInAppForUserList);
		String userRolesAsString = mapper.writeValueAsString(updatedUserRoles);
		applicationsRestClientService.post(EcompRole.class, appId, userRolesAsString, String.format("/user/%s/roles", orgUserId));
		// TODO: We should add code that verifies that the post operation did succeed. Because the SDK may still return 200 OK with an html page even when it fails!
		return updatedUserRoles;
	}

	private Set<EcompRole> constructUsersEcompRoles(List<RoleInAppForUser> roleInAppForUserList) {
		Set<EcompRole> existingUserRoles = new TreeSet<EcompRole>();
		for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
			if (roleInAppForUser.isApplied) {
				EcompRole ecompRole = new EcompRole();
				ecompRole.setId(roleInAppForUser.roleId);
				ecompRole.setName(roleInAppForUser.roleName);
				existingUserRoles.add(ecompRole);
			}
		}
		return existingUserRoles;
	}

	private static void createNewUserOnRemoteApp(String orgUserId, EPApp app, ApplicationsRestClientService applicationsRestClientService,
			SearchService searchService, ObjectMapper mapper) throws Exception {
		EPUser client = searchService.searchUserByUserId(orgUserId);
		if (client == null) {
			String msg = "cannot create user " + orgUserId + ", because he/she cannot be found in phonebook.";
			logger.error(EELFLoggerDelegate.errorLogger, msg);
			throw new Exception(msg);
		}
		client.setLoginId(orgUserId);
		client.setActive(true);
		// The remote doesn't care about other apps, and this has caused serialization problems - infinite recursion.
		client.getEPUserApps().clear();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String userAsString = mapper.writeValueAsString(client);
		logger.debug(EELFLoggerDelegate.debugLogger, "about to post new client to remote application, users json = " + userAsString);
		applicationsRestClientService.post(EPUser.class, app.getId(), userAsString, String.format("/user", orgUserId));
	}
	
	public String updateRemoteUserProfile(String orgUserId, Long appId){
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		EPUser client = searchService.searchUserByUserId(orgUserId);
		EPUser newUser = new EPUser();
		newUser.setActive(client.getActive());
		newUser.setFirstName(client.getFirstName());
		newUser.setLastName(client.getLastName());
		newUser.setLoginId(client.getLoginId());
		newUser.setLoginPwd(client.getLoginPwd());
		newUser.setMiddleInitial(client.getMiddleInitial());
		newUser.setEmail(client.getEmail());
		newUser.setOrgUserId(client.getLoginId());
		try {
			String userAsString = mapper.writeValueAsString(newUser);
			List<EPApp> appList = appsService.getUserRemoteApps(client.getId().toString());
			//applicationsRestClientService.post(EPUser.class, appId, userAsString, String.format("/user", orgUserId));
			for(EPApp eachApp : appList){
				try{
			         applicationsRestClientService.post(EPUser.class, eachApp.getId(), userAsString, String.format("/user/%s", orgUserId));
				}catch(Exception e){
					logger.error(EELFLoggerDelegate.errorLogger, "Failed to update user: " + client.getOrgUserId() + " in remote app. appId = " + eachApp.getId());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure"; 
		}
		
		return "success";
		
	}

	private static final Object syncRests = new Object();

	@Override
	public boolean setAppWithUserRoleStateForUser(EPUser user, AppWithRolesForUser newAppRolesForUser) {
		boolean result = false;
		String orgUserId = "";
		if (newAppRolesForUser != null && newAppRolesForUser.orgUserId != null) {
			orgUserId = newAppRolesForUser.orgUserId.trim();
		}
		Long appId = newAppRolesForUser.appId;
		List<RoleInAppForUser> roleInAppForUserList = newAppRolesForUser.appRoles;
		if (orgUserId.length() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			try {
				EPApp app = appsService.getApp(appId);
				EPUser remoteAppUser = getUserFromRemoteApp(orgUserId, app, applicationsRestClientService);
				if (remoteAppUser == null) {
					if (remoteUserShouldBeCreated(roleInAppForUserList)) {
						createNewUserOnRemoteApp(orgUserId, app, applicationsRestClientService, searchService, mapper);
						// If we succeed, we know that the new user was persisted on remote app.
						remoteAppUser = getUserFromRemoteApp(orgUserId, app, applicationsRestClientService);
						if (remoteAppUser == null) {
							logger.error(EELFLoggerDelegate.errorLogger, "Failed to persist new user: " + orgUserId + " in remote app. appId = " + appId);
//							return null;
						}
					}
				}
				if (remoteAppUser != null) {
					Set<EcompRole> userRolesInRemoteApp = postUsersRolesToRemoteApp(roleInAppForUserList, mapper, applicationsRestClientService, appId, orgUserId);
					RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(orgUserId, appId, userRolesInRemoteApp);
					result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser);
				}
			} catch (Exception e) {
				String message = String.format("Failed to create user or update user roles for the User %s, and for the AppId %s, Details: %s", 
						orgUserId, Long.toString(appId), EcompPortalUtils.getStackTrace(e));
				logger.error(EELFLoggerDelegate.errorLogger, message);
				result = false;
			}

		}
		return result;
	}

	// This is for a single app
	private boolean applyChangesInUserRolesForAppToEcompDB(RolesInAppForUser rolesInAppForUser) {
		boolean result = false;
		String orgUserId = rolesInAppForUser.orgUserId;
		Long appId = rolesInAppForUser.appId;
		synchronized (syncRests) {
			if (rolesInAppForUser != null) {
				createLocalUserIfNecessary(orgUserId);
			}
			
			if (rolesInAppForUser != null) {
				EcompRole[] userAppRoles = new EcompRole[rolesInAppForUser.roles.size()];
				for (int i = 0; i < rolesInAppForUser.roles.size(); i++) {
					RoleInAppForUser roleInAppForUser = rolesInAppForUser.roles.get(i);
					EcompRole role = new EcompRole();
					role.setId(roleInAppForUser.roleId);
					role.setName(roleInAppForUser.roleName);
					userAppRoles[i] = role;
				}
				try {
					syncUserRoles(sessionFactory, orgUserId, appId, userAppRoles);
					result = true;
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "applyChangesInUserRolesForAppToEcompDB syncUserRoles, orgUserId = " + orgUserId);
					logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}

	@Override
	public List<UserApplicationRoles> getUsersFromAppEndpoint(Long appId) throws HTTPException {
		RemoteUserWithRoles[] remoteUsers = applicationsRestClientService.get(RemoteUserWithRoles[].class, appId, "/users");
		ArrayList<UserApplicationRoles> userApplicationRoles = new ArrayList<UserApplicationRoles>();
		for (RemoteUserWithRoles remoteUser : remoteUsers) {
			UserApplicationRoles userWithRemoteAppRoles = convertToUserApplicationRoles(appId, remoteUser);
			if(userWithRemoteAppRoles.roles!=null && userWithRemoteAppRoles.roles.size()>0) {
				userApplicationRoles.add(userWithRemoteAppRoles);
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "User " + userWithRemoteAppRoles.orgUserId + " doesn't have any roles assigned to any app.");
			}
		}
		
		return userApplicationRoles;
	}

	private UserApplicationRoles convertToUserApplicationRoles(Long appId, RemoteUserWithRoles remoteUser) {
		UserApplicationRoles userWithRemoteAppRoles = new UserApplicationRoles();
		userWithRemoteAppRoles.appId = appId;
		userWithRemoteAppRoles.orgUserId = remoteUser.loginId;
		userWithRemoteAppRoles.firstName = remoteUser.firstName;
		userWithRemoteAppRoles.lastName = remoteUser.lastName;
		userWithRemoteAppRoles.roles = remoteUser.roles;
		return userWithRemoteAppRoles;
	}

	public static void persistExternalRoleInEcompDb(EPRole externalAppRole, Long appId, EPRoleService roleService) {
		externalAppRole.setAppId(appId);
		externalAppRole.setAppRoleId(externalAppRole.getId());
		externalAppRole.setId(null); // We will persist a new role, with ecomp role id which will be different than external app role id.

		roleService.saveRole(externalAppRole);
		logger.debug(EELFLoggerDelegate.debugLogger, String.format("ECOMP persists role from app:%d, app roleId: %d, roleName: %s", appId, externalAppRole.getAppRoleId(), externalAppRole.getName()));
	}

	@Override
	public List<EPRole> importRolesFromRemoteApplication(Long appId) throws HTTPException {
		EPRole[] appRolesFull = applicationsRestClientService.get(EPRole[].class, appId, "/rolesFull");
		List<EPRole> rolesList = Arrays.asList(appRolesFull);
		for (EPRole externalAppRole : rolesList) {

			// Try to find an existing extern role for the app in the local ecomp DB. If so, then use its id to update the existing external application role record.
			Long externAppId = externalAppRole.getId();
			EPRole existingAppRole = epRoleService.getRole(appId, externAppId);
			if (existingAppRole != null) {
				logger.debug(EELFLoggerDelegate.debugLogger, String.format("ecomp role already exists for app=%s; appRoleId=%s. No need to import this one.", appId, externAppId));
				continue;
			}
			// persistExternalRoleInEcompDb(externalAppRole, appId, roleService);
		}
		
		return rolesList;
	}

	@Override
	public List<EPUserApp> getCachedAppRolesForUser(Long appId, Long userId)  {
		// Find the records for this user-app combo, if any
		String filter = " where user_id = " + Long.toString(userId) + " and app_id = " + Long.toString(appId);
		@SuppressWarnings("unchecked")
		List<EPUserApp> roleList = dataAccessService.getList(EPUserApp.class, filter, null, null);
		logger.debug(EELFLoggerDelegate.debugLogger, "getCachedAppRolesForUser: list size is {}", 
				roleList.size());
		return roleList;
	}

}
