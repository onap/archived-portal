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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.domain;

import org.onap.portalsdk.core.domain.support.DomainVo;

public class EPUserAppRolesRequestDetail extends DomainVo {

	private static final long serialVersionUID = -4908856671135348157L;

	private Long reqRoleId;
	private String reqType;
	private EPUserAppRolesRequest epRequestIdData;

	public EPUserAppRolesRequest getEpRequestIdData() {
		return epRequestIdData;
	}

	public void setEpRequestIdData(EPUserAppRolesRequest epRequestIdData) {
		this.epRequestIdData = epRequestIdData;
	}

	public Long getReqRoleId() {
		return reqRoleId;
	}

	public void setReqRoleId(Long reqRoleId) {
		this.reqRoleId = reqRoleId;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((epRequestIdData == null) ? 0 : epRequestIdData.hashCode());
		result = prime * result + ((reqRoleId == null) ? 0 : reqRoleId.hashCode());
		result = prime * result + ((reqType == null) ? 0 : reqType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EPUserAppRolesRequestDetail other = (EPUserAppRolesRequestDetail) obj;
		if (epRequestIdData == null) {
			if (other.epRequestIdData != null)
				return false;
		} else if (!epRequestIdData.equals(other.epRequestIdData))
			return false;
		if (reqRoleId == null) {
			if (other.reqRoleId != null)
				return false;
		} else if (!reqRoleId.equals(other.reqRoleId))
			return false;
		if (reqType == null) {
			if (other.reqType != null)
				return false;
		} else if (!reqType.equals(other.reqType))
			return false;
		return true;
	}

}
