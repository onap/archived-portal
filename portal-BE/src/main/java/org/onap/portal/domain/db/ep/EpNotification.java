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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.transport.EpNotificationItemVO;

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

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "EpNotification.getNotifications",
        query = "select rowId, notification_ID, is_for_online_users,is_for_all_roles, msg_header, msg_description,msg_source, start_Time, end_time, priority, created_date, creator_ID,notification_hyperlink, active_YN from\n"
        + "( \n"
        + "select notification_ID, is_for_online_users, is_for_all_roles, msg_header, msg_description, msg_source,start_Time, end_time, priority,created_date, creator_ID,notification_hyperlink,active_YN\n"
        + "from\n"
        + "    (\n"
        + "    select user_id, notification_id, is_for_online_users, is_for_all_roles, msg_header, msg_description,msg_source,start_Time, end_time, priority, created_date,notification_hyperlink, creator_ID,active_YN\n"
        + "    from\n"
        + "    (\n"
        + "    select a.notification_ID,a.is_for_online_users,a.is_for_all_roles,a.active_YN,\n"
        + "        a.msg_header,a.msg_description,a.msg_source,a.start_time,a.end_time,a.priority,a.creator_ID,a.notification_hyperlink,a.created_date,b.role_id,b.recv_user_id \n"
        + "    from ep_notification a, ep_role_notification b\n"
        + "    where a.notification_id = b.notification_id\n"
        + "    and (end_time is null ||  SYSDATE() <= end_time )\n"
        + "    and (start_time is null ||  SYSDATE() >= start_time)\n"
        + "    and a.is_for_all_roles = 'N'\n"
        + "    ) a,\n"
        + "    (\n"
        + "    select distinct a.user_id, c.role_id, c.app_id, d.APP_NAME\n"
        + "    from fn_user a, fn_user_role b, fn_role c, fn_app d\n"
        + "    where COALESCE(c.app_id,1) = d.app_id\n"
        + "       and a.user_id = b.user_id\n"
        + "    and a.user_id = :user_id\n"
        + "    and b.role_id = c.role_id\n"
        + "      and (d.enabled='Y' or d.app_id=1)\n"
        + "    )b\n"
        + "    where\n"
        + "    (\n"
        + "    a.role_id = b.role_id\n"
        + "    )\n"
        + "    union\n"
        + "    select :user_id, notification_id, is_for_online_users, is_for_all_roles, msg_header, msg_description,msg_source,start_Time, end_time, priority, created_date,notification_hyperlink, creator_ID,active_YN\n"
        + "    from\n"
        + "    (\n"
        + "    select a.notification_ID,a.is_for_online_users,a.is_for_all_roles,a.active_YN,\n"
        + "        a.msg_header,a.msg_description,a.msg_source,a.start_time,a.end_time,a.priority,a.creator_ID,a.created_date, a.notification_hyperlink,b.role_id,b.recv_user_id \n"
        + "    from ep_notification a, ep_role_notification b\n"
        + "    where a.notification_id = b.notification_id\n"
        + "    and (end_time is null ||  SYSDATE() <= end_time )\n"
        + "    and (start_time is null ||  SYSDATE() >= start_time)\n"
        + "    and a.is_for_all_roles = 'N'\n"
        + "    ) a\n"
        + "    where\n"
        + "    (\n"
        + "    a.recv_user_id=:user_id\n"
        + "    )\n"
        + "    union\n"
        + "    (\n"
        + "    select :user_id user_id, notification_id, is_for_online_users, is_for_all_roles, msg_header, msg_description, msg_source,start_Time, end_time, priority, created_date,notification_hyperlink, creator_ID,active_YN\n"
        + "    from ep_notification a\n"
        + "    where a.notification_id\n"
        + "    and (end_time is null ||  SYSDATE() <= end_time )\n"
        + "    and (start_time is null ||  SYSDATE() >= start_time)\n"
        + "    and a.is_for_all_roles = 'Y'\n"
        + "    )\n"
        + "    ) a\n"
        + "    where\n"
        + "        active_YN = 'Y'\n"
        + "    and\n"
        + "        not exists\n"
        + "    (\n"
        + "    select ID,User_ID,notification_ID,is_viewed,updated_time from ep_user_notification m where user_id = :user_id and m.notification_id = a.notification_id and is_viewed = 'Y'\n"
        + "    )\n"
        + "    order by priority desc, created_date desc,start_Time desc\n"
        + "\n"
        + "\n"
        + " ) t,\n")}
)

@Table(name = "ep_notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpNotification implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "notification_ID", length = 11, nullable = false)
       @Digits(integer = 11, fraction = 0)
       private Long notificationId;
       @Column(name = "is_for_online_users")
       private Boolean isForOnlineUsers = false;
       @Column(name = "is_for_all_roles")
       private Boolean isForAllRoles = false;
       @Column(name = "active_yn")
       private Boolean activeYn = true;
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
       private LocalDateTime endTime;
       @Column(name = "priority", length = 11)
       @Digits(integer = 11, fraction = 0)
       private Long priority;
       @Column(name = "creator_Id", length = 11)
       @Digits(integer = 11, fraction = 0)
       private FnUser creatorId;
       @Column(name = "created_date")
       @FutureOrPresent
       private LocalDateTime createdDate;
       @Column(name = "notification_hyperlink", length = 512)
       @Size(max = 512)
       @SafeHtml
       private String notificationHyperlink;
       @Transient
       private List<Long> roleIds;
       @OneToMany(
               targetEntity = EpRoleNotification.class,
               mappedBy = "notificationId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpRoleNotification> epRoleNotifications;
       @OneToMany(
               targetEntity = EpUserNotification.class,
               mappedBy = "notificationId",
               cascade = CascadeType.MERGE,
               fetch = FetchType.LAZY
       )
       private Set<EpUserNotification> epUserNotifications;
}
