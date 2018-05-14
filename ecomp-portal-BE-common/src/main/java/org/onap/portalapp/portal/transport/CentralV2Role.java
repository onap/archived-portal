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
package org.onap.portalapp.portal.transport;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.onap.portalapp.portal.domain.CentralV2RoleFunction;

@SuppressWarnings("rawtypes")
public class CentralV2Role implements Serializable, Comparable{
		/**
	 * 
	 */
	private static final long serialVersionUID = -4332644961113063714L;
		private Long id;
		private Date created;
		private Date modified;
		private Long createdId;
		private Long modifiedId;
		private Long rowNum;
		
	    private String  name;
	    private boolean active;
	    private Integer priority;
	    
        private SortedSet<CentralV2RoleFunction> roleFunctions = new TreeSet<>();
	    
	    private SortedSet<CentralV2Role> childRoles = new TreeSet<>();

	    private SortedSet<CentralV2Role> parentRoles = new TreeSet<>();

		public CentralV2Role(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum,
				String name, boolean active, Integer priority, SortedSet<CentralV2RoleFunction> roleFunctions,
				SortedSet<CentralV2Role> childRoles, SortedSet<CentralV2Role> parentRoles) {
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
		
		public CentralV2Role(){
			
		}
		
		public CentralV2Role(Long id, String name){
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

		public boolean getActive() {
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

		public SortedSet<CentralV2RoleFunction> getRoleFunctions() {
			return roleFunctions;
		}

		public void setRoleFunctions(SortedSet<CentralV2RoleFunction> roleFunctions) {
			this.roleFunctions = roleFunctions;
		}

		public SortedSet<CentralV2Role> getChildRoles() {
			return childRoles;
		}

		public void setChildRoles(SortedSet<CentralV2Role> childRoles) {
			this.childRoles = childRoles;
		}

		public SortedSet<CentralV2Role> getParentRoles() {
			return parentRoles;
		}

		public void setParentRoles(SortedSet<CentralV2Role> parentRoles) {
			this.parentRoles = parentRoles;
		}
		public void addRoleFunction(CentralV2RoleFunction roleFunction) {
			this.roleFunctions.add(roleFunction);
		}

		public void addChildRole(CentralV2Role role) {
			this.childRoles.add(role);
		}

		public void addParentRole(CentralV2Role role) {
			this.parentRoles.add(role);
		}
		
		public int compareTo(Object obj){
	    	CentralV2Role other = (CentralV2Role)obj;

	    	String c1 = getName();
	    	String c2 = other.getName();

	    	return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	    }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			CentralV2Role other = (CentralV2Role) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		
		
}
