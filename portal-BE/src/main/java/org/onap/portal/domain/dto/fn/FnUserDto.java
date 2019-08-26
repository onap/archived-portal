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

package org.onap.portal.domain.dto.fn;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor
public class FnUserDto {
       private Long userId;
       private Long orgId;
       private Long managerId;
       private String firstName;
       private String middleName;
       private String lastName;
       private String phone;
       private String fax;
       private String cellular;
       private String email;
       private Long addressId;
       private String alertMethodCd;
       private String hrid;
       private String orgUserId;
       private String org_code;
       private String loginId;
       private String loginPwd;
       protected LocalDateTime lastLoginDate;
       private String activeYn;
       private Long createdId;
       protected LocalDateTime createdDate;
       private Long modifiedId;
       protected LocalDateTime modifiedDate;
       private String isInternalYn = "n";
       private String addressLine1;
       private String addressLine2;
       private String city;
       private String stateCd;
       private String zipCode;
       private String countryCd;
       private String locationClli;
       private String orgManagerUserId;
       private String company;
       private String departmentName;
       private String jobTitle;
       private Long timezone;
       private String department;
       private String businessUnit;
       private String businessUnitName;
       private String cost_center;
       private String finLocCode;
       private String siloStatus;
       private Long languageId;
       private boolean guest;
}
