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


@SuppressWarnings("rawtypes")
public class EPUserApp implements java.io.Serializable, Comparable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private EPApp app;
	private EPRole role;
	private Short priority;
	
	public EPUserApp() {
	}
	
	public Long getAppId() {
		return this.getApp().getId();
	}
	
	public Long getRoleId() {
		return (role == null) ? null : role.getId();
	}
	
	public Long getAppRoleId() {
		return (role.getAppRoleId() == null) ? null : role.getAppRoleId();
	}
		
	@Override 
	public String toString() {
		String str = "[u: "+getUserId()+"; a: "+getAppId()+", r: "+getRoleId()+"; appRoleId: "+getAppRoleId()+"]";
		return str;
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
		
	public Short getPriority() {
		return this.priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EPUserApp))
			return false;
		EPUserApp castOther = (EPUserApp) other;

		return (this.getUserId().equals(castOther.getUserId()))
				&& (this.getApp().getId().equals(castOther.getApp().getId()))
				&& (this.getRole().getId().equals(castOther.getRole().getId()))
				&& ((this.priority==null && castOther.getPriority()==null) || this.getPriority().equals(castOther.getPriority()));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) (this.getUserId()==null ? 0 : this.getUserId().intValue());
		result = 37 * result + (int) (this.getApp().getId()==null ? 0 : this.getApp().getId().intValue());
		result = 37 * result + (int) (this.getRole().getId()==null ? 0 : this.getRole().getId().intValue());
		result = 37 * result + (int) (this.priority==null ? 0 : this.priority);
		return result;
	}

	public int compareTo(Object other){
	    EPUserApp castOther = (EPUserApp) other;

	    Long c1 = (this.getUserId()==null ? 0 : this.getUserId()) + (this.getApp()==null||this.getApp().getId()==null ? 0 : this.getApp().getId()) + (this.getRole()==null||this.getRole().getId()==null ? 0 : this.getRole().getId()) + (this.priority==null ? 0 : this.priority);
	    Long c2 = (castOther.getUserId()==null ? 0 : castOther.getUserId()) + (castOther.getApp()==null||castOther.getApp().getId()==null ? 0 : castOther.getApp().getId()) + (castOther.getRole()==null||castOther.getRole().getId()==null ? 0 : castOther.getRole().getId()) + (castOther.priority==null ? 0 : castOther.priority);

	    return c1.compareTo(c2);
	}
}
