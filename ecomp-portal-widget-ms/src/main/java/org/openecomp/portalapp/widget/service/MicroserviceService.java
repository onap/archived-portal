package org.openecomp.portalapp.widget.service;

import org.openecomp.portalapp.widget.domain.MicroserviceData;
import org.openecomp.portalapp.widget.domain.MicroserviceParameter;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from ecomp portal Backend to widget microservice
 */
public interface MicroserviceService {
	
	Long saveMicroserivce(MicroserviceData newService);
	
	void saveMicroserviceParameter(MicroserviceParameter newParameter);
	
	Long getMicroserviceIdByName(String newServiceName);
}
