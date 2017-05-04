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

public class EPUserAppRolesRequestDetail extends DomainVo{

	private static final long serialVersionUID = -4908856671135348157L;
	
	private Long reqRoleId;
	private String reqType;
	private EPUserAppRolesRequest epRequestIdData;
	
	public final EPUserAppRolesRequest getEpRequestIdData() {
		return epRequestIdData;
	}
	public final void setEpRequestIdData(EPUserAppRolesRequest epRequestIdData) {
		this.epRequestIdData = epRequestIdData;
	}
	public final Long getReqRoleId() {
		return reqRoleId;
	}
	public final void setReqRoleId(Long reqRoleId) {
		this.reqRoleId = reqRoleId;
	}
	public final String getReqType() {
		return reqType;
	}
	public final void setReqType(String reqType) {
		this.reqType = reqType;
	}

}

