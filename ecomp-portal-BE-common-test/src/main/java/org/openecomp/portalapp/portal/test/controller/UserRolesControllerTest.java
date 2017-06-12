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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.UserRolesController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.UserRolesService;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FieldsValidator.FieldName;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;

public class UserRolesControllerTest extends MockitoTestSuite {

	String userid = "ab1234";

	@Mock
	UserRolesService userRolesService;

	@Mock
	FieldsValidator fieldsValidator;

	@InjectMocks
	UserRolesController userRolesController = new UserRolesController();

	@Mock
	private DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	EPUser epuser;

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void putAppWithUserRoleRequestTest() {

		FieldsValidator actualFieldsValidator = null;

		AppWithRolesForUser appWithRolesForUser = new AppWithRolesForUser();
		List<RoleInAppForUser> listofRoles = new ArrayList<RoleInAppForUser>();

		appWithRolesForUser.setOrgUserId("guest");
		appWithRolesForUser.setAppId((long) 550);
		appWithRolesForUser.setAppName("D2 Services Analytics Dashboard");
		appWithRolesForUser.setAppRoles(listofRoles);

		RoleInAppForUser roleInAppForUser = new RoleInAppForUser();
		roleInAppForUser.setIsApplied(false);
		roleInAppForUser.setRoleId((long) 1);
		roleInAppForUser.setRoleName("System Administrator");

		RoleInAppForUser roleInAppForUser1 = new RoleInAppForUser();
		roleInAppForUser1.setIsApplied(true);
		roleInAppForUser1.setRoleId((long) 16);
		roleInAppForUser1.setRoleName("Standard User");

		listofRoles.add(roleInAppForUser);
		listofRoles.add(roleInAppForUser1);
		EPUser user = mockUser.mockEPUser();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userRolesService.putUserAppRolesRequest(appWithRolesForUser, user))
				.thenReturn(expectedFieldValidator);

		actualFieldsValidator = userRolesController.putAppWithUserRoleRequest(mockedRequest, appWithRolesForUser,
				mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());

	}
}
