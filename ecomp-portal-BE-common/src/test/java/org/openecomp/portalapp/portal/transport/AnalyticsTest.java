package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.Analytics;

public class AnalyticsTest {

	public Analytics mockAnalytics(){
		Analytics analytics = new Analytics();
				
		analytics.setAction("test");
		analytics.setPage("test");
		analytics.setFunction("test");
		analytics.setUserid("test");
		analytics.setType("test");
		
		return analytics;
	}
	
	@Test
	public void analyticsTest(){
		Analytics analytics = mockAnalytics();
		
		assertEquals(analytics.getAction(), "test");
		assertEquals(analytics.getPage(), "test");
		assertEquals(analytics.getFunction(), "test");
		assertEquals(analytics.getUserid(), "test");
		assertEquals(analytics.getType(), "test");
		
	}
}
