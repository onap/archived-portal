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
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

/*
CREATE TABLE `fn_schedule_workflows` (
        `id_schedule_workflows` bigint(25) NOT NULL AUTO_INCREMENT,
        `workflow_server_url` varchar(45) DEFAULT NULL,
        `workflow_key` varchar(45) NOT NULL,
        `workflow_arguments` varchar(45) DEFAULT NULL,
        `startDateTimeCron` varchar(45) DEFAULT NULL,
        `endDateTime` timestamp NOT NULL DEFAULT current_timestamp(),
        `start_date_time` timestamp NOT NULL DEFAULT current_timestamp(),
        `recurrence` varchar(45) DEFAULT NULL,
        PRIMARY KEY (`id_schedule_workflows`)
        )
*/

@Table(name = "fn_schedule_workflows")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnScheduleWorkflows implements Serializable {
       @Id

  @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id_schedule_workflows", nullable = false, length = 25, columnDefinition = "bigint(25) AUTO_INCREMENT")
       @Digits(integer = 25, fraction = 0)
       private BigInteger idScheduleWorkflows;
       @Column(name = "workflow_server_url", length = 45, columnDefinition = "varchar(45) default null")
       @Size(max = 45)
       @SafeHtml
       //TODO URL
       @URL
       private String workflowServerUrl;
       @Column(name = "workflow_key", length = 45, nullable = false)
       @Size(max = 45)
       @SafeHtml
       @NotNull
       private String workflowKey;
       @Column(name = "workflow_arguments", length = 45, columnDefinition = "varchar(45) default null")
       @Size(max = 45)
       @SafeHtml
       private String workflowArguments;
       @Column(name = "startDateTimeCron", length = 45, columnDefinition = "varchar(45) default null")
       @Size(max = 45)
       @SafeHtml
       private String startDateTimeCron;
       @Column(name = "endDateTime", columnDefinition = "DATETIME default NOW()")
       @PastOrPresent
       private LocalDateTime endDateTime;
       @Column(name = "start_date_time", columnDefinition = "DATETIME default NOW()")
       @PastOrPresent
       private LocalDateTime startDateTime;
       @Column(name = "recurrence", length = 45, columnDefinition = "varchar(45) default null")
       @Size(max = 45)
       @SafeHtml
       private String recurrence;
}
