/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.ecomp.model.UploadRoleFunctionExtSystem;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.service.AuditService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EPUserUtils.class, EcompPortalUtils.class })
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

	private DelegatingServletInputStream dsi;

	@Test
	public void removeRoleRoleFunctionTest() throws Exception {

		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView = roleManageController.removeRoleRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}

	@Test
	public void addRoleRoRoleFunctionTest() throws Exception {
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView = roleManageController.addRoleRoRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}

	@Test
	public void removeChildRoleTest() throws Exception {
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView = roleManageController.removeChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}

	@Test
	public void getRoleIfRoleIdNullTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isAccountAdminOfApplication(user, CentralApp())).thenReturn(true);
		List<EPApp> apps = new ArrayList<>();
		apps.add(CentralApp());
		Mockito.when(externalAccessRolesService.getApp(CentralApp().getUebKey())).thenReturn(apps);
		ResponseEntity<String> result = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(apps.get(0))).thenReturn(result);
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
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.getRole(mockedRequest, mockedResponse, (long) 1, null);
	}

	@Test(expected = Exception.class)
	public void getRoleExceptionTest() throws Exception {
		Mockito.when(appService.getApp((long) 1)).thenReturn(mockApp());
		CentralV2Role answer = new CentralV2Role();
		Mockito.when(externalAccessRolesService.getRoleInfo((long) 1, "test")).thenReturn(answer);
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenThrow(nullPointerException);
		roleManageController.getRole(mockedRequest, mockedResponse, (long) 1, null);

	}

	@Test
	public void getRoleIfRoleIdNotNullTest() throws Exception {
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
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		roleManageController.getRole(mockedRequest, mockedResponse, (long) 1, (long) 1);
	}

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
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(user, app)).thenReturn(true);
		List<CentralV2Role> answer = new ArrayList<>();
		Mockito.when(appService.getApp((long) 1)).thenReturn(app);
		Mockito.when(externalAccessRolesService.getRolesForApp("test")).thenReturn(answer);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
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
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(user, CentralApp())).thenReturn(true);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		List<CentralV2RoleFunction> answer = new ArrayList<>();
		Mockito.when(externalAccessRolesService.getRoleFuncList("test")).thenReturn(answer);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
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
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(user, CentralApp())).thenReturn(true);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.doNothing().when(roleFunctionListController).saveRoleFunction(mockedRequest, mockedResponse, "test");
		CentralV2RoleFunction addNewFunc = new CentralV2RoleFunction();
		addNewFunc.setCode("Test");
		addNewFunc.setType("Test");
		addNewFunc.setAction("Test");
		addNewFunc.setName("Test");
		CentralV2RoleFunction roleFunction = mockCentralRoleFunction();
		roleFunction.setCode("Test|Test|Test");
		Mockito.when(externalAccessRolesService.getRoleFunction("Test|Test|Test", "test")).thenReturn(roleFunction);
		Mockito.when(externalAccessRolesService.saveCentralRoleFunction(Matchers.anyObject(), Matchers.anyObject()))
				.thenReturn(true);
		Mockito.when(EcompPortalUtils.getFunctionCode(roleFunction.getCode())).thenReturn("Test");
		Mockito.when(EcompPortalUtils.getFunctionType(roleFunction.getCode())).thenReturn("Test");
		Mockito.when(EcompPortalUtils.getFunctionAction(roleFunction.getCode())).thenReturn("Test");
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
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		PortalRestResponse<String> actual = roleManageController.saveRoleFunction(mockedRequest, mockedResponse,
				addNewFunc, (long) 1);
		PortalRestResponse<String> expected = new PortalRestResponse<String>(PortalRestStatusEnum.OK,
				"Saved Successfully!", "Success");
		assertEquals(expected, actual);
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
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdminOfApplication(user, CentralApp())).thenReturn(true);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		String roleFun = "{\"name\":\"Test\",\"type\":\"Test\",\"action\":\"Test\", \"code\":\"Test\"}";
		CentralV2RoleFunction roleFunction = mockCentralRoleFunction();
		Mockito.when(externalAccessRolesService.getRoleFunction("Test|Test|Test", "test")).thenReturn(roleFunction);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		Mockito.when(externalAccessRolesService.deleteCentralRoleFunction(Matchers.anyString(), Matchers.anyObject()))
				.thenReturn(true);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		PortalRestResponse<String> actual = roleManageController.removeRoleFunction(mockedRequest, mockedResponse,
				roleFun, (long) 1);
		PortalRestResponse<String> expected = new PortalRestResponse<String>(PortalRestStatusEnum.OK,
				"Deleted Successfully!", "Success");
		assertEquals(expected, actual);
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
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
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
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		app.setId((long) 1);
		Mockito.when(appService.getApp(1l)).thenReturn(app);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
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
      EPUser user = mockUser.mockEPUser();
      Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
      Mockito.when(adminRolesService.isAccountAdminOfApplication(user, null)).thenReturn(true);
		Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
		PortalRestResponse<String> actual = roleManageController.syncRoles(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage(null);
		portalRestResponse.setResponse("Failed");
		portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		assertEquals(portalRestResponse, actual);
	}

    @Test
    public void syncRolesUserNullException() throws Exception {
        Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
        Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
        PortalRestResponse<String> actual = roleManageController.syncRoles(mockedRequest, mockedResponse, 1l);
        PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
        portalRestResponse.setMessage("Unauthorized User");
        portalRestResponse.setResponse("Failure");
        portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
        assertEquals(portalRestResponse, actual);
    }

	@Test
	public void syncRolesFunctionsTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
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
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
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
      EPUser user = mockUser.mockEPUser();
      Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
      Mockito.when(adminRolesService.isAccountAdminOfApplication(user, null)).thenReturn(true);
      Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
		PortalRestResponse<String> actual = roleManageController.syncFunctions(mockedRequest, mockedResponse, 1l);
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
		portalRestResponse.setMessage(null);
		portalRestResponse.setResponse("Failed");
		portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		assertEquals(portalRestResponse, actual);
	}

    @Test
    public void syncRolesFunctionsUserNullException() throws Exception {
        Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
        Mockito.when(appService.getAppDetailByAppName("test")).thenThrow(nullPointerException);
        PortalRestResponse<String> actual = roleManageController.syncFunctions(mockedRequest, mockedResponse, 1l);
        PortalRestResponse<String> portalRestResponse = new PortalRestResponse<>();
        portalRestResponse.setMessage("Unauthorized User");
        portalRestResponse.setResponse("Failure");
        portalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
        assertEquals(portalRestResponse, actual);
    }

	@Test
	public void addChildRoleTest() throws Exception {
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView = roleManageController.addChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}

	@Test
	public void removeRoleTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Mockito.when(mockedResponse.getWriter()).thenReturn(writer);
		ExternalRequestFieldsValidator res = new ExternalRequestFieldsValidator(true, "success");
		Mockito.when(externalAccessRolesService.deleteDependencyRoleRecord(Matchers.anyLong(), Matchers.anyString(),
				Matchers.anyString())).thenReturn(res);
		Mockito.when(externalAccessRolesService.getUser(Matchers.anyString())).thenReturn(epuserList);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Map<String, Object> expedtedResponse = new HashMap<String, Object>();
		expedtedResponse.put("restCallStatus", " Unauthorized user");
		expedtedResponse.put("availableRoles", new ArrayList<>());
		Map<String, Object> actualResponse = roleManageController.removeRole(mockedRequest, mockedResponse, (long) 1,
				(long) 1);
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

	@SuppressWarnings("unchecked")
	@Test
	public void saveRoleNewTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		JSONObject roleJson = new JSONObject();
		roleJson.put("name", "test");
		JSONObject roleJson2 = new JSONObject();
		List<JSONObject> childRolesJson = new ArrayList<>();
		List<JSONObject> roleFunctions = new ArrayList<>();
		roleJson2.put("role", roleJson);
		roleJson2.put("childRoles", childRolesJson);
		roleJson2.put("roleFunctions", roleFunctions);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(roleJson2.toString());
		dsi = new DelegatingServletInputStream(
				new ByteArrayInputStream(actualObj.toString().getBytes(StandardCharsets.UTF_8)));
		Mockito.when(mockedRequest.getInputStream()).thenReturn(dsi);
		Mockito.when(mockedRequest.getReader()).thenReturn(new BufferedReader(new StringReader(actualObj.toString())));
		Mockito.when(mockedRequest.getContentType()).thenReturn("application/json");
		Mockito.when(mockedRequest.getCharacterEncoding()).thenReturn("UTF-8");
		Mockito.when(externalAccessRolesService.ConvertCentralRoleToRole(Matchers.anyString())).thenReturn(new Role());
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true, "");
		Mockito.when(externalAccessRolesService.saveRoleForApplication(Matchers.any(), Matchers.any()))
				.thenReturn(externalRequestFieldsValidator);
		Map<String, Object> actual = roleManageController.saveRole(mockedRequest, mockedResponse, CentralApp().getId());
		final Map<String, Object> expected = new HashMap<>();
		expected.put("role", new CentralV2Role(null, "test"));
		expected.put("status", "Success");
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void saveRoleUpdateTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		JSONObject roleJson = new JSONObject();
		roleJson.put("id", 1);
		roleJson.put("name", "test");
		roleJson.put("active", true);
		JSONObject roleJson2 = new JSONObject();
		List<JSONObject> childRolesJson = new ArrayList<>();
		List<JSONObject> roleFunctions = new ArrayList<>();
		JSONObject roleFunction = new JSONObject();
		roleFunction.put("code", "test");
		roleFunction.put("name", "test");
		roleFunction.put("type", "test");
		roleFunction.put("action", "test");
		roleFunctions.add(roleFunction);
		roleJson.put("roleFunctions", roleFunctions);
		roleJson2.put("role", roleJson);
		roleJson2.put("childRoles", childRolesJson);
		roleJson2.put("roleFunctions", roleFunctions);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(roleJson2.toString());
		dsi = new DelegatingServletInputStream(
				new ByteArrayInputStream(actualObj.toString().getBytes(StandardCharsets.UTF_8)));
		Mockito.when(mockedRequest.getInputStream()).thenReturn(dsi);
		Mockito.when(mockedRequest.getReader()).thenReturn(new BufferedReader(new StringReader(actualObj.toString())));
		Mockito.when(mockedRequest.getContentType()).thenReturn("application/json");
		Mockito.when(mockedRequest.getCharacterEncoding()).thenReturn("UTF-8");
		Mockito.when(externalAccessRolesService.ConvertCentralRoleToRole(Matchers.anyString())).thenReturn(new Role());
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true, "");
		Mockito.when(externalAccessRolesService.saveRoleForApplication(Matchers.any(), Matchers.any()))
				.thenReturn(externalRequestFieldsValidator);
		CentralV2Role cenV2Role = new CentralV2Role(1l, "test1");
		cenV2Role.setActive(true);
		Mockito.when(externalAccessRolesService.getRoleInfo(Matchers.anyLong(), Matchers.any())).thenReturn(cenV2Role);
		Map<String, Object> actual = roleManageController.saveRole(mockedRequest, mockedResponse, CentralApp().getId());
		final Map<String, Object> expected = new HashMap<>();
		expected.put("status", "Success");
		assertEquals(expected.get("status"), actual.get("status"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void saveRoleInvalidRoleExceptionTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		JSONObject roleJson = new JSONObject();
		roleJson.put("id", 1);
		roleJson.put("name", "test%%");
		roleJson.put("active", true);
		JSONObject roleJson2 = new JSONObject();
		List<JSONObject> childRolesJson = new ArrayList<>();
		List<JSONObject> roleFunctions = new ArrayList<>();
		JSONObject roleFunction = new JSONObject();
		roleFunction.put("code", "test");
		roleFunction.put("name", "test");
		roleFunction.put("type", "test");
		roleFunction.put("action", "test");
		roleFunctions.add(roleFunction);
		roleJson.put("roleFunctions", roleFunctions);
		roleJson2.put("role", roleJson);
		roleJson2.put("childRoles", childRolesJson);
		roleJson2.put("roleFunctions", roleFunctions);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(roleJson2.toString());
		dsi = new DelegatingServletInputStream(
				new ByteArrayInputStream(actualObj.toString().getBytes(StandardCharsets.UTF_8)));
		Mockito.when(mockedRequest.getInputStream()).thenReturn(dsi);
		Mockito.when(mockedRequest.getReader()).thenReturn(new BufferedReader(new StringReader(actualObj.toString())));
		Mockito.when(mockedRequest.getContentType()).thenReturn("application/json");
		Mockito.when(mockedRequest.getCharacterEncoding()).thenReturn("UTF-8");
		Map<String, Object> actual = roleManageController.saveRole(mockedRequest, mockedResponse, CentralApp().getId());
		final Map<String, Object> expected = new HashMap<>();
		expected.put("error", "Invalid role name found for 'test%%'. Any one of the following characters '%,(),=,:,comma, and double quotes' are not allowed");
		assertEquals(expected.get("error"), actual.get("error"));
	}
	
	@Test
	public void saveRoleUnauthorizedUserExceptionTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(false);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Map<String, Object> actual = roleManageController.saveRole(mockedRequest, mockedResponse, CentralApp().getId());
		final Map<String, Object> expected = new HashMap<>();
		expected.put("error", " Unauthorized user");
		assertEquals(expected.get("error"), actual.get("error"));
	}
	
	@Test
	public void toggleRoleTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		CentralV2Role role = new CentralV2Role(1l, "test");
		role.setActive(true);
		Role currentRole = new Role();
		currentRole.setName("test");
		Mockito.when(externalAccessRolesService.getRoleInfo(Matchers.anyLong(), Matchers.any())).thenReturn(role);
		Mockito.when(externalAccessRolesService.ConvertCentralRoleToRole(Matchers.anyString())).thenReturn(currentRole);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true, "");
		Mockito.when(externalAccessRolesService.saveRoleForApplication(Matchers.any(),Matchers.any())).thenReturn(externalRequestFieldsValidator);
		Map<String, Object> actual = roleManageController.toggleRole(mockedRequest, mockedResponse, CentralApp().getId(), 1l);
		final Map<String, Object> expected = new HashMap<>();
		expected.put("restcallStatus", "Success");
		assertEquals(expected.get("restcallStatus"), actual.get("restcallStatus"));
	}

	@Test
	public void toggleRoleUnauthorizedUserExceptionTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(false);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		Map<String, Object> actual = roleManageController.toggleRole(mockedRequest, mockedResponse, CentralApp().getId(), 1l);
		final Map<String, Object> expected = new HashMap<>();
		expected.put("restcallStatus", " Unauthorized user");
		assertEquals(expected.get("restcallStatus"), actual.get("restcallStatus"));
	}
	
	@Test(expected = NullPointerException.class)
	public void toggleRoleExceptionTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.doThrow(new NullPointerException()).when(appService).getApp((long) 1);
		roleManageController.toggleRole(mockedRequest, mockedResponse, CentralApp().getId(), 1l);
	}
	
	@Test
	public void toggleRoleFailedTest() throws Exception {
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		List<EPUser> epuserList = new ArrayList<>();
		List<EPApp> appList = new ArrayList<>();
		appList.add(CentralApp());
		EPUser user = mockUser.mockEPUser();
		epuserList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(appService.getApp((long) 1)).thenReturn(CentralApp());
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getApp(Matchers.anyString())).thenReturn(appList);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(externalAccessRolesService.getNameSpaceIfExists(Matchers.anyObject())).thenReturn(response);
		CentralV2Role role = new CentralV2Role(1l, "test");
		role.setActive(true);
		Role currentRole = new Role();
		currentRole.setName("test");
		Mockito.when(externalAccessRolesService.getRoleInfo(Matchers.anyLong(), Matchers.any())).thenReturn(role);
		Mockito.when(externalAccessRolesService.ConvertCentralRoleToRole(Matchers.anyString())).thenReturn(currentRole);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false, "");
		Mockito.when(externalAccessRolesService.saveRoleForApplication(Matchers.any(),Matchers.any())).thenReturn(externalRequestFieldsValidator);
		Map<String, Object> actual = roleManageController.toggleRole(mockedRequest, mockedResponse, CentralApp().getId(), 1l);
		final Map<String, Object> expected = new HashMap<>();
		expected.put("restcallStatus", "Toggle Role Failed");
		assertEquals(expected.get("restcallStatus"), actual.get("restcallStatus"));
	}
	
	@Test
	public void getAvailableChildRolesTest() throws Exception {
		List<CentralV2Role> centralV2Roles = new ArrayList<>();
		CentralV2Role centralV2Role = new CentralV2Role();
		centralV2Role.setName("test");
		centralV2Role.setId(1l);
		CentralV2Role centralV2Role2 = new CentralV2Role();
		centralV2Role2.setName("test");
		centralV2Role2.setId(1l);
		centralV2Roles.add(centralV2Role);
		centralV2Roles.add(centralV2Role2);
		SortedSet<CentralV2RoleFunction> roleFuns = new TreeSet<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction("test", "test");
		roleFuns.add(centralV2RoleFunction);
		CentralV2Role currentRole = new CentralV2Role();
		currentRole.setName("test");
		currentRole.setId(1l);
		currentRole.setRoleFunctions(roleFuns);
		Mockito.when(externalAccessRolesService.getRolesForApp(CentralApp().getUebKey())).thenReturn(centralV2Roles);
		Mockito.when(externalAccessRolesService.getRoleInfo(1l, CentralApp().getUebKey())).thenReturn(currentRole);
		List<CentralV2Role> actual = roleManageController.getAvailableChildRoles(CentralApp().getUebKey(), 1l);
		assertEquals(new ArrayList<>().size(), actual.size());
	}
	
	@Test
	public void getCentralizedAppRolesTest() throws IOException {
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<CentralizedApp> cenApps = new ArrayList<>();
		CentralizedApp centralizedApp = new CentralizedApp();
		centralizedApp.setAppId(1);
		centralizedApp.setAppName("Test");
		cenApps.add(centralizedApp);
		Mockito.when(adminRolesService.isSuperAdmin(Matchers.anyObject())).thenReturn(true);
		Mockito.when(externalAccessRolesService.getCentralizedAppsOfUser(Matchers.anyString())).thenReturn(cenApps);
		List<CentralizedApp> actual  = roleManageController.getCentralizedAppRoles(mockedRequest, mockedResponse, user.getOrgUserId());
		assertEquals(cenApps.size(), actual.size());
	}
	
	@Test
	public void getCentralizedAppRolesExceptionTest() throws IOException {
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<CentralizedApp> cenApps = new ArrayList<>();
		CentralizedApp centralizedApp = new CentralizedApp();
		centralizedApp.setAppId(1);
		centralizedApp.setAppName("Test");
		cenApps.add(centralizedApp);
		Mockito.when(adminRolesService.isAccountAdmin(Matchers.anyObject())).thenReturn(false);
		List<CentralizedApp> actual  = roleManageController.getCentralizedAppRoles(mockedRequest, mockedResponse, user.getOrgUserId());
		assertNull(actual);
	}

	@Test
  public void bulkUploadRoleFuncUserNullTest() {
      UploadRoleFunctionExtSystem data = Mockito.mock(UploadRoleFunctionExtSystem.class);
      Mockito.when(appService.getApp(127L)).thenReturn(null);
      PortalRestResponse<String> response = roleManageController.bulkUploadRoleFunc(mockedRequest, mockedResponse, data, 127L);
      assertEquals(PortalRestStatusEnum.ERROR, response.getStatus());
      assertEquals("Unauthorized User", response.getMessage());
      assertEquals("Failure", response.getResponse());
  }
	
	public CentralV2RoleFunction mockCentralRoleFunction() {
		CentralV2RoleFunction roleFunction = new CentralV2RoleFunction();
		roleFunction.setCode("Test");
		roleFunction.setName("Test");
		roleFunction.setAppId((long) 1);
		return roleFunction;
	}

	public EPApp CentralApp() {
		EPApp app = mockApp();
		app.setCentralAuth(true);
		app.setNameSpace("com.test");
		return app;
	}

	public EPApp NonCentralApp() {
		EPApp app = mockApp();
		app.setCentralAuth(false);
		return app;
	}

}
