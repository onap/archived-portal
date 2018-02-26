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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.logging.aop;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class EPEELFLoggerAdviceTest {
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Mock
	SecurityEventTypeEnum securityEventTypeEnum;
	
	@InjectMocks
	EPEELFLoggerAdvice epEELFLoggerAdvice = new EPEELFLoggerAdvice();

	@Test
	@SuppressWarnings("static-access")
	public void getCurrentDateTimeUTCTest(){
		String actual = epEELFLoggerAdvice.getCurrentDateTimeUTC();
		assertNotEquals("", actual);
	}
	
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void loadServletRequestBasedDefaultsTest(){
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user_attribute_name", user);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("http:test.com");
		request.setSession(session);
		request.addHeader("user-agent", "Mozilla/5.0");
		Mockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("user_attribute_name");
		epEELFLoggerAdvice.loadServletRequestBasedDefaults(request, securityEventTypeEnum);
	}
	
	@Test
	public void afterTest(){
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user_attribute_name", user);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("http:test.com");
		request.setSession(session);
		request.addHeader("user-agent", "Mozilla/5.0");
		Mockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("user_attribute_name");
		List<Object> args = new ArrayList<>();
		args.add("testClassName");
		args.add("testMethodName");
		Object[] passOnArgs = args.toArray(new Object[args.size()]);
		List<Object> args2 = new ArrayList<>();
		args2.add(request);
		Object[] httpArgs = args2.toArray(new Object[args2.size()]);
		epEELFLoggerAdvice.after(securityEventTypeEnum, "200", "200",  httpArgs, null, passOnArgs);
	}
	
	@Test
	public void BeforeTest(){
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user_attribute_name", user);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("http:test.com");
		request.setSession(session);
		request.addHeader("user-agent", "Mozilla/5.0");
		Mockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("user_attribute_name");
		List<Object> args = new ArrayList<>();
		args.add("testClassName");
		args.add("testMethodName");
		Object[] passOnArgs = args.toArray(new Object[args.size()]);
		List<Object> args2 = new ArrayList<>();
		args2.add(request);
		Object[] httpArgs = args2.toArray(new Object[args2.size()]);
		epEELFLoggerAdvice.before(securityEventTypeEnum, httpArgs, passOnArgs);
	}
}
