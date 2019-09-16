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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `fn_chat_room` (
        `chat_room_id` int(11) NOT NULL,
        `name` varchar(50) NOT NULL,
        `description` varchar(500) DEFAULT NULL,
        `owner_id` int(11) DEFAULT NULL,
        `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
        `updated_date` timestamp NOT NULL DEFAULT current_timestamp(),
        PRIMARY KEY (`chat_room_id`)
        )
*/

@Table(name = "fn_chat_room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnChatRoom implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "chat_room_id", nullable = false)
       @NotNull
       private Integer chatRoomId;
       @Column(name = "name", length = 50, nullable = false)
       @Size(max = 50)
       @NotNull
       @SafeHtml
       private String name;
       @Column(name = "description", length = 500)
       @Size(max = 500)
       @SafeHtml
       private String description;
       @Column(name = "owner_id")
       private Integer ownerId;
       @Column(name = "created_date", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       private LocalDateTime createdDate;
       @Column(name = "updated_date", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       private LocalDateTime updatedDate;

}
