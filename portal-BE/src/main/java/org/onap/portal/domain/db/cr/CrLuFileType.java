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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*

CREATE TABLE `cr_lu_file_type` (
        `lookup_id` decimal(2,0) NOT NULL,
        `lookup_descr` varchar(255) NOT NULL,
        `active_yn` char(1) DEFAULT 'y',
        `error_code` decimal(11,0) DEFAULT NULL,
        PRIMARY KEY (`lookup_id`)
        )
*/


@Table(name = "cr_lu_file_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrLuFileType {
       @Id
       @Column(name = "lookup_id", length = 2, nullable = false)
       @Digits(integer = 2, fraction = 0)
       @Positive
       private Long lookupId;
       @Column(name = "lookup_descr", nullable = false)
       @Size(max = 255)
       @SafeHtml
       @NotNull
       private String lookupDescr;
       @Column(name = "active_yn", length = 1, columnDefinition = "character(1) default 'y'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String activeYn;
       @Column(name = "error_code")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long errorCode;
       @OneToMany(
               targetEntity = CrReportFileHistory.class,
               mappedBy = "fileTypeId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<CrReportFileHistory> crReportFileHistories = new ArrayList<>();

}
