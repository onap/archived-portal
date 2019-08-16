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
import java.math.BigInteger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.cr.CrReportScheduleUsers.CrReportScheduleUsersId;

/*

CREATE TABLE `cr_report_schedule_users` (
        `schedule_id` decimal(11,0) NOT NULL,
        `rep_id` decimal(11,0) NOT NULL,
        `user_id` decimal(11,0) NOT NULL,
        `role_id` decimal(11,0) DEFAULT NULL,
        `order_no` decimal(11,0) NOT NULL,
        PRIMARY KEY (`schedule_id`,`rep_id`,`user_id`,`order_no`),
        CONSTRAINT `fk_cr_repor_ref_14716_cr_repor` FOREIGN KEY (`schedule_id`) REFERENCES `cr_report_schedule` (`schedule_id`)
        )
*/


@Table(name = "cr_report_schedule_users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(CrReportScheduleUsersId.class)
public class CrReportScheduleUsers {
       @Id
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "schedule_id", nullable = false)
       @Valid
       @NotNull
       private CrReportSchedule scheduleId;
       @Id
       @Column(name = "rep_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long repId;
       @Id
       @Column(name = "user_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long userId;
       @Column(name = "role_id")
       @Digits(integer = 11, fraction = 0)
       private Long roleId;
       @Id
       @Column(name = "order_no", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long orderNo;

       @Getter
       @Setter
       @EqualsAndHashCode
       @AllArgsConstructor
       @NoArgsConstructor
       public static class CrReportScheduleUsersId implements Serializable {
              @Valid
              private CrReportSchedule scheduleId;
              @Digits(integer = 11, fraction = 0)
              private Long repId;
              @Digits(integer = 11, fraction = 0)
              private Long userId;
              @Digits(integer = 11, fraction = 0)
              private Long orderNo;
       }
}