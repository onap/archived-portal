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

import static org.junit.Assert.*;

import org.junit.Test;
import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;

public class AppNameIdIsAdminTest {

	public AppNameIdIsAdmin mockAppNameIdIsAdmin(){
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appNameIdIsAdmin.setId((long)1);
		appNameIdIsAdmin.setAppName("test");
		appNameIdIsAdmin.setRestrictedApp(false);
		appNameIdIsAdmin.setIsAdmin(false);
		return appNameIdIsAdmin;
	}
	
	@Test
	public void appNameIdIsAdminTest(){
		AppNameIdIsAdmin appNameIdIsAdmin = mockAppNameIdIsAdmin(); 
		
		AppNameIdIsAdmin appNameIdIsAdmin1 = new AppNameIdIsAdmin();
		appNameIdIsAdmin1.setId((long)1);
		appNameIdIsAdmin1.setAppName("test");
		appNameIdIsAdmin1.setRestrictedApp(false);
		appNameIdIsAdmin1.setIsAdmin(false);
		
		assertEquals(appNameIdIsAdmin.getId(), new Long(1));
		assertEquals(appNameIdIsAdmin.getAppName(), "test");
		assertEquals(appNameIdIsAdmin.getRestrictedApp(), false);
		assertEquals(appNameIdIsAdmin.getIsAdmin(), false);
		
		assertEquals(appNameIdIsAdmin.toString(), "AppNameIdIsAdmin [id=1, appName=test, isAdmin=false, restrictedApp=false]");
		assertEquals(appNameIdIsAdmin.hashCode(), appNameIdIsAdmin1.hashCode());
		assertTrue(appNameIdIsAdmin.equals(appNameIdIsAdmin1));
	}
}
