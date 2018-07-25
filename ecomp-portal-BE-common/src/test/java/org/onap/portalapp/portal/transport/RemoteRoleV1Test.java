/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
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

//@RunWith(PowerMockRunner.class)
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RemoteRoleV1Test {
	RemoteRoleV1 remoteRoleV1;
	private String NAME = "test";
    private Long ID = 12L;
    
	@Before
	public void setUp() {
		remoteRoleV1 = new RemoteRoleV1();
		remoteRoleV1.setName(NAME);
		remoteRoleV1.setId(ID);
	}
	
	
	@Test
	public void testNotNull() {
		assertNotNull(remoteRoleV1);
	}
	
	@Test
	public void testRemoteRoleV1Properties() {
		assertEquals(ID, remoteRoleV1.getId());
		assertEquals(NAME, remoteRoleV1.getName());
	}
	
	@Test
	public void testRemoteRole() {
		RemoteRoleV1 remoteRoleV1=buildRemoteRoleV1();
		RemoteRoleV1 remoteRole=new RemoteRoleV1();
		RemoteRoleV1 remoteRoleV2=remoteRoleV1;
		remoteRole.setId(ID);
		remoteRole.setName(NAME);
		
		assertEquals(remoteRole.hashCode(), remoteRoleV1.hashCode());	
		remoteRole.compareTo(remoteRoleV1);
		assertTrue(remoteRole.equals(remoteRoleV1));
		assertFalse(remoteRole.equals(null));
		remoteRole.setName(null);
		assertFalse(remoteRole.equals(remoteRoleV1));
		remoteRole.setId(null);
		assertFalse(remoteRole.equals(remoteRoleV1));
		assertTrue(remoteRoleV2.equals(remoteRoleV1));
		
	}
	
	private RemoteRoleV1 buildRemoteRoleV1() {
		
		RemoteRoleV1 remoteRole=new RemoteRoleV1();
		remoteRole.setId(ID);
		remoteRole.setName(NAME);
		return remoteRole;
	}
	
	
	
	@Test
	public void remoteRoleV1Test(){
		
		RemoteRoleV1 remoteRoleV11 = new RemoteRoleV1();
		RemoteRoleV1 remoteRoleV12 = new RemoteRoleV1();
		RemoteRoleV1 remoteRoleV13 = new RemoteRoleV1();
		remoteRoleV13.setId(12L);
		remoteRoleV13.setName("test");
		
		assertEquals(new Long(12), remoteRoleV1.getId());
		assertEquals(remoteRoleV1.getName(), "test");
		assertEquals(remoteRoleV1.hashCode(), remoteRoleV13.hashCode());
		
		assertTrue(remoteRoleV11.equals(new RemoteRoleV1()));
		assertTrue(remoteRoleV12.equals(new RemoteRoleV1()));
	}
	
	@Test
	public void testcompareTo() {
		assertEquals(0, remoteRoleV1.compareTo(remoteRoleV1));
	}
	
}