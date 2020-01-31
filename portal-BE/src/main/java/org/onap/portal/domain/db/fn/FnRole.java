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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.db.ep.EpAppRoleFunction;
import org.onap.portal.domain.db.ep.EpRoleNotification;
import org.onap.portal.domain.db.ep.EpUserRolesRequestDet;
import org.onap.portal.domain.db.ep.EpWidgetCatalogRole;
import org.onap.portal.domain.dto.transport.Role;

/*
CREATE TABLE `fn_role` (
        `role_id` int(11) NOT NULL AUTO_INCREMENT,
        `role_name` varchar(300) NOT NULL,
        `active_yn` varchar(1) NOT NULL DEFAULT 'y',
        `priority` decimal(4,0) DEFAULT NULL,
        `app_id` int(11) DEFAULT NULL,
        `app_role_id` int(11) DEFAULT NULL,
        PRIMARY KEY (`role_id`),
        UNIQUE KEY `fn_role_name_app_id_idx` (`role_name`,`app_id`) USING BTREE
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "FnRole.retrieveAppRolesByRoleNameAndByAppId",
        query = "FROM FnRole where roleName =:roleName and appId =:appId"),
    @NamedQuery(
        name = "FnRole.retrieveAppRolesByAppId",
        query = "FROM FnRole where appId =:appId"),
    @NamedQuery(
        name = "FnRole.retrieveAppRolesWhereAppIdIsNull",
        query = "FROM FnRole where appId is null"),
    @NamedQuery(
        name = "FnRole.retrieveAppRoleByRoleIdWhereAppIdIsNull",
        query = "FROM FnRole where id =:roleId and appId is null"),
    @NamedQuery(
        name = "FnRole.retrieveAppRoleByAppRoleIdAndByAppId",
        query = "FROM FnRole where appRoleId =:appRoleId and appId =:appId"),
    @NamedQuery(
        name = "FnRole.retrieveAppRoleByRoleIdAndAppId",
        query = "FROM FnRole where id =:roleId and appId =:appId"),
    @NamedQuery(
        name = "FnRole.retrieveAppRolesByRoleNameAndWhereAppIdIsNull",
        query = "FROM FnRole where roleName =:roleName and appId is null"),
    @NamedQuery(
        name = "FnRole.retrieveActiveRolesOfApplication",
        query = "from FnRole where activeYn = 'Y' and appId=:appId"),
    @NamedQuery(
        name = "FnRole.getUserRoleOnUserIdAndAppId",
        query = " FROM"
            + "  FnRole fr,\n"
            + "  FnUserRole fur\n"
            + " WHERE\n"
            + "  fr.id = fur.roleId\n"
            + "  AND fur.userId = :userId"
            + "  AND fur.fnAppId.id = :appId\n"
            + "  AND fr.activeYn = 'y'"),
    @NamedQuery(
        name = "FnRole.getGlobalRolesOfPortal",
        query = "from"
            + "  FnRole"
            + " where"
            + "  roleName like 'global_%'"
            + "  and appId is null"
            + "  and activeYn = 'Y'"),
    @NamedQuery(
        name = "FnRole.getSysAdminRoleId",
        query = "FROM FnRole WHERE roleName = 'System_Administrator' and activeYn = 'true' and priority = 1 and appId is null and appRoleId is null"
    )
})

@Table(name = "fn_role")
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FnRole extends Role {

  @Column(name = "app_role_id", length = 11, columnDefinition = "int(11) default null")
  @Digits(integer = 11, fraction = 0)
  private Long appRoleId;
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinTable(
      name = "fn_user_pseudo_role",
      joinColumns = {@JoinColumn(name = "pseudo_role_Id", referencedColumnName = "id", columnDefinition = "bigint not null")},
      inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "bigint not null")},
      indexes = {
          @Index(name = "fk_pseudo_role_user_id", columnList = "user_id")
      }
  )
  private Set<FnUser> fnUsers;
  @ManyToMany(cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY, mappedBy = "role")
  private Set<FnRoleFunction> roleFunctions;
  @OneToMany(
      targetEntity = EpRoleNotification.class,
      mappedBy = "notificationId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpRoleNotification> epRoleNotifications;
  @OneToMany(
      targetEntity = FnMenuFunctionalRoles.class,
      mappedBy = "roleId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnMenuFunctionalRoles> fnMenuFunctionalRoles;
  @OneToMany(
      targetEntity = EpWidgetCatalogRole.class,
      mappedBy = "roleId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpWidgetCatalogRole> epWidgetCatalogRoles;
  @OneToMany(
      targetEntity = EpAppRoleFunction.class,
      mappedBy = "fnRole",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpAppRoleFunction> epAppRoleFunctions;
  @OneToMany(
      targetEntity = EpUserRolesRequestDet.class,
      mappedBy = "requestedRoleId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpUserRolesRequestDet> epUserRolesRequestDets;
  @OneToMany(
      targetEntity = FnUserRole.class,
      mappedBy = "roleId",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnUserRole> fnUserRoles;

  @Builder
  public FnRole(@Digits(integer = 11, fraction = 0) Long id, LocalDateTime created,
      LocalDateTime modified, Long rowNum, Serializable auditUserId,
      DomainVo createdId, DomainVo modifiedId,
      Set<DomainVo> fnUsersCreatedId,
      Set<DomainVo> fnUsersModifiedId,
      @Size(max = 300) @NotNull @SafeHtml String roleName,
      @Digits(integer = 11, fraction = 0) Long appId, @NotNull Boolean activeYn,
      @Digits(integer = 4, fraction = 0) Integer priority,
      Set<FnRoleFunction> fnRoleFunctions, Set<FnRoleComposite> childRoles,
      Set<FnRoleComposite> parentRoles,
      @Digits(integer = 11, fraction = 0) Long appRoleId, Set<FnUser> fnUsers,
      Set<FnRoleFunction> roleFunctions,
      Set<EpRoleNotification> epRoleNotifications,
      Set<FnMenuFunctionalRoles> fnMenuFunctionalRoles,
      Set<EpWidgetCatalogRole> epWidgetCatalogRoles,
      Set<EpAppRoleFunction> epAppRoleFunctions,
      Set<EpUserRolesRequestDet> epUserRolesRequestDets,
      Set<FnUserRole> fnUserRoles) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId,
        roleName, appId, activeYn, priority, fnRoleFunctions, childRoles, parentRoles);
    this.appRoleId = appRoleId;
    this.fnUsers = fnUsers;
    this.roleFunctions = roleFunctions;
    this.epRoleNotifications = epRoleNotifications;
    this.fnMenuFunctionalRoles = fnMenuFunctionalRoles;
    this.epWidgetCatalogRoles = epWidgetCatalogRoles;
    this.epAppRoleFunctions = epAppRoleFunctions;
    this.epUserRolesRequestDets = epUserRolesRequestDets;
    this.fnUserRoles = fnUserRoles;
  }
}
