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
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.http.HTTPException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.UserProfileService;
import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.EPAuditService;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.service.SharedContextService;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRole;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRolesList;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;

/**
 * Supports menus at the top of the Portal app landing page.
 */
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class FunctionalMenuController extends EPRestrictedBaseController {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FunctionalMenuController.class);

	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	FunctionalMenuService functionalMenuService;
	@Autowired
	SharedContextService sharedContextService;
	@Autowired
	UserProfileService service;
	@Autowired
	SearchService searchService;
	@Autowired
	EPAuditService epAuditService;

	/**
	 * RESTful service method to fetch all the FunctionalMenuItems.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = { "/portalApi/functionalMenu" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItems(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItems();
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenu", "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenu. Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all the FunctionalMenuItems, both active
	 * and inactive, for the EditFunctionalMenu feature. Can only be accessed by
	 * the portal admin.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuForEditing" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForEditing(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try {
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getFunctionalMenuItemDetails");
			} else {
				menuItems = functionalMenuService.getFunctionalMenuItems(true);
			}
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForEditing", "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenuForEditing. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}
	
	/**
	 * RESTful service method to fetch all the FunctionalMenuItems,  active
	 *, for the Functional menu in notification Tree feature.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuForNotificationTree" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForNotifications(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForNotificationTree(true);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForNotificationTree", "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenuForNotifications. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with
	 * an application.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuForApp/{appId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForApp(HttpServletRequest request, @PathVariable("appId") Integer appId)
			throws HTTPException {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForApp(appId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForApp/" + appId, "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenuForApp. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with
	 * the applications and roles that a user has access to.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuForUser/{orgUserId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForUser(HttpServletRequest request,
			@PathVariable("orgUserId") String orgUserId) throws HTTPException {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForUser(orgUserId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForUser/" + orgUserId, "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenuForUser. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with
	 * the applications and roles that the authenticated user has access to.
	 * 
	 * @return List of FunctionalMenuItem objects
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuForAuthUser" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForAuthUser(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getMenuItemsForAuthUser");
			} else if (adminRolesService.isSuperAdmin(user)) {
				menuItems = functionalMenuService.getFunctionalMenuItems();
			} else {
				// calculate the menu items
				String orgUserId = user.getOrgUserId();
				menuItems = functionalMenuService.getFunctionalMenuItemsForUser(orgUserId);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForUser/" + orgUserId, "result =",
						menuItems);
			}
			functionalMenuService.assignHelpURLs(menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling getMenuItemsForAuthUser. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch the details for a functional menu item.
	 * Requirement: you must be the Ecomp portal super admin user.
	 * 
	 * @return FunctionalMenuItem object
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuItemDetails/{menuId}" }, method = RequestMethod.GET, produces = "application/json")
	public FunctionalMenuItem getFunctionalMenuItemDetails(HttpServletRequest request,
			@PathVariable("menuId") Integer menuId, HttpServletResponse response) throws HTTPException {
		// TODO: return FunctionalMenuItemJson
		// TODO: modify FunctionalMenuItem to not include the transient fields
		FunctionalMenuItem menuItem = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getFunctionalMenuItemDetails");
			} else {
				menuItem = functionalMenuService.getFunctionalMenuItemDetails(menuId);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuItemDetails/" + menuId, "result =",
						menuItem);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while calling functionalMenuItemDetails. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		return menuItem;
	}

	/**
	 * RESTful service method to create a new menu item.
	 * 
	 * Requirement: you must be the Ecomp portal super admin user.
	 */
	@RequestMapping(value = { "/portalApi/functionalMenuItem" }, method = RequestMethod.POST)
	public FieldsValidator createFunctionalMenuItem(HttpServletRequest request,
			@RequestBody FunctionalMenuItemWithRoles menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"FunctionalMenuController.createFunctionalMenuItem bad permissions");
			EcompPortalUtils.setBadPermissions(user, response, "createFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.createFunctionalMenuItem(menuItemJson);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuItem", "POST result =",
					response.getStatus());
		}

		return fieldsValidator;
	}

	/**
	 * RESTful service method to update an existing menu item
	 * 
	 * Requirement: you must be the Ecomp portal super admin user.
	 * 
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/functionalMenuItem" }, method = RequestMethod.PUT)
	public FieldsValidator editFunctionalMenuItem(HttpServletRequest request,
			@RequestBody FunctionalMenuItemWithRoles menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "editFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.editFunctionalMenuItem(menuItemJson);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuItem", "PUT result =",
					response.getStatus());
		}

		return fieldsValidator;
	}

	/**
	 * RESTful service method to delete a menu item
	 * 
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/functionalMenuItem/{menuId}" }, method = RequestMethod.DELETE)
	public FieldsValidator deleteFunctionalMenuItem(HttpServletRequest request, @PathVariable("menuId") Long menuId,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "deleteFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.deleteFunctionalMenuItem(menuId);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuItem", "DELETE result =",
					response.getStatus());
		}

		return fieldsValidator;
	}

	/**
	 * RESTful service to regenerate table
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/regenerateFunctionalMenuAncestors" }, method = RequestMethod.GET)
	public FieldsValidator regenerateAncestorTable(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "deleteFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.regenerateAncestorTable();
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/regenerateAncestorTable", "GET result =",
					response.getStatus());
		}

		return fieldsValidator;
	}

	/**
	 * RESful service to set a favorite item.
	 * 
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/setFavoriteItem" }, method = RequestMethod.POST)
	public FieldsValidator addFavoriteItem(HttpServletRequest request,
			@RequestBody FavoritesFunctionalMenuItem menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		menuItemJson.userId = user.getId();
		fieldsValidator = functionalMenuService.setFavoriteItem(menuItemJson);
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/setFavoriteItem", "Post result =", response.getStatus());

		return fieldsValidator;
	}

	/**
	 * RESTful service to get favorites for the current user as identified in
	 * the session
	 * 
	 * @return List of FavoritesFunctionalMenuItemJson
	 */
	@RequestMapping(value = {
			"/portalApi/getFavoriteItems" }, method = RequestMethod.GET, produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<FavoritesFunctionalMenuItemJson> favorites = functionalMenuService.getFavoriteItems(user.getId());
		FieldsValidator fieldsValidator = new FieldsValidator();
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/getFavoriteItems", "GET result =", response.getStatus());
		return favorites;
	}

	/**
	 * RESTful service to delete a favorite menu item for the current user as
	 * identified in the session.
	 * 
	 * @return FieldsValidator
	 */
	@RequestMapping(value = { "/portalApi/removeFavoriteItem/{menuId}" }, method = RequestMethod.DELETE)
	public FieldsValidator deleteFavoriteItem(HttpServletRequest request, @PathVariable("menuId") Long menuId,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		Long userId = user.getId();
		fieldsValidator = functionalMenuService.removeFavoriteItem(userId, menuId);
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject(logger, "/deleteFavoriteItem", "DELETE result =", response.getStatus());

		return fieldsValidator;
	}

	/**
	 * RESTful service to get user information: user's first and last names, ATT
	 * UID, email and last-login. (Actually has nothing to do with the real
	 * functional menu.) First attempts to get the information from the Tomcat
	 * session (i.e., the CSP cookie); if that fails, calls the shared context
	 * service to read the information from the database. Gives back what it
	 * found, any of which may be null, as a JSON collection.
	 * 
	 * @return JSON collection of key-value pairs shown below.
	 */
	@RequestMapping(value = {
			"/portalApi/functionalMenuStaticInfo" }, method = RequestMethod.GET, produces = "application/json")
	public String getFunctionalMenuStaticInfo(HttpServletRequest request, HttpServletResponse response) {

		// Get user details from session
		logger.debug(EELFLoggerDelegate.debugLogger, "getFunctionalMenuStaticInfo: getting user info");
		String fnMenuStaticResponse = null;
		try {			
			String orgUserIdStr = null, firstNameStr = null, lastNameStr = null, emailStr = null, lastLogin = null;
			EPUser user = EPUserUtils.getUserSession(request);
			firstNameStr = user.getFirstName();
			lastNameStr = user.getLastName();
			orgUserIdStr = user.getOrgUserId();
			emailStr = user.getEmail();
			if (emailStr == null || emailStr.equals("")) {
				EPUser userResult = searchService.searchUserByUserId(orgUserIdStr);
				emailStr = userResult.getEmail();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ssZ");
			Date lastLoginDate = user.getLastLoginDate();
			if (lastLoginDate == null) {
				// should never happen
				logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenuStaticInfo: no last login in session");
				lastLogin = "no last login available";
			}
			else {
				lastLogin = sdf.format(lastLoginDate);
			}
			
			// If any item is missing from session, try the Shared Context
			// service.
			SharedContext orgUserIdSC = null, firstNameSC = null, lastNameSC = null, emailSC = null;
			String sessionId = request.getSession().getId();
			if (firstNameStr == null)
				firstNameSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_FIRST_NAME);
			if (lastNameStr == null)
				lastNameSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_LAST_NAME);
			if (emailStr == null)
				emailSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_EMAIL);
			if (orgUserIdStr == null)
				orgUserIdSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_ORG_USERID);

			// Build the response
			Map<String, String> map = new HashMap<String, String>();
			map.put("firstName",
					firstNameStr != null ? firstNameStr : (firstNameSC != null ? firstNameSC.getCvalue() : null));
			map.put("lastName",
					lastNameStr != null ? lastNameStr : (lastNameSC != null ? lastNameSC.getCvalue() : null));
			map.put("email", emailStr != null ? emailStr : (emailSC != null ? emailSC.getCvalue() : null));
			map.put("userId", orgUserIdStr != null ? orgUserIdStr : (orgUserIdSC != null ? orgUserIdSC.getCvalue() : null));
			map.put("last_login", lastLogin);
			JSONObject j = new JSONObject(map);
			fnMenuStaticResponse = j.toString();
			// Be chatty in the log
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuStaticInfo", "GET result =",
					fnMenuStaticResponse);
		} catch (Exception e) {
			// Should never happen.
			logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenuStaticInfo failed", e);
			// Return a real error?
			// fnMenuStaticResponse = "{ \"status\": \"error\", \"message\": \""
			// + e.toString() + "\" }";
			// But the angular controller expects null on error.
		}
		return fnMenuStaticResponse;
	}
	
	private Comparator<BusinessCardApplicationRole> getUserAppRolesComparator = new Comparator<BusinessCardApplicationRole>() {
		public int compare(BusinessCardApplicationRole o1, BusinessCardApplicationRole o2) {
			return o1.getAppName().compareTo(o2.getAppName());
		}
	};

	@RequestMapping(value = {
			"/portalApi/userApplicationRoles" }, method = RequestMethod.GET, produces = "application/json")
	public List<BusinessCardApplicationRolesList> getAppList(HttpServletRequest request,@RequestParam("userId") String userId) throws IOException {

		
		List<BusinessCardApplicationRolesList> AppRoles = null;
		try {
			List<BusinessCardApplicationRole> userAppRoleList = functionalMenuService.getUserAppRolesList(userId);
			
			Collections.sort(userAppRoleList, getUserAppRolesComparator);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userApplicationRoles", "result =", userAppRoleList);
			
			 AppRoles = new ArrayList<BusinessCardApplicationRolesList>();	   
			for(BusinessCardApplicationRole userAppRole: userAppRoleList)
			{    
				  boolean found = false;
			      List<String> roles = null;
			     
				for(BusinessCardApplicationRolesList app :AppRoles)
				{	
					if(app.getAppName().equals(userAppRole.getAppName()))
					{
						roles= app.getRoleNames();
						roles.add(userAppRole.getRoleName());
						app.setRoleNames(roles);
						found = true;	
						break;	
					}
				}
				
				if(!found)
				{
				roles = new ArrayList<String>();	
				roles.add(userAppRole.getRoleName());
				AppRoles.add(new BusinessCardApplicationRolesList(userAppRole.getAppName(), roles));
				}	
				
				Collections.sort(roles);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(EELFLoggerDelegate.errorLogger, "getAppList failed", e);
		}

	return  AppRoles;

	}
}
