/*-
 * ================================================================================
 * eCOMP Portal
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
package org.openecomp.portalapp.portal.ecomp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Carries row information for the functional table on the Contact Us page.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppCategoryFunctionsItem extends DomainVo {

	private static final long serialVersionUID = -1573834082471206458L;

	@Id
	private String rowId;
	private String appId;
	private String application;
	private String category;
	private String functions;

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String appName) {
		this.application = appName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}
}
