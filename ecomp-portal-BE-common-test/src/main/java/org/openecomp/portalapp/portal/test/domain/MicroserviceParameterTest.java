package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;

public class MicroserviceParameterTest {
	
	public MicroserviceParameter mockMicroserviceParameter(){
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
				
		microserviceParameter.setId((long)1);
		microserviceParameter.setServiceId((long)1);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		
		return microserviceParameter;
	}

	@Test
	public void microserviceParameterTest(){
		MicroserviceParameter microserviceParameter = mockMicroserviceParameter();
		assertEquals(microserviceParameter.getId(), new Long(1));
		assertEquals(microserviceParameter.getServiceId(),(long)1);
		assertEquals(microserviceParameter.getPara_key(), "test");
		assertEquals(microserviceParameter.getPara_value(), "test");
		
		assertEquals(microserviceParameter.toString(), "MicroserviceParameter [id=1, serviceId=1, para_key=test, para_value=test]");
	}
}
