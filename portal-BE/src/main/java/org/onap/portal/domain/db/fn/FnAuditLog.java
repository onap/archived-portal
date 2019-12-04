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
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_audit_log` (
        `log_id` int(11) NOT NULL AUTO_INCREMENT,
        `user_id` int(11) NOT NULL,
        `activity_cd` varchar(50) NOT NULL,
        `audit_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `comments` varchar(1000) DEFAULT NULL,
        `affected_record_id_bk` varchar(500) DEFAULT NULL,
        `affected_record_id` varchar(4000) DEFAULT NULL,
        PRIMARY KEY (`log_id`),
        KEY `fn_audit_log_activity_cd` (`activity_cd`) USING BTREE,
        KEY `fn_audit_log_user_id` (`user_id`) USING BTREE,
        CONSTRAINT `fk_fn_audit_ref_205_fn_lu_ac` FOREIGN KEY (`activity_cd`) REFERENCES `fn_lu_activity` (`activity_cd`),
        CONSTRAINT `fk_fn_audit_ref_209_fn_user` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`)
        )
*/

@Table(name = "fn_audit_log", indexes = {
        @Index(name = "fn_audit_log_activity_cd", columnList = "activity_cd"),
        @Index(name = "fn_audit_log_user_id", columnList = "user_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class FnAuditLog implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "log_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Integer logId;
       @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
       @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint")
       @NotNull
       @Valid
       private FnUser userId;
       @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
       @JoinColumn(name = "activity_cd", nullable = false)
       @NotNull
       @Valid
       private FnLuActivity activityCd;
       @Column(name = "audit_date", columnDefinition = "datetime default now()", nullable = false)
       @NotNull
       private LocalDateTime auditDate;
       @Column(name = "comments", length = 1000)
       @Size(max = 1000)
       @SafeHtml
       private String comments;
       @Column(name = "affected_record_id_bk", length = 500)
       @Size(max = 5000)
       @SafeHtml
       private String affectedRecordIdBk;
       @Column(name = "affected_record_id", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String affectedRecordId;

}
