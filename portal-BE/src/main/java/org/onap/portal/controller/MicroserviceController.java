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
package org.onap.portal.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.onap.portal.domain.db.ep.EpMicroservice;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.ecomp.WidgetCatalog;
import org.onap.portal.restTemplates.PortalWMSTemplate;
import org.onap.portal.service.microservice.EpMicroserviceService;
import org.onap.portal.validation.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@Configuration
@EnableAspectJAutoProxy
public class MicroserviceController {

    private final DataValidator dataValidator = new DataValidator();

    private final PortalWMSTemplate template;
    private final EpMicroserviceService microserviceService;

    @Autowired
    public MicroserviceController(PortalWMSTemplate template, EpMicroserviceService microserviceService) {
        this.template = template;
        this.microserviceService = microserviceService;
    }

    @RequestMapping(value = {"/portalApi/microservices"}, method = RequestMethod.POST)
    public PortalRestResponse<String> createMicroservice(HttpServletRequest request, HttpServletResponse response,
        @Valid @RequestBody EpMicroservice newServiceData) {
        if (newServiceData == null) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE",
                "MicroserviceData cannot be null or empty");
        } else {
            if (!dataValidator.isValid(newServiceData)) {
                return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    "ERROR", "MicroserviceData is not valid");
            }
        }
        EpMicroservice serviceId = microserviceService.saveOne(newServiceData);
        try {
            microserviceService.saveServiceParameters(serviceId.getId(), newServiceData.getEpMicroserviceParameters());
        } catch (Exception e) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
    }

    @RequestMapping(value = {"/portalApi/microservices"}, method = RequestMethod.GET)
    public List<EpMicroservice> getMicroservice(HttpServletRequest request, HttpServletResponse response) {
        return microserviceService.getAll();
    }

    @RequestMapping(value = {"/portalApi/microservices/{serviceId}"}, method = RequestMethod.PUT)
    public PortalRestResponse<String> updateMicroservice(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("serviceId") long serviceId, @Valid @RequestBody EpMicroservice newServiceData) {

        if (newServiceData == null) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE",
                "MicroserviceData cannot be null or empty");
        } else {
            if (!dataValidator.isValid(newServiceData)) {
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

    @RequestMapping(value = {"/portalApi/microservices/{serviceId}"}, method = RequestMethod.DELETE)
    public PortalRestResponse<String> deleteMicroservice(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("serviceId") long serviceId) {
        try {
            ParameterizedTypeReference<List<WidgetCatalog>> typeRef = new ParameterizedTypeReference<List<WidgetCatalog>>() {
            };
            // If this service is assoicated with widgets, cannnot be deleted
            ResponseEntity<List<WidgetCatalog>> ans = template.getWidgets(serviceId, typeRef);
            List<WidgetCatalog> widgets = ans.getBody();
            if (widgets.size() == 0) {
                microserviceService.deleteById(serviceId);
            } else {
                String sb = widgets.stream().map(WidgetCatalog::getName).collect(Collectors.joining("' "));
                return new PortalRestResponse<>(PortalRestStatusEnum.WARN, "SOME WIDGETS ASSOICATE WITH THIS SERVICE",
                    sb);
            }
        } catch (Exception e) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
    }
}
