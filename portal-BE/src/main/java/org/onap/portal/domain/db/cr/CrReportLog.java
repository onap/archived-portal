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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_report_log` (
        `rep_id` decimal(11,0) NOT NULL,
        `log_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `user_id` decimal(11,0) NOT NULL,
        `action` varchar(2000) NOT NULL,
        `action_value` varchar(50) DEFAULT NULL,
        `form_fields` varchar(4000) DEFAULT NULL,
        KEY `fk_cr_repor_ref_17645_cr_repor` (`rep_id`),
        CONSTRAINT `fk_cr_repor_ref_17645_cr_repor` FOREIGN KEY (`rep_id`) REFERENCES `cr_report` (`rep_id`)
        )
*/

@Table(name = "cr_report_log", indexes = {
        @Index(name = "fk_cr_repor_ref_17645_cr_repor", columnList = "rep_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrReportLog implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @JoinColumn(name = "id")
       private Long id;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "rep_id")
       @NotNull
       @Valid
       private CrReport repId;
       @Column(name = "log_time", nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       @PastOrPresent
       @NotNull
       protected LocalDateTime logTime;
       @Column(name = "user_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long userId;
       @Column(name = "action", length = 2000, nullable = false)
       @Size(max = 2000)
       @SafeHtml
       @NotNull
       private String action;
       @Column(name = "action_value", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String actionValue;
       @Column(name = "form_fields", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String formFields;
}
