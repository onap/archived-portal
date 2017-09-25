package org.openecomp.portalapp.portal.authentication;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalsdk.core.onboarding.exception.PortalAPIException;
import org.openecomp.portalsdk.core.util.SystemProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.authentication.OpenIdConnectLoginStrategy;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
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
	public void loginTest()
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
	public void loginIfUserNullTest()
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
//		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("auth");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserIfAuthIsOIDCTest()
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("OIDC");
		Mockito.when(SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL)).thenReturn("login_url");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserIfAuthNotNullTest()
	{		
		PowerMockito.mockStatic(SystemProperties.class);
		UserInfo  userInfo = null;
		Mockito.when(mockedRequest.getAttribute("userInfo")).thenReturn(userInfo);
		Mockito.when(SystemProperties.getProperty("authentication_mechanism")).thenReturn("test");
		Mockito.when(SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL)).thenReturn("login_url");
		assertFalse(OpenIdConnectLoginStrategy.login(mockedRequest, mockedResponse));
	}
	
	@Test
	public void loginIfUserExceptionest()
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
