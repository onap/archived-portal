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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/*
CREATE TABLE `ep_notification` (
        `notification_ID` int(11) NOT NULL AUTO_INCREMENT,
        `is_for_online_users` char(1) DEFAULT 'N',
        `is_for_all_roles` char(1) DEFAULT 'N',
        `active_YN` char(1) DEFAULT 'Y',
        `msg_header` varchar(100) DEFAULT NULL,
        `msg_description` varchar(2000) DEFAULT NULL,
        `msg_source` varchar(50) DEFAULT 'EP',
        `start_time` timestamp NOT NULL DEFAULT current_timestamp(),
        `end_time` timestamp NULL DEFAULT NULL,
        `priority` int(11) DEFAULT NULL,
        `creator_ID` int(11) DEFAULT NULL,
        `created_date` timestamp NULL DEFAULT NULL,
        `notification_hyperlink` varchar(512) DEFAULT NULL,
        PRIMARY KEY (`notification_ID`)
        )
*/

@Table(name = "ep_notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpNotification {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "notification_ID", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long notificationID;
       @Column(name = "is_for_online_users", length = 1, columnDefinition = "char(1) default 'N'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String isForOnlineUsers;
       @Column(name = "is_for_all_roles", length = 1, columnDefinition = "char(1) default 'N'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String isForAllRoles;
       @Column(name = "active_yn", length = 1, columnDefinition = "char(1) default 'Y'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String activeYn;
       @Column(name = "msg_header", length = 100)
       @Size(max = 100)
       @SafeHtml
       private String msgHeader;
       @Column(name = "msg_description", length = 2000)
       @Size(max = 2000)
       @SafeHtml
       private String msgDescription;
       @Column(name = "msg_source", length = 50, columnDefinition = "varchar(50) default 'EP'")
       @Size(max = 50)
       @SafeHtml
       private String msgSource;
       @Column(name = "start_time", nullable = false, columnDefinition = "datetime default now()")
       @PastOrPresent
       @NotNull
       private LocalDateTime startTime;
       @Column(name = "end_time")
       @FutureOrPresent
       private LocalDateTime end_time;
       @Column(name = "priority", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long priority;
       @Column(name = "creator_ID", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long creatorID;
       @Column(name = "created_date")
       @FutureOrPresent
       private LocalDateTime createdDate;
       @Column(name = "notification_hyperlink", length = 512)
       @Size(max = 512)
       @SafeHtml
       private String notificationHyperlink;
       @OneToMany(
               targetEntity = EpRoleNotification.class,
               mappedBy = "notificationID",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<EpRoleNotification> epRoleNotifications = new ArrayList<>();
       @OneToMany(
               targetEntity = EpUserNotification.class,
               mappedBy = "notificationId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY
       )
       private List<EpUserNotification> epUserNotifications = new ArrayList<>();
}
