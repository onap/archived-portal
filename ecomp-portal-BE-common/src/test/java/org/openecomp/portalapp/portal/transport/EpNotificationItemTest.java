/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.EpNotificationItem;

public class EpNotificationItemTest {
	
	public EpNotificationItem mockEpNotificationItem(){
		EpNotificationItem epNotificationItem = new EpNotificationItem();
		
		epNotificationItem.setNotificationId((long)1);
		epNotificationItem.setIsForOnlineUsers("test");
		epNotificationItem.setIsForAllRoles("test");
		epNotificationItem.setActiveYn("test");
		epNotificationItem.setMsgHeader("test");
		epNotificationItem.setMsgDescription("test");
		epNotificationItem.setMsgSource("test");
		
		epNotificationItem.setPriority((long)1);
		epNotificationItem.setCreatedId((long)1);
		epNotificationItem.setNotificationHyperlink("test");
		
		return epNotificationItem;
	}
	
	@Test
	public void epNotificationItemTest(){
		EpNotificationItem epNotificationItem = mockEpNotificationItem();
		
		EpNotificationItem epNotificationItem1 = new EpNotificationItem();
		epNotificationItem1.setNotificationId((long)1);
		epNotificationItem1.setIsForOnlineUsers("test");
		epNotificationItem1.setIsForAllRoles("test");
		epNotificationItem1.setActiveYn("test");
		epNotificationItem1.setMsgHeader("test");
		epNotificationItem1.setMsgDescription("test");
		epNotificationItem1.setMsgSource("test");
		
		epNotificationItem1.setPriority((long)1);
		epNotificationItem1.setCreatedId((long)1);
		epNotificationItem1.setNotificationHyperlink("test");
		
		assertEquals(epNotificationItem.getNotificationId(), new Long(1));
		assertEquals(epNotificationItem.getIsForOnlineUsers(), "test");
		assertEquals(epNotificationItem.getIsForAllRoles(), "test");
		assertEquals(epNotificationItem.getActiveYn(), "test");
		assertEquals(epNotificationItem.getMsgHeader(), "test");
		assertEquals(epNotificationItem.getMsgDescription(), "test");
		assertEquals(epNotificationItem.getMsgSource(), "test");
		assertEquals(epNotificationItem.getPriority(), new Long(1));
		assertEquals(epNotificationItem.getCreatedId(), new Long(1));
		assertEquals(epNotificationItem.getNotificationHyperlink(), "test");
		
		assertEquals(epNotificationItem.toString(), "EpNotificationItem [notificationId=1, isForOnlineUsers=test, isForAllRoles=test, activeYn=test, msgHeader=test, msgDescription=test, msgSource=test, startTime=null, endTime=null, priority=1, creatorId=null, createdDate=null, roles=null, roleIds=null]");
		assertEquals(epNotificationItem.hashCode(), epNotificationItem1.hashCode());
		assertTrue(epNotificationItem.equals(epNotificationItem1));
				
	}

}


