package org.onap.portalapp.widget.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FN_ROLE")
public class RoleApp implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long roleId;
	
	
	@Column(name = "ROLE_Name")
	private String roleName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="APP_ID")
	private App app;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy="widgetRoles")
	private transient  Set<WidgetCatalog> widgets;

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
		return "RoleApp [roleId=" + roleId + ", roleName=" + roleName + ", app=" + app + "]";
	}
	
}
