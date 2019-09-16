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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnQzJobDetails.FnQzJobDetailsID;

/*
CREATE TABLE `fn_qz_job_details` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `JOB_NAME` varchar(200) NOT NULL,
        `JOB_GROUP` varchar(200) NOT NULL,
        `DESCRIPTION` varchar(250) DEFAULT NULL,
        `JOB_CLASS_NAME` varchar(250) NOT NULL,
        `IS_DURABLE` varchar(1) NOT NULL,
        `IS_NONCONCURRENT` varchar(1) NOT NULL,
        `IS_UPDATE_DATA` varchar(1) NOT NULL,
        `REQUESTS_RECOVERY` varchar(1) NOT NULL,
        `JOB_DATA` blob DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
        KEY `idx_fn_qz_j_req_recovery` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
        KEY `idx_fn_qz_j_grp` (`SCHED_NAME`,`JOB_GROUP`)
        )
*/

@Table(name = "fn_qz_job_details", indexes = {
        @Index(name = "idx_fn_qz_j_req_recovery", columnList = "sched_name,requests_recovery"),
        @Index(name = "idx_fn_qz_j_grp", columnList = "sched_name, job_group")
})
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnQzJobDetailsID.class)
public class FnQzJobDetails implements Serializable{
       @Id
       @SafeHtml
       @Size(max = 120)
       @Column(name = "SCHED_NAME", length = 120)
       private String schedName;
       @Id
       @SafeHtml
       @Size(max = 200)
       @Column(name = "JOB_NAME", length = 200)
       private String jobName;
       @Id
       @SafeHtml
       @Size(max = 200)
       @Column(name = "JOB_GROUP", length = 200)
       private String jobGroup;
       @Column(name = "DESCRIPTION", length = 250, columnDefinition = "varchar(250) DEFAULT NULL")
       @Size(max = 250)
       @SafeHtml
       private String description;
       @Column(name = "JOB_CLASS_NAME", length = 250, nullable = false)
       @Size(max = 250)
       @SafeHtml
       @NotNull
       private String jobClassName;
       @Column(name = "IS_DURABLE", length = 1, nullable = false)
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String isDurable;
       @Column(name = "IS_NONCONCURRENT", length = 1, nullable = false)
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String isNonconcurrent;
       @Column(name = "IS_UPDATE_DATA", length = 1, nullable = false)
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String isUpdateData;
       @Column(name = "REQUESTS_RECOVERY", length = 1, nullable = false)
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String requestsRecovery;
       @Column(name = "JOB_DATA", columnDefinition = "blob DEFAULT NULL")
       private byte[] jobData;

       @OneToMany(
               targetEntity = FnQzTriggers.class,
               mappedBy = "fnQzJobDetails",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnQzTriggers> selectedTabCd;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnQzJobDetailsID implements Serializable {
              @Size(max = 120)
              @SafeHtml
              private String schedName;
              @Size(max = 200)
              @SafeHtml
              private String jobName;
              @Size(max = 200)
              @SafeHtml
              private String jobGroup;
       }

}