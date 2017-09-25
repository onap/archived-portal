package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EPDeleteAppsManualSortPref;

public class EPDeleteAppsManualSortPrefTest {

	public EPDeleteAppsManualSortPref mockEPDeleteAppsManualSortPref(){
		EPDeleteAppsManualSortPref epDeleteAppsManualSortPref = new EPDeleteAppsManualSortPref();
		
		epDeleteAppsManualSortPref.setAppId((long)1);
		epDeleteAppsManualSortPref.setSelect(false);
		epDeleteAppsManualSortPref.setPending(false);

		return epDeleteAppsManualSortPref;
	}
	
	@Test
	public void epDeleteAppsManualSortPrefTest(){
		EPDeleteAppsManualSortPref epDeleteAppsManualSortPref = mockEPDeleteAppsManualSortPref();
		
		assertEquals(epDeleteAppsManualSortPref.getAppId(), new Long(1));
		assertEquals(epDeleteAppsManualSortPref.isSelect(), false);
		assertEquals(epDeleteAppsManualSortPref.isPending(), false);
		
	}
}
