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

import java.util.List;

import org.openecomp.portalapp.kpidash.model.KpiApiStats;
import org.openecomp.portalapp.kpidash.model.KpiUserStoryStats;

@SuppressWarnings("rawtypes")
public interface KpiDashboardService {
	public List getUserStories();

	public void saveUserStories(KpiUserStoryStats kpiUserStoryStats);

	public void updateUserStories(KpiUserStoryStats kpiUserStoryStats);

	public List getUserApis();

	public void saveUserApis(KpiApiStats kpiApiStats);

	public void updateUserApis(KpiApiStats kpiApiStats);

	public List getKpiServiceSupported();

	public List executeGetBytesPublishedDelivered();

	public List executeGetFeedStats();

	public List getLOCStatsCat();

	public List getLOCStats();

	public String getGeoMapUrl();

	public String getRCloudAUrl();

	public String getGeoMapApiUrl();
}
