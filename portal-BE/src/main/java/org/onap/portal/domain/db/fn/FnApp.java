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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.ep.EpAppRoleFunction;
import org.onap.portal.domain.db.ep.EpMicroservice;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpWebAnalyticsSource;
import org.onap.portal.domain.db.ep.EpWidgetCatalogRole;
import org.onap.portal.domain.db.DomainVo;

/*
CREATE TABLE `fn_app` (
        `app_id` int(11) NOT NULL AUTO_INCREMENT,
        `app_name` varchar(100) NOT NULL DEFAULT '?',
        `app_image_url` varchar(256) DEFAULT NULL,
        `app_description` varchar(512) DEFAULT NULL,
        `app_notes` varchar(4096) DEFAULT NULL,
        `app_url` varchar(256) DEFAULT NULL,
        `app_alternate_url` varchar(256) DEFAULT NULL,
        `app_rest_endpoint` varchar(2000) DEFAULT NULL,
        `ml_app_name` varchar(50) NOT NULL DEFAULT '?',
        `ml_app_admin_id` varchar(7) NOT NULL DEFAULT '?',
        `mots_id` int(11) DEFAULT NULL,
        `app_password` varchar(256) NOT NULL DEFAULT '?',
        `open` char(1) DEFAULT 'N',
        `enabled` char(1) DEFAULT 'Y',
        `thumbnail` mediumblob DEFAULT NULL,
        `app_username` varchar(50) DEFAULT NULL,
        `ueb_key` varchar(256) DEFAULT NULL,
        `ueb_secret` varchar(256) DEFAULT NULL,
        `ueb_topic_name` varchar(256) DEFAULT NULL,
        `app_type` int(11) NOT NULL DEFAULT 1,
        `auth_central` char(1) NOT NULL DEFAULT 'N',
        `auth_namespace` varchar(100) DEFAULT NULL,
        PRIMARY KEY (`app_id`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "FnApp.retrieveWhereAuthCentralIsYAndOpenIsNAndAuthNamespaceIsNotNull",
        query = "from FnApp where authCentral = 'Y' and open = 'N' and authNamespace is not null"),
    @NamedQuery(
        name = "FnApp.getByUebKey",
        query = "from FnApp where uebKey = :uebKey"),
    @NamedQuery(
        name = "FnApp.getCentralizedApps",
        query = "from FnApp where authCentral = 'Y' and open = 'N' and authNamespace is not null"),
    @NamedQuery(
        name = "FnApp.retrieveWhereAppName",
        query = "FROM FnApp WHERE appName = :appName"
    )
})

//TODO appName as unique index?

@Table(name = "fn_app")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
public class FnApp extends DomainVo implements Serializable {

  @Column(name = "app_name", length = 100, nullable = false, columnDefinition = "varchar(100) not null default '?'")
  @Size(max = 100)
  @SafeHtml
  @NotNull
  private String appName = "?";
  @Column(name = "app_image_url", length = 256)
  @Size(max = 256)
  @SafeHtml
  private String appImageUrl;
  @Column(name = "app_description", length = 512)
  @Size(max = 256)
  @SafeHtml
  private String appDescription;
  @Column(name = "app_notes", length = 4096)
  @Size(max = 4096)
  @SafeHtml
  private String appNotes;
  @Column(name = "app_url", length = 256)
  @Size(max = 256)
  @SafeHtml
  //TODO URL
  @URL
  private String appUrl;
  @Column(name = "app_alternate_url", length = 256)
  @Size(max = 256)
  @SafeHtml
  private String appAlternateUrl;
  @Column(name = "app_rest_endpoint", length = 2000)
  @Size(max = 2000)
  @SafeHtml
  private String appRestEndpoint;
  @Column(name = "ml_app_name", length = 50, nullable = false, columnDefinition = "varchar(50) not null default '?'")
  @Size(max = 50)
  @SafeHtml
  @NotNull
  private String mlAppName = "?";
  @Column(name = "ml_app_admin_id", length = 7, nullable = false, columnDefinition = "varchar(7) not null default '?'")
  @Size(max = 7)
  @SafeHtml
  private String mlAppAdminId = "?";
  @Column(name = "mots_id", length = 11)
  @Digits(integer = 11, fraction = 0)
  private Long motsId;
  @Column(name = "app_password", length = 256, nullable = false, columnDefinition = "varchar(256) not null default '?'")
  @Size(max = 256)
  @SafeHtml
  @NotNull
  private String appPassword = "?";
  @Column(name = "open")
  private Boolean open = false;
  @Column(name = "enabled")
  private Boolean enabled = false;
  @Column(name = "active_yn")
  @NotNull
  private Boolean activeYn = true;
  @Column(name = "_thumbnail", columnDefinition = "mediumblob null default null")
  private byte[] thumbnail;
  @Column(name = "app_username", length = 50)
  @Size(max = 50)
  @SafeHtml
  private String appUsername;
  @Column(name = "ueb_key", length = 256)
  @Size(max = 256)
  @SafeHtml
  private String uebKey;
  @Column(name = "ueb_secret", length = 256)
  @Size(max = 256)
  @SafeHtml
  private String uebSecret;
  @Column(name = "ueb_topic_name", length = 256)
  @Size(max = 256)
  @SafeHtml
  private String uebTopicName;
  @Column(name = "app_type", length = 11, columnDefinition = "int(11) not null default 1")
  @Digits(integer = 11, fraction = 0)
  private Long appType = 1L;
  @Column(name = "auth_central", length = 1, nullable = false)
  private Boolean authCentral;
  @Column(name = "auth_namespace", length = 100)
  @Size(max = 100)
  @SafeHtml
  private String authNamespace;
  @OneToMany(
      targetEntity = FnMenuFunctionalRoles.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnMenuFunctionalRoles> fnMenuFunctionalRoles;
  @OneToMany(
      targetEntity = EpUserRolesRequest.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpUserRolesRequest> epUserRolesRequests;
  @OneToMany(
      targetEntity = EpAppFunction.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpAppFunction> epAppFunctions;
  @OneToMany(
      targetEntity = EpAppRoleFunction.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpAppRoleFunction> epAppRoleFunctions;
  @OneToMany(
      targetEntity = FnUserRole.class,
      mappedBy = "fnAppId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnUserRole> fnUserRoles;
  @OneToMany(
      targetEntity = EpWebAnalyticsSource.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpWebAnalyticsSource> epWebAnalyticsSources;
  @OneToMany(
      targetEntity = EpWidgetCatalogRole.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpWidgetCatalogRole> epWidgetCatalogRoles;
  @OneToMany(
      targetEntity = EpMicroservice.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpMicroservice> epMicroservices;
  @OneToMany(
      targetEntity = FnPersUserAppSel.class,
      mappedBy = "appId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnPersUserAppSel> fnPersUserAppSels;

  public Boolean isRestrictedApp() {
    return (this.appType == 2);
  }

  @Builder
  public FnApp(@Digits(integer = 11, fraction = 0) Long id, LocalDateTime created,
      LocalDateTime modified, Long rowNum, Serializable auditUserId,
      DomainVo createdId, DomainVo modifiedId, Set<DomainVo> fnUsersCreatedId,
      Set<DomainVo> fnUsersModifiedId,
      @Size(max = 100) @SafeHtml @NotNull String appName,
      @Size(max = 256) @SafeHtml String appImageUrl,
      @Size(max = 256) @SafeHtml String appDescription,
      @Size(max = 4096) @SafeHtml String appNotes,
      @Size(max = 256) @SafeHtml @URL String appUrl,
      @Size(max = 256) @SafeHtml String appAlternateUrl,
      @Size(max = 2000) @SafeHtml String appRestEndpoint,
      @Size(max = 50) @SafeHtml @NotNull String mlAppName,
      @Size(max = 7) @SafeHtml @NotNull String mlAppAdminId,
      @Digits(integer = 11, fraction = 0) Long motsId,
      @Size(max = 256) @SafeHtml @NotNull String appPassword, Boolean open, Boolean enabled, Boolean activeYn, byte[] thumbnail,
      @Size(max = 50) @SafeHtml String appUsername,
      @Size(max = 256) @SafeHtml String uebKey,
      @Size(max = 256) @SafeHtml String uebSecret,
      @Size(max = 256) @SafeHtml String uebTopicName,
      @Digits(integer = 11, fraction = 0) Long appType, Boolean authCentral,
      @Size(max = 100) @SafeHtml String authNamespace,
      Set<FnMenuFunctionalRoles> fnMenuFunctionalRoles,
      Set<EpUserRolesRequest> epUserRolesRequests,
      Set<EpAppFunction> epAppFunctions, Set<EpAppRoleFunction> epAppRoleFunctions,
      Set<FnUserRole> fnUserRoles, Set<EpWebAnalyticsSource> epWebAnalyticsSources,
      Set<EpWidgetCatalogRole> epWidgetCatalogRoles,
      Set<EpMicroservice> epMicroservices, Set<FnPersUserAppSel> fnPersUserAppSels) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId);
    this.appName = appName;
    this.appImageUrl = appImageUrl;
    this.appDescription = appDescription;
    this.appNotes = appNotes;
    this.appUrl = appUrl;
    this.appAlternateUrl = appAlternateUrl;
    this.appRestEndpoint = appRestEndpoint;
    this.mlAppName = mlAppName;
    this.mlAppAdminId = mlAppAdminId;
    this.motsId = motsId;
    this.appPassword = appPassword;
    this.open = open;
    this.enabled = enabled;
    this.activeYn = activeYn;
    this.thumbnail = thumbnail;
    this.appUsername = appUsername;
    this.uebKey = uebKey;
    this.uebSecret = uebSecret;
    this.uebTopicName = uebTopicName;
    this.appType = appType;
    this.authCentral = authCentral;
    this.authNamespace = authNamespace;
    this.fnMenuFunctionalRoles = fnMenuFunctionalRoles;
    this.epUserRolesRequests = epUserRolesRequests;
    this.epAppFunctions = epAppFunctions;
    this.epAppRoleFunctions = epAppRoleFunctions;
    this.fnUserRoles = fnUserRoles;
    this.epWebAnalyticsSources = epWebAnalyticsSources;
    this.epWidgetCatalogRoles = epWidgetCatalogRoles;
    this.epMicroservices = epMicroservices;
    this.fnPersUserAppSels = fnPersUserAppSels;
  }
}
