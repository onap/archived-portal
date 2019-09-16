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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `cr_report_dwnld_log` (
        `user_id` decimal(11,0) NOT NULL,
        `rep_id` int(11) NOT NULL,
        `file_name` varchar(100) NOT NULL,
        `dwnld_start_time` timestamp NOT NULL DEFAULT current_timestamp(),
        `record_ready_time` timestamp NOT NULL DEFAULT current_timestamp(),
        `filter_params` varchar(2000) DEFAULT NULL
        )
*/


@Table(name = "cr_report_dwnld_log")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrReportDwnldLog implements Serializable {
       @Id
       @Column(name = "user_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long userId;
       @Column(name = "rep_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Integer repId;
       @Column(name = "file_name", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String fileName;
       @Column(name = "dwnld_start_time", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       @FutureOrPresent
       @NotNull
       protected LocalDateTime dwnldStartTime;
       @Column(name = "record_ready_time", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       @FutureOrPresent
       protected LocalDateTime recordReadyTime;
       @Column(name = "filter_params", length = 2000, columnDefinition = "varchar(2000) DEFAULT NULL")
       @Size(max = 2000)
       @SafeHtml
       private String filterParams;
}
