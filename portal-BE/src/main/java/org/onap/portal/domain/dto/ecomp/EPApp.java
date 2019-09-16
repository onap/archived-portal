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

import javax.persistence.Lob;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.dto.DomainVo;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class EPApp extends DomainVo {

       private static final long serialVersionUID = 1L;

       @SafeHtml
       private String name;
       @SafeHtml
       private String imageUrl;
       @SafeHtml
       private String description;
       @SafeHtml
       private String notes;
       @SafeHtml
       private String url;
       @SafeHtml
       private String alternateUrl;
       @SafeHtml
       private String appRestEndpoint;
       @SafeHtml
       private String mlAppName;
       @SafeHtml
       private String mlAppAdminId;
       private Long motsId;
       @SafeHtml
       private String username;
       @SafeHtml
       private String appPassword;
       @Lob
       private byte[] thumbnail;
       private Boolean open;
       private Boolean enabled;
       @SafeHtml
       private String uebTopicName;
       @SafeHtml
       private String uebKey;
       @SafeHtml
       private String uebSecret;
       private Integer appType;
       @Valid
       private AppContactUs contactUs;
       private Boolean centralAuth;
       @SafeHtml
       private String nameSpace;

       public EPApp() {
              this.name = "";
              this.mlAppName = "";
              this.mlAppAdminId = "";
              this.username = "";
              this.appPassword = "";
              this.open = Boolean.FALSE;
              this.enabled = Boolean.TRUE;
              this.uebTopicName = "";
              this.uebKey = "";
              this.uebSecret = "";
              this.appType = 1;
       }


       public void setName(String name) {
              if (StringUtils.isEmpty(name)) {
                     name = "";
              }
              this.name = name;
       }

       public void setMlAppName(String mlAppName) {
              if (StringUtils.isEmpty(mlAppName)) {
                     mlAppName = "";
              }
              this.mlAppName = mlAppName;
       }

       public void setMlAppAdminId(String mlAppAdminId) {
              if (StringUtils.isEmpty(mlAppAdminId)) {
                     mlAppAdminId = "";
              }
              this.mlAppAdminId = mlAppAdminId;
       }


       public void setAppPassword(String appPassword) {
              if (StringUtils.isEmpty(appPassword)) {
                     appPassword = "";
              }
              this.appPassword = appPassword;
       }

       public void setOpen(Boolean open) {
              if (open == null) {
                     open = Boolean.FALSE;
              }
              this.open = open;
       }

       public void setEnabled(Boolean enabled) {
              if (enabled == null) {
                     enabled = Boolean.TRUE;
              }
              this.enabled = enabled;
       }

       public void setAppType(Integer appType) {
              if (appType == null) {
                     appType = 1;
              }
              this.appType = appType;
       }

       public void setRestrictedApp(Boolean restrictedApp) {
              Integer result = 1;
              if (restrictedApp) {
                     result = 2;
              }
              this.appType = result;
       }

       public Boolean isRestrictedApp() {
              return (this.appType == 2);
       }

       public int compareTo(Object obj) {
              Long c1 = getId();
              Long c2 = ((EPApp) obj).getId();

              return c1.compareTo(c2);
       }

       public void setUebTopicName(String topicName) {
              if (StringUtils.isEmpty(topicName)) {
                     this.uebTopicName = "";
              }
              this.uebTopicName = topicName;
       }

       public void setUebKey(String uebKey) {
              if (StringUtils.isEmpty(uebKey)) {
                     this.uebKey = "";
              }
              this.uebKey = uebKey;
       }


       public void setUebSecret(String uebSecret) {
              if (StringUtils.isEmpty(uebSecret)) {
                     this.uebSecret = "";
              }
              this.uebSecret = uebSecret;
       }

       public void setCentralAuth(Boolean centralAuth) {
              if (centralAuth == null) {
                     centralAuth = Boolean.FALSE;
              }
              this.centralAuth = centralAuth;
       }

       public void setNameSpace(String nameSpace) {
              if (StringUtils.isEmpty(nameSpace)) {
                     nameSpace = null;
              }
              this.nameSpace = nameSpace;
       }

       @Override
       public String toString() {
              return "[" + getId() + ":" + getName() + "]";
       }

}
