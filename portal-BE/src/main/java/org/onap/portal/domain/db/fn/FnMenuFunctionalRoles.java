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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
CREATE TABLE `fn_menu_functional_roles` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `menu_id` int(11) NOT NULL,
        `app_id` int(11) NOT NULL,
        `role_id` int(10) NOT NULL,
        PRIMARY KEY (`id`),
        KEY `fk_fn_menu_func_roles_menu_id_idx` (`menu_id`),
        KEY `fk_fn_menu_func_roles_app_id_idx` (`app_id`),
        KEY `fk_fn_menu_func_roles_role_id_idx` (`role_id`),
        CONSTRAINT `fk_fn_menu_func_roles_app_id` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_fn_menu_func_roles_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `fn_menu_functional` (`menu_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_fn_menu_func_roles_role_id` FOREIGN KEY (`role_id`) REFERENCES `fn_role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "FnMenuFunctionalRoles.retrieveByRoleId",
        query = "from FnMenuFunctionalRoles where roleId.roleId =:roleId"),
    @NamedQuery(
        name = "FnMenuFunctionalRoles.retrieveByMenuId",
        query = "from FnMenuFunctionalRoles where menuId.menuId =:menuId"
    )
}
)

@Table(name = "fn_menu_functional_roles", indexes = {
        @Index(columnList = "menu_id", name = "fk_fn_menu_func_roles_menu_id_idx"),
        @Index(columnList = "app_id", name = "fk_fn_menu_func_roles_app_id_idx"),
        @Index(columnList = "role_id", name = "fk_fn_menu_func_roles_role_id_idx")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnMenuFunctionalRoles implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", nullable = false, length = 11, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "menu_id", nullable = false)
       @Valid
       @NotNull
       private FnMenuFunctional menuId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "role_id", nullable = false)
       @Valid
       @NotNull
       private FnRole roleId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_Id", nullable = false)
       @Valid
       @NotNull
       private FnApp appId;

}
