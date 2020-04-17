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

public class CentralRole implements Serializable {

	private static final long serialVersionUID = -9210905386086213882L;
	private Long id;
	private Date created;
	private Date modified;
	private Long createdId;
	private Long modifiedId;
	private Long rowNum;

	private String name;
	private boolean active;
	private Integer priority;

	private SortedSet<CentralRoleFunction> roleFunctions = new TreeSet<>();

	private SortedSet<CentralRole> childRoles = new TreeSet<>();

	private SortedSet<CentralRole> parentRoles = new TreeSet<>();

	public CentralRole(){
		
	}
	
	public CentralRole(Long id, String name, boolean active, Integer priority,
			SortedSet<CentralRoleFunction> roleFunctions) {
		super();
		this.id = id;
		this.name = name;
		this.active = active;
		this.priority = priority;
		this.roleFunctions = roleFunctions;
	}
	
	public static class CentralRoleBuilder {
	    private Long id;
	    private String name;
	    private boolean active;
	    private Integer priority;
	    private SortedSet<CentralRoleFunction> roleFunctions;
	    private Date created;
	    private Date modified;
	    private Long createdId;
	    private Long modifiedId;
	    private Long rowNum;
	    private SortedSet<CentralRole> childRoles;
	    private SortedSet<CentralRole> parentRoles;

	    public CentralRoleBuilder setId(Long id) {
	        this.id = id;
	        return this;
	    }

	    public CentralRoleBuilder setName(String name) {
	        this.name = name;
	        return this;
	    }

	    public CentralRoleBuilder setActive(boolean active) {
	        this.active = active;
	        return this;
	    }

	    public CentralRoleBuilder setPriority(Integer priority) {
	        this.priority = priority;
	        return this;
	    }

	    public CentralRoleBuilder setRoleFunctions(SortedSet<CentralRoleFunction> roleFunctions) {
	        this.roleFunctions = roleFunctions;
	        return this;
	    }

	    public CentralRoleBuilder setCreated(Date created) {
	        this.created = created;
	        return this;
	    }

	    public CentralRoleBuilder setModified(Date modified) {
	        this.modified = modified;
	        return this;
	    }

	    public CentralRoleBuilder setCreatedId(Long createdId) {
	        this.createdId = createdId;
	        return this;
	    }

	    public CentralRoleBuilder setModifiedId(Long modifiedId) {
	        this.modifiedId = modifiedId;
	        return this;
	    }

	    public CentralRoleBuilder setRowNum(Long rowNum) {
	        this.rowNum = rowNum;
	        return this;
	    }

	    public CentralRoleBuilder setChildRoles(SortedSet<CentralRole> childRoles) {
	        this.childRoles = childRoles;
	        return this;
	    }

	    public CentralRoleBuilder setParentRoles(SortedSet<CentralRole> parentRoles) {
	        this.parentRoles = parentRoles;
	        return this;
	    }

	    public CentralRole createCentralRole() {
	        return new CentralRole(this);
	    }
	}

	public CentralRole(CentralRoleBuilder centralRoleBuilder) {
		super();
		this.id = centralRoleBuilder.id;
		this.created = centralRoleBuilder.created;
		this.modified = centralRoleBuilder.modified;
		this.createdId = centralRoleBuilder.createdId;
		this.modifiedId = centralRoleBuilder.modifiedId;
		this.rowNum = centralRoleBuilder.rowNum;
		this.name = centralRoleBuilder.name;
		this.active = centralRoleBuilder.active;
		this.priority = centralRoleBuilder.priority;
		this.roleFunctions = centralRoleBuilder.roleFunctions;
		this.childRoles = centralRoleBuilder.childRoles;
		this.parentRoles = centralRoleBuilder.parentRoles;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the createdId
	 */
	public Long getCreatedId() {
		return createdId;
	}

	/**
	 * @param createdId
	 *            the createdId to set
	 */
	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	/**
	 * @return the modifiedId
	 */
	public Long getModifiedId() {
		return modifiedId;
	}

	/**
	 * @param modifiedId
	 *            the modifiedId to set
	 */
	public void setModifiedId(Long modifiedId) {
		this.modifiedId = modifiedId;
	}

	/**
	 * @return the rowNum
	 */
	public Long getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum
	 *            the rowNum to set
	 */
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the roleFunctions
	 */
	public SortedSet<CentralRoleFunction> getRoleFunctions() {
		return roleFunctions;
	}

	/**
	 * @param roleFunctions
	 *            the roleFunctions to set
	 */
	public void setRoleFunctions(SortedSet<CentralRoleFunction> roleFunctions) {
		this.roleFunctions = roleFunctions;
	}

	/**
	 * @return the childRoles
	 */
	public SortedSet<CentralRole> getChildRoles() {
		return childRoles;
	}

	/**
	 * @param childRoles
	 *            the childRoles to set
	 */
	public void setChildRoles(SortedSet<CentralRole> childRoles) {
		this.childRoles = childRoles;
	}

	/**
	 * @return the parentRoles
	 */
	public SortedSet<CentralRole> getParentRoles() {
		return parentRoles;
	}

	/**
	 * @param parentRoles
	 *            the parentRoles to set
	 */
	public void setParentRoles(SortedSet<CentralRole> parentRoles) {
		this.parentRoles = parentRoles;
	}

}
