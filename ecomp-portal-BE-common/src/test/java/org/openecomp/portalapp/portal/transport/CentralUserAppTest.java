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
package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openecomp.portalapp.portal.transport.CentralApp;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.transport.CentralUserApp;

public class CentralUserAppTest {

	public CentralUserApp mockCentralUserApp(){
		CentralUserApp centralUserApp = new CentralUserApp();
				
		CentralApp app = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralRole role = new CentralRole();
		 
		centralUserApp.setUserId((long)1);
		centralUserApp.setApp(app);
		centralUserApp.setRole(role);
		centralUserApp.setPriority((short) 123);
		
		return centralUserApp;
	}
	
	@Test
	public void centralUserAppTest(){
		CentralUserApp centralUserApp = mockCentralUserApp();
		
		CentralApp app1 = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralRole role1 = new CentralRole();
		
		assertEquals(centralUserApp.getUserId(), new Long(1));
		assertEquals(centralUserApp.getPriority(), new Short((short) 123));
		assertEquals(centralUserApp.getApp(), app1);
		assertEquals(centralUserApp.getRole(), role1);
	}
}
