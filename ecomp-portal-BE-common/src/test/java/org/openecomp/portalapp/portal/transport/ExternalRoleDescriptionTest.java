package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalRoleDescription;

public class ExternalRoleDescriptionTest {

	public ExternalRoleDescription mockExternalRoleDescription(){
		
		ExternalRoleDescription externalRoleDescription = new ExternalRoleDescription();
			    
	    externalRoleDescription.setId("test");
	    externalRoleDescription.setName("test");
	    externalRoleDescription.setActive("test");
	    externalRoleDescription.setPriority("test");
	    externalRoleDescription.setAppId("test");
	    externalRoleDescription.setAppRoleId("test");
	    
		return externalRoleDescription;
	}
	
	@Test
	public void externalRoleDescriptionTest(){
		ExternalRoleDescription externalRoleDescription = mockExternalRoleDescription();
		
		assertEquals(externalRoleDescription.getId(), "test");
		assertEquals(externalRoleDescription.getName(), "test");
		assertEquals(externalRoleDescription.getActive(), "test");
		assertEquals(externalRoleDescription.getPriority(), "test");
		assertEquals(externalRoleDescription.getAppId(), "test");
		assertEquals(externalRoleDescription.getAppRoleId(), "test");
	}
}
