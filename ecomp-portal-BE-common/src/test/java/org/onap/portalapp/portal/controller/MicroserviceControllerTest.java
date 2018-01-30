/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.controller;

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
import org.onap.portalapp.portal.controller.MicroserviceController;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ConsulHealthService;
import org.onap.portalapp.portal.service.ConsulHealthServiceImpl;
import org.onap.portalapp.portal.service.MicroserviceService;
import org.onap.portalapp.portal.service.MicroserviceServiceImpl;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.util.SystemProperties;
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
@PrepareForTest({WidgetServiceHeaders.class, EcompPortalUtils.class})
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
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
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
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.createMicroservice(mockedRequest,
				mockedResponse, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void createMicroserviceExceptionTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
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
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
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
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		PortalRestResponse<String> actualportalRestResponse = microserviceController.updateMicroservice(mockedRequest,
				mockedResponse, 1, microserviceData);
		assertEquals(actualportalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void updateMicroserviceExceptionTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FAILURE");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
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
		PowerMockito.mockStatic(EcompPortalUtils.class);
		expectedportalRestResponse.setResponse(
				"I/O error on GET request for \""  + EcompPortalUtils.widgetMsProtocol() + "://null/widget/microservices/widgetCatalog/service/1\":null; nested exception is java.net.UnknownHostException: null");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
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
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.WARN);
		List<WidgetCatalog> List = new ArrayList<WidgetCatalog>();
		WidgetCatalog widgetCatalog = new WidgetCatalog();
		widgetCatalog.setId(1);
		WidgetCatalog widgetCatalog1 = new WidgetCatalog();
		widgetCatalog.setId(2);
		List.add(widgetCatalog);
		List.add(widgetCatalog1);
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
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

	@SuppressWarnings("unchecked")
	@Test
	public void deleteMicroserviceWhenNoWidgetsAssociatedTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("SUCCESS");
		expectedportalRestResponse.setResponse("");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		List<WidgetCatalog> List = new ArrayList<WidgetCatalog>();
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
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
