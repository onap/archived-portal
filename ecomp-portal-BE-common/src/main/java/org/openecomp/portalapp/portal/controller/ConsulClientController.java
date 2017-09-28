/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.orbitz.consul.ConsulException;
import com.orbitz.consul.model.health.ServiceHealth;

import io.searchbox.client.config.exception.NoServerConfiguredException;

@RestController
@RequestMapping("/portalApi/consul")
public class ConsulClientController extends EPRestrictedBaseController {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ConsulClientController.class);

	@Autowired
	private ConsulHealthService consulHealthService;

	// Get location of a healthy node running our service
	@RequestMapping(value = { "/service/{service}" }, method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<String> getServiceLocation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("service") String service) {

		try {
			return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Success!",
					consulHealthService.getServiceLocation(service, null));
		} catch (NoServerConfiguredException e) {
			logger.error(logger.errorLogger, "No healthy service exception!");
			return new PortalRestResponse<String>(PortalRestStatusEnum.WARN, "Warning!",
					"No healthy service exception!");
		} catch (ConsulException e) {
			logger.error(logger.errorLogger, "Couldn't connect ot consul - Is consul running?");
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Error!",
					"Couldn't connect ot consul - Is consul running?");
		}
	}

	@RequestMapping(value = { "/service/healthy/{service}" }, method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<List<ServiceHealth>> getAllHealthyNodes(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("service") String service) {
		try {
			return new PortalRestResponse<List<ServiceHealth>>(PortalRestStatusEnum.OK, "Success!",
					consulHealthService.getAllHealthyNodes(service));
		} catch (ConsulException e) {
			logger.error(logger.errorLogger, "Couldn't connect to consul - shouldn't break anything.");
			return new PortalRestResponse<List<ServiceHealth>>(PortalRestStatusEnum.ERROR, "Error!", new ArrayList<>());
		}
	}

	@RequestMapping(value = { "/service/all/{service}" }, method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<List<ServiceHealth>> getAllNodes(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("service") String service) {
		try {
			return new PortalRestResponse<List<ServiceHealth>>(PortalRestStatusEnum.OK, "Success!",
					consulHealthService.getAllNodes(service));
		} catch (ConsulException e) {
			logger.error(logger.errorLogger, "Couldn't connect to consul - shouldn't break anything.");
			return new PortalRestResponse<List<ServiceHealth>>(PortalRestStatusEnum.ERROR, "Error!", new ArrayList<>());
		}
	}

}
