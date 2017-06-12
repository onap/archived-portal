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
package org.openecomp.portalapp.portal.transport;

import java.util.List;

public class AppWithRolesForUser {

	public String orgUserId;

	public Long appId;

	public String appName;

	public List<RoleInAppForUser> appRoles;

	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<RoleInAppForUser> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(List<RoleInAppForUser> appRoles) {
		this.appRoles = appRoles;
	}

	@Override
	public String toString() {
		return "AppWithRolesForUser [orgUserId=" + orgUserId + ", appId=" + appId + ", appName=" + appName
				+ ", appRoles=" + appRoles + "]";
	}

}
