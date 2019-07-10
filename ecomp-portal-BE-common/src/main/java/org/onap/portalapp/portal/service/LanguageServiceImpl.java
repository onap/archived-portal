/*
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.Language;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(LanguageServiceImpl.class);

    private final DataAccessService dataAccessService;

    @Autowired
    public LanguageServiceImpl(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;

    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getLanguages() {
        List<Language> languages = (List<Language>) dataAccessService.executeNamedQuery("queryLanguage",null,new HashMap());
        JSONObject result = new JSONObject();
        result.put("languageList",languages);
        return result;
    }

    @Override
    public String setUpUserLanguage(Integer languageId, String loginId) {
        Map<String,Object> params = new HashMap<>();
        params.put("login_id",loginId);
        params.put("language_id",languageId);
        dataAccessService.executeNamedQuery("updateFnUser",params,new HashMap());
        return "success";
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getUserLanguage(String loginId) {
        // get language_id from fn_user by loginId
        JSONObject result = new JSONObject();
        HashMap getUserParams = new HashMap();
        getUserParams.put("login_id", loginId);
        List<EPUser> userList;

        try {
            userList = dataAccessService.executeNamedQuery("getEPUserByLoginId", getUserParams, new HashMap());
            if (userList != null && userList.size() > 0) {
                EPUser user = userList.get(0);
                int languageId = user.getLanguageId();
                result.put("languageId", languageId);

                // get language name and alias from fn_language by languageId
                HashMap<String,String> getLangParams = new HashMap();
                getLangParams.put("language_id", String.valueOf(languageId));
                List<Language> languageList;

                languageList = dataAccessService.executeNamedQuery("queryLanguageByLanguageId", getLangParams, new HashMap());
                if (languageList != null && languageList.size() > 0) {
                    result.put("languageName", languageList.get(0).getLanguageName());
                    result.put("languageAlias", languageList.get(0).getLanguageAlias());
                }
            }
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, e.getMessage());
        }
        return result;
    }
}
