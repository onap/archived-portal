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
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.portalapp.portal.transport.EPUserAppCurrentRoles;

public class EPUserAppCurrentRolesTest {

	public EPUserAppCurrentRoles mockEPUserAppCurrentRoles(){
		EPUserAppCurrentRoles epUserAppCurrentRoles = new EPUserAppCurrentRoles();
			
		epUserAppCurrentRoles.setRoleName("test");
		epUserAppCurrentRoles.setUserId((long)1);
		epUserAppCurrentRoles.setPriority("test");
		epUserAppCurrentRoles.setRoleId((long)1);
		
		return epUserAppCurrentRoles;
	}
	
	@Test
	public void epUserAppCurrentRolesTest(){
		EPUserAppCurrentRoles epUserAppCurrentRoles = mockEPUserAppCurrentRoles();
		
		EPUserAppCurrentRoles epUserAppCurrentRoles1 = new EPUserAppCurrentRoles();
		
		epUserAppCurrentRoles1.setRoleName("test");
		epUserAppCurrentRoles1.setUserId((long)1);
		epUserAppCurrentRoles1.setPriority("test");
		epUserAppCurrentRoles1.setRoleId((long)1);
		
		assertEquals(epUserAppCurrentRoles.getRoleName(), "test");
		assertEquals(epUserAppCurrentRoles.getUserId(), new Long(1));
		assertEquals(epUserAppCurrentRoles.getRoleId(), new Long(1));
		assertEquals(epUserAppCurrentRoles.getPriority(), "test");
		assertEquals(epUserAppCurrentRoles.hashCode(), epUserAppCurrentRoles1.hashCode());
		assertTrue(epUserAppCurrentRoles.equals(epUserAppCurrentRoles1));
		
	}
}
