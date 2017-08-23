package org.openecomp.portalapp.config;

import org.openecomp.portalapp.portal.service.EPRoleFunctionService;
import org.openecomp.portalapp.portal.service.EPRoleFunctionServiceCentralizedImpl;
import org.openecomp.portalapp.portal.service.EPRoleFunctionServiceImpl;
import org.openecomp.portalsdk.core.service.CentralAccessCondition;
import org.openecomp.portalsdk.core.service.LocalAccessCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortalCentralAccessConfiguration {

	
	   @Bean
	   @Conditional(LocalAccessCondition.class)
	   public EPRoleFunctionService ePRoleFunctionServiceImpl() {
	      return  new EPRoleFunctionServiceImpl();
	   }
	   
	  
	   @Bean
	   @Conditional(CentralAccessCondition.class)
	   public EPRoleFunctionService ePRoleFunctionServiceCentralizedImpl() {
	      return  new EPRoleFunctionServiceCentralizedImpl();
	   }
	   
}
