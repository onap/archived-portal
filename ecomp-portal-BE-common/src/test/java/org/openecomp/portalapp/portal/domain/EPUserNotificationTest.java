package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserNotification;

public class EPUserNotificationTest {

	public EPUserNotification mockEPUserNotification(){
		
		EPUserNotification epUserNotification = new EPUserNotification();
				
		epUserNotification.setUserId((long)1);
		epUserNotification.setNotificationId((long)1);
		epUserNotification.setViewed("test");
		epUserNotification.setUpdateTime(new Date());		
		
		return epUserNotification;
	}
	
	@Test
	public void epUserNotificationTest(){
		EPUserNotification epUserNotification = mockEPUserNotification();
		
		assertEquals(epUserNotification.getUserId(), new Long(1));
		assertEquals(epUserNotification.getNotificationId(), new Long(1));
		assertEquals(epUserNotification.getViewed(), "test");
	//	assertEquals(epUserNotification.getUpdateTime(), new Date());
		
	}
}
