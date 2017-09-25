package org.openecomp.portalapp.portal.test.controller;

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
import org.openecomp.portalapp.portal.controller.BasicAuthAccountController;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.BasicAuthAccountService;
import org.openecomp.portalapp.portal.service.BasicAuthAccountServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;

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
		expectedResponse.setStatus(PortalRestStatusEnum.OK);
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
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);

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
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);
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
		expectedPortalResponse.setStatus(PortalRestStatusEnum.OK);

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
		expectedPortalResponse.setStatus(PortalRestStatusEnum.ERROR);

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
		expectedResponse.setStatus(PortalRestStatusEnum.OK);
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
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);
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
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);
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
		expectedResponse.setStatus(PortalRestStatusEnum.OK);
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
		expectedResponse.setStatus(PortalRestStatusEnum.ERROR);
		long accountd = 1;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		PortalRestResponse<String> actualResponse = basicAuthAccountController.deleteAccount(mockedRequest,
				mockedResponse, accountd);
		System.out.println(actualResponse);
		assertEquals(actualResponse, expectedResponse);
	}
}
