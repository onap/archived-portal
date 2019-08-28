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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.service.fn.FnLanguageService;
import org.onap.portal.service.fn.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auxapi")
public class LanguageController {

       private final FnLanguageService languageService;
       private final FnUserService fnUserService;

       @Autowired
       public LanguageController(final FnLanguageService languageService,
               final FnUserService fnUserService) {
              this.languageService = languageService;
              this.fnUserService = fnUserService;
       }

       @RequestMapping(value = "/language",method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
       public List<FnLanguage> getLanguageList() {
              return languageService.getLanguages();
       }

       @RequestMapping(value = "/languageSetting/user/{loginId}",method = RequestMethod.POST)
       public void setUpUserLanguage(@RequestBody FnLanguage fnLanguage,
               @PathVariable("loginId") Long loginId) {
              if (fnUserService.getUser(loginId).isPresent()){
                     FnUser user = fnUserService.getUser(loginId).get();
                     user.setLanguage_id(fnLanguage.getLanguageId());
                     fnUserService.saveFnUser(user);
              }
       }

       @RequestMapping(value = "/languageSetting/user/{loginId}",method = RequestMethod.GET)
       public FnLanguage getUserLanguage(HttpServletRequest request, HttpServletResponse response,
               @PathVariable("loginId") Long loginId) {
              if (fnUserService.getUser(loginId).isPresent()){
                     Long languageId = fnUserService.getUser(loginId).get().getLanguage_id();
                     return languageService.findById(languageId).orElse(new FnLanguage());
              }
              return new FnLanguage();
       }

}
