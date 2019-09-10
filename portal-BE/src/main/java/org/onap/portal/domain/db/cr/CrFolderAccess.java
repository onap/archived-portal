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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*

CREATE TABLE `cr_folder_access` (
        `folder_access_id` decimal(11,0) NOT NULL,
        `folder_id` decimal(11,0) NOT NULL,
        `order_no` decimal(11,0) NOT NULL,
        `role_id` decimal(11,0) DEFAULT NULL,
        `user_id` decimal(11,0) DEFAULT NULL,
        `read_only_yn` varchar(1) NOT NULL DEFAULT 'n',
        PRIMARY KEY (`folder_access_id`)
        )
*/


@Table(name = "cr_folder_access")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CrFolderAccess implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "folder_access_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long folderAccessId;
       @Column(name = "folder_id", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       @NotNull
       private Long folderId;
       @Column(name = "order_no", nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       @NotNull
       private Long orderNo;
       @Column(name = "role_id")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long roleId;
       @Column(name = "user_id")
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long userId;
       @Column(name = "read_only_yn", length = 1, nullable = false, columnDefinition = "varchar(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @SafeHtml
       @NotNull
       private String readOnlyYn;
}
