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
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.support.DomainVo;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EpNotificationItem extends DomainVo {

       private static final long serialVersionUID = 1L;

       @Digits(integer = 11, fraction = 0)
       private Long notificationId;
       @Size(max = 1)
       @SafeHtml
       private String isForOnlineUsers;
       @Size(max = 1)
       @SafeHtml
       private String isForAllRoles;
       @Size(max = 1)
       @SafeHtml
       private String activeYn;
       @Size(max = 100)
       @SafeHtml
       private String msgHeader;
       @Size(max = 2000)
       @SafeHtml
       private String msgDescription;
       @Size(max = 50)
       @SafeHtml
       private String msgSource;
       private Date startTime;
       private Date endTime;
       @Digits(integer = 11, fraction = 0)
       private Long priority;
       @Digits(integer = 11, fraction = 0)
       private Long creatorId;
       private Date createdDate;
       @Size(max = 512)
       @SafeHtml
       private String notificationHyperlink;
       private Set<EpRoleNotificationItem> roles;
       private List<Long> roleIds;

}
