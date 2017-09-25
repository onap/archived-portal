package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.ONAPWelcomeController;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;

public class ONAPWelcomeControllerTest {

	@InjectMocks
	ONAPWelcomeController oNAPWelcomeController = new ONAPWelcomeController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void getIndexPageTest()
	{
		assertEquals(oNAPWelcomeController.getIndexPage(mockedRequest), "/index");
	}
	
	@Test
	public void getEcompSinglePageTest()
	{
		assertEquals(oNAPWelcomeController.getEcompSinglePage(mockedRequest, mockedResponse), "forward:/index.html");
	}
	
	@Test
	public void userTest()
	{
		assertEquals(oNAPWelcomeController.user(null), "oid-user");
	}
   
}
