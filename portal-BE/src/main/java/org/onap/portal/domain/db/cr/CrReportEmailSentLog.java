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
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_report_email_sent_log` (
        `log_id` int(11) NOT NULL,
        `schedule_id` decimal(11,0) DEFAULT NULL,
        `gen_key` varchar(25) NOT NULL,
        `rep_id` decimal(11,0) NOT NULL,
        `user_id` decimal(11,0) DEFAULT NULL,
        `sent_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `access_flag` varchar(1) NOT NULL DEFAULT 'y',
        `touch_date` timestamp NOT NULL DEFAULT current_timestamp(),
        PRIMARY KEY (`log_id`),
        KEY `fk_cr_report_rep_id` (`rep_id`),
        CONSTRAINT `fk_cr_report_rep_id` FOREIGN KEY (`rep_id`) REFERENCES `cr_report` (`rep_id`)
        )
*/

@Table(name = "cr_report_email_sent_log", indexes = {
        @Index(name = "fk_cr_report_rep_id", columnList = "rep_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrReportEmailSentLog implements Serializable {
       @Id
       @Column(name = "log_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Integer logId;
       @Column(name = "schedule_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long scheduleId;
       @Column(name = "gen_key", length = 25, nullable = false)
       @Size(max = 25)
       @SafeHtml
       @NotNull
       private String genKey;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "rep_id", nullable = false)
       @NotNull
       @Valid
       private CrReport repId;
       @Column(name = "user_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long userId;
       @Column(name = "sent_date", nullable = false, columnDefinition = "datetime default now()")
       @FutureOrPresent
       protected LocalDateTime sentDate;
       @Column(name = "access_flag", nullable = false, length = 1, columnDefinition = "character varying(1) default 'y'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String accessFlag;
       @Column(name = "touch_date", nullable = false, columnDefinition = "datetime default now()")
       @FutureOrPresent
       protected LocalDateTime touchDate;
}
