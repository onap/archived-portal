/*-
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

package org.onap.portal.domain.db.cr;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.cr.CrFavoriteReports.CrFavoriteReportsId;


/*

CREATE TABLE `cr_favorite_reports` (
        `user_id` int(11) NOT NULL,
        `rep_id` int(11) NOT NULL,
        PRIMARY KEY (`user_id`,`rep_id`)
        )
*/


@Table(name = "cr_favorite_reports")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@IdClass(CrFavoriteReportsId.class)
@Entity
public class CrFavoriteReports {
       @Id
       @Column(name = "user_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long userId;
       @Id
       @Column(name = "rep_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @Positive
       private Long repId;

       @Getter
       @Setter
       @EqualsAndHashCode
       @AllArgsConstructor
       @NoArgsConstructor
       public static class CrFavoriteReportsId implements Serializable {
              private Long userId;
              private Long repId;
       }
}

