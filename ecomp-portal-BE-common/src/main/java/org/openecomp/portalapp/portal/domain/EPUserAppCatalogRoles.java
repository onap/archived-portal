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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude
public class EPUserAppCatalogRoles extends DomainVo {

	private static final long serialVersionUID = -5259869298825093816L;


	@Id
	@Column(name="requested_role_id")
	public Long requestedRoleId;
	
	
	@Id
	@Column(name="role_name")
	public String rolename;
	
	@Id
	@Column(name="request_status")
	public String requestStatus;
	
	@Id
	@Column(name="app_id")
	public Long appId;

	public Long getRequestedRoleId() {
		return requestedRoleId;
	}

	public void setRequestedRoleId(Long requestedRoleId) {
		this.requestedRoleId = requestedRoleId;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String toString() {
		return "EPUserAppCatalogRoles [requestedRoleId=" + requestedRoleId + ", rolename=" + rolename
				+ ", requestStatus=" + requestStatus + ", appId=" + appId + "]";
	}

	
}
