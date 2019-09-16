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

package org.onap.portal.service.fn;

import java.util.List;
import org.onap.portal.dao.fn.FnAppDao;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.dto.transport.OnboardingApp;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnAppService {

       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnAppService.class);

       private final FnAppDao fnAppDao;

       @Autowired
       public FnAppService(final FnAppDao fnAppDao) {
              this.fnAppDao = fnAppDao;
       }

       public List<FnApp> getAppsFullList() {
              return fnAppDao.findAll();
       }

       public void createOnboardingFromApp(FnApp app, OnboardingApp onboardingApp) {
              onboardingApp.setId(app.getId());
              onboardingApp.setName(app.getAppName());
              onboardingApp.setImageUrl(app.getAppImageUrl());
              onboardingApp.setDescription(app.getAppDescription());
              onboardingApp.setNotes(app.getAppNotes());
              onboardingApp.setUrl(app.getAppUrl());
              onboardingApp.setAlternateUrl(app.getAppAlternateUrl());
              onboardingApp.setRestUrl(app.getAppRestEndpoint());
              onboardingApp.setIsOpen(app.getOpen());
              onboardingApp.setIsEnabled(app.getEnabled());
              onboardingApp.setUsername(app.getAppUsername());
              onboardingApp.setAppPassword((app.getAppPassword().equals(EPCommonSystemProperties.APP_DISPLAY_PASSWORD))
                      ? EPCommonSystemProperties.APP_DISPLAY_PASSWORD : decryptedPassword(app.getAppPassword(), app));
              onboardingApp.setUebTopicName(app.getUebTopicName());
              onboardingApp.setUebKey(app.getUebKey());
              onboardingApp.setUebSecret(app.getUebSecret());
              onboardingApp.setIsCentralAuth(app.getAuthCentral());
              onboardingApp.setNameSpace(app.getAuthNamespace());
              onboardingApp.setRestrictedApp(app.isRestrictedApp());
       }

       private String decryptedPassword(String encryptedAppPwd, FnApp app) {
              String result = "";
              if (encryptedAppPwd != null && !encryptedAppPwd.isEmpty()) {
                     try {
                            result = CipherUtil.decryptPKC(encryptedAppPwd,
                                    SystemProperties.getProperty(SystemProperties.Decryption_Key));
                     } catch (Exception e) {
                            logger.error(EELFLoggerDelegate.errorLogger,
                                    "decryptedPassword failed for app " + app.getAppName(), e);
                     }
              }
              return result;
       }
}
