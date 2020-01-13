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

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.OnboardingWidget;
import org.onap.portal.domain.dto.transport.WidgetCatalogPersonalization;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portal.service.PersUserWidgetService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.service.widget.WidgetService;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@EPAuditLog
@RestController
public class WidgetsController {

       private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsController.class);

       private final FnUserService fnUserService;
       private final WidgetService widgetService;
       private final PersUserWidgetService persUserWidgetService;

       @Autowired
       public WidgetsController(final FnUserService fnUserService, final WidgetService widgetService,
               final PersUserWidgetService persUserWidgetService) {
              this.fnUserService = fnUserService;
              this.widgetService = widgetService;
              this.persUserWidgetService = persUserWidgetService;
       }

       @GetMapping(value = {"/portalApi/widgets"}, produces = MediaType.APPLICATION_JSON_VALUE)
       public List<OnboardingWidget> getOnboardingWidgets(Principal principal, HttpServletRequest request,
               HttpServletResponse response) {
              FnUser user = fnUserService.loadUserByUsername(principal.getName());
              List<OnboardingWidget> onboardingWidgets = null;
              if (user.getGuest()) {
                     EcompPortalUtils.setBadPermissions(user, response, "getOnboardingWidgets");
              } else {
                     String getType = request.getHeader("X-Widgets-Type");
                     if (!getType.isEmpty() && ("managed".equals(getType) || "all".equals(getType))) {
                            onboardingWidgets = widgetService.getOnboardingWidgets(user.getOrgUserId(), user.getId(), "managed".equals(getType));
                     } else {
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                    "WidgetsController.getOnboardingApps - request must contain header 'X-Widgets-Type' with 'all' or 'managed'");
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                     }
              }

              EcompPortalUtils
                      .logAndSerializeObject(logger, "/portalApi/widgets", "GET result =", response.getStatus());
              return onboardingWidgets;
       }

       @PutMapping(value = {"/portalApi/widgets/{widgetId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
       @PreAuthorize("hasRole('System_Administrator')")
       public FieldsValidator putOnboardingWidget(Principal principal, @PathVariable("widgetId") Long widgetId,
               @RequestBody OnboardingWidget onboardingWidget, HttpServletResponse response) {
              FnUser user = fnUserService.loadUserByUsername(principal.getName());
              FieldsValidator fieldsValidator;

              assert onboardingWidget != null;
              onboardingWidget.setId(widgetId);
              onboardingWidget.normalize();
              try {
                     fieldsValidator = widgetService.setOnboardingWidget(user.getId(), onboardingWidget);
                     response.setStatus(fieldsValidator.getHttpStatusCode().intValue());
              } catch (IllegalArgumentException e) {
                     fieldsValidator = new FieldsValidator();
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_NOT_ACCEPTABLE);
                     fieldsValidator.addProblematicFieldName(e.getMessage());
                     return fieldsValidator;
              }

              EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets/" + widgetId, "GET result =",
                      response.getStatus());

              return fieldsValidator;
       }

       @PostMapping(value = {"/portalApi/widgets"}, produces = MediaType.APPLICATION_JSON_VALUE)
       @PreAuthorize("hasRole('System_Administrator') and hasRole('Account_Administrator')")
       public FieldsValidator postOnboardingWidget(Principal principal, HttpServletResponse response,
               @RequestBody OnboardingWidget onboardingWidget) {
              FnUser user = fnUserService.loadUserByUsername(principal.getName());
              FieldsValidator fieldsValidator;

              onboardingWidget.setId(null);
              onboardingWidget.normalize();

              try {
                     fieldsValidator = widgetService.setOnboardingWidget(user.getId(), onboardingWidget);
              } catch (IllegalArgumentException e) {
                     fieldsValidator = new FieldsValidator();
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_NOT_ACCEPTABLE);
                     fieldsValidator.addProblematicFieldName(e.getMessage());
                     return fieldsValidator;
              }
              response.setStatus(fieldsValidator.getHttpStatusCode().intValue());

              EcompPortalUtils
                      .logAndSerializeObject(logger, "/portalApi/widgets", "POST result =", response.getStatus());
              return fieldsValidator;
       }

       @DeleteMapping(value = {"/portalApi/widgets/{widgetId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
       @PreAuthorize("hasRole('System_Administrator') and hasRole('Account_Administrator')")
       public FieldsValidator deleteOnboardingWidget(Principal principal, HttpServletResponse response,
               @PathVariable("widgetId") Long widgetId) {
              FnUser user = fnUserService.loadUserByUsername(principal.getName());
              FieldsValidator fieldsValidator;

              fieldsValidator = widgetService.deleteOnboardingWidget(user.getOrgUserId(), user.getId(), widgetId);
              response.setStatus(fieldsValidator.getHttpStatusCode().intValue());

              EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets/" + widgetId, "DELETE result =",
                      response.getStatus());
              return fieldsValidator;
       }

       @PutMapping(value = {"portalApi/widgetCatalogSelection"}, produces = MediaType.APPLICATION_JSON_VALUE)
       public FieldsValidator putWidgetCatalogSelection(Principal principal,
               @RequestBody WidgetCatalogPersonalization persRequest, HttpServletResponse response) throws IOException {
              FieldsValidator result = new FieldsValidator();
              FnUser user = fnUserService.loadUserByUsername(principal.getName());

              try {
                     assert persRequest != null;
                     persUserWidgetService
                             .setPersUserAppValue(user.getId(), persRequest);
              } catch (IllegalArgumentException iae) {
                     logger.error(EELFLoggerDelegate.errorLogger, "Failed in putAppCatalogSelection", iae);
                     response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, iae.getMessage());
              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger, "Failed in putAppCatalogSelection", e);
                     response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
              }
              result.setHttpStatusCode((long) HttpServletResponse.SC_OK);
              return result;
       }
}
