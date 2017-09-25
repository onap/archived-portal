package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EPUserAppCurrentRoles;

public class EPUserAppCurrentRolesTest {

	public EPUserAppCurrentRoles mockEPUserAppCurrentRoles(){
		EPUserAppCurrentRoles epUserAppCurrentRoles = new EPUserAppCurrentRoles();
			
		epUserAppCurrentRoles.setRoleName("test");
		epUserAppCurrentRoles.setUserId((long)1);
		epUserAppCurrentRoles.setPriority("test");
		epUserAppCurrentRoles.setRoleId((long)1);
		
		return epUserAppCurrentRoles;
	}
	
	@Test
	public void epUserAppCurrentRolesTest(){
		EPUserAppCurrentRoles epUserAppCurrentRoles = mockEPUserAppCurrentRoles();
		
		EPUserAppCurrentRoles epUserAppCurrentRoles1 = new EPUserAppCurrentRoles();
		
		epUserAppCurrentRoles1.setRoleName("test");
		epUserAppCurrentRoles1.setUserId((long)1);
		epUserAppCurrentRoles1.setPriority("test");
		epUserAppCurrentRoles1.setRoleId((long)1);
		
		assertEquals(epUserAppCurrentRoles.getRoleName(), "test");
		assertEquals(epUserAppCurrentRoles.getUserId(), new Long(1));
		assertEquals(epUserAppCurrentRoles.getRoleId(), new Long(1));
		assertEquals(epUserAppCurrentRoles.getPriority(), "test");
		assertEquals(epUserAppCurrentRoles.hashCode(), epUserAppCurrentRoles1.hashCode());
		assertTrue(epUserAppCurrentRoles.equals(epUserAppCurrentRoles1));
		
	}
}
