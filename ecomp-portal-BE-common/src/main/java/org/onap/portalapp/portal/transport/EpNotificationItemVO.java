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
package org.onap.portalapp.portal.transport;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.onap.portalsdk.core.domain.support.DomainVo;

/**
 * This is to handle notifications in user notifications and in notification history
 */

/**
 * POJO that models a single notification with the org user ID (not integer
 * user_id).
 */
@Entity
public class EpNotificationItemVO extends DomainVo {

	private static final long serialVersionUID = 9095479701352339201L;

	@Id
	private Integer notificationId;

	private Character isForOnlineUsers;

	private Character isForAllRoles;

	private Character activeYn;

	private String msgHeader;

	private String msgDescription;
	
	private String msgSource;

	private Date startTime;

	private Date endTime;

	private Integer priority;

	private Integer creatorId;

	private Date createdDate;

	private String loginId;
	
	private String notificationHyperlink;

	
	/**
	 * Answers whether the notification is expired.
	 * 
	 * @return true if the end time is past the current time, else false.
	 */
	public boolean isExpired() {
		boolean result = false;
		if (endTime != null) {
			int expired = endTime.compareTo(new Date());
			result = (expired == -1) ? true : false;
		}
		return result;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Character getIsForOnlineUsers() {
		return isForOnlineUsers;
	}

	public void setIsForOnlineUsers(Character isForOnlineUsers) {
		this.isForOnlineUsers = isForOnlineUsers;
	}

	public Character getIsForAllRoles() {
		return isForAllRoles;
	}

	public void setIsForAllRoles(Character isForAllRoles) {
		this.isForAllRoles = isForAllRoles;
	}

	public Character getActiveYn() {
		return activeYn;
	}

	public void setActiveYn(Character activeYn) {
		this.activeYn = activeYn;
	}

	public String getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(String msgHeader) {
		this.msgHeader = msgHeader;
	}

	public String getMsgDescription() {
		return msgDescription;
	}

	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}
	
	public String getNotificationHyperlink() {
		return notificationHyperlink;
	}

	public void setNotificationHyperlink(String notificationHyperlink) {
		this.notificationHyperlink = notificationHyperlink;
	}

}
