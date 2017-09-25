package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EpRoleNotificationItem;

public class EpRoleNotificationItemTest {
	
	public EpRoleNotificationItem mockEpRoleNotificationItem(){
		EpRoleNotificationItem epRoleNotificationItem = new EpRoleNotificationItem();
		
		epRoleNotificationItem.setId((long)1);
		epRoleNotificationItem.setNotificationId((long)1);
		epRoleNotificationItem.setRoleId(1);
		epRoleNotificationItem.setRecvUserId(1);
		
		return epRoleNotificationItem;
	}
	
	@Test
	public void epRoleNotificationItemTest(){
		EpRoleNotificationItem epRoleNotificationItem = mockEpRoleNotificationItem();
		
		assertEquals(epRoleNotificationItem.getId(), new Long(1));
		assertEquals(epRoleNotificationItem.getNotificationId(), new Long(1));
		assertEquals(epRoleNotificationItem.getRoleId().toString(), "1");
		assertEquals(epRoleNotificationItem.getRecvUserId().toString(), "1");
	}

}
