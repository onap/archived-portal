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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.fn.FnRoleFunction.FnRoleFunctionId;

/*
CREATE TABLE `fn_role_function` (
        `role_id` int(11) NOT NULL,
        `function_cd` varchar(30) NOT NULL,
        PRIMARY KEY (`role_id`,`function_cd`),
        KEY `fn_role_function_function_cd` (`function_cd`) USING BTREE,
        KEY `fn_role_function_role_id` (`role_id`) USING BTREE,
        CONSTRAINT `fk_fn_role__ref_198_fn_role` FOREIGN KEY (`role_id`) REFERENCES `fn_role` (`role_id`),
        CONSTRAINT `fk_fn_role__ref_201_fn_funct` FOREIGN KEY (`function_cd`) REFERENCES `fn_function` (`function_cd`)
        )
*/

@Table(name = "fn_role_function", indexes = {
        @Index(name = "fn_role_function_function_cd", columnList = "function_cd"),
        @Index(name = "fn_role_function_role_id", columnList = "role_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@IdClass(FnRoleFunctionId.class)
public class FnRoleFunction implements Serializable{

       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "role_Id", nullable = false)
       @Valid
       @NotNull
       @Id
       private FnRole roleId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "function_cd", nullable = false)
       @Valid
       @NotNull
       @Id
       private FnFunction functionCd;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnRoleFunctionId implements Serializable {
              @Valid
              private FnRole roleId;
              @Valid
              private FnFunction functionCd;
       }
}
