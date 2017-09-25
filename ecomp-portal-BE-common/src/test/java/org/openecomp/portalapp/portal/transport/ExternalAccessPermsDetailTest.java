package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessPermsDetail;

public class ExternalAccessPermsDetailTest {

	public ExternalAccessPermsDetail mockExternalAccessPermsDetail(){
		ExternalAccessPermsDetail externalAccessPermsDetail = new ExternalAccessPermsDetail();
				
		List<String> roles = new ArrayList<String>();
		
		externalAccessPermsDetail.setType("test");
		externalAccessPermsDetail.setInstance("test");
		externalAccessPermsDetail.setAction("test");
		externalAccessPermsDetail.setDescription("test");
		externalAccessPermsDetail.setRoles(roles);
		
		return externalAccessPermsDetail;
	}
	
	@Test
	public void externalAccessPermsDetailTest(){
		ExternalAccessPermsDetail externalAccessPermsDetail = mockExternalAccessPermsDetail();
		
		List<String> roles = new ArrayList<String>();
		
		assertEquals(externalAccessPermsDetail.getType(), "test");
		assertEquals(externalAccessPermsDetail.getInstance(), "test");
		assertEquals(externalAccessPermsDetail.getAction(), "test");
		assertEquals(externalAccessPermsDetail.getDescription(), "test");
		assertEquals(externalAccessPermsDetail.getRoles(), roles);
	}
}
