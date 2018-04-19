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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkUploadUserRoles implements Serializable{

	private static final long serialVersionUID = -7478654947593502185L;
	
	@Id
	@Column(name="role_name")
	private String roleName;
	@Id
	@Column(name="org_user_id")
	private String orgUserId;
	@Id
	@Column(name="auth_namespace")
	private String appNameSpace;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getOrgUserId() {
		return orgUserId;
	}
	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}
	public String getAppNameSpace() {
		return appNameSpace;
	}
	public void setAppNameSpace(String appNameSpace) {
		this.appNameSpace = appNameSpace;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appNameSpace == null) ? 0 : appNameSpace.hashCode());
		result = prime * result + ((orgUserId == null) ? 0 : orgUserId.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
		BulkUploadUserRoles other = (BulkUploadUserRoles) obj;
		if (appNameSpace == null) {
			if (other.appNameSpace != null)
				return false;
		} else if (!appNameSpace.equals(other.appNameSpace))
			return false;
		if (orgUserId == null) {
			if (other.orgUserId != null)
				return false;
		} else if (!orgUserId.equals(other.orgUserId))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
	
	
	
}
