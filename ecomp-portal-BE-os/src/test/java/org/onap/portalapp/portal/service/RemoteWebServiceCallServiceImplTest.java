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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.service.RemoteWebServiceCallServiceImpl;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CipherUtil.class , SystemProperties.class})
public class RemoteWebServiceCallServiceImplTest {
	

	@InjectMocks
	RemoteWebServiceCallServiceImpl remoteWebServiceCallServiceImpl = new RemoteWebServiceCallServiceImpl();

	@Mock
	AppsCacheService appCacheService;
	
	@Mock
	DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void verifyRESTCredentialTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decryptPKC("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertFalse(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","requestPassword"));
	}
	
	@Test
	public void verifyRESTCredentialExceptionTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decryptPKC("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertTrue(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","pwd"));
	}
	
	@Test
	public void verifyRESTCredentialIfAppNullTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(null);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decryptPKC("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertFalse(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","pwd"));
	}
	
	@Test
	public void verifyAppKeyCredentialIfKeyIsNullTest() throws Exception
	{
		assertFalse(remoteWebServiceCallServiceImpl.verifyAppKeyCredential(null));
	}
	
	@Test
	public void verifyAppKeyCredentialTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		StringBuffer criteria = new  StringBuffer("where ueb_key = 'requestUebKey'");
//		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(null);
		assertFalse(remoteWebServiceCallServiceImpl.verifyAppKeyCredential("test"));
	}
	
	@Test
	public void verifyAppKeyCredentialSuccessTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'test'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		assertTrue(remoteWebServiceCallServiceImpl.verifyAppKeyCredential("test"));
	}
}
