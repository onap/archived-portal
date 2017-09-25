package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserAppsSortPreference;

public class EPUserAppsSortPreferenceTest {

	public EPUserAppsSortPreference mockEPUserAppsSortPreferenceTest(){

		EPUserAppsSortPreference epUserAppsSortPreference = new EPUserAppsSortPreference();
		epUserAppsSortPreference.setUserId(1);
		epUserAppsSortPreference.setSortPref("test");
		epUserAppsSortPreference.setId((long)1);
		epUserAppsSortPreference.setCreated(new Date());
		epUserAppsSortPreference.setModified(new Date());
		epUserAppsSortPreference.setModifiedId((long)1);
		epUserAppsSortPreference.setRowNum((long)1);
		epUserAppsSortPreference.setAuditTrail(null);
		
		return epUserAppsSortPreference;
	}
	
	@Test
	public void epUserAppsSortPreferenceTest() {
		EPUserAppsSortPreference epUserAppsSortPreference = mockEPUserAppsSortPreferenceTest();
		
		assertEquals(epUserAppsSortPreference.getUserId(), 1);
		assertEquals(epUserAppsSortPreference.getSortPref(), "test");
		assertEquals(epUserAppsSortPreference.getId(), new Long(1));
		assertEquals(epUserAppsSortPreference.getCreated(), new Date());
		assertEquals(epUserAppsSortPreference.getModified(), new Date());
		assertEquals(epUserAppsSortPreference.getModifiedId(), new Long(1));
		assertEquals(epUserAppsSortPreference.getRowNum(), new Long(1));
		assertEquals(epUserAppsSortPreference.getAuditTrail(), null);
		
		
	}
	
}
