package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPAppRoleFunction;

public class EPAppRoleFunctionTest {

	public EPAppRoleFunction mockEPAppRoleFunction(){
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setRoleId((long)1);
		epAppRoleFunction.setAppId((long)1);
		epAppRoleFunction.setCode("test");
		
		return epAppRoleFunction;
	}
	
	@Test
	public void epAppRoleFunctionTest(){
		EPAppRoleFunction epAppRoleFunction = mockEPAppRoleFunction();
		
		assertEquals(epAppRoleFunction.getRoleId(), new Long(1));
		assertEquals(epAppRoleFunction.getAppId(), new Long(1));
		assertEquals(epAppRoleFunction.getCode(), "test");
	}
}
