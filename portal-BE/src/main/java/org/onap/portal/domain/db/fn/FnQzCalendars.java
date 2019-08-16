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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnQzCalendars.FnQzCalendarsId;

/*
CREATE TABLE `fn_qz_calendars` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `CALENDAR_NAME` varchar(200) NOT NULL,
        `CALENDAR` blob NOT NULL,
        PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
        )
*/

@Table(name = "fn_qz_calendars")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(FnQzCalendarsId.class)
public class FnQzCalendars {
       @Id
       @SafeHtml
       @Column(name = "SCHED_NAME", length = 120, insertable = false, updatable = false)
       @Size(max = 120)
       @NotNull
       private String schedName;
       @Id
       @SafeHtml
       @Column(name = "CALENDAR_NAME", length = 200, insertable = false, updatable = false)
       @Size(max = 200)
       @NotNull
       private String calendarName;
       @Column(name = "CALENDAR", nullable = false, columnDefinition = "BLOB")
       @NotNull
       private byte[] calendar;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnQzCalendarsId implements Serializable {
              private String schedName;
              private String calendarName;
       }
}

