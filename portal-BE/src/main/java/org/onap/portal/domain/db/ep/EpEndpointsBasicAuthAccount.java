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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
CREATE TABLE `ep_endpoints_basic_auth_account` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `ep_id` int(11) DEFAULT NULL,
        `account_id` int(11) DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `ep_endpoints_basic_auth_account_account_id_fk` (`account_id`),
        KEY `ep_endpoints_basic_auth_account_ep_id_fk` (`ep_id`),
        CONSTRAINT `ep_endpoints_basic_auth_account_account_id_fk` FOREIGN KEY (`account_id`) REFERENCES `ep_basic_auth_account` (`id`),
        CONSTRAINT `ep_endpoints_basic_auth_account_ep_id_fk` FOREIGN KEY (`ep_id`) REFERENCES `ep_endpoints` (`id`)
        )
*/

@NamedQueries({
        @NamedQuery(
                name = "EpEndpointsBasicAuthAccount.deleteByAccountId",
                query = "FROM EpEndpointsBasicAuthAccount WHERE account_id = :accountId"),
        @NamedQuery(
                name = "EpEndpointsBasicAuthAccount.deleteByAccountIdAndEpId",
                query = "FROM EpEndpointsBasicAuthAccount WHERE account_id =:accountId AND ep_id =:epId")

})

@Table(name = "ep_endpoints_basic_auth_account", indexes = {
        @Index(name = "ep_endpoints_basic_auth_account_account_id_fk", columnList = "account_id"),
        @Index(name = "ep_endpoints_basic_auth_account_ep_id_fk", columnList = "ep_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpEndpointsBasicAuthAccount implements Serializable {
       @Id

  @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "ep_id", columnDefinition = "INT(11) DEFAULT NULL")
       @Valid
       private EpEndpoints epId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "account_id", columnDefinition = "INT(11) DEFAULT NULL")
       @Valid
       private EpBasicAuthAccount accountId;
}
