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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.WidgetsCatalogController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ConsulHealthService;
import org.onap.portalapp.portal.service.ConsulHealthServiceImpl;
import org.onap.portalapp.portal.service.MicroserviceService;
import org.onap.portalapp.portal.service.MicroserviceServiceImpl;
import org.onap.portalapp.portal.service.WidgetParameterService;
import org.onap.portalapp.portal.service.WidgetParameterServiceImpl;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, EcompPortalUtils.class, SystemProperties.class, EPCommonSystemProperties.class})
public class WidgetsCatalogControllerTest {

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
	@Mock
	WidgetServiceHeaders widgetServiceHeaders ;
	
	
	@Mock
	RestTemplate template = new RestTemplate();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	@Mock
	HttpEntity mockHttpEntity; 
	
	@SuppressWarnings("unchecked")
	@Test
	public void getUserWidgetCatalogTest() throws RestClientException, Exception{
		
		String resourceType = null;
		List<WidgetCatalog> widgets = new ArrayList<>();
        PowerMockito.mockStatic(EcompPortalUtils.class);	
        PowerMockito.mockStatic(SystemProperties.class);
        PowerMockito.mockStatic(EPCommonSystemProperties.class);
        Mockito.when(EcompPortalUtils.widgetMsProtocol()).thenReturn("test1");
        Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WIDGET_MS_PROTOCOL)).thenReturn("https");
//        Mockito.when(WidgetServiceHeaders.getInstance()).thenReturn(HttpHeaders.ACCEPT);
        Mockito.when(SystemProperties.getProperty("microservices.widget.local.port")).thenReturn("test");
        Mockito.when(consulHealthService.getServiceLocation("widgets-service",
							"test")).thenReturn("test.com");
        Mockito.when(new HttpEntity(WidgetServiceHeaders.getInstance())).thenReturn(mockHttpEntity);
//        HttpEntity<String> entity = new HttpEntity<String>("helloWorld");
//		ResponseEntity<ArrayList> ans = new ResponseEntity<>(statusCode);
//		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
//				Matchers.<HttpEntity<?>>any(), Matchers.eq(ArrayList.class))).thenReturn(ans);
//		List<WidgetCatalog> expectedWidgets  = 	widgetsCatalogController.getUserWidgetCatalog(mockedRequest, mockedResponse, "guestT");
	}
}
