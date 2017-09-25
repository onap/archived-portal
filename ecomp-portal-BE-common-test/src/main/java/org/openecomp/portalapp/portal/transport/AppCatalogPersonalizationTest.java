package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class AppCatalogPersonalizationTest {
	
	public AppCatalogPersonalization mockAppCatalogPersonalization(){
		AppCatalogPersonalization appCatalogPersonalization = new AppCatalogPersonalization();
		
		return appCatalogPersonalization;
	}
	
	@Test
	public void appCatalogPersonalizationTest(){
		AppCatalogPersonalization appCatalogPersonalization = mockAppCatalogPersonalization();
		
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		
		assertEquals(appCatalogPersonalization.hashCode(), appCatalogPersonalization1.hashCode());
		assertTrue(appCatalogPersonalization.equals(appCatalogPersonalization1));
	}

}
