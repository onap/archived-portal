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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Iterator;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.fn.FnRoleComposite;
import org.onap.portal.domain.db.fn.FnRoleFunction;
import org.onap.portal.domain.dto.DomainVo;
import org.onap.portalsdk.core.domain.RoleFunction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends DomainVo {

  private static final long serialVersionUID = 1L;

  private String name;
  private boolean active;
  private Integer priority;
  private Set<FnRoleFunction> roleFunctions;
  private Set<FnRoleComposite> childRoles;
  @JsonIgnore
  private Set<FnRoleComposite> parentRoles;

  public String getEditUrl() {
    return "/role.htm?role_id=" + this.getId();
  }

  public String getToggleActiveImage() {
    return "/static/fusion/images/" + (this.isActive() ? "active.png" : "inactive.png");
  }

  public String getToggleActiveAltText() {
    return this.isActive() ? "Click to Deactivate Role" : "Click to Activate Role";
  }

  public void removeChildRole(Long roleId) {
    Iterator i = this.childRoles.iterator();

    while (i.hasNext()) {
      org.onap.portalsdk.core.domain.Role childRole = (org.onap.portalsdk.core.domain.Role) i.next();
      if (childRole.getId().equals(roleId)) {
        this.childRoles.remove(childRole);
        break;
      }
    }

  }

  public void removeParentRole(Long roleId) {

    for (Object role : this.parentRoles) {
      org.onap.portalsdk.core.domain.Role parentRole = (org.onap.portalsdk.core.domain.Role) role;
      if (parentRole.getId().equals(roleId)) {
        this.parentRoles.remove(parentRole);
        break;
      }
    }

  }

  public void removeRoleFunction(String roleFunctionCd) {

    for (Object function : this.roleFunctions) {
      RoleFunction roleFunction = (RoleFunction) function;
      if (roleFunction.getCode().equals(roleFunctionCd)) {
        this.roleFunctions.remove(roleFunction);
        break;
      }
    }

  }

  public int compareTo(Object obj) {
    String c1 = this.getName();
    String c2 = ((org.onap.portalsdk.core.domain.Role) obj).getName();
    return c1 != null && c2 != null ? c1.compareTo(c2) : 1;
  }
}
