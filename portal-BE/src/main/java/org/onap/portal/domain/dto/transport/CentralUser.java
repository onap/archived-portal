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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentralUser implements Serializable {

	private static final long serialVersionUID = 7060454665330579923L;

	private Long id;
	private LocalDateTime created;
	private LocalDateTime modified;
	private Long createdId;
	private Long modifiedId;
	private Long rowNum;
	private Long orgId;
	private Long managerId;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String phone;
	private String fax;
	private String cellular;
	private String email;
	private Long addressId;
	private String alertMethodCd;
	private String hrid;
	private String orgUserId;
	private String orgCode;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private Long orgManagerUserId;
	private String locationClli;
	private String businessCountryCode;
	private String businessCountryName;
	private String businessUnit;
	private String businessUnitName;
	private String department;
	private String departmentName;
	private String companyCode;
	private String company;
	private String zipCodeSuffix;
	private String jobTitle;
	private String commandChain;
	private String siloStatus;
	private String costCenter;
	private String financialLocCode;
	private String loginId;
	private String loginPwd;
	private LocalDateTime lastLoginDate;
	private boolean active;
	private boolean internal;
	private Long selectedProfileId;
	private Long timeZoneId;
	private boolean online;
	private String chatId;
	private Set<CentralUserApp> userApps;
	private Set<CentralRole> pseudoRoles;

}
