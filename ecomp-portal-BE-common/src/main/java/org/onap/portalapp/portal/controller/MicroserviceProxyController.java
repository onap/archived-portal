/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modification Copyright © 2020 IBM.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPUnRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.MicroserviceProxyService;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class MicroserviceProxyController extends EPUnRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceProxyController.class);

	@Autowired
	private MicroserviceProxyService microserviceProxyService;

	@GetMapping(value = { "/portalApi/microservice/proxy/{serviceId}" }, produces = "application/json")
	public String getMicroserviceProxy(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceId") long serviceId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		String answer = "";
		try {
			answer = microserviceProxyService.proxyToDestination(serviceId, user, request);
		} catch (HttpClientErrorException e) {
			answer = e.getResponseBodyAsString();
		}
		return isValidJSON(answer) ? answer : "{\"error\":\"" + answer.replace(System.getProperty("line.separator"), "") + "\"}";
	}

	@GetMapping(value = { "/portalApi/microservice/proxy/parameter/{widgetId}" }, produces = "application/json")
	public String getMicroserviceProxyByWidgetId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		String answer = "";
		try {
			answer = microserviceProxyService.proxyToDestinationByWidgetId(widgetId, user, request);
		} catch (HttpClientErrorException e) {
			answer = e.getResponseBodyAsString();
		}
		return isValidJSON(answer) ? answer : "{\"error\":\"" + answer.replace(System.getProperty("line.separator"), "") + "\"}";
	}

	/**
	 * Check whether the response is a valid JSON
	 * @param response
	 * @return true if the response is valid JSON, otherwise, false
	 */
	private boolean isValidJSON(String response) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(response);
			return true;
		} catch (IOException e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "isValidJSON failed", e);
			return false;
		}
	}
}
