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

package org.onap.portal.domain.db.ep;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.ep.EpMlRec.EpMlRecId;

/*
CREATE TABLE `ep_ml_rec` (
        `time_stamp` timestamp NOT NULL DEFAULT current_timestamp(),
        `org_user_id` varchar(20) NOT NULL,
        `rec` varchar(4000) DEFAULT NULL,
        PRIMARY KEY (`time_stamp`,`org_user_id`)
        )
*/

@Table(name = "ep_ml_rec")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(EpMlRecId.class)
public class EpMlRec {
       @Id
       @Column(name = "time_stamp", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       private LocalDateTime timeStamp;
       @Id
       @Column(name = "org_user_id", length = 20, nullable = false)
       @Size(max = 20)
       @NotNull
       @SafeHtml
       private String orgUserId;
       @Column(name = "rec", length = 4000)
       @Size(max = 4000)
       @SafeHtml
       private String rec;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class EpMlRecId implements Serializable {
              private LocalDateTime timeStamp;
              @Size(max = 20)
              @NotNull
              @SafeHtml
              private String orgUserId;
       }
}