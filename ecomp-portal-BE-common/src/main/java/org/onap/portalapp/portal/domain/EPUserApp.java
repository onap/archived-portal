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

import javax.validation.Valid;
import org.onap.portalsdk.core.domain.support.DomainVo;

@SuppressWarnings("rawtypes")
public class EPUserApp extends DomainVo implements java.io.Serializable, Comparable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	@Valid
	private EPApp app;
	@Valid
	private EPRole role;
	private Integer priority;
	
	public EPUserApp() {
	}
	
	public Long getAppId() {
		return this.getApp().getId();
	}
	
	public Long getRoleId() {
		return (role == null) ? null : role.getId();
	}
	
	public Long getAppRoleId() {
		return this.role.getAppRoleId();
	}
		
	@Override 
	public String toString() {
		return "[u: "+getUserId()+"; a: "+getAppId()+", r: "+getRoleId()+"; appRoleId: "+getAppRoleId()+"]";
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long id) {
		this.userId = id;
	}

	public EPApp getApp() {
		return app;
	}

	public void setApp(EPApp app) {
		this.app = app;
	}

	public EPRole getRole() {
		return role;
	}

	public void setRole(EPRole role) {
		this.role = role;
	}
		
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EPUserApp))
			return false;
		EPUserApp castOther = (EPUserApp) other;

		return (otherUserIdIsSameAsThisUserId(castOther))
				&& (otherAppIdIsSameAsThis(castOther))
				&& (otherRoleIsSameAsThis(castOther));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) (this.getUserId()==null ? 0 : this.getUserId().intValue());
		result = 37 * result + (int) (this.getApp().getId()==null ? 0 : this.getApp().getId().intValue());
		result = 37 * result + (int) (this.getRole().getId()==null ? 0 : this.getRole().getId().intValue());
		return result;
	}

	public int compareTo(Object other){
	    EPUserApp castOther = (EPUserApp) other;

	    Long c1 = (this.getUserId()==null ? 0 : this.getUserId()) + (this.getApp()==null||this.getApp().getId()==null ? 0 : this.getApp().getId()) + (this.getRole()==null||this.getRole().getId()==null ? 0 : this.getRole().getId());
	    Long c2 = (castOther.getUserId()==null ? 0 : castOther.getUserId()) + (castOther.getApp()==null||castOther.getApp().getId()==null ? 0 : castOther.getApp().getId()) + (castOther.getRole()==null||castOther.getRole().getId()==null ? 0 : castOther.getRole().getId());

	    return c1.compareTo(c2);
	}
	
	private boolean otherRoleIsSameAsThis(EPUserApp other){
		return this.getRole().getId().equals(other.getRole().getId());
	}

	private boolean otherAppIdIsSameAsThis(EPUserApp other){
		return this.getApp().getId().equals(other.getApp().getId());
	}

	private boolean otherUserIdIsSameAsThisUserId(EPUserApp other){
		return this.getUserId().equals(other.getUserId());
	}
}
