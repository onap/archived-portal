package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPRole;

public class EPRoleTest {

	public EPRole mockEPRole(){
		EPRole epRole = new EPRole();
		
		epRole.setName("test");
		epRole.setActive(false);
		epRole.setPriority(1);
		epRole.setAppId((long)1);
		epRole.setAppRoleId((long)1);
		   
		return epRole;
	}
	
	@Test
	public void epRoleTest(){
		EPRole epRole = mockEPRole();
		
		assertEquals(epRole.getName(), "test");
		assertEquals(epRole.getActive(), false);
		assertEquals(epRole.getPriority().toString(),"1");
		assertEquals(epRole.getAppId(), new Long(1));
		assertEquals(epRole.getAppRoleId(), new Long(1));
		
		assertEquals(epRole.toString(), "[Id = null, name = test]");

		
	}
}
