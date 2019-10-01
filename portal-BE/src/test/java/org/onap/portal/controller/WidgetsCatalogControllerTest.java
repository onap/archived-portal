package org.onap.portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.portal.dao.fn.EpWidgetCatalogDao;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.ecomp.WidgetCatalog;
import org.onap.portal.service.ep.EpMicroserviceParameterService;
import org.onap.portal.service.ep.EpWidgetCatalogParameterService;
import org.onap.portal.service.ep.EpWidgetCatalogService;
import org.onap.portal.service.fn.FnLanguageService;
import org.onap.portal.service.fn.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class WidgetsCatalogControllerTest {
       private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo",
               "demo123");
       @Autowired
       WidgetsCatalogController widgetsCatalogController;
       @Autowired
       FnLanguageService fnLanguageService;
       @Autowired
       FnUserService fnUserService;
       @Autowired
       EpWidgetCatalogParameterService epWidgetCatalogParameterService;
       @Autowired
       EpMicroserviceParameterService epMicroserviceParameterService;
       @Autowired
       EpWidgetCatalogService epWidgetCatalogService;

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
       @Transactional
       public void getUserParameterById() {
              //Given
              EpWidgetCatalog widget = EpWidgetCatalog.builder()
                      .widgetId(54L)
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
       }

       @Test
       public void doDownload() {
       }

       @Test
       public void saveWidgetParameter() {
       }

       @Test
       public void getUploadFlag() {
       }

       private FnUser buildFnUser(){
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