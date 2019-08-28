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

package org.onap.portal.domain.dto.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingApp {

	private Long id;
	@SafeHtml
	private String name;
	@SafeHtml
	private String imageUrl;
	@SafeHtml
	private String imageLink;
	@SafeHtml
	private String description;
	@SafeHtml
	private String notes;
	@SafeHtml
	private String url;
	@SafeHtml
	private String alternateUrl;
	@SafeHtml
	private String restUrl;
	private Boolean isOpen;
	private Boolean isEnabled;
	private Long motsId;
	@SafeHtml
	private String myLoginsAppName;
	@SafeHtml
	private String myLoginsAppOwner;
	@SafeHtml
	private String username;
	@SafeHtml
	private String appPassword;
	@SafeHtml
	private String thumbnail;
	@SafeHtml
	private String uebTopicName;
	@SafeHtml
	private String uebKey;
	@SafeHtml
	private String uebSecret;
	private Boolean restrictedApp;
	private Boolean isCentralAuth;
	@SafeHtml
	private String nameSpace;

	public void normalize() {
		this.name = (this.name == null) ? "" : this.name.trim();
		this.myLoginsAppName = (this.myLoginsAppName == null) ? "" : this.myLoginsAppName.trim();
		this.myLoginsAppOwner = (this.myLoginsAppOwner == null) ? "" : this.myLoginsAppOwner.trim();
		this.username = (this.username == null) ? "" : this.username.trim();
		this.appPassword = (this.appPassword == null) ? "" : this.appPassword.trim();
	}

}
