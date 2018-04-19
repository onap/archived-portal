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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EPUserUtils.class, CipherUtil.class, AppUtils.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class EPLoginServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();

	@Mock
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	EPLoginServiceImpl epLoginServiceImpl = new EPLoginServiceImpl();
	
	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setNameSpace("com.test.app");
		app.setCentralAuth(true);
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	MockEPUser mockUser = new MockEPUser();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void findUserTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		EPLoginBean expected = new EPLoginBean();
		expected.setOrgUserId("guestT");
		Map<String, String> params = new HashMap<>();
		params.put("org_user_id", expected.getOrgUserId());
		List list = new ArrayList<>();
		list.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", params, new HashMap())).thenReturn(list);
		EPLoginBean actual =  epLoginServiceImpl.findUser(expected, "test", new HashMap<>());
		assertNotNull(actual);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void findUserPasswordMatchTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("guestT");
		user.setLoginPwd("abc");
		EPLoginBean expected = new EPLoginBean();
		expected.setLoginId(user.getLoginId());
		expected.setLoginPwd("xyz");
		Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List list = new ArrayList<>();
		list.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", params, new HashMap())).thenReturn(list);
		Map<String, String> params2 = new HashMap<>();
		params2.put("login_id", user.getOrgUserId());
		List list2 = new ArrayList<>();
		list2.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByLoginId", params2, new HashMap())).thenReturn(list2);
		Mockito.when(CipherUtil.decryptPKC(user.getLoginPwd())).thenReturn("xyz");
		EPLoginBean actual =  epLoginServiceImpl.findUser(expected, "test", new HashMap<>());
		assertNotNull(actual);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void findUserExcpetionTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("guestT");
		user.setLoginPwd("abc");
		EPLoginBean expected = new EPLoginBean();
		expected.setLoginId(user.getLoginId());
		expected.setLoginPwd("xyz");
		Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List list = new ArrayList<>();
		list.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", params, new HashMap())).thenReturn(list);
		Map<String, String> params2 = new HashMap<>();
		params2.put("login_id", user.getOrgUserId());
		List list2 = new ArrayList<>();
		list2.add(user);
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeNamedQuery("getEPUserByLoginId", params2, new HashMap());
		Mockito.when(CipherUtil.decryptPKC(user.getLoginPwd())).thenReturn("xyz");
		EPLoginBean actual =  epLoginServiceImpl.findUser(expected, "test", new HashMap<>());
		assertEquals(expected,actual);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void findUserAppUtilsExcpetionTest() throws Exception {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("guestT");
		user.setLoginPwd("abc");
		user.setActive(false);
		EPLoginBean expected = new EPLoginBean();
		expected.setOrgUserId(user.getOrgUserId());
		Map<String, String> params = new HashMap<>();
		params.put("org_user_id", user.getOrgUserId());
		List list = new ArrayList<>();
		list.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByOrgUserId", params, new HashMap())).thenReturn(list);
		Mockito.when(EPUserUtils.hasRole(user, SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID))).thenReturn(false);
		Mockito.when(AppUtils.isApplicationLocked()).thenReturn(true);
		Mockito.when(EPUserUtils.hasRole(user, SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID))).thenReturn(false);
		Mockito.when(AppUtils.isApplicationLocked()).thenReturn(true);
		EPLoginBean actual =  epLoginServiceImpl.findUser(expected, "test", new HashMap<>());
		assertEquals(expected,actual);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test 
	public void findUserWithoutPwdTest() {
		EPUser user = mockUser.mockEPUser();
		Map<String, String> params = new HashMap<>();
		params.put("login_id", user.getOrgUserId());
		List list = new ArrayList<>();
		list.add(user);
		Mockito.when(dataAccessService.executeNamedQuery("getEPUserByLoginId", params, new HashMap())).thenReturn(list);
		EPUser actual = epLoginServiceImpl.findUserWithoutPwd(user.getOrgUserId());
		assertEquals(user.getOrgUserId(), actual.getOrgUserId());
	}
}
