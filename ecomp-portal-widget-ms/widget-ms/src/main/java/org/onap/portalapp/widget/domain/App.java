package org.onap.portalapp.widget.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "FN_APP")
public class App implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Digits(integer = 11, fraction = 0)
	private Long appId;

	@Column(name = "APP_Name")
	@SafeHtml
	@Size(max = 100)
	@NotNull
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
