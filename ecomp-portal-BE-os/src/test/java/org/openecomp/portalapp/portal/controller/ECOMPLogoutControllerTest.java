package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.*;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.openecomp.portalapp.controller.ECOMPLogoutController;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.framework.MockEPUser;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, EPCommonSystemProperties.class,RequestContextHolder.class,RequestAttributes.class})
public class ECOMPLogoutControllerTest {

	@Mock
	DashboardSearchService searchService = new DashboardSearchServiceImpl();
	
	@InjectMocks
	ECOMPLogoutController ecompLogoutController = new ECOMPLogoutController();

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
	RequestContextHolder requestContextHolder;
	
	@Mock
	RequestAttributes requestAttributes;
	
	@Test
	public void logOutTest() throws Exception{
		ModelAndView actualData = new ModelAndView("redirect:login.htm");
		ModelAndView expedtedData = null;
		ThreadLocal<RequestAttributes> requestAttributesHolder =
				new NamedThreadLocal<RequestAttributes>("Request attributes");
		RequestAttributes requestAttributes = new ServletRequestAttributes(mockedRequest);
		PowerMockito.mockStatic(RequestContextHolder.class);
		PowerMockito.mockStatic(RequestAttributes.class);
	    Mockito.when((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).thenReturn((ServletRequestAttributes) requestAttributes);
		expedtedData = ecompLogoutController.logOut(mockedRequest, mockedResponse);
		assertEquals(actualData.getViewName(),expedtedData.getViewName());
	}
	
}
