/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.transport;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openecomp.portalsdk.core.domain.support.DomainVo;


/**
 * This is to handle notifications in notification PopUp
 */


@Entity
@Table(name = "ep_notification")
public class EpNotificationItem extends DomainVo {
	public EpNotificationItem() {
	};

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_ID")
	public Long notificationId;

	@Column(name = "is_for_online_users")
	public String isForOnlineUsers;

	@Column(name = "is_for_all_roles")
	public String isForAllRoles;

	@Column(name = "active_YN")
	public String activeYn;
	
	@Column(name = "msg_header")
	public String msgHeader;

	@Column(name = "msg_description")
	public String msgDescription;
	
	@Column(name = "msg_source")
	public String msgSource;

	@Column(name = "start_time")
	public Date startTime;
	
	@Column(name = "end_time")
	public Date endTime;

	@Column(name = "priority")
	public Long priority;
	
	@Column(name = "creator_ID")
	public Long creatorId;
	
	@Column(name = "created_date")
	public Date createdDate;
		
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name="notification_ID")
	private Set<EpRoleNotificationItem> roles;
	
	@Transient
	private List<Long> roleIds;
	
	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getIsForOnlineUsers() {
		return isForOnlineUsers;
	}

	public void setIsForOnlineUsers(String isForOnlineUsers) {
		this.isForOnlineUsers = isForOnlineUsers;
	}

	public String getIsForAllRoles() {
		return isForAllRoles;
	}

	public void setIsForAllRoles(String isForAllRoles) {
		this.isForAllRoles = isForAllRoles;
	}
	
	public String getActiveYn() {
		return activeYn;
	}

	public void setActiveYn(String activeYn) {
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

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
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

	public Set<EpRoleNotificationItem> getRoles() {
		return roles;
	}

	public void setRoles(Set<EpRoleNotificationItem> roles) {
		this.roles = roles;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	
	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activeYn == null) ? 0 : activeYn.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((creatorId == null) ? 0 : creatorId.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((isForAllRoles == null) ? 0 : isForAllRoles.hashCode());
		result = prime * result + ((isForOnlineUsers == null) ? 0 : isForOnlineUsers.hashCode());
		result = prime * result + ((msgDescription == null) ? 0 : msgDescription.hashCode());
		result = prime * result + ((msgHeader == null) ? 0 : msgHeader.hashCode());
		result = prime * result + ((msgSource == null) ? 0 : msgSource.hashCode());
		result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((roleIds == null) ? 0 : roleIds.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EpNotificationItem other = (EpNotificationItem) obj;
		if (activeYn == null) {
			if (other.activeYn != null)
				return false;
		} else if (!activeYn.equals(other.activeYn))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (creatorId == null) {
			if (other.creatorId != null)
				return false;
		} else if (!creatorId.equals(other.creatorId))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (isForAllRoles == null) {
			if (other.isForAllRoles != null)
				return false;
		} else if (!isForAllRoles.equals(other.isForAllRoles))
			return false;
		if (isForOnlineUsers == null) {
			if (other.isForOnlineUsers != null)
				return false;
		} else if (!isForOnlineUsers.equals(other.isForOnlineUsers))
			return false;
		if (msgDescription == null) {
			if (other.msgDescription != null)
				return false;
		} else if (!msgDescription.equals(other.msgDescription))
			return false;
		if (msgHeader == null) {
			if (other.msgHeader != null)
				return false;
		} else if (!msgHeader.equals(other.msgHeader))
			return false;
		if (msgSource == null) {
			if (other.msgSource != null)
				return false;
		} else if (!msgSource.equals(other.msgSource))
			return false;
		if (notificationId == null) {
			if (other.notificationId != null)
				return false;
		} else if (!notificationId.equals(other.notificationId))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (roleIds == null) {
			if (other.roleIds != null)
				return false;
		} else if (!roleIds.equals(other.roleIds))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EpNotificationItem [notificationId=" + notificationId + ", isForOnlineUsers=" + isForOnlineUsers
				+ ", isForAllRoles=" + isForAllRoles + ", activeYn=" + activeYn + ", msgHeader=" + msgHeader
				+ ", msgDescription=" + msgDescription + ", msgSource=" + msgSource + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", priority=" + priority + ", creatorId=" + creatorId + ", createdDate="
				+ createdDate + ", roles=" + roles + ", roleIds=" + roleIds + "]";
	}
	
}
