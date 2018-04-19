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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.controller.ONAPLoginController;
import org.onap.portalapp.portal.framework.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.service.LoginService;
import org.onap.portalsdk.core.service.ProfileService;
import org.onap.portalsdk.core.service.ProfileServiceImpl;
import org.springframework.web.servlet.ModelAndView;

public class ONAPLoginControllerTest {	
	
	@Mock
	ProfileService ProfileService = new ProfileServiceImpl();
	
	@Mock
	LoginService mockLoginService;
	
	@Mock
	LoginStrategy loginStrategy ;
	
	@InjectMocks
	ONAPLoginController oNAPLoginController = new ONAPLoginController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	
	@Test
	public void doLogin() throws Exception
	{
		ModelAndView expectedModelandView = null;
		ModelAndView actualModelandView = null;
		Mockito.when(loginStrategy.doLogin(mockedRequest, mockedResponse)).thenReturn(expectedModelandView);
		actualModelandView= oNAPLoginController.doLogin(mockedRequest, mockedResponse);
		oNAPLoginController.setViewName("test");
		assertEquals("test", oNAPLoginController.getViewName());	
		assertEquals(actualModelandView,expectedModelandView);
	}
	
	
	@Test
	public void ViewTest() throws Exception
	{
		ModelAndView expectedModelandView = new ModelAndView();
		expectedModelandView.setViewName("testView");
		ModelAndView actualModelandView = null;
		Mockito.when(loginStrategy.doLogin(mockedRequest, mockedResponse)).thenReturn(expectedModelandView);
		actualModelandView= oNAPLoginController.doLogin(mockedRequest, mockedResponse);
		assertEquals(actualModelandView.getViewName(),expectedModelandView.getViewName());
	}
	@Test
	public void getJessionIdTest() throws Exception
	{
	 assertNull(oNAPLoginController.getJessionId(mockedRequest));
	}
	
	@Test
	public void getLoginServiceTest() throws Exception
	{
		LoginService expectedLoginService =     oNAPLoginController.getLoginService();
		assertEquals(mockLoginService,expectedLoginService);
	}
	
	
	
}
