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

package org.onap.portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.dao.fn.FnLanguageDao;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.service.fn.FnUserService;
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
class LanguageControllerTest {
       private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo", "XZa6pS1vC0qKXWtn9wcZWdLx61L0=");

       @Autowired
       private LanguageController languageController;
       @Autowired
       private FnLanguageDao fnLanguageDao;
       @Autowired
       private FnUserService fnUserService;

       @Test
       void saveLanguage() {
              //Given
              FnLanguage fnLanguage = new FnLanguage();
              fnLanguage.setLanguageName("Polish");
              fnLanguage.setLanguageAlias("PL");
              //When
              PortalRestResponse<String> expected = new PortalRestResponse<>();
              expected.setMessage("SUCCESS");
              expected.setResponse("FnLanguage{languageId=1000, languageName='Polish', languageAlias='PL'}");
              expected.setStatus(PortalRestStatusEnum.OK);
              PortalRestResponse<String> actual = languageController.saveLanguage(principal, fnLanguage);
              //Then
              assertEquals(expected.getMessage(), actual.getMessage());
              assertEquals(expected.getStatus(), actual.getStatus());
              //Clean up
              fnLanguageDao.delete(fnLanguage);
       }

       @Test
       void saveLanguageXSS() {
              //Given
              FnLanguage fnLanguage = new FnLanguage();
              fnLanguage.setLanguageName("<script>alert(“XSS”)</script> ");
              fnLanguage.setLanguageAlias("PL");
              //When
              PortalRestResponse<String> expected = new PortalRestResponse<>();
              expected.setMessage("FAILURE");
              expected.setResponse("FnLanguage is not valid, may have unsafe html content");
              expected.setStatus(PortalRestStatusEnum.ERROR);
              PortalRestResponse<String> actual =  languageController.saveLanguage(principal, fnLanguage);
              //Then

              assertEquals(expected.getMessage(), actual.getMessage());
              assertEquals(expected.getStatus(), actual.getStatus());
              //Clean up
              fnLanguageDao.delete(fnLanguage);
       }

       @Test
       void getLanguageListTest(){
              assertEquals(languageController.getLanguageList(principal).size(), 2);
       }

       @Test
       void setUpUserLanguage(){
              //Given
              FnLanguage fnLanguage = new FnLanguage();
              fnLanguage.setLanguageName("Polish");
              fnLanguage.setLanguageAlias("PL");

              PortalRestResponse<String> expected = new PortalRestResponse<>();
              expected.setMessage("SUCCESS");
              expected.setStatus(PortalRestStatusEnum.OK);

              languageController.saveLanguage(principal, fnLanguage);
              PortalRestResponse<String> actual = languageController.setUpUserLanguage(principal, fnLanguage, 1L);

              FnUser user = fnUserService.getUser(1L).get();
              assertEquals(expected.getMessage(), actual.getMessage());
              assertEquals(expected.getStatus(), actual.getStatus());
              assertEquals(user.getLanguageId().getLanguageId(), fnLanguage.getLanguageId());


              //Clean up
              fnLanguageDao.delete(fnLanguage);
       }



}