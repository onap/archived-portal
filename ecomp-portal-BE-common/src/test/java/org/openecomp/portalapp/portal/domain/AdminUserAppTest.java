package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.AdminUserApp;

public class AdminUserAppTest {

	public AdminUserApp mockAdminUserApp(){
		
		AdminUserApp adminUserApp = new AdminUserApp();
	
		adminUserApp.setUserId((long)1);
		adminUserApp.setFirstName("test");
		adminUserApp.setLastName("test");
		adminUserApp.setOrgUserId("test");
		adminUserApp.setAppId((long)1);
		adminUserApp.setAppName("test");
		
		return adminUserApp;
	}
	
	@Test
	public void adminUserAppTest(){
		AdminUserApp adminUserApp = mockAdminUserApp();
		
		assertEquals(adminUserApp.getUser_Id(), new Long(1));
		assertEquals(adminUserApp.getFirstName(), "test");
		assertEquals(adminUserApp.getLastName(), "test");
		assertEquals(adminUserApp.getOrgUserId(), "test");
		assertEquals(adminUserApp.getAppName(), "test");
		assertEquals(adminUserApp.getAppId(), new Long(1));
		
	}
}
