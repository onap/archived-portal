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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.EPRoleServiceImpl;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.service.DataAccessService;

public class EPRoleServiceImplTest {

	@Mock
	DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	ExternalAccessRolesService externalAccessRolesService;
	@InjectMocks
	EPRoleServiceImpl ePRoleServiceImpl = new EPRoleServiceImpl();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getRoleFunctionsTest() {
		List<RoleFunction> roleFunctionList = new ArrayList<>();
		Mockito.when(dataAccessService.getList(RoleFunction.class, null)).thenReturn(roleFunctionList);
		List<RoleFunction> expectedRoleFunctionList = ePRoleServiceImpl.getRoleFunctions();
		assertEquals(roleFunctionList, expectedRoleFunctionList);
	}

	@Test
	public void getAvailableChildRolesIfRoleIdIsNullTest() {
		Long roleId = (long)123;
		List<EPRole> roleList = new ArrayList<>();
		EPRole epRole = new EPRole();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		role.addChildRole(role1);
		roleList.add(role);
		Mockito.when(dataAccessService.getList(EPRole.class, null)).thenReturn(roleList);
		Mockito.when(dataAccessService.getDomainObject(EPRole.class, roleId, null)).thenReturn(epRole);
		List<EPRole> expectedRoleList = ePRoleServiceImpl.getAvailableChildRoles(null);
		assertEquals(roleList, expectedRoleList);
	}

	@Test
	public void getAvailableChildRolesIfRoleIdTest() {
		Long roleId = (long)123;
		List<EPRole> roleList = new ArrayList<>();
		EPRole epRole = new EPRole();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		role.addChildRole(role1);
		roleList.add(role);
		Mockito.when(dataAccessService.getList(EPRole.class, null)).thenReturn(roleList);
		Mockito.when(dataAccessService.getDomainObject(EPRole.class, roleId, null)).thenReturn(epRole);
		List<EPRole> expectedRoleList = ePRoleServiceImpl.getAvailableChildRoles(roleId);
		assertEquals(roleList, expectedRoleList);
	}
	
	@Test
	public void getRoleFunctionTest() {
		RoleFunction roleFunction = new RoleFunction();
		Mockito.when(dataAccessService.getDomainObject(RoleFunction.class, "test", null)).thenReturn(roleFunction);
		RoleFunction expectedRoleFunction = ePRoleServiceImpl.getRoleFunction("test");
		assertEquals(expectedRoleFunction, roleFunction);
	}

	@Test
	public void saveRoleFunctionTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(role, null);
		ePRoleServiceImpl.saveRole(role);
	}

	@Test
	public void deleteRoleFunctionTest() {
		RoleFunction roleFunction = new RoleFunction();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(roleFunction, null);
		ePRoleServiceImpl.deleteRoleFunction(roleFunction);
	}

	@Test
	public void getRoleTest() {
		EPRole role = null;
		Mockito.when(dataAccessService.getDomainObject(EPRole.class, 1, null)).thenReturn(role);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1);
		assertEquals(expectedRole, role);
	}

	@Test
	public void getRoleIfappIdNullTest() {
		assertNull(ePRoleServiceImpl.getRole(null, null));

	}

	@Test
	public void getRoleIfappIdNotNullTest() {
		List<EPRole> roles = new ArrayList<>();
		EPRole role = new EPRole();
		roles.add(role);
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1, (long) 1);
		assertEquals(expectedRole, role);

	}

	@Test
	public void getRoleIfListSizeIsMoreThan1Test() {
		List<EPRole> roles = new ArrayList<>();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		roles.add(role);
		roles.add(role1);
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1, (long) 1);
		assertEquals(expectedRole, role);

	}

	@Test
	public void getRoleIfListSizeIsEmptyTest() {
		List<EPRole> roles = new ArrayList<>();
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		assertNull(ePRoleServiceImpl.getRole((long) 1, (long) 1));

	}

	@Test
	public void saveRoleTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(role, null);
		ePRoleServiceImpl.saveRole(role);
	}

	@Test
	public void deleteRoleTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(role, null);
		ePRoleServiceImpl.deleteRole(role);
	}

	@Test
	public void getAvailableRolesTest() {
		List<EPRole> roleList = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, null)).thenReturn(roleList);
		List<EPRole> expectedRoleList = ePRoleServiceImpl.getAvailableRoles();
		assertEquals(expectedRoleList, roleList);
	}

	@Test
	public void getAppRolesTest() {
		final Map<String, String> portalParams = null;
		List<EPRole> roleList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRoles", portalParams, null)).thenReturn(roleList);
		Mockito.when(externalAccessRolesService.getPortalAppRoleInfo(Matchers.anyLong())).thenReturn(roleList);
		assertNull(ePRoleServiceImpl.getAppRole("test", (long) 1));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAppRolesIfNotPortalTest() {
		final Map<String, String> params = null;
		List<EPRole> roleList = new ArrayList<>();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		roleList.add(role);
		roleList.add(role1);
		Mockito.when((List<EPRole>) dataAccessService.executeNamedQuery("getAppRoles", params, null))
				.thenReturn(roleList);
		List<EPRole> expectedRoleList = (List<EPRole>) ePRoleServiceImpl.getAppRole("test", (long) 10);
	}

	@Test
	public void saveRoleFunction() {
		RoleFunction domainRoleFunction = new RoleFunction();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(domainRoleFunction, null);
		ePRoleServiceImpl.saveRoleFunction(domainRoleFunction);
	}
}
