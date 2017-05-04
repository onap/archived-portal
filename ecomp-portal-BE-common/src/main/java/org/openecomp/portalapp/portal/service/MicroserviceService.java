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

import java.util.List;

import org.openecomp.portalapp.portal.domain.MicroserviceData;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;

public interface MicroserviceService {

	/**
	 * Get all microservices from the ep_microservice
	 * 
	 * @return list of all microservices
	 * @throws Exception
	 */
	List<MicroserviceData> getMicroserviceData() throws Exception;

	/**
	 * Gets the specified microservice with id from ep_microservice
	 * 
	 * @param id
	 *            ID of microservice to be fetched
	 * @return the microservice with the specified id
	 */
	MicroserviceData getMicroserviceDataById(long id);

	/**
	 * Saves the specified microservice to the table ep_microservice
	 * 
	 * @param newService
	 *            Content of microservice to be saved
	 * @return new microservice id
	 * @throws Exception
	 */
	Long saveMicroservice(MicroserviceData newService) throws Exception;

	void saveServiceParameters(long serviceId, List<MicroserviceParameter> list) throws Exception;

	/**
	 * Deletes the specified microservice from all tables where the serviceId is
	 * used
	 * 
	 * @param serviceId
	 * @throws Exception
	 */
	void deleteMicroservice(long serviceId) throws Exception;

	/**
	 * Updates the specified microservice from all tables where the serviceId is
	 * used
	 * 
	 * @param serviceId
	 *            Id of microservice to be updated
	 * @param newService
	 *            Content of microservice to be updated
	 * @throws Exception
	 */
	void updateMicroservice(long serviceId, MicroserviceData newService) throws Exception;
	
	/**
	 * Gets the Service parameters by the service Id
	 * @param serviceId
	 * @return
	 */
	List<MicroserviceParameter> getParametersById(long serviceId);

}
