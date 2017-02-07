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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiConstants;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiProperties;
import org.openecomp.portalsdk.core.onboarding.ueb.Helper;
import org.openecomp.portalsdk.core.onboarding.ueb.TopicManager;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalapp.portal.domain.AdminUserApp;
import org.openecomp.portalapp.portal.domain.AdminUserApplications;
import org.openecomp.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.openecomp.portalapp.portal.domain.AppsResponse;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompApp;
import org.openecomp.portalapp.portal.domain.UserRole;
import org.openecomp.portalapp.portal.domain.UserRoles;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.LocalRole;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.portal.ueb.EPUebHelper;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.portal.ecomp.model.AppCatalogItem;
import com.att.nsa.apiClient.http.HttpException;
import com.att.nsa.cambria.client.CambriaClient.CambriaApiException;
import com.att.nsa.cambria.client.CambriaClientBuilders;
import com.att.nsa.cambria.client.CambriaIdentityManager;

@Service("epAppService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPAppServiceImpl implements EPAppService {

	private String ECOMP_APP_ID = "1";
	private String SUPER_ADMIN_ROLE_ID = "1";
	private String ACCOUNT_ADMIN_ROLE_ID = "999";
	private String RESTRICTED_APP_ROLE_ID = "900";

	private static final String PATH_SEPARATOR = "/";
	private static final String webappsBaseFullPath = System.getProperty("catalina.base") + PATH_SEPARATOR
			+ "wtpwebapps";
	private static final String imageCacheRelativePath = "images" + PATH_SEPARATOR + "cache";
	private String ecompportalPath = null;
	private static final Long DUBLICATED_FIELD_VALUE_ECOMP_ERROR = new Long(
			EPSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);

	private static final String urlField = "url";

	private static final String nameField = "name";

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPAppServiceImpl.class);

	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	EPUebHelper epUebHelper;

	private String constructImagesCachePath() {
		String filePath = System.getProperty("catalina.base");
		File eclipseWebAppDir = new File(webappsBaseFullPath);

		if (eclipseWebAppDir.exists()) {
			// Eclipse webapps
			filePath += PATH_SEPARATOR + "wtpwebapps";
		} else {
			filePath += PATH_SEPARATOR + "webapps";
		}
		filePath += PATH_SEPARATOR + SystemProperties.getProperty(EPSystemProperties.ECOMP_CONTEXT_ROOT);;

		return filePath.toString();
		// return SystemProperties.getProperty(SystemProperties.TEMP_PATH);
	}

	@PostConstruct
	private void init() {
		ecompportalPath = constructImagesCachePath();
		writeAppsImagesToDiskCacheIfNecessary();

		SUPER_ADMIN_ROLE_ID = SystemProperties.getProperty(EPSystemProperties.SYS_ADMIN_ROLE_ID);
		ACCOUNT_ADMIN_ROLE_ID = SystemProperties.getProperty(EPSystemProperties.ACCOUNT_ADMIN_ROLE_ID);
		ECOMP_APP_ID = SystemProperties.getProperty(EPSystemProperties.ECOMP_APP_ID);
		RESTRICTED_APP_ROLE_ID = SystemProperties.getProperty(EPSystemProperties.RESTRICTED_APP_ROLE_ID);
	}

	@Override
	public List<EPApp> getUserAsAdminApps(EPUser user) {
		if (adminRolesService.isAccountAdmin(user)) {
			String sql = "SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID=FN_APP.APP_ID where "
					+ "FN_USER_ROLE.USER_ID=" + user.getId() + " AND FN_USER_ROLE.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
					+ " AND FN_APP.ENABLED = 'Y'";
			logQuery(sql);
			try {
				@SuppressWarnings("unchecked")
				List<EPApp> adminApps = dataAccessService.executeSQLQuery(sql, EPApp.class, null);
				return adminApps;
			} catch (Exception e) {
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
				return null;
			}
		} else {
			logger.error(EELFLoggerDelegate.errorLogger,
					"getUserAsAdminApps: only Account Admin may invoke this function!");
			return new ArrayList<EPApp>();
		}
	}

	@Override
	public List<EPApp> getUserByOrgUserIdAsAdminApps(String orgUserId) {
		String format = "SELECT * FROM FN_APP app INNER JOIN FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
				+ "INNER JOIN FN_USER user on user.USER_ID = userrole.USER_ID "
				+ "WHERE user.ORG_USER_ID = '%s' AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID + " AND FN_APP.ENABLED = 'Y'";

		String sql = String.format(format, orgUserId);
		logQuery(sql);

		try {
			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(sql, EPApp.class, null);
			return adminApps;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			return null;
		}
	}

	@Override
	public List<EPApp> getAppsFullList() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = dataAccessService.getList(EPApp.class, null);
		// Write the image to disk in case if it is missing
		for (EPApp app : apps)
			writeAppImageToDiskCacheIfNecessary(app);
		return apps;
	}

	@Override
	public List<EcompApp> getEcompAppAppsFullList() {
		return transformAppsToEcompApps(getAppsFullList());
	}

	@Override
	public List<EcompApp> transformAppsToEcompApps(List<EPApp> appsList) {
		List<EcompApp> ecompAppList = new ArrayList<EcompApp>();
		for (EPApp app : appsList) {
			EcompApp ecompApp = new EcompApp();
			ecompApp.setId(app.getId());
			ecompApp.setName(app.getName());
			ecompApp.setImageUrl(app.getImageUrl());
			ecompApp.setDescription(app.getDescription());
			ecompApp.setNotes(app.getNotes());
			ecompApp.setUrl(app.getUrl());
			ecompApp.setAlternateUrl(app.getAlternateUrl());
			ecompApp.setUebTopicName(app.getUebTopicName());
			ecompApp.setUebKey(app.getUebKey());
			ecompApp.setUebSecret(app.getUebSecret());
			ecompApp.setEnabled(app.getEnabled());
			ecompApp.setRestrictedApp(app.isRestrictedApp());
			ecompAppList.add(ecompApp);
		}
		return ecompAppList;
	}

	@Override
	public EPApp getApp(Long appId) {
		try {
			@SuppressWarnings("unchecked")
			List<EPApp> apps = dataAccessService.getList(EPApp.class, " where id = " + appId, null, null);
			return (apps.size() > 0) ? apps.get(0) : null;
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppIdAndNameTransportModel> getAdminApps(EPUser user) {
		if (adminRolesService.isAccountAdmin(user)) {
			String format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
					+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
					+ " AND app.ENABLED = 'Y'";
			String sql = String.format(format, user.getId());
			// sql += " AND app.APP_REST_ENDPOINT IS NOT NULL AND
			// app.APP_REST_ENDPOINT <> ''";
			logQuery(sql);
			try {
				return (ArrayList<AppIdAndNameTransportModel>) dataAccessService.executeSQLQuery(sql,
						AppIdAndNameTransportModel.class, null);
			} catch (Exception e) {
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while fetching the adminApps for user "
						+ user.getLoginId() + ". Details: " + EcompPortalUtils.getStackTrace(e));
			}
		}
		return new ArrayList<AppIdAndNameTransportModel>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppIdAndNameTransportModel> getAppsForSuperAdminAndAccountAdmin(EPUser user) {
		if (adminRolesService.isSuperAdmin(user) || adminRolesService.isAccountAdmin(user)) {
			String format = "";
			String sql = "";
			if (adminRolesService.isSuperAdmin(user)) {
				format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app " + "where app.ENABLED = 'Y'";
			} else {
				format = "SELECT app.APP_ID, app.APP_NAME, APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
						+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
						+ " AND app.ENABLED = 'Y'";
			}
			sql = String.format(format, user.getId());
			// sql += " AND app.APP_REST_ENDPOINT IS NOT NULL AND
			// app.APP_REST_ENDPOINT <> ''";
			logQuery(sql);
			try {
				return (ArrayList<AppIdAndNameTransportModel>) dataAccessService.executeSQLQuery(sql,
						AppIdAndNameTransportModel.class, null);
			} catch (Exception e) {
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while fetching the adminApps for user "
						+ user.getLoginId() + ". Details: " + EcompPortalUtils.getStackTrace(e));
			}
		}
		return new ArrayList<AppIdAndNameTransportModel>();
	}

	private void logQuery(String sql) {
		logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@Override
	public List<AdminUserApplications> getAppsAdmins() {
		String sql = "SELECT apps.APP_NAME, apps.APP_ID, user.USER_ID, user.FIRST_NAME, user.LAST_NAME, user.ORG_USER_ID FROM fn_user_role userrole "
				+ "INNER JOIN fn_user user ON user.USER_ID = userrole.USER_ID "
				+ "INNER JOIN fn_app apps ON apps.APP_ID = userrole.APP_ID " + "WHERE userrole.ROLE_ID = "
				+ ACCOUNT_ADMIN_ROLE_ID + " AND apps.ENABLED = 'Y'";
		logQuery(sql);
		try {
			@SuppressWarnings("unchecked")
			List<AdminUserApp> adminApps = dataAccessService.executeSQLQuery(sql, AdminUserApp.class, null); // DataAccessService
																												// does
																												// not
																												// use
																												// generic
																												// types.
			return aggregateRowsResultsByUserId(adminApps);
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			return null;
		}
	}

	private List<AdminUserApplications> aggregateRowsResultsByUserId(List<AdminUserApp> adminApps) {
		HashMap<Long, AdminUserApplications> adminUserApplications = new HashMap<Long, AdminUserApplications>();
		for (AdminUserApp app : adminApps) {
			Long userId = app.getUser_Id();
			if (adminUserApplications.get(userId) == null)
				adminUserApplications.put(userId, new AdminUserApplications(app));
			else
				adminUserApplications.get(userId).addApp(app.getAppId(), app.getAppName());
		}
		return new ArrayList<AdminUserApplications>(adminUserApplications.values());
	}

	@Override
	public List<AppsResponse> getAllApps(Boolean all) {
		// If all is true, return both active and inactive apps. Otherwise, just
		// active apps.
		@SuppressWarnings("unchecked")
		// Sort the list by application name so the drop-down looks pretty.
		List<EPApp> apps = all
				? (List<EPApp>) dataAccessService.getList(EPApp.class, " where id != " + ECOMP_APP_ID, "name", null)
				: (List<EPApp>) dataAccessService.getList(EPApp.class, " where enabled = 'Y'", "name", null);
		List<AppsResponse> appsModified = new ArrayList<AppsResponse>();
		for (EPApp app : apps) {
			appsModified.add(new AppsResponse(app.getId(), app.getName(), app.isRestrictedApp(), app.getEnabled()));

			// Write the image to disk in case if it is missing
			writeAppImageToDiskCacheIfNecessary(app);
		}
		return appsModified;
	}

	@Override
	public UserRoles getUserProfile(String loginId) {
		String format = "SELECT DISTINCT user.USER_ID, role.ROLE_ID, user.ORG_USER_ID, user.FIRST_NAME, user.LAST_NAME, role.ROLE_NAME  FROM fn_user_role userrole "
				+ "INNER JOIN fn_user user ON user.USER_ID = userrole.USER_ID "
				+ "INNER JOIN fn_role role ON role.ROLE_ID = userrole.ROLE_ID " + "WHERE user.ORG_USER_ID = \"%s\";";
		String sql = String.format(format, loginId);
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<UserRole> userRoleList = dataAccessService.executeSQLQuery(sql, UserRole.class, null);
		ArrayList<UserRoles> usersRolesList = aggregateUserProfileRowsResultsByRole(userRoleList);
		if (usersRolesList == null || usersRolesList.size() < 1)
			return null;

		return usersRolesList.get(0);
	}

	private ArrayList<UserRoles> aggregateUserProfileRowsResultsByRole(List<UserRole> userRoleList) {
		HashMap<String, UserRoles> userRoles = new HashMap<String, UserRoles>();
		for (UserRole user : userRoleList) {
			String orgUserId = user.getOrgUserId();
			if (userRoles.get(orgUserId) == null)
				userRoles.put(orgUserId, new UserRoles(user));
			else
				userRoles.get(orgUserId).addRole(user.getRoleId());
		}
		return new ArrayList<UserRoles>(userRoles.values());
	}

	private boolean isRestrictedApp(Long appId) {
		EPApp app = getApp(appId);
		return app.isRestrictedApp();
	}

	// For the functional menu edit
	@Override
	public List<LocalRole> getAppRoles(Long appId) {
		String sql = "";
		if (isRestrictedApp(appId)) {
			sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where ROLE_ID = '" + RESTRICTED_APP_ROLE_ID + "'";
		} else {
			sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where APP_ID = '" + appId + "'";
		}
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<LocalRole> appRoles = dataAccessService.executeSQLQuery(sql, LocalRole.class, null);
		return appRoles;
	}

	private String userAppsQuery(EPUser user) {
		StringBuilder query = new StringBuilder();
		if (adminRolesService.isSuperAdmin(user)) {
			query.append("SELECT * FROM FN_APP where FN_APP.ENABLED = 'Y'");
		} else {
			query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
			query.append(
					"FN_USER_ROLE.USER_ID = " + user.getId() + " AND FN_USER_ROLE.ROLE_ID != " + SUPER_ADMIN_ROLE_ID);
			query.append(" AND FN_APP.ENABLED = 'Y'");
		}
		return query.toString();
	}

	@Override
	public List<EPApp> getUserApps(EPUser user) {
		List<EPApp> openApps = getOpenApps();
		for (EPApp app : openApps) {
			// Write the image to disk in case if it is missing
			writeAppImageToDiskCacheIfNecessary(app);
		}

		if (user.isGuest()) {
			return openApps;
		} else {
			String sql = userAppsQuery(user);
			logQuery(sql);

			TreeSet<EPApp> distinctApps = new TreeSet<EPApp>();

			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(sql, EPApp.class, null);
			for (EPApp app : adminApps) {
				// Write the image to disk in case if it is missing
				writeAppImageToDiskCacheIfNecessary(app);

				distinctApps.add(app);
			}

			for (EPApp app : openApps) {
				distinctApps.add(app);
			}

			List<EPApp> userApps = new ArrayList<EPApp>();
			userApps.addAll(distinctApps);
			return userApps;
		}
	}

	@Override
	public List<EPApp> getPersAdminApps(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to EPApp
		@SuppressWarnings("unchecked")
		List<EPApp> list = dataAccessService.executeNamedQuery("getPersAdminApps", params, null);
		// Why is this necessary?
		for (EPApp app : list) 
			writeAppImageToDiskCacheIfNecessary(app);
		return list;
	}

	@Override
	public List<EPApp> getPersUserApps(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to EPApp
		@SuppressWarnings("unchecked")
		List<EPApp> list = dataAccessService.executeNamedQuery("getPersUserApps", params, null);
		// Why is this necessary?
		for (EPApp app : list) 
			writeAppImageToDiskCacheIfNecessary(app);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPAppService#getAppCatalog(org.openecomp.portalapp.portal.domain.EPUser)
	 */
	@Override
	public List<AppCatalogItem> getUserAppCatalog(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to AppCatalogItem
		@SuppressWarnings("unchecked")
		List<AppCatalogItem> list = dataAccessService.executeNamedQuery("getUserAppCatalog", params, null);
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPAppService#getAdminAppCatalog(org.openecomp.portalapp.portal.domain.EPUser)
	 */
	@Override
	public List<AppCatalogItem> getAdminAppCatalog(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to AppCatalogItem
		@SuppressWarnings("unchecked")
		List<AppCatalogItem> list = dataAccessService.executeNamedQuery("getAdminAppCatalog", params, null);
		return list;
	}

	private List<EPApp> getOpenApps() {
		@SuppressWarnings("unchecked")
		List<EPApp> openApps = dataAccessService.getList(EPApp.class, " where open='Y' and enabled='Y'", null, null);
		return openApps;
	}

	@Override
	public List<OnboardingApp> getOnboardingApps() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = (List<EPApp>) dataAccessService.getList(EPApp.class, " where id!=" + ECOMP_APP_ID, null,
				null);
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		for (EPApp app : apps) {
			OnboardingApp onboardingApp = new OnboardingApp();
			createOnboardingFromApp(app, onboardingApp);
			onboardingAppsList.add(onboardingApp);
		}
		return onboardingAppsList;
	}

	@Override
	public List<OnboardingApp> getEnabledNonOpenOnboardingApps() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = (List<EPApp>) dataAccessService.getList(EPApp.class,
				" where enabled = true and open = false and id!=" + ECOMP_APP_ID, null, null);
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		for (EPApp app : apps) {
			OnboardingApp onboardingApp = new OnboardingApp();
			createOnboardingFromApp(app, onboardingApp);
			onboardingAppsList.add(onboardingApp);
		}
		return onboardingAppsList;
	}

	private FieldsValidator onboardingAppFieldsChecker(OnboardingApp onboardingApp) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (onboardingApp.name == null || onboardingApp.name.length() == 0 || onboardingApp.url == null
				|| onboardingApp.url.length() == 0 || onboardingApp.restrictedApp == null
				|| onboardingApp.isOpen == null || onboardingApp.isEnabled == null
				|| (onboardingApp.id != null && onboardingApp.id.equals(ECOMP_APP_ID))
				// For a normal app (appType==1), these fields must be filled
				// in.
				// For a restricted app (appType==2), they will be empty.
				|| ((!onboardingApp.restrictedApp) && (onboardingApp.username == null
						|| onboardingApp.username.length() == 0 || onboardingApp.appPassword == null
						|| onboardingApp.appPassword.length() == 0))) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	private void validateOnboardingApp(OnboardingApp onboardingApp, FieldsValidator fieldsValidator) {
		boolean dublicatedUrl = false;
		boolean dublicatedName = false;
		List<EPApp> apps;
		if (onboardingApp.id == null) {
			apps = dataAccessService.getList(EPApp.class,
					" where url = '" + onboardingApp.url + "' or name = '" + onboardingApp.name + "'", null, null);
		} else {
			apps = dataAccessService.getList(EPApp.class, " where id = " + onboardingApp.id + " or url = '"
					+ onboardingApp.url + "' or name = '" + onboardingApp.name + "'", null, null);
		}
		for (EPApp app : apps) {
			if (onboardingApp.id != null && onboardingApp.id.equals(app.getId())) {
				continue;
			}
			if (!dublicatedUrl && app.getUrl().equalsIgnoreCase(onboardingApp.url)) {
				dublicatedUrl = true;
				if (dublicatedName) {
					break;
				}
			}
			if (!dublicatedName && app.getName().equalsIgnoreCase(onboardingApp.name)) {
				dublicatedName = true;
				if (dublicatedUrl) {
					break;
				}
			}
		}
		if (dublicatedUrl || dublicatedName) {
			if (dublicatedUrl) {
				fieldsValidator.addProblematicFieldName(urlField);
			}
			if (dublicatedName) {
				fieldsValidator.addProblematicFieldName(nameField);
			}
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
			fieldsValidator.errorCode = DUBLICATED_FIELD_VALUE_ECOMP_ERROR;
		}
	}

	@Override
	public FieldsValidator modifyOnboardingApp(OnboardingApp modifiedOnboardingApp, EPUser user) {
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: entering modifyOnboardingApp");
		FieldsValidator fieldsValidator = onboardingAppFieldsChecker(modifiedOnboardingApp);
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			validateOnboardingApp(modifiedOnboardingApp, fieldsValidator);
		}
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			if (modifiedOnboardingApp.id != null) {
				updateApp(modifiedOnboardingApp.id, modifiedOnboardingApp, fieldsValidator, user);
			} else {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		return fieldsValidator;
	}

	@Override
	public FieldsValidator addOnboardingApp(OnboardingApp newOnboardingApp, EPUser user) {
		FieldsValidator fieldsValidator = onboardingAppFieldsChecker(newOnboardingApp);
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			validateOnboardingApp(newOnboardingApp, fieldsValidator);
		}
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			if (newOnboardingApp.id == null) {
				updateApp(null, newOnboardingApp, fieldsValidator, user);
			} else {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		return fieldsValidator;
	}

	@Override
	public FieldsValidator deleteOnboardingApp(EPUser user, Long appid) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (!adminRolesService.isSuperAdmin(user)) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
			return fieldsValidator;
		}
		Boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();

			// 1) Remove the URL for any functional menu item associated with
			// this app
			String sql = "UPDATE fn_menu_functional m, fn_menu_functional_roles mr SET m.url='' "
					+ " WHERE m.menu_id=mr.menu_id " + " AND mr.app_id='" + appid + "'";
			logQuery(sql);
			Query query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// 2) Remove any favorites associated with a menu item that is
			// associated with this app
			sql = "Delete from fn_menu_favorites " + " using fn_menu_favorites inner join fn_menu_functional_roles "
					+ " where fn_menu_functional_roles.app_id='" + appid + "' "
					+ " AND fn_menu_functional_roles.menu_id=fn_menu_favorites.menu_id";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// 3) Remove all role,appid records from fn_menu_functional_role
			// that are associated with this app
			sql = "delete from fn_menu_functional_roles where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// 4) Remove all records from fn_user_role associated with this app
			sql = "delete from fn_user_role where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// 5) Remove all records from fn_role associated with this app
			sql = "delete from fn_role where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// 6) Remove any widgets associated with this app
			sql = "delete from fn_widget where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove any selections in the personalization table
			sql = "delete from fn_pers_user_app_sel where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			// Delete the app
			sql = "delete from fn_app where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			transaction.commit();
			result = true;
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			EcompPortalUtils.rollbackTransaction(transaction, "deleteOnboardingApp rollback, exception = " + e);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "deleteOnboardingApp");
		}
		if (!result) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	private static Object syncRests = new Object();

	// An app has been enabled/disabled. Must enable/disable all associated
	// functional menu items.
	private void setFunctionalMenuItemsEnabled(Session localSession, Boolean enabled, Long appId) {
		String active_yn = enabled ? "Y" : "N";
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ "FROM fn_menu_functional m, fn_menu_functional_roles r " + "WHERE m.menu_id = r.menu_id "
				+ " AND r.app_id = '" + appId + "' ";
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		for (FunctionalMenuItem menuItem : menuItems) {
			FunctionalMenuItem myMenuItem = (FunctionalMenuItem) localSession.get(FunctionalMenuItem.class,
					menuItem.menuId);
			myMenuItem.active_yn = active_yn;
			localSession.save(myMenuItem);
		}
	}

	// Attention! If (appId == null) we use this function to create application
	// otherwise we use it to modify existing application
	private void updateApp(Long appId, OnboardingApp onboardingApp, FieldsValidator fieldsValidator, EPUser user) {
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: entering updateApp");
		// Separate out the code for a restricted app, since it doesn't need any
		// of the UEB code.
		if (onboardingApp.restrictedApp) {
			boolean result = false;
			Session localSession = null;
			Transaction transaction = null;
			try {
				localSession = sessionFactory.openSession();
				transaction = localSession.beginTransaction();
				EPApp app;
				if (appId == null) {
					app = new EPApp();
				} else {
					app = (EPApp) localSession.get(EPApp.class, appId);
					if (app == null || app.getId() == null) { // App is already
						// deleted!
						transaction.commit();
						localSession.close();
						fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
				}
				createAppFromOnboarding(app, onboardingApp, localSession);
				localSession.saveOrUpdate(app);
				// Enable or disable all menu items associated with this app
				setFunctionalMenuItemsEnabled(localSession, onboardingApp.isEnabled, appId);
				transaction.commit();
				result = true;
			} catch (Exception e) {
				EcompPortalUtils.rollbackTransaction(transaction,
						"updateApp rollback, exception = " + EcompPortalUtils.getStackTrace(e));
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "updateApp");
			}
			if (!result) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		} else {
			synchronized (syncRests) {
				boolean result = false;
				Session localSession = null;
				Transaction transaction = null;
				try {
					localSession = sessionFactory.openSession();
					transaction = localSession.beginTransaction();
					EPApp app;
					if (appId == null) {
						app = new EPApp();
						// -------------------------------------------------------------------------------------------
						// Register this App with the UEB communication server.
						// Save
						// the App's unique mailbox/topic
						// name and keys to the FN_APP table. The App's mailbox
						// and
						// keys will be visible to the
						// admin on the ECOMP portal.
						// -------------------------------------------------------------------------------------------
						TopicManager topicManager = new TopicManager();
						final CambriaIdentityManager im = new CambriaClientBuilders.IdentityManagerBuilder()
								.usingHosts(Helper.uebUrlList()).build();
						com.att.nsa.apiClient.credentials.ApiCredential credential = im.createApiKey(user.getEmail(),
								"ECOMP Portal Owner");
						String appKey = credential.getApiKey();
						String appSecret = credential.getApiSecret();
						boolean isProductionBuild = EcompPortalUtils.isProductionBuild();
						String appMailboxName = null;

						int maxNumAttemptsToCreateATopic = 3;
						boolean successfullyCreatedMailbox = false;
						for (int i = 0; i < maxNumAttemptsToCreateATopic; i++) {
							if (isProductionBuild) {
								appMailboxName = "ECOMP-PORTAL-OUTBOX-" + (int) (Math.random() * 100000.0);
							} else {
								appMailboxName = "ECOMP-PORTAL-OUTBOX-TEST-" + (int) (Math.random() * 100000.0);
							}

							try {
								topicManager.createTopic(
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET),
										appMailboxName, "ECOMP outbox for app" + onboardingApp.name);
								successfullyCreatedMailbox = true;
								logger.debug(EELFLoggerDelegate.debugLogger,
										"Successfully created " + appMailboxName + " for App " + onboardingApp.name);
								logger.debug(EELFLoggerDelegate.debugLogger, "    Key = " + appKey + " Secret = "
										+ appSecret + " generated using = " + user.getEmail());
								break;
							} catch (HttpException e) {
								String stackTrace = EcompPortalUtils.getStackTrace(e);
								EPLogUtil.logEcompError(EPAppMessagesEnum.BeUebConnectionError, e.getMessage());
								if (e.getStatusCode() == 409) {
									logger.error(EELFLoggerDelegate.errorLogger,
											"Topic/mailbox " + appMailboxName
													+ " already exists. Will try using a different name, Details: "
													+ stackTrace);
								} else {
									logger.error(EELFLoggerDelegate.errorLogger,
											"HttpException when onboarding App: " + stackTrace);
								}
							}
						}

						if (successfullyCreatedMailbox) {
							onboardingApp.setUebTopicName(appMailboxName);
							onboardingApp.setUebKey(appKey);
							onboardingApp.setUebSecret(appSecret);

							try {
								/*
								 * EP is a publisher to this App's new mailbox
								 */
								topicManager.addPublisher(
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET),
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
										appMailboxName);

								/*
								 * This App is a subcriber of it's own mailbox
								 */
								topicManager.addSubscriber(
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET), appKey,
										appMailboxName);

								/*
								 * This App is a publisher to EP
								 */
								topicManager.addPublisher(
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
										PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET), appKey,
										PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME));
							} catch (HttpException | CambriaApiException | IOException e) {
								String stackTrace = EcompPortalUtils.getStackTrace(e);
								EPLogUtil.logEcompError(EPAppMessagesEnum.BeUebRegisterOnboardingAppError,
										e.getMessage());
								logger.error(EELFLoggerDelegate.errorLogger,
										"Error when configuring Publisher/Subscriber for App's new mailbox "
												+ stackTrace);
								transaction.commit();
								localSession.close();
								fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
								return;
							}
						} else {
							transaction.commit();
							localSession.close();
							fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
							return;
						}
					} else {
						app = (EPApp) localSession.get(EPApp.class, appId);
						if (app == null || app.getId() == null) { // App is
																	// already
							// deleted!
							transaction.commit();
							localSession.close();
							fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
							return;
						}
					}
					logger.debug(EELFLoggerDelegate.debugLogger, "LR: about to call createAppFromOnboarding");
					createAppFromOnboarding(app, onboardingApp, localSession);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"LR: updateApp: finished calling createAppFromOnboarding");
					localSession.saveOrUpdate(app);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"LR: updateApp: finished calling localSession.saveOrUpdate");
					// Enable or disable all menu items associated with this app
					setFunctionalMenuItemsEnabled(localSession, onboardingApp.isEnabled, appId);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"LR: updateApp: finished calling setFunctionalMenuItemsEnabled");
					transaction.commit();
					logger.debug(EELFLoggerDelegate.debugLogger, "LR: updateApp: finished calling transaction.commit");
					epUebHelper.addPublisher(app);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"LR: updateApp: finished calling epUebHelper.addPublisher");
					result = true;
				} catch (Exception e) {
					logger.debug(EELFLoggerDelegate.debugLogger, "exception: " + e.toString());
					logger.debug(EELFLoggerDelegate.debugLogger, "exception.cause: " + e.getCause());
					EPLogUtil.logEcompError(EPAppMessagesEnum.BeUebRegisterOnboardingAppError, e.getMessage());
					EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
					EcompPortalUtils.rollbackTransaction(transaction,
							"updateApp rollback, exception = " + EcompPortalUtils.getStackTrace(e));
				} finally {
					EcompPortalUtils.closeLocalSession(localSession, "updateApp");
				}
				if (!result) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		}
	}

	private void createOnboardingFromApp(EPApp app, OnboardingApp onboardingApp) {
		onboardingApp.id = app.getId();
		onboardingApp.name = app.getName();
		onboardingApp.imageUrl = app.getImageUrl();
		onboardingApp.description = app.getDescription();
		onboardingApp.notes = app.getNotes();
		onboardingApp.url = app.getUrl();
		onboardingApp.alternateUrl = app.getAlternateUrl();
		onboardingApp.restUrl = app.getAppRestEndpoint();
		onboardingApp.isOpen = app.getOpen();
		onboardingApp.isEnabled = app.getEnabled();
		onboardingApp.username = app.getUsername();
		onboardingApp.appPassword = this.decryptedPassword(app.getAppPassword(), app);
		onboardingApp.uebTopicName = app.getUebTopicName();
		onboardingApp.uebKey = app.getUebKey();
		onboardingApp.uebSecret = app.getUebSecret();
		onboardingApp.setRestrictedApp(app.isRestrictedApp());

		if (app.getThumbnail() != null) {
			onboardingApp.thumbnail = new String(Base64.getEncoder().encode(app.getThumbnail()));
			// Write the image to disk in case if it is missing
			writeAppImageToDiskCacheIfNecessary(app);
		}
	}

	private EPApp createAppFromOnboarding(EPApp app, OnboardingApp onboardingApp, Session localSession) {
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: entering createAppFromOnboarding");
		app.setName(onboardingApp.name);
		app.setDescription(onboardingApp.description);
		app.setNotes(onboardingApp.notes);
		app.setUrl(onboardingApp.url);
		app.setAlternateUrl(onboardingApp.alternateUrl);
		app.setAppRestEndpoint(onboardingApp.restUrl);
		app.setOpen(onboardingApp.isOpen);
		app.setEnabled(onboardingApp.isEnabled);
		app.setUsername(onboardingApp.username);
		app.setAppPassword(this.encryptedPassword(onboardingApp.appPassword, app));
		app.setUebTopicName(onboardingApp.uebTopicName);
		app.setUebKey(onboardingApp.uebKey);
		app.setUebSecret(onboardingApp.uebSecret);
		app.setRestrictedApp(onboardingApp.restrictedApp);
		if (!StringUtils.isEmpty(onboardingApp.thumbnail)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: onboarding thumbnail is NOT empty");
			String[] splitBase64Thumbnail = onboardingApp.thumbnail.split("base64,");
			if (splitBase64Thumbnail != null) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"LR: length of splitBase64Thumbnail: " + splitBase64Thumbnail.length);
			}
			if (splitBase64Thumbnail.length > 1) {
				// This occurs when we have a new image, not an existing image
				byte[] decodedImage = Base64.getDecoder().decode(splitBase64Thumbnail[1].getBytes());
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: finished calling decode");
				deleteOldImageFromDisk(app);
				saveNewImageToDisk(app, onboardingApp, decodedImage);
				app.setThumbnail(decodedImage);
			}
		} else if (app.getThumbnail() != null) {
			// The thumbnail that came in from the json is empty; the previous
			// thumbnail is NOT empty. Must delete it.
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: onboarding thumbnail is empty; db thumbnail is NOT null");
			deleteOldImageFromDisk(app);
			app.setThumbnail(null);
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: onboarding thumbnail is empty; db thumbnail is null");
		}
		return app;
	}

	private void deleteOldImageFromDisk(EPApp app) {
		if (app.getImageUrl() != null) {
			String oldImageFullFilePath = ecompportalPath + PATH_SEPARATOR + app.getImageUrl();
			try {
				File oldFile = new File(oldImageFullFilePath);
				if (oldFile.exists() && oldFile.delete()) {
					app.setImageUrl(null);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"old file " + oldImageFullFilePath + " was successfully deleted");
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "old file " + oldImageFullFilePath + " delete failure",
						EcompPortalUtils.getStackTrace(e));
			}
		}
	}

	// No need to optimize this code since it happens only when adding/modifying
	// an application.
	private void saveNewImageToDisk(EPApp app, OnboardingApp onboardingApp, byte[] decodedImage) {
		// Notice!!!
		// using separator did not worked on WINDOWS as the database was saved
		// as assets\images\tmp\ which the UI did not understand thus ichanged
		// it to "/"
		String imageFileName = constructImageName(onboardingApp);
		String imageRelativeFilePath = imageCacheRelativePath + PATH_SEPARATOR + imageFileName;
		String imageFullFilePath = ecompportalPath + PATH_SEPARATOR + imageRelativeFilePath;
		logger.debug(EELFLoggerDelegate.debugLogger,
				"saving app " + onboardingApp.name + " image to " + imageFullFilePath);

		FileOutputStream osf = null;
		try {
			// Base64 Image example: 'data:image/png;base64,AAAFBfj42Pj4'
			logger.info(EELFLoggerDelegate.debugLogger, "Saving new image: " + decodedImage);
			osf = writeImageAsBase64(decodedImage, imageFullFilePath);
			app.setImageUrl(imageRelativeFilePath);
			logger.debug(EELFLoggerDelegate.debugLogger,
					"saveBlobImageToDisk onboardingAppImageUrl = " + imageRelativeFilePath);
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "failed to save image to " + imageFullFilePath,
					EcompPortalUtils.getStackTrace(e));
		} finally {
			if (osf != null) {
				try {
					osf.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"failed to close outpoot stream for image " + imageFullFilePath,
							EcompPortalUtils.getStackTrace(e));
				}
			}
		}
	}

	private FileOutputStream writeImageAsBase64(byte[] decodedImage, String imageFullFilePath)
			throws FileNotFoundException, IOException {
		FileOutputStream osf;
		File of = new File(imageFullFilePath);
		osf = new FileOutputStream(of);
		osf.write(decodedImage);
		osf.flush();
		return osf;
	}

	private String constructImageName(OnboardingApp onboardingApp) {
		return "portal" + String.valueOf(onboardingApp.url.hashCode() + "_" + (int) (Math.random() * 100000.0))
				+ ".png";
	}

	// Don't encrypt or decrypt the password if it is null or the empty string
	private String decryptedPassword(String encryptedAppPwd, EPApp app) {
		String result = "";
		if (encryptedAppPwd != null & encryptedAppPwd.length() > 0) {
			try {
				result = CipherUtil.decrypt(encryptedAppPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Unable to decrypt, App name = " + app.getName(),
						EcompPortalUtils.getStackTrace(e));
			}
		}
		return result;
	}

	private String encryptedPassword(String decryptedAppPwd, EPApp app) {
		String result = "";
		if (decryptedAppPwd != null & decryptedAppPwd.length() > 0) {
			try {
				result = CipherUtil.encrypt(decryptedAppPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Unable to encrypt, App name = " + app,
						EcompPortalUtils.getStackTrace(e));
			}
		}
		return result;
	}

	public void writeAppsImagesToDiskCacheIfNecessary() {
		List<EPApp> apps = getAppsFullList();
		for (EPApp app : apps) {
			String imageFullFilePath = ecompportalPath + PATH_SEPARATOR + app.getImageUrl();
			File f = new File(imageFullFilePath);
			if (f.exists() && !f.isDirectory()) {
				continue;// logger.debug("verified existence.");
			}

			if (app.getThumbnail() == null) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"no thumbnail blob available for this app, cannot write to disk " + app.getName());
				continue;
			}

			FileOutputStream osf = null;
			try {
				logger.info(EELFLoggerDelegate.debugLogger,
						"Rewriting image for " + app.getName() + " from db to: " + imageFullFilePath);
				osf = writeImageAsBase64(app.getThumbnail(), imageFullFilePath);
				logger.debug(EELFLoggerDelegate.debugLogger, "missing, wrote to disk.");
			} catch (IOException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "failed to save image to " + imageFullFilePath,
						EcompPortalUtils.getStackTrace(e));
			} finally {
				try {
					if (osf != null)
						osf.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"writeAppsImagesToDiskCacheIfNecessary: failed to close output stream for image "
									+ imageFullFilePath,
							EcompPortalUtils.getStackTrace(e));
				}
			}
		}
	}

	private void writeAppImageToDiskCacheIfNecessary(EPApp app) {
		if (app == null || app.getImageUrl() == null) {
			return;
		}

		String imageFullFilePath = ecompportalPath + PATH_SEPARATOR + app.getImageUrl();
		File f = new File(imageFullFilePath);

		// Image file
		if (f.exists() && !f.isDirectory()) {
			return;// logger.debug("verified existence.");
		}

		if (app.getThumbnail() == null) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"no thumbnail blob available for this app, cannot write to disk " + app.getName());
			return;
		}

		FileOutputStream osf = null;
		try {
			logger.info(EELFLoggerDelegate.debugLogger,
					"Rewriting image for " + app.getName() + " from db to: " + imageFullFilePath);
			osf = writeImageAsBase64(app.getThumbnail(), imageFullFilePath);
			logger.debug(EELFLoggerDelegate.debugLogger, "missing, wrote to disk.");
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "failed to save image to " + imageFullFilePath,
					EcompPortalUtils.getStackTrace(e));
		} finally {
			try {
				if (osf != null)
					osf.close();
			} catch (IOException e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"writeAppsImagesToDiskCacheIfNecessary: failed to close output stream for image "
								+ imageFullFilePath,
						EcompPortalUtils.getStackTrace(e));
			}
		}
	}

	@Override
	public List<EPApp> getUserRemoteApps(String id) {
		
			StringBuilder query = new StringBuilder();
		
			query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
			query.append(
						"FN_USER_ROLE.USER_ID = " + id + " AND FN_USER_ROLE.ROLE_ID != " + SUPER_ADMIN_ROLE_ID);
			query.append(" AND FN_APP.ENABLED = 'Y'");

			TreeSet<EPApp> distinctApps = new TreeSet<EPApp>();

			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(query.toString(), EPApp.class, null);
			for (EPApp app : adminApps) {
				// Write the image to disk in case if it is missing
				writeAppImageToDiskCacheIfNecessary(app);

				distinctApps.add(app);
			}

			List<EPApp> userApps = new ArrayList<EPApp>();
			userApps.addAll(distinctApps);
			return userApps;
	
	}
	
}