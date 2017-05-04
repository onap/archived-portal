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

import org.springframework.stereotype.Component;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import com.ecwid.consul.ConsulException;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;

@Component
public class ConsulHealthServiceImpl implements ConsulHealthService {
	
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ConsulHealthServiceImpl.class);

	@Override
	public String getServiceLocation(String service){

		List<ServiceHealth> nodes = null;
		
		try{
			Consul consul = Consul.builder().build();
			HealthClient healthClient = consul.healthClient();
			nodes = healthClient.getHealthyServiceInstances(service).getResponse();
		}
		catch(Exception e){
			//using both loggers.
			logger.debug(logger.debugLogger, " problem getting nodes for service - " + service + e.getMessage() + " - Defaulting to localhost", e);
			logger.error(logger.errorLogger, " problem getting nodes for service - " + service + e.getMessage() + " - Defaulting to localhost", e);

			return "localhost:" + SystemProperties.getProperty("microservices.widget.local.port");
		}
		
		if(nodes == null || nodes.size() == 0){
			logger.debug(logger.debugLogger, "No healthy node found in the consul cluster running service " + service + ". Defaulting to localhost");
			return "localhost:" + SystemProperties.getProperty("microservices.widget.local.port");
		}
		else{
			String locationFromConsul;
			ServiceHealth node = nodes.get(0);
			locationFromConsul = node.getNode().getNode()  + ":" + node.getService().getPort();
			logger.debug(logger.debugLogger, "Found healthy service location using consul - returning location " + locationFromConsul);
			
			//if locationFromConsul is null for some reason (very unlikely at this point), default to localhost		
			if(null == locationFromConsul || "".equals(locationFromConsul)){
				logger.debug(logger.debugLogger, "Couldn't get location from consul for service " + service + ". Defaulting to localhost");
				return "localhost:" + SystemProperties.getProperty("microservices.widget.local.port");
			}
			else{
				logger.debug(logger.debugLogger, "Found service location from consul for service " + service + ". Location is " + locationFromConsul);
				return locationFromConsul;
			}			
		}		
	}

	@Override
	public List<ServiceHealth> getAllHealthyNodes(String service) throws ConsulException{
		Consul consul = Consul.builder().build();
		HealthClient healthClient = consul.healthClient();
		return healthClient.getHealthyServiceInstances(service).getResponse();
	}

	@Override
	public List<ServiceHealth> getAllNodes(String service){
		Consul consul = Consul.builder().build();
		HealthClient healthClient = consul.healthClient();
		return healthClient.getAllServiceInstances(service).getResponse();
	}
}
