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

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.authentication.OpenIdConnectLoginStrategy;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.util.SystemProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.StringUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class, EPUserUtils.class , SessionCookieUtil.class,SystemProperties.class})
public class OpenIdConnectLoginStrategyTest {
	
	@InjectMocks
	OpenIdConnectLoginStrategy OpenIdConnectLoginStrategy = new OpenIdConnectLoginStrategy();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
		
	@Test
	public void loginTest() throws Exception
	{
		PowerMockito.mockStatic(StringUtils.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(SessionCookieUtil.class);
		
		UserInfo  userInfo = new DefaultUserInfo();
		userInfo.setPreferredUsername("Test");
		userInfo.setEmail("test@gmail.com");
		userInfo.setName("first_name");
		userInfo.setFamilyName("last_name");
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		assertTrue(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserNullTest() throws Exception
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
//		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("auth");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserIfAuthIsOIDCTest() throws Exception
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("OIDC");
		Mockito.when(SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL)).thenReturn("login_url");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserIfAuthNotNullTest() throws Exception
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("test");
		Mockito.when(SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL)).thenReturn("login_url");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserExceptionest() throws Exception
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenThrow(nullPointerException);
		Mockito.when(SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL)).thenReturn("login_url");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	@Test(expected =  Exception.class)
	public void doLoginTest() throws Exception
	{
		OpenIdConnectLoginStrategy.doLogin(mockedRequest, mockedResponse);
	}
	
	@Test(expected =  PortalAPIException.class)
	public void getUserIdTest() throws Exception
	{
		OpenIdConnectLoginStrategy.getUserId(mockedRequest);
	}
	
}
