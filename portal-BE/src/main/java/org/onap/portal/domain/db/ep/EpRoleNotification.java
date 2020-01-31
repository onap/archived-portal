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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.fn.FnRole;

/*
CREATE TABLE `ep_role_notification` (
        `ID` int(11) NOT NULL AUTO_INCREMENT,
        `notification_ID` int(11) DEFAULT NULL,
        `role_ID` int(11) DEFAULT NULL,
        `recv_user_id` int(11) DEFAULT NULL,
        PRIMARY KEY (`ID`),
        KEY `fk_ep_role_notif_fn_role` (`role_ID`),
        KEY `fk_ep_role_notif_fn_notif` (`notification_ID`),
        KEY `ep_notif_recv_user_id_idx` (`recv_user_id`) USING BTREE,
        CONSTRAINT `fk_ep_role_notif_fn_notif` FOREIGN KEY (`notification_ID`) REFERENCES `ep_notification` (`notification_ID`),
        CONSTRAINT `fk_ep_role_notif_fn_role` FOREIGN KEY (`role_ID`) REFERENCES `fn_role` (`role_id`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "EpRoleNotification.getNotificationRoles",
        query = "from\n"
            + "  EpRoleNotification r\n"
            + " where\n"
            + "  r.notificationId.notificationId = :notificationID\n"
    )
})

@Table(name = "ep_role_notification", indexes = {
        @Index(name = "ep_notif_recv_user_id_idx", columnList = "recv_user_id"),
        @Index(name = "fk_ep_role_notif_fn_notif", columnList = "notification_ID"),
        @Index(name = "fk_ep_role_notif_fn_role", columnList = "role_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpRoleNotification implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "ID", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Long id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "notification_ID")
       @Valid
       private EpNotification notificationId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "role_ID", columnDefinition = "bigint")
       @Valid
       private FnRole roleId;
       @Column(name = "recv_user_id", length = 11, columnDefinition = "int(11) DEFAULT NULL")
       @Digits(integer = 11, fraction = 0)
       private Long recvUserId;

}
