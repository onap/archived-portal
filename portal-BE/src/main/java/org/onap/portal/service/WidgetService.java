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
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.transport.OnboardingWidget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidgetService {

       private final AdminRolesService adminRolesService;

       private static Long ACCOUNT_ADMIN_ROLE_ID = 999L;

       private static String baseSqlToken = " widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL"
               + " from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID";

       private static String validAppsFilter = "";

       private final EntityManager entityManager;

       @Autowired
       public WidgetService(final AdminRolesService adminRolesService, EntityManager entityManager) {
              this.adminRolesService = adminRolesService;
              this.entityManager = entityManager;
       }

       public List<OnboardingWidget> getOnboardingWidgets(FnUser user, boolean managed) {
              List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
              String sql = null;
              if (adminRolesService.isSuperAdmin(user)) {
                     sql = this.sqlWidgetsForAllApps();
              } else if (managed) {
                     if (adminRolesService.isAccountAdmin(user)) {
                            sql = this.sqlWidgetsForAllAppsWhereUserIsAdmin(user.getId());
                     }
              } else if (adminRolesService.isAccountAdmin(user) || adminRolesService.isUser(user)) {
                     sql = this.sqlWidgetsForAllAppsWhereUserHasAnyRole(user.getId());
              }
              if (sql != null) {
                     onboardingWidgets = (List<OnboardingWidget>) entityManager.createNativeQuery(sql, OnboardingWidget.class).getResultList();
              }
              return onboardingWidgets;
       }

       private String sqlWidgetsForAllApps() {
              return "SELECT" + baseSqlToken + validAppsFilter;
       }

       private String sqlWidgetsForAllAppsWhereUserHasAnyRole(Long userId) {
              return "SELECT DISTINCT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = "
                      + userId + validAppsFilter;
       }

       private String sqlWidgetsForAllAppsWhereUserIsAdmin(Long userId) {
              return "SELECT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = " + userId
                      + " AND FN_USER_ROLE.ROLE_ID = " + ACCOUNT_ADMIN_ROLE_ID + validAppsFilter;
       }
}
