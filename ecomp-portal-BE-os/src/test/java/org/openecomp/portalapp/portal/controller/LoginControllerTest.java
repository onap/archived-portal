package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.LoginController;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.service.EPRoleFunctionService;
import org.openecomp.portalapp.portal.service.EPRoleService;
import org.openecomp.portalapp.portal.service.SharedContextService;
import org.openecomp.portalapp.service.EPProfileService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.servlet.ModelAndView;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SystemProperties.class)
public class LoginControllerTest {

	@Mock
	EPProfileService service;
	@Mock
    EPLoginService loginService;
	@Mock
	 SharedContextService sharedContextService;
	@Mock
	 EPRoleService roleService;
	@Mock
	 EPRoleFunctionService ePRoleFunctionService;
	
	@InjectMocks
	LoginController loginController = new LoginController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void loginIfAuthNullTest()
	{
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn(null);
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(),"openIdLogin") ;
	}
	
	@Test
	public void loginIfAuthOIDCTest()
	{
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(),"openIdLogin") ;
	}
	
	@Test
	public void loginTest()
	{
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("Test");
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(),"login") ;
	}
}
