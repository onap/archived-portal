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

import java.util.ArrayList;

import org.junit.Test;
import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;
import org.onap.portalapp.portal.transport.AppsListWithAdminRole;

public class AppsListWithAdminRoleTest {
	
	public AppsListWithAdminRole mockAppsListWithAdminRole(){
		AppsListWithAdminRole appsListWithAdminRole = new AppsListWithAdminRole();
		
		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<AppNameIdIsAdmin>();
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appsRoles.add(appNameIdIsAdmin);
		
		appsListWithAdminRole.setOrgUserId("test");
		appsListWithAdminRole.setAppsRoles(appsRoles);
		
		return appsListWithAdminRole;
	}

	@Test
	public void appsListWithAdminRoleTest(){
		AppsListWithAdminRole appsListWithAdminRole = mockAppsListWithAdminRole();
		
		AppsListWithAdminRole appsListWithAdminRole1 = new AppsListWithAdminRole();
		
		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<AppNameIdIsAdmin>();
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appsRoles.add(appNameIdIsAdmin);
		
		appsListWithAdminRole1.setOrgUserId("test");
		appsListWithAdminRole1.setAppsRoles(appsRoles);
				
		assertEquals(appsListWithAdminRole.getOrgUserId(), "test");
		assertEquals(appsListWithAdminRole.getAppsRoles(), appsRoles);
		
		assertTrue(appsListWithAdminRole.equals(appsListWithAdminRole1));
		assertEquals(appsListWithAdminRole.hashCode(), appsListWithAdminRole1.hashCode());
		assertEquals(appsListWithAdminRole.toString(), "AppsListWithAdminRole [orgUserId=test, appsRoles=[AppNameIdIsAdmin [id=null, appName=null, isAdmin=null, restrictedApp=null]]]");
	}
}
