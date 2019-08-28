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
import java.util.List;
import java.util.Optional;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.service.fn.FnLanguageService;
import org.onap.portal.service.fn.FnUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auxapi")
public class LanguageController {

       private static final Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);

       private final FnLanguageService languageService;
       private final FnUserService fnUserService;

       @Autowired
       public LanguageController(final FnLanguageService languageService,
               final FnUserService fnUserService) {
              this.languageService = languageService;
              this.fnUserService = fnUserService;
       }

       @GetMapping(value = "/language", produces = MediaType.APPLICATION_JSON_VALUE)
       public List<FnLanguage> getLanguageList(final Principal principal) {
              return languageService.getLanguages(principal);
       }

       @PostMapping(value = "/languageSetting/user/{loginId}")
       public PortalRestResponse<String> setUpUserLanguage(Principal principal, @RequestBody FnLanguage fnLanguage,
               @PathVariable("loginId") Long userId) {
              PortalRestResponse<String> response = new PortalRestResponse<>();
              try {
                     if (fnUserService.getUser(userId).isPresent()) {
                            FnUser user = fnUserService.getUser(userId).get();
                            user.setLanguageId(fnLanguage);
                            fnUserService.saveFnUser(principal, user);
                     }
                     response.setMessage("SUCCESS");
                     response.setStatus(PortalRestStatusEnum.OK);
              } catch (Exception e) {
                     response.setMessage("FAILURE");
                     response.setResponse(e.getMessage());
                     response.setStatus(PortalRestStatusEnum.ERROR);
                     return response;
              }
              return response;
       }

       @GetMapping(value = "/languageSetting/user/{loginId}", produces = MediaType.APPLICATION_JSON_VALUE)
       public FnLanguage getUserLanguage(@PathVariable("loginId") final Long loginId) {
              if (fnUserService.getUser(loginId).isPresent()) {
                     return Optional.of(fnUserService.getUser(loginId).get().getLanguageId()).orElse(new FnLanguage());
              }
              return new FnLanguage();
       }

       @PostMapping(value = "/language", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
       public PortalRestResponse<String> saveLanguage(final Principal principal,
               @RequestBody final FnLanguage fnLanguage) {
              PortalRestResponse<String> response = new PortalRestResponse<>();
              try {
                     response.setMessage("SUCCESS");
                     response.setResponse(languageService.save(principal, fnLanguage).toString());
                     response.setStatus(PortalRestStatusEnum.OK);
              } catch (Exception e) {
                     response.setMessage("FAILURE");
                     response.setResponse(e.getMessage());
                     response.setStatus(PortalRestStatusEnum.ERROR);
                     return response;
              }
              return response;
       }

}
