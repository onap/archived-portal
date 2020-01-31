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
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `ep_widget_catalog` (
        `widget_id` int(11) NOT NULL AUTO_INCREMENT,
        `wdg_name` varchar(100) NOT NULL DEFAULT '?',
        `service_id` int(11) DEFAULT NULL,
        `wdg_desc` varchar(200) DEFAULT NULL,
        `wdg_file_loc` varchar(256) NOT NULL DEFAULT '?',
        `all_user_flag` char(1) NOT NULL DEFAULT 'N',
        PRIMARY KEY (`widget_id`)
        )
*/

@Table(name = "ep_widget_catalog")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class EpWidgetCatalog implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "widget_id", nullable = false)
       private Long widgetId;
       @Column(name = "wdg_name", length = 100, columnDefinition = "varchar(100) default '?'", nullable = false)
       @Size(max = 100)
       @NotNull
       @SafeHtml
       private String wdgName = "?";
       @Column(name = "service_id", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long serviceId;
       @Column(name = "wdg_desc", length = 200)
       @Size(max = 200)
       @SafeHtml
       private String wdgDesc;
       @Column(name = "wdg_file_loc", length = 256, nullable = false, columnDefinition = "varchar(256) not null default '?'")
       @Size(max = 256)
       @NotNull
       @SafeHtml
       private String wdgFileLoc = "?";
       @Column(name = "all_user_flag", length = 1, columnDefinition = "boolean default false", nullable = false)
       @NotNull
       private Boolean allUserFlag = false;
       @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
       @JoinTable(
               name = "ep_widget_microservice",
               joinColumns = {@JoinColumn(name = "widget_id", referencedColumnName = "widget_id")},
               inverseJoinColumns = {@JoinColumn(name = "microservice_id", referencedColumnName = "id")},
               indexes = {
                       @Index(name = "FK_EP_WIDGET_MICROSERVICE_EP_MICROSERVICE", columnList = "microservice_id"),
                       @Index(name = "FK_EP_WIDGET_MICROSERVICE_EP_WIDGET", columnList = "widget_id")
               }
       )
       private Set<EpMicroservice> epMicroservices;
       @OneToMany(
               targetEntity = EpWidgetCatalogRole.class,
               mappedBy = "widgetId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpWidgetCatalogRole> widgetCatalogRoles;
       @OneToMany(
               targetEntity = EpPersUserWidgetSel.class,
               mappedBy = "widgetId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpPersUserWidgetSel> epPersUserWidgetSels;
       @OneToMany(
               targetEntity = EpPersUserWidgetSel.class,
               mappedBy = "widgetId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpPersUserWidgetSel> persUserWidgetSels;
       @OneToMany(
               targetEntity = EpPersUserWidgetPlacement.class,
               mappedBy = "widgetId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpPersUserWidgetPlacement> epPersUserWidgetPlacements;
       @OneToMany(
               targetEntity = EpWidgetCatalogParameter.class,
               mappedBy = "widgetId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpWidgetCatalogParameter> epWidgetCatalogParameters;
}
