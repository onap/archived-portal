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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserNotification;
import org.onap.portalapp.portal.domain.EcompAppRole;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.EpNotificationItemVO;
import org.onap.portalapp.portal.transport.EpRoleNotificationItem;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class UserNotificationServiceImplTest {
	
	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	UserNotificationServiceImpl userNotificationServiceImpl =  new UserNotificationServiceImpl();
	
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void getNotificationsTest() {
		EPUser user = mockUser.mockEPUser();
		Map<String, String> params = new HashMap<>();
		params.put("user_id", String.valueOf(user.getId()));
		List<EpNotificationItem> mockNotificationList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getNotifications", params, null)).thenReturn(mockNotificationList);
		List<EpNotificationItem> notificationList = userNotificationServiceImpl.getNotifications(user.getId());
		assertEquals(notificationList, mockNotificationList); 
	}
	
	@Test
	public void getNotificationHistoryVOTest() {
		EPUser user = mockUser.mockEPUser();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", String.valueOf(user.getId()));
		List<EpNotificationItemVO> mockNotificationListVO = new  ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getNotificationHistoryVO",
				params, null)).thenReturn(mockNotificationListVO);
		List<EpNotificationItemVO> notificationListVO = userNotificationServiceImpl.getNotificationHistoryVO(user.getId());
		assertEquals(notificationListVO,mockNotificationListVO); 
	}
	
	@Test
	public void getAdminNotificationVOSTest() {
		EPUser user = mockUser.mockEPUser();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", String.valueOf(user.getId()));
		List<EpNotificationItemVO> mockAdminNotificationListItemVO = new  ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAdminNotificationHistoryVO",
				params, null)).thenReturn(mockAdminNotificationListItemVO);
		List<EpNotificationItemVO> adminNotificationListItemVO = userNotificationServiceImpl.getAdminNotificationVOS(user.getId());
		assertEquals(adminNotificationListItemVO,mockAdminNotificationListItemVO); 
	}
	
	@Test
	public void getNotificationRolesTest() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("notificationId", Long.toString(1l));
		List<EpRoleNotificationItem> mockRoleNotifList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getNotificationRoles",
				params, null)).thenReturn(mockRoleNotifList);
		List<EpRoleNotificationItem> roleNotifList = userNotificationServiceImpl.getNotificationRoles(1l);
		assertEquals(roleNotifList,mockRoleNotifList); 
	}
	
	@Test
	public void getAppRoleListTest() {
		List<EcompAppRole> mockAppRoleList = new ArrayList<>();
		Mockito.when(dataAccessService
				.executeNamedQuery("getEpNotificationAppRoles", null, null)).thenReturn(mockAppRoleList);
		List<EcompAppRole> appRoleList = userNotificationServiceImpl.getAppRoleList();
		assertEquals(mockAppRoleList, appRoleList);
	}
	
	@Test
	public void setNotificationReadTest() {
		EPUserNotification mockUserNotification = new EPUserNotification();
		mockUserNotification.setNotificationId(1l);
		mockUserNotification.setId(1l);
		mockUserNotification.setUpdateTime(new Date());
		mockUserNotification.setViewed("Y");
		mockUserNotification.setUserId((long) 1);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockUserNotification, null);
		userNotificationServiceImpl.setNotificationRead(1l, 1);
	}
	
	@Test
	public void saveNotificationTest() throws Exception {
		EpNotificationItem epNotificationItem = new EpNotificationItem();
		List<Long> roleIdList =  new ArrayList<>();
		Long roleId = new Long(1l);
		Long roleId2 = new Long(16l);
		roleIdList.add(roleId);
		roleIdList.add(roleId2);
		epNotificationItem.setIsForAllRoles("N");
		epNotificationItem.setRoleIds(roleIdList);
		epNotificationItem.setNotificationId(1l);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(epNotificationItem, null);
		userNotificationServiceImpl.saveNotification(epNotificationItem);
	}
	
	@Test
	public void getUsersByOrgIdsTest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("OrgIds", "test");
		List<EPUser> mockUserList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getUsersByOrgIdsNotifications",
				params, null)).thenReturn(mockUserList);
		List<String> orgIdsList =  new ArrayList<>();
		String orgId = "test";
		orgIdsList.add(orgId);
		List<EPUser> userList = userNotificationServiceImpl.getUsersByOrgIds(orgIdsList);
		assertEquals(userList, mockUserList);
	}
	
	@Test
	public void getMessageRecipientsTest() {
		Map<String, String> params = new HashMap<>();
		params.put("notificationId", Long.toString(1l));
		List<String> mockActiveUsers =  new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("messageRecipients",
				params, null)).thenReturn(mockActiveUsers);
		List<String> activeUsers = userNotificationServiceImpl.getMessageRecipients(1l);
		assertEquals(activeUsers, mockActiveUsers);
	}
	
	@Test
	public void deleteNotificationsFromEpNotificationTableTest() {
		Map<String, String> params = new HashMap<String, String>();
		Mockito.when(dataAccessService
				.executeNamedQuery("deleteNotificationsFromEpUserNotificationTable", params, null)).thenReturn(null);
		userNotificationServiceImpl.deleteNotificationsFromEpNotificationTable();
	}
	
	@Test
	public void deleteNotificationsFromEpUserNotificationTable() {
		Map<String, String> params = new HashMap<String, String>();
		Mockito.when(dataAccessService
				.executeNamedQuery("deleteNotificationsFromEpUserNotificationTable", params, null)).thenReturn(null);
		userNotificationServiceImpl.deleteNotificationsFromEpUserNotificationTable();
	}
	
	@Test
	public void deleteNotificationsFromEpRoleNotificationTable() {
		Map<String, String> params = new HashMap<String, String>();
		Mockito.when(dataAccessService
				.executeNamedQuery("deleteNotificationsFromEpRoleNotificationTable", params, null)).thenReturn(null);
		userNotificationServiceImpl.deleteNotificationsFromEpRoleNotificationTable();
	}
}
