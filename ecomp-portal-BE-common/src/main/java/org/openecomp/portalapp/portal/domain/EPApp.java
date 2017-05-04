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

import javax.persistence.Lob;

import org.apache.commons.lang.StringUtils;
import org.openecomp.portalsdk.core.domain.support.DomainVo;

/**
 * Model for all columns in the fn_app table.
 */
public class EPApp extends DomainVo {

	private static final long serialVersionUID = 1L;

	private String name;
	private String imageUrl;
	private String description;
	private String notes;
	private String url;
	private String alternateUrl;
	private String appRestEndpoint;
	private String mlAppName;
	private String mlAppAdminId;
	private Long motsId;
	private String username;
	private String appPassword;
	@Lob
	private byte[] thumbnail;
	private Boolean open;
	private Boolean enabled;
	private String uebTopicName;
	private String uebKey;
	private String uebSecret;
	private Integer appType;

	private AppContactUs contactUs;

	public EPApp() {
		// Attention!!!
		// We set here all default values. We also place protection
		// into setters for fields with default values.
		// If we don't use such protection we are able to place null
		// to these fields and save such fields into DB even if DB has
		// default values for these fields.
		this.name = "";
		this.mlAppName = "";
		this.mlAppAdminId = "";
		this.username = "";
		this.appPassword = "";
		this.open = new Boolean(false);
		this.enabled = new Boolean(true);
		this.uebTopicName = "";
		this.uebKey = "";
		this.uebSecret = "";
		this.appType = 1;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public byte[] getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlternateUrl() {
		return alternateUrl;
	}

	public void setAlternateUrl(String alternateUrl) {
		this.alternateUrl = alternateUrl;
	}

	public String getAppRestEndpoint() {
		return appRestEndpoint;
	}

	public void setAppRestEndpoint(String appRestEndpoint) {
		this.appRestEndpoint = appRestEndpoint;
	}

	public String getMlAppName() {
		return mlAppName;
	}

	public void setMlAppName(String mlAppName) {
		if (StringUtils.isEmpty(mlAppName)) {
			mlAppName = "";
		}
		this.mlAppName = mlAppName;
	}

	public String getMlAppAdminId() {
		return mlAppAdminId;
	}

	public void setMlAppAdminId(String mlAppAdminId) {
		if (StringUtils.isEmpty(mlAppAdminId)) {
			mlAppAdminId = "";
		}
		this.mlAppAdminId = mlAppAdminId;
	}

	public Long getMotsId() {
		return motsId;
	}

	public void setMotsId(Long motsId) {
		this.motsId = motsId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAppPassword() {
		return appPassword;
	}

	public void setAppPassword(String appPassword) {
		if (StringUtils.isEmpty(appPassword)) {
			appPassword = "";
		}
		this.appPassword = appPassword;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		if (open == null) {
			open = new Boolean(false);
		}
		this.open = open;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		if (enabled == null) {
			enabled = new Boolean(true);
		}
		this.enabled = enabled;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		if (appType == null) {
			appType = new Integer(1);
		}
		this.appType = appType;
	}

	public void setRestrictedApp(Boolean restrictedApp) {
		Integer result = 1;
		if (restrictedApp) {
			result = 2;
		}
		this.appType = result;
	}

	public Boolean isRestrictedApp() {
		return (this.appType == 2 ? true : false);
	}

	public int compareTo(Object obj) {
		Long c1 = getId();
		Long c2 = ((EPApp) obj).getId();

		return c1.compareTo(c2);
	}

	public String getUebTopicName() {
		return this.uebTopicName;
	}

	public void setUebTopicName(String topicName) {
		if (StringUtils.isEmpty(topicName)) {
			this.uebTopicName = "";
		}
		this.uebTopicName = topicName;
	}

	public String getUebKey() {
		return this.uebKey;
	}

	public void setUebKey(String uebKey) {
		if (StringUtils.isEmpty(uebKey)) {
			this.uebKey = "";
		}
		this.uebKey = uebKey;
	}

	public String getUebSecret() {
		return this.uebSecret;
	}

	public void setUebSecret(String uebSecret) {
		if (StringUtils.isEmpty(uebSecret)) {
			this.uebSecret = "";
		}
		this.uebSecret = uebSecret;
	}

	public AppContactUs getContactUs() {
		return contactUs;
	}

	public void setContactUs(AppContactUs contactUs) {
		this.contactUs = contactUs;
	}

	@Override
	public String toString() {
		String str = "[" + getId() + ":" + getName() + "]";
		return str;
	}

}
