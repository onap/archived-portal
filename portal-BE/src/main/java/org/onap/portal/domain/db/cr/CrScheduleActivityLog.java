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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

/*
CREATE TABLE `cr_schedule_activity_log` (
        `schedule_id` decimal(11,0) NOT NULL,
        `url` varchar(4000) DEFAULT NULL,
        `notes` varchar(2000) DEFAULT NULL,
        `run_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
        )
*/

@Table(name = "cr_schedule_activity_log")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrScheduleActivityLog {
       @Id
       @Column(name = "schedule_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long scheduleId;
       //TODO URL
       @URL
       @Column(name = "url", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String url;
       @Column(name = "notes", length = 2000)
       @Size(max = 2000)
       @SafeHtml
       private String notes;
       @Column(name = "run_time", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       private LocalDateTime runTime;
}
