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

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalsdk.core.domain.MenuData;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class EPLeftMenuServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	
	@Mock
	ExternalAccessRolesServiceImpl externalAccessRolesServiceImpl = new ExternalAccessRolesServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	EPLeftMenuServiceImpl epLeftMenuServiceImpl = new EPLeftMenuServiceImpl();
	
	
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void getLeftMenuItemsTest() {
		EPUser user = mockUser.mockEPUser();
		Set<MenuData> fullMenuSet = new TreeSet<>();
		MenuData menuData =  new MenuData();
		menuData.setAction("test");
		menuData.setFunctionCd("test_1");
		menuData.setActive(true);
		menuData.setExternalUrl("test");
		menuData.setId(1l);
		menuData.setMenuSetCode("test");
		menuData.setSortOrder((short) 1);
		menuData.setSeparator(true);
		fullMenuSet.add(menuData);
		Set<String> roleFunctionSet = new TreeSet<>();
		roleFunctionSet.add("test");
		roleFunctionSet.add("test2");
		Map<String, String> params = new HashMap<>();
		params.put("userId", user.getOrgUserId());
		List<CentralizedApp> applicationsList = new ArrayList<>();
		List<CentralizedApp> applicationsList2 = new ArrayList<>();
		CentralizedApp centralizedApp = new CentralizedApp();
		centralizedApp.setAppId(1);
		centralizedApp.setAppName("test");
		applicationsList.add(centralizedApp);
		applicationsList2.add(centralizedApp);
		Mockito.when(dataAccessService.executeNamedQuery(Matchers.anyString(), Matchers.anyMap(), Matchers.anyMap())).thenReturn(applicationsList);
		Mockito.when(externalAccessRolesServiceImpl.getCentralizedAppsOfUser(Matchers.anyString())).thenReturn(applicationsList2);
		String actual = epLeftMenuServiceImpl.getLeftMenuItems(user, fullMenuSet, roleFunctionSet);
		JSONObject notExpected = new JSONObject();
		assertNotEquals(notExpected.toString(), actual);
	}


}
