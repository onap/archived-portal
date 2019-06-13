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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name="fn_menu_functional")
@NoArgsConstructor
@AllArgsConstructor
public class FunctionalMenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	public Long menuId;
	
	@Column(name = "COLUMN_NUM")
	@Max(value = 99)
	@NotNull
	public Integer column;
	
	@Column(name = "TEXT")
	@Max(value = 100)
	@SafeHtml
	@NotNull
	public String text;
	
	@Column(name = "PARENT_MENU_ID")
	public Integer parentMenuId;
	
	@Column(name = "URL")
	@Max(value = 128)
	@SafeHtml
	@NotNull
	public String url;
	
	@Column(name="ACTIVE_YN")
	@Max(value = 1)
	@SafeHtml
	@NotNull
	public String active_yn;

	@Transient
	public Integer appid;
	
	@Transient
	private List<Integer> roles;

	@Transient
	public Boolean restrictedApp;

	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	public void normalize() {
		if (this.column == null)
			this.column = 1;
		this.text = (this.text == null) ? "" : this.text.trim();
		if (this.parentMenuId == null)
			this.parentMenuId = -1;
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
