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

package org.onap.portal.domain.db.ep;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.db.fn.FnApp;

/*
CREATE TABLE `ep_app_function` (
        `app_id` int(11) NOT NULL,
        `function_cd` varchar(250) NOT NULL,
        `function_name` varchar(250) NOT NULL,
        PRIMARY KEY (`function_cd`,`app_id`),
        KEY `fk_ep_app_function_app_id` (`app_id`),
        CONSTRAINT `fk_ep_app_function_app_id` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "EpAppFunction.getAppRoleFunctionList",
        query = "from\n"
            + "  EpAppRoleFunction rf,\n"
            + "  EpAppFunction f\n"
            + " where\n"
            + "  rf.fnRole.id = :roleId\n"
            + "  and rf.appId.id = :appId\n"
            + "  and rf.appId.id = f.appId.id\n"
            + "  and rf.epAppFunction.functionCd = f.functionCd"
    )
})

@Table(name = "ep_app_function", indexes = {
    @Index(name = "fk_ep_app_function_app_id", columnList = "app_id"),
    @Index(name = "fk_ep_app_id_function_cd", columnList = "app_id, function_cd", unique = true)})

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EpAppFunction extends DomainVo implements Serializable {

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id", columnDefinition = "bigint")
  @Valid
  private FnApp appId;
  @Column(name = "function_cd", length = 250, nullable = false)
  @Size(max = 250)
  @NotNull
  @SafeHtml
  private String functionCd;
  @Column(name = "function_name", length = 250, nullable = false)
  @Size(max = 250)
  @NotNull
  @SafeHtml
  private String functionName;

  private Long roleId;
  private String type;
  @SafeHtml
  private String action;
  @SafeHtml
  private String editUrl;

  @OneToMany(
      targetEntity = EpAppRoleFunction.class,
      mappedBy = "epAppFunction",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<EpAppRoleFunction> epAppRoleFunctions;

  public EpAppFunction(Long id, String code, String name, FnApp appId, String type, String action, String editUrl) {
    super();
    super.setId(id);
    this.functionCd = code;
    this.functionName = name;
    this.appId = appId;
    this.type = type;
    this.action = action;
    this.editUrl = editUrl;
  }

  @Builder
  public EpAppFunction(@Digits(integer = 11, fraction = 0) Long id,
      LocalDateTime created, LocalDateTime modified, Long rowNum, Serializable auditUserId,
      DomainVo createdId, DomainVo modifiedId, Set<DomainVo> fnUsersCreatedId,
      Set<DomainVo> fnUsersModifiedId, @Valid FnApp appId,
      @Size(max = 250) @NotNull @SafeHtml String functionCd,
      @Size(max = 250) @NotNull @SafeHtml String functionName, Long roleId, String type,
      @SafeHtml String action, @SafeHtml String editUrl,
      Set<EpAppRoleFunction> epAppRoleFunctions) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId);
    this.appId = appId;
    this.functionCd = functionCd;
    this.functionName = functionName;
    this.roleId = roleId;
    this.type = type;
    this.action = action;
    this.editUrl = editUrl;
    this.epAppRoleFunctions = epAppRoleFunctions;
  }
}

