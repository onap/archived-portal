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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.onap.portalapp.portal.domain.db.fn.FnApp;

/*
CREATE TABLE `ep_microservice` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `name` varchar(50) DEFAULT NULL,
        `description` varchar(50) DEFAULT NULL,
        `appId` int(11) DEFAULT NULL,
        `endpoint_url` varchar(200) DEFAULT NULL,
        `security_type` varchar(50) DEFAULT NULL,
        `username` varchar(50) DEFAULT NULL,
        `password` varchar(50) NOT NULL,
        `active` char(1) NOT NULL DEFAULT 'Y',
        PRIMARY KEY (`id`),
        KEY `FK_FN_APP_EP_MICROSERVICE` (`appId`),
        CONSTRAINT `FK_FN_APP_EP_MICROSERVICE` FOREIGN KEY (`appId`) REFERENCES `fn_app` (`app_id`)
        )
*/

@Table(name = "ep_microservice", indexes = {
        @Index(name = "FK_FN_APP_EP_MICROSERVICE", columnList = "app_Id")
})
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class EpMicroservice {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @Column(name = "name", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String name;
       @Column(name = "description", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String description;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_Id")
       @Valid
       private FnApp appId;
       @Column(name = "endpoint_url", length = 200)
       @Size(max = 200)
       @SafeHtml
       //TODO URL
       @URL
       private String endpointUrl;
       @Column(name = "security_type", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String securityType;
       @Column(name = "username", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String username;
       @Column(name = "password", length = 50, nullable = false)
       @Size(max = 50)
       @SafeHtml
       private String password;
       @Column(name = "active", length = 1, columnDefinition = "CHAR(1) DEFAULT 'Y'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String active;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       private List<EpWidgetCatalog> epWidgetCatalogList = new ArrayList<>();
       @OneToMany(
               targetEntity = EpMicroserviceParameter.class,
               mappedBy = "serviceId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<EpMicroserviceParameter> epMicroserviceParameters = new ArrayList<>();

       public void copyOf(final EpMicroservice epMicroservice) {
              this.id = epMicroservice.getId();
              this.name = epMicroservice.getName();
              this.description = epMicroservice.getDescription();
              this.appId = epMicroservice.getAppId();
              this.endpointUrl = epMicroservice.getEndpointUrl();
              this.securityType = epMicroservice.getSecurityType();
              this.username = epMicroservice.getUsername();
              this.password = epMicroservice.getPassword();
              this.active = epMicroservice.getActive();
              this.epWidgetCatalogList = epMicroservice.getEpWidgetCatalogList();
              this.epMicroserviceParameters = epMicroservice.getEpMicroserviceParameters();
       }
}
