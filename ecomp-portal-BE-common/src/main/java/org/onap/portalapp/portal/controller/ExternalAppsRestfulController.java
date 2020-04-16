/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modifications Copyright (c) 2019 Samsung
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.onap.portalapp.music.conf.MusicSession;
import org.onap.portalapp.music.util.MusicUtil;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPServiceCookie;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPRoleService;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.UserNotificationService;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ExternalAppsRestfulController extends EPRestrictedRESTfulBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAppsRestfulController.class);
	private final DataValidator DATA_VALIDATOR = new DataValidator();

	@Autowired
	private FunctionalMenuService functionalMenuService;

	@Autowired
	private EPLoginService epLoginService;

	@Autowired
	private AdminRolesService adminRolesService;

	@Autowired
	private UserNotificationService userNotificationService;

	@Autowired
	private EPRoleService epRoleService;

	@ApiOperation(value = "Creates a Portal user notification for roles identified in the content from an external application.", response = PortalAPIResponse.class)
	@RequestMapping(value = { "/publishNotification" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PortalAPIResponse publishNotification(HttpServletRequest request,
			@RequestBody EpNotificationItem notificationItem) throws Exception {

		if(!DATA_VALIDATOR.isValid(notificationItem)){
			PortalAPIResponse response = new PortalAPIResponse(false, "failed");
			return response;
		}
		String appKey = request.getHeader("uebkey");
		EPApp app = findEpApp(appKey);
		List<Long> postRoleIds = new ArrayList<Long>();
		if (app != null) {
        for (Long roleId : notificationItem.getRoleIds()) {
            EPRole role = epRoleService.getRole(app.getId(), roleId);
            if (role != null)
                postRoleIds.add(role.getId());
        	}
    	}

		// --- recreate the user notification object with the POrtal Role Ids
		EpNotificationItem postItem = new EpNotificationItem();
		postItem.setRoleIds(postRoleIds);
		postItem.setIsForAllRoles("N");
		postItem.setIsForOnlineUsers("N");
		postItem.setActiveYn("Y");
		postItem.setPriority(notificationItem.getPriority());
		postItem.setMsgHeader(notificationItem.getMsgHeader());
		postItem.setMsgDescription(notificationItem.getMsgDescription());
		postItem.setStartTime(notificationItem.getStartTime());
		postItem.setEndTime(notificationItem.getEndTime());
		postItem.setCreatedDate(Calendar.getInstance().getTime());
		// default creator to 1 for now
		postItem.setCreatorId(PortalConstants.DEFAULT_NOTIFICATION_CREATOR);
		// ----

		try {
			userNotificationService.saveNotification(postItem);
		} catch (Exception e) {
			return new PortalAPIResponse(false, e.getMessage());
		}

		PortalAPIResponse response = new PortalAPIResponse(true, "success");
		return response;
	}

	private EPApp findEpApp(String uebKey) {
		List<?> list = null;
		Map<String, String> params = new HashMap<>();
		params.put("appKey", uebKey);
		try {
			list = this.getDataAccessService().executeNamedQuery("getMyAppDetailsByUebKey", params, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMyAppDetailsByUebKey failed", e);
		}
			
		return (list == null || list.isEmpty()) ? null : (EPApp) list.get(0);
	}

	@ApiOperation(value = "Gets favorite items within the functional menu for the current user.", response = FavoritesFunctionalMenuItemJson.class, responseContainer="List")
	@RequestMapping(value = { "/getFavorites" }, method = RequestMethod.GET, produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginId = "";
		String userAgent = "";
		List<FavoritesFunctionalMenuItemJson> favorites = null;

		loginId = request.getHeader(EPCommonSystemProperties.MDC_LOGIN_ID);
		userAgent = MDC.get(EPCommonSystemProperties.PARTNER_NAME);

		EPUser epUser = epLoginService.findUserWithoutPwd(loginId);
		logger.info(EELFLoggerDelegate.errorLogger,
				"getFavorites request was received from " + userAgent + " for the user " + loginId + ".");
		if (epUser == null || epUser.getId() == null) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"No User record found for the LoginId '" + loginId + "' in the database.");
			throw new Exception("Received null for Login-Id.");
		} else {
			favorites = functionalMenuService.getFavoriteItems(epUser.getId());
			FieldsValidator fieldsValidator = new FieldsValidator();
			response.setStatus(fieldsValidator.httpStatusCode.intValue());

			EcompPortalUtils.logAndSerializeObject(logger, "/getFavorites", "result = ", favorites);
		}

		return favorites;
	}

	@ApiOperation(value = "Gets functional menu items appropriate for the current user.", response = FunctionalMenuItem.class, responseContainer="List")
	@RequestMapping(value = {
			"/functionalMenuItemsForUser" }, method = RequestMethod.GET, produces = "application/json")
	public List<FunctionalMenuItem> getFunctionalMenuItemsForUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginId = "";
		String userAgent = "";
		List<FunctionalMenuItem> fnMenuItems = null;

		loginId = request.getHeader("LoginId");
		userAgent = MDC.get(EPCommonSystemProperties.PARTNER_NAME);

		EPUser epUser = epLoginService.findUserWithoutPwd(loginId);
		logger.info(EELFLoggerDelegate.errorLogger, "getFunctionalMenuItemsForUser request was received from "
				+ userAgent + " for the user " + loginId + ".");
		if (epUser == null || epUser.getId() == null) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"No User record found for the LoginId '" + loginId + "' in the database.");
			throw new Exception("Received null for Login-Id.");
		} else if (adminRolesService.isSuperAdmin(epUser)) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"FunctionalMenuHandler: SuperUser, about to call getFunctionalMenuItems()");
			fnMenuItems = functionalMenuService.getFunctionalMenuItems();
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"getMenuItemsForAuthUser: about to call getFunctionalMenuItemsForUser()");
			fnMenuItems = functionalMenuService.getFunctionalMenuItemsForUser(epUser.getOrgUserId());
		}

		FieldsValidator fieldsValidator = new FieldsValidator();
		response.setStatus(fieldsValidator.httpStatusCode.intValue());

		EcompPortalUtils.logAndSerializeObject(logger, "/functionalMenuItemsForUser", "result = ", fnMenuItems);

		return fnMenuItems;
	}

	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.warn(EELFLoggerDelegate.errorLogger, "Handling bad request", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}
	
	@EPAuditLog
	@RequestMapping(value = { "/validateCookie" }, method = RequestMethod.POST)
	public boolean validateCookie(@RequestBody EPServiceCookie epServiceCookie, HttpServletRequest request) throws Exception {
		Map<String,String> epServiceCookieValueMap = epServiceCookie.getValue();
		if(epServiceCookieValueMap!=null) {
			String multifactorauthfrontendurl = SystemProperties.getProperty("frontend_url");
			String encryptedJSessionId = epServiceCookieValueMap.get(multifactorauthfrontendurl);
			if(encryptedJSessionId != null) {
				String jSessionId = CipherUtil.decryptPKC(encryptedJSessionId);
				if(jSessionId != null) {
					if(jSessionId.equals(request.getSession().getId())) {
						if(MusicUtil.isMusicEnable()) { 
							MusicSession musicSession = new MusicSession();
							String sessionId = musicSession.getAttribute(encryptedJSessionId);
							logger.info(EELFLoggerDelegate.errorLogger, "Music sessionid : "+sessionId);
							return (sessionId != null); 
						} else {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
