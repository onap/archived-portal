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

import com.alibaba.fastjson.JSONObject;
import org.onap.portalapp.portal.domain.Language;
import org.onap.portalapp.portal.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auxapi")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @RequestMapping(value = "/language",method = RequestMethod.GET)
    public JSONObject getLanguageList() {
        return languageService.getLanguages();
    }

    @RequestMapping(value = "/languageSetting/user/{loginId}",method = RequestMethod.POST)
    public void setUpUserLanguage(@RequestBody JSONObject jsonLanguageId,
                                  @PathVariable("loginId") String loginId) throws Exception {
        Integer languageId = jsonLanguageId.getInteger("languageId");
        languageService.setUpUserLanguage(languageId,loginId);
    }

    @RequestMapping(value = "/languageSetting/user/{loginId}",method = RequestMethod.GET)
    public JSONObject getUserLanguage(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("loginId") String loginId) {
        return languageService.getUserLanguage(loginId);
    }

}
