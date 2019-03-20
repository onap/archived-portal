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
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalsdk.core.domain.Role;

public class RolesControllerTest {

	@InjectMocks
	RolesController rolesController = new RolesController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	ExternalAccessRolesController externalAccessRolesController;

	@Mock
	private ExternalAccessRolesService externalAccessRolesService;

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void getV2RolesForAppTest() throws Exception {
		Mockito.when(externalAccessRolesController.getV2RolesForApp(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.getV2RolesForApp(mockedRequest, mockedResponse));
	}

	@Test
	public void getV2UserListTest() throws Exception {
		Mockito.when(externalAccessRolesController.getV2UserList(mockedRequest, mockedResponse, "test12"))
				.thenReturn(null);
		assertNull(rolesController.getV2UserList(mockedRequest, mockedResponse, "test12"));
	}

	@Test
	public void saveRoleTest() throws Exception {
		Role role = new Role();
		Mockito.when(externalAccessRolesController.saveRole(mockedRequest, mockedResponse, role)).thenReturn(null);
		assertNull(rolesController.saveRole(mockedRequest, mockedResponse, role));
	}

	@Test
	public void getV2RoleInfoTest() throws Exception {
		Mockito.when(externalAccessRolesController.getV2RoleInfo(mockedRequest, mockedResponse, (long) 22))
				.thenReturn(null);
		assertNull(rolesController.getV2RoleInfo(mockedRequest, mockedResponse, (long) 22));
	}

	@Test
	public void getUsersOfApplicationTest() throws Exception {
		Mockito.when(externalAccessRolesController.getUsersOfApplication(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.getUsersOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleFunctionsListTest() throws Exception {
		Mockito.when(externalAccessRolesController.getV2RoleFunctionsList(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.getRoleFunctionsList(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleFunctionTest() throws Exception {
		Mockito.when(externalAccessRolesController.getRoleFunction(mockedRequest, mockedResponse, "test"))
				.thenReturn(null);
		assertNull(rolesController.getRoleFunction(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void saveRoleFunctionTest() throws Exception {
		Mockito.when(externalAccessRolesController.saveRoleFunction(mockedRequest, mockedResponse, "test"))
				.thenReturn(null);
		assertNull(rolesController.saveRoleFunction(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void deleteRoleFunctionTest() throws Exception {
		Mockito.when(externalAccessRolesController.deleteRoleFunction(mockedRequest, mockedResponse, "test"))
				.thenReturn(null);
		assertNull(rolesController.deleteRoleFunction(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void deleteRoleTest() throws Exception {
		Mockito.when(externalAccessRolesController.deleteRole(mockedRequest, mockedResponse, (long) 1))
				.thenReturn(null);
		assertNull(rolesController.deleteRole(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void getV2ActiveRolesTest() throws Exception {
		Mockito.when(externalAccessRolesController.getV2ActiveRoles(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.getV2ActiveRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadFunctionsTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadFunctions(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.bulkUploadFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadRolesTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadRoles(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.bulkUploadRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadRoleFunctionsTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadRoleFunctions(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.bulkUploadRoleFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadUserRolesTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadUserRoles(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.bulkUploadUserRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadUsersSingleRoleTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadUsersSingleRole(mockedRequest, mockedResponse, (long) 1))
				.thenReturn(null);
		assertNull(rolesController.bulkUploadUsersSingleRole(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void bulkUploadPartnerFunctionsTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadPartnerFunctions(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.bulkUploadPartnerFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadPartnerRolesTest() throws Exception {
		List<Role> upload = new ArrayList<>();
		Mockito.when(externalAccessRolesController.bulkUploadPartnerRoles(mockedRequest, mockedResponse, upload))
				.thenReturn(null);
		assertNull(rolesController.bulkUploadPartnerRoles(mockedRequest, mockedResponse, upload));
	}

	@Test
	public void bulkUploadPartnerRoleFunctionsTest() throws Exception {
		Mockito.when(externalAccessRolesController.bulkUploadPartnerRoleFunctions(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.bulkUploadPartnerRoleFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void getMenuFunctionsTest() throws Exception {
		Mockito.when(externalAccessRolesController.getMenuFunctions(mockedRequest, mockedResponse)).thenReturn(null);
		assertNull(rolesController.getMenuFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void getEcompUserTest() throws Exception {
		Mockito.when(externalAccessRolesController.getEcompUser(mockedRequest, mockedResponse, "test12"))
				.thenReturn(null);
		assertNull(rolesController.getEcompUser(mockedRequest, mockedResponse, "test12"));
	}

	@Test
	public void getEcompRolesOfApplicationTest() throws Exception {
		Mockito.when(externalAccessRolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse))
				.thenReturn(null);
		assertNull(rolesController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void updateAppRoleDescriptionTest() throws Exception {
		Integer result = 1;
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Successfully updated app role descriptions: '1'");
		expectedportalRestResponse.setResponse("Success");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.OK);
		Mockito.when(externalAccessRolesService.updateAppRoleDescription(mockedRequest.getHeader("uebkey")))
				.thenReturn(result);
		portalRestResponse = rolesController.updateAppRoleDescription(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void updateAppRoleDescriptionExceptionTest() throws Exception {
		PortalRestResponse<String> portalRestResponse = null;
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("updateAppRoleDescription: null");
		expectedportalRestResponse.setResponse("Failure");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(externalAccessRolesService.updateAppRoleDescription(mockedRequest.getHeader("uebkey")))
				.thenThrow(new NullPointerException());
		portalRestResponse = rolesController.updateAppRoleDescription(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse, expectedportalRestResponse);
	}

}
