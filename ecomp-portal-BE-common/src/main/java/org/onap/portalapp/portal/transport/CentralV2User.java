/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.transport;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class CentralV2User implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2673289523184880563L;
	private Long id;
	private Date created;
	private Date modified;
	private Long createdId;
	private Long modifiedId;
	private Long rowNum;
	
	private Long   orgId;
    private Long   managerId;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String phone;
    private String fax;
    private String cellular;
    private String email;
    private Long   addressId;
    private String alertMethodCd;
    private String hrid;
    private String orgUserId;
    private String orgCode;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String orgManagerUserId;
    private String locationClli;
    private String businessCountryCode;
    private String businessCountryName;
    private String businessUnit;
    private String businessUnitName;
    private String department;
    private String departmentName;
    private String companyCode;
    private String company;
    private String zipCodeSuffix;
    private String jobTitle;
    private String commandChain;
    private String siloStatus;
    private String costCenter;
    private String financialLocCode;

    private String loginId;
    private String loginPwd;
    private Date   lastLoginDate;
    private boolean active;
    private boolean internal;
    private Long    selectedProfileId;
    private Long timeZoneId;
    private boolean online;
    private String chatId;
    
    private Set<CentralV2UserApp> userApps = null;
	private Set<CentralV2Role> pseudoRoles = null;
	
	public CentralV2User(){
		
	}
	
	public CentralV2User(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, Long orgId,
			Long managerId, String firstName, String middleInitial, String lastName, String phone, String fax,
			String cellular, String email, Long addressId, String alertMethodCd, String hrid, String orgUserId,
			String orgCode, String address1, String address2, String city, String state, String zipCode, String country,
			String orgManagerUserId, String locationClli, String businessCountryCode, String businessCountryName,
			String businessUnit, String businessUnitName, String department, String departmentName, String companyCode,
			String company, String zipCodeSuffix, String jobTitle, String commandChain, String siloStatus,
			String costCenter, String financialLocCode, String loginId, String loginPwd, Date lastLoginDate,
			boolean active, boolean internal, Long selectedProfileId, Long timeZoneId, boolean online, String chatId,
			Set<CentralV2UserApp> userApps, Set<CentralV2Role> pseudoRoles) {
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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the createdId
	 */
	public Long getCreatedId() {
		return createdId;
	}

	/**
	 * @param createdId the createdId to set
	 */
	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	/**
	 * @return the modifiedId
	 */
	public Long getModifiedId() {
		return modifiedId;
	}

	/**
	 * @param modifiedId the modifiedId to set
	 */
	public void setModifiedId(Long modifiedId) {
		this.modifiedId = modifiedId;
	}

	/**
	 * @return the rowNum
	 */
	public Long getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	/**
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the managerId
	 */
	public Long getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleInitial
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * @param middleInitial the middleInitial to set
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the cellular
	 */
	public String getCellular() {
		return cellular;
	}

	/**
	 * @param cellular the cellular to set
	 */
	public void setCellular(String cellular) {
		this.cellular = cellular;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the addressId
	 */
	public Long getAddressId() {
		return addressId;
	}

	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	/**
	 * @return the alertMethodCd
	 */
	public String getAlertMethodCd() {
		return alertMethodCd;
	}

	/**
	 * @param alertMethodCd the alertMethodCd to set
	 */
	public void setAlertMethodCd(String alertMethodCd) {
		this.alertMethodCd = alertMethodCd;
	}

	/**
	 * @return the hrid
	 */
	public String getHrid() {
		return hrid;
	}

	/**
	 * @param hrid the hrid to set
	 */
	public void setHrid(String hrid) {
		this.hrid = hrid;
	}

	/**
	 * @return the orgUserId
	 */
	public String getOrgUserId() {
		return orgUserId;
	}

	/**
	 * @param orgUserId the orgUserId to set
	 */
	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the orgManagerUserId
	 */
	public String getOrgManagerUserId() {
		return orgManagerUserId;
	}

	/**
	 * @param orgManagerUserId the orgManagerUserId to set
	 */
	public void setOrgManagerUserId(String orgManagerUserId) {
		this.orgManagerUserId = orgManagerUserId;
	}

	/**
	 * @return the locationClli
	 */
	public String getLocationClli() {
		return locationClli;
	}

	/**
	 * @param locationClli the locationClli to set
	 */
	public void setLocationClli(String locationClli) {
		this.locationClli = locationClli;
	}

	/**
	 * @return the businessCountryCode
	 */
	public String getBusinessCountryCode() {
		return businessCountryCode;
	}

	/**
	 * @param businessCountryCode the businessCountryCode to set
	 */
	public void setBusinessCountryCode(String businessCountryCode) {
		this.businessCountryCode = businessCountryCode;
	}

	/**
	 * @return the businessCountryName
	 */
	public String getBusinessCountryName() {
		return businessCountryName;
	}

	/**
	 * @param businessCountryName the businessCountryName to set
	 */
	public void setBusinessCountryName(String businessCountryName) {
		this.businessCountryName = businessCountryName;
	}

	/**
	 * @return the businessUnit
	 */
	public String getBusinessUnit() {
		return businessUnit;
	}

	/**
	 * @param businessUnit the businessUnit to set
	 */
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	/**
	 * @return the businessUnitName
	 */
	public String getBusinessUnitName() {
		return businessUnitName;
	}

	/**
	 * @param businessUnitName the businessUnitName to set
	 */
	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the zipCodeSuffix
	 */
	public String getZipCodeSuffix() {
		return zipCodeSuffix;
	}

	/**
	 * @param zipCodeSuffix the zipCodeSuffix to set
	 */
	public void setZipCodeSuffix(String zipCodeSuffix) {
		this.zipCodeSuffix = zipCodeSuffix;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the commandChain
	 */
	public String getCommandChain() {
		return commandChain;
	}

	/**
	 * @param commandChain the commandChain to set
	 */
	public void setCommandChain(String commandChain) {
		this.commandChain = commandChain;
	}

	/**
	 * @return the siloStatus
	 */
	public String getSiloStatus() {
		return siloStatus;
	}

	/**
	 * @param siloStatus the siloStatus to set
	 */
	public void setSiloStatus(String siloStatus) {
		this.siloStatus = siloStatus;
	}

	/**
	 * @return the costCenter
	 */
	public String getCostCenter() {
		return costCenter;
	}

	/**
	 * @param costCenter the costCenter to set
	 */
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	/**
	 * @return the financialLocCode
	 */
	public String getFinancialLocCode() {
		return financialLocCode;
	}

	/**
	 * @param financialLocCode the financialLocCode to set
	 */
	public void setFinancialLocCode(String financialLocCode) {
		this.financialLocCode = financialLocCode;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the loginPwd
	 */
	public String getLoginPwd() {
		return loginPwd;
	}

	/**
	 * @param loginPwd the loginPwd to set
	 */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/**
	 * @return the lastLoginDate
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the internal
	 */
	public boolean isInternal() {
		return internal;
	}

	/**
	 * @param internal the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	/**
	 * @return the selectedProfileId
	 */
	public Long getSelectedProfileId() {
		return selectedProfileId;
	}

	/**
	 * @param selectedProfileId the selectedProfileId to set
	 */
	public void setSelectedProfileId(Long selectedProfileId) {
		this.selectedProfileId = selectedProfileId;
	}

	/**
	 * @return the timeZoneId
	 */
	public Long getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId(Long timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the chatId
	 */
	public String getChatId() {
		return chatId;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return the userApps
	 */
	public Set<CentralV2UserApp> getUserApps() {
		return userApps;
	}

	/**
	 * @param userApps the userApps to set
	 */
	public void setUserApps(Set<CentralV2UserApp> userApps) {
		this.userApps = userApps;
	}

	/**
	 * @return the pseudoRoles
	 */
	public Set<CentralV2Role> getPseudoRoles() {
		return pseudoRoles;
	}

	/**
	 * @param pseudoRoles the pseudoRoles to set
	 */
	public void setPseudoRoles(Set<CentralV2Role> pseudoRoles) {
		this.pseudoRoles = pseudoRoles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result + ((alertMethodCd == null) ? 0 : alertMethodCd.hashCode());
		result = prime * result + ((businessCountryCode == null) ? 0 : businessCountryCode.hashCode());
		result = prime * result + ((businessCountryName == null) ? 0 : businessCountryName.hashCode());
		result = prime * result + ((businessUnit == null) ? 0 : businessUnit.hashCode());
		result = prime * result + ((businessUnitName == null) ? 0 : businessUnitName.hashCode());
		result = prime * result + ((cellular == null) ? 0 : cellular.hashCode());
		result = prime * result + ((chatId == null) ? 0 : chatId.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((commandChain == null) ? 0 : commandChain.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((costCenter == null) ? 0 : costCenter.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((createdId == null) ? 0 : createdId.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((departmentName == null) ? 0 : departmentName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((financialLocCode == null) ? 0 : financialLocCode.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((hrid == null) ? 0 : hrid.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (internal ? 1231 : 1237);
		result = prime * result + ((jobTitle == null) ? 0 : jobTitle.hashCode());
		result = prime * result + ((lastLoginDate == null) ? 0 : lastLoginDate.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((locationClli == null) ? 0 : locationClli.hashCode());
		result = prime * result + ((loginId == null) ? 0 : loginId.hashCode());
		result = prime * result + ((loginPwd == null) ? 0 : loginPwd.hashCode());
		result = prime * result + ((managerId == null) ? 0 : managerId.hashCode());
		result = prime * result + ((middleInitial == null) ? 0 : middleInitial.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((modifiedId == null) ? 0 : modifiedId.hashCode());
		result = prime * result + (online ? 1231 : 1237);
		result = prime * result + ((orgCode == null) ? 0 : orgCode.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((orgManagerUserId == null) ? 0 : orgManagerUserId.hashCode());
		result = prime * result + ((orgUserId == null) ? 0 : orgUserId.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((pseudoRoles == null) ? 0 : pseudoRoles.hashCode());
		result = prime * result + ((rowNum == null) ? 0 : rowNum.hashCode());
		result = prime * result + ((selectedProfileId == null) ? 0 : selectedProfileId.hashCode());
		result = prime * result + ((siloStatus == null) ? 0 : siloStatus.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((timeZoneId == null) ? 0 : timeZoneId.hashCode());
		result = prime * result + ((userApps == null) ? 0 : userApps.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((zipCodeSuffix == null) ? 0 : zipCodeSuffix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CentralV2User other = (CentralV2User) obj;
		if (active != other.active)
			return false;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (alertMethodCd == null) {
			if (other.alertMethodCd != null)
				return false;
		} else if (!alertMethodCd.equals(other.alertMethodCd))
			return false;
		if (businessCountryCode == null) {
			if (other.businessCountryCode != null)
				return false;
		} else if (!businessCountryCode.equals(other.businessCountryCode))
			return false;
		if (businessCountryName == null) {
			if (other.businessCountryName != null)
				return false;
		} else if (!businessCountryName.equals(other.businessCountryName))
			return false;
		if (businessUnit == null) {
			if (other.businessUnit != null)
				return false;
		} else if (!businessUnit.equals(other.businessUnit))
			return false;
		if (businessUnitName == null) {
			if (other.businessUnitName != null)
				return false;
		} else if (!businessUnitName.equals(other.businessUnitName))
			return false;
		if (cellular == null) {
			if (other.cellular != null)
				return false;
		} else if (!cellular.equals(other.cellular))
			return false;
		if (chatId == null) {
			if (other.chatId != null)
				return false;
		} else if (!chatId.equals(other.chatId))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (commandChain == null) {
			if (other.commandChain != null)
				return false;
		} else if (!commandChain.equals(other.commandChain))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (costCenter == null) {
			if (other.costCenter != null)
				return false;
		} else if (!costCenter.equals(other.costCenter))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdId == null) {
			if (other.createdId != null)
				return false;
		} else if (!createdId.equals(other.createdId))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (financialLocCode == null) {
			if (other.financialLocCode != null)
				return false;
		} else if (!financialLocCode.equals(other.financialLocCode))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (hrid == null) {
			if (other.hrid != null)
				return false;
		} else if (!hrid.equals(other.hrid))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (internal != other.internal)
			return false;
		if (jobTitle == null) {
			if (other.jobTitle != null)
				return false;
		} else if (!jobTitle.equals(other.jobTitle))
			return false;
		if (lastLoginDate == null) {
			if (other.lastLoginDate != null)
				return false;
		} else if (!lastLoginDate.equals(other.lastLoginDate))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (locationClli == null) {
			if (other.locationClli != null)
				return false;
		} else if (!locationClli.equals(other.locationClli))
			return false;
		if (loginId == null) {
			if (other.loginId != null)
				return false;
		} else if (!loginId.equals(other.loginId))
			return false;
		if (loginPwd == null) {
			if (other.loginPwd != null)
				return false;
		} else if (!loginPwd.equals(other.loginPwd))
			return false;
		if (managerId == null) {
			if (other.managerId != null)
				return false;
		} else if (!managerId.equals(other.managerId))
			return false;
		if (middleInitial == null) {
			if (other.middleInitial != null)
				return false;
		} else if (!middleInitial.equals(other.middleInitial))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (modifiedId == null) {
			if (other.modifiedId != null)
				return false;
		} else if (!modifiedId.equals(other.modifiedId))
			return false;
		if (online != other.online)
			return false;
		if (orgCode == null) {
			if (other.orgCode != null)
				return false;
		} else if (!orgCode.equals(other.orgCode))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (orgManagerUserId == null) {
			if (other.orgManagerUserId != null)
				return false;
		} else if (!orgManagerUserId.equals(other.orgManagerUserId))
			return false;
		if (orgUserId == null) {
			if (other.orgUserId != null)
				return false;
		} else if (!orgUserId.equals(other.orgUserId))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (pseudoRoles == null) {
			if (other.pseudoRoles != null)
				return false;
		} else if (!pseudoRoles.equals(other.pseudoRoles))
			return false;
		if (rowNum == null) {
			if (other.rowNum != null)
				return false;
		} else if (!rowNum.equals(other.rowNum))
			return false;
		if (selectedProfileId == null) {
			if (other.selectedProfileId != null)
				return false;
		} else if (!selectedProfileId.equals(other.selectedProfileId))
			return false;
		if (siloStatus == null) {
			if (other.siloStatus != null)
				return false;
		} else if (!siloStatus.equals(other.siloStatus))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (timeZoneId == null) {
			if (other.timeZoneId != null)
				return false;
		} else if (!timeZoneId.equals(other.timeZoneId))
			return false;
		if (userApps == null) {
			if (other.userApps != null)
				return false;
		} else if (!userApps.equals(other.userApps))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		if (zipCodeSuffix == null) {
			if (other.zipCodeSuffix != null)
				return false;
		} else if (!zipCodeSuffix.equals(other.zipCodeSuffix))
			return false;
		return true;
	}

	
	
}
