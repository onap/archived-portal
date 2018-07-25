/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
import org.onap.portalapp.portal.controller.WebAnalyticsExtAppController;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.AppsCacheServiceImple;
import org.onap.portalapp.portal.transport.Analytics;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.AuditServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
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
	@Mock
	Analytics analytics;
	

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
	
	@Test
	public void storeAnalyticsScriptIfAnalyticsTest() throws Exception
	{
		PortalAPIResponse	expectedPortalAPIResponse = new PortalAPIResponse(true, "ok");		
		expectedPortalAPIResponse.setMessage("success");
		EPApp appRecord =new EPApp();
		appRecord.setName("test");
		Mockito.when(mockedRequest.getHeader("uebkey")).thenReturn("test");
		Mockito.when(analytics.getUserid()).thenReturn("test");
		Mockito.when(analytics.getFunction()).thenReturn("test");
		PortalAPIResponse	actualPortalAPIResponse = 	webAnalyticsExtAppController.storeAnalyticsScript(mockedRequest, analytics);
		assertTrue(expectedPortalAPIResponse.getMessage().equals(actualPortalAPIResponse.getMessage()));
		assertTrue(expectedPortalAPIResponse.getStatus().equals(actualPortalAPIResponse.getStatus()));	
	}
	
	
	@Test
	public void testGetAnalyticsScript()throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty("frontend_url")).thenReturn("http://www.ecomp.com/test");
		 webAnalyticsExtAppController.getAnalyticsScript(mockedRequest);
		
	}
		
}
