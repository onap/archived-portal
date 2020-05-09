/**
 * Copyright (C) 2019 CMCC, Inc. and others. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.portalapp.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portalapp.portal.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/auxapi")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping(value = "/language", produces = "application/json;charset=UTF-8")
    public JSONObject getLanguageList() {
        return languageService.getLanguages();
    }

    @PostMapping(value = "/languageSetting/user/{loginId}")
    public void setUpUserLanguage(@RequestBody JSONObject jsonLanguageId,
                                  @PathVariable("loginId") String loginId) throws Exception {
        Integer languageId = jsonLanguageId.getInteger("languageId");
        languageService.setUpUserLanguage(languageId,loginId);
    }

    @GetMapping(value = "/languageSetting/user/{loginId}")
    public JSONObject getUserLanguage(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("loginId") String loginId) {
        return languageService.getUserLanguage(loginId);
    }

}
