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
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.UserRolesController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.service.UserRolesService;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;
import org.openecomp.portalapp.portal.transport.AppsListWithAdminRole;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FieldsValidator.FieldName;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EcompPortalUtils.class, EPCommonSystemProperties.class, EcompAuditLog.class, SystemProperties.class})
public class UserRolesControllerTest extends MockitoTestSuite {

	String userid = "ab1234";

	@Mock
	UserRolesService userRolesService;
	
	@Mock
	SearchService searchService;
	
	@Mock
	AuditService auditService;
	
	@Mock
	AdminRolesService adminRolesService;

	@Mock
	FieldsValidator fieldsValidator;

	@InjectMocks
	UserRolesController userRolesController = new UserRolesController();

	@Mock
	private DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	EPUser epuser;

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void putAppWithUserRoleRequestTest() {

		FieldsValidator actualFieldsValidator = null;

		AppWithRolesForUser appWithRolesForUser = new AppWithRolesForUser();
		List<RoleInAppForUser> listofRoles = new ArrayList<RoleInAppForUser>();

		appWithRolesForUser.setOrgUserId("guest");
		appWithRolesForUser.setAppId((long) 550);
		appWithRolesForUser.setAppName("D2 Services Analytics Dashboard");
		appWithRolesForUser.setAppRoles(listofRoles);

		RoleInAppForUser roleInAppForUser = new RoleInAppForUser();
		roleInAppForUser.setIsApplied(false);
		roleInAppForUser.setRoleId((long) 1);
		roleInAppForUser.setRoleName("System Administrator");

		RoleInAppForUser roleInAppForUser1 = new RoleInAppForUser();
		roleInAppForUser1.setIsApplied(true);
		roleInAppForUser1.setRoleId((long) 16);
		roleInAppForUser1.setRoleName("Standard User");

		listofRoles.add(roleInAppForUser);
		listofRoles.add(roleInAppForUser1);
		EPUser user = mockUser.mockEPUser();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userRolesService.putUserAppRolesRequest(appWithRolesForUser, user))
				.thenReturn(expectedFieldValidator);

		actualFieldsValidator = userRolesController.putAppWithUserRoleRequest(mockedRequest, appWithRolesForUser,
				mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());

	}
	
	@Test
	public void getPhoneBookSearchResultBadPermissionsTest() {
		String searchString = "test";
		String actualResult = null;
		String expectedResult = null;
		
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(searchService.searchUsersInPhoneBook(searchString)).thenReturn(actualResult);
		
		actualResult = userRolesController.getPhoneBookSearchResult(mockedRequest, searchString, mockedResponse);
		assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	public void getPhoneBookSearchResultValidationTest() {
		String searchString = " t";
		String actualResult = null;
		String expectedResult = null;
		
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(searchService.searchUsersInPhoneBook(searchString)).thenReturn(actualResult);
		
		actualResult = userRolesController.getPhoneBookSearchResult(mockedRequest, searchString, mockedResponse);
		assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	public void getPhoneBookSearchResultTest() {
		String searchString = "test";
		String actualResult = null;
		String expectedResult = null;
		
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(searchService.searchUsersInPhoneBook(searchString)).thenReturn(actualResult);
		
		actualResult = userRolesController.getPhoneBookSearchResult(mockedRequest, searchString, mockedResponse);
		assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	public void getAppsWithAdminRoleStateForUserTest(){
		
		String orgUserId = "hb123f";
		AppsListWithAdminRole actualResult = new AppsListWithAdminRole();
		AppsListWithAdminRole expectedResult = new AppsListWithAdminRole();
		EPUser user = mockUser.mockEPUser();
	//	PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.getAppsWithAdminRoleStateForUser(orgUserId)).thenReturn(actualResult);
		
		actualResult = userRolesController.getAppsWithAdminRoleStateForUser(mockedRequest, orgUserId, mockedResponse);
		assertEquals(expectedResult, actualResult);
	}
	
	/*@Test
	public void getAppsWithAdminRoleStateForUserBadRequestTest(){
		
		String orgUserId = "hb123f";
		AppsListWithAdminRole actualResult = null;
		AppsListWithAdminRole expectedResult = null;
		EPUser user = mockUser.mockEPUser();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.when(EcompPortalUtils.legitimateUserId(orgUserId)).thenReturn(false);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.getAppsWithAdminRoleStateForUser(orgUserId)).thenReturn(actualResult);
		
		actualResult = userRolesController.getAppsWithAdminRoleStateForUser(mockedRequest, orgUserId, mockedResponse);
		assertEquals(expectedResult, actualResult);
	}*/
	
	@Test
	public void putAppsWithAdminRoleStateForUserBadStatusCodeTest(){
		FieldsValidator actualFieldsValidator = null;
		AppsListWithAdminRole newAppsListWithAdminRoles = new  AppsListWithAdminRole();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fieldNames = new ArrayList<FieldName>();
		expectedFieldsValidator.setErrorCode(null);
		expectedFieldsValidator.setFields(fieldNames);
		expectedFieldsValidator.setHttpStatusCode((long)200);
		
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		
		actualFieldsValidator = userRolesController.putAppsWithAdminRoleStateForUser(mockedRequest, newAppsListWithAdminRoles, mockedResponse);
		assertEquals(expectedFieldsValidator, actualFieldsValidator);
	}
	
	
	/*@Test
	public void putAppsWithAdminRoleStateForUserTest() {
		
		FieldsValidator actualFieldsValidator = null;
		AppsListWithAdminRole newAppsListWithAdminRoles = new  AppsListWithAdminRole();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompAuditLog.class);
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN)).thenReturn("1400");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP)).thenReturn("1400");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP)).thenReturn("1400");
		Mockito.when(SystemProperties.getProperty(SystemProperties.MDC_TIMER)).thenReturn("1400");
		
		actualFieldsValidator = userRolesController.putAppsWithAdminRoleStateForUser(mockedRequest, newAppsListWithAdminRoles, mockedResponse);

		System.out.println(actualFieldsValidator);
		Mockito.when(searchService.searchUsersInPhoneBook(searchString)).thenReturn(actualResult);
		
		actualResult = userRolesController.getPhoneBookSearchResult(mockedRequest, searchString, mockedResponse);
		assertEquals(expectedResult, actualResult);
	
	}*/
	
	/*@Test
	public void putAppsWithAdminRoleStateForUserTest(){
		FieldsValidator actualFieldsValidator = null;
		AppsListWithAdminRole newAppsListWithAdminRoles = new  AppsListWithAdminRole();
		FieldsValidator expectedFieldsValidator = new FieldsValidator();
		List<FieldName> fieldNames = new ArrayList<FieldName>();
		expectedFieldsValidator.setErrorCode(null);
		expectedFieldsValidator.setFields(fieldNames);
		expectedFieldsValidator.setHttpStatusCode((long)200);
		
		EPUser user = mockUser.mockEPUser();
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompAuditLog.class);
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.setAppsWithAdminRoleStateForUser(newAppsListWithAdminRoles));
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

	//	Mockito.call(auditService.logActivity(auditLog, null));
//		Mockito.when(SystemProperties.getProperty(EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN)).thenReturn("1400");
//		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP)).thenReturn("1400");
//		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP)).thenReturn("1400");
//		Mockito.when(SystemProperties.getProperty(SystemProperties.MDC_TIMER)).thenReturn("1400");
		
		actualFieldsValidator = userRolesController.putAppsWithAdminRoleStateForUser(mockedRequest, newAppsListWithAdminRoles, mockedResponse);
		assertEquals(expectedFieldsValidator, actualFieldsValidator);
	}*/
}
