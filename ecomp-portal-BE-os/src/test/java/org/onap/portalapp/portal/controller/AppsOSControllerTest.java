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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.AppsOSController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.PersUserAppService;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.util.EPUserUtils;

public class AppsOSControllerTest {

	@Mock
	AdminRolesService adminRolesService;

	@Mock
	EPAppService appService;

	@Mock
	PersUserAppService persUserAppService;

	@Mock
	UserService userService;

	@Mock
	EPUserUtils ePUserUtils;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	AppsOSController appsOSController = new AppsOSController();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void saveNewUserIfUserISNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("failure");
		expectedportalRestResponse.setResponse("New User cannot be null or empty");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = appsOSController.saveNewUser(mockedRequest, null);
		assertEquals(expectedportalRestResponse, actualPortalRestResponse);
	}

	@Test
	@Ignore
	public void saveNewUserIfUserNOtNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("failure");
		expectedportalRestResponse.setResponse("UnAuthorized");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		EPUser user1 = mockUser.mockEPUser();
		user1.setLoginId("guest");
		user.setLoginId("guestT");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user1);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		PortalRestResponse<String> actualPortalRestResponse = appsOSController.saveNewUser(mockedRequest, user);
		assertEquals(expectedportalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveNewUserAndLoggedInUserIdSameTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Success");
		expectedportalRestResponse.setResponse("");
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("guestT");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(mockedRequest.getParameter("isCheck")).thenReturn("test");
		Mockito.when(userService.saveNewUser(user, "test")).thenReturn("Success");
		PortalRestResponse<String> actualPortalRestResponse = appsOSController.saveNewUser(mockedRequest, user);
		assertEquals(expectedportalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveNewUserexceptionest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("failure");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setLoginId("guestT");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(false);
		Mockito.when(mockedRequest.getParameter("isCheck")).thenReturn("test");
		Mockito.when(userService.saveNewUser(user, "test")).thenThrow(nullPointerException);
		PortalRestResponse<String> actualPortalRestResponse = appsOSController.saveNewUser(mockedRequest, user);
		assertEquals(expectedportalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void getCurrentUserProfileTest() {
		String loginId = "guestT";
		EPUser user = mockUser.mockEPUser();
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(userService.getUserByUserId(loginId)).thenReturn(expectedList);
		String expectedString = appsOSController.getCurrentUserProfile(mockedRequest, loginId);
		assertEquals("{\"firstName\":\"test\",\"lastName\":\"test\"}", expectedString);
	}

	@Test
	public void getCurrentUserProfileXSSTest() {
		String loginId = "<iframe/src=\"data:text/html,<svg &#111;&#110;load=alert(1)>\">";
		EPUser user = mockUser.mockEPUser();
		List<EPUser> expectedList = new ArrayList<>();
		expectedList.add(user);
		Mockito.when(userService.getUserByUserId(loginId)).thenReturn(expectedList);
		String expectedString = appsOSController.getCurrentUserProfile(mockedRequest, loginId);
		assertEquals("loginId is not valid", expectedString);
	}

	@Test
	public void getCurrentUserProfileExceptionTest() {
		String loginId = "guestT";
		EPUser user = mockUser.mockEPUser();
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(userService.getUserByUserId(loginId)).thenThrow(nullPointerException);
		String expectedString = appsOSController.getCurrentUserProfile(mockedRequest, loginId);
		assertEquals("{}", expectedString);
	}

}
