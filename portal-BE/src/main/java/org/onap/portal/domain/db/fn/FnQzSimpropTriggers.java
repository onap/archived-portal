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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.compositePK.FnQzTriggersId;

/*
CREATE TABLE `fn_qz_simprop_triggers` (
        `SCHED_NAME` varchar(120) NOT NULL,
        `TRIGGER_NAME` varchar(200) NOT NULL,
        `TRIGGER_GROUP` varchar(200) NOT NULL,
        `STR_PROP_1` varchar(512) DEFAULT NULL,
        `STR_PROP_2` varchar(512) DEFAULT NULL,
        `STR_PROP_3` varchar(512) DEFAULT NULL,
        `INT_PROP_1` int(11) DEFAULT NULL,
        `INT_PROP_2` int(11) DEFAULT NULL,
        `LONG_PROP_1` bigint(20) DEFAULT NULL,
        `LONG_PROP_2` bigint(20) DEFAULT NULL,
        `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
        `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
        `BOOL_PROP_1` varchar(1) DEFAULT NULL,
        `BOOL_PROP_2` varchar(1) DEFAULT NULL,
        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
        CONSTRAINT `fn_qz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `fn_qz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
        )
*/

@Table(name = "fn_qz_simprop_triggers")
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnQzTriggersId.class)
public class FnQzSimpropTriggers {
       @Id
       @ManyToOne
       @JoinColumns(value = {
               @JoinColumn(name = "SCHED_NAME", referencedColumnName = "SCHED_NAME"),
               @JoinColumn(name = "TRIGGER_NAME", referencedColumnName = "TRIGGER_NAME"),
               @JoinColumn(name = "TRIGGER_GROUP", referencedColumnName = "TRIGGER_GROUP")
       })
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
       @Column(name = "STR_PROP_1", length = 512, columnDefinition = "varchar(512) DEFAULT NULL")
       @Size(max = 512)
       @SafeHtml
       private String strProp1;
       @Column(name = "STR_PROP_2", length = 512, columnDefinition = "varchar(512) DEFAULT NULL")
       @Size(max = 512)
       @SafeHtml
       private String strProp2;
       @Column(name = "STR_PROP_3", length = 512, columnDefinition = "varchar(512) DEFAULT NULL")
       @Size(max = 512)
       @SafeHtml
       private String strProp3;
       @Column(name = "INT_PROP_1", length = 11,columnDefinition = "int(11) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Integer intProp1;
       @Column(name = "INT_PROP_2", length = 11, columnDefinition = "int(11) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Integer intProp2;
       @Column(name = "LONG_PROP_1", length = 20, columnDefinition = "bigint(20) DEFAULT NULL")
       @Digits(integer = 20, fraction = 0)
       private Long LONG_PROP_1;
       @Column(name = "LONG_PROP_2", length = 20, columnDefinition = "bigint(20) DEFAULT NULL")
       @Digits(integer = 20, fraction = 0)
       private Long LONG_PROP_2;
       @Column(name = "DEC_PROP_1", columnDefinition = "decimal(13,4) DEFAULT NULL")
       @Digits(integer = 13, fraction = 4)
       private Double decProp1;
       @Column(name = "DEC_PROP_2", columnDefinition = "decimal(13,4) DEFAULT NULL")
       @Digits(integer = 13, fraction = 4)
       private Double decProp2;
       @Column(name = "BOOL_PROP_1", length = 1, columnDefinition = "varchar(1) DEFAULT NULL")
       @Size(max = 1)
       @SafeHtml
       private String boolProp1;
       @Column(name = "BOOL_PROP_2", length = 1, columnDefinition = "varchar(1) DEFAULT NULL")
       @Size(max = 1)
       @SafeHtml
       private String boolProp2;
}