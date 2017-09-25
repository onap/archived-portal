package org.openecomp.portalapp.portal.transport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;

public class AppWithRolesForUserTest {

	public AppWithRolesForUser mockAppWithRolesForUser(){
		AppWithRolesForUser appWithRolesForUser = new AppWithRolesForUser();
		
	//	List<RoleInAppForUser> appRoles = new ArrayList<RoleInAppForUser>();		
		
		appWithRolesForUser.setOrgUserId("test");
		appWithRolesForUser.setAppId((long)1);
		appWithRolesForUser.setAppName("test");
		appWithRolesForUser.setAppRoles(null);
		
		return appWithRolesForUser;
	}
	
	@Test
	public void roleInAppForUserTest(){
		AppWithRolesForUser appWithRolesForUser = mockAppWithRolesForUser();
		
		assertEquals(appWithRolesForUser.getOrgUserId(), "test");
		assertEquals(appWithRolesForUser.getAppId(), new Long(1));
		assertEquals(appWithRolesForUser.getAppName(), "test");
		assertEquals(appWithRolesForUser.getAppRoles(), null);
		assertEquals(appWithRolesForUser.toString(), "AppWithRolesForUser [orgUserId=test, appId=1, appName=test, appRoles=null]");
	}
}
