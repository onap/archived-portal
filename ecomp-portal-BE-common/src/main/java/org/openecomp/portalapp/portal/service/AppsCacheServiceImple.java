/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

@Service("appsCacheService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class AppsCacheServiceImple implements AppsCacheService {
	@Autowired
	EPAppService appsService;
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsCacheServiceImple.class);
	
	final class CacheConfiguration {
		
		private long updateTime = 0;
		private int updateInterval = 10;
		
		public CacheConfiguration (long _updateTime, int _updateInterval) {
			updateTime = _updateTime;
			updateInterval = _updateInterval;
		}
	}
	
	CacheConfiguration appConf = null;
	CacheConfiguration analyticsAppConf = null;
	
	
	private static volatile Map<Long, EPApp> appsMap;
	private static volatile Map<String, EPApp> anlyticsAppsMap;
	
	@PostConstruct
	public void init() {
		appConf = new CacheConfiguration(0, 10);
		analyticsAppConf = new CacheConfiguration(0, 3600);
		
		this.refreshAppsMap(appConf);
	}

	private Map<Long, EPApp> refreshAppsMap(CacheConfiguration conf) {
		long now = System.currentTimeMillis();
		
		if(noNeedToUpdate(now, conf))
			return null;
		
		synchronized (this) {
			if(noNeedToUpdate(now, conf))
				return null;
			List<EPApp> allApps = appsService.getAppsFullList();
			Map<Long, EPApp> newAppsMap = new HashMap<Long, EPApp>();
			for (EPApp app : allApps) {
				newAppsMap.put(app.getId(), app);
			}
			
			Map<String, EPApp> newAnalyticsAppsMap = new HashMap<String, EPApp>();
			for (EPApp app : allApps) {
				newAnalyticsAppsMap.put(app.getUebKey(), app);
			}
			// Switch cache with the new one.
			appsMap = newAppsMap;
			anlyticsAppsMap = newAnalyticsAppsMap;
			conf.updateTime = now;
		}
		
		return appsMap;
	}

	private boolean noNeedToUpdate(long now, CacheConfiguration conf) {
		long secondsPassed = (now - conf.updateTime)/1000;
		if(secondsPassed < conf.updateInterval){
			logger.debug(EELFLoggerDelegate.debugLogger, "no need to refresh yet, seconds since last refresh: " + secondsPassed + ", refresh interval (sec) = " + conf.updateInterval);
			return true; // no need to update cache
		}
		return false; // its time to update
	}

	@Override
	public String getAppEndpoint(Long appId) {
		refreshAppsMap(appConf);
		EPApp app = appsMap.get(appId);
		if(app != null)
			return app.getAppRestEndpoint();
		return null;
	}
	
	@Override
	public EPApp getApp(Long appId) {
		refreshAppsMap(appConf);
		EPApp app = appsMap.get(appId);
		if(app != null)
			return app;
		return null;		
	}
	
	@Override
	public EPApp getAppForAnalytics(String appKey) {
		refreshAppsMap(analyticsAppConf);
		EPApp app = anlyticsAppsMap.get(appKey);
		if(app != null)
			return app;
		return null;		
	}

}
