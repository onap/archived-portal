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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, SystemProperties.class, PortalConstants.class,
		EPCommonSystemProperties.class, Criterion.class, CipherUtil.class, Restrictions.class })
public class UserServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	
	@InjectMocks
	UserServiceImpl userServiceImpl= new UserServiceImpl();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setNameSpace("com.test.app");
		app.setCentralAuth(true);
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 10);
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void getUserByUserIdExceptionTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
		Mockito.when(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER)).thenReturn("http://www.test.com");
		HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		JSONObject response = new JSONObject();
		JSONObject userJson = new JSONObject();
		userJson.put("id", 1);
		userJson.put("givenName", "Guest");
		userJson.put("familyName", "Test");
		userJson.put("email", "test@123.com");
		List<JSONObject> userListJson =  new ArrayList<>();
		userListJson.add(userJson);
		response.put("response", userListJson);
		ByteArrayInputStream getBody = new ByteArrayInputStream(response.toString().getBytes("UTF-8"));
		PowerMockito.when(connection.getInputStream()).thenReturn(getBody);
		userServiceImpl.getUserByUserId(user.getOrgUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void saveNewUserTest() throws Exception {
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = mockUser.mockEPUser();
		List<EPUser> users = new ArrayList<>();
		Mockito.when(CipherUtil.encryptPKC(user.getLoginPwd())).thenReturn("xyz");
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId",user.getLoginId());
		restrictionsList.add(orgUserIdCriterion);
		Mockito.when((List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null)).thenReturn(users);
		String actual = userServiceImpl.saveNewUser(user, "No");
		assertEquals("success", actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void saveExistingUserTest() throws Exception {
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = mockUser.mockEPUser();
		user.setLoginPwd("xyz");
		List<EPUser> users = new ArrayList<>();
		users.add(user);
		EPUser oldUser = mockUser.mockEPUser();
		oldUser.setLoginPwd("abc");
		List<EPUser> oldUsers = new ArrayList<>();
		oldUsers.add(oldUser);
		Mockito.when(CipherUtil.encryptPKC(user.getLoginPwd())).thenReturn("xyz");
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId",user.getLoginId());
		restrictionsList.add(orgUserIdCriterion);
		Mockito.when((List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null)).thenReturn(oldUsers);
		String actual = userServiceImpl.saveNewUser(user, "No");
		assertEquals("success", actual);
	}

	
}
