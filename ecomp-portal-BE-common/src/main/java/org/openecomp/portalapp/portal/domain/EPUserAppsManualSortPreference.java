/*-
 * ================================================================================
 * ECOMP Portal
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
package org.openecomp.portalapp.portal.domain;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class EPUserAppsManualSortPreference extends DomainVo {

	private static final long serialVersionUID = -6270136291139278216L;
	private int userId;
	private Long appId;
	private int appManualSortOrder;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public int getAppManualSortOrder() {
		return appManualSortOrder;
	}
	public void setAppManualSortOrder(int appManualSortOrder) {
		this.appManualSortOrder = appManualSortOrder;
	}

}
