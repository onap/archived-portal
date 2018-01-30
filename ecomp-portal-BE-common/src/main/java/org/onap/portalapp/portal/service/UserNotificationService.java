/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.util.List;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompAppRole;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.EpNotificationItemVO;
import org.onap.portalapp.portal.transport.EpRoleNotificationItem;

public interface UserNotificationService {

	/**
	 * Gets the specified notifications with userId from ep_notification
	 * 
	 * @param userId
	 * 
	 * @return the notifications with the specified userId
	 */

	List<EpNotificationItem> getNotifications(Long userId);

	/**
	 * Gets the specified roles from ep_role_notification
	 * 
	 * @param notificationId
	 * 
	 * @return the roles for a specified notification
	 */

	List<EpRoleNotificationItem> getNotificationRoles(Long notificationId);

	/**
	 * Get all app role list from the fn_app and fn_role table
	 * 
	 * @return list of all roles associated with the applications
	 */
	List<EcompAppRole> getAppRoleList();

	/**
	 * Marks the notification as viewed by the specified user.
	 * 
	 * @param notificationId
	 * @param userId
	 */
	void setNotificationRead(Long notificationId, int userId);

	/**
	 * Saves the specified notification to the table ep_notification
	 * 
	 * @param notificationItem
	 * @throws Exception
	 */

	String saveNotification(EpNotificationItem notificationItem) throws Exception;

	/**
	 * Gets the specified notification with the userId for view all recent
	 * notifications
	 * 
	 * @param userId
	 * 
	 * @return the notification list
	 */

	List<EpNotificationItemVO> getNotificationHistoryVO(Long userId);

	/**
	 * Gets the notifications with the userId for user notifications
	 * 
	 * @param userId
	 * 
	 * @return the notification list
	 */

	List<EpNotificationItemVO> getAdminNotificationVOS(Long userId);

	/**
	 * Gets the user list from fn_user
	 * 
	 * @param OrgIds
	 * 
	 * @return the users list
	 */

	List<EPUser> getUsersByOrgIds(List<String> OrgIds);

	/**
	 * Gets the received recipient to whom the notification is delivered from
	 * external system
	 * 
	 * @param notificationId
	 * 
	 * @return the active users
	 */

	List<String> getMessageRecipients(Long notificationId);

	/**
	 * delete the records from ep_notification table when the endtime is more
	 * than 3 months
	 * 
	 */

	void deleteNotificationsFromEpNotificationTable();

	/**
	 * delete the records from ep_user_notification table when the endtime is
	 * more than 3 months
	 * 
	 */
	void deleteNotificationsFromEpUserNotificationTable();

	/**
	 * delete the records from ep_role_notification table when the endtime is
	 * more than 3 months
	 * 
	 */
	void deleteNotificationsFromEpRoleNotificationTable();

}
