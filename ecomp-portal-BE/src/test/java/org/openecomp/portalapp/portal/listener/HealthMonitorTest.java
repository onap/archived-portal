package org.openecomp.portalapp.portal.listener;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HealthMonitorTest {

	@Test
	public void initialFlagsTest() {
		assertEquals(false, HealthMonitor.isBackEndUp());
		assertEquals(false, HealthMonitor.isFrontEndUp());
		assertEquals(false, HealthMonitor.isDatabaseUp());
		assertEquals(false, HealthMonitor.isUebUp());
	}
	
}
