package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserAppCatalogRoles;

public class EPUserAppCatalogRolesTest {

	public EPUserAppCatalogRoles mockEPUserAppCatalogRoles(){
		EPUserAppCatalogRoles epUserAppCatalogRoles = new EPUserAppCatalogRoles();
				
		epUserAppCatalogRoles.setRequestedRoleId((long)1);
		epUserAppCatalogRoles.setRolename("test");
		epUserAppCatalogRoles.setRequestStatus("status");
		epUserAppCatalogRoles.setAppId((long)1);
		
		return epUserAppCatalogRoles;
	}
	
	@Test
	public void epUserAppCatalogRolesTest(){
		EPUserAppCatalogRoles epUserAppCatalogRoles = mockEPUserAppCatalogRoles();
		
		assertEquals(epUserAppCatalogRoles.getRequestedRoleId(), new Long(1));
		assertEquals(epUserAppCatalogRoles.getRolename(), "test");
		assertEquals(epUserAppCatalogRoles.getRequestStatus(), "status");
		assertEquals(epUserAppCatalogRoles.getAppId(), new Long(1));
		
		assertEquals("EPUserAppCatalogRoles [requestedRoleId=1, rolename=test, requestStatus=status, appId=1]", epUserAppCatalogRoles.toString());
	}
}
