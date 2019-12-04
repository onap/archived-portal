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

package org.onap.portal.domain.dto.ecomp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portal.domain.db.DomainVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EPRole extends DomainVo {

       private static final long serialVersionUID = 1L;
       @SafeHtml
       private String name;
       private boolean active;
       private Integer priority;
       private Long appId;     // used by ONAP only
       private Long appRoleId; // used by ONAP only
       private SortedSet<RoleFunction> roleFunctions = new TreeSet<>();
       @Valid
       private SortedSet<EPRole> childRoles = new TreeSet<>();
       @JsonIgnore
       private SortedSet<EPRole> parentRoles = new TreeSet<>();


       public void addRoleFunction(RoleFunction roleFunction) {
              this.roleFunctions.add(roleFunction);
       }

       public void addChildRole(EPRole role) {
              this.childRoles.add(role);
       }

       public void addParentRole(EPRole role) {
              this.parentRoles.add(role);
       }

       public String getEditUrl() {
              return "/role.htm?role_id=" + getId();
       }

       public String getToggleActiveImage() {
              return "/static/fusion/images/" + (isActive() ? "active.png" : "inactive.png");
       }

       public String getToggleActiveAltText() {
              return isActive() ? "Click to Deactivate Role" : "Click to Activate Role";
       }

       public void removeChildRole(Long roleId) {

              for (EPRole childRole : this.childRoles) {
                     if (childRole.getId().equals(roleId)) {
                            this.childRoles.remove(childRole);
                            break;
                     }
              }
       }

       public void removeParentRole(Long roleId) {

              for (EPRole parentRole : this.parentRoles) {
                     if (parentRole.getId().equals(roleId)) {
                            this.parentRoles.remove(parentRole);
                            break;
                     }
              }
       }

       public void removeRoleFunction(String roleFunctionCd) {

              for (RoleFunction roleFunction : this.roleFunctions) {
                     if (roleFunction.getCode().equals(roleFunctionCd)) {
                            this.roleFunctions.remove(roleFunction);
                            break;
                     }
              }
       }

       public int compareTo(Object obj) {
              EPRole other = (EPRole) obj;

              if (this.appId == null) {
                     if (other.getAppId() == null) {
                            return compareByName(other); //equal
                     } else {
                            return -1;
                     }
              } else if (other.getAppId() == null) {
                     return 1;
              } else {
                     int appIdCompareResult = appId.compareTo(other.getAppId());
                     return appIdCompareResult == 0 ? compareByName(other) : appIdCompareResult;
              }
       }

       private int compareByName(EPRole other) {
              String c1 = getName();
              String c2 = other.getName();

              return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
       }

       @Override
       public String toString() {
              return "[Id = " + super.getId() + ", name = " + name + "]";
       }

}
