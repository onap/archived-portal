package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EcompAppRoleId;

public class EcompAppRoleIdTest {
	
	public EcompAppRoleId mockEcompAppRoleId(){
		
		EcompAppRoleId ecompAppRoleId = new EcompAppRoleId();
		
		ecompAppRoleId.setAppId((long)1);
		ecompAppRoleId.setAppName("test");
		ecompAppRoleId.setRoleId(1);
		ecompAppRoleId.setRoleName("test");
				
		return ecompAppRoleId;
	}
	
	@Test
	public void ecompAppRoleIdTest(){
		
	EcompAppRoleId ecompAppRoleId = mockEcompAppRoleId();
	
	assertEquals(ecompAppRoleId.getAppId(), new Long(1));
	assertEquals(ecompAppRoleId.getAppName(), "test");
	assertEquals(ecompAppRoleId.getRoleId().toString(), "1");
	assertEquals(ecompAppRoleId.getRoleName(), "test");
	}

}
