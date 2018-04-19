package org.onap.portalapp.portal.core;
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
//package org.onap.portalapp.portal.core;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.onap.portalapp.framework.ApplicationCommonContextTestSuite;
//import org.onap.portalapp.portal.domain.EPUser;
//import org.onap.portalapp.portal.test.core.MockEPUser;
//import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;
//import org.onap.portalapp.portal.transport.AppsListWithAdminRole;
//import org.onap.portalsdk.core.util.SystemProperties;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * 
// * Use RestURLsTestSuite to test Rest API URL's
// */
//public class RestURLsTestSuite extends ApplicationCommonContextTestSuite {
//
//	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
//			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//
//	String url = null;
//
//	MockEPUser mockUser = new MockEPUser();
//
//	public byte[] convertObjectToJsonBytes(Object object) throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		return mapper.writeValueAsBytes(object);
//	}
//
//	public void requestBuilder(String url) throws Exception {
//		EPUser user = mockUser.mockEPUser();
//
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertData(ra);
//	}
//
//	public void assertData(ResultActions ra) {
//		Assert.assertEquals("application/json", ra.andReturn().getResponse().getContentType());
//		Assert.assertEquals(200, ra.andReturn().getResponse().getStatus());
//
//	}
//
//	@Test
//	public void getMenuItemsTest() throws Exception {
//		url = "/portalApi/functionalMenu";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getMenuItemsForNotificationsTest() throws Exception {
//		url = "/portalApi/functionalMenuForNotificationTree";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getUserAppsTestnew() throws Exception {
//		url = "/portalApi/userApps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getPersUserAppsTest() throws Exception {
//		url = "/portalApi/persUserApps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAppCatalogTest() throws Exception {
//		url = "/portalApi/appCatalog";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAppListNewTest() throws Exception {
//		ResultActions ra = getMockMvc()
//				.perform(MockMvcRequestBuilders.get("/portalApi/userApplicationRoles").param("userId", "guest"));
//		assertData(ra);
//	}
//
//	@Test
//	public void getAvailableAppListTest() throws Exception {
//		url = "/portalApi/availableApps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAllAppsTest() throws Exception {
//		url = "/portalApi/allAvailableApps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getUserProfileTest() throws Exception {
//		url = "/portalApi/userProfile";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getRolesByAppTest() throws Exception {
//		EPUser user = mockUser.mockEPUser();
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/portalApi/adminAppsRoles/550");
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertData(ra);
//	}
//
//	@Test
//	public void getAppsWithAdminRoleStateForUserTest() throws Exception {
//		url = "/portalApi/adminAppsRoles?user=guest";
//		requestBuilder(url);
//
//	}
//
//	@Test
//	public void getUsersFromAppEndpointTest() throws Exception {
//		url = "/portalApi/app/550/users";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getOnboardingAppsTest() throws Exception {
//		url = "/portalApi/onboardingApps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getMenuItemsForAuthUserTest() throws Exception {
//		url = "/portalApi/functionalMenuForAuthUser";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getMenuItemsForEditingTest() throws Exception {
//		url = "/portalApi/functionalMenuForEditing";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAppRolesTest() throws Exception {
//		url = "/portalApi/appRoles/455";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void regenerateAncestorTableTest() throws Exception {
//
//		EPUser user = mockUser.mockEPUser();
//
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//				.get("/portalApi/regenerateFunctionalMenuAncestors");
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertEquals("application/json;charset=UTF-8", ra.andReturn().getResponse().getContentType());
//		Assert.assertEquals(200, ra.andReturn().getResponse().getStatus());
//
//	}
//
//	@Test
//	public void getAppListTest() throws Exception {
//		url = "/portalApi/getAppList";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getFavoritesForUserTest() throws Exception {
//		url = "/portalApi/getFavoriteItems";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getManifestTest() throws Exception {
//		url = "/portalApi/manifest";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getActiveUsersTest() throws Exception {
//		url = "/portalApi/dashboard/activeUsers";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void searchPortalTest() throws Exception {
//		EPUser user = mockUser.mockEPUser();
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/portalApi/dashboard/search")
//				.param("searchString", "guest");
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertData(ra);
//	}
//
//	@Test
//	public void getWidgetDataTest() throws Exception {
//		EPUser user = mockUser.mockEPUser();
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/portalApi/dashboard/widgetData")
//				.param("resourceType", "guest");
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertData(ra);
//	}
//
//	@Test
//	public void getAppsAndContactsTest() throws Exception {
//		url = "/portalApi/contactus/allapps";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getPortalDetailsTest() throws Exception {
//		url = "/portalApi/contactus/feedback";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAppCategoryFunctionsTest() throws Exception {
//		url = "/portalApi/contactus/functions";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getOnlineUserUpdateRateTest() throws Exception {
//		url = "/portalApi/dashboard/onlineUserUpdateRate";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getMenuIdRoleIdTest() throws Exception {
//		url = "/portalApi/getFunctionalMenuRole";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getNotificationsTest() throws Exception {
//		url = "/portalApi/getNotifications";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getAdminNotificationsTest() throws Exception {
//		url = "/portalApi/getAdminNotifications";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getNotificationAppRolesTest() throws Exception {
//		url = "/portalApi/getNotificationAppRoles";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void getNotificationUpdateRateTest() throws Exception {
//		url = "/portalApi/notificationUpdateRate";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void notificationReadTest() throws Exception {
//		url = "/portalApi/notificationRead?notificationId=262";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void testGetRolesTest() throws Exception {
//		url = "/portalApi/notificationRole/248/roles";
//		requestBuilder(url);
//	}
//
//	@Test
//	public void putAppsWithAdminRoleStateForUserTest() throws Exception {
//
//		AppsListWithAdminRole appsListWithAdminRole = new AppsListWithAdminRole();
//		appsListWithAdminRole.setOrgUserId("guest");
//		ArrayList<AppNameIdIsAdmin> appsRoles = new ArrayList<AppNameIdIsAdmin>();
//		AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
//		appNameIdIsAdmin.setId((long) 455);
//		appNameIdIsAdmin.setAppName("CCD");
//		appNameIdIsAdmin.setIsAdmin(true);
//		appNameIdIsAdmin.setRestrictedApp(false);
//		appsRoles.add(appNameIdIsAdmin);
//		appsListWithAdminRole.setAppsRoles(appsRoles);
//		EPUser user = mockUser.mockEPUser();
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/portalApi/adminAppsRoles")
//				.contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(appsListWithAdminRole));
//
//		requestBuilder.sessionAttr(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertData(ra);
//	}
//
//	@Test
//	public void ticketEventControllerForExternalSystem() throws Exception {
//		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auxapi/ticketevent")
//				.contentType(APPLICATION_JSON_UTF8).content(ticketEventJson);
//		ResultActions ra = getMockMvc().perform(requestBuilder);
//		assertTrue(ra.andReturn().getResponse().getContentType().contains("application/json"));
//	}
//}
