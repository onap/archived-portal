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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.http.HTTPException;
import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.ApplicationsRestClientService;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.service.UserRolesService;
import org.openecomp.portalapp.portal.transport.AppNameIdIsAdmin;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;
import org.openecomp.portalapp.portal.transport.AppsListWithAdminRole;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;
import org.openecomp.portalapp.portal.transport.UserApplicationRoles;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.restful.domain.EcompRole;
import org.openecomp.portalsdk.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserRolesController extends EPRestrictedBaseController {
	
	static final String FAILURE = "failure";

	@Autowired
	SearchService searchService;
	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	UserRolesService userRolesService;
	@Autowired
	ApplicationsRestClientService applicationsRestClientService;
	@Autowired
	RoleService roleService;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRolesController.class);

	/**
	 * RESTful service method to fetch users in the WebPhone external service
	 * 
	 * @return array of found users as json
	 */
	@RequestMapping(value = { "/portalApi/queryUsers" }, method = RequestMethod.GET, produces = "application/json")
	public String getPhoneBookSearchResult(HttpServletRequest request, @RequestParam("search") String searchString, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		String searchResult = null;
		if (!adminRolesService.isSuperAdmin(user) && !adminRolesService.isAccountAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "getPhoneBookSearchResult");
		} else {
			searchString = searchString.trim();
			if (searchString.length() > 0) {
				//searchResult = searchService.searchUsersInPhoneBook(searchString);
				searchResult = searchService.searchUsersInFnTable(searchString);
			} else {
				logger.info(EELFLoggerDelegate.errorLogger, "getPhoneBookSearchResult - too short search string: " + searchString);
			}
		}
		EcompPortalUtils.logAndSerializeObject("/portalApi/queryUsers", "result =", searchResult);
		
		return searchResult;
	}

	/**
	 * RESTful service method to fetch applications where user is admin
	 * 
	 * @return for GET: array of all applications with boolean isAdmin=true/false for each application
	 */
	@RequestMapping(value = { "/portalApi/adminAppsRoles" }, method = { RequestMethod.GET }, produces = "application/json")
	public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(HttpServletRequest request, @RequestParam("orgUserId") String orgUserId,
			HttpServletResponse response) {
		
		EPUser user = EPUserUtils.getUserSession(request);
		AppsListWithAdminRole result = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "getAppsWithAdminRoleStateForUser");
		} else {
			if (EcompPortalUtils.legitimateUserId(orgUserId)) {
				result = adminRolesService.getAppsWithAdminRoleStateForUser(orgUserId);
			} else {
				logger.info(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser - parms error, no orgUserId");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}

		StringBuilder adminAppRoles = new StringBuilder();
		if(result != null && result.appsRoles.size() >= 1) {
			adminAppRoles.append("User '" + result.orgUserId + "' has admin role to the apps = {");
			for(AppNameIdIsAdmin adminAppRole : result.appsRoles) {
				if (adminAppRole.isAdmin) {
					adminAppRoles.append(adminAppRole.appName + ", ");
				}
			}
			adminAppRoles.append("}.");
		} else {
			adminAppRoles.append("User '" + result.orgUserId + "' has no Apps with Admin Role.");
		}
		logger.info(EELFLoggerDelegate.errorLogger, adminAppRoles.toString());
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/adminAppsRoles", "get result =", result);

		return result;
	}

	@RequestMapping(value = { "/portalApi/adminAppsRoles" }, method = { RequestMethod.PUT }, produces = "application/json")
	public FieldsValidator putAppsWithAdminRoleStateForUser(HttpServletRequest request, @RequestBody AppsListWithAdminRole newAppsListWithAdminRoles,
			HttpServletResponse response) {
		
		//newAppsListWithAdminRoles.appsRoles
		FieldsValidator fieldsValidator = new FieldsValidator();
		StringBuilder newAppRoles = new StringBuilder();
		if(newAppsListWithAdminRoles != null && newAppsListWithAdminRoles.appsRoles.size() >= 1) {
			newAppRoles.append("User '" + newAppsListWithAdminRoles.orgUserId + "' has admin role to the apps = {");
			for(AppNameIdIsAdmin adminAppRole : newAppsListWithAdminRoles.appsRoles) {
				if (adminAppRole.isAdmin) {
					newAppRoles.append(adminAppRole.appName + ", ");
				}
			}
			newAppRoles.append("}.");
		} else {
			newAppRoles.append("User '" + newAppsListWithAdminRoles.orgUserId + "' has no Apps with Admin Role.");
		}
		logger.info(EELFLoggerDelegate.errorLogger, newAppRoles.toString());
			
		EPUser user = EPUserUtils.getUserSession(request);
		boolean changesApplied = false;
		
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "putAppsWithAdminRoleStateForUser");
		} else {
			changesApplied = adminRolesService.setAppsWithAdminRoleStateForUser(newAppsListWithAdminRoles);
		}
		EcompPortalUtils.logAndSerializeObject("/portalApi/adminAppsRoles", "put result =", changesApplied);
		
		return fieldsValidator;
	}

	@RequestMapping(value = { "/portalApi/userAppRoles" }, method = { RequestMethod.GET }, produces = "application/json")
	public List<RoleInAppForUser> getAppRolesForUser(HttpServletRequest request, @RequestParam("orgUserId") String orgUserId, @RequestParam("app") Long appid, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<RoleInAppForUser> result = null;
		String feErrorString = "";
		if (!adminRolesService.isAccountAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "getAppRolesForUser");
			feErrorString = EcompPortalUtils.getFEErrorString(true, response.getStatus());
		} else {
			if (EcompPortalUtils.legitimateUserId(orgUserId)) {
				result = userRolesService.getAppRolesForUser(appid, orgUserId);
				int responseCode = EcompPortalUtils.getExternalAppResponseCode();
				if (responseCode != 0 && responseCode != 200) {
					// external error
					response.setStatus(responseCode);
					feErrorString = EcompPortalUtils.getFEErrorString(false, responseCode);
				} else if (result == null) {
					// If the result is null, there was an internal ecomp error in the service call.
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					feErrorString = EcompPortalUtils.getFEErrorString(true, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
				logger.info(EELFLoggerDelegate.errorLogger, "getAppRolesForUser - no userId");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				feErrorString = EcompPortalUtils.getFEErrorString(true, HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		
		StringBuilder sbUserApps = new StringBuilder();
		if (result!=null && result.size()>=1) {
			sbUserApps.append("User '" + orgUserId + "' has Roles={");
			for(RoleInAppForUser appRole : result) {
				if (appRole.isApplied) {
					sbUserApps.append(appRole.roleName + ", ");
				}
			}
			sbUserApps.append("} assigned to the appId '" + appid + "'.");
		} else {
			if (result==null) {
				result = new ArrayList<RoleInAppForUser>();
			}
			sbUserApps.append("User '" + orgUserId + "' and appid " + appid + " has no roles");
		}
		logger.info(EELFLoggerDelegate.errorLogger, sbUserApps.toString());
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/userAppRoles", "get result =", result);
		if (feErrorString != "") {
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: FEErrorString to header: "+feErrorString);

			response.addHeader("FEErrorString", feErrorString);
			response.addHeader("Access-Control-Expose-Headers", "FEErrorString");
		}
		return result;
	}

	@RequestMapping(value = { "/portalApi/userAppRoles" }, method = { RequestMethod.PUT }, produces = "application/json")
	public FieldsValidator putAppWithUserRoleStateForUser(HttpServletRequest request, @RequestBody AppWithRolesForUser newAppRolesForUser,
			HttpServletResponse response) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		StringBuilder sbUserApps = new StringBuilder();
		if (newAppRolesForUser!=null) {
			sbUserApps.append("User '" + newAppRolesForUser.orgUserId);
			if (newAppRolesForUser.appRoles!=null && newAppRolesForUser.appRoles.size()>=1) {
				sbUserApps.append("' has roles = {");
				for(RoleInAppForUser appRole : newAppRolesForUser.appRoles) {
						if (appRole.isApplied) {
							sbUserApps.append(appRole.roleName + ", ");
						}
				}
				sbUserApps.append("} assigned for the app '" + newAppRolesForUser.appName + "'.");
			} else {
				sbUserApps.append("' has no roles assigned for the app '" + newAppRolesForUser.appName + "'.");
			}
		}
		logger.info(EELFLoggerDelegate.errorLogger, sbUserApps.toString());
		
		EPUser user = EPUserUtils.getUserSession(request);
		
		boolean changesApplied = false;
		if (!adminRolesService.isAccountAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "putAppWithUserRoleStateForUser");
		} else {
			changesApplied = userRolesService.setAppWithUserRoleStateForUser(user, newAppRolesForUser);
			if (changesApplied == false) {
				logger.debug(EELFLoggerDelegate.debugLogger, "putAppWithUserRoleStateForUser - Set new User Roles failure");
			}
		}

		EcompPortalUtils.logAndSerializeObject("/portalApi/userAppRoles", "put result =", changesApplied);
				
		return fieldsValidator;
	}
	
	@RequestMapping(value = { "/portalApi/updateRemoteUserProfile" }, method = { RequestMethod.GET }, produces = "application/json")
	public PortalRestResponse<String> updateRemoteUserProfile(HttpServletRequest request,HttpServletResponse response) {
		
		String updateRemoteUserFlag = FAILURE;
		try {
			//saveNewUser = userService.saveNewUser(newUser);
			String orgUserId = request.getParameter("loginId");
			Long appId = Long.parseLong(request.getParameter("appId"));
			userRolesService.updateRemoteUserProfile(orgUserId, appId);
			
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.OK, updateRemoteUserFlag, e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, updateRemoteUserFlag, "");
		
	}

	@RequestMapping(value = { "/portalApi/app/{appId}/users" }, method = { RequestMethod.GET }, produces = "application/json")
	public List<UserApplicationRoles> getUsersFromAppEndpoint(HttpServletRequest request, @PathVariable("appId") Long appId) throws HTTPException {
		try {
			List<UserApplicationRoles> appUsers = userRolesService.getUsersFromAppEndpoint(appId);
			return appUsers;
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing UserRolesController.getUsersFromAppEndpoint. Details: " + EcompPortalUtils.getStackTrace(e));
			return new ArrayList<UserApplicationRoles>();
		}
	}

	@RequestMapping(value = { "/portalApi/app/{appId}/roles" }, method = { RequestMethod.GET }, produces = "application/json")
	public List<EcompRole> testGetRoles(HttpServletRequest request, @PathVariable("appId") Long appId) throws HTTPException {
		EcompRole[] appRoles = applicationsRestClientService.get(EcompRole[].class, appId, "/roles");
		List<EcompRole> rolesList = Arrays.asList(appRoles);
		EcompPortalUtils.logAndSerializeObject("/portalApi/app/{appId}/roles", "response for appId=" + appId, rolesList);
		
		return rolesList;
	}

	@RequestMapping(value = { "/portalApi/admin/import/app/{appId}/roles" }, method = { RequestMethod.GET }, produces = "application/json")
	public List<EPRole> importRolesFromRemoteApplication(HttpServletRequest request, @PathVariable("appId") Long appId) throws HTTPException {
		List<EPRole> rolesList = userRolesService.importRolesFromRemoteApplication(appId);
		EcompPortalUtils.logAndSerializeObject("/portalApi/admin/import/app/{appId}/roles", "response for appId=" + appId, rolesList);
		
		return rolesList;
	}

	@RequestMapping(value = { "/portalApi/app/{appId}/user/{orgUserId}/roles" }, method = { RequestMethod.GET }, produces = "application/json")
	public EcompRole testGetRoles(HttpServletRequest request, @PathVariable("appId") Long appId, @PathVariable("orgUserId") String orgUserId) throws Exception {
		if (!EcompPortalUtils.legitimateUserId(orgUserId)) {
			String msg = "Error /user/<user>/roles not legitimate orgUserId = " + orgUserId;
			logger.error(EELFLoggerDelegate.errorLogger, msg);
			throw new Exception(msg);
		}
		EcompRole[] roles = applicationsRestClientService.get(EcompRole[].class, appId, String.format("/user/%s/roles", orgUserId));
		if (roles.length != 1) {
			String msg = "Error /user/<user>/roles returned array. expected size 1 recieved size = " + roles.length;
			logger.error(EELFLoggerDelegate.errorLogger, msg);
			throw new Exception(msg);
		}
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/app/{appId}/user/{orgUserId}/roles", "response for appId='" + appId + "' and orgUserId='" + orgUserId + "'", roles[0]);
		return roles[0];
	}
	
}
