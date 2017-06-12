package org.openecomp.portalapp.widget.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="EP_WIDGET_CATALOG")
public class WidgetCatalog{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "widget_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name = "wdg_name")
	private String name;
	
	@Column(name = "wdg_desc")
	private String desc;	
	
	@Column(name = "wdg_file_loc")
	private String fileLocation;
	
	@Column(name = "all_user_flag")
	private String allowAllUser;
	
	@Column(name = "service_id")
	private Long serviceId;
	
	@Transient
	private String sortOrder;

	@Transient
	private String statusCode;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "ep_widget_catalog_role", 
			joinColumns = {@JoinColumn(name = "WIDGET_ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
	)
	private Set<RoleApp> widgetRoles;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Set<RoleApp> getWidgetRoles() {
		return widgetRoles;
	}

	public void setWidgetRoles(Set<RoleApp> widgetRoles) {
		this.widgetRoles = widgetRoles;
	}
	
	public String getAllowAllUser() {
		return allowAllUser;
	}

	public void setAllowAllUser(String allowAllUser) {
		this.allowAllUser = allowAllUser;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "WidgetCatalog [name=" + name + ", desc=" + desc + ", allowAllUser=" + allowAllUser + "]";
	}
	
}
