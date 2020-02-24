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
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.dto.transport.Role;

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
    @Index(name = "fn_role_function_role_id", columnList = "role_id"),
    @Index(name = "fn_role_function_roleId_functionCd", columnList = "role_id, function_cd", unique = true)})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnRoleFunction extends DomainVo implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", nullable = false, columnDefinition = "bigint")
  @Valid
  @NotNull
  private FnRole role;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "function_cd", nullable = false, columnDefinition = "varchar(255) not null")
  @Valid
  @NotNull
  private FnFunction functionCd;

  @Builder
  public FnRoleFunction(@Digits(integer = 11, fraction = 0) Long id,
      LocalDateTime created, LocalDateTime modified, Long rowNum, Serializable auditUserId,
      DomainVo createdId, DomainVo modifiedId, Set<DomainVo> fnUsersCreatedId,
      Set<DomainVo> fnUsersModifiedId,
      @Valid @NotNull FnRole role,
      @Valid @NotNull FnFunction functionCd) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId);
    this.role = role;
    this.functionCd = functionCd;
  }
}
