package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.AppNameIdIsAdmin;
import org.openecomp.portalapp.portal.transport.AppsListWithAdminRole;

public class AppsListWithAdminRoleTest {
	
	public AppsListWithAdminRole mockAppsListWithAdminRole(){
		AppsListWithAdminRole appsListWithAdminRole = new AppsListWithAdminRole();
		
		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<AppNameIdIsAdmin>();
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appsRoles.add(appNameIdIsAdmin);
		
		appsListWithAdminRole.setOrgUserId("test");
		appsListWithAdminRole.setAppsRoles(appsRoles);
		
		return appsListWithAdminRole;
	}

	@Test
	public void appsListWithAdminRoleTest(){
		AppsListWithAdminRole appsListWithAdminRole = mockAppsListWithAdminRole();
		
		AppsListWithAdminRole appsListWithAdminRole1 = new AppsListWithAdminRole();
		
		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<AppNameIdIsAdmin>();
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appsRoles.add(appNameIdIsAdmin);
		
		appsListWithAdminRole1.setOrgUserId("test");
		appsListWithAdminRole1.setAppsRoles(appsRoles);
				
		assertEquals(appsListWithAdminRole.getOrgUserId(), "test");
		assertEquals(appsListWithAdminRole.getAppsRoles(), appsRoles);
		
		assertTrue(appsListWithAdminRole.equals(appsListWithAdminRole1));
		assertEquals(appsListWithAdminRole.hashCode(), appsListWithAdminRole1.hashCode());
		assertEquals(appsListWithAdminRole.toString(), "AppsListWithAdminRole [orgUserId=test, appsRoles=[AppNameIdIsAdmin [id=null, appName=null, isAdmin=null, restrictedApp=null]]]");
	}
}
