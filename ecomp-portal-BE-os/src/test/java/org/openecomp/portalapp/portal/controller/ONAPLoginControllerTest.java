package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.ONAPLoginController;
import org.openecomp.portalapp.portal.framework.MockEPUser;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.auth.LoginStrategy;
import org.openecomp.portalsdk.core.service.LoginService;
import org.openecomp.portalsdk.core.service.ProfileService;
import org.openecomp.portalsdk.core.service.ProfileServiceImpl;
import org.springframework.web.servlet.ModelAndView;

public class ONAPLoginControllerTest {	
	
	@Mock
	ProfileService ProfileService = new ProfileServiceImpl();
	
	@Mock
	LoginService mockLoginService;
	
	@Mock
	LoginStrategy loginStrategy ;
	
	@InjectMocks
	ONAPLoginController oNAPLoginController = new ONAPLoginController();

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
	public void doLogin() throws Exception
	{
		ModelAndView expectedModelandView = null;
		ModelAndView actualModelandView = null;
		Mockito.when(loginStrategy.doLogin(mockedRequest, mockedResponse)).thenReturn(expectedModelandView);
		actualModelandView= oNAPLoginController.doLogin(mockedRequest, mockedResponse);
		assertEquals(actualModelandView,expectedModelandView);
	}
	
	
	@Test
	public void ViewTest() throws Exception
	{
		ModelAndView expectedModelandView = new ModelAndView();
		expectedModelandView.setViewName("testView");
		ModelAndView actualModelandView = null;
		Mockito.when(loginStrategy.doLogin(mockedRequest, mockedResponse)).thenReturn(expectedModelandView);
		actualModelandView= oNAPLoginController.doLogin(mockedRequest, mockedResponse);
		assertEquals(actualModelandView.getViewName(),expectedModelandView.getViewName());
	}
	@Test
	public void getJessionIdTest() throws Exception
	{
	 assertNull(oNAPLoginController.getJessionId(mockedRequest));
	}
	
	@Test
	public void getLoginServiceTest() throws Exception
	{
		LoginService expectedLoginService =     oNAPLoginController.getLoginService();
		assertEquals(mockLoginService,expectedLoginService);
	}
	
	
	
}
