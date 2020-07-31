/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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
 * 
 */
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;
import org.onap.portalapp.portal.domain.WidgetParameterResult;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.MicroserviceServiceImpl;
import org.onap.portalapp.portal.service.WidgetMService;
import org.onap.portalapp.portal.service.WidgetMServiceImpl;
import org.onap.portalapp.portal.service.WidgetParameterServiceImpl;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("rawtypes")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EPUserUtils.class, CipherUtil.class, EcompPortalUtils.class, SystemProperties.class,
		EPCommonSystemProperties.class, EPUserUtils.class })
public class WidgetsCatalogUnRestrictedControllerTest {

	@Mock
	WidgetMService widgetMService = new WidgetMServiceImpl();

	@InjectMocks
	WidgetsCatalogUnRestrictedController widgetsCatalogControllerTest = new WidgetsCatalogUnRestrictedController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	RestTemplate template = new RestTemplate();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();


	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetControllerTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(CipherUtil.class);
		Mockito.when(EcompPortalUtils.widgetMsProtocol()).thenReturn("test1");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WIDGET_MS_PROTOCOL)).thenReturn("https");
		Mockito.when(SystemProperties.getProperty("microservices.widget.local.port")).thenReturn("test");
		Mockito.when(EcompPortalUtils.getPropertyOrDefault("microservices.widget.username", "widget_user"))
				.thenReturn("test");
		Mockito.when(CipherUtil
				.decryptPKC(EcompPortalUtils.getPropertyOrDefault("microservices.widget.password", "widget_password")))
				.thenReturn("abc");
		Mockito.when(widgetMService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.getForObject(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(String.class)))
				.thenReturn("test123");
		String result = widgetsCatalogControllerTest.getWidgetController(1);
		assertNull(result);
	}

	
	
}
