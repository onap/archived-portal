/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modification Copyright © 2020 IBM.
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
package org.onap.portalapp.portal.controller;

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
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.SharedContext;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalapp.portal.transport.BusinessCardApplicationRole;
import org.onap.portalapp.portal.transport.BusinessCardApplicationRolesList;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Supports menus at the top of the Portal app landing page.
 */
@RestController
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class FunctionalMenuController extends EPRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FunctionalMenuController.class);
	private final DataValidator dataValidator = new DataValidator();

	@Autowired
	private AdminRolesService adminRolesService;
	@Autowired
	private FunctionalMenuService functionalMenuService;
	@Autowired
	private SharedContextService sharedContextService;
	@Autowired
	private SearchService searchService;

	/**
	 * RESTful service method to fetch all the FunctionalMenuItems.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = { "/portalApi/functionalMenu" }, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItems(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItems();
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenu", "result =", menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItems failed", e);
		}
		return menuItems;
	}

	/**
	 * RESTful service method to get ONAP Portal Title.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return PortalRestResponse of ONAP portal title
	 */
	@GetMapping(value = { "/portalApi/ecompTitle" }, produces = "application/json")
	public PortalRestResponse<String> getECOMPTitle(HttpServletRequest request, HttpServletResponse response) {
		PortalRestResponse<String> portalRestResponse = null;
		try {
			String ecompTitle = SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME);
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success", ecompTitle);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/ecompTitle", "result =", ecompTitle);
		} catch (Exception e) {
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), null);
			logger.error(EELFLoggerDelegate.errorLogger, "getEcompTitle failed", e);
		}
		return portalRestResponse;
	}

	/**
	 * RESTful service method to fetch all the FunctionalMenuItems, both active and
	 * inactive, for the EditFunctionalMenu feature. Can only be accessed by the
	 * portal admin.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuForEditing" }, produces = "application/json")
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
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForEditing", "result =",
					menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItemsForEditing failed", e);
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all the FunctionalMenuItems, active , for the
	 * Functional menu in notification Tree feature.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuForNotificationTree" }, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForNotifications(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		// EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForNotificationTree(true);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForNotificationTree", "result =",
					menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItemsForNotifications failed", e);
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with an
	 * application.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param appId
	 *            application ID
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuForApp/{appId}" }, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForApp(HttpServletRequest request,
			@PathVariable("appId") Integer appId) {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForApp(appId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForApp/" + appId, "result =",
					menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItemsForApp failed", e);
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with the
	 * applications and roles that a user has access to.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param orgUserId
	 *            user ID
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuForUser/{orgUserId}" }, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForUser(HttpServletRequest request,
			@PathVariable("orgUserId") String orgUserId) {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try {
			menuItems = functionalMenuService.getFunctionalMenuItemsForUser(orgUserId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForUser/" + orgUserId, "result =",
					menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItemsForUser failed", e);
		}

		return menuItems;
	}

	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with the
	 * applications and roles that the authenticated user has access to.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List of FunctionalMenuItem objects
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuForAuthUser" }, produces = "application/json")
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
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuForUser/" + orgUserId,
						"result =", menuItems);
			}
			functionalMenuService.assignHelpURLs(menuItems);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuItemsForAuthUser failed", e);
		}
		return menuItems;
	}

	/**
	 * RESTful service method to fetch the details for a functional menu item.
	 * Requirement: you must be the ONAP portal super admin user.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuId
	 *            menu ID
	 * @return FunctionalMenuItem object
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuItemDetails/{menuId}" }, produces = "application/json")
	public FunctionalMenuItem getFunctionalMenuItemDetails(HttpServletRequest request,
			@PathVariable("menuId") Long menuId, HttpServletResponse response) {
		// TODO: return FunctionalMenuItemJson
		// TODO: modify FunctionalMenuItem to not include the transient fields
		FunctionalMenuItem menuItem = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getFunctionalMenuItemDetails");
			} else {
				menuItem = functionalMenuService.getFunctionalMenuItemDetails(menuId);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/functionalMenuItemDetails/" + menuId,
						"result =", menuItem);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenuItemDetails failed", e);
		}

		return menuItem;
	}

	/**
	 * RESTful service method to create a new menu item.
	 *
	 * Requirement: you must be the ONAP portal super admin user.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuItemJson
	 *            FunctionalMenuItemWithRoles
	 * @return FieldsValidator
	 */
	@PostMapping(value = { "/portalApi/functionalMenuItem" })
	public FieldsValidator createFunctionalMenuItem(HttpServletRequest request,
			@RequestBody FunctionalMenuItemWithRoles menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

		if(!dataValidator.isValid(menuItemJson)){
			fieldsValidator = new FieldsValidator();
			logger.warn(EELFLoggerDelegate.debugLogger,"FunctionalMenuController.createFunctionalMenuItem not valid object");
			fieldsValidator.httpStatusCode = (long)HttpServletResponse.SC_NOT_ACCEPTABLE;
			return fieldsValidator;
		}

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
	 * Requirement: you must be the ONAP portal super admin user.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuItemJson
	 *            FunctionalMenuItemWithRoles
	 * @return FieldsValidator
	 */
	@PutMapping(value = { "/portalApi/functionalMenuItem" })
	public FieldsValidator editFunctionalMenuItem(HttpServletRequest request,
			@RequestBody FunctionalMenuItemWithRoles menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

           if(!dataValidator.isValid(menuItemJson)){
			   fieldsValidator = new FieldsValidator();
			   logger.warn(EELFLoggerDelegate.debugLogger,"FunctionalMenuController.createFunctionalMenuItem not valid object");
			   fieldsValidator.httpStatusCode = (long)HttpServletResponse.SC_NOT_ACCEPTABLE;
			   return fieldsValidator;
           }

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
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuId
	 *            menu identifier
	 * @return FieldsValidator
	 */
	@DeleteMapping(value = { "/portalApi/functionalMenuItem/{menuId}" })
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
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return FieldsValidator
	 */
	@GetMapping(value = { "/portalApi/regenerateFunctionalMenuAncestors" })
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
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuItemJson
	 *            FunctionalMenuItemWithRoles
	 * @return FieldsValidator
	 */
	@PostMapping(value = { "/portalApi/setFavoriteItem" })
	public FieldsValidator addFavoriteItem(HttpServletRequest request,
			@RequestBody FavoritesFunctionalMenuItem menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		menuItemJson.userId = user.getId();
		fieldsValidator = functionalMenuService.setFavoriteItem(menuItemJson);
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/setFavoriteItem", "Post result =",
				response.getStatus());

		return fieldsValidator;
	}

	/**
	 * RESTful service to get favorites for the current user as identified in the
	 * session
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List of FavoritesFunctionalMenuItemJson
	 */
	@GetMapping(value = {
			"/portalApi/getFavoriteItems" }, produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<FavoritesFunctionalMenuItemJson> favorites = functionalMenuService.getFavoriteItems(user.getId());
		FieldsValidator fieldsValidator = new FieldsValidator();
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/getFavoriteItems", "GET result =",
				response.getStatus());
		return favorites;
	}

	/**
	 * RESTful service to delete a favorite menu item for the current user as
	 * identified in the session.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param menuId
	 *            menu identifier
	 * @return FieldsValidator
	 */
	@DeleteMapping(value = { "/portalApi/removeFavoriteItem/{menuId}" })
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
	 * RESTful service to get user information: user's first and last names, org
	 * user ID, email and last-login. (Actually has nothing to do with the real
	 * functional menu.) First attempts to get the information from the Tomcat
	 * session (i.e., the CSP cookie); if that fails, calls the shared context
	 * service to read the information from the database. Gives back what it found,
	 * any of which may be null, as a JSON collection.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return JSON collection of key-value pairs shown below.
	 */
	@GetMapping(value = {
			"/portalApi/functionalMenuStaticInfo" }, produces = "application/json")
	public String getFunctionalMenuStaticInfo(HttpServletRequest request, HttpServletResponse response) {

		// Get user details from session
		logger.debug(EELFLoggerDelegate.debugLogger, "getFunctionalMenuStaticInfo: getting user info");
		String fnMenuStaticResponse = null;
		try {
			String orgUserIdStr = null;
            String firstNameStr = null;
            String lastNameStr = null;
            String emailStr = null;
            String lastLogin = null;
			EPUser user = EPUserUtils.getUserSession(request);
			firstNameStr = user.getFirstName();
			lastNameStr = user.getLastName();
			orgUserIdStr = user.getOrgUserId();
			emailStr = user.getEmail();
			if (emailStr == null || emailStr.equals("")) {
				EPUser userResult = searchService.searchUserByUserId(orgUserIdStr);
				emailStr = userResult.getEmail();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss Z a");
			Date lastLoginDate = user.getLastLoginDate();
			if (lastLoginDate == null) {
				// should never happen
				logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenuStaticInfo: no last login in session");
				lastLogin = "no last login available";
			} else {
				lastLogin = sdf.format(lastLoginDate);
			}

			// If any item is missing from session, try the Shared Context
			// service.
			SharedContext orgUserIdSC = null;
			SharedContext firstNameSC = null;
			SharedContext lastNameSC = null;
			SharedContext emailSC = null;
			String sessionId = request.getSession().getId();
			if (firstNameStr == null)
				firstNameSC = sharedContextService.getSharedContext(sessionId,
						EPCommonSystemProperties.USER_FIRST_NAME);
			if (lastNameStr == null)
				lastNameSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_LAST_NAME);
			if (emailStr == null)
				emailSC = sharedContextService.getSharedContext(sessionId, EPCommonSystemProperties.USER_EMAIL);
			if (orgUserIdStr == null)
				orgUserIdSC = sharedContextService.getSharedContext(sessionId,
						EPCommonSystemProperties.USER_ORG_USERID);

			// Build the response
			Map<String, String> map = new HashMap<String, String>();
			map.put("firstName",
					firstNameStr != null ? firstNameStr : (firstNameSC != null ? firstNameSC.getCvalue() : null));
			map.put("lastName",
					lastNameStr != null ? lastNameStr : (lastNameSC != null ? lastNameSC.getCvalue() : null));
			map.put("email", emailStr != null ? emailStr : (emailSC != null ? emailSC.getCvalue() : null));
			map.put("userId",
					orgUserIdStr != null ? orgUserIdStr : (orgUserIdSC != null ? orgUserIdSC.getCvalue() : null));
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

	/**
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param userId
	 *            user ID
	 * @return List<BusinessCardApplicationRolesList>
	 * @throws IOException
	 *             on error
	 */
	@GetMapping(value = {
			"/portalApi/userApplicationRoles" }, produces = "application/json")
	public List<BusinessCardApplicationRolesList> getAppList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userId") String userId) throws IOException {

		List<BusinessCardApplicationRolesList> AppRoles = null;
		
		if(!UserUtils.getUserSession(request).getOrgUserId().equalsIgnoreCase(userId)) {
			logger.error(EELFLoggerDelegate.errorLogger, "Not authorized to view roles of others ");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().flush();
			return null;
		}
			
		try {
			List<BusinessCardApplicationRole> userAppRoleList = functionalMenuService.getUserAppRolesList(userId);

			Collections.sort(userAppRoleList, getUserAppRolesComparator);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userApplicationRoles", "result =",
					userAppRoleList);

			AppRoles = new ArrayList<BusinessCardApplicationRolesList>();
			for (BusinessCardApplicationRole userAppRole : userAppRoleList) {
				boolean found = false;
				List<String> roles = null;

				for (BusinessCardApplicationRolesList app : AppRoles) {
					if (app.getAppName().equals(userAppRole.getAppName())) {
						roles = app.getRoleNames();
						roles.add(userAppRole.getRoleName());
						app.setRoleNames(roles);
						found = true;
						break;
					}
				}

				if (!found) {
					roles = new ArrayList<String>();
					roles.add(userAppRole.getRoleName());
					AppRoles.add(new BusinessCardApplicationRolesList(userAppRole.getAppName(), roles));
				}

				Collections.sort(roles);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppList failed", e);
		}

		return AppRoles;

	}
}
