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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
CREATE TABLE `fn_user_role` (
        `user_id` int(10) NOT NULL,
        `role_id` int(10) NOT NULL,
        `priority` decimal(4,0) DEFAULT NULL,
        `app_id` int(11) NOT NULL DEFAULT 2,
        PRIMARY KEY (`user_id`,`role_id`,`app_id`),
        KEY `fn_user_role_role_id` (`role_id`) USING BTREE,
        KEY `fn_user_role_user_id` (`user_id`) USING BTREE,
        KEY `fk_fn_user__ref_178_fn_app_idx` (`app_id`),
        CONSTRAINT `fk_fn_user__ref_172_fn_user` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `fk_fn_user__ref_175_fn_role` FOREIGN KEY (`role_id`) REFERENCES `fn_role` (`role_id`),
        CONSTRAINT `fk_fn_user__ref_178_fn_app` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`)
        )
*/

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "FnUserRole.retrieveUserRoleOnUserIdAndRoleIdAndAppId",
                query = "select * from FnUserRole where user_id= :userId"
                        + " and role_id= :roleId"
                        + " and app_id= :appId"),
        @NamedNativeQuery(
                name = "FnUserRole.retrieveCachedAppRolesForUser",
                query = "select * from FnUserRole where user_id= :userId"
                        + " and user_id= :userId"
                        + " and app_id= :appId")
})

@Table(
        name = "fn_user_role",
        indexes = {
                @Index(name = "fn_user_role_role_id", columnList = "role_id"),
                @Index(name = "fn_user_role_user_id", columnList = "user_id"),
                @Index(name = "fk_fn_user__ref_178_fn_app_idx", columnList = "app_id")},
        uniqueConstraints = {
                @UniqueConstraint(name = "fn_user_role_id", columnNames = {"role_id", "user_id", "app_id"})
        })
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
public class FnUserRole {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", columnDefinition = "int(11) auto_increment")
       private Long id;
       @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
       @JoinColumn(name = "user_id")
       @Valid
       private FnUser userId;
       @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
       @JoinColumn(name = "role_id")
       @Valid
       private FnRole roleId;
       @Column(name = "priority", length = 4, columnDefinition = "decimal(4,0) DEFAULT NULL")
       @Digits(integer = 4, fraction = 0)
       private Long priority;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "app_Id")
       @Valid
       private FnApp appId;
}