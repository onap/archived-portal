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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
CREATE TABLE `fn_menu_functional_ancestors` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `menu_id` int(11) NOT NULL,
        `ancestor_menu_id` int(11) NOT NULL,
        `depth` int(2) NOT NULL,
        PRIMARY KEY (`id`),
        KEY `fk_fn_menu_func_anc_menu_id_idx` (`menu_id`),
        KEY `fk_fn_menu_func_anc_anc_menu_id_idx` (`ancestor_menu_id`),
        CONSTRAINT `fk_fn_menu_func_anc_anc_menu_id` FOREIGN KEY (`ancestor_menu_id`) REFERENCES `fn_menu_functional` (`menu_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_fn_menu_func_anc_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `fn_menu_functional` (`menu_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
        )
*/

@Table(name = "fn_menu_functional_ancestors", indexes = {
        @Index(columnList = "menu_id", name = "fk_fn_menu_func_anc_menu_id_idx"),
        @Index(columnList = "ancestor_menu_id", name = "fk_fn_menu_func_anc_anc_menu_id_idx")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class FnMenuFunctionalAncestors implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", nullable = false, length = 11, columnDefinition = "int(11)  AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Integer id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "menu_id", nullable = false)
       @NotNull
       private FnMenuFunctional menuId;
       @ManyToOne(fetch = FetchType.LAZY, cascade =CascadeType.MERGE)
       @JoinColumn(name = "ancestor_menu_id", nullable = false)
       @NotNull
       private FnMenuFunctional ancestorMenuId;
       @Column(name = "depth", nullable = false, length = 2)
       @Digits(integer = 2, fraction = 0)
       @NotNull
       private Integer depth;
}
