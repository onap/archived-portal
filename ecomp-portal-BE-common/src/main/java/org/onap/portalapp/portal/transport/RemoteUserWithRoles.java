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

import java.util.ArrayList;
import java.util.List;

/**
 * User description which we receive in response from request to remote
 * application: applicationsRestClientService.get(RemoteUserWithRoles[].class,
 * appId, "/users"). It contains the most important info about remote
 * application user including his roles in this application.
 */
public class RemoteUserWithRoles {

	private Long orgId;

	private Long managerId;

	private String firstName;

	private String middleInitial;

	private String lastName;

	private String phone;

	private String email;

	private String hrid;

	private String orgUserId;

	private String orgCode;

	private String orgManagerUserId;

	private String jobTitle;

	private String loginId;

	private Boolean active;

	private List<RemoteRole> roles = new ArrayList<RemoteRole>();

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
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
		if(this.orgUserId == null)
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<RemoteRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RemoteRole> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "RemoteUserWithRoles [orgId=" + orgId + ", managerId=" + managerId + ", firstName=" + firstName
				+ ", middleInitial=" + middleInitial + ", lastName=" + lastName + ", phone=" + phone + ", email="
				+ email + ", hrid=" + hrid + ", orgUserId=" + orgUserId + ", orgCode=" + orgCode + ", orgManagerUserId="
				+ orgManagerUserId + ", jobTitle=" + jobTitle + ", loginId=" + loginId + ", active=" + active
				+ ", roles=" + roles + "]";
	}
	
	
	
	
}
