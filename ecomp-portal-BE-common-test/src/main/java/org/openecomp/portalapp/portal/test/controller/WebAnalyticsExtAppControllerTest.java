package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.WebAnalyticsExtAppController;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.AppsCacheService;
import org.openecomp.portalapp.portal.service.AppsCacheServiceImple;
import org.openecomp.portalapp.portal.transport.Analytics;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.service.AuditServiceImpl;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class,IOUtils.class,Object.class})
public class WebAnalyticsExtAppControllerTest {

	
	
	@InjectMocks
	WebAnalyticsExtAppController webAnalyticsExtAppController = new WebAnalyticsExtAppController();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();
	
	@Mock
	AppsCacheService appCacheService = new AppsCacheServiceImple();

	@Mock
	AuditService auditService = new AuditServiceImpl();

//	@Mock 
//	InputStream analyticsFileStream;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	/*@Test
	public void getAnalyticsScriptTest() throws Exception
	{
		String expectedResponse = "http://www.ecomp.com";

		InputStream analyticsFileStream = null;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(IOUtils.class);
		Mockito.when(IOUtils.toString(analyticsFileStream, StandardCharsets.UTF_8.name())).thenReturn("PORTAL_ENV_URL");
		Mockito.when(SystemProperties.getProperty("frontend_url")).thenReturn("http://www.ecomp.com/test");
		String actualResponse = webAnalyticsExtAppController.getAnalyticsScript(mockedRequest);
//	assertNull(webAnalyticsExtAppController.getAnalyticsScript(mockedRequest));
		
	//	System.out.println(actualResponse);
		assertTrue(actualResponse.equals(expectedResponse));	
	}*/

	/*@Test
	public void getAnalyticsScriptExceptionTest() throws Exception
	{
		String expectedResponse = "";
		InputStream analyticsFileStream = null;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(IOUtils.class);
		Mockito.when(IOUtils.toString(analyticsFileStream, StandardCharsets.UTF_8.name())).thenThrow(nullPointerException);
		Mockito.when(SystemProperties.getProperty("frontend_url")).thenReturn("http://www.ecomp.com/test");
       String actualResponse = webAnalyticsExtAppController.getAnalyticsScript(mockedRequest);
	  assertEquals(actualResponse,expectedResponse);
	}*/

	@Test
	public void storeAnalyticsScriptIfAnalyticsNullTest() throws Exception
	{
		PortalAPIResponse	expectedPortalAPIResponse = new PortalAPIResponse(true, "error");
		Analytics analytics= null;
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn(null);
		PortalAPIResponse	actualPortalAPIResponse = 	webAnalyticsExtAppController.storeAnalyticsScript(mockedRequest, analytics);
		assertTrue(expectedPortalAPIResponse.getMessage().equals(actualPortalAPIResponse.getMessage()));
		assertTrue(expectedPortalAPIResponse.getStatus().equals(actualPortalAPIResponse.getStatus()));	
	}
		
}
