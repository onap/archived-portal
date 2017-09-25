package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.DashboardController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.transport.CommonWidget;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.domain.support.CollaborateList;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, CollaborateList.class, SystemProperties.class, EPCommonSystemProperties.class})
public class DashboardControllerTest {
	
	@Mock
	DashboardSearchService searchService = new DashboardSearchServiceImpl();
	
	@InjectMocks
	DashboardController dashboardController = new DashboardController();

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
	    commonWidget.setEventDate("testDate");
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
		System.out.println(actualResponse);
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
		
				    
				
	/*	commonWidgetMeta.setItems(widgetList);
		
		commonWidgetMeta.setCategory("EVENTS");*/
		
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
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget);
		assertEquals(expectedData,actualResponse);
		
	}
	
	@Test
	public void saveWidgetDataErrorTest() throws IOException {
				
		CommonWidget commonWidget = mockCommonWidget();		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Invalid category: test");
		expectedData.setResponse(null); 
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget);
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
		
		Mockito.when(searchService.saveWidgetData(commonWidget)).thenReturn("success");
		
		PortalRestResponse<String> actualResponse = dashboardController.saveWidgetData(commonWidget);
		assertEquals(expectedData,actualResponse);
		
	}
	
	@Test
	public void deleteWidgetDataTest() throws IOException {
				
		CommonWidget commonWidget = mockCommonWidget();
		
		PortalRestResponse<String> expectedData = new PortalRestResponse<String>();
		expectedData.setStatus(PortalRestStatusEnum.OK);
		expectedData.setMessage("success");
		expectedData.setResponse(null); 
		
		Mockito.when(searchService.saveWidgetData(commonWidget)).thenReturn("success");
		
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
	
//	@Test
//	public void activeUsersExceptionTest(){
//		EPUser user = mockUser.mockEPUser();
//		user.setLoginId("test");
//		String loginId = "abc";
//		PortalRestResponse<List<String>> expectedData = new PortalRestResponse<List<String>>();
//		expectedData.setStatus(PortalRestStatusEnum.ERROR);
//		expectedData.setMessage("java.lang.NullPointerException");
//		expectedData.setResponse(null);  
//		
////		PowerMockito.mockStatic(EPUserUtils.class);
//		Mockito.when(searchService.getRelatedUsers(user.getLoginId(user.getLoginId()))).thenReturn(nullPointerException);
//		PortalRestResponse<List<String>> actualResponse = dashboardController.activeUsers(mockedRequest);
//		System.out.println(actualResponse);
//		//assertEquals(expectedData,actualResponse);
//	}
}
