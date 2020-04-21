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
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class BasicAuthAccountController extends EPRestrictedBaseController {

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    private static final String AUTHORIZATION_REQUIRED = "Authorization Required";
    private static final String ADMIN_ONLY_OPERATIONS = "Admin Only Operation! ";

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(BasicAuthAccountController.class);
    private final DataValidator dataValidator = new DataValidator();

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
	@PostMapping(value = { "/portalApi/basicAuthAccount" })
	public PortalRestResponse<String> createBasicAuthAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BasicAuthCredentials newBasicAuthAccount) throws Exception {



		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, AUTHORIZATION_REQUIRED,
                    ADMIN_ONLY_OPERATIONS);
		}

		if (newBasicAuthAccount == null) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
					"newBasicAuthAccount cannot be null or empty");
		}

		if(!dataValidator.isValid(newBasicAuthAccount)){
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "createBasicAuthAccount() failed, new credential are not safe",
				"");
		}

		long accountId;
		try {
			accountId = basicAuthAccountService.saveBasicAuthAccount(newBasicAuthAccount);
		} catch (Exception e){
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
		}

		List<Long> endpointIdList = new ArrayList<>();
		try {
			for (EPEndpoint ep : newBasicAuthAccount.getEndpoints()) {
				endpointIdList.add(basicAuthAccountService.saveEndpoints(ep));
			}
			for (Long endpointId : endpointIdList) {
				basicAuthAccountService.saveEndpointAccount(accountId, endpointId);
			}
		} catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "createBasicAuthAccount failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
		}

        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, "");
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

	@GetMapping(value = { "/portalApi/basicAuthAccount" })
	public PortalRestResponse<List<BasicAuthCredentials>> getBasicAuthAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
					"UnAuthorized! Admin Only Operation", new ArrayList<>());
		}

        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Success",
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
	@PutMapping(value = { "/portalApi/basicAuthAccount/{accountId}" })
	public PortalRestResponse<String> updateAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId, @RequestBody BasicAuthCredentials newBasicAuthAccount)
			throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, AUTHORIZATION_REQUIRED,
                    ADMIN_ONLY_OPERATIONS);
		}

		if (newBasicAuthAccount == null) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
					"BasicAuthCredentials cannot be null or empty");
		}
		try {
			basicAuthAccountService.updateBasicAuthAccount(accountId, newBasicAuthAccount);
		} catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "updateAccount failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
		}
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, "");
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
	@DeleteMapping(value = { "/portalApi/basicAuthAccount/{accountId}" })
	public PortalRestResponse<String> deleteAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId) throws Exception {

		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, AUTHORIZATION_REQUIRED,
                    ADMIN_ONLY_OPERATIONS);
		}

		try {
			basicAuthAccountService.deleteEndpointAccout(accountId);
		} catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteAccount failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
		}
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, "");
	}

}
