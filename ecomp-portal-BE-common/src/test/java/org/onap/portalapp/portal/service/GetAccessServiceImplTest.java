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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.GetAccessResult;
import org.onap.portalapp.portal.service.GetAccessServiceImpl;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class GetAccessServiceImplTest {

	
    @Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
    @Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    
    @InjectMocks
    GetAccessServiceImpl getAccessServiceImpl = new GetAccessServiceImpl();

    MockEPUser mockUser = new MockEPUser();
    
    @Test
	public void getAppAccessListTest()
	{
		EPUser user = mockUser.mockEPUser();
		Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		List<GetAccessResult> appAccessList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppAccessFunctionRole", params, null)).thenReturn(appAccessList);
		
		List<GetAccessResult> expectedAppAccessList = 	getAccessServiceImpl.getAppAccessList(user);
		assertEquals(expectedAppAccessList, appAccessList); 
	}
}
