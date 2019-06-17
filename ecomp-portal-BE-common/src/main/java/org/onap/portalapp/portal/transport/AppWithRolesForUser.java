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
 * 
 */
package org.onap.portalapp.portal.transport;

import java.util.List;

public class AppWithRolesForUser {

	public String orgUserId;
		
	public boolean isSystemUser;

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

	

	public boolean isSystemUser() {
		return isSystemUser;
	}

	public void setSystemUser(boolean isSystemUser) {
		this.isSystemUser = isSystemUser;
	}

	@Override
	public String toString() {
		return "AppWithRolesForUser [orgUserId=" + orgUserId + ", isSystemUser=" + isSystemUser + ", appId=" + appId
				+ ", appName=" + appName + ", appRoles=" + appRoles + "]";
	}

}
