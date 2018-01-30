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
package org.onap.portalapp.portal.transport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OnboardingWidget implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WIDGET_ID")
	public Long id;

	@Column(name = "WDG_NAME")
	public String name;

	@Column(name = "APP_ID")
	public Long appId;

	@Column(name = "APP_NAME")
	public String appName;

	@Column(name = "WDG_WIDTH")
	public Integer width;

	@Column(name = "WDG_HEIGHT")
	public Integer height;

	@Column(name = "WDG_URL")
	public String url;

	public void normalize() {
		this.name = (this.name == null) ? "" : this.name.trim();
		this.appName = (this.appName == null) ? "" : this.appName.trim();
		if (this.width == null)
			this.width = new Integer(0);
		if (this.height == null)
			this.height = new Integer(0);
		this.url = (this.url == null) ? "" : this.url.trim();
	}

}
