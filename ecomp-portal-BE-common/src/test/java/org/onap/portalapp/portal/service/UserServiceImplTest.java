/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
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
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemProperties.class, EPCommonSystemProperties.class, CipherUtil.class })
public class UserServiceImplTest {

	private static final String TEST = "test";

	@InjectMocks
	UserServiceImpl userServiceImpl = new UserServiceImpl();

	@Mock
	DataAccessService dataAccessService;

	@Mock
	HttpURLConnection con;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getUserByUserIdTest() throws UnsupportedEncodingException, IOException {

		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = buildEpUser();
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
		Mockito.when(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER))
				.thenReturn("http://www.test.com");
		HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);

		JSONObject response = new JSONObject();
		JSONObject userJson = new JSONObject();
		userJson.put("id", 1);
		userJson.put("givenName", "Guest");
		userJson.put("familyName", TEST);
		userJson.put("email", "test@123.com");
		List<JSONObject> userListJson = new ArrayList<>();
		userListJson.add(userJson);
		response.put("response", userListJson);
		ByteArrayInputStream getBody = new ByteArrayInputStream(response.toString().getBytes("UTF-8"));
		PowerMockito.when(connection.getInputStream()).thenReturn(getBody);
		userServiceImpl.getUserByUserId(user.getOrgUserId());
	}

	@Test
	public void testGetUserByNameInvalidODC() throws Exception {

		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn(TEST);
		List list = new ArrayList<>();
		StringBuffer criteria = new StringBuffer();
		String firstName = TEST;
		String lastName = TEST;
		if (firstName != null)
			criteria.append(" where first_name = '").append(firstName).append("'");
		if (lastName != null)
			criteria.append(" where last_name = '").append(lastName).append("'");
		when(dataAccessService.getList(EPUser.class, criteria.toString(), null, null)).thenReturn(list);
		userServiceImpl.getUserByFirstLastName(TEST, TEST);

	}

	@Test
	public void testGetUserByName() throws Exception {

		PowerMockito.mockStatic(SystemProperties.class);
		EPUser user = buildEpUser();
		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
		Mockito.when(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER))
				.thenReturn("http://www.test.com");
		//HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		JSONObject response = new JSONObject();
		JSONObject userJson = new JSONObject();
		userJson.put("id", 1);
		userJson.put("givenName", "Guest");
		userJson.put("familyName", TEST);
		userJson.put("email", "test@123.com");
		List<JSONObject> userListJson = new ArrayList<>();
		userListJson.add(userJson);
		response.put("response", userListJson);
		//ByteArrayInputStream getBody = new ByteArrayInputStream(response.toString().getBytes("UTF-8"));
		//PowerMockito.when(connection.getInputStream()).thenReturn(getBody);
		userServiceImpl.getUserByFirstLastName(TEST, TEST);

	}

	@Test
	public void saveNewUserTest() throws Exception {
		
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = buildEpUser();
		List users = new ArrayList<>();
		users.add(user);
		Mockito.when(CipherUtil.encryptPKC(user.getLoginPwd())).thenReturn("xyz");
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId", user.getLoginId());
		restrictionsList.add(orgUserIdCriterion);
		
		when( dataAccessService.getList(EPUser.class, null, restrictionsList, null)).thenReturn(users);
		String actual = userServiceImpl.saveNewUser(user, "No");
		assertEquals("success", actual);

	}

	@Test
	public void saveNewUserEmptyTest() throws Exception {
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(CipherUtil.class);
		EPUser user = buildEpUser();
		List users = new ArrayList<>();
		Mockito.when(CipherUtil.encryptPKC(user.getLoginPwd())).thenReturn("xyz");
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId", user.getLoginId());
		restrictionsList.add(orgUserIdCriterion);
		StringBuffer criteria = new StringBuffer();
		criteria.append(" where org_user_id = '").append(user.getLoginId()).append("'");
		Mockito.when(dataAccessService.getList(EPUser.class, criteria.toString(), null, null)).thenReturn(users);
		String actual = userServiceImpl.saveNewUser(user, "No");
		assertEquals("success", actual);

	}
	
	@Test
	public void saveUser()throws Exception {
		
		EPUser user = buildEpUser();
		userServiceImpl.saveUser(user);
		
	}

	EPUser buildEpUser() {
		EPUser epUser = new EPUser();

		epUser.setId((long) 1);
		epUser.setManagerId((long) 1234);
		epUser.setFirstName(TEST);
		epUser.setLastName(TEST);
		epUser.setMiddleInitial(TEST);
		epUser.setPhone(TEST);
		epUser.setFax(TEST);
		epUser.setCellular(TEST);
		epUser.setEmail(TEST);
		epUser.setAddressId((long) 123);
		epUser.setAlertMethodCd(TEST);
		epUser.setHrid(TEST);
		epUser.setOrgUserId(TEST);
		epUser.setOrgCode(TEST);
		epUser.setAddress1(TEST);
		epUser.setAddress2(TEST);
		epUser.setCity(TEST);
		epUser.setState(TEST);
		epUser.setZipCode(TEST);
		epUser.setCountry(TEST);
		epUser.setOrgManagerUserId(TEST);
		epUser.setLocationClli(TEST);
		epUser.setBusinessCountryCode(TEST);
		epUser.setBusinessCountryName(TEST);
		epUser.setBusinessUnit(TEST);
		epUser.setBusinessUnitName(TEST);
		epUser.setDepartment(TEST);
		epUser.setDepartmentName(TEST);
		epUser.setCompanyCode(TEST);
		epUser.setCompany(TEST);
		epUser.setZipCodeSuffix(TEST);
		epUser.setJobTitle(TEST);
		epUser.setCommandChain(TEST);
		epUser.setSiloStatus(TEST);
		epUser.setCostCenter(TEST);
		epUser.setFinancialLocCode(TEST);
		epUser.setLoginId(TEST);
		epUser.setLoginPwd(TEST);
		epUser.setLastLoginDate(new Date());
		epUser.setActive(false);
		epUser.setInternal(false);
		epUser.setSelectedProfileId((long) 12345);
		epUser.setTimeZoneId((long) 12345);
		epUser.setOnline(false);
		epUser.setChatId(TEST);
		return epUser;
	}
}
