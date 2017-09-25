//package org.openecomp.portalapp.portal.controller;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openecomp.portalapp.portal.core.MockEPUser;
//import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
//import org.openecomp.portalapp.portal.service.ConsulHealthService;
//import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
//import org.openecomp.portalapp.portal.service.MicroserviceService;
//import org.openecomp.portalapp.portal.service.MicroserviceServiceImpl;
//import org.openecomp.portalapp.portal.service.WidgetParameterService;
//import org.openecomp.portalapp.portal.service.WidgetParameterServiceImpl;
//import org.openecomp.portalapp.util.EPUserUtils;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({EPUserUtils.class})
//public class WidgetsCatalogControllerTest {
//
//	@Mock
//	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();
//	
//	@Mock
//	MicroserviceService microserviceService = new MicroserviceServiceImpl();
//	
//	@Mock
//	WidgetParameterService widgetParameterService = new WidgetParameterServiceImpl();
//	
//	@InjectMocks
//	WidgetsCatalogController widgetsCatalogController = new WidgetsCatalogController();
//
//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
//
//	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
//	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
//
//	NullPointerException nullPointerException = new NullPointerException();
//	
//	MockEPUser mockUser = new MockEPUser();
//	
//	/*public List<WidgetCatalog> getUserWidgetCatalog(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable("loginName") String loginName) throws RestClientException, Exception {
//		List<WidgetCatalog> widgets = new ArrayList<>();
//		try {
//			CustomLoggingFilter d;
//			ResponseEntity<ArrayList> ans = template.exchange(
//					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
//							SystemProperties.getProperty("microservices.widget.local.port"))
//							+ "/widget/microservices/widgetCatalog/" + loginName,
//					HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), ArrayList.class);
//			widgets = ans.getBody();
//		} catch (Exception e) {
//			logger.error(EELFLoggerDelegate.errorLogger, "getUserWidgetCatalog failed", e);
//			// returning null because null help check on the UI if there was a
//			// communication problem with Microservice.
//			return null;
//		}
//		return widgets;
//	}
//	
//	@Test
//	public void getWidgetDataTest(){
//		
//		String resourceType = null;
//		List<WidgetCatalog> expectedData = new ArrayList<WidgetCatalog>();
//		expectedData.setStatus(PortalRestStatusEnum.ERROR);
//		expectedData.setMessage("Unexpected resource type null");
//		expectedData.setResponse(null);
//		
//		PortalRestResponse<CommonWidgetMeta> actualResponse = 	dashboardController.getWidgetData(mockedRequest, resourceType);
//		assertEquals(expectedData,actualResponse);	
//		
//		
//	}*/
//}
