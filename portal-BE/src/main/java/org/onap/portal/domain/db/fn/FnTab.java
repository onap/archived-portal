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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_tab` (
        `tab_cd` varchar(30) NOT NULL,
        `tab_name` varchar(50) NOT NULL,
        `tab_descr` varchar(100) DEFAULT NULL,
        `action` varchar(100) NOT NULL,
        `function_cd` varchar(30) NOT NULL,
        `active_yn` char(1) NOT NULL,
        `sort_order` decimal(11,0) NOT NULL,
        `parent_tab_cd` varchar(30) DEFAULT NULL,
        `tab_set_cd` varchar(30) DEFAULT NULL,
        PRIMARY KEY (`tab_cd`),
        KEY `fk_fn_tab_function_cd` (`function_cd`),
        KEY `fk_fn_tab_set_cd` (`tab_set_cd`),
        CONSTRAINT `fk_fn_tab_function_cd` FOREIGN KEY (`function_cd`) REFERENCES `fn_function` (`function_cd`),
        CONSTRAINT `fk_fn_tab_set_cd` FOREIGN KEY (`tab_set_cd`) REFERENCES `fn_lu_tab_set` (`tab_set_cd`)
        )
*/

@Table(name = "fn_tab", indexes = {
        @Index(name = "fk_fn_tab_function_cd", columnList = "function_cd"),
        @Index(name = "fk_fn_tab_set_cd", columnList = "tab_set_cd")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnTab {
       @Id
       @Column(name = "tab_cd", length = 30, nullable = false)
       @Size(max = 30)
       @SafeHtml
       private String tabCd;
       @Column(name = "tab_name", length = 50, nullable = false)
       @Size(max = 50)
       @SafeHtml
       @NotNull
       private String tabName;
       @Column(name = "tab_descr", length = 100, columnDefinition = "varchar(100) DEFAULT NULL")
       @Size(max = 100)
       @SafeHtml
       private String tabDescr;
       @Column(name = "action", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String action;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "function_cd", nullable = false)
       @NotNull
       @Valid
       private FnFunction functionCd;
       @Column(name = "active_yn", length = 1, nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String activeYn;
       @Column(name = "sort_order", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private BigInteger sortDrder;
       @Column(name = "parent_tab_cd", length = 30, columnDefinition = "varchar(30) DEFAULT NULL")
       @Size(max = 30)
       @SafeHtml
       private String parentTabCd;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "tab_set_cd", nullable = false)
       @NotNull
       @Valid
       private FnLuTabSet fnLuTabSet;
       @OneToMany(
               targetEntity = FnTabSelected.class,
               mappedBy = "selectedTabCd",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<FnTabSelected> selectedTabCd = new ArrayList<>();

}
