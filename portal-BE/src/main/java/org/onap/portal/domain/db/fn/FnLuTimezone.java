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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_lu_timezone` (
        `timezone_id` int(11) NOT NULL,
        `timezone_name` varchar(100) NOT NULL,
        `timezone_value` varchar(100) NOT NULL,
        PRIMARY KEY (`timezone_id`)
        )
*/

@Table(name = "fn_lu_timezone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnLuTimezone implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "timezone_id", length = 11, nullable = false)
       private Long timezoneId;
       @Column(name = "timezone_name", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String timezone_name;
       @Column(name = "timezone_value", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String timezone_value;

       @OneToMany(
               targetEntity = FnUser.class,
               mappedBy = "timezone",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnUser> fnUsers;

}
