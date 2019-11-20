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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.ep.EpAppFunction.EpAppFunctionId;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.dto.DomainVo;

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
            + "  rf.fnRole.roleId = :roleId\n"
            + "  and rf.appId.appId = :appId\n"
            + "  and rf.appId.appId = f.appId.appId\n"
            + "  and rf.epAppFunction.functionCd = f.functionCd"
    )
})

@Table(name = "ep_app_function", indexes = {@Index(name = "fk_ep_app_function_app_id", columnList = "app_id")})

@Getter
@Setter
@Entity
@IdClass(EpAppFunctionId.class)
@NoArgsConstructor
@AllArgsConstructor
public class EpAppFunction extends DomainVo implements Serializable {

  @Id
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id")
  @Valid
  private FnApp appId;
  @Id
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
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  private Set<EpAppRoleFunction> epAppRoleFunctions;

  public EpAppFunction(Long id, String code, String name, FnApp appId, String type, String action, String editUrl) {
    super();
    this.id = id;
    this.functionCd = code;
    this.functionName = name;
    this.appId = appId;
    this.type = type;
    this.action = action;
    this.editUrl = editUrl;
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EpAppFunctionId implements Serializable {

    @Valid
    private FnApp appId;
    @Size(max = 250)
    @NotNull
    @SafeHtml
    private String functionCd;
  }
}

