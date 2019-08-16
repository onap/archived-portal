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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
/*
CREATE TABLE `fn_workflow` (
        `id` mediumint(9) NOT NULL AUTO_INCREMENT,
        `name` varchar(20) NOT NULL,
        `description` varchar(500) DEFAULT NULL,
        `run_link` varchar(300) DEFAULT NULL,
        `suspend_link` varchar(300) DEFAULT NULL,
        `modified_link` varchar(300) DEFAULT NULL,
        `active_yn` varchar(300) DEFAULT NULL,
        `created` varchar(300) DEFAULT NULL,
        `created_by` int(11) DEFAULT NULL,
        `modified` varchar(300) DEFAULT NULL,
        `modified_by` int(11) DEFAULT NULL,
        `workflow_key` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `name` (`name`)
        )
*/

@Table(name = "fn_workflow", uniqueConstraints = {
        @UniqueConstraint(name = "name", columnNames = "name")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnWorkflow {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", nullable = false, length = 9, columnDefinition = "mediumint(9) AUTO_INCREMENT")
       @Digits(integer = 9, fraction = 0)
       private Integer id;
       @Column(name = "name", length = 20, nullable = false, unique = true)
       @Size(max = 20)
       @SafeHtml
       @NotNull
       private String name;
       @Column(name = "description", length = 500, columnDefinition = "varchar(500) default null")
       @Size(max = 500)
       @SafeHtml
       private String description;
       @Column(name = "run_link", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String runLink;
       @Column(name = "suspend_link", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String suspendLink;
       @Column(name = "modified_link", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String modifiedLink;
       //TODO varchar(300)? Should be varchar(1)?
       @Column(name = "active_yn", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String activeYn;
       @Column(name = "created", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String created;
       @Column(name = "created_by", length = 11, columnDefinition = "int(11) default null")
       @Digits(integer = 11, fraction = 0)
       private Integer created_by;
       @Column(name = "modified", length = 300, columnDefinition = "varchar(300) default null")
       @Size(max = 300)
       @SafeHtml
       private String modified;
       @Column(name = "modified_by", length = 11, columnDefinition = "int(11) default null")
       @Digits(integer = 11, fraction = 0)
       private Integer modified_by;
       @Column(name = "workflow_key", length = 50, columnDefinition = "varchar(50) default null")
       @Size(max = 50)
       @SafeHtml
       private String workflowKey;

}
