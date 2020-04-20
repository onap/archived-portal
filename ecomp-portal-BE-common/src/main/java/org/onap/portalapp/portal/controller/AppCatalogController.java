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

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.AppCatalogItem;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.PersUserAppService;
import org.onap.portalapp.portal.transport.AppCatalogPersonalization;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class AppCatalogController extends EPRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppCatalogController.class);

	@Autowired
	private AdminRolesService adminRolesService;
	@Autowired
	private EPAppService appService;
	@Autowired
	private PersUserAppService persUserAppService;

	/**
	 * RESTful service method to fetch all enabled applications, with details
	 * about which are accessible to the current user, selected by the current
	 * user.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException If sendError fails
	 * @return List of items suitable for display
	 */
	@GetMapping(value = { "/portalApi/appCatalog" }, produces = "application/json")
	public List<AppCatalogItem> getAppCatalog(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		EPUser user = EPUserUtils.getUserSession(request);
		List<AppCatalogItem> appCatalog = null;
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getAppCatalog");
			} else {
				if (adminRolesService.isSuperAdmin(user))
					appCatalog = appService.getAdminAppCatalog(user);
				else
					appCatalog = appService.getUserAppCatalog(user);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/getAppCatalog", "GET result =", appCatalog);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppCatalog failed", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		return appCatalog;
	}

	/**
	 * RESTful service to accept a user's action made on the application
	 * catalog.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param persRequest
	 *            JSON with data including application ID
	 * @param response
	 *            HttpServletResponse
	 * @return FieldsValidator
	 * @throws IOException If sendError fails
	 */
	@PutMapping(value = { "/portalApi/appCatalog" }, produces = "application/json")
	public FieldsValidator putAppCatalogSelection(HttpServletRequest request,
			@RequestBody AppCatalogPersonalization persRequest, HttpServletResponse response) throws IOException {
		FieldsValidator result = new FieldsValidator();
		EPApp app = appService.getApp(persRequest.getAppId());
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			if (app == null || user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "putAppCatalogSelection");
			} else {
				persUserAppService.setPersUserAppValue(user, app, persRequest.getSelect(), persRequest.getPending());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putAppCatalogSelection failed", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		result.httpStatusCode = new Long(HttpServletResponse.SC_OK);
		return result;
	}

}