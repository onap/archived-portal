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

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_table_source` (
        `table_name` varchar(30) NOT NULL,
        `display_name` varchar(30) NOT NULL,
        `pk_fields` varchar(200) DEFAULT NULL,
        `web_view_action` varchar(50) DEFAULT NULL,
        `large_data_source_yn` varchar(1) NOT NULL DEFAULT 'n',
        `filter_sql` varchar(4000) DEFAULT NULL,
        `source_db` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`table_name`)
        )
*/

@Table(name = "cr_table_source")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrTableSource implements Serializable {
       @Id
       @Column(name = "table_name", length = 30, nullable = false)
       @Size(max = 30)
       @SafeHtml
       private String tableName;
       @Column(name = "display_name", length = 30, nullable = false)
       @Size(max = 30)
       @SafeHtml
       @NotNull
       private String displayName;
       @Column(name = "pk_fields", length = 200)
       @Size(max = 200)
       @SafeHtml
       private String pkFields;
       @Column(name = "web_view_action", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String webViewAction;
       @Column(name = "large_data_source_yn", length = 1, columnDefinition = "character varying(1) default 'n'", nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String largeDataSourceYn;
       @Column(name = "filter_sql", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String filterSql;
       @Column(name = "source_db", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String sourceDb;
       @OneToMany(
               targetEntity = CrTableJoin.class,
               mappedBy = "srcTableName",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrTableJoin> crTableJoins;
       @OneToMany(
               targetEntity = CrTableJoin.class,
               mappedBy = "destTableName",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrTableJoin> crTableJoins1;
       @OneToMany(
               targetEntity = CrTableRole.class,
               mappedBy = "tableName",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrTableRole> crTableRoles;
}
