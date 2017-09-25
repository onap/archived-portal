package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openecomp.portalapp.portal.transport.CentralApp;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.transport.CentralUserApp;

public class CentralUserAppTest {

	public CentralUserApp mockCentralUserApp(){
		CentralUserApp centralUserApp = new CentralUserApp();
				
		CentralApp app = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralRole role = new CentralRole();
		 
		centralUserApp.setUserId((long)1);
		centralUserApp.setApp(app);
		centralUserApp.setRole(role);
		centralUserApp.setPriority((short) 123);
		
		return centralUserApp;
	}
	
	@Test
	public void centralUserAppTest(){
		CentralUserApp centralUserApp = mockCentralUserApp();
		
		CentralApp app1 = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralRole role1 = new CentralRole();
		
		assertEquals(centralUserApp.getUserId(), new Long(1));
		assertEquals(centralUserApp.getPriority(), new Short((short) 123));
		assertEquals(centralUserApp.getApp(), app1);
		assertEquals(centralUserApp.getRole(), role1);
	}
}
