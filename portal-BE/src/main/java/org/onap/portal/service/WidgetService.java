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

package org.onap.portal.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.dao.fn.FnWidgetDao;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnWidget;
import org.onap.portal.domain.dto.ecomp.EPUserApp;
import org.onap.portal.domain.dto.ecomp.Widget;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.OnboardingWidget;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WidgetService {

       private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetService.class);
       private final Long LONG_ECOMP_APP_ID = 1L;
       private final Long ACCOUNT_ADMIN_ROLE_ID = 999L;

       private static String baseSqlToken =
               " new org.onap.portal.domain.dto.transport.OnboardingWidget("
                       + "widget.WIDGET_ID,widget.WDG_NAME,widget.APP_ID,"
                       + "app.APP_NAME,widget.WDG_WIDTH,widget.WDG_HEIGHT,"
                       + "widget.WDG_URL) widget.WIDGET_ID,widget.WDG_NAME,widget.APP_ID,app.APP_NAME,widget.WDG_WIDTH,widget.WDG_HEIGHT,widget.WDG_URL from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID";

       private static final String urlField = "url";
       private static final Long DUBLICATED_FIELD_VALUE_ECOMP_ERROR = new Long(
               EPCommonSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);
       private static final String nameField = "name";

       private final AdminRolesService adminRolesService;
       private final EntityManager entityManager;
       private final FnWidgetDao fnWidgetDao;

       @Autowired
       public WidgetService(final AdminRolesService adminRolesService, final EntityManager entityManager,
               final FnWidgetDao fnWidgetDao) {
              this.adminRolesService = adminRolesService;
              this.entityManager = entityManager;
              this.fnWidgetDao = fnWidgetDao;
       }

       private static final Object syncRests = new Object();

       public List<OnboardingWidget> getOnboardingWidgets(FnUser user, boolean managed) {
              if (adminRolesService.isSuperAdmin(user)) {
                     return entityManager.createQuery(sqlWidgetsForAllApps(), OnboardingWidget.class).getResultList();
              } else if (managed) {
                     if (adminRolesService.isAccountAdmin(user)) {
                            return entityManager
                                    .createQuery(sqlWidgetsForAllAppsWhereUserIsAdmin(), OnboardingWidget.class)
                                    .setParameter("USERID", user.getId()).getResultList();
                     }
              } else if (adminRolesService.isAccountAdmin(user) || adminRolesService.isUser(user)) {
                     return entityManager
                             .createQuery(sqlWidgetsForAllAppsWhereUserHasAnyRole(), OnboardingWidget.class)
                             .setParameter("USERID", user.getId()).getResultList();
              }
              return new ArrayList<>();
       }

       private String sqlWidgetsForAllApps() {
              return "SELECT" + baseSqlToken;
       }

       private String sqlWidgetsForAllAppsWhereUserIsAdmin() {
              return "SELECT" + baseSqlToken
                      + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = :USERID AND FN_USER_ROLE.ROLE_ID = "
                      + ACCOUNT_ADMIN_ROLE_ID;
       }

       private String sqlWidgetsForAllAppsWhereUserHasAnyRole() {
              return "SELECT DISTINCT" + baseSqlToken
                      + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = "
                      + ":USERID";
       }

       public FieldsValidator setOnboardingWidget(FnUser user, OnboardingWidget onboardingWidget) {
              if (onboardingWidget.getAppName().isEmpty() || onboardingWidget.getUrl().isEmpty()
                      || onboardingWidget.getAppId() == null
                      || onboardingWidget.getAppId().equals(LONG_ECOMP_APP_ID) || onboardingWidget.getWidth() <= 0 ||
                      onboardingWidget.getHeight() <= 0) {
                     FieldsValidator fieldsValidator = new FieldsValidator();
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_BAD_REQUEST);
                     return fieldsValidator;
              }
              return this.updateOrSaveWidget(adminRolesService.isSuperAdmin(user), user.getId(), onboardingWidget);
       }

       private FieldsValidator updateOrSaveWidget(boolean superAdmin, Long userId, OnboardingWidget onboardingWidget) {
              FieldsValidator fieldsValidator = new FieldsValidator();
              if (!this.isUserAdminOfAppForWidget(superAdmin, userId, onboardingWidget.getAppId())) {
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_FORBIDDEN);
                     return fieldsValidator;
              }
              synchronized (syncRests) {
                     if (onboardingWidget.getId() == null) {
                            this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
                     } else {
                            FnWidget widget = fnWidgetDao.getOne(onboardingWidget.getId());
                            if (widget == null || widget.getAppId() == null) {
                                   fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_NOT_FOUND);
                                   return fieldsValidator;
                            }
                            this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
                     }
                     if (fieldsValidator.getHttpStatusCode() == HttpServletResponse.SC_OK) {
                            this.applyOnboardingWidget(onboardingWidget, fieldsValidator);
                     }
              }
              return fieldsValidator;
       }

       private boolean isUserAdminOfAppForWidget(boolean superAdmin, Long userId, Long appId) {
              if (!superAdmin) {
                     List<EPUserApp> userRoles = getAdminUserRoles(userId, appId);
                     return (userRoles.size() > 0);
              }
              return true;
       }

       private List<EPUserApp> getAdminUserRoles(Long userId, Long appId) {
              return entityManager.createQuery(
                      "SELECT new org.onap.portal.domain.dto.ecomp.EPUserApp(fn.userId, fn.roleId, fn.appId) FROM FnUserRole fn"
                              + "WHERE  fn.userId = :USERID "
                              + "AND fn.roleId = :ROLEID "
                              + "AND fn.appId = :APPID", EPUserApp.class)
                      .setParameter("USERID", userId)
                      .setParameter("ROLEID", ACCOUNT_ADMIN_ROLE_ID)
                      .setParameter("APPID", appId)
                      .getResultList();
       }

       @Transactional
       private void applyOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
              boolean result;
              FnWidget widget;
              if (onboardingWidget.getId() == null) {
                     widget = new FnWidget();
              } else {
                     widget = fnWidgetDao.getOne(onboardingWidget.getId());
              }
              widget.setAppId(onboardingWidget.getAppId());
              widget.setName(onboardingWidget.getName());
              widget.setWidth(onboardingWidget.getWidth());
              widget.setHeight(onboardingWidget.getHeight());
              widget.setUrl(onboardingWidget.getUrl());
              result = widget.equals(fnWidgetDao.saveAndFlush(widget));
              if (!result) {
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              }
       }

       private void validateOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
              List<Widget> widgets = getWidgets(onboardingWidget);
              boolean dublicatedUrl = false;
              boolean dublicatedName = false;
              for (Widget widget : widgets) {
                     if (onboardingWidget.getId() != null && onboardingWidget.getId().equals(widget.getId())) {
                            // widget should not be compared with itself
                            continue;
                     }
                     if (!dublicatedUrl && widget.getUrl().equals(onboardingWidget.getUrl())) {
                            dublicatedUrl = true;
                            if (dublicatedName) {
                                   break;
                            }
                     }
                     if (!dublicatedName && widget.getName().equalsIgnoreCase(onboardingWidget.getName()) && widget
                             .getAppId().equals(onboardingWidget.getAppId())) {
                            dublicatedName = true;
                            if (dublicatedUrl) {
                                   break;
                            }
                     }
              }
              if (dublicatedUrl || dublicatedName) {
                     if (dublicatedUrl) {
                            fieldsValidator.addProblematicFieldName(urlField);
                     }
                     if (dublicatedName) {
                            fieldsValidator.addProblematicFieldName(nameField);
                     }
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_CONFLICT);
                     fieldsValidator.setErrorCode(DUBLICATED_FIELD_VALUE_ECOMP_ERROR);
              }
       }

       private List<Widget> getWidgets(OnboardingWidget onboardingWidget) {
              return entityManager.createQuery(
                      "SELECT new org.onap.portal.domain.dto.ecomp.Widget(fn.APP_ID, fn.WDG_NAME, fn.WDG_URL) FROM FnWidget fn"
                              + "WHERE  fn.WDG_URL = :WDGURL "
                              + "AND fn.WDG_NAME = :WDGNAME "
                              + "AND fn.APP_ID = :APPID", Widget.class)
                      .setParameter("WDGURL", onboardingWidget.getUrl())
                      .setParameter("WDGNAME", onboardingWidget.getName())
                      .getResultList();
       }

       @Transactional
       public FieldsValidator deleteOnboardingWidget(FnUser user, Long onboardingWidgetId) {
              FieldsValidator fieldsValidator = new FieldsValidator();
              synchronized (syncRests) {
                     FnWidget widget = fnWidgetDao.getOne(onboardingWidgetId);
                     if (widget != null && widget.getAppId() != null) { // widget exists
                            if (!this.isUserAdminOfAppForWidget(adminRolesService.isSuperAdmin(user), user.getId(),
                                    widget.getAppId())) {
                                   fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_FORBIDDEN);
                            } else {
                                   fnWidgetDao.deleteById(onboardingWidgetId);
                                   fieldsValidator.setHttpStatusCode(
                                           (long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            }
                     }
              }
              return fieldsValidator;
       }
}
