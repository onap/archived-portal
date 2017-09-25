/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.TicketEventService;
import org.openecomp.portalapp.portal.service.UserNotificationService;
import org.openecomp.portalapp.portal.transport.EpNotificationItem;
import org.openecomp.portalapp.portal.transport.EpRoleNotificationItem;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

/**
 * Receives messages from the Collaboration Bus (C-BUS) notification and event
 * brokering tool. Creates notifications for ECOMP Portal users.
 */
@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class TicketEventController implements BasicAuthenticationController {


	@Autowired
	private UserNotificationService userNotificationService;
	
	@Autowired
	private TicketEventService ticketEventService;

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(TicketEventController.class);

	public boolean isAuxRESTfulCall() {
		return true;
	}

	private final ObjectMapper mapper = new ObjectMapper();



	@ApiOperation(value = "Accepts messages from external ticketing systems and creates notifications for Portal users.", response = PortalRestResponse.class)
	@RequestMapping(value = { "/ticketevent" }, method = RequestMethod.POST)
	public PortalRestResponse<String> handleRequest(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String ticketEventJson) throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "Ticket Event notification" + ticketEventJson);
		PortalRestResponse<String> portalResponse = new PortalRestResponse<>();
		try {
			JsonNode ticketEventNotif = mapper.readTree(ticketEventJson);

			// Reject request if required fields are missing.
			String error = validateTicketEventMessage(ticketEventNotif);
			if (error != null) {
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage(error);
				response.setStatus(400);
				return portalResponse;
			}

			EpNotificationItem epItem = new EpNotificationItem();
			epItem.setCreatedDate(new Date());
			epItem.setIsForOnlineUsers("Y");
			epItem.setIsForAllRoles("N");
			epItem.setActiveYn("Y");

			JsonNode event = ticketEventNotif.get("event");
			JsonNode header = event.get("header");
			JsonNode body = event.get("body");
			JsonNode application = ticketEventNotif.get("application");
			epItem.setMsgDescription(body.toString());
			Long eventDate = System.currentTimeMillis();
			if (body.get("eventDate") != null) {
				eventDate = body.get("eventDate").asLong();
			}
			String eventSource = header.get("eventSource").asText();
			epItem.setMsgSource(eventSource);
			String ticket = body.get("ticketNum").asText();
			String hyperlink = ticketEventService.getNotificationHyperLink(application, ticket, eventSource);			
			if(body.get("notificationHyperlink")!=null){
				hyperlink=body.get("notificationHyperlink").asText();
			}
			epItem.setNotificationHyperlink(hyperlink);
			epItem.setStartTime(new Date(eventDate));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(epItem.getStartTime());
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth + 30);
			epItem.setEndTime(calendar.getTime());
			String severityString = "1";
			if (body.get("severity") != null) {
				severityString = (body.get("severity").toString()).substring(1, 2);
			}
			Long severity = Long.parseLong(severityString);
			epItem.setPriority(severity);
			epItem.setCreatorId(null);
			Set<EpRoleNotificationItem> roles = new HashSet<>();
			JsonNode SubscriberInfo = ticketEventNotif.get("SubscriberInfo");
			JsonNode userList = SubscriberInfo.get("UserList");
			String UserIds[] = userList.toString().replace("[", "").replace("]", "").trim().replace("\"", "")
					.split(",");
			String assetID = eventSource + ' '
					+ userList.toString().replace("[", "").replace("]", "").trim().replace("\"", "") + ' '
					+ new Date(eventDate);
			if (body.get("assetID") != null) {
				assetID = body.get("assetID").asText();
			}
			epItem.setMsgHeader(assetID);
			List<EPUser> users = userNotificationService.getUsersByOrgIds(Arrays.asList(UserIds));
			for (String userId : UserIds) {
				EpRoleNotificationItem roleNotifItem = new EpRoleNotificationItem();
				for (EPUser user : users) {
					if (user.getOrgUserId().equals(userId)) {
						roleNotifItem.setRecvUserId(user.getId().intValue());
						roles.add(roleNotifItem);
						break;
					}
				}

			}
			epItem.setRoles(roles);
			userNotificationService.saveNotification(epItem);

			portalResponse.setStatus(PortalRestStatusEnum.OK);
			portalResponse.setMessage("processEventNotification: notification created");
			portalResponse.setResponse("NotificationId is :" + epItem.notificationId);
		} catch (Exception ex) {
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			response.setStatus(400);
			portalResponse.setMessage(ex.toString());
		}
		return portalResponse;
	}

	/**
	 * Validates that mandatory fields are present.
	 * 
	 * @param ticketEventNotif
	 * @return Error message if a problem is found; null if all is well.
	 */
	private String validateTicketEventMessage(JsonNode ticketEventNotif) {
		JsonNode application = ticketEventNotif.get("application");
		JsonNode event = ticketEventNotif.get("event");
		JsonNode header = event.get("header");
		JsonNode eventSource=header.get("eventSource");
		JsonNode body = event.get("body");
		JsonNode SubscriberInfo = ticketEventNotif.get("SubscriberInfo");
		JsonNode userList = SubscriberInfo.get("UserList");

		if (application == null||application.asText().length()==0||application.asText().equalsIgnoreCase("null"))
			return "Application is mandatory";
		if (body == null)
			return "body is mandatory";
		if (eventSource == null||eventSource.asText().trim().length()==0||eventSource.asText().equalsIgnoreCase("null"))
			return "Message Source is mandatory";
		if (userList == null)
			return "At least one user Id is mandatory";
		JsonNode eventDate=body.get("eventDate");
		
		if(eventDate!=null&&eventDate.asText().length()==8)
			return "EventDate is invalid";
		String UserIds[] = userList.toString().replace("[", "").replace("]", "").trim().replace("\"", "")
				.split(",");		
		List<EPUser> users = userNotificationService.getUsersByOrgIds(Arrays.asList(UserIds));
		if(users==null||users.size()==0)
			return "Invalid Org User ID";
		return null;
	}
	
}
