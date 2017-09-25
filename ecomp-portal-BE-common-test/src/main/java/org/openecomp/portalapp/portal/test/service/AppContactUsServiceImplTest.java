package org.openecomp.portalapp.portal.test.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.AppContactUs;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.openecomp.portalapp.portal.ecomp.model.AppContactUsItem;
import org.openecomp.portalapp.portal.service.AppContactUsService;
import org.openecomp.portalapp.portal.service.AppContactUsServiceImpl;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.DataAccessServiceImpl;

public class AppContactUsServiceImplTest {

	
     @Mock
	 DataAccessService dataAccessService = new DataAccessServiceImpl();
    
     @Mock
     AppContactUsService AppContactUsService = new AppContactUsServiceImpl();
     
     @Before
 	public void setup() {
 		MockitoAnnotations.initMocks(this);
 	}
 	
     @InjectMocks
     AppContactUsServiceImpl appContactUsServiceImpl = new AppContactUsServiceImpl();

 	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

 	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
 	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
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
 	
 	@Test(expected = java.lang.NullPointerException.class)
 	public void deleteContactUsTest() throws Exception
 	{
 		HashMap<String, Object> map = new HashMap<String, Object>();
		AppContactUs contactUs = new AppContactUs();
		Mockito.when((AppContactUs) dataAccessService.getDomainObject(AppContactUs.class, 1, map)).thenReturn(contactUs);
		appContactUsServiceImpl.deleteContactUs((long) 1);
 	}
 	
}
