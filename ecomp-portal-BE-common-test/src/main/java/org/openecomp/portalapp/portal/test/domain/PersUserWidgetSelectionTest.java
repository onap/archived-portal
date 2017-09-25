package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.PersUserWidgetSelection;

public class PersUserWidgetSelectionTest {

	public PersUserWidgetSelection mockPersUserWidgetSelection(){
		
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		
		persUserWidgetSelection.setUserId((long)1);
		persUserWidgetSelection.setWidgetId((long)1);
		persUserWidgetSelection.setStatusCode("test");
		
		return persUserWidgetSelection;
	}
	
	@Test
	public void persUserWidgetSelectionTest(){
		PersUserWidgetSelection persUserWidgetSelection = mockPersUserWidgetSelection();
		
		PersUserWidgetSelection persUserWidgetSelection1 = new PersUserWidgetSelection((long)1, (long)1, (long)1, "test");
		
		assertEquals(persUserWidgetSelection.getUserId(), new Long(1));
		assertEquals(persUserWidgetSelection.getWidgetId(), new Long(1));
		assertEquals(persUserWidgetSelection.getStatusCode(), "test");
		assertEquals(persUserWidgetSelection1,  new PersUserWidgetSelection((long)1, (long)1, (long)1, "test"));
		assertTrue(persUserWidgetSelection1.equals(new PersUserWidgetSelection((long)1, (long)1, (long)1,"test")));
		assertEquals(persUserWidgetSelection.hashCode(), persUserWidgetSelection1.hashCode());
	}
	
}
