package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserAppsManualSortPreference;

public class EPUserAppsManualSortPreferenceTest {

	public EPUserAppsManualSortPreference mockEPUserAppsManualSortPreference(){
		
		EPUserAppsManualSortPreference epUserAppsManualSortPreference = new EPUserAppsManualSortPreference();
		epUserAppsManualSortPreference.setUserId(1);
		epUserAppsManualSortPreference.setAppId((long)1);
		epUserAppsManualSortPreference.setAppManualSortOrder(1);
		
		return epUserAppsManualSortPreference;
	}
	
	@Test
	public void epUserAppsManualSortPreferenceTest(){
	
		EPUserAppsManualSortPreference epUserAppsManualSortPreference = mockEPUserAppsManualSortPreference();
		
		assertEquals(epUserAppsManualSortPreference.getUserId(), 1);
		assertEquals(epUserAppsManualSortPreference.getAppId(), new Long(1));
		assertEquals(epUserAppsManualSortPreference.getAppManualSortOrder(), 1);
		
	}
}
