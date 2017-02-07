/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.kpidash.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openecomp.portalapp.kpidash.model.KpiApiStats;
import org.openecomp.portalapp.kpidash.model.KpiServiceSupported;
import org.openecomp.portalapp.kpidash.model.KpiUserStoryStats;
import org.openecomp.portalapp.kpidash.model.KpidashProperties;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Service("kpiDashboardService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class KpiDashboardServiceImpl implements KpiDashboardService {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(KpiDashboardServiceImpl.class);
	
	@Autowired
	private DataAccessService dataAccessService;

	@Override
	public void saveUserStories(KpiUserStoryStats kpiUserStoryStats) {
		dataAccessService.saveDomainObject(kpiUserStoryStats, null);
	}

	@Override
	public void updateUserStories(KpiUserStoryStats kpiUserStoryStats) {
		dataAccessService.saveDomainObject(kpiUserStoryStats, null);
	}

	@Override
	public List getUserStories() {
		return dataAccessService.getList(KpiUserStoryStats.class, null);
	}

	@Override
	public List getUserApis() {
		return dataAccessService.getList(KpiApiStats.class, null);
	}

	@Override
	public void saveUserApis(KpiApiStats kpiApiStats) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserApis(KpiApiStats kpiApiStats) {
		dataAccessService.saveDomainObject(kpiApiStats, null);
	}

	@Override
	public List getKpiServiceSupported() {
		return dataAccessService.getList(KpiServiceSupported.class, null);
	}

	@Override
	public List<String> executeGetBytesPublishedDelivered() {
		List list = dataAccessService.executeNamedQuery("getBytesPublishedDelivered", null, null);
		List<String> newStringList = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			Object[] retunVals = (Object[]) list.get(0); // current entry in
															// VSPEntAdmin table
			newStringList.add((String) retunVals[0]);
			newStringList.add((String) retunVals[1]);
		}
		return newStringList;
	}

	@Override
	public List<Long> executeGetFeedStats() {
		List list = dataAccessService.executeNamedQuery("getDataRouterFeedStats", null, null);
		List<Long> newLongList = new ArrayList<Long>();
		if (list != null && list.size() > 0) {
			Object[] retunVals = (Object[]) list.get(0); // current entry in
															// VSPEntAdmin table
			newLongList.add((Long) retunVals[0]);
			newLongList.add((Long) retunVals[1]);
			newLongList.add((Long) retunVals[2]);
		}
		return newLongList;
	}

	@Override
	public List<String> getLOCStatsCat() {
		List list = dataAccessService.executeNamedQuery("getLOCStatsCat", null, null);
		List<String> newStringList = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				/* Object[] retunVals = (Object[]) list.get(i); */// current
																	// entry in
																	// VSPEntAdmin
																	// table
				newStringList.add((String) list.get(i));
			}
		}
		return newStringList;
	};

	@Override
	public List<Long> getLOCStats() {
		List list = dataAccessService.executeNamedQuery("getLOCStats", null, null);
		List<Long> newLongList = new ArrayList<Long>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				/* Object[] retunVals = (Object[]) list.get(i); */// current
																	// entry in
																	// VSPEntAdmin
																	// table
				newLongList.add(((BigDecimal) list.get(i)).longValue());
			}
		}
		return newLongList;
	};

	@Override
	public String getGeoMapUrl() {
		String newURL = "";
		try {
			newURL = KpidashProperties.getProperty(KpidashProperties.GEO_MAP_URL);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return newURL;
	}

	@Override
	public String getRCloudAUrl() {
		String newURL = "";
		try {
			newURL = KpidashProperties.getProperty(KpidashProperties.RCLOUD_A_URL);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return newURL;
	}

	@Override
	public String getGeoMapApiUrl() {
		String newURL = "";
		try {
			newURL = KpidashProperties.getProperty(KpidashProperties.GEO_API_URL);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return newURL;
	}

}
