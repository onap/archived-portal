/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.ExternalAccessRolesService;
import org.openecomp.portalapp.portal.service.ExternalAccessRolesServiceImpl;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalsdk.core.domain.AuditLog;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.restful.domain.EcompUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
public class ExternalAccessRolesControllerTest {

	@Mock
	ExternalAccessRolesService externalAccessRolesService = new ExternalAccessRolesServiceImpl();

	@InjectMocks
	ExternalAccessRolesController externalAccessRolesController = new ExternalAccessRolesController();

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
		 List<EPUser> userList = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getUser(loginId)).thenReturn(userList);
		assertNull(externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId));
	}

	@Test
	public void getUserExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.getUser(loginId))
				.thenThrow(nullPointerException);
		assertNull(externalAccessRolesController.getUser(mockedRequest, mockedResponse, loginId));
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
	
	@Test
	public void getRolesForAppTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		List<CentralRole> answer = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey))).thenReturn(answer);
		assertEquals(externalAccessRolesController.getRolesForApp(mockedRequest, mockedResponse), answer);
	}

	@Test
	public void getRolesForAppExceptionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		List<CentralRole> answer = new ArrayList<>();
		EPApp app = mockApp();
		applicationList.add(app);
		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader(uebKey))).thenReturn(applicationList);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		Mockito.when(externalAccessRolesService.getRolesForApp(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getRolesForApp(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleFunctionsListTest() throws Exception {
		List<CentralRoleFunction> answer = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getRoleFuncList(mockedRequest.getHeader(uebKey))).thenReturn(answer);
		assertEquals(externalAccessRolesController.getRoleFunctionsList(mockedRequest, mockedResponse), answer);
	}

	@Test
	public void getRoleFunctionsListExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.getRoleFuncList(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getRoleFunctionsList(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleInfoTest() throws Exception {
		CentralRole answer = new CentralRole();
		long roleId = 1;
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader(uebKey)))
				.thenReturn(answer);
		assertEquals(externalAccessRolesController.getRoleInfo(mockedRequest, mockedResponse, roleId), answer);
	}

	@Test
	public void getRoleInfoExceptionTest() throws Exception {
		long roleId = 1;
		Mockito.when(externalAccessRolesService.getRoleInfo(roleId, mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getRoleInfo(mockedRequest, mockedResponse, roleId));
	}

	@Test
	public void getRoleFunctionTest() throws Exception {
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		String code = "test_menu";
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader(uebKey)))
				.thenReturn(centralRoleFunction);
		assertEquals(externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, code),
				centralRoleFunction);
	}

	@Test
	public void getRoleFunctionExceptionTest() throws Exception {
		String code = "test_menu";
		Mockito.when(externalAccessRolesService.getRoleFunction(code, mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, code));
	}

	@Test
	public void saveRoleFunctionIfIsIsNotDeletedTest() throws Exception {
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
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralRoleFunction, app)).thenReturn(false);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void saveRoleFunctionExceptionTest() throws Exception {
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
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralRoleFunction, app)).thenThrow(nullPointerException);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		System.out.println(portalRestResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void saveRoleFunctionTest() throws Exception {
		List<EPApp> applicationList = new ArrayList<EPApp>();
		List<CentralRole> answer = new ArrayList<>();
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
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(centralRoleFunction, app)).thenReturn(true);
		portalRestResponse = externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, data);
		System.out.println(portalRestResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

//	@Test
//	public void deleteRoleFunctionTest() throws Exception {
//		PortalRestResponse<String> portalRestResponse = null;
//		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
//		expectedportalRestResponse.setMessage("Successfully Deleted");
//		expectedportalRestResponse.setResponse("Success");
//		PortalRestStatusEnum portalRestStatusEnum = null;
//		EPUser user = mockUser.mockEPUser();
//		EPApp app = mockApp();
//		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
//		String code ="testNew";
//		Mockito.when(externalAccessRolesService.getUser(mockedRequest.getHeader("LOGIN_ID"))).thenReturn((List<EPUser>) user);
//		Mockito.when(externalAccessRolesService.getApp(mockedRequest.getHeader("UEBKEY")).get(0)).thenReturn(app);
//		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(code, app)).thenReturn(true);
//		portalRestResponse = externalAccessRolesController.deleteRoleFunction(mockedRequest, mockedResponse, code);
//		assertEquals(portalRestResponse, expectedportalRestResponse);
//	}

	@Test
	public void getActiveRolesTest() throws Exception {
		List<CentralRole> cenRole = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey))).thenReturn(cenRole);
		List<CentralRole> expectedCenRole = externalAccessRolesController.getActiveRoles(mockedRequest, mockedResponse);
		assertEquals(expectedCenRole, cenRole);
	}

	@Test
	public void getActiveRolesExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.getActiveRoles(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getActiveRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void deleteDependcyRoleRecordExceptionTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to deleteDependencyRoleRecord");
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
		List<String> functionsList = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getMenuFunctionsList(mockedRequest.getHeader(uebKey)))
				.thenReturn(functionsList);
		List<String> expectedFunctionsList = externalAccessRolesController.getMenuFunctions(mockedRequest,
				mockedResponse);
		assertEquals(functionsList, expectedFunctionsList);
	}

	@Test
	public void getMenuFunctionsExceptionTest() throws Exception {
		Mockito.when(externalAccessRolesService.getMenuFunctionsList(mockedRequest.getHeader(uebKey)))
				.thenThrow(httpClientErrorException);
		assertNull(externalAccessRolesController.getMenuFunctions(mockedRequest, mockedResponse));
	}

//	@Test
//	public void getUsersOfApplicationTest() throws Exception {
//		List<String> users = new ArrayList<>();
//		Mockito.when(externalAccessRolesService.getAllUsers(mockedRequest.getHeader(uebKey))).thenReturn(users);
//		List<String> expectedusers = externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse);
//		assertEquals(users, expectedusers);
//	}

//	@Test
//	public void getUsersOfApplicationExceptionTest() throws Exception {
//		Mockito.when(externalAccessRolesService.getAllUsers(mockedRequest.getHeader(uebKey)))
//				.thenThrow(httpClientErrorException);
//		assertNull(externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse));
//	}
	
	@Test
	public void saveRoleExceptionTest() throws Exception {
		Role role = new Role();
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to saveRole");
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
		expectedportalRestResponse.setMessage("Failed to deleteRole for 'TestNew'");
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
		Mockito.when(externalAccessRolesService.getAllAppUsers(mockedRequest.getHeader(uebKey))).thenReturn(users);
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
}
