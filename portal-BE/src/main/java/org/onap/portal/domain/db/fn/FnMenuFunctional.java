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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.hibernate.validator.constraints.URL;

/*
CREATE TABLE `fn_menu_functional` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `column_num` int(2) NOT NULL,
  `text` varchar(100) NOT NULL,
  `parent_menu_id` int(11) DEFAULT NULL,
  `url` varchar(128) NOT NULL DEFAULT '',
  `active_yn` varchar(1) NOT NULL DEFAULT 'y',
  `image_src` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `fk_fn_menu_func_parent_menu_id_idx` (`parent_menu_id`),
  CONSTRAINT `fk_fn_menu_func_parent_menu_id` FOREIGN KEY (`parent_menu_id`) REFERENCES `fn_menu_functional` (`menu_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)
*/

@Table(name = "fn_menu_functional", indexes = {@Index(columnList = "parent_menu_id", name = "fk_fn_menu_func_parent_menu_id_idx")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnMenuFunctional {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "menu_id", nullable = false, length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long menuId;
       @Column(name = "column_num", nullable = false, length = 2)
       @Digits(integer = 2, fraction = 0)
       private Long columnNum;
       @Column(name = "text", length = 100, nullable = false)
       @Size(max = 100)
       @SafeHtml
       @NotNull
       private String text;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "parent_menu_id")
       @Valid
       private FnMenuFunctional parentMenuId;
       @Column(name = "url", length = 128, nullable = false, columnDefinition = "varchar(128) default ''")
       @Size(max = 128)
       @SafeHtml
       @NotNull
       //TODO URL
       @URL
       private String url;
       @Column(name = "active_yn", length = 1, columnDefinition = "varchar(1) default 'Y'", nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String activeYn;
       @Column(name = "image_src", length = 100, columnDefinition = "varchar(100) default null")
       @Size(max = 100)
       @SafeHtml
       private String imageSrc;
       @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinTable(
               name = "fn_menu_favorites",
               joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "menu_id")},
               inverseJoinColumns = {@JoinColumn(name = "role_Id", referencedColumnName = "user_id")},
               indexes = {
                       @Index(name = "sys_c0014619", columnList = "menu_id")
               }
       )
       private List<FnUser> fnUsers = new ArrayList<>();
       @OneToMany(
               targetEntity = FnMenuFunctionalAncestors.class,
               mappedBy = "menuId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<FnMenuFunctionalAncestors> fnMenuFunctionalAncestorsMenuId = new ArrayList<>();
       @OneToMany(
               targetEntity = FnMenuFunctionalAncestors.class,
               mappedBy = "ancestorMenuId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<FnMenuFunctionalAncestors> fnMenuFunctionalsAncestorMenuId = new ArrayList<>();
       @OneToMany(
               targetEntity = FnMenuFunctionalRoles.class,
               mappedBy = "menuId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<FnMenuFunctionalRoles> fnMenuFunctionalRoles = new ArrayList<>();
}
