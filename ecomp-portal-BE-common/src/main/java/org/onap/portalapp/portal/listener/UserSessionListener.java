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

package org.onap.portalapp.portal.listener;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalsdk.core.domain.support.CollaborateList;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Listens for session-create and session-destroy events.
 */
@WebListener
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserSessionListener implements HttpSessionListener {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserSessionListener.class);

	/**
	 * Access to the database
	 */
	@Autowired
	SharedContextService sharedContextService;

	public void init(ServletConfig config) {
	}

	/**
	 * Adds sessions to the context scoped HashMap when they begin.
	 */
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		HashMap activeUsers = (HashMap) context.getAttribute("activeUsers");

		activeUsers.put(session.getId(), session);
		context.setAttribute("activeUsers", activeUsers);
		logger.info(EELFLoggerDelegate.debugLogger, "Session Created : " + session.getId());
	}

	/**
	 * Removes sessions from the context scoped HashMap when they expire or are
	 * invalidated.
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		try {
			HttpSession session = event.getSession();
			ServletContext context = session.getServletContext();
			HashMap activeUsers = (HashMap) context.getAttribute("activeUsers");
			activeUsers.remove(session.getId());

			EPUser user = (EPUser) session
					.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
			if (user != null && !StringUtils.isEmpty(user.getOrgUserId())) {
				CollaborateList.delUserName(user.getOrgUserId());
			}

			// Remove any shared context entries for this session.
			if (getSharedContextService() != null) {
				getSharedContextService().deleteSharedContexts(session.getId());

				// Clean the shared context each time a session is destroyed.
				// TODO: move the threshold to configuration file.
				getSharedContextService().expireSharedContexts(60 * 60 * 8);
			}

			logger.info(EELFLoggerDelegate.debugLogger, "Session Destroyed : " + session.getId());

		} catch (Exception e) {
			logger.warn(EELFLoggerDelegate.errorLogger, "sessionDestroyed failed", e);
		}
	}

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext _applicationContext) {
		applicationContext = _applicationContext;
	}

	public SharedContextService getSharedContextService() {
		if (sharedContextService == null) {
			if (applicationContext != null)
				sharedContextService = (SharedContextService) applicationContext.getBean("sharedContextService");
		}

		return sharedContextService;
	}

	public void setSharedContextService(SharedContextService sharedContextService) {
		this.sharedContextService = sharedContextService;
	}
}
