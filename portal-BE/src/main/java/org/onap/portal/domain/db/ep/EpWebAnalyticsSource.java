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
import org.onap.portal.domain.db.fn.FnApp;

/*
CREATE TABLE `ep_web_analytics_source` (
        `resource_id` int(11) NOT NULL AUTO_INCREMENT,
        `app_id` int(11) NOT NULL,
        `report_source` varchar(500) DEFAULT NULL,
        `report_name` varchar(500) DEFAULT NULL,
        PRIMARY KEY (`resource_id`),
        KEY `app_id` (`app_id`),
        CONSTRAINT `ep_web_analytics_source_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`)
        )
*/

@Table(name = "ep_web_analytics_source", indexes = {
        @Index(name = "app_id", columnList = "app_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpWebAnalyticsSource implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "resource_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Integer resourceId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_id", nullable = false)
       @NotNull
       @Valid
       private FnApp appId;
       @Column(name = "report_source", length = 500)
       @Size(max = 500)
       @SafeHtml
       private String reportSource;
       @Column(name = "report_name", length = 500)
       @Size(max = 500)
       @SafeHtml
       private String reportName;
}
