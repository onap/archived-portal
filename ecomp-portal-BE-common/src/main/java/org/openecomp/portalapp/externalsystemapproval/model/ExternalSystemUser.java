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
package org.openecomp.portalapp.externalsystemapproval.model;


import java.util.ArrayList;
import java.util.List;

public class ExternalSystemUser {

	private String loginId;
	
	private String applicationName;
	
	private String myloginrequestId;
	
	private List<ExternalSystemRoleApproval> roles;

	public ExternalSystemUser() {
		roles = new ArrayList<ExternalSystemRoleApproval>();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<ExternalSystemRoleApproval> getRoles() {
		return roles;
	}

	public void setRoles(List<ExternalSystemRoleApproval> roles) {
		this.roles = roles;
	}

	public String getMyloginrequestId() {
		return myloginrequestId;
	}

	public void setMyloginrequestId(String myloginrequestId) {
		this.myloginrequestId = myloginrequestId;
	}
	
	

}
