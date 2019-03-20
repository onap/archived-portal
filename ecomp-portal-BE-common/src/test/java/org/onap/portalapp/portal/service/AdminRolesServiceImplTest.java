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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.UserRole;
import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;
import org.onap.portalapp.portal.transport.AppsListWithAdminRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, PortalConstants.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class AdminRolesServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();

	@Mock
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;

	@Mock
	RestTemplate template = new RestTemplate();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
	}

	@After
	public void after() {
		session.close();
	}

	@InjectMocks
	AdminRolesServiceImpl adminRolesServiceImpl = new AdminRolesServiceImpl();

	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	
	private Long ECOMP_APP_ID = 1L;
	
	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setNameSpace("com.test.app");
		app.setCentralAuth(true);
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
		app.setEnabled(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	MockEPUser mockUser = new MockEPUser();

	@SuppressWarnings("deprecation")
	@Test
	public void getAppsWithAdminRoleStateForUserTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId(1l);
		List<EPUser> users = new ArrayList<>();
		users.add(user);
		Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", user.getOrgUserId());
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null)).thenReturn(users);
		List<EPUserApp> userAppList = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(1l);
		role.setName("test role");
		epUserApp.setApp(app);
		epUserApp.setRole(role);
		epUserApp.setUserId(1l);
		userAppList.add(epUserApp);
		Mockito.when(dataAccessService.getList(EPUserApp.class,
				" where userId = " + user.getId() + " and role.id = " + 999, null, null)).thenReturn(userAppList);
		List<EPApp> appsList = new ArrayList<>();
		appsList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class,
				"  where ( enabled = 'Y' or id = " + ECOMP_APP_ID + ")", null, null)).thenReturn(appsList);
		AppsListWithAdminRole  actual = adminRolesServiceImpl.getAppsWithAdminRoleStateForUser(user.getOrgUserId());
		assertNotNull(actual);
	}
	
	@Test
	public void getAppsWithAdminRoleStateForUserTestWithException() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId(1l);
		List<EPUser> users = new ArrayList<>();
		users.add(user);
		Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", user.getOrgUserId());
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null)).thenReturn(users);
		AppsListWithAdminRole  actual = adminRolesServiceImpl.getAppsWithAdminRoleStateForUser(user.getOrgUserId());

		
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	public void setAppsWithAdminRoleStateForUserTest() {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId(1l);
		EPApp app2 = mockApp();
		app2.setName("app2");
		app2.setNameSpace("com.test.app2");
		app2.setId(2l);
		EPApp app3 = mockApp();
		app3.setName("app3");
		app3.setNameSpace("com.test.app3");
		app3.setId(3l);
		List<EPApp> apps = new ArrayList<>();
		apps.add(app);
		apps.add(app2);
		apps.add(app3);
		Mockito.when(epAppCommonServiceImpl.getAppsFullList()).thenReturn(apps);

		List<EPUser> localUserList = new ArrayList<>();
		localUserList.add(user);
		Mockito.when(
				dataAccessService.getList(EPUser.class, " where org_user_id='" + user.getOrgUserId() + "'", null, null))
				.thenReturn(localUserList);
		List<EPUserApp> oldAppsWhereUserIsAdmin = new ArrayList<EPUserApp>();
		EPUserApp epUserApp = new EPUserApp();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(999l);
		role.setName("app5");
		epUserApp.setApp(app);
		epUserApp.setRole(role);
		epUserApp.setUserId(1l);
		oldAppsWhereUserIsAdmin.add(epUserApp);
		Mockito.when(dataAccessService.getList(EPUserApp.class,
				" where userId = " + user.getId() + " and role.id = " + ACCOUNT_ADMIN_ROLE_ID, null,
				null)).thenReturn(oldAppsWhereUserIsAdmin);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		EPApp app4 = mockApp();
		app4.setId(6l);
		app4.setName("app7");
		app4.setNameSpace("com.test.app7");
		List<EPApp> apps2 = new ArrayList<>();
		apps2.add(app);
		apps2.add(app2);
		apps2.add(app3);
		apps2.add(app4);
		Mockito.when(dataAccessService.executeNamedQuery("getCentralizedApps", null, null)).thenReturn(apps2);
		Mockito.when(
				EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		JSONObject getUserRoles = new JSONObject();
		ResponseEntity<String> getResponse = new ResponseEntity<>(getUserRoles.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		
		AppsListWithAdminRole newAppsListWithAdminRoles = new AppsListWithAdminRole();
		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<>();
		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
		appNameIdIsAdmin.setAppName("app1");
		appNameIdIsAdmin.setId(2l);
		appNameIdIsAdmin.setIsAdmin(true);
		appNameIdIsAdmin.setRestrictedApp(false);
		AppNameIdIsAdmin appNameIdIsAdmin2 = new AppNameIdIsAdmin();
		appNameIdIsAdmin2.setAppName("app2");
		appNameIdIsAdmin2.setId(3l);
		appNameIdIsAdmin2.setIsAdmin(true);
		appNameIdIsAdmin2.setRestrictedApp(false);
		appsRoles.add(appNameIdIsAdmin);
		appsRoles.add(appNameIdIsAdmin2);
		newAppsListWithAdminRoles.setOrgUserId(user.getOrgUserId());
		newAppsListWithAdminRoles.setAppsRoles(appsRoles);
		Mockito.when((EPApp) session.get(EPApp.class, appNameIdIsAdmin.id)).thenReturn(app2);
		Mockito.when((EPApp) session.get(EPApp.class, appNameIdIsAdmin2.id)).thenReturn(app3);
		JSONObject getUserRoles2 = new JSONObject();
		JSONObject getUserRoles3 = new JSONObject();
		JSONObject getUserRoles4 = new JSONObject();
		JSONObject finalUserRoles = new JSONObject();
		getUserRoles2.put("role", "com.test.app3.Account_Administrator");
		getUserRoles3.put("role", "com.test.app3.admin");
		getUserRoles4.put("role", "com.test.app3.owner");
		List<JSONObject> userRoles =  new ArrayList<>();
		userRoles.add(getUserRoles2);
		userRoles.add(getUserRoles3);
		userRoles.add(getUserRoles4);
		finalUserRoles.put("userRole", userRoles);
		ResponseEntity<String> getResponse2 = new ResponseEntity<>(finalUserRoles.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse2);
		boolean actual = adminRolesServiceImpl.setAppsWithAdminRoleStateForUser(newAppsListWithAdminRoles);
		assertTrue(actual);
	}

	@Test
	public void isSuperAdminTest() {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		List<UserRole> userRoleList = new ArrayList<>();
		UserRole userRole = new UserRole();
		userRole.setFirstName("Hello");
		userRole.setLastName("World");
		userRole.setRoleId(1l);
		userRole.setRoleName("test");
		userRole.setUser_Id(1l);
		userRoleList.add(userRole);
		Mockito.when(dataAccessService.executeSQLQuery(Matchers.anyString(), Matchers.any(), Matchers.anyMap()))
				.thenReturn(userRoleList);
		boolean actual = adminRolesServiceImpl.isSuperAdmin(user);
		assertTrue(actual);
	}

	@Test
	public void isSuperAdminExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeSQLQuery(Matchers.anyString(),
				Matchers.any(), Matchers.anyMap());
		boolean actual = adminRolesServiceImpl.isSuperAdmin(user);
		assertFalse(actual);
	}

	@Test
	public void isAccountAdminTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId(2l);
		SortedSet<EPUserApp> userApps = user.getEPUserApps();
		EPUserApp epUserApp = new EPUserApp();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(999l);
		role.setName("test role");
		epUserApp.setApp(app);
		epUserApp.setRole(role);
		epUserApp.setUserId(1l);
		userApps.add(epUserApp);
		user.setUserApps(userApps);
		Mockito.when((EPUser) dataAccessService.getDomainObject(Matchers.any(), Matchers.anyLong(), Matchers.anyMap()))
				.thenReturn(user);
		boolean actual = adminRolesServiceImpl.isAccountAdmin(user);
		assertTrue(actual);
	}

	@Test
	public void isAccountAdminExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).getDomainObject(Matchers.any(),
				Matchers.anyLong(), Matchers.anyMap());
		boolean actual = adminRolesServiceImpl.isAccountAdmin(user);
		assertFalse(actual);
	}

	@Test
	public void isUserTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId(2l);
		SortedSet<EPUserApp> userApps = user.getEPUserApps();
		EPUserApp epUserApp = new EPUserApp();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(2l);
		role.setName("test role");
		epUserApp.setApp(app);
		epUserApp.setRole(role);
		epUserApp.setUserId(1l);
		userApps.add(epUserApp);
		user.setUserApps(userApps);
		Mockito.when((EPUser) dataAccessService.getDomainObject(Matchers.any(), Matchers.anyLong(), Matchers.anyMap()))
				.thenReturn(user);
		boolean actual = adminRolesServiceImpl.isUser(user);
		assertTrue(actual);
	}

	@Test
	public void isUserExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).getDomainObject(Matchers.any(),
				Matchers.anyLong(), Matchers.anyMap());
		boolean actual = adminRolesServiceImpl.isUser(user);
		assertFalse(actual);
	}

	@Test
	public void getRolesByAppTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		List<EPRole> expected = new ArrayList<>();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(1l);
		role.setName("test role");
		expected.add(role);
		Mockito.when(dataAccessService.executeSQLQuery(Matchers.anyString(), Matchers.any(), Matchers.anyMap()))
				.thenReturn(expected);
		List<EPRole> actual = adminRolesServiceImpl.getRolesByApp(user, app.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void isAccountAdminOfApplicationTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		SortedSet<EPUserApp> userApps = user.getEPUserApps();
		EPUserApp epUserApp = new EPUserApp();
		EPRole role = new EPRole();
		role.setActive(true);
		role.setId(999l);
		role.setName("test role");
		epUserApp.setApp(app);
		epUserApp.setRole(role);
		epUserApp.setUserId(1l);
		userApps.add(epUserApp);
		user.setUserApps(userApps);
		List<Integer> userAdminApps =  new ArrayList<>();
		userAdminApps.add(1);
		userAdminApps.add(2);
		Mockito.when(dataAccessService.executeNamedQuery(Matchers.anyString(), Matchers.anyMap(), Matchers.anyMap()))
				.thenReturn(userAdminApps);
		boolean actual = adminRolesServiceImpl.isAccountAdminOfApplication(user, app);
		assertTrue(actual);
	}

	@Test
	public void isAccountAdminOfApplicationExceptionTest() {
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).getDomainObject(Matchers.any(),
				Matchers.anyLong(), Matchers.anyMap());
		boolean actual = adminRolesServiceImpl.isAccountAdminOfApplication(user, app);
		assertFalse(actual);
	}
}
