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

package org.onap.portal.service.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityExistsException;

import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FnRoleService {

  private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnRoleService.class);

  private final FnRoleDao fnRoleDao;

  @Autowired
  public FnRoleService(FnRoleDao fnRoleDao) {
    this.fnRoleDao = fnRoleDao;
  }

  public FnRole getById(final Long id) {
    return fnRoleDao.findById(id).orElseThrow(EntityExistsException::new);
  }

  public FnRole getRole(final Long appId, final Long appRoleId) {
    List<FnRole> roles = Optional.of(fnRoleDao.retrieveAppRoleByAppRoleIdAndByAppId(appId, appRoleId))
        .orElse(new ArrayList<>());
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

  public List<FnRole> getAppRoles(Long appId) {
    List<FnRole> applicationRoles;
    try {
      if (appId == 1) {
        applicationRoles = retrieveAppRolesWhereAppIdIsNull();
      } else {
        applicationRoles = retrieveAppRolesByAppId(appId);
      }
    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles: failed", e);
      throw e;
    }
    return applicationRoles;
  }

  public List<FnRole> retrieveAppRoleByAppRoleIdAndByAppId(final Long appId, final Long appRoleId) {
    return Optional.of(fnRoleDao.retrieveAppRoleByAppRoleIdAndByAppId(appId, appRoleId)).orElse(new ArrayList<>());
  }

  public List<FnRole> getUserRoleOnUserIdAndAppId(final Long userId, final Long appId) {
    return Optional.of(fnRoleDao.getUserRoleOnUserIdAndAppId(userId, appId)).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveAppRoleByRoleIdWhereAppIdIsNull(final Long roleId) {
    return Optional.of(fnRoleDao.retrieveAppRoleByRoleIdWhereAppIdIsNull(roleId)).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveAppRolesWhereAppIdIsNull() {
    return Optional.of(fnRoleDao.retrieveAppRolesWhereAppIdIsNull()).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveAppRolesByAppId(final Long id) {
    return Optional.of(fnRoleDao.retrieveAppRolesByAppId(id)).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveAppRolesByRoleNameAndByAppId(final String roleName, final Long appId) {
    return Optional.of(fnRoleDao.retrieveAppRolesByRoleNameAndByAppId(roleName, appId)).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveActiveRolesOfApplication(final Long appId) {
    return Optional.of(fnRoleDao.retrieveActiveRolesOfApplication(appId)).orElse(new ArrayList<>());
  }

  public List<FnRole> retrieveAppRolesByRoleNameAndWhereAppIdIsNull(final String roleName){
    return fnRoleDao.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(roleName).orElse(new ArrayList<>());
  }

  public List<FnRole> getGlobalRolesOfPortal() {
    List<FnRole> globalRoles = new ArrayList<>();
    try {
      globalRoles = Optional.of(fnRoleDao.getGlobalRolesOfPortal()).orElse(new ArrayList<>());
    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger, "getGlobalRolesOfPortal failed", e);
    }
    return globalRoles;
  }

  public Long getSysAdminRoleId(){
    FnRole role = fnRoleDao.getSysAdminRoleId();
    return role.getId();
  }

  public void delete(FnRole role) {
    fnRoleDao.delete(role);
  }

  public FnRole saveOne(final FnRole role){
    return fnRoleDao.save(role);
  }

  public List<FnRole> saveAll(List<FnRole> fnRoles) {
    return fnRoleDao.saveAll(fnRoles);
  }

  public List<FnRole> userAppGlobalRoles(final Long userId, final Long appId) {
      return fnRoleDao.userAppGlobalRoles(userId, appId).orElse(new ArrayList<>());
  }


  public List<FnRole> retrieveActiveRolesWhereAppIdIsNull() {
      return fnRoleDao.retrieveActiveRolesWhereAppIdIsNull().orElse(new ArrayList<>());
  }
}
