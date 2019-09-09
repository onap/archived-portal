/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.service.fn.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.dto.transport.OnboardingApp;
import org.onap.portal.service.fn.FnAppService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("appsCacheService")
@Configuration
@EnableAspectJAutoProxy
public class AppsCacheService {
	@Autowired
	private
	FnAppService appsService;
	
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsCacheService.class);
	
	final class CacheConfiguration {
		
		private long updateTime = 0;
		private int updateInterval = 10;
		
		public CacheConfiguration (long _updateTime, int _updateInterval) {
			updateTime = _updateTime;
			updateInterval = _updateInterval;
		}
	}
	
	private CacheConfiguration quickRefreshCacheConf = null;
	private CacheConfiguration slowRefreshCacheConf = null;
	
	
	private static volatile Map<Long, FnApp> appsMap;
	private static volatile Map<String, FnApp> uebAppsMap;
	
	@PostConstruct
	public void init() {
		quickRefreshCacheConf = new CacheConfiguration(0, 120);
		slowRefreshCacheConf = new CacheConfiguration(0, 3600);
		
		this.refreshAppsMap(quickRefreshCacheConf);
	}

	private void refreshAppsMap(CacheConfiguration conf) {
		long now = System.currentTimeMillis();
		
		if(noNeedToUpdate(now, conf))
			return;
		
		synchronized (this) {
			if(noNeedToUpdate(now, conf))
				return;
			List<FnApp> allApps = appsService.getAppsFullList();
			Map<Long, FnApp> newAppsMap = new HashMap<>();
			for (FnApp app : allApps) {
				newAppsMap.put(app.getId(), app);
			}
			
			Map<String, FnApp> newUebAppsMap = new HashMap<>();
			for (FnApp app : allApps) {
				newUebAppsMap.put(app.getUebKey(), app);
			}
			// Switch cache with the new one.
			appsMap = newAppsMap;
			uebAppsMap = newUebAppsMap;
			conf.updateTime = now;
		}
		
	}

	private boolean noNeedToUpdate(long now, CacheConfiguration conf) {
		long secondsPassed = (now - conf.updateTime)/1000;
		if(secondsPassed < conf.updateInterval){
			logger.debug(EELFLoggerDelegate.debugLogger, "no need to refresh yet, seconds since last refresh: " + secondsPassed + ", refresh interval (sec) = " + conf.updateInterval);
			return true; // no need to update cache
		}
		return false; // its time to update
	}

	public String getAppEndpoint(Long appId) {
		refreshAppsMap(quickRefreshCacheConf);
		FnApp app = appsMap.get(appId);
		if(app != null)
			return app.getAppRestEndpoint();
		return null;
	}

	public List<OnboardingApp> getAppsFullList() {
		refreshAppsMap(quickRefreshCacheConf);
		List<FnApp> appList = new ArrayList<FnApp> (appsMap.values());
		appList.removeIf(app -> app.getId() == 1);
		List<FnApp> appsFinalList = appList.stream()
		.filter(app -> app.getEnabled() && !app.getOpen()).collect(Collectors.toList());
		
		List<OnboardingApp> onboardingAppsList = new ArrayList<OnboardingApp>();
		for (FnApp app : appsFinalList) {
			OnboardingApp onboardingApp = new OnboardingApp();
			appsService.createOnboardingFromApp(app, onboardingApp);
			onboardingAppsList.add(onboardingApp);
		}
		return onboardingAppsList;	
	}

	public FnApp getApp(Long appId) {
		refreshAppsMap(quickRefreshCacheConf);
		FnApp app = appsMap.get(appId);
		if(app != null)
			return app;
		return null;		
	}

	public FnApp getAppFromUeb(String appKey) {
		return 	getAppFromUeb(appKey,0);	
	}

	public FnApp getAppFromUeb(String appKey, Integer quickCacheRefresh) {
		refreshAppsMap(quickCacheRefresh == 1 ? quickRefreshCacheConf:slowRefreshCacheConf);
		FnApp app = uebAppsMap.get(appKey);
		if(app != null)
			return app;
		return null;		
	}

}
