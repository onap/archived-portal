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

package org.onap.portal.domain.mapper;

import lombok.NoArgsConstructor;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.fn.FnUserDto;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FnUserMapper {
              public FnUserDto fnUserToFnUserDto(final FnUser fnUser){
              return FnUserDto.builder()
                      .userId(fnUser.getUserId())
                      .orgId(fnUser.getOrgId().getOrgId())
                      .managerId(fnUser.getManagerId().getUserId())
                      .firstName(fnUser.getFirstName())
                      .middleName(fnUser.getMiddleName())
                      .lastName(fnUser.getLastName())
                      .phone(fnUser.getPhone())
                      .fax(fnUser.getFax())
                      .cellular(fnUser.getCellular())
                      .email(fnUser.getEmail())
                      .addressId(fnUser.getAddressId())
                      .alertMethodCd(fnUser.getAlertMethodCd().getAlertMethodCd())
                      .hrid(fnUser.getHrid())
                      .orgUserId(fnUser.getOrgUserId())
                      .org_code(fnUser.getOrg_code())
                      .loginId(fnUser.getLoginId())
                      .loginPwd(fnUser.getLoginPwd())
                      .lastLoginDate(fnUser.getLastLoginDate())
                      .activeYn(fnUser.getActiveYn())
                      .createdId(fnUser.getCreatedId().getUserId())
                      .createdDate(fnUser.getCreatedDate())
                      .modifiedId(fnUser.getModifiedId().getUserId())
                      .modifiedDate(fnUser.getModifiedDate())
                      .isInternalYn(fnUser.getIsInternalYn())
                      .addressLine1(fnUser.getAddressLine1())
                      .addressLine2(fnUser.getAddressLine2())
                      .city(fnUser.getCity())
                      .stateCd(fnUser.getStateCd())
                      .zipCode(fnUser.getZipCode())
                      .countryCd(fnUser.getCountryCd())
                      .locationClli(fnUser.getLocationClli())
                      .orgManagerUserId(fnUser.getOrgManagerUserId())
                      .company(fnUser.getCompany())
                      .departmentName(fnUser.getDepartmentName())
                      .jobTitle(fnUser.getJobTitle())
                      .timezone(fnUser.getTimezone().getTimezoneId())
                      .department(fnUser.getDepartment())
                      .businessUnit(fnUser.getBusinessUnit())
                      .businessUnitName(fnUser.getBusinessUnitName())
                      .cost_center(fnUser.getCost_center())
                      .finLocCode(fnUser.getFinLocCode())
                      .siloStatus(fnUser.getSiloStatus())
                      .languageId(fnUser.getLanguageId().getLanguageId())
                      .guest(fnUser.getGuest())
                      .build();

       }

       public FnUser fnUserToFnUser(final FnUser fnUser){
              return FnUser.builder()
                      .userId(fnUser.getUserId())
                      .orgId(fnUser.getOrgId())
                      .managerId(fnUser.getManagerId())
                      .firstName(fnUser.getFirstName())
                      .middleName(fnUser.getMiddleName())
                      .lastName(fnUser.getLastName())
                      .phone(fnUser.getPhone())
                      .fax(fnUser.getFax())
                      .cellular(fnUser.getCellular())
                      .email(fnUser.getEmail())
                      .addressId(fnUser.getAddressId())
                      .alertMethodCd(fnUser.getAlertMethodCd())
                      .hrid(fnUser.getHrid())
                      .orgUserId(fnUser.getOrgUserId())
                      .org_code(fnUser.getOrg_code())
                      .loginId(fnUser.getLoginId())
                      .loginPwd(fnUser.getLoginPwd())
                      .lastLoginDate(fnUser.getLastLoginDate())
                      .activeYn(fnUser.getActiveYn())
                      .createdId(fnUser.getCreatedId())
                      .createdDate(fnUser.getCreatedDate())
                      .modifiedId(fnUser.getModifiedId())
                      .modifiedDate(fnUser.getModifiedDate())
                      .isInternalYn(fnUser.getIsInternalYn())
                      .addressLine1(fnUser.getAddressLine1())
                      .addressLine2(fnUser.getAddressLine2())
                      .city(fnUser.getCity())
                      .stateCd(fnUser.getStateCd())
                      .zipCode(fnUser.getZipCode())
                      .countryCd(fnUser.getCountryCd())
                      .locationClli(fnUser.getLocationClli())
                      .orgManagerUserId(fnUser.getOrgManagerUserId())
                      .company(fnUser.getCompany())
                      .departmentName(fnUser.getDepartmentName())
                      .jobTitle(fnUser.getJobTitle())
                      .timezone(fnUser.getTimezone())
                      .department(fnUser.getDepartment())
                      .businessUnit(fnUser.getBusinessUnit())
                      .businessUnitName(fnUser.getBusinessUnitName())
                      .cost_center(fnUser.getCost_center())
                      .finLocCode(fnUser.getFinLocCode())
                      .siloStatus(fnUser.getSiloStatus())
                      .languageId(fnUser.getLanguageId())
                      .guest(fnUser.getGuest())
                      .build();
       }
}
