package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.UserIdRoleId;

public class UserIdRoleIdTest {

	public UserIdRoleId mockUserIdRoleId(){
		UserIdRoleId userIdRoleId = new UserIdRoleId();
		userIdRoleId.setUser_Id("test");
		userIdRoleId.setRoleId("test");
		userIdRoleId.setOrgUserId("test");
		userIdRoleId.setAppId("test");
		
		return userIdRoleId;
	}
	
	@Test
	public void userIdRoleIdTest(){
		UserIdRoleId userIdRoleId = mockUserIdRoleId();
		
		assertEquals(userIdRoleId.getUser_Id(), "test");
		assertEquals(userIdRoleId.getRoleId(), "test");
		assertEquals(userIdRoleId.getOrgUserId(), "test");
		assertEquals(userIdRoleId.getAppId(), "test");
	}
}
