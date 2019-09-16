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
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
CREATE TABLE `fn_broadcast_message` (
        `message_id` int(11) NOT NULL AUTO_INCREMENT,
        `message_text` varchar(1000) NOT NULL,
        `message_location_id` decimal(11,0) NOT NULL,
        `broadcast_start_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `broadcast_end_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `active_yn` char(1) NOT NULL DEFAULT 'y',
        `sort_order` decimal(4,0) NOT NULL,
        `broadcast_site_cd` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`message_id`)
        )
*/

@Table(name = "fn_broadcast_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnBroadcastMessage implements Serializable {

       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "message_id", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long messageId;
       @Column(name = "message_text", length = 10000, nullable = false)
       @Size(max = 50)
       @NotNull
       @SafeHtml
       private String messageText;
       @Column(name = "message_location_id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long messageLocationId;
       @Column(name = "broadcast_start_date", columnDefinition = "timestamp DEFAULT current_timestamp()", nullable = false)
       @NotNull
       private LocalDateTime broadcastStartDate;
       @Column(name = "broadcast_end_date", columnDefinition = "timestamp DEFAULT current_timestamp()", nullable = false)
       @NotNull
       private LocalDateTime broadcastEndDate;
       @Column(name = "active_yn", length = 1, columnDefinition = "character varying(1) default 'y'", nullable = false)
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String activeYn;
       @Column(name = "sort_order", length = 4, nullable = false)
       @Digits(integer = 4, fraction = 0)
       @NotNull
       private Long sortOrder;
       @Column(name = "broadcast_site_cd", length = 50)
       @Size(max = 50)
       @SafeHtml
       private String broadcastSiteCd;

}
