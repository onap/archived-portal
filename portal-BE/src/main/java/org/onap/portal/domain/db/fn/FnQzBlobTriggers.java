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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalapp.portal.domain.db.fn.compositePK.FnQzTriggersId;

/*
CREATE TABLE `fn_qz_blob_triggers` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `TRIGGER_NAME` varchar(200) NOT NULL,
        `TRIGGER_GROUP` varchar(200) NOT NULL,
        `BLOB_DATA` blob DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        CONSTRAINT `fn_qz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `fn_qz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
        )
*/

@Table(name = "fn_qz_blob_triggers", indexes = {
        @Index(columnList = "SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP")
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(FnQzTriggersId.class)
public class FnQzBlobTriggers {
       @Id
       @ManyToOne
       @JoinColumns(value = {
               @JoinColumn(name = "SCHED_NAME", referencedColumnName = "SCHED_NAME"),
               @JoinColumn(name = "TRIGGER_NAME", referencedColumnName = "TRIGGER_NAME"),
               @JoinColumn(name = "TRIGGER_GROUP", referencedColumnName = "TRIGGER_GROUP")
       })
       @NotNull
       private FnQzTriggers fnQzTriggers;
       @Id
       @Valid
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
       @Column(name = "BLOB_DATA",columnDefinition = "blob DEFAULT NULL")
       private byte[] blobData;
}
