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
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnQzFiredTriggers.FnQzFiredTriggersID;

/*
CREATE TABLE `fn_qz_fired_triggers` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `ENTRY_ID` varchar(95) NOT NULL,
        `TRIGGER_NAME` varchar(200) NOT NULL,
        `TRIGGER_GROUP` varchar(200) NOT NULL,
        `INSTANCE_NAME` varchar(200) NOT NULL,
        `FIRED_TIME` bigint(13) NOT NULL,
        `SCHED_TIME` bigint(13) NOT NULL,
        `PRIORITY` int(11) NOT NULL,
        `STATE` varchar(16) NOT NULL,
        `JOB_NAME` varchar(200) DEFAULT NULL,
        `JOB_GROUP` varchar(200) DEFAULT NULL,
        `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
        `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
        KEY `idx_fn_qz_ft_trig_inst_name` (`SCHED_NAME`,`INSTANCE_NAME`),
        KEY `idx_fn_qz_ft_inst_job_req_rcvry` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
        KEY `idx_fn_qz_ft_j_g` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
        KEY `idx_fn_qz_ft_jg` (`SCHED_NAME`,`JOB_GROUP`),
        KEY `idx_fn_qz_ft_t_g` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        KEY `idx_fn_qz_ft_tg` (`SCHED_NAME`,`TRIGGER_GROUP`)
        )
*/

@Table(name = "fn_qz_fired_triggers", indexes = {
        @Index(name = "idx_fn_qz_ft_trig_inst_name", columnList = "sched_name, instance_name"),
        @Index(name = "idx_fn_qz_ft_inst_job_req_rcvry", columnList = "sched_name, instance_name, requests_recovery"),
        @Index(name = "idx_fn_qz_ft_j_g", columnList = "sched_name, job_name, job_group"),
        @Index(name = "idx_fn_qz_ft_jg", columnList = "sched_name, job_group"),
        @Index(name = "idx_fn_qz_ft_t_g", columnList = "sched_name, trigger_name, trigger_group"),
        @Index(name = "idx_fn_qz_ft_tg", columnList = "sched_name, trigger_group")

})
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnQzFiredTriggersID.class)
public class FnQzFiredTriggers implements Serializable{
       @Id
       @Size(max = 120)
       @SafeHtml
       @Column(name = "SCHED_NAME", length = 120, nullable = false)
       private String schedName;
       @Id
       @Size(max = 95)
       @SafeHtml
       @Column(name = "ENTRY_ID", length = 95, nullable = false)
       private String entryId;
       @Column(name = "TRIGGER_NAME", length = 200, nullable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String triggerName;
       @Column(name = "TRIGGER_GROUP", length = 200, nullable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String triggerGroup;
       @Column(name = "INSTANCE_NAME", length = 200, nullable = false)
       @Size(max = 200)
       @SafeHtml
       @NotNull
       private String instanceName;
       @Column(name = "FIRED_TIME", length = 13, nullable = false)
       @Digits(integer = 13, fraction = 0)
       @NotNull
       private BigInteger firedTime;
       @Column(name = "SCHED_TIME", length = 13, nullable = false)
       @Digits(integer = 13, fraction = 0)
       @NotNull
       private BigInteger schedTime;
       @Column(name = "PRIORITY", nullable = false)
       @NotNull
       private Integer priority;
       @Column(name = "STATE", length = 16, nullable = false)
       @Size(max = 16)
       @SafeHtml
       @NotNull
       private String state;
       @Column(name = "JOB_NAME", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       private String jobName;
       @Column(name = "JOB_GROUP", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       private String jobGroup;
       @Column(name = "IS_NONCONCURRENT", length = 1, columnDefinition = "varchar(1) DEFAULT NULL")
       @Size(max = 1)
       @SafeHtml
       private String isNonconcurrent;
       @Column(name = "REQUESTS_RECOVERY", length = 1, columnDefinition = "varchar(1) DEFAULT NULL")
       @Size(max = 1)
       @SafeHtml
       private String requestsRecovery;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnQzFiredTriggersID implements Serializable {
              @Size(max = 120)
              @SafeHtml
              private String schedName;
              @Size(max = 95)
              @SafeHtml
              private String entryId;
       }
}