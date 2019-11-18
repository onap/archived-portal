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

import java.security.Principal;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.transport.ProfileDetail;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portal.validation.DataValidator;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class UserController {

       private static final String HIDDEN_DEFAULT_PASSWORD = "*****";
       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserController.class);

       private final FnUserService userService;
       private final DataValidator dataValidator;

       @Autowired
       public UserController(final FnUserService userService,
               final DataValidator dataValidator) {
              this.userService = userService;
              this.dataValidator = dataValidator;
       }

       @GetMapping(value = {"/portalApi/loggedinUser"}, produces = MediaType.APPLICATION_JSON_VALUE)
       public PortalRestResponse<ProfileDetail> getLoggedinUser(Principal principal) {
              PortalRestResponse<ProfileDetail> portalRestResponse = null;
              try {
                     FnUser user = userService.loadUserByUsername(principal.getName());
                     ProfileDetail profileDetail = new ProfileDetail(user.getFirstName(), user.getLastName(),
                             user.getMiddleName(), user.getEmail(), user.getLoginId(), HIDDEN_DEFAULT_PASSWORD);
                     portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
                             profileDetail);
              } catch (Exception e) {
                     portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(),
                             null);
                     logger.error(EELFLoggerDelegate.errorLogger, "getLoggedinUser failed", e);
              }
              return portalRestResponse;
       }

       @PutMapping(value = {"/portalApi/modifyLoggedinUser"}, produces = MediaType.APPLICATION_JSON_VALUE)
       public PortalRestResponse<String> modifyLoggedinUser(Principal principal,
               @RequestBody ProfileDetail profileDetail) {
              PortalRestResponse<String> portalRestResponse = null;
              try {
                     String errorMsg = "";
                     if (!dataValidator.isValid(profileDetail)) {
                            errorMsg = "Required field(s) is missing";
                            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                                    dataValidator.getConstraintViolationsString(profileDetail), null);
                            logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", errorMsg);
                     } else {
                            FnUser user = userService.loadUserByUsername(principal.getName());
                            user.setFirstName(profileDetail.getFirstName());
                            user.setLastName(profileDetail.getLastName());
                            user.setEmail(profileDetail.getEmail());
                            user.setMiddleName(profileDetail.getMiddleName());
                            user.setLoginId(profileDetail.getLoginId());
                            if (!HIDDEN_DEFAULT_PASSWORD.equals(profileDetail.getLoginPassword())) {
                                   user.setLoginPwd(CipherUtil
                                           .encryptPKC(profileDetail.getLoginPassword(), "AGLDdG4D04BKm2IxIWEr8o==!"));
                            }
                            userService.saveFnUser(user);
                            // Update user info in the session
                            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);
                     }
              } catch (Exception e) {
                     portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
                     logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", e);
              }
              return portalRestResponse;
       }
}
