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

package org.onap.portal.service.ep;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.controller.WidgetsCatalogController;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.service.fn.FnLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
class EpWidgetCatalogParameterServiceTest {

       private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo",
               "demo123");

       private EpWidgetCatalogParameterService epWidgetCatalogParameterService;
       private WidgetsCatalogController widgetsCatalogController;
       private FnLanguageService fnLanguageService;
       private EpMicroserviceParameterService epMicroserviceParameterService;
       private EpWidgetCatalogService epWidgetCatalogService;

       @Autowired
       public EpWidgetCatalogParameterServiceTest(
               EpWidgetCatalogParameterService epWidgetCatalogParameterService,
               WidgetsCatalogController widgetsCatalogController,
               FnLanguageService fnLanguageService,
               EpMicroserviceParameterService epMicroserviceParameterService,
               EpWidgetCatalogService epWidgetCatalogService) {
              this.epWidgetCatalogParameterService = epWidgetCatalogParameterService;
              this.widgetsCatalogController = widgetsCatalogController;
              this.fnLanguageService = fnLanguageService;
              this.epMicroserviceParameterService = epMicroserviceParameterService;
              this.epWidgetCatalogService = epWidgetCatalogService;
       }

       @Test
       void deleteUserParameterById() {
       }

       @Test
       void deleteByParamId() {
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
              fnLanguageService.save(principal, language);
              FnUser user = buildFnUser();
              language.setFnUsers(new HashSet<>(Collections.singleton(user)));
              user.setLanguageId(language);
              EpWidgetCatalogParameter data = EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();
              //When
              assertEquals(0, widgetsCatalogController.getUserParameterById(parameter.getId()).size());
              epWidgetCatalogParameterService.saveUserParameter(data);
              //Then
              assertEquals(1, epWidgetCatalogParameterService.getUserParameterById(parameter.getId()).size());
              epWidgetCatalogParameterService.deleteByParamId(parameter.getId());
              assertEquals(0, epWidgetCatalogParameterService.getUserParameterById(parameter.getId()).size());
              //Clean

       }

       @Test
       void getUserParamById() {
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
              fnLanguageService.save(principal, language);
              FnUser user = buildFnUser();
              language.setFnUsers(new HashSet<>(Collections.singleton(user)));
              user.setLanguageId(language);
              EpWidgetCatalogParameter data = EpWidgetCatalogParameter.builder()
                      .widgetId(widget).userId(user).paramId(parameter).userValue("TestData").build();
              //When
              assertEquals(0, widgetsCatalogController.getUserParameterById(parameter.getId()).size());
              epWidgetCatalogParameterService.saveUserParameter(data);
              Long id = data.getId();
              assertEquals(1, epWidgetCatalogParameterService.getUserParameterById(parameter.getId()).size());
              EpWidgetCatalogParameter actual = epWidgetCatalogParameterService.getUserParamById(widget.getWidgetId(), user.getUserId(), parameter.getId());
              //Then
              assertEquals(id, actual.getId());
              assertEquals(data.getUserValue(), actual.getUserValue());
              assertEquals(data.getWidgetId().getWidgetId(), actual.getWidgetId().getWidgetId());
              assertEquals(data.getParamId().getId(), actual.getParamId().getId());

       }

       private FnUser buildFnUser() {
              return FnUser.builder()
                      .lastLoginDate(LocalDateTime.now())
                      .activeYn(true)
                      .modifiedDate(LocalDateTime.now())
                      .createdDate(LocalDateTime.now())
                      .isInternalYn(true)
                      .guest(false)
                      .build();
       }
}