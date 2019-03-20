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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.annotation.ApiVersion;
import org.onap.portalapp.controller.sessionmgt.SessionCommunicationVersionController;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.Analytics;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalsdk.core.domain.Role;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AopUtils.class)
public class AuxApiRequestMapperControllerTest {
	@InjectMocks
	AuxApiRequestMapperController auxApiRequestMapperController = new AuxApiRequestMapperController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	RolesController rolesController = new RolesController();
	SessionCommunicationVersionController sessionCommunicationController = new SessionCommunicationVersionController();
	WebAnalyticsExtAppVersionController webAnalyticsExtAppController = new WebAnalyticsExtAppVersionController();
	RolesApprovalSystemVersionController rolesApprovalSystemController = new RolesApprovalSystemVersionController();
	TicketEventVersionController ticketEventVersionController = new TicketEventVersionController();
	AppsControllerExternalVersionRequest appsControllerExternalVersionRequest = new AppsControllerExternalVersionRequest();
	ExternalAppsRestfulVersionController externalAppsRestfulVersionController = new ExternalAppsRestfulVersionController();

	@Mock
	ApplicationContext context;
	Method method;

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void getUserTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getUser(mockedRequest, mockedResponse, "test12"));
	}
	
	@Test
	public void getUserTestWithException() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getUser(mockedRequest, mockedResponse, "test12"));
	}

	@Test
	public void getRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void saveRoleTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/role");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		Role role = new Role();
		assertNull(auxApiRequestMapperController.saveRole(mockedRequest, mockedResponse, role));
	}

	@Test
	public void getEPRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v4/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void getUser1Test() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/user/test12");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getUser(mockedRequest, mockedResponse, "test12"));
	}

	@Test
	public void getRoleTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/role/1");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getRoleInfo(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void getUsersOfApplicationTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/users");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getUsersOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleFunctionsListTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/functions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getRoleFunctionsList(mockedRequest, mockedResponse));
	}

	@Test
	public void getRoleFunctionTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/function/test");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getRoleFunction(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void saveRoleFunctionTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/roleFunction");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse<String> response = auxApiRequestMapperController.saveRoleFunction(mockedRequest, mockedResponse, "test");
		assertNotNull(response);
	}

	@Test
	public void deleteRoleFunctionTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/roleFunction/test");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("DELETE");
		assertNull(auxApiRequestMapperController.deleteRoleFunction(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void deleteRoleTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/deleteRole/1");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("DELETE");
		assertNull(auxApiRequestMapperController.deleteRole(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void getActiveRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/activeRoles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getActiveRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void getEcompUserTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v4/user/test");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getEcompUser(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void getEcompRolesOfApplicationTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v4/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getEcompRolesOfApplication(mockedRequest, mockedResponse));
	}

	@Test
	public void getSessionSlotCheckIntervalTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/getSessionSlotCheckInterval");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", sessionCommunicationController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getSessionSlotCheckInterval(mockedRequest, mockedResponse));
	}

	@Test
	public void extendSessionTimeOutsTest() throws Exception {
		String sessionMap = "test";
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/extendSessionTimeOuts");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", sessionCommunicationController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		assertNull(auxApiRequestMapperController.extendSessionTimeOuts(mockedRequest, mockedResponse, sessionMap));
	}

	@Test
	public void getAnalyticsScriptTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/analytics");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", webAnalyticsExtAppController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getAnalyticsScript(mockedRequest, mockedResponse));
	}

	@Test
	public void storeAnalyticsScriptTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/storeAnalytics");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", webAnalyticsExtAppController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		Analytics analyticsMap = new Analytics();
		assertNull(auxApiRequestMapperController.storeAnalyticsScript(mockedRequest, mockedResponse, analyticsMap));
	}

	@Test
	public void bulkUploadFunctionsTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/portal/functions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadFunctions");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/portal/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadRoles");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadRoleFunctionsTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/portal/roleFunctions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadRoleFunctions");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadRoleFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadUserRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/portal/userRoles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadUserRoles");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadUserRoles(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadUsersSingleRoleTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/portal/userRole/1");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadUsersSingleRole");
		res.setResponse("Failed");
		assertEquals(res,
				auxApiRequestMapperController.bulkUploadUsersSingleRole(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void bulkUploadPartnerFunctionsTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/partner/roleFunctions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadPartnerRoleFunctions");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadPartnerFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void bulkUploadPartnerRolesTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/partner/roles");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		List<Role> upload = new ArrayList<>();
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadRoles");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadPartnerRoles(mockedRequest, mockedResponse, upload));
	}

	@Test
	public void bulkUploadPartnerRoleFunctionsTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/upload/partner/roleFunctions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		PortalRestResponse res = new PortalRestResponse();
		res.setStatus(PortalRestStatusEnum.ERROR);
		res.setMessage("Failed to bulkUploadPartnerRoleFunctions");
		res.setResponse("Failed");
		assertEquals(res, auxApiRequestMapperController.bulkUploadPartnerRoleFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void getMenuFunctionsTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/menuFunctions");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getMenuFunctions(mockedRequest, mockedResponse));
	}

	@Test
	public void postUserProfileTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/userProfile");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesApprovalSystemController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		assertNull(auxApiRequestMapperController.postUserProfile(mockedRequest, extSysUser, mockedResponse));
	}

	@Test
	public void putUserProfileTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/userProfile");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesApprovalSystemController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("PUT");
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		assertNull(auxApiRequestMapperController.putUserProfile(mockedRequest, extSysUser, mockedResponse));
	}

	@Test
	public void deleteUserProfileTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/userProfile");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesApprovalSystemController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("DELETE");
		ExternalSystemUser extSysUser = new ExternalSystemUser();
		assertNull(auxApiRequestMapperController.deleteUserProfile(mockedRequest, extSysUser, mockedResponse));
	}

	@Test
	public void handleRequestTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/ticketevent");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", ticketEventVersionController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		assertNull(auxApiRequestMapperController.handleRequest(mockedRequest, mockedResponse, "test"));
	}

	@Test
	public void postPortalAdminTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/portalAdmin");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", appsControllerExternalVersionRequest);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		EPUser epUser = new EPUser();
		assertNull(auxApiRequestMapperController.postPortalAdmin(mockedRequest, mockedResponse, epUser));
	}

	@Test
	public void getOnboardAppExternalTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/onboardApp/1");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", appsControllerExternalVersionRequest);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getOnboardAppExternal(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void postOnboardAppExternalTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/onboardApp");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", appsControllerExternalVersionRequest);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		OnboardingApp newOnboardApp = new OnboardingApp();
		assertNull(auxApiRequestMapperController.postOnboardAppExternal(mockedRequest, mockedResponse, newOnboardApp));
	}

	@Test
	public void putOnboardAppExternalTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/onboardApp/1");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", appsControllerExternalVersionRequest);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("PUT");
		OnboardingApp newOnboardApp = new OnboardingApp();
		assertNull(auxApiRequestMapperController.putOnboardAppExternal(mockedRequest, mockedResponse, (long) 1,
				newOnboardApp));
	}

	@Test
	public void publishNotificationTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/publishNotification");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", externalAppsRestfulVersionController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("POST");
		EpNotificationItem notificationItem = new EpNotificationItem();
		assertNull(auxApiRequestMapperController.publishNotification(mockedRequest, notificationItem, mockedResponse));
	}

	@Test
	public void getFavoritesForUserTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/getFavorites");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", externalAppsRestfulVersionController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getFavoritesForUser(mockedRequest, mockedResponse));
	}

	@Test
	public void functionalMenuItemsForUserTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/functionalMenuItemsForUser");
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", externalAppsRestfulVersionController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("GET");
		assertNull(auxApiRequestMapperController.getFunctionalMenuItemsForUser(mockedRequest, mockedResponse));
	}
	
	@Test
	public void updateAppRoleDescriptionApiTest() throws Exception {
		Mockito.when(mockedRequest.getRequestURI()).thenReturn("/auxapi/v3/update/app/roleDescription");
		PortalRestResponse<String> expectedportalRestResponse = new PortalRestResponse<String>();
		expectedportalRestResponse.setMessage("updateAppRoleDescription: null");
		expectedportalRestResponse.setResponse("Failure");
		expectedportalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		Mockito.when(mockedRequest.getHeader("MinorVersion")).thenReturn("0");
		Map<String, Object> beans = new HashMap<>();
		beans.put("bean1", rolesController);
		Mockito.when(context.getBeansWithAnnotation(ApiVersion.class)).thenReturn(beans);
		PowerMockito.mockStatic(AopUtils.class);
		Mockito.when(AopUtils.isAopProxy(Matchers.anyObject())).thenReturn(false);
		Mockito.when(mockedRequest.getMethod()).thenReturn("PUT");
		assertEquals(auxApiRequestMapperController.updateAppRoleDescription(mockedRequest, mockedResponse),
				expectedportalRestResponse);
	}

}
