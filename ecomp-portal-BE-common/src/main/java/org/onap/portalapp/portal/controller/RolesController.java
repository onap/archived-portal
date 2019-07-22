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
 */
package org.onap.portalapp.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.aaf.cadi.aaf.AAFPermission;
import org.onap.portalapp.annotation.ApiVersion;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.AuthUtil;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.swagger.annotations.ApiOperation;

@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
@ApiVersion
public class RolesController implements BasicAuthenticationController {
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RolesController.class);


	final String LOGINID_PATTERN = "/v3/user/[a-zA-Z0-9]{1,25}$";
	final String FUNCTION_CD_PATTERN = "/v3/function/[a-zA-Z0-9_-]{1,75}$";

	final String DELETE_ROLEFUNCTION = "/v3/roleFunction/[a-zA-Z0-9_-]{1,75}$";
	
	private static final String UEBKEY = "uebkey";
	
	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	@Autowired
	ExternalAccessRolesController externalAccessRolesController;
	

	@ApiOperation(value = "Gets roles for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer = "Json")
	@ApiVersion(max = "v3", service = "/v3/roles", min = 0, method = "GET")
	public List<CentralV2Role> getV2RolesForApp(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.getV2RolesForApp(request, response);
	}

	@ApiVersion(max = "v3", service = LOGINID_PATTERN, min = 0, method = "GET")
	public String getV2UserList(HttpServletRequest request, HttpServletResponse response, String loginId)
			throws Exception {
		return externalAccessRolesController.getV2UserList(request, response, loginId);
	}

	@ApiVersion(max = "v3", service = "/v3/role", min = 0, method = "POST")
	public PortalRestResponse<String> saveRole(HttpServletRequest request, HttpServletResponse response, Role role)
			throws Exception {
		return externalAccessRolesController.saveRole(request, response, role);
	}

	@ApiVersion(max = "v3", service = "/v3/role/[0-9]{1,25}$", min = 0, method = "GET")
	public CentralV2Role getV2RoleInfo(HttpServletRequest request, HttpServletResponse response, Long roleId)
			throws Exception {
		return externalAccessRolesController.getV2RoleInfo(request, response, roleId);
	}

	@ApiVersion(max = "v3", service = "/v3/users", min = 0, method = "GET")
	public List<EcompUser> getUsersOfApplication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.getUsersOfApplication(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/functions", min = 0, method = "GET")
	public List<CentralV2RoleFunction> getRoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.getV2RoleFunctionsList(request, response);
	}

	@ApiVersion(max = "v3", service = FUNCTION_CD_PATTERN, min = 0, method = "GET")
	public CentralV2RoleFunction getRoleFunction(HttpServletRequest request, HttpServletResponse response, String code)
			throws Exception {
		return externalAccessRolesController.getV2RoleFunction(request, response, code);
	}

	@ApiVersion(max = "v3", service = "/v3/roleFunction", min = 0, method = "POST")
	public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response,
			String roleFunc) throws Exception {
		return externalAccessRolesController.saveRoleFunction(request, response, roleFunc);
	}

	@ApiVersion(max = "v3", service = DELETE_ROLEFUNCTION, min = 0, method = "DELETE")
	public PortalRestResponse<String> deleteRoleFunction(HttpServletRequest request, HttpServletResponse response,
			String code) throws Exception {
		return externalAccessRolesController.deleteRoleFunction(request, response, code);
	}

	@ApiVersion(max = "v3", service = "/v3/deleteRole/[0-9]{1,25}$", min = 0, method = "DELETE")
	public PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response, Long roleId)
			throws Exception {
		return externalAccessRolesController.deleteRole(request, response, roleId);
	}

	@ApiVersion(max = "v3", service = "/v3/activeRoles", min = 0, method = "GET")
	public List<CentralV2Role> getV2ActiveRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.getV2ActiveRoles(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/portal/functions", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadFunctions(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.bulkUploadFunctions(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/portal/roles", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.bulkUploadRoles(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/portal/roleFunctions", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadRoleFunctions(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.bulkUploadRoleFunctions(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/portal/userRoles", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadUserRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.bulkUploadUserRoles(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/portal/userRole/[0-9]{1,25}$", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadUsersSingleRole(HttpServletRequest request,
			HttpServletResponse response, Long roleId) throws Exception {
		return externalAccessRolesController.bulkUploadUsersSingleRole(request, response, roleId);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/partner/functions", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadPartnerFunctions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return externalAccessRolesController.bulkUploadPartnerFunctions(request, response);
	}
//not using this
	@ApiVersion(max = "v3", service = "/v3/upload/partner/roles", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadPartnerRoles(HttpServletRequest request, HttpServletResponse response,
			List<Role> upload) throws Exception {
		return externalAccessRolesController.bulkUploadPartnerRoles(request, response, upload);
	}

	@ApiVersion(max = "v3", service = "/v3/upload/partner/roleFunctions", min = 0, method = "POST")
	public PortalRestResponse<String> bulkUploadPartnerRoleFunctions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return externalAccessRolesController.bulkUploadPartnerRoleFunctions(request, response);
	}

	@ApiVersion(max = "v3", service = "/v3/menuFunctions", min = 0, method = "GET")
	public List<String> getMenuFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return externalAccessRolesController.getMenuFunctions(request, response);
	}
	
	@ApiVersion(max = "v3", service = "/v3/update/app/roleDescription", min = 0, method = "PUT")
	public PortalRestResponse<String> updateAppRoleDescription(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer updatedRoleDesc = 0;
		try {
			updatedRoleDesc = externalAccessRolesService.updateAppRoleDescription(request.getHeader(UEBKEY));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "updateAppRoleDescription: failed!", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
					"updateAppRoleDescription: " + e.getMessage(), "Failure");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK,
				"Successfully updated app role descriptions: '" + updatedRoleDesc + "'", "Success");
	}

	@ApiVersion(max = "v4", service = "/v4/user/[a-zA-Z0-9]{1,25}$", min = 0, method = "GET")
	public String getEcompUser(HttpServletRequest request, HttpServletResponse response, String loginId)
			throws Exception {
		return externalAccessRolesController.getEcompUser(request, response, loginId);
	}

	@ApiVersion(max = "v4", service = "/v4/roles", min = 0, method = "GET")
	public List<EcompRole> getEcompRolesOfApplication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return externalAccessRolesController.getEcompRolesOfApplication(request, response);
	}
	
	@ApiVersion(max = "v3", service = "/v3/systemUser", min = 0, method = "GET")
	public List<AAFPermission> getSystemUser(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		return AuthUtil.getAAFPermissions(request);
	}
}
