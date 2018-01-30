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
package org.onap.portalapp.portal.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.AdminUserApplications;
import org.onap.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.onap.portalapp.portal.domain.AppsResponse;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.domain.UserRoles;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.EPLeftMenuService;
import org.onap.portalapp.portal.transport.EPAppsManualPreference;
import org.onap.portalapp.portal.transport.EPAppsSortPreference;
import org.onap.portalapp.portal.transport.EPDeleteAppsManualSortPref;
import org.onap.portalapp.portal.transport.EPWidgetsSortPreference;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAspectJAutoProxy
@EPAuditLog
public class AppsController extends EPRestrictedBaseController {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsController.class);

	@Autowired
	private AdminRolesService adminRolesService;

	@Autowired
	private EPAppService appService;

	@Autowired
	private EPLeftMenuService leftMenuService;

	/**
	 * RESTful service method to fetch all Applications available to current
	 * user
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List<EcompApp>
	 */
	@RequestMapping(value = { "/portalApi/userApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<EcompApp> getUserApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EcompApp> ecompApps = null;

		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				ecompApps = appService.transformAppsToEcompApps(appService.getUserApps(user));
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userApps", "GET result =", ecompApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserApps failed", e);
		}

		return ecompApps;
	}

	/**
	 * RESTful service method to fetch all applications accessible to the
	 * current user, with personalizations.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List<EcompApp>
	 * @throws IOException
	 *             if sendError fails
	 */
	@RequestMapping(value = { "/portalApi/persUserApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<EcompApp> getPersUserApps(HttpServletRequest request, HttpServletResponse response) throws IOException {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EcompApp> ecompApps = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getPersUserApps");
			} else {
				List<EPApp> apps = null;
				if (adminRolesService.isSuperAdmin(user))
					apps = appService.getPersAdminApps(user);
				else
					apps = appService.getPersUserApps(user);
				ecompApps = appService.transformAppsToEcompApps(apps);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userPersApps", "GET result =", ecompApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getPersUserApps failed", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		return ecompApps;
	}

	/**
	 * RESTful service method to fetch applications for which the current user
	 * is an Administrator
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List<AppIdAndNameTransportModel>
	 */
	@RequestMapping(value = { "/portalApi/adminApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<AppIdAndNameTransportModel> getAdminApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AppIdAndNameTransportModel> adminApps = null;

		try {
			if (!adminRolesService.isAccountAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getAdminApps");
			} else {
				adminApps = appService.getAdminApps(user);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/adminApps", "GET result =", adminApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAdminApps failed", e);
		}

		return adminApps;
	}

	/**
	 * RESTful service method to fetch Applications for user who is super admin
	 * and/or app admin.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List<AppIdAndNameTransportModel>
	 */
	@RequestMapping(value = {
			"/portalApi/appsForSuperAdminAndAccountAdmin" }, method = RequestMethod.GET, produces = "application/json")
	public List<AppIdAndNameTransportModel> getAppsForSuperAdminAndAccountAdmin(HttpServletRequest request,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AppIdAndNameTransportModel> adminApps = null;

		try {
			if (!adminRolesService.isSuperAdmin(user) && !adminRolesService.isAccountAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getAdminApps");
			} else {
				adminApps = appService.getAppsForSuperAdminAndAccountAdmin(user);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/appsForSuperAdminAndAccountAdmin",
						"GET result =", adminApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsForSuperAdminAndAccountAdmin failed", e);
		}

		return adminApps;
	}

	/**
	 * RESTful service method to fetch left menu items from the user's session.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return JSON with left menu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "/portalApi/leftmenuItems" }, method = RequestMethod.GET, produces = "application/json")
	public String getLeftMenuItems(HttpServletRequest request, HttpServletResponse response) {
		String menuList = null;
		Set menuSet = (Set) AppUtils.getSession(request)
				.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME));

		Set roleFunctionSet = (Set) AppUtils.getSession(request)
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));

		EPUser user = EPUserUtils.getUserSession(request);

		try {
			menuList = leftMenuService.getLeftMenuItems(user, menuSet, roleFunctionSet);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/leftmenuItems", "GET result =", menuList);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getLeftMenuItems failed", e);
		}
		return menuList;
	}

	@RequestMapping(value = {
			"/portalApi/userAppsOrderBySortPref" }, method = RequestMethod.GET, produces = "application/json")
	public List<EcompApp> getUserAppsOrderBySortPref(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EcompApp> ecompApps = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserAppsOrderBySortPref");
			} else {
				String usrSortPref = request.getParameter("mparams");
				if (usrSortPref.equals("")) {
					usrSortPref = "N";
				}
				switch (usrSortPref) {
				case "N":
					ecompApps = appService.transformAppsToEcompApps(appService.getAppsOrderByName(user));
					EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppsOrderBySortPref", "GET result =",
							ecompApps);
					break;
				case "L":
					ecompApps = appService.transformAppsToEcompApps(appService.getAppsOrderByLastUsed(user));
					EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppsOrderBySortPref", "GET result =",
							ecompApps);
					break;
				case "F":
					ecompApps = appService.transformAppsToEcompApps(appService.getAppsOrderByMostUsed(user));
					EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppsOrderBySortPref", "GET result =",
							ecompApps);
					break;
				case "M":
					ecompApps = appService.transformAppsToEcompApps(appService.getAppsOrderByManual(user));
					EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppsOrderBySortPref", "GET result =",
							ecompApps);
					break;
				default:
					logger.error(EELFLoggerDelegate.errorLogger,
							"getUserAppsOrderBySortPref failed: no match for " + usrSortPref);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserAppsOrderBySortPref failed", e);
		}
		return ecompApps;
	}

	/**
	 * Sets the user apps manual sort preference
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param epAppsManualPref
	 *            sort pref
	 * @return FieldsValidator
	 */
	@RequestMapping(value = {
			"/portalApi/saveUserAppsSortingManual" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putUserAppsSortingManual(HttpServletRequest request,
			@RequestBody List<EPAppsManualPreference> epAppsManualPref, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			fieldsValidator = appService.saveAppsSortManual(epAppsManualPref, user);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserAppsSortingManual failed", e);
		}
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/saveUserAppsSortingManual", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = {
			"/portalApi/saveUserWidgetsSortManual" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putUserWidgetsSortManual(HttpServletRequest request,
			@RequestBody List<EPWidgetsSortPreference> saveManualWidgetSData, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			fieldsValidator = appService.saveWidgetsSortManual(saveManualWidgetSData, user);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserWidgetsSortManual failed", e);
		}
		// return fieldsValidator;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/putUserWidgetsSortManual", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = {
			"/portalApi/updateWidgetsSortPref" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putUserWidgetsSortPref(HttpServletRequest request,
			@RequestBody List<EPWidgetsSortPreference> delManualWidgetData, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			fieldsValidator = appService.deleteUserWidgetSortPref(delManualWidgetData, user);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserWidgetsSortPref failed", e);

		}
		// return fieldsValidator;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/putUserWidgetsSortPref", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	/**
	 * Deletes the user app manual sort preference record
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param delManualAppData
	 *            data to delete
	 * @return FieldsValidator
	 */
	@RequestMapping(value = {
			"/portalApi/UpdateUserAppsSortManual" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator deleteUserAppSortManual(HttpServletRequest request,
			@RequestBody EPDeleteAppsManualSortPref delManualAppData, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			fieldsValidator = appService.deleteUserAppSortManual(delManualAppData, user);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteUserAppSortManual failed", e);

		}
		// return fieldsValidator;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/deleteUserAppSortManual", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = {
			"/portalApi/saveUserAppsSortingPreference" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putUserAppsSortingPreference(HttpServletRequest request,
			@RequestBody EPAppsSortPreference userAppsValue, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			fieldsValidator = appService.saveAppsSortPreference(userAppsValue, user);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserAppsSortingPreference failed", e);

		}

		// return fieldsValidator;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/putUserAppsSortingPreference", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = {
			"/portalApi/userAppsSortTypePreference" }, method = RequestMethod.GET, produces = "application/String")
	public String getUserAppsSortTypePreference(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		String userSortPreference = null;

		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "userAppsSortTypePreference");
			} else {
				userSortPreference = appService.getUserAppsSortTypePreference(user);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppsSortTypePreference", "GET result =",
						userSortPreference);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserAppsSortTypePreference failed", e);
		}

		return userSortPreference;
	}

	/**
	 * RESTful service method to fetch Application Administrators to Super
	 * Administrator user. Attention: Users which have Super Administrator roles
	 * only are not included!
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @return List<AdminUserApplications>
	 */
	@RequestMapping(value = { "/portalApi/accountAdmins" }, method = RequestMethod.GET, produces = "application/json")
	public List<AdminUserApplications> getAppsAdministrators(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AdminUserApplications> admins = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getAppsAdministrators");
			} else {
				admins = appService.getAppsAdmins();
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/accountAdmins", "GET result =", admins);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppsAdministrators failed", e);
		}

		return admins;
	}

	@RequestMapping(value = { "/portalApi/availableApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<AppsResponse> getApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AppsResponse> apps = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getApps");
			} else {
				apps = appService.getAllApps(false);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/availableApps", "GET result =", apps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getApps failed", e);
		}

		return apps;
	}

	/**
	 * Gets all apps, both active and inactive; i.e., all on-boarded apps,
	 * regardless of enabled status.
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @return List of applications
	 */
	@RequestMapping(value = {
			"/portalApi/allAvailableApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<AppsResponse> getAllApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AppsResponse> apps = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getApps");
			} else {
				apps = appService.getAllApps(true);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/availableApps", "GET result =", apps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAllApps failed", e);
		}

		return apps;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @return List of applications
	 */
	@RequestMapping(value = { "/portalApi/appsFullList" }, method = RequestMethod.GET, produces = "application/json")
	public List<EcompApp> getAppsFullList(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EcompApp> ecompApps = null;
		if (user == null) {
			EcompPortalUtils.setBadPermissions(user, response, "getAppsFullList");
		} else {
			ecompApps = appService.getEcompAppAppsFullList();
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/appsFullList", "GET result =", ecompApps);
		}
		return ecompApps;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @return UserRoles
	 */
	@RequestMapping(value = { "/portalApi/userProfile" }, method = RequestMethod.GET, produces = "application/json")
	public UserRoles getUserProfile(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		UserRoles userAndRoles = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserProfile");
			} else {
				userAndRoles = appService.getUserProfileNormalized(user);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserProfile failed", e);
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userProfile", "getUserProfile result =",
				userAndRoles);
		return userAndRoles;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param appId
	 *            application ID
	 * @return List<LocalRole>
	 */
	@RequestMapping(value = { "/portalApi/appRoles/{appId}" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public List<LocalRole> getAppRoles(HttpServletRequest request, @PathVariable("appId") Long appId,
			HttpServletResponse response) {
		List<LocalRole> roleList = null;
		EPUser user = EPUserUtils.getUserSession(request);
		EPApp requestedApp = appService.getApp(appId);
		if (user != null && (adminRolesService.isAccountAdminOfApplication(user, requestedApp)
				|| (adminRolesService.isSuperAdmin(user) && requestedApp.getId() == PortalConstants.PORTAL_APP_ID))) {
			try {
				roleList = appService.getAppRoles(appId);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/appRoles/" + appId, "GET result =",
						roleList);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles failed", e);
			}
		} else {
			EcompPortalUtils.setBadPermissions(user, response, "getAppRoles");
		}
		return roleList;
	}
	
	/**
	 * 
	 * Return single app information with appName as parameter
	 * 
	 * @param request
	 * @param response
	 * @return EPApp object
	 */
	@RequestMapping(value = { "/portalApi/singleAppInfo" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public EPApp getSingleAppInfo(HttpServletRequest request, HttpServletResponse response) {
		EPApp app = null;
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			String appName = request.getParameter("appParam");
			app = appService.getAppDetailByAppName(appName);
			if (user != null && (adminRolesService.isAccountAdminOfApplication(user, app)
					|| (adminRolesService.isSuperAdmin(user) && app.getId() == PortalConstants.PORTAL_APP_ID)))
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/singleAppInfo" + appName, "GET result =", app);
			else{
				app= null;
				EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getSingleAppInfo failed", e);
		}
		return app;
	}

	/**
	 * 
	 * Return single app information with appId as parameter
	 * 
	 * @param request
	 * @param response
	 * @return EPApp object
	 */
	@RequestMapping(value = { "/portalApi/singleAppInfoById" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public EPApp getSingleAppInfoById(HttpServletRequest request, HttpServletResponse response) {
		EPApp app = null;
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			String appId = request.getParameter("appParam");
			app = appService.getApp(Long.valueOf(appId));
			if(!EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
				app.setCentralAuth(false);
			}
			if (user != null && (adminRolesService.isAccountAdminOfApplication(user, app)
					|| (adminRolesService.isSuperAdmin(user) && app.getId() == PortalConstants.PORTAL_APP_ID)))
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/singleAppInfoById" + appId, "GET result =", app);
			else{
				app= null;
				EcompPortalUtils.setBadPermissions(user, response, "createAdmin");
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getSingleAppInfo failed", e);
		}
		return app;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @return List<OnboardingApp>
	 */
	@RequestMapping(value = { "/portalApi/onboardingApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<OnboardingApp> getOnboardingApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<OnboardingApp> onboardingApps = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getOnboardingApps");
			} else {
				onboardingApps = appService.getOnboardingApps();
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/onboardingApps", "GET result =",
						"onboardingApps of size " + onboardingApps.size());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getOnboardingApps failed", e);
		}

		return onboardingApps;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param modifiedOnboardingApp
	 *            app to update
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/onboardingApps" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putOnboardingApp(HttpServletRequest request,
			@RequestBody OnboardingApp modifiedOnboardingApp, HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "putOnboardingApp");
			} else {
				modifiedOnboardingApp.normalize();
				fieldsValidator = appService.modifyOnboardingApp(modifiedOnboardingApp, user);
				response.setStatus(fieldsValidator.httpStatusCode.intValue());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putOnboardingApps failed", e);
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/onboardingApps", "PUT result =",
				response.getStatus());
		return fieldsValidator;
	}

	/**
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param newOnboardingApp
	 *            app to add
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/onboardingApps" }, method = RequestMethod.POST, produces = "application/json")
	public FieldsValidator postOnboardingApp(HttpServletRequest request, @RequestBody OnboardingApp newOnboardingApp,
			HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "postOnboardingApps");
			} else {
				newOnboardingApp.normalize();
				fieldsValidator = appService.addOnboardingApp(newOnboardingApp, user);
				response.setStatus(fieldsValidator.httpStatusCode.intValue());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "postOnboardingApp failed", e);
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/onboardingApps", "POST result =",
				response.getStatus());
		return fieldsValidator;
	}

	/**
	 * REST endpoint to process a request to delete an on-boarded application.
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param appId
	 *            ID of app to delete
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/onboardingApps/{appId}" }, method = {
			RequestMethod.DELETE }, produces = "application/json")
	public FieldsValidator deleteOnboardingApp(HttpServletRequest request, @PathVariable("appId") Long appId,
			HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "deleteOnboardingApps");
			} else {
				fieldsValidator = appService.deleteOnboardingApp(user, appId);
				response.setStatus(fieldsValidator.httpStatusCode.intValue());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteOnboardingApp failed", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/onboardingApps" + appId, "DELETE result =",
				response.getStatus());
		return fieldsValidator;
	}

	/**
	 * Gets the application thumbnail image; sets status 404 if none exists.
	 * 
	 * @param request
	 *            HTTP servlet request
	 * @param response
	 *            HTTP servlet response
	 * @param appId
	 *            Application ID
	 * @return Bytes with the app thumbnail image; null if not available.
	 */
	@RequestMapping(value = { "/portalApi/appThumbnail/{appId}" }, method = { RequestMethod.GET })
	public HttpEntity<byte[]> getAppThumbnail(HttpServletRequest request, @PathVariable("appId") Long appId,
			HttpServletResponse response) {
		EPApp app = appService.getApp(appId);
		if (app == null || app.getImageUrl() == null || app.getThumbnail() == null || app.getThumbnail().length == 0) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"getAppThumbnail: no app and/or no thumbnail for app " + appId);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		String url = app.getImageUrl();
		int indexOfDot = url.lastIndexOf('.');
		String urlSuffix = indexOfDot > 0 ? url.substring(indexOfDot + 1).toLowerCase() : "UNK";
		// Default to JPG if no usable suffix.
		MediaType mediaType = MediaType.IMAGE_JPEG;
		if ("png".equals(urlSuffix))
			mediaType = MediaType.IMAGE_PNG;
		else if ("gif".equals(urlSuffix))
			mediaType = MediaType.IMAGE_GIF;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(mediaType);
		header.setContentLength(app.getThumbnail().length);
		return new HttpEntity<byte[]>(app.getThumbnail(), header);
	}

}
