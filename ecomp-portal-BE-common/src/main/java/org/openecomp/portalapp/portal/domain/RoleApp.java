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

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity
//@Table(name = "FN_ROLE")
public class RoleApp implements Serializable{
	private static final long serialVersionUID = 1L;

	//@Id
	//@Column(name = "ROLE_ID")
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private Long roleId;
	
	
	//@Column(name = "ROLE_Name")
	private String roleName;
	
	//@ManyToOne(fetch = FetchType.EAGER)
	//@JoinColumn(name="APP_ID")
	private App app;
	
	//@JsonIgnore
	//@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy="widgetRoles")
	private Set<WidgetCatalog> widgets;

	/*@PreRemove
	private void removeGroupsFromUsers() {
	    for (WidgetCatalog w : widgets) {
	        w.getWidgetRoles().remove(this);
	    }
	}*/
	
	/*@ManyToOne
	@JoinColumn(name = "WIDGET_ID", nullable = false)
	WidgetCatalog widgetCatalog;*/

	//@JsonIgnore
	//@ManyToMany(mappedBy = "widgetRoles")
	//@ManyToMany(fetch = FetchType.EAGER, mappedBy = "widgetRoles")
	//private Set<WidgetCatalog> widgets  = new HashSet<WidgetCatalog>();
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
	
	

	public Set<WidgetCatalog> getWidgets() {
		return widgets;
	}

	public void setWidgets(Set<WidgetCatalog> widgets) {
		this.widgets = widgets;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", app=" + app + "]";
	}
	
}
