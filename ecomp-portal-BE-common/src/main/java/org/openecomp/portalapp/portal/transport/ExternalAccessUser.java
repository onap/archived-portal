package org.openecomp.portalapp.portal.transport;

public class ExternalAccessUser {

	private String user;
	private String role;

	public ExternalAccessUser(String user, String role) {
		super();
		this.user = user;
		this.role = role;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
