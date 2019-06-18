/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.transport;

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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.support.DomainVo;


/**
 * This is to handle notifications in notification PopUp
 */


@Entity
@Table(name = "ep_notification")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EpNotificationItem extends DomainVo {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_ID")
	@Digits(integer = 11, fraction = 0)
	public Long notificationId;

	@Column(name = "is_for_online_users")
	@Size(max = 1)
	@SafeHtml
	public String isForOnlineUsers;

	@Column(name = "is_for_all_roles")
	@Size(max = 1)
	@SafeHtml
	public String isForAllRoles;

	@Column(name = "active_YN")
	@Size(max = 1)
	@SafeHtml
	public String activeYn;

	@Column(name = "msg_header")
	@Size(max = 100)
	@SafeHtml
	public String msgHeader;

	@Column(name = "msg_description")
	@Size(max = 2000)
	@SafeHtml
	public String msgDescription;

	@Column(name = "msg_source")
	@Size(max = 50)
	@SafeHtml
	public String msgSource;

	@Column(name = "start_time")
	public Date startTime;

	@Column(name = "end_time")
	public Date endTime;

	@Column(name = "priority")
	@Digits(integer = 11, fraction = 0)
	public Long priority;

	@Column(name = "creator_ID")
	@Digits(integer = 11, fraction = 0)
	public Long creatorId;

	@Column(name = "created_date")
	public Date createdDate;

	@Column(name = "notification_hyperlink")
	@Size(max = 512)
	@SafeHtml
	public String notificationHyperlink;

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name="notification_ID")
	private Set<EpRoleNotificationItem> roles;

	@Transient
	private List<Long> roleIds;

	@Override
	public String toString() {
		return "EpNotificationItem [notificationId=" + notificationId + ", isForOnlineUsers=" + isForOnlineUsers
				+ ", isForAllRoles=" + isForAllRoles + ", activeYn=" + activeYn + ", msgHeader=" + msgHeader
				+ ", msgDescription=" + msgDescription + ", msgSource=" + msgSource + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", priority=" + priority + ", creatorId=" + creatorId + ", createdDate="
				+ createdDate + ", roles=" + roles + ", roleIds=" + roleIds + "]";
	}

}
