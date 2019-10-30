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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.ecomp.ExternalSystemAccess;
import org.onap.portal.framework.MockitoTestSuite;
import org.onap.portal.service.fn.FnAppService;
import org.onap.portal.service.fn.FnLanguageService;
import org.onap.portal.service.fn.FnLuTimezoneService;
import org.onap.portal.service.fn.FnRoleService;
import org.onap.portal.service.fn.FnUserRoleService;
import org.onap.portal.service.fn.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
class UserRolesControllerTest {
       @Autowired
       private UserRolesController userRolesController;
       @Autowired
       private FnUserService fnUserService;
       @Autowired
       private FnUserRoleService fnUserRoleService;
       @Autowired
       private FnLanguageService fnLanguageService;
       @Autowired
       private FnAppService fnAppService;
       @Autowired
       private FnRoleService fnRoleService;

       @Test
       void checkIfUserIsSuperAdminFalse() {
              UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("ps0001",
                      "demo123");
              //Given
              boolean expected = false;
              //When
              boolean actual = userRolesController.checkIfUserIsSuperAdmin(principal);
              //Then
              assertEquals(expected, actual);
       }

       @Test
       void checkIfUserIsSuperAdminTrue() {
              UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo",
                      "demo123");
              //Given
              boolean expected = true;
              //When
              boolean actual = userRolesController.checkIfUserIsSuperAdmin(principal);
              //Then
              assertEquals(expected, actual);
       }

       @Test
       void readExternalRequestAccess() {
              ExternalSystemAccess expected = new ExternalSystemAccess("external_access_enable", false);
              ExternalSystemAccess actual = userRolesController.readExternalRequestAccess();

              assertEquals(expected.getAccessValue(), actual.getAccessValue());
              assertEquals(expected.getKey(), actual.getKey());
       }
}