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
package org.onap.portalapp.portal.service;

import com.alibaba.fastjson.JSONObject;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.Language;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private DataAccessService dataAccessService;

    @Override
    public JSONObject getLanguages() {
        List<Language> languages = (List<Language>) dataAccessService.executeNamedQuery("queryLanguage",null,new HashMap());
        JSONObject result = new JSONObject();
        result.put("languageList",languages);
        return result;
    }

    @Override
    public String setUpUserLanguage(Integer languageId, String loginId) throws Exception{
        Map<String,Object> params = new HashMap<>();
        params.put("login_id",loginId);
        params.put("language_id",languageId);
        dataAccessService.executeNamedQuery("updateFnUser",params,new HashMap());
        return "success";
    }

    @Override
    public JSONObject getUserLanguage(String loginId) {
        // get language_id from fn_user by loginId
        JSONObject result = new com.alibaba.fastjson.JSONObject();
        HashMap params = new HashMap();
        params.put("login_id",loginId);
        
        EPUser user = (EPUser) dataAccessService.executeNamedQuery("getEPUserByLoginId",params,new HashMap()).get(0);
        int languageId = user.getLanguageId();
        HashMap<String,String> params1 = new HashMap();
        params1.put("language_id", String.valueOf(languageId));
        Language language = (Language) dataAccessService.executeNamedQuery("queryLanguageByLanguageId",params1,new HashMap());
		result.put("languageId",languageId);
		result.put("languageName",language.getLanguageName());
		result.put("languageAlias",language.getLanguageAlias());

        return result;
    }
}
