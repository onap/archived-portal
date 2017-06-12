/*-
 * ================================================================================
 * ECOMP Portal
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
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.cxf.common.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.domain.UserIdRoleId;
import org.openecomp.portalapp.portal.domain.UserRole;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.AppNameIdIsAdmin;
import org.openecomp.portalapp.portal.transport.AppsListWithAdminRole;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;

@Service("adminRolesService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy

public class AdminRolesServiceImpl implements AdminRolesService {

	private Long SYS_ADMIN_ROLE_ID = 1L;
	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	private Long ECOMP_APP_ID = 1L;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AdminRolesServiceImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	SearchService searchService;
	@Autowired
	EPAppService appsService;

	@PostConstruct
	private void init() {
		try {
			SYS_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.SYS_ADMIN_ROLE_ID));
			ACCOUNT_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
			ECOMP_APP_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID));
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
	}
	
	@Override
	@EPMetricsLog
	@SuppressWarnings("unchecked")
	public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(String orgUserId) {
		AppsListWithAdminRole appsListWithAdminRole = null;

		try {
			List<EPUser> userList = dataAccessService.getList(EPUser.class, " where orgUserId = '" + orgUserId + "'", null,
					null);
			HashMap<Long, Long> appsUserAdmin = new HashMap<Long, Long>();
			if (userList.size() > 0) {
				EPUser user = userList.get(0);
				List<EPUserApp> userAppList = null;
				try {
					userAppList = dataAccessService.getList(EPUserApp.class,
							" where userId = " + user.getId() + " and role.id = " + ACCOUNT_ADMIN_ROLE_ID, null, null);
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
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
				appsList = dataAccessService.getList(EPApp.class, "  where ( enabled = 'Y' or id = " + ECOMP_APP_ID + ")", null, null);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
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
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing AdminRolesServiceImpl.getAppsWithAdminRoleStateForUser operation, Details:"
							+ EcompPortalUtils.getStackTrace(e));
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
					if (app.getEnabled().booleanValue() || app.getId() == ECOMP_APP_ID) {
						enabledApps.put(app.getId(), app);
					}
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
				List<EPUser> localUserList = dataAccessService.getList(EPUser.class, " where org_user_id='" + orgUserId + "'",
						null, null);
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
						result = true;
					} catch (Exception e) {
						EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
						logger.error(EELFLoggerDelegate.errorLogger, "setAppsWithAdminRoleStateForUser: exception in point 2", e);
						try {
							transaction.rollback();
						} catch (Exception ex) {
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeExecuteRollbackError, e);
							logger.error(EELFLoggerDelegate.errorLogger, "setAppsWithAdminRoleStateForUser: exception in point 3", ex);
						}
					} finally {
						try {
							localSession.close();
						} catch (Exception e) {
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoCloseSessionError, e);
							logger.error(EELFLoggerDelegate.errorLogger, "setAppsWithAdminRoleStateForUser: exception in point 4", e);
						}
					}
				}
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSuperAdmin(EPUser user) {
		if ((user != null) /* && (user.getId() == null) */ && (user.getOrgUserId() != null)) {
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
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isSuperAdmin operation", e);
			}
		}
		// else
		// {
		// User currentUser = user != null ? (User)
		// dataAccessService.getDomainObject(User.class, user.getId(), null) :
		// null;
		// if (currentUser != null && currentUser.getId() != null) {
		// for (UserApp userApp : currentUser.getUserApps()) {
		// if (userApp.getApp().getId().equals(ECOMP_APP_ID) &&
		// userApp.getRole().getId().equals(SYS_ADMIN_ROLE_ID)) {
		// // Super Administrator role is global, no need to keep iterating
		// return true;
		// }
		// }
		// }
		// }
		return false;
	}

	public boolean isAccountAdmin(EPUser user) {
		try {
			EPUser currentUser = user != null
					? (EPUser) dataAccessService.getDomainObject(EPUser.class, user.getId(), null) : null;
			if (currentUser != null && currentUser.getId() != null) {
				for (EPUserApp userApp : currentUser.getEPUserApps()) {
					if (//!userApp.getApp().getId().equals(ECOMP_APP_ID)
							// && 
							userApp.getRole().getId().equals(ACCOUNT_ADMIN_ROLE_ID)) {
						// Account Administrator sees only the applications
						// he/she is Administrator
						return true;
					}
				}
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isAccountAdmin operation", e);
		}
		return false;
	}

	public boolean isUser(EPUser user) {
		try {
			EPUser currentUser = user != null
					? (EPUser) dataAccessService.getDomainObject(EPUser.class, user.getId(), null) : null;
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
		String sql = "SELECT * FROM FN_ROLE WHERE APP_ID = " + appId;
		@SuppressWarnings("unchecked")
		List<EPRole> roles = dataAccessService.executeSQLQuery(sql, EPRole.class, null);
		for (EPRole role: roles) {
			list.add(role);
		}
		return list;
	}
}
