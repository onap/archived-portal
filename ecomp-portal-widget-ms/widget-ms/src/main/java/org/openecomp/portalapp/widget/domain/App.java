package org.openecomp.portalapp.widget.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FN_APP")
public class App implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long appId;
	
	@Column(name = "APP_Name")
	private String appName;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return "App [appId=" + appId + ", appName=" + appName + "]";
	}

}
