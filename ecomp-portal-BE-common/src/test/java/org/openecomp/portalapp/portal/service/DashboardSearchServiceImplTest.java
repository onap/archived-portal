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
package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.ecomp.model.SearchResultItem;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.CommonWidget;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.service.DataAccessService;

public class DashboardSearchServiceImplTest {
	
	@Mock
	DataAccessService dataAccessService;
	
	@InjectMocks
	DashboardSearchServiceImpl dashboardSearchServiceImpl = new DashboardSearchServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	MockEPUser mockUser = new MockEPUser();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
    
	@Test
	public void searchResultsTest()
	{
		Map<String, String> params = new HashMap<>();
		params.put("userId", "guestT");
		params.put("searchQuery", "test");
		
		List<SearchResultItem> list = new ArrayList<>();
		SearchResultItem searchResultItem= new SearchResultItem();
		searchResultItem.setCategory("test");
		list.add(searchResultItem);
		Mockito.when(dataAccessService.executeNamedQuery("searchPortal", params, null)).thenReturn(list);
		Map<String, List<SearchResultItem>> result  =	dashboardSearchServiceImpl.searchResults("guestT", "test");
		assertTrue(result.keySet().contains("test"));
	}
	
	@Test
	public void getRelatedUsersTest()
	{
		List<String> activeUsers = new ArrayList<>();
		Map<String, String> params = new HashMap<>();
		params.put("userId", "guestT");
		Mockito.when(dataAccessService.executeNamedQuery("relatedUsers", params, null)).thenReturn(activeUsers);
		List<String> expectedActiveUsers  =	dashboardSearchServiceImpl.getRelatedUsers("guestT");
		assertEquals(expectedActiveUsers,activeUsers);
	}
	@Test
	public void getWidgetDataTest()
	{
		CommonWidgetMeta CommonWidgetMeta = null;
		Map<String, String> params = new HashMap<>();
		params.put("cat", "test");
		@SuppressWarnings("unchecked")
		List<CommonWidget> widgetItems = new ArrayList<>();
		CommonWidget commonWidget = new CommonWidget();
		widgetItems.add(commonWidget);
		Mockito.when(dataAccessService.executeNamedQuery("getCommonWidgetItem", params, null)).thenReturn(widgetItems);
		CommonWidgetMeta expectedCommonWidgetMeta =dashboardSearchServiceImpl.getWidgetData("test");
		assertEquals(expectedCommonWidgetMeta.getCategory(), "test");
	}
	
	@Test
	public void saveWidgetDataBulkTest()
	{
		CommonWidgetMeta CommonWidgetMeta = new CommonWidgetMeta();
		List<CommonWidget> widgetList = new ArrayList<>();
		CommonWidget commonWidget = new CommonWidget();
		widgetList.add(commonWidget);
		CommonWidgetMeta.setItems(widgetList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(commonWidget, null);
        assertEquals(dashboardSearchServiceImpl.saveWidgetDataBulk(CommonWidgetMeta), "success");
	}
	
	@Test
	public void saveWidgetDataTest()
	{
		CommonWidget commonWidget = new CommonWidget();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(commonWidget, null);
		 assertEquals(dashboardSearchServiceImpl.saveWidgetData(commonWidget), "success");
	}
	
	@Test
	public void deleteWidgetDataTest()
	{
		CommonWidget commonWidget = new CommonWidget();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(commonWidget, null);
		 assertEquals(dashboardSearchServiceImpl.deleteWidgetData(commonWidget), "success");
	}
}
