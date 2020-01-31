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

package org.onap.portal.service.epNotification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.hibernate.transform.Transformers;
import org.onap.portal.domain.db.ep.EpNotification;
import org.onap.portal.domain.db.ep.EpRoleNotification;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.dto.transport.EpNotificationItemVO;
import org.onap.portal.service.role.FnRoleService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EpNotificationService {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(EpNotificationService.class);


    private final EpNotificationDao epNotificationDao;
    private final FnRoleService fnRoleService;
    private final EntityManager entityManager;

    private final String messageRecipients = "select u.org_user_id from ep_notification n join ep_role_notification r on "
        + " r.notification_ID=n.notification_ID join fn_user u on u.user_id=r.recv_user_id where n.notification_id=:notificationId\n";

    @Autowired
    public EpNotificationService(
        final EpNotificationDao epNotificationDao,
        FnRoleService fnRoleService, final EntityManager entityManager) {
        this.epNotificationDao = epNotificationDao;
        this.fnRoleService = fnRoleService;
        this.entityManager = entityManager;
    }

    public Optional<EpNotification> getOne(final long notficationId){
        return Optional.of(epNotificationDao.getOne(notficationId));
    }

    public List<EpNotification> getNotifications(final Long userId) {
        List<EpNotification> notificationList = epNotificationDao.getNotifications(userId);
        for (EpNotification item : notificationList) {
            item.setEpRoleNotifications(null);
        }
        return notificationList;
    }

    public List<EpNotificationItemVO> getAdminNotificationVOS(final Long userId) {
        return  entityManager.createNamedQuery("getAdminNotificationHistoryVO")
            .setParameter("user_id", userId).unwrap(org.hibernate.query.NativeQuery.class)
            .setResultTransformer(Transformers.aliasToBean( EpNotificationItemVO.class ))
            .getResultList();
    }


    public EpNotification saveNotification(final EpNotification notificationItem) {

        // gather the roles
        if (notificationItem.getRoleIds() != null && !notificationItem.getIsForAllRoles().equals("Y")) {
            if (notificationItem.getEpRoleNotifications() == null) {
                Set<EpRoleNotification> roleSet = new HashSet<>();
                notificationItem.setEpRoleNotifications(roleSet);
            }
            for (Long roleId : notificationItem.getRoleIds()) {
                FnRole role = null;
                try {
                     role = fnRoleService.getById(roleId);
                    EpRoleNotification roleItem = new EpRoleNotification();
                    roleItem.setNotificationId(notificationItem);
                    roleItem.setRoleId(role);
                    notificationItem.getEpRoleNotifications().add(roleItem);
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
            }
        }

        // for updates fetch roles and then save
        if (notificationItem.getNotificationId() != null) {
            Optional<EpNotification> updateNotificationItem = Optional.of(epNotificationDao.getOne(notificationItem.getNotificationId()));
            updateNotificationItem.ifPresent(
                epNotification -> notificationItem.setEpRoleNotifications(epNotification.getEpRoleNotifications()));
        }
        if (notificationItem.getMsgSource() == null) {
            notificationItem.setMsgSource("EP");
        }
        return epNotificationDao.saveAndFlush(notificationItem);
    }

    public List<String> getMessageRecipients(final Long notificationId) {
        return entityManager.createQuery(messageRecipients, String.class).setParameter("notificationId", notificationId).getResultList();
    }
}
