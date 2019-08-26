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

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnUser;

/*
CREATE TABLE `ep_user_roles_request` (
        `req_id` int(11) NOT NULL AUTO_INCREMENT,
        `user_id` int(11) NOT NULL,
        `app_id` int(11) NOT NULL,
        `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `updated_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `request_status` varchar(50) NOT NULL,
        PRIMARY KEY (`req_id`),
        KEY `fk_user_roles_req_fn_user` (`user_id`),
        KEY `fk_user_roles_req_fn_app` (`app_id`),
        CONSTRAINT `fk_user_roles_req_fn_app` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`),
        CONSTRAINT `fk_user_roles_req_fn_user` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`)
        )
*/

@Table(name = "ep_user_roles_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpUserRolesRequest {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "req_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long reqId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "user_id", nullable = false)
       @NotNull
       @Valid
       private FnUser userId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_id", nullable = false)
       @NotNull
       @Valid
       private FnApp appId;
       @Column(name = "created_date", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       private LocalDateTime createdDate;
       @Column(name = "updated_date", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       private LocalDateTime updatedDate;
       @Column(name = "request_status", nullable = false, length = 50)
       @Size(max = 50)
       @NotNull
       @SafeHtml
       private String requestStatus;
       @OneToMany(
               targetEntity = EpUserRolesRequestDet.class,
               mappedBy = "reqId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<EpUserRolesRequestDet> epUserRolesRequestDets;
}
