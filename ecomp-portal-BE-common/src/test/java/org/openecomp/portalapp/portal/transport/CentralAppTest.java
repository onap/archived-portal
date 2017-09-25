package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.CentralApp;

public class CentralAppTest {

	public CentralApp mockCentralApp(){
		CentralApp centralApp = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
	

	/*	public CentralApp(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, String name,
				String imageUrl, String description, String notes, String url, String alternateUrl, String restEndpoint,
				String mlAppName, String mlAppAdminId, String motsId, String appPassword, String open, String enabled,
				byte[] thumbnail, String username, String uebKey, String uebSecret, String uebTopicName)*/
		
		centralApp.setId((long)1);
		centralApp.setCreatedId((long)1);
		centralApp.setModifiedId((long)1);
		centralApp.setRowNum((long)1);
		centralApp.setName("test");
		centralApp.setImageUrl("test");
		centralApp.setDescription("test");
		centralApp.setNotes("test");
		centralApp.setUrl("test");
		centralApp.setAlternateUrl("test");
		centralApp.setRestEndpoint("test");
		centralApp.setMlAppName("test");
		centralApp.setMlAppAdminId("test");
		centralApp.setMotsId("test");
		centralApp.setAppPassword("test");
		centralApp.setOpen("test");
		centralApp.setEnabled("test");
		centralApp.setUsername("test");
		centralApp.setUebKey("test");
		centralApp.setUebSecret("test");
		centralApp.setUebTopicName("test");
		
		return centralApp;
	}
	
	@Test
	public void centralAppTest(){
		CentralApp centralApp = mockCentralApp();
		
		CentralApp centralApp1 =  new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		/*centralApp1.setId((long)1);
		centralApp1.setCreatedId((long)1);
		centralApp1.setModifiedId((long)1);
		centralApp1.setRowNum((long)1);
		centralApp1.setName("test");
		centralApp1.setImageUrl("test");
		centralApp1.setDescription("test");
		centralApp1.setNotes("test");
		centralApp1.setUrl("test");
		centralApp1.setAlternateUrl("test");
		centralApp1.setRestEndpoint("test");
		centralApp1.setMlAppName("test");
		centralApp1.setMlAppAdminId("test");
		centralApp1.setMotsId("test");
		centralApp1.setAppPassword("test");
		centralApp1.setOpen("test");
		centralApp1.setEnabled("test");
		centralApp1.setUsername("test");
		centralApp1.setUebKey("test");
		centralApp1.setUebSecret("test");
		centralApp1.setUebTopicName("test");*/
		
		assertEquals(centralApp.getId(), new Long(1));
		assertEquals(centralApp.getCreatedId(), new Long(1));
		assertEquals(centralApp.getModifiedId(), new Long(1));
		assertEquals(centralApp.getRowNum(), new Long(1));
		assertEquals(centralApp.getName(), "test");
		assertEquals(centralApp.getImageUrl(), "test");
		assertEquals(centralApp.getDescription(), "test");
		assertEquals(centralApp.getNotes(), "test");
		assertEquals(centralApp.getUrl(), "test");
		assertEquals(centralApp.getAlternateUrl(), "test");
		assertEquals(centralApp.getRestEndpoint(), "test");
		assertEquals(centralApp.getMlAppName(), "test");
		assertEquals(centralApp.getMlAppAdminId(), "test");
		assertEquals(centralApp.getMotsId(), "test");
		assertEquals(centralApp.getAppPassword(), "test");
		assertEquals(centralApp.getOpen(), "test");
		assertEquals(centralApp.getEnabled(), "test");
		assertEquals(centralApp.getUsername(), "test");
		assertEquals(centralApp.getUebKey(), "test");
		assertEquals(centralApp.getUebSecret(), "test");
		assertEquals(centralApp.getUebTopicName(), "test");
		
		assertTrue(centralApp.equals(centralApp1));
		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
		/*	assertTrue(centralApp.equals(new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test")));*/
	}
	
}
