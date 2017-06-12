/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.AppContactUsController;
import org.openecomp.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.openecomp.portalapp.portal.ecomp.model.AppContactUsItem;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.AppContactUsService;
import org.openecomp.portalapp.portal.service.AppContactUsServiceImpl;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;

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

}
