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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.http.HTTPException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemRoleApproval;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.EPUserAppCatalogRoles;
import org.onap.portalapp.portal.domain.EPUserAppRoles;
import org.onap.portalapp.portal.domain.EPUserAppRolesRequest;
import org.onap.portalapp.portal.domain.EPUserAppRolesRequestDetail;
import org.onap.portalapp.portal.domain.ExternalSystemAccess;
import org.onap.portalapp.portal.transport.AppWithRolesForUser;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.EPUserAppCurrentRoles;
import org.onap.portalapp.portal.transport.EcompUserAppRoles;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;
import org.onap.portalapp.portal.transport.RemoteRole;
import org.onap.portalapp.portal.transport.RemoteUserWithRoles;
import org.onap.portalapp.portal.transport.RoleInAppForUser;
import org.onap.portalapp.portal.transport.UserApplicationRoles;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.service.RoleServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, SystemProperties.class, PortalConstants.class,
		EPCommonSystemProperties.class })
public class UserRolesCommonServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();

	@Mock
	ExternalAccessRolesServiceImpl externalAccessRolesServiceImpl = new ExternalAccessRolesServiceImpl();

	@Mock
	ApplicationsRestClientService applicationsRestClientService;

	@Mock
	RoleServiceImpl roleServiceImpl = new RoleServiceImpl();

	@Mock
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	EPRoleServiceImpl epRoleServiceImpl = new EPRoleServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;

	@InjectMocks
	UserRolesCommonServiceImpl userRolesCommonServiceImpl = new UserRolesCommonServiceImpl();

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

	MockEPUser mockUser = new MockEPUser();

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

	@SuppressWarnings("unchecked")
	@Test
	public void getAppRolesForUserCentralizedForPortal() throws Exception {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<RoleInAppForUser> mockRoleInAppForUserList = getMockedRoleInAppUserList();
		List<CentralV2Role> mockCenV2Role = new ArrayList<>();
		CentralV2Role cenV2Role = new CentralV2Role(1l, null, null, null, null, null, "test1", true, null,
				new TreeSet<>(), new TreeSet<>(), new TreeSet<>());
		CentralV2Role cenV2Role2 = new CentralV2Role(16l, null, null, null, null, null, "test2", true, null,
				new TreeSet<>(), new TreeSet<>(), new TreeSet<>());
		mockCenV2Role.add(cenV2Role);
		mockCenV2Role.add(cenV2Role2);
		Mockito.when(externalAccessRolesServiceImpl.getRolesForApp(mockApp.getUebKey())).thenReturn(mockCenV2Role);
		List<EPUser> mockUserList = new ArrayList<>();
		mockUserList.add(user);
		Mockito.when((List<EPUser>) dataAccessService
				.executeQuery("from EPUser where orgUserId='" + user.getOrgUserId() + "'", null))
				.thenReturn(mockUserList);
		Mockito.when(userRolesCommonServiceImpl.getAppRolesForUser(1l, user.getOrgUserId(), true, user))
				.thenReturn(mockRoleInAppForUserList);
		List<RoleInAppForUser> roleInAppForUser = userRolesCommonServiceImpl.getAppRolesForUser(1l, "test", true, user);
		assertEquals(roleInAppForUser, mockRoleInAppForUserList);
	}

	private List<RoleInAppForUser> getMockedRoleInAppUserList() {
		List<RoleInAppForUser> mockRoleInAppForUserList = new ArrayList<>();
		RoleInAppForUser mockRoleInAppForUser = new RoleInAppForUser();
		mockRoleInAppForUser.setIsApplied(false);
		mockRoleInAppForUser.setRoleId(1l);
		mockRoleInAppForUser.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(false);
		mockRoleInAppForUser2.setRoleId(16l);
		mockRoleInAppForUser2.setRoleName("test2");
		mockRoleInAppForUserList.add(mockRoleInAppForUser);
		mockRoleInAppForUserList.add(mockRoleInAppForUser2);
		return mockRoleInAppForUserList;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAppRolesForUserNonCentralizedForPortal() throws Exception {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		EPApp mockApp = mockApp();
		mockApp.setCentralAuth(false);
		EPRole mockEPRole = new EPRole();
		mockEPRole.setId(1l);
		mockEPRole.setName("test1");
		mockEPRole.setActive(true);
		SortedSet<EPUserApp> mockUserApps = new TreeSet<EPUserApp>();
		EPUserApp mockEPUserApp = new EPUserApp();
		mockEPUserApp.setApp(mockApp);
		mockEPUserApp.setRole(mockEPRole);
		mockEPUserApp.setUserId(1l);
		mockUserApps.add(mockEPUserApp);
		user.setEPUserApps(mockUserApps);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<RoleInAppForUser> mockRoleInAppForUserListNonCentralizedList = getMockedRoleInAppUserList();
		List<Role> mockRole = new ArrayList<>();
		Role role = new Role();
		role.setId(1l);
		role.setName("test1");
		role.setActive(true);
		Role role2 = new Role();
		role.setId(16l);
		role.setName("test2");
		role.setActive(true);
		mockRole.add(role);
		mockRole.add(role2);
		Mockito.when(roleServiceImpl.getAvailableRoles(user.getOrgUserId())).thenReturn(mockRole);
		List<EPUser> mockUserList = new ArrayList<>();
		mockUserList.add(user);
		Mockito.when((List<EPUser>) dataAccessService
				.executeQuery("from EPUser where orgUserId='" + user.getOrgUserId() + "'", null))
				.thenReturn(mockUserList);
		Mockito.when(userRolesCommonServiceImpl.getAppRolesForUser(1l, user.getOrgUserId(), true, user))
				.thenReturn(mockRoleInAppForUserListNonCentralizedList);
		List<RoleInAppForUser> roleInAppForUserNonCentralized = userRolesCommonServiceImpl.getAppRolesForUser(1l,
				user.getOrgUserId(), true, user);
		assertNull(roleInAppForUserNonCentralized);
	}

	@Test
	public void getAppRolesForCentralizedPartnerAppTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<RoleInAppForUser> mockRoleInAppForUserList = getMockedRoleInAppUserList();
		List<EPRole> mockEPRoleList = new ArrayList<>();
		EPRole mockEpRole = new EPRole();
		mockEpRole.setActive(true);
		mockEpRole.setAppId(2l);
		mockEpRole.setName("test1");
		mockEpRole.setAppRoleId(333l);
		mockEpRole.setId(1l);
		EPRole mockEpRole2 = new EPRole();
		mockEpRole2.setActive(true);
		mockEpRole2.setAppId(2l);
		mockEpRole2.setName("test2");
		mockEpRole2.setAppRoleId(444l);
		mockEpRole2.setId(16l);
		mockEPRoleList.add(mockEpRole);
		mockEPRoleList.add(mockEpRole2);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", 2l);
		Mockito.when(dataAccessService.executeNamedQuery("getActiveRolesOfApplication", appParams, null))
				.thenReturn(mockEPRoleList);
		final Map<String, String> params = new HashMap<>();
		params.put("orgUserIdValue", "guestT");
		List<EPUser> mockEPUserList = new ArrayList<>();
		mockEPUserList.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("epUserAppId", params, null)).thenReturn(mockEPUserList);
		final Map<String, Long> userParams = new HashMap<>();
		userParams.put("appId", mockApp.getId());
		userParams.put("userId", mockEPUserList.get(0).getId());
		List<EPUserAppCurrentRoles> epUserAppCurrentRolesList = new ArrayList<>();
		EPUserAppCurrentRoles epUserAppCurrentRoles = new EPUserAppCurrentRoles();
		epUserAppCurrentRoles.setRoleId(444l);
		epUserAppCurrentRoles.setRoleName("TestPartnerRole2");
		epUserAppCurrentRoles.setUserId(mockEPUserList.get(0).getId());
		epUserAppCurrentRolesList.add(epUserAppCurrentRoles);
		Mockito.when(dataAccessService.executeNamedQuery("getUserAppCurrentRoles", userParams, null))
				.thenReturn(epUserAppCurrentRolesList);
		Mockito.when(userRolesCommonServiceImpl.getAppRolesForUser(2l, user.getOrgUserId(), true, user))
				.thenReturn(mockRoleInAppForUserList);
		List<RoleInAppForUser> roleInAppForUser = userRolesCommonServiceImpl.getAppRolesForUser(2l, user.getOrgUserId(),
				true, user);
		assertNotEquals(roleInAppForUser, mockRoleInAppForUserList);
	}

	@Test
	public void getAppRolesForNonCentralizedPartnerAppTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		user.setId(2l);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setCentralAuth(false);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<RoleInAppForUser> mockRoleInAppForUserList = new ArrayList<>();
		RoleInAppForUser mockRoleInAppForUser = new RoleInAppForUser();
		mockRoleInAppForUser.setIsApplied(true);
		mockRoleInAppForUser.setRoleId(333l);
		mockRoleInAppForUser.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(true);
		mockRoleInAppForUser2.setRoleId(777l);
		mockRoleInAppForUser2.setRoleName("test2");
		RoleInAppForUser mockRoleInAppForUser3 = new RoleInAppForUser();
		mockRoleInAppForUser3.setIsApplied(false);
		mockRoleInAppForUser3.setRoleId(888l);
		mockRoleInAppForUser3.setRoleName("test5");
		mockRoleInAppForUserList.add(mockRoleInAppForUser);
		mockRoleInAppForUserList.add(mockRoleInAppForUser2);
		mockRoleInAppForUserList.add(mockRoleInAppForUser3);
		List<EcompRole> mockEcompRoleList = new ArrayList<>();
		EcompRole mockEcompRole = new EcompRole();
		mockEcompRole.setId(333l);
		mockEcompRole.setName("test1");
		EcompRole mockEcompRole2 = new EcompRole();
		mockEcompRole2.setId(777l);
		mockEcompRole2.setName("test2");
		EcompRole mockEcompRole3 = new EcompRole();
		mockEcompRole3.setId(888l);
		mockEcompRole3.setName("test5");
		mockEcompRoleList.add(mockEcompRole);
		mockEcompRoleList.add(mockEcompRole2);
		mockEcompRoleList.add(mockEcompRole3);
		EcompRole[] mockEcompRoleArray = mockEcompRoleList.toArray(new EcompRole[mockEcompRoleList.size()]);
		List<EPRole> mockEPRoleList = new ArrayList<>();
		EPRole mockEpRole = new EPRole();
		mockEpRole.setActive(true);
		mockEpRole.setAppId(2l);
		mockEpRole.setName("test1");
		mockEpRole.setAppRoleId(333l);
		mockEpRole.setId(16l);
		EPRole mockEpRole2 = new EPRole();
		mockEpRole2.setActive(true);
		mockEpRole2.setAppId(2l);
		mockEpRole2.setName("test3");
		mockEpRole2.setAppRoleId(555l);
		mockEpRole2.setId(15l);
		EPRole mockEpRole3 = new EPRole();
		mockEpRole3.setActive(true);
		mockEpRole3.setAppId(2l);
		mockEpRole3.setName("test4");
		mockEpRole3.setAppRoleId(888l);
		mockEpRole3.setId(17l);
		mockEPRoleList.add(mockEpRole);
		mockEPRoleList.add(mockEpRole2);
		mockEPRoleList.add(mockEpRole3);
		List<EPUserApp> mockUserRolesList = new ArrayList<>();
		EPUserApp mockEpUserApp = new EPUserApp();
		mockEpUserApp.setApp(mockApp);
		mockEpUserApp.setRole(mockEpRole2);
		mockEpUserApp.setUserId(user.getId());
		mockUserRolesList.add(mockEpUserApp);
		List<FunctionalMenuRole> mockFunctionalMenuRolesList = new ArrayList<>();
		FunctionalMenuRole mockFunctionalMenuRole = new FunctionalMenuRole();
		mockFunctionalMenuRole.setAppId((int) (long) mockApp.getId());
		mockFunctionalMenuRole.setRoleId((int) (long) 15l);
		mockFunctionalMenuRole.setMenuId(10l);
		mockFunctionalMenuRole.setId(10);
		mockFunctionalMenuRolesList.add(mockFunctionalMenuRole);
		List<FunctionalMenuItem> mockFunctionalMenuItemList = new ArrayList<>();
		FunctionalMenuItem mockFunctionalMenuItem = new FunctionalMenuItem();
		List<Integer> mockRolesList = new ArrayList<>();
		Integer role1 = 1;
		mockRolesList.add(role1);
		mockFunctionalMenuItem.setRestrictedApp(false);
		mockFunctionalMenuItem.setRoles(mockRolesList);
		mockFunctionalMenuItem.setUrl("http://test.com");
		mockFunctionalMenuItemList.add(mockFunctionalMenuItem);
		Query epRoleQuery = Mockito.mock(Query.class);
		Query epUserAppsQuery = Mockito.mock(Query.class);
		Query epFunctionalMenuQuery = Mockito.mock(Query.class);
		Query epFunctionalMenuQuery2 = Mockito.mock(Query.class);
		Query epFunctionalMenuItemQuery = Mockito.mock(Query.class);
		Query epUserListQuery = Mockito.mock(Query.class);
		Query epUserRolesListQuery = Mockito.mock(Query.class);
		Mockito.when(applicationsRestClientService.get(EcompRole[].class, mockApp.getId(), "/roles"))
				.thenReturn(mockEcompRoleArray);
		// syncAppRolesTest

		Mockito.when(session.createQuery("from :name where appId = :appId"))
				.thenReturn(epRoleQuery);

		Mockito.when(epRoleQuery.setParameter("name",EPRole.class.getName())).thenReturn(epRoleQuery);
		Mockito.when(epRoleQuery.setParameter("appId",mockApp.getId())).thenReturn(epRoleQuery);

		Mockito.doReturn(mockEPRoleList).when(epRoleQuery).list();
		Mockito.when(session.createQuery(
				"from " + EPUserApp.class.getName() + " where app.id=" + mockApp.getId() + " and role_id=" + 15l))
				.thenReturn(epUserAppsQuery);
		Mockito.doReturn(mockUserRolesList).when(epUserAppsQuery).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuRole.class.getName() + " where roleId=" + 15l))
				.thenReturn(epFunctionalMenuQuery);
		Mockito.doReturn(mockFunctionalMenuRolesList).when(epFunctionalMenuQuery).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuRole.class.getName() + " where menuId=" + 10l))
				.thenReturn(epFunctionalMenuQuery2);
		Mockito.doReturn(mockFunctionalMenuRolesList).when(epFunctionalMenuQuery2).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuItem.class.getName() + " where menuId=" + 10l))
				.thenReturn(epFunctionalMenuItemQuery);
		Mockito.doReturn(mockFunctionalMenuItemList).when(epFunctionalMenuItemQuery).list();
		List<EcompRole> mockEcompRoleList2 = new ArrayList<>();
		EcompRole mockUserAppRoles = new EcompRole();
		mockUserAppRoles.setId(333l);
		mockUserAppRoles.setName("test1");
		EcompRole mockUserAppRoles2 = new EcompRole();
		mockUserAppRoles2.setId(777l);
		mockUserAppRoles2.setName("test2");
		mockEcompRoleList2.add(mockUserAppRoles);
		mockEcompRoleList2.add(mockUserAppRoles2);
		EcompRole[] mockEcompRoleArray2 = mockEcompRoleList2.toArray(new EcompRole[mockEcompRoleList2.size()]);
		Mockito.when(applicationsRestClientService.get(EcompRole[].class, mockApp.getId(),
				String.format("/user/%s/roles", user.getOrgUserId()))).thenReturn(mockEcompRoleArray2);
		// SyncUserRoleTest
		Mockito.when(session
				.createQuery("from " + EPUser.class.getName() + " where orgUserId='" + user.getOrgUserId() + "'"))
				.thenReturn(epUserListQuery);
		Mockito.doReturn(mockEpUserList).when(epUserListQuery).list();

		List<EPUserApp> mockUserRolesList2 = new ArrayList<>();
		EPUserApp mockEpUserAppRoles = new EPUserApp();
		mockEpUserAppRoles.setApp(mockApp);
		mockEpUserAppRoles.setRole(mockEpRole3);
		mockEpUserAppRoles.setUserId(user.getId());
		mockUserRolesList2.add(mockEpUserAppRoles);
		Mockito.when(session.createQuery(
				"from org.onap.portalapp.portal.domain.EPUserApp where app.id=2 and role.active = 'Y' and userId=2"))
				.thenReturn(epUserRolesListQuery);
		Mockito.doReturn(mockUserRolesList2).when(epUserRolesListQuery).list();
		List<RoleInAppForUser> roleInAppForUser = userRolesCommonServiceImpl.getAppRolesForUser(2l, user.getOrgUserId(),
				true, user);
		assertEquals(roleInAppForUser, mockRoleInAppForUserList);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAppWithUserRoleStateForUserTestForCentralizedAppForPortal() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		EPUser user = mockUser.mockEPUser();
		user.setId(2l);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		EPApp mockApp = mockApp();
		mockApp.setNameSpace("com.test.com");
		mockApp.setId(1l);
		mockApp.setCentralAuth(true);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		AppWithRolesForUser mockWithRolesForUser = new AppWithRolesForUser();
		List<RoleInAppForUser> mockRoleInAppForUserList = new ArrayList<>();
		RoleInAppForUser mockRoleInAppForUser = new RoleInAppForUser();
		mockRoleInAppForUser.setIsApplied(true);
		mockRoleInAppForUser.setRoleId(1l);
		mockRoleInAppForUser.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(true);
		mockRoleInAppForUser2.setRoleId(1000l);
		mockRoleInAppForUser2.setRoleName("test3");
		mockRoleInAppForUserList.add(mockRoleInAppForUser);
		mockRoleInAppForUserList.add(mockRoleInAppForUser2);
		mockWithRolesForUser.setAppId(mockApp.getId());
		mockWithRolesForUser.setAppName(mockApp.getName());
		mockWithRolesForUser.setOrgUserId(user.getOrgUserId());
		mockWithRolesForUser.setAppRoles(mockRoleInAppForUserList);
		List<EPUserAppRolesRequest> mockEpRequestIdValList = new ArrayList<EPUserAppRolesRequest>();
		Set<EPUserAppRolesRequestDetail> mockEpUserAppRolesRequestDetailList = new TreeSet<>();
		EPUserAppRolesRequestDetail mockEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		EPUserAppRolesRequest mockEPUserAppRolesRequest = new EPUserAppRolesRequest();
		mockEPUserAppRolesRequest.setAppId(mockApp.getId());
		mockEPUserAppRolesRequest.setId(1l);
		mockEPUserAppRolesRequest.setRequestStatus("P");
		mockEPUserAppRolesRequest.setUserId(user.getId());
		mockEPUserAppRolesRequest.setEpRequestIdDetail(mockEpUserAppRolesRequestDetailList);
		mockEpRequestIdValList.add(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetail.setEpRequestIdData(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetailList.add(mockEpUserAppRolesRequestDetail);

		// Update Requests if any requests are pending
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", mockApp.getId());
		params.put("userId", user.getId());
		Mockito.when((List<EPUserAppRolesRequest>) dataAccessService.executeNamedQuery("userAppRolesRequestList",
				params, null)).thenReturn(mockEpRequestIdValList);
		mockEPUserAppRolesRequest.setUpdatedDate(new Date());
		mockEPUserAppRolesRequest.setRequestStatus("O");
		HashMap<String, Long> additionalUpdateParam = new HashMap<String, Long>();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockEPUserAppRolesRequest, additionalUpdateParam);
		List<EPUserAppRolesRequestDetail> mockGetEpUserAppRolesRequestDetailList = new ArrayList<EPUserAppRolesRequestDetail>();
		EPUserAppRolesRequestDetail mockGetEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		mockGetEpUserAppRolesRequestDetailList.add(mockGetEpUserAppRolesRequestDetail);
		// Updates in External Auth System
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("orgUserIdValue", user.getOrgUserId());
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("epUserAppId", userParams, null))
				.thenReturn(mockEpUserList);
		PowerMockito
				.when(EPCommonSystemProperties
						.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		PowerMockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		JSONObject mockJsonObject1 = new JSONObject();
		JSONObject mockJsonObject2 = new JSONObject();
		JSONObject mockJsonObject3 = new JSONObject();
		mockJsonObject1.put("name", "com.test.com.test1");
		mockJsonObject2.put("name", "com.test.com.test2");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObject1);
		mockJson.add(mockJsonObject2);
		mockJsonObject3.put("role", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObject3.toString(), HttpStatus.OK);
		Mockito.when(externalAccessRolesServiceImpl.getUserRolesFromExtAuthSystem(Matchers.anyString(), Matchers.any(HttpEntity.class))).thenReturn(getResponse);
		EPRole mockEPRole = new EPRole();
		mockEPRole.setActive(true);
		mockEPRole.setAppId(null);
		mockEPRole.setAppRoleId(null);
		mockEPRole.setId(1l);
		mockEPRole.setName("test1");
		EPRole mockEPRole2 = new EPRole();
		mockEPRole2.setActive(true);
		mockEPRole2.setAppId(null);
		mockEPRole2.setAppRoleId(null);
		mockEPRole2.setId(16l);
		mockEPRole2.setName("test2");
		EPRole mockEPRole3 = new EPRole();
		mockEPRole3.setActive(true);
		mockEPRole3.setAppId(null);
		mockEPRole3.setAppRoleId(null);
		mockEPRole3.setId(1000l);
		mockEPRole3.setName("test3");
		Map<String, EPRole> mockEPRoleList = new HashMap<>();
		mockEPRoleList.put("test1", mockEPRole);
		mockEPRoleList.put("test2", mockEPRole2);
		mockEPRoleList.put("test3", mockEPRole3);
		Mockito.when(externalAccessRolesServiceImpl.getAppRoleNamesWithUnderscoreMap(mockApp)).thenReturn(mockEPRoleList);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("appId", mockApp.getId());
		params2.put("userId", user.getId());
		List<EcompUserAppRoles> mockEPuserAppList = getCurrentUserRoles(user, mockApp);
		Mockito.when(dataAccessService.executeNamedQuery("getUserAppExistingRoles", params2, null))
				.thenReturn(mockEPuserAppList);
		final Map<String, Long> epDetailParams = new HashMap<>();
		epDetailParams.put("reqId", mockEPUserAppRolesRequest.getId());
		Mockito.when(dataAccessService.executeNamedQuery("userAppRolesRequestDetailList", epDetailParams, null))
				.thenReturn(mockGetEpUserAppRolesRequestDetailList);

		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);

		ResponseEntity<String> deleteResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(deleteResponse);

		// Updates in EP DB
		Query epsetAppWithUserRoleGetUserQuery = Mockito.mock(Query.class);
		Query epsetAppWithUserRoleGetUserAppsQuery = Mockito.mock(Query.class);
		Query epsetAppWithUserRoleGetRolesQuery = Mockito.mock(Query.class);
		SQLQuery epsetAppWithUserRoleUpdateEPRoleQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.get(EPApp.class, mockApp.getId())).thenReturn(mockApp);
		Mockito.when(session
				.createQuery("from " + EPUser.class.getName() + " where orgUserId='" + user.getOrgUserId() + "'"))
				.thenReturn(epsetAppWithUserRoleGetUserQuery);
		Mockito.doReturn(mockEpUserList).when(epsetAppWithUserRoleGetUserQuery).list();
		List<EPUserApp> mockUserRolesList2 = new ArrayList<>();
		EPUserApp mockEpUserAppRoles = new EPUserApp();
		mockEpUserAppRoles.setApp(mockApp);
		mockEpUserAppRoles.setRole(mockEPRole);
		mockEpUserAppRoles.setUserId(user.getId());
		mockUserRolesList2.add(mockEpUserAppRoles);
		Mockito.when(session.createQuery(
				"from org.onap.portalapp.portal.domain.EPUserApp where app.id=1 and role.active = 'Y' and userId=2"))
				.thenReturn(epsetAppWithUserRoleGetUserAppsQuery);
		Mockito.doReturn(mockUserRolesList2).when(epsetAppWithUserRoleGetUserAppsQuery).list();
		List<EPRole> mockEPRoles = new ArrayList<>();
		mockEPRoles.add(mockEPRole2);
		mockEPRoles.add(mockEPRole3);
		Mockito.when(session.createQuery("from " + EPRole.class.getName() + " where appId is null and id != 1"))
				.thenReturn(epsetAppWithUserRoleGetRolesQuery);
		Mockito.doReturn(mockEPRoles).when(epsetAppWithUserRoleGetRolesQuery).list();
		Mockito.when(session.createSQLQuery("update fn_role set app_id = null where app_id = 1 "))
				.thenReturn(epsetAppWithUserRoleUpdateEPRoleQuery);
		ExternalRequestFieldsValidator actual = userRolesCommonServiceImpl.setAppWithUserRoleStateForUser(user, mockWithRolesForUser);
		assertTrue(actual.isResult());
	}

	private List<EcompUserAppRoles> getCurrentUserRoles(EPUser user, EPApp mockApp) {
		List<EcompUserAppRoles> mockEPuserAppList = new ArrayList<>();
		EcompUserAppRoles mockEcompUserAppRoles = new EcompUserAppRoles();
		mockEcompUserAppRoles.setAppId(String.valueOf(mockApp.getId()));
		mockEcompUserAppRoles.setRoleId(1l);
		mockEcompUserAppRoles.setRoleName("test1");
		mockEcompUserAppRoles.setUserId(user.getId());
		mockEPuserAppList.add(mockEcompUserAppRoles);
		return mockEPuserAppList;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAppWithUserRoleStateForUserTestForNonCentralizedApp() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		EPUser user = mockUser.mockEPUser();
		user.setId(2l);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		EPApp mockApp = mockApp();
		mockApp.setNameSpace("com.test.com");
		mockApp.setId(2l);
		mockApp.setCentralAuth(false);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		AppWithRolesForUser mockWithRolesForUser = new AppWithRolesForUser();
		List<RoleInAppForUser> mockRoleInAppForUserList = new ArrayList<>();
		RoleInAppForUser mockRoleInAppForUser = new RoleInAppForUser();
		mockRoleInAppForUser.setIsApplied(true);
		mockRoleInAppForUser.setRoleId(1l);
		mockRoleInAppForUser.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(true);
		mockRoleInAppForUser2.setRoleId(1000l);
		mockRoleInAppForUser2.setRoleName("test3");
		mockRoleInAppForUserList.add(mockRoleInAppForUser);
		mockRoleInAppForUserList.add(mockRoleInAppForUser2);
		mockWithRolesForUser.setAppId(mockApp.getId());
		mockWithRolesForUser.setAppName(mockApp.getName());
		mockWithRolesForUser.setOrgUserId(user.getOrgUserId());
		mockWithRolesForUser.setAppRoles(mockRoleInAppForUserList);
		List<EPUserAppRolesRequest> mockEpRequestIdValList = new ArrayList<EPUserAppRolesRequest>();
		Set<EPUserAppRolesRequestDetail> mockEpUserAppRolesRequestDetailList = new TreeSet<>();
		EPUserAppRolesRequestDetail mockEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		EPUserAppRolesRequest mockEPUserAppRolesRequest = new EPUserAppRolesRequest();
		mockEPUserAppRolesRequest.setAppId(mockApp.getId());
		mockEPUserAppRolesRequest.setId(1l);
		mockEPUserAppRolesRequest.setRequestStatus("P");
		mockEPUserAppRolesRequest.setUserId(user.getId());
		mockEPUserAppRolesRequest.setEpRequestIdDetail(mockEpUserAppRolesRequestDetailList);
		mockEpRequestIdValList.add(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetail.setEpRequestIdData(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetailList.add(mockEpUserAppRolesRequestDetail);

		// Update Requests if any requests are pending
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", mockApp.getId());
		params.put("userId", user.getId());
		Mockito.when((List<EPUserAppRolesRequest>) dataAccessService.executeNamedQuery("userAppRolesRequestList",
				params, null)).thenReturn(mockEpRequestIdValList);
		mockEPUserAppRolesRequest.setUpdatedDate(new Date());
		mockEPUserAppRolesRequest.setRequestStatus("O");
		HashMap<String, Long> additionalUpdateParam = new HashMap<String, Long>();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockEPUserAppRolesRequest, additionalUpdateParam);
		List<EPUserAppRolesRequestDetail> mockGetEpUserAppRolesRequestDetailList = new ArrayList<EPUserAppRolesRequestDetail>();
		EPUserAppRolesRequestDetail mockGetEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		mockGetEpUserAppRolesRequestDetailList.add(mockGetEpUserAppRolesRequestDetail);
		final Map<String, Long> epDetailParams = new HashMap<>();
		epDetailParams.put("reqId", mockEPUserAppRolesRequest.getId());

		final Map<String, String> userParams = new HashMap<>();
		userParams.put("orgUserIdValue", user.getOrgUserId());
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("epUserAppId", userParams, null))
				.thenReturn(mockEpUserList);

		PowerMockito
				.when(EPCommonSystemProperties
						.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		PowerMockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		EPRole mockEPRole = new EPRole();
		mockEPRole.setActive(true);
		mockEPRole.setAppId(2l);
		mockEPRole.setAppRoleId(null);
		mockEPRole.setId(10l);
		mockEPRole.setAppRoleId(1l);
		mockEPRole.setName("test1");
		EPRole mockEPRole2 = new EPRole();
		mockEPRole2.setActive(true);
		mockEPRole2.setAppId(2l);
		mockEPRole2.setAppRoleId(null);
		mockEPRole2.setId(160l);
		mockEPRole2.setName("test2");
		mockEPRole2.setAppRoleId(16l);
		EPRole mockEPRole3 = new EPRole();
		mockEPRole3.setActive(true);
		mockEPRole3.setAppId(2l);
		mockEPRole3.setAppRoleId(null);
		mockEPRole3.setId(1100l);
		mockEPRole3.setAppRoleId(100l);
		mockEPRole3.setName("test3");
		Mockito.when(applicationsRestClientService.get(EPUser.class, mockApp.getId(),
				String.format("/user/%s", user.getOrgUserId()), true)).thenReturn(user);
		// Updates in EP DB
		Query epsetAppWithUserRoleNonCentralizedGetUserQuery = Mockito.mock(Query.class);
		Query epsetAppWithUserRoleNonCentralizedGetUserAppsQuery = Mockito.mock(Query.class);
		Query epsetAppWithUserRoleNonCentralizedGetRolesQuery = Mockito.mock(Query.class);
		Mockito.when(session.get(EPApp.class, mockApp.getId())).thenReturn(mockApp);
		Mockito.when(session
				.createQuery("from " + EPUser.class.getName() + " where orgUserId='" + user.getOrgUserId() + "'"))
				.thenReturn(epsetAppWithUserRoleNonCentralizedGetUserQuery);
		Mockito.doReturn(mockEpUserList).when(epsetAppWithUserRoleNonCentralizedGetUserQuery).list();
		List<EPUserApp> mockUserRolesList2 = new ArrayList<>();
		EPUserApp mockEpUserAppRoles = new EPUserApp();
		mockEpUserAppRoles.setApp(mockApp);
		mockEpUserAppRoles.setRole(mockEPRole);
		mockEpUserAppRoles.setUserId(user.getId());
		mockUserRolesList2.add(mockEpUserAppRoles);
		Mockito.when(session.createQuery(
				"from org.onap.portalapp.portal.domain.EPUserApp where app.id=2 and role.active = 'Y' and userId=2"))
				.thenReturn(epsetAppWithUserRoleNonCentralizedGetUserAppsQuery);
		Mockito.doReturn(mockUserRolesList2).when(epsetAppWithUserRoleNonCentralizedGetUserAppsQuery).list();
		List<EPRole> mockEPRoles = new ArrayList<>();
		mockEPRoles.add(mockEPRole2);
		mockEPRoles.add(mockEPRole3);
		Mockito.when(session.createQuery("from " + EPRole.class.getName() + " where appId=2"))
				.thenReturn(epsetAppWithUserRoleNonCentralizedGetRolesQuery);
		Mockito.doReturn(mockEPRoles).when(epsetAppWithUserRoleNonCentralizedGetRolesQuery).list();
		ExternalRequestFieldsValidator expected = userRolesCommonServiceImpl.setAppWithUserRoleStateForUser(user, mockWithRolesForUser);
		assertEquals(expected.isResult(), false);
	}

	/*@SuppressWarnings("unchecked")
	@Test
	public void setExternalRequestUserAppRoleMerdianCentralizedAppTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		EPUser user = mockUser.mockEPUser();
		user.setId(2l);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		EPApp mockApp = mockApp();
		mockApp.setNameSpace("com.test.com");
		mockApp.setId(1l);
		mockApp.setCentralAuth(true);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		ExternalSystemUser externalSystemUser = new ExternalSystemUser();
		List<ExternalSystemRoleApproval> mockExternalSystemRoleApprovalList = new ArrayList<>();
		ExternalSystemRoleApproval mockExternalSystemRoleApproval = new ExternalSystemRoleApproval();
		mockExternalSystemRoleApproval.setRoleName("test1");
		ExternalSystemRoleApproval mockExternalSystemRoleApproval2 = new ExternalSystemRoleApproval();
		mockExternalSystemRoleApproval2.setRoleName("test2");
		mockExternalSystemRoleApprovalList.add(mockExternalSystemRoleApproval);
		mockExternalSystemRoleApprovalList.add(mockExternalSystemRoleApproval2);
		externalSystemUser.setApplicationName(mockApp.getMlAppName());
		externalSystemUser.setLoginId(user.getOrgUserId());
		externalSystemUser.setRoles(mockExternalSystemRoleApprovalList);
		EPRole mockEPRole = new EPRole();
		mockEPRole.setActive(true);
		mockEPRole.setAppId(null);
		mockEPRole.setAppRoleId(null);
		mockEPRole.setId(1l);
		mockEPRole.setName("test1");
		EPRole mockEPRole2 = new EPRole();
		mockEPRole2.setActive(true);
		mockEPRole2.setAppId(null);
		mockEPRole2.setAppRoleId(null);
		mockEPRole2.setId(16l);
		mockEPRole2.setName("test2");
		EPRole mockEPRole3 = new EPRole();
		mockEPRole3.setActive(true);
		mockEPRole3.setAppId(null);
		mockEPRole3.setAppRoleId(null);
		mockEPRole3.setId(1000l);
		mockEPRole3.setName("test3");

		Mockito.when(epAppCommonServiceImpl.getAppDetail(mockApp.getMlAppName())).thenReturn(mockApp);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("orgUserIdValue", user.getOrgUserId());
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("epUserAppId", userParams, null))
				.thenReturn(mockEpUserList);

		List<EPUserAppRolesRequest> mockMerdianEpRequestIdValList = new ArrayList<EPUserAppRolesRequest>();
		Set<EPUserAppRolesRequestDetail> mockEpUserAppRolesRequestDetailList = new TreeSet<>();
		EPUserAppRolesRequestDetail mockEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		EPUserAppRolesRequest mockEPUserAppRolesRequest = new EPUserAppRolesRequest();
		mockEPUserAppRolesRequest.setAppId(mockApp.getId());
		mockEPUserAppRolesRequest.setId(1l);
		mockEPUserAppRolesRequest.setRequestStatus("P");
		mockEPUserAppRolesRequest.setUserId(user.getId());
		mockEPUserAppRolesRequest.setEpRequestIdDetail(mockEpUserAppRolesRequestDetailList);
		mockMerdianEpRequestIdValList.add(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetail.setEpRequestIdData(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetailList.add(mockEpUserAppRolesRequestDetail);

		final Map<String, Long> params = new HashMap<>();
		params.put("appId", mockApp.getId());
		params.put("userId", user.getId());
		Mockito.when((List<EPUserAppRolesRequest>) dataAccessService.executeNamedQuery("userAppRolesRequestList",
				params, null)).thenReturn(mockMerdianEpRequestIdValList);
		RoleInAppForUser mockRoleInAppForUser1 = new RoleInAppForUser();
		mockRoleInAppForUser1.setIsApplied(true);
		mockRoleInAppForUser1.setRoleId(1l);
		mockRoleInAppForUser1.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(true);
		mockRoleInAppForUser2.setRoleId(16l);
		mockRoleInAppForUser2.setRoleName("test2");
		Mockito.when(epRoleServiceImpl.getAppRole(mockExternalSystemRoleApproval.getRoleName(), mockApp.getId()))
				.thenReturn(mockEPRole);
		Mockito.when(epRoleServiceImpl.getAppRole(mockExternalSystemRoleApproval2.getRoleName(), mockApp.getId()))
				.thenReturn(mockEPRole2);
		List<EcompUserAppRoles> mockEPuserAppList = getCurrentUserRoles(user, mockApp);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("userId", user.getId());
		params2.put("appId", mockApp.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getUserAppExistingRoles", params2, null))
				.thenReturn(mockEPuserAppList);
		// Updates in External Auth System
		List<EPUserAppRolesRequestDetail> mockGetEpUserAppRolesRequestDetailList = new ArrayList<EPUserAppRolesRequestDetail>();
		EPUserAppRolesRequestDetail mockGetEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		mockGetEpUserAppRolesRequestDetailList.add(mockGetEpUserAppRolesRequestDetail);
		final Map<String, String> userParams2 = new HashMap<>();
		userParams2.put("orgUserIdValue", user.getOrgUserId());
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("epUserAppId", userParams2, null))
				.thenReturn(mockEpUserList);
		PowerMockito
				.when(EPCommonSystemProperties
						.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		PowerMockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		JSONObject mockJsonObject1 = new JSONObject();
		JSONObject mockJsonObject2 = new JSONObject();
		JSONObject mockJsonObject3 = new JSONObject();
		mockJsonObject1.put("name", "com.test.com.test1");
		mockJsonObject2.put("name", "com.test.com.test2");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObject1);
		mockJson.add(mockJsonObject2);
		mockJsonObject3.put("role", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObject3.toString(), HttpStatus.OK);
		Mockito.when(externalAccessRolesServiceImpl.getUserRolesFromExtAuthSystem(Matchers.anyString(), Matchers.any(HttpEntity.class))).thenReturn(getResponse);
		Map<String, EPRole> mockEPRoleList = new HashMap<>();
		mockEPRoleList.put("test1", mockEPRole);
		mockEPRoleList.put("test2", mockEPRole2);
		mockEPRoleList.put("test3", mockEPRole3);
		Mockito.when(externalAccessRolesServiceImpl.getAppRoleNamesWithUnderscoreMap(mockApp)).thenReturn(mockEPRoleList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);

		ResponseEntity<String> deleteResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(deleteResponse);
		// Updated in EP DB
		Query epsetExternalRequestUserAppRoleGetUserQuery = Mockito.mock(Query.class);
		Query epsetExternalRequestUserAppRoleGetUserAppsQuery = Mockito.mock(Query.class);
		Query epsetExternalRequestUserAppRoleGetRolesQuery = Mockito.mock(Query.class);
		SQLQuery epsetAppWithUserRoleUpdateEPRoleQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.get(EPApp.class, mockApp.getId())).thenReturn(mockApp);
		Mockito.when(session
				.createQuery("from " + EPUser.class.getName() + " where orgUserId='" + user.getOrgUserId() + "'"))
				.thenReturn(epsetExternalRequestUserAppRoleGetUserQuery);
		Mockito.doReturn(mockEpUserList).when(epsetExternalRequestUserAppRoleGetUserQuery).list();
		List<EPUserApp> mockUserRolesList2 = new ArrayList<>();
		EPUserApp mockEpUserAppRoles = new EPUserApp();
		mockEpUserAppRoles.setApp(mockApp);
		mockEpUserAppRoles.setRole(mockEPRole);
		mockEpUserAppRoles.setUserId(user.getId());
		mockUserRolesList2.add(mockEpUserAppRoles);
		Mockito.when(session.createQuery(
				"from org.onap.portalapp.portal.domain.EPUserApp where app.id=1 and role.active = 'Y' and userId=2"))
				.thenReturn(epsetExternalRequestUserAppRoleGetUserAppsQuery);
		Mockito.doReturn(mockUserRolesList2).when(epsetExternalRequestUserAppRoleGetUserAppsQuery).list();
		List<EPRole> mockEPRoles = new ArrayList<>();
		mockEPRoles.add(mockEPRole2);
		mockEPRoles.add(mockEPRole3);
		Mockito.when(session.createQuery("from org.onap.portalapp.portal.domain.EPRole where appId is null "))
				.thenReturn(epsetExternalRequestUserAppRoleGetRolesQuery);
		Mockito.doReturn(mockEPRoles).when(epsetExternalRequestUserAppRoleGetRolesQuery).list();
		Mockito.when(session.createSQLQuery("update fn_role set app_id = null where app_id = 1 "))
				.thenReturn(epsetAppWithUserRoleUpdateEPRoleQuery);

		ExternalRequestFieldsValidator mockExternalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Updated Successfully");
		ExternalRequestFieldsValidator externalRequestFieldsValidator = userRolesCommonServiceImpl
				.setExternalRequestUserAppRole(externalSystemUser, "POST");
		assertTrue(mockExternalRequestFieldsValidator.equals(externalRequestFieldsValidator));
	}
*/
	@SuppressWarnings("unchecked")
	@Test
	public void setExternalRequestUserAppRoleMerdianNonCentralizedAppTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		EPUser user = mockUser.mockEPUser();
		user.setId(2l);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		ExternalSystemUser externalSystemUser = new ExternalSystemUser();
		List<ExternalSystemRoleApproval> mockExternalSystemRoleApprovalList = new ArrayList<>();
		ExternalSystemRoleApproval mockExternalSystemRoleApproval = new ExternalSystemRoleApproval();
		mockExternalSystemRoleApproval.setRoleName("test1");
		ExternalSystemRoleApproval mockExternalSystemRoleApproval2 = new ExternalSystemRoleApproval();
		mockExternalSystemRoleApproval2.setRoleName("test2");
		mockExternalSystemRoleApprovalList.add(mockExternalSystemRoleApproval);
		mockExternalSystemRoleApprovalList.add(mockExternalSystemRoleApproval2);
		externalSystemUser.setApplicationName(mockApp.getMlAppName());
		externalSystemUser.setLoginId(user.getOrgUserId());
		externalSystemUser.setRoles(mockExternalSystemRoleApprovalList);

		EPRole mockEPRole = new EPRole();
		mockEPRole.setActive(true);
		mockEPRole.setAppId(2l);
		mockEPRole.setAppRoleId(1l);
		mockEPRole.setId(1000l);
		mockEPRole.setName("test1");
		EPRole mockEPRole2 = new EPRole();
		mockEPRole2.setActive(true);
		mockEPRole2.setAppId(2l);
		mockEPRole2.setAppRoleId(16l);
		mockEPRole2.setId(160l);
		mockEPRole2.setName("test2");
		EPRole mockEPRole3 = new EPRole();
		mockEPRole3.setActive(true);
		mockEPRole3.setAppId(2l);
		mockEPRole3.setAppRoleId(10l);
		mockEPRole3.setId(100l);
		mockEPRole3.setName("test3");

		Mockito.when(epAppCommonServiceImpl.getAppDetail(mockApp.getMlAppName())).thenReturn(mockApp);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("orgUserIdValue", user.getOrgUserId());
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("epUserAppId", userParams, null))
				.thenReturn(mockEpUserList);

		List<EPUserAppRolesRequest> mockMerdianEpRequestIdValList = new ArrayList<EPUserAppRolesRequest>();
		Set<EPUserAppRolesRequestDetail> mockEpUserAppRolesRequestDetailList = new TreeSet<>();
		EPUserAppRolesRequestDetail mockEpUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		mockEpUserAppRolesRequestDetail.setId(2l);
		mockEpUserAppRolesRequestDetail.setReqType("P");
		mockEpUserAppRolesRequestDetail.setReqRoleId(16l);
		EPUserAppRolesRequest mockEPUserAppRolesRequest = new EPUserAppRolesRequest();
		mockEPUserAppRolesRequest.setAppId(mockApp.getId());
		mockEPUserAppRolesRequest.setId(1l);
		mockEPUserAppRolesRequest.setRequestStatus("P");
		mockEPUserAppRolesRequest.setUserId(user.getId());
		mockEPUserAppRolesRequest.setEpRequestIdDetail(mockEpUserAppRolesRequestDetailList);
		mockMerdianEpRequestIdValList.add(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetail.setEpRequestIdData(mockEPUserAppRolesRequest);
		mockEpUserAppRolesRequestDetailList.add(mockEpUserAppRolesRequestDetail);

		final Map<String, Long> params = new HashMap<>();
		params.put("appId", mockApp.getId());
		params.put("userId", user.getId());
		Mockito.when((List<EPUserAppRolesRequest>) dataAccessService.executeNamedQuery("userAppRolesRequestList",
				params, null)).thenReturn(mockMerdianEpRequestIdValList);
		RoleInAppForUser mockRoleInAppForUser1 = new RoleInAppForUser();
		mockRoleInAppForUser1.setIsApplied(true);
		mockRoleInAppForUser1.setRoleId(1l);
		mockRoleInAppForUser1.setRoleName("test1");
		RoleInAppForUser mockRoleInAppForUser2 = new RoleInAppForUser();
		mockRoleInAppForUser2.setIsApplied(true);
		mockRoleInAppForUser2.setRoleId(16l);
		mockRoleInAppForUser2.setRoleName("test2");
		Mockito.when(epRoleServiceImpl.getAppRole(mockExternalSystemRoleApproval.getRoleName(), mockApp.getId()))
				.thenReturn(mockEPRole);
		Mockito.when(epRoleServiceImpl.getAppRole(mockExternalSystemRoleApproval2.getRoleName(), mockApp.getId()))
				.thenReturn(mockEPRole2);
		List<EcompUserAppRoles> mockEPuserAppList = new ArrayList<>();
		EcompUserAppRoles mockEcompUserAppRoles = new EcompUserAppRoles();
		mockEcompUserAppRoles.setAppId(String.valueOf(mockApp.getId()));
		mockEcompUserAppRoles.setRoleId(100l);
		mockEcompUserAppRoles.setRoleName("test1");
		mockEcompUserAppRoles.setUserId(user.getId());
		mockEPuserAppList.add(mockEcompUserAppRoles);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("userId", user.getId());
		params2.put("appId", mockApp.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getUserAppExistingRoles", params2, null))
				.thenReturn(mockEPuserAppList);
		List<EcompRole> mockEcompRoleList = new ArrayList<>();
		EcompRole mockEcompRole = new EcompRole();
		mockEcompRole.setId(1l);
		mockEcompRole.setName("test1");
		EcompRole mockEcompRole2 = new EcompRole();
		mockEcompRole2.setId(16l);
		mockEcompRole2.setName("test2");
		mockEcompRoleList.add(mockEcompRole);
		mockEcompRoleList.add(mockEcompRole2);
		EcompRole[] mockEcompRoleArray = mockEcompRoleList.toArray(new EcompRole[mockEcompRoleList.size()]);
		Mockito.when(applicationsRestClientService.get(EcompRole[].class, mockApp.getId(), "/roles"))
				.thenReturn(mockEcompRoleArray);

		// SyncAppRoles
		List<EPUserApp> mockUserRolesList = new ArrayList<>();
		EPUserApp mockEpUserApp = new EPUserApp();
		mockEpUserApp.setApp(mockApp);
		mockEpUserApp.setRole(mockEPRole2);
		mockEpUserApp.setUserId(user.getId());
		mockUserRolesList.add(mockEpUserApp);
		List<FunctionalMenuRole> mockFunctionalMenuRolesList = new ArrayList<>();
		FunctionalMenuRole mockFunctionalMenuRole = new FunctionalMenuRole();
		mockFunctionalMenuRole.setAppId((int) (long) mockApp.getId());
		mockFunctionalMenuRole.setRoleId((int) (long) 15l);
		mockFunctionalMenuRole.setMenuId(10l);
		mockFunctionalMenuRole.setId(10);
		mockFunctionalMenuRolesList.add(mockFunctionalMenuRole);
		List<FunctionalMenuItem> mockFunctionalMenuItemList = new ArrayList<>();
		FunctionalMenuItem mockFunctionalMenuItem = new FunctionalMenuItem();
		List<Integer> mockRolesList = new ArrayList<>();
		Integer role1 = 1;
		mockRolesList.add(role1);
		mockFunctionalMenuItem.setRestrictedApp(false);
		mockFunctionalMenuItem.setRoles(mockRolesList);
		mockFunctionalMenuItem.setUrl("http://test.com");
		mockFunctionalMenuItemList.add(mockFunctionalMenuItem);
		Query epRoleQuery = Mockito.mock(Query.class);
		Query epUserAppsQuery = Mockito.mock(Query.class);
		Query epFunctionalMenuQuery = Mockito.mock(Query.class);
		Query epFunctionalMenuQuery2 = Mockito.mock(Query.class);
		Query epFunctionalMenuItemQuery = Mockito.mock(Query.class);
		Mockito.when(applicationsRestClientService.get(EcompRole[].class, mockApp.getId(), "/roles"))
				.thenReturn(mockEcompRoleArray);
		// syncAppRolesTest
		Mockito.when(session.createQuery("from " + EPRole.class.getName() + " where appId=" + mockApp.getId()))
				.thenReturn(epRoleQuery);
		Mockito.doReturn(mockEcompRoleList).when(epRoleQuery).list();
		Mockito.when(session.createQuery(
				"from " + EPUserApp.class.getName() + " where app.id=" + mockApp.getId() + " and role_id=" + 100l))
				.thenReturn(epUserAppsQuery);
		Mockito.doReturn(mockUserRolesList).when(epUserAppsQuery).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuRole.class.getName() + " where roleId=" + 100l))
				.thenReturn(epFunctionalMenuQuery);
		Mockito.doReturn(mockFunctionalMenuRolesList).when(epFunctionalMenuQuery).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuRole.class.getName() + " where menuId=" + 10l))
				.thenReturn(epFunctionalMenuQuery2);
		Mockito.doReturn(mockFunctionalMenuRolesList).when(epFunctionalMenuQuery2).list();

		Mockito.when(session.createQuery("from " + FunctionalMenuItem.class.getName() + " where menuId=" + 10l))
				.thenReturn(epFunctionalMenuItemQuery);
		Mockito.doReturn(mockFunctionalMenuItemList).when(epFunctionalMenuItemQuery).list();

		Mockito.when(applicationsRestClientService.get(EPUser.class, mockApp.getId(),
				String.format("/user/%s", user.getOrgUserId()), true)).thenReturn(user);
		// Updated in EP DB
		Query epsetExternalRequestUserAppRoleGetUserQuery = Mockito.mock(Query.class);
		Query epsetExternalRequestUserAppRoleGetUserAppsQuery = Mockito.mock(Query.class);
		Query epsetExternalRequestUserAppRoleGetRolesQuery = Mockito.mock(Query.class);
		Mockito.when(session.get(EPApp.class, mockApp.getId())).thenReturn(mockApp);
		Mockito.when(session
				.createQuery("from " + EPUser.class.getName() + " where orgUserId='" + user.getOrgUserId() + "'"))
				.thenReturn(epsetExternalRequestUserAppRoleGetUserQuery);
		Mockito.doReturn(mockEpUserList).when(epsetExternalRequestUserAppRoleGetUserQuery).list();
		List<EPUserApp> mockUserRolesList2 = new ArrayList<>();
		EPUserApp mockEpUserAppRoles = new EPUserApp();
		mockEpUserAppRoles.setApp(mockApp);
		mockEpUserAppRoles.setRole(mockEPRole);
		mockEpUserAppRoles.setUserId(user.getId());
		mockUserRolesList2.add(mockEpUserAppRoles);
		Mockito.when(session.createQuery(
				"from org.onap.portalapp.portal.domain.EPUserApp where app.id=2 and role.active = 'Y' and userId=2"))
				.thenReturn(epsetExternalRequestUserAppRoleGetUserAppsQuery);
		Mockito.doReturn(mockUserRolesList2).when(epsetExternalRequestUserAppRoleGetUserAppsQuery).list();
		List<EPRole> mockEPRoles = new ArrayList<>();
		mockEPRoles.add(mockEPRole2);
		mockEPRoles.add(mockEPRole3);
		Mockito.when(session.createQuery("from org.onap.portalapp.portal.domain.EPRole where appId=2"))
				.thenReturn(epsetExternalRequestUserAppRoleGetRolesQuery);
		Mockito.doReturn(mockEPRoles).when(epsetExternalRequestUserAppRoleGetRolesQuery).list();

		ExternalRequestFieldsValidator mockExternalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Updated Successfully");
		ExternalRequestFieldsValidator externalRequestFieldsValidator = userRolesCommonServiceImpl
				.setExternalRequestUserAppRole(externalSystemUser, "POST");
		assertFalse(mockExternalRequestFieldsValidator.equals(externalRequestFieldsValidator));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getUsersFromAppEndpointCentralizedAppTest() throws HTTPException {
		EPApp mockApp = mockApp();
		mockApp.setId(1l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(true);
		EPUser user = mockUser.mockEPUser();
		EPUser user2 = mockUser.mockEPUser();
		user2.setActive(true);
		user2.setOrgUserId("guestT2");
		user2.setId(2l);
		user2.setFirstName("Guest2");
		user2.setLastName("Test2");
		user.setId(1l);
		user.setFirstName("Guest");
		user.setLastName("Test");
		EPRole mockEPRole1 = new EPRole();
		mockEPRole1.setId(1l);
		mockEPRole1.setName("test1");
		mockEPRole1.setActive(true);
		EPRole mockEPRole2 = new EPRole();
		mockEPRole2.setId(16l);
		mockEPRole2.setName("test2");
		mockEPRole2.setActive(true);
		SortedSet<EPUserApp> mockUserApps1 = new TreeSet<EPUserApp>();
		EPUserApp mockEPUserApp1 = new EPUserApp();
		mockEPUserApp1.setApp(mockApp);
		mockEPUserApp1.setRole(mockEPRole1);
		mockEPUserApp1.setUserId(1l);
		mockUserApps1.add(mockEPUserApp1);
		user.setEPUserApps(mockUserApps1);
		SortedSet<EPUserApp> mockUserApps2 = new TreeSet<EPUserApp>();
		EPUserApp mockEPUserApp2 = new EPUserApp();
		mockEPUserApp2.setApp(mockApp);
		mockEPUserApp2.setRole(mockEPRole2);
		mockEPUserApp2.setUserId(2l);
		mockUserApps2.add(mockEPUserApp2);
		user2.setEPUserApps(mockUserApps2);
		List<EPUser> mockEpUserList = new ArrayList<>();
		mockEpUserList.add(user);
		mockEpUserList.add(user2);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<UserApplicationRoles> mockUserApplicationRolesList = new ArrayList<>();
		UserApplicationRoles mockUserApplicationRoles = new UserApplicationRoles();
		List<RemoteRole> mockRemoteRoleList = new ArrayList<>();
		RemoteRole mockRemoteRole = new RemoteRole();
		mockRemoteRole.setId(1l);
		mockRemoteRole.setName("test1");
		mockRemoteRoleList.add(mockRemoteRole);
		mockUserApplicationRoles.setAppId(mockApp.getId());
		mockUserApplicationRoles.setFirstName("Guest");
		mockUserApplicationRoles.setLastName("Test");
		mockUserApplicationRoles.setOrgUserId("guestT");
		mockUserApplicationRoles.setRoles(mockRemoteRoleList);
		UserApplicationRoles mockUserApplicationRoles2 = new UserApplicationRoles();
		List<RemoteRole> mockRemoteRoleList2 = new ArrayList<>();
		RemoteRole mockRemoteRole2 = new RemoteRole();
		mockRemoteRole2.setId(16l);
		mockRemoteRole2.setName("test2");
		mockRemoteRoleList2.add(mockRemoteRole2);
		mockUserApplicationRoles2.setAppId(mockApp.getId());
		mockUserApplicationRoles2.setFirstName("Guest2");
		mockUserApplicationRoles2.setLastName("Test2");
		mockUserApplicationRoles2.setOrgUserId("guestT2");
		mockUserApplicationRoles2.setRoles(mockRemoteRoleList2);
		mockUserApplicationRolesList.add(mockUserApplicationRoles);
		mockUserApplicationRolesList.add(mockUserApplicationRoles2);
		Mockito.when((List<EPUser>) dataAccessService.executeNamedQuery("getActiveUsers", null, null))
				.thenReturn(mockEpUserList);
		assertEquals(userRolesCommonServiceImpl.getUsersFromAppEndpoint(1l).size(),
				mockUserApplicationRolesList.size());
	}

	@Test
	public void getUsersFromAppEndpointNonCentralizedAppTest() throws HTTPException, JsonProcessingException {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		Mockito.when(epAppCommonServiceImpl.getApp(mockApp.getId())).thenReturn(mockApp);
		List<UserApplicationRoles> mockUserApplicationRolesNonCentralizedList = new ArrayList<>();
		UserApplicationRoles mockUserApplicationRoles = new UserApplicationRoles();
		List<RemoteRole> mockRemoteRoleList = new ArrayList<>();
		RemoteRole mockRemoteRole = new RemoteRole();
		mockRemoteRole.setId(1l);
		mockRemoteRole.setName("test1");
		mockRemoteRoleList.add(mockRemoteRole);
		mockUserApplicationRoles.setAppId(mockApp.getId());
		mockUserApplicationRoles.setFirstName("Guest");
		mockUserApplicationRoles.setLastName("Test");
		mockUserApplicationRoles.setOrgUserId("guestT");
		mockUserApplicationRoles.setRoles(mockRemoteRoleList);
		UserApplicationRoles mockUserApplicationRoles2 = new UserApplicationRoles();
		List<RemoteRole> mockRemoteRoleList2 = new ArrayList<>();
		RemoteRole mockRemoteRole2 = new RemoteRole();
		mockRemoteRole2.setId(16l);
		mockRemoteRole2.setName("test2");
		mockRemoteRoleList2.add(mockRemoteRole2);
		mockUserApplicationRoles2.setAppId(mockApp.getId());
		mockUserApplicationRoles2.setFirstName("Guest2");
		mockUserApplicationRoles2.setLastName("Test2");
		mockUserApplicationRoles2.setOrgUserId("guestT2");
		mockUserApplicationRoles2.setRoles(mockRemoteRoleList2);
		mockUserApplicationRolesNonCentralizedList.add(mockUserApplicationRoles);
		mockUserApplicationRolesNonCentralizedList.add(mockUserApplicationRoles2);
		RemoteUserWithRoles mockRemoteUserWithRoles1 = new RemoteUserWithRoles();
		mockRemoteUserWithRoles1.setFirstName("Guest1");
		mockRemoteUserWithRoles1.setLastName("Test1");
		mockRemoteUserWithRoles1.setOrgUserId("guestT");
		mockRemoteUserWithRoles1.setRoles(mockRemoteRoleList);
		RemoteUserWithRoles mockRemoteUserWithRoles2 = new RemoteUserWithRoles();
		mockRemoteUserWithRoles2.setFirstName("Guest2");
		mockRemoteUserWithRoles2.setLastName("Test2");
		mockRemoteUserWithRoles2.setOrgUserId("guestT");
		mockRemoteUserWithRoles2.setRoles(mockRemoteRoleList2);
		List<RemoteUserWithRoles> mockRemoteUserWithRolesList = new ArrayList<>();
		mockRemoteUserWithRolesList.add(mockRemoteUserWithRoles1);
		mockRemoteUserWithRolesList.add(mockRemoteUserWithRoles2);
		ObjectMapper mapper = new ObjectMapper();
		String mockGetRemoteUsersWithRoles = mapper.writeValueAsString(mockRemoteUserWithRolesList);
		Mockito.when(applicationsRestClientService.getIncomingJsonString(mockApp.getId(), "/users"))
				.thenReturn(mockGetRemoteUsersWithRoles);
		List<UserApplicationRoles> userApplicationRolesNonCentralizedList = userRolesCommonServiceImpl
				.getUsersFromAppEndpoint(2l);
		assertEquals(mockUserApplicationRolesNonCentralizedList.size(), userApplicationRolesNonCentralizedList.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void putUserAppRolesRequestTest() {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		EPUser user = mockUser.mockEPUser();
		AppWithRolesForUser appWithRolesForUser = new AppWithRolesForUser();
		List<RoleInAppForUser> mockRoleInAppForUserList = new ArrayList<>();
		RoleInAppForUser roleInAppForUser = new RoleInAppForUser();
		roleInAppForUser.setIsApplied(true);
		roleInAppForUser.setRoleId(1l);
		roleInAppForUser.setRoleName("test1");
		RoleInAppForUser roleInAppForUser2 = new RoleInAppForUser();
		roleInAppForUser2.setIsApplied(true);
		roleInAppForUser2.setRoleId(1000l);
		roleInAppForUser2.setRoleName("test3");
		mockRoleInAppForUserList.add(roleInAppForUser);
		mockRoleInAppForUserList.add(roleInAppForUser2);
		appWithRolesForUser.setAppId(mockApp.getId());
		appWithRolesForUser.setAppName(mockApp.getName());
		appWithRolesForUser.setOrgUserId(user.getOrgUserId());
		appWithRolesForUser.setAppRoles(mockRoleInAppForUserList);
		List<EPUserAppRoles> epUserAppRolesList = new ArrayList<>();
		EPUserAppRoles appRole1 = new EPUserAppRoles();
		appRole1.setAppId(mockApp.getId());
		appRole1.setId(10l);
		appRole1.setRoleId(roleInAppForUser.roleId);
		epUserAppRolesList.add(appRole1);
		List<EPUserAppRoles> epUserAppRolesList2 = new ArrayList<>();
		EPUserAppRoles appRole2 = new EPUserAppRoles();
		appRole2.setAppId(mockApp.getId());
		appRole2.setId(11l);
		appRole2.setRoleId(roleInAppForUser2.roleId);
		epUserAppRolesList2.add(appRole2);
		EPUserAppRolesRequest mockEpAppRolesRequestData = new EPUserAppRolesRequest();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockEpAppRolesRequestData, null);
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", appWithRolesForUser.appId);
		params.put("appRoleId", roleInAppForUser.roleId);
		Mockito.when((List<EPUserAppRoles>) dataAccessService.executeNamedQuery("appRoles", params, null))
				.thenReturn(epUserAppRolesList);
		params.put("appRoleId", roleInAppForUser2.roleId);
		Mockito.when((List<EPUserAppRoles>) dataAccessService.executeNamedQuery("appRoles", params, null))
				.thenReturn(epUserAppRolesList2);
		EPUserAppRolesRequestDetail mockEPAppRoleDetail = new EPUserAppRolesRequestDetail();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockEPAppRoleDetail, null);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		FieldsValidator actual = userRolesCommonServiceImpl.putUserAppRolesRequest(appWithRolesForUser, user);
		assertEquals(expected, actual);
	}

	@Test
	public void importRolesFromRemoteApplicationTest() throws HTTPException {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		List<EPRole> expected = new ArrayList<>();
		EPRole epRole = new EPRole();
		epRole.setAppId(mockApp.getId());
		epRole.setActive(true);
		epRole.setId(10l);
		epRole.setName("test1");
		EPRole epRole2 = new EPRole();
		epRole2.setAppId(mockApp.getId());
		epRole2.setActive(true);
		epRole2.setId(11l);
		epRole2.setName("test2");
		expected.add(epRole);
		expected.add(epRole2);
		EPRole[] epRoleArray = expected.toArray(new EPRole[expected.size()]);
		Mockito.when(applicationsRestClientService.get(EPRole[].class, mockApp.getId(), "/rolesFull"))
				.thenReturn(epRoleArray);
		Mockito.when(epRoleServiceImpl.getRole(mockApp.getId(), epRole.getId())).thenReturn(epRole);
		Mockito.when(epRoleServiceImpl.getRole(mockApp.getId(), epRole2.getId())).thenReturn(epRole2);
		List<EPRole> actual = userRolesCommonServiceImpl.importRolesFromRemoteApplication(mockApp.getId());
		assertEquals(expected.size(), actual.size());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getCachedAppRolesForUserTest() {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		EPUser user = mockUser.mockEPUser();
		List<EPUserApp> expected = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		EPRole epRole = new EPRole();
		epRole.setAppId(mockApp.getId());
		epRole.setActive(true);
		epRole.setId(10l);
		epRole.setName("test1");
		epUserApp.setApp(mockApp);
		epUserApp.setRole(epRole);
		epUserApp.setUserId(user.getId());
		expected.add(epUserApp);
		String filter = " where user_id = " + Long.toString(user.getId()) + " and app_id = "
				+ Long.toString(mockApp.getId());
		Mockito.when(dataAccessService.getList(EPUserApp.class, filter, null, null)).thenReturn(expected);
		List<EPUserApp> actual = userRolesCommonServiceImpl.getCachedAppRolesForUser(mockApp.getId(), user.getId());
		assertEquals(expected.size(), actual.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getUserAppCatalogRolesTest() {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		EPUser user = mockUser.mockEPUser();
		List<EPUserAppCatalogRoles> expected = new ArrayList<>();
		EPUserAppCatalogRoles epUserAppCatalogRoles = new EPUserAppCatalogRoles();
		epUserAppCatalogRoles.setAppId(mockApp.getId());
		epUserAppCatalogRoles.setId(2l);
		epUserAppCatalogRoles.setRequestedRoleId(10l);
		epUserAppCatalogRoles.setRequestStatus("S");
		epUserAppCatalogRoles.setRolename("test1");
		expected.add(epUserAppCatalogRoles);
		Map<String, String> params = new HashMap<>();
		params.put("userid", user.getId().toString());
		params.put("appName", mockApp.getName());
		Mockito.when(
				(List<EPUserAppCatalogRoles>) dataAccessService.executeNamedQuery("userAppCatalogRoles", params, null))
				.thenReturn(expected);
		List<EPUserAppCatalogRoles> actual = userRolesCommonServiceImpl.getUserAppCatalogRoles(user, mockApp.getName());
		assertEquals(expected.size(), actual.size());
	}

	@Test
	public void getExternalRequestAccessTest() {
		ExternalSystemAccess expected = new ExternalSystemAccess("external_access_enable", false);
		ExternalSystemAccess actual = userRolesCommonServiceImpl.getExternalRequestAccess();
		assertEquals(expected, actual);
	}

	@Test
	public void getEPUserAppListTest() {
		EPApp mockApp = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		EPUser user = mockUser.mockEPUser();
		List<EPUserApp> expected = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		EPRole epRole = new EPRole();
		epRole.setAppId(mockApp.getId());
		epRole.setActive(true);
		epRole.setId(10l);
		epRole.setName("test1");
		epUserApp.setApp(mockApp);
		epUserApp.setRole(epRole);
		epUserApp.setUserId(user.getId());
		expected.add(epUserApp);
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", mockApp.getId());
		params.put("userId", user.getId());
		params.put("roleId", epRole.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getUserRoleOnUserIdAndRoleIdAndAppId", params, null))
				.thenReturn(expected);
		List<EPUserApp> actual = userRolesCommonServiceImpl.getEPUserAppList(mockApp.getId(), user.getId(),
				epRole.getId());
		assertEquals(expected.size(), actual.size());
	}

	@Test
	public void updateRemoteUserProfileTest() {
		EPApp mockApp = mockApp();
		EPApp mockApp2 = mockApp();
		mockApp.setId(2l);
		mockApp.setEnabled(true);
		mockApp.setCentralAuth(false);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> mockEpAppList = new ArrayList<>();
		mockEpAppList.add(mockApp);
		mockEpAppList.add(mockApp2);
		Mockito.when(searchServiceImpl.searchUserByUserId(user.getOrgUserId())).thenReturn(user);
		Mockito.when(epAppCommonServiceImpl.getUserRemoteApps(user.getId().toString())).thenReturn(mockEpAppList);
		String expected = "success";
		String actual = userRolesCommonServiceImpl.updateRemoteUserProfile(user.getOrgUserId(), mockApp.getId());
		assertEquals(expected, actual);
	}
}
