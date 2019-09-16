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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnRole;

/*
CREATE TABLE `ep_user_roles_request_det` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `req_id` int(11) DEFAULT NULL,
        `requested_role_id` int(10) NOT NULL,
        `request_type` varchar(10) NOT NULL,
        PRIMARY KEY (`id`),
        KEY `fk_user_roles_req_fn_req_id` (`req_id`),
        KEY `fk_user_roles_req_fn_role_id` (`requested_role_id`),
        CONSTRAINT `fk_user_roles_req_fn_req_id` FOREIGN KEY (`req_id`) REFERENCES `ep_user_roles_request` (`req_id`),
        CONSTRAINT `fk_user_roles_req_fn_role_id` FOREIGN KEY (`requested_role_id`) REFERENCES `fn_role` (`role_id`)
        )
*/

@Table(name = "ep_user_roles_request_det", indexes = {
        @Index(name = "fk_user_roles_req_fn_req_id", columnList = "req_id"),
        @Index(name = "fk_user_roles_req_fn_role_id", columnList = "requested_role_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpUserRolesRequestDet implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "req_id", columnDefinition = "int(11) default null")
       @Valid
       private EpUserRolesRequest reqId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "requested_role_id", nullable = false)
       @NotNull
       @Valid
       private FnRole requestedRoleId;
       @Column(name = "request_type", length = 10, nullable = false)
       @Size(max = 10)
       @NotNull
       @SafeHtml
       private String requestType;

}
