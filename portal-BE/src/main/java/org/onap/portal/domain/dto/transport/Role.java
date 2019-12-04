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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnRoleComposite;
import org.onap.portal.domain.db.fn.FnRoleFunction;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portalsdk.core.domain.RoleFunction;

@Table(name = "role", indexes = {
    @Index(name = "fn_role_name_app_id_idx", columnList = "role_name, app_id", unique = true)
})
@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Role extends DomainVo {

  private static final long serialVersionUID = 1L;

  @Column(name = "role_name", length = 300, nullable = false)
  @Size(max = 300)
  @NotNull
  @SafeHtml
  private String roleName;
  @Column(name = "app_Id", length = 11, columnDefinition = "int(11) default null")
  @Digits(integer = 11, fraction = 0)
  private Long appId;
  @Column(name = "active_yn", length = 1, columnDefinition = "boolean default true", nullable = false)
  @NotNull
  private Boolean activeYn = true;
  @Column(name = "priority", length = 4, columnDefinition = "decimal(4,0) DEFAULT NULL")
  @Digits(integer = 4, fraction = 0)
  private Integer priority;
  @OneToMany(
      targetEntity = FnRoleFunction.class,
      mappedBy = "role",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnRoleFunction> fnRoleFunctions;
  @OneToMany(
      targetEntity = FnRoleComposite.class,
      mappedBy = "childRoles",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnRoleComposite> childRoles;
  @JsonIgnore
  @OneToMany(
      targetEntity = FnRoleComposite.class,
      mappedBy = "parentRoles",
      cascade = CascadeType.MERGE,
      fetch = FetchType.LAZY
  )
  private Set<FnRoleComposite> parentRoles;

  public String getEditUrl() {
    return "/role.htm?role_id=" + this.getId();
  }

  public String getToggleActiveImage() {
    return "/static/fusion/images/" + (this.activeYn ? "active.png" : "inactive.png");
  }

  public String getToggleActiveAltText() {
    return this.activeYn ? "Click to Deactivate Role" : "Click to Activate Role";
  }

  public void removeChildRole(Long roleId) {

    for (FnRoleComposite role : this.childRoles) {
      Role childRole = role.getChildRoles();
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

    for (Object function : this.fnRoleFunctions) {
      RoleFunction roleFunction = (RoleFunction) function;
      if (roleFunction.getCode().equals(roleFunctionCd)) {
        this.fnRoleFunctions.remove(roleFunction);
        break;
      }
    }

  }

  public int compareTo(Object obj) {
    String c1 = this.getRoleName();
    String c2 = ((org.onap.portalsdk.core.domain.Role) obj).getName();
    return c1 != null && c2 != null ? c1.compareTo(c2) : 1;
  }

  public Role( Long id, LocalDateTime created,
      LocalDateTime modified, Long rowNum, Serializable auditUserId,
      DomainVo createdId, DomainVo modifiedId, Set<DomainVo> fnUsersCreatedId,
      Set<DomainVo> fnUsersModifiedId, String roleName, Long appId, Boolean activeYn, Integer priority,
      Set<FnRoleFunction> fnRoleFunctions, Set<FnRoleComposite> childRoles,
      Set<FnRoleComposite> parentRoles) {
    super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId, fnUsersModifiedId);
    this.roleName = roleName;
    this.appId = appId;
    this.activeYn = activeYn;
    this.priority = priority;
    this.fnRoleFunctions = fnRoleFunctions;
    this.childRoles = childRoles;
    this.parentRoles = parentRoles;
  }
}
