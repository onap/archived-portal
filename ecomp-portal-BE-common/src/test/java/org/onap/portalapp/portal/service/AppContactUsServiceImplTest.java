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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.AppContactUs;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.onap.portalapp.portal.ecomp.model.AppContactUsItem;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AppContactUsService;
import org.onap.portalapp.portal.service.AppContactUsServiceImpl;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class AppContactUsServiceImplTest {

	
     @Mock
	 DataAccessService dataAccessService ;
    
     @Mock
     AppContactUsService AppContactUsService ;
     @InjectMocks
     AppContactUsServiceImpl appContactUsServiceImpl = new AppContactUsServiceImpl();

 	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

 	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
 	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
     
     @Before
 	public void setup() {
 		MockitoAnnotations.initMocks(this);
 	}
 	
   
 	NullPointerException nullPointerException = new NullPointerException();
 	
 	
 	public EPApp getApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(true);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}
 	@Test
 	public void getAppContactUsTest() throws Exception
 	{
 		
 		List<AppContactUsItem> contactUsItemList  = new ArrayList<>();
 		AppContactUsItem appContactUsItem= new AppContactUsItem();
 		appContactUsItem.setAppName("testNew");
 		contactUsItemList.add(appContactUsItem);
 		AppContactUsItem appContactUsItem1= new AppContactUsItem();
 		appContactUsItem1.setAppName("test");
 		contactUsItemList.add(appContactUsItem1);
 		Mockito.when(dataAccessService.executeNamedQuery("getAppContactUsItems", null, null)).thenReturn(contactUsItemList);
 		List<AppContactUsItem> expectedcontactUsItemList = appContactUsServiceImpl.getAppContactUs();
 		assertEquals(expectedcontactUsItemList, contactUsItemList); 		
 	}
 	
 	@Test
 	public void getAppsAndContactsTest() throws Exception
 	{
 		List<AppContactUsItem> contactUsItemList  = new ArrayList<>();
 		AppContactUsItem appContactUsItem= new AppContactUsItem();
 		appContactUsItem.setAppName("testNew");
 		contactUsItemList.add(appContactUsItem);
 		AppContactUsItem appContactUsItem1= new AppContactUsItem();
 		appContactUsItem1.setAppName("test");
 		contactUsItemList.add(appContactUsItem1);
 		Mockito.when(dataAccessService.executeNamedQuery("getAppsAndContacts", null, null)).thenReturn(contactUsItemList);
 		List<AppContactUsItem> expectedcontactUsItemList = appContactUsServiceImpl.getAppsAndContacts();
 		assertEquals(expectedcontactUsItemList, contactUsItemList); 		
 	}
 	
 	
 	@Test
 	public void getAppCategoryFunctionsTest() throws Exception
 	{
 		List<AppCategoryFunctionsItem> list  = new ArrayList<>();
 		Mockito.when(dataAccessService.executeNamedQuery("getAppCategoryFunctions", null, null)).thenReturn(list);
 		List<AppCategoryFunctionsItem> expectedlist = appContactUsServiceImpl.getAppCategoryFunctions();
 		assertEquals(list, expectedlist); 
 	}
 	
 	@Test(expected = java.lang.Exception.class)
 	public void saveAppContactUsTest() throws Exception
 	{
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		List<AppContactUsItem> contactUsModelList = new ArrayList<>();
 		AppContactUsItem appContactUsItem= new AppContactUsItem();
 		appContactUsItem.setAppId((long) 1);
 		contactUsModelList.add(appContactUsItem);
 		AppContactUs appContact = new AppContactUs();
 		Mockito.when(dataAccessService.getDomainObject(AppContactUs.class, 1, map)).thenReturn(appContact);
 		EPApp app = getApp();
 		Mockito.when(dataAccessService.getDomainObject(EPApp.class, 1, new HashMap<String, Object>())).thenReturn(app);
 		AppContactUs contactUs  = new AppContactUs();
 		contactUs.setApp(app);
		contactUs.setDescription(appContactUsItem.getDescription());
		contactUs.setContactName(appContactUsItem.getContactName());
		contactUs.setContactEmail(appContactUsItem.getContactEmail());
		contactUs.setActiveYN(appContactUsItem.getActiveYN());
		contactUs.setUrl(appContactUsItem.getUrl());
		Mockito.doNothing().when(dataAccessService).saveDomainObject(contactUs,map);
		appContactUsServiceImpl.saveAppContactUs(contactUsModelList);
 	}
 	
 	@Test
 	public void saveAppContacts()throws Exception {
 		
 		List<AppContactUsItem> contactUsModelList = new ArrayList<>();
 		AppContactUsItem appContactUsItem= new AppContactUsItem();
 		appContactUsItem.setAppId((long) 1);
 		contactUsModelList.add(appContactUsItem);
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		
 		Mockito.when(dataAccessService.getDomainObject(AppContactUs.class,
						appContactUsItem.getAppId(), map)).thenReturn(appContactUsItem);
 		
 		Mockito.when(dataAccessService.getDomainObject(EPApp.class, appContactUsItem.getAppId(), map)).thenReturn(getApp());
 		appContactUsServiceImpl.saveAppContactUs(contactUsModelList);
 	}
 	
 	@Test(expected = java.lang.NullPointerException.class)
 	public void deleteContactUs_error_Test() throws Exception
 	{
 		HashMap<String, Object> map = new HashMap<String, Object>();
		AppContactUs contactUs = new AppContactUs();
		Mockito.when((AppContactUs) dataAccessService.getDomainObject(AppContactUs.class, 1, map)).thenReturn(contactUs);
		appContactUsServiceImpl.deleteContactUs((long) 1);
 	}
 	@Test(expected=Exception.class)
 	public void deleteContactUsTest()throws Exception {
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		
 		AppContactUs contactUs = new AppContactUs();
 		contactUs.setId(1l);
 		Mockito.when(dataAccessService.getDomainObject(AppContactUs.class,
 				contactUs.getId(), map)).thenReturn(contactUs);
 		appContactUsServiceImpl.deleteContactUs(	contactUs.getId());
 	}
 	
}
