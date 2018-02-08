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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPAppRoleFunction;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.exceptions.InactiveApplicationException;
import org.onap.portalapp.portal.exceptions.InvalidUserException;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.BulkUploadUserRoles;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.EcompUserRoles;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.transport.GlobalRoleWithApplicationRoleFunction;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, SystemProperties.class, EPCommonSystemProperties.class })
public class ExternalAccessRolesServiceImplTest {
	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@InjectMocks
	ExternalAccessRolesServiceImpl externalAccessRolesServiceImpl = new ExternalAccessRolesServiceImpl();

	@Mock 
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();
	
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
	
	@After
	public void after() {
		session.close();
	}

	private static final String APP_ROLE_NAME_PARAM = "appRoleName";

	private static final String GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM = "getRoletoUpdateInExternalAuthSystem";

	private static final String GET_PORTAL_APP_ROLES_QUERY = "getPortalAppRoles";

	private static final String GET_ROLE_FUNCTION_QUERY = "getRoleFunction";

	private static final String FUNCTION_CODE_PARAMS = "functionCode";

	private static final String AND_FUNCTION_CD_EQUALS = " and function_cd = '";

	private static final String OWNER = ".owner";

	private static final String ADMIN = ".admin";

	private static final String ACCOUNT_ADMINISTRATOR = ".Account_Administrator";

	private static final String FUNCTION_PIPE = "|";

	private static final String IS_NULL_STRING = "null";

	private static final String EXTERNAL_AUTH_PERMS = "perms";

	private static final String EXTERNAL_AUTH_ROLE_DESCRIPTION = "description";

	private static final String IS_EMPTY_JSON_STRING = "{}";

	private static final String CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE = "Connecting to External Auth system";

	private static final String APP_ROLE_ID = "appRoleId";

	private static final String APP_ID = "appId";

	private static final String PRIORITY = "priority";

	private static final String ACTIVE = "active";

	private static final String ROLE_NAME = "name";

	private static final String ID = "id";

	private static final String APP_ID_EQUALS = " app_id = ";

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	String uebKey = "test-ueb-key";

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

	@SuppressWarnings("deprecation")
	@Test
	public void getAppRolesIfAppIsPortalTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 1);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getAppRolesTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 10);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getAppExceptionTest() throws Exception {
		List<EPApp> app = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(app);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
		assertEquals(app, expectedapp);
	}

	@Test(expected = InactiveApplicationException.class)
	public void getAppErrorTest() throws Exception {
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		app.setEnabled(false);
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		externalAccessRolesServiceImpl.getApp(uebKey);
	}

	@Test
	public void getAppTest() throws Exception {
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		app.setId((long) 1);
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
		assertEquals(appList, expectedapp);
	}

	@Test
	public void addRoleTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		String uebKey = "test-ueb-key";
		Role role = new Role();
		role.setId((long) 25);
		EPApp app = mockApp();
		app.setEnabled(true);
		app.setId((long) 10);
		app.setNameSpace("test_namesapce");
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		List<EPRole> roleList = new ArrayList<>();
		EPRole ePRole = new EPRole();
		role.setName("Test Role");
		roleList.add(ePRole);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", role.getId());
		getPartnerAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(roleList);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL))
				.thenReturn("Testurl");
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);

		assertTrue(externalAccessRolesServiceImpl.addRole(role, uebKey));
	}

	@Test
	public void addRoleMethodNotAllowedTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		Role role = new Role();
		role.setId((long) 25);
		EPApp app = mockApp();
		app.setEnabled(true);
		app.setId((long) 10);
		app.setNameSpace("test_namesapce");
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		List<EPRole> roleList = new ArrayList<>();
		EPRole ePRole = new EPRole();
		role.setName("Test Role");
		roleList.add(ePRole);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", role.getId());
		getPartnerAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(roleList);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL))
				.thenReturn("Testurl");
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		assertFalse(externalAccessRolesServiceImpl.addRole(role, uebKey));
	}

	public EPApp getApp() {
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
		app.setOpen(true);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test
	public void deleteCentralRoleFunctionTest() throws Exception {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCode", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralV2RoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralV2RoleFunction domainCentralRoleFunction = new CentralV2RoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getRoleFunction", params, null))
				.thenReturn(centralRoleFunctionList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);

		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(EPAppRoleFunction.class,
				"app_id = " + app.getId() + " and function_cd = '" + "menu_fun_code" + "'", null);

		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void deleteCentralRoleFunctionFailTest() throws Exception {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCode", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralV2RoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralV2RoleFunction domainCentralRoleFunction = new CentralV2RoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
		Mockito.when(dataAccessService.executeNamedQuery("getRoleFunction", params, null))
				.thenReturn(centralRoleFunctionList);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenThrow(httpClientErrorException);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(EPAppRoleFunction.class,
				"app_id = " + app.getId() + " and function_cd = '" + "menu_fun_code" + "'", null);

		boolean returnedValue = externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app);
		assertTrue(returnedValue);
	}

	@Test
	public void deleteCentralRoleFunctionExceptionTest() {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCd", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralV2RoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralV2RoleFunction domainCentralRoleFunction = new CentralV2RoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionDetails", params, null))
				.thenThrow(nullPointerException);
		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void getUserTest() throws InvalidUserException {
		List<EPUser> userList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		userList.add(user);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", "guestT");
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null))
				.thenReturn(userList);
		List<EPUser> expectedUserList = externalAccessRolesServiceImpl.getUser("guestT");
		assertEquals(expectedUserList, userList);
	}

	@Test
	public void saveCentralRoleFunctionNewTestForV2() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPApp app = mockApp();
		app.setId((long) 1);
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setCode("test_code");
		centralV2RoleFunction.setName("test name");
		centralV2RoleFunction.setAppId(app.getId());
		centralV2RoleFunction.setAction("*");
		centralV2RoleFunction.setType("test_type");
		final Map<String, String> params = new HashMap<>();
		params.put("appId", String.valueOf(1));

		List<CentralV2RoleFunction> appRoleFunc = new ArrayList<>();
		appRoleFunc.add(centralV2RoleFunction);
		params.put(FUNCTION_CODE_PARAMS, centralV2RoleFunction.getType() + FUNCTION_PIPE
				+ centralV2RoleFunction.getCode() + FUNCTION_PIPE + centralV2RoleFunction.getAction());
		Mockito.when(dataAccessService.executeNamedQuery("getRoleFunction", params, null)).thenReturn(appRoleFunc);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		JSONObject mockJsonObjectPerm = new JSONObject();
		JSONObject mockJsonObjectFinalPerm = new JSONObject();
		mockJsonObjectPerm.put("type", "com.test.app.test_type");
		mockJsonObjectPerm.put("instance", "com.test.app.test_code");
		mockJsonObjectPerm.put("action", "*");
		mockJsonObjectPerm.put("description", "test name");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObjectPerm);
		mockJsonObjectFinalPerm.put("perm", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalPerm.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> updateResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(updateResponse);
		Boolean actual = externalAccessRolesServiceImpl.saveCentralRoleFunction(centralV2RoleFunction, app);
		assertEquals(true, actual);
	}

	@Test
	public void saveCentralRoleFunctionUpdateForV2Test() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPApp app = mockApp();
		app.setId((long) 1);
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setCode("test_code");
		centralV2RoleFunction.setName("test name2");
		centralV2RoleFunction.setAppId(app.getId());
		centralV2RoleFunction.setAction("*");
		centralV2RoleFunction.setType("test_type");
		CentralV2RoleFunction centralV2RoleFunctionExisting = new CentralV2RoleFunction();
		centralV2RoleFunctionExisting.setCode("test_code");
		centralV2RoleFunctionExisting.setName("test name");
		centralV2RoleFunctionExisting.setAppId(app.getId());
		centralV2RoleFunctionExisting.setAction("*");
		centralV2RoleFunctionExisting.setType("test_type");
		final Map<String, String> params = new HashMap<>();
		params.put("appId", String.valueOf(1));
		List<CentralV2RoleFunction> appRoleFunc = new ArrayList<>();
		appRoleFunc.add(centralV2RoleFunctionExisting);
		params.put(FUNCTION_CODE_PARAMS, centralV2RoleFunction.getType() + FUNCTION_PIPE
				+ centralV2RoleFunction.getCode() + FUNCTION_PIPE + centralV2RoleFunction.getAction());
		Mockito.when(dataAccessService.executeNamedQuery("getRoleFunction", params, null)).thenReturn(appRoleFunc);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		JSONObject mockJsonObjectPerm = new JSONObject();
		JSONObject mockJsonObjectFinalPerm = new JSONObject();
		mockJsonObjectPerm.put("type", "com.test.app.test_type");
		mockJsonObjectPerm.put("instance", "test_code");
		mockJsonObjectPerm.put("action", "*");
		mockJsonObjectPerm.put("description", "test name");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObjectPerm);
		mockJsonObjectFinalPerm.put("perm", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalPerm.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> updateResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(updateResponse);
		Boolean actual = externalAccessRolesServiceImpl.saveCentralRoleFunction(centralV2RoleFunction, app);
		assertEquals(true, actual);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = IndexOutOfBoundsException.class)
	public void getAllAppUsersTest() throws Exception {
		List<EPApp> expectedapps = new ArrayList<>();
		EPApp app = new EPApp();
		app.setEnabled(true);
		app.setId((long) 10);
		expectedapps.add(app);
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		Mockito.when(dataAccessService.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(expectedapps);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		List<EcompUserRoles> userList = new ArrayList<>();
		EcompUserRoles ecompUserRoles = new EcompUserRoles();
		ecompUserRoles.setOrgUserId("guestT");
		ecompUserRoles.setRoleId((long) 1);
		ecompUserRoles.setRoleName("test");

		EcompUserRoles ecompUserRoles2 = new EcompUserRoles();
		ecompUserRoles2.setOrgUserId("guestT");
		ecompUserRoles2.setRoleId((long) 2);
		ecompUserRoles2.setRoleName("test new");
		userList.add(ecompUserRoles);
		userList.add(ecompUserRoles2);

		Mockito.when(dataAccessService.executeNamedQuery("ApplicationUserRoles", appParams, null)).thenReturn(userList);
		List<EcompUser> usersfinalList = externalAccessRolesServiceImpl.getAllAppUsers(uebKey);
		assertEquals(usersfinalList.get(0).getRoles().size(), 2);
	}

	@Test
	public void getGlobalRolesOfPortalTest() {
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(null);
		assertEquals(externalAccessRolesServiceImpl.getGlobalRolesOfPortal(), null);
	}

	@Test
	public void getGlobalRolesOfPortalExceptionTest() {
		List<EPRole> globalRoles = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null))
				.thenThrow(nullPointerException);
		assertEquals(externalAccessRolesServiceImpl.getGlobalRolesOfPortal(), globalRoles);
	}

	@Test
	public void getRolesForAppTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> applicationRoles = new ArrayList<>();
		EPRole appRole = new EPRole();
		appRole.setActive(true);
		appRole.setAppId(app.getId());
		appRole.setAppRoleId(100l);
		appRole.setId(10l);
		appRole.setName("test");
		applicationRoles.add(appRole);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
				.thenReturn(applicationRoles);
		List<CentralV2RoleFunction> cenRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction = new CentralV2RoleFunction();
		v2RoleFunction.setAppId(app.getId());
		v2RoleFunction.setCode("test_type|test_code|*");
		v2RoleFunction.setName("test name");
		cenRoleFuncList.add(v2RoleFunction);
		final Map<String, Long> params = new HashMap<>();
		params.put("roleId", appRole.getId());
		params.put(APP_ID, appList.get(0).getId());
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionList", params, null))
				.thenReturn(cenRoleFuncList);
		List<GlobalRoleWithApplicationRoleFunction> mockGlobalRoles = new ArrayList<>();
		GlobalRoleWithApplicationRoleFunction mockGlobalRole = new GlobalRoleWithApplicationRoleFunction();
		mockGlobalRole.setActive(true);
		mockGlobalRole.setAppId(app.getId());
		mockGlobalRole.setRoleId(1111l);
		mockGlobalRole.setRoleName("global_test");
		mockGlobalRole.setFunctionCd("test_type|test_code|*");
		mockGlobalRole.setFunctionName("test name");
		mockGlobalRoles.add(mockGlobalRole);
		Map<String, Long> params2 = new HashMap<>();
		params2.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRoleWithApplicationRoleFunctions", params2, null))
				.thenReturn(mockGlobalRoles);
		List<EPRole> globalRoles = new ArrayList<>();
		EPRole globalRole = new EPRole();
		globalRole.setName("global_test");
		globalRole.setId(1111l);
		globalRole.setActive(true);
		globalRoles.add(globalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<CentralV2Role> expected = new ArrayList<>();
		CentralV2Role cenV2Role = new CentralV2Role();
		CentralV2Role cenV2Role2 = new CentralV2Role();
		expected.add(cenV2Role);
		expected.add(cenV2Role2);
		List<CentralV2Role> actual = externalAccessRolesServiceImpl.getRolesForApp(app.getUebKey());
		assertEquals(expected.size(), actual.size());
	}

	@Test
	public void saveRoleForPortalApplicationNewTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPApp app = mockApp();
		app.setId(1l);
		Role addRoleTest = new Role();
		addRoleTest.setActive(true);
		addRoleTest.setName("Test");
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		HttpHeaders headers = new HttpHeaders();
		JSONObject mockJsonObjectRole = new JSONObject();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectRole.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		final Map<String, String> epAppPortalRoleParams = new HashMap<>();
		epAppPortalRoleParams.put(APP_ROLE_NAME_PARAM, addRoleTest.getName());
		List<EPRole> getRoleCreated = new ArrayList<>();
		EPRole roleCreate = new EPRole();
		roleCreate.setActive(true);
		roleCreate.setId(10l);
		roleCreate.setName("test");
		getRoleCreated.add(roleCreate);
		Mockito.when(dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, epAppPortalRoleParams, null))
				.thenReturn(getRoleCreated);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void saveRoleForPortalApplicationUpdateTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(1l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc= new RoleFunction();
		roleFunc.setName("Test Name");
		roleFunc.setCode("test_type|test_instance|*");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setName("Test Name3");
		roleFunc2.setCode("test_type3|test_instance3|*");
		roleFuncSet.add(roleFunc);
		roleFuncSet.add(roleFunc2);
		addRoleTest.setActive(true);
		addRoleTest.setName("Test2");
		addRoleTest.setId(2l);
		addRoleTest.setRoleFunctions(roleFuncSet);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> globalRoles = new ArrayList<>();
		EPRole globalRole = new EPRole();
		globalRole.setName("global_test");
		globalRole.setId(1111l);
		globalRole.setActive(true);
		globalRoles.add(globalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		epRoleList.add(getEPRole);
		final Map<String, Long> getPortalAppRoleParams = new HashMap<>();
		getPortalAppRoleParams.put("roleId", addRoleTest.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRoleByRoleId", getPortalAppRoleParams, null))
				.thenReturn(epRoleList);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		JSONObject mockJsonObjectRole = new JSONObject();
		JSONObject mockJsonObjectFinalRole = new JSONObject();
		JSONObject mockJsonObjectPerm1 = new JSONObject();
		JSONObject mockJsonObjectPerm2 = new JSONObject();
		mockJsonObjectPerm1.put("type", "com.test.app.test_type");
		mockJsonObjectPerm1.put("instance", "test_instance");
		mockJsonObjectPerm1.put("action", "*");
		mockJsonObjectPerm2.put("type", "com.test.app.test_type2");
		mockJsonObjectPerm2.put("instance", "test_instance2");
		mockJsonObjectPerm2.put("action", "*");
		List<JSONObject> permsList =  new ArrayList<>();
		permsList.add(mockJsonObjectPerm1);
		permsList.add(mockJsonObjectPerm2);
		mockJsonObjectRole.put("name", "com.test.app.Test");
		mockJsonObjectRole.put("perms", permsList);
		mockJsonObjectRole.put("description",
				"{\"id\":\"2\",\"name\":\"Test\",\"active\":\"true\",\"priority\":\"null\",\"appId\":\"null\",\"appRoleId\":\"null\"}");
		List<JSONObject> roleList = new ArrayList<>();
		roleList.add(mockJsonObjectRole);
		mockJsonObjectFinalRole.put("role", roleList);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalRole.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> delResponse = new ResponseEntity<>(roleList.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(delResponse);
		ResponseEntity<String> addRoleResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addRoleResponse);
		final Map<String, String> params = new HashMap<>();
		params.put("uebKey", app.getUebKey());
		params.put("roleId", String.valueOf(getEPRole.getId()));
		List<BulkUploadUserRoles> userRolesList = new ArrayList<>();
		BulkUploadUserRoles bulkUploadUserRoles = new BulkUploadUserRoles();
		bulkUploadUserRoles.setAppNameSpace("com.test.app");
		bulkUploadUserRoles.setOrgUserId("guestT");
		bulkUploadUserRoles.setRoleName("Test2");
		userRolesList.add(bulkUploadUserRoles);
		Mockito.when(dataAccessService.executeNamedQuery("getBulkUsersForSingleRole", params, null))
				.thenReturn(userRolesList);
		Mockito.when(
				EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)).thenReturn("@test.com");
		ResponseEntity<String> mockBulkUsersUpload = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(mockBulkUsersUpload);
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>(); 
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", getEPRole.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null)).thenReturn(appRoleFunctionList);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		final Map<String, String> getAppFunctionParams = new HashMap<>(); 
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> v2RoleFunction = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction1 =  new CentralV2RoleFunction("test_type|test_instance|*", "Test Name");
		v2RoleFunction.add(v2RoleFunction1);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null)).thenReturn(v2RoleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>(); 
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> v2RoleFunction2 = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction3 =  new CentralV2RoleFunction("test_type3|test_instance3|*", "Test Name3");
		v2RoleFunction2.add(v2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null)).thenReturn(v2RoleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}
	
	@Test
	public void saveGlobalRoleFunctionsForPartnerApplicationUpdateTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc= new RoleFunction();
		roleFunc.setName("Test Name");
		roleFunc.setCode("test_type|test_instance|*");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setName("Test Name3");
		roleFunc2.setCode("test_type3|test_instance3|*");
		roleFuncSet.add(roleFunc);
		roleFuncSet.add(roleFunc2);
		addRoleTest.setActive(true);
		addRoleTest.setName("global_test");
		addRoleTest.setId(1111l);
		addRoleTest.setRoleFunctions(roleFuncSet);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> globalRoles = new ArrayList<>();
		EPRole globalRole = new EPRole();
		globalRole.setName("global_test");
		globalRole.setId(1111l);
		globalRole.setActive(true);
		EPRole globalRole2 = new EPRole();
		globalRole2.setName("global_test2");
		globalRole2.setId(2222l);
		globalRole2.setActive(true);
		globalRoles.add(globalRole);
		globalRoles.add(globalRole2);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<EPRole> getGlobalRoles = new ArrayList<>();
		EPRole getEPGlobalRole = new EPRole();
		getEPGlobalRole.setName("global_test");
		getEPGlobalRole.setId(1111l);
		getEPGlobalRole.setActive(true);
		getGlobalRoles.add(getEPGlobalRole);
		final Map<String, Long> getPortalAppRoleParams = new HashMap<>();
		getPortalAppRoleParams.put("roleId", globalRole.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRoleByRoleId", getPortalAppRoleParams, null))
				.thenReturn(getGlobalRoles);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(epAppCommonServiceImpl.getApp(PortalConstants.PORTAL_APP_ID)).thenReturn(app);
		JSONObject mockJsonObjectPerm = new JSONObject();
		JSONObject mockJsonObjectPerm2 = new JSONObject();
		JSONObject mockJsonObjectPerm3 = new JSONObject();
		JSONObject mockJsonObjectFinalPerm = new JSONObject();
		mockJsonObjectPerm.put("type", "com.test.app.test_type");
		mockJsonObjectPerm.put("instance", "test_instance");
		mockJsonObjectPerm.put("action", "*");
		mockJsonObjectPerm.put("description", "Test Name");
		mockJsonObjectPerm2.put("type", "com.test.app.access");
		mockJsonObjectPerm2.put("instance", "test_instance2");
		mockJsonObjectPerm2.put("action", "*");
		mockJsonObjectPerm2.put("description", "Test Name2");
		mockJsonObjectPerm3.put("type", "com.test.app.test_type3");
		mockJsonObjectPerm3.put("instance", "test_instance3");
		mockJsonObjectPerm3.put("action", "*");
		mockJsonObjectPerm3.put("description", "Test Name3");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObjectPerm);
		mockJson.add(mockJsonObjectPerm2);
		mockJson.add(mockJsonObjectPerm3);
		mockJsonObjectFinalPerm.put("perm", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalPerm.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		final Map<String, Long> epAppRoleFuncParams =  new HashMap<>();
		epAppRoleFuncParams.put("requestedAppId", app.getId());
		epAppRoleFuncParams.put("roleId",globalRole.getId());
		List<GlobalRoleWithApplicationRoleFunction> globalRoleFunctionList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRoleForRequestedApp", epAppRoleFuncParams, null)).thenReturn(globalRoleFunctionList);
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", globalRole.getId());
		List<EPAppRoleFunction> appRoleFunctionList =  new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setRoleAppId("1");
		epAppRoleFunction.setCode("test");
		epAppRoleFunction.setRoleId(1111l);
		appRoleFunctionList.add(epAppRoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null)).thenReturn(appRoleFunctionList);
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> roleFunction = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction(null, roleFunc.getCode(), roleFunc.getName(), app.getId(), null);
		roleFunction.add(centralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null)).thenReturn(roleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> roleFunction2 = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction2 = new CentralV2RoleFunction(null, roleFunc2.getCode(), roleFunc2.getName(), app.getId(), null);
		roleFunction2.add(centralV2RoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null)).thenReturn(roleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}
	
	@Test
	public void syncRoleFunctionFromExternalAccessSystemTest() {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		JSONObject mockJsonObjectFinalPerm = new JSONObject();
		JSONObject mockJsonObjectPerm = new JSONObject();
		JSONObject mockJsonObjectPerm2 = new JSONObject();
		JSONObject mockJsonObjectPerm3 = new JSONObject();
		mockJsonObjectPerm.put("type", "com.test.app.test_type");
		mockJsonObjectPerm.put("instance", "test_instance");
		mockJsonObjectPerm.put("action", "*");
		mockJsonObjectPerm.put("description", "test_name");
		List<String> rolePermList =  new ArrayList<>();
		rolePermList.add("com.test.app|test1");
		mockJsonObjectPerm.put("roles", rolePermList);
		mockJsonObjectPerm2.put("type", "com.test.app.test_type2");
		mockJsonObjectPerm2.put("instance", "test_instance2");
		mockJsonObjectPerm2.put("action", "*");
		mockJsonObjectPerm2.put("description", "test_name2");
		mockJsonObjectPerm3.put("type", "com.test.app.access");
		mockJsonObjectPerm3.put("instance", "test_instance3");
		mockJsonObjectPerm3.put("action", "*");
		mockJsonObjectPerm3.put("description", "test_name3");
		List<JSONObject> permsList =  new ArrayList<>();
		permsList.add(mockJsonObjectPerm);
		permsList.add(mockJsonObjectPerm2);
		permsList.add(mockJsonObjectPerm3);
		mockJsonObjectFinalPerm.put("perm", permsList);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalPerm.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		final Map<String, Long> params = new HashMap<>();
		params.put(APP_ID, app.getId());
		List<CentralV2RoleFunction> appFunctions = new ArrayList<>();	
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction(null, "test_type|test_instance|*", "test_name", app.getId(), null);
		appFunctions.add(centralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAllRoleFunctions", params,null)).thenReturn(appFunctions);
		List<EPRole> globalRoles = new ArrayList<>();
		EPRole globalRole = new EPRole();
		globalRole.setName("global_test");
		globalRole.setId(1111l);
		globalRole.setActive(true);
		globalRoles.add(globalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<EPRole> getCurrentRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("test1");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("global_test");
		getEPRole2.setId(1111l);
		getEPRole2.setActive(true);
		getCurrentRoleList.add(getEPRole);
		getCurrentRoleList.add(getEPRole2);
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRolesList", null, null)).thenReturn(getCurrentRoleList);
		final Map<String, String> appSyncFuncsParams = new HashMap<>();
		appSyncFuncsParams.put("appId", String.valueOf(app.getId()));
		appSyncFuncsParams.put("functionCd", "");
		List<CentralV2RoleFunction> roleFunctionList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
				null)).thenReturn(roleFunctionList);
		String code = centralV2RoleFunction.getCode();
		appSyncFuncsParams.put("functionCd", code);
		CentralV2RoleFunction getCentralV2RoleFunction = new CentralV2RoleFunction(null, "test_type|test_instance|*", "test_name", app.getId(), null);
		roleFunctionList.add(getCentralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
				null)).thenReturn(roleFunctionList);
		final Map<String, String> appRoleFuncParams = new HashMap<>();
		appRoleFuncParams.put("functionCd", roleFunctionList.get(0).getCode());
		appRoleFuncParams.put("appId", String.valueOf(app.getId()));
		Mockito.when(dataAccessService.executeNamedQuery("getCurrentAppRoleFunctions",
				appRoleFuncParams, null)).thenReturn(new ArrayList<LocalRole>());
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app", app.getNameSpace())).thenReturn(true);
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type2|test_instance2|*")).thenReturn("test_instance2");
		appSyncFuncsParams.put("functionCd", "test_instance2");
		List<CentralV2RoleFunction> roleFunctionList2 = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
				null)).thenReturn(roleFunctionList2);
		String code2 = "test_type2|test_instance2|*";
		appSyncFuncsParams.put("functionCd", code2);
		CentralV2RoleFunction getCentralV2RoleFunction2 = new CentralV2RoleFunction(null, "test_type2|test_instance2|*", "test_name2", app.getId(), null);
		roleFunctionList2.add(getCentralV2RoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams,
				null)).thenReturn(roleFunctionList2);
		externalAccessRolesServiceImpl.syncRoleFunctionFromExternalAccessSystem(app);
	}
	
	@Test
	public void syncApplicationRolesWithEcompDBTest(){
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		JSONObject mockJsonObjectRole = new JSONObject();
		JSONObject mockJsonObjectRole2 = new JSONObject();
		JSONObject mockJsonObjectFinalRole = new JSONObject();
		JSONObject mockJsonObjectPerm1 = new JSONObject();
		JSONObject mockJsonObjectPerm2 = new JSONObject();
		mockJsonObjectPerm1.put("type", "com.test.app.test_type");
		mockJsonObjectPerm1.put("instance", "test_instance");
		mockJsonObjectPerm1.put("action", "*");
		mockJsonObjectPerm2.put("type", "com.test.app.test_type2");
		mockJsonObjectPerm2.put("instance", "test_instance2");
		mockJsonObjectPerm2.put("action", "*");
		List<JSONObject> permsList =  new ArrayList<>();
		permsList.add(mockJsonObjectPerm1);
		permsList.add(mockJsonObjectPerm2);
		mockJsonObjectRole.put("name", "com.test.app.Test");
		mockJsonObjectRole.put("perms", permsList);
		mockJsonObjectRole.put("description",
				"{\"id\":\"2\",\"name\":\"test1\",\"active\":\"true\",\"priority\":\"null\",\"appId\":\"2\",\"appRoleId\":\"2\"}");
		mockJsonObjectRole2.put("name", "com.test.app.Test2");
		List<JSONObject> permsList2 =  new ArrayList<>();
		permsList2.add(mockJsonObjectPerm1);
		mockJsonObjectRole2.put("perms", permsList2);
		List<JSONObject> roleList = new ArrayList<>();
		roleList.add(mockJsonObjectRole);
		roleList.add(mockJsonObjectRole2);
		mockJsonObjectFinalRole.put("role", roleList);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalRole.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		List<EPRole> getCurrentRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setAppId(app.getId());
		getEPRole.setAppRoleId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test3");
		getEPRole2.setId(3l);
		getEPRole.setAppId(app.getId());
		getEPRole.setAppRoleId(3l);
		getEPRole2.setActive(true);
		getCurrentRoleList.add(getEPRole);
		getCurrentRoleList.add(getEPRole2);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null)).thenReturn(getCurrentRoleList);
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app.test_type", app.getNameSpace())).thenReturn(true);
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app.test_type2", app.getNameSpace())).thenReturn(true);
		List<EPAppRoleFunction> appRoleFunctions = new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setCode("test_type|test_instance|*");
		epAppRoleFunction.setRoleId(getEPRole.getId());
		appRoleFunctions.add(epAppRoleFunction);
		final Map<String, Long> appRoleFuncsParams = new  HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", Long.valueOf(getEPRole.getId()));
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null)).thenReturn(appRoleFunctions);
		List<CentralV2RoleFunction> getV2RoleFunction =  new ArrayList<>();
		final Map<String, String> appFuncsParams = new  HashMap<>();
		appFuncsParams.put("appId", String.valueOf(app.getId()));
		appFuncsParams.put("functionCd", "test_instance2");
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null)).thenReturn(getV2RoleFunction);
		appFuncsParams.put("functionCd", "test_type2|test_instance2|*");
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setAppId(app.getId());
		centralV2RoleFunction.setCode("test_type2|test_instance2|*");
		centralV2RoleFunction.setName("test_name2");
		getV2RoleFunction.add(centralV2RoleFunction);
		final Map<String, String> extRoleParams = new HashMap<>();
		List<EPRole> roleListDeactivate = new ArrayList<>();
		extRoleParams.put(APP_ROLE_NAME_PARAM, "Test3");
		extRoleParams.put(APP_ID, app.getId().toString());
		EPRole getEPRoleDeactivate = new EPRole();
		getEPRoleDeactivate.setName("Test3");
		getEPRoleDeactivate.setId(3l);
		getEPRoleDeactivate.setAppId(app.getId());
		getEPRoleDeactivate.setAppRoleId(3l);
		roleListDeactivate.add(getEPRoleDeactivate);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, extRoleParams, null)).thenReturn(roleListDeactivate);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null)).thenReturn(getV2RoleFunction);
		List<EPRole> updateLocalFromExtAuth = new ArrayList<>();
		updateLocalFromExtAuth.add(getEPRole);
		final Map<String, String> roleParams = new HashMap<>();
		roleParams.put(APP_ROLE_NAME_PARAM, getEPRole.getName());
		roleParams.put(APP_ID, app.getId().toString());
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams, null)).thenReturn(updateLocalFromExtAuth);
		roleParams.put(APP_ROLE_NAME_PARAM, getEPRole2.getName());
		List<EPRole> updateLocalFromExtAuth2 = new ArrayList<>();
		updateLocalFromExtAuth.add(getEPRole);
		Mockito.when(dataAccessService.executeNamedQuery("getRoletoUpdateInExternalAuthSystem", roleParams, null)).thenReturn(updateLocalFromExtAuth2);
		final Map<String, String> globalRoleParams = new HashMap<>();
		globalRoleParams.put("appId", String.valueOf(app.getId()));
		globalRoleParams.put("appRoleName", "Test2");
		List<EPRole> addNewRoleList = new ArrayList<>();
		EPRole addRoleInLocal = new EPRole();
		addRoleInLocal.setName("Test2");
		addRoleInLocal.setId(4l);
		addRoleInLocal.setAppId(app.getId());
		addRoleInLocal.setActive(true);
		addNewRoleList.add(addRoleInLocal);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, globalRoleParams, null)).thenReturn(addNewRoleList);
		final Map<String, String> params = new HashMap<>();
		params.put(APP_ROLE_NAME_PARAM, "Test2");
		params.put(APP_ID, app.getId().toString());
		addRoleInLocal.setAppRoleId(4l);
		addNewRoleList.add(addRoleInLocal);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, params, null)).thenReturn(addNewRoleList);
		externalAccessRolesServiceImpl.syncApplicationRolesWithEcompDB(app);
	}
	
	@Test 
	public void deleteDependencyRoleRecord() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		EPApp app = mockApp();
		app.setId(2l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setAppRoleId(2l);
		getEPRole.setActive(true);
		epRoleList.add(getEPRole);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", getEPRole.getId());
		getPartnerAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(epRoleList);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		JSONObject getUser =  new JSONObject();
		getUser.put("name", "com.test.app.test1");
		ResponseEntity<String> getResponse = new ResponseEntity<>(getUser.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.deleteDependencyRoleRecord(2l, app.getUebKey(), user.getOrgUserId());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}
	
	@Test 
	public void deleteDependencyRoleRecordForPortal() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		EPApp app = mockApp();
		app.setId(1l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setAppRoleId(2l);
		getEPRole.setActive(true);
		epRoleList.add(getEPRole);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("roleId", getEPRole.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(epRoleList);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		JSONObject getUser =  new JSONObject();
		getUser.put("name", "com.test.app.test1");
		ResponseEntity<String> getResponse = new ResponseEntity<>(getUser.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.deleteDependencyRoleRecord(2l, app.getUebKey(), user.getOrgUserId());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}
	
	@Test 
	public void bulkUploadFunctionsTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<RoleFunction> roleFuncList = new ArrayList<>();
		RoleFunction roleFunc = new RoleFunction();
		roleFunc.setCode("test_code");
		roleFunc.setName("test_name");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setCode("test_code2");
		roleFunc2.setName("test_name2");
		roleFuncList.add(roleFunc);
		roleFuncList.add(roleFunc2);
		Mockito.when(dataAccessService.executeNamedQuery("getAllFunctions", null, null)).thenReturn(roleFuncList);
		JSONObject perm =  new JSONObject();
		JSONObject permList =  new JSONObject();
		perm.put("type", app.getNameSpace()+".access");
		perm.put("instance", "type_instance");
		perm.put("action", "*");
		List<JSONObject> addPerms =  new ArrayList<>();
		addPerms.add(perm);
		permList.put("perm", addPerms);
		ResponseEntity<String> getResponse = new ResponseEntity<>(permList.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadFunctions(app.getUebKey());
		Integer expected = 2;
		assertEquals(expected, actual);
	}
	
	@Test
	public void bulkUploadRolesTest() throws Exception{
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setAppRoleId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test2");
		getEPRole2.setId(3l);
		getEPRole2.setAppRoleId(3l);
		getEPRole2.setActive(true);
		epRoleList.add(getEPRole);
		epRoleList.add(getEPRole2);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null)).thenReturn(epRoleList);
		List<EPRole> epRoleList1 = new ArrayList<>();
		EPRole ePRole = new EPRole();
		ePRole.setName("Test");
		ePRole.setId(2l);
		ePRole.setAppRoleId(2l);
		ePRole.setActive(true);
		epRoleList1.add(ePRole);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", ePRole.getId());
		getPartnerAppRoleParams.put("appId", app.getId());				
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null)).thenReturn(epRoleList1);
		List<EPRole> epRoleList2 = new ArrayList<>();
		EPRole ePRole2 = new EPRole();
		ePRole2.setName("Test2");
		ePRole2.setId(3l);
		ePRole2.setAppRoleId(3l);
		ePRole2.setActive(true);
		epRoleList2.add(ePRole2);
		final Map<String, Long> getPartnerAppRoleParams2 = new HashMap<>();
		getPartnerAppRoleParams2.put("appRoleId", ePRole2.getId());
		getPartnerAppRoleParams2.put("appId", app.getId());		
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams2, null)).thenReturn(epRoleList2);
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadRoles(app.getUebKey());
		Integer expected = 2;
		assertEquals(expected, actual);
	}
	
	@Test
	public void bulkUploadUserRolesTest() throws Exception{
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPApp app = mockApp();
		app.setId(2l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<BulkUploadUserRoles> userRolesList = new ArrayList<>();
		BulkUploadUserRoles bulkUploadUserRoles = new BulkUploadUserRoles();
		bulkUploadUserRoles.setAppNameSpace(app.getName());
		bulkUploadUserRoles.setOrgUserId(user.getOrgUserId());
		bulkUploadUserRoles.setRoleName("Test1");
		BulkUploadUserRoles bulkUploadUserRoles2 = new BulkUploadUserRoles();
		bulkUploadUserRoles2.setAppNameSpace(app.getName());
		bulkUploadUserRoles2.setOrgUserId(user.getOrgUserId());
		bulkUploadUserRoles2.setRoleName("Test2");
		userRolesList.add(bulkUploadUserRoles);
		userRolesList.add(bulkUploadUserRoles2);
		final Map<String, String> appParams = new HashMap<>();
		appParams.put("uebKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getBulkUserRoles", appParams, null)).thenReturn(userRolesList);
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadUserRoles(app.getUebKey());
		Integer expected = 2;
		assertEquals(expected, actual);
	}
	
	
}
