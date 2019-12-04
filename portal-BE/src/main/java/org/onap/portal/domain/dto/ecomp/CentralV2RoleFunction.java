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
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.DomainVo;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CentralV2RoleFunction extends DomainVo implements Serializable, Comparable {

       private static final long serialVersionUID = -4018975640065252688L;

       @SafeHtml
       private String code;
       @SafeHtml
       private String name;
       @JsonIgnore
       private Long appId;
       @JsonIgnore
       private Long roleId;
       private String type;
       @SafeHtml
       private String action;
       @SafeHtml
       private String editUrl;

       public CentralV2RoleFunction(Long id, String code, String name, Long appId, String type, String action,
               String editUrl) {
              super();
              super.setId(id);
              this.code = code;
              this.name = name;
              this.appId = appId;
              this.type = type;
              this.action = action;
              this.editUrl = editUrl;
       }

       public CentralV2RoleFunction(Long id, String code, String name, Long appId, String editUrl) {
              super();
              super.setId(id);
              this.code = code;
              this.name = name;
              this.appId = appId;
              this.editUrl = editUrl;
       }


       public CentralV2RoleFunction(String code, String name) {
              super();
              this.code = code;
              this.name = name;
       }

       public int compareTo(Object obj) {
              String c1 = getName();
              String c2 = ((CentralV2RoleFunction) obj).getName();

              return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
       }

}
