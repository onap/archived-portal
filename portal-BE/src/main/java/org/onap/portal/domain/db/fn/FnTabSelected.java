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

package org.onap.portal.domain.db.fn;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnTabSelected.FnTabSelectedId;

/*
CREATE TABLE `fn_tab_selected` (
        `selected_tab_cd` varchar(30) NOT NULL,
        `tab_uri` varchar(40) NOT NULL,
        PRIMARY KEY (`selected_tab_cd`,`tab_uri`),
        CONSTRAINT `fk_fn_tab_selected_tab_cd` FOREIGN KEY (`selected_tab_cd`) REFERENCES `fn_tab` (`tab_cd`)
        )
*/

@Table(name = "fn_tab_selected")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@IdClass(FnTabSelectedId.class)
public class FnTabSelected {
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "selected_tab_cd", nullable = false)
       @NotNull
       @Valid
       @Id
       private FnTab selectedTabCd;
       @Column(name = "tab_uri", length = 40, nullable = false)
       @Size(max = 40)
       @SafeHtml
       @Id
       private String tab_uri;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnTabSelectedId implements Serializable {
              @Valid
              private FnTab selectedTabCd;
              @Size(max = 40)
              @SafeHtml
              private String tab_uri;
       }
}