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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.authentication.LoginStrategy;
import org.onap.portalapp.authentication.SimpleLoginStrategy;
import org.onap.portalapp.controller.EPFusionBaseController;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.interceptor.SessionTimeoutInterceptor;
import org.springframework.web.method.HandlerMethod;

public class SessionTimeoutInterceptorTest {

	
	@Mock
	LoginStrategy loginStrategy = new SimpleLoginStrategy();
	
	@Mock
	EPFusionBaseController ePFusionBaseController = new EPFusionBaseController() {
	};
	
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
		assertFalse(sessionTimeoutInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod));
	}
}
