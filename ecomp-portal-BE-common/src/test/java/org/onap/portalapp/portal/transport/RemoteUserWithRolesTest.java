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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onap.portalapp.portal.transport.RemoteRole;
import org.onap.portalapp.portal.transport.RemoteUserWithRoles;

public class RemoteUserWithRolesTest {

	public RemoteUserWithRoles mockRemoteUserWithRoles(){
		RemoteUserWithRoles remoteUserWithRoles = new RemoteUserWithRoles();
		
		List<RemoteRole> roles = new ArrayList<RemoteRole>();
		RemoteRole remoteRole = new RemoteRole();
		remoteRole.setId((long)1);
		remoteRole.setName("test");
		roles.add(remoteRole);
		
		remoteUserWithRoles.setOrgId((long)1);
		remoteUserWithRoles.setManagerId((long)1);
		remoteUserWithRoles.setFirstName("test");
		remoteUserWithRoles.setMiddleInitial("test");
		remoteUserWithRoles.setLastName("test");
		remoteUserWithRoles.setPhone("test");
		remoteUserWithRoles.setEmail("test");
		remoteUserWithRoles.setHrid("test");
		remoteUserWithRoles.setOrgUserId("test");
		remoteUserWithRoles.setOrgCode("test");
		remoteUserWithRoles.setOrgManagerUserId("test");
		remoteUserWithRoles.setJobTitle("test");
		remoteUserWithRoles.setLoginId("test");
		remoteUserWithRoles.setActive(false);
		remoteUserWithRoles.setRoles(roles);
		
		return remoteUserWithRoles;
	}
	
	@Test
	public void remoteUserWithRolesTest(){
		RemoteUserWithRoles remoteUserWithRoles = mockRemoteUserWithRoles();
		
		assertEquals(remoteUserWithRoles.getOrgId(), new Long(1));
		assertEquals(remoteUserWithRoles.getManagerId(), new Long(1));
		assertEquals(remoteUserWithRoles.getFirstName(), "test");
		assertEquals(remoteUserWithRoles.getMiddleInitial(), "test");
		assertEquals(remoteUserWithRoles.getLastName(), "test");
		assertEquals(remoteUserWithRoles.getPhone(), "test");
		assertEquals(remoteUserWithRoles.getEmail(), "test");
		assertEquals(remoteUserWithRoles.getHrid(), "test");
		assertEquals(remoteUserWithRoles.getOrgUserId(), "test");
		assertEquals(remoteUserWithRoles.getOrgCode(), "test");
		assertEquals(remoteUserWithRoles.getOrgManagerUserId(), "test");
		assertEquals(remoteUserWithRoles.getJobTitle(), "test");
		assertEquals(remoteUserWithRoles.getLoginId(), "test");
		assertEquals(remoteUserWithRoles.getActive(), false);
		assertEquals(remoteUserWithRoles.getRoles().size(), 1);
		assertEquals(remoteUserWithRoles.toString(), "RemoteUserWithRoles [orgId=1, managerId=1, firstName=test, middleInitial=test, lastName=test, phone=test, "
				+ "email=test, hrid=test, orgUserId=test, orgCode=test, orgManagerUserId=test, jobTitle=test, loginId=test, active=false, roles=[RemoteRole [id=1, name=test]]]");
		
	}
}
