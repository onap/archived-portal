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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.transport.OnboardingWidget;
import org.onap.portal.service.WidgetService;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@EnableAspectJAutoProxy
public class WidgetsController {
       private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsController.class);

       private final FnUserService fnUserService;
       private final WidgetService widgetService;

       @Autowired
       public WidgetsController(FnUserService fnUserService, WidgetService widgetService) {
              this.fnUserService = fnUserService;
              this.widgetService = widgetService;
       }

       @RequestMapping(value = { "/portalApi/widgets" }, method = RequestMethod.GET, produces = "application/json")
       public List<OnboardingWidget> getOnboardingWidgets(Principal principal, HttpServletRequest request, HttpServletResponse response) {
              FnUser user = fnUserService.loadUserByUsername(principal.getName());
              List<OnboardingWidget> onboardingWidgets = null;

              if (user == null || user.isGuest()) {
                     EcompPortalUtils.setBadPermissions(user, response, "getOnboardingWidgets");
              } else {
                     String getType = request.getHeader("X-Widgets-Type");
                     if (!getType.isEmpty() && ("managed".equals(getType) || "all".equals(getType))) {
                            onboardingWidgets = widgetService.getOnboardingWidgets(user, "managed".equals(getType));
                     } else {
                            logger.debug(EELFLoggerDelegate.debugLogger, "WidgetsController.getOnboardingApps - request must contain header 'X-Widgets-Type' with 'all' or 'managed'");
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                     }
              }

              EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets", "GET result =", response.getStatus());
              return onboardingWidgets;
       }
}
