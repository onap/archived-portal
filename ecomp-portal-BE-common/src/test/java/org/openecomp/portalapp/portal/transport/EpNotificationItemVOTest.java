package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EpNotificationItemVO;

public class EpNotificationItemVOTest {

	public EpNotificationItemVO mockEpNotificationItemVO(){
		EpNotificationItemVO epNotificationItemVO = new EpNotificationItemVO();
				
		epNotificationItemVO.setNotificationId(1);
		epNotificationItemVO.setIsForOnlineUsers('a');
		epNotificationItemVO.setIsForAllRoles('a');
		epNotificationItemVO.setActiveYn('a');
		epNotificationItemVO.setMsgHeader("test");
		epNotificationItemVO.setMsgDescription("test");
		epNotificationItemVO.setMsgSource("test");
		epNotificationItemVO.setPriority(1);
		epNotificationItemVO.setCreatorId(1);
		epNotificationItemVO.setLoginId("test");
		epNotificationItemVO.setNotificationHyperlink("test");
		
		 return epNotificationItemVO;
	}
	
	@Test
	public void epNotificationItemVOTest(){
		EpNotificationItemVO epNotificationItemVO = mockEpNotificationItemVO();
		
		assertEquals(epNotificationItemVO.getNotificationId().toString(), "1");
		/*assertEquals(epNotificationItemVO.getIsForOnlineUsers(), 'a');
		assertEquals(epNotificationItemVO.getIsForAllRoles(), 'a');
		assertEquals(epNotificationItemVO.getActiveYn(), 'a');*/
		assertEquals(epNotificationItemVO.getMsgHeader(), "test");
		assertEquals(epNotificationItemVO.getMsgDescription(), "test");
		assertEquals(epNotificationItemVO.getMsgSource(), "test");
		assertEquals(epNotificationItemVO.getPriority().toString(), "1");
		assertEquals(epNotificationItemVO.getCreatorId().toString(), "1");
		assertEquals(epNotificationItemVO.getLoginId(), "test");
		assertEquals(epNotificationItemVO.getNotificationHyperlink(), "test");
	}
}
