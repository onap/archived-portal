/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.domain.dto.ecomp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.onap.portal.domain.db.DomainVo;

@Getter
@Setter
@AllArgsConstructor
public class Widget extends DomainVo {

	private static final long serialVersionUID = 1L;

	private String name;
	private Integer width;
	private Integer height;
	private String url;
	private Long appId;

	public Widget() {
		this.name = "";
		this.width = 0;
		this.height = 0;
		this.url = "";
	}

	public Widget(Long appId, String name, String url) {
		this.name = name;
		this.url = url;
		this.appId = appId;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			name = "";
		}
		this.name = name;
	}

	public void setWidth(Integer width) {
		if (width == null) {
			width = 0;
		}
		this.width = width;
	}

	public void setHeight(Integer height) {
		if (height == null) {
			height = 0;
		}
		this.height = height;
	}

	public void setUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			url = "";
		}
		this.url = url;
	}

}
