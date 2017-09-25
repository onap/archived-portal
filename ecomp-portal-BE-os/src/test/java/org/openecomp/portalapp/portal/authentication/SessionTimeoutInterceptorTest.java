package org.openecomp.portalapp.portal.authentication;

import static org.junit.Assert.assertFalse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.authentication.LoginStrategy;
import org.openecomp.portalapp.authentication.SimpleLoginStrategy;
import org.openecomp.portalapp.controller.EPFusionBaseController;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.interceptor.SessionTimeoutInterceptor;
import org.springframework.web.method.HandlerMethod;

public class SessionTimeoutInterceptorTest {

	
	@Mock
	LoginStrategy loginStrategy = new SimpleLoginStrategy();
	
	@Mock
	EPFusionBaseController ePFusionBaseController = new EPFusionBaseController() {
	};
	
	@Mock
	HandlerMethod handlerMethod;
	
	@InjectMocks
	SessionTimeoutInterceptor  sessionTimeoutInterceptor = new  SessionTimeoutInterceptor();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	
	@Test
	public void preHandleTest() throws Exception{
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, ePFusionBaseController));
	}
	
	@Test
	public void preHandleTestIfMethodIsinstanceOfHandlerMethod() throws Exception{
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));
	}
}
