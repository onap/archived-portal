package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessPerms;

public class ExternalAccessPermsTest {

	public ExternalAccessPerms mockExternalAccessPerms(){
		ExternalAccessPerms externalAccessPerms = new ExternalAccessPerms();
				
		externalAccessPerms.setType("test");
		externalAccessPerms.setInstance("test");
		externalAccessPerms.setAction("test");
		externalAccessPerms.setDescription("test");
		
		return externalAccessPerms;
	}
	
	@Test
	public void externalAccessPermsTest(){
		ExternalAccessPerms externalAccessPerms = mockExternalAccessPerms();
		
		ExternalAccessPerms externalAccessPerms1 = new ExternalAccessPerms("test", "test", "test");
		ExternalAccessPerms externalAccessPerms2 = new ExternalAccessPerms("test", "test", "test", "test");
		ExternalAccessPerms externalAccessPerms3 = new ExternalAccessPerms();
		externalAccessPerms3.setType("test");
		externalAccessPerms3.setInstance("test");
		externalAccessPerms3.setAction("test");
		externalAccessPerms3.setDescription("test");
		
		assertEquals(externalAccessPerms.getType(), "test");
		assertEquals(externalAccessPerms.getInstance(), "test");
		assertEquals(externalAccessPerms.getAction(), "test");
		assertEquals(externalAccessPerms.getDescription(), "test");
		assertEquals(externalAccessPerms.hashCode(), externalAccessPerms3.hashCode());
		
		assertTrue(externalAccessPerms1.equals(new ExternalAccessPerms("test", "test", "test")));
		assertTrue(externalAccessPerms2.equals(new ExternalAccessPerms("test", "test", "test", "test")));
	}
}
