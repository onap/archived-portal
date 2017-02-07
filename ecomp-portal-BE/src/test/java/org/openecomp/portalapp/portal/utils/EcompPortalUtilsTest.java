package org.openecomp.portalapp.portal.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EcompPortalUtilsTest {

	
	@Test
	public void legitimateUserIdFailureTest() {
		assertEquals(false, EcompPortalUtils.legitimateUserId("1#@23456"));
	}
}
