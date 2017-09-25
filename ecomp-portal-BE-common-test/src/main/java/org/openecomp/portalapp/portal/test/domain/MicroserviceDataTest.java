package org.openecomp.portalapp.portal.test.domain;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.MicroserviceData;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;

public class MicroserviceDataTest {

	public MicroserviceData mockMicroserviceData(){
		
		MicroserviceData microserviceData= new MicroserviceData();
		
		List<MicroserviceParameter> parameterList = new ArrayList<MicroserviceParameter>();
		
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId((long)1);
		microserviceParameter.setServiceId((long)1);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		parameterList.add(microserviceParameter);
		
		microserviceData.setId((long)1);
		microserviceData.setName("test");
		microserviceData.setActive("test");
		microserviceData.setDesc("test");
		microserviceData.setAppId((long)1);
		microserviceData.setUrl("test");
		microserviceData.setSecurityType("test");
		microserviceData.setUsername("test");
		microserviceData.setPassword("test");
		
		
		return microserviceData;
	}
	
	@Test
	public void microserviceDataTest(){
		MicroserviceData microserviceData= mockMicroserviceData();
		
		assertEquals(microserviceData.getId(), new Long(1));
		assertEquals(microserviceData.getName(), "test");
		assertEquals(microserviceData.getActive(), "test");
		assertEquals(microserviceData.getDesc(), "test");
	//	assertEquals(microserviceData.getAppId(), new long(1));
		assertEquals(microserviceData.getUrl(), "test");
		assertEquals(microserviceData.getSecurityType(), "test");
		assertEquals(microserviceData.getUsername(), "test");
		assertEquals(microserviceData.getPassword(), "test");
		
		assertEquals(microserviceData.toString(), "MicroserviceData [id=1, name=test, desc=test, appId=1, "
				+ "url=test, securityType=test, username=test, password=test, parameterList=null]");
	}
}
