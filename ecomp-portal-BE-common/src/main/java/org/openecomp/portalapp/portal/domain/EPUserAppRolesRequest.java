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


import java.util.Date;
import java.util.Set;

import org.openecomp.portalsdk.core.domain.support.DomainVo;


public class EPUserAppRolesRequest extends DomainVo {
	
	private static final long serialVersionUID = -7225288307806389019L;
	private Long userId;
	private Long appId;
	private Date createdDate;
	private Date updatedDate;
	private String requestStatus;
	
	private Set<EPUserAppRolesRequestDetail> epRequestIdDetail;

	public final Long getUserId() {
		return userId;
	}

	public final void setUserId(Long userId) {
		this.userId = userId;
	}

	public final Long getAppId() {
		return appId;
	}

	public final void setAppId(Long appId) {
		this.appId = appId;
	}

	public final Date getCreatedDate() {
		return createdDate;
	}

	public final void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public final Date getUpdatedDate() {
		return updatedDate;
	}

	public final void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public final String getRequestStatus() {
		return requestStatus;
	}

	public final void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public final Set<EPUserAppRolesRequestDetail> getEpRequestIdDetail() {
		return epRequestIdDetail;
	}

	public final void setEpRequestIdDetail(Set<EPUserAppRolesRequestDetail> epMyLoginsDetail) {
		this.epRequestIdDetail = epMyLoginsDetail;
	}
}
