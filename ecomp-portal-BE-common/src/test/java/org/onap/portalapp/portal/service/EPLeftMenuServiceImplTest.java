/*
* ============LICENSE_START=======================================================
* ONAP  PORTAL
* ================================================================================
* Copyright 2018 TechMahindra
*=================================================================================
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* ============LICENSE_END=========================================================
*/
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalsdk.core.domain.MenuData;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class EPLeftMenuServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	@Mock
	 ExternalAccessRolesService externalAccessRolesService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	EPLeftMenuServiceImpl epLeftMenuServiceImpl = new EPLeftMenuServiceImpl();
	
	@Test
	public void getLeftMenuItemsTest() {
		Map<String, JSONObject> defaultNavMap = new LinkedHashMap<String, JSONObject>();
		Set<MenuData> fullMenuSet = new HashSet<>();
		Set<String> roleFunctionSet = new HashSet<>();
		defaultNavMap.clear();
		JSONObject navItemsDetails1 = new JSONObject();
		navItemsDetails1.put("name", "test");
		navItemsDetails1.put("state", "demo");
		navItemsDetails1.put("imageSrc", "img");
		defaultNavMap.put("root.applicationsHome", navItemsDetails1);

		JSONObject navItemsDetails2 = new JSONObject();
		navItemsDetails2.put("name", "test1");
		navItemsDetails2.put("state", "demo1");
		navItemsDetails2.put("imageSrc", "img1");
		defaultNavMap.put("root.appCatalog", navItemsDetails2);

		JSONObject navItemsDetails3 = new JSONObject();
		navItemsDetails3.put("name", "test2");
		navItemsDetails3.put("state", "demo2");
		navItemsDetails3.put("imageSrc", "img2");
		defaultNavMap.put("root.widgetCatalog", navItemsDetails3);
		
		List<CentralizedApp> applicationsList = new ArrayList<>();
		EPUser epUser = new EPUser();
		epUser.setOrgUserId("userId");
		Mockito.when((List<CentralizedApp>)externalAccessRolesService.getCentralizedAppsOfUser("userId")).thenReturn(applicationsList);
		
		MenuData data = new MenuData();
		data.setLabel("labelTest");
		data.setImageSrc("imgTest");
		data.setAction("actionTest");
		JSONObject navItemsDetails = new JSONObject();
		navItemsDetails.put("name", "labelTest");
		navItemsDetails.put("state", "actionTest");
		navItemsDetails.put("imageSrc", "imgTest");
		defaultNavMap.put("actionTest", navItemsDetails);
		
		JSONObject sidebarModel = new JSONObject();
		JSONArray navItems = new JSONArray();
		Collection<JSONObject> jsonObjs = defaultNavMap.values();
		navItems.put(navItemsDetails3);
		sidebarModel.put("label", "ECOMP portal");
		sidebarModel.put("navItems", navItems);
		
		epLeftMenuServiceImpl.getLeftMenuItems(epUser, fullMenuSet, roleFunctionSet);
		
		
	}
}
