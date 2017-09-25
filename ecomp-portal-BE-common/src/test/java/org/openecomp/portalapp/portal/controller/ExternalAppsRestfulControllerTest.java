package org.openecomp.portalapp.portal.controller;

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
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.ExternalAppsRestfulController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.service.EPLoginServiceImpl;
import org.openecomp.portalapp.portal.service.EPRoleService;
import org.openecomp.portalapp.portal.service.EPRoleServiceImpl;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.openecomp.portalapp.portal.service.UserNotificationService;
import org.openecomp.portalapp.portal.service.UserNotificationServiceImpl;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.DataAccessServiceImpl;
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
