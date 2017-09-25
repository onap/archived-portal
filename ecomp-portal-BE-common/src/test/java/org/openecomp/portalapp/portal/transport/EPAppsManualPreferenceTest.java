package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EPAppsManualPreference;

public class EPAppsManualPreferenceTest {

	public EPAppsManualPreference mockEPAppsManualPreference(){
		EPAppsManualPreference epAppsManualPreference = new EPAppsManualPreference();
				
		epAppsManualPreference.setAppid((long)1);
		epAppsManualPreference.setCol(1);
		epAppsManualPreference.setHeaderText("test");
		epAppsManualPreference.setImageLink("test");
		epAppsManualPreference.setOrder(1);
		epAppsManualPreference.setRestrictedApp(false);
		epAppsManualPreference.setRow(1);
		epAppsManualPreference.setSizeX(1);
		epAppsManualPreference.setSizeY(1);
		epAppsManualPreference.setSubHeaderText("test");
		epAppsManualPreference.setUrl("test");
		epAppsManualPreference.setAddRemoveApps(false);
		
		return epAppsManualPreference;
	}
	
	@Test
	public void epAppsManualPreferenceTest(){
		
		EPAppsManualPreference epAppsManualPreference = mockEPAppsManualPreference();
		
		assertEquals(epAppsManualPreference.getAppid(), new Long(1));
		assertEquals(epAppsManualPreference.getCol(), 1);
		assertEquals(epAppsManualPreference.getHeaderText(), "test");
		assertEquals(epAppsManualPreference.getImageLink(), "test");
		assertEquals(epAppsManualPreference.getOrder(), 1);
		assertEquals(epAppsManualPreference.isRestrictedApp(), false);
		assertEquals(epAppsManualPreference.getRow(), 1);
		assertEquals(epAppsManualPreference.getSizeX(), 1);
		assertEquals(epAppsManualPreference.getSizeY(), 1);
		assertEquals(epAppsManualPreference.getSubHeaderText(), "test");
		assertEquals(epAppsManualPreference.getUrl(), "test");
		assertEquals(epAppsManualPreference.isAddRemoveApps(), false);
	}
}
