package org.openecomp.portalapp.portal.transport;

import java.util.Date;
import java.util.SortedSet;

import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPRole;

public class CentralRole implements Comparable{
		public Long id;
		public Date created;
		public Date modified;
		public Long createdId;
		public Long modifiedId;
		public Long rowNum;
		
	    public String  name;
	    public boolean active;
	    public Integer priority;
	    
	    public SortedSet<CentralRoleFunction> roleFunctions = null;
	    
	    public SortedSet<CentralRole> childRoles = null;

	    public SortedSet<CentralRole> parentRoles = null;

		public CentralRole(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum,
				String name, boolean active, Integer priority, SortedSet<CentralRoleFunction> roleFunctions,
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
		
		public CentralRole(){
			
		}
		
		public CentralRole(Long id, String name){
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
		
		public int compareTo(Object obj){
	    	EPRole other = (EPRole)obj;

	    	String c1 = getName();
	    	String c2 = other.getName();

	    	return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	    }

}
