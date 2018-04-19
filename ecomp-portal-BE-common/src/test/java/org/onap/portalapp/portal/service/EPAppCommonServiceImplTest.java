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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.onap.portalapp.portal.domain.AppsResponse;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserAppsManualSortPreference;
import org.onap.portalapp.portal.domain.EPUserAppsSortPreference;
import org.onap.portalapp.portal.domain.EPWidgetsManualSortPreference;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.domain.UserRoles;
import org.onap.portalapp.portal.transport.EPAppsSortPreference;
import org.onap.portalapp.portal.transport.EPDeleteAppsManualSortPref;
import org.onap.portalapp.portal.transport.EPWidgetsSortPreference;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.ueb.EPUebHelper;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.att.nsa.apiClient.credentials.ApiCredential;
import com.att.nsa.cambria.client.CambriaClientBuilders;
import com.att.nsa.cambria.client.CambriaIdentityManager;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*" })
@PrepareForTest({ EcompPortalUtils.class, PortalApiProperties.class, PortalApiConstants.class, SystemProperties.class,
		PortalConstants.class, EPCommonSystemProperties.class })
public class EPAppCommonServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock
	AdminRolesServiceImpl adminRolesServiceImpl = new AdminRolesServiceImpl();

	@Mock
	EPUebHelper epUebHelper;

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
	}

	MockEPUser mockUser = new MockEPUser();

	String ECOMP_APP_ID = "1";
	String SUPER_ADMIN_ROLE_ID = "1";
	String ACCOUNT_ADMIN_ROLE_ID = "999";
	String RESTRICTED_APP_ROLE_ID = "900";

	@InjectMocks
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();

	public EPApp mockApp() {
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
		app.setCentralAuth(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test
	public void getUserAsAdminAppsTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> expected = new ArrayList<>();
		expected.add(mockApp);
		expected.add(mockApp2);
		String sql = "SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID=FN_APP.APP_ID where "
				+ "FN_USER_ROLE.USER_ID=" + user.getId() + " AND FN_USER_ROLE.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND FN_APP.ENABLED = 'Y'";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPApp.class, null)).thenReturn(expected);
		Mockito.when(adminRolesServiceImpl.isAccountAdmin(user)).thenReturn(true);
		List<EPApp> actual = epAppCommonServiceImpl.getUserAsAdminApps(user);
		assertEquals(expected, actual);
	}

	@Test
	public void getUserAsAdminAppsTestException() {
		EPUser user = mockUser.mockEPUser();
		String sql = "SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID=FN_APP.APP_ID where "
				+ "FN_USER_ROLE.USER_ID=" + user.getId() + " AND FN_USER_ROLE.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND FN_APP.ENABLED = 'Y'";
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeSQLQuery(sql, EPApp.class, null);
		Mockito.when(adminRolesServiceImpl.isAccountAdmin(user)).thenReturn(true);
		List<EPApp> actual = epAppCommonServiceImpl.getUserAsAdminApps(user);
		assertNull(actual);
	}

	@Test
	public void getUserByOrgUserIdAsAdminAppsTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> expected = new ArrayList<>();
		expected.add(mockApp);
		expected.add(mockApp2);
		String format = "SELECT * FROM FN_APP app INNER JOIN FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
				+ "INNER JOIN FN_USER user on user.USER_ID = userrole.USER_ID "
				+ "WHERE user.org_user_id = '%s' AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND FN_APP.ENABLED = 'Y'";
		String sql = String.format(format, user.getOrgUserId());
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPApp.class, null)).thenReturn(expected);
		List<EPApp> actual = epAppCommonServiceImpl.getUserByOrgUserIdAsAdminApps(user.getOrgUserId());
		assertEquals(expected, actual);
	}

	@Test
	public void getUserByOrgUserIdAsAdminAppsTestException() {
		EPUser user = mockUser.mockEPUser();
		String format = "SELECT * FROM FN_APP app INNER JOIN FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
				+ "INNER JOIN FN_USER user on user.USER_ID = userrole.USER_ID "
				+ "WHERE user.org_user_id = '%s' AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND FN_APP.ENABLED = 'Y'";
		String sql = String.format(format, user.getOrgUserId());
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeSQLQuery(sql, EPApp.class, null);
		List<EPApp> actual = epAppCommonServiceImpl.getUserByOrgUserIdAsAdminApps(user.getOrgUserId());
		assertNull(actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getAppsFullListTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		List<EPApp> expected = new ArrayList<>();
		expected.add(mockApp);
		expected.add(mockApp2);
		Mockito.when(dataAccessService.getList(EPApp.class, null)).thenReturn(expected);
		List<EPApp> actual = epAppCommonServiceImpl.getAppsFullList();
		assertEquals(expected, actual);

	}

	@Test
	public void getEcompAppAppsFullListTest() {
		List<EcompApp> expected = new ArrayList<>();
		List<EcompApp> actual = epAppCommonServiceImpl.getEcompAppAppsFullList();
		assertEquals(expected, actual);
	}

	@Test
	public void transformAppsToEcompAppsTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		List<EPApp> epAppsList = new ArrayList<>();
		epAppsList.add(mockApp);
		epAppsList.add(mockApp2);
		List<EcompApp> expected = new ArrayList<>();
		EcompApp ecompApp = new EcompApp();
		ecompApp.setName("test1");
		EcompApp ecompApp2 = new EcompApp();
		ecompApp2.setName("test2");
		expected.add(ecompApp);
		expected.add(ecompApp2);
		List<EcompApp> actual = epAppCommonServiceImpl.transformAppsToEcompApps(epAppsList);
		assertEquals(expected.size(), actual.size());
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void getAllAppsForAllTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		List<EPApp> appsList = new ArrayList<>();
		appsList.add(mockApp);
		appsList.add(mockApp2);
		List<AppsResponse> expected = new ArrayList<>();
		AppsResponse appResponse1 = new AppsResponse(mockApp.getId(), mockApp.getName(), mockApp.isRestrictedApp(),
				mockApp.getEnabled());
		AppsResponse appResponse2 = new AppsResponse(mockApp2.getId(), mockApp2.getName(), mockApp2.isRestrictedApp(),
				mockApp2.getEnabled());
		expected.add(appResponse1);
		expected.add(appResponse2);
		Mockito.when((List<EPApp>) dataAccessService.getList(EPApp.class, " where id != " + ECOMP_APP_ID, "name", null))
				.thenReturn(appsList);
		List<AppsResponse> actual = epAppCommonServiceImpl.getAllApps(true);
		assertEquals(expected.size(), actual.size());
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void getAllAppsIsNotAllTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp2.setId(2l);
		List<EPApp> appsList = new ArrayList<>();
		appsList.add(mockApp);
		appsList.add(mockApp2);
		List<AppsResponse> expected = new ArrayList<>();
		AppsResponse appResponse1 = new AppsResponse(mockApp.getId(), mockApp.getName(), mockApp.isRestrictedApp(),
				mockApp.getEnabled());
		AppsResponse appResponse2 = new AppsResponse(mockApp2.getId(), mockApp2.getName(), mockApp2.isRestrictedApp(),
				mockApp2.getEnabled());
		expected.add(appResponse1);
		expected.add(appResponse2);
		Mockito.when((List<EPApp>) dataAccessService.getList(EPApp.class,
				" where ( enabled = 'Y' or id = " + ECOMP_APP_ID + ")", "name", null)).thenReturn(appsList);
		List<AppsResponse> actual = epAppCommonServiceImpl.getAllApps(false);
		assertEquals(expected.size(), actual.size());
	}

	@Test
	public void getAppTest() {
		EPApp expected = mockApp();
		Mockito.when((EPApp) dataAccessService.getDomainObject(EPApp.class, expected.getId(), null))
				.thenReturn(expected);
		EPApp actual = epAppCommonServiceImpl.getApp(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void getAppExceptionTest() {
		EPApp expected = mockApp();
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).getDomainObject(EPApp.class,
				expected.getId(), null);
		EPApp actual = epAppCommonServiceImpl.getApp(expected.getId());
		assertNull(actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAppDetailTest() {
		EPApp expected = mockApp();
		List<EPApp> appList = new ArrayList<>();
		appList.add(expected);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("appName", expected.getName());
		Mockito.when((List<EPApp>) dataAccessService.executeNamedQuery("getMyloginAppDetails", params, null))
				.thenReturn(appList);
		EPApp actual = epAppCommonServiceImpl.getAppDetail(expected.getName());
		assertEquals(expected, actual);
	}

	@Test
	public void getAppDetailExceptionTest() {
		EPApp expected = mockApp();
		List<EPApp> appList = new ArrayList<>();
		appList.add(expected);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("appName", expected.getName());
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeNamedQuery("getMyloginAppDetails",
				params, null);
		EPApp actual = epAppCommonServiceImpl.getAppDetail(expected.getName());
		assertNull(actual);
	}

	@Test
	public void getUserProfile() {
		EPUser user = mockUser.mockEPUser();
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List<UserRole> userRoleList = new ArrayList<>();
		UserRole userRole = new UserRole();
		userRole.setFirstName("GuestT");
		userRole.setLastName("GuestT");
		userRole.setOrgUserId("guestT");
		userRole.setRoleId(1l);
		userRole.setRoleName("Test");
		userRole.setUser_Id(-1l);
		userRoleList.add(userRole);
		UserRoles expected = new UserRoles(userRole);
		Mockito.when(dataAccessService.executeNamedQuery("getUserRoles", params, null)).thenReturn(userRoleList);
		UserRoles actual = epAppCommonServiceImpl.getUserProfile(user.getOrgUserId());
		assertEquals(expected.getOrgUserId(), actual.getOrgUserId());
	}

	@Test
	public void getUserProfileNullTest() {
		EPUser user = mockUser.mockEPUser();
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List<UserRole> userRoleList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getUserRoles", params, null)).thenReturn(userRoleList);
		UserRoles actual = epAppCommonServiceImpl.getUserProfile(user.getOrgUserId());
		assertNull(actual);
	}

	@Test
	public void getUserProfileNormalizedTest() {
		EPUser user = mockUser.mockEPUser();
		user.setGuest(true);
		user.setLoginId("guestT");
		final Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List<UserRole> userRoleList = new ArrayList<>();
		UserRole userRole = new UserRole();
		userRole.setFirstName("GuestT");
		userRole.setLastName("GuestT");
		userRole.setOrgUserId("guestT");
		userRole.setRoleId(1l);
		userRole.setRoleName("Test");
		userRole.setUser_Id(-1l);
		userRoleList.add(userRole);
		UserRoles expected = new UserRoles(userRole);
		Mockito.when(dataAccessService.executeNamedQuery("getUserRoles", params, null)).thenReturn(userRoleList);
		UserRoles actual = epAppCommonServiceImpl.getUserProfileNormalized(user);
		assertEquals(expected.getOrgUserId(), actual.getOrgUserId());
	}

	@Test
	public void getRestrictedAppRolesTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setRestrictedApp(true);
		user.setLoginId("guestT");
		List<LocalRole> expected = new ArrayList<>();
		LocalRole localRole = new LocalRole();
		localRole.setRoleId(1);
		localRole.setRolename("test1");
		expected.add(localRole);
		String sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND ROLE_ID = '"
				+ RESTRICTED_APP_ROLE_ID + "'";
		Mockito.when(dataAccessService.executeSQLQuery(sql, LocalRole.class, null)).thenReturn(expected);
		Mockito.when((EPApp) dataAccessService.getDomainObject(EPApp.class, mockApp.getId(), null)).thenReturn(mockApp);
		List<LocalRole> actual = epAppCommonServiceImpl.getAppRoles(mockApp.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void getPoralAppRolesTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setRestrictedApp(false);
		mockApp.setId(1l);
		user.setLoginId("guestT");
		List<LocalRole> expected = new ArrayList<>();
		LocalRole localRole = new LocalRole();
		localRole.setRoleId(1);
		localRole.setRolename("test1");
		expected.add(localRole);
		String sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND APP_ID IS NULL";
		Mockito.when(dataAccessService.executeSQLQuery(sql, LocalRole.class, null)).thenReturn(expected);
		Mockito.when((EPApp) dataAccessService.getDomainObject(EPApp.class, mockApp.getId(), null)).thenReturn(mockApp);
		List<LocalRole> actual = epAppCommonServiceImpl.getAppRoles(mockApp.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void getNonPortalAndNonRestrictedAppRolesTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setRestrictedApp(false);
		mockApp.setId(2l);
		user.setLoginId("guestT");
		List<LocalRole> expected = new ArrayList<>();
		LocalRole localRole = new LocalRole();
		localRole.setRoleId(1);
		localRole.setRolename("test1");
		expected.add(localRole);
		String sql = "SELECT ROLE_ID, ROLE_NAME from FN_ROLE where UPPER(ACTIVE_YN) = 'Y' AND APP_ID = '"
				+ mockApp.getId() + "'";
		Mockito.when(dataAccessService.executeSQLQuery(sql, LocalRole.class, null)).thenReturn(expected);
		Mockito.when((EPApp) dataAccessService.getDomainObject(EPApp.class, mockApp.getId(), null)).thenReturn(mockApp);
		List<LocalRole> actual = epAppCommonServiceImpl.getAppRoles(mockApp.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void getAdminAppsTest() {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		List<AppIdAndNameTransportModel> expected = new ArrayList<>();
		AppIdAndNameTransportModel appIdAndNameTransportModel = new AppIdAndNameTransportModel();
		appIdAndNameTransportModel.setId(1l);
		appIdAndNameTransportModel.setName("test1");
		expected.add(appIdAndNameTransportModel);
		Mockito.when(adminRolesServiceImpl.isAccountAdmin(user)).thenReturn(true);
		String format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
				+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND (app.ENABLED = 'Y' OR app.APP_ID=1)";
		String sql = String.format(format, user.getId());
		Mockito.when(dataAccessService.executeSQLQuery(sql, AppIdAndNameTransportModel.class, null))
				.thenReturn(expected);
		List<AppIdAndNameTransportModel> actual = epAppCommonServiceImpl.getAdminApps(user);
		assertEquals(expected, actual);
	}

	@Test
	public void getAdminAppsExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		List<AppIdAndNameTransportModel> expected = new ArrayList<>();
		Mockito.when(adminRolesServiceImpl.isAccountAdmin(user)).thenReturn(true);
		String format = "SELECT app.APP_ID, app.APP_NAME, app.APP_TYPE FROM FN_APP app inner join FN_USER_ROLE userrole ON userrole.APP_ID=app.APP_ID "
				+ "where userrole.USER_ID = %d AND userrole.ROLE_ID=" + ACCOUNT_ADMIN_ROLE_ID
				+ " AND (app.ENABLED = 'Y' OR app.APP_ID=1)";
		String sql = String.format(format, user.getId());
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeSQLQuery(sql,
				AppIdAndNameTransportModel.class, null);
		List<AppIdAndNameTransportModel> actual = epAppCommonServiceImpl.getAdminApps(user);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void addOnboardingAppUnKnownHostExceptionTest() throws Exception {
		PowerMockito.mockStatic(PortalApiConstants.class);
		PowerMockito.mockStatic(PortalApiProperties.class);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> mockAppList = new ArrayList<>();
		OnboardingApp onboardApp = new OnboardingApp();
		onboardApp.setRestrictedApp(false);
		onboardApp.name = "test1";
		onboardApp.url = "http://test.com";
		onboardApp.isOpen = false;
		onboardApp.isEnabled = true;
		onboardApp.username = "test123";
		onboardApp.appPassword = "test123";
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion urlCrit = Restrictions.eq("url", onboardApp.url);
		Criterion nameCrit = Restrictions.eq("name", onboardApp.name);
		Criterion orCrit = Restrictions.or(urlCrit, nameCrit);
		restrictionsList.add(orCrit);
		List<String> uebList = new ArrayList<>();
		uebList.add("localhost");
		com.att.nsa.apiClient.credentials.ApiCredential apiCredential = new ApiCredential(user.getEmail(),
				"ECOMP Portal Owner");
		CambriaIdentityManager mockIm = Mockito
				.spy(new CambriaClientBuilders.IdentityManagerBuilder().usingHosts(uebList).build());
		Mockito.doReturn(apiCredential).when(mockIm).createApiKey(user.getEmail(), "ECOMP Portal Owner");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_URL_LIST)).thenReturn("localhost");
		Mockito.when((List<EPApp>) dataAccessService.getList(EPApp.class, null, restrictionsList, null))
				.thenReturn(mockAppList);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		FieldsValidator actual = epAppCommonServiceImpl.addOnboardingApp(onboardApp, user);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void modifyOnboardingAppTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setName("test1");
		mockApp.setId(2l);
		mockApp.setUrl("http://test.com");
		mockApp.setUsername("test123");
		mockApp.setAppPassword("test123");
		mockApp.setRestrictedApp(false);
		mockApp.setEnabled(true);
		mockApp.setOpen(false);
		List<EPApp> mockAppList = new ArrayList<>();
		mockAppList.add(mockApp);
		OnboardingApp onboardApp = new OnboardingApp();
		onboardApp.setRestrictedApp(false);
		onboardApp.name = "test1";
		onboardApp.id = 2l;
		onboardApp.url = "http://test.com";
		onboardApp.isOpen = false;
		onboardApp.isEnabled = true;
		onboardApp.thumbnail = "test123imgthumbnail";
		onboardApp.username = "test123";
		onboardApp.appPassword = "test123";
		List<Criterion> restrictionsList1 = new ArrayList<Criterion>();
		Criterion idCrit = Restrictions.eq("id", onboardApp.id);
		Criterion urlCrit = Restrictions.eq("url", onboardApp.url);
		Criterion nameCrit = Restrictions.eq("name", onboardApp.name);
		Criterion orCrit = Restrictions.or(idCrit, urlCrit, nameCrit);
		restrictionsList1.add(orCrit);
		Mockito.when((List<EPApp>) dataAccessService.getList(EPApp.class, null, restrictionsList1, null))
				.thenReturn(mockAppList);
		Mockito.when((EPApp) session.get(EPApp.class, onboardApp.id)).thenReturn(mockApp);
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ "FROM fn_menu_functional m, fn_menu_functional_roles r " + "WHERE m.menu_id = r.menu_id "
				+ " AND r.app_id = '" + onboardApp.id + "' ";
		List<Integer> roles = new ArrayList<>();
		roles.add(1);
		roles.add(2);
		List<FunctionalMenuItem> menuItems = new ArrayList<>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		functionalMenuItem.setRestrictedApp(false);
		functionalMenuItem.setUrl("http://test1.com");
		functionalMenuItem.setRoles(roles);
		menuItems.add(functionalMenuItem);
		Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null)).thenReturn(menuItems);
		Mockito.when((FunctionalMenuItem) session.get(FunctionalMenuItem.class, functionalMenuItem.menuId))
				.thenReturn(functionalMenuItem);
		Mockito.doNothing().when(epUebHelper).addPublisher(mockApp);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		FieldsValidator actual = epAppCommonServiceImpl.modifyOnboardingApp(onboardApp, user);
		assertEquals(expected, actual);
	}

	@Test
	public void saveWidgetsSortManualTest() {
		EPUser user = mockUser.mockEPUser();
		List<EPWidgetsManualSortPreference> mockEPManualWidgets = new ArrayList<>();
		EPWidgetsManualSortPreference epWidgetsManualSortPreference = new EPWidgetsManualSortPreference();
		epWidgetsManualSortPreference.setUserId(user.getId());
		epWidgetsManualSortPreference.setWidgetCol(1);
		epWidgetsManualSortPreference.setWidgetHeight(1);
		epWidgetsManualSortPreference.setWidgetId(1l);
		epWidgetsManualSortPreference.setWidgetRow(1);
		epWidgetsManualSortPreference.setWidgetWidth(1);
		mockEPManualWidgets.add(epWidgetsManualSortPreference);
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userWidgetManualSortPrfQuery", params, null))
				.thenReturn(mockEPManualWidgets);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		List<EPWidgetsSortPreference> epWidgetsSortPreferenceList = new ArrayList<>();
		EPWidgetsSortPreference mockEPWidgetsSortPreference = new EPWidgetsSortPreference();
		mockEPWidgetsSortPreference.setRow(2);
		mockEPWidgetsSortPreference.setHeaderText("test");
		mockEPWidgetsSortPreference.setSizeX(2);
		mockEPWidgetsSortPreference.setSizeY(2);
		mockEPWidgetsSortPreference.setWidgetid(2l);
		mockEPWidgetsSortPreference.setWidgetIdentifier("test");
		mockEPWidgetsSortPreference.setCol(2);
		epWidgetsSortPreferenceList.add(mockEPWidgetsSortPreference);
		FieldsValidator actual = epAppCommonServiceImpl.saveWidgetsSortManual(epWidgetsSortPreferenceList, user);
		assertEquals(expected, actual);
	}

	@Test
	public void saveWidgetsSortManualExistingRecordTest() {
		EPUser user = mockUser.mockEPUser();
		List<EPWidgetsManualSortPreference> mockEPManualWidgets = new ArrayList<>();
		EPWidgetsManualSortPreference epWidgetsManualSortPreference = new EPWidgetsManualSortPreference();
		epWidgetsManualSortPreference.setUserId(user.getId());
		epWidgetsManualSortPreference.setWidgetCol(1);
		epWidgetsManualSortPreference.setWidgetHeight(1);
		epWidgetsManualSortPreference.setWidgetId(2l);
		epWidgetsManualSortPreference.setWidgetRow(1);
		epWidgetsManualSortPreference.setWidgetWidth(1);
		mockEPManualWidgets.add(epWidgetsManualSortPreference);
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userWidgetManualSortPrfQuery", params, null))
				.thenReturn(mockEPManualWidgets);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		List<EPWidgetsSortPreference> epWidgetsSortPreferenceList = new ArrayList<>();
		EPWidgetsSortPreference mockEPWidgetsSortPreference = new EPWidgetsSortPreference();
		mockEPWidgetsSortPreference.setRow(2);
		mockEPWidgetsSortPreference.setHeaderText("test");
		mockEPWidgetsSortPreference.setSizeX(2);
		mockEPWidgetsSortPreference.setSizeY(2);
		mockEPWidgetsSortPreference.setWidgetid(2l);
		mockEPWidgetsSortPreference.setWidgetIdentifier("test");
		mockEPWidgetsSortPreference.setCol(2);
		epWidgetsSortPreferenceList.add(mockEPWidgetsSortPreference);
		FieldsValidator actual = epAppCommonServiceImpl.saveWidgetsSortManual(epWidgetsSortPreferenceList, user);
		assertEquals(expected, actual);
	}

	@Test
	public void deleteUserWidgetSortPrefTest() {
		EPUser user = mockUser.mockEPUser();
		List<EPWidgetsManualSortPreference> mockEPManualWidgets = new ArrayList<>();
		EPWidgetsManualSortPreference epWidgetsManualSortPreference = new EPWidgetsManualSortPreference();
		epWidgetsManualSortPreference.setUserId(user.getId());
		epWidgetsManualSortPreference.setWidgetCol(1);
		epWidgetsManualSortPreference.setWidgetHeight(1);
		epWidgetsManualSortPreference.setWidgetId(2l);
		epWidgetsManualSortPreference.setWidgetRow(1);
		epWidgetsManualSortPreference.setWidgetWidth(1);
		mockEPManualWidgets.add(epWidgetsManualSortPreference);
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userWidgetManualSortPrfQuery", params, null))
				.thenReturn(mockEPManualWidgets);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		List<EPWidgetsSortPreference> epWidgetsSortPreferenceList = new ArrayList<>();
		EPWidgetsSortPreference mockEPWidgetsSortPreference = new EPWidgetsSortPreference();
		mockEPWidgetsSortPreference.setRow(2);
		mockEPWidgetsSortPreference.setHeaderText("test");
		mockEPWidgetsSortPreference.setSizeX(2);
		mockEPWidgetsSortPreference.setSizeY(2);
		mockEPWidgetsSortPreference.setWidgetid(2l);
		mockEPWidgetsSortPreference.setWidgetIdentifier("test");
		mockEPWidgetsSortPreference.setCol(2);
		epWidgetsSortPreferenceList.add(mockEPWidgetsSortPreference);
		FieldsValidator actual = epAppCommonServiceImpl.deleteUserWidgetSortPref(epWidgetsSortPreferenceList, user);
		assertEquals(expected, actual);
	}

	@Test
	public void saveAppsSortPreferenceForNewUserTest() {
		EPUser user = mockUser.mockEPUser();
		List<EPUserAppsSortPreference> mockEPAppSortPrefList = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userAppsSortPreferenceQuery", params, null))
				.thenReturn(mockEPAppSortPrefList);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		EPAppsSortPreference mockEPAppsSortPreference = new EPAppsSortPreference();
		mockEPAppsSortPreference.setIndex(1);
		mockEPAppsSortPreference.setTitle("Last Used");
		mockEPAppsSortPreference.setValue("L");
		FieldsValidator actual = epAppCommonServiceImpl.saveAppsSortPreference(mockEPAppsSortPreference, user);
		assertEquals(expected, actual);
	}

	@Test
	public void saveAppsSortPreferenceUpdateTest() {
		EPUser user = mockUser.mockEPUser();
		List<EPUserAppsSortPreference> mockEPAppSortPrefList = new ArrayList<>();
		EPUserAppsSortPreference mockEPAppSortPref = new EPUserAppsSortPreference();
		mockEPAppSortPref.setSortPref("L");
		mockEPAppSortPref.setId(2l);
		mockEPAppSortPref.setUserId((int) (long) user.getId());
		mockEPAppSortPrefList.add(mockEPAppSortPref);
		final Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userAppsSortPreferenceQuery", params, null))
				.thenReturn(mockEPAppSortPrefList);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		EPAppsSortPreference mockEPAppsSortPreference = new EPAppsSortPreference();
		mockEPAppsSortPreference.setIndex(1);
		mockEPAppsSortPreference.setTitle("Last Used");
		mockEPAppsSortPreference.setValue("L");
		FieldsValidator actual = epAppCommonServiceImpl.saveAppsSortPreference(mockEPAppsSortPreference, user);
		assertEquals(expected, actual);
	}

	@Test
	public void getUserAppsSortTypePreferenceTest() {
		EPUser user = mockUser.mockEPUser();
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsSortPreference> mockEPAppSortPrefList = new ArrayList<>();
		EPUserAppsSortPreference mockEPAppSortPref = new EPUserAppsSortPreference();
		mockEPAppSortPref.setSortPref("L");
		mockEPAppSortPref.setId(2l);
		mockEPAppSortPref.setUserId((int) (long) user.getId());
		mockEPAppSortPrefList.add(mockEPAppSortPref);
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userAppsSortPreferenceQuery", params, null))
				.thenReturn(mockEPAppSortPrefList);
		String actual = epAppCommonServiceImpl.getUserAppsSortTypePreference(user);
		assertEquals(mockEPAppSortPref.getSortPref(), actual);
	}

	@Test
	public void getUserAppsSortTypePreferenceExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsSortPreference> mockEPAppSortPrefList = new ArrayList<>();
		EPUserAppsSortPreference mockEPAppSortPref = new EPUserAppsSortPreference();
		mockEPAppSortPref.setSortPref("L");
		mockEPAppSortPref.setId(2l);
		mockEPAppSortPref.setUserId((int) (long) user.getId());
		mockEPAppSortPrefList.add(mockEPAppSortPref);
		params.put("userId", user.getId());
		Mockito.doThrow(new NullPointerException()).when(dataAccessService)
				.executeNamedQuery("userAppsSortPreferenceQuery", params, null);
		String actual = epAppCommonServiceImpl.getUserAppsSortTypePreference(user);
		assertNull(actual);
	}

	@Test
	public void deleteUserAppSortManualTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setId(1l);
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsManualSortPreference> epManualApps = new ArrayList<EPUserAppsManualSortPreference>();
		EPUserAppsManualSortPreference epManualApp = new EPUserAppsManualSortPreference();
		epManualApp.setAppId(mockApp.getId());
		epManualApp.setAppManualSortOrder(1);
		epManualApp.setId(1l);
		epManualApp.setUserId(user.getId());
		epManualApps.add(epManualApp);
		params.put("userId", user.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userAppsManualSortPrfQuery", params, null)).thenReturn(epManualApps);
		EPDeleteAppsManualSortPref delAppSortManual = new EPDeleteAppsManualSortPref();
		delAppSortManual.setAppId(mockApp.getId());
		delAppSortManual.setPending(false);
		delAppSortManual.setSelect(false);
		FieldsValidator actual = epAppCommonServiceImpl.deleteUserAppSortManual(delAppSortManual, user);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		assertEquals(expected, actual);
	}
	
	@Test
	public void deleteUserAppSortManualExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setId(1l);
		final Map<String, Long> params = new HashMap<>();
		List<EPUserAppsManualSortPreference> epManualApps = new ArrayList<EPUserAppsManualSortPreference>();
		EPUserAppsManualSortPreference epManualApp = new EPUserAppsManualSortPreference();
		epManualApp.setAppId(mockApp.getId());
		epManualApp.setAppManualSortOrder(1);
		epManualApp.setId(1l);
		epManualApp.setUserId(user.getId());
		epManualApps.add(epManualApp);
		params.put("userId", user.getId());
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeNamedQuery("userAppsManualSortPrfQuery", params, null);
		EPDeleteAppsManualSortPref delAppSortManual = new EPDeleteAppsManualSortPref();
		delAppSortManual.setAppId(mockApp.getId());
		delAppSortManual.setPending(false);
		delAppSortManual.setSelect(false);
		FieldsValidator actual = epAppCommonServiceImpl.deleteUserAppSortManual(delAppSortManual, user);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		assertEquals(expected, actual);
	}
}
