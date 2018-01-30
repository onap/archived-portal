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
package org.onap.portalapp.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("appsCacheService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class AppsCacheServiceImple implements AppsCacheService {
	@Autowired
	EPAppService appsService;
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsCacheServiceImple.class);
	
	private static long updateTime = 0;
	private static final int cacheUpdateIntervalInSeconds = 10;
	
	private static volatile Map<Long, EPApp> appsMap;
	
	@PostConstruct
	public void init() {
		this.refreshAppsMap();
	}

	private Map<Long, EPApp> refreshAppsMap() {
		long now = System.currentTimeMillis();
		
		if(noNeedToUpdate(now))
			return null;
		
		synchronized (this) {
			if(noNeedToUpdate(now))
				return null;
			List<EPApp> allApps = appsService.getAppsFullList();
			Map<Long, EPApp> newAppsMap = new HashMap<Long, EPApp>();
			for (EPApp app : allApps) {
				newAppsMap.put(app.getId(), app);
			}
			// Switch cache with the new one.
			appsMap = newAppsMap;
			updateTime = now;
		}
		
		return appsMap;
	}

	private boolean noNeedToUpdate(long now) {
		long secondsPassed = (now - updateTime)/1000;
		if(secondsPassed < cacheUpdateIntervalInSeconds){
			logger.debug(EELFLoggerDelegate.debugLogger, "no need to refresh yet, seconds since last refresh: " + secondsPassed + ", refresh interval (sec) = " + cacheUpdateIntervalInSeconds);
			return true; // no need to update cache
		}
		return false; // its time to update
	}

	@Override
	public String getAppEndpoint(Long appId) {
		refreshAppsMap();
		EPApp app = appsMap.get(appId);
		if(app != null)
			return app.getAppRestEndpoint();
		return null;
	}
	
	@Override
	public EPApp getApp(Long appId) {
		refreshAppsMap();
		EPApp app = appsMap.get(appId);
		if(app != null)
			return app;
		return null;		
	}

}
