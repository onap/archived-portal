package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EpNotificationItemExtVO;

public class EpNotificationItemExtVOTest {

	public EpNotificationItemExtVO mockEpNotificationItemExtVO(){
		EpNotificationItemExtVO epNotificationItemExtVO = new EpNotificationItemExtVO();
		
		//epNotificationItemExtVO.setActiveYn("abc");
		epNotificationItemExtVO.setMsgHeader("test");
		epNotificationItemExtVO.setMsgDescription("test");
		epNotificationItemExtVO.setPriority(1);
		epNotificationItemExtVO.setCreatorId(1);
		epNotificationItemExtVO.setLoginId("test");
		
		return epNotificationItemExtVO;
	}
	
	@Test
	public void epNotificationItemExtVOTest(){
		EpNotificationItemExtVO epNotificationItemExtVO = mockEpNotificationItemExtVO();
		
		assertEquals(epNotificationItemExtVO.getMsgHeader(), "test");
		assertEquals(epNotificationItemExtVO.getMsgDescription(), "test");
		assertEquals(epNotificationItemExtVO.getPriority().toString(), "1");
		assertEquals(epNotificationItemExtVO.getCreatorId().toString(), "1");
		assertEquals(epNotificationItemExtVO.getLoginId(), "test");
	}
}
