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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
/*
CREATE TABLE `fn_common_widget_data` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `category` varchar(32) DEFAULT NULL,
        `href` varchar(512) DEFAULT NULL,
        `title` varchar(256) DEFAULT NULL,
        `content` varchar(4096) DEFAULT NULL,
        `event_date` varchar(10) DEFAULT NULL,
        `sort_order` int(11) DEFAULT NULL,
        PRIMARY KEY (`id`)
        )
*/

@Table(name = "fn_common_widget_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class FnCommonWidgetData implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       private Long id;
       @Column(name = "category", length = 32)
       @Size(max = 32)
       @SafeHtml
       private String category;
       @Column(name = "href", length = 512)
       @Size(max = 512)
       @SafeHtml
       private String href;
       @Column(name = "title", length = 256)
       @Size(max = 256)
       @SafeHtml
       private String title;
       @Column(name = "content", length = 4096)
       @Size(max = 4096)
       @SafeHtml
       private String content;
       @Column(name = "event_date", length = 10)
       @Pattern(regexp = "([1-2][0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")
       @Size(max = 10)
       @SafeHtml
       private String eventDate;
       @Column(name = "sort_order")
       @Digits(integer = 11, fraction = 0)
       private Long sortOrder;

}
