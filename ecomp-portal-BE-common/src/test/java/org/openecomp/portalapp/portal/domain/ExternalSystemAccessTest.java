package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.ExternalSystemAccess;

public class ExternalSystemAccessTest {
	
	public ExternalSystemAccess mockExternalSystemAccess(){
		ExternalSystemAccess externalSystemAccess = new ExternalSystemAccess("test", false);
	
		externalSystemAccess.setKey("test");
		externalSystemAccess.setAccessValue(false);
		
		return externalSystemAccess;
	}

	@Test
	public void externalSystemAccessTest(){
		ExternalSystemAccess externalSystemAccess = mockExternalSystemAccess();
		
		assertEquals(externalSystemAccess.getKey(), "test");
		assertEquals(externalSystemAccess.getAccessValue(), false);
	}
}

