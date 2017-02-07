package org.openecomp.portalapp.portal.transport;


/**
 * Model for the object PUT to the controller when the user takes an action on
 * an application in the catalog.
 */
public class AppCatalogPersonalization {

	public Long appId;
	public Boolean select;
	public Boolean pending;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public Boolean getPending() {
		return pending;
	}

	public void setPending(Boolean pending) {
		this.pending = pending;
	}

}