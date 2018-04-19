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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

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
import org.onap.portalapp.portal.controller.BasicAuthAccountController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPEndpoint;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.BasicAuthAccountService;
import org.onap.portalapp.portal.service.BasicAuthAccountServiceImpl;
import org.onap.portalapp.util.EPUserUtils;

public class BasicAuthAccountControllerTest extends MockitoTestSuite {

	@Mock
	BasicAuthAccountService basicAuthAccountService = new BasicAuthAccountServiceImpl();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@InjectMocks
	BasicAuthAccountController basicAuthAccountController = new BasicAuthAccountController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	MockEPUser mockUser = new MockEPUser();

	public BasicAuthCredentials basicAuthCredentials() {
		BasicAuthCredentials basicAuthCredentials = new BasicAuthCredentials();

		basicAuthCredentials.setId((long) 1);
		basicAuthCredentials.setApplicationName("test");
		basicAuthCredentials.setUsername("Test");
		basicAuthCredentials.setPassword("Password");
		basicAuthCredentials.setIsActive("YES");

		List<EPEndpoint> endpoints = new ArrayList<EPEndpoint>();

		EPEndpoint ePEndpoint = new EPEndpoint();
		ePEndpoint.setId((long) 1);
		ePEndpoint.setName("Test");
		endpoints.add(ePEndpoint);
		basicAuthCredentials.setEndpoints(endpoints);

		return basicAuthCredentials;

	}

	@Test
	public void createBasicAuthAccountTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("SUCCESS");
		expectedResponse.setResponse("");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.OK);
		long accountd = 1;

		Mockito.when(basicAuthAccountService.saveBasicAuthAccount(basicAuthCredentials)).thenReturn(accountd);

		PortalRestResponse<String> actualResponse = basicAuthAccountController.createBasicAuthAccount(mockedRequest,
				mockedResponse, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void createBasicAuthAccountAdminTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("Authorization Required");
		expectedResponse.setResponse("Admin Only Operation! ");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.ERROR);

		PortalRestResponse<String> actualResponse = basicAuthAccountController.createBasicAuthAccount(mockedRequest,
				mockedResponse, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void createBasicAuthAccountIfInputNullTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("FAILURE");
		expectedResponse.setResponse("newBasicAuthAccount cannot be null or empty");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualResponse = basicAuthAccountController.createBasicAuthAccount(mockedRequest,
				mockedResponse, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void getBasicAuthAccountTest() throws Exception {
		PortalRestResponse<List<BasicAuthCredentials>> expectedPortalResponse = new PortalRestResponse<List<BasicAuthCredentials>>();
		List<BasicAuthCredentials> basicAuthCredentialsList = new ArrayList<BasicAuthCredentials>();
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();
		basicAuthCredentialsList.add(basicAuthCredentials);

		expectedPortalResponse.setMessage("Success");
		expectedPortalResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedPortalResponse.setStatus(portalRestStatusEnum.OK);

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(basicAuthAccountService.getAccountData()).thenReturn(null);
		PortalRestResponse<List<BasicAuthCredentials>> actualResponse = basicAuthAccountController
				.getBasicAuthAccount(mockedRequest, mockedResponse);
		assertEquals(expectedPortalResponse, actualResponse);
	}

	@Test
	public void getBasicAuthAccountIfSuperAdminTest() throws Exception {
		PortalRestResponse<List<BasicAuthCredentials>> expectedPortalResponse = new PortalRestResponse<List<BasicAuthCredentials>>();
		List<BasicAuthCredentials> basicAuthCredentialsList = new ArrayList<BasicAuthCredentials>();
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();
		basicAuthCredentialsList.add(basicAuthCredentials);

		expectedPortalResponse.setMessage("UnAuthorized! Admin Only Operation");
		expectedPortalResponse.setResponse(new ArrayList<>());
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedPortalResponse.setStatus(portalRestStatusEnum.ERROR);

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(basicAuthAccountService.getAccountData()).thenReturn(null);
		PortalRestResponse<List<BasicAuthCredentials>> actualResponse = basicAuthAccountController
				.getBasicAuthAccount(mockedRequest, mockedResponse);
		assertEquals(expectedPortalResponse, actualResponse);
	}

	@Test
	public void updateAccountTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("SUCCESS");
		expectedResponse.setResponse("");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.OK);
		long accountd = 1;
		PortalRestResponse<String> actualResponse = basicAuthAccountController.updateAccount(mockedRequest,
				mockedResponse, accountd, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void updateAccountIfSuperAdminTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = basicAuthCredentials();

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("Authorization Required");
		expectedResponse.setResponse("Admin Only Operation! ");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.ERROR);
		long accountd = 1;
		PortalRestResponse<String> actualResponse = basicAuthAccountController.updateAccount(mockedRequest,
				mockedResponse, accountd, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void updateAccountIfInputNullTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = null;

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("FAILURE");
		expectedResponse.setResponse("BasicAuthCredentials cannot be null or empty");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.ERROR);
		long accountd = 1;
		PortalRestResponse<String> actualResponse = basicAuthAccountController.updateAccount(mockedRequest,
				mockedResponse, accountd, basicAuthCredentials);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void deleteAccountTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("SUCCESS");
		expectedResponse.setResponse("");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.OK);
		long accountd = 1;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> actualResponse = basicAuthAccountController.deleteAccount(mockedRequest,
				mockedResponse, accountd);
		assertEquals(actualResponse, expectedResponse);
	}

	@Test
	public void deleteAccountIfNotSuperAdminTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);

		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("Authorization Required");
		expectedResponse.setResponse("Admin Only Operation! ");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedResponse.setStatus(portalRestStatusEnum.ERROR);
		long accountd = 1;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		PortalRestResponse<String> actualResponse = basicAuthAccountController.deleteAccount(mockedRequest,
				mockedResponse, accountd);
		System.out.println(actualResponse);
		assertEquals(actualResponse, expectedResponse);
	}
}
