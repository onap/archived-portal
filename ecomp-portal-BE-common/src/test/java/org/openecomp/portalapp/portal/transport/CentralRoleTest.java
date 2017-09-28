/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CentralRoleTest {

	public CentralRole mockCentralRole(){
		CentralRole centralRole = new CentralRole((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
	    
	    centralRole.setId((long)1);
	    centralRole.setCreatedId((long)1);
	    centralRole.setModifiedId((long)1);
	    centralRole.setRowNum((long)1);
	    centralRole.setName("test");
	    centralRole.setActive(false);
	    centralRole.setPriority(1);
	    
	    centralRole.setCreated(null);
	    centralRole.setModified(null);
	    centralRole.setRoleFunctions(null);
	    centralRole.setChildRoles(null);
	    centralRole.setParentRoles(null);
	    
		return centralRole;
	}
	
	@Test
	public void centralRoleTest(){
		CentralRole centralRole = mockCentralRole();
		
		CentralRole centralRole1 = new CentralRole((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
		
		CentralRole centralRole2 = new CentralRole((long)1, "test");
		
		assertEquals(centralRole.getId(), new Long(1));
		assertEquals(centralRole.getCreatedId(), new Long(1));
		assertEquals(centralRole.getModifiedId(), new Long(1));
		assertEquals(centralRole.getRowNum(), new Long(1));
		assertEquals(centralRole.getName(), "test");
		assertEquals(centralRole.isActive(), false);
		assertEquals(centralRole.getCreated(), null);
		assertEquals(centralRole.getModified(), null);
		assertEquals(centralRole.getRoleFunctions(), null);
		assertEquals(centralRole.getChildRoles(), null);
		assertEquals(centralRole.getParentRoles(), null);		
		assertEquals(centralRole.getPriority().toString(), "1");
		assertEquals(centralRole.hashCode(), centralRole1.hashCode());
		assertTrue(centralRole.equals(centralRole1));
		assertEquals(centralRole, centralRole1);
		assertEquals(centralRole2, new CentralRole((long)1, "test"));
	}
	
	
}
