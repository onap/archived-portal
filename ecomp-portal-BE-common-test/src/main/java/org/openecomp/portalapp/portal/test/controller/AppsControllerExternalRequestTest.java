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
import org.openecomp.portalapp.portal.controller.AppsControllerExternalRequest;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.EPAppService;
import org.openecomp.portalapp.portal.service.PortalAdminService;
import org.openecomp.portalapp.portal.service.UserService;
import org.openecomp.portalapp.portal.service.UserServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;

public class AppsControllerExternalRequestTest extends MockitoTestSuite {

	@Mock
	AdminRolesService adminRolesService;

	@Mock
	EPAppService appService;

	@Mock
	PortalAdminService portalAdminService;

	@Mock
	UserService userService = new UserServiceImpl();

	@InjectMocks
	AppsControllerExternalRequest appsControllerExternalRequest = new AppsControllerExternalRequest();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();
	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Test
	public void postPortalAdminIfUSerNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Missing required field: email, loginId, or loginPwd");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("java.lang.NullPointerException");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenThrow(nullPointerException);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfNotFoundTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfFoundTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfNotSuperAdminTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfFieldValidatorErrorTest() throws Exception {
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 500);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FieldsValidator [httpStatusCode=500, errorCode=null, fields=null]");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getOnboardAppExternalTest() {
		EPApp epApp = new EPApp();
		Long appId = (long) 1;
		Mockito.when(appService.getApp(appId)).thenReturn(epApp);
		OnboardingApp expectedApp = new OnboardingApp();
		Mockito.doNothing().when(appService).createOnboardingFromApp(epApp, expectedApp);
		OnboardingApp actualApp = appsControllerExternalRequest.getOnboardAppExternal(mockedRequest, mockedResponse,
				appId);
		assertEquals(expectedApp.getClass(), actualApp.getClass());
	}

	@Test
	public void postOnboardAppExternalExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Unexpected field: id");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);

		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = (long) 1;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postOnboardAppExternalTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(
				"Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);

		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void putOnboardAppExternalifAppNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Unexpected value for field: id");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		Long appId = null;
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void putOnboardAppExternalIfOnboardingAppDetailsNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(
				"Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		Long appId = (long) 1;
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = (long) 1;
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
}
