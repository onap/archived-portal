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
import java.util.List;

import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.ueb.UebException;
import org.onap.portalsdk.core.onboarding.ueb.UebManager;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class WidgetNotificationHandler {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetNotificationHandler.class);

	final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	@Autowired
	EPAppService appSvc;

	@Autowired
	SearchService searchSvc;

	public WidgetNotificationHandler() {
	}

	@Async
	public void handleWidgetNotification(UebMsg requestMsg) {
		if (requestMsg.getUserId() != null) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"handleWidgetNotification: getting widgets/apps for user = " + requestMsg.getUserId());
			EPUser user = searchSvc.searchUserByUserId(requestMsg.getUserId());
			if (user != null && (appSvc != null)) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Debug mytag: " + appSvc);
				List<EPApp> apps = appSvc.getUserApps(user);
				for (EPApp app : apps) {
					if (app.getUebTopicName() != null) {
						UebMsg widgetMsg = new UebMsg();
						widgetMsg.putSourceTopicName(app.getUebTopicName());
						logger.debug(EELFLoggerDelegate.debugLogger, "app.getUebTopicName was invoked");
						widgetMsg.putPayload(requestMsg.getPayload());
						try {
							logger.debug(EELFLoggerDelegate.debugLogger, "Sending widget notification from "
									+ requestMsg.getSourceTopicName() + " to " + app.getUebTopicName());
							UebManager.getInstance().publishEP(widgetMsg, app.getUebTopicName());
						} catch (UebException e) {
							logger.error(EELFLoggerDelegate.errorLogger, "handleWidgetNotification failed", e);
						}
					}
				}
			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"handleWidgetNotification: user " + requestMsg.getUserId() + " not found, source = "
								+ requestMsg.getSourceTopicName()
								+ ". This widget notification cannot be posted to other widgets");
			}
		}
	}

}
