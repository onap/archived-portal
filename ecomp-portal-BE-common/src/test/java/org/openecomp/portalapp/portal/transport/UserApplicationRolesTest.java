package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.RemoteRole;
import org.openecomp.portalapp.portal.transport.UserApplicationRoles;

public class UserApplicationRolesTest {

	public UserApplicationRoles mockUserApplicationRoles(){
		UserApplicationRoles userApplicationRoles = new UserApplicationRoles();
		
		List<RemoteRole> roles = new ArrayList<RemoteRole>();
		RemoteRole remoteRole = new RemoteRole();
		remoteRole.setId((long)1);
		remoteRole.setName("test");
		roles.add(remoteRole);
		
		userApplicationRoles.setAppId((long)1);
		userApplicationRoles.setOrgUserId("test");
		userApplicationRoles.setFirstName("test");
		userApplicationRoles.setLastName("test");
		userApplicationRoles.setRoles(roles);
		
		return userApplicationRoles;
	}
	
	@Test
	public void userApplicationRolesTest(){
		UserApplicationRoles userApplicationRoles = mockUserApplicationRoles();
		
		UserApplicationRoles userApplicationRoles1 = new UserApplicationRoles();
		
		List<RemoteRole> roles = new ArrayList<RemoteRole>();
		RemoteRole remoteRole = new RemoteRole();
		remoteRole.setId((long)1);
		remoteRole.setName("test");
		roles.add(remoteRole);
		
		userApplicationRoles1.setAppId((long)1);
		userApplicationRoles1.setOrgUserId("test");
		userApplicationRoles1.setFirstName("test");
		userApplicationRoles1.setLastName("test");
		userApplicationRoles1.setRoles(roles);
		
		assertEquals(userApplicationRoles.getAppId(), userApplicationRoles1.getAppId());
		assertEquals(userApplicationRoles.getOrgUserId(), userApplicationRoles1.getOrgUserId());
		assertEquals(userApplicationRoles.getFirstName(), userApplicationRoles1.getFirstName());
		assertEquals(userApplicationRoles.getLastName(), userApplicationRoles1.getLastName());
		assertEquals(userApplicationRoles.getRoles(), userApplicationRoles1.getRoles());
		
		
	}
}
