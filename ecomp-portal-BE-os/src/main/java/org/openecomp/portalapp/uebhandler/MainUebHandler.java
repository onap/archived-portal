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
package org.openecomp.portalapp.uebhandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openecomp.portalapp.portal.ueb.EPUebMsgTypes;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsg;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsgTypes;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.att.eelf.configuration.Configuration;


//-------------------------------------------------------------------------
// Listens for received UEB messages and handles the messages
//
// Note: To implement a synchronous reply call getMsgId on the request 
//       and putMsgId on the reply (echoing the request MsgId).
//       
//-------------------------------------------------------------------------
@Component("MainUebHandler")
public class MainUebHandler 
{
	final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MainUebHandler.class);
	
	ConcurrentLinkedQueue<UebMsg> inboxQueue = null;
	
	@Autowired 
	FunctionalMenuHandler funcMenuHandler;
	
	@Autowired
	WidgetNotificationHandler widgetNotificationHandler;

	@Async
	public void runHandler(ConcurrentLinkedQueue<UebMsg> queue)
	{
		inboxQueue = queue;
    	logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "==> MainUebHandler started");
		while (true)
		{
			UebMsg msg = null;
		    while ((msg = inboxQueue.poll()) != null) 
		    {
		    	if ((msg.getMsgType() != null) && (!msg.getMsgType().equalsIgnoreCase(EPUebMsgTypes.UEB_MSG_TYPE_HEALTH_CHECK)))
		    	{
		    	    // TODO: switch this back to debug
		    	    logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== Received UEB message : " + msg.toString());
		    	    logger.info(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== Received UEB message : " + msg.toString());
		    	    MDC.put(EPSystemProperties.PARTNER_NAME, msg.getSourceTopicName());
		    	    MDC.put(Configuration.MDC_SERVICE_NAME, msg.getMsgType().toString());
		    	    switch(msg.getMsgType())
		    	    {
		    	        case UebMsgTypes.UEB_MSG_TYPE_GET_FUNC_MENU:
		    	        {
		    	        	funcMenuHandler.getFunctionalMenu(msg);
		    	            break;
		    	        }		    	    
		    	        case UebMsgTypes.UEB_MSG_TYPE_WIDGET_NOTIFICATION:
		    	        {
		    	    	    widgetNotificationHandler.handleWidgetNotification(msg);
		    	            break;
		    	        }
		    	        default:
		    	        {	
  				    	    logger.info(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "Unknown UEB message type " + msg.toString());
		    	    	    break;
		    	        }
		    	    }
		    	}
	        }
		    
		    if (Thread.interrupted())
		    {
		    	logger.info(EELFLoggerDelegate.errorLogger, "==> UebMainHandler exiting");
		    	break;
		    }
		    
		    try {
				Thread.sleep(10);  
			} catch (InterruptedException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "UebMainHandler interrupted during sleep" + EcompPortalUtils.getStackTrace(e));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred during sleep" + EcompPortalUtils.getStackTrace(e));
			}
		}
	}
}
