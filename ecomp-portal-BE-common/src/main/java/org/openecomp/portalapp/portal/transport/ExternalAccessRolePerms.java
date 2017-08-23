package org.openecomp.portalapp.portal.transport;

public class ExternalAccessRolePerms {
	
	private ExternalAccessPerms perm;	
	private String role;
	
	
	public ExternalAccessRolePerms(ExternalAccessPerms perm, String role) {
		super();
		this.perm = perm;
		this.role = role;
	}
	
	public ExternalAccessPerms getPerm() {
		return perm;
	}
	public void setPerm(ExternalAccessPerms perm) {
		this.perm = perm;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
