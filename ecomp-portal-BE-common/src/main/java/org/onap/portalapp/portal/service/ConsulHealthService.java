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
package org.onap.portalapp.portal.service;

import java.util.List;

import org.onap.portalapp.portal.exceptions.NoHealthyServiceException;

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
