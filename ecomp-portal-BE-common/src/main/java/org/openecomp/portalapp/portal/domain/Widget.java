/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.domain;

import org.apache.commons.lang.StringUtils;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class Widget extends DomainVo {

	private static final long serialVersionUID = 1L;

	private String name;

	private Integer width;

	private Integer height;

	private String url;

	private Long appId;

	public Widget() {
		// Attention!!!
		// We set here all default values. We also place protection
		// into setters for fields with default values.
		// If we don't use such protection we are able to place null
		// to these fields and save such fields into DB even if DB has
		// default values for these fields.
		this.name = "";
		this.width = new Integer(0);
		this.height = new Integer(0);
		this.url = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			name = "";
		}
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		if (width == null) {
			width = new Integer(0);
		}
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		if (height == null) {
			height = new Integer(0);
		}
		this.height = height;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			url = "";
		}
		this.url = url;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

}
