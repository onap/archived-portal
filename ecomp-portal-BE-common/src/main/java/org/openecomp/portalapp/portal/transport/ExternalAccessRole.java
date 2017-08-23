package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;

public class ExternalAccessRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3439986826362436339L;
	public String name;
	public String description;
	
	public ExternalAccessRole() {
	
	}
	
	public ExternalAccessRole(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
