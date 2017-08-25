package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.TicketEventController;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.UserNotificationService;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserUtils.class)
public class TicketEventControllerTest {

	@Mock
	UserNotificationService userNotificationService;

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

//	@Test
//	public void saveTest() throws Exception {
//		String ticketEventJson = "{\"application\": \"cbus\",\"event\": {\"body\": {\"ticketStatePhrase\": \"We recently detected a problem with the equipment at your site. The event is in queue for immediate work.\", \"ivrNotificationFlag\": \"1\",\"expectedRestoreDate\": 0,\"bridgeTransport\": \"AOTS\",  \"reptRequestType\": 0,\"ticketNum\": \"000002000857405\",\"assetID\": \"CISCO_1921C1_ISR_G2\", \"eventDate\": 1490545134601,\"eventAbstract\": \"ospfIfConfigError trap received from Cisco_1921c1_ISR_G2 with arguments: ospfRouterId=Cisco_1921c1_ISR_G2; ospfIfIpAddress=1921c1_288266; ospfAddressLessIf=0; ospfPacketSrc=172.17.0.11; ospfConfigErrorType=2; ospfPacketType=1\",\"severity\": \"2 - Major\",\"ticketPriority\": \"3\",\"reportedCustomerImpact\": 0,\"testAutoIndicator\": 0,\"supportGroupName\": \"US-TEST-ORT\",\"lastModifiedDate\": \"1487687703\",\"messageGroup\": \"SNMP\",\"csi\": 0,\"mfabRestoredTime\": 0},\"header\": {\"timestamp\": \"2017-02-21T14:35:05.219+0000\",\"eventSource\": \"aotstm\",\"entityId\": \"000002000857405\",      \"sequenceNumber\": 2 },\"blinkMsgId\": \"f38c071e-1a47-4b55-9e72-1db830100a61\",\"sourceIP\": \"130.4.165.158\"},\"SubscriberInfo\": {\"UserList\": [\"hk8777\"] }}";
//		PortalRestResponse<String> actualPortalRestResponse = ticketEventController.handleRequest(mockedRequest,
//				mockedResponse, ticketEventJson);
//		assertTrue(actualPortalRestResponse.getStatus().compareTo(PortalRestStatusEnum.OK) == 0);
//	}

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
}