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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portalapp.portal.domain.db.fn.FnApp;
import org.onap.portalapp.portal.domain.db.fn.FnRole;

/*
CREATE TABLE `ep_widget_catalog_role` (
        `widget_id` int(10) NOT NULL,
        `app_id` int(11) DEFAULT 1,
        `role_id` int(10) NOT NULL,
        KEY `fk_ep_widget_catalog_role_fn_widget` (`widget_id`),
        KEY `fk_ep_widget_catalog_role_ref_fn_role` (`role_id`),
        KEY `fk_ep_widget_catalog_role_app_id` (`app_id`),
        CONSTRAINT `fk_ep_widget_catalog_role_app_id` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`),
        CONSTRAINT `fk_ep_widget_catalog_role_fn_widget` FOREIGN KEY (`widget_id`) REFERENCES `ep_widget_catalog` (`widget_id`),
        CONSTRAINT `fk_ep_widget_catalog_role_ref_fn_role` FOREIGN KEY (`role_id`) REFERENCES `fn_role` (`role_id`)
        )
*/

@Table(name = "ep_widget_catalog_role", indexes = {
        @Index(name = "fk_ep_widget_catalog_role_fn_widget", columnList = "widget_id"),
        @Index(name = "fk_ep_widget_catalog_role_ref_fn_role", columnList = "role_id"),
        @Index(name = "fk_ep_widget_catalog_role_app_id", columnList = "app_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
//TODO there is something wrong with "KEY"
public class EpWidgetCatalogRole {
       @Id
       @Column(name = "id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "widget_id", nullable = false)
       @NotNull
       @Valid
       private EpWidgetCatalog widgetId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_id", columnDefinition = "bigint default '1'")
       @Valid
       private FnApp appId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "role_id", nullable = false)
       @NotNull
       @Valid
       private FnRole roleId;
}
