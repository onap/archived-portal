package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPWidgetsManualSortPreference;

public class EPWidgetsManualSortPreferenceTest {

	public EPWidgetsManualSortPreference mockEPWidgetsManualSortPreference(){
		EPWidgetsManualSortPreference epWidgetsManualSortPreference = new EPWidgetsManualSortPreference();
				
		epWidgetsManualSortPreference.setUserId(1);
		epWidgetsManualSortPreference.setWidgetId((long)1);
		epWidgetsManualSortPreference.setWidgetRow(1);
		epWidgetsManualSortPreference.setWidgetCol(1);
		epWidgetsManualSortPreference.setWidgetWidth(1);
		epWidgetsManualSortPreference.setWidgetHeight(1);
				
		return epWidgetsManualSortPreference;
	}
	
	@Test
	public void epWidgetsManualSortPreferenceTest(){
		EPWidgetsManualSortPreference epWidgetsManualSortPreference = mockEPWidgetsManualSortPreference();
		
		assertEquals(epWidgetsManualSortPreference.getWidgetId(), new Long(1));
		assertEquals(epWidgetsManualSortPreference.getUserId(), 1);
		assertEquals(epWidgetsManualSortPreference.getWidgetRow(), 1);
		assertEquals(epWidgetsManualSortPreference.getWidgetCol(), 1);
		assertEquals(epWidgetsManualSortPreference.getWidgetHeight(), 1);
		assertEquals(epWidgetsManualSortPreference.getWidgetWidth(), 1);
		
		
	}
}
