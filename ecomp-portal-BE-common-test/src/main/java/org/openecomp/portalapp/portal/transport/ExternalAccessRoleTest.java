package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessRole;

public class ExternalAccessRoleTest {

	public ExternalAccessRole mockExternalAccessRole(){
		ExternalAccessRole externalAccessRole = new ExternalAccessRole();
		
		externalAccessRole.setName("test");
		externalAccessRole.setDescription("test");
		
		return externalAccessRole;
	}
	
	@Test
	public void externalAccessRoleTest(){
		ExternalAccessRole externalAccessRole = mockExternalAccessRole();
		
		assertEquals(externalAccessRole.getName(), "test");
		assertEquals(externalAccessRole.getDescription(), "test");
	}
}
