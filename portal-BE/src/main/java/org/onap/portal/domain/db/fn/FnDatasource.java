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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

/*
CREATE TABLE `fn_datasource` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `name` varchar(50) DEFAULT NULL,
        `driver_name` varchar(256) DEFAULT NULL,
        `server` varchar(256) DEFAULT NULL,
        `port` int(11) DEFAULT NULL,
        `user_name` varchar(256) DEFAULT NULL,
        `password` varchar(256) DEFAULT NULL,
        `url` varchar(256) DEFAULT NULL,
        `min_pool_size` int(11) DEFAULT NULL,
        `max_pool_size` int(11) DEFAULT NULL,
        `adapter_id` int(11) DEFAULT NULL,
        `ds_type` varchar(20) DEFAULT NULL,
        PRIMARY KEY (`id`)
        )
*/

@Table(name = "fn_datasource")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnDatasource implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "message_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long messageId;
       @Column(name = "name", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String name;
       @Column(name = "driver_name", length = 256)
       @Size(max = 256)
       @SafeHtml
       private String driverName;
       @Column(name = "server", length = 256)
       @Size(max = 256)
       @SafeHtml
       private String server;
       @Column(name = "port", length = 11)
       @Min(value = 0)
       @Max(value = 65535)
       private Long port;
       @Column(name = "user_name", length = 256)
       @Size(max = 256)
       @SafeHtml
       private String userName;
       @Column(name = "password", length = 256)
       @Size(max = 256)
       @SafeHtml
       private String password;
       //TODO URL
       @Column(name = "url", length = 256)
       @Size(max = 256)
       @SafeHtml
       @URL
       private String url;
       @Column(name = "min_pool_size")
       private Long minPoolSize;
       @Column(name = "max_pool_size")
       private Long maxPoolSize;
       @Column(name = "adapter_id")
       private Long adapterId;
       @Column(name = "ds_type", length = 20)
       @Size(max = 20)
       @SafeHtml
       private String dsType;



}
