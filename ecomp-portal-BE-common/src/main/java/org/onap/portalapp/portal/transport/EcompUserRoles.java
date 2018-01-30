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
package org.onap.portalapp.portal.transport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EcompUserRoles implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "org_id")
	private Long orgId;
	@Id
	@Column(name = "manager_id")
	private String managerId;
	@Id
	@Column(name = "first_name")
	private String firstName;
	@Id
	@Column(name = "middle_name")
	private String middleInitial;
	@Id
	@Column(name = "last_name")
	private String lastName;
	@Id
	@Column(name = "phone")
	private String phone;
	@Id
	@Column(name = "email")
	private String email;
	@Id
	@Column(name = "hrid")
	private String hrid;
	@Id
	@Column(name = "org_user_id")
	private String orgUserId;
	@Id
	@Column(name = "org_code")
	private String orgCode;
	@Id
	@Column(name = "org_manager_userid")
	private String orgManagerUserId;
	@Id
	@Column(name = "job_title")
	private String jobTitle;
	@Id
	@Column(name = "login_id")
	private String loginId;

	@Id
	@Column(name = "app_role_id")
	private Long roleId;
	@Id
	@Column(name = "role_name")
	private String roleName;
	@Id
	@Column(name = "active_yn")
	private boolean active;
 
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHrid() {
		return hrid;
	}
	public void setHrid(String hrid) {
		this.hrid = hrid;
	}
	public String getOrgUserId() {
		return orgUserId;
	}
	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgManagerUserId() {
		return orgManagerUserId;
	}
	public void setOrgManagerUserId(String orgManagerUserId) {
		this.orgManagerUserId = orgManagerUserId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "EcompUserRoles [orgId=" + orgId + ", managerId=" + managerId + ", firstName=" + firstName
				+ ", middleInitial=" + middleInitial + ", lastName=" + lastName + ", phone=" + phone + ", email="
				+ email + ", hrid=" + hrid + ", orgUserId=" + orgUserId + ", orgCode=" + orgCode + ", orgManagerUserId="
				+ orgManagerUserId + ", jobTitle=" + jobTitle + ", loginId=" + loginId + ", active=" + active
				+ ", roleId=" + roleId + ", roleName=" + roleName + "]";
	}
	

}
