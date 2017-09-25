package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;

public class RoleInAppForUserTest {

	public RoleInAppForUser mockRoleInAppForUser(){
		RoleInAppForUser roleInAppForUser = new RoleInAppForUser((long)1 , "test");
		roleInAppForUser.setRoleId((long)1);
		roleInAppForUser.setRoleName("test");
		roleInAppForUser.setIsApplied(false);
		
		return roleInAppForUser;
	}
	
	@Test
	public void roleInAppForUserTest(){
		RoleInAppForUser roleInAppForUser = mockRoleInAppForUser();
		
		RoleInAppForUser roleInAppForUser1 = new RoleInAppForUser((long)1 , "test");
		roleInAppForUser1.setRoleId((long)1);
		roleInAppForUser1.setRoleName("test");
		roleInAppForUser1.setIsApplied(false);
		
		assertEquals(roleInAppForUser.getRoleId(), new Long(1));
		assertEquals(roleInAppForUser.getRoleName(), "test");
		assertEquals(roleInAppForUser.getIsApplied(), false);
		
		assertEquals(roleInAppForUser.toString(), "RoleInAppForUser [roleId=1, roleName=test, isApplied=false]");
		assertTrue(roleInAppForUser.equals(roleInAppForUser1));
		assertEquals(roleInAppForUser.hashCode(), roleInAppForUser1.hashCode());
		//constructor
	}
}
