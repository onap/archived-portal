package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPApp;

public class EPAppTest {

	public EPApp mockEPApp(){
		EPApp epApp = new EPApp();
				
		epApp.setName("test");
		epApp.setImageUrl("test");
		epApp.setDescription("test");
		epApp.setNotes("test");
		epApp.setUrl("test");
		epApp.setAlternateUrl("test");
		epApp.setAppRestEndpoint("test");
		epApp.setMlAppName("test");
		epApp.setMlAppAdminId("test");
		epApp.setMotsId((long)1);
		epApp.setUsername("test");
		epApp.setAppPassword("test");
		epApp.setOpen(false);
		epApp.setEnabled(false);
		epApp.setUebTopicName("test");
		epApp.setUebSecret("test");
		epApp.setAppType(1);
		epApp.setCentralAuth(false);
		epApp.setNameSpace("test");
		
		return epApp;
	}
	
	@Test
	public void epAppTest(){
		EPApp epApp = mockEPApp();
		
		assertEquals(epApp.getName(), "test");
		assertEquals(epApp.getImageUrl(), "test");
		assertEquals(epApp.getDescription(), "test");
		assertEquals(epApp.getNotes(), "test");
		assertEquals(epApp.getUrl(), "test");
		assertEquals(epApp.getAlternateUrl(), "test");
		assertEquals(epApp.getAppRestEndpoint(), "test");
		assertEquals(epApp.getMlAppName(), "test");
		assertEquals(epApp.getMlAppAdminId(), "test");
		assertEquals(epApp.getMotsId(), new Long(1));
		assertEquals(epApp.getUsername(), "test");
		assertEquals(epApp.getAppPassword(), "test");
		assertEquals(epApp.getOpen(), false);
		assertEquals(epApp.getEnabled(), false);
		assertEquals(epApp.getUebTopicName(), "test");
		assertEquals(epApp.getUebSecret(), "test");
		assertEquals(epApp.getAppType(), Integer.valueOf(1));
		assertEquals(epApp.getCentralAuth(), false);
		assertEquals(epApp.getNameSpace(), "test");

	}
}
