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

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.compositePK.FnQzTriggersId;

/*
CREATE TABLE `fn_qz_triggers` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `TRIGGER_NAME` varchar(200) NOT NULL,
        `TRIGGER_GROUP` varchar(200) NOT NULL,
        `JOB_NAME` varchar(200) NOT NULL,
        `JOB_GROUP` varchar(200) NOT NULL,
        `DESCRIPTION` varchar(250) DEFAULT NULL,
        `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
        `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
        `PRIORITY` int(11) DEFAULT NULL,
        `TRIGGER_STATE` varchar(16) NOT NULL,
        `TRIGGER_TYPE` varchar(8) NOT NULL,
        `START_TIME` bigint(13) NOT NULL,
        `END_TIME` bigint(13) DEFAULT NULL,
        `CALENDAR_NAME` varchar(200) DEFAULT NULL,
        `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
        `JOB_DATA` blob DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        KEY `idx_fn_qz_t_j` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
        KEY `idx_fn_qz_t_jg` (`SCHED_NAME`,`JOB_GROUP`),
        KEY `idx_fn_qz_t_c` (`SCHED_NAME`,`CALENDAR_NAME`),
        KEY `idx_fn_qz_t_g` (`SCHED_NAME`,`TRIGGER_GROUP`),
        KEY `idx_fn_qz_t_state` (`SCHED_NAME`,`TRIGGER_STATE`),
        KEY `idx_fn_qz_t_n_state` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
        KEY `idx_fn_qz_t_n_g_state` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
        KEY `idx_fn_qz_t_next_fire_time` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
        KEY `idx_fn_qz_t_nft_st` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
        KEY `idx_fn_qz_t_nft_misfire` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
        KEY `idx_fn_qz_t_nft_st_misfire` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
        KEY `idx_fn_qz_t_nft_st_misfire_grp` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
        CONSTRAINT `fn_qz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `fn_qz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
        )
*/

@Table(name = "fn_qz_triggers", indexes = {
        @Index(name = "idx_fn_qz_t_j", columnList = "sched_name, job_name, job_group"),
        @Index(name = "idx_fn_qz_t_jg", columnList = "sched_name, job_group"),
        @Index(name = "idx_fn_qz_t_c", columnList = "sched_name, calendar_name"),
        @Index(name = "idx_fn_qz_t_g", columnList = "sched_name, trigger_group"),
        @Index(name = "idx_fn_qz_t_state", columnList = "sched_name, trigger_state"),
        @Index(name = "idx_fn_qz_t_n_state", columnList = "sched_name, trigger_name, trigger_group, trigger_state"),
        @Index(name = "idx_fn_qz_t_n_g_state", columnList = "sched_name, trigger_group, trigger_state"),
        @Index(name = "idx_fn_qz_t_next_fire_time", columnList = "sched_name, next_fire_time"),
        @Index(name = "idx_fn_qz_t_nft_st", columnList = "sched_name, trigger_state, next_fire_time"),
        @Index(name = "idx_fn_qz_t_nft_misfire", columnList = "sched_name, misfire_instr, next_fire_time"),
        @Index(name = "idx_fn_qz_t_nft_st_misfire", columnList = "sched_name, misfire_instr, next_fire_time, trigger_state"),
        @Index(name = "idx_fn_qz_t_nft_st_misfire_grp", columnList = "sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@IdClass(FnQzTriggersId.class)
public class FnQzTriggers {
       @ManyToOne
       @JoinColumns(value = {
               @JoinColumn(name = "SCHED_NAME", referencedColumnName = "SCHED_NAME"),
               @JoinColumn(name = "JOB_NAME", referencedColumnName = "JOB_NAME"),
               @JoinColumn(name = "JOB_GROUP", referencedColumnName = "JOB_GROUP")
       })
       @NotNull
       private FnQzJobDetails fnQzJobDetails;
       @Id
       @Valid
       @Column(name = "SCHED_NAME", length = 120, insertable = false, updatable = false)
       private String schedName;
       @Id
       @Valid
       @Column(name = "TRIGGER_NAME", length = 200)
       private String triggerName;
       @Id
       @Valid
       @Column(name = "TRIGGER_GROUP", length = 200)
       private String triggerGroup;
       @Column(name = "JOB_NAME", length = 200, nullable = false, insertable = false, updatable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String jobName;
       @Column(name = "JOB_GROUP", length = 200, nullable = false, insertable = false, updatable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String jobGroup;

       @Column(name = "DESCRIPTION", length = 250, columnDefinition = "varchar(250) DEFAULT NULL")
       @Size(max = 250)
       @SafeHtml
       private String description;
       @Column(name = "NEXT_FIRE_TIME", length = 13, columnDefinition = "bigint(13) DEFAULT NULL")
       @Digits(integer = 13, fraction = 0)
       private Long nextFireTime;
       @Column(name = "PREV_FIRE_TIME", length = 13, columnDefinition = "bigint(13) DEFAULT NULL")
       @Digits(integer = 13, fraction = 0)
       private Long prevFireTime;
       @Column(name = "PRIORITY", length = 11, columnDefinition = "int(11) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Integer priority;
       @Column(name = "TRIGGER_STATE", length = 16, nullable = false)
       @Size(max = 16)
       @SafeHtml
       @NotNull
       private String triggerState;
       @Column(name = "TRIGGER_TYPE", length = 8, nullable = false)
       @Size(max = 8)
       @SafeHtml
       @NotNull
       private String triggerType;
       @Column(name = "START_TIME", length = 13, nullable = false)
       @Digits(integer = 13, fraction = 0)
       @NotNull
       private Long startTime;
       @Column(name = "END_TIME", length = 13, columnDefinition = "bigint(13) DEFAULT NULL")
       @Digits(integer = 13, fraction = 0)
       private Long endTime;
       @Column(name = "CALENDAR_NAME", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       private String calendarName;
       @Column(name = "MISFIRE_INSTR", length = 2, columnDefinition = "smallint(2) DEFAULT NULL")
       @Digits(integer = 2, fraction = 0)
       private BigInteger misfireInstr;
       @Column(name = "JOB_DATA", columnDefinition = "blob DEFAULT NULL")
       private byte[] jobData;
}