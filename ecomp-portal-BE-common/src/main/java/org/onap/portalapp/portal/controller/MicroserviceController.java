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

import java.util.List;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.WidgetMService;
import org.onap.portalapp.portal.service.MicroserviceService;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("unchecked")
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class MicroserviceController extends EPRestrictedBaseController {
	private final DataValidator dataValidator = new DataValidator();
	
	String whatService = "widgets-service";
	RestTemplate template = new RestTemplate();

	@Autowired
	private WidgetMService widgetMService;

	@Autowired
	private MicroserviceService microserviceService;

	@PostMapping(value = { "/portalApi/microservices" })
	public PortalRestResponse<String> createMicroservice(HttpServletRequest request, HttpServletResponse response,
			@Valid @RequestBody MicroserviceData newServiceData) throws Exception {
		if (newServiceData == null) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE",
				"MicroserviceData cannot be null or empty");
		}else {
			if(!dataValidator.isValid(newServiceData)){
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
					"ERROR", "MicroserviceData is not valid");
			}
		}
		long serviceId = microserviceService.saveMicroservice(newServiceData);

		try {
			microserviceService.saveServiceParameters(serviceId, newServiceData.getParameterList());
		} catch (Exception e) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}

		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}

	@GetMapping(value = { "/portalApi/microservices" })
	public List<MicroserviceData> getMicroservice(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return microserviceService.getMicroserviceData();
	}

	@PutMapping(value = { "/portalApi/microservices/{serviceId}" })
	public PortalRestResponse<String> updateMicroservice(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceId") long serviceId, @Valid @RequestBody MicroserviceData newServiceData) {

		if (newServiceData == null) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE",
				"MicroserviceData cannot be null or empty");
		}else {
			if(!dataValidator.isValid(newServiceData)){
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
					"ERROR", "MicroserviceData is not valid");
			}
		}
		try {
			microserviceService.updateMicroservice(serviceId, newServiceData);
		} catch (Exception e) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
	
	@DeleteMapping(value = { "/portalApi/microservices/{serviceId}" })
	public PortalRestResponse<String> deleteMicroservice(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceId") long serviceId) {
		try {
			ParameterizedTypeReference<List<WidgetCatalog>> typeRef = new ParameterizedTypeReference<List<WidgetCatalog>>() {
			};
			// If this service is assoicated with widgets, cannnot be deleted
			ResponseEntity<List<WidgetCatalog>> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://" + widgetMService.getServiceLocation(whatService, SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog/service/" + serviceId,
					HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), typeRef);
			List<WidgetCatalog> widgets = ans.getBody();
			if(widgets.size() == 0)
				microserviceService.deleteMicroservice(serviceId);
			else{
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < widgets.size(); i++){
					sb.append("'").append(widgets.get(i).getName()).append("' ");
					if(i < (widgets.size()-1)){
						sb.append(",");
					}
				}
				return new PortalRestResponse<>(PortalRestStatusEnum.WARN, "SOME WIDGETS ASSOICATE WITH THIS SERVICE",
					sb.toString());
			}
		} catch (Exception e) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}

}
