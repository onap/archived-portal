/*-
 * ================================================================================
 * eCOMP Portal
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

import java.util.List;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.service.SearchService;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.ueb.UebException;
import org.openecomp.portalsdk.core.onboarding.ueb.UebManager;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsg;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class FunctionalMenuHandler { 
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FunctionalMenuHandler.class);
	
	@Autowired
	AdminRolesService adminRolesService;
	
	@Autowired
	FunctionalMenuService functionalMenuService;
	
	@Autowired
	SearchService searchSvc;
	
	@Autowired 
	DataAccessService dataAccessService;

	@Async
	public Boolean getFunctionalMenu(UebMsg requestMsg)
	{
		UebMsg returnMsg = new UebMsg();
		
		if (requestMsg == null)
		{
			logger.error(EELFLoggerDelegate.errorLogger, "handleMenuRequest received null message");
			return false;
		}
		else if (requestMsg.getSourceTopicName() == null)
		{
			logger.error(EELFLoggerDelegate.errorLogger, "A source topic name is required and not found in this msg:" + requestMsg.toString());
			return false;
		}
		else if (requestMsg.getUserId() == null)
		{
			logger.debug(EELFLoggerDelegate.debugLogger, "Error getting functional menu.  A userId is required and not found in this msg: " + requestMsg.toString());
			returnMsg.putMsgId(requestMsg.getMsgId());  // echo tells requester this is a response
			returnMsg.putPayload("Error: A userId is required.  Call msg.putUserId() with an userId");
		}
		else
		{
			logger.debug(EELFLoggerDelegate.debugLogger, "Getting functional menu for user = " + requestMsg.getUserId());
			EPUser user = searchSvc.searchUserByUserId(requestMsg.getUserId());
			
			List<FunctionalMenuItem> menuItems = null;
			if (user == null) 
			{
				logger.debug(EELFLoggerDelegate.debugLogger, "Error getting functional menu.  userId not found in directory or is guest: " + requestMsg.toString());
			} 
	        else if (adminRolesService.isSuperAdmin(user)) 
	        {  
				logger.debug(EELFLoggerDelegate.debugLogger, "FunctionalMenuHandler: SuperUser, about to call getFunctionalMenuItems()");
				menuItems = functionalMenuService.getFunctionalMenuItems();
			} 
	        else 
			{
				logger.debug(EELFLoggerDelegate.debugLogger, "getMenuItemsForAuthUser: about to call getFunctionalMenuItemsForUser()");
				menuItems = functionalMenuService.getFunctionalMenuItemsForUser(requestMsg.getUserId());
			}
		    
			if ( menuItems != null )
		    {
  			    String functionalMenuJsonString = new Gson().toJson(menuItems);
				logger.debug(EELFLoggerDelegate.debugLogger, "returning functional menu : " + functionalMenuJsonString);
			    returnMsg.putMsgId(requestMsg.getMsgId());  // echo tells requester this is a response
			    returnMsg.putPayload(functionalMenuJsonString);
			} else {
				returnMsg.putMsgId(requestMsg.getMsgId());  // echo tells requester this is a response
				returnMsg.putPayload("Error: Not found for userId = " + requestMsg.getUserId());
			}
		}
	    
		try {
        	UebManager.getInstance().publishReplyEP(returnMsg, requestMsg.getSourceTopicName());
	    } catch (UebException e) {
        	logger.error(EELFLoggerDelegate.errorLogger, "UebException occurred while responding to the Ueb message, Details:" + EcompPortalUtils.getStackTrace(e));
	    } catch (Exception e) {
        	logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while responding to the Ueb message, Details:" + EcompPortalUtils.getStackTrace(e));
	    }
        
        return true;
	}
}
