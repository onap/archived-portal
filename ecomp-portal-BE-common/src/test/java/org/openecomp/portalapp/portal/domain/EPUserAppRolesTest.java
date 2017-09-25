package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserAppRoles;

public class EPUserAppRolesTest {

	public EPUserAppRoles mockEPUserAppRoles(){
		EPUserAppRoles epUserAppRoles = new EPUserAppRoles();
		
		epUserAppRoles.setRoleId((long)1);
		epUserAppRoles.setAppId((long)1); 
		
		return epUserAppRoles;
	}
	
	@Test
	public void epUserAppRolesTest(){
		EPUserAppRoles epUserAppRoles = mockEPUserAppRoles();
		
		assertEquals(epUserAppRoles.getRoleId(), new Long(1));
		assertEquals(epUserAppRoles.getAppId(), new Long(1));
		assertEquals("EpUserAppRoles [roleId=1, appId=1]", epUserAppRoles.toString());
	}
}

