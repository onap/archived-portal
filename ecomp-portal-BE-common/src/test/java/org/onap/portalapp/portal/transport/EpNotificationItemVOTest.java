/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 */
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class EpNotificationItemVOTest {
	
	private static final String TEST="test";
	private static final Integer ID=1;
	private static final Long EP_ID=1l;
	private static final Date  DATE=new Date();
	private static final Character CHARACTER='a';

	public EpNotificationItemVO mockEpNotificationItemVO(){
		EpNotificationItemVO epNotificationItemVO = new EpNotificationItemVO();
				
		epNotificationItemVO.setNotificationId(ID);
		epNotificationItemVO.setIsForOnlineUsers(CHARACTER);
		epNotificationItemVO.setIsForAllRoles(CHARACTER);
		epNotificationItemVO.setActiveYn(CHARACTER);
		epNotificationItemVO.setMsgHeader(TEST);
		epNotificationItemVO.setMsgDescription(TEST);
		epNotificationItemVO.setMsgSource(TEST);
		epNotificationItemVO.setStartTime(DATE);
		epNotificationItemVO.setEndTime(DATE);
		epNotificationItemVO.setPriority(1);
		epNotificationItemVO.setCreatorId(1);
		epNotificationItemVO.setCreatedDate(DATE);
		epNotificationItemVO.setLoginId(TEST);
		epNotificationItemVO.setNotificationHyperlink(TEST);
		epNotificationItemVO.setId(EP_ID);
		epNotificationItemVO.setCreated(DATE);
		epNotificationItemVO.setModified(DATE);
		epNotificationItemVO.setCreatedId(EP_ID);
		epNotificationItemVO.setModifiedId(EP_ID);
		epNotificationItemVO.setRowNum(EP_ID);
		
		epNotificationItemVO.setAuditUserId(EP_ID);
		epNotificationItemVO.setAuditTrail(null);
			
		
		 return epNotificationItemVO;
	}
	
	@Test
	public void epNotificationItemVOTest(){
		EpNotificationItemVO epNotification = mockEpNotificationItemVO();
		
		EpNotificationItemVO epNotificationItemVO =new EpNotificationItemVO();
		
		epNotificationItemVO.setNotificationId(epNotification.getNotificationId());
		epNotificationItemVO.setIsForOnlineUsers(epNotification.getIsForOnlineUsers());
		epNotificationItemVO.setIsForAllRoles(epNotification.getIsForAllRoles());
		epNotificationItemVO.setActiveYn(epNotification.getActiveYn());
		epNotificationItemVO.setMsgHeader(epNotification.getMsgHeader());
		epNotificationItemVO.setMsgDescription(epNotification.getMsgDescription());
		epNotificationItemVO.setMsgSource(epNotification.getMsgSource());
		epNotificationItemVO.setStartTime(epNotification.getStartTime());
		epNotificationItemVO.setEndTime(epNotification.getEndTime());
		epNotificationItemVO.setPriority(epNotification.getPriority());
		epNotificationItemVO.setCreatorId(epNotification.getCreatorId());
		epNotificationItemVO.setCreatedDate(epNotification.getCreatedDate());
		epNotificationItemVO.setLoginId(epNotification.getLoginId());
		epNotificationItemVO.setNotificationHyperlink(epNotification.getNotificationHyperlink());
		epNotificationItemVO.setId(epNotification.getId());
		epNotificationItemVO.setCreated(epNotification.getCreated());
		epNotificationItemVO.setModified(epNotification.getModified());
		epNotificationItemVO.setCreatedId(epNotification.getCreatedId());
		epNotificationItemVO.setModifiedId(epNotification.getModifiedId());
		epNotificationItemVO.setRowNum(epNotification.getRowNum());
		
		epNotificationItemVO.setAuditUserId(epNotification.getAuditUserId());
		epNotificationItemVO.setAuditTrail(epNotification.getAuditTrail());
		
		
	}
}
