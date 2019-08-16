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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.ep.EpMlModel.EpMlModelId;

/*
CREATE TABLE `ep_ml_model` (
        `time_stamp` timestamp NOT NULL DEFAULT current_timestamp(),
        `group_id` int(11) NOT NULL,
        `model` longblob DEFAULT NULL,
        PRIMARY KEY (`time_stamp`,`group_id`)
        )
*/

@Table(name = "ep_ml_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(EpMlModelId.class)
public class EpMlModel {
       @Id
       @Digits(integer = 11, fraction = 0)
       @Column(name = "group_id", length = 11, nullable = false)
       @NotNull
       private Long groupId;
       @Id
       @PastOrPresent
       @Column(name = "time_stamp", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
       private LocalDateTime timeStamp;
       @Column(name = "model", columnDefinition = "longblob DEFAULT NULL")
       private byte[] model;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class EpMlModelId implements Serializable {
              @Digits(integer = 11, fraction = 0)
              private Long groupId;
              @PastOrPresent
              private LocalDateTime timeStamp;
       }
}