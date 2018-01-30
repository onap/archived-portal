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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import javax.persistence.Column;
import javax.persistence.Id;

import org.junit.Test;
import org.onap.portalapp.portal.domain.AdminUserApp;
import org.onap.portalapp.portal.domain.AdminUserApplications;

public class AdminUserApplicationsTest {

	public AdminUserApplications mockAdminUserApplications(){
		
		AdminUserApp adminUserApp = new AdminUserApp();
		
		adminUserApp.setUserId((long)1);
		adminUserApp.setFirstName("test");
		adminUserApp.setLastName("test");
		adminUserApp.setOrgUserId("test");
		adminUserApp.setAppId((long)1);
		adminUserApp.setAppName("test");
		
		AdminUserApplications adminUserApplications = new AdminUserApplications(adminUserApp);
		
		adminUserApplications.setUser_Id((long)1);
		adminUserApplications.setFirstName("test");
		adminUserApplications.setLastName("test");
		adminUserApplications.setOrgUserId("test");	
		return adminUserApplications;
	}
	
	@Test
	public void adminUserAppTest(){
		AdminUserApplications adminUserApplications = mockAdminUserApplications();
	    AdminUserApp adminUserApp = new AdminUserApp();
		
		adminUserApp.setUserId((long)1);
		adminUserApp.setFirstName("test");
		adminUserApp.setLastName("test");
		adminUserApp.setOrgUserId("test");
		adminUserApp.setAppId((long)1);
		adminUserApp.setAppName("test");
		AdminUserApplications adminUserApplications1 = new AdminUserApplications(adminUserApp);
		
		assertEquals(adminUserApplications.getUser_Id(), new Long(1));
		assertEquals(adminUserApplications.getFirstName(), "test");
		assertEquals(adminUserApplications.getLastName(), "test");
		assertEquals(adminUserApplications.getOrgUserId(), "test");
		
		
		assertEquals(adminUserApplications1.getApps().get(0).getAppId(),adminUserApp.getAppId());
		assertEquals(adminUserApplications1.getApps().get(0).getAppName(),adminUserApp.getAppName());
		
	}
}
