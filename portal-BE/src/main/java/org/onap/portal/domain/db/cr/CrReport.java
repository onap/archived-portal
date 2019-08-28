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
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*

CREATE TABLE `cr_report` (
        `rep_id` decimal(11,0) NOT NULL,
        `title` varchar(100) NOT NULL,
        `descr` varchar(255) DEFAULT NULL,
        `public_yn` varchar(1) NOT NULL DEFAULT 'n',
        `report_xml` text DEFAULT NULL,
        `create_id` decimal(11,0) DEFAULT NULL,
        `create_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `maint_id` decimal(11,0) DEFAULT NULL,
        `maint_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `menu_id` varchar(500) DEFAULT NULL,
        `menu_approved_yn` varchar(1) NOT NULL DEFAULT 'n',
        `owner_id` decimal(11,0) DEFAULT NULL,
        `folder_id` int(11) DEFAULT 0,
        `dashboard_type_yn` varchar(1) DEFAULT 'n',
        `dashboard_yn` varchar(1) DEFAULT 'n',
        PRIMARY KEY (`rep_id`),
        KEY `cr_report_create_idpublic_yntitle` (`create_id`,`public_yn`,`title`) USING BTREE
        )
*/


@Table(name = "cr_report", indexes = {
        @Index(name = "cr_report_create_idpublic_yntitle", columnList = "create_id, public_yn, title")
})
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@Embeddable
public class CrReport implements Serializable {
       @Id
       @Column(name = "rep_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long repId;
       @Column(name = "title", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String title;
       @Column(name = "descr", columnDefinition = "varchar(255) DEFAULT NULL")
       @Size(max = 255)
       @SafeHtml
       private String descr;
       @Column(name = "public_yn", nullable = false, length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String publicYn;
       @Column(name = "report_xml", columnDefinition = "text DEFAULT NULL")
       private String reportXml;
       @Column(name = "create_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long createId;
       @Column(name = "create_date", nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp()")
       @PastOrPresent
       @NotNull
       protected LocalDateTime createDate;
       @Column(name = "maint_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long maintId;
       @Column(name = "maint_date", nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp()")
       @PastOrPresent
       @NotNull
       protected LocalDateTime maintDate;
       @Column(name = "menu_id", length = 500, columnDefinition = "varchar(500) DEFAULT NULL")
       @Size(max = 500)
       @SafeHtml
       private String menuId;
       @Column(name = "menu_approved_yn", nullable = false, length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       @NotNull
       private String menuApprovedYn;
       @Column(name = "owner_id", columnDefinition = "decimal(11,0) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long ownerId;
       @Column(name = "folder_id", length = 11, columnDefinition = "int(11) DEFAULT 0")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Integer folderId;
       @Column(name = "dashboard_type_yn", length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String dashboardTypeYn;
       @Column(name = "dashboard_yn", length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String dashboardYn;

       public CrReport(
               @Digits(integer = 11, fraction = 0) @Positive Long repId,
               @Size(max = 100) @SafeHtml @NotNull String title,
               @Size(max = 255) @SafeHtml String descr,
               @Pattern(regexp = "[YNyn]") @Size(max = 1) @SafeHtml @NotNull String publicYn,
               @SafeHtml String reportXml,
               @Digits(integer = 11, fraction = 0) @Positive Long createId,
               @FutureOrPresent @NotNull LocalDateTime createDate,
               @Digits(integer = 11, fraction = 0) Long maintId,
               @FutureOrPresent @NotNull LocalDateTime maintDate,
               @Size(max = 500) @SafeHtml String menuId,
               @Pattern(regexp = "[YNyn]") @Size(max = 1) @SafeHtml @NotNull String menuApprovedYn,
               @Digits(integer = 11, fraction = 0) Long ownerId,
               @Digits(integer = 11, fraction = 0) @Positive Integer folderId,
               @Pattern(regexp = "[YNyn]") @Size(max = 1) @SafeHtml String dashboardTypeYn,
               @Pattern(regexp = "[YNyn]") @Size(max = 1) @SafeHtml String dashboardYn) {
              this.repId = repId;
              this.title = title;
              this.descr = descr;
              this.publicYn = publicYn;
              this.reportXml = reportXml;
              this.createId = createId;
              this.createDate = createDate;
              this.maintId = maintId;
              this.maintDate = maintDate;
              this.menuId = menuId;
              this.menuApprovedYn = menuApprovedYn;
              this.ownerId = ownerId;
              this.folderId = folderId;
              this.dashboardTypeYn = dashboardTypeYn;
              this.dashboardYn = dashboardYn;
       }

       @OneToMany(
               targetEntity = CrReportSchedule.class,
               mappedBy = "repId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrReportSchedule> crReportSchedules;
       @OneToMany(
               targetEntity = CrReportAccess.class,
               mappedBy = "repId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrReportAccess> crReportAccesses;
       @OneToMany(
               targetEntity = CrReportLog.class,
               mappedBy = "repId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrReportLog> crReportLogs;
       @OneToMany(
               targetEntity = CrReportEmailSentLog.class,
               mappedBy = "repId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrReportEmailSentLog> crReportEmailSentLogs;
       @OneToMany(
               targetEntity = CrReportFileHistory.class,
               mappedBy = "repId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrReportFileHistory> crReportFileHistories;

}
