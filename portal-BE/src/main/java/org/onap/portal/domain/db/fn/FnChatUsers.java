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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_chat_users` (
        `chat_room_id` int(11) DEFAULT NULL,
        `user_id` int(11) DEFAULT NULL,
        `last_activity_date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `chat_status` varchar(20) DEFAULT NULL,
        `id` int(11) NOT NULL,
        PRIMARY KEY (`id`)
        )
*/

@Table(name = "fn_chat_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnChatUsers implements Serializable {
       @Id
       @Column(name = "id", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       @NotNull
       private Long id;
       @Column(name = "chat_room_id", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long chatRoomId;
       @Column(name = "user_id", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long userId;
       @Column(name = "last_activity_date_time", nullable = false, columnDefinition = "DATETIME DEFAULT current_timestamp() ON UPDATE current_timestamp()")
       @PastOrPresent
       private LocalDateTime lastActivityDateTime;
       @Column(name = "chat_status", length = 20)
       @Size(max = 20)
       @SafeHtml
       private String chatStatus;
}
