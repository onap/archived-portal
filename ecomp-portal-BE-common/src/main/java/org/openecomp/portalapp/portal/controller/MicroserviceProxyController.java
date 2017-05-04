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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPUnRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.WidgetParameterResult;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.MicroserviceProxyService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class MicroserviceProxyController extends EPUnRestrictedBaseController {

	@Autowired
	private MicroserviceProxyService microserviceProxyService;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceProxyController.class);

	@RequestMapping(value = { "/portalApi/microservice/proxy/{serviceId}" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public String getMicroserviceProxy(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceId") long serviceId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		String answer = "";
		try{
			answer = microserviceProxyService.proxyToDestination(serviceId, user, request);
		}catch(HttpClientErrorException e){
			//Check whether the error message is valid JSON format
			boolean valid = true;
			ObjectMapper objectMapper = new ObjectMapper();
		    try{ 
		        objectMapper.readTree(e.getResponseBodyAsString());
		    } catch(JsonProcessingException exception){
		        valid = false;
		    }
		    if(valid)
		    	return e.getResponseBodyAsString();
		    else
		    	return "{\"error\":\""+ e.getResponseBodyAsString() +"\"}";
		}
		return answer;		
	}
	
	@RequestMapping(value = { "/portalApi/microservice/proxy/parameter/{widgetId}" }, method = {
			RequestMethod.GET }, produces = "application/json")
	public String getMicroserviceProxyByWidgetId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		String answer = "";
		try{
			answer = microserviceProxyService.proxyToDestinationByWidgetId(widgetId, user, request);
		}catch(HttpClientErrorException e){
			//Check whether the error message is valid JSON format
			boolean valid = true;
			ObjectMapper objectMapper = new ObjectMapper();
		    try{ 
		        objectMapper.readTree(e.getResponseBodyAsString());
		    } catch(JsonProcessingException exception){
		        valid = false;
		    }
		    if(valid)
		    	return e.getResponseBodyAsString();
		    else
		    	return "{\"error\":\""+ e.getResponseBodyAsString() +"\"}";
		}
		return answer;		
	}
}
