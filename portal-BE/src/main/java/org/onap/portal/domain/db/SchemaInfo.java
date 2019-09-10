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

package org.onap.portal.domain.db;

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
CREATE TABLE `schema_info` (
        `SCHEMA_ID` varchar(25) NOT NULL,
        `SCHEMA_DESC` varchar(75) NOT NULL,
        `DATASOURCE_TYPE` varchar(100) DEFAULT NULL,
        `CONNECTION_URL` varchar(200) NOT NULL,
        `USER_NAME` varchar(45) NOT NULL,
        `PASSWORD` varchar(45) DEFAULT NULL,
        `DRIVER_CLASS` varchar(100) NOT NULL,
        `MIN_POOL_SIZE` int(11) NOT NULL,
        `MAX_POOL_SIZE` int(11) NOT NULL,
        `IDLE_CONNECTION_TEST_PERIOD` int(11) NOT NULL
        )
*/

@Table(name = "schema_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class SchemaInfo implements Serializable {
       @Id
       @Column(name = "SCHEMA_ID", length = 25, nullable = false)
       @Size(max = 25)
       @SafeHtml
       private String schemaId;
       @Column(name = "SCHEMA_DESC", length = 75, nullable = false)
       @Size(max = 75)
       @SafeHtml
       @NotNull
       private String schemaDesc;
       @Column(name = "DATASOURCE_TYPE", length = 100, columnDefinition = "varchar(100) DEFAULT NULL")
       @Size(max = 100)
       @SafeHtml
       private String datasourceType;
       @Column(name = "CONNECTION_URL", length = 200, nullable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String connectionUrl;
       @Column(name = "USER_NAME", length = 45, nullable = false)
       @Size(max = 45)
       @SafeHtml
       @NotNull
       private String userName;
       @Column(name = "PASSWORD", length = 45, columnDefinition = "varchar(45) default null")
       @Size(max = 45)
       @SafeHtml
       private String password;
       @Column(name = "DRIVER_CLASS", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String driverClass;
       @Column(name = "MIN_POOL_SIZE", nullable = false)
       @NotNull
       private Long minPoolSize;
       @Column(name = "MAX_POOL_SIZE", nullable = false)
       @NotNull
       private Long maxPoolSize;
       @Column(name = "IDLE_CONNECTION_TEST_PERIOD", nullable = false)
       @NotNull
       private Long idleConnectionTestPeriod;

}
