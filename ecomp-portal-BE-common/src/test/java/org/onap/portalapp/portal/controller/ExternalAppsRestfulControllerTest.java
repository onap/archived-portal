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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.ExternalAppsRestfulController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
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
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MDC.class, EPCommonSystemProperties.class })
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
}
