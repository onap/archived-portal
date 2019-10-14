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
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import org.onap.portal.dao.fn.FnRoleDao;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FnRoleService {
       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnRoleService.class);


       private final FnRoleDao fnRoleDao;

       @Autowired
       public FnRoleService(FnRoleDao fnRoleDao) {
              this.fnRoleDao = fnRoleDao;
       }

       public FnRole getById(final Long id) {
              return fnRoleDao.findById(id).orElseThrow(EntityExistsException::new);
       }

       public FnRole getRole(final Long appId, final Long appRoleId) {

              String sql = "SELECT * FROM fn_role where APP_ID = :appId AND APP_ROLE_ID = :appRoleId";

              List<FnRole> roles = Optional.of(fnRoleDao.retrieveAppRoleByAppRoleIdAndByAppId(appId, appRoleId)).orElse(new ArrayList<>());
              if (!roles.isEmpty()) {
                     logger.error(EELFLoggerDelegate.errorLogger,
                             String.format(
                                     "search by appId=%s, appRoleid=%s should have returned 0 or 1 results. Got %d. This is an internal server error.",
                                     appId, appRoleId, roles.size()));
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "Trying to recover from duplicates by returning the first search result. This issue should be treated, it is probably not critical because duplicate roles should be similar.");
                     return roles.get(0);
              }
              return null;
       }
}
