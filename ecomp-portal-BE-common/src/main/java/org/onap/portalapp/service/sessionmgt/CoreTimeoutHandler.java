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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.service.sessionmgt;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.domain.sessionmgt.TimeoutVO;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class CoreTimeoutHandler {
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(CoreTimeoutHandler.class);

	public static final Map<String, HttpSession> sessionMap = new Hashtable<String, HttpSession>();
	public static final Integer repeatInterval = 15 * 60; // 15 minutes
	ObjectMapper mapper = new ObjectMapper();

	public static void sessionCreated(String portalJSessionId, String jSessionId, HttpSession session) {

		storeMaxInactiveTime(session);

		// this key is a combination of portal jsession id and app session id
		session.setAttribute(PortalApiConstants.PORTAL_JSESSION_ID, jSessionKey(jSessionId, portalJSessionId));
		sessionMap.put((String) session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID), session);

	}

	protected static void storeMaxInactiveTime(HttpSession session) {

		if (session.getAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME) == null)
			session.setAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME, session.getMaxInactiveInterval());
	}

	public static void sessionDestroyed(HttpSession session) {

		try {
			sessionMap.remove((String) session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "sessionDestroyed failed on session " + session.getId(), e);
		}

	}

	public String gatherSessionExtenstions() {

		Map<String, TimeoutVO> sessionTimeoutMap = new Hashtable<String, TimeoutVO>();
		String jsonMap = "";

		for (String jSessionKey : sessionMap.keySet()) {

			try {
				// get the expirytime in seconds
				HttpSession session = sessionMap.get(jSessionKey);

				Long lastAccessedTimeMilliSec = session.getLastAccessedTime();
				Long maxIntervalMilliSec = session.getMaxInactiveInterval() * 1000L;
				// Long currentTimeMilliSec = Calendar.getInstance().getTimeInMillis() ;
				// (maxIntervalMilliSec - (currentTimeMilliSec - lastAccessedTimeMilliSec) + ;
				Calendar instance = Calendar.getInstance();
				instance.setTimeInMillis(session.getLastAccessedTime());
				logger.info(EELFLoggerDelegate.errorLogger,
						"gatherSessionExtenstions: Session Management: Last Accessed time for " + jSessionKey + ": "
								+ instance.getTime());

				Long sessionTimOutMilliSec = maxIntervalMilliSec + lastAccessedTimeMilliSec;

				sessionTimeoutMap.put(portalJSessionId(jSessionKey),
						new TimeoutVO(jSessionId(jSessionKey), sessionTimOutMilliSec));

				jsonMap = mapper.writeValueAsString(sessionTimeoutMap);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "gatherSessionExtenstions failed", e);
			}
		}

		return jsonMap;

	}

	public void updateSessionExtensions(String sessionTimeoutMapStr) throws Exception {

		Map<String, TimeoutVO> sessionTimeoutMap;
		try {
			TypeReference<Hashtable<String, TimeoutVO>> typeRef = new TypeReference<Hashtable<String, TimeoutVO>>() {
			};

			sessionTimeoutMap = mapper.readValue(sessionTimeoutMapStr, typeRef);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateSessionExtensions failed 1", e);
			return;
		}
		for (String jPortalSessionId : sessionTimeoutMap.keySet()) {
			try {

				TimeoutVO extendedTimeoutVO = mapper
						.readValue(mapper.writeValueAsString(sessionTimeoutMap.get(jPortalSessionId)), TimeoutVO.class);
				HttpSession session = sessionMap.get(jSessionKey(extendedTimeoutVO.getjSessionId(), jPortalSessionId));

				if (session == null) {
					continue;
				}

				Long lastAccessedTimeMilliSec = session.getLastAccessedTime();
				Long maxIntervalMilliSec = session.getMaxInactiveInterval() * 1000L;
				Long sessionTimOutMilliSec = maxIntervalMilliSec + lastAccessedTimeMilliSec;

				Long maxTimeoutTimeMilliSec = extendedTimeoutVO.getSessionTimOutMilliSec();
				if (maxTimeoutTimeMilliSec > sessionTimOutMilliSec) {
					logger.debug(EELFLoggerDelegate.debugLogger,
							"updateSessionExtensions: Session Management: updated session max idle time");
					session.setMaxInactiveInterval((int) (maxTimeoutTimeMilliSec - lastAccessedTimeMilliSec) / 1000);
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"updateSessionExtensions failed", e);
			}

		}

	}

	protected static String jSessionKey(String jSessionId, String portalJSessionId) {
		return portalJSessionId + "-" + jSessionId;
	}

	protected String portalJSessionId(String jSessionKey) {
		return jSessionKey.split("-")[0];
	}

	protected String jSessionId(String jSessionKey) {
		return jSessionKey.split("-")[1];
	}

}
