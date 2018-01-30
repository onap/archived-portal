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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPEndpoint;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.BasicAuthAccountService;
import org.onap.portalapp.util.EPUserUtils;
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
public class BasicAuthAccountController extends EPRestrictedBaseController {

	@Autowired
	private BasicAuthAccountService basicAuthAccountService;

	@Autowired
	private AdminRolesService adminRolesService;

	/**
	 * Saves Basic Authentication account for external systems
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param newBasicAuthAccount
	 *            BasicAuthCredentials
	 * @return Id of the newly created account
	 * @throws Exception
	 *             on failure
	 */
	@RequestMapping(value = { "/portalApi/basicAuthAccount" }, method = RequestMethod.POST)
	public PortalRestResponse<String> createBasicAuthAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BasicAuthCredentials newBasicAuthAccount) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required",
					"Admin Only Operation! ");
		}

		if (newBasicAuthAccount == null) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE",
					"newBasicAuthAccount cannot be null or empty");
		}
		long accountId = basicAuthAccountService.saveBasicAuthAccount(newBasicAuthAccount);

		List<Long> endpointIdList = new ArrayList<>();
		try {
			for (EPEndpoint ep : newBasicAuthAccount.getEndpoints()) {
				endpointIdList.add(basicAuthAccountService.saveEndpoints(ep));
			}
			for (Long endpointId : endpointIdList) {
				basicAuthAccountService.saveEndpointAccount(accountId, endpointId);
			}
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}

		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}

	/**
	 * Returns list of all BasicAuthCredentials in the system
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return List<BasicAuthCredentials>
	 * @throws Exception
	 *             on failure
	 */

	@RequestMapping(value = { "/portalApi/basicAuthAccount" }, method = RequestMethod.GET)
	public PortalRestResponse<List<BasicAuthCredentials>> getBasicAuthAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
			return new PortalRestResponse<List<BasicAuthCredentials>>(PortalRestStatusEnum.ERROR,
					"UnAuthorized! Admin Only Operation", new ArrayList<>());
		}

		return new PortalRestResponse<List<BasicAuthCredentials>>(PortalRestStatusEnum.OK, "Success",
				basicAuthAccountService.getAccountData());
	}

	/**
	 * Updates an existing BasicAuthCredentials account
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param accountId
	 *            account ID
	 * @param newBasicAuthAccount
	 *            BasicAuthCredentials
	 * @return PortalRestResponse<String>
	 * @throws Exception
	 *             on failure
	 */
	@RequestMapping(value = { "/portalApi/basicAuthAccount/{accountId}" }, method = RequestMethod.PUT)
	public PortalRestResponse<String> updateAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId, @RequestBody BasicAuthCredentials newBasicAuthAccount)
			throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required",
					"Admin Only Operation! ");
		}

		if (newBasicAuthAccount == null) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE",
					"BasicAuthCredentials cannot be null or empty");
		}
		try {
			basicAuthAccountService.updateBasicAuthAccount(accountId, newBasicAuthAccount);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}

	/**
	 * deletes an existing BasicAuthCredentials account
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param accountId
	 *            account ID
	 * @return PortalRestResponse<String>
	 * @throws Exception
	 *             on failure
	 */
	@RequestMapping(value = { "/portalApi/basicAuthAccount/{accountId}" }, method = RequestMethod.DELETE)
	public PortalRestResponse<String> deleteAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required",
					"Admin Only Operation! ");
		}

		try {
			basicAuthAccountService.deleteEndpointAccout(accountId);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}

}
