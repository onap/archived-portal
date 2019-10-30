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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.servlet.http.HttpServletResponse;
import org.apache.cxf.transport.http.HTTPException;
import org.onap.portal.dao.fn.FnUserRoleDao;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpUserRolesRequestDet;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.ecomp.EPUserAppCatalogRoles;
import org.onap.portal.domain.dto.ecomp.ExternalSystemAccess;
import org.onap.portal.domain.dto.transport.AppWithRolesForUser;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.RemoteRole;
import org.onap.portal.domain.dto.transport.RemoteUserWithRoles;
import org.onap.portal.domain.dto.transport.RoleInAppForUser;
import org.onap.portal.domain.dto.transport.UserApplicationRoles;
import org.onap.portal.service.ApplicationsRestClientService;
import org.onap.portal.service.ep.EpUserRolesRequestDetService;
import org.onap.portal.service.ep.EpUserRolesRequestService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FnUserRoleService {

       private static final String USER_APP_CATALOG_ROLES =
               "select\n"
                       + "  A.reqId as reqId,\n"
                       + "  B.requestedRoleId.roleId as requestedRoleId,\n"
                       + "  A.requestStatus as requestStatus,\n"
                       + "  A.appId.appId as appId,\n"
                       + "  (\n"
                       + "    select\n"
                       + "      roleName\n"
                       + "    from\n"
                       + "      FnRole\n"
                       + "    where\n"
                       + "      roleId = B.requestedRoleId.roleId\n"
                       + "  ) as roleName\n"
                       + "from\n"
                       + "  EpUserRolesRequest A\n"
                       + "  left join EpUserRolesRequestDet B on A.reqId = B.reqId.reqId\n"
                       + "where\n"
                       + "  A.userId.userId = :userid\n"
                       + "  and A.appId IN (\n"
                       + "    select\n"
                       + "      appId\n"
                       + "    from\n"
                       + "      FnApp\n"
                       + "    where\n"
                       + "      appName = :appName\n"
                       + "  )\n"
                       + "  and A.requestStatus = 'P'\n";

       private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnUserRoleService.class);
       private final FnUserRoleDao fnUserRoleDao;
       private final FnAppService fnAppService;
       private final FnRoleService fnRoleService;
       private final FnUserService fnUserService;
       private final EpUserRolesRequestService epUserRolesRequestService;
       private final EpUserRolesRequestDetService epUserRolesRequestDetService;
       private final EntityManager entityManager;
       private final ApplicationsRestClientService applicationsRestClientService;

       @Autowired
       public FnUserRoleService(FnUserRoleDao fnUserRoleDao, FnAppService fnAppService,
               FnRoleService fnRoleService,
               FnUserService fnUserService,
               EpUserRolesRequestService epUserRolesRequestService,
               EpUserRolesRequestDetService epUserRolesRequestDetService,
               EntityManager entityManager,
               ApplicationsRestClientService applicationsRestClientService) {
              this.fnUserRoleDao = fnUserRoleDao;
              this.fnAppService = fnAppService;
              this.fnRoleService = fnRoleService;
              this.fnUserService = fnUserService;
              this.epUserRolesRequestService = epUserRolesRequestService;
              this.epUserRolesRequestDetService = epUserRolesRequestDetService;
              this.entityManager = entityManager;
              this.applicationsRestClientService = applicationsRestClientService;
       }

       public List<FnUserRole> getAdminUserRoles(final Long userId, final Long roleId, final Long appId) {
              return fnUserRoleDao.getAdminUserRoles(userId, roleId, appId).orElse(new ArrayList<>());
       }

       public boolean isSuperAdmin(final String orgUserId, final Long roleId, final Long appId) {
              List<FnUserRole> roles = getUserRolesForRoleIdAndAppId(roleId, appId).stream()
                      .filter(role -> role.getUserId().getOrgUserId().equals(orgUserId)).collect(Collectors.toList());
              return !roles.isEmpty();
       }

       private List<FnUserRole> getUserRolesForRoleIdAndAppId(final Long roleId, final Long appId) {
              return Optional.of(fnUserRoleDao.getUserRolesForRoleIdAndAppId(roleId, appId)).orElse(new ArrayList<>());
       }

       public FnUserRole saveOne(final FnUserRole fnUserRole) {
              return fnUserRoleDao.save(fnUserRole);
       }

       public ExternalSystemAccess getExternalRequestAccess() {
              ExternalSystemAccess res = null;
              try {
                     res = new ExternalSystemAccess(EPCommonSystemProperties.EXTERNAL_ACCESS_ENABLE,
                             Boolean.parseBoolean(
                                     SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_ACCESS_ENABLE)));
              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger, "getExternalRequestAccess failed" + e.getMessage());
              }
              return res;
       }

       public List<EPUserAppCatalogRoles> getUserAppCatalogRoles(FnUser userid, String appName) {
              List<Tuple> tuples = entityManager.createQuery(USER_APP_CATALOG_ROLES, Tuple.class)
                      .setParameter("userid", userid.getUserId())
                      .setParameter("appName", appName)
                      .getResultList();
              return tuples.stream().map(this::tupleToEPUserAppCatalogRoles).collect(Collectors.toList());
       }

       private EPUserAppCatalogRoles tupleToEPUserAppCatalogRoles(Tuple tuple) {
              return new EPUserAppCatalogRoles((Long) tuple.get("reqId"), (Long) tuple.get("requestedRoleId"),
                      (String) tuple.get("roleName"), (String) tuple.get("requestStatus"), (Long) tuple.get("appId"));
       }

       private boolean postUserRolesToMylogins(AppWithRolesForUser userAppRolesData,
               ApplicationsRestClientService applicationsRestClientService, Long appId, Long userId)
               throws JsonProcessingException, HTTPException {
              boolean result = false;
              ObjectMapper mapper = new ObjectMapper();
              mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
              String userRolesAsString = mapper.writeValueAsString(userAppRolesData);
              logger.error(EELFLoggerDelegate.errorLogger,
                      "Should not be reached here, as the endpoint is not defined yet from the Mylogins");
              applicationsRestClientService.post(AppWithRolesForUser.class, appId, userRolesAsString,
                      String.format("/user/%s/myLoginroles", userId));
              return result;
       }

       public FieldsValidator putUserAppRolesRequest(AppWithRolesForUser newAppRolesForUser, FnUser user) {
              FieldsValidator fieldsValidator = new FieldsValidator();
              List<FnRole> appRole;
              try {
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "Should not be reached here, still the endpoint is yet to be defined");
                     boolean result = postUserRolesToMylogins(newAppRolesForUser, applicationsRestClientService,
                             newAppRolesForUser.getAppId(), user.getId());
                     logger.debug(EELFLoggerDelegate.debugLogger, "putUserAppRolesRequest: result {}", result);
                     FnApp app = fnAppService.getById(newAppRolesForUser.getAppId());
                     EpUserRolesRequest epUserRolesRequest = new EpUserRolesRequest();
                     epUserRolesRequest.setCreatedDate(LocalDateTime.now());
                     epUserRolesRequest.setUpdatedDate(LocalDateTime.now());
                     epUserRolesRequest.setUserId(user);
                     epUserRolesRequest.setAppId(app);
                     epUserRolesRequest.setRequestStatus("P");
                     List<RoleInAppForUser> appRoleIdList = newAppRolesForUser.getAppRoles();
                     Set<EpUserRolesRequestDet> appRoleDetails = new LinkedHashSet<>();
                     epUserRolesRequestService.saveOne(epUserRolesRequest);
                     for (RoleInAppForUser userAppRoles : appRoleIdList) {
                            Boolean isAppliedVal = userAppRoles.getIsApplied();
                            if (isAppliedVal) {
                                   appRole = fnRoleService
                                           .retrieveAppRoleByAppRoleIdAndByAppId(newAppRolesForUser.getAppId(),
                                                   userAppRoles.getRoleId());
                                   if (!appRole.isEmpty()) {
                                          EpUserRolesRequestDet epAppRoleDetail = new EpUserRolesRequestDet();
                                          epAppRoleDetail.setRequestedRoleId(appRole.get(0));
                                          epAppRoleDetail.setRequestType("P");
                                          epAppRoleDetail.setReqId(epUserRolesRequest);
                                          epUserRolesRequestDetService.saveOne(epAppRoleDetail);
                                   }
                            }
                     }
                     epUserRolesRequest.setEpRequestIdDetail(appRoleDetails);
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_OK);

              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger, "putUserAppRolesRequest failed", e);
                     fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              }
              return fieldsValidator;
       }

       public List<FnRole> importRolesFromRemoteApplication(Long appId) throws HTTPException {
              FnRole[] appRolesFull = applicationsRestClientService.get(FnRole[].class, appId, "/rolesFull");
              List<FnRole> rolesList = Arrays.asList(appRolesFull);
              for (FnRole externalAppRole : rolesList) {

                     // Try to find an existing extern role for the app in the local
                     // onap DB. If so, then use its id to update the existing external
                     // application role record.
                     Long externAppId = externalAppRole.getId();
                     FnRole existingAppRole = fnRoleService.getRole(appId, externAppId);
                     if (existingAppRole != null) {
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                    String.format(
                                            "ecomp role already exists for app=%s; appRoleId=%s. No need to import this one.",
                                            appId, externAppId));
                            continue;
                     }
                     // persistExternalRoleInEcompDb(externalAppRole, appId,
                     // roleService);
              }

              return rolesList;
       }

       public List<UserApplicationRoles> getUsersFromAppEndpoint(Long appId) throws HTTPException {
              ArrayList<UserApplicationRoles> userApplicationRoles = new ArrayList<>();

              FnApp app = fnAppService.getById(appId);
              //If local or centralized application
              if (PortalConstants.PORTAL_APP_ID.equals(appId) || app.getAuthCentral()) {
                     List<FnUser> userList = fnUserService.getActiveUsers();
                     for (FnUser user : userList) {
                            UserApplicationRoles userWithAppRoles = convertToUserApplicationRoles(appId, user, app);
                            if (userWithAppRoles.getRoles() != null && userWithAppRoles.getRoles().size() > 0) {
                                   userApplicationRoles.add(userWithAppRoles);
                            }
                     }

              }
              // remote app
              else {
                     RemoteUserWithRoles[] remoteUsers = null;
                     String remoteUsersString = applicationsRestClientService.getIncomingJsonString(appId, "/users");

                     remoteUsers = doGetUsers(isAppUpgradeVersion(app), remoteUsersString);

                     userApplicationRoles = new ArrayList<>();
                     for (RemoteUserWithRoles remoteUser : remoteUsers) {
                            UserApplicationRoles userWithRemoteAppRoles = convertToUserApplicationRoles(appId,
                                    remoteUser);
                            if (userWithRemoteAppRoles.getRoles() != null
                                    && userWithRemoteAppRoles.getRoles().size() > 0) {
                                   userApplicationRoles.add(userWithRemoteAppRoles);
                            } else {
                                   logger.debug(EELFLoggerDelegate.debugLogger,
                                           "User " + userWithRemoteAppRoles.getOrgUserId()
                                                   + " doesn't have any roles assigned to any app.");
                            }
                     }
              }

              return userApplicationRoles;
       }

       private UserApplicationRoles convertToUserApplicationRoles(Long appId, RemoteUserWithRoles remoteUser) {
              UserApplicationRoles userWithRemoteAppRoles = new UserApplicationRoles();
              userWithRemoteAppRoles.setAppId(appId);
              userWithRemoteAppRoles.setOrgUserId(remoteUser.getOrgUserId());
              userWithRemoteAppRoles.setFirstName(remoteUser.getFirstName());
              userWithRemoteAppRoles.setLastName(remoteUser.getLastName());
              userWithRemoteAppRoles.setRoles(remoteUser.getRoles());
              return userWithRemoteAppRoles;
       }

       private boolean isAppUpgradeVersion(FnApp app) {
              return true;
       }

       private RemoteUserWithRoles[] doGetUsers(boolean postOpenSource, String remoteUsersString) {

              ObjectMapper mapper = new ObjectMapper();
              try {
                     return mapper.readValue(remoteUsersString, RemoteUserWithRoles[].class);
              } catch (IOException e) {
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "doGetUsers : Failed : Unexpected property in incoming JSON",
                             e);
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "doGetUsers : Incoming JSON that caused it --> " + remoteUsersString);
              }

              return new RemoteUserWithRoles[0];
       }

       private UserApplicationRoles convertToUserApplicationRoles(Long appId, FnUser user, FnApp app) {
              UserApplicationRoles userWithRemoteAppRoles = new UserApplicationRoles();
              userWithRemoteAppRoles.setAppId(appId);
              userWithRemoteAppRoles.setOrgUserId(user.getOrgUserId());
              userWithRemoteAppRoles.setFirstName(user.getFirstName());
              userWithRemoteAppRoles.setLastName(user.getLastName());
              userWithRemoteAppRoles.setRoles(convertToRemoteRoleList(user, app));
              return userWithRemoteAppRoles;
       }

       private List<RemoteRole> convertToRemoteRoleList(FnUser user, FnApp app) {
              List<RemoteRole> roleList = new ArrayList<>();
              SortedSet<FnRole> roleSet = user.getAppEPRoles(app);
              for (FnRole role : roleSet) {
                     logger.debug(EELFLoggerDelegate.debugLogger, "In convertToRemoteRoleList() - for user {}, found Name {}", user.getOrgUserId(), role.getRoleName());
                     RemoteRole rRole = new RemoteRole();
                     rRole.setId(role.getId());
                     rRole.setName(role.getRoleName());
                     roleList.add(rRole);
              }

              //Get the active roles of user for that application using query
              List<FnRole> userEpRoleList = fnRoleService.getUserRoleOnUserIdAndAppId(user.getId(), app.getId());

              for (FnRole remoteUserRoleList : userEpRoleList) {

                     RemoteRole remoteRoleListId = roleList.stream().filter(x -> remoteUserRoleList.getId().equals(x.getId()))
                             .findAny().orElse(null);
                     if (remoteRoleListId == null) {
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                    "Adding the role to the rolelist () - for user {}, found Name {}", user.getOrgUserId(),

                                    remoteUserRoleList.getRoleName());
                            RemoteRole role = new RemoteRole();
                            role.setId(remoteUserRoleList.getId());
                            role.setName(remoteUserRoleList.getRoleName());

                            roleList.add(role);
                     }

              }

              logger.debug(EELFLoggerDelegate.debugLogger, "rolelist size of the USER() - for user {}, found RoleListSize {}", user.getOrgUserId(), roleList.size());

              return roleList;



       }
}
