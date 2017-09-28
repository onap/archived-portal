/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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

	public Long orgId;
	public Long managerId;
	public String firstName;
	public String middleInitial;
	public String lastName;
	public String phone;
	public String fax;
	public String cellular;
	public String email;
	public Long addressId;
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
	public Date lastLoginDate;
	public boolean active;
	public boolean internal;
	public Long selectedProfileId;
	public Long timeZoneId;
	public boolean online;
	public String chatId;

	public Set<CentralUserApp> userApps = null;
	public Set<CentralRole> pseudoRoles = null;

	public CentralUser() {

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
		CentralUser other = (CentralUser) obj;
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
