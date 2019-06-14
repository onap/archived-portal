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

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.SafeHtml;

/***
 * 
 * This class is almost identical to org.onap.portalapp.portal.transport.FunctionalMenuItem
 * The only difference is the appId field. In FunctionMenuItem, we used @Transient as an attribute, and we are using @Column in this class.
 * In some sql queries, we need to have appId, but it's not a field in fn_menu_functional table.
 * 
 * @author robertlo
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FunctionalMenuItemWithAppID{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	@Digits(integer = 11, fraction = 0)
	public Long menuId;
	
	@Column(name = "COLUMN_NUM")
	@Digits(integer = 2, fraction = 0)
	@NotNull
	public Integer column;
	
	@Column(name = "TEXT")
	@Max(value = 100)
	@SafeHtml
	@NotNull
	public String text;
	
	@Column(name = "PARENT_MENU_ID")
	@Digits(integer = 11, fraction = 0)
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

	@Column(name="APP_ID")
	public Integer appid;
	
	@Transient
	public List<Integer> roles;
	
	@Transient
	public Boolean restrictedApp;
	
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

