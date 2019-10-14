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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.onap.portal.dao.fn.FnUserRoleDao;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FnUserRoleService {
       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnUserRoleService.class);
       private final FnUserRoleDao fnUserRoleDao;

       @Autowired
       public FnUserRoleService(FnUserRoleDao fnUserRoleDao) {
              this.fnUserRoleDao = fnUserRoleDao;
       }

       public List<FnUserRole> getAdminUserRoles(final Long userId, final Long roleId, final Long appId) {
              return fnUserRoleDao.getAdminUserRoles(userId, roleId, appId).orElse(new ArrayList<>());
       }

       public boolean isSuperAdmin(final String orgUserId, final Long roleId, final Long appId){
              List<FnUserRole> roles = getUserRolesForRoleIdAndAppId(roleId, appId).stream().filter(role -> role.getUserId().getOrgUserId().equals(orgUserId)).collect(Collectors.toList());
              return !roles.isEmpty();
       }

       private List<FnUserRole> getUserRolesForRoleIdAndAppId(final Long roleId, final Long appId){
              return Optional.of(fnUserRoleDao.getUserRolesForRoleIdAndAppId(roleId, appId)).orElse(new ArrayList<>());
       }

       public FnUserRole saveOne(final FnUserRole fnUserRole){
              return fnUserRoleDao.save(fnUserRole);
       }
}
