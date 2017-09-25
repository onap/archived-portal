package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.AppNameIdIsAdmin;

public class AppNameIdIsAdminTest {

	public AppNameIdIsAdmin mockAppNameIdIsAdmin(){
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appNameIdIsAdmin.setId((long)1);
		appNameIdIsAdmin.setAppName("test");
		appNameIdIsAdmin.setRestrictedApp(false);
		appNameIdIsAdmin.setIsAdmin(false);
		return appNameIdIsAdmin;
	}
	
	@Test
	public void appNameIdIsAdminTest(){
		AppNameIdIsAdmin appNameIdIsAdmin = mockAppNameIdIsAdmin(); 
		
		AppNameIdIsAdmin appNameIdIsAdmin1 = new AppNameIdIsAdmin();
		appNameIdIsAdmin1.setId((long)1);
		appNameIdIsAdmin1.setAppName("test");
		appNameIdIsAdmin1.setRestrictedApp(false);
		appNameIdIsAdmin1.setIsAdmin(false);
		
		assertEquals(appNameIdIsAdmin.getId(), new Long(1));
		assertEquals(appNameIdIsAdmin.getAppName(), "test");
		assertEquals(appNameIdIsAdmin.getRestrictedApp(), false);
		assertEquals(appNameIdIsAdmin.getIsAdmin(), false);
		
		assertEquals(appNameIdIsAdmin.toString(), "AppNameIdIsAdmin [id=1, appName=test, isAdmin=false, restrictedApp=false]");
		assertEquals(appNameIdIsAdmin.hashCode(), appNameIdIsAdmin1.hashCode());
		assertTrue(appNameIdIsAdmin.equals(appNameIdIsAdmin1));
	}
}
