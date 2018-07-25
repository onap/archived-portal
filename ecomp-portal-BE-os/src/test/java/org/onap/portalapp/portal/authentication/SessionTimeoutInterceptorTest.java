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
 * 
 */
package org.onap.portalapp.portal.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.authentication.LoginStrategy;
import org.onap.portalapp.authentication.SimpleLoginStrategy;
import org.onap.portalapp.controller.EPFusionBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.interceptor.SessionTimeoutInterceptor;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.controller.FusionBaseController;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.method.HandlerMethod;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ EPUserUtils.class})
public class SessionTimeoutInterceptorTest {

	
	@Mock
	LoginStrategy loginStrategy = new SimpleLoginStrategy();
	
	@Mock
	EPFusionBaseController ePFusionBaseController = new EPFusionBaseController() {
	};
	
	@Mock
	FusionBaseController fusionBaseController;
	
	@Mock
	HandlerMethod handlerMethod;
	
	@InjectMocks
	SessionTimeoutInterceptor  sessionTimeoutInterceptor = new  SessionTimeoutInterceptor();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	
	@Test
	public void preHandleTest() throws Exception{
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, ePFusionBaseController));
	}
	
	@Test
	public void preHandleTestIfMethodIsinstanceOfHandlerMethod() throws Exception{
		
		EPUser user=new EPUser();
		user.setOrgUserId("test");
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));

		when(handlerMethod.getBean()).thenReturn(fusionBaseController);
		when(fusionBaseController.isAccessible()).thenReturn(false);
		PowerMockito.mockStatic(EPUserUtils.class);
		
		PowerMockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));
		
	}
	
	@Test
	public void preHandleTestLogout() throws Exception{
		
		EPUser user=new EPUser();
		user.setOrgUserId("test");
		when(mockedRequest.getRequestURI()).thenReturn("http://logout.html");

		when(handlerMethod.getBean()).thenReturn(fusionBaseController);
		when(fusionBaseController.isAccessible()).thenReturn(false);
		PowerMockito.mockStatic(EPUserUtils.class);
		
		PowerMockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));
		
	}
	
	@Test
	public void preHandleTestLogin() throws Exception{
		
		EPUser user=new EPUser();
		user.setOrgUserId("test");
		when(mockedRequest.getRequestURI()).thenReturn("http://login.html");

		when(handlerMethod.getBean()).thenReturn(fusionBaseController);
		when(fusionBaseController.isAccessible()).thenReturn(false);
		PowerMockito.mockStatic(EPUserUtils.class);
		
		PowerMockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		assertTrue(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));
		
	}
}
