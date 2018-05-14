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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.onap.portalapp.portal.ueb.EPUebMsgTypes;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;
import org.onap.portalsdk.core.onboarding.ueb.UebMsgTypes;
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
public class MainUebHandler {
	final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MainUebHandler.class);

	private ConcurrentLinkedQueue<UebMsg> inboxQueue = null;

	@Autowired
	private FunctionalMenuHandler funcMenuHandler;

	@Autowired
	private WidgetNotificationHandler widgetNotificationHandler;

	@Async
	public void runHandler(ConcurrentLinkedQueue<UebMsg> queue) {
		inboxQueue = queue;
		logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "==> MainUebHandler started");
		while (true) {
			UebMsg msg = null;
			while ((msg = inboxQueue.poll()) != null) {
				if ((msg.getMsgType() != null)
						&& (!msg.getMsgType().equalsIgnoreCase(EPUebMsgTypes.UEB_MSG_TYPE_HEALTH_CHECK))) {
					// TODO: switch this back to debug
					logger.info(EELFLoggerDelegate.errorLogger,
							dateFormat.format(new Date()) + "<== Received UEB message : " + msg.toString());
					logger.info(EELFLoggerDelegate.debugLogger,
							dateFormat.format(new Date()) + "<== Received UEB message : " + msg.toString());
					MDC.put(EPCommonSystemProperties.PARTNER_NAME, msg.getSourceTopicName());
					MDC.put(Configuration.MDC_SERVICE_NAME, msg.getMsgType().toString());
					switch (msg.getMsgType()) {
					case UebMsgTypes.UEB_MSG_TYPE_GET_FUNC_MENU: {
						funcMenuHandler.getFunctionalMenu(msg);
						break;
					}
					case UebMsgTypes.UEB_MSG_TYPE_WIDGET_NOTIFICATION: {
						widgetNotificationHandler.handleWidgetNotification(msg);
						break;
					}
					default: {
						logger.info(EELFLoggerDelegate.debugLogger,
								dateFormat.format(new Date()) + "Unknown UEB message type " + msg.toString());
						break;
					}
					}
				}
			}

			if (Thread.interrupted()) {
				logger.info(EELFLoggerDelegate.errorLogger, "==> UebMainHandler exiting");
				break;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "runHandler interrupted", e);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "runHandler failed", e);
			}
		}
	}
}
