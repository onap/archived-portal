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
package org.onap.portalapp.portal.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.Valid;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EPUser extends User {

    private Long orgId;
    private Long managerId;
    @SafeHtml
    private String firstName;
    @SafeHtml
    private String middleInitial;
    @SafeHtml
    private String lastName;
    @SafeHtml
    private String phone;
    @SafeHtml
    private String fax;
    @SafeHtml
    private String cellular;
    @SafeHtml
    private String email;
    private Long addressId;
    @SafeHtml
    private String alertMethodCd;
    @SafeHtml
    private String hrid;
    @SafeHtml
    private String orgUserId;
    @SafeHtml
    private String orgCode;
    @SafeHtml
    private String address1;
    @SafeHtml
    private String address2;
    @SafeHtml
    private String city;
    @SafeHtml
    private String state;
    @SafeHtml
    private String zipCode;
    @SafeHtml
    private String country;
    @SafeHtml
    private String orgManagerUserId;
    @SafeHtml
    private String locationClli;
    @SafeHtml
    private String businessCountryCode;
    @SafeHtml
    private String businessCountryName;
    @SafeHtml
    private String businessUnit;
    @SafeHtml
    private String businessUnitName;
    @SafeHtml
    private String department;
    @SafeHtml
    private String departmentName;
    @SafeHtml
    private String companyCode;
    @SafeHtml
    private String company;
    @SafeHtml
    private String zipCodeSuffix;
    @SafeHtml
    private String jobTitle;
    @SafeHtml
    private String commandChain;
    @SafeHtml
    private String siloStatus;
    @SafeHtml
    private String costCenter;
    @SafeHtml
    private String financialLocCode;

    @SafeHtml
    private String loginId;
    @SafeHtml
    private String loginPwd;
    private Date lastLoginDate;
    private boolean active;
    private boolean internal;
    private Long selectedProfileId;
    private Long timeZoneId;
    private boolean online;
    @SafeHtml
    private String chatId;
    private boolean systemUser;
    private Integer languageId;
    private static final long serialVersionUID = 1L;

    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUser.class);
    private static final String ECOMP_PORTAL_NAME = "ECOMP";
    private boolean isGuest = false;
    @Valid
    private SortedSet<EPUserApp> userApps = new TreeSet<>();
    @Valid
    private SortedSet<EPRole> pseudoRoles = new TreeSet<>();

    public EPUser() {}

    @Override
    public Long getAddressId() {
        return addressId;
    }

    @Override
    public String getAlertMethodCd() {
        return alertMethodCd;
    }

    @Override
    public String getCellular() {
        return cellular;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFax() {
        return fax;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getHrid() {
        return hrid;
    }

    @Override
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    @Override
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getLoginId() {
        return loginId;
    }

    @Override
    public String getLoginPwd() {
        return loginPwd;
    }

    @Override
    public Long getManagerId() {
        return managerId;
    }

    @Override
    public String getMiddleInitial() {
        return middleInitial;
    }

    @Override
    public String getOrgCode() {
        return orgCode;
    }

    @Override
    public Long getOrgId() {
        return orgId;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getOrgUserId() {
        return orgUserId;
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public boolean getInternal() {
        return internal;
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String getBusinessCountryCode() {
        return businessCountryCode;
    }

    @Override
    public String getCommandChain() {
        return commandChain;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public String getLocationClli() {
        return locationClli;
    }

    @Override
    public String getOrgManagerUserId() {
        return orgManagerUserId;
    }

    @Override
    public String getZipCodeSuffix() {
        return zipCodeSuffix;
    }

    @Override
    public String getBusinessCountryName() {
        return businessCountryName;
    }

    @Override
    public Long getSelectedProfileId() {
        return selectedProfileId;
    }

    @Override
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public void setAlertMethodCd(String alertMethodCd) {
        this.alertMethodCd = alertMethodCd;
    }

    @Override
    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setHrid(String hrid) {
        this.hrid = hrid;
    }

    @Override
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    @Override
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    @Override
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    @Override
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    @Override
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Override
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public void setBusinessCountryCode(String businessCountryCode) {
        this.businessCountryCode = businessCountryCode;
    }

    @Override
    public void setCommandChain(String commandChain) {
        this.commandChain = commandChain;
    }

    @Override
    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public void setLocationClli(String locationClli) {
        this.locationClli = locationClli;
    }

    @Override
    public void setOrgManagerUserId(String orgManagerUserId) {
        this.orgManagerUserId = orgManagerUserId;
    }

    @Override
    public void setZipCodeSuffix(String zipCodeSuffix) {
        this.zipCodeSuffix = zipCodeSuffix;
    }

    @Override
    public void setBusinessCountryName(String businessCountryName) {
        this.businessCountryName = businessCountryName;
    }

    @Override
    public SortedSet<EPRole> getPseudoRoles() {
        return pseudoRoles;
    }

    public void setPseudoRoles(SortedSet<EPRole> pseudoRoles) {
        this.pseudoRoles = pseudoRoles;
    }

    @Override
    public void setSelectedProfileId(Long selectedProfileId) {
        this.selectedProfileId = selectedProfileId;
    }

    @Override
    public Long getTimeZoneId() {
        return timeZoneId;
    }

    @Override
    public void setTimeZoneId(Long timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    @Override
    public String getBusinessUnit() {
        return businessUnit;
    }

    @Override
    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public String getSiloStatus() {
        return siloStatus;
    }

    @Override
    public void setSiloStatus(String siloStatus) {
        this.siloStatus = siloStatus;
    }

    @Override
    public String getCostCenter() {
        return costCenter;
    }

    @Override
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    @Override
    public String getFinancialLocCode() {
        return financialLocCode;
    }

    @Override
    public void setFinancialLocCode(String financialLocCode) {
        this.financialLocCode = financialLocCode;
    }

    @Override
    public String getBusinessUnitName() {
        return businessUnitName;
    }

    @Override
    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    @Override
    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public int compareTo(Object obj) {
        EPUser user = (EPUser) obj;

        String c1 = getLastName() + getFirstName() + getMiddleInitial();
        String c2 = user.getLastName() + user.getFirstName() + user.getMiddleInitial();

        return c1.compareTo(c2);
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String getChatId() {
        return chatId;
    }

    @Override
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public void setPseudoEPRoles(SortedSet<EPRole> pseudoRoles) {
        this.pseudoRoles = pseudoRoles;
    }

    public SortedSet<EPUserApp> getEPUserApps() {
        return userApps;
    }

    public void setEPUserApps(SortedSet<EPUserApp> userApps) {
        this.userApps = userApps;
    }

    public void addUserApp(EPUserApp userApp) {
        userApps.add(userApp);
    }

    public void addAppRoles(EPApp app, SortedSet<EPRole> roles) {
        if (roles != null) {
            // add all
            SortedSet<EPUserApp> userApplications = new TreeSet<>();
            // this.userApps.removeAll(this.userApps);
            Iterator<EPRole> itr = roles.iterator();
            while (itr.hasNext()) {
                EPRole role = itr.next();
                EPUserApp userApp = new EPUserApp();
                userApp.setUserId(this.id);
                userApp.setApp(app);
                userApp.setRole(role);
                userApplications.add(userApp);
            }
            setEPUserApps(userApplications);
        } else {
            // remove all
            setEPUserApps(null);
        }

    }

    public SortedSet<EPRole> getAppEPRoles(EPApp app) {

        logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - app = {}", app.getName());

        SortedSet<EPRole> roles = new TreeSet<>();
        SortedSet<EPUserApp> userAppRoles = getEPUserApps();

        logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - userApps = {} ", userAppRoles.size());

        Iterator<EPUserApp> userAppRolesIterator = userAppRoles.iterator();

        EPUserApp userAppRole = null;
        // getting default app
        while (userAppRolesIterator.hasNext()) {
            EPUserApp tempUserApp = userAppRolesIterator.next();
            if (tempUserApp.getApp().getId().equals(app.getId())) {

                logger.debug(EELFLoggerDelegate.debugLogger,
                        "In EPUser.getAppEPRoles() - for user {}, found application {}", this.getFullName(),
                        app.getName());

                userAppRole = tempUserApp;

                EPRole role = userAppRole.getRole();
                if (role.getActive()) {
                    logger.debug(EELFLoggerDelegate.debugLogger,
                            "In EPUser.getAppEPRoles() - Role {} is active - adding for user {} and app {}",
                            role.getName(), this.getFullName(), app.getName());
                    roles.add((EPRole) role);
                } else {
                    logger.debug(EELFLoggerDelegate.debugLogger,
                            "In EPUser.getAppEPRoles() - Role {} is NOT active - NOT adding for user {} and app {}",
                            role.getName(), this.getFullName(), app.getName());
                }
            }
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - roles = {}", roles.size());

        return roles;
    }

    /**
     * Attention! Not for use in ONAP
     */
    public SortedSet<EPRole> getAppRoles(EPApp app) {
        SortedSet<EPRole> roles = new TreeSet<>();
        SortedSet<EPUserApp> apps = getEPUserApps();
        Iterator<EPUserApp> appsItr = apps.iterator();
        EPUserApp userApp = null;
        // getting default app
        while (appsItr.hasNext()) {
            EPUserApp tempUserApp = appsItr.next();
            if (tempUserApp.getApp().getId().equals(app.getId())) {
                userApp = tempUserApp;
                roles.add((EPRole) userApp.getRole());
            }
        }
        return roles;
    }

    /**
     * Attention! Not for use in ONAP
     */
    public SortedSet<EPRole> getEPRoles() {
        EPApp app = new EPApp();
        app.setId(PortalConstants.PORTAL_APP_ID);
        app.setName(ECOMP_PORTAL_NAME);
        return getAppEPRoles(app);
    }

    /**
     * Attention! Not for use in ONAP
     */
    public void setEPRoles(SortedSet<EPRole> roles) {
        EPApp app = new EPApp();
        app.setId(PortalConstants.PORTAL_APP_ID);
        app.setName(ECOMP_PORTAL_NAME);
        addAppRoles(app, roles);
    }

    /**
     * Attention! Not for use in ONAP
     */
    public void removeEPRole(Long roleId) {
        SortedSet<EPUserApp> apps = getEPUserApps();
        Iterator<EPUserApp> appsItr = apps.iterator();
        // getting default app
        while (appsItr.hasNext()) {
            EPUserApp tempUserApp = appsItr.next();
            if (tempUserApp.getAppId().equals(PortalConstants.PORTAL_APP_ID)
                    && tempUserApp.getRole().getId().equals(roleId)) {
                appsItr.remove();
            }
        }
    }

    /**
     * Attention! Not for use in ONAP
     */
    public void addEPRole(EPRole role) {
        if (role != null) {
            SortedSet<EPRole> roles = getEPRoles();
            if (roles == null) {
                roles = new TreeSet<>();
            }
            roles.add(role);
            setEPRoles(roles);
        }
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

    public boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
    }

    @Override
    public String toString() {
        return "EPUser [orgId=" + orgId + ", managerId=" + managerId + ", firstName=" + firstName + ", middleInitial="
                + middleInitial + ", lastName=" + lastName + ", phone=" + phone + ", fax=" + fax + ", cellular="
                + cellular + ", email=" + email + ", addressId=" + addressId + ", alertMethodCd=" + alertMethodCd
                + ", hrid=" + hrid + ", orgUserId=" + orgUserId + ", orgCode=" + orgCode + ", address1=" + address1
                + ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode
                + ", country=" + country + ", orgManagerUserId=" + orgManagerUserId + ", locationClli=" + locationClli
                + ", businessCountryCode=" + businessCountryCode + ", businessCountryName=" + businessCountryName
                + ", businessUnit=" + businessUnit + ", businessUnitName=" + businessUnitName + ", department="
                + department + ", departmentName=" + departmentName + ", companyCode=" + companyCode + ", company="
                + company + ", zipCodeSuffix=" + zipCodeSuffix + ", jobTitle=" + jobTitle + ", commandChain="
                + commandChain + ", siloStatus=" + siloStatus + ", costCenter=" + costCenter + ", financialLocCode="
                + financialLocCode + ", loginId=" + loginId + ", loginPwd=" + loginPwd + ", lastLoginDate="
                + lastLoginDate + ", active=" + active + ", internal=" + internal + ", selectedProfileId="
                + selectedProfileId + ", timeZoneId=" + timeZoneId + ", online=" + online + ", chatId=" + chatId
                + ", isGuest=" + isGuest + ", userApps=" + userApps + ", pseudoRoles=" + pseudoRoles + "]";
    }

}
