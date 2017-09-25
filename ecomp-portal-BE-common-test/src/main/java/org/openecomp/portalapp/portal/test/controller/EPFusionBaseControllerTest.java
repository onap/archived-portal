package org.openecomp.portalapp.portal.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.EPFusionBaseController;
import org.openecomp.portalapp.portal.controller.DashboardController;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, SystemProperties.class})
public class EPFusionBaseControllerTest {

	@Mock
	DashboardSearchService searchService = new DashboardSearchServiceImpl();
	
	@InjectMocks
	EPFusionBaseController epFusionBaseController; /*= mock(EPFusionBaseController.class);*/

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
	public void messagesTest(){
		Map<String, Object> expectedData = new HashMap<String, Object>();
		Map<String, Object> actualData = null;
		
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME)).then(null);
		actualData = epFusionBaseController.messages(mockedRequest);
		System.out.println();
		
	}
}
