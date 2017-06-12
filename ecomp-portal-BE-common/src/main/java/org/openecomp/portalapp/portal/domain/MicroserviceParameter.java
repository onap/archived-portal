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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class MicroserviceParameter extends DomainVo {

	private static final long serialVersionUID = 1L;

	public MicroserviceParameter() {

	}

	private Long id;

	private long serviceId;

	private String para_key;

	private String para_value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public String getPara_key() {
		return para_key;
	}

	public void setPara_key(String para_key) {
		this.para_key = para_key;
	}

	public String getPara_value() {
		return para_value;
	}

	public void setPara_value(String para_value) {
		this.para_value = para_value;
	}

	@Override
	public String toString() {
		return "MicroserviceParameter [id=" + id + ", serviceId=" + serviceId + ", para_key=" + para_key
				+ ", para_value=" + para_value + "]";
	}

}
