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
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.AppsControllerExternalRequest;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.PortalAdminService;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.portal.service.UserServiceImpl;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.util.EPUserUtils;

public class AppsControllerExternalRequestTest extends MockitoTestSuite {

	@Mock
	AdminRolesService adminRolesService;

	@Mock
	EPAppService appService;

	@Mock
	PortalAdminService portalAdminService;

	@Mock
	UserService userService = new UserServiceImpl();

	@InjectMocks
	AppsControllerExternalRequest appsControllerExternalRequest = new AppsControllerExternalRequest();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();
	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Test
	public void postPortalAdminIfUSerNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Missing required field: email, loginId, or loginPwd");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("java.lang.NullPointerException");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenThrow(nullPointerException);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfNotFoundTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfFoundTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
/*reference*/
	@Test
	public void postPortalAdminCreateUserIfNotSuperAdminTest() throws Exception {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postPortalAdminCreateUserIfFieldValidatorErrorTest() throws Exception {
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 500);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FieldsValidator [httpStatusCode=500, errorCode=null, fields=null]");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);
		Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postPortalAdmin(mockedRequest, mockedResponse, user);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void getOnboardAppExternalTest() {
		EPApp epApp = new EPApp();
		Long appId = (long) 1;
		Mockito.when(appService.getApp(appId)).thenReturn(epApp);
		OnboardingApp expectedApp = new OnboardingApp();
		Mockito.doNothing().when(appService).createOnboardingFromApp(epApp, expectedApp);
		OnboardingApp actualApp = appsControllerExternalRequest.getOnboardAppExternal(mockedRequest, mockedResponse,
				appId);
		assertEquals(expectedApp.getClass(), actualApp.getClass());
	}

	@Test
	public void postOnboardAppExternalExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Unexpected field: id");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);

		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = (long) 1;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postOnboardAppExternalTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(
				"Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);

		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.postOnboardAppExternal(mockedRequest, mockedResponse, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);

	}

	@Test
	public void putOnboardAppExternalifAppNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Unexpected value for field: id");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		Long appId = null;
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = null;

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void putOnboardAppExternalIfOnboardingAppDetailsNullTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(
				"Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		Long appId = (long) 1;
		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = (long) 1;
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void putOnboardAppExternalIfOnboardingAppDetailsTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("Failed to find user: testUser");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		Long appId = (long) 1;

		OnboardingApp expectedOnboardingApp = new OnboardingApp();
		expectedOnboardingApp.id = (long) 1;
		expectedOnboardingApp.name = "test";
		expectedOnboardingApp.url="test.com";
		expectedOnboardingApp.restUrl="test1.com";
		expectedOnboardingApp.myLoginsAppOwner="testUser";
		expectedOnboardingApp.restrictedApp=false;
		expectedOnboardingApp.isOpen=true;
		expectedOnboardingApp.isEnabled=true;
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(userService.getUserByUserId(user.getOrgUserId())).thenReturn(expectedList);

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId, expectedOnboardingApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void putOnboardAppExternalIfOnboardingAppDetailsTest2() throws Exception {

		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage(null);
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
		
		Long appId = (long) 1;
		
		OnboardingApp oldOnboardApp = new OnboardingApp();
		oldOnboardApp.id = (long) 1;
		oldOnboardApp.name = "test";
		oldOnboardApp.url="test.com";
		oldOnboardApp.restUrl="test1.com";
		oldOnboardApp.myLoginsAppOwner="12";
		oldOnboardApp.restrictedApp=false;
		oldOnboardApp.isOpen=true;
		oldOnboardApp.isEnabled=true;
		
		
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(oldOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
		//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(appService.modifyOnboardingApp(oldOnboardApp, user)).thenReturn(expectedFieldValidator);
		//Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.putOnboardAppExternal(mockedRequest, mockedResponse, appId, oldOnboardApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
		}
	
	
	@Test
	public void putOnboardAppExternalTest() throws Exception {

		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("FieldsValidator [httpStatusCode=500, errorCode=null, fields=null]");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		
		Long appId = (long) 1;
		
		OnboardingApp oldOnboardApp = new OnboardingApp();
		oldOnboardApp.id = (long) 1;
		oldOnboardApp.name = "test";
		oldOnboardApp.url="test.com";
		oldOnboardApp.restUrl="test1.com";
		oldOnboardApp.myLoginsAppOwner="12";
		oldOnboardApp.restrictedApp=false;
		oldOnboardApp.isOpen=true;
		oldOnboardApp.isEnabled=true;
		
		
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		List<EPUser> expectedList = new ArrayList<EPUser>();
		expectedList.add(user);
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(oldOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
		//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		

		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 500);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(appService.modifyOnboardingApp(oldOnboardApp, user)).thenReturn(expectedFieldValidator);
		//Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.putOnboardAppExternal(mockedRequest, mockedResponse, appId, oldOnboardApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
		}
	
	@Test
	public void putOnboardAppExternalIsNotSuperAdminTest() throws Exception {

	PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
	expectedportalRestResponse.setMessage("User lacks Portal admin role: Test");
	expectedportalRestResponse.setResponse(null);
	PortalRestStatusEnum portalRestStatusEnum = null;
	expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
	
	Long appId = (long) 1;
	
	OnboardingApp oldOnboardApp = new OnboardingApp();
	oldOnboardApp.id = (long) 1;
	oldOnboardApp.name = "test";
	oldOnboardApp.url="test.com";
	oldOnboardApp.restUrl="test1.com";
	oldOnboardApp.myLoginsAppOwner="12";
	oldOnboardApp.restrictedApp=false;
	oldOnboardApp.isOpen=true;
	oldOnboardApp.isEnabled=true;
	
	
	EPUser user = mockUser.mockEPUser();
	user.setEmail("guestT@test.portal.onap.org");
	user.setLoginPwd("pwd");
	user.setLoginId("Test");
	List<EPUser> expectedList = new ArrayList<EPUser>();
	expectedList.add(user);
	Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	Mockito.when(userService.getUserByUserId(oldOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
	//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
	Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);

	FieldsValidator expectedFieldValidator = new FieldsValidator();
	expectedFieldValidator.setHttpStatusCode((long) 200);
	expectedFieldValidator.setFields(null);
	expectedFieldValidator.setErrorCode(null);
	//Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

	PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.putOnboardAppExternal(mockedRequest, mockedResponse, appId, oldOnboardApp);
	assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void putOnboardAppExternalWithExceptionTest() {
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("java.lang.NullPointerException");
		expectedportalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
		EPUser user = mockUser.mockEPUser();
		user.setEmail("guestT@test.portal.onap.org");
		user.setLoginPwd("pwd");
		user.setLoginId("Test");
		Long appId = (long) 1;
		
		OnboardingApp oldOnboardApp = new OnboardingApp();
		oldOnboardApp.id = (long) 1;
		oldOnboardApp.name = "test";
		oldOnboardApp.url="test.com";
		oldOnboardApp.restUrl="test1.com";
		oldOnboardApp.myLoginsAppOwner="12";
		oldOnboardApp.restrictedApp=false;
		oldOnboardApp.isOpen=true;
		oldOnboardApp.isEnabled=true;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(userService.getUserByUserId(oldOnboardApp.myLoginsAppOwner)).thenThrow(nullPointerException);
		PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest
				.putOnboardAppExternal(mockedRequest, mockedResponse, appId,oldOnboardApp);
		assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void postOnboardAppExternalwithExceptionTest() throws Exception {

	PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
	expectedportalRestResponse.setMessage("User lacks Portal admin role: Test");
	expectedportalRestResponse.setResponse(null);
	PortalRestStatusEnum portalRestStatusEnum = null;
	expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
	
	Long appId = (long) 1;
	
	
	
	OnboardingApp newOnboardApp = new OnboardingApp();
	//newOnboardApp.id = (long) 1;
	newOnboardApp.name = "test";
	newOnboardApp.url="test.com";
	newOnboardApp.restUrl="test1.com";
	newOnboardApp.myLoginsAppOwner="12";
	newOnboardApp.restrictedApp=false;
	newOnboardApp.isOpen=true;
	newOnboardApp.isEnabled=true;
	
	
	EPUser user = mockUser.mockEPUser();
	user.setEmail("guestT@test.portal.onap.org");
	user.setLoginPwd("pwd");
	user.setLoginId("Test");
	List<EPUser> expectedList = new ArrayList<EPUser>();
	expectedList.add(user);
	Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	Mockito.when(userService.getUserByUserId(newOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
	//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
	Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);

	FieldsValidator expectedFieldValidator = new FieldsValidator();
	expectedFieldValidator.setHttpStatusCode((long) 200);
	expectedFieldValidator.setFields(null);
	expectedFieldValidator.setErrorCode(null);
	//Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

	PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.postOnboardAppExternal(mockedRequest, mockedResponse,newOnboardApp);
	assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}

	@Test
	public void postOnboardAppExternalIsNotSuperAdminTest() throws Exception {

	PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
	expectedportalRestResponse.setMessage("java.lang.NullPointerException");
	expectedportalRestResponse.setResponse(null);
	PortalRestStatusEnum portalRestStatusEnum = null;
	expectedportalRestResponse.setStatus(portalRestStatusEnum.ERROR);
	
	Long appId = (long) 1;
	
	
	
	OnboardingApp newOnboardApp = new OnboardingApp();
	//newOnboardApp.id = (long) 1;
	newOnboardApp.name = "test";
	newOnboardApp.url="test.com";
	newOnboardApp.restUrl="test1.com";
	newOnboardApp.myLoginsAppOwner="12";
	newOnboardApp.restrictedApp=false;
	newOnboardApp.isOpen=true;
	newOnboardApp.isEnabled=true;
	
	
	EPUser user = mockUser.mockEPUser();
	user.setEmail("guestT@test.portal.onap.org");
	user.setLoginPwd("pwd");
	user.setLoginId("Test");
	List<EPUser> expectedList = new ArrayList<EPUser>();
	expectedList.add(user);
	Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	Mockito.when(userService.getUserByUserId(newOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
	//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
	Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);

	FieldsValidator expectedFieldValidator = new FieldsValidator();
	expectedFieldValidator.setHttpStatusCode((long) 200);
	expectedFieldValidator.setFields(null);
	expectedFieldValidator.setErrorCode(null);
	//Mockito.when(portalAdminService.createPortalAdmin(user.getOrgUserId())).thenReturn(expectedFieldValidator);

	PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.postOnboardAppExternal(mockedRequest, mockedResponse,newOnboardApp);
	assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
	@Test
	public void postOnboardAppExternalIsSuperAdminTest() throws Exception {

	PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
	expectedportalRestResponse.setMessage(null);
	expectedportalRestResponse.setResponse(null);
	PortalRestStatusEnum portalRestStatusEnum = null;
	expectedportalRestResponse.setStatus(portalRestStatusEnum.OK);
	
	Long appId = (long) 1;
	
	
	
	OnboardingApp newOnboardApp = new OnboardingApp();
	//newOnboardApp.id = (long) 1;
	newOnboardApp.name = "test";
	newOnboardApp.url="test.com";
	newOnboardApp.restUrl="test1.com";
	newOnboardApp.myLoginsAppOwner="12";
	newOnboardApp.restrictedApp=false;
	newOnboardApp.isOpen=true;
	newOnboardApp.isEnabled=true;
	
	
	EPUser user = mockUser.mockEPUser();
	user.setEmail("guestT@test.portal.onap.org");
	user.setLoginPwd("pwd");
	user.setLoginId("Test");
	List<EPUser> expectedList = new ArrayList<EPUser>();
	expectedList.add(user);
	Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	Mockito.when(userService.getUserByUserId(newOnboardApp.myLoginsAppOwner)).thenReturn(expectedList);
	//Mockito.when(userService.saveNewUser(user, "Yes")).thenReturn(null);
	Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);

	FieldsValidator expectedFieldValidator = new FieldsValidator();
	expectedFieldValidator.setHttpStatusCode((long) 200);
	expectedFieldValidator.setFields(null);
	expectedFieldValidator.setErrorCode(null);
	Mockito.when(appService.addOnboardingApp(newOnboardApp, user)).thenReturn(expectedFieldValidator);

	PortalRestResponse<String> actualPortalRestResponse = appsControllerExternalRequest.postOnboardAppExternal(mockedRequest, mockedResponse,newOnboardApp);
	assertEquals(actualPortalRestResponse, expectedportalRestResponse);
	}
	
}
