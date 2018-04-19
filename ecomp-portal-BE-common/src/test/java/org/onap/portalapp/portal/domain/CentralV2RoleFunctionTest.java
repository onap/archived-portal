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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CentralV2RoleFunctionTest {

	public CentralV2RoleFunction mockCentralRoleFunction(){
		
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		   
		   centralV2RoleFunction.setCode("test");
		   centralV2RoleFunction.setName("test");
		   centralV2RoleFunction.setAppId((long)1);
		   centralV2RoleFunction.setEditUrl("test");
		   centralV2RoleFunction.setType("testType");
		   centralV2RoleFunction.setAction("testAction");
		   
		return centralV2RoleFunction;
	}
	
	@Test
	public void centralRoleFunctionTest(){
		CentralV2RoleFunction centralV2RoleFunction = mockCentralRoleFunction();
		
		assertEquals(centralV2RoleFunction.getAppId(), new Long(1));
		assertEquals(centralV2RoleFunction.getCode(), "test");
		assertEquals(centralV2RoleFunction.getName(), "test");
		assertEquals(centralV2RoleFunction.getEditUrl(), "test");
		assertEquals(centralV2RoleFunction.getAction(), "testAction");
		assertEquals(centralV2RoleFunction.getType(), "testType");
	}
}
