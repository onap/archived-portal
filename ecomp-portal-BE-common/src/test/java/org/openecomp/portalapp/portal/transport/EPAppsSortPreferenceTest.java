package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EPAppsSortPreference;

public class EPAppsSortPreferenceTest {

	public EPAppsSortPreference mockEPAppsSortPreference(){
		EPAppsSortPreference epAppsSortPreference = new EPAppsSortPreference();
		
		epAppsSortPreference.setIndex(1);
		epAppsSortPreference.setValue("test");
		epAppsSortPreference.setTitle("test");
		
		return epAppsSortPreference;
	}
	
	@Test
	public void epAppsSortPreferenceTest(){
		EPAppsSortPreference epAppsSortPreference = mockEPAppsSortPreference();
		
		assertEquals(epAppsSortPreference.getIndex(), 1);
		assertEquals(epAppsSortPreference.getValue(), "test");
		assertEquals(epAppsSortPreference.getTitle(), "test");
		
	}
}
