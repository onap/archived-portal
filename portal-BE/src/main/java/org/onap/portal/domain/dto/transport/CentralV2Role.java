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
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.fn.FnRoleFunction;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CentralV2Role implements Serializable, Comparable {

  private static final long serialVersionUID = -4332644961113063714L;

  private Long id;
  private LocalDateTime created;
  private LocalDateTime modified;
  private Long createdId;
  private Long modifiedId;
  private Long rowNum;
  private String name;
  private boolean active;
  private Integer priority;
  @Builder.Default
  private SortedSet<FnRoleFunction> roleFunctions = new TreeSet<>();
  @Builder.Default
  private SortedSet<CentralV2Role> childRoles = new TreeSet<>();
  @Builder.Default
  private SortedSet<CentralV2Role> parentRoles = new TreeSet<>();

  public CentralV2Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public void addRoleFunction(FnRoleFunction roleFunction) {
    this.roleFunctions.add(roleFunction);
  }

  public void addChildRole(CentralV2Role role) {
    this.childRoles.add(role);
  }

  public void addParentRole(CentralV2Role role) {
    this.parentRoles.add(role);
  }

  @Override
  public int compareTo(Object obj) {
    CentralV2Role other = (CentralV2Role) obj;

    String c1 = getName();
    String c2 = other.getName();

    return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
  }

}
