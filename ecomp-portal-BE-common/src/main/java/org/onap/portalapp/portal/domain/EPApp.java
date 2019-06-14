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
 * 
 */
package org.onap.portalapp.portal.domain;

import java.util.Arrays;

import javax.persistence.Lob;

import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.support.DomainVo;

/**
 * Model for all columns in the fn_app table.
 */
public class EPApp extends DomainVo {

	private static final long serialVersionUID = 1L;
	@SafeHtml
	private String name;
	@SafeHtml
	private String imageUrl;
	@SafeHtml
	private String description;
	@SafeHtml
	private String notes;
	@SafeHtml
	private String url;
	@SafeHtml
	private String alternateUrl;
	@SafeHtml
	private String appRestEndpoint;
	@SafeHtml
	private String mlAppName;
	@SafeHtml
	private String mlAppAdminId;
	private Long motsId;
	@SafeHtml
	private String username;
	@SafeHtml
	private String appPassword;
	@Lob
	private byte[] thumbnail;
	private Boolean open;
	private Boolean enabled;
	@SafeHtml
	private String uebTopicName;
	@SafeHtml
	private String uebKey;
	@SafeHtml
	private String uebSecret;
	private Integer appType;
	@Valid
	private AppContactUs contactUs;
	private Boolean centralAuth;
	@SafeHtml
	private String	nameSpace;

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
	
	public Boolean getCentralAuth() {
		return centralAuth;
	}

	public void setCentralAuth(Boolean centralAuth) {
		if (centralAuth == null) {
			centralAuth = new Boolean(false);
		}
		this.centralAuth = centralAuth;
	}
	
	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		if (StringUtils.isEmpty(nameSpace)) {
			nameSpace = null;
		}
		this.nameSpace = nameSpace;
	}

	@Override
	public String toString() {
		String str = "[" + getId() + ":" + getName() + "]";
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternateUrl == null) ? 0 : alternateUrl.hashCode());
		result = prime * result + ((appPassword == null) ? 0 : appPassword.hashCode());
		result = prime * result + ((appRestEndpoint == null) ? 0 : appRestEndpoint.hashCode());
		result = prime * result + ((appType == null) ? 0 : appType.hashCode());
		result = prime * result + ((centralAuth == null) ? 0 : centralAuth.hashCode());
		result = prime * result + ((contactUs == null) ? 0 : contactUs.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((mlAppAdminId == null) ? 0 : mlAppAdminId.hashCode());
		result = prime * result + ((mlAppName == null) ? 0 : mlAppName.hashCode());
		result = prime * result + ((motsId == null) ? 0 : motsId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameSpace == null) ? 0 : nameSpace.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + Arrays.hashCode(thumbnail);
		result = prime * result + ((uebKey == null) ? 0 : uebKey.hashCode());
		result = prime * result + ((uebSecret == null) ? 0 : uebSecret.hashCode());
		result = prime * result + ((uebTopicName == null) ? 0 : uebTopicName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		EPApp other = (EPApp) obj;
		if (alternateUrl == null) {
			if (other.alternateUrl != null)
				return false;
		} else if (!alternateUrl.equals(other.alternateUrl))
			return false;
		if (appPassword == null) {
			if (other.appPassword != null)
				return false;
		} else if (!appPassword.equals(other.appPassword))
			return false;
		if (appRestEndpoint == null) {
			if (other.appRestEndpoint != null)
				return false;
		} else if (!appRestEndpoint.equals(other.appRestEndpoint))
			return false;
		if (appType == null) {
			if (other.appType != null)
				return false;
		} else if (!appType.equals(other.appType))
			return false;
		if (centralAuth == null) {
			if (other.centralAuth != null)
				return false;
		} else if (!centralAuth.equals(other.centralAuth))
			return false;
		if (contactUs == null) {
			if (other.contactUs != null)
				return false;
		} else if (!contactUs.equals(other.contactUs))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (mlAppAdminId == null) {
			if (other.mlAppAdminId != null)
				return false;
		} else if (!mlAppAdminId.equals(other.mlAppAdminId))
			return false;
		if (mlAppName == null) {
			if (other.mlAppName != null)
				return false;
		} else if (!mlAppName.equals(other.mlAppName))
			return false;
		if (motsId == null) {
			if (other.motsId != null)
				return false;
		} else if (!motsId.equals(other.motsId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameSpace == null) {
			if (other.nameSpace != null)
				return false;
		} else if (!nameSpace.equals(other.nameSpace))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (!Arrays.equals(thumbnail, other.thumbnail))
			return false;
		if (uebKey == null) {
			if (other.uebKey != null)
				return false;
		} else if (!uebKey.equals(other.uebKey))
			return false;
		if (uebSecret == null) {
			if (other.uebSecret != null)
				return false;
		} else if (!uebSecret.equals(other.uebSecret))
			return false;
		if (uebTopicName == null) {
			if (other.uebTopicName != null)
				return false;
		} else if (!uebTopicName.equals(other.uebTopicName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
