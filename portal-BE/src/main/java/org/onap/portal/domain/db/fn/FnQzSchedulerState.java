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
import org.onap.portal.domain.db.fn.FnQzSchedulerState.FnQzSchedulerStateID;

/*
CREATE TABLE `fn_qz_scheduler_state` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `INSTANCE_NAME` varchar(200) NOT NULL,
        `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
        `CHECKIN_INTERVAL` bigint(13) NOT NULL,
        PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
        )
*/

@Table(name = "fn_qz_scheduler_state")
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnQzSchedulerStateID.class)
public class FnQzSchedulerState implements Serializable{
       @Id
       @Size(max = 120)
       @SafeHtml
       @Column(name = "SCHED_NAME", length = 120, nullable = false)
       private String schedName;
       @Id
       @Size(max = 200)
       @SafeHtml
       @Column(name = "INSTANCE_NAME", length = 200, nullable = false)
       private String instanceName;
       @Column(name = "LAST_CHECKIN_TIME", length = 13, nullable = false)
       @Digits(integer = 13, fraction = 0)
       @NotNull
       private BigInteger lastCheckinTime;
       @Column(name = "CHECKIN_INTERVAL", length = 13, nullable = false)
       @Digits(integer = 13, fraction = 0)
       @NotNull
       private BigInteger checkinInterval;


       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnQzSchedulerStateID implements Serializable {
              @Size(max = 120)
              @SafeHtml
              private String schedName;
              @Size(max = 200)
              @SafeHtml
              private String instanceName;
       }
}