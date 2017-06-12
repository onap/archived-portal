package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openecomp.portalapp.portal.controller.MicroserviceProxyController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.MicroserviceProxyService;
import org.openecomp.portalapp.portal.service.MicroserviceProxyServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;



public class MicroserviceProxyControllerTest extends MockitoTestSuite {

	@Mock
	MicroserviceProxyService microserviceProxyService = new MicroserviceProxyServiceImpl();

	@InjectMocks
	MicroserviceProxyController microserviceProxyController = new MicroserviceProxyController();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();
	@Mock
	ObjectMapper objectMapper = new ObjectMapper();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getMicroserviceProxyTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest)).thenReturn("Success");
		String acutualString = microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
		assertTrue(acutualString.equals("Success"));
	}

	@Test(expected = NullPointerException.class)
	public void getMicroserviceProxyNullPoniterExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest))
				.thenThrow(nullPointerException);
		microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
	}

	@Test
	public void getMicroserviceProxyExceptionTest() throws Exception {
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.OK, "Success");
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest))
				.thenThrow(httpClientErrorException);
		String acutualString = microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"\"}"));
	}

	@Test
	public void getMicroserviceProxyByWidgetIdTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenReturn("Success");
		String acutualString = microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest,
				getMockedResponse(), 1);
		assertTrue(acutualString.equals("Success"));
	}

	@Test(expected = NullPointerException.class)
	public void getMicroserviceProxyByWidgetIdNullPointerExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenThrow(nullPointerException);
		microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest, getMockedResponse(), 1);
	}

	@Test
	public void getMicroserviceProxyByWidgetIdExceptionTest() throws Exception {
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.OK, "Success");
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenThrow(httpClientErrorException);
		String acutualString = microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest,
				getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"\"}"));
	}
}
