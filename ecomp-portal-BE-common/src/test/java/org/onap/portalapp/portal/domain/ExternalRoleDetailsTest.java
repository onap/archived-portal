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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPAppRoleFunction;
import org.onap.portalapp.portal.domain.ExternalRoleDetails;

public class ExternalRoleDetailsTest {

	public ExternalRoleDetails mockExternalRoleDetails(){
		
		List<EPAppRoleFunction> epAppRoleFunctionList = new ArrayList<EPAppRoleFunction>();
		
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setRoleId((long)1);
		epAppRoleFunction.setAppId((long)1);
		epAppRoleFunction.setCode("test");
		
		epAppRoleFunctionList.add(epAppRoleFunction);
		
		ExternalRoleDetails externalRoleDetails = new ExternalRoleDetails();
		
		externalRoleDetails.setName("test");
		externalRoleDetails.setActive(false);
		externalRoleDetails.setPriority(1);
		externalRoleDetails.setAppId((long)1);
		externalRoleDetails.setAppRoleId((long)1);
		externalRoleDetails.setPerms(epAppRoleFunctionList);
		
		return externalRoleDetails;
	}
	
	@Test
	public void externalRoleDetailsTest(){
		
		List<EPAppRoleFunction> epAppRoleFunctionList = new ArrayList<EPAppRoleFunction>();
		
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setRoleId((long)1);
		epAppRoleFunction.setAppId((long)1);
		epAppRoleFunction.setCode("test");
		
		epAppRoleFunctionList.add(epAppRoleFunction);
		
		ExternalRoleDetails externalRoleDetails = mockExternalRoleDetails();
		
		assertEquals(externalRoleDetails.getAppId(), new Long(1));
		assertEquals(externalRoleDetails.getAppRoleId(), new Long(1));
		assertEquals(externalRoleDetails.getPriority().toString(), "1");
		assertEquals(externalRoleDetails.getName(), "test");
		assertEquals(externalRoleDetails.getPriority().toString(),  "1");
		assertEquals(externalRoleDetails.getPerms().size(), epAppRoleFunctionList.size());
		
	}
}
