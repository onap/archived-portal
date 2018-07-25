/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
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
 * 
 */package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class EpNotificationItemTest {
	
	private static final String TEST="test";
	private static Date date=new Date();
	
	public EpNotificationItem mockEpNotificationItem(){
		EpNotificationItem epNotificationItem = new EpNotificationItem();
		Set<EpRoleNotificationItem> list=new HashSet<>();
		EpRoleNotificationItem item=new EpRoleNotificationItem();
		item.setRoleId(3);
		item.setId(1l);
		list.add(item);
	List<Long> roleIds=	list.stream().map(e->e.getId()).collect(Collectors.toList());
		epNotificationItem.setNotificationId((long)1);
		epNotificationItem.setIsForOnlineUsers(TEST);
		epNotificationItem.setIsForAllRoles(TEST);
		epNotificationItem.setActiveYn(TEST);
		epNotificationItem.setMsgHeader(TEST);
		epNotificationItem.setMsgDescription(TEST);
		epNotificationItem.setMsgSource(TEST);
		
		epNotificationItem.setPriority((long)1);
		epNotificationItem.setCreatedId((long)1);
		epNotificationItem.setNotificationHyperlink(TEST);
		epNotificationItem.setStartTime(date);
		epNotificationItem.setEndTime(date);
		epNotificationItem.setCreatedDate(date);
		epNotificationItem.setCreatorId(1l);
		epNotificationItem.setRoles(list);
		epNotificationItem.setRoleIds(roleIds);
		
		
		return epNotificationItem;
	}
	
	
	
	@Test
	public void epNotificationItemTest(){
		EpNotificationItem epNotificationItem1 = mockEpNotificationItem();
		
		EpNotificationItem epNotificationItem = new EpNotificationItem();	
		epNotificationItem.setNotificationId(epNotificationItem1.getNotificationId());
		epNotificationItem.setIsForOnlineUsers(epNotificationItem1.getIsForOnlineUsers());
		epNotificationItem.setIsForAllRoles(epNotificationItem1.getIsForAllRoles());
		epNotificationItem.setActiveYn(epNotificationItem1.getActiveYn());
		epNotificationItem.setMsgHeader(epNotificationItem1.getMsgHeader());
		epNotificationItem.setMsgDescription(epNotificationItem1.getMsgDescription());
		epNotificationItem.setMsgSource(epNotificationItem1.getMsgSource());

		epNotificationItem.setPriority((long)1);
		epNotificationItem.setCreatedId(epNotificationItem1.getCreatedId());
		epNotificationItem.setNotificationHyperlink(epNotificationItem1.getNotificationHyperlink());
		epNotificationItem.setStartTime(new Date());
		epNotificationItem.setEndTime(epNotificationItem1.getEndTime());
		epNotificationItem.setCreatedDate(epNotificationItem1.getCreatedDate());
		epNotificationItem.setCreatorId(epNotificationItem1.getCreatorId());
		epNotificationItem.setRoles(epNotificationItem1.getRoles());
		epNotificationItem.setRoleIds(epNotificationItem1.getRoleIds());
		epNotificationItem.setStartTime(epNotificationItem1.getStartTime());
		assertNotNull(epNotificationItem.toString());
		//assertNotEquals(epNotificationItem.toString(), "EpNotificationItem [notificationId=1, isForOnlineUsers=test, isForAllRoles=test, activeYn=test, msgHeader=test, msgDescription=test, msgSource=test, startTime=null, endTime=null, priority=1, creatorId=null, createdDate=null, roles=null, roleIds=null]");
		assertEquals(epNotificationItem.hashCode(), epNotificationItem1.hashCode());
		EpNotificationItem epNotificationItem2 =epNotificationItem;
		assertTrue(epNotificationItem.equals(epNotificationItem2));
		assertTrue(epNotificationItem.equals(epNotificationItem1));
					
		assertTrue(epNotificationItem.equals(epNotificationItem1));
		assertFalse(epNotificationItem.equals(null));
		epNotificationItem.setStartTime(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setRoles(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setRoleIds(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setPriority(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setNotificationId(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setMsgSource(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setMsgHeader(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setMsgDescription(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setIsForOnlineUsers(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setIsForAllRoles(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setEndTime(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setCreatedId(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setCreatedDate(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		epNotificationItem.setActiveYn(null);
		assertFalse(epNotificationItem.equals(epNotificationItem1));
		
		
	}

}


