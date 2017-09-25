package org.openecomp.portalapp.portal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openecomp.portalapp.portal.ecomp.model.AppContactUsItem;

public class AppContactUsItemTest {
	
	public AppContactUsItem mockAppContactUsItem(){
		AppContactUsItem appContactUsItem = new AppContactUsItem();
				
		appContactUsItem.setAppId((long)1);
		appContactUsItem.setAppName("test");
		appContactUsItem.setDescription("test");
		appContactUsItem.setContactName("test");
		appContactUsItem.setContactEmail("test");
		appContactUsItem.setUrl("test");
		appContactUsItem.setActiveYN("test");
		
		return appContactUsItem;
	}

	@Test
	public void appContactUsItemTest(){
		AppContactUsItem appContactUsItem = mockAppContactUsItem();
		
		AppContactUsItem appContactUsItem1 = new AppContactUsItem();
		appContactUsItem1.setAppId((long)1);
		appContactUsItem1.setAppName("test");
		appContactUsItem1.setDescription("test");
		appContactUsItem1.setContactName("test");
		appContactUsItem1.setContactEmail("test");
		appContactUsItem1.setUrl("test");
		appContactUsItem1.setActiveYN("test");
		
		assertEquals(appContactUsItem.getAppId(), appContactUsItem1.getAppId());
		assertEquals(appContactUsItem.getAppName(), appContactUsItem1.getAppName());
		assertEquals(appContactUsItem.getDescription(), appContactUsItem1.getDescription());
		assertEquals(appContactUsItem.getContactName(), appContactUsItem1.getContactName());
		assertEquals(appContactUsItem.getContactEmail(), appContactUsItem1.getContactEmail());
		assertEquals(appContactUsItem.getUrl(), appContactUsItem1.getUrl());
		assertEquals(appContactUsItem.getActiveYN(), appContactUsItem1.getActiveYN());
		assertEquals(appContactUsItem.toString(), "AppContactUsItem [appId=1, appName=test, description=test, contactName=test, contactEmail=test, url=test, activeYN=test]");
		assertEquals(appContactUsItem.hashCode(), appContactUsItem1.hashCode());
		assertTrue(appContactUsItem.equals(appContactUsItem1));
	}
}
