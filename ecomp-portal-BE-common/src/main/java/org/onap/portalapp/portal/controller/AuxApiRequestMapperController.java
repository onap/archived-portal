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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.aaf.cadi.aaf.AAFPermission;
import org.onap.portalapp.annotation.ApiVersion;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.ManifestService;
import org.onap.portalapp.portal.transport.Analytics;
import org.onap.portalapp.portal.transport.CentralUser;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalapp.validation.SecureString;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auxapi")
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class AuxApiRequestMapperController implements ApplicationContextAware, BasicAuthenticationController {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AuxApiRequestMapperController.class);
	private DataValidator dataValidator = new DataValidator();

	ApplicationContext context = null;
	int minorVersion = 0;

	@Autowired
	private ManifestService manifestService;

	@ApiOperation(value = "Gets user roles for an application which is upgraded to newer version.", response = String.class, responseContainer = "List")
	@GetMapping(value = { "/v3/user/{loginId}" }, produces = "application/json")
	public String getUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginId") String loginId) throws Exception {
		if (loginId!=null){
			SecureString secureLoginId = new SecureString(loginId);
			if (!dataValidator.isValid(secureLoginId))
				return "Provided data is not valid";
		}


		Map<String, Object> res = getMethod(request, response);
		String answer = null;
		try {
			answer = (String) invokeMethod(res, request, response, loginId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUser failed", e);
		}
		return answer;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets roles for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/roles" }, produces = "application/json")
	public List<CentralV2Role> getRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> res = getMethod(request, response);
		request.getMethod();
		List<CentralV2Role> answer = null;
		try {
			answer = (List<CentralV2Role>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoles failed", e);
		}
		return answer;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Saves role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/v3/role" }, produces = "application/json")
	public PortalRestResponse<String> saveRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Role role) throws Exception {
		Map<String, Object> res = getMethod(request, response);
		PortalRestResponse<String> out = null;
		try {
			out = (PortalRestResponse<String>) invokeMethod(res, request, response, role);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return out;
	}

	@ApiOperation(value = "Gets v2 role information for an application which is upgraded to newer version.", response = CentralV2Role.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/role/{role_id}" }, produces = "application/json")
	public CentralV2Role getRoleInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("role_id") Long roleId) throws Exception {
		Map<String, Object> res = getMethod(request, response);
		CentralV2Role role = null;
		try {
			role = (CentralV2Role) invokeMethod(res, request, response, roleId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo failed", e);
		}
		return role;

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets all active Users of application", response = String.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/users" }, produces = "application/json")
	public List<EcompUser> getUsersOfApplication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> res = getMethod(request, response);
		List<EcompUser> users = null;
		try {
			users = (List<EcompUser>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUsersOfApplication failed", e);
		}
		return users;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets all role functions for an application which is upgraded to newer version.", response = CentralV2RoleFunction.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/functions" }, produces = "application/json")
	public List<CentralV2RoleFunction> getRoleFunctionsList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> res = getMethod(request, response);
		List<CentralV2RoleFunction> roleFunctionsList = null;
		try {
			roleFunctionsList = (List<CentralV2RoleFunction>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunctionsList failed", e);
		}
		return roleFunctionsList;
	}

	@ApiOperation(value = "Gets role information for an application provided by function code.", response = CentralV2RoleFunction.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/function/{code}" }, produces = "application/json")
	public CentralV2RoleFunction getRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) throws Exception {
		if (code!=null){
			SecureString secureCode = new SecureString(code);
			if (!dataValidator.isValid(secureCode))
				return new CentralV2RoleFunction();
		}

		Map<String, Object> res = getMethod(request, response);
		CentralV2RoleFunction roleFunction = null;
		try {
			roleFunction = (CentralV2RoleFunction) invokeMethod(res, request, response, code);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction failed", e);
		}
		return roleFunction;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Saves role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/v3/roleFunction" }, produces = "application/json")
	public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String roleFunc) throws Exception {
		if (roleFunc!=null){
			SecureString secureRoleFunc = new SecureString(roleFunc);
			if(!dataValidator.isValid(secureRoleFunc))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Provided data is not valid", "Failed");
		}
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed", new Exception("saveRoleFunction failed"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "saveRoleFunction failed", "Failed");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return result.get();
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Deletes role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@DeleteMapping(value = { "/v3/roleFunction/{code}" }, produces = "application/json")
	public PortalRestResponse<String> deleteRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) throws Exception {
		PortalRestResponse<String> result = null;

		if (code!=null){
			SecureString secureCode = new SecureString(code);
			if(!dataValidator.isValid(secureCode))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Provided data is not valid", "Failed");
		}

		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, code);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "deletes  roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@DeleteMapping(value = { "/v3/deleteRole/{roleId}" }, produces = "application/json")
	public PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("roleId") Long roleId) throws Exception {
		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, roleId);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets active roles for an application.", response = CentralV2Role.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/activeRoles" }, produces = "application/json")
	public List<CentralV2Role> getActiveRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<CentralV2Role> cenRole = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			cenRole = (List<CentralV2Role>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
		}
		return cenRole;
	}

	@ApiOperation(value = "Gets ecompUser of an application.", response = CentralUser.class, responseContainer = "List")
	@GetMapping(value = { "/v4/user/{loginId}" }, produces = "application/json")
	public String getEcompUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginId") String loginId) throws Exception {
		Map<String, Object> res = getMethod(request, response);

		if (loginId!=null){
			SecureString secureLoginId = new SecureString(loginId);

			if (!dataValidator.isValid(secureLoginId))
				return null;
		}

		String answer = null;
		try {
			answer = (String) invokeMethod(res, request, response, loginId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getEcompUser failed", e);
		}
		return answer;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets user ecomp role for an application.", response = CentralUser.class, responseContainer = "List")
	@GetMapping(value = { "/v4/roles" }, produces = "application/json")
	public List<EcompRole> getEcompRolesOfApplication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> res = getMethod(request, response);
		List<EcompRole> answer = null;
		try {
			answer = (List<EcompRole>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getEcompRolesOfApplication failed", e);
		}
		return answer;
	}

	@ApiOperation(value = "Gets session slot-check interval, a duration in milliseconds.", response = Integer.class)
	@GetMapping(value = {
			"/v3/getSessionSlotCheckInterval" }, produces = "application/json")
	public Integer getSessionSlotCheckInterval(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> res = getMethod(request, response);
		Integer ans = null;
		try {
			ans = (Integer) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getSessionSlotCheckInterval failed", e);
		}
		return ans;
	}

	@ApiOperation(value = "Extends session timeout values for all on-boarded applications.", response = Boolean.class)
	@PostMapping(value = { "/v3/extendSessionTimeOuts" })
	public Boolean extendSessionTimeOuts(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String sessionMap) throws Exception {

		if (sessionMap!=null){
			SecureString secureSessionMap = new SecureString(sessionMap);
			if (!dataValidator.isValid(secureSessionMap)){
				return null;
			}
		}

		Map<String, Object> res = getMethod(request, response);
		Boolean ans = null;
		try {
			ans = (Boolean) invokeMethod(res, request, response, sessionMap);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "extendSessionTimeOuts failed", e);
		}
		return ans;
	}

	@ApiOperation(value = "Gets javascript with functions that support gathering and reporting web analytics.", response = String.class)
	@GetMapping(value = { "/v3/analytics" }, produces = "application/javascript")
	public String getAnalyticsScript(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> res = getMethod(request, response);
		String ans = null;
		try {
			ans = (String) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAnalyticsScript failed", e);
		}
		return ans;
	}

	@PostMapping(value = { "/v3/storeAnalytics" }, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Accepts data from partner applications with web analytics data.", response = PortalAPIResponse.class)
	public PortalAPIResponse storeAnalyticsScript(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Analytics analyticsMap) throws Exception {

		if (analyticsMap!=null){
			if (!dataValidator.isValid(analyticsMap))
				return new PortalAPIResponse(false, "analyticsScript is not valid");
		}

		Map<String, Object> res = getMethod(request, response);
		PortalAPIResponse ans = new PortalAPIResponse(true, "error");
		try {
			ans = (PortalAPIResponse) invokeMethod(res, request, response, analyticsMap);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "storeAnalyticsScript failed", e);
		}
		return ans;

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/portal/functions" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadFunctions(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadFunctions", new Exception("Failed to bulkUploadFunctions"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadFunctions", "Failed");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return result.get();
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/v3/upload/portal/roles" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Optional<PortalRestResponse<String>> result;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadRoles", new Exception("Failed to bulkUploadRoles"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload role functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/portal/roleFunctions" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadRoleFunctions(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Optional<PortalRestResponse<String>> result;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadRoleFunctions", new Exception("Failed to bulkUploadRoleFunctions"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoleFunctions", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoleFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload user roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/portal/userRoles" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadUserRoles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Optional<PortalRestResponse<String>> result;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadUserRoles", new Exception("Failed to bulkUploadUserRoles"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUserRoles", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUserRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload users for renamed role of an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/portal/userRole/{roleId}" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadUsersSingleRole(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Long roleId) throws Exception {
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadUsersSingleRole", new Exception("Failed to bulkUploadUsersSingleRole"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUsersSingleRole", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUsersSingleRole failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/partner/functions" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadPartnerFunctions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadPartnerRoleFunctions", new Exception("Failed to bulkUploadPartnerRoleFunctions"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadPartnerRoleFunctions", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	// not using
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload roles for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = { "/v3/upload/partner/roles" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadPartnerRoles(HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<Role> upload) throws Exception {
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadRoles", new Exception("Failed to bulkUploadRoles"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Bulk upload role functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PostMapping(value = {
			"/v3/upload/partner/roleFunctions" }, produces = "application/json")
	public PortalRestResponse<String> bulkUploadPartnerRoleFunctions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Optional<PortalRestResponse<String>> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = Optional.ofNullable((PortalRestResponse<String>) invokeMethod(res, request, response));
			if (!result.isPresent()){
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to bulkUploadPartnerRoleFunctions", new Exception("Failed to bulkUploadPartnerRoleFunctions"));
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadPartnerRoleFunctions", "Failed");
			}
			return result.get();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerRoleFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets all functions along with global functions", response = List.class, responseContainer = "Json")
	@GetMapping(value = { "/v3/menuFunctions" }, produces = "application/json")
	public List<String> getMenuFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<String> functionsList = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			functionsList = (List<String>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuFunctions failed", e);
		}
		return functionsList;
	}

	private String getPatchNumber() {
		String response = "0";
		try {
			Attributes attributes = manifestService.getWebappManifest();
			response = attributes.getValue("Build-Number");
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegate.errorLogger, "getPatchNumber failed", ex);
		}
		return response;
	}

	@SuppressWarnings("rawtypes")
	private List<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> beans = context.getBeansWithAnnotation(ApiVersion.class);
		@SuppressWarnings("unchecked")
		List<Object> beansList = new ArrayList(beans.values());
		return beansList;

	}

	private Map<String, Object> getMethod(HttpServletRequest request, HttpServletResponse response) {
		Method finalmethod = null;
		String url = request.getRequestURI();
		String version = "";
		String service = "";
		Object currentObject = null;
		Map<String, Object> res = new HashMap<String, Object>();
		String[] uriArray = url.split("/auxapi");
		List<Integer> minorversionList = new ArrayList<>();
		if (uriArray.length > 1) {
			service = uriArray[1];
		}
		int first = service.indexOf("/");
		int second = service.indexOf("/", first + 1);
		version = service.substring(first + 1, second);
		int min = minorVersion;
		if (request.getHeader("MinorVersion") != null) {
			min = Integer.parseInt(request.getHeader("MinorVersion"));
		}
		res.put("min", version+"."+min);
		res.put("service", service);
		List<Object> objList = getObject(request, response);
		String requestedApiMethodType = request.getMethod();
		String majorVersion = latestMajorVersionOfService(objList, service, version, requestedApiMethodType);
		int latestMinorVersion = latestMinorVersionOfService(objList, service, version, requestedApiMethodType);
		res.put("majorVersion", majorVersion);
		res.put("latestMinorVersion", String.valueOf(latestMinorVersion));
		outerloop: for (Object obj : objList) {
			final List<Method> allMethods = getAllMethodsOfClass(obj);
			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(ApiVersion.class)) {
					ApiVersion annotInstance = method.getAnnotation(ApiVersion.class);
					Pattern p = Pattern.compile(annotInstance.service(),
							Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
					Matcher matcher = p.matcher(service);
					boolean b = matcher.matches();
					logger.debug(EELFLoggerDelegate.debugLogger, "Requested Servie is:" + service
							+ "Requested MinVersion is:" + min + "Requested MajorVersion is: " + version);
					if (annotInstance.max().equals(version) && b && annotInstance.min() == min
							&& annotInstance.method().equals(request.getMethod())) {
						finalmethod = method;
						currentObject = obj;
						res.put("method", method);
						res.put("Obj", obj);
						break outerloop;
					}
				}
			}
		}
		return res;
	}

	private String latestMajorVersionOfService(List<Object> objList, String service, String reuqestedVersion,
			String requestedApiMethodType) {
		Integer majorVersion = 0;
		String serviceEndPoint = service;
		int firstindex = serviceEndPoint.indexOf("/");
		int secondindex = serviceEndPoint.indexOf("/", firstindex + 1);
		serviceEndPoint = serviceEndPoint.substring(secondindex + 1);

		List<Integer> latestMajorVersionList = new ArrayList<>();
		for (Object obj : objList) {
			final List<Method> allMethods = getAllMethodsOfClass(obj);
			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(ApiVersion.class)) {
					ApiVersion annotInstance = method.getAnnotation(ApiVersion.class);
					String endpoint = annotInstance.service();
					int first = endpoint.indexOf("/");
					int second = endpoint.indexOf("/", first + 1);
					endpoint = endpoint.substring(second + 1);
					Pattern p = Pattern.compile(endpoint,
							Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
					Matcher matcher = p.matcher(serviceEndPoint);
					boolean b = matcher.matches();
					if (b && annotInstance.method().equals(requestedApiMethodType)) {
						int index1 = annotInstance.service().indexOf("/");
						int index2 = annotInstance.service().indexOf("/", index1 + 1);
						String majorversion = annotInstance.service().substring(index1 + 2, index2);
						latestMajorVersionList.add(Integer.parseInt(majorversion));
					}
				}
			}
		}
		majorVersion = Collections.max(latestMajorVersionList);
		String majorVersionWithLastestMin = "/v"+String.valueOf(majorVersion)+"/"+serviceEndPoint;
		int latestMinorVersion = latestMinorVersionOfService(objList, majorVersionWithLastestMin, "v"+String.valueOf(majorVersion), requestedApiMethodType);
		return majorVersion+"."+latestMinorVersion;
	}

	private List<Method> getAllMethodsOfClass(Object obj) {
		List<Method> allMethods = new ArrayList<>();
		Class<?> objClz = obj.getClass();
		if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
			objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
		}
		allMethods = new ArrayList<Method>(Arrays.asList(objClz.getMethods()));
		allMethods.removeIf(s -> !(s.isAnnotationPresent(ApiVersion.class)));
		return allMethods;
	}

	private Integer latestMinorVersionOfService(List<Object> objList, String service, String reuqestedVersion,
			String requestedApiMethodType) {
		Integer minVersion = 0;
		String serviceEndPoint = service;
		List<Integer> latestMinorVersionList = new ArrayList<>();
		for (Object obj : objList) {
			final List<Method> allMethods = getAllMethodsOfClass(obj);
			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(ApiVersion.class)) {
					ApiVersion annotInstance = method.getAnnotation(ApiVersion.class);
					String endpoint = annotInstance.service();

					Pattern p = Pattern.compile(endpoint,
							Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
					Matcher matcher = p.matcher(serviceEndPoint);
					boolean b = matcher.matches();
					if (annotInstance.max().equals(reuqestedVersion) && b
							&& annotInstance.method().equals(requestedApiMethodType)) {
						int minorversion = annotInstance.min();
						latestMinorVersionList.add(minorversion);
					}
				}
			}
		}
		minVersion = Collections.max(latestMinorVersionList);
		return minVersion;
	}

	private HttpServletResponse setResponse(HttpServletResponse response, String requestedMinVersion,
			String majorVersion, String latestMinorVersion, String service) {
		response.setHeader("X-MinorVersion", requestedMinVersion.toUpperCase());
		response.setHeader("X-PatchVersion", getPatchNumber());
		response.setHeader("X-LatestVersion", "V"+majorVersion);
		return response;
	}

	/**
	 * 
	 * @param res
	 * @param args
	 *            method parameters(Maintain HttpServletRequest at 0th position
	 *            and HttpServletResponse at 1th position in args array)
	 * @return
	 * @throws Exception
	 */
	private Object invokeMethod(Map<String, Object> res, Object... args) throws Exception {
		Method method = (Method) res.get("method");
		Object obj = res.get("Obj");
		Object responseObj = null;
		String min = res.get("min").toString();
		String majorVersion = res.get("majorVersion").toString();
		String latestMinorVersion = res.get("latestMinorVersion").toString();
		String service = res.get("service").toString();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		setResponse(response, min, majorVersion, latestMinorVersion, service);
		final Map<String, String> errorMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		String reason = "";
		try {
			if (method != null && obj != null) {
				responseObj = method.invoke(obj, args);
			} else {
				errorMap.put("error", "Requested api is not available");
				reason = mapper.writeValueAsString(errorMap);
				response.getWriter().write(reason);
				logger.debug(EELFLoggerDelegate.debugLogger, "Requested api " + request.getRequestURI()
						+ "is not available with minorVersion " + request.getHeader("MinorVersion"));
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Method :" + method + "invocation failed", e);
		}
		return responseObj;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Creates an application user with the specified roles.", response = PortalRestResponse.class)
	@PostMapping(value = { "/v3/userProfile" }, produces = "application/json")
	public PortalRestResponse<String> postUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {

		if (extSysUser!=null){
			if (!dataValidator.isValid(extSysUser))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ExternalSystemUser is not valid", "Failed");
		}

		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, extSysUser);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "postUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Updates an application user to have only the specified roles.", response = PortalRestResponse.class)
	@PutMapping(value = { "/v3/userProfile" }, produces = "application/json")
	public PortalRestResponse<String> putUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {

		if (extSysUser!=null){
			if (!dataValidator.isValid(extSysUser))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ExternalSystemUser is not valid", "Failed");
		}

		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, extSysUser);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Processes a request to delete one or more application roles for one	specified user who has roles.", response = PortalRestResponse.class)
	@DeleteMapping(value = { "/v3/userProfile" }, produces = "application/json")
	public PortalRestResponse<String> deleteUserProfile(HttpServletRequest request,
			@RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {

		if (extSysUser!=null){
			if (!dataValidator.isValid(extSysUser))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ExternalSystemUser is not valid", "Failed");
		}

		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, extSysUser);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deleteUserProfile failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Accepts messages from external ticketing systems and creates notifications for Portal users.", response = PortalRestResponse.class)
	@PostMapping(value = { "/v3/ticketevent" })
	public PortalRestResponse<String> handleRequest(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String ticketEventJson) throws Exception {

		if (ticketEventJson!=null){
			SecureString secureTicketEventJson = new SecureString(ticketEventJson);
			if (!dataValidator.isValid(secureTicketEventJson))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ticketEventJson is not valid", "Failed");
		}

		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, ticketEventJson);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "handleRequest failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Creates a new user as a Portal administrator.", response = PortalRestResponse.class)
	@PostMapping(value = "/v3/portalAdmin", produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> postPortalAdmin(HttpServletRequest request, HttpServletResponse response,
			@RequestBody EPUser epUser) {

		if (epUser!=null){
			if (!dataValidator.isValid(epUser))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "EPUser is not valid", "Failed");
		}

		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, epUser);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "postPortalAdmin failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@ApiOperation(value = "Gets the specified application that is on-boarded in Portal.", response = OnboardingApp.class)
	@GetMapping(value = { "/v3/onboardApp/{appId}" }, produces = "application/json")
	@ResponseBody
	public OnboardingApp getOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId) {
		OnboardingApp result = new OnboardingApp();
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (OnboardingApp) invokeMethod(res, request, response, appId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getOnboardAppExternal failed", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Adds a new application to Portal.", response = PortalRestResponse.class)
	@PostMapping(value = { "/v3/onboardApp" }, produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> postOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OnboardingApp newOnboardApp) {

		if (newOnboardApp!=null){
			if (!dataValidator.isValid(newOnboardApp))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "OnboardingApp is not valid", "Failed");
		}

		PortalRestResponse<String> result = new PortalRestResponse<>();
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, newOnboardApp);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "postOnboardAppExternal failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Updates information about an on-boarded application in Portal.", response = PortalRestResponse.class)
	@PutMapping(value = { "/v3/onboardApp/{appId}" }, produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> putOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId, @RequestBody OnboardingApp oldOnboardApp) {

		if (oldOnboardApp!=null){
			if (!dataValidator.isValid(oldOnboardApp))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "OnboardingApp is not valid", "Failed");
		}

		PortalRestResponse<String> result;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response, appId, oldOnboardApp);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "putOnboardAppExternal failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}

	@ApiOperation(value = "Creates a Portal user notification for roles identified in the content from an external application.", response = PortalAPIResponse.class)
	@PostMapping(value = { "/v3/publishNotification" }, produces = "application/json")
	@ResponseBody
	public PortalAPIResponse publishNotification(HttpServletRequest request,
			@RequestBody EpNotificationItem notificationItem, HttpServletResponse response) {

		if (notificationItem!=null){
			if (!dataValidator.isValid(notificationItem))
				return new PortalAPIResponse(false, "EpNotificationItem is not valid");
		}

		Map<String, Object> res = getMethod(request, response);
		try {
			return (PortalAPIResponse) invokeMethod(res, request, response, notificationItem);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "publishNotification failed", e);
			return new PortalAPIResponse(false, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets favorite items within the functional menu for the current user.", response = FavoritesFunctionalMenuItemJson.class, responseContainer = "List")
	@GetMapping(value = { "/v3/getFavorites" }, produces = "application/json")
	public List<FavoritesFunctionalMenuItemJson> getFavoritesForUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<FavoritesFunctionalMenuItemJson> favorites = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			favorites = (List<FavoritesFunctionalMenuItemJson>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFavoritesForUser failed", e);
		}
		return favorites;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Gets functional menu items appropriate for the current user.", response = FunctionalMenuItem.class, responseContainer = "List")
	@GetMapping(value = {
			"/v3/functionalMenuItemsForUser" }, produces = "application/json")
	public List<FunctionalMenuItem> getFunctionalMenuItemsForUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<FunctionalMenuItem> fnMenuItems = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			fnMenuItems = (List<FunctionalMenuItem>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenuItemsForUser failed", e);
		}
		return fnMenuItems;
	}

	
	@ApiOperation(value = "Gets MechId roles", response = String.class, responseContainer = "List")
	@GetMapping(value = { "/v3/systemUser" }, produces = "application/json")
	public List<AAFPermission> getSystemUserPerms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<AAFPermission> permsList = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			permsList = (List<AAFPermission>) invokeMethod(res, request, response);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getSystemUserPerms failed", e);
		}
		return permsList;
	}
	
	@ApiOperation(value = "Update role description in external auth system for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@PutMapping(value = { "/v3/update/app/roleDescription" }, produces = "application/json")
	public  PortalRestResponse<String> updateAppRoleDescription(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortalRestResponse<String> result = null;
		Map<String, Object> res = getMethod(request, response);
		try {
			result = (PortalRestResponse<String>) invokeMethod(res, request, response);
			return result;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateAppRoleDescription failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
	}
}
