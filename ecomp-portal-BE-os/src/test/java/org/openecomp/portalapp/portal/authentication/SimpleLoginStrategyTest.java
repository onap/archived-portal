package org.openecomp.portalapp.portal.authentication;

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
import org.openecomp.portalapp.authentication.SimpleLoginStrategy;
import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.service.EPRoleFunctionService;
import org.openecomp.portalapp.portal.service.EPRoleService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
import org.openecomp.portalsdk.core.menu.MenuProperties;
import org.openecomp.portalsdk.core.onboarding.exception.PortalAPIException;
import org.openecomp.portalsdk.core.util.SystemProperties;
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
