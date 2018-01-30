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
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPAppRoleFunction;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.exceptions.InactiveApplicationException;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.EcompUserRoles;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.domain.Role;
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
	DataAccessService dataAccessService1 = new DataAccessServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@InjectMocks
	ExternalAccessRolesServiceImpl externalAccessRolesServiceImpl = new ExternalAccessRolesServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

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
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test
	public void getAppRolesIfAppIsPortalTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 1);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	@Test
	public void getAppRolesTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 10);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	// @SuppressWarnings("null")
	// @Test(expected = java.lang.Exception.class)
	// public void getAppRolesExceptionTest() throws Exception{
	// List<EPRole> applicationRoles = new ArrayList<>();
	// DataAccessService dataAccessService = null ;
	// Mockito.when(dataAccessService.getList(EPRole.class, "where app_id = 10",
	// null, null)).thenThrow(nullPointerException);
	// List<EPRole> expectedApplicationRoles =
	// externalAccessRolesServiceImpl.getAppRoles((long) 10);
	// assertEquals(expectedApplicationRoles,applicationRoles);
	// }

	@Test
	public void getAppExceptionTest() throws Exception {
		List<EPApp> app = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(app);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
		assertEquals(app, expectedapp);
	}

	@Test(expected = InactiveApplicationException.class)
	public void getAppErrorTest() throws Exception {
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		appList.add(app);
		final Map<String, String> appUebkeyParams = new HashMap<>();
		appUebkeyParams.put("appKey", "test-ueb-key");
		Mockito.when(dataAccessService1.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
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
		Mockito.when(dataAccessService1.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
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
		Mockito.when(dataAccessService1.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", role.getId());
		getPartnerAppRoleParams.put("appId", app.getId());				
		Mockito.when(dataAccessService1.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null)).thenReturn(roleList);
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
		Mockito.when(dataAccessService1.executeNamedQuery("getMyAppDetailsByUebKey", appUebkeyParams, null))
				.thenReturn(appList);
		final Map<String, Long> getPartnerAppRoleParams = new HashMap<>();
		getPartnerAppRoleParams.put("appRoleId", role.getId());
		getPartnerAppRoleParams.put("appId", app.getId());				
		Mockito.when(dataAccessService1.executeNamedQuery("getPartnerAppRoleByRoleId", getPartnerAppRoleParams, null)).thenReturn(roleList);
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
		Mockito.when(dataAccessService1.executeNamedQuery("getRoleFunction", params, null))
				.thenReturn(centralRoleFunctionList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);

		Mockito.doNothing().when(dataAccessService1).deleteDomainObjects(EPAppRoleFunction.class,
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
		Mockito.when(dataAccessService1.executeNamedQuery("getRoleFunction", params, null))
				.thenReturn(centralRoleFunctionList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenThrow(httpClientErrorException);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);

		Mockito.doNothing().when(dataAccessService1).deleteDomainObjects(EPAppRoleFunction.class,
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
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params, null))
				.thenThrow(nullPointerException);
		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void getUserTest() {
		List<EPUser> userList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		userList.add(user);
		final Map<String, String> userParams = new HashMap<>();
		userParams.put("org_user_id", "guestT");
		Mockito.when(dataAccessService1.executeNamedQuery("getEPUserByOrgUserId", userParams, null))
				.thenReturn(userList);
		List<EPUser> expectedUserList = externalAccessRolesServiceImpl.getUser("guestT");
		assertEquals(expectedUserList, userList);
	}
    
	
	public void saveCentralRoleFunctionTest() throws Exception {
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setCode("menu_test");
		EPApp app = mockApp();
		app.setId((long) 1);
		final Map<String, String> params = new HashMap<>();
		params.put("functionCd", "menu_test");
		params.put("appId", String.valueOf(1));
		PowerMockito.mockStatic(EcompPortalUtils.class);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<CentralV2RoleFunction> appRoleFunc  = new ArrayList<>();
		appRoleFunc.add(centralV2RoleFunction);
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params,
				null)).thenReturn(appRoleFunc);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
	}
	
//	@Test
//	public void getAllAppUsersIfAppIsPortalTest() throws Exception
//	{
//		List<EPApp> expectedapps =  new ArrayList<>();
//		EPApp app = new EPApp();
//		app.setEnabled(true);
//		app.setId((long) 1);
//		expectedapps.add(app);
//		List<EPRole> applicationRoles = new ArrayList<>();
//		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
//		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null)).thenReturn(expectedapps);
//		externalAccessRolesServiceImpl.getAllAppUsers(uebKey);
//	}
	
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getAllAppUsersTest() throws Exception
	{
		List<EPApp> expectedapps =  new ArrayList<>();
		EPApp app = new EPApp();
		app.setEnabled(true);
		app.setId((long) 10);
		expectedapps.add(app);
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null)).thenReturn(expectedapps);
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
		
		Mockito.when(dataAccessService1.executeNamedQuery("ApplicationUserRoles", appParams, null)).thenReturn(userList);
		List<EcompUser> usersfinalList = externalAccessRolesServiceImpl.getAllAppUsers(uebKey);
		assertEquals(usersfinalList.get(0).getRoles().size(), 2);
	}
	
	@Test
	public void getGlobalRolesOfPortalTest()
	{
		Mockito.when(dataAccessService1.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenReturn(null);
		assertEquals(externalAccessRolesServiceImpl.getGlobalRolesOfPortal(), null);
	}
	
	@Test
	public void getGlobalRolesOfPortalExceptionTest()
	{
		List<EPRole> globalRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.executeNamedQuery("getGlobalRolesOfPortal", null, null)).thenThrow(nullPointerException);
		assertEquals(externalAccessRolesServiceImpl.getGlobalRolesOfPortal(), globalRoles);
	}
}
