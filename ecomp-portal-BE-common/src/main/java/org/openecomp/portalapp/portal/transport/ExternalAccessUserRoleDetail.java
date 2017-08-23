package org.openecomp.portalapp.portal.transport;

public class ExternalAccessUserRoleDetail {
		
	private String name;
	private ExternalRoleDescription description;
	
	
	/**
	 * 
	 */
	public ExternalAccessUserRoleDetail() {
		super();
	}

	public ExternalAccessUserRoleDetail(String name, ExternalRoleDescription description) {
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
	public ExternalRoleDescription getDescription() {
		return description;
	}
	public void setDescription(ExternalRoleDescription description) {
		this.description = description;
	}
	
	
}
