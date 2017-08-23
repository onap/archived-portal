package org.openecomp.portalapp.portal.transport;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value="perm")
public class ExternalAccessPermsDetail {

	private String type;
	private String instance;
	private String action;
	private List<String> roles;
	private String description;
	
	
	
	public ExternalAccessPermsDetail() {
		super();
	}

	/**
	 * @param type
	 * @param instance
	 * @param action
	 * @param roles
	 * @param description
	 */
	public ExternalAccessPermsDetail(String type, String instance, String action, List<String> roles,
			String description) {
		super();
		this.type = type;
		this.instance = instance;
		this.action = action;
		this.roles = roles;
		this.description = description;
	}
	
	/**
	 * @param type
	 * @param instance
	 * @param action
	 * @param description
	 */
	public ExternalAccessPermsDetail(String type, String instance, String action,
			String description) {
		super();
		this.type = type;
		this.instance = instance;
		this.action = action;
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
