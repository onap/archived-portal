package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.MicroserviceController;
import org.openecomp.portalapp.portal.domain.MicroserviceData;
import org.openecomp.portalapp.portal.domain.WidgetCatalog;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
import org.openecomp.portalapp.portal.service.MicroserviceService;
import org.openecomp.portalapp.portal.service.MicroserviceServiceImpl;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WidgetServiceHeaders.class)
public class MicroserviceControllerTest extends MockitoTestSuite{

	@InjectMocks
	MicroserviceController microserviceController = new MicroserviceController();

	@Mock
	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();

	@Mock
	MicroserviceService microserviceService = new MicroserviceServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@Mock
	MicroserviceData microserviceData = new MicroserviceData();

	@SuppressWarnings("rawtypes")
	@Mock
	ResponseEntity<List<WidgetCatalog>> ans = new ResponseEntity<List<WidgetCatalog>>(HttpStatus.OK);

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	EcompPortalUtils EcompPortalUtils = new EcompPortalUtils();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void createMicroserviceIfServiceDataNullTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse("MicroserviceData cannot be null or empty");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		MicroserviceData microserviceData = null;
		PortalRestResponse<String> actualportalRestResponse = microserviceController.createMicroservice(mockedRequest,
				mockedResponse, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void createMicroserviceTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("SUCCESS");
		expectedportalRestResponse.setResponse("");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.createMicroservice(mockedRequest,
				mockedResponse, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void createMicroserviceExceptionTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse(null);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(microserviceService.saveMicroservice(microserviceData)).thenReturn((long) 1);
		Mockito.when(microserviceData.getParameterList()).thenThrow(nullPointerException);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.createMicroservice(mockedRequest,
				mockedResponse, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getMicroserviceTest() throws Exception {
		Mockito.when(microserviceService.getMicroserviceData()).thenReturn(null);
		List<MicroserviceData> list = microserviceController.getMicroservice(mockedRequest, mockedResponse);
		assertEquals(list, null);
	}

	@Test
	public void updateMicroserviceIfServiceISNullTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse("MicroserviceData cannot be null or empty");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		MicroserviceData microserviceData = null;
		PortalRestResponse<String> actualportalRestResponse = microserviceController.updateMicroservice(mockedRequest,
				mockedResponse, 1, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void updateMicroserviceTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("SUCCESS");
		expectedportalRestResponse.setResponse("");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.updateMicroservice(mockedRequest,
				mockedResponse, 1, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void updateMicroserviceExceptionTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse(null);
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(microserviceController.updateMicroservice(mockedRequest, mockedResponse, 1, microserviceData))
				.thenThrow(nullPointerException);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.updateMicroservice(mockedRequest,
				mockedResponse, 1, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void deleteMicroserviceExceptionTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse(
				"I/O error on GET request for \""  + EcompPortalUtils.widgetMsProtocol() + "://null/widget/microservices/widgetCatalog/service/1\":null; nested exception is java.net.UnknownHostException: null");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		PortalRestResponse<String> actuaPportalRestResponse = microserviceController.deleteMicroservice(mockedRequest,
				mockedResponse, 1);
		assertEquals(actuaPportalRestResponse.getStatus(), expectedportalRestResponse.getStatus());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deleteMicroserviceTest() throws Exception {
		String HTTPS = "https://";
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("SOME WIDGETS ASSOICATE WITH THIS SERVICE");
		expectedportalRestResponse.setResponse("'null' ,'null' ");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.WARN);
		List<WidgetCatalog> List = new ArrayList<WidgetCatalog>();
		WidgetCatalog widgetCatalog = new WidgetCatalog();
		widgetCatalog.setId(1);
		WidgetCatalog widgetCatalog1 = new WidgetCatalog();
		widgetCatalog.setId(2);
		List.add(widgetCatalog);
		List.add(widgetCatalog1);
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		String whatService = "widgets-service";
		Mockito.when(consulHealthService.getServiceLocation(whatService, SystemProperties.getProperty("microservices.widget.local.port"))).thenReturn("Test");
		Mockito.when(ans.getBody()).thenReturn(List);
		ParameterizedTypeReference<List<WidgetCatalog>> typeRef = new ParameterizedTypeReference<List<WidgetCatalog>>() {
		};
		Mockito.when(template.exchange(
				HTTPS + consulHealthService.getServiceLocation(whatService, SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/service/" + 1,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), typeRef)).thenReturn(ans);

		PortalRestResponse<String> actuaPportalRestResponse = microserviceController.deleteMicroservice(mockedRequest,
				mockedResponse, 1);
		assertEquals(actuaPportalRestResponse, expectedportalRestResponse);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deleteMicroserviceWhenNoWidgetsAssociatedTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("SUCCESS");
		expectedportalRestResponse.setResponse("");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		List<WidgetCatalog> List = new ArrayList<WidgetCatalog>();
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		String whatService = "widgets-service";
		Mockito.when(consulHealthService.getServiceLocation(whatService, SystemProperties.getProperty("microservices.widget.local.port"))).thenReturn("Test");
		Mockito.when(ans.getBody()).thenReturn(List);
		ParameterizedTypeReference<List<WidgetCatalog>> typeRef = new ParameterizedTypeReference<List<WidgetCatalog>>() {
		};
		Mockito.when(template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService, SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/service/" + 1,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), typeRef)).thenReturn(ans);
		PortalRestResponse<String> actuaPportalRestResponse = microserviceController.deleteMicroservice(mockedRequest,
				mockedResponse, 1);
		assertEquals(actuaPportalRestResponse, expectedportalRestResponse);
	}
}
