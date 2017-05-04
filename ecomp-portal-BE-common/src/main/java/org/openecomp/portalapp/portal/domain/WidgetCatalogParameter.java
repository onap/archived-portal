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

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class WidgetCatalogParameter extends DomainVo{
	
	private static final long serialVersionUID = 1L;

	public WidgetCatalogParameter() {

	}
	private Long id;

	private Long widgetId;

	private Long userId;

	private Long paramId;

	private String user_value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}

	public String getUser_value() {
		return user_value;
	}

	public void setUser_value(String user_value) {
		this.user_value = user_value;
	}

	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "WidgetCatalogParameter [id=" + id + ", widgetId=" + widgetId + ", userId=" + userId + ", paramId="
				+ paramId + ", user_value=" + user_value + "]";
	}

	

	
}
