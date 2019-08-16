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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_audit_action` (
        `audit_action_id` int(11) NOT NULL,
        `class_name` varchar(500) NOT NULL,
        `method_name` varchar(50) NOT NULL,
        `audit_action_cd` varchar(20) NOT NULL,
        `audit_action_desc` varchar(200) DEFAULT NULL,
        `active_yn` varchar(1) DEFAULT NULL,
        PRIMARY KEY (`audit_action_id`)
        )
*/

@Table(name = "fn_audit_action")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnAuditAction {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "role_id", nullable = false)
       private Integer roleId;
       @Column(name = "class_name", length = 500, nullable = false)
       @Size(max = 500)
       @NotNull
       @SafeHtml
       private String className;
       @Column(name = "method_name", length = 50, nullable = false)
       @Size(max = 50)
       @NotNull
       @SafeHtml
       private String methodName;
       @Column(name = "audit_action_cd", length = 20, nullable = false)
       @Size(max = 20)
       @NotNull
       @SafeHtml
       private String auditActionCd;
       @Column(name = "audit_action_desc", length = 200)
       @Size(max = 200)
       @NotNull
       @SafeHtml
       private String auditActionDesc;
       @Column(name = "active_yn", length = 1)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String active_yn;

}
