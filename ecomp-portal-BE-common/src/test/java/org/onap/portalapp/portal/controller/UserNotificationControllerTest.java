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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.UserNotificationController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.onap.portalapp.portal.service.UserNotificationService;
import org.onap.portalapp.portal.service.UserNotificationServiceImpl;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.EpNotificationItemVO;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserUtils.class)
public class UserNotificationControllerTest {

	@Mock
	FunctionalMenuService functionalMenuService = new FunctionalMenuServiceImpl();

	@Mock
	UserNotificationService userNotificationService = new UserNotificationServiceImpl();

	@InjectMocks
	UserNotificationController userNotificationController = new UserNotificationController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void getMenuIdRoleIdTest() {
		List<FunctionalMenuRole> expectedMenuRoleList = new ArrayList<FunctionalMenuRole>();
		FunctionalMenuRole functionalMenuRole = new FunctionalMenuRole();
		functionalMenuRole.setId(new Integer(99999999));
		functionalMenuRole.setMenuId((long) 137);
		functionalMenuRole.setAppId(new Integer(456));
		functionalMenuRole.setRoleId(new Integer(6214));
		expectedMenuRoleList.add(functionalMenuRole);
		List<FunctionalMenuRole> actualFunctionalMenuRoleList = null;
		Mockito.when(functionalMenuService.getFunctionalMenuRole()).thenReturn(expectedMenuRoleList);
		actualFunctionalMenuRoleList = userNotificationController.getMenuIdRoleId(mockedRequest, mockedResponse);
		assertTrue(actualFunctionalMenuRoleList.equals(expectedMenuRoleList));

	}

	@Test
	public void getNotificationsTest() {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EpNotificationItem> expectedEpNotificationList = new ArrayList<EpNotificationItem>();
		EpNotificationItem epNotificationItem = new EpNotificationItem();
		epNotificationItem.setNotificationId((long) 200);
		expectedEpNotificationList.add(epNotificationItem);
		PortalRestResponse<List<EpNotificationItem>> expectedportalRestResponse = new PortalRestResponse<List<EpNotificationItem>>();
		expectedportalRestResponse.setMessage("success");
		expectedportalRestResponse.setResponse(expectedEpNotificationList);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<List<EpNotificationItem>> actualPortalRestResponse = null;
		Mockito.when(userNotificationService.getNotifications(user.getId())).thenReturn(expectedEpNotificationList);
		actualPortalRestResponse = userNotificationController.getNotifications(mockedRequest, mockedResponse);
		assertTrue(expectedportalRestResponse.equals(actualPortalRestResponse));

	}

	@Test
	public void getNotificationsCatchesExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EpNotificationItem> expectedEpNotificationList = null;
		PortalRestResponse<List<EpNotificationItem>> expectedportalRestResponse = new PortalRestResponse<List<EpNotificationItem>>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(expectedEpNotificationList);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<List<EpNotificationItem>> actualPortalRestResponse = null;
		Mockito.when(userNotificationService.getNotifications(user.getId())).thenThrow(new NullPointerException());
		actualPortalRestResponse = userNotificationController.getNotifications(mockedRequest, mockedResponse);
		assertTrue(expectedportalRestResponse.equals(actualPortalRestResponse));
	}

	@Test
	public void getAdminNotificationsTest() {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EpNotificationItemVO> actualEpNotificationsList = new ArrayList<EpNotificationItemVO>();
		List<EpNotificationItemVO> expectedEpNotificationsList = new ArrayList<EpNotificationItemVO>();
		EpNotificationItemVO epNotificationItemVO = new EpNotificationItemVO();
		epNotificationItemVO.setId((long) 1);
		expectedEpNotificationsList.add(epNotificationItemVO);
		Mockito.when(userNotificationService.getAdminNotificationVOS(Matchers.anyLong())).thenReturn(expectedEpNotificationsList);
		actualEpNotificationsList = userNotificationController.getAdminNotifications(mockedRequest, mockedResponse);
		assertTrue(actualEpNotificationsList.equals(expectedEpNotificationsList));
	}

	@Test
	public void saveTestWhenNotificationIsNull() throws Exception {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setMessage("FAILURE");
		expectedPortalRestResponse.setResponse("Notification Header cannot be null or empty");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);

		EpNotificationItem notificationItem = null;
		actualPortalRestResponse = userNotificationController.save(mockedRequest, mockedResponse, notificationItem);
		assertTrue(actualPortalRestResponse.equals(expectedPortalRestResponse));
	}

	@Test
	public void saveTestWhenEndTimeIsGreater() throws Exception {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setMessage("FAILURE");
		expectedPortalRestResponse.setResponse("End Time should be greater than  start time");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		EpNotificationItem notificationItem = new EpNotificationItem();
		notificationItem.setNotificationId((long) 1);
		notificationItem.setMsgHeader("Test");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 1);
		Date currentDatePlusOne = c.getTime();
		notificationItem.setStartTime(currentDatePlusOne);
		notificationItem.setEndTime(currentDate);

		actualPortalRestResponse = userNotificationController.save(mockedRequest, mockedResponse, notificationItem);
		assertTrue(actualPortalRestResponse.equals(expectedPortalRestResponse));

	}

	@Test
	public void saveTestWhenNoRoleIDExists() throws Exception {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setMessage("FAILURE");
		expectedPortalRestResponse.setResponse("No Roles Ids Exist for the selected Roles");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		EpNotificationItem notificationItem = new EpNotificationItem();
		notificationItem.setNotificationId((long) 1);
		notificationItem.setMsgHeader("Test");
		notificationItem.setIsForAllRoles("N");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 1);
		Date currentDatePlusOne = c.getTime();
		notificationItem.setStartTime(currentDate);
		notificationItem.setEndTime(currentDatePlusOne);
		List<Long> roleList = new ArrayList<Long>();
		notificationItem.setRoleIds(roleList);
		actualPortalRestResponse = userNotificationController.save(mockedRequest, mockedResponse, notificationItem);
		assertTrue(actualPortalRestResponse.equals(expectedPortalRestResponse));
	}

	@Test
	public void saveTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		HttpSession session = mockedRequest.getSession();
		session.setAttribute("user", user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setMessage("SUCCESS");
		expectedPortalRestResponse.setResponse("");
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		EpNotificationItem notificationItem = new EpNotificationItem();
		notificationItem.setNotificationId((long) 1);
		notificationItem.setMsgHeader("Test");
		notificationItem.setIsForAllRoles("Y");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 1);
		Date currentDatePlusOne = c.getTime();
		notificationItem.setStartTime(currentDate);
		notificationItem.setEndTime(currentDatePlusOne);
		List<Long> roleList = new ArrayList<Long>();
		Long role1 = (long) 1;
		roleList.add(role1);
		notificationItem.setRoleIds(roleList);
		HttpServletRequest request = mockitoTestSuite.getMockedRequest();
		PowerMockito.mockStatic(UserUtils.class);
		Mockito.when(UserUtils.getUserIdAsLong(request)).thenReturn((long) 1);
		Mockito.when(userNotificationService.saveNotification(notificationItem)).thenReturn("Test");
		actualPortalRestResponse = userNotificationController.save(mockedRequest, mockedResponse, notificationItem);
		assertTrue(actualPortalRestResponse.equals(expectedPortalRestResponse));
	}

}
