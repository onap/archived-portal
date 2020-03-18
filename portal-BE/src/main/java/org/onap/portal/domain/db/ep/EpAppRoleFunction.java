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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnRole;

/*
CREATE TABLE `ep_app_role_function` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `app_id` int(11) NOT NULL,
        `role_id` int(11) NOT NULL,
        `function_cd` varchar(250) NOT NULL,
        `role_app_id` varchar(20) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UNIQUE KEY` (`app_id`,`role_id`,`function_cd`),
        KEY `fk_ep_app_role_function_ep_app_func` (`app_id`,`function_cd`),
        KEY `fk_ep_app_role_function_role_id` (`role_id`),
        CONSTRAINT `fk_ep_app_role_function_app_id` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`),
        CONSTRAINT `fk_ep_app_role_function_ep_app_func` FOREIGN KEY (`app_id`, `function_cd`) REFERENCES `ep_app_function` (`app_id`, `function_cd`),
        CONSTRAINT `fk_ep_app_role_function_role_id` FOREIGN KEY (`role_id`) REFERENCES `fn_role` (`role_id`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "EpAppRoleFunction.getAppRoleFunctionOnRoleIdAndAppId",
        query = "from EpAppRoleFunction where appId.id = :appId and fnRole.id = :roleId"
    )
})

@Table(name = "ep_app_role_function", indexes = {
    @Index(name = "fk_ep_app_role_function_ep_app_func_role_id", columnList = "app_id, role_id, function_cd", unique = true),
    @Index(name = "fk_ep_app_role_function_ep_app_func", columnList = "app_id, function_cd"),
    @Index(name = "fk_ep_app_role_function_role_id", columnList = "role_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class EpAppRoleFunction implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
  @Digits(integer = 11, fraction = 0)
  private Integer id;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "app_id", insertable = false, updatable = false, columnDefinition = "bigint")
  @Valid
  @NotNull
  private FnApp appId;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", columnDefinition = "bigint")
  @Valid
  @NotNull
  private FnRole fnRole;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumns({
      @JoinColumn(name = "app_id", referencedColumnName = "app_id"),
      @JoinColumn(name = "function_cd", referencedColumnName = "function_cd")
  })
  @Valid
  @NotNull
  private EpAppFunction epAppFunction;
  @Column(name = "role_app_id", length = 20)
  @Digits(integer = 20, fraction = 0)
  @SafeHtml
  private String roleAppId;
}
