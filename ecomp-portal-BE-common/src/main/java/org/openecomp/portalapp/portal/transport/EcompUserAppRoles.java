package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)	
public class EcompUserAppRoles implements Serializable {

	private static final long serialVersionUID = -3394219387296578741L;
	
	@Id
	@Column(name="app_id")
	private String appId;
	@Id
	@Column(name="user_id")
	private Long userId;
	@Id
	@Column(name="priority")
	private String priority ;
	@Id
	@Column(name="role_id")
	private Long roleId;
	@Id
	@Column(name="role_name")
	private String roleName;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
	
	
}
