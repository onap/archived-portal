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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPAppRoleFunction;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.EpAppType;
import org.onap.portalapp.portal.ecomp.model.UploadRoleFunctionExtSystem;
import org.onap.portalapp.portal.exceptions.InactiveApplicationException;
import org.onap.portalapp.portal.exceptions.InvalidUserException;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.*;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, Criterion.class, EPUserUtils.class, Restrictions.class, SystemProperties.class,
		EPCommonSystemProperties.class })
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
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
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

	private static final String FUNCTION_PIPE = "|";

	private static final String APP_ID = "appId";

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
		app.setRolesInAAF(true);
		app.setAppDescription("test");
		app.setAppNotes("test");
		app.setLandingPage("test");
		app.setId((long) 10);
		app.setAppRestEndpoint("test");
		app.setAlternateLandingPage("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("test");
		app.setOpen(false);
		app.setEnabled(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(EpAppType.GUI);
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
		app.setAppDescription("test");
		app.setAppNotes("test");
		app.setLandingPage("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateLandingPage("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("test");
		app.setOpen(true);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(EpAppType.GUI);
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
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(EPAppRoleFunction.class,
				"app_id = " + app.getId() + " and function_cd = '" + "menu_fun_code" + "'", null);
		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void deleteRoleForApplicationTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, String> deleteRoleParams = new HashMap<>();
		deleteRoleParams.put(APP_ROLE_NAME_PARAM, "test_delete");
		deleteRoleParams.put(APP_ID, String.valueOf(app.getId()));
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole epRole = new EPRole();
		epRole.setName("test_delete");
		epRole.setId(1l);
		epRole.setActive(true);
		epRole.setAppRoleId(11l);
		epRoleList.add(epRole);
		Mockito.when(
				dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, deleteRoleParams, null))
				.thenReturn(epRoleList);
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", epRole.getId());
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setCode("test_code");
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setRoleAppId(null);
		appRoleFunctionList.add(epAppRoleFunction);
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		assertTrue(externalAccessRolesServiceImpl.deleteRoleForApplication(epRole.getName(), app.getUebKey()));
	}

	@Test
	public void deleteRoleForPortalApplicationTest() throws Exception {
		EPApp app = mockApp();
		app.setId(1l);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, String> deleteRoleParams2 = new HashMap<>();
		deleteRoleParams2.put(APP_ROLE_NAME_PARAM, "test_delete");
		List<EPRole> epRoleList2 = new ArrayList<>();
		EPRole epRole = new EPRole();
		epRole.setName("test_delete");
		epRole.setId(1l);
		epRole.setActive(true);
		epRoleList2.add(epRole);
		Mockito.when(dataAccessService.executeNamedQuery(GET_PORTAL_APP_ROLES_QUERY, deleteRoleParams2, null))
				.thenReturn(epRoleList2);
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", epRole.getId());
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setCode("test_code");
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setRoleAppId(null);
		appRoleFunctionList.add(epAppRoleFunction);
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		SQLQuery SqlQuery = Mockito.mock(SQLQuery.class);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		assertTrue(externalAccessRolesServiceImpl.deleteRoleForApplication(epRole.getName(), app.getUebKey()));
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
	public void getRoleFunctionTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type|type_code|*")).thenReturn("type_code");
		Mockito.when(EcompPortalUtils.getFunctionType("test_type|type_code|*")).thenReturn("test_type");
		Mockito.when(EcompPortalUtils.getFunctionAction("test_type|type_code|*")).thenReturn("*");
		List<CentralV2RoleFunction> getRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction getCenRole = new CentralV2RoleFunction("test_type|type_code|*", "test_name");
		getRoleFuncList.add(getCenRole);
		final Map<String, String> params = new HashMap<>();
		params.put(FUNCTION_CODE_PARAMS, "test_type|type_code|*");
		params.put(APP_ID, String.valueOf(app.getId()));
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, params, null))
				.thenReturn(getRoleFuncList);
		CentralV2RoleFunction actual = externalAccessRolesServiceImpl.getRoleFunction("test_type|type_code|*",
				app.getUebKey());
		assertEquals("type_code", actual.getCode());
	}

	@Test
	public void getRoleFunctionMutilpleFilterTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		CentralV2RoleFunction expected = new CentralV2RoleFunction(null, "type_code", "test_name", null, "test_type",
				"*", null);
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type|type_code|*")).thenReturn("type_code");
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type_1|type_code_1|*")).thenReturn("type_code_1");
		Mockito.when(EcompPortalUtils.getFunctionType("test_type|type_code|*")).thenReturn("test_type");
		Mockito.when(EcompPortalUtils.getFunctionAction("test_type|type_code|*")).thenReturn("*");
		Mockito.when(EcompPortalUtils.encodeFunctionCode("type_code")).thenReturn("type_code");
		List<CentralV2RoleFunction> getRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction getCenRole = new CentralV2RoleFunction("test_type|type_code|*", "test_name");
		CentralV2RoleFunction getCenRole2 = new CentralV2RoleFunction("test_type_1|type_code_1|*", "test_name_1");
		getRoleFuncList.add(getCenRole);
		getRoleFuncList.add(getCenRole2);
		final Map<String, String> params = new HashMap<>();
		params.put(FUNCTION_CODE_PARAMS, "test_type|type_code|*");
		params.put(APP_ID, String.valueOf(app.getId()));
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, params, null))
				.thenReturn(getRoleFuncList);
		CentralV2RoleFunction actual = externalAccessRolesServiceImpl.getRoleFunction("test_type|type_code|*",
				app.getUebKey());
		assertEquals(expected.getCode(), actual.getCode());
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
	public void getV2UserWithRolesTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		EPUser user = mockUser.mockEPUser();
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", user.getOrgUserId());
		List<EPUser> userList = new ArrayList<>();
		Set<EPUserApp> userAppSet = user.getEPUserApps();
		EPUserApp epUserApp = new EPUserApp();
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setId(1l);
		epRole.setActive(true);
		epRole.setAppRoleId(11l);
		epUserApp.setApp(app);
		epUserApp.setUserId(user.getId());
		epUserApp.setRole(epRole);
		userAppSet.add(epUserApp);
		user.setUserApps(userAppSet);
		userList.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null))
				.thenReturn(userList);
		final Map<String, Long> params = new HashMap<>();
		List<CentralV2RoleFunction> appRoleFunctionList = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setCode("test_type|test_code|*");
		centralV2RoleFunction.setName("test name");
		centralV2RoleFunction.setAppId(app.getId());
		appRoleFunctionList.add(centralV2RoleFunction);
		params.put("roleId", epUserApp.getRole().getId());
		params.put(APP_ID, epUserApp.getApp().getId());
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionList", params, null))
				.thenReturn(appRoleFunctionList);
		String actual = externalAccessRolesServiceImpl.getV2UserWithRoles(user.getOrgUserId(), app.getUebKey());
		String notExpected = "";
		assertNotEquals(actual, notExpected);
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
	@Test
	public void getAllAppUsersTest() throws Exception {
		EPApp app = new EPApp();
		app.setEnabled(true);
		app.setId((long) 10);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		Mockito.when(dataAccessService.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(appList);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		List<EcompUserRoles> userList = new ArrayList<>();
		EcompUserRoles ecompUserRoles = new EcompUserRoles();
		ecompUserRoles.setOrgUserId("guestT");
		ecompUserRoles.setRoleId((long) 1);
		ecompUserRoles.setRoleName("test");
		ecompUserRoles.setFunctionCode("test_type|test_instance|test_action");
		ecompUserRoles.setFunctionName("test1");
		EcompUserRoles ecompUserRoles2 = new EcompUserRoles();
		ecompUserRoles2.setOrgUserId("guestT");
		ecompUserRoles2.setRoleId((long) 2);
		ecompUserRoles2.setRoleName("test new");
		ecompUserRoles2.setFunctionCode("test_instance2");
		ecompUserRoles2.setFunctionName("test2");
		userList.add(ecompUserRoles);
		userList.add(ecompUserRoles2);
		Mockito.when(EcompPortalUtils.getFunctionCode(ecompUserRoles.getFunctionCode())).thenReturn("test_instance");
        Mockito.when(EPUserUtils.decodeFunctionCode("test_instance")).thenReturn("test_instance");
        Mockito.when(EcompPortalUtils.getFunctionCode(ecompUserRoles2.getFunctionCode())).thenReturn("test_instance2");
        Mockito.when(EPUserUtils.decodeFunctionCode("test_instance2")).thenReturn("test_instance2");
        Mockito.when(EcompPortalUtils.getFunctionType("test_type|test_instance|test_action")).thenReturn("test_type");
        Mockito.when(EcompPortalUtils.getFunctionAction("test_type|test_instance|test_action")).thenReturn("test_action");
        Mockito.when(dataAccessService.executeNamedQuery("ApplicationUserRoles", appParams, null)).thenReturn(userList);
		List<EcompUser> usersfinalList = externalAccessRolesServiceImpl.getAllAppUsers(app.getUebKey());
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
		CentralV2Role cenV2Role = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		CentralV2Role cenV2Role2 = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		expected.add(cenV2Role);
		expected.add(cenV2Role2);
		List<CentralV2Role> actual = externalAccessRolesServiceImpl.getRolesForApp(app.getUebKey());
		assertEquals(expected.size(), actual.size());
	}

	@Test
	public void getRoleFuncListTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> params = new HashMap<>();
		params.put(APP_ID, app.getId());
		List<CentralV2RoleFunction> expected = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction("test_type|type_code|*", "test_name");
		expected.add(centralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null)).thenReturn(expected);
		List<CentralV2RoleFunction> actual = externalAccessRolesServiceImpl.getRoleFuncList(app.getUebKey());
		assertEquals(expected, actual);
	}

	@Test
	public void getRoleInfoTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> globalRoles = new ArrayList<>();
		EPRole globalRole = new EPRole();
		globalRole.setName("global_test");
		globalRole.setId(2l);
		globalRole.setActive(true);
		globalRoles.add(globalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<GlobalRoleWithApplicationRoleFunction> mockGlobalRoles = new ArrayList<>();
		GlobalRoleWithApplicationRoleFunction mockGlobalRole = new GlobalRoleWithApplicationRoleFunction();
		mockGlobalRole.setActive(true);
		mockGlobalRole.setAppId(app.getId());
		mockGlobalRole.setRoleId(2l);
		mockGlobalRole.setRoleName("global_test");
		mockGlobalRole.setFunctionCd("test_type|test_code|*");
		mockGlobalRole.setFunctionName("test name");
		mockGlobalRoles.add(mockGlobalRole);
		Map<String, Long> params = new HashMap<>();
		params.put("roleId", 2l);
		params.put("requestedAppId", 2l);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRoleForRequestedApp", params, null))
				.thenReturn(mockGlobalRoles);
		CentralV2Role actual = externalAccessRolesServiceImpl.getRoleInfo(2l, app.getUebKey());
		assertNotEquals(null, actual);
	}

	@Test
	public void getPartnerRoleInfoTest() throws Exception {
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
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", 10l);
		getPartnerAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(applicationRoles);
		final Map<String, Long> params = new HashMap<>();
		params.put("roleId", appRole.getId());
		params.put(APP_ID, app.getId());
		List<CentralV2RoleFunction> cenRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction("test_type|type_code|*", "test_name");
		cenRoleFuncList.add(centralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionList", params, null))
				.thenReturn(cenRoleFuncList);
		CentralV2Role actual = externalAccessRolesServiceImpl.getRoleInfo(10l, app.getUebKey());
		assertNotEquals(null, actual);
	}

	@Test
	public void saveRoleForPortalApplicationNewTest() throws Exception {
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
		EPApp app = mockApp();
		app.setId(1l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
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
		List<JSONObject> permsList = new ArrayList<>();
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
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		ResponseEntity<String> mockBulkUsersUpload = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(mockBulkUsersUpload);
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", getEPRole.getId());
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> v2RoleFunction = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction1 = new CentralV2RoleFunction("test_type|test_instance|*", "Test Name");
		v2RoleFunction.add(v2RoleFunction1);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null))
				.thenReturn(v2RoleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> v2RoleFunction2 = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction3 = new CentralV2RoleFunction("test_type3|test_instance3|*", "Test Name3");
		v2RoleFunction2.add(v2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null))
				.thenReturn(v2RoleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void saveRoleExitsInDbButNotInExtAuthSystemTest() throws Exception {
		EPApp app = mockApp();
		app.setId(1l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
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
		JSONObject mockJsonObjectFinalRole = new JSONObject();
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalRole.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
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
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", getEPRole.getId());
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> v2RoleFunction = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction1 = new CentralV2RoleFunction("test_type|test_instance|*", "Test Name");
		v2RoleFunction.add(v2RoleFunction1);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null))
				.thenReturn(v2RoleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> v2RoleFunction2 = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction3 = new CentralV2RoleFunction("test_type3|test_instance3|*", "Test Name3");
		v2RoleFunction2.add(v2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null))
				.thenReturn(v2RoleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void saveGlobalRoleForPortalApplicationUpdateTest() throws Exception {
		EPApp app = mockApp();
		app.setId(1l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
		roleFunc.setName("Test Name");
		roleFunc.setCode("test_type|test_instance|*");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setName("Test Name3");
		roleFunc2.setCode("test_type3|test_instance3|*");
		roleFuncSet.add(roleFunc);
		roleFuncSet.add(roleFunc2);
		addRoleTest.setActive(true);
		addRoleTest.setName("global_test2");
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
		globalRoles.add(globalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(globalRoles);
		List<EPRole> epRoleList = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("global_test");
		getEPRole.setId(1111l);
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
		List<JSONObject> permsList = new ArrayList<>();
		permsList.add(mockJsonObjectPerm1);
		permsList.add(mockJsonObjectPerm2);
		mockJsonObjectRole.put("name", "com.test.app.global_test");
		mockJsonObjectRole.put("perms", permsList);
		mockJsonObjectRole.put("description",
				"{\"id\":\"1111\",\"name\":\"global_test\",\"active\":\"true\",\"priority\":\"null\",\"appId\":\"null\",\"appRoleId\":\"null\"}");
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
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		ResponseEntity<String> mockBulkUsersUpload = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(mockBulkUsersUpload);
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", getEPRole.getId());
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> v2RoleFunction = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction1 = new CentralV2RoleFunction("test_type|test_instance|*", "Test Name");
		v2RoleFunction.add(v2RoleFunction1);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null))
				.thenReturn(v2RoleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> v2RoleFunction2 = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction3 = new CentralV2RoleFunction("test_type3|test_instance3|*", "Test Name3");
		v2RoleFunction2.add(v2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null))
				.thenReturn(v2RoleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void saveRoleForPartnerApplicationUpdateTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
		roleFunc.setName("Test Name");
		roleFunc.setCode("test_type|test_instance|*");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setName("Test Name3");
		roleFunc2.setCode("test_type3|test_instance3|*");
		roleFuncSet.add(roleFunc);
		roleFuncSet.add(roleFunc2);
		addRoleTest.setActive(false);
		addRoleTest.setName("Test2");
		addRoleTest.setId(22l);
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
		getEPRole.setName("Test2");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		getEPRole.setAppRoleId(22l);
		epRoleList.add(getEPRole);
		final Map<String, Long> getPortalAppRoleParams = new HashMap<>();
		getPortalAppRoleParams.put("appRoleId", addRoleTest.getId());
		getPortalAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPortalAppRoleParams, null))
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
		List<JSONObject> permsList = new ArrayList<>();
		permsList.add(mockJsonObjectPerm1);
		permsList.add(mockJsonObjectPerm2);
		mockJsonObjectRole.put("name", "com.test.app.Test");
		mockJsonObjectRole.put("perms", permsList);
		mockJsonObjectRole.put("description",
				"{\"id\":\"2\",\"name\":\"Test2\",\"active\":\"true\",\"priority\":\"null\",\"appId\":\"2\",\"appRoleId\":\"22\"}");
		List<JSONObject> roleList = new ArrayList<>();
		roleList.add(mockJsonObjectRole);
		mockJsonObjectFinalRole.put("role", roleList);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalRole.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> delResponse = new ResponseEntity<>(roleList.toString(), HttpStatus.OK);
		final Map<String, String> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put(APP_ROLE_NAME_PARAM, addRoleTest.getName());
		getPartnerAppRoleParams.put("appId", String.valueOf(app.getId()));
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM,
				getPartnerAppRoleParams, null)).thenReturn(epRoleList);
		ResponseEntity<String> updateRoleResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(updateRoleResponse);
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
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		ResponseEntity<String> mockBulkUsersUpload = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(mockBulkUsersUpload);
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", getEPRole.getId());
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> v2RoleFunction = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction1 = new CentralV2RoleFunction("test_type|test_instance|*", "Test Name");
		v2RoleFunction.add(v2RoleFunction1);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null))
				.thenReturn(v2RoleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> v2RoleFunction2 = new ArrayList<>();
		CentralV2RoleFunction v2RoleFunction3 = new CentralV2RoleFunction("test_type3|test_instance3|*", "Test Name3");
		v2RoleFunction2.add(v2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null))
				.thenReturn(v2RoleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void saveGlobalRoleFunctionsForPartnerApplicationUpdateTest() throws Exception {
		EPApp app = mockApp();
		app.setId(2l);
		Role addRoleTest = new Role();
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
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
		JSONObject mockJsonObjectPerm4 = new JSONObject();
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
		mockJsonObjectPerm4.put("type", "com.test.app.test_type4");
		mockJsonObjectPerm4.put("instance", "test_instance4");
		mockJsonObjectPerm4.put("action", "*");
		mockJsonObjectPerm4.put("description", "Test Name4");
		List<JSONObject> mockJson = new ArrayList<>();
		mockJson.add(mockJsonObjectPerm);
		mockJson.add(mockJsonObjectPerm2);
		mockJson.add(mockJsonObjectPerm3);
		mockJson.add(mockJsonObjectPerm4);
		mockJsonObjectFinalPerm.put("perm", mockJson);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalPerm.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		final Map<String, Long> epAppRoleFuncParams = new HashMap<>();
		epAppRoleFuncParams.put("requestedAppId", app.getId());
		epAppRoleFuncParams.put("roleId", globalRole.getId());
		List<GlobalRoleWithApplicationRoleFunction> mockGlobalRoles = new ArrayList<>();
		GlobalRoleWithApplicationRoleFunction mockGlobalRole = new GlobalRoleWithApplicationRoleFunction();
		mockGlobalRole.setActive(true);
		mockGlobalRole.setAppId(app.getId());
		mockGlobalRole.setRoleId(1111l);
		mockGlobalRole.setRoleName("global_test");
		mockGlobalRole.setFunctionCd("test_type4|test_instance4|*");
		mockGlobalRole.setFunctionName("test name");
		mockGlobalRoles.add(mockGlobalRole);
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRoleForRequestedApp", epAppRoleFuncParams, null))
				.thenReturn(mockGlobalRoles);
		ResponseEntity<String> delResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(delResponse);
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", globalRole.getId());
		List<EPAppRoleFunction> appRoleFunctionList = new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setRoleAppId("1");
		epAppRoleFunction.setCode("test");
		epAppRoleFunction.setRoleId(1111l);
		appRoleFunctionList.add(epAppRoleFunction);
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctionList);
		final Map<String, String> getAppFunctionParams = new HashMap<>();
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunc2.getCode())).thenReturn("test_instance3");
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type4|test_instance4|*")).thenReturn("test_instance4");
		getAppFunctionParams.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams.put(FUNCTION_CODE_PARAMS, roleFunc.getCode());
		List<CentralV2RoleFunction> roleFunction = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction(null, roleFunc.getCode(),
				roleFunc.getName(), app.getId(), null);
		roleFunction.add(centralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams, null))
				.thenReturn(roleFunction);
		final Map<String, String> getAppFunctionParams2 = new HashMap<>();
		getAppFunctionParams2.put("appId", String.valueOf(app.getId()));
		getAppFunctionParams2.put(FUNCTION_CODE_PARAMS, roleFunc2.getCode());
		List<CentralV2RoleFunction> roleFunction2 = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction2 = new CentralV2RoleFunction(null, roleFunc2.getCode(),
				roleFunc2.getName(), app.getId(), null);
		roleFunction2.add(centralV2RoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_FUNCTION_QUERY, getAppFunctionParams2, null))
				.thenReturn(roleFunction2);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.saveRoleForApplication(addRoleTest,
				app.getUebKey());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void syncRoleFunctionFromExternalAccessSystemTest() {
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
		List<String> rolePermList = new ArrayList<>();
		rolePermList.add("com.test.app|test1");
		mockJsonObjectPerm.put("roles", rolePermList);
		mockJsonObjectPerm2.put("type", "com.test.app.test_type2");
		mockJsonObjectPerm2.put("instance", "test_instance2");
		List<String> rolePermList2 = new ArrayList<>();
		rolePermList2.add("com.test.app|test1");
		rolePermList2.add("com.test.app|test2");
		rolePermList2.add("com.test.app|test6");
		rolePermList2.add("com.test.app.new|global_test");
		mockJsonObjectPerm2.put("action", "*");
		mockJsonObjectPerm2.put("roles", rolePermList2);
		mockJsonObjectPerm2.put("description", "test_name2");
		mockJsonObjectPerm3.put("type", "com.test.app.access");
		mockJsonObjectPerm3.put("instance", "test_instance3");
		mockJsonObjectPerm3.put("action", "*");
		mockJsonObjectPerm3.put("description", "test_name3");
		List<JSONObject> permsList = new ArrayList<>();
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
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction(null, "test_type|test_instance|*",
				"test_name", app.getId(), null);
		CentralV2RoleFunction centralV2RoleFunction2 = new CentralV2RoleFunction(null, "test_instance2", "test_name2",
				app.getId(), null);
		CentralV2RoleFunction centralV2RoleFunction3 = new CentralV2RoleFunction(null, "test_instance5", "test_name5",
				app.getId(), null);
		appFunctions.add(centralV2RoleFunction);
		appFunctions.add(centralV2RoleFunction2);
		appFunctions.add(centralV2RoleFunction3);
		Mockito.when(dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null)).thenReturn(appFunctions);
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
		EPRole getEPRole3 = new EPRole();
		getEPRole3.setName("test2");
		getEPRole3.setId(4l);
		getEPRole3.setActive(true);
		getCurrentRoleList.add(getEPRole);
		getCurrentRoleList.add(getEPRole2);
		getCurrentRoleList.add(getEPRole3);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
				.thenReturn(getCurrentRoleList);
		final Map<String, String> appSyncFuncsParams = new HashMap<>();
		appSyncFuncsParams.put("appId", String.valueOf(app.getId()));
		appSyncFuncsParams.put("functionCd", "");
		List<CentralV2RoleFunction> roleFunctionList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams, null))
				.thenReturn(roleFunctionList);
		String code = centralV2RoleFunction.getCode();
		appSyncFuncsParams.put("functionCd", code);
		CentralV2RoleFunction getCentralV2RoleFunction = new CentralV2RoleFunction(null, "test_type|test_instance|*",
				"test_name", app.getId(), null);
		roleFunctionList.add(getCentralV2RoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams, null))
				.thenReturn(roleFunctionList);
		List<LocalRole> localRoles = new ArrayList<>();
		LocalRole localRole = new LocalRole();
		localRole.setRoleId(2);
		localRole.setRolename("test1");
		LocalRole localRole2 = new LocalRole();
		localRole2.setRoleId(3);
		localRole2.setRolename("test3");
		localRoles.add(localRole);
		localRoles.add(localRole2);
		final Map<String, String> appRoleFuncParams = new HashMap<>();
		appRoleFuncParams.put("functionCd", "test_type2|test_instance2|*");
		appRoleFuncParams.put("appId", String.valueOf(app.getId()));
		Mockito.when(dataAccessService.executeNamedQuery("getCurrentAppRoleFunctions", appRoleFuncParams, null))
				.thenReturn(localRoles);
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app", app.getNameSpace())).thenReturn(true);
		Mockito.when(EcompPortalUtils.getFunctionCode("test_type2|test_instance2|*")).thenReturn("test_instance2");
		appSyncFuncsParams.put("functionCd", "test_instance2");
		List<CentralV2RoleFunction> roleFunctionList2 = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams, null))
				.thenReturn(roleFunctionList2);
		String code2 = "test_type2|test_instance2|*";
		appSyncFuncsParams.put("functionCd", code2);
		CentralV2RoleFunction getCentralV2RoleFunction2 = new CentralV2RoleFunction(null, "test_type2|test_instance2|*",
				"test_name2", app.getId(), null);
		roleFunctionList2.add(getCentralV2RoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appSyncFuncsParams, null))
				.thenReturn(roleFunctionList2);
		final Map<String, Long> params3 = new HashMap<>();
		params3.put("appId", app.getId());
		params3.put("roleId", getEPRole2.getId());
		List<EPAppRoleFunction> currentGlobalRoleFunctionsList = new ArrayList<>();
		EPAppRoleFunction addGlobalRoleFunction = new EPAppRoleFunction();
		addGlobalRoleFunction.setAppId(app.getId());
		addGlobalRoleFunction.setCode("test_type|test_instance|*");
		addGlobalRoleFunction.setRoleId(1111l);
		currentGlobalRoleFunctionsList.add(addGlobalRoleFunction);
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", params3, null))
				.thenReturn(currentGlobalRoleFunctionsList);
		final Map<String, String> roleParams = new HashMap<>();
		roleParams.put(APP_ROLE_NAME_PARAM, "test6");
		roleParams.put("appId", String.valueOf(app.getId()));
		List<EPRole> roleCreated = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams, null))
				.thenReturn(roleCreated);
		final Map<String, String> getRoleByNameParams = new HashMap<>();
		getRoleByNameParams.put(APP_ROLE_NAME_PARAM, "test6");
		getRoleByNameParams.put("appId", String.valueOf(app.getId()));
		EPRole getNewEPRole = new EPRole();
		getNewEPRole.setName("test6");
		getNewEPRole.setId(8l);
		getNewEPRole.setActive(true);
		List<EPRole> roleCreated2 = new ArrayList<>();
		roleCreated2.add(getNewEPRole);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM,
				getRoleByNameParams, null)).thenReturn(roleCreated2);
		EPRole getNewEPRoleFinal = new EPRole();
		getNewEPRoleFinal.setName("test6");
		getNewEPRoleFinal.setId(8l);
		getNewEPRoleFinal.setActive(true);
		getNewEPRoleFinal.setAppRoleId(8l);
		final Map<String, String> getRoleByNameParams2 = new HashMap<>();
		getRoleByNameParams2.put(APP_ROLE_NAME_PARAM, "test6");
		getRoleByNameParams2.put("appId", String.valueOf(app.getId()));
		List<EPRole> roleCreated3 = new ArrayList<>();
		roleCreated3.add(getNewEPRole);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM,
				getRoleByNameParams2, null)).thenReturn(roleCreated3);
		List<EPRole> roleInfo = new ArrayList<>();
		roleInfo.add(getNewEPRoleFinal);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", getNewEPRoleFinal.getId());
		getPartnerAppRoleParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(roleInfo);
		externalAccessRolesServiceImpl.syncRoleFunctionFromExternalAccessSystem(app);
	}

	@Test
	public void syncApplicationRolesWithEcompDBTest() {
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
		List<JSONObject> permsList = new ArrayList<>();
		permsList.add(mockJsonObjectPerm1);
		permsList.add(mockJsonObjectPerm2);
		mockJsonObjectRole.put("name", "com.test.app.Test");
		mockJsonObjectRole.put("perms", permsList);
		mockJsonObjectRole.put("description",
				"Test role");
		mockJsonObjectRole2.put("name", "com.test.app.Test2_role");
		List<JSONObject> permsList2 = new ArrayList<>();
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
		getEPRole.setName("Test role");
		getEPRole.setId(2l);
		getEPRole.setAppId(app.getId());
		getEPRole.setAppRoleId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test2_role");
		getEPRole2.setId(3l);
		getEPRole2.setAppId(app.getId());
		getEPRole2.setAppRoleId(3l);
		getEPRole2.setActive(true);
		EPRole getEPRole3 = new EPRole();
		getEPRole3.setName("Test3_role");
		getEPRole3.setId(3l);
		getEPRole3.setAppId(app.getId());
		getEPRole3.setAppRoleId(3l);
		getEPRole3.setActive(true);
		getCurrentRoleList.add(getEPRole);
		getCurrentRoleList.add(getEPRole2);
		getCurrentRoleList.add(getEPRole3);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
				.thenReturn(getCurrentRoleList);
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app.test_type", app.getNameSpace()))
				.thenReturn(true);
		Mockito.when(EcompPortalUtils.checkNameSpaceMatching("com.test.app.test_type2", app.getNameSpace()))
				.thenReturn(true);
		List<EPAppRoleFunction> appRoleFunctions = new ArrayList<>();
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setAppId(app.getId());
		epAppRoleFunction.setCode("test_type|test_instance|*");
		epAppRoleFunction.setRoleId(getEPRole.getId());
		appRoleFunctions.add(epAppRoleFunction);
		final Map<String, Long> appRoleFuncsParams = new HashMap<>();
		appRoleFuncsParams.put("appId", app.getId());
		appRoleFuncsParams.put("roleId", Long.valueOf(getEPRole.getId()));
		Mockito.when(
				dataAccessService.executeNamedQuery("getAppRoleFunctionOnRoleIdandAppId", appRoleFuncsParams, null))
				.thenReturn(appRoleFunctions);
		List<CentralV2RoleFunction> getV2RoleFunction = new ArrayList<>();
		final Map<String, String> appFuncsParams = new HashMap<>();
		appFuncsParams.put("appId", String.valueOf(app.getId()));
		appFuncsParams.put("functionCd", "test_instance2");
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null))
				.thenReturn(getV2RoleFunction);
		appFuncsParams.put("functionCd", "test_type2|test_instance2|*");
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setAppId(app.getId());
		centralV2RoleFunction.setCode("test_type2|test_instance2|*");
		centralV2RoleFunction.setName("test_name2");
		getV2RoleFunction.add(centralV2RoleFunction);
		final Map<String, String> extRoleParams = new HashMap<>();
		List<EPRole> roleListDeactivate = new ArrayList<>();
		extRoleParams.put(APP_ROLE_NAME_PARAM, "Test3_role");
		extRoleParams.put(APP_ID, app.getId().toString());
		EPRole getEPRoleDeactivate = new EPRole();
		getEPRoleDeactivate.setName("Test3_role");
		getEPRoleDeactivate.setId(3l);
		getEPRoleDeactivate.setAppId(app.getId());
		getEPRoleDeactivate.setAppRoleId(3l);
		roleListDeactivate.add(getEPRoleDeactivate);
		Mockito.when(
				dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, extRoleParams, null))
				.thenReturn(roleListDeactivate);
		Mockito.when(dataAccessService.executeNamedQuery("getAppFunctionOnCodeAndAppId", appFuncsParams, null))
				.thenReturn(getV2RoleFunction);
		List<EPRole> updateLocalFromExtAuth = new ArrayList<>();
		updateLocalFromExtAuth.add(getEPRole);
		final Map<String, String> roleParams = new HashMap<>();
		roleParams.put(APP_ROLE_NAME_PARAM, getEPRole.getName());
		roleParams.put(APP_ID, app.getId().toString());
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, roleParams, null))
				.thenReturn(updateLocalFromExtAuth);
		roleParams.put(APP_ROLE_NAME_PARAM, getEPRole2.getName());
		List<EPRole> updateLocalFromExtAuth2 = new ArrayList<>();
		updateLocalFromExtAuth.add(getEPRole);
		Mockito.when(dataAccessService.executeNamedQuery("getRoletoUpdateInExternalAuthSystem", roleParams, null))
				.thenReturn(updateLocalFromExtAuth2);
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
		Mockito.when(
				dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, globalRoleParams, null))
				.thenReturn(addNewRoleList);
		final Map<String, String> params = new HashMap<>();
		params.put(APP_ROLE_NAME_PARAM, "Test2");
		params.put(APP_ID, app.getId().toString());
		addRoleInLocal.setAppRoleId(4l);
		addNewRoleList.add(addRoleInLocal);
		Mockito.when(dataAccessService.executeNamedQuery(GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM, params, null))
				.thenReturn(addNewRoleList);
		externalAccessRolesServiceImpl.syncApplicationRolesWithEcompDB(app);
	}

	@Test
	public void deleteDependencyRoleRecord() throws Exception {
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
		JSONObject getUser = new JSONObject();
		getUser.put("name", "com.test.app.test1");
		ResponseEntity<String> getResponse = new ResponseEntity<>(getUser.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.deleteDependencyRoleRecord(2l,
				app.getUebKey(), user.getOrgUserId());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void deleteDependencyRoleRecordForPortal() throws Exception {
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
		JSONObject getUser = new JSONObject();
		getUser.put("name", "com.test.app.test1");
		ResponseEntity<String> getResponse = new ResponseEntity<>(getUser.toString(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		ResponseEntity<String> DelResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(DelResponse);
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(SqlQuery);
		ExternalRequestFieldsValidator actual = externalAccessRolesServiceImpl.deleteDependencyRoleRecord(2l,
				app.getUebKey(), user.getOrgUserId());
		ExternalRequestFieldsValidator expected = new ExternalRequestFieldsValidator(true, "");
		assertEquals(expected, actual);
	}

	@Test
	public void bulkUploadFunctionsTest() throws Exception {
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
		JSONObject perm = new JSONObject();
		JSONObject permList = new JSONObject();
		perm.put("type", app.getNameSpace() + ".access");
		perm.put("instance", "type_instance");
		perm.put("action", "*");
		List<JSONObject> addPerms = new ArrayList<>();
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
	public void bulkUploadRolesTest() throws Exception {
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
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
				.thenReturn(epRoleList);
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
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null))
				.thenReturn(epRoleList1);
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
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams2, null))
				.thenReturn(epRoleList2);
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadRoles(app.getUebKey());
		Integer expected = 3;
		assertEquals(expected, actual);
	}

	@Test
	public void bulkUploadUserRolesTest() throws Exception {
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
		Mockito.when(dataAccessService.executeNamedQuery("getBulkUserRoles", appParams, null))
				.thenReturn(userRolesList);
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadUserRoles(app.getUebKey());
		Integer expected = 2;
		assertEquals(expected, actual);
	}

	@Test
	public void getUserRolesTest() throws Exception {
		EPApp app = mockApp();
		EPUser user = mockUser.mockEPUser();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", user.getOrgUserId());
		List<EPUser> userList = new ArrayList<>();
		Set<EPUserApp> userAppSet = user.getEPUserApps();
		EPUserApp epUserApp = new EPUserApp();
		EPRole epRole = new EPRole();
		epRole.setName("test");
		epRole.setId(1l);
		epRole.setActive(true);
		epRole.setAppRoleId(11l);
		epUserApp.setApp(app);
		epUserApp.setUserId(user.getId());
		epUserApp.setRole(epRole);
		userAppSet.add(epUserApp);
		user.setUserApps(userAppSet);
		userList.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null))
				.thenReturn(userList);
		CentralUser actual = externalAccessRolesServiceImpl.getUserRoles(user.getOrgUserId(), app.getUebKey());
		assertNotNull(actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getActiveRolesTest() throws Exception {
		EPApp app = mockApp();
		app.setId(1l);
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> epRoles = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test2");
		getEPRole2.setId(3l);
		getEPRole2.setActive(true);
		epRoles.add(getEPRole);
		epRoles.add(getEPRole2);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion active_ynCrt = Restrictions.eq("active", Boolean.TRUE);
		Criterion appIdCrt = Restrictions.isNull("appId");
		Criterion andCrit = Restrictions.and(active_ynCrt, appIdCrt);
		restrictionsList.add(andCrit);
		Mockito.when((List<EPRole>) dataAccessService.getList(EPRole.class, null, restrictionsList, null))
				.thenReturn(epRoles);
		final Map<String, Long> params = new HashMap<>();
		params.put("roleId", getEPRole.getId());
		params.put(APP_ID, app.getId());
		List<CentralV2RoleFunction> cenRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction("test_type|test_instance|*",
				"test_name");
		CentralV2RoleFunction centralV2RoleFunction2 = new CentralV2RoleFunction("test_instance2", "test_name2");
		cenRoleFuncList.add(centralV2RoleFunction);
		cenRoleFuncList.add(centralV2RoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionList", params, null))
				.thenReturn(cenRoleFuncList);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("roleId", getEPRole2.getId());
		params2.put(APP_ID, app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getAppRoleFunctionList", params2, null))
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
		Map<String, Long> params3 = new HashMap<>();
		params3.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getGlobalRoleWithApplicationRoleFunctions", params3, null))
				.thenReturn(mockGlobalRoles);
		Mockito.when(EcompPortalUtils.getFunctionCode(centralV2RoleFunction.getCode())).thenReturn("test_instance");
		Mockito.when(EcompPortalUtils.getFunctionCode(centralV2RoleFunction2.getCode())).thenReturn("test_instance2");
		List<CentralV2Role> actual = externalAccessRolesServiceImpl.getActiveRoles(app.getUebKey());
		assertEquals(actual.size(), 3);
	}

	@Test
	public void bulkUploadRolesFunctionsTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> applicationRoles = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test2");
		getEPRole2.setId(3l);
		getEPRole2.setActive(true);
		applicationRoles.add(getEPRole);
		applicationRoles.add(getEPRole2);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
				.thenReturn(applicationRoles);
		final Map<String, Long> params = new HashMap<>();
		params.put("roleId", getEPRole.getId());
		List<BulkUploadRoleFunction> appRoleFunc = new ArrayList<>();
		BulkUploadRoleFunction bulkUploadRoleFunction = new BulkUploadRoleFunction();
		bulkUploadRoleFunction.setFunctionCd("testcode");
		bulkUploadRoleFunction.setFunctionName("test_name");
		BulkUploadRoleFunction bulkUploadRoleFunction2 = new BulkUploadRoleFunction();
		bulkUploadRoleFunction2.setFunctionCd("menu_testcode2");
		bulkUploadRoleFunction2.setFunctionName("test_name2");
		appRoleFunc.add(bulkUploadRoleFunction);
		appRoleFunc.add(bulkUploadRoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("uploadAllRoleFunctions", params, null))
				.thenReturn(appRoleFunc);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("roleId", getEPRole2.getId());
		List<BulkUploadRoleFunction> appRoleFunc2 = new ArrayList<>();
		appRoleFunc2.add(bulkUploadRoleFunction);
		appRoleFunc2.add(bulkUploadRoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("uploadAllRoleFunctions", params2, null))
				.thenReturn(appRoleFunc2);
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadRolesFunctions(app.getUebKey());
		Integer expected = 4;
		assertEquals(actual, expected);
	}

	@Test
	public void bulkUploadPartnerRoleFunctionsTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		List<EPRole> applicationRoles = new ArrayList<>();
		EPRole getEPRole = new EPRole();
		getEPRole.setName("Test");
		getEPRole.setId(2l);
		getEPRole.setActive(true);
		EPRole getEPRole2 = new EPRole();
		getEPRole2.setName("Test2");
		getEPRole2.setId(3l);
		getEPRole2.setActive(true);
		applicationRoles.add(getEPRole);
		applicationRoles.add(getEPRole2);
		final Map<String, Long> paramsRoles = new HashMap<>();
		paramsRoles.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", paramsRoles, null))
				.thenReturn(applicationRoles);
		final Map<String, Long> params = new HashMap<>();
		params.put("roleId", getEPRole.getId());
		List<BulkUploadRoleFunction> appRoleFunc = new ArrayList<>();
		BulkUploadRoleFunction bulkUploadRoleFunction = new BulkUploadRoleFunction();
		bulkUploadRoleFunction.setFunctionCd("testcode");
		bulkUploadRoleFunction.setFunctionName("test_name");
		BulkUploadRoleFunction bulkUploadRoleFunction2 = new BulkUploadRoleFunction();
		bulkUploadRoleFunction2.setFunctionCd("menu_testcode2");
		bulkUploadRoleFunction2.setFunctionName("test_name2");
		appRoleFunc.add(bulkUploadRoleFunction);
		appRoleFunc.add(bulkUploadRoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("uploadPartnerRoleFunctions", params, null))
				.thenReturn(appRoleFunc);
		final Map<String, Long> params2 = new HashMap<>();
		params2.put("roleId", getEPRole2.getId());
		List<BulkUploadRoleFunction> appRoleFunc2 = new ArrayList<>();
		appRoleFunc2.add(bulkUploadRoleFunction);
		appRoleFunc2.add(bulkUploadRoleFunction2);
		Mockito.when(dataAccessService.executeNamedQuery("uploadPartnerRoleFunctions", params2, null))
				.thenReturn(appRoleFunc2);
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		// GlobalRoleFunctionsTest
		final Map<String, Long> partnerAppParams = new HashMap<>();
		partnerAppParams.put("appId", app.getId());
		Mockito.when(epAppCommonServiceImpl.getApp(1l)).thenReturn(app);
		List<GlobalRoleWithApplicationRoleFunction> globalRoleFuncsList = new ArrayList<>();
		GlobalRoleWithApplicationRoleFunction globalRoleFunc = new GlobalRoleWithApplicationRoleFunction();
		globalRoleFunc.setActive(true);
		globalRoleFunc.setAppId(10l);
		globalRoleFunc.setFunctionCd("test|test|test");
		globalRoleFunc.setRoleId(2l);
		globalRoleFunc.setFunctionName("test");
		globalRoleFunc.setRoleName("global_test");
		globalRoleFuncsList.add(globalRoleFunc);
		Mockito.when(dataAccessService.executeNamedQuery("getBulkUploadPartnerGlobalRoleFunctions", partnerAppParams, null))
				.thenReturn(globalRoleFuncsList);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		Integer actual = externalAccessRolesServiceImpl.bulkUploadPartnerRoleFunctions(app.getUebKey());
		Integer expected = 5;
		assertEquals(expected, actual);
	}

	@Test
	public void getMenuFunctionsListTest() throws Exception {
		EPApp app = mockApp();
		List<EPApp> appList = new ArrayList<>();
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appList.add(app);
		appUebkeyParams.put("appKey", app.getUebKey());
		Mockito.when(dataAccessService.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put(APP_ID, app.getId());
		List<String> expected = new ArrayList<>();
		expected.add("test_menu1");
		expected.add("test_menu2");
		Mockito.when(dataAccessService.executeNamedQuery("getMenuFunctions", appParams, null)).thenReturn(expected);
		List<String> actual = externalAccessRolesServiceImpl.getMenuFunctionsList(app.getUebKey());
		assertEquals(expected, actual);
	}

	@Test
	public void getCentralizedAppsOfUserTest() {
		EPUser user = mockUser.mockEPUser();
		Map<String, String> params = new HashMap<>();
		params.put("userId", user.getOrgUserId());
		List<CentralizedApp> expected = new ArrayList<>();
		CentralizedApp centralizedApp = new CentralizedApp();
		centralizedApp.setAppId(2);
		centralizedApp.setAppName("testapp1");
		expected.add(centralizedApp);
		Mockito.when(dataAccessService.executeNamedQuery("getCentralizedAppsOfUser", params, null))
				.thenReturn(expected);
		List<CentralizedApp> actual = externalAccessRolesServiceImpl.getCentralizedAppsOfUser(user.getOrgUserId());
		assertEquals(expected, actual);
	}

	@Test
	public void ConvertCentralRoleToRoleTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Role role = new Role();
		role.setName("Test");
		role.setId(3l);
		role.setActive(true);
		SortedSet<RoleFunction> roleFuncSet = new TreeSet<>();
		RoleFunction roleFunc = new RoleFunction();
		roleFunc.setName("Test Name");
		roleFunc.setCode("testcode");
		RoleFunction roleFunc2 = new RoleFunction();
		roleFunc2.setName("Test Name3");
		roleFunc2.setCode("menu_testcode2");
		roleFuncSet.add(roleFunc);
		roleFuncSet.add(roleFunc2);
		role.setRoleFunctions(roleFuncSet);
		String roleInfo = mapper.writeValueAsString(role);
		Role actual = externalAccessRolesServiceImpl.ConvertCentralRoleToRole(roleInfo);
		assertNotNull(actual);
	}

	@Test
	public void convertV2CentralRoleListToOldVerisonCentralRoleListTest() {
		List<CentralV2Role> v2CenRoleList = new ArrayList<>();
		CentralV2Role cenV2Role = new CentralV2Role.CentralV2RoleBuilder().setId(2l).setName("test1").createCentralV2Role();
		CentralV2RoleFunction CentralV2Role = new CentralV2RoleFunction("testcode", "test_name");
		SortedSet<CentralV2RoleFunction> setV2RoleFuncs = new TreeSet<>();
		setV2RoleFuncs.add(CentralV2Role);
		cenV2Role.setRoleFunctions(setV2RoleFuncs);
		v2CenRoleList.add(cenV2Role);
		List<CentralRole> actual = externalAccessRolesServiceImpl.convertV2CentralRoleListToOldVerisonCentralRoleList(v2CenRoleList);
		assertEquals(1, actual.size());
	}
	
	@Test
	public void bulkUploadRoleFuncTest() throws Exception {
		EPApp app = mockApp();
		UploadRoleFunctionExtSystem  data = new UploadRoleFunctionExtSystem();
		data.setRoleName("test");
		data.setType("test");
		data.setInstance("test");
		data.setIsGlobalRolePartnerFunc(false);
		data.setAction("test");
		data.setName("test");
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		externalAccessRolesServiceImpl.bulkUploadRoleFunc(data, app);
	}
	
	@Test
	public void bulkUploadGlobalRoleFuncTest() throws Exception {
		EPApp app = mockApp();
		EPApp portalApp = mockApp();
		portalApp.setId(1L);
		Mockito.when(epAppCommonServiceImpl.getApp(PortalConstants.PORTAL_APP_ID)).thenReturn(portalApp);
		UploadRoleFunctionExtSystem  data = new UploadRoleFunctionExtSystem();
		data.setRoleName("test");
		data.setType("test");
		data.setInstance("test");
		data.setIsGlobalRolePartnerFunc(true);
		data.setAction("test");
		data.setName("test");
		ResponseEntity<String> getResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		externalAccessRolesServiceImpl.bulkUploadRoleFunc(data, app);
	}
	
	@Test(expected = HttpClientErrorException.class)
	public void bulkUploadRoleFuncExcpetionTest() throws Exception {
		UploadRoleFunctionExtSystem  data = new UploadRoleFunctionExtSystem();
		data.setRoleName("test");
		data.setType("test");
		data.setInstance("test");
		data.setAction("test");
		data.setName("test");
		data.setInstance("test");
		EPApp app = mockApp();
		Mockito.doThrow(new HttpClientErrorException(HttpStatus.CONFLICT)).when(template).exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class));
		externalAccessRolesServiceImpl.bulkUploadRoleFunc(data, app);
	}
	
	@Test
	public void syncApplicationUserRolesFromExtAuthSystemTest() throws Exception {
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(new HttpHeaders());
		Mockito.when(EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)).thenReturn(true);
		JSONObject mockJsonObjectRole = new JSONObject();
		JSONObject mockJsonObjectRole2 = new JSONObject();
		JSONObject mockJsonObjectRole3 = new JSONObject();
		mockJsonObjectRole.put("name", "com.test.app.test_role");
		mockJsonObjectRole2.put("name", "com.test.app2.test_role");
		mockJsonObjectRole3.put("name", "com.test.app2.Account_Administrator");
		List<JSONObject> userRolesList = new ArrayList<>();
		JSONObject mockJsonObjectFinalUserRole = new JSONObject();
		userRolesList.add(mockJsonObjectRole);
		userRolesList.add(mockJsonObjectRole2);
		userRolesList.add(mockJsonObjectRole3);
		mockJsonObjectFinalUserRole.put("role", userRolesList);
		ResponseEntity<String> getResponse = new ResponseEntity<>(mockJsonObjectFinalUserRole.toString(),HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(getResponse);
		List<EPUser> users = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		user.setOrgUserId("test");
		users.add(user);
		List<EPApp> apps = new ArrayList<>();
		EPApp app = mockApp();
		app.setNameSpace("com.test.app");
		app.setId(1l);
		EPApp app2 = mockApp();
		app2.setNameSpace("com.test.app2");
		app2.setId(2l);
		apps.add(app);
		apps.add(app2);
		Mockito.when(dataAccessService
		.executeNamedQuery("getCentralizedApps", null, null)).thenReturn(apps);
		HashMap<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", "test");
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", userParams, null)).thenReturn(users);
		List<CentralizedAppRoles> currentUserAppRoles = new ArrayList<>();
		CentralizedAppRoles currentUserAppRole = new CentralizedAppRoles();
		currentUserAppRole.setAppId(1l);
		currentUserAppRole.setAppNameSpace("com.test.app");
		currentUserAppRole.setRoleId(2l);
		currentUserAppRole.setRoleName("test role");
		currentUserAppRoles.add(currentUserAppRole);
		HashMap<String, String> userParams2 = new HashMap<>();
		userParams2.put("orgUserId", user.getOrgUserId());
		Mockito.when(dataAccessService
		.executeNamedQuery("getUserCentralizedAppRoles", userParams2, null)).thenReturn(currentUserAppRoles);
		List<CentralizedAppRoles> centralizedAppRoles = new ArrayList<>();
		CentralizedAppRoles centralizedAppRole = new CentralizedAppRoles();
		centralizedAppRole.setAppId(1l);
		centralizedAppRole.setAppNameSpace("com.test.app");
		centralizedAppRole.setRoleId(2l);
		centralizedAppRole.setRoleName("test role");
		CentralizedAppRoles centralizedAppRole2 = new CentralizedAppRoles();
		centralizedAppRole2.setAppId(1l);
		centralizedAppRole2.setAppNameSpace("com.test.app2");
		centralizedAppRole2.setRoleId(2l);
		centralizedAppRole2.setRoleName("test role");
		centralizedAppRoles.add(centralizedAppRole);
		centralizedAppRoles.add(centralizedAppRole2);
		Mockito.when(dataAccessService
		.executeNamedQuery("getAllCentralizedAppsRoles", null, null)).thenReturn(centralizedAppRoles);
		externalAccessRolesServiceImpl.syncApplicationUserRolesFromExtAuthSystem(user.getOrgUserId());
	}
	
	@Test
	public void updateAppRoleDescriptionTest() {
		EPApp app = mockUpdateAppRoleDescription();
		ResponseEntity<String> postResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(postResponse);
		Integer actual = externalAccessRolesServiceImpl.updateAppRoleDescription(app.getUebKey());
		Integer expected = 1;
		assertEquals(expected, actual);
	}
	
	@Test
	public void updateAppRoleDescriptionExceptionTest() {
		EPApp app = mockUpdateAppRoleDescription();
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE));
		Integer actual = externalAccessRolesServiceImpl.updateAppRoleDescription(app.getUebKey());
		Integer expected = 0;
		assertEquals(expected, actual);
	}
	
	@Test
	public void updateAppRoleDescriptionExceptionTest2() throws Exception {
		EPApp app = mockUpdateAppRoleDescription();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenThrow(new NullPointerException());
		Integer actual = externalAccessRolesServiceImpl.updateAppRoleDescription(app.getUebKey());
		Integer expected = 0;
		assertEquals(expected, actual);
	}

	private EPApp mockUpdateAppRoleDescription() {
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
		epRoleList.add(getEPRole);
		final Map<String, Long> appParams = new HashMap<>();
		appParams.put("appId", app.getId());
		Mockito.when(dataAccessService.executeNamedQuery("getPartnerAppRolesList", appParams, null))
		.thenReturn(epRoleList);
		return app;
	}
	
}
