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
package org.onap.portalapp.portal.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.util.SystemProperties;

public class UserRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	/* 
	private static final HashMap<Long, String> rolesDictionary;
	// remove hard coded roles
	static {
		rolesDictionary = new HashMap<Long, String>();
		rolesDictionary.put(Long.valueOf(SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID)).longValue(), "superAdmin");
		rolesDictionary.put(Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ACCOUNT_ADMIN_ROLE_ID)).longValue(), "admin");
	}
	*/

	public UserRoles(UserRole user) {
		setOrgUserId(user.getOrgUserId());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setGuestSession(user.getUser_Id()==-1 ? true : false);

		addRole(user.getRoleName());
	}

	public void addRole(String roleName) {
		//String normalizedRole = normalizeRole(roleId);
		if (!getRoles().contains(roleName)) {
			this.roles.add(roleName);
		}
	}

	/*
	public static String normalizeRole(Long role) {
		String roleTranslated = rolesDictionary.get(role);
		return roleTranslated == null ? "user" : roleTranslated;
	}
	*/

	private String orgUserId;
	private String firstName;
	private String lastName;
	private boolean guestSession;

	// TODO: Make into set
	private List<String> roles = new ArrayList<String>();

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

	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setGuestSession(boolean guestSession) {
		this.guestSession = guestSession;
	}

	public boolean getGuestSession() {
		return this.guestSession;
	}
}
