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
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.service.ExternalAccessRolesServiceImpl;
import org.onap.portalapp.portal.transport.CentralRole;
import org.onap.portalapp.portal.transport.CentralRoleFunction;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, PortalConstants.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class ExternalAccessRolesControllerTest {

	@Mock
	ExternalAccessRolesService externalAccessRolesService = new ExternalAccessRolesServiceImpl();

	@InjectMocks
	ExternalAccessRolesController externalAccessRolesController = new ExternalAccessRolesController();
	@Mock
	UserService userservice =  new UserServiceCentalizedImpl();
	
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

	@Test
	public void getUserTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		List<EPUser> userList = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getUser(loginId)).thenReturn(userList);
		externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
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
	
	public EPApp mockApp()
	{
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
		return app;
	}
	
	@Test(expected = NullPointerException.class)
	public void getRolesForAppTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
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
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		externalAccessRolesController.getRolesForApp(mockedRequest,mockedResponse);
		List<CentralV2Role> role = externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey));
		assertEquals(null,role);
	}

	@Test
	public void getRoleFunctionsListTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		List<CentralV2RoleFunction> answer = null;
		Mockito.when(externalAccessRolesService.getRoleFuncList(mockedRequest.getHeader(uebKey))).thenReturn(null);
		assertEquals(externalAccessRolesController.getRoleFunctionsList(mockedRequest, mockedResponse), answer);
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
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
	public void getRoleInfoTest() throws Exception {
		String reason = getInvalidKeyJson();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);		
		CentralV2Role answer = new CentralV2Role();
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
	public void getRoleFunctionTest() throws Exception {
		EPApp mockApp = mockApp();
		mockApp.setCentralAuth(true);
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
		CentralRoleFunction returnedValue = externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, code);
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
		assertEquals(new CentralRoleFunction(),externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, code));
		String result = sw.getBuffer().toString().trim();
		assertEquals(reason, result);
	}

	@Test(expected = NullPointerException.class)
	public void saveRoleFunctionIfIsIsNotDeletedTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		String data = null;
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralV2RoleFunction, app)).thenReturn(false);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test(expected = NullPointerException.class)
	public void saveRoleFunctionExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, null);
		System.out.println(portalRestResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test(expected = NullPointerException.class)
	public void saveRoleFunctionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		String data = null;
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralV2RoleFunction, app)).thenReturn(true);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		System.out.println(portalRestResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void deleteRoleFunctionTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully Deleted");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		EPUser user = mockUser.mockEPUser();
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		EPApp app = mockApp();
		app.setCentralAuth(true);
		List<EPApp> appList  = new ArrayList<>();
		appList.add(app);
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		String code ="testNew";
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
		final Map<String,String> uebkeyResponse = new HashMap<>();
		String reason = "";
		ObjectMapper mapper = new ObjectMapper();
		uebkeyResponse.put("error","Invalid uebkey!");
		reason = mapper.writeValueAsString(uebkeyResponse);
		return reason;
	}

	@Test
	public void deleteDependcyRoleRecordExceptionTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		long roleId = 123;
		portalRestResponse = externalAccessRolesController.deleteDependencyRoleRecord(mockedRequest, mockedResponse, roleId);
		assertEquals(expectedportalRestResponse, portalRestResponse);
	}

	@Test
	public void bulkUploadFunctionsTest() throws Exception {
		Integer result = 0;
		Mockito.when(externalAccessRolesService.bulkUploadFunctions(mockedRequest.getHeader(uebKey)))
				.thenReturn(result);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		portalRestResponse = externalAccessRolesController.bulkUploadFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadFunctionsExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadFunctions(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadFunctions");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRolesTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		Mockito.when(externalAccessRolesService.bulkUploadRoles(mockedRequest.getHeader(uebKey))).thenReturn(result);
		portalRestResponse = externalAccessRolesController.bulkUploadRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRolesTestException() throws Exception {
		Mockito.when(externalAccessRolesService.bulkUploadRoles(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadRoles");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadRoleFunctionsTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
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
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadRoleFunctions");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadRoleFunctions(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadUserRolesTest() throws Exception {
		Integer result = 0;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added: 0");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
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
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to bulkUploadUserRoles");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.bulkUploadUserRoles(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void bulkUploadPartnerFunctionsTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerFunctions(mockedRequest, mockedResponse, null);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}


	@Test
	public void bulkUploadPartnerRolesTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		List<Role> upload = new ArrayList<>();
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerRoles(mockedRequest, mockedResponse,
				upload);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	 @Test
	 public void bulkUploadPartnerRolesExceptionTest() throws Exception
	 {
		 ExternalAccessRolesService externalAccessRolesService = null;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
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
		Mockito.when(externalAccessRolesService.getMenuFunctionsList(mockedRequest.getHeader(uebKey)))
				.thenReturn(null);
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
	public void saveRoleExceptionTest() throws Exception {
		Role role = new Role();
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		List<Role> upload = new ArrayList<>();
		portalRestResponse = externalAccessRolesController.saveRole(mockedRequest, mockedResponse,role);
		
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void deleteRoleExceptionTest() throws Exception {
		String role = "TestNew";
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		portalRestResponse = externalAccessRolesController.deleteRole(mockedRequest, mockedResponse,role);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	
	@Test
	public void bulkUploadPartnerRoleFunctionsTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully added");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		List<Role> upload = new ArrayList<>();
		portalRestResponse = externalAccessRolesController.bulkUploadPartnerRoleFunctions(mockedRequest, mockedResponse,upload);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void getUsersOfApplicationTest() throws Exception
	{
		List<EcompUser> users = new ArrayList<>();
		EcompUser user = new EcompUser();
		user.setOrgUserId("guestT");
		users.add(user);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setCentralAuth(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.when(externalAccessRolesService.getAllAppUsers(mockedRequest.getHeader(uebKey))).thenReturn(users);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		List<EcompUser> expectedUsers = 	externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse);
		assertEquals(expectedUsers, users);
	}
	
	@Test(expected = Exception.class)
	public void getUsersOfApplicationExceptionTest() throws Exception
	{
		List<EcompUser> users = new ArrayList<>();
		EcompUser user = new EcompUser();
		user.setOrgUserId("guestT");
		users.add(user);
		Mockito.when(externalAccessRolesService.getAllAppUsers(mockedRequest.getHeader(uebKey))).thenThrow(nullPointerException);
		assertNull(externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse));
	}
	
	@Test(expected = NullPointerException.class)
	public void deleteRoleV2Test() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true, "Success");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(),Matchers.anyString(),Matchers.anyString())).thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully Deleted");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		PortalRestResponse<String> actualResponse = 	externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, (long)1);
		assertNull(actualResponse);
	}
	
	@Test
	public void deleteRoleV2InvalidUebKeyTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenThrow(new Exception("Invalid uebkey!"));
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Invalid uebkey!");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = 	externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, (long)1);
		assertEquals(actualResponse, expectedportalRestResponse);
	}
	
	@Test
	public void deleteRoleV2InvalidUebKeyWithDiffErrorTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenThrow(new Exception("test"));
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("test");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = 	externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, (long)1);
		assertEquals(actualResponse, expectedportalRestResponse);
	}
	
	
	@Test(expected = NullPointerException.class)
	public void deleteRoleV2ExceptionTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false, "failed");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(),Matchers.anyString(),Matchers.anyString())).thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to deleteRole");
		expectedportalRestResponse.setResponse("Failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = 	externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, (long)1);
		assertEquals(actualResponse, null);
	}
	
	@Test
	public void getEpUserNullTest() throws Exception{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setCentralAuth(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		assertNull(externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12"));
	}
	
	@Test
	public void getEpUserTest() throws Exception{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setCentralAuth(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
       String user = "{\"id\":null,\"created\":null,\"modified\":null,\"createdId\":null,\"modifiedId\":null,\"rowNum\":null,\"auditUserId\":null,\"auditTrail\":null,\"orgId\":null,\"managerId\":null,\"firstName\":\"test\",\"middleInitial\":null,\"lastName\":null,\"phone\":null,\"fax\":null,\"cellular\":null,\"email\":null,\"addressId\":null,\"alertMethodCd\":null,\"hrid\":null,\"orgUserId\":null,\"orgCode\":null,\"address1\":null,\"address2\":null,\"city\":null,\"state\":null,\"zipCode\":null,\"country\":null,\"orgManagerUserId\":null,\"locationClli\":null,\"businessCountryCode\":null,\"businessCountryName\":null,\"businessUnit\":null,\"businessUnitName\":null,\"department\":null,\"departmentName\":null,\"companyCode\":null,\"company\":null,\"zipCodeSuffix\":null,\"jobTitle\":null,\"commandChain\":null,\"siloStatus\":null,\"costCenter\":null,\"financialLocCode\":null,\"loginId\":null,\"loginPwd\":null,\"lastLoginDate\":null,\"active\":false,\"internal\":false,\"selectedProfileId\":null,\"timeZoneId\":null,\"online\":false,\"chatId\":null,\"userApps\":[],\"pseudoRoles\":[],\"defaultUserApp\":null,\"roles\":[],\"fullName\":\"test null\"}";
		Mockito.when(externalAccessRolesService.getV2UserWithRoles("test12", mockedRequest.getHeader(uebKey))).thenReturn(user);
		User EPuser = new User();
		EPuser.setFirstName("test");
		Mockito.when(userservice.userMapper(user)).thenReturn(EPuser);
		String res = "{\"orgId\":null,\"managerId\":null,\"firstName\":\"test\",\"middleInitial\":null,\"lastName\":null,\"phone\":null,\"email\":null,\"hrid\":null,\"orgUserId\":null,\"orgCode\":null,\"orgManagerUserId\":null,\"jobTitle\":null,\"loginId\":null,\"active\":false,\"roles\":[]}";
		assertEquals(externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12"),res);
	}
	
	@Test
	public void getEpUserExceptionTest() throws Exception{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setCentralAuth(true);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		assertNull(externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12"));
	}
	
	@Test
	public void  getEPRolesOfApplicationTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setCentralAuth(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<CentralV2Role> cenRoleList = new ArrayList<>();
		CentralV2Role role = new CentralV2Role();
		role.setName("test");
		cenRoleList.add(role);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey))).thenReturn(cenRoleList);
		List<EcompRole> ecompRoles = new ArrayList<>();
		EcompRole eprole = new EcompRole();
		eprole.setName("test");
		ecompRoles.add(eprole);
		assertEquals(ecompRoles,externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}	
	@Test
	public void  getEPRolesOfApplicationNullTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setUebKey("uebKey");
		app.setCentralAuth(true);
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(app)).thenReturn(response);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<CentralV2Role> cenRoleList = new ArrayList<>();
		CentralV2Role role = new CentralV2Role();
		role.setName("test");
		cenRoleList.add(role);
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey))).thenReturn(null);
		assertNull(externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));

	}
	
	@Test
	public void  getEPRolesOfApplicationExceptionTest() throws Exception
	{
		List<EPApp> applicationList = new ArrayList<EPApp>();
		EPApp app = mockApp();
		app.setCentralAuth(true);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		assertNull(externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));

	}
}
