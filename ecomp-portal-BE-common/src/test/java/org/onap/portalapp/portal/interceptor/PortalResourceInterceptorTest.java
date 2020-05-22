/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.interceptor;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.BasicAuthenticationController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalsdk.core.controller.FusionBaseController;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.method.HandlerMethod;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemProperties.class })
public class PortalResourceInterceptorTest {
	
	@InjectMocks
	PortalResourceInterceptor portalResourceInterceptor;
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HandlerMethod handler;
	@Mock
	HttpSession session;
	@Mock
	FusionBaseController fusionBaseController;
	@Mock
	BasicAuthenticationController basicAuthenticationController;
	@Mock
	private ExternalAccessRolesService externalAccessRolesService;
	@Mock
	PrintWriter printWriter;
	
	MockEPUser mockUser = new MockEPUser();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(SystemProperties.class);
		
	}
	
	@Test
	public void testPreHandle()throws Exception {
		Set<String> data=new HashSet<>();
		data.add("test");
		when(request.getRequestURI()).thenReturn("test/portalApi/test");
		
		when(session.getAttribute(anyString())).thenReturn(data);
		when(request.getSession()).thenReturn(session);
		when(fusionBaseController.isAccessible()).thenReturn(true);
		when(handler.getBean()).thenReturn(fusionBaseController);
		
		
	boolean result=	portalResourceInterceptor.preHandle(request, response, handler);
	assertEquals(true, result);
		
		
	}
	
	@Ignore
	@Test
	public void testPreHandlePass()throws Exception {
		Set<String> data=new HashSet<>();
		data.add("test/test");
		Set<String> allFunctions=new HashSet<>();
		allFunctions.add("test/test");
		when(request.getRequestURI()).thenReturn("test/portalApi/test/test");
		EPUser user = mockUser.mockEPUser();
		when(request.getSession()).thenReturn(session);
		when(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME)).thenReturn("role_functions_attribute_name");
		when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("user_attribute_name");
		when(session.getAttribute("user_attribute_name")).thenReturn(user);
		when(session.getAttribute("role_functions_attribute_name")).thenReturn(data);
		when(session.getAttribute("allRoleFunctions")).thenReturn(allFunctions);
		when(fusionBaseController.isAccessible()).thenReturn(false);
		when(handler.getBean()).thenReturn(fusionBaseController);
		
		
	boolean result=	portalResourceInterceptor.preHandle(request, response, handler);
	
	assertEquals(true, result);
		
	}
	
	

	@Test
	public void testPreHandleAuth()throws Exception {
		Set<String> data=new HashSet<>();
		data.add("test/test");
		List<EPApp> apps=new ArrayList<>();
		EPApp app=new EPApp();
		app.setAppBasicAuthUsername("test");
		apps.add(app);
		when(request.getRequestURI()).thenReturn("test/portalApi/test/test");
		
		when(request.getHeader("Authorization")).thenReturn("Basictest");
		when(request.getHeader("uebkey")).thenReturn("test");
				when(request.getSession()).thenReturn(session);
		when(fusionBaseController.isAccessible()).thenReturn(false);
		when(handler.getBean()).thenReturn(basicAuthenticationController);
		when( externalAccessRolesService.getApp("test")).thenReturn(apps);
		when(response.getWriter()).thenReturn(printWriter);
		
	boolean result=	portalResourceInterceptor.preHandle(request, response, handler);
	
	assertEquals(false, result);
		
	}
	
	

}
