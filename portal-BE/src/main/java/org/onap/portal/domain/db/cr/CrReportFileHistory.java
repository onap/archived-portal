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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.hibernate.validator.constraints.URL;
import org.onap.portal.domain.db.fn.FnUser;

/*
CREATE TABLE `cr_report_file_history` (
        `hist_id` int(11) NOT NULL,
        `sched_user_id` decimal(11,0) NOT NULL,
        `schedule_id` decimal(11,0) NOT NULL,
        `user_id` decimal(11,0) NOT NULL,
        `rep_id` decimal(11,0) DEFAULT NULL,
        `run_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `recurrence` varchar(50) DEFAULT NULL,
        `file_type_id` decimal(2,0) DEFAULT NULL,
        `file_name` varchar(80) DEFAULT NULL,
        `file_blob` blob DEFAULT NULL,
        `file_size` decimal(11,0) DEFAULT NULL,
        `raptor_url` varchar(4000) DEFAULT NULL,
        `error_yn` char(1) DEFAULT 'n',
        `error_code` decimal(11,0) DEFAULT NULL,
        `deleted_yn` char(1) DEFAULT 'n',
        `deleted_by` decimal(38,0) DEFAULT NULL,
        PRIMARY KEY (`hist_id`),
        KEY `sys_c0014614` (`file_type_id`),
        KEY `sys_c0014615` (`rep_id`),
        CONSTRAINT `sys_c0014614` FOREIGN KEY (`file_type_id`) REFERENCES `cr_lu_file_type` (`lookup_id`),
        CONSTRAINT `sys_c0014615` FOREIGN KEY (`rep_id`) REFERENCES `cr_report` (`rep_id`)
        )
*/

@Table(name = "cr_report_file_history", indexes = {
        @Index(name = "sys_c0014617", columnList = "user_id"),
        @Index(name = "sys_c0014614", columnList = "file_type_id"),
        @Index(name = "sys_c0014615", columnList = "rep_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrReportFileHistory {
       @Id
       @Column(name = "hist_id", nullable = false, length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long histId;
       @Column(name = "sched_user_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long schedUserId;
       @Column(name = "schedule_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long scheduleId;
       @Column(name = "user_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long userId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "rep_id")
       @Valid
       private CrReport repId;
       @Column(name = "run_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       @FutureOrPresent
       private LocalDateTime runDate;
       @Column(name = "recurrence", length = 50, columnDefinition = "varchar(50) DEFAULT NULL")
       @Size(max = 50)
       @SafeHtml
       private String recurrence;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "file_type_id")
       @Valid
       private CrLuFileType fileTypeId;
       @Column(name = "file_name", length = 80, columnDefinition = "varchar(80) DEFAULT NULL")
       @Size(max = 80)
       @SafeHtml
       private String fileName;
       @Column(name = "file_blob", columnDefinition = "blob DEFAULT NULL")
       private byte[] fileBlob;
       @Column(name = "file_size", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long file_size;
       //TODO URL @URL
       @URL
       @Column(name = "raptor_url", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String raptorUrl;
       @Column(name = "error_yn", length = 1, columnDefinition = "character(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String errorYn;
       @Column(name = "error_code", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long errorCode;
       @Column(name = "deleted_yn", length = 1, columnDefinition = "character(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String deletedYn;
       @Column(name = "deleted_by", columnDefinition = "decimal(38,0) DEFAULT NULL")
       @Digits(integer = 38, fraction = 0)
       private Long deletedBy;

       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinTable(
               name = "cr_hist_user_map",
               joinColumns = {@JoinColumn(name = "hist_id", referencedColumnName = "hist_id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")}
       )
       private Set<FnUser> fnUserList;
}
