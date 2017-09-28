/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.domain;

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
