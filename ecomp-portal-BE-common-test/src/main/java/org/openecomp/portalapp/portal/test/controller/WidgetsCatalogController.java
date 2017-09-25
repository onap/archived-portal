package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.DashboardController;
import org.openecomp.portalapp.portal.domain.WidgetCatalog;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.service.MicroserviceService;
import org.openecomp.portalapp.portal.service.MicroserviceServiceImpl;
import org.openecomp.portalapp.portal.service.WidgetParameterService;
import org.openecomp.portalapp.portal.service.WidgetParameterServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.portal.utils.CustomLoggingFilter;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class})
public class WidgetsCatalogController {

	@Mock
	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();
	
	@Mock
	MicroserviceService microserviceService = new MicroserviceServiceImpl();
	
	@Mock
	WidgetParameterService widgetParameterService = new WidgetParameterServiceImpl();
	
	@InjectMocks
	WidgetsCatalogController widgetsCatalogController = new WidgetsCatalogController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	/*public List<WidgetCatalog> getUserWidgetCatalog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginName") String loginName) throws RestClientException, Exception {
		List<WidgetCatalog> widgets = new ArrayList<>();
		try {
			CustomLoggingFilter d;
			ResponseEntity<ArrayList> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
							SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog/" + loginName,
					HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), ArrayList.class);
			widgets = ans.getBody();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserWidgetCatalog failed", e);
			// returning null because null help check on the UI if there was a
			// communication problem with Microservice.
			return null;
		}
		return widgets;
	}
	
	@Test
	public void getWidgetDataTest(){
		
		String resourceType = null;
		List<WidgetCatalog> expectedData = new ArrayList<WidgetCatalog>();
		expectedData.setStatus(PortalRestStatusEnum.ERROR);
		expectedData.setMessage("Unexpected resource type null");
		expectedData.setResponse(null);
		
		PortalRestResponse<CommonWidgetMeta> actualResponse = 	dashboardController.getWidgetData(mockedRequest, resourceType);
		assertEquals(expectedData,actualResponse);	
		
		
	}*/
}
