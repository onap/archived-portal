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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.ExternalAppsRestfulController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPLoginServiceImpl;
import org.onap.portalapp.portal.service.EPRoleService;
import org.onap.portalapp.portal.service.EPRoleServiceImpl;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.onap.portalapp.portal.service.UserNotificationService;
import org.onap.portalapp.portal.service.UserNotificationServiceImpl;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.menu.MenuBuilder;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MDC.class, EPCommonSystemProperties.class ,EPApp.class})
public class ExternalAppsRestfulControllerTest {

	@InjectMocks
	ExternalAppsRestfulController externalAppsRestfulController = new ExternalAppsRestfulController();
	@Mock
	FunctionalMenuService functionalMenuService = new FunctionalMenuServiceImpl();

	@Mock
	EPLoginService epLoginService = new EPLoginServiceImpl();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@Mock
	UserNotificationService userNotificationService = new UserNotificationServiceImpl();

	@Mock
	EPRoleService epRoleService = new EPRoleServiceImpl();

	@Mock
	EcompPortalUtils EcompPortalUtils = new EcompPortalUtils();
	
	@Mock
	DataAccessService DataAccessService = new DataAccessServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();
	
	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setNameSpace("com.test.app");
		app.setCentralAuth(true);
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 10);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	 @Test(expected = Exception.class)
	 public void getFunctionalMenuItemsForUserIfUSerNullTest() throws
	 Exception
	 {
	 PowerMockito.mockStatic(EPCommonSystemProperties.class);
	 PowerMockito.mockStatic(MDC.class);
	 EPUser epUser = null;
	 String loginId = "guestT";
	 Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
	 Mockito.when(epLoginService.findUserWithoutPwd(loginId)).thenReturn(epUser);
	 externalAppsRestfulController.getFunctionalMenuItemsForUser(mockedRequest,
	 mockedResponse);
	 }
	
	@Test
	public void getFunctionalMenuItemsForUserIfSuperAdminTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(MDC.class);
		EPUser epUser = mockUser.mockEPUser();
		epUser.setId((long) 1);
		epUser.setLoginId("guestT");
		String loginId = "guestT";
		Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
		Mockito.when(epLoginService.findUserWithoutPwd(loginId)).thenReturn(epUser);
		List<FunctionalMenuItem> expectedList = new ArrayList<FunctionalMenuItem>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		expectedList.add(functionalMenuItem);
		Mockito.when(mockedRequest.getHeader("LoginId")).thenReturn("guestT");
		Mockito.when(adminRolesService.isSuperAdmin(epUser)).thenReturn(true);
		Mockito.when(functionalMenuService.getFunctionalMenuItems()).thenReturn(expectedList);
		List<FunctionalMenuItem> actualList = externalAppsRestfulController.getFunctionalMenuItemsForUser(mockedRequest,
				mockedResponse);
		assertNull(actualList.get(0).menuId);
	}

	@Test
	public void getFunctionalMenuItemsForUserTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(MDC.class);
		EPUser epUser = mockUser.mockEPUser();
		epUser.setId((long) 1);
		epUser.setLoginId("guestT");
		String loginId = "guestT";
		Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
		Mockito.when(epLoginService.findUserWithoutPwd(loginId)).thenReturn(epUser);
		List<FunctionalMenuItem> expectedList = new ArrayList<FunctionalMenuItem>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		expectedList.add(functionalMenuItem);
		Mockito.when(mockedRequest.getHeader("LoginId")).thenReturn("guestT");
		Mockito.when(adminRolesService.isSuperAdmin(epUser)).thenReturn(false);
		Mockito.when(functionalMenuService.getFunctionalMenuItemsForUser(epUser.getOrgUserId()))
				.thenReturn(expectedList);
		List<FunctionalMenuItem> actualList = externalAppsRestfulController.getFunctionalMenuItemsForUser(mockedRequest,
				mockedResponse);
		assertNull(actualList.get(0).menuId);
	}

	@Test(expected = Exception.class)
	public void getFavoritesForUserIfUserNullTest() throws Exception {
		List<FavoritesFunctionalMenuItemJson> favorites = new ArrayList<FavoritesFunctionalMenuItemJson>();
		FavoritesFunctionalMenuItemJson favoritesFunctionalMenuItemJson = new FavoritesFunctionalMenuItemJson();
		favorites.add(favoritesFunctionalMenuItemJson);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(MDC.class);
		Mockito.when(mockedRequest.getHeader(EPCommonSystemProperties.MDC_LOGIN_ID)).thenReturn("Login_URL");
		Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
		EPUser epUser = null;
		externalAppsRestfulController.getFavoritesForUser(mockedRequest, mockedResponse);
	}

	@Test
	public void getFavoritesForUserTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(MDC.class);
		EPUser epUser = mockUser.mockEPUser();
		epUser.setId((long) 1);
		epUser.setLoginId("guestT");
		String loginId = "guestT";
		Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
		List<FavoritesFunctionalMenuItemJson> favorites = new ArrayList<FavoritesFunctionalMenuItemJson>();
		FavoritesFunctionalMenuItemJson favoritesFunctionalMenuItemJson = new FavoritesFunctionalMenuItemJson();
		favorites.add(favoritesFunctionalMenuItemJson);
		Mockito.when(mockedRequest.getHeader(EPCommonSystemProperties.MDC_LOGIN_ID)).thenReturn("Login_URL");
		Mockito.when(MDC.get(EPCommonSystemProperties.PARTNER_NAME)).thenReturn("Test");
		Mockito.when(epLoginService.findUserWithoutPwd("Login_URL")).thenReturn(epUser);
		Mockito.when(functionalMenuService.getFavoriteItems(epUser.getId())).thenReturn(favorites);
		List<FavoritesFunctionalMenuItemJson> actaulFavorites = externalAppsRestfulController
				.getFavoritesForUser(mockedRequest, mockedResponse);
		assertEquals(actaulFavorites.size(), 1);
	}
	
	@Test(expected=NullPointerException.class)
	public void publishNotificationTest() throws Exception{
		EPApp appTest=new EPApp();
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn("RxH3983AHiyBOQmj");
		appTest.setUebKey("123456");
		String appKey="123456";
		EpNotificationItem notificationItem=new EpNotificationItem();
		List<Long> roleList = new ArrayList<Long>();
		Long role1 = (long) 1;
		roleList.add(role1);
		notificationItem.setRoleIds(roleList);
		notificationItem.setIsForAllRoles("N");
		notificationItem.setIsForOnlineUsers("N");
		notificationItem.setActiveYn("Y");
		notificationItem.setPriority(1L);
		notificationItem.setMsgHeader("testHeader");
		notificationItem.setMsgDescription("Test Description");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 1);
		Date currentDatePlusOne = c.getTime();
		notificationItem.setStartTime(currentDate);
		notificationItem.setEndTime(currentDatePlusOne);
		notificationItem.setCreatedDate(c.getTime());
		
		PortalAPIResponse actualPortalRestResponse = new PortalAPIResponse(true, appKey);
		PortalAPIResponse expectedPortalRestResponse = new PortalAPIResponse(true, appKey);
		expectedPortalRestResponse.setMessage("SUCCESS");
		expectedPortalRestResponse.setStatus("ok");
		Map<String, String> params = new HashMap<>();
		params.put("appKey", "1234567");
		
		Mockito.when(DataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", params, null)).thenReturn(null);

		Mockito.when(userNotificationService.saveNotification(notificationItem)).thenReturn("Test");
		actualPortalRestResponse = externalAppsRestfulController.publishNotification(mockedRequest, notificationItem);
		assertTrue(actualPortalRestResponse.equals(expectedPortalRestResponse));
		
	}
	
	@Test(expected=NullPointerException.class)
	public void publishNotificationTest1() throws Exception{
		EpNotificationItem notificationItem=new EpNotificationItem();
		List<Long> roleList = new ArrayList<Long>();
		Long role1 = (long) 1;
		roleList.add(role1);
		notificationItem.setRoleIds(roleList);
		notificationItem.setIsForAllRoles("N");
		notificationItem.setIsForOnlineUsers("N");
		notificationItem.setActiveYn("Y");
		notificationItem.setPriority(1L);
		notificationItem.setMsgHeader("testHeader");
		notificationItem.setMsgDescription("Test Description");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 1);
		Date currentDatePlusOne = c.getTime();
		notificationItem.setStartTime(currentDate);
		notificationItem.setEndTime(currentDatePlusOne);
		notificationItem.setCreatedDate(c.getTime());
		
		//PowerMockito.mockStatic(EPApp.class);

		
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		app.setId((long) 1);
		appList.add(app);
		
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		
		Mockito.when(DataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
		.thenReturn(appList);
		//EPApp epApp=new EPApp();
		
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn("RxH3983AHiyBOQmj");

		 externalAppsRestfulController.publishNotification(mockedRequest, notificationItem);

	}
}
