package com.openecomp.portalapp.portal.ecomp.test.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.ecomp.model.AppCatalogItem;

public class AppCatalogItemTest {

	public AppCatalogItem mockAppCatalogItem(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setMlAppName("test");
		
		return appCatalogItem;
	}
	
	@Test
	public void appCatalogItemTest(){
		AppCatalogItem appCatalogItem = mockAppCatalogItem();
		
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem1.setAlternateUrl("test");
		appCatalogItem1.setMlAppName("test");
		
		assertEquals(appCatalogItem.getAlternateUrl(), appCatalogItem1.getAlternateUrl());
		assertEquals(appCatalogItem.getMlAppName(), appCatalogItem1.getMlAppName());

		assertEquals(appCatalogItem.toString(), "AppCatalogItem [id=null, name=null, access=null, select=null, pending=null]");
		assertEquals(appCatalogItem.hashCode(), appCatalogItem1.hashCode());
		assertTrue(appCatalogItem.equals(appCatalogItem1));
		
	}
}
