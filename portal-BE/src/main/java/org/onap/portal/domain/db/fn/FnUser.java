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

package org.onap.portal.domain.db.fn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.db.cr.CrReportFileHistory;
import org.onap.portal.domain.db.ep.EpPersUserWidgetPlacement;
import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.onap.portal.domain.db.ep.EpUserNotification;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/*
CREATE TABLE `fn_user` (
        `user_id` int(11) NOT NULL AUTO_INCREMENT,
        `org_id` int(11) DEFAULT NULL,
        `manager_id` int(11) DEFAULT NULL,
        `first_name` varchar(50) DEFAULT NULL,
        `middle_name` varchar(50) DEFAULT NULL,
        `last_name` varchar(50) DEFAULT NULL,
        `phone` varchar(25) DEFAULT NULL,
        `fax` varchar(25) DEFAULT NULL,
        `cellular` varchar(25) DEFAULT NULL,
        `email` varchar(50) DEFAULT NULL,
        `address_id` decimal(11,0) DEFAULT NULL,
        `alert_method_cd` varchar(10) DEFAULT NULL,
        `hrid` varchar(20) DEFAULT NULL,
        `org_user_id` varchar(20) DEFAULT NULL,
        `org_code` varchar(30) DEFAULT NULL,
        `login_id` varchar(25) DEFAULT NULL,
        `login_pwd` varchar(100) DEFAULT NULL,
        `last_login_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `active_yn` varchar(1) NOT NULL DEFAULT 'y',
        `created_id` int(11) DEFAULT NULL,
        `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `modified_id` int(11) DEFAULT NULL,
        `modified_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `is_internal_yn` char(1) NOT NULL DEFAULT 'n',
        `address_line_1` varchar(100) DEFAULT NULL,
        `address_line_2` varchar(100) DEFAULT NULL,
        `city` varchar(50) DEFAULT NULL,
        `state_cd` varchar(3) DEFAULT NULL,
        `zip_code` varchar(11) DEFAULT NULL,
        `country_cd` varchar(3) DEFAULT NULL,
        `location_clli` varchar(8) DEFAULT NULL,
        `org_manager_userid` varchar(20) DEFAULT NULL,
        `company` varchar(100) DEFAULT NULL,
        `department_name` varchar(100) DEFAULT NULL,
        `job_title` varchar(100) DEFAULT NULL,
        `timezone` int(11) DEFAULT NULL,
        `department` varchar(25) DEFAULT NULL,
        `business_unit` varchar(25) DEFAULT NULL,
        `business_unit_name` varchar(100) DEFAULT NULL,
        `cost_center` varchar(25) DEFAULT NULL,
        `fin_loc_code` varchar(10) DEFAULT NULL,
        `silo_status` varchar(10) DEFAULT NULL,
        `language_id` int(2) NOT NULL DEFAULT 1,
        PRIMARY KEY (`user_id`),
        UNIQUE KEY `fn_user_hrid` (`hrid`) USING BTREE,
        UNIQUE KEY `fn_user_login_id` (`login_id`) USING BTREE,
        KEY `fn_user_address_id` (`address_id`) USING BTREE,
        KEY `fn_user_alert_method_cd` (`alert_method_cd`) USING BTREE,
        KEY `fn_user_org_id` (`org_id`) USING BTREE,
        KEY `fk_fn_user_ref_197_fn_user` (`manager_id`),
        KEY `fk_fn_user_ref_198_fn_user` (`created_id`),
        KEY `fk_fn_user_ref_199_fn_user` (`modified_id`),
        KEY `fk_timezone` (`timezone`),
        CONSTRAINT `fk_fn_user_ref_110_fn_org` FOREIGN KEY (`org_id`) REFERENCES `fn_org` (`org_id`),
        CONSTRAINT `fk_fn_user_ref_123_fn_lu_al` FOREIGN KEY (`alert_method_cd`) REFERENCES `fn_lu_alert_method` (`alert_method_cd`),
        CONSTRAINT `fk_fn_user_ref_197_fn_user` FOREIGN KEY (`manager_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `fk_fn_user_ref_198_fn_user` FOREIGN KEY (`created_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `fk_fn_user_ref_199_fn_user` FOREIGN KEY (`modified_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `fk_timezone` FOREIGN KEY (`timezone`) REFERENCES `fn_lu_timezone` (`timezone_id`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "FnUser.getUserWithOrgUserId",
        query = "FROM FnUser WHERE orgUserId = :orgId"),
    @NamedQuery(
        name = "FnUser.findByLoginId",
        query = "FROM FnUser WHERE loginId = :loginId"),
    @NamedQuery(
        name = "FnUser.getActiveUsers",
        query = "FROM FnUser WHERE activeYn = 'Y'"),
    @NamedQuery(
        name = "FnUser.getUsersByOrgIds",
        query = "FROM FnUser WHERE orgUserId IN :orgIds"
    )
})

@Table(name = "fn_user", indexes = {
    @Index(name = "fn_user_address_id", columnList = "address_id"),
    @Index(name = "fn_user_alert_method_cd", columnList = "alert_method_cd"),
    @Index(name = "fn_user_org_id", columnList = "org_id"),
//    @Index(name = "fk_fn_user_ref_197_fn_user", columnList = "manager_id"),
    @Index(name = "fk_fn_user_ref_198_fn_user", columnList = "created_id"),
    @Index(name = "fk_fn_user_ref_199_fn_user", columnList = "modified_id"),
    @Index(name = "fk_timezone", columnList = "timezone")
},
    uniqueConstraints = {
        @UniqueConstraint(name = "fn_user_hrid", columnNames = "hrid"),
        @UniqueConstraint(name = "fn_user_login_id", columnNames = "login_id")

    })

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class FnUser extends DomainVo implements UserDetails, Serializable {

  private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnUser.class);

/*  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id")
  private FnUser managerId;*/
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "org_id")
  private FnOrg orgId;
  @Column(name = "first_name", length = 50)
  @Size(max = 50)
  @SafeHtml
  private String firstName;
  @Column(name = "middle_name", length = 50)
  @Size(max = 50)
  @SafeHtml
  private String middleName;
  @Column(name = "last_name", length = 50)
  @Size(max = 50)
  @SafeHtml
  private String lastName;
  @Column(name = "phone", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String phone;
  @Column(name = "fax", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String fax;
  @Column(name = "cellular", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String cellular;
  @Column(name = "email", length = 50)
  @Size(max = 50)
  @Email
  @SafeHtml
  private String email;
  @Column(name = "address_id")
  @Digits(integer = 11, fraction = 0)
  private Long addressId;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "alert_method_cd")
  private FnLuAlertMethod alertMethodCd;
  @Column(name = "hrid", length = 20)
  @Size(max = 20)
  @SafeHtml
  private String hrid;
  @Column(name = "org_user_id", length = 20)
  @Size(max = 20)
  @SafeHtml
  private String orgUserId;
  @Column(name = "org_code", length = 30)
  @Size(max = 30)
  @SafeHtml
  private String org_code;
  @Column(name = "login_id", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String loginId;
  @Column(name = "login_pwd", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String loginPwd;
  @Column(name = "last_login_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()")
  @PastOrPresent
  protected LocalDateTime lastLoginDate;
  @Column(name = "active_yn", nullable = false)
  private Boolean activeYn;
  @Column(name = "created_date", columnDefinition = "datetime DEFAULT current_timestamp()", nullable = false)
  @PastOrPresent
  protected LocalDateTime createdDate;
  @Column(name = "modified_date", nullable = false, columnDefinition = "datetime default now()")
  @PastOrPresent
  protected LocalDateTime modifiedDate;
  @Column(name = "is_internal_yn", nullable = false, columnDefinition = "boolean DEFAULT false")
  private Boolean isInternalYn = false;
  @Column(name = "is_system_user", nullable = false, columnDefinition = "boolean DEFAULT false")
  private Boolean isSystemUser = false;
  @Column(name = "address_line_1", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String addressLine1;
  @Column(name = "address_line_2", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String addressLine2;
  @Column(name = "city", length = 50)
  @Size(max = 50)
  @SafeHtml
  private String city;
  @Column(name = "state_cd", length = 3)
  @Size(max = 3)
  @SafeHtml
  private String stateCd;
  @Column(name = "zip_code", length = 11)
  @Size(max = 11)
  @SafeHtml
  private String zipCode;
  @Column(name = "country_cd", length = 3)
  @Size(max = 3)
  @SafeHtml
  private String countryCd;
  @Column(name = "location_clli", length = 8)
  @Size(max = 8)
  @SafeHtml
  private String locationClli;
  @Column(name = "org_manager_userid", length = 20)
  @Size(max = 20)
  @SafeHtml
  private String orgManagerUserId;
  @Column(name = "company", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String company;
  @Column(name = "department_name", length = 200)
  @Size(max = 200)
  @SafeHtml
  private String departmentName;
  @Column(name = "job_title", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String jobTitle;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "timezone")
  private FnLuTimezone timezone;
  @Column(name = "department", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String department;
  @Column(name = "business_unit", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String businessUnit;
  @Column(name = "business_unit_name", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String businessUnitName;
  @Column(name = "cost_center", length = 25)
  @Size(max = 25)
  @SafeHtml
  private String cost_center;
  @Column(name = "fin_loc_code", length = 10)
  @Size(max = 10)
  @SafeHtml
  private String finLocCode;
  @Column(name = "silo_status", length = 10)
  @Size(max = 10)
  @SafeHtml
  private String siloStatus;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "language_id", nullable = false)
  @NotNull(message = "languageId must not be null")
  private FnLanguage languageId;
  @Column(name = "is_guest", nullable = false, columnDefinition = "boolean DEFAULT false")
  @NotNull(message = "guest must not be null")
  private Boolean guest = false;
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "fnUserList")
  private Set<CrReportFileHistory> crReportFileHistorie;
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "fnUsers")
  private Set<FnRole> fnRoles;
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private Set<FnMenuFunctional> fnRoleList;
  @OneToMany(
      targetEntity = FnAuditLog.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnAuditLog> fnAuditLogs;
  @OneToMany(
      targetEntity = EpUserRolesRequest.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpUserRolesRequest> epUserRolesRequests;
  @OneToMany(
      targetEntity = FnPersUserAppSel.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnPersUserAppSel> persUserAppSels;
  @OneToMany(
      targetEntity = EpWidgetCatalogParameter.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpWidgetCatalogParameter> epWidgetCatalogParameters;
  @OneToMany(
      targetEntity = EpPersUserWidgetPlacement.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements;
  @OneToMany(
      targetEntity = EpPersUserWidgetSel.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpPersUserWidgetSel> epPersUserWidgetSels;
  @OneToMany(
      targetEntity = FnUserRole.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnUserRole> userApps;
  @OneToMany(
      targetEntity = EpUserNotification.class,
      mappedBy = "userId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpUserNotification> epUserNotifications;
/*  @OneToMany(
      targetEntity = FnUser.class,
      mappedBy = "managerId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnUser> fnUsersManagerId;*/

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return fnRoles
        .stream()
        .map(fnRole -> new SimpleGrantedAuthority("ROLE_" + fnRole.getRoleName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.getLoginPwd();
  }

  @Override
  public String getUsername() {
    return this.getLoginId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  public SortedSet<FnRole> getAppEPRoles(FnApp app) {

    logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - app = {}", app.getAppName());

    SortedSet<FnRole> roles = new TreeSet<>();
    Set<FnUserRole> userAppRoles = getUserApps();

    logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - userApps = {} ", userAppRoles.size());

    Iterator<FnUserRole> userAppRolesIterator = userAppRoles.iterator();

    FnUserRole userAppRole;
    // getting default app
    while (userAppRolesIterator.hasNext()) {
      FnUserRole tempUserApp = userAppRolesIterator.next();
      if (tempUserApp.getFnAppId().getId().equals(app.getId())) {

        logger.debug(EELFLoggerDelegate.debugLogger,
            "In EPUser.getAppEPRoles() - for user {}, found application {}", this.getFullName(),
            app.getAppName());

        userAppRole = tempUserApp;

        FnRole role = userAppRole.getRoleId();
        if (role.getActiveYn()) {
          logger.debug(EELFLoggerDelegate.debugLogger,
              "In EPUser.getAppEPRoles() - Role {} is active - adding for user {} and app {}",
              role.getRoleName(), this.getFullName(), app.getAppName());
          roles.add(role);
        } else {
          logger.debug(EELFLoggerDelegate.debugLogger,
              "In EPUser.getAppEPRoles() - Role {} is NOT active - NOT adding for user {} and app {}",
              role.getRoleName(), this.getFullName(), app.getAppName());
        }
      }
    }
    logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - roles = {}", roles.size());

    return roles;
  }

  public void setRoles(Set<FnRole> roles) {
    FnApp app = new FnApp();
    app.setId(1L);
    app.setAppName("Default");
    this.addAppRoles(app, roles);
  }

  private void addAppRoles(FnApp app, Set<FnRole> roles) {
    if (roles != null) {
      Set<FnUserRole> newUserApps = new HashSet<>();
      for (FnRole role : roles) {
        FnUserRole userApp = new FnUserRole();
        userApp.setUserId(this);
        userApp.setFnAppId(app);
        userApp.setRoleId(role);
        newUserApps.add(userApp);
      }

      this.setUserApps(newUserApps);
    } else {
      this.userApps.clear();
    }
  }

  @Builder
  public FnUser(@Digits(integer = 11, fraction = 0) Long id, LocalDateTime created, LocalDateTime modified,
      Long rowNum, Serializable auditUserId, DomainVo createdId, DomainVo modifiedId,
      Set<DomainVo> fnUsersCreatedId, Set<DomainVo> fnUsersModifiedId, FnOrg orgId,
      @Size(max = 50) @SafeHtml String firstName,
      @Size(max = 50) @SafeHtml String middleName,
      @Size(max = 50) @SafeHtml String lastName,
      @Size(max = 25) @SafeHtml String phone,
      @Size(max = 25) @SafeHtml String fax,
      @Size(max = 25) @SafeHtml String cellular,
      @Size(max = 50) @Email @SafeHtml String email,
      @Digits(integer = 11, fraction = 0) Long addressId, FnLuAlertMethod alertMethodCd,
      @Size(max = 20) @SafeHtml String hrid,
      @Size(max = 20) @SafeHtml String orgUserId,
      @Size(max = 30) @SafeHtml String org_code,
      @Size(max = 25) @SafeHtml String loginId,
      @Size(max = 100) @SafeHtml String loginPwd,
      @PastOrPresent LocalDateTime lastLoginDate, Boolean activeYn,
      @PastOrPresent LocalDateTime createdDate,
      @PastOrPresent LocalDateTime modifiedDate, Boolean isInternalYn, Boolean isSystemUser,
      @Size(max = 100) @SafeHtml String addressLine1,
      @Size(max = 100) @SafeHtml String addressLine2,
      @Size(max = 50) @SafeHtml String city,
      @Size(max = 3) @SafeHtml String stateCd,
      @Size(max = 11) @SafeHtml String zipCode,
      @Size(max = 3) @SafeHtml String countryCd,
      @Size(max = 8) @SafeHtml String locationClli,
      @Size(max = 20) @SafeHtml String orgManagerUserId,
      @Size(max = 100) @SafeHtml String company,
      @Size(max = 200) @SafeHtml String departmentName,
      @Size(max = 100) @SafeHtml String jobTitle, FnLuTimezone timezone,
      @Size(max = 25) @SafeHtml String department,
      @Size(max = 25) @SafeHtml String businessUnit,
      @Size(max = 100) @SafeHtml String businessUnitName,
      @Size(max = 25) @SafeHtml String cost_center,
      @Size(max = 10) @SafeHtml String finLocCode,
      @Size(max = 10) @SafeHtml String siloStatus,
      @NotNull(message = "languageId must not be null") FnLanguage languageId,
      @NotNull(message = "guest must not be null") Boolean guest,
      Set<CrReportFileHistory> crReportFileHistorie, Set<FnRole> fnRoles,
      Set<FnMenuFunctional> fnRoleList, Set<FnAuditLog> fnAuditLogs,
      Set<EpUserRolesRequest> epUserRolesRequests,
      Set<FnPersUserAppSel> persUserAppSels,
      Set<EpWidgetCatalogParameter> epWidgetCatalogParameters,
      Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements,
      Set<EpPersUserWidgetSel> epPersUserWidgetSels, Set<FnUserRole> userApps,
      Set<EpUserNotification> epUserNotifications) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId);
    this.orgId = orgId;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.phone = phone;
    this.fax = fax;
    this.cellular = cellular;
    this.email = email;
    this.addressId = addressId;
    this.alertMethodCd = alertMethodCd;
    this.hrid = hrid;
    this.orgUserId = orgUserId;
    this.org_code = org_code;
    this.loginId = loginId;
    this.loginPwd = loginPwd;
    this.lastLoginDate = lastLoginDate;
    this.activeYn = activeYn;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    this.isInternalYn = isInternalYn;
    this.isSystemUser = isSystemUser;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.city = city;
    this.stateCd = stateCd;
    this.zipCode = zipCode;
    this.countryCd = countryCd;
    this.locationClli = locationClli;
    this.orgManagerUserId = orgManagerUserId;
    this.company = company;
    this.departmentName = departmentName;
    this.jobTitle = jobTitle;
    this.timezone = timezone;
    this.department = department;
    this.businessUnit = businessUnit;
    this.businessUnitName = businessUnitName;
    this.cost_center = cost_center;
    this.finLocCode = finLocCode;
    this.siloStatus = siloStatus;
    this.languageId = languageId;
    this.guest = guest;
    this.crReportFileHistorie = crReportFileHistorie;
    this.fnRoles = fnRoles;
    this.fnRoleList = fnRoleList;
    this.fnAuditLogs = fnAuditLogs;
    this.epUserRolesRequests = epUserRolesRequests;
    this.persUserAppSels = persUserAppSels;
    this.epWidgetCatalogParameters = epWidgetCatalogParameters;
    this.epPersUserWidgetPlacements = epPersUserWidgetPlacements;
    this.epPersUserWidgetSels = epPersUserWidgetSels;
    this.userApps = userApps;
    this.epUserNotifications = epUserNotifications;
  }
}
