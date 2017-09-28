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
		return "WidgetCatalog [id=" + id + ", name=" + name + ", desc=" + desc + ", fileLocation=" + fileLocation
				+ ", allowAllUser=" + allowAllUser + ", serviceId=" + serviceId + ", sortOrder=" + sortOrder
				+ ", statusCode=" + statusCode + ", widgetRoles=" + widgetRoles + "]";
	}
}
