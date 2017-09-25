/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.openecomp.portalapp.portal.controller.FunctionalMenuController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRole;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRolesList;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FieldsValidator.FieldName;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SystemProperties.class)
public class FunctionalMenuControllerTest extends MockitoTestSuite {

	String userid = "ab1234";

	@Mock
	FunctionalMenuService functionalMenuService = new FunctionalMenuServiceImpl();

	@InjectMocks
	FunctionalMenuController functionalMenuController = new FunctionalMenuController();

	@Mock
	private DataAccessService dataAccessService;
	
	@Mock
	SearchService searchService;

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@InjectMocks
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	EPUser epuser;

	MockEPUser mockUser = new MockEPUser();

	List<BusinessCardApplicationRolesList> appRoles = new ArrayList<BusinessCardApplicationRolesList>();
	List<BusinessCardApplicationRolesList> appRolesActual = new ArrayList<BusinessCardApplicationRolesList>();

	List<BusinessCardApplicationRole> userAppRoleList = new ArrayList<BusinessCardApplicationRole>();

	public List<BusinessCardApplicationRole> mockBusinessCardApplicationRole() {
		List<BusinessCardApplicationRole> userAppRoleList = new ArrayList<BusinessCardApplicationRole>();

		BusinessCardApplicationRole businessCardApplicationRole = new BusinessCardApplicationRole();
		businessCardApplicationRole.setRoleName("ADMIN");
		businessCardApplicationRole.setAppName("ASDC");

		BusinessCardApplicationRole businessCardApplicationRole1 = new BusinessCardApplicationRole();
		businessCardApplicationRole1.setAppName("ASDC");
		businessCardApplicationRole1.setRoleName("Tester");

		userAppRoleList.add(businessCardApplicationRole);
		userAppRoleList.add(businessCardApplicationRole1);
		return userAppRoleList;
	}

	public List<BusinessCardApplicationRolesList> mockBusinessCardApplicationRolesList() {
		List<BusinessCardApplicationRolesList> appRolesActual = new ArrayList<BusinessCardApplicationRolesList>();

		BusinessCardApplicationRolesList businessCardApplicationRolesList = new BusinessCardApplicationRolesList();
		businessCardApplicationRolesList.setAppName("ASDC");

		List<String> roleNames = new ArrayList<String>();
		roleNames.add("ADMIN");
		roleNames.add("Tester");
		businessCardApplicationRolesList.setRoleNames(roleNames);

		appRolesActual.add(businessCardApplicationRolesList);
		return appRolesActual;
	}

	@Test
	public void getAppListTestIfAppAlredyExistsBusinessCardApplicationRolesList() throws IOException {

		userAppRoleList = mockBusinessCardApplicationRole();
		appRolesActual = mockBusinessCardApplicationRolesList();

		Mockito.when(functionalMenuService.getUserAppRolesList(userid)).thenReturn(userAppRoleList);

		appRoles = functionalMenuController.getAppList(mockedRequest, userid);

		assertEquals(appRolesActual.size(), appRoles.size());
		assertEquals(appRolesActual.get(0).getAppName(), appRoles.get(0).getAppName());
		assertEquals(appRolesActual.get(0).getRoleNames(), appRoles.get(0).getRoleNames());

	}

	@Test
	public void getAppListTestIfAppDoesnotExistsInBusinessCardApplicationRolesList() throws IOException {

		userAppRoleList = mockBusinessCardApplicationRole();
		BusinessCardApplicationRole businessCardApplicationRole = new BusinessCardApplicationRole();
		businessCardApplicationRole.setAppName("CCD");
		businessCardApplicationRole.setRoleName("ADMIN");
		userAppRoleList.add(businessCardApplicationRole);
		appRolesActual = mockBusinessCardApplicationRolesList();
		BusinessCardApplicationRolesList businessCardApplicationRolesList = new BusinessCardApplicationRolesList();
		businessCardApplicationRolesList.setAppName("CCD");
		List<String> roleNames1 = new ArrayList<String>();
		roleNames1.add("ADMIN");
		businessCardApplicationRolesList.setRoleNames(roleNames1);
		appRolesActual.add(businessCardApplicationRolesList);
		Mockito.when(functionalMenuService.getUserAppRolesList(userid)).thenReturn(userAppRoleList);
		appRoles = functionalMenuController.getAppList(mockedRequest, userid);
		assertEquals(appRolesActual.size(), appRoles.size());
		assertEquals(appRolesActual.get(0).getAppName(), appRoles.get(0).getAppName());
		assertEquals(appRolesActual.get(0).getRoleNames(), appRoles.get(0).getRoleNames());
		assertEquals(appRolesActual.get(1).getAppName(), appRoles.get(1).getAppName());
		assertEquals(appRolesActual.get(1).getRoleNames(), appRoles.get(1).getRoleNames());

	}

	@Test
	public void regenerateAncestorTableTest() {
		EPUser user = mockUser.mockEPUser();

		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator acutualFieldValidator = null;
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(!adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(functionalMenuService.regenerateAncestorTable()).thenReturn(expectedFieldValidator);
		acutualFieldValidator = functionalMenuController.regenerateAncestorTable(mockedRequest, mockedResponse);
		assertTrue(acutualFieldValidator.equals(expectedFieldValidator));
	}
	
	@Test
	public void getFunctionalMenuStaticInfoExceptionTest(){
		String fnMenuStaticactualResponse = null;
		String fnMenuStaticexpectedResponse = null;
		String orgUserIdStr = null;
		
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(searchService.searchUserByUserId(orgUserIdStr)).thenReturn(user);	
		fnMenuStaticactualResponse = functionalMenuController.getFunctionalMenuStaticInfo(mockedRequest, mockedResponse);
		
		assertEquals(fnMenuStaticactualResponse, fnMenuStaticexpectedResponse);
		
	}
	
	@Test
	public void getFunctionalMenuStaticInfoTest(){
		String fnMenuStaticactualResponse = null;
		String fnMenuStaticexpectedResponse = "{\"firstName\":\"test\",\"lastName\":\"test\",\"last_login\":\"09/08/2017 03:48:13-0400\",\"userId\":\"guestT\",\"email\":\"test\"}";
		String orgUserIdStr = null;
		
		EPUser user = mockUser.mockEPUser();
		user.setEmail("test");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(searchService.searchUserByUserId(orgUserIdStr)).thenReturn(user);	
		fnMenuStaticactualResponse = functionalMenuController.getFunctionalMenuStaticInfo(mockedRequest, mockedResponse);
		assertEquals(fnMenuStaticactualResponse.length(), fnMenuStaticexpectedResponse.length());		
	}
	
	@Test
	public void getMenuItemsForAuthUserNullTest(){
		List<FunctionalMenuItem> actualmenuItems = null;
		List<FunctionalMenuItem> expectedmenuItems = null;
		
		actualmenuItems = functionalMenuController.getMenuItemsForAuthUser(mockedRequest, mockedResponse);
		assertEquals(actualmenuItems, expectedmenuItems);
	}
	
	@Test
	public void getMenuItemsForAuthUserIsSuperAdminTest(){
		List<FunctionalMenuItem> actualmenuItems = null;
		List<FunctionalMenuItem> expectedmenuItems = new ArrayList<FunctionalMenuItem>();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		
		actualmenuItems = functionalMenuController.getMenuItemsForAuthUser(mockedRequest, mockedResponse);
		assertEquals(actualmenuItems, expectedmenuItems);
	}

	@Test
	public void getMenuItemsForAuthUserTest(){
		List<FunctionalMenuItem> actualmenuItems = null;
		List<FunctionalMenuItem> expectedmenuItems = new ArrayList<FunctionalMenuItem>();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		
		actualmenuItems = functionalMenuController.getMenuItemsForAuthUser(mockedRequest, mockedResponse);
		assertEquals(actualmenuItems, expectedmenuItems);
	}
	
	@Test
	public void getFunctionalMenuItemDetailsBadPermissionTest(){
		Integer menuId = 1234;
		FunctionalMenuItem actualmenuItem = null;
		FunctionalMenuItem expectedmenuItem = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);	
		
		actualmenuItem = functionalMenuController.getFunctionalMenuItemDetails(mockedRequest, menuId, mockedResponse);
		assertEquals(actualmenuItem, expectedmenuItem);
	}
	
	@Test
	public void getFunctionalMenuItemDetailsTest(){
		Integer menuId = 1234;
		FunctionalMenuItem actualmenuItem = null;
		FunctionalMenuItem expectedmenuItem = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(functionalMenuService.getFunctionalMenuItemDetails(menuId)).thenReturn(actualmenuItem);	
		
		actualmenuItem = functionalMenuController.getFunctionalMenuItemDetails(mockedRequest, menuId, mockedResponse);
		assertEquals(actualmenuItem, expectedmenuItem);
	}
	
	@Test
	public void getMenuItemsForEditingTest(){
		List<FunctionalMenuItem> actualMenuItems = null;
		List<FunctionalMenuItem> expectedMenuItems = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(functionalMenuService.getFunctionalMenuItems(true)).thenReturn(actualMenuItems);	
		
		actualMenuItems = functionalMenuController.getMenuItemsForEditing(mockedRequest, mockedResponse);
		assertEquals(actualMenuItems, expectedMenuItems);
	}
	
	@Test
	public void getMenuItemsForEditingBadPermissionsTest(){
		List<FunctionalMenuItem> actualMenuItems = null;
		List<FunctionalMenuItem> expectedMenuItems = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(functionalMenuService.getFunctionalMenuItems(true)).thenReturn(actualMenuItems);	
		
		actualMenuItems = functionalMenuController.getMenuItemsForEditing(mockedRequest, mockedResponse);
		assertEquals(actualMenuItems, expectedMenuItems);
	}
	
	@Test
	public void getMenuItemsForNotificationsTest(){
		List<FunctionalMenuItem> actualMenuItems = null;
		List<FunctionalMenuItem> expectedMenuItems = new ArrayList<FunctionalMenuItem>();
		Mockito.when(functionalMenuService.getFunctionalMenuItemsForNotificationTree(false)).thenReturn(actualMenuItems);	
		
		actualMenuItems = functionalMenuController.getMenuItemsForNotifications(mockedRequest, mockedResponse);
		assertEquals(actualMenuItems, expectedMenuItems);
	}
	
	@Test
	public void getMenuItemsForAppTest(){
		Integer appId = 1234;
		List<FunctionalMenuItem> actualMenuItems = null;
		List<FunctionalMenuItem> expectedMenuItems = null;
		Mockito.when(functionalMenuService.getFunctionalMenuItemsForApp(appId)).thenReturn(actualMenuItems);
		
		actualMenuItems = functionalMenuController.getMenuItemsForApp(mockedRequest, appId);
		assertEquals(actualMenuItems, expectedMenuItems);		
	}
	
	@Test
	public void getMenuItemsForUserTest(){
		String orgUserId ="test";
		List<FunctionalMenuItem> actualMenuItems = null;
		List<FunctionalMenuItem> expectedMenuItems = null;
		Mockito.when(functionalMenuService.getFunctionalMenuItemsForUser(orgUserId)).thenReturn(actualMenuItems);                
		
		actualMenuItems = functionalMenuController.getMenuItemsForUser(mockedRequest, orgUserId);
		assertEquals(actualMenuItems, expectedMenuItems);		
	}
	
	@Test
	public void createFunctionalMenuItemTest(){
		FieldsValidator	actualFieldsValidator = new FieldsValidator();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldsValidator.setHttpStatusCode((long) 200);
		expectedFieldsValidator.setFields(fields);
		expectedFieldsValidator.setErrorCode(null);
		FunctionalMenuItemWithRoles menuItemJson = new FunctionalMenuItemWithRoles();
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(functionalMenuService.createFunctionalMenuItem(menuItemJson)).thenReturn(expectedFieldsValidator);
		actualFieldsValidator = functionalMenuController.createFunctionalMenuItem(mockedRequest, menuItemJson, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void createFunctionalMenuItemBadPermisssionsTest(){
		FieldsValidator	actualFieldsValidator = null;
		FieldsValidator expectedFieldsValidator = null;
		FunctionalMenuItemWithRoles menuItemJson = new FunctionalMenuItemWithRoles();
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(functionalMenuService.createFunctionalMenuItem(menuItemJson)).thenReturn(expectedFieldsValidator);
		actualFieldsValidator = functionalMenuController.createFunctionalMenuItem(mockedRequest, menuItemJson, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void getFavoritesForUserTest(){
		List<FavoritesFunctionalMenuItemJson> actualFavoritesFunctionalMenuItemsJson = null;
		List<FavoritesFunctionalMenuItemJson> expectedFunctionalMenuItemsJson = new ArrayList<FavoritesFunctionalMenuItemJson>();
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(functionalMenuService.getFavoriteItems(user.getId())).thenReturn(expectedFunctionalMenuItemsJson);
		actualFavoritesFunctionalMenuItemsJson = functionalMenuController.getFavoritesForUser(mockedRequest, mockedResponse);
		assertEquals(actualFavoritesFunctionalMenuItemsJson, expectedFunctionalMenuItemsJson);
	}
	
	@Test
	public void deleteFavoriteItemTest(){
		Long userId = (long)1;
		Long menuId = (long)1;
		FieldsValidator	actualFieldsValidator = new FieldsValidator();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldsValidator.setHttpStatusCode((long) 200);
		expectedFieldsValidator.setFields(fields);
		expectedFieldsValidator.setErrorCode(null);
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(functionalMenuService.removeFavoriteItem(user.getId(), menuId)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.deleteFavoriteItem(mockedRequest, menuId, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void addFavoriteItemTest(){
		FavoritesFunctionalMenuItem menuItemJson = new FavoritesFunctionalMenuItem();
		FieldsValidator	actualFieldsValidator = new FieldsValidator();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldsValidator.setHttpStatusCode((long) 200);
		expectedFieldsValidator.setFields(fields);
		expectedFieldsValidator.setErrorCode(null);
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(functionalMenuService.setFavoriteItem(menuItemJson)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.addFavoriteItem(mockedRequest, menuItemJson, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void getMenuItemsTest(){
		List<FunctionalMenuItem> actualFunctionalMenuItems = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> expectedFunctionalMenuItems = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> menuItems = new ArrayList<FunctionalMenuItem>();
		Mockito.when(functionalMenuService.getFunctionalMenuItems()).thenReturn(menuItems);
		actualFunctionalMenuItems = functionalMenuController.getMenuItems(mockedRequest, mockedResponse);
		assertEquals(actualFunctionalMenuItems, expectedFunctionalMenuItems);
	}
	
	@Test
	public void deleteFunctionalMenuItemTest(){
		Long menuId = (long)1;
		FieldsValidator	actualFieldsValidator = new FieldsValidator();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldsValidator.setHttpStatusCode((long) 200);
		expectedFieldsValidator.setFields(fields);
		expectedFieldsValidator.setErrorCode(null);
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(functionalMenuService.deleteFunctionalMenuItem(menuId)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.deleteFunctionalMenuItem(mockedRequest, menuId, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void deleteFunctionalMenuItemBadPermissionsTest(){
		Long menuId = (long)1;
		FieldsValidator	actualFieldsValidator = null;
		FieldsValidator expectedFieldsValidator = null;
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(functionalMenuService.deleteFunctionalMenuItem(menuId)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.deleteFunctionalMenuItem(mockedRequest, menuId, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void editFunctionalMenuItemTest(){
		FunctionalMenuItemWithRoles menuItemJson = new FunctionalMenuItemWithRoles();
		FieldsValidator	actualFieldsValidator = new FieldsValidator();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldsValidator.setHttpStatusCode((long) 200);
		expectedFieldsValidator.setFields(fields);
		expectedFieldsValidator.setErrorCode(null);
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true); 
		Mockito.when(functionalMenuService.editFunctionalMenuItem(menuItemJson)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.editFunctionalMenuItem(mockedRequest, menuItemJson, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void editFunctionalMenuItemBadPermissionsTest(){
		FunctionalMenuItemWithRoles menuItemJson = new FunctionalMenuItemWithRoles();
		FieldsValidator	actualFieldsValidator = null;
		FieldsValidator expectedFieldsValidator = null;
		EPUser user = mockUser.mockEPUser();	
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false); 
		Mockito.when(functionalMenuService.editFunctionalMenuItem(menuItemJson)).thenReturn(actualFieldsValidator);
		actualFieldsValidator = functionalMenuController.editFunctionalMenuItem(mockedRequest, menuItemJson, mockedResponse);
		assertEquals(actualFieldsValidator, expectedFieldsValidator);
	}
	
	@Test
	public void getECOMPTitleTest(){
		PortalRestResponse<String> actualportalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		expectedportalRestResponse.setMessage("success");
		expectedportalRestResponse.setResponse("Portal");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME)).thenReturn("Portal");
		actualportalRestResponse = functionalMenuController.getECOMPTitle(mockedRequest, mockedResponse);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}
	
}
