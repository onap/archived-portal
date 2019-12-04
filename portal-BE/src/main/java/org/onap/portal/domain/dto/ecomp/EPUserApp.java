/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.domain.dto.ecomp;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.DomainVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EPUserApp extends DomainVo implements java.io.Serializable, Comparable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	@Valid
	private EPApp app;
	@Valid
	private EPRole role;

	private Integer priority;

	public EPUserApp(final Long userId, final EPApp app, final EPRole role) {
		this.userId = userId;
		this.app = app;
		this.role = role;
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

	public Integer getPriority() {
		return (this.priority == null) ? 1 : priority;
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
				&& (otherRoleIsSameAsThis(castOther))
				&& (otherPriorityIsSameAsThis(castOther));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (this.getUserId()==null ? 0 : this.getUserId().intValue());
		result = 37 * result + (this.getApp().getId()==null ? 0 : this.getApp().getId().intValue());
		result = 37 * result + (this.getRole().getId()==null ? 0 : this.getRole().getId().intValue());
		result = 37 * result + (this.priority==null ? 0 : this.priority);
		return result;
	}

	public int compareTo(Object other){
	    EPUserApp castOther = (EPUserApp) other;

	    Long c1 = (this.getUserId()==null ? 0 : this.getUserId()) + (this.getApp()==null||this.getApp().getId()==null ? 0 : this.getApp().getId()) + (this.getRole()==null||this.getRole().getId()==null ? 0 : this.getRole().getId()) + (this.priority==null ? 0 : this.priority);
	    Long c2 = (castOther.getUserId()==null ? 0 : castOther.getUserId()) + (castOther.getApp()==null||castOther.getApp().getId()==null ? 0 : castOther.getApp().getId()) + (castOther.getRole()==null||castOther.getRole().getId()==null ? 0 : castOther.getRole().getId()) + (castOther.priority==null ? 0 : castOther.priority);

	    return c1.compareTo(c2);
	}
	private boolean otherPriorityIsSameAsThis(EPUserApp other){
		return (this.priority==null && other.getPriority()==null) || this.getPriority().equals(other.getPriority());
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
