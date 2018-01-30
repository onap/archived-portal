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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

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
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemRoleApproval;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.onap.portalapp.portal.controller.RolesApprovalSystemController;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.UserRolesService;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;

public class RolesApprovalSystemControllerTest extends MockitoTestSuite {

	@Mock
	UserRolesService userRolesService;

	@InjectMocks
	RolesApprovalSystemController rolesApprovalSystemController = new RolesApprovalSystemController();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void postUserProfileIfRolesNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Request has no roles");
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = null;
		extSysUser.setRoles(externalSystemRoleApprovalList);

		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.postUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void postUserProfileTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Saved Successfully");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Saved Successfully");

		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "POST"))
				.thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.postUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void postUserProfileFailureTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Received Bad String");
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"Received Bad String");
		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "POST"))
				.thenReturn(externalRequestFieldsValidator);

		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.postUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void postUserProfileExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "POST")).thenThrow(nullPointerException);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.postUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void putUserProfileIfLoginIdNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Request has no login ID");
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId(null);
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		extSysUser.setRoles(externalSystemRoleApprovalList);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.putUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void putUserProfileTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Updated Successfully");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Updated Successfully");

		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "PUT"))
				.thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.putUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void putUserProfileFailureTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Received Bad String");
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"Received Bad String");

		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "PUT"))
				.thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.putUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void putUserProfileExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse("save user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "PUT")).thenThrow(nullPointerException);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.putUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void deleteUserProfileIfApplicationNameNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Request has no application name");
		expectedportalRestResponse.setResponse("delete user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName(null);
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		extSysUser.setRoles(externalSystemRoleApprovalList);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.deleteUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void deleteUserProfileIfMyloginrequestIdNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Request has no request ID");
		expectedportalRestResponse.setResponse("delete user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId(null);
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		extSysUser.setRoles(externalSystemRoleApprovalList);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.deleteUserProfile(mockedRequest, extSysUser, mockedResponse);
		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void deleteUserProfileTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Deleted Successfully");
		expectedportalRestResponse.setResponse("Success");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(true,
				"Success");

		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "DELETE"))
				.thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.deleteUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void deleteUserProfileFailureTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("failed");
		expectedportalRestResponse.setResponse("delete user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		ExternalRequestFieldsValidator externalRequestFieldsValidator = new ExternalRequestFieldsValidator(false,
				"failed");

		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "DELETE"))
				.thenReturn(externalRequestFieldsValidator);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.deleteUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}

	@Test
	public void deleteUserProfileExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse("delete user profile failed");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		extSysUser.setApplicationName("Test_App");
		extSysUser.setLoginId("1");
		extSysUser.setMyloginrequestId("Test");
		List<ExternalSystemRoleApproval> externalSystemRoleApprovalList = new ArrayList<ExternalSystemRoleApproval>();
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApprovalList.add(externalSystemRoleApproval);
		extSysUser.setRoles(externalSystemRoleApprovalList);
		Mockito.when(userRolesService.setExternalRequestUserAppRole(extSysUser, "DELETE")).thenThrow(nullPointerException);
		PortalRestResponse<String> actualportalRestResponse = rolesApprovalSystemController
				.deleteUserProfile(mockedRequest, extSysUser, mockedResponse);

		assertEquals(expectedportalRestResponse, actualportalRestResponse);
	}
}
