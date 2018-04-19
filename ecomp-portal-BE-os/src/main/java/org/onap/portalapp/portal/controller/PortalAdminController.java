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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.controller.PortalAdminController;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.PortalAdminService;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.PortalAdmin;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
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
public class PortalAdminController extends EPRestrictedBaseController {
	@Autowired
	PortalAdminService portalAdminService;
	@Autowired
	AdminRolesService adminRolesService;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalAdminController.class);

	@RequestMapping(value = { "/portalApi/portalAdmins" }, method = RequestMethod.GET, produces = "application/json")
	public List<PortalAdmin> getPortalAdmins(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<PortalAdmin> portalAdmins = null;
		if (user == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.getPortalAdmins, null user");
			EcompPortalUtils.setBadPermissions(user, response, "getPortalAdmins");
		} else if (!adminRolesService.isSuperAdmin(user)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.getPortalAdmins, bad permissions");
			EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
		} else {
			// return the list of portal admins
			portalAdmins = portalAdminService.getPortalAdmins();
			logger.debug(EELFLoggerDelegate.debugLogger, "portalAdmins: called getPortalAdmins()");
			EcompPortalUtils.logAndSerializeObject("/portalApi/getPortalAdmins", "result =", portalAdmins);
		}

		return portalAdmins;
	}

	/**
	 * RESTful service method to create a new portal admin. Requirement: you
	 * must be the Onap portal super admin user.
	 */

	@RequestMapping(value = { "/portalApi/portalAdmin" }, method = RequestMethod.POST)
	public FieldsValidator createPortalAdmin(HttpServletRequest request, @RequestBody String userid,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (user == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.createPortalAdmin, null user");
			EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
		} else if (!adminRolesService.isSuperAdmin(user)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.createPortalAdmin bad permissions");
			EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
		} else {
			fieldsValidator = portalAdminService.createPortalAdmin(userid);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		EcompPortalUtils.logAndSerializeObject("/portalAdmin", "POST result =", response.getStatus());

		return fieldsValidator;
	}

	@RequestMapping(value = { "/portalApi/portalAdmin/{orgUserId}" }, method = RequestMethod.DELETE)
	public FieldsValidator deletePortalAdmin(HttpServletRequest request, @PathVariable("orgUserId") Long orgUserId,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (!adminRolesService.isSuperAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, "deletePortalAdmin");
		} else {
			fieldsValidator = portalAdminService.deletePortalAdmin(orgUserId);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		EcompPortalUtils.logAndSerializeObject("/portalAdmin", "DELETE result =", response.getStatus());

		return fieldsValidator;
	}
	
	@RequestMapping(value = { "/portalApi/adminAppsRoles/{appId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<EPRole> getRolesByApp(HttpServletRequest request, @PathVariable("appId") Long appId,
			HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<EPRole> rolesByApp = null;

		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				rolesByApp = adminRolesService.getRolesByApp(user, appId);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRolesByApp failed", e);
		}

		return rolesByApp;
	}
}
