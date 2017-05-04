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
package org.openecomp.portalapp.portal.service;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.springframework.web.client.RestClientException;

public interface MicroserviceProxyService {

	/**
	 * Gets the specific microservice from table ep_microservice, communicates
	 * to microservice with the specified security type. The microservice sends
	 * back the response.
	 * 
	 * Gets the data while testing the microservice with no widget associated with
	 * 
	 * @param serviceId
	 *            Id of microservice to be used
	 * @return response sent from microservice
	 * @throws Exception
	 */
	String proxyToDestination(long serviceId, EPUser user, HttpServletRequest request) throws Exception;
	
	
	
	/**
	 * Gets the microservice data based on the user id and widget id. Different
	 * users have his/her own parameters for one widget. The method sends back
	 * the response.
	 * 
	 * @param widgetId
	 * 			  Id of widget to be used
	 * @param user
	 * 			  User information
	 * @param request
	 * @return response sent from microserivce 
	 * @throws Exception
	 */
	String proxyToDestinationByWidgetId(long widgetId, EPUser user, HttpServletRequest request) throws Exception;
}
