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

import org.openecomp.portalapp.portal.exceptions.NoHealthyServiceException;
import com.ecwid.consul.ConsulException;
import com.orbitz.consul.model.health.ServiceHealth;

public interface ConsulHealthService {
	/**
	 * This method returns the location of one healthy node if found in Consul -
	 * If not found in / by Consul, it falls back to 'localhost'
	 * 
	 * @param service
	 * @param fallbackPortOnLocalhost
	 *            value provided by the calling service
	 * @return Service location
	 */
	public String getServiceLocation(String service, String fallbackPortOnLocalhost) throws NoHealthyServiceException;

	public List<ServiceHealth> getAllHealthyNodes(String service) throws ConsulException;

	public List<ServiceHealth> getAllNodes(String service) throws ConsulException;
}
