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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.logging.format.EPAppMessagesEnum;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.fn.FnUserRoleService;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminRolesService {

       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AdminRolesService.class);

       private final Long SYS_ADMIN_ROLE_ID = 1L;
       private final Long ACCOUNT_ADMIN_ROLE_ID = 999L;
       private final Long ECOMP_APP_ID = 1L;
       private final String ADMIN_ACCOUNT = "Is account admin for user {}";

       private final EntityManager entityManager;
       private final FnUserService fnUserService;
       private final FnUserRoleService fnUserRoleService;

       @Autowired
       public AdminRolesService(final EntityManager entityManager,
               final FnUserService fnUserService, final FnUserRoleService fnUserRoleService) {
              this.entityManager = entityManager;
              this.fnUserService = fnUserService;
              this.fnUserRoleService = fnUserRoleService;
       }

       public boolean isSuperAdmin(final String orgUserId) {
              boolean isSuperAdmin;
              try {
                     isSuperAdmin = fnUserRoleService
                             .isSuperAdmin(orgUserId, SYS_ADMIN_ROLE_ID, ECOMP_APP_ID);
              } catch (Exception e) {
                     logger.error("isSuperAdmin exception: " + e.toString());
                     throw e;
              }
              logger.info("isSuperAdmin " + isSuperAdmin);
              return isSuperAdmin;
       }

       public boolean isAccountAdmin(FnUser user) {
              try {
                     final Map<String, Long> userParams = new HashMap<>();
                     userParams.put("userId", user.getId());
                     logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, user.getId());
                     List<Integer> userAdminApps;
                     String query = "select fa.app_id from fn_user_role ur,fn_app fa where ur.user_id =:userId and ur.app_id=fa.app_id and ur.role_id= 999 and (fa.enabled = 'Y' || fa.app_id=1)";
                     userAdminApps = entityManager.createQuery(query, Integer.class)
                             .setParameter("userId", user.getId()).getResultList();
                     logger.debug(EELFLoggerDelegate.debugLogger,
                             "Is account admin for userAdminApps() - for user {}, found userAdminAppsSize {}",
                             user.getOrgUserId(), userAdminApps.size());

                     if (user.getId() != null) {
                            for (FnUserRole userApp : user.getFnUserRoles()) {
                                   if (userApp.getRoleId().getId().equals(ACCOUNT_ADMIN_ROLE_ID) || (
                                           userAdminApps.size() > 1)) {
                                          logger.debug(EELFLoggerDelegate.debugLogger,
                                                  "Is account admin for userAdminApps() - for user {}, found Id {}",
                                                  user.getOrgUserId(), userApp.getRoleId().getId());
                                          return true;
                                   }
                            }
                     }
              } catch (Exception e) {
                     EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "Exception occurred while executing isAccountAdmin operation",
                             e);
              }
              return false;
       }

       public boolean isUser(FnUser user) {
              try {
                     FnUser currentUser = fnUserService.getUser(user.getId()).orElseThrow(Exception::new);
                     if (currentUser != null && currentUser.getId() != null) {
                            for (FnUserRole userApp : currentUser.getFnUserRoles()) {
                                   if (!userApp.getAppId().getId().equals(ECOMP_APP_ID)) {
                                          FnRole role = userApp.getRoleId();
                                          if (!role.getId().equals(SYS_ADMIN_ROLE_ID) && !role.getId()
                                                  .equals(ACCOUNT_ADMIN_ROLE_ID)) {
                                                 if (role.getActiveYn()) {
                                                        return true;
                                                 }
                                          }
                                   }
                            }
                     }
              } catch (Exception e) {
                     EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
                     logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing isUser operation",
                             e);
              }
              return false;
       }
}
