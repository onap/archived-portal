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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.transport;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class CentralUser implements Serializable {

	private static final long serialVersionUID = 7060454665330579923L;
	private Long id;
	private Date created;
	private Date modified;
	private Long createdId;
	private Long modifiedId;
	private Long rowNum;

	private Long orgId;
	private Long managerId;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String phone;
	private String fax;
	private String cellular;
	private String email;
	private Long addressId;
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
	private Date lastLoginDate;
	private boolean active;
	private boolean internal;
	private Long selectedProfileId;
	private Long timeZoneId;
	private boolean online;
	private String chatId;

	private Set<CentralUserApp> userApps = new TreeSet<>();
	private Set<CentralRole> pseudoRoles = new TreeSet<>();

	public CentralUser(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, Long orgId,
			Long managerId, String firstName, String middleInitial, String lastName, String phone, String fax,
			String cellular, String email, Long addressId, String alertMethodCd, String hrid, String orgUserId,
			String orgCode, String address1, String address2, String city, String state, String zipCode, String country,
			String orgManagerUserId, String locationClli, String businessCountryCode, String businessCountryName,
			String businessUnit, String businessUnitName, String department, String departmentName, String companyCode,
			String company, String zipCodeSuffix, String jobTitle, String commandChain, String siloStatus,
			String costCenter, String financialLocCode, String loginId, String loginPwd, Date lastLoginDate,
			boolean active, boolean internal, Long selectedProfileId, Long timeZoneId, boolean online, String chatId,
			Set<CentralUserApp> userApps) {
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
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param created
	 *            the created to set
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
	 * @param modified
	 *            the modified to set
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
	 * @param createdId
	 *            the createdId to set
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
	 * @param modifiedId
	 *            the modifiedId to set
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
	 * @param rowNum
	 *            the rowNum to set
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
	 * @param orgId
	 *            the orgId to set
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
	 * @param managerId
	 *            the managerId to set
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
	 * @param firstName
	 *            the firstName to set
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
	 * @param middleInitial
	 *            the middleInitial to set
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
	 * @param lastName
	 *            the lastName to set
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
	 * @param phone
	 *            the phone to set
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
	 * @param fax
	 *            the fax to set
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
	 * @param cellular
	 *            the cellular to set
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
	 * @param email
	 *            the email to set
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
	 * @param addressId
	 *            the addressId to set
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
	 * @param alertMethodCd
	 *            the alertMethodCd to set
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
	 * @param hrid
	 *            the hrid to set
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
	 * @param orgUserId
	 *            the orgUserId to set
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
	 * @param orgCode
	 *            the orgCode to set
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
	 * @param address1
	 *            the address1 to set
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
	 * @param address2
	 *            the address2 to set
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
	 * @param city
	 *            the city to set
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
	 * @param state
	 *            the state to set
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
	 * @param zipCode
	 *            the zipCode to set
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
	 * @param country
	 *            the country to set
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
	 * @param orgManagerUserId
	 *            the orgManagerUserId to set
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
	 * @param locationClli
	 *            the locationClli to set
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
	 * @param businessCountryCode
	 *            the businessCountryCode to set
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
	 * @param businessCountryName
	 *            the businessCountryName to set
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
	 * @param businessUnit
	 *            the businessUnit to set
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
	 * @param businessUnitName
	 *            the businessUnitName to set
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
	 * @param department
	 *            the department to set
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
	 * @param departmentName
	 *            the departmentName to set
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
	 * @param companyCode
	 *            the companyCode to set
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
	 * @param company
	 *            the company to set
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
	 * @param zipCodeSuffix
	 *            the zipCodeSuffix to set
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
	 * @param jobTitle
	 *            the jobTitle to set
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
	 * @param commandChain
	 *            the commandChain to set
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
	 * @param siloStatus
	 *            the siloStatus to set
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
	 * @param costCenter
	 *            the costCenter to set
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
	 * @param financialLocCode
	 *            the financialLocCode to set
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
	 * @param loginId
	 *            the loginId to set
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
	 * @param loginPwd
	 *            the loginPwd to set
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
	 * @param lastLoginDate
	 *            the lastLoginDate to set
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
	 * @param active
	 *            the active to set
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
	 * @param internal
	 *            the internal to set
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
	 * @param selectedProfileId
	 *            the selectedProfileId to set
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
	 * @param timeZoneId
	 *            the timeZoneId to set
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
	 * @param online
	 *            the online to set
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
	 * @param chatId
	 *            the chatId to set
	 */
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return the userApps
	 */
	public Set<CentralUserApp> getUserApps() {
		return userApps;
	}

	/**
	 * @param userApps
	 *            the userApps to set
	 */
	public void setUserApps(Set<CentralUserApp> userApps) {
		this.userApps = userApps;
	}

	/**
	 * @return the pseudoRoles
	 */
	public Set<CentralRole> getPseudoRoles() {
		return pseudoRoles;
	}

	/**
	 * @param pseudoRoles
	 *            the pseudoRoles to set
	 */
	public void setPseudoRoles(Set<CentralRole> pseudoRoles) {
		this.pseudoRoles = pseudoRoles;
	}

}
