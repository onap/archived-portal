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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.onap.portalapp.portal.transport.CentralApp;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.CentralV2UserApp;

public class CentralUserAppTest {

	public CentralV2UserApp mockCentralUserApp(){
		CentralV2UserApp centralV2UserApp = new CentralV2UserApp();
				
		CentralApp app = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralV2Role role = new CentralV2Role();
		 
		centralV2UserApp.setUserId((long)1);
		centralV2UserApp.setApp(app);
		centralV2UserApp.setRole(role);
		centralV2UserApp.setPriority((short) 123);
		
		return centralV2UserApp;
	}
	
	@Test
	public void centralUserAppTest(){
		CentralV2UserApp centralV2UserApp = mockCentralUserApp();
		
		CentralApp app1 = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralV2Role role1 = new CentralV2Role();
		
		assertEquals(centralV2UserApp.getUserId(), new Long(1));
		assertEquals(centralV2UserApp.getPriority(), new Short((short) 123));
		assertEquals(centralV2UserApp.getApp(), app1);
		assertEquals(centralV2UserApp.getRole(), role1);
	}
}
