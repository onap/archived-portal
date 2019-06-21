/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 *
 * Modifications Copyright (C) 2019 IBM.
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
package org.onap.portalapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

@ServerEndpoint("/opencontact")
public class PeerBroadcastSocket {
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PeerBroadcastSocket.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	protected static final Map<String, Object> channelMap = new HashMap<>();
	private Map<String, String> sessionMap = new HashMap<>();

	@OnMessage
	public void message(String message, Session session) {
		try {
			Map<String, Object> jsonObject = mapper.readValue(message, Map.class);
			save(jsonObject, session);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed" + ex.getMessage());
		}
	}

	@OnOpen
	public void open(Session session) {
		logger.info(EELFLoggerDelegate.debugLogger, "Channel opened");
	}

	@OnClose
	public void close(Session session) {
		String channel = sessionMap.get(session.getId());
		if (channel != null) {
			Object sessObj = channelMap.get(channel);
			if (sessObj != null) {
				try {
					((Session) sessObj).close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "Failed to close" + e.getMessage());
				}
			}
			channelMap.remove(channel);
		}
		logger.info(EELFLoggerDelegate.debugLogger, "Channel closed");
	}

	private void save(Map<String, Object> jsonObject, Session session) {
		final Optional<String> from = Optional.of(jsonObject.get("from").toString());
		if (from.isPresent() && channelMap.get(from.get()) == null) {
			this.channelMap.put(from.toString(), session);
			this.sessionMap.put(session.getId(), from.toString());
		}
	}

}

