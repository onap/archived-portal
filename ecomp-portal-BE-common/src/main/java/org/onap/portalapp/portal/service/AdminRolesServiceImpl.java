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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.cxf.common.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.UserIdRoleId;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.exceptions.RoleFunctionException;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;
import org.onap.portalapp.portal.transport.AppsListWithAdminRole;
import org.onap.portalapp.portal.transport.ExternalAccessUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("adminRolesService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy

public class AdminRolesServiceImpl implements AdminRolesService {

	private Long SYS_ADMIN_ROLE_ID = 1L;
	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	private Long ECOMP_APP_ID = 1L;
	public static final String TYPE_APPROVER = "approver";
	private static final String ADMIN_ACCOUNT= "Is account admin for user {}";

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AdminRolesServiceImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private EPAppService appsService;
	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	private RestTemplate template = new RestTemplate();

	@PostConstruct
	private void init() {
		try {
			SYS_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.SYS_ADMIN_ROLE_ID));
			ACCOUNT_ADMIN_ROLE_ID = Long
					.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
			ECOMP_APP_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "init failed", e);
		}
	}

	@Override
	@EPMetricsLog
	@SuppressWarnings("unchecked")
	public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(String orgUserId) {
		AppsListWithAdminRole appsListWithAdminRole = null;

		try {
			List<EPUser> userList = null;
			Map<String, String> userParams = new HashMap<>();
			userParams.put("org_user_id", orgUserId);
			try {
				userList = dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "getEPUserByOrgUserId failed", e);
			}

			HashMap<Long, Long> appsUserAdmin = new HashMap<Long, Long>();
			if (userList!= null && userList.size() > 0) {
				EPUser user = userList.get(0);
				List<EPUserApp> userAppList = null;
				try {
					userAppList = dataAccessService.getList(EPUserApp.class,
							" where userId = " + user.getId() + " and role.id = " + ACCOUNT_ADMIN_ROLE_ID, null, null);
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 1 failed", e);
					EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
				}
				for (EPUserApp userApp : userAppList) {
					appsUserAdmin.put(userApp.getAppId(), userApp.getUserId());
				}
			}

			appsListWithAdminRole = new AppsListWithAdminRole();
			appsListWithAdminRole.orgUserId = orgUserId;
			List<EPApp> appsList = null;
			try {
//				appsList = dataAccessService.getList(EPApp.class,
//						null, null, null);
				
				appsList = dataAccessService.getList(EPApp.class, null);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 2 failed", e);
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			}
			for (EPApp app : appsList) {
				AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
				appNameIdIsAdmin.id = app.getId();
				appNameIdIsAdmin.appName = app.getName();
				appNameIdIsAdmin.isAdmin = new Boolean(appsUserAdmin.containsKey(app.getId()));
				appNameIdIsAdmin.restrictedApp = app.isRestrictedApp();
				appsListWithAdminRole.appsRoles.add(appNameIdIsAdmin);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 3 failed", e);
		}

		return appsListWithAdminRole;
	}

	private static final Object syncRests = new Object();

	@Override
	@EPMetricsLog
	@SuppressWarnings("unchecked")
	public boolean setAppsWithAdminRoleStateForUser(AppsListWithAdminRole newAppsListWithAdminRoles) {
		boolean result = false;
		// No changes if no new roles list or no userId.
		if (!StringUtils.isEmpty(newAppsListWithAdminRoles.orgUserId) && newAppsListWithAdminRoles.appsRoles != null) {
			synchronized (syncRests) {
				List<EPApp> apps = appsService.getAppsFullList();
				HashMap<Long, EPApp> enabledApps = new HashMap<Long, EPApp>();
				for (EPApp app : apps) {
//					if (app.getEnabled().booleanValue() || app.getId() == ECOMP_APP_ID) {
						enabledApps.put(app.getId(), app);
//					}
				}
				List<AppNameIdIsAdmin> newAppsWhereUserIsAdmin = new ArrayList<AppNameIdIsAdmin>();
				for (AppNameIdIsAdmin adminRole : newAppsListWithAdminRoles.appsRoles) {
					// user Admin role may be added only for enabled apps
					if (adminRole.isAdmin.booleanValue() && enabledApps.containsKey(adminRole.id)) {
						newAppsWhereUserIsAdmin.add(adminRole);
					}
				}
				EPUser user = null;
				boolean createNewUser = false;
				String orgUserId = newAppsListWithAdminRoles.orgUserId.trim();
				List<EPUser> localUserList = dataAccessService.getList(EPUser.class,
						" where org_user_id='" + orgUserId + "'", null, null);
				List<EPUserApp> oldAppsWhereUserIsAdmin = new ArrayList<EPUserApp>();
				if (localUserList.size() > 0) {
					EPUser tmpUser = localUserList.get(0);
					oldAppsWhereUserIsAdmin = dataAccessService.getList(EPUserApp.class,
							" where userId = " + tmpUser.getId() + " and role.id = " + ACCOUNT_ADMIN_ROLE_ID, null,
							null);
					if (oldAppsWhereUserIsAdmin.size() > 0 || newAppsWhereUserIsAdmin.size() > 0) {
						user = tmpUser;
					}
				} else if (newAppsWhereUserIsAdmin.size() > 0) {
					// we create new user only if he has Admin Role for any App
					createNewUser = true;
				}
				if (user != null || createNewUser) {
					Session localSession = null;
					Transaction transaction = null;
					try {
						localSession = sessionFactory.openSession();
						transaction = localSession.beginTransaction();
						if (createNewUser) {
							user = this.searchService.searchUserByUserId(orgUserId);
							if (user != null) {
								// insert the user with active true in order to
								// pass login phase.
								user.setActive(true);
								localSession.save(EPUser.class.getName(), user);
							}
						}
						for (EPUserApp oldUserApp : oldAppsWhereUserIsAdmin) {
							// user Admin role may be deleted only for enabled
							// apps
							if (enabledApps.containsKey(oldUserApp.getAppId())) {
								localSession.delete(oldUserApp);
							}
						}
						for (AppNameIdIsAdmin appNameIdIsAdmin : newAppsWhereUserIsAdmin) {
							EPApp app = (EPApp) localSession.get(EPApp.class, appNameIdIsAdmin.id);
							EPRole role = (EPRole) localSession.get(EPRole.class, new Long(ACCOUNT_ADMIN_ROLE_ID));
							EPUserApp newUserApp = new EPUserApp();
							newUserApp.setUserId(user.getId());
							newUserApp.setApp(app);
							newUserApp.setRole(role);
							localSession.save(EPUserApp.class.getName(), newUserApp);
						}
						transaction.commit();
						if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
							// Add user admin role for list of centralized applications in external system
							addAdminRoleInExternalSystem(user, localSession, newAppsWhereUserIsAdmin);
							result = true;
						}
					} catch (Exception e) {
						EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
						logger.error(EELFLoggerDelegate.errorLogger,
								"setAppsWithAdminRoleStateForUser: exception in point 2", e);
						try {
							if(transaction!=null)
								transaction.rollback();
							else
								logger.error(EELFLoggerDelegate.errorLogger, "setAppsWithAdminRoleStateForUser: transaction is null cannot rollback");
						} catch (Exception ex) {
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeExecuteRollbackError, e);
							logger.error(EELFLoggerDelegate.errorLogger,
									"setAppsWithAdminRoleStateForUser: exception in point 3", ex);
						}
					} finally {
						try {
							localSession.close();
						} catch (Exception e) {
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoCloseSessionError, e);
							logger.error(EELFLoggerDelegate.errorLogger,
									"setAppsWithAdminRoleStateForUser: exception in point 4", e);
						}
					}
				}
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private boolean addAdminRoleInExternalSystem(EPUser user, Session localSession,
			List<AppNameIdIsAdmin> newAppsWhereUserIsAdmin) {
		boolean result = false;
		try {
			// Reset All admin role for centralized applications
			List<EPApp> appList = dataAccessService.executeNamedQuery("getCentralizedApps", null, null);
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			for (EPApp app : appList) {
				String name = "";
				if (EPCommonSystemProperties
						.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
					name = user.getOrgUserId() + SystemProperties
							.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
				}
				String extRole = app.getNameSpace() + "." + PortalConstants.ADMIN_ROLE.replaceAll(" ", "_");
				HttpEntity<String> entity = new HttpEntity<>(headers);
				logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
				try {
					ResponseEntity<String> getResponse = template
							.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
									+ "roles/" + extRole, HttpMethod.GET, entity, String.class);

					if (getResponse.getBody().equals("{}")) {
						String addDesc = "{\"name\":\"" + extRole + "\"}";
						HttpEntity<String> roleEntity = new HttpEntity<>(addDesc, headers);
						template.exchange(
								SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
										+ "role",
								HttpMethod.POST, roleEntity, String.class);
					} else {
						try {
							HttpEntity<String> deleteUserRole = new HttpEntity<>(headers);
							template.exchange(
									SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
											+ "userRole/" + name + "/" + extRole,
									HttpMethod.DELETE, deleteUserRole, String.class);
						} catch (Exception e) {
							logger.error(EELFLoggerDelegate.errorLogger,
									" Role not found for this user may be it gets deleted before", e);
						}
					}
				} catch (Exception e) {
					if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
						logger.debug(EELFLoggerDelegate.debugLogger, "Application Not found for app {}",
								app.getNameSpace(), e.getMessage());
					} else {
						logger.error(EELFLoggerDelegate.errorLogger, "Application Not found for app {}",
								app.getNameSpace(), e);
					}
				}
			}
			// Add admin role in external application
			// application
			for (AppNameIdIsAdmin appNameIdIsAdmin : newAppsWhereUserIsAdmin) {
				EPApp app = (EPApp) localSession.get(EPApp.class, appNameIdIsAdmin.id);
				try {
					if (app.getRolesInAAF()) {
						String extRole = app.getNameSpace() + "." + PortalConstants.ADMIN_ROLE.replaceAll(" ", "_");
						HttpEntity<String> entity = new HttpEntity<>(headers);
						String name = "";
						if (EPCommonSystemProperties
								.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
							name = user.getOrgUserId() + SystemProperties
									.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
						}
						logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
						ResponseEntity<String> getUserRolesResponse = template.exchange(
								SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
										+ "userRoles/user/" + name,
								HttpMethod.GET, entity, String.class);
						logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
						if (!getUserRolesResponse.getBody().equals("{}")) {
							JSONObject jsonObj = new JSONObject(getUserRolesResponse.getBody());
							JSONArray extRoles = jsonObj.getJSONArray("userRole");
							final Map<String, JSONObject> extUserRoles = new HashMap<>();
							for (int i = 0; i < extRoles.length(); i++) {
								String userRole = extRoles.getJSONObject(i).getString("role");
								if (userRole.startsWith(app.getNameSpace() + ".")
										&& !userRole.equals(app.getNameSpace() + ".admin")
										&& !userRole.equals(app.getNameSpace() + ".owner")) {

									extUserRoles.put(userRole, extRoles.getJSONObject(i));
								}
							}
							if (!extUserRoles.containsKey(extRole)) {
								// Assign with new apps user admin
								try {
									ExternalAccessUser extUser = new ExternalAccessUser(name, extRole);
									// Assign user role for an application in external access system
									ObjectMapper addUserRoleMapper = new ObjectMapper();
									String userRole = addUserRoleMapper.writeValueAsString(extUser);
									HttpEntity<String> addUserRole = new HttpEntity<>(userRole, headers);
									template.exchange(
											SystemProperties.getProperty(
													EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole",
											HttpMethod.POST, addUserRole, String.class);
								} catch (Exception e) {
									logger.error(EELFLoggerDelegate.errorLogger, "Failed to add user admin role", e);
								}

							}
						}
					}
					result = true;
				} catch (Exception e) {
					if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
						logger.debug(EELFLoggerDelegate.errorLogger,
								"Application name space not found in External system for app {} due to bad rquest name space ",
								app.getNameSpace(), e.getMessage());
					} else {
						logger.error(EELFLoggerDelegate.errorLogger, "Failed to assign admin role for application {}",
								app.getNameSpace(), e);
						result = false;
					}
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to assign admin roles operation", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSuperAdmin(EPUser user) {
		if ((user != null) && (user.getOrgUserId() != null)) {
			String sql = "SELECT user.USER_ID, user.org_user_id, userrole.ROLE_ID, userrole.APP_ID FROM fn_user_role userrole "
					+ "INNER JOIN fn_user user ON user.USER_ID = userrole.USER_ID " + "WHERE user.org_user_id = '"
					+ user.getOrgUserId() + "' " + "AND userrole.ROLE_ID = '" + SYS_ADMIN_ROLE_ID + "' "
					+ "AND userrole.APP_ID = '" + ECOMP_APP_ID + "';";
			try {
				List<UserRole> userRoleList = dataAccessService.executeSQLQuery(sql, UserIdRoleId.class, null);
				if (userRoleList != null && userRoleList.size() > 0) {
					return true;
				}
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				logger.error(EELFLoggerDelegate.errorLogger,
						"Exception occurred while executing isSuperAdmin operation", e);
			}
		}
		return false;
	}

	public boolean isAccountAdmin(EPUser user) {
		try {
        if (user == null) {
            return false;
        }

			EPUser currentUser = (EPUser) dataAccessService.getDomainObject(EPUser.class, user.getId(), null);

			final Map<String, Long> userParams = new HashMap<>();
			userParams.put("userId", user.getId());
			logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, user.getId());
			List<Integer> userAdminApps = new ArrayList<>();

			userAdminApps =dataAccessService.executeNamedQuery("getAdminAppsForTheUser", userParams, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for userAdminApps() - for user {}, found userAdminAppsSize {}", user.getOrgUserId(), userAdminApps.size());


			if (currentUser != null && currentUser.getId() != null) {
				for (EPUserApp userApp : currentUser.getEPUserApps()) {


					if (userApp.getRole().getId().equals(ACCOUNT_ADMIN_ROLE_ID)||(userAdminApps.size()>1)) {
						logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for userAdminApps() - for user {}, found Id {}", user.getOrgUserId(), userApp.getRole().getId());
						// Account Administrator sees only the applications
						// he/she is Administrator
						return true;
					}
				}
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isAccountAdmin operation",
					e);
		}
		return false;
	}


	public boolean isRoleAdmin(EPUser user) {
		try {
			logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has isRoleAdmin access");

					final Map<String, Long> userParams = new HashMap<>();
					userParams.put("userId", user.getId());
					List getRoleFuncListOfUser = dataAccessService.executeNamedQuery("getRoleFunctionsOfUserforAlltheApplications", userParams, null);
					logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has isRoleAdmin access :: getRoleFuncListOfUser" , getRoleFuncListOfUser);
					Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfUser);
					Set<String> getRoleFuncListOfPortalSet1=new HashSet<>();
					Set<String> roleFunSet = new HashSet<>();
					roleFunSet = getRoleFuncListOfPortalSet.stream().filter(x -> x.contains("|")).collect(Collectors.toSet());
					if (!roleFunSet.isEmpty())
						for (String roleFunction : roleFunSet) {
							String type = externalAccessRolesService.getFunctionCodeType(roleFunction);
							getRoleFuncListOfPortalSet1.add(type);
						}
				
					boolean checkIfFunctionsExits = getRoleFuncListOfPortalSet1.stream()
							.anyMatch(roleFunction -> roleFunction.equalsIgnoreCase("Approver"));
					logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has approver rolefunction" , checkIfFunctionsExits);

					return checkIfFunctionsExits;
		
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isRoleAdmin operation",
					e);
		}
		return false;
	}

	public boolean isUser(EPUser user) {
		try {
			EPUser currentUser = user != null
					? (EPUser) dataAccessService.getDomainObject(EPUser.class, user.getId(), null)
					: null;
			if (currentUser != null && currentUser.getId() != null) {
				for (EPUserApp userApp : currentUser.getEPUserApps()) {
					if (!userApp.getApp().getId().equals(ECOMP_APP_ID)) {
						EPRole role = userApp.getRole();
						if (!role.getId().equals(SYS_ADMIN_ROLE_ID) && !role.getId().equals(ACCOUNT_ADMIN_ROLE_ID)) {
							if (role.getActive()) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isUser operation", e);
		}
		return false;
	}

	@Override
	@EPMetricsLog
	public List<EPRole> getRolesByApp(EPUser user, Long appId) {
		List<EPRole> list = new ArrayList<>();
		String sql = "SELECT * FROM FN_ROLE WHERE UPPER(ACTIVE_YN) = 'Y' AND APP_ID = " + appId;
		@SuppressWarnings("unchecked")
		List<EPRole> roles = dataAccessService.executeSQLQuery(sql, EPRole.class, null);
		for (EPRole role : roles) {
			list.add(role);
		}
		return list;
	}

	@Override
	public boolean isAccountAdminOfApplication(EPUser user, EPApp app) {
		Boolean isApplicationAccountAdmin=false;
		try {
					final Map<String, Long> userParams = new HashMap<>();
					userParams.put("userId", user.getId());
					logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, user.getId());
					List<Integer> userAdminApps = new ArrayList<>();
					userAdminApps =dataAccessService.executeNamedQuery("getAdminAppsForTheUser", userParams, null);
					if(!userAdminApps.isEmpty()){
					isApplicationAccountAdmin=userAdminApps.contains((int) (long) app.getId());
					logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for user is true{} ,appId {}", user.getId(),app.getId());
					}
			} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while executing isAccountAdminOfApplication operation", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "In AdminRolesServiceImpl() - isAccountAdminOfApplication = {} and userId ={} ", isApplicationAccountAdmin, user.getOrgUserId());
		return isApplicationAccountAdmin;

	}

	@Override
	public Set<String> getAllAppsFunctionsOfUser(String OrgUserId) throws RoleFunctionException {
		final Map<String, String> params = new HashMap<>();
		params.put("userId", OrgUserId);
		List getRoleFuncListOfPortal = dataAccessService.executeNamedQuery("getAllAppsFunctionsOfUser", params, null);
		Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfPortal);
		Set<String> roleFunSet = new HashSet<>();
		roleFunSet = getRoleFuncListOfPortalSet.stream().filter(x -> x.contains("|")).collect(Collectors.toSet());
		if (!roleFunSet.isEmpty())
			for (String roleFunction : roleFunSet) {
				String roleFun = EcompPortalUtils.getFunctionCode(roleFunction);
				getRoleFuncListOfPortalSet.remove(roleFunction);
				getRoleFuncListOfPortalSet.add(roleFun);
			}

		Set<String> finalRoleFunctionSet = new HashSet<>();
		for (String roleFn : getRoleFuncListOfPortalSet) {
			finalRoleFunctionSet.add(EPUserUtils.decodeFunctionCode(roleFn));
		}
		
		return finalRoleFunctionSet;
	}

	
	@Override
	public boolean isAccountAdminOfAnyActiveorInactiveApplication(EPUser user, EPApp app) {
		Boolean isApplicationAccountAdmin=false;
		try {
					final Map<String, Long> userParams = new HashMap<>();
					userParams.put("userId", user.getId());	
					logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, user.getId());
					List<Integer> userAdminApps = new ArrayList<>();
					userAdminApps =dataAccessService.executeNamedQuery("getAllAdminAppsofTheUser", userParams, null);
					if(!userAdminApps.isEmpty()){
					isApplicationAccountAdmin=userAdminApps.contains((int) (long) app.getId());
					logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for user is true{} ,appId {}", user.getId(),app.getId());
					}					
			} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while executing isAccountAdminOfApplication operation", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "In AdminRolesServiceImpl() - isAccountAdminOfApplication = {} and userId ={} ", isApplicationAccountAdmin, user.getOrgUserId());
		return isApplicationAccountAdmin;

	}
}
