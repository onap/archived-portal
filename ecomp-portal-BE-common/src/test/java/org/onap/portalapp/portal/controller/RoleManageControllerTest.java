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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.controller.core.RoleController;
import org.onap.portalapp.controller.core.RoleFunctionListController;
import org.onap.portalapp.controller.core.RoleListController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.service.AuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public class RoleManageControllerTest {

	@Mock
	 RoleController roleController;

	@Mock
    RoleListController roleListController;

	@Mock
    RoleFunctionListController roleFunctionListController;


	@Mock
	ExternalAccessRolesService externalAccessRolesService;
	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();
	
	@Mock
	ExternalAccessRolesService externalAccessRolesService1 = null;
	
	@InjectMocks
	RoleManageController roleManageController = new RoleManageController(); 
	
	@Mock
	EPAppService appService;

	@Mock
	AuditService auditService;
	
	@Mock
	AdminRolesService adminRolesService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	
	
	@Test
	public void removeRoleRoleFunctionTest() throws Exception
	{
		
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.removeRoleRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void addRoleRoRoleFunctionTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.addRoleRoRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void removeChildRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.removeChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	
	@Test
	public void getRoleIfRoleIdNullTest() throws Exception
	{
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		CentralV2Role answer = new CentralV2Role();
		Mockito.when(externalAccessRolesService.getRoleInfo((long) 1, "test")).thenReturn(answer);
		List<CentralV2RoleFunction> finalRoleFunctionList = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenReturn(finalRoleFunctionList);
		Mockito.when(externalAccessRolesService.getRolesForApp("test")).thenReturn(null);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
	   roleManageController.getRole(mockedRequest, mockedResponse, (long)1,null);
	  
}
	
	@Test(expected = Exception.class)
	public void getRoleExceptionTest() throws Exception
	{
		Mockito.when(appService.getApp((long) 1)).thenReturn(mockApp());
		CentralV2Role answer = new CentralV2Role();
		Mockito.when(externalAccessRolesService.getRoleInfo((long) 1, "test")).thenReturn(answer);
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenThrow(nullPointerException);
	   roleManageController.getRole(mockedRequest, mockedResponse, (long)1,null);
	  
}
	@Test
	public void getRoleIfRoleIdNotNullTest() throws Exception
	{
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		CentralV2Role answer = new CentralV2Role();
		Mockito.when(externalAccessRolesService.getRoleInfo((long) 1, "test")).thenReturn(answer);
		List<CentralV2RoleFunction> finalRoleFunctionList = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenReturn(finalRoleFunctionList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);	
		CentralV2Role currentRole = new CentralV2Role();
	    SortedSet<CentralV2Role> parentRoles = new TreeSet<>();
		CentralV2Role centralV2Role = new CentralV2Role();
		centralV2Role.setName("test");     
		parentRoles.add(centralV2Role);
		currentRole.setParentRoles(parentRoles);
		Mockito.when(externalAccessRolesService.getRoleInfo((long) 1, "test")).thenReturn(currentRole);
		List<CentralV2Role> availableChildRoles = new ArrayList<>();
		availableChildRoles.add(currentRole);
		Mockito.when(externalAccessRolesService.getRolesForApp("test")).thenReturn(availableChildRoles);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
	   roleManageController.getRole(mockedRequest, mockedResponse, (long)1,(long)1);
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
	public void getRolesTest() throws Exception {
		EPApp app = mockApp();
		app.setCentralAuth(true);
		List<CentralV2Role> answer = new ArrayList<>();
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		Mockito.when(externalAccessRolesService.getRolesForApp("test")).thenReturn(answer);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.getRoles(mockedRequest, mockedResponse, (long) 1);
	}

	@Test
	public void getRolesExceptionTest() throws Exception {
		EPApp app = CentralApp();
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		Mockito.when(externalAccessRolesService.getRolesForApp("test")).thenThrow(new java.lang.Exception());
		roleManageController.getRoles(mockedRequest, mockedResponse, (long) 1);

	}

	@Test
	public void getRoleFunctionListTest() throws Exception {
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		List<CentralV2RoleFunction> answer = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenReturn(answer);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.getRoleFunctionList(mockedRequest, mockedResponse, (long) 1);
	}

	@Test(expected = Exception.class)
	public void getRoleFunctionListExceptionTest() throws Exception {
		Mockito.when(appService.getApp((long) 1)).thenReturn(NonCentralApp());
		roleManageController.getRoleFunctionList(mockedRequest, mockedResponse, (long) 1);
	}

	@Test
	public void saveRoleFunctionTest() throws Exception {
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.doNothing().when(roleFunctionListController).saveRoleFunction(mockedRequest, mockedResponse, "test");
		CentralV2RoleFunction addNewFunc = new CentralV2RoleFunction();
		addNewFunc.setCode("Test");
		addNewFunc.setName("Test");
		CentralV2RoleFunction roleFunction = mockCentralRoleFunction();
		Mockito.when(externalAccessRolesService.getRoleFunction("Test", "test")).thenReturn(roleFunction);
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(Matchers.anyObject(), Matchers.anyObject()))
				.thenReturn(true);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPUser> userList = new ArrayList<>();
		userList.add(user);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		Mockito.when(externalAccessRolesService.getUser("guestT")).thenReturn(userList);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.saveRoleFunction(mockedRequest, mockedResponse, addNewFunc, (long) 1);
	}

	@Test
	public void saveRoleFunctionExceptionTest() throws Exception {
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.doNothing().when(roleFunctionListController).saveRoleFunction(mockedRequest, mockedResponse, "test");
		CentralV2RoleFunction addNewFunc = new CentralV2RoleFunction();
		addNewFunc.setCode("Test");
		addNewFunc.setName("Test");
		Mockito.when(externalAccessRolesService.getRoleFunction("Test", "test")).thenReturn(null);
		roleManageController.saveRoleFunction(mockedRequest, mockedResponse, addNewFunc, (long) 1);
	}

	@Test
	public void saveRoleFunctionIfAppNotCentralizedTest() throws Exception {
		CentralV2RoleFunction addNewFunc = new CentralV2RoleFunction();
		addNewFunc.setCode("Test");
		addNewFunc.setName("Test");
		Mockito.when(appService.getApp((long) 1)).thenReturn(NonCentralApp());
		roleManageController.saveRoleFunction(mockedRequest, mockedResponse, addNewFunc, (long) 1);
	}

	@Test
	public void removeRoleFunctionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		String roleFun = "{\"name\":\"Test\",\"code\":\"Test\"}";
		CentralV2RoleFunction roleFunction = mockCentralRoleFunction();
		Mockito.when(externalAccessRolesService.getRoleFunction("Test", "test")).thenReturn(roleFunction);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(Matchers.anyString(), Matchers.anyObject()))
				.thenReturn(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.removeRoleFunction(mockedRequest, mockedResponse, roleFun, (long) 1);
	}

	@Test
	public void removeRoleFunctionExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		String roleFun = "{\"name\":\"Test\",\"code\":\"Test\"}";
		CentralV2RoleFunction roleFunction = mockCentralRoleFunction();
		Mockito.when(externalAccessRolesService.getRoleFunction("Test", "test")).thenReturn(roleFunction);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(Matchers.anyString(), Matchers.anyObject()))
				.thenReturn(false);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.removeRoleFunction(mockedRequest, mockedResponse, roleFun, (long) 1);
	}

	@Test
	public void removeRoleFunctionIfAppNotCentralizedTest() throws Exception {
		EPApp app = mockApp();
		app.setCentralAuth(false);
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		String roleFun = "{\"name\":\"Test\",\"code\":\"Test\"}";
		roleManageController.removeRoleFunction(mockedRequest, mockedResponse, roleFun, (long) 1);
	}

	@Test
	public void syncRolesTest() throws Exception {
		EPApp app = mockApp();
		app.setId((long) 1);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(appService.getApp(1l)).thenReturn(app);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		PortalRestResponse<String> actual = roleManageController.syncRoles(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage("Sync roles completed successfully!");
		portalRestResponse.setResponse("Success");
		portalRestResponse.setStatus(PortalRestStatusEnum.OK);
		assertEquals(portalRestResponse, actual);
	}

	@Test
	public void syncRolesException() throws Exception {
		Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
		PortalRestResponse<String> actual = roleManageController.syncRoles(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage(null);
		portalRestResponse.setResponse("Failed");
		portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		assertEquals(portalRestResponse, actual);
	}
	
	@Test
	public void syncRolesFunctionsTest() throws Exception {
		EPApp app = mockApp();
		app.setId((long) 1);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(appService.getApp(1l)).thenReturn(app);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.doNothing().when(externalAccessRolesService).syncRoleFunctionFromExternalAccessSystem(app);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		PortalRestResponse<String> actual = roleManageController.syncFunctions(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage("Sync Functions completed successfully!");
		portalRestResponse.setResponse("Success");
		portalRestResponse.setStatus(PortalRestStatusEnum.OK);
		assertEquals(portalRestResponse, actual);
	}

	@Test
	public void syncRolesFunctionsException() throws Exception {
		Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
		PortalRestResponse<String> actual = roleManageController.syncFunctions(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage(null);
		portalRestResponse.setResponse("Failed");
		portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		assertEquals(portalRestResponse, actual);
	}
	
	@Test
	public void addeChildRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.addChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void removeRoleTest() throws Exception {
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		ExternalRequestFieldsValidator res = new ExternalRequestFieldsValidator(true, "success");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(), Matchers.anyString(),
				Matchers.anyString())).thenReturn(res);
		Mockito.when(externalAccessRolesService.getUser(Matchers.anyString())).thenReturn(epuserList);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when( externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Map<String, Object> expedtedResponse = new HashMap<String, Object>();
		expedtedResponse.put("restCallStatus", " Unauthorized user");
		Map<String, Object> actualResponse =	roleManageController.removeRole(mockedRequest, mockedResponse, (long) 1, (long) 1);
		assertEquals(actualResponse.size(), expedtedResponse.size());

	}
	
	@Test(expected = Exception.class)
	public void removeRoleExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenThrow(nullPointerException);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		roleManageController.removeRole(mockedRequest, mockedResponse, (long) 1, (long) 1);
	}
	
	public CentralV2RoleFunction mockCentralRoleFunction()
	{
		CentralV2RoleFunction roleFunction = new CentralV2RoleFunction();
		roleFunction.setCode("Test");
		roleFunction.setName("Test");
		roleFunction.setAppId((long) 1);
		return roleFunction;
	}
	
	public EPApp CentralApp()
	{
		EPApp app =  mockApp();
		app.setCentralAuth(true);
		app.setNameSpace("com.test");
		return app;
	}
	
	public EPApp NonCentralApp()
	{
		EPApp app =  mockApp();
		app.setCentralAuth(false);
		return app;
	}
}
