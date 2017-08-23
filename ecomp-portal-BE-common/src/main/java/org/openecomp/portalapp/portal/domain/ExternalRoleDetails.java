package org.openecomp.portalapp.portal.domain;

import java.util.List;

public class ExternalRoleDetails implements Comparable {

	
	private String  name;
    private boolean active;
    private Integer priority;
    
    private Long appId;     // used by ECOMP only 
    private Long appRoleId; // used by ECOMP only

	private List<EPAppRoleFunction> perms;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getAppRoleId() {
		return appRoleId;
	}

	public void setAppRoleId(Long appRoleId) {
		this.appRoleId = appRoleId;
	}



	public List<EPAppRoleFunction> getPerms() {
		return perms;
	}

	public void setPerms(List<EPAppRoleFunction> perms) {
		this.perms = perms;
	}

	@Override
	public int compareTo(Object obj) {
		EPRole other = (EPRole)obj;

    	String c1 = getName();
    	String c2 = other.getName();

    	return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	}

	
}
