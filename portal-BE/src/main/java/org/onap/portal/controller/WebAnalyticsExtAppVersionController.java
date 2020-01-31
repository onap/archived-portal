/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.annotation.ApiVersion;
import org.onap.portal.domain.dto.transport.Analytics;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@EPAuditLog
@ApiVersion
public class WebAnalyticsExtAppVersionController {

	private final WebAnalyticsExtAppController webAnalyticsExtAppController;

	@Autowired
	public WebAnalyticsExtAppVersionController(final
		WebAnalyticsExtAppController webAnalyticsExtAppController) {
		this.webAnalyticsExtAppController = webAnalyticsExtAppController;
	}

	@ApiVersion(max = "v3", service = "/v3/analytics", min = 0,method = "GET")
	public String getAnalyticsScript(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return webAnalyticsExtAppController.getAnalyticsScript(request);
	}

	@ApiVersion(max = "v3", service = "/v3/storeAnalytics", min = 0,method = "POST")
	public PortalAPIResponse storeAnalyticsScript(HttpServletRequest request, HttpServletResponse response, Analytics analyticsMap) throws Exception {
		return webAnalyticsExtAppController.storeAnalyticsScript(request, analyticsMap);
	}

}
