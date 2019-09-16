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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_remote_schema_info` (
        `schema_prefix` varchar(5) NOT NULL,
        `schema_desc` varchar(75) NOT NULL,
        `datasource_type` varchar(100) DEFAULT NULL,
        PRIMARY KEY (`schema_prefix`)
        )
*/


@Table(name = "cr_remote_schema_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrRemoteSchemaInfo implements Serializable {
       @Id
       @Column(name = "schema_prefix", length = 5, nullable = false)
       @Size(max = 5)
       @SafeHtml
       @NotNull
       private String schemaPrefix;
       @Column(name = "schema_desc", length = 75, nullable = false)
       @Size(max = 75)
       @NotNull
       @SafeHtml
       private String schemaDesc;
       @Column(name = "datasource_type", length = 100)
       @Size(max = 100)
       @SafeHtml
       private String datasourceType;
}
