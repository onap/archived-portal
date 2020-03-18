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

package org.onap.portal.domain.dto.transport;

import java.io.Serializable;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NamedNativeQuery(
    name = "GlobalRoleWithApplicationRoleFunction.getGlobalRoleForRequestedApp",
    query = "select distinct "
          + "    d.role_id as roleId, "
          + "    d.role_name as roleName, "
          + "    d.active_yn as active, "
          + "    d.priority as priority, "
          + "    c.function_cd as functionCd, "
          + "    e.function_name as functionName, "
          + "    c.app_id as appId, "
          + "    c.role_app_id as roleAppId"
          + "from fn_user_role a, fn_app b, ep_app_role_function c, fn_role d, ep_app_function e"
          + "    where b.app_id = c.app_id"
          + "    and a.app_id = c.role_app_id"
          + "    and b.enabled = 'Y' "
          + "    and c.role_id = d.role_id"
          + "    and d.active_yn='Y'"
          + "    and e.function_cd = c.function_cd"
          + "    and c.app_id=:requestedAppId "
          + "    and c.role_id =:roleId "
          + "    and e.app_id = c.app_id",
    resultSetMapping = "GlobalRoleWithApplicationRoleFunction"
)

@SqlResultSetMapping(
    name = "GlobalRoleWithApplicationRoleFunction",
    classes = @ConstructorResult(
        targetClass = GlobalRoleWithApplicationRoleFunction.class,
        columns = {
            @ColumnResult(name = "roleId"),
            @ColumnResult(name = "roleName"),
            @ColumnResult(name = "active"),
            @ColumnResult(name = "priority"),
            @ColumnResult(name = "functionCd"),
            @ColumnResult(name = "functionName"),
            @ColumnResult(name = "appId"),
            @ColumnResult(name = "roleAppId")
        }
    )
)


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalRoleWithApplicationRoleFunction implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long roleId;
  private String roleName;
  private Boolean active;
  private Integer priority;
  private String functionCd;
  private String functionName;
  private Long appId;
  private Long roleAppId;

}
