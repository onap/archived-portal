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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.onap.portalsdk.core.domain.support.DomainVo;

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
