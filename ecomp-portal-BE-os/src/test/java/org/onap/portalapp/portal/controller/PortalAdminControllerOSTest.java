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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.PortalAdminController;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AdminRolesServiceImpl;
import org.onap.portalapp.portal.service.PortalAdminService;
import org.onap.portalapp.portal.service.PortalAdminServiceImpl;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.PortalAdmin;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.AuditServiceImpl;

public class PortalAdminControllerOSTest {

	@InjectMocks
	PortalAdminController portalAdminController = new PortalAdminController();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();
	
	@Mock
	PortalAdminService portalAdminService = new PortalAdminServiceImpl();

	@Mock
	AuditService auditService = new AuditServiceImpl();

	 
	@Mock
	EcompPortalUtils ecompPortalUtils = new EcompPortalUtils();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void deletePortalAdminIfUserIsSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Long userInfo = (long) 12;
		assertNull(portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse));

	}
	
	@Test
	public void deletePortalAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Long userInfo = (long) 12;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.deletePortalAdmin((long) 12)).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse);
        assertEquals(actualFieldValidator,expectedFieldValidator);

	}
	
	@Test
	public void deletePortalAdminWithNoUserInfoTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Long userInfo = null;
		assertNull(portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse));
	}
	
	@Test
	public void getRolesByAppExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPRole> expectedRoleList = new ArrayList<EPRole>();	
		EPRole ePRole = new EPRole();
		expectedRoleList.add(ePRole);
		Long appId = (long) 1;
		Mockito.when(adminRolesService.getRolesByApp(user, appId)).thenThrow(nullPointerException);
		assertNull(portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse));
	}
	
	@Test
	public void getRolesByAppIfUserNullTest()
	{
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Long appId = (long) 1;
		assertNull(portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse));
	}
	
	@Test
	public void getRolesByAppTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPRole> expectedRoleList = new ArrayList<EPRole>();	
		EPRole ePRole = new EPRole();
		expectedRoleList.add(ePRole);
		Long appId = (long) 1;
		Mockito.when(adminRolesService.getRolesByApp(user, appId)).thenReturn(expectedRoleList);
		List<EPRole> actualRoleList = 	portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse);
		assertEquals(actualRoleList,expectedRoleList);
	}
	
	@Test
	public void createPortalAdminIfUserNullTest()
	{
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		assertNull(portalAdminController.createPortalAdmin(mockedRequest, "guestT", mockedResponse));
	}
	
	
	@Test
	public void createPortalAdminIfUserIsSuperAdminTest()
	{

		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.createPortalAdmin("guestT")).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.createPortalAdmin(mockedRequest, "guestT", mockedResponse);
        assertEquals(actualFieldValidator,expectedFieldValidator);
	}
	
	@Test
	public void createPortalAdminIfUserIsNotSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(portalAdminController.createPortalAdmin(mockedRequest, "guestT", mockedResponse));
	}
		
	@Test
	public void getPortalAdminsIfUserNullTest()
	{
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		assertNull(portalAdminController.getPortalAdmins(mockedRequest, mockedResponse));
	}
	
	@Test
	public void getPortalAdminsIfUserAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<PortalAdmin> portalAdmins = new ArrayList<>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.getPortalAdmins()).thenReturn(portalAdmins);
		List<PortalAdmin> actualortalAdmins = portalAdminController.getPortalAdmins(mockedRequest, mockedResponse);
        assertEquals(actualortalAdmins,portalAdmins);
	}
	
	@Test
	public void getPortalAdminIfUserIsNotSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		assertNull(portalAdminController.getPortalAdmins(mockedRequest, mockedResponse));
	}
}
