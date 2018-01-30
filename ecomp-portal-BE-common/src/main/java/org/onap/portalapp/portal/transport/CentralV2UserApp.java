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

@SuppressWarnings("rawtypes")
public class CentralV2UserApp implements Serializable, Comparable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4954830080839125389L;
	private Long userId;
	private CentralApp app;
	private CentralV2Role role;
	private Short priority;
	
	
	
	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public CentralApp getApp() {
		return app;
	}



	public void setApp(CentralApp app) {
		this.app = app;
	}



	public CentralV2Role getRole() {
		return role;
	}



	public void setRole(CentralV2Role role) {
		this.role = role;
	}



	public Short getPriority() {
		return priority;
	}



	public void setPriority(Short priority) {
		this.priority = priority;
	}



	public int compareTo(Object other){
	    CentralV2UserApp castOther = (CentralV2UserApp) other;

	    Long c1 = (this.getUserId()==null ? 0 : this.getUserId()) + (this.priority==null ? 0 : this.priority);
	    Long c2 = (castOther.getUserId()==null ? 0 : castOther.getUserId()) + (castOther.getApp()==null||castOther.getApp().getId()==null ? 0 : castOther.getApp().getId()) + (castOther.priority==null ? 0 : castOther.priority);

	    return c1.compareTo(c2);
	}
	
}
