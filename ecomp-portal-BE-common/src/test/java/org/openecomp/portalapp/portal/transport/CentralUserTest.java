package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.transport.CentralUser;
import org.openecomp.portalapp.portal.transport.CentralUserApp;

public class CentralUserTest {

	public CentralUser mockCentralUser(){
		Set<CentralUserApp> userApps = new HashSet<CentralUserApp>();
		Set<CentralRole> pseudoRoles = new HashSet<CentralRole>();
		CentralUser centralUser = new CentralUser((long)1, null, null, (long)1, (long)1, (long)1, (long)1,
				(long)1, "test", "test", "test", "test", "test",
				"test", "test", (long)1, "test", "test", "test",
				"test", "test", "test", "test", "test", "test", "test",
				"test", "test", "test", "test",
				"test", "test", "test", "test", "test",
				"test", "test", "test", "test", "test",
				"test", "test", "test", "test", null,
				false, false, (long)1, (long)1, false, "test", userApps, pseudoRoles);
		
		return centralUser;
	}
	
	@Test
	public void centralRoleTest(){
		CentralUser centralUser = mockCentralUser();
		
		Set<CentralUserApp> userApps = new HashSet<CentralUserApp>();
		Set<CentralRole> pseudoRoles = new HashSet<CentralRole>();
		CentralUser centralUser1 = new CentralUser((long)1, null, null, (long)1, (long)1, (long)1, (long)1,
				(long)1, "test", "test", "test", "test", "test",
				"test", "test", (long)1, "test", "test", "test",
				"test", "test", "test", "test", "test", "test", "test",
				"test", "test", "test", "test",
				"test", "test", "test", "test", "test",
				"test", "test", "test", "test", "test",
				"test", "test", "test", "test", null,
				false, false, (long)1, (long)1, false, "test", userApps, pseudoRoles);
		
		
		assertEquals(centralUser, centralUser1);
		assertEquals(centralUser.hashCode(), centralUser1.hashCode());
		assertTrue(centralUser.equals(centralUser1));
	}
}
