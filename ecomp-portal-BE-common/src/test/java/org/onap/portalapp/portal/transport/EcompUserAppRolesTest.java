
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
 */package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.onap.portalapp.portal.transport.EcompUserAppRoles;

public class EcompUserAppRolesTest {

	public EcompUserAppRoles mockEcompUserAppRoles(){
		EcompUserAppRoles ecompUserAppRoles = new EcompUserAppRoles();
			
		ecompUserAppRoles.setAppId("test");
		ecompUserAppRoles.setUserId((long)1);
		ecompUserAppRoles.setPriority((Integer) 123);
		ecompUserAppRoles.setRoleId((long)1);
		ecompUserAppRoles.setRoleName("test");
		
		return ecompUserAppRoles;
	}
	
	@Test
	public void ecompUserAppRolesTest(){
		
		EcompUserAppRoles ecompUserAppRoles = mockEcompUserAppRoles();
		
		assertEquals(ecompUserAppRoles.getAppId(), "test");
		assertEquals(ecompUserAppRoles.getPriority(), new Integer((Integer) 123));
		assertEquals(ecompUserAppRoles.getRoleName(), "test");
		assertEquals(ecompUserAppRoles.getUserId(), new Long(1));
		assertEquals(ecompUserAppRoles.getRoleId(), new Long(1));
	}
}
