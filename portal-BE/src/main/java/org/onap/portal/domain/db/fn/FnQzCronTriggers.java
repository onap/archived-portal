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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.compositePK.FnQzTriggersId;

/*
CREATE TABLE `fn_qz_cron_triggers` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `TRIGGER_NAME` varchar(200) NOT NULL,
        `TRIGGER_GROUP` varchar(200) NOT NULL,
        `CRON_EXPRESSION` varchar(120) NOT NULL,
        `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        CONSTRAINT `fn_qz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `fn_qz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
        )
*/

@Table(name = "fn_qz_cron_triggers")
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnQzTriggersId.class)
public class FnQzCronTriggers implements Serializable {
       @Id
       @ManyToOne
       @JoinColumns(value = {
               @JoinColumn(name = "SCHED_NAME", referencedColumnName = "SCHED_NAME"),
               @JoinColumn(name = "TRIGGER_NAME", referencedColumnName = "TRIGGER_NAME"),
               @JoinColumn(name = "TRIGGER_GROUP", referencedColumnName = "TRIGGER_GROUP")
       })
       private FnQzTriggers fnQzTriggers;
       @Id
       @SafeHtml
       @Column(name = "SCHED_NAME", length = 120, insertable = false, updatable = false)
       private String schedName;
       @Id
       @Column(name = "TRIGGER_NAME", length = 200, nullable = false, insertable = false, updatable = false)
       @Size(max = 200)
       @SafeHtml
       private String triggerName;
       @Id
       @Column(name = "TRIGGER_GROUP", length = 200, nullable = false, insertable = false, updatable = false)
       @Size(max = 200)
       @SafeHtml
       private String triggerGroup;
       @Column(name = "CRON_EXPRESSION", length = 120, nullable = false)
       @Size(max = 120)
       @SafeHtml
       @NotNull
       private String cronExpression;
       @Column(name = "TIME_ZONE_ID", length = 80, columnDefinition = "varchar(80) DEFAULT NULL")
       @Size(max = 80)
       @SafeHtml
       private String timeZoneId;
}