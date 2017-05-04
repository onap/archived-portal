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
package org.openecomp.portalapp.externalsystemapproval.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.externalsystemapproval.model.ExternalSystemRoleApproval;
import org.openecomp.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.openecomp.portalapp.portal.controller.BasicAuthenticationController;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.UserRolesService;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class RolesApprovalSystemController implements BasicAuthenticationController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RolesApprovalSystemController.class);

	@Autowired
	private UserRolesService userRolesService;

	/**
	 * Creates an application user with the specified roles.
	 * 
	 * @param request
	 * @param extSysUser
	 * @return PortalRestResponse with appropriate status value and message
	 */
	@ApiOperation(value = "Creates an application user with the specified roles.", response = PortalRestResponse.class)
	@RequestMapping(value = { "/userProfile" }, method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> postUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser) {
		try {
			validateExtSystemUser(extSysUser, true);
			String response = userRolesService.setAppWithUserRoleStateForUser(extSysUser);
			return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Saved Successfully", response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "postUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}
	}

	/**
	 * Updates an application user to have only the specified roles.
	 * 
	 * @param request
	 * @param extSysUser
	 * @return PortalRestResponse with appropriate status value and message
	 */
	@ApiOperation(value = "Updates an application user to have only the specified roles.", response = PortalRestResponse.class)
	@RequestMapping(value = { "/userProfile" }, method = RequestMethod.PUT, produces = "application/json")
	public PortalRestResponse<String> putUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser) {
		try {
			validateExtSystemUser(extSysUser, true);
			String response = userRolesService.setAppWithUserRoleStateForUser(extSysUser);
			return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Updated Successfully", response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}
	}

	/**
	 * Deletes an application user by removing all assigned roles.
	 * 
	 * @param request
	 * @param extSysUser
	 *            This object must have zero roles.
	 * @return PortalRestResponse with appropriate status value and message
	 */
	@ApiOperation(value = "Processes a request to delete one or more application roles for one	specified user who has roles.", response = PortalRestResponse.class)
	@RequestMapping(value = { "/userProfile" }, method = RequestMethod.DELETE, produces = "application/json")
	public PortalRestResponse<String> deleteUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser) {
		try {
			validateExtSystemUser(extSysUser, false);
			// Ignore any roles that might be mistakenly present in the request
			extSysUser.setRoles(new ArrayList<ExternalSystemRoleApproval>());
			String result = userRolesService.setAppWithUserRoleStateForUser(extSysUser);
			return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Deleted Successfully", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}
	}

	/**
	 * Checks for presence of required fields.
	 * 
	 * @param extSysUser
	 * @param rolesRequired
	 *            If true, checks whether roles are present
	 * @throws Exception
	 *             If any field is missing.
	 */
	private void validateExtSystemUser(ExternalSystemUser extSysUser, boolean rolesRequired) throws Exception {
		if (extSysUser.getLoginId() == null)
			throw new Exception("Request has no login ID");
		if (extSysUser.getApplicationName() == null)
			throw new Exception("Request has no application name");
		if (extSysUser.getMyloginrequestId() == null)
			throw new Exception("Request has no request ID");
		if (rolesRequired && (extSysUser.getRoles() == null || extSysUser.getRoles().size() == 0))
			throw new Exception("Request has no roles");
	}

}
