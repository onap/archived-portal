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
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.json.JSONObject;
import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.AdminUserApplications;
import org.openecomp.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.openecomp.portalapp.portal.domain.AppsResponse;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompApp;
import org.openecomp.portalapp.portal.domain.UserRole;
import org.openecomp.portalapp.portal.domain.UserRoles;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.EPAppService;
import org.openecomp.portalapp.portal.service.PersUserAppService;
import org.openecomp.portalapp.portal.service.UserService;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.LocalRole;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class AppsController extends EPRestrictedBaseController {
	
	static final String FAILURE = "failure";
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsController.class);

	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	EPAppService appService;
	@Autowired
	PersUserAppService persUserAppService;
	@Autowired
	UserService userService;

	/**
	 * RESTful service method to fetch all Applications available to watch for
	 * current user
	 * 
	 * @return
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/userApps", "GET result =", ecompApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getUserApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return ecompApps;
	}
	
	/**
	 * Create new application's contact us details.
	 * 
	 * @param contactUs
	 * @return
	 */
	@RequestMapping(value = "/portalApi/saveNewUser", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> saveNewUser(HttpServletRequest request,@RequestBody EPUser newUser) {
		EPUser user = EPUserUtils.getUserSession(request);
		if (newUser == null)
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, FAILURE,
					"New User cannot be null or empty");
		
		if (!(adminRolesService.isSuperAdmin(user) || adminRolesService.isAccountAdmin(user))){
			if(!user.getLoginId().equalsIgnoreCase(newUser.getLoginId()))
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, FAILURE,
						"UnAuthorized");
		}
			
        String checkDuplicate = request.getParameter("isCheck");
		String saveNewUser = FAILURE;
		try {
			saveNewUser = userService.saveNewUser(newUser,checkDuplicate);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, saveNewUser, e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, saveNewUser, "");
	}

	/**
	 * RESTful service method to fetch all applications accessible to the
	 * current user, with personalizations.
	 * 
	 * @return
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/userPersApps", "GET result =", ecompApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed in getPersUserApps", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		return ecompApps;
	}

	/**
	 * RESTful service method to fetch applications for which the current user
	 * is an Administrator
	 * 
	 * @return
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/adminApps", "GET result =", adminApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getAdminApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return adminApps;
	}

	/**
	 * RESTful service method to fetch Applications in which the logged in user
	 * is an Administrator
	 * 
	 * @return
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/appsForSuperAdminAndAccountAdmin", "GET result =",
						adminApps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getAppsForSuperAdminAndAccountAdmin operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return adminApps;
	}

	/**
	 * RESTful service method to fetch Application Administrators to Super
	 * Administrator user. Attention: Users which have Super Administrator roles
	 * only are not included!
	 * 
	 * @return
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/accountAdmins", "GET result =", admins);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getAppsAdministrators operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/availableApps", "GET result =", apps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return apps;
	}

	/**
	 * Gets all apps, both active and inactive; i.e., all on-boarded apps,
	 * regardless of enabled status.
	 * 
	 * @param request
	 * @param response
	 * @return List of applications
	 */
	// This API returns
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
				EcompPortalUtils.logAndSerializeObject("/portalApi/availableApps", "GET result =", apps);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed in getAllApps", e);
		}

		return apps;
	}

	@RequestMapping(value = { "/portalApi/appsFullList" }, method = RequestMethod.GET, produces = "application/json")
	public List<EcompApp> getAppsFullList(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EcompApp> ecompApps = null;
		if (user == null) {
			EcompPortalUtils.setBadPermissions(user, response, "getAppsFullList");
		} else {
			ecompApps = appService.getEcompAppAppsFullList();
			EcompPortalUtils.logAndSerializeObject("/portalApi/appsFullList", "GET result =", ecompApps);
		}

		return ecompApps;
	}

	@RequestMapping(value = { "/portalApi/userProfile" }, method = RequestMethod.GET, produces = "application/json")
	public UserRoles getUserProfile(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		UserRoles userAndRoles = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserProfile");
			} else {
				// Check database.
				userAndRoles = appService.getUserProfile(user.getLoginId());
				// If no roles are defined, treat this user as a guest.
				if (user.isGuest() || userAndRoles == null) {
					logger.debug(EELFLoggerDelegate.debugLogger, "getUserProfile: treating user {} as guest",
							user.getLoginId());
					UserRole userRole = new UserRole();
					userRole.setOrgUserId(user.getLoginId());
					userRole.setFirstName(user.getFirstName());
					userRole.setLastName(user.getLastName());
					userRole.setRoleId(-1L);
					userRole.setRoleName("Guest");
					userRole.setUser_Id(-1L);
					userAndRoles = new UserRoles(userRole);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to get user names and roles", e);
		}

		EcompPortalUtils.logAndSerializeObject("/portalApi/userProfile", "getUserProfile result =", userAndRoles);
		return userAndRoles;
	}
	
	@RequestMapping(value = { "/portalApi/currentUserProfile/{loginId}" }, method = RequestMethod.GET, produces = "application/json")
	public String getCurrentUserProfile(HttpServletRequest request, @PathVariable("loginId") String loginId) {
		
		Map<String,String> map = new HashMap<String,String>();
		EPUser user = null;
		try {
			 user = (EPUser) userService.getUserByUserId(loginId).get(0);
			 map.put("firstName", user.getFirstName());
		     map.put("lastName", user.getLastName());
		     map.put("email", user.getEmail());
			 map.put("loginId", user.getLoginId());
			 map.put("loginPwd",user.getLoginPwd());
			 map.put("middleInitial",user.getMiddleInitial());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to get user info", e);
		}

		JSONObject j = new JSONObject(map);;
		return j.toString();
	}

	@RequestMapping(value = { "/portalApi/appRoles/{appId}" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public List<LocalRole> getAppRoles(HttpServletRequest request, @PathVariable("appId") Long appId) {
		List<LocalRole> roleList = null;
		try {
			roleList = appService.getAppRoles(appId);
			EcompPortalUtils.logAndSerializeObject("/portalApi/appRoles/" + appId, "GET result =", roleList);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getAppRoles operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return roleList;
	}

	@RequestMapping(value = { "/portalApi/onboardingApps" }, method = RequestMethod.GET, produces = "application/json")
	public List<OnboardingApp> getOnboardingApps(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<OnboardingApp> onboardingApps = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getOnboardingApps");
			} else {
				onboardingApps = appService.getOnboardingApps();
				EcompPortalUtils.logAndSerializeObject("/portalApi/onboardingApps", "GET result =",
						"onboardingApps of size " + onboardingApps.size());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getOnboardingApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return onboardingApps;
	}

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
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing putOnboardingApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		EcompPortalUtils.logAndSerializeObject("/portalApi/onboardingApps", "PUT result =", response.getStatus());
		return fieldsValidator;
	}

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
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing postOnboardingApps operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		EcompPortalUtils.logAndSerializeObject("/portalApi/onboardingApps", "POST result =", response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = { "/portalApi/onboardingApps/{appId}" }, method = {
			RequestMethod.DELETE }, produces = "application/json")
	public FieldsValidator deleteOnboardingApp(HttpServletRequest request, @PathVariable("appId") Long appId,
			HttpServletResponse response) {
		FieldsValidator fieldsValidator = null;
		;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "deleteOnboardingApps");
			} else {
				fieldsValidator = appService.deleteOnboardingApp(user, appId);
				response.setStatus(fieldsValidator.httpStatusCode.intValue());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}

		EcompPortalUtils.logAndSerializeObject("/portalApi/onboardingApps" + appId, "DELETE result =",
				response.getStatus());
		return fieldsValidator;
	}
}