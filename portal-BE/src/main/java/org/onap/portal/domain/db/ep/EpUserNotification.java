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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnUser;

/*
CREATE TABLE `ep_user_notification` (
        `ID` int(11) NOT NULL AUTO_INCREMENT,
        `User_ID` int(11) DEFAULT NULL,
        `notification_ID` int(11) DEFAULT NULL,
        `is_viewed` char(1) DEFAULT 'N',
        `updated_time` timestamp NOT NULL DEFAULT current_timestamp(),
        PRIMARY KEY (`ID`),
        KEY `fk_ep_urole_notif_fn_user` (`User_ID`),
        KEY `fk_ep_urole_notif_fn_notif` (`notification_ID`),
        CONSTRAINT `fk_ep_urole_notif_fn_notif` FOREIGN KEY (`notification_ID`) REFERENCES `ep_notification` (`notification_ID`),
        CONSTRAINT `fk_ep_urole_notif_fn_user` FOREIGN KEY (`User_ID`) REFERENCES `fn_user` (`user_id`)
        )
*/

@Table(name = "ep_user_notification", indexes = {
        @Index(name = "fk_ep_urole_notif_fn_user", columnList = "User_ID"),
        @Index(name = "fk_ep_urole_notif_fn_notif", columnList = "notification_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpUserNotification implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "ID", length = 11, nullable = false, columnDefinition = "int(11) AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Integer id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "User_ID")
       @Valid
       private FnUser userId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "notification_ID")
       @Valid
       private EpNotification notificationId;
       @Column(name = "is_viewed", length = 1, columnDefinition = "char(1) default 'N'")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String isViewed;
       @Column(name = "updated_time", nullable = false, columnDefinition = "datetime default now()")
       @NotNull
       private LocalDateTime updatedTime;

}
