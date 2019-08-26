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
import org.onap.portal.domain.builder.FnUserBuilder;
import org.onap.portal.domain.builder.FnUserDtoBuilder;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.fn.FnUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FnUserMapper {

       private FnUserDtoBuilder fnUserDtoBuilder;
       private FnUserBuilder fnUserBuilder;

       @Autowired
       public FnUserMapper(final FnUserDtoBuilder fnUserDtoBuilder,
               FnUserBuilder fnUserBuilder) {
              this.fnUserDtoBuilder = fnUserDtoBuilder;
              this.fnUserBuilder = fnUserBuilder;
       }

       public FnUserDto fnUserToFnUserDto(final FnUser fnUser){
              return fnUserDtoBuilder
                      .setUserId(fnUser.getUserId())
                      .setOrgId(fnUser.getOrgId().getOrgId())
                      .setManagerId(fnUser.getManagerId().getUserId())
                      .setFirstName(fnUser.getFirstName())
                      .setMiddleName(fnUser.getMiddleName())
                      .setLastName(fnUser.getLastName())
                      .setPhone(fnUser.getPhone())
                      .setFax(fnUser.getFax())
                      .setCellular(fnUser.getCellular())
                      .setEmail(fnUser.getEmail())
                      .setAddressId(fnUser.getAddressId())
                      .setAlertMethodCd(fnUser.getAlertMethodCd().getAlertMethodCd())
                      .setHrid(fnUser.getHrid())
                      .setOrgUserId(fnUser.getOrgUserId())
                      .setOrg_code(fnUser.getOrg_code())
                      .setLoginId(fnUser.getLoginId())
                      .setLoginPwd(fnUser.getLoginPwd())
                      .setLastLoginDate(fnUser.getLastLoginDate())
                      .setActiveYn(fnUser.getActiveYn())
                      .setCreatedId(fnUser.getCreatedId().getUserId())
                      .setCreatedDate(fnUser.getCreatedDate())
                      .setModifiedId(fnUser.getModifiedId().getUserId())
                      .setModifiedDate(fnUser.getModifiedDate())
                      .setIsInternalYn(fnUser.getIsInternalYn())
                      .setAddressLine1(fnUser.getAddressLine1())
                      .setAddressLine2(fnUser.getAddressLine2())
                      .setCity(fnUser.getCity())
                      .setStateCd(fnUser.getStateCd())
                      .setZipCode(fnUser.getZipCode())
                      .setCountryCd(fnUser.getCountryCd())
                      .setLocationClli(fnUser.getLocationClli())
                      .setOrgManagerUserId(fnUser.getOrgManagerUserId())
                      .setCompany(fnUser.getCompany())
                      .setDepartmentName(fnUser.getDepartmentName())
                      .setJobTitle(fnUser.getJobTitle())
                      .setTimezone(fnUser.getTimezone().getTimezoneId())
                      .setDepartment(fnUser.getDepartment())
                      .setBusinessUnit(fnUser.getBusinessUnit())
                      .setBusinessUnitName(fnUser.getBusinessUnitName())
                      .setCost_center(fnUser.getCost_center())
                      .setFinLocCode(fnUser.getFinLocCode())
                      .setSiloStatus(fnUser.getSiloStatus())
                      .setLanguageId(fnUser.getLanguageId().getLanguageId())
                      .setGuest(fnUser.isGuest()).createFnUserDto();
       }

       public FnUser fnUserToFnUser(final FnUser fnUser){
              return fnUserBuilder
                      .setUserId(fnUser.getUserId())
                      .setOrgId(fnUser.getOrgId())
                      .setManagerId(fnUser.getManagerId())
                      .setFirstName(fnUser.getFirstName())
                      .setMiddleName(fnUser.getMiddleName())
                      .setLastName(fnUser.getLastName())
                      .setPhone(fnUser.getPhone())
                      .setFax(fnUser.getFax())
                      .setCellular(fnUser.getCellular())
                      .setEmail(fnUser.getEmail())
                      .setAddressId(fnUser.getAddressId())
                      .setAlertMethodCd(fnUser.getAlertMethodCd())
                      .setHrid(fnUser.getHrid())
                      .setOrgUserId(fnUser.getOrgUserId())
                      .setOrg_code(fnUser.getOrg_code())
                      .setLoginId(fnUser.getLoginId())
                      .setLoginPwd(fnUser.getLoginPwd())
                      .setLastLoginDate(fnUser.getLastLoginDate())
                      .setActiveYn(fnUser.getActiveYn())
                      .setCreatedId(fnUser.getCreatedId())
                      .setCreatedDate(fnUser.getCreatedDate())
                      .setModifiedId(fnUser.getModifiedId())
                      .setModifiedDate(fnUser.getModifiedDate())
                      .setIsInternalYn(fnUser.getIsInternalYn())
                      .setAddressLine1(fnUser.getAddressLine1())
                      .setAddressLine2(fnUser.getAddressLine2())
                      .setCity(fnUser.getCity())
                      .setStateCd(fnUser.getStateCd())
                      .setZipCode(fnUser.getZipCode())
                      .setCountryCd(fnUser.getCountryCd())
                      .setLocationClli(fnUser.getLocationClli())
                      .setOrgManagerUserId(fnUser.getOrgManagerUserId())
                      .setCompany(fnUser.getCompany())
                      .setDepartmentName(fnUser.getDepartmentName())
                      .setJobTitle(fnUser.getJobTitle())
                      .setTimezone(fnUser.getTimezone())
                      .setDepartment(fnUser.getDepartment())
                      .setBusinessUnit(fnUser.getBusinessUnit())
                      .setBusinessUnitName(fnUser.getBusinessUnitName())
                      .setCost_center(fnUser.getCost_center())
                      .setFinLocCode(fnUser.getFinLocCode())
                      .setSiloStatus(fnUser.getSiloStatus())
                      .setLanguageId(fnUser.getLanguageId())
                      .setGuest(fnUser.isGuest()).createFnUser();
       }
}
