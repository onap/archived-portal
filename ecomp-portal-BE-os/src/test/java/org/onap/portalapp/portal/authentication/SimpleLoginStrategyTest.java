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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.authentication.SimpleLoginStrategy;
import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPRoleFunctionService;
import org.onap.portalapp.portal.service.EPRoleService;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.menu.MenuProperties;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.StringUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StringUtils.class, EPUserUtils.class, SessionCookieUtil.class, SystemProperties.class,
		SessionCookieUtil.class, MenuProperties.class })
public class SimpleLoginStrategyTest {

	@InjectMocks
	SimpleLoginStrategy simpleLoginStrategy = new SimpleLoginStrategy();

	@Mock
	EPLoginService loginService;
	@Mock
	EPRoleService roleService;
	@Mock
    EPRoleFunctionService ePRoleFunctionService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();

	@Test(expected = Exception.class)
	public void loginTest() throws Exception {
		PowerMockito.mockStatic(MenuProperties.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(SessionCookieUtil.getUserIdFromCookie(mockedRequest, mockedResponse)).thenReturn("guestT");
		Mockito.when(StringUtils.isEmpty("guestT")).thenReturn(false);
		EPLoginBean commandBean = new EPLoginBean();
		EPUser user = new EPUser();
		commandBean.setUser(user);
		commandBean.setOrgUserId("guestT");
		Mockito.when(mockedRequest.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY))
				.thenReturn("menu_properties_filename");
		Mockito.when(loginService.findUser(commandBean, "menu_properties_filename", null)).thenReturn(commandBean);
		assertTrue(simpleLoginStrategy.login(mockedRequest, mockedResponse));
	}

	@Test
	public void loginIfUserEmptyTest() throws Exception {
		PowerMockito.mockStatic(MenuProperties.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(SessionCookieUtil.getUserIdFromCookie(mockedRequest, mockedResponse)).thenReturn("guestT");
		Mockito.when(StringUtils.isEmpty("guestT")).thenReturn(true);
		EPLoginBean commandBean = new EPLoginBean();
		EPUser user = new EPUser();
		commandBean.setUser(user);
		commandBean.setOrgUserId("guestT");
		assertFalse(simpleLoginStrategy.login(mockedRequest, mockedResponse));
	}

	@Test
	public void loginIfAuthIsBothTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(SessionCookieUtil.getUserIdFromCookie(mockedRequest, mockedResponse)).thenReturn("guestT");
		Mockito.when(StringUtils.isEmpty("guestT")).thenReturn(true);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("BOTH");
		assertFalse(simpleLoginStrategy.login(mockedRequest, mockedResponse));
	}

	@Test
	public void loginIfAuthIsNotNullTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(SessionCookieUtil.getUserIdFromCookie(mockedRequest, mockedResponse)).thenReturn("guestT");
		Mockito.when(StringUtils.isEmpty("guestT")).thenReturn(true);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("Test");
		assertFalse(simpleLoginStrategy.login(mockedRequest, mockedResponse));
	}

	@Test
	public void loginExceptionTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(SessionCookieUtil.getUserIdFromCookie(mockedRequest, mockedResponse)).thenReturn("guestT");
		Mockito.when(StringUtils.isEmpty("guestT")).thenReturn(true);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM))
				.thenThrow(nullPointerException);
		assertFalse(simpleLoginStrategy.login(mockedRequest, mockedResponse));
	}

	@Test(expected = Exception.class)
	public void doLoginTest() throws Exception {
		simpleLoginStrategy.doLogin(mockedRequest, mockedResponse);
	}

	@Test(expected = PortalAPIException.class)
	public void getUserIdTest() throws Exception {
		simpleLoginStrategy.getUserId(mockedRequest);
	}
}
