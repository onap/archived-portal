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
package org.onap.portalapp.portal.transport;

import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalapp.portal.domain.EpAppType;

/**
 * Model of rows in the fn_app table; serialized as a message add or update an
 * on-boarded application.
 */
public class OnboardingApp {

	private Long id;
	@SafeHtml
	private String appName;
	@SafeHtml
	private String imageUrl;
	@SafeHtml
	private String imageLink;
	@SafeHtml
	private String appDescription;
	@SafeHtml
	private String appNotes;
	@SafeHtml
	private String landingPage;
	@SafeHtml
	private String alternateLandingPage;
	@SafeHtml
	private String restUrl;

	@SafeHtml
	private String applicationType;
	
	private Boolean isOpen;

	private Boolean isEnabled;

	private Long motsId;
	@SafeHtml
	private String myLoginsAppName;
	@SafeHtml
	private String myLoginsAppOwner;
	@SafeHtml
	private String appBasicAuthUsername;
	@SafeHtml
	private String appBasicAuthPassword;
	@SafeHtml
	private String thumbnail;
	@SafeHtml
	private String uebTopicName;
	@SafeHtml
	private String uebKey;
	@SafeHtml
	private String uebSecret;

	private Boolean restrictedApp;
	
	private Boolean rolesInAAF;
	@SafeHtml
	private String nameSpace;

	@SafeHtml
	private String modeOfIntegration;

	private Boolean appAck;

	private Boolean usesCadi;
	
	/**
	 * Sets the name, myLoginsAppName, myLoginsAppOwner, username and
	 * appPassword fields to the empty string OR trims leading/trailing space,
	 * as appropriate.
	 */
	public void normalize() {
		this.appName = (this.appName == null) ? "" : this.appName.trim();
		this.myLoginsAppName = (this.myLoginsAppName == null) ? "" : this.myLoginsAppName.trim();
		this.myLoginsAppOwner = (this.myLoginsAppOwner == null) ? "" : this.myLoginsAppOwner.trim();
		this.appBasicAuthUsername = (this.appBasicAuthUsername == null) ? "" : this.appBasicAuthUsername.trim();
		this.appBasicAuthPassword = (this.appBasicAuthPassword == null) ? "" : this.appBasicAuthPassword.trim();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
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

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getMotsId() {
		return motsId;
	}

	public void setMotsId(Long motsId) {
		this.motsId = motsId;
	}

	public String getMyLoginsAppName() {
		return myLoginsAppName;
	}

	public void setMyLoginsAppName(String myLoginsAppName) {
		this.myLoginsAppName = myLoginsAppName;
	}

	public String getMyLoginsAppOwner() {
		return myLoginsAppOwner;
	}

	public void setMyLoginsAppOwner(String myLoginsAppOwner) {
		this.myLoginsAppOwner = myLoginsAppOwner;
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
		this.appBasicAuthPassword = appBasicAuthPassword;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUebTopicName() {
		return uebTopicName;
	}

	public void setUebTopicName(String uebTopicName) {
		this.uebTopicName = uebTopicName;
	}

	public String getUebKey() {
		return uebKey;
	}

	public void setUebKey(String uebKey) {
		this.uebKey = uebKey;
	}

	public String getUebSecret() {
		return uebSecret;
	}

	public void setUebSecret(String uebSecret) {
		this.uebSecret = uebSecret;
	}

	public Boolean getRestrictedApp() {
		return restrictedApp;
	}

	public void setRestrictedApp(Boolean restrictedApp) {
		this.restrictedApp = restrictedApp;
	}

	public Boolean getRolesInAAF() {
		return rolesInAAF;
	}

	public void setRolesInAAF(Boolean rolesInAAF) {
		this.rolesInAAF = rolesInAAF;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
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

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	
	public Integer appTypePersistedValue() {
		switch (this.applicationType) {
		case EpAppType.GUI_STR:
			return EpAppType.GUI;
		case EpAppType.HYPERLINK_STR:
			return EpAppType.HYPERLINK;
		case EpAppType.NONGUI_STR:
			return EpAppType.NONGUI;
		default:
			return 0;
		}
	}
}
