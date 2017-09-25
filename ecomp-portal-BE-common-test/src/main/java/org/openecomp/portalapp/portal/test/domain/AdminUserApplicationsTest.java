package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import javax.persistence.Column;
import javax.persistence.Id;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.AdminUserApp;
import org.openecomp.portalapp.portal.domain.AdminUserApplications;

public class AdminUserApplicationsTest {

	public AdminUserApplications mockAdminUserApplications(){
		
		AdminUserApp adminUserApp = new AdminUserApp();
		
		adminUserApp.setUserId((long)1);
		adminUserApp.setFirstName("test");
		adminUserApp.setLastName("test");
		adminUserApp.setOrgUserId("test");
		adminUserApp.setAppId((long)1);
		adminUserApp.setAppName("test");
		
		AdminUserApplications adminUserApplications = new AdminUserApplications(adminUserApp);
		
		adminUserApplications.setUser_Id((long)1);
		adminUserApplications.setFirstName("test");
		adminUserApplications.setLastName("test");
		adminUserApplications.setOrgUserId("test");	
		return adminUserApplications;
	}
	
	@Test
	public void adminUserAppTest(){
		AdminUserApplications adminUserApplications = mockAdminUserApplications();
	    AdminUserApp adminUserApp = new AdminUserApp();
		
		adminUserApp.setUserId((long)1);
		adminUserApp.setFirstName("test");
		adminUserApp.setLastName("test");
		adminUserApp.setOrgUserId("test");
		adminUserApp.setAppId((long)1);
		adminUserApp.setAppName("test");
		AdminUserApplications adminUserApplications1 = new AdminUserApplications(adminUserApp);
		
		assertEquals(adminUserApplications.getUser_Id(), new Long(1));
		assertEquals(adminUserApplications.getFirstName(), "test");
		assertEquals(adminUserApplications.getLastName(), "test");
		assertEquals(adminUserApplications.getOrgUserId(), "test");
		
		
		assertEquals(adminUserApplications1.getApps().get(0).getAppId(),adminUserApp.getAppId());
		assertEquals(adminUserApplications1.getApps().get(0).getAppName(),adminUserApp.getAppName());
		
	}
}
