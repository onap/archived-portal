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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.onap.portalapp.portal.transport.CentralApp;

public class CentralAppTest {

	public CentralApp mockCentralApp(){
		CentralApp centralApp = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
				return centralApp;
	}
	
	@Test
	public void centralAppTest(){
		CentralApp centralApp = mockCentralApp();
		
		CentralApp centralApp1 =  new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		assertEquals(centralApp.getId(), new Long(1));
		assertEquals(centralApp.getCreatedId(), new Long(1));
		assertEquals(centralApp.getModifiedId(), new Long(1));
		assertEquals(centralApp.getRowNum(), new Long(1));
		assertEquals(centralApp.getName(), "test");
		assertEquals(centralApp.getImageUrl(), "test");
		assertEquals(centralApp.getDescription(), "test");
		assertEquals(centralApp.getNotes(), "test");
		assertEquals(centralApp.getUrl(), "test");
		assertEquals(centralApp.getAlternateUrl(), "test");
		assertEquals(centralApp.getRestEndpoint(), "test");
		assertEquals(centralApp.getMlAppName(), "test");
		assertEquals(centralApp.getMlAppAdminId(), "test");
		assertEquals(centralApp.getMotsId(), "test");
		assertEquals(centralApp.getAppPassword(), "test");
		assertEquals(centralApp.getOpen(), "test");
		assertEquals(centralApp.getEnabled(), "test");
		assertEquals(centralApp.getUsername(), "test");
		assertEquals(centralApp.getUebKey(), "test");
		assertEquals(centralApp.getUebSecret(), "test");
		assertEquals(centralApp.getUebTopicName(), "test");
		
		assertTrue(centralApp.equals(centralApp1));
		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
	}
	
}
