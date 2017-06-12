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
package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="fn_menu_functional")
public class FunctionalMenuItem implements Serializable {
	public FunctionalMenuItem(){};
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	public Long menuId;
	
	@Column(name = "COLUMN_NUM")
	public Integer column;
	
	@Column(name = "TEXT")
	public String text;
	
	@Column(name = "PARENT_MENU_ID")
	public Integer parentMenuId;
	
	@Column(name = "URL")
	public String url;
	
	@Column(name="ACTIVE_YN")
	public String active_yn;

	@Transient
	public Integer appid;
	
	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	@Transient
	private List<Integer> roles;
	
	@Transient
	public Boolean restrictedApp;
	
	public void normalize() {
		if (this.column == null)
			this.column = new Integer(1);
		this.text = (this.text == null) ? "" : this.text.trim();
		if (this.parentMenuId == null)
			this.parentMenuId = new Integer(-1);
		this.url = (this.url == null) ? "" : this.url.trim();
	}

	@Override
	public String toString() {
		return "FunctionalMenuItem [menuId=" + menuId + ", column=" + column + ", text=" + text + ", parentMenuId="
				+ parentMenuId + ", url=" + url + ", active_yn=" + active_yn + ", appid=" + appid + ", roles=" + roles
				+ ", restrictedApp=" + restrictedApp + "]";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRestrictedApp(Boolean restrictedApp) {
		this.restrictedApp = restrictedApp;
	}
}
