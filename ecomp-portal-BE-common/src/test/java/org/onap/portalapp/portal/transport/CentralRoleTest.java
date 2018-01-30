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

public class CentralRoleTest {

	public CentralV2Role mockCentralRole(){
		CentralV2Role centralV2Role = new CentralV2Role((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
	    
	    centralV2Role.setId((long)1);
	    centralV2Role.setCreatedId((long)1);
	    centralV2Role.setModifiedId((long)1);
	    centralV2Role.setRowNum((long)1);
	    centralV2Role.setName("test");
	    centralV2Role.setActive(false);
	    centralV2Role.setPriority(1);
	    
	    centralV2Role.setCreated(null);
	    centralV2Role.setModified(null);
	    centralV2Role.setRoleFunctions(null);
	    centralV2Role.setChildRoles(null);
	    centralV2Role.setParentRoles(null);
	    
		return centralV2Role;
	}
	
	@Test
	public void centralRoleTest(){
		CentralV2Role centralV2Role = mockCentralRole();
		
		CentralV2Role centralRole1 = new CentralV2Role((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
		
		CentralV2Role centralRole2 = new CentralV2Role((long)1, "test");
		
		assertEquals(centralV2Role.getId(), new Long(1));
		assertEquals(centralV2Role.getCreatedId(), new Long(1));
		assertEquals(centralV2Role.getModifiedId(), new Long(1));
		assertEquals(centralV2Role.getRowNum(), new Long(1));
		assertEquals(centralV2Role.getName(), "test");
		assertEquals(centralV2Role.isActive(), false);
		assertEquals(centralV2Role.getCreated(), null);
		assertEquals(centralV2Role.getModified(), null);
		assertEquals(centralV2Role.getRoleFunctions(), null);
		assertEquals(centralV2Role.getChildRoles(), null);
		assertEquals(centralV2Role.getParentRoles(), null);		
		assertEquals(centralV2Role.getPriority().toString(), "1");
		assertEquals(centralV2Role.hashCode(), centralRole1.hashCode());
		assertTrue(centralV2Role.equals(centralRole1));
		assertEquals(centralV2Role, centralRole1);
		assertEquals(centralRole2, new CentralV2Role((long)1, "test"));
	}
	
	
}
