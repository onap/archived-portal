package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessUserRoleDetail;
import org.openecomp.portalapp.portal.transport.ExternalRoleDescription;

public class ExternalAccessUserRoleDetailTest {

	public ExternalAccessUserRoleDetail mockExternalAccessUserRoleDetail(){
		
		ExternalRoleDescription externalRoleDescription = new ExternalRoleDescription();
		externalRoleDescription.setId("test");
		externalRoleDescription.setName("test");
		externalRoleDescription.setActive("test");
		externalRoleDescription.setPriority("test");
		externalRoleDescription.setAppId("test");
		externalRoleDescription.setAppRoleId("test");
			    
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail = new ExternalAccessUserRoleDetail("test", externalRoleDescription);
		
		externalAccessUserRoleDetail.setName("test");
		externalAccessUserRoleDetail.setDescription(externalRoleDescription);
		return externalAccessUserRoleDetail;
	}
	
	@Test
	public void externalAccessUserRoleDetailTest(){
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail = mockExternalAccessUserRoleDetail();
		
		ExternalRoleDescription externalRoleDescription1 = new ExternalRoleDescription();
		externalRoleDescription1.setId("test");
		externalRoleDescription1.setName("test");
		externalRoleDescription1.setActive("test");
		externalRoleDescription1.setPriority("test");
		externalRoleDescription1.setAppId("test");
		externalRoleDescription1.setAppRoleId("test");
			    
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail1 = new ExternalAccessUserRoleDetail("test", externalRoleDescription1);
		
		assertEquals(externalAccessUserRoleDetail.getName(), externalAccessUserRoleDetail1.getName());
		assertEquals(externalAccessUserRoleDetail.getDescription(), externalAccessUserRoleDetail1.getDescription());
		assertEquals(externalAccessUserRoleDetail.hashCode(), externalAccessUserRoleDetail1.hashCode());
		assertTrue(externalAccessUserRoleDetail.equals(externalAccessUserRoleDetail1));
	}
}
