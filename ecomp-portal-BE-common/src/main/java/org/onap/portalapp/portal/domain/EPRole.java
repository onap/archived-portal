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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.Valid;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.domain.support.DomainVo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class EPRole extends DomainVo {

	private static final long serialVersionUID = 1L;
	@SafeHtml
	private String  name;
    private boolean active;
    private Integer priority;
    
    // ONAP will identify the specific remote application role id by appID;appRoleId among all the application roles it persists.
    private Long appId;     // used by ONAP only 
    private Long appRoleId; // used by ONAP only

    private SortedSet<RoleFunction>     roleFunctions = new TreeSet<RoleFunction>();
    @Valid
    private SortedSet<EPRole> childRoles = new TreeSet<EPRole>();
    
    @JsonIgnore
    private SortedSet<EPRole> parentRoles = new TreeSet<EPRole>();

    public EPRole() {}

    public String getName() {
        return name;
    }

    public boolean getActive() {
        return active;
    }

    public SortedSet<RoleFunction> getRoleFunctions() {
        return roleFunctions;
    }

    public Integer getPriority() {
        return priority;
    }

    public SortedSet<EPRole> getChildRoles() {
        return childRoles;
    }

    public SortedSet<EPRole> getParentRoles() {
        return parentRoles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRoleFunctions(SortedSet<RoleFunction> roleFunctions) {
        this.roleFunctions = roleFunctions;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setChildRoles(SortedSet<EPRole> childRoles) {
        this.childRoles = childRoles;
    }

    public void setParentRoles(SortedSet<EPRole> parentRoles) {
        this.parentRoles = parentRoles;
    }

    public void addRoleFunction(RoleFunction roleFunction) {
        this.roleFunctions.add(roleFunction);
    }

    public void addChildRole(EPRole role) {
        this.childRoles.add(role);
    }

    public void addParentRole(EPRole role) {
        this.parentRoles.add(role);
    }

    public String getEditUrl() {
        return "/role.htm?role_id=" + getId();    	
    }
    
	public String getToggleActiveImage() {
		return "/static/fusion/images/" + (getActive() ? "active.png" : "inactive.png" );
	}

	public String getToggleActiveAltText() {
		return getActive() ? "Click to Deactivate Role" : "Click to Activate Role";
	}
    
    public void removeChildRole(Long roleId) {
      Iterator<EPRole> i = this.childRoles.iterator();

      while (i.hasNext()) {
        EPRole childRole = (EPRole)i.next();
        if (childRole.getId().equals(roleId)) {
          this.childRoles.remove(childRole);
          break;
        }
      }
    }

    public void removeParentRole(Long roleId) {
        Iterator<EPRole> i = this.parentRoles.iterator();

        while (i.hasNext()) {
          EPRole parentRole = (EPRole)i.next();
          if (parentRole.getId().equals(roleId)) {
            this.parentRoles.remove(parentRole);
            break;
          }
        }
      }

    public void removeRoleFunction(String roleFunctionCd) {
      Iterator<RoleFunction> i = this.roleFunctions.iterator();

      while (i.hasNext()) {
        RoleFunction roleFunction = (RoleFunction)i.next();
        if (roleFunction.getCode().equals(roleFunctionCd)) {
          this.roleFunctions.remove(roleFunction);
          break;
        }
      }
    }

    public int compareTo(Object obj){
    	EPRole other = (EPRole)obj;

    	if(this.appId == null)
    		if(other.getAppId() == null)
    			return compareByName(other); //equal
    		else
    			return -1; 
    	else // this.appId != null
    		if(other.getAppId() == null)
    			return 1;  // appId != null, but others is null
    		else{
    			int appIdCompareResult = appId.compareTo(other.getAppId());
    			return appIdCompareResult == 0? compareByName(other) : appIdCompareResult;
    		}
    }

	private int compareByName(EPRole other) {
		String c1 = getName();
    	String c2 = other.getName();

    	return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getAppRoleId() {
		return appRoleId;
	}

	public void setAppRoleId(Long appRoleId) {
		this.appRoleId = appRoleId;
	}
	
	@Override
	public String toString() {
		return "[Id = " + id + ", name = " + name + "]";
	}

}
