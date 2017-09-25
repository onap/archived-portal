package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EcompUserAppRoles;

public class EcompUserAppRolesTest {

	public EcompUserAppRoles mockEcompUserAppRoles(){
		EcompUserAppRoles ecompUserAppRoles = new EcompUserAppRoles();
			
		ecompUserAppRoles.setAppId("test");
		ecompUserAppRoles.setUserId((long)1);
		ecompUserAppRoles.setPriority("test");
		ecompUserAppRoles.setRoleId((long)1);
		ecompUserAppRoles.setRoleName("test");
		
		return ecompUserAppRoles;
	}
	
	@Test
	public void ecompUserAppRolesTest(){
		
		EcompUserAppRoles ecompUserAppRoles = mockEcompUserAppRoles();
		
		assertEquals(ecompUserAppRoles.getAppId(), "test");
		assertEquals(ecompUserAppRoles.getPriority(), "test");
		assertEquals(ecompUserAppRoles.getRoleName(), "test");
		assertEquals(ecompUserAppRoles.getUserId(), new Long(1));
		assertEquals(ecompUserAppRoles.getRoleId(), new Long(1));
	}
}
