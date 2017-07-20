package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.WidgetsCatalogMarkupController;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WidgetServiceHeaders.class)
public class WidgetsCatalogMarkupControllerTest extends MockitoTestSuite {
	
	@InjectMocks
	WidgetsCatalogMarkupController widgetsCatalogMarkupController = new WidgetsCatalogMarkupController();
	
	@Mock
	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();
	
	@Mock
	RestTemplate template = new RestTemplate();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    @Mock
    CipherUtil cipherUtil= new CipherUtil();
    
    @Mock
    EcompPortalUtils EcompPortalUtils =new EcompPortalUtils();
	
	@Mock
	WidgetServiceHeaders WidgetServiceHeaders ;
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@SuppressWarnings("static-access")
	@Test
	public void getWidgetMarkupTest() throws RestClientException, Exception
	{
		String whatService = "widgets-service";
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		Mockito.when(template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService, null) + "/widget/microservices/markup/" + 1, String.class,
				WidgetServiceHeaders.getInstance())).thenReturn("Success");
		String response = widgetsCatalogMarkupController.getWidgetMarkup(mockedRequest, mockedResponse, 1);
		assertTrue(response.equals("Success"));	
	}
	
}
