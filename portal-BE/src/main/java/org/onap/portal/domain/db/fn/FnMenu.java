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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import org.hibernate.validator.constraints.URL;

/*
CREATE TABLE `fn_menu` (
        `menu_id` int(11) NOT NULL AUTO_INCREMENT,
        `label` varchar(100) DEFAULT NULL,
        `parent_id` int(11) DEFAULT NULL,
        `sort_order` decimal(4,0) DEFAULT NULL,
        `action` varchar(200) DEFAULT NULL,
        `function_cd` varchar(30) DEFAULT NULL,
        `active_yn` varchar(1) NOT NULL DEFAULT 'y',
        `servlet` varchar(50) DEFAULT NULL,
        `query_string` varchar(200) DEFAULT NULL,
        `external_url` varchar(200) DEFAULT NULL,
        `target` varchar(25) DEFAULT NULL,
        `menu_set_cd` varchar(10) DEFAULT 'app',
        `separator_yn` char(1) DEFAULT 'n',
        `image_src` varchar(100) DEFAULT NULL,
        PRIMARY KEY (`menu_id`),
        KEY `fk_fn_menu_ref_196_fn_menu` (`parent_id`),
        KEY `fk_fn_menu_menu_set_cd` (`menu_set_cd`),
        KEY `idx_fn_menu_label` (`label`),
        CONSTRAINT `fk_fn_menu_menu_set_cd` FOREIGN KEY (`menu_set_cd`) REFERENCES `fn_lu_menu_set` (`menu_set_cd`),
        CONSTRAINT `fk_fn_menu_ref_196_fn_menu` FOREIGN KEY (`parent_id`) REFERENCES `fn_menu` (`menu_id`)
        )
*/

@Table(name = "fn_menu", indexes = {
        @Index(name = "idx_fn_menu_label", columnList = "label"),
        @Index(name = "fk_fn_menu_ref_196_fn_menu", columnList = "parent_id"),
        @Index(name = "fk_fn_menu_menu_set_cd", columnList = "menu_set_cd")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnMenu implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "menu_id", nullable = false, length = 11, columnDefinition = "int(11) auto_increment")
       @Digits(integer = 11, fraction = 0)
       private Integer menu_id;
       @Column(name = "label", length = 100, columnDefinition = "varchar(100) DEFAULT NULL")
       @Size(max = 100)
       @SafeHtml
       private String label;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "parent_Id", columnDefinition = "int(11) DEFAULT NULL")
       @Valid
       private FnMenu parentId;
       @Column(name = "sort_order", length = 4, columnDefinition = "decimal(4,0) DEFAULT NULL")
       @Digits(integer = 4, fraction = 0)
       private Integer sortOrder;
       @Column(name = "action", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       private String action;
       @Column(name = "function_cd", length = 30, columnDefinition = "varchar(30) DEFAULT NULL")
       @Size(max = 30)
       @SafeHtml
       private String functionCd;
       @Column(name = "active_yn", length = 1, columnDefinition = "character varying(1) default 'y'", nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String activeYn;
       @Column(name = "servlet", length = 50, columnDefinition = "varchar(50) DEFAULT NULL")
       @Size(max = 50)
       @SafeHtml
       private String servlet;
       @Column(name = "query_string", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       private String queryString;
       @Column(name = "external_url", length = 200, columnDefinition = "varchar(200) DEFAULT NULL")
       @Size(max = 200)
       @SafeHtml
       @URL
       //TODO url
       private String externalUrl;
       @Column(name = "target", length = 25, columnDefinition = "varchar(25) DEFAULT NULL")
       @Size(max = 25)
       @SafeHtml
       private String target;
       @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "menu_set_cd", columnDefinition = "character varying(10) default 'app'", foreignKey = @ForeignKey(name = "fk_fn_menu_menu_set_cd"))
       @Valid
       private FnLuMenuSet menuSetCd;
       @Column(name = "separator_yn", length = 1, columnDefinition = "character varying(1) default 'n'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String separatorYn;
       @Column(name = "image_src", length = 100, columnDefinition = "varchar(100) DEFAULT NULL")
       @Size(max = 100)
       @SafeHtml
       private String imageSrc;
       @OneToMany(
               targetEntity = FnMenu.class,
               mappedBy = "parentId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private Set<FnMenu> fnMenus;
}
