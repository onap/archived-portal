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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
CREATE TABLE `ep_widget_catalog_files` (
        `file_id` int(11) NOT NULL AUTO_INCREMENT,
        `widget_id` int(11) DEFAULT NULL,
        `widget_name` varchar(100) NOT NULL,
        `framework_js` longblob DEFAULT NULL,
        `controller_js` longblob DEFAULT NULL,
        `markup_html` longblob DEFAULT NULL,
        `widget_css` longblob DEFAULT NULL,
        PRIMARY KEY (`file_id`)
        )
*/

@Table(name = "ep_widget_catalog_files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class EpWidgetCatalogFiles implements Serializable {
       @Id

  @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "file_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long fileId;
       @Column(name = "widget_id", length = 11, columnDefinition = " int(11) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long widgetId;
       @Column(name = "widget_name", length = 100, nullable = false)
       @Size(max = 100)
       @NotNull
       @SafeHtml
       private String widgetName;
       @Column(name = "framework_js", columnDefinition = "longblob DEFAULT NULL")
       private byte[] frameworkJs;
       @Column(name = "controller_js", columnDefinition = "longblob DEFAULT NULL")
       private byte[] controllerJs;
       @Column(name = "markup_html", columnDefinition = "longblob DEFAULT NULL")
       private byte[] markupHtml;
       @Column(name = "widget_css", columnDefinition = "longblob DEFAULT NULL")
       private byte[] widgetCss;
}
