/*package org.openecomp.portalapp.portal.controller;

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
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.EPAppService;
import org.openecomp.portalapp.portal.service.PortalAdminService;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;

public class AppsControllerExternalRequestOSTest {

	@Mock
	AdminRolesService adminRolesService;

	@Mock
	EPAppService appService;

	@Mock
	PortalAdminService portalAdminService;


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
	public void postOnboardAppExternalFieldValidatorTestTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FieldsValidator [httpStatusCode=500, errorCode=null, fields=null]");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;
		expectedOnboardingApp.name = "Test";
		expectedOnboardingApp.url = "Test";
		expectedOnboardingApp.restUrl = "Test";
		expectedOnboardingApp.restrictedApp = false;
		expectedOnboardingApp.isOpen = false;
		expectedOnboardingApp.isEnabled = false;
		List<EPUser> userList = new ArrayList<EPUser>();
		EPUser user = mockUser.mockEPUser();
		userList.add(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 500);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.addOnboardingApp(expectedOnboardingApp, user)).thenReturn(expectedFieldValidator);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}
	
	@Test
	public void postOnboardAppExternalIfSuperAdminTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;
		expectedOnboardingApp.name = "Test";
		expectedOnboardingApp.url = "Test";
		expectedOnboardingApp.restUrl = "Test";
		expectedOnboardingApp.restrictedApp = false;
		expectedOnboardingApp.isOpen = false;
		expectedOnboardingApp.isEnabled = false;
		List<EPUser> userList = new ArrayList<EPUser>();
		EPUser user = mockUser.mockEPUser();
		userList.add(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.addOnboardingApp(expectedOnboardingApp, user)).thenReturn(expectedFieldValidator);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}
}
*/