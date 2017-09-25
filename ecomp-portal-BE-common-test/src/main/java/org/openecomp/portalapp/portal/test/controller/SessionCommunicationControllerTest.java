package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.core.command.assertion.AssertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.sessionmgt.SessionCommunicationController;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.service.sessionmgt.ManageService;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;

public class SessionCommunicationControllerTest {
	

	@Mock
	ManageService manageService;

	@InjectMocks
	SessionCommunicationController SessionCommunicationController = new SessionCommunicationController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	MockEPUser mockUser = new MockEPUser();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	
	@Test
	public void getSessionSlotCheckIntervalTest() throws Exception
	{
		Mockito.when(manageService.fetchSessionSlotCheckInterval()).thenReturn(1);
		int result = SessionCommunicationController.getSessionSlotCheckInterval(mockedRequest, mockedResponse);
		assertEquals(result, 1);
		
	}

	@Test
	public void extendSessionTimeOutsTest() throws Exception
	{
		Mockito.doNothing().when(manageService).extendSessionTimeOuts("test");
		Boolean result = SessionCommunicationController.extendSessionTimeOuts(mockedRequest, mockedResponse, "test");
		assertEquals(result, true);
		
	}
}
