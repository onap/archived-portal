package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EcompAppRole;

public class EcompAppRoleTest {

	public EcompAppRole mockEcompAppRole(){
		EcompAppRole ecompAppRole = new EcompAppRole();
		
		ecompAppRole.setAppId((long)1);
		ecompAppRole.setAppName("test");
		ecompAppRole.setRoleId(1);
		ecompAppRole.setRoleName("test");
		
		return ecompAppRole;
	}
	
	@Test
	public void ecompAppRoleTest(){
		EcompAppRole ecompAppRole = mockEcompAppRole();
		
		assertEquals(ecompAppRole.getAppId(), new Long(1));
		assertEquals(ecompAppRole.getAppName(), "test");
		assertEquals(ecompAppRole.getRoleId().toString(), "1");
		assertEquals(ecompAppRole.getRoleName(), "test");
		
	}
}
