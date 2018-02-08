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
package org.onap.portalapp.portal.service;
//package org.onap.portalapp.portal.service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.onap.portalapp.portal.framework.MockitoTestSuite;
//import org.onap.portalapp.portal.utils.EPSystemProperties;
//import org.onap.portalsdk.core.service.DataAccessService;
//import org.onap.portalsdk.core.util.SystemProperties;
//import org.powermock.api.mockito.PowerMockito;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ SystemProperties.class , EPSystemProperties.class , SystemProperties.class})
//public class UserServiceImplTest {
//
//	
//	@InjectMocks
//	UserServiceImpl userServiceImpl = new UserServiceImpl();
//
//	@Mock
//	DataAccessService dataAccessService;
//	
//	@Mock
//	HttpURLConnection con;
//	
//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
//
//	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
//	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
//	
//	@Test
//	public void getUserByUserIdTest() throws UnsupportedEncodingException, IOException
//	{
//		 BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//		PowerMockito.mockStatic(SystemProperties.class);
//		PowerMockito.mockStatic(EPSystemProperties.class);
//		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
//		Mockito.when(EPSystemProperties.getProperty(EPSystemProperties.AUTH_USER_SERVER)).thenReturn("http://www.google.com");
//		Mockito.when(new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))).thenReturn(reader).thenReturn(reader);
//		userServiceImpl.getUserByUserId("guestT");
//	}
//}
