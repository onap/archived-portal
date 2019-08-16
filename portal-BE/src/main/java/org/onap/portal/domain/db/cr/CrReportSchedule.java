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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
CREATE TABLE `cr_report_schedule` (
        `schedule_id` decimal(11,0) NOT NULL,
        `sched_user_id` decimal(11,0) NOT NULL,
        `rep_id` decimal(11,0) NOT NULL,
        `enabled_yn` varchar(1) NOT NULL,
        `start_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `end_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `run_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `recurrence` varchar(50) DEFAULT NULL,
        `conditional_yn` varchar(1) NOT NULL,
        `condition_sql` varchar(4000) DEFAULT NULL,
        `notify_type` int(11) DEFAULT 0,
        `max_row` int(11) DEFAULT 1000,
        `initial_formfields` varchar(3500) DEFAULT NULL,
        `processed_formfields` varchar(3500) DEFAULT NULL,
        `formfields` varchar(3500) DEFAULT NULL,
        `condition_large_sql` text DEFAULT NULL,
        `encrypt_yn` char(1) DEFAULT 'n',
        `attachment_yn` char(1) DEFAULT 'y',
        PRIMARY KEY (`schedule_id`),
        KEY `fk_cr_repor_ref_14707_cr_repor` (`rep_id`),
        CONSTRAINT `fk_cr_repor_ref_14707_cr_repor` FOREIGN KEY (`rep_id`) REFERENCES `cr_report` (`rep_id`)
        )
*/


@Table(name = "cr_report_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrReportSchedule {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "schedule_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long scheduleId;
       @Column(name = "sched_user_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       @NotNull
       private Long schedUserId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "rep_id", nullable = false)
       @Valid
       @NotNull
       private CrReport repId;
       @Column(name = "enabled_yn", length = 1, nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String enabledYn;
       @Column(name = "start_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       @FutureOrPresent
       @NotNull
       private LocalDateTime startDate;
       @Column(name = "end_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       @FutureOrPresent
       @NotNull
       private LocalDateTime endDate;
       @Column(name = "run_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       @FutureOrPresent
       @NotNull
       private LocalDateTime runDate;
       @Column(name = "recurrence_", length = 50, columnDefinition = "varchar(50) DEFAULT NULL")
       @Size(max = 50)
       @SafeHtml
       private String recurrence;
       @Column(name = "conditional_yn", length = 1, nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String conditionalYn;
       @Column(name = "condition_sql", length = 4000, columnDefinition = "varchar(4000) DEFAULT NULL")
       @Size(max = 4000)
       @SafeHtml
       private String conditionSql;
       @Column(name = "notify_type", columnDefinition = "int(11) DEFAULT 0")
       private Integer notifyType;
       @Column(name = "max_row", columnDefinition = "integer default 1000")
       private Integer max_row;
       @Column(name = "initial_formfields", length = 3500, columnDefinition = "varchar(3500) DEFAULT NULL")
       @Size(max = 3500)
       @SafeHtml
       private String initialFormfields;
       @Column(name = "processed_formfields", length = 3500, columnDefinition = "varchar(3500) DEFAULT NULL")
       @Size(max = 3500)
       @SafeHtml
       private String processedFormfields;
       @Column(name = "formfields", length = 3500, columnDefinition = "varchar(3500) DEFAULT NULL")
       @Size(max = 3500)
       @SafeHtml
       private String formfields;
       @Column(name = "condition_large_sql", length = 65535, columnDefinition = "text DEFAULT NULL")
       private String conditionLargeSql;
       @Column(name = "encrypt_yn", length = 1, columnDefinition = "character(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String encryptYn;
       @Column(name = "attachment_yn", length = 1, columnDefinition = "character(1) default 'y'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String attachmentYn;
       @OneToMany(
               targetEntity = CrReportScheduleUsers.class,
               mappedBy = "scheduleId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<CrReportScheduleUsers> crReportScheduleUsers = new ArrayList<>();
}
