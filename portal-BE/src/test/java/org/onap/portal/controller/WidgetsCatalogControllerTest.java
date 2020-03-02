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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.ecomp.WidgetCatalog;
import org.onap.portal.service.microserviceParameter.EpMicroserviceParameterService;
import org.onap.portal.service.widgetCatalogParameter.EpWidgetCatalogParameterService;
import org.onap.portal.service.widgetCatalog.EpWidgetCatalogService;
import org.onap.portal.service.language.FnLanguageService;
import org.onap.portal.service.user.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
public class WidgetsCatalogControllerTest {
       private final UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo",
               "demo123");
       @Autowired
       private WidgetsCatalogController widgetsCatalogController;
       @Autowired
       private FnUserService fnUserService;
       @Autowired
       private FnLanguageService fnLanguageService;
       @Autowired
       private EpWidgetCatalogParameterService epWidgetCatalogParameterService;
       @Autowired
       private EpMicroserviceParameterService epMicroserviceParameterService;
       @Autowired
       private EpWidgetCatalogService epWidgetCatalogService;

       @Test
       public void getUserWidgetCatalog() {
              List<WidgetCatalog> actual = widgetsCatalogController.getUserWidgetCatalog("demo");
              assertNull(actual);
       }

       @Test
       public void getWidgetCatalog() {
       }

       @Test
       public void updateWidgetCatalog() {
       }

       @Test
       public void deleteOnboardingWidget() {
       }

       @Test
       public void updateWidgetCatalogWithFiles() {
       }

       @Test
       public void createWidgetCatalog() {
       }

       @Test
       public void getWidgetFramework() {
       }

       @Test
       public void getWidgetController() {
       }

       @Test
       public void getWidgetCSS() {
       }

       @Test
       public void getWidgetParameterResult() {
       }

       @Test
       public void getUserParameterById() {
              //Given
              EpWidgetCatalog widget = EpWidgetCatalog.builder()
                      .wdgName("Name")
                      .wdgFileLoc("loc")
                      .allUserFlag(true)
                      .build();
              epWidgetCatalogService.save(widget);
              EpMicroserviceParameter parameter = new EpMicroserviceParameter();
              epMicroserviceParameterService.save(parameter);
              FnLanguage language = FnLanguage.builder().languageAlias("TS").languageName("TEST").build();
              fnLanguageService.save(language);
              FnUser user = buildFnUser();
              language.setFnUsers(new HashSet<>(Collections.singleton(user)));
              user.setLanguageId(language);
              fnUserService.saveFnUser(user);
              EpWidgetCatalogParameter data =  EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();
              //When
              epWidgetCatalogParameterService.saveUserParameter(data);
              List<EpWidgetCatalogParameter> actual = widgetsCatalogController.getUserParameterById(parameter.getId());
              //Then
              assertEquals(1, actual.size());
              //Clean
       }

       @Test
       public void deleteUserParameterById() {
              //Given
              EpWidgetCatalog widget = EpWidgetCatalog.builder()
                      .wdgName("Name")
                      .wdgFileLoc("loc")
                      .allUserFlag(true)
                      .build();
              epWidgetCatalogService.save(widget);
              EpMicroserviceParameter parameter = new EpMicroserviceParameter();
              epMicroserviceParameterService.save(parameter);
              FnUser user = buildFnUser();
              FnLanguage language = fnLanguageService.getByLanguageAlias("EN");
              user.setLanguageId(language);
              fnUserService.saveFnUser(user);
              EpWidgetCatalogParameter data =  EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();
              //When
              assertEquals(0, widgetsCatalogController.getUserParameterById(parameter.getId()).size());
              epWidgetCatalogParameterService.saveUserParameter(data);
              //Then assert
              assertEquals(1, widgetsCatalogController.getUserParameterById(parameter.getId()).size());
              assertTrue(widgetsCatalogController.deleteUserParameterById(parameter.getId()));
              assertEquals(0, widgetsCatalogController.getUserParameterById(parameter.getId()).size());

       }

       @Test
       public void doDownload() {
       }

       @Test
       public void saveWidgetParameter() {
              //Given
              EpWidgetCatalog widget = EpWidgetCatalog.builder()
                      .wdgName("Name")
                      .wdgFileLoc("loc")
                      .allUserFlag(true)
                      .build();
              epWidgetCatalogService.save(widget);
              EpMicroserviceParameter parameter = new EpMicroserviceParameter();
              epMicroserviceParameterService.save(parameter);
              FnLanguage language = FnLanguage.builder().languageAlias("TS").languageName("TEST").build();
              fnLanguageService.save(language);
              FnUser user = buildFnUser();
              language.setFnUsers(new HashSet<>(Collections.singleton(user)));
              user.setLanguageId(language);
              EpWidgetCatalogParameter data =  EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();

              //When
              widgetsCatalogController.saveWidgetParameter(principal, data);
              //Then
              EpWidgetCatalogParameter actual = epWidgetCatalogParameterService.getById(data.getId());

              assertEquals("TestData", actual.getUserValue());

       }

       @Test
       public void saveWidgetParameterOldParamTest() {
              //Given
              EpWidgetCatalog widget = EpWidgetCatalog.builder()
                      .wdgName("Name")
                      .wdgFileLoc("loc")
                      .allUserFlag(true)
                      .build();
              epWidgetCatalogService.save(widget);
              EpMicroserviceParameter parameter = new EpMicroserviceParameter();
              epMicroserviceParameterService.save(parameter);
              FnLanguage language = FnLanguage.builder().languageAlias("TS").languageName("TEST").build();
              fnLanguageService.save(language);
              FnUser user = buildFnUser();
              language.setFnUsers(new HashSet<>(Collections.singleton(user)));
              user.setLanguageId(language);
              fnUserService.saveFnUser(user);

              EpWidgetCatalogParameter old =  EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();

              //When
              widgetsCatalogController.saveWidgetParameter(principal, old);

              EpWidgetCatalogParameter newWidgetParameter =  EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData2").build();

              widgetsCatalogController.saveWidgetParameter(principal, newWidgetParameter);

              EpWidgetCatalogParameter oldOne = epWidgetCatalogParameterService.getById(old.getId());

              //Then
              assertEquals("TestData2", oldOne.getUserValue());

       }

       @Test
       public void getUploadFlag() {
              String expected = "true";
              String actual = widgetsCatalogController.getUploadFlag();

              assertEquals(expected, actual);
       }

       private FnUser buildFnUser(){
              return FnUser.builder()
                      .lastLoginDate(LocalDateTime.now())
                      .activeYn(true)
                      .modifiedDate(LocalDateTime.now())
                      .createdDate(LocalDateTime.now())
                      .isInternalYn(true)
                      .isSystemUser(true)
                      .guest(false)
                      .build();
       }
}
