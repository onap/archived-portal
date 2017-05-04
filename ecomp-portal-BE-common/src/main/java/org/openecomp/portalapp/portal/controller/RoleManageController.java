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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.controller.core.RoleController;
import org.openecomp.portalapp.controller.core.RoleFunctionListController;
import org.openecomp.portalapp.controller.core.RoleListController;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Proxies REST calls to role-management functions that arrive on paths
 * /portalApi/* over to controller methods provided by the SDK-Core library.
 * Those controller methods are mounted on paths not exposed by the Portal FE.
 */
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class RoleManageController extends EPRestrictedBaseController {

	@Autowired
	private RoleController roleController;

	@Autowired
	private RoleListController roleListController;

	@Autowired
	private RoleFunctionListController roleFunctionListController;

	/**
	 * Calls an SDK-Core library method that gets the available roles and writes
	 * them to the request object. Portal specifies a Hibernate mappings from
	 * the Role class to the fn_role_v view, which ensures that only Portal
	 * (app_id is null) roles are fetched.
	 * 
	 * Any method declared void (no return value) or returning null causes the
	 * audit log aspect method to declare failure. TODO: should return a JSON
	 * string.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/portalApi/get_roles" }, method = RequestMethod.GET)
	public void getRoles(HttpServletRequest request, HttpServletResponse response) {
		getRoleListController().getRoles(request, response);
	}

	@RequestMapping(value = { "/portalApi/role_list/toggleRole" }, method = RequestMethod.POST)
	public PortalRestResponse<String> toggleRole(HttpServletRequest request, HttpServletResponse response) {
		PortalRestResponse<String> portalRestResponse = null;
		try{
			getRoleListController().toggleRole(request, response);
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success", null);
		}catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "failure", e.getMessage());
		}
		return portalRestResponse;		
	}

	@RequestMapping(value = { "/portalApi/role_list/removeRole" }, method = RequestMethod.POST)
	public ModelAndView removeRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleListController().removeRole(request, response);
	}

	@RequestMapping(value = { "/portalApi/role/saveRole" }, method = RequestMethod.POST)
	public ModelAndView saveRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleController().saveRole(request, response);
	}

	@RequestMapping(value = { "/portalApi/role/removeRoleFunction" }, method = RequestMethod.POST)
	public ModelAndView removeRoleRoleFunction(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getRoleController().removeRoleFunction(request, response);
	}

	@RequestMapping(value = { "/portalApi/role/addRoleFunction" }, method = RequestMethod.POST)
	public ModelAndView addRoleRoRoleFunction(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getRoleController().addRoleFunction(request, response);
	}

	@RequestMapping(value = { "/portalApi/role/removeChildRole" }, method = RequestMethod.POST)
	public ModelAndView removeChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleController().removeChildRole(request, response);
	}

	@RequestMapping(value = { "/portalApi/role/addChildRole" }, method = RequestMethod.POST)
	public ModelAndView addChildRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getRoleController().addChildRole(request, response);
	}

	@RequestMapping(value = { "/portalApi/get_role" }, method = RequestMethod.GET)
	public void getRole(HttpServletRequest request, HttpServletResponse response) {
		getRoleController().getRole(request, response);
	}

	@RequestMapping(value = { "/portalApi/get_role_functions" }, method = RequestMethod.GET)
	public void getRoleFunctionList(HttpServletRequest request, HttpServletResponse response) {
		getRoleFunctionListController().getRoleFunctionList(request, response);
	}

	@RequestMapping(value = { "/portalApi/role_function_list/saveRoleFunction" }, method = RequestMethod.POST)
	public void saveRoleFunction(HttpServletRequest request, HttpServletResponse response, @RequestBody String roleFunc) throws Exception {
		getRoleFunctionListController().saveRoleFunction(request, response, roleFunc);
	}

	@RequestMapping(value = { "/portalApi/role_function_list/removeRoleFunction" }, method = RequestMethod.POST)
	public void removeRoleFunction(HttpServletRequest request, HttpServletResponse response, @RequestBody String roleFunc) throws Exception {
		getRoleFunctionListController().removeRoleFunction(request, response, roleFunc);
	}

	public RoleListController getRoleListController() {
		return roleListController;
	}

	public void setRoleListController(RoleListController roleListController) {
		this.roleListController = roleListController;
	}

	public RoleController getRoleController() {
		return roleController;
	}

	public void setRoleController(RoleController roleController) {
		this.roleController = roleController;
	}

	public RoleFunctionListController getRoleFunctionListController() {
		return roleFunctionListController;
	}

	public void setRoleFunctionListController(RoleFunctionListController roleFunctionListController) {
		this.roleFunctionListController = roleFunctionListController;
	}

}
