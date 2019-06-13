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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CentralUserAppTest {
	
	private static final Long ID=1l;
	private static final String TEST="test";
	
	private CentralUserApp buildCentralUserApp() {
		CentralUserApp app=new CentralUserApp();
		app.setUserId(ID);
		app.setPriority((Integer)1);
		CentralApp centralApp=new CentralApp();
		centralApp.setName(TEST);
		CentralRole role= new CentralRole.CentralRoleBuilder().createCentralRole();
		role.setName(TEST);
		app.setApp(centralApp);
		app.setRole(role);
		
		return app;
	}
	
	
	@Test
	public void test() {
		CentralUserApp app1=buildCentralUserApp();
		CentralUserApp app=new CentralUserApp();
		app.setApp(app1.getApp());
		app.setPriority(app1.getPriority());
		app.setRole(app1.getRole());
		app.setUserId(app1.getUserId());
		assertEquals(app.hashCode(), app1.hashCode());
		app.compareTo(app1);
		assertTrue(app.equals(app1));
		assertFalse(app.equals(null));
		app.setRole(null);
		assertFalse(app.equals(app1));
		app.setRole(app1.getRole());
		app.getRole().setName("test2");
		
		assertTrue(app.equals(app1));
		
	}

    public CentralV2UserApp mockCentralUserApp() {
        CentralV2UserApp centralV2UserApp = new CentralV2UserApp();

        CentralApp app = new CentralApp((long) 1, null, null, ID, ID, ID, TEST, TEST, TEST, TEST, TEST, TEST, TEST,
                TEST, TEST, TEST, TEST, TEST, TEST, null, TEST, TEST, TEST, TEST);

        CentralV2Role role = new CentralV2Role();

        centralV2UserApp.setUserId((long) 1);
        centralV2UserApp.setApp(app);
        centralV2UserApp.setRole(role);
        centralV2UserApp.setPriority((Integer) 123);

        return centralV2UserApp;
    }
	
	
	
	@Test
    public void centralUserAppTest() {
        CentralV2UserApp centralV2UserApp = mockCentralUserApp();

        CentralApp app1 = new CentralApp((long) 1, null, null, ID, ID, ID, TEST, TEST, TEST, TEST, TEST, TEST, TEST,
                TEST, TEST, TEST, TEST, TEST, TEST, null, TEST, TEST, TEST, TEST);


        CentralV2Role role1 = new CentralV2Role();

        assertEquals(centralV2UserApp.getUserId(), new Long(1));
        assertEquals(centralV2UserApp.getPriority(), new Integer((Integer) 123));
        assertEquals(centralV2UserApp.getApp(), app1);
        assertEquals(centralV2UserApp.getRole(), role1);
    }

    @Test
	public void centralUserAppEqualsTest(){
		CentralV2UserApp centralV2UserApp = mockCentralUserApp();
		CentralV2UserApp centralV2UserApp2 = mockCentralUserApp();

		assertTrue(centralV2UserApp.equals(centralV2UserApp));
		assertTrue(centralV2UserApp.equals(centralV2UserApp2));
		assertFalse(centralV2UserApp.equals(new Long(1)));
		centralV2UserApp2.setPriority(213);
		assertFalse(centralV2UserApp.equals(centralV2UserApp2));
	}
	
	@Test
	public void unt_hashCodeTest(){
		AppCatalogPersonalization appCatalogPersonalization=new AppCatalogPersonalization();
		appCatalogPersonalization.setAppId(123L);
		appCatalogPersonalization.setPending(true);
		appCatalogPersonalization.setSelect(true);
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		appCatalogPersonalization1.setAppId(123L);
		appCatalogPersonalization1.setPending(true);
		appCatalogPersonalization1.setSelect(true);
		assertEquals(appCatalogPersonalization.hashCode(), appCatalogPersonalization1.hashCode());
		assertTrue(appCatalogPersonalization.equals(appCatalogPersonalization1));
		
	}
}
