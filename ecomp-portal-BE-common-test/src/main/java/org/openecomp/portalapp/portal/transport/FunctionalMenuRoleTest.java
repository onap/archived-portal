package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.FunctionalMenuRole;

public class FunctionalMenuRoleTest {

	public FunctionalMenuRole mockFunctionalMenuRole(){
		FunctionalMenuRole functionalMenuRole = new FunctionalMenuRole();
		
		functionalMenuRole.setId(1);
		functionalMenuRole.setMenuId((long)1);
		functionalMenuRole.setAppId(1);
		functionalMenuRole.setRoleId(1);
		
		return functionalMenuRole;
	}
	
	@Test
	public void functionalMenuRoleTest(){
		FunctionalMenuRole functionalMenuRole = mockFunctionalMenuRole();
		
		FunctionalMenuRole functionalMenuRole1 = new FunctionalMenuRole();
		functionalMenuRole1.setId(1);
		functionalMenuRole1.setMenuId((long)1);
		functionalMenuRole1.setAppId(1);
		functionalMenuRole1.setRoleId(1);
				 
		assertEquals(functionalMenuRole.getId().toString(), "1");
		assertEquals(functionalMenuRole.getMenuId(), new Long(1));
		assertEquals(functionalMenuRole.getAppId().toString(), "1");
		assertEquals(functionalMenuRole.getRoleId().toString(), "1");
		assertEquals(functionalMenuRole.toString(), "FunctionalMenuRole [id=1, menuId=1, appId=1, roleId=1]");
		assertTrue(functionalMenuRole.equals(functionalMenuRole1));
		assertEquals(functionalMenuRole.hashCode(), functionalMenuRole1.hashCode());
		
	}
}
