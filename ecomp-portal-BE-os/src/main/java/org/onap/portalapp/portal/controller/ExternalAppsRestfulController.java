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
 * 
 */
package org.onap.portalapp.portal.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.onap.portalapp.portal.controller.ExternalAppsRestfulController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ExternalAppsRestfulController extends EPRestrictedRESTfulBaseController {
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAppsRestfulController.class);
	
	@Autowired
	FunctionalMenuService functionalMenuService;
	
	@Autowired
	EPLoginService epLoginService;
	
	@Autowired
	AdminRolesService adminRolesService;
	
	@RequestMapping(value={"/getFavorites"}, method = RequestMethod.GET,produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginId 			= "";
		String userAgent		= "";
		List<FavoritesFunctionalMenuItemJson> favorites = null;
		
		loginId 		= request.getHeader(EPSystemProperties.MDC_LOGIN_ID);
		userAgent 		= MDC.get(EPSystemProperties.PARTNER_NAME);
				
		EPUser epUser = epLoginService.findUserWithoutPwd(loginId);
		logger.info(EELFLoggerDelegate.errorLogger, "getFavorites request was received from " + userAgent + " for the user " + loginId + ".");
		if (epUser==null || epUser.getId()==null) {
			logger.error(EELFLoggerDelegate.errorLogger, "No User record found for the LoginId '" + loginId + "' in the database.");
			throw new Exception("Received null for Login-Id.");
		} else {
			favorites = functionalMenuService.getFavoriteItems(epUser.getId());
			FieldsValidator fieldsValidator = new FieldsValidator();
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
			
			EcompPortalUtils.logAndSerializeObject("/auxapi/getFavorites", "result = ", favorites);
		}
		
		return favorites;
	}
	
	@RequestMapping(value={"/functionalMenuItemsForUser"}, method = RequestMethod.GET,produces = "application/json")
	public List<FunctionalMenuItem> getFunctionalMenuItemsForUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginId 			= "";
		String userAgent 		= "";
		List<FunctionalMenuItem> fnMenuItems = null;
		
		loginId 		= request.getHeader("LoginId");
		userAgent 		= MDC.get(EPSystemProperties.PARTNER_NAME);

		EPUser epUser = epLoginService.findUserWithoutPwd(loginId);
		logger.info(EELFLoggerDelegate.errorLogger, "getFunctionalMenuItemsForUser request was received from " + userAgent + " for the user " + loginId + ".");
		if (epUser==null || epUser.getId()==null) {
			logger.error(EELFLoggerDelegate.errorLogger, "No User record found for the LoginId '" + loginId + "' in the database.");
			throw new Exception("Received null for Login-Id.");
		} else if (adminRolesService.isSuperAdmin(epUser)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "FunctionalMenuHandler: SuperUser, about to call getFunctionalMenuItems()");
			fnMenuItems = functionalMenuService.getFunctionalMenuItems();
		}
		else {
			logger.debug(EELFLoggerDelegate.debugLogger, "getMenuItemsForAuthUser: about to call getFunctionalMenuItemsForUser()");
			fnMenuItems = functionalMenuService.getFunctionalMenuItemsForUser(epUser.getOrgUserId());
		}
		
		FieldsValidator fieldsValidator = new FieldsValidator();
		response.setStatus(fieldsValidator.httpStatusCode.intValue());
		
		EcompPortalUtils.logAndSerializeObject("/auxapi/functionalMenuItemsForUser", "result = ", fnMenuItems);
		
		return fnMenuItems;
	}
	
	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.warn(EELFLoggerDelegate.errorLogger, "Handling bad request", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}
}
