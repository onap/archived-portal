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

package org.onap.portal.domain.db.cr;

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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_table_join` (
        `src_table_name` varchar(30) NOT NULL,
        `dest_table_name` varchar(30) NOT NULL,
        `join_expr` varchar(500) NOT NULL,
        KEY `cr_table_join_dest_table_name` (`dest_table_name`) USING BTREE,
        KEY `cr_table_join_src_table_name` (`src_table_name`) USING BTREE,
        CONSTRAINT `fk_cr_table_ref_311_cr_tab` FOREIGN KEY (`src_table_name`) REFERENCES `cr_table_source` (`table_name`),
        CONSTRAINT `fk_cr_table_ref_315_cr_tab` FOREIGN KEY (`dest_table_name`) REFERENCES `cr_table_source` (`table_name`)
        )
*/


@Table(name = "cr_table_join", indexes = {
        @Index(name = "cr_table_join_dest_table_name", columnList = "dest_table_name"),
        @Index(name = "cr_table_join_src_table_name", columnList = "src_table_name")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrTableJoin {
       //TODO Unique constrains {srcTableName, destTableName}?
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", nullable = false, columnDefinition = "int(11) auto_increment")
       private Long id;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "src_table_name", nullable = false)
       @Valid
       @NotNull
       private CrTableSource srcTableName;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "dest_table_name", nullable = false)
       @Valid
       @NotNull
       private CrTableSource destTableName;
       @Column(name = "join_expr", length = 500, nullable = false)
       @Size(max = 500)
       @SafeHtml
       @NotNull
       private String joinExpr;
}
