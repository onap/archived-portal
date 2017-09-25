package org.openecomp.portalapp.portal.service;

import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service("ticketService")
public class TicketEventServiceImpl implements TicketEventService{

	@Override
	public String getNotificationHyperLink(JsonNode application, String ticket, String eventSource) {
		String hyperlink = SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_SYSTEM_NOTIFICATION_URL)+ticket;
		return hyperlink;
	}

}
