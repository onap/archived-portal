/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.transport.ProfileDetail;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class UserControllerTest {

       private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("demo",
               "demo123");

       private final FnUserService userService;
       private final UserController userController;

       @Autowired
       UserControllerTest(final FnUserService userService, final UserController userController) {
              this.userService = userService;
              this.userController = userController;
       }

       @Test
       void getLoggedinUser() {
              ProfileDetail expectedDetails = new ProfileDetail();
              expectedDetails.setFirstName("Demo");
              expectedDetails.setLastName("User");
              expectedDetails.setEmail("demo@openecomp.org");
              expectedDetails.setLoginId("demo");
              expectedDetails.setLoginPassword("*****");
              PortalRestResponse<ProfileDetail> expected = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
                      expectedDetails);

              PortalRestResponse<ProfileDetail> actual = userController.getLoggedinUser(principal);

              assertEquals(expected.getStatus(), actual.getStatus());
              assertEquals(expected.getMessage(), actual.getMessage());
              assertEquals(expected.getResponse(), actual.getResponse());
       }

       @Test
       void modifyLoggedinUserBlanklastName() {
              ProfileDetail expectedDetails = new ProfileDetail();
              expectedDetails.setFirstName("Demo");
              expectedDetails.setLastName("");
              expectedDetails.setEmail("demo@openecomp.org");
              expectedDetails.setLoginId("demo");
              expectedDetails.setLoginPassword("*****");

              PortalRestResponse<String> actual = userController.modifyLoggedinUser(principal, expectedDetails);
              PortalRestResponse<String> expected = new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                      "lastName must not be blank", null);
              assertEquals(expected, actual);
       }

       @Test
       void modifyLoggedinUser() {
              ProfileDetail expectedDetails = new ProfileDetail();
              expectedDetails.setFirstName("Demo");
              expectedDetails.setLastName("User");
              expectedDetails.setEmail("demo@openecomp.org");
              expectedDetails.setLoginId("demo");
              expectedDetails.setLoginPassword("*****");

              PortalRestResponse<String> actual = userController.modifyLoggedinUser(principal, expectedDetails);
              PortalRestResponse<String> expected = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);
              assertEquals(expected, actual);
       }

       @Test
       void modifyLoggedinUserChangePassword() throws CipherUtilException {
              ProfileDetail expectedDetails = new ProfileDetail();
              expectedDetails.setFirstName("Demo");
              expectedDetails.setLastName("User");
              expectedDetails.setEmail("demo@openecomp.org");
              expectedDetails.setLoginId("demo");
              expectedDetails.setLoginPassword("123password");

              FnUser user = userService.loadUserByUsername(principal.getName());
              String oldPassword = user.getLoginPwd();

              PortalRestResponse<String> actual = userController.modifyLoggedinUser(principal, expectedDetails);
              PortalRestResponse<String> expected = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);

              FnUser user2 = userService.loadUserByUsername(principal.getName());
              String newPassword = user2.getLoginPwd();

              assertEquals(expected, actual);
              assertNotEquals(oldPassword, newPassword);
       }
}