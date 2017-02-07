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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.http.HTTPException;
import org.json.JSONObject;
import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.service.SharedContextService;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItemJson;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.UserProfileService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


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
	
	/**
	 * RESTful service method to fetch all the FunctionalMenuItems.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/portalApi/functionalMenu" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItems(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try{
			menuItems = functionalMenuService.getFunctionalMenuItems();
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenu", "result =", menuItems);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling functionalMenu. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}	
		return menuItems;
	}
	
	
	/**
	 * RESTful service method to fetch all the FunctionalMenuItems, both active and inactive,
	 * for the EditFunctionalMenu feature.
	 * Can only be accessed by the portal admin.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/portalApi/functionalMenuForEditing" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForEditing(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try{
		    if (!adminRolesService.isSuperAdmin(user)) {
			    EcompPortalUtils.setBadPermissions(user, response, "getFunctionalMenuItemDetails");
		    } else {
			    menuItems = functionalMenuService.getFunctionalMenuItems(true);
		    }
		    EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuForEditing", "result =", menuItems);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling functionalMenuForEditing. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}    
		return menuItems;
	}
	
	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with an application.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/portalApi/functionalMenuForApp/{appId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForApp(HttpServletRequest request, @PathVariable("appId") Integer appId) throws HTTPException {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try{
			menuItems = functionalMenuService.getFunctionalMenuItemsForApp(appId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuForApp/"+appId, "result =", menuItems);			
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling functionalMenuForApp. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}
	
	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with the applications
	 * and roles that a user has access to.
	 * 
	 * @return
	 */
	
	@RequestMapping(value = { "/portalApi/functionalMenuForUser/{userId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForUser(HttpServletRequest request, @PathVariable("userId") String userId) throws HTTPException {
		// TODO: should only the superuser be allowed to use this API?
		List<FunctionalMenuItem> menuItems = null;
		try{
			menuItems = functionalMenuService.getFunctionalMenuItemsForUser(userId);
			functionalMenuService.assignHelpURLs(menuItems);
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuForUser/"+userId, "result =", menuItems);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling functionalMenuForUser. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}

		return menuItems;
	}
	
	/**
	 * RESTful service method to fetch all FunctionalMenuItems associated with the applications
	 * and roles that the authenticated user has access to.
	 * 
	 * @return
	 */
	
	@RequestMapping(value = { "/portalApi/functionalMenuForAuthUser" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getMenuItemsForAuthUser(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<FunctionalMenuItem> menuItems = null;
		try{
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getMenuItemsForAuthUser");
			} else if (adminRolesService.isSuperAdmin(user)) {
				menuItems = functionalMenuService.getFunctionalMenuItems();
			} else {
				// calculate the menu items
				String orgUserId = user.getOrgUserId();
				menuItems = functionalMenuService.getFunctionalMenuItemsForUser(orgUserId);
				EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuForUser/"+orgUserId, "result =", menuItems);
			}
			functionalMenuService.assignHelpURLs(menuItems);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling getMenuItemsForAuthUser. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}
		return menuItems;
	}
	
	/**
	 * RESTful service method to fetch the details for a functional menu item.
	 * Requirement: you must be the Ecomp portal super admin user.
	 * 
	 * @return
	 */
	
	@RequestMapping(value = { "/portalApi/functionalMenuItemDetails/{menuId}" }, method = RequestMethod.GET, produces = "application/json")
	public FunctionalMenuItem getFunctionalMenuItemDetails(HttpServletRequest request, @PathVariable("menuId") Integer menuId, HttpServletResponse response) throws HTTPException {
		// TODO: return FunctionalMenuItemJson
		// TODO: modify FunctionalMenuItem to not include the transient fields
		FunctionalMenuItem menuItem = null;
		try{
			EPUser user = EPUserUtils.getUserSession(request);
			
			if (!adminRolesService.isSuperAdmin(user)) {
				EcompPortalUtils.setBadPermissions(user, response, "getFunctionalMenuItemDetails");
			} else {
				menuItem = functionalMenuService.getFunctionalMenuItemDetails(menuId);
				EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuItemDetails/"+menuId, "result =", menuItem);
			}			
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while calling functionalMenuItemDetails. Details: " + 
					EcompPortalUtils.getStackTrace(e));
		}
			
		return menuItem;
	}
	
	/**
	 * RESTful service method to create a new menu item.
	 * Requirement: you must be the Ecomp portal super admin user.
	 */

	@RequestMapping(value={"/portalApi/functionalMenuItem"}, method = RequestMethod.POST)
	public FieldsValidator createFunctionalMenuItem(HttpServletRequest request, @RequestBody FunctionalMenuItemJson menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "FunctionalMenuController.createFunctionalMenuItem bad permissions");
			EcompPortalUtils.setBadPermissions(user, response, "createFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.createFunctionalMenuItem(menuItemJson);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuItem", "POST result =", response.getStatus());
		}
		
		return fieldsValidator;
	}

	/**
	 * RESTful service method to update an existing menu item
	 * Requirement: you must be the Ecomp portal super admin user.
	 */

	@RequestMapping(value={"/portalApi/functionalMenuItem"}, method = RequestMethod.PUT)
	public FieldsValidator editFunctionalMenuItem(HttpServletRequest request, @RequestBody FunctionalMenuItemJson menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "editFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.editFunctionalMenuItem(menuItemJson);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuItem", "PUT result =", response.getStatus());
		}
		
		return fieldsValidator;
	}

	@RequestMapping(value={"/portalApi/functionalMenuItem/{menuId}"}, method = RequestMethod.DELETE)
	public FieldsValidator deleteFunctionalMenuItem(HttpServletRequest request, @PathVariable("menuId") Long menuId, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "deleteFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.deleteFunctionalMenuItem(menuId);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuItem", "DELETE result =", response.getStatus());
		}
		
		return fieldsValidator;
	}

	@RequestMapping(value = {"/portalApi/regenerateFunctionalMenuAncestors" }, method = RequestMethod.GET)
	public FieldsValidator regenerateAncestorTable(HttpServletRequest request, HttpServletResponse response) {
		// TODO: should only the superuser be allowed to use this API?
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "deleteFunctionalMenuItem");
		} else {
			fieldsValidator = functionalMenuService.regenerateAncestorTable();
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			EcompPortalUtils.logAndSerializeObject("/portalApi/regenerateAncestorTable", "GET result =", response.getStatus());
		}
		
		return fieldsValidator;
	}
	
	
	@RequestMapping(value={"/portalApi/setFavoriteItem"}, method = RequestMethod.POST)
	public FieldsValidator addFavoriteItem(HttpServletRequest request, @RequestBody FavoritesFunctionalMenuItem menuItemJson, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		menuItemJson.userId = user.getId();
		fieldsValidator = functionalMenuService.setFavoriteItem(menuItemJson);
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject("/portalApi/setFavoriteItem", "Post result =", response.getStatus());
		
		return fieldsValidator;
	}
	
	
	@RequestMapping(value={"/portalApi/getFavoriteItems"}, method = RequestMethod.GET,produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);		
		List<FavoritesFunctionalMenuItemJson> favorites = functionalMenuService.getFavoriteItems(user.getId());
		FieldsValidator fieldsValidator = new FieldsValidator();
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject("/portalApi/getFavoriteItems", "GET result =", response.getStatus());
		
		return favorites;
	}
	
	@RequestMapping(value={"/portalApi/removeFavoriteItem/{menuId}"}, method = RequestMethod.DELETE)
	public FieldsValidator deleteFavoriteItem(HttpServletRequest request, @PathVariable("menuId") Long menuId, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		Long userId = user.getId();
		fieldsValidator = functionalMenuService.removeFavoriteItem(userId,menuId);
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		EcompPortalUtils.logAndSerializeObject("/deleteFavoriteItem", "DELETE result =", response.getStatus());
		
		return fieldsValidator;
	}
	
	@RequestMapping(value = {"/portalApi/functionalMenuStaticInfo" }, method = RequestMethod.GET,produces = "application/json")
	public String functionalMenuStaticInfo(HttpServletRequest request, HttpServletResponse response) {

		/*Getting first name, last name, and email from session*/
		logger.debug(EELFLoggerDelegate.debugLogger, "getting functionalMenuStaticInfo values from session");
		Map<String,String> map = new HashMap<String,String>();
		String sessionId = request.getSession().getId();
		SharedContext userIdSC = null ,	firstNameSC = null, lastNameSC = null, emailSC = null;
		String userIdStr= null, firstNameStr= null, lastNameStr = null, emailStr = null;			
		EPUser user = EPUserUtils.getUserSession(request);
		String contact_us_link = null;
		String last_login = null;
		try{
			contact_us_link = SystemProperties.getProperty(EPSystemProperties.CONTACT_US_URL);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			Date lastLoginDate = user.getLastLoginDate();
			last_login =sdf.format(lastLoginDate);// sdf.parse(lastLoginDate.);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "CONTACT_US_URL is missing in property file! Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
		try{
			if (user!=null) {
				firstNameStr = user.getFirstName();
				lastNameStr = user.getLastName();
				userIdStr = user.getOrgUserId();
				emailStr = user.getEmail();
				/*if(emailStr==null || emailStr.equals("")){
					EPUser userResult = searchService.searchUsersByUserId(user); //.searchUserByUserId(userIdStr);
					emailStr = userResult.getEmail();
				}		*/		
			} else {
				logger.warn(EELFLoggerDelegate.errorLogger, "Unable to locate the user information in the session. LoginId: " + MDC.get(EPSystemProperties.MDC_LOGIN_ID));
			}
			
			/*If failed getting from session, then get the values from Shared Context*/
			if(firstNameStr==null)
				firstNameSC = sharedContextService.getSharedContext(sessionId,EPSystemProperties.USER_FIRST_NAME);
			if(lastNameStr==null)
				lastNameSC = sharedContextService.getSharedContext(sessionId,EPSystemProperties.USER_LAST_NAME);
			if(emailStr==null)
				emailSC = sharedContextService.getSharedContext(sessionId,EPSystemProperties.USER_EMAIL);
			if(userIdStr==null)
				userIdSC = sharedContextService.getSharedContext(sessionId,EPSystemProperties.USER_ORG_USERID);
			
			/*Getting Contact Us link from properties file*/	
			
			map.put("firstName", firstNameStr!=null?firstNameStr:(firstNameSC!=null?firstNameSC.getCvalue():null));
			map.put("lastName", lastNameStr!=null?lastNameStr:(lastNameSC!=null?lastNameSC.getCvalue():null));
			map.put("email", emailStr!=null?emailStr:(emailSC!=null?emailSC.getCvalue():null));
			map.put("userId", userIdStr!=null?userIdStr:(userIdSC!=null?userIdSC.getCvalue():null));
			map.put("last_login",last_login);
			map.put("contact_us_link",contact_us_link);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while getting values : " + EcompPortalUtils.getStackTrace(e));
		}
		
		JSONObject j = new JSONObject(map);
		String fnMenuStaticResponse = "";
		try {
			fnMenuStaticResponse = j.toString();
		//	response.getWriter().write(fnMenuStaticResponse);
			EcompPortalUtils.logAndSerializeObject("/portalApi/functionalMenuStaticInfo", "GET result =", fnMenuStaticResponse);
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while writing the result to the HttpServletResponse object. Details: " + 
							EcompPortalUtils.getStackTrace(e));
		}
		return fnMenuStaticResponse;
	}

	@RequestMapping(value = {"/portalApi/resetFunctionalMenuStaticInfo" }, method = RequestMethod.GET,produces = "application/json")
	public PortalRestResponse<Map<String, String>> resetFunctionalMenuStaticInfo(HttpServletRequest request, HttpServletResponse response) {
		PortalRestResponse<String> portalRestResponse = null;
		try {
		// get current user
		EPUser user = EPUserUtils.getUserSession(request);
		String userId = user.getOrgUserId();
        StringBuffer criteria = new StringBuffer();
        criteria.append(" where org_user_id = '").append(userId).append("'");
		// retrieve latest user info from EPUser
        List list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
        EPUser updatedUser = (EPUser)list.get(0);
        EPUserUtils.setUserSession(request, updatedUser,  new HashSet(), new HashSet(), SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM));
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);
		}
		catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getOnlineUserUpdateRate failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}	

	}
}

