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

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

CREATE TABLE `cr_folder` (
        `folder_id` int(11) NOT NULL,
        `folder_name` varchar(50) NOT NULL,
        `descr` varchar(500) DEFAULT NULL,
        `create_id` int(11) NOT NULL,
        `create_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `parent_folder_id` int(11) DEFAULT NULL,
        `public_yn` varchar(1) NOT NULL DEFAULT 'n',
        PRIMARY KEY (`folder_id`),
        KEY `fk_parent_key_cr_folder` (`parent_folder_id`),
        CONSTRAINT `fk_parent_key_cr_folder` FOREIGN KEY (`parent_folder_id`) REFERENCES `cr_folder` (`folder_id`)
        )
*/


@Table(name = "cr_folder", indexes = {
        @Index(name = "fk_parent_key_cr_folder", columnList = "parent_folder_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrFolder {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "folder_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long folderId;
       @Column(name = "folder_name", length = 50, nullable = false)
       @Size(max = 50)
       @NotNull
       @SafeHtml
       private String folderName;
       @Column(name = "descr", length = 500, columnDefinition = "varchar(500) DEFAULT NULL")
       @Size(max = 500)
       @SafeHtml
       private String descr;
       @Column(name = "create_id", length = 50, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long createId;
       @Column(name = "create_date", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       @NotNull
       private LocalDateTime createDate;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "parent_Folder_Id")
       private CrFolder parentFolderId;
       @Column(name = "public_Yn", length = 1, nullable = false, columnDefinition = "varchar(1) DEFAULT 'n'")
       @Pattern(regexp = "[YNyn]")
       @SafeHtml
       @NotNull
       private String publicYn;
       @OneToMany(
               targetEntity = CrFolder.class,
               mappedBy = "parentFolderId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<CrFolder> crFolders;
}
