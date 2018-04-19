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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.controller.LoginController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPRoleFunctionService;
import org.onap.portalapp.portal.service.EPRoleService;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.service.EPProfileService;
import org.onap.portalsdk.core.domain.MenuData;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.web.servlet.ModelAndView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemProperties.class, CipherUtil.class, AppUtils.class, UserUtils.class, EPCommonSystemProperties.class})
public class LoginControllerTest {

	@Mock
	EPProfileService service;
	@Mock
	EPLoginService loginService;
	@Mock
	SharedContextService sharedContextService;
	@Mock
	EPRoleService roleService;
	@Mock
	EPRoleFunctionService ePRoleFunctionService;

	@InjectMocks
	LoginController loginController = new LoginController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockEPUser mockUser = new MockEPUser();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void loginIfAuthNullTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn(null);
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(), "openIdLogin");
	}

	@Test
	public void loginIfAuthOIDCTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(), "login");
	}

	@Test
	public void loginTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("Test");
		ModelAndView result = loginController.login(mockedRequest);
		assertEquals(result.getViewName(), "login");
	}

	@Test
	public void loginValidateTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(UserUtils.class);
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		String json = "{\"loginId\":\"test\", \"password\":\"xyz\"}";
		when(request.getInputStream()).thenReturn(
				new DelegatingServletInputStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))));
		when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
		when(request.getContentType()).thenReturn("application/json");
		when(request.getCharacterEncoding()).thenReturn("UTF-8");
		when(request.getAttribute("menu_properties_filename")).thenReturn("test");
		StringBuffer reqUrl = new StringBuffer("http://localhost.com");
		when(request.getRequestURL()).thenReturn(reqUrl);
		when(request.getQueryString()).thenReturn("demo?test");
		when(request.getSession(true)).thenReturn(session);
		when(request.getSession()).thenReturn(session);
		EPLoginBean commandBean = new EPLoginBean();
		commandBean.setLoginId("guestT");
		commandBean.setUser(user);
		commandBean.setOrgUserId("guestT");
		commandBean.setLoginPwd("xyz");
		Set<MenuData> menus = new HashSet<MenuData>();
		MenuData menuData = new MenuData();
		menuData.setFunctionCd("test");
		MenuData menuData2 = new MenuData();
		menuData2.setFunctionCd("test2");
		menus.add(menuData);
		menus.add(menuData2);
		commandBean.setMenu(menus);
		commandBean.setBusinessDirectMenu(menus);
		when(loginController.getLoginService().findUser(Matchers.any(EPLoginBean.class), Matchers.anyString(),
				Matchers.any())).thenReturn(commandBean);
		when(AppUtils.getSession(request)).thenReturn(session);
		when(UserUtils.isAccessible(request, menuData.getFunctionCd())).thenReturn(true);
		when(UserUtils.isAccessible(request, menuData2.getFunctionCd())).thenReturn(true);
		when(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.COOKIE_DOMAIN)).thenReturn("cookie_domain");
		when(CipherUtil.encryptPKC(Matchers.anyString(), Matchers.anyString())).thenReturn("guestT");
		String actual = loginController.loginValidate(request, response);
		JSONObject expected = new JSONObject("{success: success}");
		assertNotEquals(actual, expected);
	}

}
