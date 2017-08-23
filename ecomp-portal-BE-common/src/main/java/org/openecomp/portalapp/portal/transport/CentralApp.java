package org.openecomp.portalapp.portal.transport;

import java.util.Date;

public class CentralApp {
	public Long id;
	public Date created;
	public Date modified;
	public Long createdId;
	public Long modifiedId;
	public Long rowNum;
	public String name; // app_name
	public String imageUrl; // app_image_url
	public String description; // app_description
	public String notes; // app_notes
	public String url; // app_url
	public String alternateUrl; // app_alternate_url
	public String restEndpoint; // app_rest_endpoint
	public String mlAppName; // ml_app_name
	public String mlAppAdminId; // ml_app_admin_id;
	public String motsId; // mots_id
	public String appPassword; // app_password
	public String open;
	public String enabled;
	public byte[] thumbnail;
	public String username; // app_username
	public String uebKey; // ueb_key
	public String uebSecret; // ueb_secret
	public String uebTopicName; // ueb_topic_name
	
	
	public CentralApp(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, String name,
			String imageUrl, String description, String notes, String url, String alternateUrl, String restEndpoint,
			String mlAppName, String mlAppAdminId, String motsId, String appPassword, String open, String enabled,
			byte[] thumbnail, String username, String uebKey, String uebSecret, String uebTopicName) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.createdId = createdId;
		this.modifiedId = modifiedId;
		this.rowNum = rowNum;
		this.name = name;
		this.imageUrl = imageUrl;
		this.description = description;
		this.notes = notes;
		this.url = url;
		this.alternateUrl = alternateUrl;
		this.restEndpoint = restEndpoint;
		this.mlAppName = mlAppName;
		this.mlAppAdminId = mlAppAdminId;
		this.motsId = motsId;
		this.appPassword = appPassword;
		this.open = open;
		this.enabled = enabled;
		this.thumbnail = thumbnail;
		this.username = username;
		this.uebKey = uebKey;
		this.uebSecret = uebSecret;
		this.uebTopicName = uebTopicName;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Long getCreatedId() {
		return createdId;
	}
	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}
	public Long getModifiedId() {
		return modifiedId;
	}
	public void setModifiedId(Long modifiedId) {
		this.modifiedId = modifiedId;
	}
	public Long getRowNum() {
		return rowNum;
	}
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	public String getRestEndpoint() {
		return restEndpoint;
	}
	public void setRestEndpoint(String restEndpoint) {
		this.restEndpoint = restEndpoint;
	}
	public String getMlAppName() {
		return mlAppName;
	}
	public void setMlAppName(String mlAppName) {
		this.mlAppName = mlAppName;
	}
	public String getMlAppAdminId() {
		return mlAppAdminId;
	}
	public void setMlAppAdminId(String mlAppAdminId) {
		this.mlAppAdminId = mlAppAdminId;
	}
	public String getMotsId() {
		return motsId;
	}
	public void setMotsId(String motsId) {
		this.motsId = motsId;
	}
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public byte[] getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getUebTopicName() {
		return uebTopicName;
	}
	public void setUebTopicName(String uebTopicName) {
		this.uebTopicName = uebTopicName;
	}
	
	
}
