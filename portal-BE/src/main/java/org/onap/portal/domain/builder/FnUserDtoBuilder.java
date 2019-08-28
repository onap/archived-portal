/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.domain.builder;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.dto.fn.FnUserDto;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FnUserDtoBuilder {

       private Long userId;
       private Long orgId;
       private Long managerId;
       private String firstName;
       private String middleName;
       private String lastName;
       private String phone;
       private String fax;
       private String cellular;
       private String email;
       private Long addressId;
       private String alertMethodCd;
       private String hrid;
       private String orgUserId;
       private String org_code;
       private String loginId;
       private String loginPwd;
       private LocalDateTime lastLoginDate;
       private String activeYn;
       private Long createdId;
       private LocalDateTime createdDate;
       private Long modifiedId;
       private LocalDateTime modifiedDate;
       private String isInternalYn;
       private String addressLine1;
       private String addressLine2;
       private String city;
       private String stateCd;
       private String zipCode;
       private String countryCd;
       private String locationClli;
       private String orgManagerUserId;
       private String company;
       private String departmentName;
       private String jobTitle;
       private Long timezone;
       private String department;
       private String businessUnit;
       private String businessUnitName;
       private String cost_center;
       private String finLocCode;
       private String siloStatus;
       private Long languageId;
       private boolean guest;

       public FnUserDtoBuilder setUserId(Long userId) {
              this.userId = userId;
              return this;
       }

       public FnUserDtoBuilder setOrgId(Long orgId) {
              this.orgId = orgId;
              return this;
       }

       public FnUserDtoBuilder setManagerId(Long managerId) {
              this.managerId = managerId;
              return this;
       }

       public FnUserDtoBuilder setFirstName(String firstName) {
              this.firstName = firstName;
              return this;
       }

       public FnUserDtoBuilder setMiddleName(String middleName) {
              this.middleName = middleName;
              return this;
       }

       public FnUserDtoBuilder setLastName(String lastName) {
              this.lastName = lastName;
              return this;
       }

       public FnUserDtoBuilder setPhone(String phone) {
              this.phone = phone;
              return this;
       }

       public FnUserDtoBuilder setFax(String fax) {
              this.fax = fax;
              return this;
       }

       public FnUserDtoBuilder setCellular(String cellular) {
              this.cellular = cellular;
              return this;
       }

       public FnUserDtoBuilder setEmail(String email) {
              this.email = email;
              return this;
       }

       public FnUserDtoBuilder setAddressId(Long addressId) {
              this.addressId = addressId;
              return this;
       }

       public FnUserDtoBuilder setAlertMethodCd(String alertMethodCd) {
              this.alertMethodCd = alertMethodCd;
              return this;
       }

       public FnUserDtoBuilder setHrid(String hrid) {
              this.hrid = hrid;
              return this;
       }

       public FnUserDtoBuilder setOrgUserId(String orgUserId) {
              this.orgUserId = orgUserId;
              return this;
       }

       public FnUserDtoBuilder setOrg_code(String org_code) {
              this.org_code = org_code;
              return this;
       }

       public FnUserDtoBuilder setLoginId(String loginId) {
              this.loginId = loginId;
              return this;
       }

       public FnUserDtoBuilder setLoginPwd(String loginPwd) {
              this.loginPwd = loginPwd;
              return this;
       }

       public FnUserDtoBuilder setLastLoginDate(LocalDateTime lastLoginDate) {
              this.lastLoginDate = lastLoginDate;
              return this;
       }

       public FnUserDtoBuilder setActiveYn(String activeYn) {
              this.activeYn = activeYn;
              return this;
       }

       public FnUserDtoBuilder setCreatedId(Long createdId) {
              this.createdId = createdId;
              return this;
       }

       public FnUserDtoBuilder setCreatedDate(LocalDateTime createdDate) {
              this.createdDate = createdDate;
              return this;
       }

       public FnUserDtoBuilder setModifiedId(Long modifiedId) {
              this.modifiedId = modifiedId;
              return this;
       }

       public FnUserDtoBuilder setModifiedDate(LocalDateTime modifiedDate) {
              this.modifiedDate = modifiedDate;
              return this;
       }

       public FnUserDtoBuilder setIsInternalYn(String isInternalYn) {
              this.isInternalYn = isInternalYn;
              return this;
       }

       public FnUserDtoBuilder setAddressLine1(String addressLine1) {
              this.addressLine1 = addressLine1;
              return this;
       }

       public FnUserDtoBuilder setAddressLine2(String addressLine2) {
              this.addressLine2 = addressLine2;
              return this;
       }

       public FnUserDtoBuilder setCity(String city) {
              this.city = city;
              return this;
       }

       public FnUserDtoBuilder setStateCd(String stateCd) {
              this.stateCd = stateCd;
              return this;
       }

       public FnUserDtoBuilder setZipCode(String zipCode) {
              this.zipCode = zipCode;
              return this;
       }

       public FnUserDtoBuilder setCountryCd(String countryCd) {
              this.countryCd = countryCd;
              return this;
       }

       public FnUserDtoBuilder setLocationClli(String locationClli) {
              this.locationClli = locationClli;
              return this;
       }

       public FnUserDtoBuilder setOrgManagerUserId(String orgManagerUserId) {
              this.orgManagerUserId = orgManagerUserId;
              return this;
       }

       public FnUserDtoBuilder setCompany(String company) {
              this.company = company;
              return this;
       }

       public FnUserDtoBuilder setDepartmentName(String departmentName) {
              this.departmentName = departmentName;
              return this;
       }

       public FnUserDtoBuilder setJobTitle(String jobTitle) {
              this.jobTitle = jobTitle;
              return this;
       }

       public FnUserDtoBuilder setTimezone(Long timezone) {
              this.timezone = timezone;
              return this;
       }

       public FnUserDtoBuilder setDepartment(String department) {
              this.department = department;
              return this;
       }

       public FnUserDtoBuilder setBusinessUnit(String businessUnit) {
              this.businessUnit = businessUnit;
              return this;
       }

       public FnUserDtoBuilder setBusinessUnitName(String businessUnitName) {
              this.businessUnitName = businessUnitName;
              return this;
       }

       public FnUserDtoBuilder setCost_center(String cost_center) {
              this.cost_center = cost_center;
              return this;
       }

       public FnUserDtoBuilder setFinLocCode(String finLocCode) {
              this.finLocCode = finLocCode;
              return this;
       }

       public FnUserDtoBuilder setSiloStatus(String siloStatus) {
              this.siloStatus = siloStatus;
              return this;
       }

       public FnUserDtoBuilder setLanguageId(Long languageId) {
              this.languageId = languageId;
              return this;
       }

       public FnUserDtoBuilder setGuest(boolean guest) {
              this.guest = guest;
              return this;
       }

       public FnUserDto createFnUserDto() {
              return new FnUserDto(userId, orgId, managerId, firstName, middleName, lastName, phone, fax, cellular,
                      email, addressId, alertMethodCd, hrid, orgUserId, org_code, loginId, loginPwd, lastLoginDate,
                      activeYn, createdId, createdDate, modifiedId, modifiedDate, isInternalYn, addressLine1,
                      addressLine2, city, stateCd, zipCode, countryCd, locationClli, orgManagerUserId, company,
                      departmentName, jobTitle, timezone, department, businessUnit, businessUnitName, cost_center,
                      finLocCode, siloStatus, languageId, guest);
       }
}