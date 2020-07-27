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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.portal.domain.AdminUserApp;
import org.onap.portalapp.portal.domain.AdminUserApplications;
import org.onap.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.onap.portalapp.portal.domain.AppsResponse;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserAppRolesRequest;
import org.onap.portalapp.portal.domain.EPUserAppRolesRequestDetail;
import org.onap.portalapp.portal.domain.EPUserAppsManualSortPreference;
import org.onap.portalapp.portal.domain.EPUserAppsSortPreference;
import org.onap.portalapp.portal.domain.EPWidgetsManualSortPreference;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.domain.UserRoles;
import org.onap.portalapp.portal.ecomp.model.AppCatalogItem;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.transport.EPAppsManualPreference;
import org.onap.portalapp.portal.transport.EPAppsSortPreference;
import org.onap.portalapp.portal.transport.EPDeleteAppsManualSortPref;
import org.onap.portalapp.portal.transport.EPWidgetsSortPreference;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.ueb.Helper;
import org.onap.portalsdk.core.onboarding.ueb.TopicManager;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.KeyConstants;
import org.onap.portalsdk.core.onboarding.util.KeyProperties;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.att.nsa.apiClient.http.HttpException;
import com.att.nsa.cambria.client.CambriaClient.CambriaApiException;
import com.att.nsa.cambria.client.CambriaClientBuilders;
import com.att.nsa.cambria.client.CambriaIdentityManager;
import com.att.nsa.cambria.client.CambriaTopicManager;

public class EPAppCommonServiceImpl implements EPAppService {

	protected String ECOMP_APP_ID = "1";
	protected String SUPER_ADMIN_ROLE_ID = "1";
	protected String ACCOUNT_ADMIN_ROLE_ID = "999";
	protected String RESTRICTED_APP_ROLE_ID = "900";

	//private static final String urlField = "url";
	private static final String nameSpaceField = "url";

	private static final String nameField = "name";

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPAppCommonServiceImpl.class);

	@Autowired
	private AdminRolesService adminRolesService;
	@Autowired
	protected SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;	
	
	RestTemplate template = new RestTemplate();

	@PostConstruct
	private void init() {
		SUPER_ADMIN_ROLE_ID = SystemProperties.getProperty(EPCommonSystemProperties.SYS_ADMIN_ROLE_ID);
		ACCOUNT_ADMIN_ROLE_ID = SystemProperties.getProperty(EPCommonSystemProperties.ACCOUNT_ADMIN_ROLE_ID);
		ECOMP_APP_ID = SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID);
		RESTRICTED_APP_ROLE_ID = SystemProperties.getProperty(EPCommonSystemProperties.RESTRICTED_APP_ROLE_ID);
	}
	
	public Boolean onboardingAppFieldsValidation(OnboardingApp onboardingApp) {
		//FieldsValidator fieldsValidator = new FieldsValidator();

		if ((!onboardingApp.getRestrictedApp()) &&( onboardingApp.getAppName() == null || onboardingApp.getAppName().length() == 0 || onboardingApp.getRestrictedApp() == null
				|| onboardingApp.getLandingPage() == null || onboardingApp.getLandingPage().length() == 0 || onboardingApp.getRestUrl() == null || onboardingApp.getRestUrl().length() == 0
			    || onboardingApp.getAppBasicAuthUsername() == null || onboardingApp.getAppBasicAuthUsername().length() == 0
				|| onboardingApp.getIsOpen() == null
				|| (onboardingApp.getId() != null && onboardingApp.getId().equals(ECOMP_APP_ID)))
				// For a normal app (appType == PortalConstants.PortalAppId),
				// these fields must be filled
				// in.
				// For a restricted app (appType==2), they will be empty.
				|| ((onboardingApp.getRestrictedApp()) && (onboardingApp.getAppName() == null || onboardingApp.getAppName().length() == 0
						|| onboardingApp.getLandingPage() == null || onboardingApp.getLandingPage().length() == 0 || onboardingApp.getIsOpen() == null))) {
			return false;
		}
		return true;
		
	}
	
	private Boolean onboardingInactiveAppFieldsForValidation(OnboardingApp onboardingApp) {
		if (onboardingApp.getAppName() == null || onboardingApp.getAppName().length() == 0
				|| onboardingApp.getIsOpen() == null) {
			return false;
		}
		return true;
	}

	protected FieldsValidator onboardingAppFieldsChecker(OnboardingApp onboardingApp) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (onboardingApp.getRolesInAAF()) {
			if (!onboardingApp.getIsEnabled()) {
				if (!onboardingInactiveAppFieldsForValidation(onboardingApp)) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else if (onboardingApp.getIsEnabled()) {
				if (onboardingAppFieldsValidation(onboardingApp) == false || onboardingApp.getNameSpace() == null
						|| onboardingApp.getNameSpace().length() == 0) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		} else {
			if (!onboardingApp.getIsEnabled()) {
				if (!onboardingInactiveAppFieldsForValidation(onboardingApp)) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else if (onboardingApp.getIsEnabled()) {
				if(onboardingApp.getRestrictedApp() && onboardingAppFieldsValidation(onboardingApp) == false){
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				}
				else if (!onboardingApp.getRestrictedApp() && (onboardingAppFieldsValidation(onboardingApp) == false || onboardingApp.getAppBasicAuthPassword() == null
						|| onboardingApp.getAppBasicAuthPassword().length() == 0)) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		}
		return fieldsValidator;
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
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
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
				+ "WHERE user.org_user_id = '%s' AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND FN_APP.ENABLED = 'Y'";

		String sql = String.format(format, orgUserId);
		logQuery(sql);

		try {
			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(sql, EPApp.class, null);
			return adminApps;
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			return null;
		}
	}

	@Override
	public List<EPApp> getAppsFullList() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = dataAccessService.getList(EPApp.class, null);
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
			ecompApp.setDescription(app.getAppDescription());
			ecompApp.setNotes(app.getAppNotes());
			ecompApp.setUrl(app.getLandingPage());
			ecompApp.setAlternateUrl(app.getAlternateLandingPage());
			ecompApp.setUebTopicName(app.getUebTopicName());
			//ecompApp.setUebKey(app.getUebKey());
			ecompApp.setUebSecret(app.getUebSecret());
			ecompApp.setEnabled(app.getEnabled());
			ecompApp.setCentralAuth(app.getRolesInAAF());
			ecompApp.setNameSpace(app.getNameSpace());
			ecompApp.setRestrictedApp(app.isRestrictedApp());
			ecompAppList.add(ecompApp);
		}
		return ecompAppList;
	}

	@Override
	public EPApp getApp(Long appId) {
		try {
			return (EPApp) dataAccessService.getDomainObject(EPApp.class, appId, null);
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			return null;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppIdAndNameTransportModel> getAdminApps(EPUser user) {

		if (adminRolesService.isAccountAdmin(user) && adminRolesService.isRoleAdmin(user)) {
			final Map<String, Long> params = new HashMap<>();
			params.put("userId", user.getId());
			List applicationRoleswithAccountandRoleadmin = dataAccessService
					.executeNamedQuery("getApplicationsofTheUserwithAdminAndRoleAdmin", params, null);
			try {
				return applicationRoleswithAccountandRoleadmin;
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				logger.error(EELFLoggerDelegate.errorLogger,
						"Exception occurred while fetching the list of user who has type account and role approver "
								+ user.getLoginId(),
						e);
			}
		}

		else {
			if (adminRolesService.isAccountAdmin(user)) {
				String format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
						+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
						+ " AND (app.ENABLED = 'Y' OR app.APP_ID=1)";
				String sql = String.format(format, user.getId());
				logQuery(sql);
				try {
					return dataAccessService.executeSQLQuery(sql, AppIdAndNameTransportModel.class, null);
				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
					logger.error(EELFLoggerDelegate.errorLogger,
							"Exception occurred while fetching the adminApps for user " + user.getLoginId(), e);
				}

			}

			if (adminRolesService.isRoleAdmin(user)) {
				final Map<String, Long> params = new HashMap<>();
				params.put("userId", user.getId());
				List applicationRoles = dataAccessService.executeNamedQuery("getApplicationsofTheUserContainsApprover",
						params, null);

				try {
					return applicationRoles;
				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
					logger.error(EELFLoggerDelegate.errorLogger,
							"Exception occurred while fetching the list of user who has type approver "
									+ user.getLoginId(),
							e);
				}

			}
		}
		// sql += " AND app.APP_REST_ENDPOINT IS NOT NULL AND
		// app.APP_REST_ENDPOINT <> ''";

		return new ArrayList<AppIdAndNameTransportModel>();
	}

	@Override
	public EPApp getAppDetail(String appName) {
		final Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("appName", appName);
			@SuppressWarnings("unchecked")
			List<EPApp> apps = (List<EPApp>) dataAccessService.executeNamedQuery("getMyloginAppDetails", params, null);
			return (apps.size() > 0) ? apps.get(0) : null;
		} catch(Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			return null;
		}
	}
	
	@Override
	public EPApp getAppDetailByAppName(String appName) {
		final Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("appName", appName);
			@SuppressWarnings("unchecked")
			List<EPApp> apps = (List<EPApp>) dataAccessService.executeNamedQuery("getAppDetailsByAppName", params, null);
			if (apps.size() > 0) {
				EPApp app = apps.get(0);
				if (!EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
					app.setRolesInAAF(false);
				}
				return app;
			} else{
				return null;
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppIdAndNameTransportModel> getAppsForSuperAdminAndAccountAdmin(EPUser user) {
		if (adminRolesService.isSuperAdmin(user) || adminRolesService.isAccountAdmin(user)) {
			String format = "";
			String sql = "";
			if (adminRolesService.isSuperAdmin(user)) {
				format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app "
						+ "where app.ENABLED = 'Y' AND app.app_type = 1";
			} else {
				format = "SELECT app.APP_ID, app.APP_NAME, APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
						+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
						+ " AND app.ENABLED = 'Y' AND app.app_type = 1";
			}
			sql = String.format(format, user.getId());
			// sql += " AND app.APP_REST_ENDPOINT IS NOT NULL AND
			// app.APP_REST_ENDPOINT <> ''";
			logQuery(sql);
			try {
				return dataAccessService.executeSQLQuery(sql, AppIdAndNameTransportModel.class, null);
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				logger.error(EELFLoggerDelegate.errorLogger,
						"Exception occurred while fetching the adminApps for user " + user.getLoginId(), e);
			}
		}
		return new ArrayList<AppIdAndNameTransportModel>();
	}

	protected void logQuery(String sql) {
		logger.debug(EELFLoggerDelegate.debugLogger, "logQuery: " + sql);
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdminUserApplications> getAppsAdmins() {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("accountAdminRoleId", ACCOUNT_ADMIN_ROLE_ID);
			List<AdminUserApp> adminApps = (List<AdminUserApp>) dataAccessService.executeNamedQuery("getAppsAdmins",
					params, null);
			return aggregateRowsResultsByUserId(adminApps);
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
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
				: (List<EPApp>) dataAccessService.getList(EPApp.class,
						" where ( enabled = 'Y' or id = " + ECOMP_APP_ID + ")", "name", null);

		List<AppsResponse> appsModified = new ArrayList<AppsResponse>();
		for (EPApp app : apps) {
			appsModified.add(new AppsResponse(app.getId(), app.getName(), app.isRestrictedApp(), app.getEnabled()));
		}
		return appsModified;
	}

	
	@Override
	public List<AppsResponse> getAllApplications(Boolean all) {
		// If all is true, return both active and inactive apps. Otherwise, just
		// active apps.
		@SuppressWarnings("unchecked")
		// Sort the list by application name so the drop-down looks pretty.
		List<EPApp> apps = all
				? (List<EPApp>) dataAccessService.getList(EPApp.class, " where id != " + ECOMP_APP_ID, "name", null)
						:dataAccessService.getList(EPApp.class, null);

		List<AppsResponse> appsModified = new ArrayList<AppsResponse>();
		for (EPApp app : apps) {
			appsModified.add(new AppsResponse(app.getId(), app.getName(), app.isRestrictedApp(), app.getEnabled()));
		}
		return appsModified;
	}
	@Override
	public UserRoles getUserProfile(String loginId) {
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", loginId);
		@SuppressWarnings("unchecked")
		List<UserRole> userRoleList = dataAccessService.executeNamedQuery( "getUserRoles", params, null);
		ArrayList<UserRoles> usersRolesList = aggregateUserProfileRowsResultsByRole(userRoleList);
		if (usersRolesList == null || usersRolesList.size() < 1)
			return null;

		return usersRolesList.get(0);
	}

	@Override
	public UserRoles getUserProfileNormalized(EPUser user) {
		// Check database.
		UserRoles userAndRoles = getUserProfile(user.getLoginId());
		// If no roles are defined, treat this user as a guest.
		if (user.isGuest() || userAndRoles == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserProfile: treating user {} as guest",
					user.getLoginId());
			UserRole userRole = new UserRole();
			userRole.setUser_Id(user.getId());
			userRole.setOrgUserId(user.getLoginId());
			userRole.setFirstName(user.getFirstName());
			userRole.setLastName(user.getLastName());
			userRole.setRoleId(-1L);
			userRole.setRoleName("Guest");
			userRole.setUser_Id(-1L);
			userAndRoles = new UserRoles(userRole);
		}

		return userAndRoles;
	}

	protected ArrayList<UserRoles> aggregateUserProfileRowsResultsByRole(List<UserRole> userRoleList) {
		HashMap<String, UserRoles> userRoles = new HashMap<String, UserRoles>();
		for (UserRole user : userRoleList) {
			String orgUserId = user.getOrgUserId();
			if (userRoles.get(orgUserId) == null)
				userRoles.put(orgUserId, new UserRoles(user));
			else
				userRoles.get(orgUserId).addRole(user.getRoleName());
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
			sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND ROLE_ID = '" + RESTRICTED_APP_ROLE_ID + "'";
		}else if(appId == 1){
			sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND APP_ID IS NULL";
		}else{
			sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND APP_ID = '" + appId + "'";
		}
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<LocalRole> appRoles = dataAccessService.executeSQLQuery(sql, LocalRole.class, null);
		return appRoles;
	}

	protected String userAppsQuery(EPUser user) {
		StringBuilder query = new StringBuilder();
		if (adminRolesService.isSuperAdmin(user)) {
			query.append("SELECT * FROM FN_APP where FN_APP.ENABLED = 'Y' ORDER BY APP_NAME");
		} else {
			query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
			query.append(
					"FN_USER_ROLE.USER_ID = " + user.getId() + " AND FN_USER_ROLE.ROLE_ID != " + SUPER_ADMIN_ROLE_ID);
			query.append(" AND FN_APP.ENABLED = 'Y'");
		}
		return query.toString();
	}

	/*protected FieldsValidator onboardingAppFieldsChecker(OnboardingApp onboardingApp) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if(onboardingApp.isCentralAuth){
		if (onboardingApp.name == null || onboardingApp.name.length() == 0 || onboardingApp.url == null
				|| onboardingApp.url.length() == 0 || onboardingApp.getRestrictedApp() == null
				|| onboardingApp.getIsOpen() == null || onboardingApp.getIsEnabled() == null
				|| (onboardingApp.getId() != null && ECOMP_APP_ID.equals(onboardingApp.getId().toString()))
				// For a normal app (appType == PortalConstants.PortalAppId),
				// these fields must be filled
				// in.
				// For a restricted app (appType==2), they will be empty.
				|| ((!onboardingApp.getRestrictedApp()) && (onboardingApp.myLoginsAppName == null
						|| onboardingApp.myLoginsAppName.length() == 0 || onboardingApp.myLoginsAppOwner == null
						|| onboardingApp.myLoginsAppOwner.length() == 0 || onboardingApp.username == null
						|| onboardingApp.username.length() == 0 ))) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
		}
		}else{

			if (onboardingApp.name == null || onboardingApp.name.length() == 0 || onboardingApp.url == null
					|| onboardingApp.url.length() == 0 || onboardingApp.getRestrictedApp() == null
					|| onboardingApp.getIsOpen() == null || onboardingApp.getIsEnabled() == null
					|| (onboardingApp.getId() != null && ECOMP_APP_ID.equals(onboardingApp.getId().toString()))
					// For a normal app (appType == PortalConstants.PortalAppId),
					// these fields must be filled
					// in.
					// For a restricted app (appType==2), they will be empty.
					|| ((!onboardingApp.getRestrictedApp()) && (onboardingApp.myLoginsAppName == null
							|| onboardingApp.myLoginsAppName.length() == 0 || onboardingApp.myLoginsAppOwner == null
							|| onboardingApp.myLoginsAppOwner.length() == 0 || onboardingApp.username == null
							|| onboardingApp.username.length() == 0 || onboardingApp.appPassword == null
							|| onboardingApp.appPassword.length() == 0))) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			
		}
		return fieldsValidator;
	}*/

	@Override
	public List<EPApp> getUserApps(EPUser user) {
		List<EPApp> openApps = getOpenApps();

		if (user.isGuest()) {
			return openApps;
		} else {
			String sql = userAppsQuery(user);
			logQuery(sql);

			// TreeSet<EPApp> distinctApps = new TreeSet<EPApp>();
			List<EPApp> appsList = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(sql, EPApp.class, null);
			HashSet<EPApp> appSet = new HashSet<>();
			for (EPApp app : adminApps) {
				appSet.add(app);
				appsList.add(app);
			}

			for (EPApp app : openApps) {
				if (!appSet.contains(app))
					appsList.add(app);
			}

			return appsList;
		}
	}

	@Override
	public List<EPApp> getPersAdminApps(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to EPApp
		@SuppressWarnings("unchecked")
		List<EPApp> list = dataAccessService.executeNamedQuery("getPersAdminApps", params, null);
		return list;
	}

	@Override
	public List<EPApp> getPersUserApps(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		// Named query is stored in EP.hbm.xml, mapped to EPApp
		@SuppressWarnings("unchecked")
		List<EPApp> list = dataAccessService.executeNamedQuery("getPersUserApps", params, null);
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.onap.portalapp.portal.service.EPAppService#getAppCatalog(
	 * org.onap.portalapp.portal.domain.EPUser)
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
	 *
	 * @see
	 * org.onap.portalapp.portal.service.EPAppService#getAdminAppCatalog(
	 * org.onap.portalapp.portal.domain.EPUser)
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

	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getAppsOrderByName(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		List<EPApp> sortedAppsByName = null;
		try {
			if (adminRolesService.isSuperAdmin(user)) {
				params.put("userId", user.getId());
				sortedAppsByName = dataAccessService.executeNamedQuery("getPersAdminAppsOrderByName", params, null);
			} else {
				params.put("userId", user.getId());
				sortedAppsByName = dataAccessService.executeNamedQuery("getPersUserAppsOrderByName", params, null);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsOrderByName failed", e);
		}
		return sortedAppsByName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getAppsOrderByLastUsed(EPUser user) {

		final Map<String, Long> params = new HashMap<>();
		List<EPApp> sortedAppsByLastUsed = new ArrayList<EPApp>();
		List<EPApp> finalsortedAppsByLastUsed = new ArrayList<EPApp>();
		try {
			if (adminRolesService.isSuperAdmin(user)) {
				params.put("userId", user.getId());
				sortedAppsByLastUsed = dataAccessService.executeNamedQuery("getAdminAppsOrderByLastUsed", params, null);
			} else {
				params.put("userId", user.getId());
				sortedAppsByLastUsed = dataAccessService.executeNamedQuery("getUserAppsOrderByLastUsed", params, null);
			}
			Set<String> epAppSet = new HashSet<String>();
			for (EPApp eapp : sortedAppsByLastUsed)
				if (!epAppSet.contains(eapp.getName())) {
					finalsortedAppsByLastUsed.add(eapp);
					epAppSet.add(eapp.getName());
				}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsOrderByLastUsed failed", e);
		}
		return finalsortedAppsByLastUsed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getAppsOrderByMostUsed(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		List<EPApp> sortedAppsByMostUsed = new ArrayList<EPApp>();
		List<EPApp> finalsortedAppsByMostUsed = new ArrayList<EPApp>();
		try {
			if (adminRolesService.isSuperAdmin(user)) {
				params.put("userId", user.getId());
				sortedAppsByMostUsed = dataAccessService.executeNamedQuery("getAdminAppsOrderByMostUsed", params, null);
			} else {
				params.put("userId", user.getId());
				sortedAppsByMostUsed = dataAccessService.executeNamedQuery("getUserAppsOrderByMostUsed", params, null);
			}
			Set<String> epAppSet = new HashSet<String>();

			for (EPApp eapp : sortedAppsByMostUsed) {
				if (!epAppSet.contains(eapp.getName())) {
					finalsortedAppsByMostUsed.add(eapp);
					epAppSet.add(eapp.getName());
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsOrderByMostUsed failed", e);
		}

		return finalsortedAppsByMostUsed;
	}

	/*
	 * This Method retrieves the User Apps by Sort Manual Preference
	 *
	 * @param: user--contains LoggedIn User Data
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EPApp> getAppsOrderByManual(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		List<EPApp> sortedAppsByManual = new ArrayList<EPApp>();
		List<EPApp> finalsortedAppsByManual = new ArrayList<EPApp>();
		try {
			if (adminRolesService.isSuperAdmin(user)) {
				params.put("userId", user.getId());
				sortedAppsByManual = dataAccessService.executeNamedQuery("getAdminAppsOrderByManual", params, null);
			} else {
				params.put("userId", user.getId());
				sortedAppsByManual = dataAccessService.executeNamedQuery("getUserAppsOrderByManual", params, null);
			}
			Set<String> epAppSet = new HashSet<String>();

			for (EPApp eapp : sortedAppsByManual) {
				if (!epAppSet.contains(eapp.getName())) {
					finalsortedAppsByManual.add(eapp);
					epAppSet.add(eapp.getName());
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsOrderByManual failed", e);
		}
		return finalsortedAppsByManual;
	}
	
	@Override
	public List<OnboardingApp> getOnboardingApps() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = dataAccessService.getList(EPApp.class, " where id!=" + ECOMP_APP_ID, null, null);
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		for (EPApp app : apps) {
			OnboardingApp onboardingApp = new OnboardingApp();
			app.setAppBasicAuthPassword(EPCommonSystemProperties.APP_DISPLAY_PASSWORD);//to hide password from get request
			createOnboardingFromApp(app, onboardingApp);
			onboardingAppsList.add(onboardingApp);
		}
		return onboardingAppsList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OnboardingApp> getAdminAppsOfUser(EPUser user) {
		
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		List<Integer> userAdminApps = new ArrayList<>();
		final Map<String, Long> userParams = new HashMap<>();
		userParams.put("userId", user.getId());	
		userAdminApps =  dataAccessService.executeNamedQuery("getAllAdminAppsofTheUser", userParams, null);
		
//		userAdminApps.removeIf(x -> x == Integer.valueOf(ECOMP_APP_ID));
		
		logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for userAdminApps() - for user {}, found userAdminAppsSize {}", user.getOrgUserId(), userAdminApps.size());
		onboardingAppsList = getOnboardingApps();
		
		final List<Integer> userAdminApps1 = userAdminApps;
		List<OnboardingApp> userApplicationAdmins = onboardingAppsList.stream().filter(x -> userAdminApps1.contains((int) (long)x.getId())).collect(Collectors.toList());
		
        return userApplicationAdmins;
	}

	@Override
	public List<OnboardingApp> getEnabledNonOpenOnboardingApps() {
		@SuppressWarnings("unchecked")
		List<EPApp> apps = dataAccessService.getList(EPApp.class,
				" where enabled = true and open = false and app_type!= 3 and id!=" + ECOMP_APP_ID, null, null);
	
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		for (EPApp app : apps) {
			OnboardingApp onboardingApp = new OnboardingApp();
			createOnboardingFromApp(app, onboardingApp);
			onboardingAppsList.add(onboardingApp);
		}
		return onboardingAppsList;
	}

	@SuppressWarnings("unchecked")
	private void validateOnboardingApp(OnboardingApp onboardingApp, FieldsValidator fieldsValidator) {
		boolean duplicatedNameSpace = false;
		boolean duplicatedName = false;
		List<EPApp> apps;
		if (onboardingApp.getId() == null) {
			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion nameCrit = Restrictions.eq("name",onboardingApp.getAppName());
			Criterion nameSpaceCrit = null;
			Criterion	orCrit = null;
			if (onboardingApp.getRolesInAAF()) {
				nameSpaceCrit = Restrictions.eq("nameSpace", onboardingApp.getNameSpace());
				orCrit = Restrictions.or(nameCrit, nameSpaceCrit);
			} else
				orCrit = Restrictions.or(nameCrit);
			restrictionsList.add(orCrit);
			apps = (List<EPApp>) dataAccessService.getList(EPApp.class, null, restrictionsList, null);
		} else {
			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion idCrit =Restrictions.eq("id", onboardingApp.getId());
			Criterion nameCrit = Restrictions.eq("name",onboardingApp.getAppName());
			Criterion nameSpaceCrit = null;
			Criterion orCrit= null;
			if (onboardingApp.getRolesInAAF()) {
				nameSpaceCrit = Restrictions.eq("nameSpace",onboardingApp.getNameSpace());
				orCrit = Restrictions.or(idCrit, nameSpaceCrit, nameCrit);
			}
			else
			 orCrit = Restrictions.or(idCrit, nameCrit);
			
			restrictionsList.add(orCrit);
			apps = (List<EPApp>) dataAccessService.getList(EPApp.class, null, restrictionsList, null);
			
		}
		for (EPApp app : apps) {
			if (onboardingApp.getId() != null && onboardingApp.getId().equals(app.getId())) {
				continue;
			}
			if (!duplicatedName && app.getName().equalsIgnoreCase(onboardingApp.getAppName())) {
				duplicatedName = true;
				if (duplicatedName) {
					break;
				}
			}
			if (!duplicatedNameSpace && app.getNameSpace().equalsIgnoreCase(onboardingApp.getNameSpace())) {
				duplicatedNameSpace = true;
				if (duplicatedNameSpace) {
					break;
				}
			}
			
		}
		if (duplicatedNameSpace || duplicatedName) {
			if (duplicatedNameSpace) {
				fieldsValidator.addProblematicFieldName(nameSpaceField);
			}
			if (duplicatedName) {
				fieldsValidator.addProblematicFieldName(nameField);
			}
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
			fieldsValidator.errorCode = new Long(EPCommonSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);
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
			if (modifiedOnboardingApp.getId() != null) {
				updateApp(modifiedOnboardingApp.getId(), modifiedOnboardingApp, fieldsValidator, user);
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
			if (newOnboardingApp.getId() == null) {
				updateApp(null, newOnboardingApp, fieldsValidator, user);
			} else {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator deleteOnboardingApp(EPUser user, Long appid) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (!adminRolesService.isSuperAdmin(user)) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
			return fieldsValidator;
		}
		final Map<String, Long> params = new HashMap<>();
		params.put("app_id", appid);
		
		//Checking if App is associated with any exiting microservices- ep_microservice:
		final Map<String, Long> queryparams = new HashMap<>();
		queryparams.put("applicationId", appid);
		List<MicroserviceData> microservicesList  = dataAccessService.executeNamedQuery( "getMicroservicesByAppId", queryparams, null);
		if(microservicesList!=null && microservicesList.size()>0) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return fieldsValidator;
		}
		
		List<EPUserAppRolesRequest> EPUserAppRolesRequestList= new ArrayList<>();
		EPUserAppRolesRequestList = dataAccessService.executeNamedQuery( "getRequestIdsForApp", params, null);
	    for(int i=0;i<EPUserAppRolesRequestList.size();i++)
	    {
	     dataAccessService.deleteDomainObjects(EPUserAppRolesRequestDetail.class , "req_id=" + EPUserAppRolesRequestList.get(i).getId(),null);
	    	
	    }
	    Session localSession = null;
		Transaction transaction = null;
		Boolean result = false;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			
			// 1) Remove the URL for any functional menu item associated with
			// this app
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting URL for any functional menu item associated with app");
			// Named query is stored in EP.hbm.xml, mapped to EPApp
			 dataAccessService.executeNamedQuery("updateMenuFunctionalAndRoles", params, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp:  Deleted URL for any functional menu item associated with app");

			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting favorites associated with a menu item that is associated with this app");
			// 2)Remove any favorites associated with a menu item that is
			// associated with this app
			dataAccessService.executeNamedQuery("removeAppFromMenuFavorites", params, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted favorites associated with a menu item that is associated with this app");

			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting role, appid records from fn_menu_functional_role that are associated with this app");
			// 3)Remove all role, appid records from fn_menu_functional_role
			// that are associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromMenuFunctionalRoles", params, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted role, appid records from fn_menu_functional_role that are associated with this app");

			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting all roles, rolefunctions, appid records from ep_app_role_function that are associated with this app");
			// 4)Remove all roles, rolefunctions, appid records from ep_app_role_function
			// that are associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromEpAppRoleFunction", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted all roles, rolefunctions, appid records from ep_app_role_function that are associated with this app");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting all rolefunctions, appid records from ep_app_function that are associated with this app");
			// 5)Remove all rolefunctions, appid records from ep_app_function
			// that are associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromEpAppFunction", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp:  Deleted all rolefunctions, appid records from ep_app_function that are associated with this app");
	 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting all records from fn_user_role associated with this app");
			// 6)Remove all records from fn_user_role associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromFnUserRole", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted all records from fn_user_role associated with this app");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting any widgets associated with this app");
			// 7)Remove any widgets associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromEpWidgetCatalogRole", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted widgets associated with this app");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting any roles associated with this app");
			// 8)Remove any roles associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromEpRoleNotification", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted roles associated with this app");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting all records from fn_role associated with this app");
			// 9)Remove all records from fn_role associated with this app
			 dataAccessService.executeNamedQuery("removeAppFromFnRole", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted all records from fn_role associated with this app");
			
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting in table fn_app_contact_us entries associated with this app");
			 // 10)Remove app contact us entries
			 dataAccessService.executeNamedQuery("removeAppFromAppContactUs", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted in table fn_app_contact_us entries associated with this app");

			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting rows in the fn_pers_user_app_sel table");
			// 11)Remove rows in the app personalization selection table
			 dataAccessService.executeNamedQuery("removeAppFromEpPersUserAppSel", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted rows in the fn_pers_user_app_sel table");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting rows in the ep_pers_user_app_man_sort table");
			// 12)Remove rows in the app personalization sort table
			 dataAccessService.executeNamedQuery("removeAppFromEpPersUserAppManSort", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted rows in the ep_pers_user_app_man_sort table");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting rows in the ep_user_roles_request table");
			// 13)Remove rows in the app personalization sort table
			 dataAccessService.executeNamedQuery("removeAppFromEpUserRolesRequest", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted rows in the ep_user_roles_request table");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting rows in the ep_web_analytics_source");
			// 14)Remove rows in the ep_web_analytics_source
			 dataAccessService.executeNamedQuery("removeAppFromEpWebAnalytics", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted rows in the ep_web_analytics_source");
			 
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleting the app ");
			// 15)Delete the app
			 dataAccessService.executeNamedQuery("removeAppFromFnApp", params, null);
			 logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp: Deleted the app");
			
			/*
			// 1) Remove the URL for any functional menu item associated with
			// this app
			String sql = "UPDATE fn_menu_functional m, fn_menu_functional_roles mr SET m.url='' "
					+ " WHERE m.menu_id=mr.menu_id " + " AND mr.app_id='" + appid + "'";
			logQuery(sql);
			Query query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove any favorites associated with a menu item that is
			// associated with this app
			sql = "Delete from fn_menu_favorites " + " using fn_menu_favorites inner join fn_menu_functional_roles "
					+ " where fn_menu_functional_roles.app_id='" + appid + "' "
					+ " AND fn_menu_functional_roles.menu_id=fn_menu_favorites.menu_id";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove all role, appid records from fn_menu_functional_role
			// that are associated with this app
			sql = "delete from fn_menu_functional_roles where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			
			// Remove all roles, rolefunctions, appid records from ep_app_role_function
			// that are associated with this app
		    sql = "DELETE FROM ep_app_role_function WHERE app_id='" + appid + "'";
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			 query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			
			//Remove all rolefunctions, appid records from ep_app_function
			// that are associated with this app
			sql = "DELETE FROM ep_app_function WHERE app_id='" + appid + "'";
			logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove all records from fn_user_role associated with this app
			sql = "delete from fn_user_role where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove any widgets associated with this app
			sql = "delete from ep_widget_catalog_role where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove any roles associated with this app
			sql = "delete from ep_role_notification " + " using ep_role_notification inner join fn_role "
					+ " where fn_role.app_id='" + appid + "' " + " and ep_role_notification.role_id= fn_role.role_id";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove all records from fn_role associated with this app
			sql = "delete from fn_role where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove app contact us entries
			sql = "delete from fn_app_contact_us where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove rows in the app personalization selection table
			sql = "delete from fn_pers_user_app_sel where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove rows in the app personalization sort table
			sql = "delete from ep_pers_user_app_man_sort where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove rows in the app personalization sort table
			sql = "delete from ep_user_roles_request where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Remove rows in the app personalization sort table
			sql = "delete from ep_web_analytics_source where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			// Delete the app
			sql = "delete from fn_app where app_id='" + appid + "'";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();

			transaction.commit();
			*/
			result = true;
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteOnboardingApp success");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteOnboardingApp failed", e);
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
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
	protected void setFunctionalMenuItemsEnabled(Session localSession, Boolean enabled, Long appId) {
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
	protected void updateApp(Long appId, OnboardingApp onboardingApp, FieldsValidator fieldsValidator, EPUser user) {
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: entering updateApp");
		// Separate out the code for a restricted app, since it doesn't need any
		// of the UEB code.
		if (Boolean.TRUE.equals(onboardingApp.getRestrictedApp())) {
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
				setFunctionalMenuItemsEnabled(localSession, onboardingApp.getIsEnabled(), appId);
				transaction.commit();
				result = true;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "updateApp failed", e);
				EcompPortalUtils.rollbackTransaction(transaction,
						"updateApp rollback, exception = " + e.toString());
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "updateApp");
			}
			if (!result) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		} else {
			updateRestrictedApp(appId, onboardingApp, fieldsValidator, user);
			
		}
	}
	

	protected void updateRestrictedApp(Long appId, OnboardingApp onboardingApp, FieldsValidator fieldsValidator,
			EPUser user) {
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
					/*
					 * In the parent class, the UEB code is responsible for generating the
					 * keys/secret/mailbox but UEB Messaging is not actually being used currently;
					 * may be used in future at which point we can just remove this method and
					 * depend on parent class's method So, using UUID generator to generate the
					 * unique key instead.
					 */
					String uuidStr = UUID.randomUUID().toString();
					String appKey = uuidStr;
					String appSecret = uuidStr;
					String appMailboxName = "ECOMP-PORTAL-OUTBOX";
					onboardingApp.setUebTopicName(appMailboxName);
					onboardingApp.setUebKey(appKey);
					onboardingApp.setUebSecret(appSecret);
				} else {
					app = (EPApp) localSession.get(EPApp.class, appId);
					if (app == null || app.getId() == null) {
						// App is already deleted!
						transaction.commit();
						localSession.close();
						fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
				}
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: about to call createAppFromOnboarding");
				createAppFromOnboarding(app, onboardingApp, localSession);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling createAppFromOnboarding");
				localSession.saveOrUpdate(app);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling localSession.saveOrUpdate");
				// Enable or disable all menu items associated with this app
				setFunctionalMenuItemsEnabled(localSession, onboardingApp.getIsEnabled(), appId);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling setFunctionalMenuItemsEnabled");
				transaction.commit();
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling transaction.commit");
				result = true;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "updateRestrictedApp failed", e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebRegisterOnboardingAppError, e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				EcompPortalUtils.rollbackTransaction(transaction,
						"updateRestrictedApp rollback, exception = " + e.toString());
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "updateRestrictedApp");
			}
			if (!result) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}

	}

	@Deprecated
	protected void updateRestrictedAppUeb(Long appId, OnboardingApp onboardingApp, FieldsValidator fieldsValidator,
			EPUser user) {
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
					// admin on the ONAP portal.
					// -------------------------------------------------------------------------------------------
					TopicManager topicManager = new TopicManager() {

						EPAppCommonServiceImpl service;

						public void init(EPAppCommonServiceImpl _service) {
							service = _service;
						}

						public void createTopic(String key, String secret, String topicName,
								String topicDescription) throws HttpException, CambriaApiException, IOException {

							init(EPAppCommonServiceImpl.this);
							final LinkedList<String> urlList = (LinkedList<String>) Helper.uebUrlList();
							if (logger.isInfoEnabled()) {
								logger.info("==> createTopic");
								logger.info("topicName: " + topicName);
								logger.info("topicDescription: " + topicDescription);
							}
							CambriaTopicManager tm = null;
							try {
								tm = service.getTopicManager(urlList, key, secret);
							} catch (Exception e) {
								logger.error("pub.build Exception ", e);
								throw new CambriaApiException(topicName);
							}
							tm.createTopic(topicName, topicDescription, 1, 1);
						}

						public void addPublisher(String topicOwnerKey, String topicOwnerSecret, String publisherKey,
								String topicName) throws HttpException, CambriaApiException, IOException {
							logger.info("==> addPublisher to topic " + topicName);
							final LinkedList<String> urlList = (LinkedList<String>) Helper.uebUrlList();
							CambriaTopicManager tm = null;
							try {
								tm = service.getTopicManager(urlList, topicOwnerKey, topicOwnerSecret);
							} catch (Exception e) {
								logger.error("pub.build Exception ", e);
								throw new CambriaApiException(topicName);
							}
							tm.allowProducer(topicName, publisherKey);
						}

					};
					final CambriaIdentityManager im = new CambriaClientBuilders.IdentityManagerBuilder()
							.usingHosts(Helper.uebUrlList()).build();
					com.att.nsa.apiClient.credentials.ApiCredential credential = im.createApiKey(user.getEmail(),
							"ONAP Portal Owner");
					String appKey = credential.getApiKey();
					String appSecret = credential.getApiSecret();
					String appMailboxName = null;

					int maxNumAttemptsToCreateATopic = 3;
					boolean successfullyCreatedMailbox = false;
					for (int i = 0; i < maxNumAttemptsToCreateATopic; i++) {
						appMailboxName = "ECOMP-PORTAL-OUTBOX-" + (int) (Math.random() * 100000.0);

						try {
							topicManager.createTopic(
									PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY),
									PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET),
									appMailboxName, "ECOMP outbox for app" + onboardingApp.getAppName());
							successfullyCreatedMailbox = true;
							logger.debug(EELFLoggerDelegate.debugLogger,
									"Successfully created " + appMailboxName + " for App " + onboardingApp.getAppName());
							logger.debug(EELFLoggerDelegate.debugLogger, "    Key = " + appKey + " Secret = "
									+ appSecret + " generated using = " + user.getEmail());
							break;
						} catch (HttpException e) {
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebConnectionError, e);
							if (e.getStatusCode() == 409) {
								logger.error(EELFLoggerDelegate.errorLogger, "Topic/mailbox " + appMailboxName
										+ " already exists. Will try using a different name", e);
							} else {
								logger.error(EELFLoggerDelegate.errorLogger, "HttpException when onboarding App: ",
										e);
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
							 * This App is a subscriber of its own mailbox
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
							EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebRegisterOnboardingAppError, e);
							logger.error(EELFLoggerDelegate.errorLogger,
									"Error when configuring Publisher/Subscriber for App's new mailbox", e);
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
					if (app == null || app.getId() == null) {
						// App is already deleted!
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
				setFunctionalMenuItemsEnabled(localSession, onboardingApp.getIsEnabled(), appId);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"LR: updateApp: finished calling setFunctionalMenuItemsEnabled");
				transaction.commit();
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: updateApp: finished calling transaction.commit");
				logger.debug(EELFLoggerDelegate.debugLogger,
						"LR: updateApp: finished calling epUebHelper.addPublisher");
				result = true;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "updateApp failed", e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebRegisterOnboardingAppError, e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				EcompPortalUtils.rollbackTransaction(transaction,
						"updateApp rollback, exception = " + e.toString());
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "updateApp");
			}
			if (!result) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}

	}

	public CambriaTopicManager getTopicManager(List<String> urlList, String key, String secret)
			throws GeneralSecurityException, Exception {
		throw new Exception("This method can only be invoked from child class");
	}

	/**
	 * Populates a transport model of the application from a database row model.
	 * Leaves out the thumbnail because the FE fetches images via a different
	 * API.
	 * 
	 * @param app
	 *            Model of database row
	 * @param onboardingApp
	 *            Model for transport as JSON
	 */
	@Override
	public void createOnboardingFromApp(EPApp app, OnboardingApp onboardingApp) {
		onboardingApp.setId(app.getId());
		onboardingApp.setAppName(app.getName());
		onboardingApp.setImageUrl(app.getImageUrl());
		onboardingApp.setAppDescription(app.getAppDescription());
		onboardingApp.setAppNotes(app.getAppNotes());
		onboardingApp.setLandingPage(app.getLandingPage());
		onboardingApp.setAlternateLandingPage(app.getAlternateLandingPage());
		onboardingApp.setRestUrl(app.getAppRestEndpoint());
		onboardingApp.setIsOpen(app.getOpen());
		onboardingApp.setIsEnabled(app.getEnabled());
		onboardingApp.setAppBasicAuthUsername(app.getAppBasicAuthUsername());
		
		String effectivePwd = null;
		if (app.getAppBasicAuthPassword().equals(EPCommonSystemProperties.APP_DISPLAY_PASSWORD))
			effectivePwd = EPCommonSystemProperties.APP_DISPLAY_PASSWORD;
		else
			effectivePwd = decryptedPassword(app.getAppBasicAuthPassword(), app);
		
		onboardingApp.setAppBasicAuthPassword(effectivePwd);
		onboardingApp.setUebTopicName(app.getUebTopicName());
		onboardingApp.setUebKey(app.getUebKey());
		onboardingApp.setUebSecret(app.getUebSecret());
		onboardingApp.setRolesInAAF(app.getRolesInAAF());
		onboardingApp.setNameSpace(app.getNameSpace());
		onboardingApp.setRestrictedApp(app.isRestrictedApp());
		onboardingApp.setModeOfIntegration(app.getModeOfIntegration());
		onboardingApp.setAppAck(app.getAppAck());
		onboardingApp.setUsesCadi(app.getUsesCadi());
		onboardingApp.setApplicationType(app.getAppType().toString());
	}

	/**
	 * Creates a database object for an application from an uploaded transport
	 * model. Must decode the thumbnail, if any.
	 * 
	 * @param app
	 * @param onboardingApp
	 * @param localSession
	 * @return The first argument.
	 */
	protected EPApp createAppFromOnboarding(EPApp app, OnboardingApp onboardingApp, Session localSession) {
		app.setName(onboardingApp.getAppName());
		app.setAppDescription(onboardingApp.getAppDescription());
		app.setAppNotes(onboardingApp.getAppNotes());
		app.setLandingPage(onboardingApp.getLandingPage());
		app.setAlternateLandingPage(onboardingApp.getAlternateLandingPage());
		app.setAppRestEndpoint(onboardingApp.getRestUrl());
		app.setOpen(onboardingApp.getIsOpen());
		app.setEnabled(onboardingApp.getIsEnabled());
		app.setAppBasicAuthUsername(onboardingApp.getAppBasicAuthUsername());
		if(!onboardingApp.getAppBasicAuthPassword().equals(EPCommonSystemProperties.APP_DISPLAY_PASSWORD))
		app.setAppBasicAuthPassword(this.encryptedPassword(onboardingApp.getAppBasicAuthPassword(), app));
		//app.setUebTopicName(onboardingApp.uebTopicName);
		app.setUebKey(onboardingApp.getUebKey());
		app.setUebSecret(onboardingApp.getUebSecret());
		app.setRolesInAAF(onboardingApp.getRolesInAAF());
		app.setNameSpace(onboardingApp.getNameSpace());
		app.setAppType(new Integer(onboardingApp.getApplicationType()));		
		app.setModeOfIntegration(onboardingApp.getModeOfIntegration());
		app.setAppAck(onboardingApp.getAppAck());
		app.setUsesCadi(onboardingApp.getUsesCadi());
		
		
		if (!StringUtils.isEmpty(onboardingApp.getThumbnail())) {
			logger.debug(EELFLoggerDelegate.debugLogger, "createAppFromOnboarding: onboarding thumbnail is NOT empty");
			String[] splitBase64Thumbnail = onboardingApp.getThumbnail().split("base64,");
			logger.debug(EELFLoggerDelegate.debugLogger,
					"createAppFromOnboarding: length of splitBase64Thumbnail: " + splitBase64Thumbnail.length);
			if (splitBase64Thumbnail.length > 1) {
				// This occurs when we have a new image, not an existing image
				byte[] decodedImage = Base64.getDecoder().decode(splitBase64Thumbnail[1].getBytes());
				logger.debug(EELFLoggerDelegate.debugLogger, "createAppFromOnboarding: finished calling decode");
				// This is basically a boolean indicator that an image is
				// present.
				app.setImageUrl(constructImageName(onboardingApp));
				app.setThumbnail(decodedImage);
			}
		} else if (app.getThumbnail() != null && onboardingApp.getImageLink() == null) {
			// The thumbnail that came in from the json is empty; the previous
			// thumbnail is NOT empty. Must delete it.
			logger.debug(EELFLoggerDelegate.debugLogger,
					"createAppFromOnboarding: onboarding thumbnail is empty; db thumbnail is NOT null");
			app.setImageUrl(null);
			app.setThumbnail(null);
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"createAppFromOnboarding: making no changes to thumbnail as imageLink is not null");
		}
		return app;
	}

	protected String constructImageName(OnboardingApp onboardingApp) {
		return "portal_" + String.valueOf(onboardingApp.getLandingPage().hashCode() + "_" + (int) (Math.random() * 100000.0))
				+ ".png";
	}

	// Don't encrypt or decrypt the password if it is null or the empty string
	private String decryptedPassword(String encryptedAppPwd, EPApp app) {
		String result = "";
		if (encryptedAppPwd != null && !encryptedAppPwd.isEmpty()) {
			try {
				result = CipherUtil.decryptPKC(encryptedAppPwd,
						KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed for app " + app.getName(), e);
			}
		}
		return result;
	}

	protected String encryptedPassword(String decryptedAppPwd, EPApp app) {
		String result = "";
		if (decryptedAppPwd != null && !decryptedAppPwd.isEmpty()) {
			try {
				result = CipherUtil.encryptPKC(decryptedAppPwd,
						KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword failed for app " + app.getName(), e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator saveWidgetsSortManual(List<EPWidgetsSortPreference> widgetsSortManual, EPUser user) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		final Map<String, Long> params = new HashMap<>();
		List<EPWidgetsManualSortPreference> epManualWidgets = new ArrayList<EPWidgetsManualSortPreference>();

		try {
			params.put("userId", user.getId());
			epManualWidgets = dataAccessService.executeNamedQuery("userWidgetManualSortPrfQuery", params, null);
			Map<Long, EPWidgetsManualSortPreference> existingWidgetsIds = new HashMap<Long, EPWidgetsManualSortPreference>();
			for (EPWidgetsManualSortPreference userWidgetManualPref : epManualWidgets) {
				existingWidgetsIds.put(userWidgetManualPref.getWidgetId(), userWidgetManualPref);
			}
			for (EPWidgetsSortPreference epWidgetsManPref : widgetsSortManual) {
				if (epWidgetsManPref.getWidgetid() != null) {
					Long widgetid = epWidgetsManPref.getWidgetid();
					if (existingWidgetsIds.containsKey(widgetid)) {
						EPWidgetsManualSortPreference epWidgetsManualSort = existingWidgetsIds.get(widgetid);
						epWidgetsManualSort.setWidgetRow(epWidgetsManPref.getRow());
						epWidgetsManualSort.setWidgetCol(epWidgetsManPref.getCol());
						epWidgetsManualSort.setWidgetWidth(epWidgetsManPref.getSizeX());
						epWidgetsManualSort.setWidgetHeight(epWidgetsManPref.getSizeY());
						HashMap<String, Long> additionalUpdateParam = new HashMap<>();
						additionalUpdateParam.put("userId", epWidgetsManualSort.getUserId());
						dataAccessService.saveDomainObject(epWidgetsManualSort, additionalUpdateParam);
					} else {
						EPWidgetsManualSortPreference epWidgetsManualSort = new EPWidgetsManualSortPreference();
						epWidgetsManualSort.setWidgetId(epWidgetsManPref.getWidgetid());
						epWidgetsManualSort.setWidgetRow(epWidgetsManPref.getRow());
						epWidgetsManualSort.setWidgetCol(epWidgetsManPref.getCol());
						epWidgetsManualSort.setWidgetWidth(epWidgetsManPref.getSizeX());
						epWidgetsManualSort.setWidgetHeight(epWidgetsManPref.getSizeY());
						epWidgetsManualSort.setUserId(user.getId());
						dataAccessService.saveDomainObject(epWidgetsManualSort, null);
					}
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveWidgetsSortManual failed", e);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator deleteUserWidgetSortPref(List<EPWidgetsSortPreference> delWidgetSortPref, EPUser user) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		final Map<String, Long> params = new HashMap<>();
		List<EPWidgetsManualSortPreference> epWidgets = new ArrayList<EPWidgetsManualSortPreference>();
		try {
			params.put("userId", user.getId());
			epWidgets = dataAccessService.executeNamedQuery("userWidgetManualSortPrfQuery", params, null);
			Map<Long, EPWidgetsManualSortPreference> existingWidgetIds = new HashMap<Long, EPWidgetsManualSortPreference>();
			for (EPWidgetsManualSortPreference userWidgetSortPref : epWidgets) {
				existingWidgetIds.put(userWidgetSortPref.getWidgetId(), userWidgetSortPref);
			}
			for (EPWidgetsSortPreference delEpWidgetsManPref : delWidgetSortPref) {
				if (delEpWidgetsManPref.getWidgetid() != null) {
					Long widgetId = delEpWidgetsManPref.getWidgetid();
					if (existingWidgetIds.containsKey(widgetId)) {
						params.put("widgetId",widgetId);
						dataAccessService.executeNamedQuery("deleteUserWidgetPlacement", params, null);
					}
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteUserWidgetSortPref failed", e);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	/*
	 * This Method Stores the Sort Order of User Apps by Sort Manual Preference
	 *
	 * @param: appsSortManual--contains User Apps Data
	 *
	 * @param: user--contains LoggedIn User Data
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator saveAppsSortManual(List<EPAppsManualPreference> appsSortManual, EPUser user) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsManualSortPreference> epManualApps = new ArrayList<EPUserAppsManualSortPreference>();

		try {
			params.put("userId", user.getId());
			epManualApps = dataAccessService.executeNamedQuery("userAppsManualSortPrfQuery", params, null);
			Map<Long, EPUserAppsManualSortPreference> existingAppIds = new HashMap<Long, EPUserAppsManualSortPreference>();
			for (EPUserAppsManualSortPreference userAppManualPref : epManualApps) {
				existingAppIds.put(userAppManualPref.getAppId(), userAppManualPref);
			}
			for (EPAppsManualPreference epAppsManPref : appsSortManual) {
				if (epAppsManPref.getAppid() != null) {
					Long appid = epAppsManPref.getAppid();
					if (existingAppIds.containsKey(appid)) {
						EPUserAppsManualSortPreference epAppsManualSort = existingAppIds.get(appid);
						epAppsManualSort
								.setAppManualSortOrder((epAppsManPref.getCol() + (6 * epAppsManPref.getRow())) + 1);
						HashMap<String, Long> additionalUpdateParam = new HashMap<>();
						additionalUpdateParam.put("userId", epAppsManualSort.getUserId());
						dataAccessService.saveDomainObject(epAppsManualSort, additionalUpdateParam);
					} else {
						EPUserAppsManualSortPreference epAppsManualSort = new EPUserAppsManualSortPreference();
						epAppsManualSort.setAppId(epAppsManPref.getAppid());
						epAppsManualSort
								.setAppManualSortOrder((epAppsManPref.getCol() + (6 * epAppsManPref.getRow())) + 1);
						epAppsManualSort.setUserId(user.getId());
						dataAccessService.saveDomainObject(epAppsManualSort, null);
					}
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveAppsSortManual failed", e);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.onap.portalapp.portal.service.EPAppService#
	 * deleteUserAppSortManual(java.lang.String,
	 * org.onap.portalapp.portal.domain.EPUser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator deleteUserAppSortManual(EPDeleteAppsManualSortPref delAppSortManual, EPUser user) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsManualSortPreference> epManualApps = new ArrayList<EPUserAppsManualSortPreference>();
		try {
			params.put("userId", user.getId());
			epManualApps = dataAccessService.executeNamedQuery("userAppsManualSortPrfQuery", params, null);
			Map<Long, EPUserAppsManualSortPreference> existingAppIds = new HashMap<Long, EPUserAppsManualSortPreference>();
			for (EPUserAppsManualSortPreference userAppPref : epManualApps) {
				existingAppIds.put(userAppPref.getAppId(), userAppPref);
			}
			if (existingAppIds.containsKey(delAppSortManual.getAppId()) && !delAppSortManual.isSelect()) {
				dataAccessService.deleteDomainObjects(EPUserAppsManualSortPreference.class,
						"app_id=" + delAppSortManual.getAppId() + " AND user_id=" + user.getId(), null);
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteUserAppSortManual failed", e);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FieldsValidator saveAppsSortPreference(EPAppsSortPreference appsSortPreference, EPUser user) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsSortPreference> epSortTypes = new ArrayList<EPUserAppsSortPreference>();
		EPUserAppsSortPreference usrSortPr = null;
		try {
			params.put("userId", user.getId());
			epSortTypes = dataAccessService.executeNamedQuery("userAppsSortPreferenceQuery", params, null);
			if (epSortTypes.size() == 0) {
				usrSortPr = new EPUserAppsSortPreference();
				usrSortPr.setUserId((int)(long)(user.getId()));
				usrSortPr.setSortPref(appsSortPreference.getValue());
				dataAccessService.saveDomainObject(usrSortPr, null);
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
			} else {
				usrSortPr = epSortTypes.get(0);
				usrSortPr.setSortPref(appsSortPreference.getValue());
				HashMap<String, Integer> additionalUpdateParam = new HashMap<String, Integer>();
				additionalUpdateParam.put("userId", usrSortPr.getUserId());
				dataAccessService.saveDomainObject(usrSortPr, additionalUpdateParam);
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveAppsSortPreference failed", e);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getUserAppsSortTypePreference(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsSortPreference> userSortPrefs = new ArrayList<EPUserAppsSortPreference>();
		try {
			params.put("userId", user.getId());
			userSortPrefs = dataAccessService.executeNamedQuery("userAppsSortPreferenceQuery", params, null);
			if (userSortPrefs.size() > 0)
				return userSortPrefs.get(0).getSortPref();
			else
				return null;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserAppsSortTypePreference failed", e);
		}
		return null;

	}

	@Override
	public List<EPApp> getUserRemoteApps(String id) {
		throw new RuntimeException(" Cannot be called from parent class");
	}
	
	@Override
	public UserRoles getUserProfileForLeftMenu(String loginId) {
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", loginId);
		@SuppressWarnings("unchecked")
		List<UserRole> userRoleList = dataAccessService.executeNamedQuery( "getUserRolesForLeftMenu", params, null);
		ArrayList<UserRoles> usersRolesList = aggregateUserProfileRowsResultsByRole(userRoleList);
		if (usersRolesList == null || usersRolesList.size() < 1)
			return null;

		return usersRolesList.get(0);
	}
	
	
	@Override
	public UserRoles getUserProfileForRolesLeftMenu(String loginId) {
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", loginId);
		@SuppressWarnings("unchecked")
		List<UserRole> userRoleList = dataAccessService.executeNamedQuery( "getRolesForLeftMenu", params, null);
		ArrayList<UserRoles> usersRolesList = aggregateUserProfileRowsResultsByRole(userRoleList);
		if (usersRolesList == null || usersRolesList.size() < 1)
			return null;

		return usersRolesList.get(0);
	}
	
	@Override
	public UserRoles getUserProfileNormalizedForLeftMenu(EPUser user) {
		// Check database.
		UserRoles userAndRoles = getUserProfileForLeftMenu(user.getLoginId());
		// If no roles are defined, treat this user as a guest.
		if (user.isGuest() || userAndRoles == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserProfileForLeftMenu: treating user {} as guest",
					user.getLoginId());
			userAndRoles = createUserRoles(user);
		}

		return userAndRoles;
	}
	
	@Override
	public UserRoles getUserProfileNormalizedForRolesLeftMenu(EPUser user) {
		// Check database.
		UserRoles userAndRoles = getUserProfileForRolesLeftMenu(user.getLoginId());
		// If no roles are defined, treat this user as a guest.
		if (user.isGuest() || userAndRoles == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserProfileForLeftMenu: treating user {} as guest",
					user.getLoginId());
			userAndRoles = createUserRoles(user);
		}

		return userAndRoles;
	}

	
	public UserRoles createUserRoles(EPUser user)
	{
		UserRole userRole = new UserRole();
		userRole.setUser_Id(user.getId());
		userRole.setOrgUserId(user.getLoginId());
		userRole.setFirstName(user.getFirstName());
		userRole.setLastName(user.getLastName());
		userRole.setRoleId(-1L);
		userRole.setRoleName("Guest");
		userRole.setUser_Id(-1L);
		UserRoles userAndRoles = new UserRoles(userRole);
		return userAndRoles;
		
	}

	@SuppressWarnings("unused")
	@Override
	public ResponseEntity<String> checkIfNameSpaceIsValid(String namespace) throws Exception {
		HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists: Connecting to External Auth system for : "+namespace);
		ResponseEntity<String> response = null;
		try {
			
			String namespaceUrl = SystemProperties.
					getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "nss/" + namespace;
			
			response = template.exchange(namespaceUrl, HttpMethod.GET, entity, String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists for"+ namespace ,
					response.getStatusCode().value());
			if (response.getStatusCode().value() == 200) {
				String res = response.getBody();
				JSONObject jsonObj = new JSONObject(res);
				JSONArray namespaceArray = jsonObj.getJSONArray("ns");
				if(!namespaceArray.getJSONObject(0).has("admin")){
					logger.error(EELFLoggerDelegate.errorLogger,
							"No admins are available for requested namespace:" + namespace);		
					throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
							"Portal Mechid is not an admin of" + namespace);
				}
				
				JSONArray namespaceAdminArray = namespaceArray.getJSONObject(0).getJSONArray("admin");
				ArrayList<String> list = new ArrayList<String>();
				if (namespaceAdminArray != null) {
					int len = namespaceAdminArray.length();
					for (int i = 0; i < len; i++) {
						list.add(namespaceAdminArray.get(i).toString());
					}
				}
				logger.debug(EELFLoggerDelegate.debugLogger, "List of Admins of requested namespace" + list);
				final String userName = SystemProperties
						.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_AUTH_USER_NAME);
				boolean idExists = list.stream().anyMatch(t -> userName.equals(t));
				if (false) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"Portal mechid is not admin of requested namespace:" + namespace);
					throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
							"Portal Mechid is not an admin of" + namespace);
				}
			}
			
		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkIfNameSpaceExists failed", e);
			EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
				throw e;
		}
		return response;

	}
}

