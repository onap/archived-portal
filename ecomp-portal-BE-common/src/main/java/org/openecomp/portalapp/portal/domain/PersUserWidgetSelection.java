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

/**
 * Models a row in the table with personalization of user widget selections.
 */
public class PersUserWidgetSelection extends DomainVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6547880514779039200L;

	private Long userId;

	private Long widgetId;

	private String statusCode;

	public PersUserWidgetSelection() {}
	
	/**
	 * Convenience constructor
	 * 
	 * @param id
	 * @param userId
	 * @param widgetId
	 * @param statusCode
	 */
	public PersUserWidgetSelection(final Long id, final Long userId, final Long widgetId, final String statusCode) {
		super.id = id;
		this.userId = userId;
		this.widgetId = widgetId;
		this.statusCode = statusCode;
	}
	
	public Long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
