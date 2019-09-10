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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
import org.onap.portal.domain.db.fn.FnUser;

/*
CREATE TABLE `ep_widget_catalog_parameter` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `widget_id` int(11) NOT NULL,
        `user_id` int(11) NOT NULL,
        `param_id` int(11) NOT NULL,
        `user_value` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `EP_FN_USER_WIDGET_PARAMETER_FK` (`user_id`),
        KEY `EP_WIDGET_CATALOG_WIDGET_PARAMETER_FK` (`widget_id`),
        KEY `EP_PARAMETER_ID_WIDGET_PARAMETER_FK` (`param_id`),
        CONSTRAINT `EP_FN_USER_WIDGET_PARAMETER_FK` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `EP_PARAMETER_ID_WIDGET_PARAMETER_FK` FOREIGN KEY (`param_id`) REFERENCES `ep_microservice_parameter` (`id`),
        CONSTRAINT `EP_WIDGET_CATALOG_WIDGET_PARAMETER_FK` FOREIGN KEY (`widget_id`) REFERENCES `ep_widget_catalog` (`widget_id`)
        )
*/

@NamedQueries({
        @NamedQuery(
                name = "EpWidgetCatalogParameter.retrieveByParamId",
                query = "FROM EpWidgetCatalogParameter WHERE paramId = :PARAMID"),
        @NamedQuery(
                name = "EpWidgetCatalogParameter.deleteWidgetCatalogParameter",
                query = "DELETE FROM EpWidgetCatalogParameter WHERE paramId = :PARAMID"),
        @NamedQuery(
                name = "EpWidgetCatalogParameter.getUserParamById",
                query = "FROM EpWidgetCatalogParameter WHERE paramId = :PARAMID and userId = :USERID and widgetId = :WIDGETID"
        )
})

@Table(name = "ep_widget_catalog_parameter", indexes = {
        @Index(name = "EP_FN_USER_WIDGET_PARAMETER_FK", columnList = "user_id"),
        @Index(name = "EP_WIDGET_CATALOG_WIDGET_PARAMETER_FK", columnList = "widget_id"),
        @Index(name = "EP_PARAMETER_ID_WIDGET_PARAMETER_FK", columnList = "param_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpWidgetCatalogParameter implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "widget_id", nullable = false)
       @NotNull
       @Valid
       private EpWidgetCatalog widgetId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "user_id", nullable = false)
       @NotNull
       @Valid
       private FnUser userId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "param_id", nullable = false)
       @NotNull
       @Valid
       private EpMicroserviceParameter paramId;
       @Column(name = "user_value", length = 50, columnDefinition = "varchar(50) DEFAULT NULL")
       @Size(max = 50)
       @SafeHtml
       private String userValue;
}
