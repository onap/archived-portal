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
import org.onap.portalapp.portal.transport.RoleInAppForUser;

public class RoleInAppForUserTest {

	public RoleInAppForUser mockRoleInAppForUser(){
		RoleInAppForUser roleInAppForUser = new RoleInAppForUser((long)1 , "test");
		roleInAppForUser.setRoleId((long)1);
		roleInAppForUser.setRoleName("test");
		roleInAppForUser.setIsApplied(false);
		
		return roleInAppForUser;
	}
	
	@Test
	public void roleInAppForUserTest(){
		RoleInAppForUser roleInAppForUser = mockRoleInAppForUser();
		
		RoleInAppForUser roleInAppForUser1 = new RoleInAppForUser((long)1 , "test");
		roleInAppForUser1.setRoleId((long)1);
		roleInAppForUser1.setRoleName("test");
		roleInAppForUser1.setIsApplied(false);
		
		assertEquals(roleInAppForUser.getRoleId(), new Long(1));
		assertEquals(roleInAppForUser.getRoleName(), "test");
		assertEquals(roleInAppForUser.getIsApplied(), false);
		
		assertEquals(roleInAppForUser.toString(), "RoleInAppForUser [roleId=1, roleName=test, isApplied=false]");
		assertTrue(roleInAppForUser.equals(roleInAppForUser1));
		assertEquals(roleInAppForUser.hashCode(), roleInAppForUser1.hashCode());
		//constructor
	}
}
