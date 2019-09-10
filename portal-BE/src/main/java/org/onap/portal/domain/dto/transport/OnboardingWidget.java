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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

@Getter
@Setter
@NoArgsConstructor
public class OnboardingWidget implements Serializable {

       private static final long serialVersionUID = 1L;

       private Long id;
       @SafeHtml
       private String name;
       private Long appId;
       @SafeHtml
       private String appName;
       private Integer width;
       private Integer height;
       @SafeHtml
       private String url;

       public OnboardingWidget(Long id, String name, Long appId,
               String appName, Integer width, Integer height,
               String url) {
              this.id = id;
              this.name = name;
              this.appId = appId;
              this.appName = appName;
              this.width = width;
              this.height = height;
              this.url = url;
       }

       public void normalize() {
              this.name = (this.name == null) ? "" : this.name.trim();
              this.appName = (this.appName == null) ? "" : this.appName.trim();
              if (this.width == null) {
                     this.width = 0;
              }
              if (this.height == null) {
                     this.height = 0;
              }
              this.url = (this.url == null) ? "" : this.url.trim();
       }

}
