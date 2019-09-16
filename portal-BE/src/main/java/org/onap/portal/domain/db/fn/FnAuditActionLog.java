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
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_audit_action_log` (
        `audit_log_id` int(11) NOT NULL AUTO_INCREMENT,
        `audit_action_cd` varchar(200) DEFAULT NULL,
        `action_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `user_id` decimal(11,0) DEFAULT NULL,
        `class_name` varchar(100) DEFAULT NULL,
        `method_name` varchar(50) DEFAULT NULL,
        `success_msg` varchar(20) DEFAULT NULL,
        `error_msg` varchar(500) DEFAULT NULL,
        PRIMARY KEY (`audit_log_id`)
        )
*/

@Table(name = "fn_audit_action_log")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnAuditActionLog implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "audit_log_id", nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       private Integer auditLogId;
       @Column(name = "audit_action_cd", length = 200)
       @Size(max = 200)
       @SafeHtml
       private String auditActionCd;
       @Column(name = "action_time", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       @PastOrPresent
       private LocalDateTime actionTime;
       @Column(name = "user_id", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long userId;
       @Column(name = "class_name", length = 100)
       @Size(max = 100)
       @SafeHtml
       private String className;
       @Column(name = "methodName", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String methodName;
       @Column(name = "success_msg", length = 20)
       @Size(max = 20)
       @SafeHtml
       private String successMsg;
       @Column(name = "error_msg", length = 500)
       @Size(max = 500)
       @SafeHtml
       private String errorMsg;
}
