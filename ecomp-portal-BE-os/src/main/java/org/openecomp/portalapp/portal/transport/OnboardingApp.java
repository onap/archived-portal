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
package org.openecomp.portalapp.portal.transport;

public class OnboardingApp {

	public Long id;

	public String name;

	public String imageUrl;
	
	public String imageLink;

	public String description;

	public String notes;

	public String url;

	public String alternateUrl;

	public String restUrl;

	public Boolean isOpen;

	public Boolean isEnabled;

	public String username;

	public String appPassword;

	public String thumbnail;
	
	public String uebTopicName;
	
	public String uebKey;
	
	public String uebSecret;
	
	public Boolean restrictedApp;
	
	public Boolean isCentralAuth;
	
	public String nameSpace;

	public void normalize() {
		this.name = (this.name == null) ? "" : this.name.trim();
		this.username = (this.username == null) ? "" : this.username.trim();
		this.appPassword = (this.appPassword == null) ? "" : this.appPassword.trim();
	}

	public void setUebTopicName(String topicName) {
		this.uebTopicName = topicName;
	}

	public void setUebKey(String key) {
		this.uebKey = key;
	}
	
	public void setUebSecret(String secret) {
		this.uebSecret = secret;
	}
	
	// Hide the implementation of restricted and normal app from the front end.
	// The json sent and received will include restrictedApp but not appType.
	
	public void setRestrictedApp(Boolean restrictedApp) {
		this.restrictedApp = restrictedApp;
	}
}

