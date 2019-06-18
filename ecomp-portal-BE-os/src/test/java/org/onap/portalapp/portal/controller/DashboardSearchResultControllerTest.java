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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.DashboardSearchResultController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.ecomp.model.SearchResultItem;
import org.onap.portalapp.portal.framework.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.DashboardSearchService;
import org.onap.portalapp.portal.transport.CommonWidget;
import org.onap.portalapp.portal.transport.CommonWidgetMeta;
import org.onap.portalapp.util.EPUserUtils;

public class DashboardSearchResultControllerTest {

	@Mock
	DashboardSearchService searchService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	DashboardSearchResultController dashboardSearchResultController = new DashboardSearchResultController();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getWidgetDataTest() {
		PortalRestResponse<CommonWidgetMeta> ecpectedPortalRestResponse = new PortalRestResponse<CommonWidgetMeta>();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(new CommonWidgetMeta());
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		CommonWidgetMeta commonWidgetMeta = new CommonWidgetMeta();
		Mockito.when(searchService.getWidgetData("test")).thenReturn(commonWidgetMeta);
		PortalRestResponse<CommonWidgetMeta> actualPortalRestResponse = dashboardSearchResultController
				.getWidgetData(mockedRequest, "test");
		assertEquals(ecpectedPortalRestResponse.getStatus(), actualPortalRestResponse.getStatus());
	}

	@Test
	public void getWidgetDataXSSTest() {
		String resourceType = "\"<IMG SRC=\\\"jav\\tascript:alert('XSS');\\\">\"";
		PortalRestResponse expectedPortalRestResponse = new PortalRestResponse<>();
		expectedPortalRestResponse.setMessage("Provided data is invalid");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(searchService.getWidgetData(resourceType)).thenReturn(null);
		PortalRestResponse acutualPoratlRestResponse = dashboardSearchResultController
			.getWidgetData(mockedRequest, resourceType);
		assertEquals(acutualPoratlRestResponse, expectedPortalRestResponse);
	}

	@Test
	public void saveWidgetDataBulkIfCatrgoryNullTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("ERROR");
		ecpectedPortalRestResponse.setResponse("Category cannot be null or empty");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidgetMeta commonWidgetMeta = new CommonWidgetMeta();
		commonWidgetMeta.setCategory(null);
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataBulkTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		CommonWidgetMeta commonWidgetMeta = new CommonWidgetMeta();
		commonWidgetMeta.setCategory("test");
		List<CommonWidget> items = new ArrayList<>();

		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setId((long) 1);
		commonWidget.setEventDate("2017-06-06");
		items.add(commonWidget);
		commonWidgetMeta.setItems(items);
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataBulkExceptionTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("ERROR");
		ecpectedPortalRestResponse.setResponse("Category is not valid");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidgetMeta commonWidgetMeta = new CommonWidgetMeta();
		commonWidgetMeta.setCategory("test");
		List<CommonWidget> items = new ArrayList<>();

		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setId((long) 1);
		commonWidget.setEventDate("date");
		items.add(commonWidget);
		commonWidgetMeta.setItems(items);
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataBulkXSSTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<>();
		ecpectedPortalRestResponse.setMessage("ERROR");
		ecpectedPortalRestResponse.setResponse("Category is not valid");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);

		CommonWidgetMeta commonWidgetMeta = new CommonWidgetMeta();
		commonWidgetMeta.setCategory("test");

		List<CommonWidget> commonWidgetList = new ArrayList<>();
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setId((long) 1);
		commonWidget.setCategory("test");
		commonWidget.setHref("\"<IMG SRC=\\\"jav\\tascript:alert('XSS');\\\">\"");
		commonWidget.setTitle("test_title");
		commonWidget.setContent("test_content");
		commonWidget.setEventDate(null);
		commonWidget.setSortOrder(1);

		commonWidgetList.add(commonWidget);

		commonWidgetMeta.setItems(commonWidgetList);

		Mockito.when(searchService.saveWidgetDataBulk(commonWidgetMeta)).thenReturn(null);

		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
			.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataXSSTest() {
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<>();
		expectedPortalRestResponse.setMessage("ERROR");
		expectedPortalRestResponse.setResponse("Category is not valid");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setId((long) 1);
		commonWidget.setCategory("test");
		commonWidget.setHref("\"<IMG SRC=\"jav\\tascript:alert('XSS');\">\"");
		commonWidget.setTitle("test_title");
		commonWidget.setContent("test_content");
		commonWidget.setEventDate(null);
		commonWidget.setSortOrder(1);

		Mockito.when(searchService.saveWidgetData(commonWidget)).thenReturn(null);

		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
			.saveWidgetData(commonWidget);
		assertEquals(expectedPortalRestResponse, actualPortalRestResponse);

	}

	@Test
	public void deleteWidgetDataXSSTest() {
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<>();
		expectedPortalRestResponse.setMessage("ERROR");
		expectedPortalRestResponse.setResponse("Data is not valid");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setId((long) 1);
		commonWidget.setCategory("test");
		commonWidget.setHref("test_href");
		commonWidget.setTitle("\"<IMG SRC=\"jav\\tascript:alert('XSS');\">\"");
		commonWidget.setContent("test_content");
		commonWidget.setEventDate(null);
		commonWidget.setSortOrder(1);
		Mockito.when(searchService.deleteWidgetData(commonWidget)).thenReturn(null);

		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
			.deleteWidgetData(commonWidget);

		assertEquals(expectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataIfCatagoryNullTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("ERROR");
		ecpectedPortalRestResponse.setResponse("Cateogry cannot be null or empty");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setCategory(null);
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetData(commonWidget);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setCategory("test");
		commonWidget.setId((long) 1);
		commonWidget.setEventDate("2017-06-06");
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetData(commonWidget);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveWidgetDataExceptionTest() {
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("ERROR");
		ecpectedPortalRestResponse.setResponse("Category is not valid");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		CommonWidget commonWidget = new CommonWidget();
		commonWidget.setCategory("test");
		commonWidget.setId((long) 1);
		commonWidget.setEventDate("date");
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.saveWidgetData(commonWidget);
		assertEquals(ecpectedPortalRestResponse, actualPortalRestResponse);
	}
	
	@Test
	public void deleteWidgetDataTest()
	{
		PortalRestResponse<String> ecpectedPortalRestResponse = new PortalRestResponse<String>();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		CommonWidget commonWidget = new CommonWidget();
		Mockito.when(searchService.deleteWidgetData(commonWidget)).thenReturn("test");
		PortalRestResponse<String> actualPortalRestResponse = dashboardSearchResultController
				.deleteWidgetData(commonWidget);
		assertEquals(ecpectedPortalRestResponse.getStatus(), actualPortalRestResponse.getStatus());
	}
	
	@Test
	public void searchPortalIfUserIsTest()
	{
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String searchString = "test";
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("searchPortal: User object is null? - check logs");
		expectedResult.setResponse(new HashMap<String, List<SearchResultItem>>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardSearchResultController
				.searchPortal(mockedRequest, searchString);
		System.out.println(actualResult);

		assertEquals(expectedResult, actualResult);
	}
	
	
	@Test
	public void getActiveUsersTest() {
		List<String> expectedActiveUsers = new ArrayList<String>();
		EPUser user = mockUser.mockEPUser();
		;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String userId = user.getOrgUserId();
		Mockito.when(searchService.getRelatedUsers(userId)).thenReturn(expectedActiveUsers);
		List<String> actualOnlineUsers = dashboardSearchResultController.getActiveUsers(mockedRequest);
		assertEquals(expectedActiveUsers, actualOnlineUsers);

	}

	@Test
	public void getActiveUsersExceptionTest() {
		List<String> expectedActiveUsers = new ArrayList<String>();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String userId = user.getOrgUserId();
		Mockito.when(searchService.getRelatedUsers(userId)).thenThrow(nullPointerException);
		List<String> actualOnlineUsers = dashboardSearchResultController.getActiveUsers(mockedRequest);
		assertEquals(expectedActiveUsers, actualOnlineUsers);

	}

	@Test
	public void activeUsersTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<List<String>> expectedResult = new PortalRestResponse<List<String>>();
		expectedResult.setMessage("success");
		expectedResult.setResponse(new ArrayList<>());
		expectedResult.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<List<String>> actualResult = dashboardSearchResultController.activeUsers(mockedRequest);

		assertEquals(actualResult, expectedResult);

	}

	@Test
	public void activeUsersIfUserNullTest() {
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<List<String>> expectedResult = new PortalRestResponse<List<String>>();
		expectedResult.setMessage("User object is null? - check logs");
		expectedResult.setResponse(new ArrayList<>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<List<String>> actualResult = dashboardSearchResultController.activeUsers(mockedRequest);
		assertEquals(actualResult, expectedResult);

	}

	@Test
	public void activeUsersExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<List<String>> expectedResult = new PortalRestResponse<List<String>>();
		expectedResult.setMessage("null - check logs.");
		expectedResult.setResponse(new ArrayList<>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(searchService.getRelatedUsers(user.getLoginId())).thenThrow(nullPointerException);
		PortalRestResponse<List<String>> actualResult = dashboardSearchResultController.activeUsers(mockedRequest);
		assertEquals(actualResult, expectedResult);

	}
	
	@Test
	public void searchPortalIfSearchStringNullTest() {
		EPUser user = mockUser.mockEPUser();
		;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String searchString = null;

		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("searchPortal: String string is null");
		expectedResult.setResponse(new HashMap<String, List<SearchResultItem>>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);

		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardSearchResultController
				.searchPortal(mockedRequest, searchString);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void searchPortalIfSearchTest() {
		EPUser user = mockUser.mockEPUser();
		;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String searchString = "test";
		List<SearchResultItem> searchResultItemList = new ArrayList<SearchResultItem>();
		SearchResultItem searchResultItem = new SearchResultItem();

		searchResultItem.setId((long) 1);
		searchResultItem.setCategory("test");
		searchResultItem.setName("test_name");
		searchResultItem.setTarget("test_target");
		searchResultItem.setUuid("test_UUId");
		searchResultItemList.add(searchResultItem);
		Map<String, List<SearchResultItem>> expectedResultMap = new HashMap<String, List<SearchResultItem>>();
		expectedResultMap.put(searchString, searchResultItemList);

		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("success");
		expectedResult.setResponse(expectedResultMap);
		expectedResult.setStatus(PortalRestStatusEnum.OK);

		Mockito.when(searchService.searchResults(user.getLoginId(), searchString)).thenReturn(expectedResultMap);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardSearchResultController
				.searchPortal(mockedRequest, searchString);
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void searchPortalXSS() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String searchString = "<script>alert(“XSS”)</script> ";

		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("searchPortal: User object is invalid");
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);

		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardSearchResultController
			.searchPortal(mockedRequest, searchString);
		assertEquals(actualResult, expectedResult);

	}

	@Test
	public void searchPortalIfSearchExcptionTest() {
		EPUser user = mockUser.mockEPUser();
		;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String searchString = "test";

		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("null - check logs.");
		expectedResult.setResponse(new HashMap<String, List<SearchResultItem>>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);

		Mockito.when(searchService.searchResults(user.getLoginId(), searchString)).thenThrow(nullPointerException);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardSearchResultController
				.searchPortal(mockedRequest, searchString);
		assertEquals(expectedResult, actualResult);
	}

}
