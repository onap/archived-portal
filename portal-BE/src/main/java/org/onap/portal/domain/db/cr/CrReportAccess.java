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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.cr.CrReportAccess.CrReportAccessId;

/*

CREATE TABLE `cr_report_access` (
        `rep_id` decimal(11,0) NOT NULL,
        `order_no` decimal(11,0) NOT NULL,
        `role_id` decimal(11,0) DEFAULT NULL,
        `user_id` decimal(11,0) DEFAULT NULL,
        `read_only_yn` varchar(1) NOT NULL DEFAULT 'n',
        PRIMARY KEY (`rep_id`,`order_no`),
        CONSTRAINT `fk_cr_repor_ref_8550_cr_repor` FOREIGN KEY (`rep_id`) REFERENCES `cr_report` (`rep_id`)
        )
*/


@Table(name = "cr_report_access")
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(CrReportAccessId.class)
public class CrReportAccess implements Serializable{
       @Id
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "rep_id")
       @Valid
       private CrReport repId;
       @Id
       @Column(name = "order_no", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       @NotNull
       private Long orderNo;
       @Column(name = "role_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long roleId;
       @Column(name = "user_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long userId;
       @Column(name = "menu_approved_yn", nullable = false, length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @SafeHtml
       @NotNull
       private String menuApprovedYn;

       @NoArgsConstructor
       @AllArgsConstructor
       @EqualsAndHashCode
       @Getter
       @Setter
       public static class CrReportAccessId implements Serializable{
              private CrReport repId;
              private Long orderNo;
       }
}
