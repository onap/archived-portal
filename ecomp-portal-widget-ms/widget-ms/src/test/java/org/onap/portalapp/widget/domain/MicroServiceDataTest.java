/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.widget.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MicroServiceDataTest {
	
	private static final String TEST="test";
	
	@Test
	public void testMicorServiceData() {
		
		MicroserviceData microserviceData=buildData();
		assertEquals( new Long(1),microserviceData.getId());
		assertEquals( TEST,microserviceData.getName());
		assertEquals( TEST,microserviceData.getActive());
		assertEquals(TEST,microserviceData.getDesc());
		assertEquals(1l,microserviceData.getAppId());
		assertEquals(TEST,microserviceData.getUrl());
		assertEquals(TEST,microserviceData.getSecurityType());
		assertEquals(TEST,microserviceData.getUsername());
		assertEquals(TEST,microserviceData.getPassword());
		assertNotNull(microserviceData.toString());
		
	}

	public MicroserviceData buildData() {
		MicroserviceData microserviceData=new MicroserviceData();
		microserviceData.setId((long)1);
		microserviceData.setName(TEST);
		microserviceData.setActive(TEST);
		microserviceData.setDesc(TEST);
		microserviceData.setAppId((long)1);
		microserviceData.setUrl(TEST);
		microserviceData.setSecurityType(TEST);
		microserviceData.setUsername(TEST);
		microserviceData.setPassword(TEST);
		
		return microserviceData;
	}
}
