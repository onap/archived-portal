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

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_chat_logs` (
        `chat_log_id` int(11) NOT NULL,
        `chat_room_id` int(11) DEFAULT NULL,
        `user_id` int(11) DEFAULT NULL,
        `message` varchar(1000) DEFAULT NULL,
        `message_date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        PRIMARY KEY (`chat_log_id`)
        )
*/

@Table(name = "fn_chat_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnChatLogs {
       @Id
       @Column(name = "chat_log_id", nullable = false)
       private Long chatLogId;
       @Column(name = "chat_room_id")
       private Long chatRoomId;
       @Column(name = "user_id")
       private Long userId;
       @Column(name = "message", length = 1000)
       @Size(max = 1000)
       @SafeHtml
       private String message;
       @Column(name = "message_date_time", nullable = false, columnDefinition = "DATETIME DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       private LocalDateTime messageDateTime;

}
