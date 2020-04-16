/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.music.conf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.music.exceptions.MusicLockingException;
import org.onap.portalapp.music.service.MusicService;
import org.onap.portalapp.music.util.MusicUtil;
import org.springframework.session.Session;

public class MusicSessionRepositoryHandler {
	
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicSessionRepositoryHandler.class);
	private final Map<String, Session> sessions = new ConcurrentHashMap<>();
	private boolean musicCache = MusicUtil.isCached();
	
	
	public Session get(String id) {
		if(musicCache){
		 // todo need to add the clean up for "sessions" map if musicCache is enabled 	     
			return this.sessions.get(id);
		}else{
			try {
				Session session = MusicService.getMetaAttribute(id);
				return session;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "get failed with id " + id, e);
				return null;
			}
		}
	}



	public void remove(String id) {
		if(musicCache){
			 // todo need to add the clean up for "sessions" map if musicCache is enabled 	     
			sessions.remove(id);	
		}else{
			try {
				MusicService.removeSession(id);
			} catch (MusicLockingException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "removeSession locking failed with id " + id, e);
			} 
		}
	}



	public void put(String id, MusicSession musicSession) {
		if(musicCache){
			 // todo need to add the clean up for "sessions" map if musicCache is enabled 	     
			sessions.put(id, musicSession);		
		}else{
			try {
				MusicService.setMetaAttribute(musicSession);
				MusicService.cleanUpMusic();
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "setMetaAttribute failed with id " + id, e);
			}
		}
	}

}
