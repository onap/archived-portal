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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.TicketEventController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.TicketEventService;
import org.onap.portalapp.portal.service.UserNotificationService;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserUtils.class)
public class TicketEventControllerTest {

	@Mock
	UserNotificationService userNotificationService;
	
	@Mock
	TicketEventService ticketEventService;
	
	@InjectMocks
	TicketEventController ticketEventController = new TicketEventController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void saveUserValidationTest() throws Exception {
		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		expectedPortalRestResponse.setMessage("Invalid Org User ID");
		expectedPortalRestResponse.setResponse(null);
		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site."
				+ " The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  "
				+ "\"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,"
				+ "\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; "
				+ "ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\","
				+ "\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\","
				+ "\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": "
				+ "{\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      "
				+ "\"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},"
				+ "\"SubscriberInfo\": {\"UserList\": [\"guest\"] }}";

		actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertEquals(actualPortalRestResponse, expectedPortalRestResponse);
	}
	
	@Test
	public void saveTest() throws Exception {
		String UserIds[] = new String[1];
		UserIds[0] = "guest";
		ArrayList<EPUser> users = new ArrayList<>();
		EPUser user = new EPUser();
		user.setOrgUserId("guest");
		user.setId((long)1);
		users.add(user);
		List<String> userIdlist = new ArrayList<>();
		userIdlist.add("guest");
		JsonNode application = null ;
		String ticketEventJson = "{\"application\":\"cbus\",\"event\":{\"body\":{\"ticketStatePhrase\":\"We recently detected a problem with the equipment at your site. "
				+ "The event is in queue for immediate work.\",\"ivrNotificationFlag\":\"1\",\"expectedRestoreDate\":0,\"bridgeTransport\":\"AOTS\",\"reptRequestType\":0,"
				+ "\"ticketNum\":\"000002000857405\",\"assetID\":\"CISCO_1921C1_ISR_G2\",\"eventDate\":1490545134601,"
				+ "\"eventAbstract\":\"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; "
				+ "ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\","
				+ "\"severity\":\"2 - Major\",\"ticketPriority\":\"3\",\"reportedCustomerImpact\":0,\"testAutoIndicator\":0,"
				+ "\"supportGroupName\":\"US-TEST-ORT\",\"lastModifiedDate\":\"1487687703\",\"messageGroup\":\"SNMP\",\"csi\":0,\"mfabRestoredTime\":0},"
				+ "\"header\":{\"timestamp\":\"2017-02-21T14:35:05.219+0000\",\"eventSource\":\"aotstm\",\"entityId\":\"000002000857405\",\"sequenceNumber\":2},"
				+ "\"blinkMsgId\":\"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\":\"130.4.165.158\"},\"SubscriberInfo\":{\"UserList\":[\"guest\"]}}";
		PortalRestResponse<String> actualPortalRestResponse = new PortalRestResponse<String>();
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<String>();
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		expectedPortalRestResponse.setMessage("processEventNotification: notification created");
		expectedPortalRestResponse.setResponse("NotificationId");
		
		
		Mockito.when(userNotificationService.getUsersByOrgIds(userIdlist)).thenReturn(users);
		Mockito.when(ticketEventService.getNotificationHyperLink(application, "", "")).thenReturn("");
		actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.OK) == 0);
	}

	@Test
	public void saveXSSTest() throws Exception {
		String ticketEventJson = "<iframe %00 src=\"&Tab;javascript:prompt(1)&Tab;\"%00>";
		PortalRestResponse<String> actualPortalRestResponse;
		PortalRestResponse<String> expectedPortalRestResponse = new PortalRestResponse<>();
		expectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		expectedPortalRestResponse.setMessage("Data is not valid");
		actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
			mockedResponse, ticketEventJson);
		assertEquals(expectedPortalRestResponse, actualPortalRestResponse);
	}

	@Test
	public void saveTestForException() throws Exception {
		String ticketEventJson = "\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
	}

//	@Test
//	public void saveTestForApplicationValid() throws Exception {
//		String ticketEventJson = "{\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
//		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
//				mockedResponse, ticketEventJson);
//		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
//		assertEquals(actualPortalRestResponse.getMessage(), "application is mandatory");
//
//	}

	@Test
	public void saveTestForBodyValid() throws Exception {
		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",\"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
		assertEquals(actualPortalRestResponse.getMessage(), "body is mandatory");
	}

	@Test
	public void saveTestForEventSourceValid() throws Exception {
		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
		assertEquals(actualPortalRestResponse.getMessage(), "Message Source is mandatory");
	}

	@Test
	public void saveTestForUserListValid() throws Exception {
		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {}}";
		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
				mockedResponse, ticketEventJson);
		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
		assertEquals(actualPortalRestResponse.getMessage(), "At least one user Id is mandatory");
	}
	
	@Test
	public void saveTestForApplicationValid() throws Exception {
	String ticketEventJson = "{\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\", \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\", \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
	PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
	mockedResponse, ticketEventJson);
	assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.ERROR) == 0);
	assertEquals(actualPortalRestResponse.getMessage(), "Application is mandatory");

	}
}