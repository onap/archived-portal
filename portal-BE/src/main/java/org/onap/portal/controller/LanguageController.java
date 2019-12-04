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
import org.onap.portal.domain.dto.fn.FnLanguageDto;
import org.onap.portal.domain.mapper.FnLanguageMapper;
import org.onap.portal.domain.mapper.FnUserMapper;
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

       private final FnUserMapper fnUserMapper;
       private final FnLanguageMapper fnLanguageMapper;

       @Autowired
       public LanguageController(final FnLanguageService languageService,
               final FnUserService fnUserService, final FnUserMapper fnUserMapper,
               final FnLanguageMapper fnLanguageMapper) {
              this.languageService = languageService;
              this.fnUserService = fnUserService;
              this.fnUserMapper = fnUserMapper;
              this.fnLanguageMapper = fnLanguageMapper;
       }

       @GetMapping(value = "/language", produces = MediaType.APPLICATION_JSON_VALUE)
       public List<FnLanguageDto> getLanguageList(final Principal principal) {
              return fnLanguageMapper.fnLanguageListToDtoList(languageService.getLanguages(principal));
       }

       @PostMapping(value = "/languageSetting/user/{loginId}")
       public PortalRestResponse<String> setUpUserLanguage(Principal principal, @RequestBody FnLanguage fnLanguage,
               @PathVariable("loginId") Long userId) {
              PortalRestResponse<String> response = new PortalRestResponse<>();
              LOGGER.info("User " + principal.getName() + " try to setUpUserLanguage fnUser with id " + userId);
              try {
                     if (fnUserService.existById(userId)) {
                            LOGGER.info("User " + principal.getName() + " found fnUser with id " + userId);
                            @SuppressWarnings("OptionalGetWithoutIsPresent")
                            FnUser user = fnUserService.getUser(userId).get();
                            user.setLanguageId(fnLanguage);
                            fnUserService.saveFnUser(user);
                            //response.setResponse(fnUserMapper.fnUserToFnUserDto(user).toString());
                            response.setMessage("SUCCESS");
                            response.setStatus(PortalRestStatusEnum.OK);
                     } else {
                            response.setMessage("FAILURE");
                            response.setResponse("User for id: " + userId + " do not exist");
                            response.setStatus(PortalRestStatusEnum.ERROR);
                     }
              } catch (Exception e) {
                     LOGGER.error("Exception in setUpUserLanguage", e);
                     response.setMessage("FAILURE");
                     response.setResponse(e.toString());
                     response.setStatus(PortalRestStatusEnum.ERROR);
                     return response;
              }
              return response;
       }

       @GetMapping(value = "/languageSetting/user/{loginId}", produces = MediaType.APPLICATION_JSON_VALUE)
       public FnLanguageDto getUserLanguage(final Principal principal, @PathVariable("loginId") final Long loginId) {
              if (fnUserService.existById(loginId)) {
                     return fnLanguageMapper.fnLanguageToDto(Optional.of(fnUserService.getUser(loginId).get().getLanguageId()).orElse(new FnLanguage()));
              }
              return new FnLanguageDto();
       }

       @PostMapping(value = "/language", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
       public PortalRestResponse<String> saveLanguage(final Principal principal,
               @RequestBody final FnLanguage fnLanguage) {
              PortalRestResponse<String> response = new PortalRestResponse<>();
              try {
                     response.setMessage("SUCCESS");
                     response.setResponse(languageService.save(fnLanguage).toString());
                     response.setStatus(PortalRestStatusEnum.OK);
              } catch (Exception e) {
                     LOGGER.error("Exception in saveLanguage", e);
                     response.setMessage("FAILURE");
                     response.setResponse(e.getMessage());
                     response.setStatus(PortalRestStatusEnum.ERROR);
                     return response;
              }
              return response;
       }

}
