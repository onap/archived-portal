/*-
 * ================================================================================
 * ECOMP Portal SDK
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

import java.util.ArrayList;
import java.util.List;

/**
 * A specific application user and his application specific roles.
 *
 */
public class UserApplicationRoles {

	private Long appId;

	private String orgUserId;

	private String firstName;

	private String lastName;

	private List<RemoteRole> roles = new ArrayList<RemoteRole>();

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<RemoteRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RemoteRole> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return "UserApplicationRoles [appId=" + appId + ", orgUserId=" + orgUserId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", roles=" + roles + "]";
	}
}
