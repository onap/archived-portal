package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.AppContactUs;

public class AppContactUsTest {

	public AppContactUs  mockAppContactUs(){
		AppContactUs appContactUs = new AppContactUs();
		appContactUs.setDescription("test");
		appContactUs.setContactEmail("test");
		appContactUs.setContactName("test");
		appContactUs.setUrl("test");
		appContactUs.setActiveYN("test");
				
		return appContactUs;
	}
	
	@Test
	public void mockAppContactUsTest(){
		AppContactUs appContactUs = mockAppContactUs();
		
		assertEquals(appContactUs.getDescription(), "test");
		assertEquals(appContactUs.getContactEmail(), "test");
		assertEquals(appContactUs.getContactName(), "test");
		assertEquals(appContactUs.getUrl(), "test");
		assertEquals(appContactUs.getActiveYN(), "test");
		
	}
}
