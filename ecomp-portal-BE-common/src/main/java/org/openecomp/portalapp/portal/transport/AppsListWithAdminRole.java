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

import java.util.ArrayList;

public class AppsListWithAdminRole {

	public String orgUserId;

	public ArrayList<AppNameIdIsAdmin> appsRoles;

	public AppsListWithAdminRole() {
		appsRoles = new ArrayList<AppNameIdIsAdmin>();
	}

	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public ArrayList<AppNameIdIsAdmin> getAppsRoles() {
		return appsRoles;
	}

	public void setAppsRoles(ArrayList<AppNameIdIsAdmin> appsRoles) {
		this.appsRoles = appsRoles;
	}

	@Override
	public String toString() {
		return "AppsListWithAdminRole [orgUserId=" + orgUserId + ", appsRoles=" + appsRoles + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appsRoles == null) ? 0 : appsRoles.hashCode());
		result = prime * result + ((orgUserId == null) ? 0 : orgUserId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppsListWithAdminRole other = (AppsListWithAdminRole) obj;
		if (appsRoles == null) {
			if (other.appsRoles != null)
				return false;
		} else if (!appsRoles.equals(other.appsRoles))
			return false;
		if (orgUserId == null) {
			if (other.orgUserId != null)
				return false;
		} else if (!orgUserId.equals(other.orgUserId))
			return false;
		return true;
	}

}
