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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `ep_microservice_parameter` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `service_id` int(11) DEFAULT NULL,
        `para_key` varchar(50) DEFAULT NULL,
        `para_value` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `FK_EP_MICROSERIVCE_EP_MICROSERVICE_PARAMETER` (`service_id`),
        CONSTRAINT `FK_EP_MICROSERIVCE_EP_MICROSERVICE_PARAMETER` FOREIGN KEY (`service_id`) REFERENCES `ep_microservice` (`id`)
        )
*/

@NamedQueries({
        @NamedQuery(
                name = "EpMicroserviceParameter.deleteByServiceId",
                query = "FROM EpMicroserviceParameter WHERE service_id =:serviceId")
})

@Table(name = "ep_microservice_parameter", indexes = {
        @Index(name = "FK_EP_MICROSERIVCE_EP_MICROSERVICE_PARAMETER", columnList = "service_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpMicroserviceParameter implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "service_id")
       private EpMicroservice serviceId;
       @Column(name = "para_key", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String paraKey;
       @Column(name = "para_value", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String paraValue;
       @OneToMany(
               targetEntity = EpWidgetCatalogParameter.class,
               mappedBy = "paramId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpWidgetCatalogParameter> epWidgetCatalogParameter;

}
