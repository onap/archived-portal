package org.openecomp.portalapp.portal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openecomp.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;

public class AppCategoryFunctionsItemTest {

	public AppCategoryFunctionsItem mockAppCategoryFunctionsItem(){
		AppCategoryFunctionsItem appCategoryFunctionsItem = new AppCategoryFunctionsItem();
		
		appCategoryFunctionsItem.setRowId("test");
		appCategoryFunctionsItem.setAppId("test");
		appCategoryFunctionsItem.setApplication("test");
		appCategoryFunctionsItem.setFunctions("test");
		
		return appCategoryFunctionsItem;
	}
	
	@Test
	public void appCategoryFunctionsItemTest(){
		AppCategoryFunctionsItem appCategoryFunctionsItem = mockAppCategoryFunctionsItem();
		
		AppCategoryFunctionsItem appCategoryFunctionsItem1 = new AppCategoryFunctionsItem();
		
		appCategoryFunctionsItem1.setRowId("test");
		appCategoryFunctionsItem1.setAppId("test");
		appCategoryFunctionsItem1.setApplication("test");
		appCategoryFunctionsItem1.setFunctions("test");
		
		assertEquals(appCategoryFunctionsItem.getRowId(), appCategoryFunctionsItem1.getRowId());
		assertEquals(appCategoryFunctionsItem.getAppId(), appCategoryFunctionsItem1.getAppId());
		assertEquals(appCategoryFunctionsItem.getApplication(), appCategoryFunctionsItem1.getApplication());
		assertEquals(appCategoryFunctionsItem.getFunctions(), appCategoryFunctionsItem1.getFunctions());
		assertEquals(appCategoryFunctionsItem.toString(), "AppCategoryFunctionsItem [rowId=test, appId=test, application=test, category=null, functions=test]");
		assertEquals(appCategoryFunctionsItem.hashCode(), appCategoryFunctionsItem1.hashCode());
		assertTrue(appCategoryFunctionsItem.equals(appCategoryFunctionsItem1));

	}
}
