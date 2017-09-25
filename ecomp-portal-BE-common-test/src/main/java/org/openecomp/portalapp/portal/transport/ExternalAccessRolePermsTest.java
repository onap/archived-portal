package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessPerms;
import org.openecomp.portalapp.portal.transport.ExternalAccessRolePerms;

public class ExternalAccessRolePermsTest {

	public ExternalAccessRolePerms mockExternalAccessRolePerms(){
		ExternalAccessPerms externalAccessPerms = new ExternalAccessPerms();
		externalAccessPerms.setAction("test");
		externalAccessPerms.setDescription("test");
		externalAccessPerms.setInstance("test");
		externalAccessPerms.setType("test");
		ExternalAccessRolePerms ExternalAccessRolePerms = new ExternalAccessRolePerms(externalAccessPerms, "test");
		
		return ExternalAccessRolePerms;
	}
	
	@Test
	public void externalAccessRolePermsTest(){
		ExternalAccessPerms externalAccessPerms = new ExternalAccessPerms();
		externalAccessPerms.setAction("test");
		externalAccessPerms.setDescription("test");
		externalAccessPerms.setInstance("test");
		externalAccessPerms.setType("test");
		
		ExternalAccessRolePerms ExternalAccessRolePerms = mockExternalAccessRolePerms();
		
		assertEquals(ExternalAccessRolePerms.getRole(), "test");
		assertEquals(ExternalAccessRolePerms.getPerm(), externalAccessPerms);
	}
}
