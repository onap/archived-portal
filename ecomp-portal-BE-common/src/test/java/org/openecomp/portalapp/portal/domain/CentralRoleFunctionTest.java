package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;

public class CentralRoleFunctionTest {

	public CentralRoleFunction mockCentralRoleFunction(){
		
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		   
		   centralRoleFunction.setCode("test");
		   centralRoleFunction.setName("test");
		   centralRoleFunction.setAppId((long)1);
		   centralRoleFunction.setEditUrl("test");
		   
		return centralRoleFunction;
	}
	
	@Test
	public void centralRoleFunctionTest(){
		CentralRoleFunction centralRoleFunction = mockCentralRoleFunction();
		
		assertEquals(centralRoleFunction.getAppId(), new Long(1));
		assertEquals(centralRoleFunction.getCode(), "test");
		assertEquals(centralRoleFunction.getName(), "test");
		assertEquals(centralRoleFunction.getEditUrl(), "test");
	}
}
