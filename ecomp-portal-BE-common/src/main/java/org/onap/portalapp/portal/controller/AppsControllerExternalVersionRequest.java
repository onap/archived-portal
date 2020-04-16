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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.annotation.ApiVersion;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
@ApiVersion
public class AppsControllerExternalVersionRequest implements BasicAuthenticationController {

	@Autowired
	AppsControllerExternalRequest appsControllerExternalRequest;

	@ApiVersion(max = "v3", service = "/v3/portalAdmin", min = 0, method = "POST")
	public PortalRestResponse<String> postPortalAdmin(HttpServletRequest request, HttpServletResponse response,
			EPUser epUser) {
		return appsControllerExternalRequest.postPortalAdmin(request, response, epUser);
	}

	@ApiVersion(max = "v3", service = "/v3/onboardApp/[0-9]{1,25}$", min = 0, method = "GET")
	public OnboardingApp getOnboardAppExternal(HttpServletRequest request, HttpServletResponse response, Long appId) {
		return appsControllerExternalRequest.getOnboardAppExternal(request, response, appId);
	}

	@ApiVersion(max = "v3", service = "/v3/onboardApp", min = 0, method = "POST")
	public PortalRestResponse<String> postOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			OnboardingApp newOnboardApp) {
		return appsControllerExternalRequest.postOnboardAppExternal(request, response, newOnboardApp);

	}
	@ApiVersion(max = "v3", service = "/v3/onboardApp/[0-9]{1,25}$", min = 0, method = "PUT")
	public PortalRestResponse<String> putOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			Long appId, OnboardingApp oldOnboardApp) {
		return appsControllerExternalRequest.putOnboardAppExternal(request, response, appId, oldOnboardApp);
	}
}
