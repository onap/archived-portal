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
import org.onap.portalapp.portal.controller.WidgetsCatalogController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;
import org.onap.portalapp.portal.domain.WidgetParameterResult;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
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
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
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
	WidgetServiceHeaders widgetServiceHeaders;

	@Mock
	RestTemplate template = new RestTemplate();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getUserWidgetCatalogTest() throws RestClientException, Exception {
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
		List<WidgetCatalog> widgetsList = new ArrayList<>();
		WidgetCatalog widgetCatalog = new WidgetCatalog();
		widgetCatalog.setId(1l);
		widgetCatalog.setName("test");
		widgetsList.add(widgetCatalog);
		ResponseEntity<List> ans = new ResponseEntity<>(widgetsList, HttpStatus.OK);
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(),
				Matchers.eq(List.class))).thenReturn(ans);
		List<WidgetCatalog> expectedWidgets = widgetsCatalogController.getUserWidgetCatalog("guestT");
		assertEquals(expectedWidgets, widgetsList);
	}

	@Test
	public void getUserWidgetCatalogExceptionTest() throws RestClientException, Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.doThrow(new NullPointerException()).when(template).exchange(Matchers.anyString(),
				Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(), Matchers.eq(List.class));
		List<WidgetCatalog> expectedWidgets = widgetsCatalogController.getUserWidgetCatalog("guestT");
		assertNull(expectedWidgets);
	}

	@Test
	public void getWidgetCatalogTest() throws CipherUtilException {
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
		List<WidgetCatalog> widgetsList = new ArrayList<>();
		WidgetCatalog widgetCatalog = new WidgetCatalog();
		widgetCatalog.setId(1l);
		widgetCatalog.setName("test");
		widgetsList.add(widgetCatalog);
		ResponseEntity<List> ans = new ResponseEntity<>(widgetsList, HttpStatus.OK);
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(),
				Matchers.eq(List.class))).thenReturn(ans);
		List<WidgetCatalog> expectedWidgets = widgetsCatalogController.getWidgetCatalog();
		assertEquals(expectedWidgets, widgetsList);
	}

	@Test
	public void getWidgetCatalogExceptionTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.doThrow(new NullPointerException()).when(template).exchange(Matchers.anyString(),
				Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(), Matchers.eq(List.class));
		List<WidgetCatalog> expectedWidgets = widgetsCatalogController.getUserWidgetCatalog("guestT");
		assertNull(expectedWidgets);
	}

	@Test
	public void updateWidgetCatalogTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		ResponseEntity<List> ans = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.PUT), Matchers.<HttpEntity<?>>any(),
				Matchers.eq(List.class))).thenReturn(ans);
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1l);
		widgetsCatalogController.updateWidgetCatalog(widget, 1);
	}

	@Test
	public void deleteOnboardingWidgetTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		ResponseEntity<List> ans = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<?>>any(), Matchers.eq(List.class))).thenReturn(ans);
		widgetsCatalogController.deleteOnboardingWidget(1l);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateWidgetCatalogWithFilesTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		String ans = "success";
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class),
				Mockito.any(Class.class))).thenReturn(ans);
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		byte[] mockData = "test".getBytes();
		String originalFilename = "Test_File.zip";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", originalFilename, "application/zip",
				mockData);
		request.addFile(mockMultipartFile);
		String actual = widgetsCatalogController.updateWidgetCatalogWithFiles(request, 1l);
		assertEquals(ans, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createWidgetCatalogTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		String ans = "success";
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class),
				Mockito.any(Class.class))).thenReturn(ans);
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		byte[] mockData = "test".getBytes();
		String originalFilename = "Test_File.zip";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", originalFilename, "application/zip",
				mockData);
		request.addFile(mockMultipartFile);
		String actual = widgetsCatalogController.createWidgetCatalog(request);
		assertEquals(ans, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetFrameworkTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.getForObject(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(String.class)))
				.thenReturn("test123");
		String result = widgetsCatalogController.getWidgetFramework(1l);
		assertNull(result);
	}

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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.getForObject(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(String.class)))
				.thenReturn("test123");
		String result = widgetsCatalogController.getWidgetController(1);
		assertNull(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetCSSTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.getForObject(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(String.class)))
				.thenReturn("test123");
		String result = widgetsCatalogController.getWidgetCSS(1);
		assertNull(result);
	}

	@Test
	public void getWidgetParameterResultTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		MockHttpServletRequest request = new MockHttpServletRequest();
		Mockito.when(EPUserUtils.getUserSession(request)).thenReturn(user);
		Mockito.when(EcompPortalUtils.widgetMsProtocol()).thenReturn("test1");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.WIDGET_MS_PROTOCOL)).thenReturn("https");
		Mockito.when(SystemProperties.getProperty("microservices.widget.local.port")).thenReturn("test");
		Mockito.when(EcompPortalUtils.getPropertyOrDefault("microservices.widget.username", "widget_user"))
				.thenReturn("test");
		Mockito.when(CipherUtil
				.decryptPKC(EcompPortalUtils.getPropertyOrDefault("microservices.widget.password", "widget_password")))
				.thenReturn("abc");
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		ResponseEntity<Long> ans = new ResponseEntity<>(1l, HttpStatus.OK);
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(),
				Matchers.eq(Long.class))).thenReturn(ans);
		List<MicroserviceParameter> defaultParam = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		MicroserviceParameter microserviceParameter2 = new MicroserviceParameter();
		microserviceParameter2.setId(2l);
		microserviceParameter2.setPara_key("test2");
		defaultParam.add(microserviceParameter);
		defaultParam.add(microserviceParameter2);
		Mockito.when(microserviceService.getParametersById(1)).thenReturn(defaultParam);
		Mockito.when(widgetParameterService.getUserParamById(1l, user.getId(), 1l)).thenReturn(null);
		WidgetCatalogParameter userValue = new WidgetCatalogParameter();
		userValue.setUser_value("test123");
		Mockito.when(widgetParameterService.getUserParamById(1l, user.getId(), 2l)).thenReturn(userValue);
		PortalRestResponse<List<WidgetParameterResult>> actual = widgetsCatalogController
				.getWidgetParameterResult(request, 1);
		PortalRestResponse<List<WidgetParameterResult>> expected = new PortalRestResponse<List<WidgetParameterResult>>(
				PortalRestStatusEnum.OK, "SUCCESS", new ArrayList<>());
		assertEquals(expected.getStatus(), actual.getStatus());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void doDownloadTest() throws Exception {
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
		Mockito.when(consulHealthService.getServiceLocation("widgets-service", "test")).thenReturn("test.com");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResponseEntity<byte[]> mockData = new ResponseEntity("testfile.zip".getBytes(), HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET), Matchers.<HttpEntity<?>>any(),
				Matchers.eq(byte[].class))).thenReturn(mockData);
		widgetsCatalogController.doDownload(request, response, 1l);
	}

	@Test
	public void saveWidgetParameterTest() {
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		WidgetCatalogParameter widgetCatalogParameter = new WidgetCatalogParameter();
		widgetCatalogParameter.setId(1l);
		widgetCatalogParameter.setParamId(1l);
		widgetCatalogParameter.setUserId(user.getId());
		widgetCatalogParameter.setUser_value("test123");
		MockHttpServletRequest request = new MockHttpServletRequest();
		Mockito.when(EPUserUtils.getUserSession(request)).thenReturn(user);
		Mockito.when(widgetParameterService.getUserParamById(widgetCatalogParameter.getWidgetId(),
				widgetCatalogParameter.getUserId(), widgetCatalogParameter.getParamId())).thenReturn(widgetCatalogParameter);
		PortalRestResponse<String> response = widgetsCatalogController.saveWidgetParameter(request,
				widgetCatalogParameter);
		PortalRestResponse<String> expected = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
		assertEquals(expected.getMessage(), response.getMessage());
	}
	
	@Test
	public void saveWidgetParameterExceptionTest() {
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		WidgetCatalogParameter widgetCatalogParameter = new WidgetCatalogParameter();
		widgetCatalogParameter.setId(1l);
		widgetCatalogParameter.setParamId(1l);
		widgetCatalogParameter.setUserId(user.getId());
		widgetCatalogParameter.setUser_value("test123");
		MockHttpServletRequest request = new MockHttpServletRequest();
		Mockito.when(EPUserUtils.getUserSession(request)).thenReturn(user);
		Mockito.doThrow(new NullPointerException()).when(widgetParameterService).getUserParamById(widgetCatalogParameter.getWidgetId(),
				widgetCatalogParameter.getUserId(), widgetCatalogParameter.getParamId());
		PortalRestResponse<String> response = widgetsCatalogController.saveWidgetParameter(request,
				widgetCatalogParameter);
		PortalRestResponse<String> expected = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", "");
		assertEquals(expected.getMessage(), response.getMessage());
	}
}
