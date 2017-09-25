package org.openecomp.portalapp.portal.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface TicketEventService {

	public String getNotificationHyperLink(JsonNode application, String ticket, String eventSource);
	
}
