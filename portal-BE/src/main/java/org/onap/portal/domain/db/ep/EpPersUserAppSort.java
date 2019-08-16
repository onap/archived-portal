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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
import org.onap.portalapp.portal.domain.db.fn.FnUser;

/*
CREATE TABLE `ep_pers_user_app_sort` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `user_id` int(11) NOT NULL,
        `sort_pref` char(1) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `uk_1_ep_pers_user_app_sort` (`user_id`),
        CONSTRAINT `fk_ep_pers_user_app_sort_fn_user` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`)
        )
*/

@NamedQueries({
        @NamedQuery(
                name = "EpPersUserAppSort.retrieveByUserId",
                query = "from EpPersUserAppSort where user_id = :userId"
        )
})

@Table(name = "ep_pers_user_app_sort", indexes = {
        @Index(name = "uk_1_ep_pers_user_app_sort", columnList = "user_id", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpPersUserAppSort {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "user_id", unique = true, nullable = false)
       @NotNull
       @Valid
       private FnUser userID;
       @Column(name = "sort_pref", length = 1, nullable = false)
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String sortPref;
}
