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
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.cr.CrReportFileHistory;
import org.onap.portal.domain.db.ep.EpPersUserWidgetPlacement;
import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.onap.portal.domain.db.ep.EpUserNotification;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.db.fn.FnAuditLog;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnLuAlertMethod;
import org.onap.portal.domain.db.fn.FnLuTimezone;
import org.onap.portal.domain.db.fn.FnMenuFunctional;
import org.onap.portal.domain.db.fn.FnOrg;
import org.onap.portal.domain.db.fn.FnPersUserAppSel;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.springframework.stereotype.Component;

@Component
public class FnUserBuilder {

       private @Digits(integer = 11, fraction = 0) Long userId;
       private @Valid FnOrg orgId;
       private @Valid FnUser managerId;
       private @Size(max = 50)
       @SafeHtml String firstName;
       private @Size(max = 50)
       @SafeHtml String middleName;
       private @Size(max = 50)
       @SafeHtml String lastName;
       private @Size(max = 25)
       @SafeHtml String phone;
       private @Size(max = 25)
       @SafeHtml String fax;
       private @Size(max = 25)
       @SafeHtml String cellular;
       private @Size(max = 50) @Email
       @SafeHtml String email;
       private @Digits(integer = 11, fraction = 0) Long addressId;
       private FnLuAlertMethod alertMethodCd;
       private @Size(max = 20)
       @SafeHtml String hrid;
       private @Size(max = 20)
       @SafeHtml String orgUserId;
       private @Size(max = 30)
       @SafeHtml String org_code;
       private @Size(max = 25)
       @SafeHtml String loginId;
       private @Size(max = 100)
       @SafeHtml String loginPwd;
       private @PastOrPresent LocalDateTime lastLoginDate;
       private @Size(max = 1)
       @SafeHtml
       @NotNull(message = "activeYn must not be null") String activeYn;
       private @Valid FnUser createdId;
       private @PastOrPresent LocalDateTime createdDate;
       private @Valid FnUser modifiedId;
       private @PastOrPresent LocalDateTime modifiedDate;
       private @Size(max = 1)
       @SafeHtml
       @NotNull(message = "isInternalYn must not be null") String isInternalYn;
       private @Size(max = 100)
       @SafeHtml String addressLine1;
       private @Size(max = 100)
       @SafeHtml String addressLine2;
       private @Size(max = 50)
       @SafeHtml String city;
       private @Size(max = 3)
       @SafeHtml String stateCd;
       private @Size(max = 11)
       @SafeHtml String zipCode;
       private @Size(max = 3)
       @SafeHtml String countryCd;
       private @Size(max = 8)
       @SafeHtml String locationClli;
       private @Size(max = 20)
       @SafeHtml String orgManagerUserId;
       private @Size(max = 100)
       @SafeHtml String company;
       private @Size(max = 200)
       @SafeHtml String departmentName;
       private @Size(max = 100)
       @SafeHtml String jobTitle;
       private @Valid FnLuTimezone timezone;
       private @Size(max = 25)
       @SafeHtml String department;
       private @Size(max = 25)
       @SafeHtml String businessUnit;
       private @Size(max = 100)
       @SafeHtml String businessUnitName;
       private @Size(max = 25)
       @SafeHtml String cost_center;
       private @Size(max = 10)
       @SafeHtml String finLocCode;
       private @Size(max = 10)
       @SafeHtml String siloStatus;
       private @Valid @NotNull(message = "languageId must not be null") FnLanguage languageId;
       private @NotNull(message = "guest must not be null") boolean guest;
       private Set<CrReportFileHistory> crReportFileHistorie;
       private Set<FnRole> fnRoles;
       private Set<FnMenuFunctional> fnRoleList;
       private Set<FnAuditLog> fnAuditLogs;
       private Set<FnUser> fnUsersCreatedId;
       private Set<FnUser> fnUsersManagerId;
       private Set<FnUser> fnUsersModifiedId;
       private Set<EpUserRolesRequest> epUserRolesRequests;
       private Set<FnPersUserAppSel> persUserAppSels;
       private Set<EpWidgetCatalogParameter> epWidgetCatalogParameters;
       private Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements;
       private Set<EpPersUserWidgetSel> epPersUserWidgetSels;
       private Set<FnUserRole> fnUserRoles;
       private Set<EpUserNotification> epUserNotifications;

       public FnUserBuilder setUserId(@Digits(integer = 11, fraction = 0) Long userId) {
              this.userId = userId;
              return this;
       }

       public FnUserBuilder setOrgId(@Valid FnOrg orgId) {
              this.orgId = orgId;
              return this;
       }

       public FnUserBuilder setManagerId(@Valid FnUser managerId) {
              this.managerId = managerId;
              return this;
       }

       public FnUserBuilder setFirstName(@Size(max = 50) @SafeHtml String firstName) {
              this.firstName = firstName;
              return this;
       }

       public FnUserBuilder setMiddleName(@Size(max = 50) @SafeHtml String middleName) {
              this.middleName = middleName;
              return this;
       }

       public FnUserBuilder setLastName(@Size(max = 50) @SafeHtml String lastName) {
              this.lastName = lastName;
              return this;
       }

       public FnUserBuilder setPhone(@Size(max = 25) @SafeHtml String phone) {
              this.phone = phone;
              return this;
       }

       public FnUserBuilder setFax(@Size(max = 25) @SafeHtml String fax) {
              this.fax = fax;
              return this;
       }

       public FnUserBuilder setCellular(@Size(max = 25) @SafeHtml String cellular) {
              this.cellular = cellular;
              return this;
       }

       public FnUserBuilder setEmail(@Size(max = 50) @Email @SafeHtml String email) {
              this.email = email;
              return this;
       }

       public FnUserBuilder setAddressId(@Digits(integer = 11, fraction = 0) Long addressId) {
              this.addressId = addressId;
              return this;
       }

       public FnUserBuilder setAlertMethodCd(FnLuAlertMethod alertMethodCd) {
              this.alertMethodCd = alertMethodCd;
              return this;
       }

       public FnUserBuilder setHrid(@Size(max = 20) @SafeHtml String hrid) {
              this.hrid = hrid;
              return this;
       }

       public FnUserBuilder setOrgUserId(@Size(max = 20) @SafeHtml String orgUserId) {
              this.orgUserId = orgUserId;
              return this;
       }

       public FnUserBuilder setOrg_code(@Size(max = 30) @SafeHtml String org_code) {
              this.org_code = org_code;
              return this;
       }

       public FnUserBuilder setLoginId(@Size(max = 25) @SafeHtml String loginId) {
              this.loginId = loginId;
              return this;
       }

       public FnUserBuilder setLoginPwd(@Size(max = 100) @SafeHtml String loginPwd) {
              this.loginPwd = loginPwd;
              return this;
       }

       public FnUserBuilder setLastLoginDate(@PastOrPresent LocalDateTime lastLoginDate) {
              this.lastLoginDate = lastLoginDate;
              return this;
       }

       public FnUserBuilder setActiveYn(
               @Size(max = 1) @SafeHtml @NotNull(message = "activeYn must not be null") String activeYn) {
              this.activeYn = activeYn;
              return this;
       }

       public FnUserBuilder setCreatedId(@Valid FnUser createdId) {
              this.createdId = createdId;
              return this;
       }

       public FnUserBuilder setCreatedDate(@PastOrPresent LocalDateTime createdDate) {
              this.createdDate = createdDate;
              return this;
       }

       public FnUserBuilder setModifiedId(@Digits(integer = 11, fraction = 0) FnUser modifiedId) {
              this.modifiedId = modifiedId;
              return this;
       }

       public FnUserBuilder setModifiedDate(@PastOrPresent LocalDateTime modifiedDate) {
              this.modifiedDate = modifiedDate;
              return this;
       }

       public FnUserBuilder setIsInternalYn(
               @Size(max = 1) @SafeHtml @NotNull(message = "isInternalYn must not be null") String isInternalYn) {
              this.isInternalYn = isInternalYn;
              return this;
       }

       public FnUserBuilder setAddressLine1(@Size(max = 100) @SafeHtml String addressLine1) {
              this.addressLine1 = addressLine1;
              return this;
       }

       public FnUserBuilder setAddressLine2(@Size(max = 100) @SafeHtml String addressLine2) {
              this.addressLine2 = addressLine2;
              return this;
       }

       public FnUserBuilder setCity(@Size(max = 50) @SafeHtml String city) {
              this.city = city;
              return this;
       }

       public FnUserBuilder setStateCd(@Size(max = 3) @SafeHtml String stateCd) {
              this.stateCd = stateCd;
              return this;
       }

       public FnUserBuilder setZipCode(@Size(max = 11) @SafeHtml String zipCode) {
              this.zipCode = zipCode;
              return this;
       }

       public FnUserBuilder setCountryCd(@Size(max = 3) @SafeHtml String countryCd) {
              this.countryCd = countryCd;
              return this;
       }

       public FnUserBuilder setLocationClli(@Size(max = 8) @SafeHtml String locationClli) {
              this.locationClli = locationClli;
              return this;
       }

       public FnUserBuilder setOrgManagerUserId(@Size(max = 20) @SafeHtml String orgManagerUserId) {
              this.orgManagerUserId = orgManagerUserId;
              return this;
       }

       public FnUserBuilder setCompany(@Size(max = 100) @SafeHtml String company) {
              this.company = company;
              return this;
       }

       public FnUserBuilder setDepartmentName(@Size(max = 200) @SafeHtml String departmentName) {
              this.departmentName = departmentName;
              return this;
       }

       public FnUserBuilder setJobTitle(@Size(max = 100) @SafeHtml String jobTitle) {
              this.jobTitle = jobTitle;
              return this;
       }

       public FnUserBuilder setTimezone(@Valid FnLuTimezone timezone) {
              this.timezone = timezone;
              return this;
       }

       public FnUserBuilder setDepartment(@Size(max = 25) @SafeHtml String department) {
              this.department = department;
              return this;
       }

       public FnUserBuilder setBusinessUnit(@Size(max = 25) @SafeHtml String businessUnit) {
              this.businessUnit = businessUnit;
              return this;
       }

       public FnUserBuilder setBusinessUnitName(@Size(max = 100) @SafeHtml String businessUnitName) {
              this.businessUnitName = businessUnitName;
              return this;
       }

       public FnUserBuilder setCost_center(@Size(max = 25) @SafeHtml String cost_center) {
              this.cost_center = cost_center;
              return this;
       }

       public FnUserBuilder setFinLocCode(@Size(max = 10) @SafeHtml String finLocCode) {
              this.finLocCode = finLocCode;
              return this;
       }

       public FnUserBuilder setSiloStatus(@Size(max = 10) @SafeHtml String siloStatus) {
              this.siloStatus = siloStatus;
              return this;
       }

       public FnUserBuilder setLanguageId(
               @Valid @NotNull(message = "languageId must not be null") FnLanguage languageId) {
              this.languageId = languageId;
              return this;
       }

       public FnUserBuilder setGuest(@NotNull(message = "guest must not be null") boolean guest) {
              this.guest = guest;
              return this;
       }

       public FnUserBuilder setCrReportFileHistorie(Set<CrReportFileHistory> crReportFileHistorie) {
              this.crReportFileHistorie = crReportFileHistorie;
              return this;
       }

       public FnUserBuilder setFnRoles(Set<FnRole> fnRoles) {
              this.fnRoles = fnRoles;
              return this;
       }

       public FnUserBuilder setFnRoleList(Set<FnMenuFunctional> fnRoleList) {
              this.fnRoleList = fnRoleList;
              return this;
       }

       public FnUserBuilder setFnAuditLogs(Set<FnAuditLog> fnAuditLogs) {
              this.fnAuditLogs = fnAuditLogs;
              return this;
       }

       public FnUserBuilder setFnUsersCreatedId(Set<FnUser> fnUsersCreatedId) {
              this.fnUsersCreatedId = fnUsersCreatedId;
              return this;
       }

       public FnUserBuilder setFnUsersManagerId(Set<FnUser> fnUsersManagerId) {
              this.fnUsersManagerId = fnUsersManagerId;
              return this;
       }

       public FnUserBuilder setFnUsersModifiedId(Set<FnUser> fnUsersModifiedId) {
              this.fnUsersModifiedId = fnUsersModifiedId;
              return this;
       }

       public FnUserBuilder setEpUserRolesRequests(Set<EpUserRolesRequest> epUserRolesRequests) {
              this.epUserRolesRequests = epUserRolesRequests;
              return this;
       }

       public FnUserBuilder setPersUserAppSels(Set<FnPersUserAppSel> persUserAppSels) {
              this.persUserAppSels = persUserAppSels;
              return this;
       }

       public FnUserBuilder setEpWidgetCatalogParameters(Set<EpWidgetCatalogParameter> epWidgetCatalogParameters) {
              this.epWidgetCatalogParameters = epWidgetCatalogParameters;
              return this;
       }

       public FnUserBuilder setEpPersUserWidgetPlacements(Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements) {
              this.epPersUserWidgetPlacements = epPersUserWidgetPlacements;
              return this;
       }

       public FnUserBuilder setEpPersUserWidgetSels(Set<EpPersUserWidgetSel> epPersUserWidgetSels) {
              this.epPersUserWidgetSels = epPersUserWidgetSels;
              return this;
       }

       public FnUserBuilder setFnUserRoles(Set<FnUserRole> fnUserRoles) {
              this.fnUserRoles = fnUserRoles;
              return this;
       }

       public FnUserBuilder setEpUserNotifications(Set<EpUserNotification> epUserNotifications) {
              this.epUserNotifications = epUserNotifications;
              return this;
       }

       public FnUser createFnUser() {
              return new FnUser(userId, orgId, managerId, firstName, middleName, lastName, phone, fax, cellular, email,
                      addressId, alertMethodCd, hrid, orgUserId, org_code, loginId, loginPwd, lastLoginDate, activeYn,
                      createdId, createdDate, modifiedId, modifiedDate, isInternalYn, addressLine1, addressLine2, city,
                      stateCd, zipCode, countryCd, locationClli, orgManagerUserId, company, departmentName, jobTitle,
                      timezone, department, businessUnit, businessUnitName, cost_center, finLocCode, siloStatus,
                      languageId, guest, crReportFileHistorie, fnRoles, fnRoleList, fnAuditLogs, fnUsersCreatedId,
                      fnUsersManagerId, fnUsersModifiedId, epUserRolesRequests, persUserAppSels,
                      epWidgetCatalogParameters, epPersUserWidgetPlacements, epPersUserWidgetSels, fnUserRoles,
                      epUserNotifications);
       }
}