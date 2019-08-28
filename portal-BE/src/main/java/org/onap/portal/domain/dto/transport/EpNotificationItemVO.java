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

package org.onap.portal.domain.dto.transport;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portalsdk.core.domain.support.DomainVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpNotificationItemVO extends DomainVo {

	private static final long serialVersionUID = 9095479701352339201L;

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

	public boolean isExpired() {
		boolean result = false;
		if (endTime != null) {
			int expired = endTime.compareTo(new Date());
			result = expired < 0;
		}
		return result;
	}

}
