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
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
import org.onap.portal.domain.db.cr.CrReportFileHistory;
import org.onap.portal.domain.db.ep.EpPersUserWidgetPlacement;
import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.onap.portal.domain.db.ep.EpUserNotification;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.dto.DomainVo;
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
        @Index(name = "fk_fn_user_ref_197_fn_user", columnList = "manager_id"),
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SequenceGenerator(name = "seq", initialValue = 1000, allocationSize = 100000)
public class FnUser extends DomainVo implements UserDetails, Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
       @Column(name = "user_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long userId;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "org_id")
       private FnOrg orgId;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "manager_id")
       private FnUser managerId;
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
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "created_id")
       private FnUser createdId;
       @Column(name = "created_date", columnDefinition = "datetime DEFAULT current_timestamp()", nullable = false)
       @PastOrPresent
       protected LocalDateTime createdDate;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "modified_id")
       private FnUser modifiedId;
       @Column(name = "modified_date", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       protected LocalDateTime modifiedDate;
       @Column(name = "is_internal_yn", nullable = false, columnDefinition = "bit DEFAULT 0")
       private Boolean isInternalYn;
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
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "language_id", nullable = false, columnDefinition = "int(11) DEFAULT 1")
       @NotNull(message = "languageId must not be null")
       private FnLanguage languageId;
       @Column(name = "is_guest", nullable = false, columnDefinition = "bit DEFAULT 0")
       @NotNull(message = "guest must not be null")
       private Boolean guest;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fnUserList")
       private Set<CrReportFileHistory> crReportFileHistorie;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
       private Set<FnRole> fnRoles;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       private Set<FnMenuFunctional> fnRoleList;
       @OneToMany(
               targetEntity = FnAuditLog.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnAuditLog> fnAuditLogs;
       @OneToMany(
               targetEntity = FnUser.class,
               mappedBy = "createdId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUser> fnUsersCreatedId;
       @OneToMany(
               targetEntity = FnUser.class,
               mappedBy = "managerId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUser> fnUsersManagerId;
       @OneToMany(
               targetEntity = FnUser.class,
               mappedBy = "modifiedId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUser> fnUsersModifiedId;
       @OneToMany(
               targetEntity = EpUserRolesRequest.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpUserRolesRequest> epUserRolesRequests;
       @OneToMany(
               targetEntity = FnPersUserAppSel.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnPersUserAppSel> persUserAppSels;
       @OneToMany(
               targetEntity = EpWidgetCatalogParameter.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpWidgetCatalogParameter> epWidgetCatalogParameters;
       @OneToMany(
               targetEntity = EpPersUserWidgetPlacement.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements;
       @OneToMany(
               targetEntity = EpPersUserWidgetSel.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpPersUserWidgetSel> epPersUserWidgetSels;
       @OneToMany(
               targetEntity = FnUserRole.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUserRole> fnUserRoles;
       @OneToMany(
               targetEntity = EpUserNotification.class,
               mappedBy = "userId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpUserNotification> epUserNotifications;

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

}
