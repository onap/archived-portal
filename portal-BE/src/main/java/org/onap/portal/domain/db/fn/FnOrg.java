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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_org` (
        `org_id` int(11) NOT NULL,
        `org_name` varchar(50) NOT NULL,
        `access_cd` varchar(10) DEFAULT NULL,
        PRIMARY KEY (`org_id`),
        KEY `fn_org_access_cd` (`access_cd`) USING BTREE
        )
*/

@Table(name = "fn_org", indexes = {
        @Index(name = "fn_org_access_cd", columnList = "access_cd")
})
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
public class FnOrg implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "org_id", nullable = false, length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long orgId;
       @Column(name = "org_name", length = 50, nullable = false)
       @Size(max = 50)
       @SafeHtml
       @NotNull
       private String orgName;
       @Column(name = "access_cd", length = 10, columnDefinition = "varchar(10) DEFAULT NULL")
       @Size(max = 10)
       @SafeHtml
       private String accessCd;

       @OneToMany(
               targetEntity = FnUser.class,
               mappedBy = "orgId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUser> fnUsers;
}
