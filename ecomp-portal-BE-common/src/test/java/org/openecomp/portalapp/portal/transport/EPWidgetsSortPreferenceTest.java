package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EPWidgetsSortPreference;

public class EPWidgetsSortPreferenceTest {
	
	public EPWidgetsSortPreference mockEPWidgetsSortPreference(){
		EPWidgetsSortPreference epWidgetsSortPreference = new EPWidgetsSortPreference();
				
		epWidgetsSortPreference.setSizeX(1);
		epWidgetsSortPreference.setSizeY(1);
		epWidgetsSortPreference.setHeaderText("test");
		epWidgetsSortPreference.setUrl("test");
		epWidgetsSortPreference.setWidgetid((long)1);
		epWidgetsSortPreference.setWidgetIdentifier("test");
		epWidgetsSortPreference.setRow(1);
		epWidgetsSortPreference.setCol(1);
		
		return epWidgetsSortPreference;
	}
	
	@Test
	public void epWidgetsSortPreferenceTest(){
		EPWidgetsSortPreference epWidgetsSortPreference = mockEPWidgetsSortPreference();
		
		assertEquals(epWidgetsSortPreference.getSizeX(), 1);
		assertEquals(epWidgetsSortPreference.getSizeY(), 1);
		assertEquals(epWidgetsSortPreference.getHeaderText(), "test");
		assertEquals(epWidgetsSortPreference.getUrl(), "test");
		assertEquals(epWidgetsSortPreference.getWidgetid(), new Long(1));
		assertEquals(epWidgetsSortPreference.getWidgetIdentifier(), "test");
		assertEquals(epWidgetsSortPreference.getRow(), 1);
		assertEquals(epWidgetsSortPreference.getRow(), 1);
		
	}
	

}
