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

/**
 * Model for a subset of the columns in the fn_app table.
 */
public class EcompApp {
	
	protected Long id;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private String notes;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}	

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String alternateUrl;

	public String getAlternateUrl() {
		return alternateUrl;
	}

	public void setAlternateUrl(String alternateUrl) {
		this.alternateUrl = alternateUrl;
	}
	
	private String uebTopicName;
	
	public String getUebTopicName() {
		return uebTopicName;
	}
	
	public void setUebTopicName(String topicName) {
		this.uebTopicName = topicName;
	}
	
	private String uebKey;
	
	public String getUebKey() {
		return uebKey;
	}
	
	public void setUebKey(String uebKey) {
		this.uebKey = uebKey;
	}
	
	private String uebSecret;
	
	public String getUebSecret() {
		return uebSecret;
	}
	
	public void setUebSecret(String secret) {
		this.uebSecret = secret;
	}
	
	private Boolean enabled;
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enable) {
		this.enabled = enable;
	}

	private Boolean restrictedApp;
	
	public Boolean isRestrictedApp() {
		return restrictedApp;
	}
	
	public void setRestrictedApp(Boolean restrictedApp) {
		this.restrictedApp = restrictedApp;
	}
}
