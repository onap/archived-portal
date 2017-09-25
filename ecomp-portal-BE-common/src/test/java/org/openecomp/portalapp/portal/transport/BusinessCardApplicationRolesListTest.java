package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BusinessCardApplicationRolesListTest {

	public BusinessCardApplicationRolesList mockBusinessCardApplicationRolesList(){
		BusinessCardApplicationRolesList businessCardApplicationRolesList = new BusinessCardApplicationRolesList();
		
		return businessCardApplicationRolesList;
	}
	
	@Test
	public void businessCardApplicationRolesListTest(){
		BusinessCardApplicationRolesList businessCardApplicationRolesList = mockBusinessCardApplicationRolesList();
		
		BusinessCardApplicationRolesList businessCardApplicationRolesList1 = new BusinessCardApplicationRolesList();
		
		assertEquals(businessCardApplicationRolesList.hashCode(), businessCardApplicationRolesList1.hashCode());
		assertTrue(businessCardApplicationRolesList.equals(businessCardApplicationRolesList1));
		assertEquals(businessCardApplicationRolesList.toString(), "BusinessCardUserAppRoles [appName=null, roleNames=null]");
	}
}
