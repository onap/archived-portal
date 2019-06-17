/*-
  * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.onap.portalapp.portal.controller.AppsController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.AdminUserApplications;
import org.onap.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.onap.portalapp.portal.domain.AppsResponse;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.domain.UserRoles;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.EPAppCommonServiceImpl;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.EPLeftMenuService;
import org.onap.portalapp.portal.service.EPLeftMenuServiceImpl;
import org.onap.portalapp.portal.transport.EPAppsManualPreference;
import org.onap.portalapp.portal.transport.EPAppsSortPreference;
import org.onap.portalapp.portal.transport.EPDeleteAppsManualSortPref;
import org.onap.portalapp.portal.transport.EPWidgetsSortPreference;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class,AppUtils.class, EPUserUtils.class, MediaType.class})
public class AppsControllerTest extends MockitoTestSuite{

	@InjectMocks
	AppsController appsController = new AppsController();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@Mock
	EPAppService appService = new EPAppCommonServiceImpl();

	@Mock
	EPLeftMenuService leftMenuService = new EPLeftMenuServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	AppUtils appUtils = new AppUtils();

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getUserAppsTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EcompApp> expectedEcompApps = new ArrayList<EcompApp>();

		EcompApp ecompApp = new EcompApp();
		ecompApp.setId((long) 1);
		ecompApp.setName("Test_app");
		ecompApp.setUrl("Test_URL");
		ecompApp.setUebKey("Test_key");
		ecompApp.setAlternateUrl("Test_alt_URL");
		expectedEcompApps.add(ecompApp);
		List<EcompApp> actualEcompApps = new ArrayList<EcompApp>();
		Mockito.when(appService.transformAppsToEcompApps(appService.getUserApps(user))).thenReturn(expectedEcompApps);
		actualEcompApps = appsController.getUserApps(mockedRequest, mockedResponse);
		assertEquals(expectedEcompApps, actualEcompApps);
	}

	@Test
	public void getUserAppsNoUserTest() {
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.transformAppsToEcompApps(appService.getUserApps(user))).thenReturn(null);
		assertNull(appsController.getUserApps(mockedRequest, mockedResponse));

	}

	@Test
	public void getUserAppsExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.transformAppsToEcompApps(appService.getUserApps(user))).thenThrow(nullPointerException);
		assertNull(appsController.getUserApps(mockedRequest, mockedResponse));

	}

	@Test
	public void getPersUserAppsIfUserIsAdminTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EcompApp> expectedEcompApps = new ArrayList<EcompApp>();

		EcompApp ecompApp = new EcompApp();
		ecompApp.setId((long) 1);
		ecompApp.setName("Test_app");
		ecompApp.setUrl("Test_URL");
		ecompApp.setUebKey("Test_key");
		ecompApp.setAlternateUrl("Test_alt_URL");
		expectedEcompApps.add(ecompApp);
		List<EcompApp> actualEcompApps = new ArrayList<EcompApp>();

		List<EPApp> expectedApps = new ArrayList<EPApp>();

		EPApp app = new EPApp();

		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);

		expectedApps.add(app);

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getPersAdminApps(user)).thenReturn(expectedApps);

		Mockito.when(appService.transformAppsToEcompApps(expectedApps)).thenReturn(expectedEcompApps);
		actualEcompApps = appsController.getPersUserApps(mockedRequest, mockedResponse);
		assertEquals(expectedEcompApps, actualEcompApps);
	}

	@Test
	public void getPersUserAppsIfUserNotAdminTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EcompApp> expectedEcompApps = new ArrayList<EcompApp>();

		EcompApp ecompApp = new EcompApp();
		ecompApp.setId((long) 1);
		ecompApp.setName("Test_app");
		ecompApp.setUrl("Test_URL");
		ecompApp.setUebKey("Test_key");
		ecompApp.setAlternateUrl("Test_alt_URL");
		expectedEcompApps.add(ecompApp);
		List<EcompApp> actualEcompApps = new ArrayList<EcompApp>();

		List<EPApp> expectedApps = new ArrayList<EPApp>();

		EPApp app = new EPApp();

		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);

		expectedApps.add(app);

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(appService.getPersUserApps(user)).thenReturn(expectedApps);
		Mockito.when(appService.transformAppsToEcompApps(expectedApps)).thenReturn(expectedEcompApps);
		actualEcompApps = appsController.getPersUserApps(mockedRequest, mockedResponse);
		assertEquals(expectedEcompApps, actualEcompApps);
	}

	@Test
	public void getPersUserAppsIfUserNullTest() throws IOException {
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		assertNull(appsController.getPersUserApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getPersUserAppsExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getPersAdminApps(user)).thenThrow(nullPointerException);
		assertNull(appsController.getPersUserApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAdminAppsIfNotAdminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		assertNull(appsController.getAdminApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAdminAppsTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppIdAndNameTransportModel> expectedAdminApps = new ArrayList<AppIdAndNameTransportModel>();
		AppIdAndNameTransportModel appIdAndNameTransportModel = new AppIdAndNameTransportModel();
		appIdAndNameTransportModel.setId((long) 1);
		appIdAndNameTransportModel.setName("Test_app");
		expectedAdminApps.add(appIdAndNameTransportModel);
		List<AppIdAndNameTransportModel> actualAdminApps = new ArrayList<AppIdAndNameTransportModel>();
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAdminApps(user)).thenReturn(expectedAdminApps);
		actualAdminApps = appsController.getAdminApps(mockedRequest, mockedResponse);
		assertEquals(actualAdminApps, expectedAdminApps);

	}

	@Test
	public void getAdminAppsExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAdminApps(user)).thenThrow(nullPointerException);
		assertNull(appsController.getAdminApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsForSuperAdminAndAccountAdminifOnlyAccountAdminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		assertNull(appsController.getAppsForSuperAdminAndAccountAdmin(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsForSuperAdminAndAccountAdminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppIdAndNameTransportModel> expectedAdminApps = new ArrayList<AppIdAndNameTransportModel>();
		AppIdAndNameTransportModel appIdAndNameTransportModel = new AppIdAndNameTransportModel();
		appIdAndNameTransportModel.setId((long) 1);
		appIdAndNameTransportModel.setName("Test_app");
		expectedAdminApps.add(appIdAndNameTransportModel);
		List<AppIdAndNameTransportModel> actualAdminApps = new ArrayList<AppIdAndNameTransportModel>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAppsForSuperAdminAndAccountAdmin(user)).thenReturn(expectedAdminApps);
		actualAdminApps = appsController.getAppsForSuperAdminAndAccountAdmin(mockedRequest, mockedResponse);
		assertEquals(actualAdminApps, expectedAdminApps);

	}

	@Test
	public void getAppsForSuperAdminExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(appService.getAppsForSuperAdminAndAccountAdmin(user)).thenThrow(nullPointerException);
		assertNull(appsController.getAppsForSuperAdminAndAccountAdmin(mockedRequest, mockedResponse));
	}

	@Test
	public void putUserAppsSortingManualTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPAppsManualPreference> ePAppsManualPreference = new ArrayList<EPAppsManualPreference>();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(appService.saveAppsSortManual(ePAppsManualPreference, user)).thenReturn(expectedFieldValidator);
		actualFieldValidator = appsController.putUserAppsSortingManual(mockedRequest, ePAppsManualPreference,
				mockedResponse);
		assertEquals(actualFieldValidator, expectedFieldValidator);
	}

	@Test
	public void putUserAppsSortingManualExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPAppsManualPreference> ePAppsManualPreference = new ArrayList<EPAppsManualPreference>();
		Mockito.when(appService.saveAppsSortManual(ePAppsManualPreference, user)).thenThrow(nullPointerException);
		assertNull(appsController.putUserAppsSortingManual(mockedRequest, ePAppsManualPreference, mockedResponse));
	}

	@Test
	public void putUserWidgetsSortManualTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPWidgetsSortPreference> ePWidgetsSortPreference = new ArrayList<EPWidgetsSortPreference>();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(appService.saveWidgetsSortManual(ePWidgetsSortPreference, user))
				.thenReturn(expectedFieldValidator);
		actualFieldValidator = appsController.putUserWidgetsSortManual(mockedRequest, ePWidgetsSortPreference,
				mockedResponse);
		assertEquals(actualFieldValidator, expectedFieldValidator);
	}

	@Test
	public void putUserWidgetsSortManualExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPAppsManualPreference> ePAppsManualPreference = new ArrayList<EPAppsManualPreference>();
		Mockito.when(appService.saveAppsSortManual(ePAppsManualPreference, user)).thenThrow(nullPointerException);
		assertNull(appsController.putUserAppsSortingManual(mockedRequest, ePAppsManualPreference, mockedResponse));
	}

	@Test
	public void putUserWidgetsSortPrefTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPWidgetsSortPreference> ePWidgetsSortPreference = new ArrayList<EPWidgetsSortPreference>();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(appService.deleteUserWidgetSortPref(ePWidgetsSortPreference, user))
				.thenReturn(expectedFieldValidator);
		actualFieldValidator = appsController.putUserWidgetsSortPref(mockedRequest, ePWidgetsSortPreference,
				mockedResponse);
		assertEquals(actualFieldValidator, expectedFieldValidator);
	}

	@Test
	public void putUserWidgetsSortPrefExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPWidgetsSortPreference> ePWidgetsSortPreference = new ArrayList<EPWidgetsSortPreference>();
		Mockito.when(appService.deleteUserWidgetSortPref(ePWidgetsSortPreference, user))
				.thenThrow(nullPointerException);
		assertNull(appsController.putUserWidgetsSortPref(mockedRequest, ePWidgetsSortPreference, mockedResponse));
	}

	@Test
	public void deleteUserAppSortManualTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		EPDeleteAppsManualSortPref epDeleteAppsManualSortPref = new EPDeleteAppsManualSortPref();

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(appService.deleteUserAppSortManual(epDeleteAppsManualSortPref, user))
				.thenReturn(expectedFieldValidator);
		actualFieldValidator = appsController.deleteUserAppSortManual(mockedRequest, epDeleteAppsManualSortPref,
				mockedResponse);
		assertEquals(actualFieldValidator, expectedFieldValidator);
	}

	@Test
	public void deleteUserAppSortManualExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		EPDeleteAppsManualSortPref epDeleteAppsManualSortPref = new EPDeleteAppsManualSortPref();
		Mockito.when(appService.deleteUserAppSortManual(epDeleteAppsManualSortPref, user))
				.thenThrow(nullPointerException);
		assertNull(appsController.deleteUserAppSortManual(mockedRequest, epDeleteAppsManualSortPref, mockedResponse));
	}

	@Test
	public void putUserAppsSortingPreferenceTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		EPAppsSortPreference userAppsValue = new EPAppsSortPreference();

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(appService.saveAppsSortPreference(userAppsValue, user)).thenReturn(expectedFieldValidator);
		actualFieldValidator = appsController.putUserAppsSortingPreference(mockedRequest, userAppsValue,
				mockedResponse);
		assertEquals(actualFieldValidator, expectedFieldValidator);
	}

	@Test
	public void putUserAppsSortingPreferenceExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		EPAppsSortPreference userAppsValue = new EPAppsSortPreference();
		Mockito.when(appService.saveAppsSortPreference(userAppsValue, user)).thenThrow(nullPointerException);
		assertNull(appsController.putUserAppsSortingPreference(mockedRequest, userAppsValue, mockedResponse));
	}

	@Test
	public void getUserAppsSortTypePreferenceTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String expectedUserSortPreference = "TEST_DECE";
		String actualUserSortPreference = new String();
		Mockito.when(appService.getUserAppsSortTypePreference(user)).thenReturn(expectedUserSortPreference);
		actualUserSortPreference = appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse);
		assertEquals(actualUserSortPreference, expectedUserSortPreference);
	}

	@Test
	public void getUserAppsSortTypePreferenceExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getUserAppsSortTypePreference(user)).thenThrow(nullPointerException);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getUserAppsSortTypePreferenceIfUserNullTest() throws IOException {
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getUserAppsSortTypePreference(user)).thenThrow(nullPointerException);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsAdministratorsTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AdminUserApplications> expecteAdminUserApplications = new ArrayList<AdminUserApplications>();
		List<AdminUserApplications> actualAdminUserApplications = new ArrayList<AdminUserApplications>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAppsAdmins()).thenReturn(expecteAdminUserApplications);
		actualAdminUserApplications = appsController.getAppsAdministrators(mockedRequest, mockedResponse);
		assertEquals(expecteAdminUserApplications, actualAdminUserApplications);
	}

	@Test
	public void getAppsAdministratorsIfUserNotSuperAdminTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsAdministratorsExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAppsAdmins()).thenThrow(nullPointerException);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppsResponse> expectedApps = new ArrayList<AppsResponse>();
		AppsResponse apps = new AppsResponse((long) 1, "test", true, true);
		expectedApps.add(apps);

		List<AppsResponse> atualApps = new ArrayList<AppsResponse>();

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAllApplications(false)).thenReturn(expectedApps);
		atualApps = appsController.getApps(mockedRequest, mockedResponse);
		assertEquals(expectedApps, atualApps);
	}

	@Test
	public void getAppsExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(appsController.getApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsIfUserNotSuperAdminTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAllApplications(false)).thenThrow(nullPointerException);
		assertNull(appsController.getApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAllAppsTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppsResponse> expectedApps = new ArrayList<AppsResponse>();
		AppsResponse apps = new AppsResponse((long) 1, "test", true, true);
		expectedApps.add(apps);
		List<AppsResponse> atualApps = new ArrayList<AppsResponse>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAllApps(true)).thenReturn(expectedApps);
		atualApps = appsController.getAllApps(mockedRequest, mockedResponse);
		assertEquals(expectedApps, atualApps);
	}

	@Test
	public void getAllAppsExceptionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(appsController.getAllApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAllAppsIfUserNotSuperAdminTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAllApps(true)).thenThrow(nullPointerException);
		assertNull(appsController.getAllApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppsFullListTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EcompApp> expectedEcompApps = new ArrayList<EcompApp>();

		EcompApp ecompApp = new EcompApp();
		ecompApp.setId((long) 1);
		ecompApp.setName("Test_app");
		ecompApp.setUrl("Test_URL");
		ecompApp.setUebKey("Test_key");
		ecompApp.setAlternateUrl("Test_alt_URL");
		expectedEcompApps.add(ecompApp);
		List<EcompApp> actualEcompApps = new ArrayList<EcompApp>();
		Mockito.when(appService.getEcompAppAppsFullList()).thenReturn(expectedEcompApps);
		actualEcompApps = appsController.getAppsFullList(mockedRequest, mockedResponse);
		assertEquals(expectedEcompApps, actualEcompApps);
	}

//	@Test
//	public void getAppsFullListNoUserTest() {
//		EPUser user = null;
//		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
//		Mockito.when(appService.getEcompAppAppsFullList()).thenReturn(null);
//		assertNull(appsController.getAppsFullList(mockedRequest, mockedResponse));
//
//	}

	@Test
	public void getUserProfileTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		UserRole userRole = new UserRole();
		userRole.setUser_Id((long) 1);
		userRole.setOrgUserId("guest");
		userRole.setFirstName("Test_User_FirstName");
		userRole.setLastName("Test_User_LastName");
		userRole.setRoleId((long) 1);
		userRole.setRoleName("test");

		UserRoles unexpectedserAndRoles = new UserRoles(userRole);
		unexpectedserAndRoles.setFirstName("Test_User_FirstName");
		unexpectedserAndRoles.setLastName("Test_User_LastName");
		unexpectedserAndRoles.setGuestSession(false);
		unexpectedserAndRoles.setOrgUserId("guest");
		List<String> roles = new ArrayList<String>();
		roles.add("Test");
		unexpectedserAndRoles.setRoles(roles);
		Mockito.when(appService.getUserProfileNormalized(user)).thenReturn(unexpectedserAndRoles);
		UserRoles actualUserAndRoles = appsController.getUserProfile(mockedRequest, mockedResponse);
		assertEquals(unexpectedserAndRoles, actualUserAndRoles);
	}

	@Test
	public void getUserProfileIfUserNullTest() throws IOException {
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getUserProfileNormalized(user)).thenReturn(null);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getUserProfileExcpetionTest() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getUserProfileNormalized(user)).thenThrow(nullPointerException);
		assertNull(appsController.getUserAppsSortTypePreference(mockedRequest, mockedResponse));
	}

	@Test
	public void getAppRolesTest() {
		List<LocalRole> expectedRoleList = new ArrayList<LocalRole>();
		LocalRole localRole = new LocalRole();
		localRole.setRoleId(1);
		localRole.setRolename("test");
		expectedRoleList.add(localRole);
		long appId = 1;
		Mockito.when(appService.getAppRoles(appId)).thenReturn(expectedRoleList);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(Matchers.anyObject(), Matchers.anyObject())).thenReturn(true);
		List<LocalRole> actualRoleList = appsController.getAppRoles(mockedRequest, appId, mockedResponse);
		assertEquals(actualRoleList, expectedRoleList);
	}

	@Test
	public void getAppRolesExceptionTest() {
		long appId = 1;
		Mockito.when(appService.getAppRoles(appId)).thenThrow(nullPointerException);
		assertNull(appsController.getAppRoles(mockedRequest, appId, mockedResponse));
	}

	@Test
	public void getOnboardingAppsTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<OnboardingApp> expectedOnboardingApps = new ArrayList<OnboardingApp>();
		OnboardingApp onboardingApp = new OnboardingApp();
		onboardingApp.setUebKey("test");
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getOnboardingApps()).thenReturn(expectedOnboardingApps);
		List<OnboardingApp> actualOnboardingApps = appsController.getOnboardingApps(mockedRequest, mockedResponse);
		assertEquals(expectedOnboardingApps, actualOnboardingApps);
	}

	@Test
	public void getOnboardingAppsifSuperAdiminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(appsController.getOnboardingApps(mockedRequest, mockedResponse));
	}

	@Test
	public void getOnboardingAppsExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(!adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getOnboardingApps()).thenThrow(nullPointerException);
		assertNull(appsController.getOnboardingApps(mockedRequest, mockedResponse));
	}

	@Test
	public void putOnboardingAppTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp OnboardingApp = new OnboardingApp();
		OnboardingApp.isCentralAuth = true;
		OnboardingApp.nameSpace = "test1";
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		EPApp OnboardingApp1 = new EPApp();
		OnboardingApp1.setCentralAuth(false);
		OnboardingApp1.setNameSpace("test"); 
		Mockito.when(appService.getApp(Matchers.anyLong())).thenReturn(OnboardingApp1);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(appService.checkIfNameSpaceIsValid(Matchers.anyString())).thenReturn(response);
		Mockito.when(appService.modifyOnboardingApp(OnboardingApp, user)).thenReturn(expectedFieldValidator);
		Mockito.when(mockedResponse.getStatus()).thenReturn(200);
		FieldsValidator actualFieldValidator = appsController.putOnboardingApp(mockedRequest, OnboardingApp,
				mockedResponse);
		assertEquals(expectedFieldValidator, actualFieldValidator);
	}
	
	@Test
	public void putOnboardingApp2Test() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp onboardingApp = new OnboardingApp();
		onboardingApp.isCentralAuth = true;
		onboardingApp.nameSpace = "com.test1";
		EPApp app = new EPApp();
		app.setNameSpace("com.test ");
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(Matchers.any(EPUser.class),Matchers.any(EPApp.class))).thenReturn(true);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(appService.checkIfNameSpaceIsValid("com.test1")).thenReturn(response);
		Mockito.when(appService.getApp(Matchers.anyLong())).thenReturn(app);
		Mockito.when(mockedResponse.getStatus()).thenReturn(200);
		Mockito.when(appService.modifyOnboardingApp(Matchers.any(OnboardingApp.class), Matchers.any(EPUser.class))).thenReturn(expectedFieldValidator);
		FieldsValidator actualFieldValidator = appsController.putOnboardingApp(mockedRequest, onboardingApp,
				mockedResponse);
	}
	
	

	
	@Test
	public void putOnboardingApp4Test() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp onboardingApp = new OnboardingApp();
		onboardingApp.isCentralAuth = false;
		onboardingApp.nameSpace = "com.test1";
		EPApp app = new EPApp();
		app.setCentralAuth(false);
		app.setNameSpace("com.test ");
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 404);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdminOfAnyActiveorInactiveApplication(Matchers.any(EPUser.class),Matchers.any(EPApp.class))).thenReturn(true);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		
		HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.FORBIDDEN);
		Mockito.when(appService.checkIfNameSpaceIsValid("com.test1")).thenThrow(exception);
		Mockito.when(appService.getApp(Matchers.anyLong())).thenReturn(app);
		Mockito.when(mockedResponse.getStatus()).thenReturn(200);
		Mockito.when(appService.modifyOnboardingApp(Matchers.any(OnboardingApp.class), Matchers.any(EPUser.class))).thenReturn(expectedFieldValidator);
		FieldsValidator actualFieldValidator = appsController.putOnboardingApp(mockedRequest, onboardingApp,
				mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldValidator.getHttpStatusCode());
	}
	
	@Test
	public void putOnboardingApp5Test() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp onboardingApp = new OnboardingApp();
		onboardingApp.isCentralAuth = true;
		onboardingApp.nameSpace = "com.test1";
		EPApp app = new EPApp();
		app.setNameSpace("com.test ");
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 400);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(Matchers.any(EPUser.class),Matchers.any(EPApp.class))).thenReturn(true);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		
		HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
		Mockito.when(appService.checkIfNameSpaceIsValid("com.test1")).thenThrow(exception);
		Mockito.when(appService.getApp(Matchers.anyLong())).thenReturn(app);
		Mockito.when(mockedResponse.getStatus()).thenReturn(400);
		Mockito.when(appService.modifyOnboardingApp(Matchers.any(OnboardingApp.class), Matchers.any(EPUser.class))).thenReturn(expectedFieldValidator);
		FieldsValidator actualFieldValidator = appsController.putOnboardingApp(mockedRequest, onboardingApp,
				mockedResponse);
	}


	@Test
	public void putOnboardingAppIfSuperAdminTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = null;
		OnboardingApp OnboardingApp = new OnboardingApp();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(appService.modifyOnboardingApp(OnboardingApp, user)).thenReturn(expectedFieldValidator);
		assertNull(appsController.putOnboardingApp(mockedRequest, OnboardingApp, mockedResponse));
	}

	@Test
	public void putOnboardingAppExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp OnboardingApp = new OnboardingApp();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.modifyOnboardingApp(OnboardingApp, user)).thenThrow(nullPointerException);
		assertNull(appsController.putOnboardingApp(mockedRequest, OnboardingApp, mockedResponse));
	}

    @Test
    public void putOnboardingAppNullUserTest() throws Exception {
        Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenThrow(nullPointerException);
        Mockito.when(mockedResponse.getStatus()).thenReturn(200);
        assertNull(appsController.putOnboardingApp(mockedRequest, new OnboardingApp(), mockedResponse));
    }
	
	@Test
	public void postOnboardingAppTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp OnboardingApp = new OnboardingApp();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.addOnboardingApp(OnboardingApp, user)).thenReturn(expectedFieldValidator);
		FieldsValidator actualFieldValidator = appsController.postOnboardingApp(mockedRequest, OnboardingApp,
				mockedResponse);
		assertEquals(expectedFieldValidator, actualFieldValidator);
	}

	@Test
	public void postOnboardingAppIfSuperAdminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = null;
		OnboardingApp OnboardingApp = new OnboardingApp();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(appService.addOnboardingApp(OnboardingApp, user)).thenReturn(expectedFieldValidator);
		assertNull(appsController.postOnboardingApp(mockedRequest, OnboardingApp, mockedResponse));
	}

	@Test
	public void postOnboardingAppExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingApp OnboardingApp = new OnboardingApp();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.addOnboardingApp(OnboardingApp, user)).thenThrow(nullPointerException);
		assertNull(appsController.postOnboardingApp(mockedRequest, OnboardingApp, mockedResponse));
	}
	
	@Test
	public void deleteOnboardingAppTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		long appId = 1;
		Mockito.when(appService.deleteOnboardingApp(user,appId )).thenReturn(expectedFieldValidator);
		FieldsValidator actualFieldValidator = appsController.deleteOnboardingApp(mockedRequest,appId,
				mockedResponse);
		assertEquals(expectedFieldValidator, actualFieldValidator);
	}

	@Test
	public void deleteOnboardingAppIfSuperAdminTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = null;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		long appId = 1;
        Mockito.when(appService.deleteOnboardingApp(user,appId)).thenReturn(expectedFieldValidator);
		assertNull(appsController.deleteOnboardingApp(mockedRequest,appId,mockedResponse));
	}

	@Test
	public void deleteOnboardingAppExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		long appId = 1;
      Mockito.when(appService.deleteOnboardingApp(user,appId)).thenThrow(nullPointerException);
		assertNull(appsController.deleteOnboardingApp(mockedRequest,appId,mockedResponse));
	}
	
	@Test
	public void getLeftMenuItemsTest()
	{
		EPUser user = mockUser.mockEPUser();
		String menuList = "Test";
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Set menuSet = new HashSet<>();
		menuSet.add(1);
		Mockito.when(AppUtils.getSession(mockedRequest)
				.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME))).thenReturn(menuSet);
		Mockito.when(AppUtils.getSession(mockedRequest)
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME))).thenReturn(menuSet);
		Mockito.when(leftMenuService.getLeftMenuItems(user, menuSet, menuSet)).thenReturn(menuList);
		String response = appsController.getLeftMenuItems(mockedRequest, mockedResponse);
		assertTrue(response.equals("Test"));
	}
	
	@Test
	public void getLeftMenuItemsExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();
		String menuList = "Test";
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Set menuSet = new HashSet<>();
		menuSet.add(1);
		Mockito.when(AppUtils.getSession(mockedRequest)
				.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME))).thenReturn(menuSet);
		Mockito.when(AppUtils.getSession(mockedRequest)
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME))).thenReturn(menuSet);
		Mockito.when(leftMenuService.getLeftMenuItems(user, menuSet, menuSet)).thenThrow(nullPointerException);
		assertNull(appsController.getLeftMenuItems(mockedRequest, mockedResponse));
	}
	
	@Test
	public void getAppThumbnailExceptionTest()
	{
		EPApp app = null;
		assertNull(appsController.getAppThumbnail(mockedRequest, (long) 1, mockedResponse));
	}
	
	@Test
	public void getAppThumbnailTest()
	{
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppType(1);
		app.setImageUrl("www.ecomp.com");
		app.setThumbnail(new byte[] {1, 6, 3});
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		HttpEntity<byte[]> response = appsController.getAppThumbnail(mockedRequest, (long) 1, mockedResponse);
		assertEquals(response.getHeaders().getContentLength(), 3);
	}
	
	@Test
	public void getAppThumbnailForMediaTypePngTest()
	{
		
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppType(1);
		app.setImageUrl("www.ecomp.png");
		app.setThumbnail(new byte[] {1, 6, 3});
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		PowerMockito.mockStatic(MediaType.class);
		HttpEntity<byte[]> response = appsController.getAppThumbnail(mockedRequest, (long) 1, mockedResponse);
		assertEquals(response.getHeaders().getContentLength(), 3);
	}
	
	@Test
	public void getUserAppsOrderBySortPrefIfUSerNullTest(){
		List<EcompApp> listOfApps = new ArrayList<EcompApp>();
		
		EcompApp app = new EcompApp();
		listOfApps.add(app);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		assertNull(appsController.getUserAppsOrderBySortPref(mockedRequest, mockedResponse));
	}
	
	@Test
	public void getUserAppsOrderBySortPrefTest(){
		List<EcompApp> listOfApps = new ArrayList<EcompApp>();
		
		EcompApp app = new EcompApp();
		listOfApps.add(app);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(mockedRequest.getParameter("mparams")).thenReturn("");
		Mockito.when(appService.transformAppsToEcompApps(appService.getAppsOrderByName(user))).thenReturn(listOfApps);
		List<EcompApp> listOfActualApps = 	appsController.getUserAppsOrderBySortPref(mockedRequest, mockedResponse);
		assertEquals(listOfActualApps.size(), 1);
	}
	
	@Test
	public void getUserAppsOrderBySortPrefIfusrSortPrefIsMTest(){
		List<EcompApp> listOfApps = new ArrayList<EcompApp>();
		
		EcompApp app = new EcompApp();
		listOfApps.add(app);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(mockedRequest.getParameter("mparams")).thenReturn("M");
		Mockito.when(appService.transformAppsToEcompApps(appService.getAppsOrderByName(user))).thenReturn(listOfApps);
		List<EcompApp> listOfActualApps = 	appsController.getUserAppsOrderBySortPref(mockedRequest, mockedResponse);
		assertEquals(listOfActualApps.size(), 1);
	}
	
	@Test
	public void getSingleAppInfoWithExceptionTest (){
		EPApp epApp=new EPApp();
		epApp.setName("test");
		epApp.setId(1L);
		//EPApp listOfApps = new ArrayList<EcompApp>();
		
		//EcompApp app = new EcompApp();
		//listOfApps.add(app);
		EPUser user = mockUser.mockEPUser();
		String appName="test";
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(mockedRequest.getParameter("mparams")).thenReturn("M");
		Mockito.when(appService.getAppDetailByAppName(appName)).thenReturn(epApp);
		EPApp listOfActualApps = 	appsController.getSingleAppInfo(mockedRequest, mockedResponse);
		//assertEquals(listOfActualApps,epApp);
		assertNull(listOfActualApps);
	}
	
	@Test
	public void getSingleAppInfoTest (){
		EPApp epApp=new EPApp();
		epApp.setName("test");
		epApp.setId(1L);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(mockedRequest.getParameter("appParam")).thenReturn("test");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(mockedRequest.getParameter("mparams")).thenReturn("M");
		Mockito.when(appService.getAppDetailByAppName("test")).thenReturn(epApp);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);

		EPApp listOfActualApps = 	appsController.getSingleAppInfo(mockedRequest, mockedResponse);
		//assertEquals(listOfActualApps,epApp);
		assertEquals(listOfActualApps,epApp);
	}
	
	
	@Test
	public void getSingleAppInfoByIdTest(){
		EPApp epApp=new EPApp();
		epApp.setName("test");
		epApp.setId(1L);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(mockedRequest.getParameter("appParam")).thenReturn("123");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(mockedRequest.getParameter("mparams")).thenReturn("M");
		Mockito.when(appService.getApp(123L)).thenReturn(epApp);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		EPApp listOfActualApps = 	appsController.getSingleAppInfoById(mockedRequest, mockedResponse);
		assertEquals(listOfActualApps,epApp);
	}
	
}
