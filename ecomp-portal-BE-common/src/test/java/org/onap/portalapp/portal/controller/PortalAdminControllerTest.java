
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
 */package org.onap.portalapp.portal.controller;

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
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
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

public class PortalAdminControllerTest extends MockitoTestSuite{

	@InjectMocks
	PortalAdminController portalAdminController;

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
	public void getPortalAdminsTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<PortalAdmin> expectedPortalAdminsList = new ArrayList<PortalAdmin>();
		PortalAdmin portalAdmin= new PortalAdmin();
		
		portalAdmin.setUserId((long) 1);
		portalAdmin.setLoginId("guestT");
		portalAdmin.setFirstName("Test_FirstName");
		portalAdmin.setLastName("Test_LastName");
		
		expectedPortalAdminsList.add(portalAdmin);
		
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		
         Mockito.when(portalAdminService.getPortalAdmins()).thenReturn(expectedPortalAdminsList);
         List<PortalAdmin> actualPortalAdminsList =  portalAdminController.getPortalAdmins(mockedRequest, mockedResponse);
         assertEquals(actualPortalAdminsList,expectedPortalAdminsList);

	}
//	@Test
//	public void getPortalAdminsIfUserIsNullTest()
//	{
//		EPUser user = null;
//		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
//
//         assertNull(portalAdminController.getPortalAdmins(mockedRequest, mockedResponse));
//
//	}
	
	@Test
	public void getPortalAdminsIfUserIsSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		 assertNull(portalAdminController.getPortalAdmins(mockedRequest, mockedResponse));

	}
	
	
	
	@Test
	public void createPortalAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		String sbcid = "Test";
		
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.createPortalAdmin(sbcid)).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.createPortalAdmin(mockedRequest, sbcid, mockedResponse);
        assertEquals(actualFieldValidator,expectedFieldValidator);

	}

	@Test
	public void createPortalAdminXSSTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = null;
		FieldsValidator actualFieldValidator;
		String userId = "<IMG SRC=jAVasCrIPt:alert(‘XSS’)>";
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.createPortalAdmin(userId)).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.createPortalAdmin(mockedRequest, userId, mockedResponse);
		assertEquals(expectedFieldValidator, actualFieldValidator);

	}

	@Test
	public void createPortalAdminIfUserIsNullTest()
	{
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		String sbcid = "null";
		assertNull(portalAdminController.createPortalAdmin(mockedRequest, sbcid, mockedResponse));

	}
	
	@Test
	public void createPortalAdminIfUserIsSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		String sbcid = "Test";
		assertNull(portalAdminController.createPortalAdmin(mockedRequest, sbcid, mockedResponse));

	}
	
	@Test
	public void deletePortalAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<PortalAdmin> expectedPortalAdminsList = new ArrayList<PortalAdmin>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
	    Mockito.when(portalAdminService.deletePortalAdmin(12L)).thenReturn(null);
         FieldsValidator actualPortalAdminsList =  portalAdminController.deletePortalAdmin(mockedRequest, "test", mockedResponse);
         assertNull(actualPortalAdminsList);

	}

	@Test
	public void deletePortalAdminXSSTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		FieldsValidator actualFieldValidator = portalAdminController.deletePortalAdmin(mockedRequest,"<img src=xss onerror=alert(1)>" , mockedResponse);
		assertNull(actualFieldValidator);

	}
	
	@Test
	public void deletePortalAdminTest1()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		String sbcid = "Test";
		List<PortalAdmin> expectedPortalAdminsList = new ArrayList<PortalAdmin>();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
	    Mockito.when(portalAdminService.deletePortalAdmin(112L)).thenReturn(null);
         FieldsValidator actualPortalAdminsList =  portalAdminController.deletePortalAdmin(mockedRequest, "112-1", mockedResponse);
         assertNull(actualPortalAdminsList);

	}
	
	@Test
	public void deletePortalAdminTest2()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		//String sbcid = "Test";
		Long userId=123L;
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.deletePortalAdmin(userId)).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.deletePortalAdmin(mockedRequest,"123-Test" , mockedResponse);
        assertEquals(actualFieldValidator,expectedFieldValidator);

	}

	@Test
	public void getRolesByAppsTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPRole> expectedgetAppsList = new ArrayList<EPRole>();
		EPRole epRole= new EPRole();
		epRole.setName("Test");
		epRole.setActive(true);
		epRole.setPriority(1);
		epRole.setAppId(12L);
		epRole.setId(1111L);
		expectedgetAppsList.add(epRole);
		Mockito.when(adminRolesService.getRolesByApp(user,12L)).thenReturn(expectedgetAppsList);
		//Mockito.when(portalAdminService.deletePortalAdmin(userId)).thenReturn(expectedFieldValidator);
		List<EPRole> actualgetAppsList = portalAdminController.getRolesByApp(mockedRequest,12L , mockedResponse);
        assertEquals(actualgetAppsList,expectedgetAppsList);

	}

	@Test
	public void getRolesByAppsWhenUserIsNullTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		List<EPRole> expectedgetAppsList = new ArrayList<EPRole>();
		EPRole epRole= new EPRole();
		expectedgetAppsList.add(epRole);
		Mockito.when(adminRolesService.getRolesByApp(user,12L)).thenReturn(expectedgetAppsList);
		List<EPRole> actualgetAppsList = portalAdminController.getRolesByApp(mockedRequest,12L , mockedResponse);
        assertNull(actualgetAppsList);

	}
			
}
