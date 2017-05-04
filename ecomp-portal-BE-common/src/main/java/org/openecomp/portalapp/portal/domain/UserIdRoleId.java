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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserIdRoleId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
	private String user_Id;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="ORG_USER_ID")
	private String orgUserId;
	
	@Column(name="APP_ID")
	private String appId;

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String id) {
		this.user_Id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}
}
