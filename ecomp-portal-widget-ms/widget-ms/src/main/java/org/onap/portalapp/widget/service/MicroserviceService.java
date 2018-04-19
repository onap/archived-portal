package org.onap.portalapp.widget.service;

import org.onap.portalapp.widget.domain.MicroserviceData;
import org.onap.portalapp.widget.domain.MicroserviceParameter;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from onap portal Backend to widget microservice
 */
public interface MicroserviceService {
	
	Long saveMicroserivce(MicroserviceData newService);
	
	void saveMicroserviceParameter(MicroserviceParameter newParameter);
	
	Long getMicroserviceIdByName(String newServiceName);
}
