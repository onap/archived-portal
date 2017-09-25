package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BusinessCardApplicationRoleTest {

	public BusinessCardApplicationRole mockBusinessCardApplicationRole(){
		BusinessCardApplicationRole businessCardApplicationRole = new BusinessCardApplicationRole();
		
		return businessCardApplicationRole;
	}
	
	@Test
	public void businessCardApplicationRoleTest(){
		BusinessCardApplicationRole businessCardApplicationRole = mockBusinessCardApplicationRole();
		
		BusinessCardApplicationRole businessCardApplicationRole1 = new BusinessCardApplicationRole();
		
		assertEquals(businessCardApplicationRole.hashCode(), businessCardApplicationRole1.hashCode());
		assertTrue(businessCardApplicationRole.equals(businessCardApplicationRole1));
		assertEquals(businessCardApplicationRole.toString(), "BusinessCardUserApplicationRoles [appName=null, roleName=null]");
	}
}
