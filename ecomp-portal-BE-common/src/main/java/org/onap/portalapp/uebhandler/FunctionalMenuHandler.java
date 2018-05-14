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
package org.onap.portalapp.uebhandler;

import java.util.List;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.ueb.UebException;
import org.onap.portalsdk.core.onboarding.ueb.UebManager;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;
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
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FunctionalMenuHandler.class);

	@Autowired
	private AdminRolesService adminRolesService;

	@Autowired
	private FunctionalMenuService functionalMenuService;

	@Autowired
	private SearchService searchSvc;

	@Async
	public Boolean getFunctionalMenu(UebMsg requestMsg) {
		UebMsg returnMsg = new UebMsg();

		if (requestMsg == null) {
			logger.error(EELFLoggerDelegate.errorLogger, "handleMenuRequest received null message");
			return false;
		} else if (requestMsg.getSourceTopicName() == null) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"A source topic name is required and not found in this msg:" + requestMsg.toString());
			return false;
		} else if (requestMsg.getUserId() == null) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"Error getting functional menu.  A userId is required and not found in this msg: "
							+ requestMsg.toString());
			returnMsg.putMsgId(requestMsg.getMsgId()); // echo tells requester this is a response
			returnMsg.putPayload("Error: A userId is required.  Call msg.putUserId() with an userId");
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"Getting functional menu for user = " + requestMsg.getUserId());
			EPUser user = searchSvc.searchUserByUserId(requestMsg.getUserId());

			List<FunctionalMenuItem> menuItems = null;
			if (user == null) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"Error getting functional menu.  userId not found in directory or is guest: "
								+ requestMsg.toString());
			} else if (adminRolesService.isSuperAdmin(user)) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"FunctionalMenuHandler: SuperUser, about to call getFunctionalMenuItems()");
				menuItems = functionalMenuService.getFunctionalMenuItems();
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"getMenuItemsForAuthUser: about to call getFunctionalMenuItemsForUser()");
				menuItems = functionalMenuService.getFunctionalMenuItemsForUser(requestMsg.getUserId());
			}

			if (menuItems != null) {
				String functionalMenuJsonString = new Gson().toJson(menuItems);
				logger.debug(EELFLoggerDelegate.debugLogger, "returning functional menu : " + functionalMenuJsonString);
				returnMsg.putMsgId(requestMsg.getMsgId()); // echo tells requester this is a response
				returnMsg.putPayload(functionalMenuJsonString);
			} else {
				returnMsg.putMsgId(requestMsg.getMsgId()); // echo tells requester this is a response
				returnMsg.putPayload("Error: Not found for userId = " + requestMsg.getUserId());
			}
		}

		try {
			UebManager.getInstance().publishReplyEP(returnMsg, requestMsg.getSourceTopicName());
		} catch (UebException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenu failed on UEB exception", e);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFunctionalMenu failed", e);
		}

		return true;
	}
}
