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
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.PortalAdmin;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, Criterion.class, Restrictions.class, PortalConstants.class,
		SystemProperties.class, EPCommonSystemProperties.class })
public class PortalAdminServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Mock 
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl(); 
	
	@Mock
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;

	@Mock
	RestTemplate template = new RestTemplate();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
	}

	@After
	public void after() {
		session.close();
	}

	@InjectMocks
	PortalAdminServiceImpl portalAdminServiceImpl = new PortalAdminServiceImpl();

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

	@SuppressWarnings("unchecked")
	@Test
	public void getPortalAdminsTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		List<PortalAdmin> portalAdmins = new ArrayList<>();
		PortalAdmin portalAdmin = new PortalAdmin();
		portalAdmin.setFirstName("guest");
		portalAdmin.setLastName("test");
		portalAdmin.setLoginId("test");
		portalAdmin.setUserId(1l);
		portalAdmins.add(portalAdmin);
		Map<String, String> params = new HashMap<>();
		params.put("adminRoleId", "1");
		Mockito.when((List<PortalAdmin>) dataAccessService.executeNamedQuery("getPortalAdmins", params, null))
				.thenReturn(portalAdmins);
		List<PortalAdmin> actual = portalAdminServiceImpl.getPortalAdmins();
		assertEquals(1, actual.size());
	}

	@Test
	public void getPortalAdminsExceptionTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		List<PortalAdmin> portalAdmins = new ArrayList<>();
		PortalAdmin portalAdmin = new PortalAdmin();
		portalAdmin.setFirstName("guest");
		portalAdmin.setLastName("test");
		portalAdmin.setLoginId("test");
		portalAdmin.setUserId(1l);
		portalAdmins.add(portalAdmin);
		Map<String, String> params = new HashMap<>();
		params.put("adminRoleId", "1");
		Mockito.doThrow(new NullPointerException()).when(dataAccessService).executeNamedQuery("getPortalAdmins", params,
				null);
		List<PortalAdmin> actual = portalAdminServiceImpl.getPortalAdmins();
		assertNull(actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createPortalAdminNewUserTest() {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		List<EPUser> users = new ArrayList<>();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId", user.getOrgUserId());
		restrictionsList.add(orgUserIdCriterion);
		Mockito.when((List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null))
				.thenReturn(users);
		List<PortalAdmin> portalAdmins = new ArrayList<>();
		Mockito.when(dataAccessService.executeSQLQuery(Matchers.anyString(), Matchers.any() , Matchers.anyMap()))
				.thenReturn(portalAdmins);
		Mockito.when(searchServiceImpl.searchUserByUserId(user.getOrgUserId())).thenReturn(user);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(
				EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		Mockito.when(epAppCommonServiceImpl.getApp(PortalConstants.PORTAL_APP_ID)).thenReturn(app);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		FieldsValidator actual = portalAdminServiceImpl.createPortalAdmin(user.getOrgUserId());
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createPortalAdminExistingUserTest() {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		List<EPUser> users = new ArrayList<>();
		users.add(user);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId", user.getOrgUserId());
		restrictionsList.add(orgUserIdCriterion);
		Mockito.when((List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null))
				.thenReturn(users);
		List<PortalAdmin> portalAdmins = new ArrayList<>();
		Mockito.when(dataAccessService.executeSQLQuery(Matchers.anyString(), Matchers.any() , Matchers.anyMap()))
				.thenReturn(portalAdmins);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(
				EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		Mockito.when(epAppCommonServiceImpl.getApp(PortalConstants.PORTAL_APP_ID)).thenReturn(app);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		FieldsValidator actual = portalAdminServiceImpl.createPortalAdmin(user.getOrgUserId());
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deletePortalAdminTest() {
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		EPUser user = mockUser.mockEPUser();
		EPApp app = mockApp();
		List<EPUser> users = new ArrayList<>();
		users.add(user);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("id", user.getId());
		restrictionsList.add(orgUserIdCriterion);
		Mockito.when((List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null))
				.thenReturn(users);
		Mockito.when(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()).thenReturn(true);
		Mockito.when(
				EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn(true);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN))
				.thenReturn("@test.com");
		Mockito.when(epAppCommonServiceImpl.getApp(PortalConstants.PORTAL_APP_ID)).thenReturn(app);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		FieldsValidator actual = portalAdminServiceImpl.deletePortalAdmin(user.getId());
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(Long.valueOf(HttpServletResponse.SC_OK));
		assertEquals(expected, actual);
	}

}
