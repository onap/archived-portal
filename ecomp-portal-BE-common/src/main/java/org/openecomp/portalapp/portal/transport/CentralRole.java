/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.transport;

import java.util.Date;
import java.util.SortedSet;

import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPRole;

public class CentralRole implements Comparable {
	public Long id;
	public Date created;
	public Date modified;
	public Long createdId;
	public Long modifiedId;
	public Long rowNum;

	public String name;
	public boolean active;
	public Integer priority;

	public SortedSet<CentralRoleFunction> roleFunctions = null;

	public SortedSet<CentralRole> childRoles = null;

	public SortedSet<CentralRole> parentRoles = null;

	public CentralRole(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, String name,
			boolean active, Integer priority, SortedSet<CentralRoleFunction> roleFunctions,
			SortedSet<CentralRole> childRoles, SortedSet<CentralRole> parentRoles) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.createdId = createdId;
		this.modifiedId = modifiedId;
		this.rowNum = rowNum;
		this.name = name;
		this.active = active;
		this.priority = priority;
		this.roleFunctions = roleFunctions;
		this.childRoles = childRoles;
		this.parentRoles = parentRoles;
	}

	public CentralRole() {

	}

	public CentralRole(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Long getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	public Long getModifiedId() {
		return modifiedId;
	}

	public void setModifiedId(Long modifiedId) {
		this.modifiedId = modifiedId;
	}

	public Long getRowNum() {
		return rowNum;
	}

	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public SortedSet<CentralRoleFunction> getRoleFunctions() {
		return roleFunctions;
	}

	public void setRoleFunctions(SortedSet<CentralRoleFunction> roleFunctions) {
		this.roleFunctions = roleFunctions;
	}

	public SortedSet<CentralRole> getChildRoles() {
		return childRoles;
	}

	public void setChildRoles(SortedSet<CentralRole> childRoles) {
		this.childRoles = childRoles;
	}

	public SortedSet<CentralRole> getParentRoles() {
		return parentRoles;
	}

	public void setParentRoles(SortedSet<CentralRole> parentRoles) {
		this.parentRoles = parentRoles;
	}

	public int compareTo(Object obj) {
		EPRole other = (EPRole) obj;

		String c1 = getName();
		String c2 = other.getName();

		return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((childRoles == null) ? 0 : childRoles.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((createdId == null) ? 0 : createdId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((modifiedId == null) ? 0 : modifiedId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentRoles == null) ? 0 : parentRoles.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((roleFunctions == null) ? 0 : roleFunctions.hashCode());
		result = prime * result + ((rowNum == null) ? 0 : rowNum.hashCode());
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
		CentralRole other = (CentralRole) obj;
		if (active != other.active)
			return false;
		if (childRoles == null) {
			if (other.childRoles != null)
				return false;
		} else if (!childRoles.equals(other.childRoles))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdId == null) {
			if (other.createdId != null)
				return false;
		} else if (!createdId.equals(other.createdId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (modifiedId == null) {
			if (other.modifiedId != null)
				return false;
		} else if (!modifiedId.equals(other.modifiedId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentRoles == null) {
			if (other.parentRoles != null)
				return false;
		} else if (!parentRoles.equals(other.parentRoles))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (roleFunctions == null) {
			if (other.roleFunctions != null)
				return false;
		} else if (!roleFunctions.equals(other.roleFunctions))
			return false;
		if (rowNum == null) {
			if (other.rowNum != null)
				return false;
		} else if (!rowNum.equals(other.rowNum))
			return false;
		return true;
	}

}
