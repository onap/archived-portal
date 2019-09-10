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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.ep.EpAppRoleFunction;
import org.onap.portal.domain.db.ep.EpRoleNotification;
import org.onap.portal.domain.db.ep.EpUserRolesRequestDet;
import org.onap.portal.domain.db.ep.EpWidgetCatalogRole;
import org.onap.portal.domain.dto.DomainVo;

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
                query = "FROM FnRole where role_name =:roleName and app_id =:appId"),
        @NamedQuery(
                name = "FnRole.retrieveAppRolesByAppId",
                query = "FROM FnRole where app_id =:appId"),
        @NamedQuery(
                name = "FnRole.retrieveAppRolesWhereAppIdIsNull",
                query = "FROM FnRole where app_id is null"),
        @NamedQuery(
                name = "FnRole.retrieveAppRoleByRoleIdWhereAppIdIsNull",
                query = "FROM FnRole where role_id =:roleId and app_id is null"),
        @NamedQuery(
                name = "FnRole.retrieveAppRoleByAppRoleIdAndByAppId",
                query = "FROM FnRole where app_role_id =:appRoleId and app_id =:appId"),
        @NamedQuery(
                name = "FnRole.retrieveAppRoleByRoleIdAndAppId",
                query = "FROM FnRole where role_id =:roleId and app_id =:appId"),
        @NamedQuery(
                name = "FnRole.retrieveAppRolesByRoleNameAndWhereAppIdIsNull",
                query = "FROM FnRole where role_name =:roleName and app_id is null"),
        @NamedQuery(
                name = "FnRole.retrieveActiveRolesOfApplication",
                query = "from FnRole where active_yn = 'Y' and app_id=:appId"),
        @NamedQuery(name = "FnRole.retrieveRoleToUpdateInExternalAuthSystem",
                query = "FROM FnRole where role_name =:roleName and app_id =:appId")
})

@Table(name = "fn_role", indexes = {
        @Index(name = "fn_role_name_app_id_idx", columnList = "role_name, app_id", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnRole extends DomainVo implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "role_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long roleId;
       @Column(name = "role_name", length = 300, nullable = false)
       @Size(max = 300)
       @NotNull
       @SafeHtml
       private String roleName;
       @Column(name = "active_yn", length = 1, columnDefinition = "character varying(1) default 'y'", nullable = false)
       @NotNull
       private Boolean activeYn;
       @Column(name = "priority", length = 4, columnDefinition = "decimal(4,0) DEFAULT NULL")
       @Digits(integer = 4, fraction = 0)
       private Long priority;
       @Column(name = "app_Id", length = 11, columnDefinition = "int(11) default null")
       @Digits(integer = 11, fraction = 0)
       private Long appId;
       @Column(name = "app_role_id", length = 11, columnDefinition = "int(11) default null")
       @Digits(integer = 11, fraction = 0)
       private Long appRoleId;
       @OneToMany(
               targetEntity = FnRoleFunction.class,
               mappedBy = "roleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnRoleFunction> fnRoleFunctions;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinTable(
               name = "fn_user_pseudo_role",
               joinColumns = {@JoinColumn(name = "pseudo_role_Id", referencedColumnName = "role_id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
               indexes = {
                       @Index(name = "fk_pseudo_role_user_id", columnList = "user_id")
               }
       )
       private Set<FnUser> fnUsers;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinTable(
               name = "fn_role_composite",
               joinColumns = {@JoinColumn(name = "parent_role_id", referencedColumnName = "role_id")},
               inverseJoinColumns = {@JoinColumn(name = "child_role_id", referencedColumnName = "role_id")},
               indexes = {
                       @Index(name = "fk_fn_role_composite_child", columnList = "child_role_id")
               }
       )
       private Set<FnRole> fnRoles;
       @ManyToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
       private Set<FnRole> fnRoleList;
       @OneToMany(
               targetEntity = EpRoleNotification.class,
               mappedBy = "notificationID",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpRoleNotification> epRoleNotifications;
       @OneToMany(
               targetEntity = FnMenuFunctionalRoles.class,
               mappedBy = "roleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnMenuFunctionalRoles> fnMenuFunctionalRoles;
       @OneToMany(
               targetEntity = EpWidgetCatalogRole.class,
               mappedBy = "roleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpWidgetCatalogRole> epWidgetCatalogRoles;
       @OneToMany(
               targetEntity = EpAppRoleFunction.class,
               mappedBy = "fnRole",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpAppRoleFunction> epAppRoleFunctions;
       @OneToMany(
               targetEntity = EpUserRolesRequestDet.class,
               mappedBy = "requestedRoleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpUserRolesRequestDet> epUserRolesRequestDets;
       @OneToMany(
               targetEntity = FnUserRole.class,
               mappedBy = "roleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUserRole> fnUserRoles;
}
