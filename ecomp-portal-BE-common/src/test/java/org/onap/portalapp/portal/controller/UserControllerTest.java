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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.UserController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.portal.service.UserServiceImpl;
import org.onap.portalapp.portal.transport.ProfileDetail;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CipherUtil.class)
public class UserControllerTest extends MockitoTestSuite {

	@InjectMocks
	UserController userController = new UserController();

	@Mock
	UserService userService = new UserServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getLoggedinUserExceptionTest() {
		EPUser epUser = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<ProfileDetail> expectedResponse = new PortalRestResponse<ProfileDetail>();
		expectedResponse.setMessage(null);
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		PortalRestResponse<ProfileDetail> response = userController.getLoggedinUser(mockedRequest);
		assertEquals(response, expectedResponse);
	}

	@Test
	public void getLoggedinUserTest() throws Exception {
		EPUser epUser = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<ProfileDetail> expectedResponse = new PortalRestResponse<ProfileDetail>();
		expectedResponse.setMessage("success");
		ProfileDetail profileDetail = new ProfileDetail();
		expectedResponse.setResponse(profileDetail);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.OK);
		PowerMockito.mockStatic(CipherUtil.class);
		Mockito.when(CipherUtil.decryptPKC(epUser.getLoginPwd())).thenReturn("Password");
		PortalRestResponse<ProfileDetail> response = userController.getLoggedinUser(mockedRequest);
		assertEquals(response.getMessage(), expectedResponse.getMessage());
		assertEquals(response.getStatus(), expectedResponse.getStatus());
	}

	@Test
	public void modifyLoggedinUserIfProfileNullTest() {
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("java.lang.NullPointerException");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		ProfileDetail profileDetail = null;
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		assertEquals(actualResponse, expectedResponse);
		assertEquals(actualResponse.getStatus(), expectedResponse.getStatus());
	}

	@Test
	public void modifyLoggedinUserExceptionTest() {
		EPUser epUser =null;
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("java.lang.NullPointerException");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		ProfileDetail profileDetail = new ProfileDetail();
		profileDetail.setFirstName("Test_FirstName");
		profileDetail.setLastName("Test_LastName");
		profileDetail.setEmail("Test_Email");
		profileDetail.setLoginId("Test_LoginId");
		profileDetail.setLoginPassword("Test_LoginPassword");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		assertEquals(actualResponse, expectedResponse);

	}

	/*@Test
	public void modifyLoggedinUserTest() throws Exception {
		EPUser epUser = mockUser.mockEPUser();
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("success");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.OK);
		ProfileDetail profileDetail = new ProfileDetail();
		profileDetail.setFirstName("Test_FirstName");
		profileDetail.setLastName("Test_LastName");
		profileDetail.setEmail("Test_Email");
		profileDetail.setLoginId("Test_LoginId");
		profileDetail.setLoginPassword("Test_LoginPassword");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PowerMockito.mockStatic(CipherUtil.class);
		Mockito.when(CipherUtil.decrypt(epUser.getLoginPwd())).thenReturn("Password");
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		System.out.println(actualResponse);
	}*/
}
