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
	private String appDescription;

	@SafeHtml
	private String appNotes;

	@SafeHtml
	private String landingPage;

	@SafeHtml
	private String alternateLandingPage;

	@SafeHtml
	private String appRestEndpoint;

	@SafeHtml
	private String mlAppName;

	@SafeHtml
	private String mlAppAdminId;
	private Long motsId;

	@SafeHtml
	private String appBasicAuthUsername;

	@SafeHtml
	private String appBasicAuthPassword;

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

	private Boolean rolesInAAF;
	@SafeHtml
	private String nameSpace;

	@SafeHtml
	private String modeOfIntegration;

	private Boolean appAck;

	private Boolean usesCadi;

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
		this.appBasicAuthUsername = "";
		this.appBasicAuthPassword = "";
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

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getAppNotes() {
		return appNotes;
	}

	public void setAppNotes(String appNotes) {
		this.appNotes = appNotes;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getAlternateLandingPage() {
		return alternateLandingPage;
	}

	public void setAlternateLandingPage(String alternateLandingPage) {
		this.alternateLandingPage = alternateLandingPage;
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

	public String getAppBasicAuthUsername() {
		return appBasicAuthUsername;
	}

	public void setAppBasicAuthUsername(String appBasicAuthUsername) {
		this.appBasicAuthUsername = appBasicAuthUsername;
	}

	public String getAppBasicAuthPassword() {
		return appBasicAuthPassword;
	}

	public void setAppBasicAuthPassword(String appBasicAuthPassword) {
		if (StringUtils.isEmpty(appBasicAuthPassword)) {
			appBasicAuthPassword = "";
		}
		this.appBasicAuthPassword = appBasicAuthPassword;
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

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
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

	public Boolean getRolesInAAF() {
		return rolesInAAF;
	}

	public void setRolesInAAF(Boolean rolesInAAF) {
		if (rolesInAAF == null) {
			rolesInAAF = new Boolean(false);
		}
		this.rolesInAAF = rolesInAAF;
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

	public String getModeOfIntegration() {
		return modeOfIntegration;
	}

	public void setModeOfIntegration(String modeOfIntegration) {
		this.modeOfIntegration = modeOfIntegration;
	}

	public Boolean getAppAck() {
		return appAck;
	}

	public void setAppAck(Boolean appAck) {
		this.appAck = appAck;
	}

	public Boolean getUsesCadi() {
		return usesCadi;
	}

	public void setUsesCadi(Boolean usesCadi) {
		this.usesCadi = usesCadi;
	}
	
	@Override
	public String toString() {
		return "EPApp [name=" + name + ", imageUrl=" + imageUrl + ", appDescription=" + appDescription + ", appNotes="
				+ appNotes + ", landingPage=" + landingPage + ", alternateLandingPage=" + alternateLandingPage
				+ ", appRestEndpoint=" + appRestEndpoint + ", mlAppName=" + mlAppName + ", mlAppAdminId=" + mlAppAdminId
				+ ", motsId=" + motsId + ", appBasicAuthUsername=" + appBasicAuthUsername + ", appBasicAuthPassword="
				+ appBasicAuthPassword + ", thumbnail=" + Arrays.toString(thumbnail) + ", open=" + open + ", enabled="
				+ enabled + ", uebTopicName=" + uebTopicName + ", uebKey=" + uebKey + ", uebSecret=" + uebSecret
				+ ", appType=" + appType + ", contactUs=" + contactUs + ", rolesInAAF=" + rolesInAAF + ", nameSpace="
				+ nameSpace + ", modeOfIntegration=" + modeOfIntegration + ", appAck=" + appAck + ", usesCadi="
				+ usesCadi + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternateLandingPage == null) ? 0 : alternateLandingPage.hashCode());
		result = prime * result + ((appAck == null) ? 0 : appAck.hashCode());
		result = prime * result + ((appBasicAuthPassword == null) ? 0 : appBasicAuthPassword.hashCode());
		result = prime * result + ((appBasicAuthUsername == null) ? 0 : appBasicAuthUsername.hashCode());
		result = prime * result + ((appDescription == null) ? 0 : appDescription.hashCode());
		result = prime * result + ((appNotes == null) ? 0 : appNotes.hashCode());
		result = prime * result + ((appRestEndpoint == null) ? 0 : appRestEndpoint.hashCode());
		result = prime * result + ((appType == null) ? 0 : appType.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((landingPage == null) ? 0 : landingPage.hashCode());
		result = prime * result + ((mlAppAdminId == null) ? 0 : mlAppAdminId.hashCode());
		result = prime * result + ((mlAppName == null) ? 0 : mlAppName.hashCode());
		result = prime * result + ((modeOfIntegration == null) ? 0 : modeOfIntegration.hashCode());
		result = prime * result + ((motsId == null) ? 0 : motsId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameSpace == null) ? 0 : nameSpace.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((rolesInAAF == null) ? 0 : rolesInAAF.hashCode());
		result = prime * result + Arrays.hashCode(thumbnail);
		result = prime * result + ((uebKey == null) ? 0 : uebKey.hashCode());
		result = prime * result + ((uebSecret == null) ? 0 : uebSecret.hashCode());
		result = prime * result + ((uebTopicName == null) ? 0 : uebTopicName.hashCode());
		result = prime * result + ((usesCadi == null) ? 0 : usesCadi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof EPApp))
			return false;
		EPApp other = (EPApp) obj;
		if (alternateLandingPage == null) {
			if (other.alternateLandingPage != null)
				return false;
		} else if (!alternateLandingPage.equals(other.alternateLandingPage))
			return false;
		if (appAck == null) {
			if (other.appAck != null)
				return false;
		} else if (!appAck.equals(other.appAck))
			return false;
		if (appBasicAuthPassword == null) {
			if (other.appBasicAuthPassword != null)
				return false;
		} else if (!appBasicAuthPassword.equals(other.appBasicAuthPassword))
			return false;
		if (appBasicAuthUsername == null) {
			if (other.appBasicAuthUsername != null)
				return false;
		} else if (!appBasicAuthUsername.equals(other.appBasicAuthUsername))
			return false;
		if (appDescription == null) {
			if (other.appDescription != null)
				return false;
		} else if (!appDescription.equals(other.appDescription))
			return false;
		if (appNotes == null) {
			if (other.appNotes != null)
				return false;
		} else if (!appNotes.equals(other.appNotes))
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
		if (landingPage == null) {
			if (other.landingPage != null)
				return false;
		} else if (!landingPage.equals(other.landingPage))
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
		if (modeOfIntegration == null) {
			if (other.modeOfIntegration != null)
				return false;
		} else if (!modeOfIntegration.equals(other.modeOfIntegration))
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
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (rolesInAAF == null) {
			if (other.rolesInAAF != null)
				return false;
		} else if (!rolesInAAF.equals(other.rolesInAAF))
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
		if (usesCadi == null) {
			if (other.usesCadi != null)
				return false;
		} else if (!usesCadi.equals(other.usesCadi))
			return false;
		return true;
	}

	
}
