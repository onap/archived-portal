package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessUser;

public class ExternalAccessUserTest {

	public ExternalAccessUser mockExternalAccessUser(){
		ExternalAccessUser externalAccessUser = new ExternalAccessUser("test", "test");
		
		return externalAccessUser;
	}
	
	@Test
	public void externalAccessUserTest(){
		ExternalAccessUser externalAccessUser = mockExternalAccessUser();
		
		assertEquals(externalAccessUser.getRole(), "test");
		assertEquals(externalAccessUser.getUser(), "test");
		
	}
}
