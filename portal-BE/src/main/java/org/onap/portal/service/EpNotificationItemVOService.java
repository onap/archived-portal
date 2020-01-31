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

package org.onap.portal.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.onap.portal.domain.dto.transport.EpNotificationItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EpNotificationItemVOService {

    private final EntityManager entityManager;

    private final String notificationHistoryVOResult =
        "\t\tSELECT\n"
            + "\t\t\tnotificationId, isForOnlineUsers, isForAllRoles, msgHeader,\tmsgDescription,msgSource,\n"
            + "\t\t\tstartTime, endTime, priority, createdDate,notificationHyperlink,creatorId, loginId,\tactiveYn \n"
            + "\t\tFROM\n"
            + "\t\t\t(\n"
            + "\t\t\t\t\tselect distinct \n"
            + "\t\t\t\t\ta.notification_ID AS notificationId, \n"
            + "\t\t\t\t\tis_for_online_users AS isForOnlineUsers, \n"
            + "\t\t\t\t\tis_for_all_roles AS isForAllRoles, \n"
            + "\t\t\t\t\tmsg_header AS msgHeader, \n"
            + "\t\t\t\t\tmsg_description AS msgDescription,\n"
            + "\t\t\t\t\tmsg_source AS msgSource,  \n"
            + "\t\t\t\t\tstart_Time AS startTime, \n"
            + "\t\t\t\t\tend_time AS endTime, \n"
            + "\t\t\t\t\tpriority,\n"
            + "\t\t\t\t\tcreated_date AS createdDate, \n"
            + "\t\t\t\t\tcreator_ID AS creatorId,\n"
            + "\t\t\t\t\tnotification_hyperlink AS notificationHyperlink,\n"
            + "\t\t\t\t\tlogin_id AS loginId,\n"
            + "\t\t\t\t\tactive_YN AS activeYn, \n"
            + "\t\t\t\t\tif (is_viewed is null, 'N', is_viewed)\n"
            + "\t\t\tfrom\n"
            + "\t\t\t(\n"
            + "\t\t\t\tselect \n"
            + "\t\t\t\t\tuser_id, login_id,notification_id, is_for_online_users, is_for_all_roles, \n"
            + "\t\t\t\t\tmsg_header, msg_description,msg_source, start_Time, end_time, priority, created_date, \n"
            + "\t\t\t\t\tcreator_ID,notification_hyperlink,active_YN\n"
            + "\t\t\t\tfrom\n"
            + "\t\t\t\t(\n"
            + "\t\t\t\t\tselect a.notification_ID,a.is_for_online_users,a.is_for_all_roles,a.active_YN,\n"
            + "\t\t\t\t\ta.msg_header,a.msg_description,a.msg_source,a.start_time,a.end_time,a.priority,a.creator_ID,a.notification_hyperlink,a.created_date, \n"
            + "\t\t\t\t\t b.role_id,CASE WHEN a.creator_ID IS NOT NULL THEN u.org_user_id\n"
            + "                   \n"
            + "                   ELSE NULL\n"
            + "              END  AS login_id,b.recv_user_id \n"
            + "\t\t\t\tfrom ep_notification a, ep_role_notification b,fn_user u\n"
            + "\t\t\t\twhere a.notification_id = b.notification_id and  (u.user_id=a.creator_ID OR a.creator_ID IS NULL)\n"
            + "\t\t\t\tand a.is_for_all_roles = 'N'\n"
            + "\t\t\t\tand (\n"
            + "\t\t\t\t(start_time is null and end_time is null and a.created_date >= DATE_ADD(curdate(),INTERVAL-31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is null and start_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\t)\n"
            + "\t\t\t\t) a,\n"
            + "\t\t\t\t(\n"
            + "\t\t\t\tselect distinct a.user_id, c.role_id, c.app_id, d.APP_NAME\n"
            + "\t\t\t\tfrom fn_user a, fn_user_role b, fn_role c, fn_app d\n"
            + "\t\t\t\twhere COALESCE(c.app_id,1) = d.app_id\n"
            + "        \t\tand a.user_id = b.user_id\n"
            + "\t\t\t\tand a.user_id = :user_id\n"
            + "\t\t\t\tand b.role_id = c.role_id\n"
            + "      \t\t\tand (d.enabled='Y' or d.app_id=1)\n"
            + "\t\t\t\t) b\n"
            + "\t\t\t\twhere\n"
            + "\t\t\t\t(\n"
            + "\t\t\t\ta.role_id = b.role_id\n"
            + "\t\t\t\t)\n"
            + "\t\t\t\t   UNION\n"
            + "        \t\tselect \n"
            + "\t\t\t\t\t:user_id, login_id,notification_id, is_for_online_users, is_for_all_roles, \n"
            + "\t\t\t\t\tmsg_header, msg_description,msg_source, start_Time, end_time, priority, created_date, \n"
            + "\t\t\t\t\tcreator_ID,notification_hyperlink,active_YN\n"
            + "\t\t\t\tfrom\n"
            + "\t\t\t\t(\n"
            + "\t\t\t\t\tselect a.notification_ID,a.is_for_online_users,a.is_for_all_roles,a.active_YN,\n"
            + "\t\t\t\t\ta.msg_header,a.msg_description,a.msg_source,a.start_time,a.end_time,a.priority,a.creator_ID,a.created_date,a.notification_hyperlink, \n"
            + "\t\t\t\t\t b.role_id,CASE WHEN a.creator_ID IS NOT NULL THEN u.org_user_id\n"
            + "                   \n"
            + "                   ELSE NULL\n"
            + "              END  AS login_id,b.recv_user_id \n"
            + "\t\t\t\tfrom ep_notification a, ep_role_notification b,fn_user u\n"
            + "\t\t\t\twhere a.notification_id = b.notification_id and  (u.user_id=a.creator_ID OR a.creator_ID IS NULL)\n"
            + "\t\t\t\tand a.is_for_all_roles = 'N'\n"
            + "\t\t\t\tand (\n"
            + "\t\t\t\t(start_time is null and end_time is null and a.created_date >= DATE_ADD(curdate(),INTERVAL-31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is null and start_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\t)\n"
            + "\t\t\t\t) a\n"
            + "\t\t        where\n"
            + "\t\t        (\n"
            + "\t\t        a.recv_user_id=:user_id\n"
            + "\t\t        )\n"
            + "\t\t\t\tunion\n"
            + "\t\t\t\t(\n"
            + "\t\t\t\tselect \n"
            + "\t\t\t\t\t:user_id user_id, b.login_id,notification_id, is_for_online_users, is_for_all_roles,\n"
            + "\t\t\t\t\tmsg_header, msg_description,msg_source, start_Time, end_time, priority, a.created_date, \n"
            + "\t\t\t\t\tcreator_ID, a.notification_hyperlink,a.active_YN\n"
            + "\t\t\t\tfrom ep_notification a  JOIN fn_user b on b.user_id=a.creator_ID\n"
            + "\t\t\t\twhere a.notification_id\n"
            + "\t\t\t\tand a.is_for_all_roles = 'Y'\n"
            + "\t\t\t\tand (\n"
            + "\t\t\t\t(start_time is null and end_time is null and a.created_date >= DATE_ADD(curdate(),INTERVAL-31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is null and start_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\tor\n"
            + "\t\t\t\t(start_time is not null and end_time is not null and end_time >= DATE_ADD(curdate(),INTERVAL  -31 DAY))\n"
            + "\t\t\t\t)\n"
            + "\t\t\t\t)\n"
            + "\t\t\t\t) a left outer join (\n"
            + "\t\t\t\tselect m.notification_ID, m.is_viewed from ep_user_notification m where user_id = :user_id\n"
            + "\t\t\t\t) m\n"
            + "\t\t\t\ton  a.notification_id = m.notification_ID\n"
            + "\t\t\t\twhere\n"
            + "\t\t\t\tactive_YN = 'Y'\n"
            + "\t\t\t\t\n"
            + "\t\t\t\torder by  start_Time desc,end_time desc\n"
            + "\t\t\t) t,\n"
            + "                     (SELECT @rn /*'*/:=/*'*/ 0) t2 where startTime<=SYSDATE() ";

    @Autowired
    public EpNotificationItemVOService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<EpNotificationItemVO> getNotificationHistoryVO(Long id) {
        return entityManager.createQuery(notificationHistoryVOResult, EpNotificationItemVO.class).setParameter("user_id", id).getResultList();
    }
}
