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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.onap.portalapp.portal.controller.AppContactUsController;
import org.onap.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.onap.portalapp.portal.ecomp.model.AppContactUsItem;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AppContactUsService;
import org.onap.portalapp.portal.service.AppContactUsServiceImpl;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.support.CollaborateList;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class, EPCommonSystemProperties.class})
public class AppContactUsControllerTest extends MockitoTestSuite{

	@Mock
	AppContactUsService contactUsService = new AppContactUsServiceImpl();

	@InjectMocks
	AppContactUsController appContactUsController = new AppContactUsController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	public List<AppContactUsItem> mockResponse() {
		List<AppContactUsItem> appContactUsItemList = new ArrayList<AppContactUsItem>();
		AppContactUsItem appContactUsItem = new AppContactUsItem();
		appContactUsItem.setAppId((long) 1);
		appContactUsItem.setAppName("ECOMP Portal");
		appContactUsItem.setDescription("Test");
		appContactUsItem.setContactName("Test");
		appContactUsItem.setContactEmail("person@onap.org");
		appContactUsItem.setUrl("Test_URL");
		appContactUsItem.setActiveYN("Y");
		appContactUsItemList.add(appContactUsItem);

		return appContactUsItemList;

	}

	public PortalRestResponse<List<AppContactUsItem>> successPortalRestResponse() {
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		List<AppContactUsItem> appContactUsItemList = mockResponse();
		expectedportalRestResponse.setMessage("success");
		expectedportalRestResponse.setResponse(appContactUsItemList);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		return expectedportalRestResponse;

	}

	public PortalRestResponse<List<AppContactUsItem>> exceptionPortalRestResponse() {
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		return expectedportalRestResponse;

	}

	@Test
	public void getAppContactUsList() throws Exception {
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = successPortalRestResponse();
		List<AppContactUsItem> appContactUsItemList = mockResponse();
		PortalRestResponse<List<AppContactUsItem>> actualPortalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		Mockito.when(contactUsService.getAppContactUs()).thenReturn(appContactUsItemList);
		actualPortalRestResponse = appContactUsController.getAppContactUsList(mockedRequest);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getAppContactUsListCatchesExeptionTest() throws Exception {

		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = exceptionPortalRestResponse();
		PortalRestResponse<List<AppContactUsItem>> actualPortalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		Mockito.when(contactUsService.getAppContactUs()).thenThrow(nullPointerException);
		actualPortalRestResponse = appContactUsController.getAppContactUsList(mockedRequest);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getAppsAndContactsTest() throws Exception {
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = successPortalRestResponse();
		List<AppContactUsItem> appContactUsItemList = mockResponse();
		PortalRestResponse<List<AppContactUsItem>> actualPortalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		Mockito.when(contactUsService.getAppsAndContacts()).thenReturn(appContactUsItemList);
		actualPortalRestResponse = appContactUsController.getAppsAndContacts(mockedRequest);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void getAppsAndContactsCatchesExceptionTest() throws Exception {
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = exceptionPortalRestResponse();
		PortalRestResponse<List<AppContactUsItem>> actualPortalRestResponse = new PortalRestResponse<List<AppContactUsItem>>();
		Mockito.when(contactUsService.getAppsAndContacts()).thenThrow(nullPointerException);
		actualPortalRestResponse = appContactUsController.getAppsAndContacts(mockedRequest);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void getAppCategoryFunctionsTest() throws Exception {
		PortalRestResponse<List<AppCategoryFunctionsItem>> actualportalRestResponse = null;

		List<AppCategoryFunctionsItem> contents = new ArrayList<AppCategoryFunctionsItem>();

		AppCategoryFunctionsItem appCategoryFunctionsItem = new AppCategoryFunctionsItem();
		AppCategoryFunctionsItem appCategoryFunctionsItem1 = new AppCategoryFunctionsItem();

		appCategoryFunctionsItem.setRowId("1");
		appCategoryFunctionsItem.setAppId("1");
		appCategoryFunctionsItem.setApplication("Ecomp-portal");
		appCategoryFunctionsItem.setCategory("test");
		appCategoryFunctionsItem.setFunctions("test");

		appCategoryFunctionsItem1.setRowId("2");
		appCategoryFunctionsItem1.setAppId("2");
		appCategoryFunctionsItem1.setApplication("Ecomp-portal-test");
		appCategoryFunctionsItem1.setCategory("test");
		appCategoryFunctionsItem1.setFunctions("test");
		contents.add(appCategoryFunctionsItem);
		contents.add(appCategoryFunctionsItem1);

		PortalRestResponse<List<AppCategoryFunctionsItem>> expectedportalRestResponse = new PortalRestResponse<List<AppCategoryFunctionsItem>>();
		expectedportalRestResponse.setMessage("success");
		expectedportalRestResponse.setResponse(contents);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);

		Mockito.when(contactUsService.getAppCategoryFunctions()).thenReturn(contents);
		actualportalRestResponse = appContactUsController.getAppCategoryFunctions(mockedRequest);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void getAppCategoryFunctionsCatchesExceptionTest() throws Exception {
		PortalRestResponse<List<AppCategoryFunctionsItem>> actualportalRestResponse = null;
		PortalRestResponse<List<AppContactUsItem>> expectedportalRestResponse = exceptionPortalRestResponse();
		Mockito.when(contactUsService.getAppCategoryFunctions()).thenThrow(nullPointerException);
		actualportalRestResponse = appContactUsController.getAppCategoryFunctions(mockedRequest);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void saveTest() throws Exception {
		PortalRestResponse<String> actualSaveAppContactUS = null;

		AppContactUsItem contactUs = new AppContactUsItem();
		contactUs.setAppId((long) 1);
		contactUs.setAppName("Ecomp Portal");
		contactUs.setDescription("Test");
		contactUs.setContactName("Test");
		contactUs.setContactEmail("person@onap.org");
		contactUs.setUrl("Test_URL");
		contactUs.setActiveYN("Y");

		Mockito.when(contactUsService.saveAppContactUs(contactUs)).thenReturn("SUCCESS");
		actualSaveAppContactUS = appContactUsController.save(contactUs);
		assertEquals(actualSaveAppContactUS.getMessage(), "SUCCESS");
	}

	@Test
	public void saveExceptionTest() throws Exception {
		PortalRestResponse<String> actualSaveAppContactUS = null;

		AppContactUsItem contactUs = new AppContactUsItem();
		contactUs.setAppId((long) 1);
		contactUs.setAppName("Ecomp Portal");
		contactUs.setDescription("Test");
		contactUs.setContactName("Test");
		contactUs.setContactEmail("person@onap.org");
		contactUs.setUrl("Test_URL");
		contactUs.setActiveYN("Y");

		Mockito.when(contactUsService.saveAppContactUs(contactUs)).thenThrow(new Exception());
		actualSaveAppContactUS = appContactUsController.save(contactUs);
		assertEquals(actualSaveAppContactUS.getMessage(), "failure");
	}

	@Test
	public void saveWhenAppContactUsItemNullTest() throws Exception {
		PortalRestResponse<String> actualSaveAppContactUS = null;
		AppContactUsItem contactUs = null;
		actualSaveAppContactUS = appContactUsController.save(contactUs);
		assertEquals(actualSaveAppContactUS.getMessage(), "failure");

	}

	@Test
	public void saveAllTest() throws Exception {

		List<AppContactUsItem> contactUs = mockResponse();
		PortalRestResponse<String> actualSaveAppContactUS = null;
		Mockito.when(contactUsService.saveAppContactUs(contactUs)).thenReturn("SUCCESS");
		actualSaveAppContactUS = appContactUsController.save(contactUs);
		assertEquals(actualSaveAppContactUS.getMessage(), "SUCCESS");
	}

	@Test
	public void saveAllExceptionTest() throws Exception {

		List<AppContactUsItem> contactUs = mockResponse();
		PortalRestResponse<String> actualSaveAppContactUS = null;
		Mockito.when(contactUsService.saveAppContactUs(contactUs)).thenThrow(new Exception());
		actualSaveAppContactUS = appContactUsController.save(contactUs);
		assertEquals(actualSaveAppContactUS.getMessage(), "failure");
	}

	@Test
	public void deleteTest() throws Exception {

		PortalRestResponse<String> actualSaveAppContactUS = null;
		Long id = (long) 1;
		String saveAppContactUs = "SUCCESS";
		Mockito.when(contactUsService.deleteContactUs(id)).thenReturn(saveAppContactUs);
		actualSaveAppContactUS = appContactUsController.delete(id);
		assertEquals(actualSaveAppContactUS.getMessage(), "SUCCESS");
	}

	@Test
	public void deleteExceptionTest() throws Exception {

		PortalRestResponse<String> actualSaveAppContactUS = null;
		Long id = (long) 1;
		Mockito.when(contactUsService.deleteContactUs(id)).thenThrow(new Exception());
		actualSaveAppContactUS = appContactUsController.delete(id);
		assertEquals(actualSaveAppContactUS.getMessage(), "failure");
	}

	@Test
	public void getPortalDetailsTest(){
		PortalRestResponse<String> actualResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setStatus(PortalRestStatusEnum.OK);
		expectedResponse.setMessage("success");
		expectedResponse.setResponse("\"ush_ticket_url\":\"http://todo_enter_ush_ticket_url\",\"portal_info_url\":\"https://todo_enter_portal_info_url\",\"feedback_email_address\":\"portal@lists.openecomp.org\"");
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.USH_TICKET_URL)).thenReturn("http://todo_enter_ush_ticket_url"); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.PORTAL_INFO_URL)).thenReturn("https://todo_enter_portal_info_url"); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.FEEDBACK_EMAIL_ADDRESS)).thenReturn("portal@lists.openecomp.org"); 
		
		actualResponse = appContactUsController.getPortalDetails(mockedRequest);
		assertTrue(actualResponse.getStatus().compareTo(PortalRestStatusEnum.OK) == 0);
	}
	
	@Test
	public void getPortalDetailsExceptionTest(){
		PortalRestResponse<String> actualResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);
		expectedResponse.setMessage("failure");
		expectedResponse.setResponse(null);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.USH_TICKET_URL)).thenThrow(nullPointerException); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.PORTAL_INFO_URL)).thenReturn("https://todo_enter_portal_info_url"); 
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.FEEDBACK_EMAIL_ADDRESS)).thenReturn("portal@lists.openecomp.org"); 
		
		actualResponse = appContactUsController.getPortalDetails(mockedRequest);
		assertEquals(actualResponse, expectedResponse);
	}
}
