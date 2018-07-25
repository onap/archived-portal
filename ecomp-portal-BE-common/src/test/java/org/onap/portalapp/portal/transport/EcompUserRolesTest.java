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
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

public class EcompUserRolesTest {
	private static final String TEST="test";
	private static final Long ID=1l;
	private static final Date DATE=new Date();
	
	@Test
	public void testUserRole() {
		
		EcompUserRoles userRole=buildEcompUserRole();
		assertEquals(TEST, userRole.getManagerId());
		assertEquals(TEST, userRole.getFirstName());
		
		assertEquals(TEST, userRole.getMiddleInitial());
		assertEquals(TEST, userRole.getLastName());
		
		assertEquals(ID, userRole.getOrgId());
		assertEquals(TEST, userRole.getPhone());
		assertEquals(TEST, userRole.getFunctionCode());
		assertEquals(TEST, userRole.getFunctionName());
		assertEquals(TEST, userRole.getOrgManagerUserId());
		assertEquals(TEST, userRole.getOrgUserId());
		assertEquals(TEST, userRole.getOrgCode());
		assertEquals(TEST, userRole.getJobTitle());
		assertEquals(TEST, userRole.getLoginId());
		assertEquals(TEST, userRole.getEmail());
		assertEquals(TEST, userRole.getHrid());
		assertEquals(true, userRole.isActive());
		assertEquals(TEST, userRole.getRoleName());
		assertEquals(ID, userRole.getRoleId());
		
		assertNotNull(userRole.toString());
		
	}
	
	
	
	private EcompUserRoles buildEcompUserRole() {
		
		EcompUserRoles userRole=new EcompUserRoles();
		
		userRole.setOrgId(ID);
		userRole.setManagerId(TEST);
		userRole.setFirstName(TEST);
		userRole.setMiddleInitial(TEST);
		userRole.setLastName(TEST);
		userRole.setPhone(TEST);
		userRole.setOrgManagerUserId(TEST);
		userRole.setOrgCode(TEST);
		userRole.setJobTitle(TEST);
		userRole.setLoginId(TEST);
		userRole.setFunctionCode(TEST);
		userRole.setFunctionName(TEST);
		userRole.setEmail(TEST);
		userRole.setHrid(TEST);
		userRole.setOrgUserId(TEST);
		userRole.setHrid(TEST);
		userRole.setActive(true);
		userRole.setRoleId(ID);
		userRole.setRoleName(TEST);
		
		
	return userRole;
	}
	

}
