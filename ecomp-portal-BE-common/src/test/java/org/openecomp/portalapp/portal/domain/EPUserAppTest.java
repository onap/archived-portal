package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUserApp;

public class EPUserAppTest {

	public EPUserApp mockEPUserApp(){
		
		EPApp epApp = new EPApp();
		epApp.setName("test");
		epApp.setImageUrl("test");
		epApp.setDescription("test");
		epApp.setNotes("test");
		epApp.setUrl("test");
		epApp.setAlternateUrl("test");
		epApp.setAppRestEndpoint("test");
		epApp.setMlAppName("test");
		epApp.setMlAppAdminId("test");
		epApp.setMotsId((long)1);
		epApp.setUsername("test");
		epApp.setAppPassword("test");
			
		
		//Role
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setActive(false);
		epRole.setPriority(1);
		epRole.setAppId((long)1);
		epRole.setAppRoleId((long)1);
		
		EPUserApp user = new EPUserApp();
		user.setUserId((long)1);
		user.setApp(epApp);
		user.setRole(epRole);
		user.setPriority((short)32767);
		
		
		return user;
	}
	
	@Test
	public void userTest(){
		EPUserApp user = mockEPUserApp();
		
		EPApp epApp = new EPApp();
		epApp.setName("test");
		epApp.setImageUrl("test");
		epApp.setDescription("test");
		epApp.setNotes("test");
		epApp.setUrl("test");
		epApp.setAlternateUrl("test");
		epApp.setAppRestEndpoint("test");
		epApp.setMlAppName("test");
		epApp.setMlAppAdminId("test");
		epApp.setMotsId((long)1);
		epApp.setUsername("test");
		epApp.setAppPassword("test");
		user.setApp(epApp);
		
		//Role
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setActive(false);
		epRole.setPriority(1);
		epRole.setAppId((long)1);
		epRole.setAppRoleId((long)1);
		
		
        assertEquals(user.getUserId(),Long.valueOf(1));
		assertEquals(user.getApp(), epApp); 
		assertEquals(user.getPriority().getClass(), Short.class);
	
		assertEquals(user.toString(), "[u: 1; a: null, r: null; appRoleId: 1]");
		
		assertEquals(user.hashCode(), user.hashCode());
		
		
		}
}
