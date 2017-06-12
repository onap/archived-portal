/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.openecomp.portalapp.portal.controller.FunctionalMenuController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRole;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRolesList;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FieldsValidator.FieldName;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;

@RunWith(MockitoJUnitRunner.class)
public class FunctionalMenuControllerTest extends MockitoTestSuite {

	String userid = "ab1234";

	@Mock
	FunctionalMenuService functionalMenuService = new FunctionalMenuServiceImpl();

	@InjectMocks
	FunctionalMenuController functionalMenuController = new FunctionalMenuController();

	@Mock
	private DataAccessService dataAccessService;

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@InjectMocks
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	EPUser epuser;

	MockEPUser mockUser = new MockEPUser();

	List<BusinessCardApplicationRolesList> appRoles = new ArrayList<BusinessCardApplicationRolesList>();
	List<BusinessCardApplicationRolesList> appRolesActual = new ArrayList<BusinessCardApplicationRolesList>();

	List<BusinessCardApplicationRole> userAppRoleList = new ArrayList<BusinessCardApplicationRole>();

	public List<BusinessCardApplicationRole> mockBusinessCardApplicationRole() {
		List<BusinessCardApplicationRole> userAppRoleList = new ArrayList<BusinessCardApplicationRole>();

		BusinessCardApplicationRole businessCardApplicationRole = new BusinessCardApplicationRole();
		businessCardApplicationRole.setRoleName("ADMIN");
		businessCardApplicationRole.setAppName("ASDC");

		BusinessCardApplicationRole businessCardApplicationRole1 = new BusinessCardApplicationRole();
		businessCardApplicationRole1.setAppName("ASDC");
		businessCardApplicationRole1.setRoleName("Tester");

		userAppRoleList.add(businessCardApplicationRole);
		userAppRoleList.add(businessCardApplicationRole1);
		return userAppRoleList;
	}

	public List<BusinessCardApplicationRolesList> mockBusinessCardApplicationRolesList() {
		List<BusinessCardApplicationRolesList> appRolesActual = new ArrayList<BusinessCardApplicationRolesList>();

		BusinessCardApplicationRolesList businessCardApplicationRolesList = new BusinessCardApplicationRolesList();
		businessCardApplicationRolesList.setAppName("ASDC");

		List<String> roleNames = new ArrayList<String>();
		roleNames.add("ADMIN");
		roleNames.add("Tester");
		businessCardApplicationRolesList.setRoleNames(roleNames);

		appRolesActual.add(businessCardApplicationRolesList);
		return appRolesActual;
	}

	@Test
	public void getAppListTestIfAppAlredyExistsBusinessCardApplicationRolesList() throws IOException {

		userAppRoleList = mockBusinessCardApplicationRole();
		appRolesActual = mockBusinessCardApplicationRolesList();

		Mockito.when(functionalMenuService.getUserAppRolesList(userid)).thenReturn(userAppRoleList);

		appRoles = functionalMenuController.getAppList(mockedRequest, userid);

		assertEquals(appRolesActual.size(), appRoles.size());
		assertEquals(appRolesActual.get(0).getAppName(), appRoles.get(0).getAppName());
		assertEquals(appRolesActual.get(0).getRoleNames(), appRoles.get(0).getRoleNames());

	}

	@Test
	public void getAppListTestIfAppDoesnotExistsInBusinessCardApplicationRolesList() throws IOException {

		userAppRoleList = mockBusinessCardApplicationRole();
		BusinessCardApplicationRole businessCardApplicationRole = new BusinessCardApplicationRole();
		businessCardApplicationRole.setAppName("CCD");
		businessCardApplicationRole.setRoleName("ADMIN");
		userAppRoleList.add(businessCardApplicationRole);
		appRolesActual = mockBusinessCardApplicationRolesList();
		BusinessCardApplicationRolesList businessCardApplicationRolesList = new BusinessCardApplicationRolesList();
		businessCardApplicationRolesList.setAppName("CCD");
		List<String> roleNames1 = new ArrayList<String>();
		roleNames1.add("ADMIN");
		businessCardApplicationRolesList.setRoleNames(roleNames1);
		appRolesActual.add(businessCardApplicationRolesList);
		Mockito.when(functionalMenuService.getUserAppRolesList(userid)).thenReturn(userAppRoleList);
		appRoles = functionalMenuController.getAppList(mockedRequest, userid);
		assertEquals(appRolesActual.size(), appRoles.size());
		assertEquals(appRolesActual.get(0).getAppName(), appRoles.get(0).getAppName());
		assertEquals(appRolesActual.get(0).getRoleNames(), appRoles.get(0).getRoleNames());
		assertEquals(appRolesActual.get(1).getAppName(), appRoles.get(1).getAppName());
		assertEquals(appRolesActual.get(1).getRoleNames(), appRoles.get(1).getRoleNames());

	}

	@Test
	public void regenerateAncestorTableTest() {
		EPUser user = mockUser.mockEPUser();

		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator acutualFieldValidator = null;
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(!adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(functionalMenuService.regenerateAncestorTable()).thenReturn(expectedFieldValidator);
		acutualFieldValidator = functionalMenuController.regenerateAncestorTable(mockedRequest, mockedResponse);
		assertTrue(acutualFieldValidator.equals(expectedFieldValidator));
	}

}
