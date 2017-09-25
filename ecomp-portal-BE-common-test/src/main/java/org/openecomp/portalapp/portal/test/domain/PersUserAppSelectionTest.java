package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.PersUserAppSelection;

public class PersUserAppSelectionTest {

	public PersUserAppSelection mockPersUserAppSelection(){
		
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setUserId((long)1);
		persUserAppSelection.setAppId((long)1);
		persUserAppSelection.setStatusCode("test");
		
		return persUserAppSelection;
	}
	
	@Test
	public void persUserAppSelectionTest(){
		
		PersUserAppSelection persUserAppSelection1 = new PersUserAppSelection((long)1, (long)1, (long)1,"test");
		
		PersUserAppSelection persUserAppSelection = mockPersUserAppSelection();
		
		assertEquals(persUserAppSelection.getUserId(), new Long(1));
		assertEquals(persUserAppSelection.getAppId(), new Long(1));
		assertEquals(persUserAppSelection.getStatusCode(), "test");
		assertEquals(persUserAppSelection1.hashCode(), new PersUserAppSelection((long)1, (long)1, (long)1,"test").hashCode());
		assertEquals(persUserAppSelection1, new PersUserAppSelection((long)1, (long)1, (long)1,"test"));
		assertTrue(persUserAppSelection1.equals(new PersUserAppSelection((long)1, (long)1, (long)1,"test")));
	}
	
}
