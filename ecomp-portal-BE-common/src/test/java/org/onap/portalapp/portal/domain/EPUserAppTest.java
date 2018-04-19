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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUserApp;

public class EPUserAppTest {

	public EPUserApp mockEPUserApp(){
		
		EPApp epApp = new EPApp();
		epApp.setName("test");
		epApp.setImageUrl("test");
		epApp.setDescription("test");
		epApp.setNotes("test");
		epApp.setUrl("test");
		epApp.setAlternateUrl("test");
		epApp.setAppRestEndpoint("test");
		epApp.setMlAppName("test");
		epApp.setMlAppAdminId("test");
		epApp.setMotsId((long)1);
		epApp.setUsername("test");
		epApp.setAppPassword("test");
			
		
		//Role
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setActive(false);
		epRole.setPriority(1);
		epRole.setAppId((long)1);
		epRole.setAppRoleId((long)1);
		
		EPUserApp user = new EPUserApp();
		user.setUserId((long)1);
		user.setApp(epApp);
		user.setRole(epRole);
		user.setPriority((short)32767);
		
		
		return user;
	}
	
	@Test
	public void userTest(){
		EPUserApp user = mockEPUserApp();
		
		EPApp epApp = new EPApp();
		epApp.setName("test");
		epApp.setImageUrl("test");
		epApp.setDescription("test");
		epApp.setNotes("test");
		epApp.setUrl("test");
		epApp.setAlternateUrl("test");
		epApp.setAppRestEndpoint("test");
		epApp.setMlAppName("test");
		epApp.setMlAppAdminId("test");
		epApp.setMotsId((long)1);
		epApp.setUsername("test");
		epApp.setAppPassword("test");
		user.setApp(epApp);
		
		//Role
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setActive(false);
		epRole.setPriority(1);
		epRole.setAppId((long)1);
		epRole.setAppRoleId((long)1);
		
		
        assertEquals(user.getUserId(),Long.valueOf(1));
		assertEquals(user.getApp(), epApp); 
		assertEquals(user.getPriority().getClass(), Short.class);
	
		assertEquals(user.toString(), "[u: 1; a: null, r: null; appRoleId: 1]");
		
		assertEquals(user.hashCode(), user.hashCode());
		
		
		}
}
