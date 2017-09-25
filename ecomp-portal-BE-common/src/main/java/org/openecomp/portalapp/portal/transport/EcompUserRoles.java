package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EcompUserRoles implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "org_id")
	private Long orgId;
	@Id
	@Column(name = "manager_id")
	private String managerId;
	@Id
	@Column(name = "first_name")
	private String firstName;
	@Id
	@Column(name = "middle_name")
	private String middleInitial;
	@Id
	@Column(name = "last_name")
	private String lastName;
	@Id
	@Column(name = "phone")
	private String phone;
	@Id
	@Column(name = "email")
	private String email;
	@Id
	@Column(name = "hrid")
	private String hrid;
	@Id
	@Column(name = "org_user_id")
	private String orgUserId;
	@Id
	@Column(name = "org_code")
	private String orgCode;
	@Id
	@Column(name = "org_manager_userid")
	private String orgManagerUserId;
	@Id
	@Column(name = "job_title")
	private String jobTitle;
	@Id
	@Column(name = "login_id")
	private String loginId;

	@Id
	@Column(name = "app_role_id")
	private Long roleId;
	@Id
	@Column(name = "role_name")
	private String roleName;
	@Id
	@Column(name = "active_yn")
	private boolean active;
 
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHrid() {
		return hrid;
	}
	public void setHrid(String hrid) {
		this.hrid = hrid;
	}
	public String getOrgUserId() {
		return orgUserId;
	}
	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgManagerUserId() {
		return orgManagerUserId;
	}
	public void setOrgManagerUserId(String orgManagerUserId) {
		this.orgManagerUserId = orgManagerUserId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "EcompUserRoles [orgId=" + orgId + ", managerId=" + managerId + ", firstName=" + firstName
				+ ", middleInitial=" + middleInitial + ", lastName=" + lastName + ", phone=" + phone + ", email="
				+ email + ", hrid=" + hrid + ", orgUserId=" + orgUserId + ", orgCode=" + orgCode + ", orgManagerUserId="
				+ orgManagerUserId + ", jobTitle=" + jobTitle + ", loginId=" + loginId + ", active=" + active
				+ ", roleId=" + roleId + ", roleName=" + roleName + "]";
	}
	

}
