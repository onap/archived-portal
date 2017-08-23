package org.openecomp.portalapp.portal.transport;

import java.util.Date;
import java.util.Set;

public class CentralUser {


	public Long id;
	public Date created;
	public Date modified;
	public Long createdId;
	public Long modifiedId;
	public Long rowNum;
	
	public Long   orgId;
    public Long   managerId;
    public String firstName;
    public String middleInitial;
    public String lastName;
    public String phone;
    public String fax;
    public String cellular;
    public String email;
    public Long   addressId;
    public String alertMethodCd;
    public String hrid;
    public String orgUserId;
    public String orgCode;
    public String address1;
    public String address2;
    public String city;
    public String state;
    public String zipCode;
    public String country;
    public String orgManagerUserId;
    public String locationClli;
    public String businessCountryCode;
    public String businessCountryName;
    public String businessUnit;
    public String businessUnitName;
    public String department;
    public String departmentName;
    public String companyCode;
    public String company;
    public String zipCodeSuffix;
    public String jobTitle;
    public String commandChain;
    public String siloStatus;
    public String costCenter;
    public String financialLocCode;

    public String loginId;
    public String loginPwd;
    public Date   lastLoginDate;
    public boolean active;
    public boolean internal;
    public Long    selectedProfileId;
    public Long timeZoneId;
    public boolean online;
    public String chatId;
    
    public Set<CentralUserApp> userApps = null;
	public Set<CentralRole> pseudoRoles = null;
	
	public CentralUser(){
		
	}
	
	public CentralUser(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, Long orgId,
			Long managerId, String firstName, String middleInitial, String lastName, String phone, String fax,
			String cellular, String email, Long addressId, String alertMethodCd, String hrid, String orgUserId,
			String orgCode, String address1, String address2, String city, String state, String zipCode, String country,
			String orgManagerUserId, String locationClli, String businessCountryCode, String businessCountryName,
			String businessUnit, String businessUnitName, String department, String departmentName, String companyCode,
			String company, String zipCodeSuffix, String jobTitle, String commandChain, String siloStatus,
			String costCenter, String financialLocCode, String loginId, String loginPwd, Date lastLoginDate,
			boolean active, boolean internal, Long selectedProfileId, Long timeZoneId, boolean online, String chatId,
			Set<CentralUserApp> userApps, Set<CentralRole> pseudoRoles) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.createdId = createdId;
		this.modifiedId = modifiedId;
		this.rowNum = rowNum;
		this.orgId = orgId;
		this.managerId = managerId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.phone = phone;
		this.fax = fax;
		this.cellular = cellular;
		this.email = email;
		this.addressId = addressId;
		this.alertMethodCd = alertMethodCd;
		this.hrid = hrid;
		this.orgUserId = orgUserId;
		this.orgCode = orgCode;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
		this.orgManagerUserId = orgManagerUserId;
		this.locationClli = locationClli;
		this.businessCountryCode = businessCountryCode;
		this.businessCountryName = businessCountryName;
		this.businessUnit = businessUnit;
		this.businessUnitName = businessUnitName;
		this.department = department;
		this.departmentName = departmentName;
		this.companyCode = companyCode;
		this.company = company;
		this.zipCodeSuffix = zipCodeSuffix;
		this.jobTitle = jobTitle;
		this.commandChain = commandChain;
		this.siloStatus = siloStatus;
		this.costCenter = costCenter;
		this.financialLocCode = financialLocCode;
		this.loginId = loginId;
		this.loginPwd = loginPwd;
		this.lastLoginDate = lastLoginDate;
		this.active = active;
		this.internal = internal;
		this.selectedProfileId = selectedProfileId;
		this.timeZoneId = timeZoneId;
		this.online = online;
		this.chatId = chatId;
		this.userApps = userApps;
		this.pseudoRoles = pseudoRoles;
	}
	
	

	
}
