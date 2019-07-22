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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.ecomp.model.SearchResultItem;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.DashboardSearchService;
import org.onap.portalapp.portal.service.DashboardSearchServiceImpl;
import org.onap.portalapp.portal.transport.CommonWidget;
import org.onap.portalapp.portal.transport.CommonWidgetMeta;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.support.CollaborateList;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;


@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, CollaborateList.class, SystemProperties.class, EPCommonSystemProperties.class})
public class DashboardControllerTest {
	
	@Mock
	DashboardSearchService searchService = new DashboardSearchServiceImpl();

	@InjectMocks
	DashboardController dashboardController;

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();
	
	@Autowired
	AuditService auditService;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	public CommonWidgetMeta mockCommonWidgetMeta() {
		CommonWidgetMeta commonWidgetMeta= new CommonWidgetMeta();
		List<CommonWidget> widgetList = new ArrayList<>();
		CommonWidget commonWidget = new CommonWidget();		
		commonWidget.setId((long) 1);
		commonWidget.setCategory("test");
		commonWidget.setHref("testhref");
		commonWidget.setTitle("testTitle");
	    commonWidget.setContent("testcontent");
	    commonWidget.setEventDate("2017-03-24");
	    commonWidget.setSortOrder(1);		    
		widgetList.add(commonWidget);		
		commonWidgetMeta.setItems(widgetList);
		
		return commonWidgetMeta;
	}
	
	public CommonWidget mockCommonWidget() {
		
		CommonWidget commonWidget = new CommonWidget();		
		commonWidget.setId((long) 1);
		commonWidget.setCategory("test");
		commonWidget.setHref("testhref");
		commonWidget.setTitle("testTitle");
	    commonWidget.setContent("testcontent");
	    commonWidget.setEventDate("testDate");
	    commonWidget.setSortOrder(1);
	    
	    return commonWidget;
	}
	
	
	@Test
	public void getWidgetDataTest() throws IOException {
		
		String resourceType = null;
		PortalRestResponse<CommonWidgetMeta> expectedData = new PortalRestResponse<CommonWidgetMeta>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Unexpected resource type null");
		expectedData.setResponse(null);
		
		PortalRestResponse<CommonWidgetMeta> actualResponse = 	dashboardController.getWidgetData(mockedRequest, resourceType);
		assertEquals(expectedData,actualResponse);		
	}

	@Test
	public void getWidgetDataTestXSS() {

		String resourceType = "“><script>alert(“XSS”)</script>";
		PortalRestResponse<CommonWidgetMeta> expectedData = new PortalRestResponse<>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Unexpected resource type “><script>alert(“XSS”)</script>");
		expectedData.setResponse(null);

		PortalRestResponse<CommonWidgetMeta> actualResponse = dashboardController.getWidgetData(mockedRequest, resourceType);
		assertEquals(expectedData, actualResponse);
	}

	@Test
	public void getWidgetDataWithValidResourceTest() throws IOException {
		String resourceType = "EVENTS";
		CommonWidgetMeta commonWidgetMeta= mockCommonWidgetMeta();
		commonWidgetMeta.setCategory(null);
				
		Mockito.when(searchService.getWidgetData(resourceType)).thenReturn(commonWidgetMeta);
		PortalRestResponse<CommonWidgetMeta> expectedData = new PortalRestResponse<CommonWidgetMeta>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse(commonWidgetMeta);
		
		PortalRestResponse<CommonWidgetMeta> actualResponse = dashboardController.getWidgetData(mockedRequest, resourceType);
		assertEquals(expectedData,actualResponse);
	}
		
	@Test
	public void saveWidgetDataBulkNullTest() throws IOException {
		CommonWidgetMeta commonWidgetMeta= mockCommonWidgetMeta();
		commonWidgetMeta.setCategory(null);
		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("ERROR");
		expectedData.setResponse("Category cannot be null or empty");
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(expectedData,actualResponse);		
	}

	@Test
	public void saveWidgetDataBulkXSSTest() {
		CommonWidgetMeta commonWidgetMeta= mockCommonWidgetMeta();
		commonWidgetMeta.setCategory("<script>alert(‘XSS’)</script>");

		PortalRestResponse<String> expectedData = new PortalRestResponse<>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setResponse("ERROR");
		expectedData.setMessage("Unsafe resource type " + commonWidgetMeta.toString());

		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(expectedData,actualResponse);
	}
	
	@Test
	public void saveWidgetUnexpectedDataBulkTest() throws IOException {
		CommonWidgetMeta commonWidgetMeta= mockCommonWidgetMeta();
		commonWidgetMeta.setCategory("Unexpected Data");
		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Unexpected resource type Unexpected Data");
		expectedData.setResponse(null);
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(expectedData,actualResponse);
		
	}
		
	@Test
	public void saveWidgetInvalidDataBulkTest() throws IOException {
		CommonWidgetMeta commonWidgetMeta= mockCommonWidgetMeta();
		commonWidgetMeta.setCategory("EVENTS");
		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Invalid category: test");
		expectedData.setResponse(null);
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(expectedData,actualResponse);		
	}
	
	@Test
	public void saveWidgetDataBulkTest() throws IOException {
		
		List<CommonWidget> widgetList = new ArrayList<>();		
		CommonWidget commonWidget = new CommonWidget("EVENTS", "http://test.com", "testTitle", "testcontent", "2017-07-01", 1);
		widgetList.add(commonWidget);
		CommonWidgetMeta commonWidgetMeta= new CommonWidgetMeta("EVENTS", widgetList);
		commonWidgetMeta.setItems(widgetList);		
		commonWidgetMeta.setCategory("EVENTS");		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse("success");
		
		Mockito.when(searchService.saveWidgetDataBulk(commonWidgetMeta)).thenReturn("success");
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetDataBulk(commonWidgetMeta);
		assertEquals(expectedData,actualResponse);		
	}
	
	@Test
	public void saveWidgetDataNullTest() throws IOException {
				
		CommonWidget commonWidget = mockCommonWidget();	
		commonWidget.setId((long)1);
		commonWidget.setContent("test");
		commonWidget.setCategory(null);
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("ERROR");
		expectedData.setResponse("Category cannot be null or empty");

		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget, mockedRequest, mockedResponse);
		assertEquals(expectedData,actualResponse);
		
	}

	@Test
	public void saveWidgetDataXSSTest() {

		CommonWidget commonWidget = mockCommonWidget();
		commonWidget.setId((long)1);
		commonWidget.setContent("test");
		commonWidget.setCategory("<form><a href=\"javascript:\\u0061lert&#x28;1&#x29;\">X");
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setResponse("ERROR");
		expectedData.setMessage("Unsafe resource type " + commonWidget.toString());

		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget, mockedRequest, mockedResponse);
		assertEquals(expectedData,actualResponse);

	}
	
	@Test
	public void saveWidgetDataTitleTest() throws IOException {				
		CommonWidget commonWidget = mockCommonWidget();	
		commonWidget.setId((long)1);
		commonWidget.setContent("test");
		commonWidget.setTitle("test");
		commonWidget.setEventDate("2017-05-06");
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Invalid category: test");
		expectedData.setResponse(null);
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget, mockedRequest, mockedResponse);
		assertEquals(expectedData.getMessage(),actualResponse.getMessage());
	}
	
	@Test
	public void saveWidgetDataErrorTest() throws IOException {
				
		CommonWidget commonWidget = mockCommonWidget();
		commonWidget.setEventDate("2017-03-05");
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Invalid category: test");
		expectedData.setResponse(null); 
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget,mockedRequest, mockedResponse);
		assertEquals(expectedData,actualResponse);		
	}
	
	@Test
	public void saveWidgetDataTest() throws IOException {
				
		CommonWidgetMeta commonWidgetMeta= new CommonWidgetMeta();
		List<CommonWidget> widgetList = new ArrayList<>();
		CommonWidget commonWidget = new CommonWidget();		
		commonWidget.setId((long) 1);
		commonWidget.setCategory("EVENTS");
		commonWidget.setHref("http://test.com");
		commonWidget.setTitle("testTitle");
	    commonWidget.setContent("testcontent");
	    commonWidget.setEventDate("2017-07-01");
	    commonWidget.setSortOrder(1);		    
		widgetList.add(commonWidget);		
		commonWidgetMeta.setItems(widgetList);
		
		commonWidgetMeta.setCategory("EVENTS");
		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse("success"); 
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(searchService.saveWidgetData(commonWidget)).thenReturn("success");
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget, mockedRequest, mockedResponse);
		assertEquals(expectedData,actualResponse);
		
	}
	
	@Test
	public void deleteWidgetDataTest() throws IOException {
				
		CommonWidget commonWidget = mockCommonWidget();
		commonWidget.setEventDate("2017-03-25");
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse(null); 
		
		Mockito.when(searchService.saveWidgetData(commonWidget)).thenReturn("success");
		
		PortalRestResponse<String> actualResponse = dashboardController.deleteWidgetData(commonWidget);
		assertEquals(expectedData,actualResponse);
		
	}

	@Test
	public void deleteWidgetDataXSSTest() {

		CommonWidget commonWidget = mockCommonWidget();
		commonWidget.setCategory("<svg><script x:href='https://dl.dropbox.com/u/13018058/js.js' {Opera}");
		PortalRestResponse<String> expectedData = new PortalRestResponse<>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Unsafe resource type " + commonWidget.toString());
		expectedData.setResponse("ERROR");
		PortalRestResponse<String> actualResponse = dashboardController.deleteWidgetData(commonWidget);
		assertEquals(expectedData,actualResponse);

	}
		
	@Test
	public void getActiveUsersTest(){
		List<String> activeUsers = new ArrayList<>();
		List<String> expectedUsersList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String userId = user.getOrgUserId();
		Mockito.when(searchService.getRelatedUsers(userId)).thenReturn(activeUsers);
		expectedUsersList= 	dashboardController.getActiveUsers(mockedRequest);
		assertEquals(expectedUsersList, activeUsers);
	}
	
	
	@Test
	public void getActiveUsersExceptionTest(){
		List<String> activeUsers = new ArrayList<>();
		List<String> expectedUsersList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String userId = user.getOrgUserId();
		Mockito.when(searchService.getRelatedUsers(userId)).thenThrow(nullPointerException);
		expectedUsersList = dashboardController.getActiveUsers(mockedRequest);
		assertEquals(expectedUsersList, activeUsers);
	}
		
	@Test
	public void getOnlineUserUpdateRateTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse("{onlineUserUpdateRate=1400000, onlineUserUpdateDuration=1400000}"); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_RATE)).thenReturn("1400"); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_DURATION)).thenReturn("1400");
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getOnlineUserUpdateRate(mockedRequest);
		assertEquals(expectedData.getStatus(),actualResponse.getStatus());
	}
	
	@Test
	public void getOnlineUserUpdateRateExceptionTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("java.lang.NullPointerException");
		expectedData.setResponse(null); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_RATE)).thenThrow(nullPointerException); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_DURATION)).thenThrow(nullPointerException);
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getOnlineUserUpdateRate(mockedRequest);
		assertEquals(expectedData,actualResponse);
	}
	
	@Test
	public void getWindowWidthThresholdForRightMenuTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse("{windowWidth=1400}"); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_RIGHT_MENU)).thenReturn("1400");
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getWindowWidthThresholdForRightMenu(mockedRequest);
		assertEquals(expectedData.getStatus(),actualResponse.getStatus());
	}
	
	@Test
	public void getWindowWidthThresholdForRightMenuExceptionTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("java.lang.NullPointerException");
		expectedData.setResponse(null); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_RIGHT_MENU)).thenThrow(nullPointerException);
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getWindowWidthThresholdForRightMenu(mockedRequest);
		assertEquals(expectedData,actualResponse);
	}
	
	@Test
	public void getWindowWidthThresholdForLeftMenuTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");		
		expectedData.setResponse("{windowWidth=1400}"); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_LEFT_MENU)).thenReturn("1400");
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getWindowWidthThresholdForLeftMenu(mockedRequest);
		assertEquals(expectedData.getStatus(),actualResponse.getStatus());
	}
	
	@Test
	public void getWindowWidthThresholdForLeftMenuExceptionTest(){
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("java.lang.NullPointerException");
		expectedData.setResponse(null); 
		
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_LEFT_MENU)).thenThrow(nullPointerException);
		
		PortalRestResponse<Map<String, String>> actualResponse = dashboardController.getWindowWidthThresholdForLeftMenu(mockedRequest);
		assertEquals(expectedData,actualResponse);
	}
		
	@Test
	public void getActiveUsersNullTest(){
		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("User object is null? - check logs");
		expectedData.setResponse(new ArrayList<>()); 

		PortalRestResponse<List<String>> actualResponse = dashboardController.activeUsers(mockedRequest);
		assertEquals(expectedData,actualResponse);
	}
	
	@Test
	public void activeUsersTest(){
		EPUser user = mockUser.mockEPUser();
		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse(new ArrayList<>()); 
		PowerMockito.mockStatic(EPUserUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<List<String>> actualResponse = dashboardController.activeUsers(mockedRequest);
		assertEquals(expectedData,actualResponse);
	}
	
	@Test
	public void activeUsersExceptionTest(){
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("test");
		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("null - check logs.");
		expectedData.setResponse(null);  
		
		PowerMockito.mockStatic(EPUserUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(searchService.getRelatedUsers(user.getLoginId())).thenThrow(nullPointerException);
		PortalRestResponse<List<String>> actualResponse = dashboardController.activeUsers(mockedRequest);
		assertTrue(actualResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
	}
	@Test
	public void searchPortalTestWhenSearchStringIsNull(){
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("test");
		user.setId(1L);
		String searchString = null;
		//user.setLoginId("test");
		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("null - check logs.");
		expectedData.setResponse(Matchers.any());  
		
		PowerMockito.mockStatic(EPUserUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("null - check logs.");
		expectedResult.setResponse(null);
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		//Mockito.doNothing().when(auditService).logActivity(auditLog, null);

		//Mockito.when(auditService.logActivity(auditLog, null).;
		//Mockito.when(searchService.searchResults(user.getLoginId(), searchString )).thenReturn((Map<String, List<SearchResultItem>>) expectedResult);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResponse = dashboardController.searchPortal(mockedRequest, null);
		assertTrue(actualResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
	}
	
	@Test
	public void searchPortalTest(){
		EPUser user = null;
		String searchString = null;
		//user.setLoginId("test");
		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("null - check logs.");
		expectedData.setResponse(Matchers.any());  
		
		PowerMockito.mockStatic(EPUserUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("null - check logs.");
		expectedResult.setResponse(null);
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		//Mockito.doNothing().when(auditService).logActivity(auditLog, null);

		//Mockito.when(auditService.logActivity(auditLog, null).;
		//Mockito.when(searchService.searchResults(user.getLoginId(), searchString )).thenReturn((Map<String, List<SearchResultItem>>) expectedResult);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResponse = dashboardController.searchPortal(mockedRequest, null);
		assertTrue(actualResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
	}

	@Test
	public void searchPortalXSSTest(){
		EPUser user = null;
		String searchString = "\n"
			+ "<form><textarea &#13; onkeyup='\\u0061\\u006C\\u0065\\u0072\\u0074&#x28;1&#x29;'>";
		PowerMockito.mockStatic(EPUserUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<>();
		expectedResult.setMessage("searchPortal: String string is not safe");
		expectedResult.setResponse(new HashMap<>());
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);

		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResponse = dashboardController.searchPortal(mockedRequest, searchString);
		assertEquals(expectedResult, actualResponse);
	}

	@Test
	public void searchPortalTestWithException(){
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("test");
		user.setId(1L);
		
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
		
		AuditLog auditLog = new AuditLog();
		auditLog.setUserId(1L);
		auditLog.setActivityCode("test");
		auditLog.setComments("test");
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedResult = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedResult.setMessage("null - check logs.");
		expectedResult.setResponse(null);
		expectedResult.setStatus(PortalRestStatusEnum.ERROR);
		//Mockito.doNothing().when(auditService).logActivity(auditLog, null);

		//Mockito.when(auditService.logActivity(auditLog, null).;
		Mockito.when(searchService.searchResults(user.getLoginId(), searchString)).thenReturn(expectedResultMap);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualResult = dashboardController.searchPortal(mockedRequest, searchString);
				
		assertTrue(expectedResult.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);

	}
	
	@Test
	public void searchPortalUserNullTest(){
		EPUser user = null;
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedData = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedData.setMessage("searchPortal: User object is null? - check logs");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);		
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualData = dashboardController.searchPortal(mockedRequest, null);
		assertEquals(actualData.getMessage(), expectedData.getMessage());
	}
	
	@Test
	public void searchPortalsearchStringNullTest(){
		EPUser user = mockUser.mockEPUser();
		String searchString = null;
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedData = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedData.setMessage("searchPortal: String string is null");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);		
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualData = dashboardController.searchPortal(mockedRequest, searchString);
		assertEquals(actualData.getMessage(), expectedData.getMessage());
	}
	@Ignore
	@Test
	public void searchPortalsearchStringTest(){
		EPUser user = mockUser.mockEPUser();
		String searchString = "test";
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedData = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedData.setMessage("success");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);	
		Mockito.doNothing().when(auditService).logActivity(null, null);
		PortalRestResponse<Map<String, List<SearchResultItem>>> actualData = dashboardController.searchPortal(mockedRequest, searchString);
		assertEquals(actualData.getMessage(), expectedData.getMessage());
	}
	//@Ignore
	@Test
	public void searchPortalsearchStringExceptionTest(){
		EPUser user = mockUser.mockEPUser();
		String searchString = "test";
		PortalRestResponse<Map<String, List<SearchResultItem>>> expectedData = new PortalRestResponse<Map<String, List<SearchResultItem>>>();
		expectedData.setMessage("searchPortal: String string is null");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);		
		Mockito.when(dashboardController.searchPortal(mockedRequest, searchString)).thenThrow(nullPointerException);
	}
	
	
}
