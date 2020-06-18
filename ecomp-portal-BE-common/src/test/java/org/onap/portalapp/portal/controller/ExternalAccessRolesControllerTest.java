/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EpAppType;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.service.ExternalAccessRolesServiceImpl;
import org.onap.portalapp.portal.transport.*;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.UserService;
import org.onap.portalsdk.core.service.UserServiceCentalizedImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, PortalConstants.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class ExternalAccessRolesControllerTest {
	@Mock
	ExternalAccessRolesService externalAccessRolesService = new ExternalAccessRolesServiceImpl();
	@InjectMocks
	ExternalAccessRolesController externalAccessRolesController;
	@Mock
	UserService userservice = new UserServiceCentalizedImpl();
	@Mock
	AuditService auditService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	AuditLog auditLog = new AuditLog();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Failed");
	MockEPUser mockUser = new MockEPUser();
	String loginId = "guestT";
	String uebKey = "testUebKey";

	public EPApp mockApp() {
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
		app.setOpen(false);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(EpAppType.GUI);
		return app;
	}

	@Test
	public void getUserTest() throws Exception {
        CentralUser expectedCentralUser =
                new CentralUser.CentralUserBuilder().setId(null).setCreated(null).setModified(null).setCreatedId(null)
                        .setModifiedId(null).setRowNum(null).setOrgId(null).setManagerId(null).setFirstName(loginId)
                        .setMiddleInitial(loginId).setLastName(loginId).setPhone(loginId).setFax(loginId)
                        .setCellular(loginId).setEmail(loginId).setAddressId(null).setAlertMethodCd(loginId)
                        .setHrid(loginId).setOrgUserId(loginId).setOrgCode(loginId).setAddress1(loginId)
                        .setAddress2(loginId).setCity(loginId).setState(loginId).setZipCode(loginId).setCountry(loginId)
                        .setOrgManagerUserId(loginId).setLocationClli(loginId).setBusinessCountryCode(loginId)
                        .setBusinessCountryName(loginId).setBusinessUnit(loginId).setBusinessUnitName(loginId)
                        .setDepartment(loginId).setDepartmentName(loginId).setCompanyCode(loginId).setCompany(loginId)
                        .setZipCodeSuffix(loginId).setJobTitle(loginId).setCommandChain(loginId).setSiloStatus(loginId)
                        .setCostCenter(loginId).setFinancialLocCode(loginId).setLoginId(loginId).setLoginPwd(loginId)
                        .setLastLoginDate(null).setActive(false).setInternal(false).setSelectedProfileId(null)
                        .setTimeZoneId(null).setOnline(false).setChatId(loginId).setUserApps(null).createCentralUser();
		String loginId = "test";
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		Mockito.when(externalAccessRolesService.getUserRoles(loginId, mockedRequest.getHeader("uebkey")))
				.thenReturn(expectedCentralUser);
		CentralUser actualCentralUser = externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId);
		assertEquals(actualCentralUser.isActive(), expectedCentralUser.isActive());
	}

	@Test
	public void getUserExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getUserXSSTest() throws Exception {
		String loginId = "<script ~~~>alert(0%0)</script ~~~>";
		String expected = getXSSKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId);
		String actual = sw.getBuffer().toString().trim();
		assertEquals(expected, actual);
		}

	@Test
	public void getV2UserListTest() throws Exception {
		String expectedCentralUser = "test";
		String loginId = "test";
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		Mockito.when(externalAccessRolesService.getV2UserWithRoles(loginId, mockedRequest.getHeader("uebkey")))
				.thenReturn(expectedCentralUser);
		String actualString = externalAccessRolesController.getV2UserList(mockedRequest, mockedResponse, loginId);
		assertEquals(actualString, expectedCentralUser);
	}

	@Test(expected = NullPointerException.class)
	public void getV2UserListExceptionTest() throws Exception {
		String expectedCentralUser = null;
		String loginId = "test";
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = null;
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		Mockito.when(externalAccessRolesService.getV2UserWithRoles(loginId, mockedRequest.getHeader("uebkey")))
				.thenReturn(expectedCentralUser);
		String actualString = externalAccessRolesController.getV2UserList(mockedRequest, mockedResponse, loginId);
		assertEquals(actualString, expectedCentralUser);
	}

	@Test
	public void getRolesForAppCentralRoleTest() throws Exception {
		List<CentralRole> expectedCentralRoleList = new ArrayList<>();
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2Role> centralV2RoleList = new ArrayList<>();
		List<CentralRole> centralRoleList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey)))
				.thenReturn(centralV2RoleList);
		Mockito.when(externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(centralV2RoleList))
				.thenReturn(centralRoleList);
		List<CentralRole> actualCentralRoleList = externalAccessRolesController.getRolesForApp(mockedRequest,
				mockedResponse);
		assertEquals(actualCentralRoleList.size(), expectedCentralRoleList.size());
	}

	@Test(expected = NullPointerException.class)
	public void getRolesForAppCentralRoleExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2Role> centralV2RoleList = new ArrayList<>();
		List<CentralRole> centralRoleList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = null;
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey)))
				.thenReturn(centralV2RoleList);
		Mockito.when(externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(centralV2RoleList))
				.thenReturn(centralRoleList);
		List<CentralRole> actualCentralRoleList = externalAccessRolesController.getRolesForApp(mockedRequest,
				mockedResponse);
		assertEquals(null, actualCentralRoleList);
	}

	@Test
	public void getV2RolesForAppTest() throws Exception {
		List<CentralRole> expectedCentralRoleList = new ArrayList<>();
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2Role> centralV2Role = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey)))
				.thenReturn(centralV2Role);
		List<CentralV2Role> actualCentralV2Role = externalAccessRolesController.getV2RolesForApp(mockedRequest,
				mockedResponse);
		assertEquals(actualCentralV2Role.size(), expectedCentralRoleList.size());
	}

	@Test(expected = NullPointerException.class)
	public void getV2RolesForAppExceptionTest() throws Exception {
		List<CentralRole> expectedCentralRoleList = new ArrayList<>();
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2Role> centralV2Role = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = null;
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey)))
				.thenReturn(centralV2Role);
		List<CentralV2Role> actualCentralV2Role = externalAccessRolesController.getV2RolesForApp(mockedRequest,
				mockedResponse);
		assertEquals(actualCentralV2Role.size(), expectedCentralRoleList.size());
	}

	@Test(expected = NullPointerException.class)
	public void getRolesForAppTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2Role> answer = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey))).thenReturn(answer);
		assertEquals(externalAccessRolesController.getRolesForApp(mockedRequest, mockedResponse), null);
	}

	@Test(expected = NullPointerException.class)
	public void getRolesForAppExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		externalAccessRolesController.getRolesForApp(mockedRequest, mockedResponse);
		List<CentralV2Role> role = externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey));
		assertEquals(null, role);
	}

	@Test
	public void getRoleFunctionsListTest() throws Exception {
		List<CentralRole> expectedCentralRoleList = new ArrayList<>();
		List<CentralRoleFunction> roleFuncList = new ArrayList<>();
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2RoleFunction> centralV2RoleFunction = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRoleFuncList(mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2RoleFunction);
		Mockito.when(externalAccessRolesService.convertCentralRoleFunctionToRoleFunctionObject(centralV2RoleFunction))
				.thenReturn(roleFuncList);
		List<CentralRoleFunction> actualCentralRoleFunction = externalAccessRolesController
				.getRoleFunctionsList(mockedRequest, mockedResponse);
		assertEquals(actualCentralRoleFunction.size(), expectedCentralRoleList.size());
	}

	@Test
	public void getRoleFunctionsListExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getRoleFunctionsList(mockedRequest, mockedResponse);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getV2RoleFunctionsListTest() throws Exception {
		List<CentralV2RoleFunction> expectedCentralV2RoleFunctionList = new ArrayList<>();
		List<EPApp> applicationList = new ArrayList<>();
		List<CentralV2RoleFunction> centralV2RoleFunction = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRoleFuncList(mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2RoleFunction);
		List<CentralV2RoleFunction> actualCentralV2RoleFunctionList = externalAccessRolesController
				.getV2RoleFunctionsList(mockedRequest, mockedResponse);
		assertEquals(actualCentralV2RoleFunctionList.size(), expectedCentralV2RoleFunctionList.size());
	}

	@Test
	public void getV2RoleFunctionsListExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getV2RoleFunctionsList(mockedRequest, mockedResponse);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getRoleInfoValidationTest() throws Exception {
		CentralRole expectedCentralRole = null;
		List<EPApp> applicationList = new ArrayList<>();
		long roleId = 1;
		CentralV2Role centralV2Role = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2Role);
		CentralRole actualCentralRole = externalAccessRolesController.getRoleInfo(mockedRequest, mockedResponse,
				roleId);
		assertEquals(actualCentralRole, expectedCentralRole);
	}

	@Test
	public void getRoleInfoTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		CentralV2Role answer = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		long roleId = 1;
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader(uebKey)))
				.thenReturn(answer);
		externalAccessRolesController.getRoleInfo(mockedRequest, mockedResponse, roleId);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getRoleInfoExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		long roleId = 1;
		assertNull(externalAccessRolesController.getRoleInfo(mockedRequest, mockedResponse, roleId));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getV2RoleInfoValidationTest() throws Exception {
		CentralV2Role expectedCentralRole = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		expectedCentralRole.setActive(false);
		List<EPApp> applicationList = new ArrayList<>();
		long roleId = 1;
		CentralV2Role centralV2Role = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2Role);
		CentralV2Role actualCentralRole = externalAccessRolesController.getV2RoleInfo(mockedRequest, mockedResponse,
				roleId);
		assertEquals(actualCentralRole.getActive(), expectedCentralRole.getActive());
	}

	@Test
	public void getV2RoleInfoTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		CentralV2Role answer = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		long roleId = 1;
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader(uebKey)))
				.thenReturn(answer);
		externalAccessRolesController.getV2RoleInfo(mockedRequest, mockedResponse, roleId);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getV2RoleInfoExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		long roleId = 1;
		assertNull(externalAccessRolesController.getV2RoleInfo(mockedRequest, mockedResponse, roleId));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getV2RoleFunctionTest() throws Exception {
		CentralV2RoleFunction expectedCentralV2RoleFunction = new CentralV2RoleFunction();
		expectedCentralV2RoleFunction.setCode("test");
		List<EPApp> applicationList = new ArrayList<>();
		String code = "test";
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		centralV2RoleFunction.setCode("test");
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2RoleFunction);
		CentralV2RoleFunction actualCentralV2RoleFunction = externalAccessRolesController
				.getV2RoleFunction(mockedRequest, mockedResponse, code);
		assertEquals(actualCentralV2RoleFunction.getCode(), expectedCentralV2RoleFunction.getCode());
	}


	@Test
	public void getV2RoleFunctionNullCheckTest() throws Exception {
		CentralV2RoleFunction expectedCentralV2RoleFunction = new CentralV2RoleFunction();
		List<EPApp> applicationList = new ArrayList<>();
		String code = "test";
		CentralV2RoleFunction centralV2RoleFunction = null;
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader("uebkey")))
				.thenReturn(centralV2RoleFunction);
		CentralV2RoleFunction actualCentralV2RoleFunction = externalAccessRolesController
				.getV2RoleFunction(mockedRequest, mockedResponse, code);
		assertEquals(actualCentralV2RoleFunction.getAction(), expectedCentralV2RoleFunction.getAction());
	}

	@Test
	public void getV2RoleFunctionExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		String code = "test";
		assertNull(externalAccessRolesController.getV2RoleFunction(mockedRequest, mockedResponse, code));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getRoleFunctionTest() throws Exception {
		EPApp mockApp = mockApp();
		mockApp.setRolesInAAF(true);
		List<EPApp> mockAppList = new ArrayList<>();
		mockAppList.add(mockApp);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		CentralV2RoleFunction roleFunction1 = new CentralV2RoleFunction();
		CentralRoleFunction roleFunction2 = new CentralRoleFunction();
		roleFunction1.setCode("test2");
		String code = "test_menu";
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(mockAppList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(mockAppList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader("uebkey")))
				.thenReturn(roleFunction1);
		CentralRoleFunction returnedValue = externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse,
				code);
		assertEquals(returnedValue, roleFunction2);
		String result = sw.getBuffer().toString().trim();
		assertEquals("", result);
	}

	@Test
	public void getRoleFunctionExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		String code = "test_menu";
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertEquals(new CentralRoleFunction(),
				externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, code));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getRoleFunctionXSSTest() throws Exception {
		String expected = getXSSKeyJson();
		EPApp mockApp = mockApp();
		mockApp.setRolesInAAF(true);
		List<EPApp> mockAppList = new ArrayList<>();
		mockAppList.add(mockApp);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		CentralV2RoleFunction roleFunction1 = new CentralV2RoleFunction();
		CentralRoleFunction roleFunction2 = new CentralRoleFunction();
		roleFunction1.setCode("test2");
		String code = "<script>alert(‘XSS’)</script>";
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(mockAppList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(mockAppList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader("uebkey")))
			.thenReturn(roleFunction1);
		CentralRoleFunction returnedValue = externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse,
			code);
		assertEquals(returnedValue, roleFunction2);
		String result = sw.getBuffer().toString().trim();
		assertEquals(expected, result);
	}

	@Test
	public void saveRoleFunctionIfIsNotDeletedTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		String data = null;
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralV2RoleFunction, app)).thenReturn(false);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void saveRoleFunctionExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("argument \"content\" is null");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, null);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void saveRoleFunctionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		JSONObject roleFunc = new JSONObject();
		roleFunc.put("type", "test_type");
		roleFunc.put("code", "test_instance");
		roleFunc.put("action", "test_action");
		roleFunc.put("name", "test_name");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		CentralV2RoleFunction saveRoleFunc = mapper.readValue(roleFunc.toString(), CentralV2RoleFunction.class);
		saveRoleFunc.setAppId(app.getId());
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully saved!");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getRoleFunction("test_type|test_instance|test_action", app.getUebKey()))
				.thenReturn(null);
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(Matchers.any(CentralV2RoleFunction.class),
				Matchers.any(EPApp.class))).thenReturn(true);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader(Matchers.anyString())))
				.thenReturn(userList);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(Matchers.anyString())))
				.thenReturn(applicationList);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse,
				roleFunc.toString());
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void saveRoleFunctionXSSTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		JSONObject roleFunc = new JSONObject();
		roleFunc.put("type", "<script>alert(“XSS”)</script> ");
		roleFunc.put("code", "test_instance");
		roleFunc.put("action", "test_action");
		roleFunc.put("name", "test_name");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		CentralV2RoleFunction saveRoleFunc = mapper.readValue(roleFunc.toString(), CentralV2RoleFunction.class);
		saveRoleFunc.setAppId(app.getId());
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to roleFunc, not valid data.");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(applicationList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getRoleFunction("test_type|test_instance|test_action", app.getUebKey()))
			.thenReturn(null);
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(Matchers.any(CentralV2RoleFunction.class),
			Matchers.any(EPApp.class))).thenReturn(true);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader(Matchers.anyString())))
			.thenReturn(userList);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(Matchers.anyString())))
			.thenReturn(applicationList);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse,
			roleFunc.toString());
		assertEquals(expectedportalRestResponse, portalRestResponse);
	}

	@Test
	public void deleteRoleFunctionTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Deleted");
		expectedportalRestResponse.setResponse("Success");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		String code = "testNew";
		Mockito.when(mockedRequest.getHeader("LoginId")).thenReturn("guestT");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(code, app)).thenReturn(true);
		portalRestResponse = externalAccessRolesController.deleteRoleFunction(mockedRequest, mockedResponse, code);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void deleteRoleFunctionXSSTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to deleteRoleFunction, not valid data.");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		String code = "<script>alert(‘XSS’)</script>";
		Mockito.when(mockedRequest.getHeader("LoginId")).thenReturn("guestT");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(code, app)).thenReturn(true);
		portalRestResponse = externalAccessRolesController.deleteRoleFunction(mockedRequest, mockedResponse, code);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getActiveRolesTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey))).thenReturn(null);
		List<CentralRole> expectedCenRole = externalAccessRolesController.getActiveRoles(mockedRequest, mockedResponse);
		assertNull(expectedCenRole);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getActiveRolesValidationTest() throws Exception {
		List<CentralRole> expectedRolesList = null;
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		List<CentralV2Role> cenRoles = new ArrayList<>();
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader("uebkey"))).thenReturn(cenRoles);
		Mockito.when(externalAccessRolesService.convertV2CentralRoleListToOldVerisonCentralRoleList(cenRoles))
				.thenReturn(expectedRolesList);
		List<CentralRole> actualRolesList = externalAccessRolesController.getActiveRoles(mockedRequest, mockedResponse);
		assertEquals(actualRolesList, expectedRolesList);
	}

	@Test
	public void getActiveRolesExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getActiveRoles(mockedRequest, mockedResponse);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	/**
	 * It return JSON string which has error information
	 * 
	 * @return JSON String
	 * @throws JsonProcessingException
	 */
	private String getInvalidKeyJson() throws JsonProcessingException {
		final Map<String, String> uebkeyResponse = new HashMap<>();
		String reason = "";
		ObjectMapper mapper = new ObjectMapper();
		uebkeyResponse.put("error", "Invalid credentials!");
		reason = mapper.writeValueAsString(uebkeyResponse);
		return reason;
	}

	private String getXSSKeyJson() throws JsonProcessingException {
		final Map<String, String> uebkeyResponse = new HashMap<>();
		String reason = "";
		ObjectMapper mapper = new ObjectMapper();
		uebkeyResponse.put("error", "Data is not valid");
		reason = mapper.writeValueAsString(uebkeyResponse);
		return reason;
	}

	@Test
	public void deleteDependcyRoleRecordExceptionTest() {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Invalid credentials!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		long roleId = 123;
		portalRestResponse = externalAccessRolesController.deleteDependencyRoleRecord(mockedRequest, mockedResponse,
				roleId);
		assertEquals(expectedportalRestResponse, portalRestResponse);
	}

	@Test
	public void bulkUploadFunctionsTest() throws Exception {
		Integer result = 0;
		Mockito.when(externalAccessRolesService.bulkUploadFunctions(mockedRequest.getHeader(uebKey)))
				.thenReturn(result);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		portalRestResponse = externalAccessRolesController.bulkUploadFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadFunctionsExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadFunctions(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadFunctions");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRolesTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Mockito.when(externalAccessRolesService.bulkUploadRoles(mockedRequest.getHeader(uebKey))).thenReturn(result);
		portalRestResponse = externalAccessRolesController.bulkUploadRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRolesTestException() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadRoles(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadRoles");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRoleFunctionsTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Mockito.when(externalAccessRolesService.bulkUploadRolesFunctions(mockedRequest.getHeader(uebKey)))
				.thenReturn(result);
		portalRestResponse = externalAccessRolesController.bulkUploadRoleFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRoleFunctionsException() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadRolesFunctions(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadRoleFunctions");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadRoleFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadUserRolesTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Mockito.when(externalAccessRolesService.bulkUploadUserRoles(mockedRequest.getHeader(uebKey)))
				.thenReturn(result);
		portalRestResponse = externalAccessRolesController.bulkUploadUserRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadUserRolesExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadUserRoles(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadUserRoles");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadUserRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadPartnerFunctionsTest() {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: '0' functions");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadPartnerRolesTest() {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		List<Role> upload = new ArrayList<>();
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerRoles(mockedRequest, mockedResponse,
				upload);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadPartnerRolesExceptionTest() {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		List<Role> upload = new ArrayList<>();
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerRoles(mockedRequest, mockedResponse,
				upload);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getMenuFunctionsTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.getMenuFunctionsList(mockedRequest.getHeader(uebKey))).thenReturn(null);
		List<String> expectedFunctionsList = externalAccessRolesController.getMenuFunctions(mockedRequest,
				mockedResponse);
		assertNull(expectedFunctionsList);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void getMenuFunctionsExceptionTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.getMenuFunctionsList(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getMenuFunctions(mockedRequest, mockedResponse));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test
	public void saveRoleExceptionTest() {
		Role role = new Role();
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Invalid credentials!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.saveRole(mockedRequest, mockedResponse, role);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void deleteRoleExceptionTest() {
		String role = "TestNew";
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Invalid credentials!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, role);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadPartnerRoleFunctionsTest() {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully added: '0' role functions");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerRoleFunctions(mockedRequest,
				mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getUsersOfApplicationTest() throws Exception {
		List<EcompUser> users = new ArrayList<>();
		EcompUser user = new EcompUser();
		user.setOrgUserId("guestT");
		users.add(user);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getAllAppUsers(mockedRequest.getHeader(uebKey))).thenReturn(users);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		List<EcompUser> expectedUsers = externalAccessRolesController.getUsersOfApplication(mockedRequest,
				mockedResponse);
		assertEquals(expectedUsers, users);
	}

	@Test(expected = Exception.class)
	public void getUsersOfApplicationExceptionTest() throws Exception {
		List<EcompUser> users = new ArrayList<>();
		EcompUser user = new EcompUser();
		user.setOrgUserId("guestT");
		users.add(user);
		Mockito.when(externalAccessRolesService.getAllAppUsers(mockedRequest.getHeader(uebKey)))
				.thenThrow(nullPointerException);
		assertNull(externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse));
	}

	@Test(expected = NullPointerException.class)
	public void deleteRoleV2Test() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Success");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(), Matchers.anyString(),
				Matchers.anyString())).thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Deleted");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<String> actualResponse = externalAccessRolesController.deleteRole(mockedRequest,
				mockedResponse, (long) 1);
		assertNull(actualResponse);
	}

	@Test
	public void deleteRoleV2InvalidUebKeyTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey)))
				.thenThrow(new Exception("Invalid credentials!"));
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Invalid credentials!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = externalAccessRolesController.deleteRole(mockedRequest,
				mockedResponse, (long) 1);
		assertEquals(actualResponse, expectedportalRestResponse);
	}

	@Test
	public void deleteRoleV2InvalidUebKeyWithDiffErrorTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey)))
				.thenThrow(new Exception("test"));
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("test");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = externalAccessRolesController.deleteRole(mockedRequest,
				mockedResponse, (long) 1);
		assertEquals(actualResponse, expectedportalRestResponse);
	}

	@Test(expected = NullPointerException.class)
	public void deleteRoleV2ExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"failed");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(), Matchers.anyString(),
				Matchers.anyString())).thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to deleteRole");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = externalAccessRolesController.deleteRole(mockedRequest,
				mockedResponse, (long) 1);
		assertEquals(actualResponse, null);
	}

	@Test
	public void getEpUserNullTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12");
	}

	@Test
	public void getEpUserTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		String user = "{\"id\":null,\"created\":null,\"modified\":null,\"createdId\":null,\"modifiedId\":null,\"rowNum\":null,\"auditUserId\":null,\"auditTrail\":null,\"orgId\":null,\"managerId\":null,\"firstName\":\"test\",\"middleInitial\":null,\"lastName\":null,\"phone\":null,\"fax\":null,\"cellular\":null,\"email\":null,\"addressId\":null,\"alertMethodCd\":null,\"hrid\":null,\"orgUserId\":null,\"orgCode\":null,\"address1\":null,\"address2\":null,\"city\":null,\"state\":null,\"zipCode\":null,\"country\":null,\"orgManagerUserId\":null,\"locationClli\":null,\"businessCountryCode\":null,\"businessCountryName\":null,\"businessUnit\":null,\"businessUnitName\":null,\"department\":null,\"departmentName\":null,\"companyCode\":null,\"company\":null,\"zipCodeSuffix\":null,\"jobTitle\":null,\"commandChain\":null,\"siloStatus\":null,\"costCenter\":null,\"financialLocCode\":null,\"loginId\":null,\"loginPwd\":null,\"lastLoginDate\":null,\"active\":false,\"internal\":false,\"selectedProfileId\":null,\"timeZoneId\":null,\"online\":false,\"chatId\":null,\"userApps\":[],\"pseudoRoles\":[],\"roles\":[]}";
		Mockito.when(externalAccessRolesService.getV2UserWithRoles("test12", mockedRequest.getHeader(uebKey)))
				.thenReturn(user);
		User EPuser = new User();
		EPuser.setFirstName("test");
		Mockito.when(userservice.userMapper(user)).thenReturn(EPuser);
		String res = "{\"orgId\":null,\"managerId\":null,\"firstName\":\"test\",\"middleInitial\":null,\"lastName\":null,\"phone\":null,\"email\":null,\"hrid\":null,\"orgUserId\":null,\"orgCode\":null,\"orgManagerUserId\":null,\"jobTitle\":null,\"loginId\":null,\"active\":false,\"roles\":[]}";
		assertEquals(externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12"), res);
	}

	@Test
	public void getEpUserExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12");
	}

	@Test
	public void getEPRolesOfApplicationTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<CentralV2Role> cenRoleList = new ArrayList<>();
		CentralV2Role role = new CentralV2Role();
		role.setName("test");
		cenRoleList.add(role);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey)))
				.thenReturn(cenRoleList);
		List<EcompRole> ecompRoles = new ArrayList<>();
		EcompRole eprole = new EcompRole();
		eprole.setName("test");
		ecompRoles.add(eprole);
		assertEquals(ecompRoles,
				externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void getEPRolesOfApplicationNullTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setRolesInAAF(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<CentralV2Role> cenRoleList = new ArrayList<>();
		CentralV2Role role = new CentralV2Role.CentralV2RoleBuilder().createCentralV2Role();
		role.setName("test");
		cenRoleList.add(role);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey))).thenReturn(null);
		assertNull(externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void getEPRolesOfApplicationExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		assertNull(externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void saveRoleTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Saved");
		expectedportalRestResponse.setResponse("Success");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Role role = new Role();
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Success");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.saveRoleForApplication(role, mockedRequest.getHeader("uebkey")))
				.thenReturn(externalRequestFieldsValidator);
		actualPortalRestResponse = externalAccessRolesController.saveRole(mockedRequest, mockedResponse, role);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test
	public void saveRoleNegativeTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Saved");
		expectedportalRestResponse.setResponse("Success");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Role role = new Role();
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"Failed");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.saveRoleForApplication(role, mockedRequest.getHeader("uebkey")))
				.thenReturn(externalRequestFieldsValidator);
		actualPortalRestResponse = externalAccessRolesController.saveRole(mockedRequest, mockedResponse, role);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test
	public void saveRole406Test() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Saved");
		expectedportalRestResponse.setResponse("Failed");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Role role = new Role();
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"406");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.saveRoleForApplication(role, mockedRequest.getHeader("uebkey")))
				.thenReturn(externalRequestFieldsValidator);
		actualPortalRestResponse = externalAccessRolesController.saveRole(mockedRequest, mockedResponse, role);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test(expected = NullPointerException.class)
	public void saveRoleNullExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Role role = new Role();
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"failed");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(), Matchers.anyString(),
				Matchers.anyString())).thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to deleteRole");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = externalAccessRolesController.saveRole(mockedRequest,
				mockedResponse, role);
		assertEquals(actualResponse, null);
	}

	@Test
	public void deleteRoleTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Successfully Deleted");
		expectedportalRestResponse.setResponse("Success");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		String code = "test";
		boolean deleteResponse = true;
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.deleteRoleForApplication(code, mockedRequest.getHeader("uebkey")))
				.thenReturn(deleteResponse);
		actualPortalRestResponse = externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, code);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test
	public void deleteRoleXSSTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to deleteRole, not valid data.");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		String code = "<img src=xss onerror=alert(1)>";
		boolean deleteResponse = true;
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.deleteRoleForApplication(code, mockedRequest.getHeader("uebkey")))
			.thenReturn(deleteResponse);
		actualPortalRestResponse = externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, code);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test
	public void deleteRoleNegativeTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> actualPortalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Failed to delete Role for 'test");
		expectedportalRestResponse.setResponse("Failed");
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setRolesInAAF(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		String code = "test";
		boolean deleteResponse = false;
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.FOUND);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(appList.get(0))).thenReturn(response);
		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LoginId"))).thenReturn(userList);
		Mockito.when(externalAccessRolesService.deleteRoleForApplication(code, mockedRequest.getHeader("uebkey")))
				.thenReturn(deleteResponse);
		actualPortalRestResponse = externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, code);
		assertEquals(actualPortalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@Test
	public void deleteDependcyRoleRecordTest() throws Exception {
		ExternalRequestFieldsValidator removeResult = new ExternalRequestFieldsValidator(true, "success");
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<>();
		expectedportalRestResponse.setMessage("Invalid credentials!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		long roleId = 123;
		String LoginId = "loginId";
		List<EPApp> appList = new ArrayList<>();
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(uebKey);
		Mockito.when(mockedRequest.getHeader("LoginId")).thenReturn(LoginId);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("uebkey"))).thenReturn(appList);
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(roleId, mockedRequest.getHeader("uebkey"),
				mockedRequest.getHeader("LoginId"))).thenReturn(removeResult);
		portalRestResponse = externalAccessRolesController.deleteDependencyRoleRecord(mockedRequest, mockedResponse,
				roleId);
		assertEquals(expectedportalRestResponse, portalRestResponse);
	}
	
}
