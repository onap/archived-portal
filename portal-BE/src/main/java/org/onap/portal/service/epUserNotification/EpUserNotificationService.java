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

package org.onap.portal.service.epUserNotification;

import java.time.LocalDateTime;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import org.onap.portal.domain.db.ep.EpNotification;
import org.onap.portal.domain.db.ep.EpUserNotification;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.service.epNotification.EpNotificationService;
import org.onap.portal.service.user.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EpUserNotificationService {

    private final EpUserNotificationDao epUserNotificationDao;
    private final EpNotificationService epNotificationService;
    private final FnUserService fnUserService;

    @Autowired
    public EpUserNotificationService(
        final EpUserNotificationDao epUserNotificationDao,
        final EpNotificationService epNotificationService,
        final FnUserService fnUserService) {
        this.epUserNotificationDao = epUserNotificationDao;
        this.epNotificationService = epNotificationService;
        this.fnUserService = fnUserService;
    }

    public void setNotificationRead(Long notificationId, long userId) {

        EpNotification notification = epNotificationService.getOne(notificationId).orElse(new EpNotification());
        FnUser user = fnUserService.getUser(userId).orElseThrow(EntityExistsException::new);

        EpUserNotification userNotification = new EpUserNotification();
        userNotification.setNotificationId(notification);
        userNotification.setUpdatedTime(LocalDateTime.now());
        userNotification.setIsViewed(true);
        userNotification.setUserId(user);

        epUserNotificationDao.saveAndFlush(userNotification);
    }

}
