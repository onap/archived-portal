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

package org.onap.portal.service.fn;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnLuTimezone;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.builder.FnUserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@Transactional
class FnUserServiceTest {
       private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo", "XZa6pS1vC0qKXWtn9wcZWdLx61L0=");
       @Autowired
       private FnUserService fnUserService;
       @Autowired
       private FnLuTimezoneService fnLuTimezoneService;
       @Autowired
       private FnLanguageService fnLanguageService;

       @Test
       void saveUser(){
              FnUser actual = fnUserService.getUser(1L).get();

              FnUser expected = new FnUserBuilder().createFnUser();
              expected.setUserId(123L);
              expected.setFirstName("Demo");
              expected.setLastName("User");
              expected.setEmail("demo@openecomp.org");
              expected.setOrgUserId("demo");
              expected.setTimezone(fnLuTimezoneService.getById(10L).orElse(new FnLuTimezone()));
              expected.setLoginId("demo");
              expected.setLoginPwd("4Gl6WL1bmwviYm+XZa6pS1vC0qKXWtn9wcZWdLx61L0=");
              expected.setLastLoginDate(LocalDateTime.parse("2019-08-08T12:18:17"));
              expected.setActiveYn("Y");
              expected.setCreatedDate(LocalDateTime.parse("2016-10-14T21:00"));
              expected.setModifiedId(actual);
              expected.setModifiedDate(LocalDateTime.parse("2019-08-08T12:18:17"));
              expected.setIsInternalYn("N");
              expected.setStateCd("NJ");
              expected.setCountryCd("US");
              expected.setLanguageId(fnLanguageService.findById(1L).orElse(new FnLanguage()));

              fnUserService.saveFnUser(principal, expected);

              //Clean up
              fnUserService.deleteUser(expected);
       }

       @Test
       void getUser() {
              FnUser actual = fnUserService.getUser(1L).get();


              FnUser expected = new FnUserBuilder().createFnUser();
              expected.setUserId(1L);
              expected.setFirstName("Demo");
              expected.setLastName("User");
              expected.setEmail("demo@openecomp.org");
              expected.setOrgUserId("demo");
              expected.setLoginId("demo");
              expected.setLoginPwd("4Gl6WL1bmwviYm+XZa6pS1vC0qKXWtn9wcZWdLx61L0=");
              expected.setLastLoginDate(LocalDateTime.parse("2019-08-08T12:18:17"));
              expected.setActiveYn("Y");
              expected.setCreatedDate(LocalDateTime.parse("2016-10-14T21:00"));
              expected.setModifiedId(actual);
              expected.setModifiedDate(LocalDateTime.parse("2019-08-08T12:18:17"));
              expected.setIsInternalYn("N");
              expected.setStateCd("NJ");
              expected.setCountryCd("US");
              expected.setTimezone(fnLuTimezoneService.getById(10L).orElse(new FnLuTimezone()));
              expected.setLanguageId(fnLanguageService.findById(1L).orElse(new FnLanguage()));


              assertEquals(expected.getUserId(), actual.getUserId());
              assertEquals(expected.getOrgId(), actual.getOrgId());
              assertEquals(expected.getManagerId(), actual.getManagerId());
              assertEquals(expected.getFirstName(), actual.getFirstName());
              assertEquals(expected.getMiddleName(), actual.getMiddleName());
              assertEquals(expected.getLastName(), actual.getLastName());
              assertEquals(expected.getPhone(), actual.getPhone());
              assertEquals(expected.getFax(), actual.getFax());
              assertEquals(expected.getCellular(), actual.getCellular());
              assertEquals(expected.getEmail(), actual.getEmail());
              assertEquals(expected.getAddressId(), actual.getAddressId());
              assertEquals(expected.getAlertMethodCd(), actual.getAlertMethodCd());
              assertEquals(expected.getHrid(), actual.getHrid());
              assertEquals(expected.getOrgUserId(), actual.getOrgUserId());
              assertEquals(expected.getOrg_code(), actual.getOrg_code());
              assertEquals(expected.getLoginId(), actual.getLoginId());
              assertEquals(expected.getLoginPwd(), actual.getLoginPwd());
              assertEquals(expected.getLastLoginDate(), actual.getLastLoginDate());
              assertEquals(expected.getActiveYn(), actual.getActiveYn());
              assertEquals(expected.getCreatedId(), actual.getCreatedId());
              assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
              assertEquals(expected.getModifiedId(), actual.getModifiedId());
              assertEquals(expected.getModifiedDate(), actual.getModifiedDate());
              assertEquals(expected.getIsInternalYn(), actual.getIsInternalYn());
              assertEquals(expected.getAddressLine1(), actual.getAddressLine1());
              assertEquals(expected.getAddressLine2(), actual.getAddressLine2());
              assertEquals(expected.getCity(), actual.getCity());
              assertEquals(expected.getStateCd(), actual.getStateCd());
              assertEquals(expected.getZipCode(), actual.getZipCode());
              assertEquals(expected.getCountryCd(), actual.getCountryCd());
              assertEquals(expected.getLocationClli(), actual.getLocationClli());
              assertEquals(expected.getOrgManagerUserId(), actual.getOrgManagerUserId());
              assertEquals(expected.getCompany(), actual.getCompany());
              assertEquals(expected.getDepartmentName(), actual.getDepartmentName());
              assertEquals(expected.getJobTitle(), actual.getJobTitle());
              assertEquals(expected.getTimezone().getTimezoneId(), actual.getTimezone().getTimezoneId());
              assertEquals(expected.getDepartment(), actual.getDepartment());
              assertEquals(expected.getBusinessUnit(), actual.getBusinessUnit());
              assertEquals(expected.getBusinessUnitName(), actual.getBusinessUnitName());
              assertEquals(expected.getCost_center(), actual.getCost_center());
              assertEquals(expected.getFinLocCode(), actual.getFinLocCode());
              assertEquals(expected.getSiloStatus(), actual.getSiloStatus());
              assertEquals(expected.getLanguageId(), actual.getLanguageId());
       }
}