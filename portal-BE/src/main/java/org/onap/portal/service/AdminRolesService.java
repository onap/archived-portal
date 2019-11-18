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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.transport.http.HTTPException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpUserRolesRequestDet;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.transport.AppWithRolesForUser;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.domain.dto.transport.EcompUserAppRoles;
import org.onap.portal.domain.dto.transport.ExternalAccessUser;
import org.onap.portal.domain.dto.transport.ExternalAccessUserRoleDetail;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.domain.dto.transport.ExternalRoleDescription;
import org.onap.portal.domain.dto.transport.RemoteRoleV1;
import org.onap.portal.domain.dto.transport.RoleInAppForUser;
import org.onap.portal.domain.dto.transport.RolesInAppForUser;
import org.onap.portal.exception.SyncUserRolesException;
import org.onap.portal.logging.format.EPAppMessagesEnum;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.ep.EpUserRolesRequestDetService;
import org.onap.portal.service.ep.EpUserRolesRequestService;
import org.onap.portal.service.fn.FnAppService;
import org.onap.portal.service.fn.FnRoleService;
import org.onap.portal.service.fn.FnUserRoleService;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class AdminRolesService {

  private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AdminRolesService.class);
  private static final Object syncRests = new Object();
  private RestTemplate template = new RestTemplate();

  private final Long SYS_ADMIN_ROLE_ID = 1L;
  private final Long ACCOUNT_ADMIN_ROLE_ID = 999L;
  private final Long ECOMP_APP_ID = 1L;
  private final String ADMIN_ACCOUNT = "Is account admin for user {}";

  private final AppsCacheService appsCacheService;
  private final EntityManager entityManager;
  private final FnUserService fnUserService;
  private final FnRoleService fnRoleService;
  private final FnAppService fnAppService;
  private final FnUserRoleService fnUserRoleService;
  private final EcompUserAppRolesService ecompUserAppRolesService;
  private final ApplicationsRestClientService applicationsRestClientService;
  private final EpUserRolesRequestDetService epUserRolesRequestDetService;
  private final ExternalAccessRolesService externalAccessRolesService;
  private final EpUserRolesRequestService epUserRolesRequestService;

  @Autowired
  public AdminRolesService(AppsCacheService appsCacheService,
      final EntityManager entityManager,
      final FnUserService fnUserService, FnRoleService fnRoleService,
      FnAppService fnAppService,
      final FnUserRoleService fnUserRoleService,
      EcompUserAppRolesService ecompUserAppRolesService,
      ApplicationsRestClientService applicationsRestClientService,
      EpUserRolesRequestDetService epUserRolesRequestDetService,
      ExternalAccessRolesService externalAccessRolesService,
      EpUserRolesRequestService epUserRolesRequestService) {
    this.appsCacheService = appsCacheService;
    this.entityManager = entityManager;
    this.fnUserService = fnUserService;
    this.fnRoleService = fnRoleService;
    this.fnAppService = fnAppService;
    this.fnUserRoleService = fnUserRoleService;
    this.ecompUserAppRolesService = ecompUserAppRolesService;
    this.applicationsRestClientService = applicationsRestClientService;
    this.epUserRolesRequestDetService = epUserRolesRequestDetService;
    this.externalAccessRolesService = externalAccessRolesService;
    this.epUserRolesRequestService = epUserRolesRequestService;
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
      List<Integer> userAdminApps = getAdminAppsForTheUser(user.getId());
      logger.debug(EELFLoggerDelegate.debugLogger,
          "Is account admin for userAdminApps() - for user {}, found userAdminAppsSize {}",
          user.getOrgUserId(), userAdminApps.size());

      if (user.getId() != null) {
        for (FnUserRole userApp : user.getUserApps()) {
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
        for (FnUserRole userApp : currentUser.getUserApps()) {
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

  public boolean isRoleAdmin(FnUser user) {
    try {
      logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has isRoleAdmin access");
      List getRoleFuncListOfUser = fnUserRoleService.getRoleFunctionsOfUserforAlltheApplications(user.getId());
      logger.debug(EELFLoggerDelegate.debugLogger,
          "Checking if user has isRoleAdmin access :: getRoleFuncListOfUser", getRoleFuncListOfUser);
      Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfUser);
      Set<String> getRoleFuncListOfPortalSet1 = new HashSet<>();
      Set<String> roleFunSet;
      roleFunSet = getRoleFuncListOfPortalSet.stream().filter(x -> x.contains("|"))
          .collect(Collectors.toSet());
      if (!roleFunSet.isEmpty()) {
        for (String roleFunction : roleFunSet) {
          String type = externalAccessRolesService.getFunctionCodeType(roleFunction);
          getRoleFuncListOfPortalSet1.add(type);
        }
      }

      boolean checkIfFunctionsExits = getRoleFuncListOfPortalSet1.stream()
          .anyMatch(roleFunction -> roleFunction.equalsIgnoreCase("Approver"));
      logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has approver rolefunction",
          checkIfFunctionsExits);

      return checkIfFunctionsExits;

    } catch (Exception e) {
      EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
      logger.error(EELFLoggerDelegate.errorLogger,
          "Exception occurred while executing isRoleAdmin operation",
          e);
    }
    return false;
  }

  public boolean isAccountAdminOfApplication(FnUser user, FnApp app) {
    boolean isApplicationAccountAdmin = false;
    try {
      logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, user.getId());
      List<Integer> userAdminApps = getAdminAppsForTheUser(user.getId());
      if (!userAdminApps.isEmpty()) {
        isApplicationAccountAdmin = userAdminApps.contains(app.getId());
        logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for user is true{} ,appId {}", user.getId(),
            app.getId());
      }
    } catch (Exception e) {
      EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
      logger.error(EELFLoggerDelegate.errorLogger,
          "Exception occurred while executing isAccountAdminOfApplication operation", e);
    }
    logger.debug(EELFLoggerDelegate.debugLogger,
        "In AdminRolesServiceImpl() - isAccountAdminOfApplication = {} and userId ={} ", isApplicationAccountAdmin,
        user.getOrgUserId());
    return isApplicationAccountAdmin;

  }

  private List<Integer> getAdminAppsForTheUser(final Long userId) {
    String query = "select fa.app_id from fn_user_role ur,fn_app fa where ur.user_id =:userId and ur.app_id=fa.app_id and ur.role_id= 999 and (fa.enabled = 'Y' || fa.app_id=1)";
    return entityManager.createQuery(query, Integer.class)
        .setParameter("userId", userId).getResultList();
  }

  public ExternalRequestFieldsValidator setAppWithUserRoleStateForUser(FnUser user,
      AppWithRolesForUser newAppRolesForUser) {
    boolean result = false;
    boolean epRequestValue = false;
    String userId = "";
    String reqMessage = "";
    if (newAppRolesForUser != null && newAppRolesForUser.getOrgUserId() != null) {
      userId = newAppRolesForUser.getOrgUserId().trim();
    }
    Long appId = newAppRolesForUser.getAppId();
    List<RoleInAppForUser> roleInAppForUserList = newAppRolesForUser.getAppRoles();

    if (userId.length() > 0) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      try {
        FnApp app = fnAppService.getById(appId);

        boolean checkIfUserisApplicationAccAdmin = isAccountAdminOfApplication(user,
            app);
        Set<EcompRole> rolesGotDeletedFromApprover = new TreeSet<>();

        boolean checkIfUserIsOnlyRoleAdmin =
            isRoleAdmin(user) && !checkIfUserisApplicationAccAdmin;
        if (checkIfUserIsOnlyRoleAdmin) {
          for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
            if (!roleInAppForUser.getIsApplied()) {
              EcompRole ecompRole = new EcompRole();
              ecompRole.setId(roleInAppForUser.getRoleId());
              ecompRole.setName(roleInAppForUser.getRoleName());
              rolesGotDeletedFromApprover.add(ecompRole);
            }
          }
        }

        applyChangesToUserAppRolesForMyLoginsRequest(user, appId);

        boolean systemUser = newAppRolesForUser.isSystemUser();

        if ((app.getAuthCentral() || app.getId().equals(PortalConstants.PORTAL_APP_ID))
            && systemUser) {

          Set<EcompRole> userRolesInLocalApp = postUsersRolesToLocalApp(roleInAppForUserList);
          RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(userId, appId,
              userRolesInLocalApp);
          List<RoleInAppForUser> roleAppUserList = rolesInAppForUser.getRoles();
          Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
          if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
            // Apply changes in external Access system

            updateUserRolesInExternalSystem(app, rolesInAppForUser.getOrgUserId(),
                roleAppUserList,
                epRequestValue, systemUser, rolesGotDeletedByApprover, false);
          }
          result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser, epRequestValue,
              "Portal",
              systemUser, rolesGotDeletedByApprover, false);

        } else if (!app.getAuthCentral() && systemUser) {
          throw new Exception("For non-centralized application we cannot add systemUser");
        } else {    // if centralized app
          if (app.getAuthCentral()) {
            if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
              pushRemoteUser(roleInAppForUserList, userId, app, mapper,
                  applicationsRestClientService, false);
            }

            Set<EcompRole> userRolesInLocalApp = postUsersRolesToLocalApp(
                roleInAppForUserList);
            RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(userId,
                appId,
                userRolesInLocalApp);
            List<RoleInAppForUser> roleAppUserList = rolesInAppForUser.getRoles();
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {

              // Apply changes in external Access system
              updateUserRolesInExternalSystem(app, rolesInAppForUser.getOrgUserId(),
                  roleAppUserList,
                  epRequestValue, false, rolesGotDeletedFromApprover,
                  checkIfUserIsOnlyRoleAdmin);
            }
            result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser,
                epRequestValue, "Portal", systemUser, rolesGotDeletedFromApprover,
                checkIfUserIsOnlyRoleAdmin);
          }
          // In case if portal is not centralized then follow existing approach
          else if (!app.getAuthCentral() && app.getId()
              .equals(PortalConstants.PORTAL_APP_ID)) {
            Set<EcompRole> userRolesInLocalApp = postUsersRolesToLocalApp(
                roleInAppForUserList);
            RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(userId,
                appId,
                userRolesInLocalApp);
            Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
            result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser,
                epRequestValue, "Portal", false, rolesGotDeletedByApprover, false);
          } else {// remote app
            FnUser remoteAppUser;
            if (!app.getAuthCentral() && !app.getId()
                .equals(PortalConstants.PORTAL_APP_ID)) {

              remoteAppUser = checkIfRemoteUserExits(userId, app,
                  applicationsRestClientService);

              if (remoteAppUser == null) {
                addRemoteUser(roleInAppForUserList, userId, app,
                    mapper, applicationsRestClientService);
              }
              Set<EcompRole> userRolesInRemoteApp = postUsersRolesToRemoteApp(
                  roleInAppForUserList, mapper,
                  applicationsRestClientService, appId, userId);
              RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(
                  userId, appId,
                  userRolesInRemoteApp);
              Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
              result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser,
                  epRequestValue, null, false, rolesGotDeletedByApprover, false);

              // If no roles remain, request app to set user inactive.
              if (userRolesInRemoteApp.size() == 0) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "setAppWithUserRoleStateForUser: no roles in app {}, set user {} to inactive",
                    app,
                    userId);
                postUserToRemoteApp(userId, app,
                    applicationsRestClientService);
              }
            }
          }
        }
      } catch (Exception e) {
        String message = String.format(
            "Failed to create user or update user roles for User %s, AppId %s",
            userId, Long.toString(appId));
        logger.error(EELFLoggerDelegate.errorLogger, message, e);
        result = false;
        reqMessage = e.getMessage();
      }
    }
    //return result;
    return new ExternalRequestFieldsValidator(result, reqMessage);

  }

  private void pushRemoteUser(List<RoleInAppForUser> roleInAppForUserList, String userId, FnApp app,
      ObjectMapper mapper, ApplicationsRestClientService applicationsRestClientService, boolean appRoleIdUsed)
      throws Exception {
    pushUserOnRemoteApp(userId, app, applicationsRestClientService, mapper,
        roleInAppForUserList, appRoleIdUsed);
  }


  private void postUserToRemoteApp(String userId, FnApp app,
      ApplicationsRestClientService applicationsRestClientService) throws HTTPException {

    getUser(userId, app, applicationsRestClientService);

  }

  private FnUser getUser(String userId, FnApp app, ApplicationsRestClientService applicationsRestClientService)
      throws HTTPException {
    return applicationsRestClientService.get(FnUser.class, app.getId(), String.format("/user/%s", userId), true);

  }

  private void pushUserOnRemoteApp(String userId, FnApp app,
      ApplicationsRestClientService applicationsRestClientService,
      ObjectMapper mapper, List<RoleInAppForUser> roleInAppForUserList, boolean appRoleIdUsed)
      throws Exception {

    FnUser client;
    client = fnUserService.loadUserByUsername(userId);

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    if (client == null) {
      String msg = "cannot create user " + userId + ", because he/she cannot be found in directory.";
      logger.error(EELFLoggerDelegate.errorLogger, msg);
      List<FnUser> userList = fnUserService.getUserWithOrgUserId(userId);
      if (!userList.isEmpty()) {
        logger.debug(EELFLoggerDelegate.debugLogger,
            userList.get(0).getOrgUserId() + " User was found in Portal");
        client = userList.get(0);
        client.setUserApps(Collections.EMPTY_SET);
        client.setIsSystemUser(false);
      } else {
        logger.error(EELFLoggerDelegate.errorLogger, "user cannot be found be in directory or in portal");
        throw new Exception(msg);
      }

    }

    client.setLoginId(userId);
    client.setActiveYn(true);
    client.setOrgUserId(userId);

    roleInAppForUserList.removeIf(role -> role.getIsApplied().equals(false));
    SortedSet<Role> roles = new TreeSet<>();

    List<FnRole> getAppRoles = externalAccessRolesService.getAppRoles(app.getId());
    List<FnApp> appList = new ArrayList<>();
    appList.add(app);
    List<CentralV2Role> roleList = new ArrayList<>();
    Map<String, Long> params = new HashMap<>();

    List<FnRole> userRoles = new ArrayList<>();

    for (RoleInAppForUser roleInappForUser : roleInAppForUserList) {
      FnRole role = new FnRole();
      role.setId(roleInappForUser.getRoleId());
      role.setRoleName(roleInappForUser.getRoleName());
      userRoles.add(role);
    }

    if (appRoleIdUsed) {
      List<FnRole> userAppRoles = new ArrayList<>();
      for (FnRole role : userRoles) {
        FnRole appRole = getAppRoles.stream()
            .filter(applicationRole -> role.getId().equals(applicationRole.getAppRoleId())).findAny()
            .orElse(null);
        FnRole epRole = new FnRole();
        if (appRole != null) {
          epRole.setId(appRole.getId());
          epRole.setRoleName(appRole.getRoleName());
        }
        userAppRoles.add(epRole);
      }
      userRoles = new ArrayList<>(userAppRoles);
    }
    roleList = externalAccessRolesService.createCentralRoleObject(appList, userRoles, roleList);

    for (CentralV2Role epRole : roleList) {
      Role role = new Role();
      FnRole appRole = getAppRoles.stream()
          .filter(applicationRole -> epRole.getId().equals(applicationRole.getId())).findAny().orElse(null);
      if (appRole != null) {
        role.setId(appRole.getAppRoleId());
        role.setName(epRole.getName());
        role.setRoleFunctions(epRole.getRoleFunctions());
      }
      roles.add(role);
    }
    client.setRoles(roles.stream().map(this::roleToFnRole).collect(Collectors.toSet()));
    String userInString;
    userInString = mapper.writerFor(FnUser.class).writeValueAsString(client);
    logger.debug(EELFLoggerDelegate.debugLogger,
        "about to post a client to remote application, users json = " + userInString);
    applicationsRestClientService.post(FnUser.class, app.getId(), userInString, String.format("/user/%s", userId));
  }

  private FnRole roleToFnRole(Role role) {
    return new FnRole(role.getId(), role.getName(), role.getActive(), role.getPriority(), role.getRoleFunctions(),
        role.getChildRoles(), role.getParentRoles());
  }

  private Set<EcompRole> postUsersRolesToRemoteApp(List<RoleInAppForUser> roleInAppForUserList, ObjectMapper mapper,
      ApplicationsRestClientService applicationsRestClientService, Long appId, String userId)
      throws JsonProcessingException, HTTPException {
    Set<EcompRole> updatedUserRolesinRemote = constructUsersRemoteAppRoles(roleInAppForUserList);
    Set<EcompRole> updateUserRolesInEcomp = constructUsersEcompRoles(roleInAppForUserList);
    String userRolesAsString = mapper.writeValueAsString(updatedUserRolesinRemote);
    FnApp externalApp;
    externalApp = appsCacheService.getApp(appId);
    String appBaseUri = null;
    Set<RemoteRoleV1> updatedUserRolesinRemoteV1 = new TreeSet<>();
    if (externalApp != null) {
      appBaseUri = externalApp.getAppRestEndpoint();
    }
    if (appBaseUri != null && appBaseUri.endsWith("/api")) {
      for (EcompRole eprole : updatedUserRolesinRemote) {
        RemoteRoleV1 role = new RemoteRoleV1();
        role.setId(eprole.getId());
        role.setName(eprole.getName());
        updatedUserRolesinRemoteV1.add(role);
      }
      userRolesAsString = mapper.writeValueAsString(updatedUserRolesinRemoteV1);
    }
    applicationsRestClientService.post(EcompRole.class, appId, userRolesAsString,
        String.format("/user/%s/roles", userId));
    return updateUserRolesInEcomp;
  }

  private void addRemoteUser(List<RoleInAppForUser> roleInAppForUserList, String userId, FnApp app,
      ObjectMapper mapper, ApplicationsRestClientService applicationsRestClientService) throws Exception {
    if (remoteUserShouldBeCreated(roleInAppForUserList)) {
      createNewUserOnRemoteApp(userId, app, applicationsRestClientService, mapper);
    }
  }

  private void createNewUserOnRemoteApp(String userId, FnApp app,
      ApplicationsRestClientService applicationsRestClientService, ObjectMapper mapper)
      throws Exception {

    FnUser client = fnUserService.loadUserByUsername(userId);

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    if (client == null) {
      String msg = "cannot create user " + userId + ", because he/she cannot be found in phonebook.";
      logger.error(EELFLoggerDelegate.errorLogger, msg);
      throw new Exception(msg);
    }

    client.setLoginId(userId);
    client.setActiveYn(true);

    String userInString;
    userInString = mapper.writerFor(FnUser.class).writeValueAsString(client);
    logger.debug(EELFLoggerDelegate.debugLogger,
        "about to post new client to remote application, users json = " + userInString);
    applicationsRestClientService.post(FnUser.class, app.getId(), userInString, String.format("/user", userId));

  }

  private boolean remoteUserShouldBeCreated(List<RoleInAppForUser> roleInAppForUserList) {
    for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
      if (roleInAppForUser.getIsApplied()) {
        return true;
      }
    }
    return false;
  }

  private Set<EcompRole> constructUsersRemoteAppRoles(List<RoleInAppForUser> roleInAppForUserList) {
    Set<EcompRole> existingUserRoles = new TreeSet<>();
    for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
      if (roleInAppForUser.getIsApplied() && !roleInAppForUser.getRoleId()
          .equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID)) {
        EcompRole ecompRole = new EcompRole();
        ecompRole.setId(roleInAppForUser.getRoleId());
        ecompRole.setName(roleInAppForUser.getRoleName());
        existingUserRoles.add(ecompRole);
      }
    }
    return existingUserRoles;
  }

  private void applyChangesToUserAppRolesForMyLoginsRequest(FnUser user, Long appId) {
    List<EpUserRolesRequest> epRequestIdVal;
    try {
      epRequestIdVal = epUserRolesRequestService.userAppRolesRequestList(user.getId(), appId);
      if (epRequestIdVal.size() > 0) {
        EpUserRolesRequest epAppRolesRequestData = epRequestIdVal.get(0);
        epAppRolesRequestData.setUpdatedDate(LocalDateTime.now());
        epAppRolesRequestData.setRequestStatus("O");
        epAppRolesRequestData.setUserId(user);
        epUserRolesRequestService.saveOne(epAppRolesRequestData);
        List<EpUserRolesRequestDet> epUserAppRolesDetailList = epUserRolesRequestDetService
            .appRolesRequestDetailList(epAppRolesRequestData.getReqId());
        if (epUserAppRolesDetailList.size() > 0) {
          for (EpUserRolesRequestDet epRequestUpdateList : epUserAppRolesDetailList) {
            epRequestUpdateList.setRequestType("O");
            epRequestUpdateList.setReqId(epAppRolesRequestData);
            epRequestUpdateList.setReqId(epAppRolesRequestData);
            epUserRolesRequestDetService.saveOne(epRequestUpdateList);
          }
          logger.debug(EELFLoggerDelegate.debugLogger,
              "User App roles request from User Page is overridden");
        }
      }

    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger, "applyChangesToUserAppRolesRequest failed", e);
    }
  }

  private Set<EcompRole> postUsersRolesToLocalApp(List<RoleInAppForUser> roleInAppForUserList) {
    return constructUsersEcompRoles(roleInAppForUserList);
  }

  private Set<EcompRole> constructUsersEcompRoles(List<RoleInAppForUser> roleInAppForUserList) {
    Set<EcompRole> existingUserRoles = new TreeSet<>();
    for (RoleInAppForUser roleInAppForUser : roleInAppForUserList) {
      if (roleInAppForUser.getIsApplied()) {
        EcompRole ecompRole = new EcompRole();
        ecompRole.setId(roleInAppForUser.getRoleId());
        ecompRole.setName(roleInAppForUser.getRoleName());
        existingUserRoles.add(ecompRole);
      }
    }
    return existingUserRoles;
  }

  public RolesInAppForUser constructRolesInAppForUserUpdate(String userId, Long appId,
      Set<EcompRole> userRolesInRemoteApp) {
    RolesInAppForUser result;
    result = new RolesInAppForUser();
    result.setAppId(appId);
    result.setOrgUserId(userId);

    for (EcompRole role : userRolesInRemoteApp) {
      RoleInAppForUser roleInAppForUser = new RoleInAppForUser();
      roleInAppForUser.setRoleId(role.getId());
      roleInAppForUser.setRoleName(role.getName());
      roleInAppForUser.setIsApplied(true);
      result.getRoles().add(roleInAppForUser);
    }
    return result;
  }

  private void updateUserRolesInExternalSystem(FnApp app, String orgUserId, List<RoleInAppForUser> roleInAppUser,
      boolean isPortalRequest, boolean isSystemUser, Set<EcompRole> deletedRolesByApprover,
      boolean isLoggedInUserRoleAdminofApp) throws Exception {
    try {
      List<FnUser> userInfo = checkIfUserExists(orgUserId);
      if (userInfo.isEmpty()) {
        createLocalUserIfNecessary(orgUserId, isSystemUser);
      }
      String name;
      if (EPCommonSystemProperties
          .containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
          && !isSystemUser) {
        name = orgUserId
            + SystemProperties
            .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
      } else {
        name = orgUserId;
      }
      ObjectMapper mapper = new ObjectMapper();
      HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
      HttpEntity<String> getUserRolesEntity = new HttpEntity<>(headers);
      ResponseEntity<String> getResponse = externalAccessRolesService
          .getUserRolesFromExtAuthSystem(name, getUserRolesEntity);

      List<ExternalAccessUserRoleDetail> userRoleDetailList = new ArrayList<>();
      String res = getResponse.getBody();
      JSONObject jsonObj;
      JSONArray extRoles = null;
      if (!res.equals("{}")) {
        jsonObj = new JSONObject(res);
        extRoles = jsonObj.getJSONArray("role");
      }
      ExternalAccessUserRoleDetail userRoleDetail;
      if (extRoles != null) {
        for (int i = 0; i < extRoles.length(); i++) {
          if (extRoles.getJSONObject(i).getString("name").startsWith(app.getAuthNamespace() + ".")
              && !extRoles.getJSONObject(i).getString("name")
              .equals(app.getAuthNamespace() + ".admin")
              && !extRoles.getJSONObject(i).getString("name")
              .equals(app.getAuthNamespace() + ".owner")) {
            if (extRoles.getJSONObject(i).has("description")) {
              ExternalRoleDescription desc = new ExternalRoleDescription(
                  extRoles.getJSONObject(i).getString("description"));
              userRoleDetail = new ExternalAccessUserRoleDetail(
                  extRoles.getJSONObject(i).getString("name"), desc);
              userRoleDetailList.add(userRoleDetail);
            } else {
              userRoleDetail = new ExternalAccessUserRoleDetail(
                  extRoles.getJSONObject(i).getString("name"), null);
              userRoleDetailList.add(userRoleDetail);
            }

          }
        }
      }

      List<ExternalAccessUserRoleDetail> userRoleListMatchingInExtAuthAndLocal = checkIfRoleAreMatchingInUserRoleDetailList(
          userRoleDetailList, app);

      List<EcompUserAppRoles> userAppList;
      // If request coming from portal not from external role approval system then we have to check if user already
      // have account admin or system admin as GUI will not send these roles
      if (!isPortalRequest) {
        FnUser user = fnUserService.getUserWithOrgUserId(orgUserId).get(0);
        userAppList = ecompUserAppRolesService.getUserAppExistingRoles(app.getAppId(), user.getUserId());
        if (!roleInAppUser.isEmpty()) {
          for (EcompUserAppRoles userApp : userAppList) {
            if (userApp.getRoleId().equals(PortalConstants.SYS_ADMIN_ROLE_ID)
                || userApp.getRoleId()
                .equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID)) {
              RoleInAppForUser addSpecialRole = new RoleInAppForUser();
              addSpecialRole.setIsApplied(true);
              addSpecialRole.setRoleId(userApp.getRoleId());
              addSpecialRole.setRoleName(userApp.getRoleName());
              roleInAppUser.add(addSpecialRole);
            }
          }
        }
      }
      List<RoleInAppForUser> roleInAppUserNonDupls = roleInAppUser.stream().distinct()
          .collect(Collectors.toList());
      Map<String, RoleInAppForUser> currentUserRolesToUpdate = new HashMap<>();
      for (RoleInAppForUser roleInAppUserNew : roleInAppUserNonDupls) {
        currentUserRolesToUpdate.put(roleInAppUserNew.getRoleName()
            .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS,
                "_"), roleInAppUserNew);
      }
      final Map<String, ExternalAccessUserRoleDetail> currentUserRolesInExternalSystem = new HashMap<>();
      for (ExternalAccessUserRoleDetail extAccessUserRole : userRoleListMatchingInExtAuthAndLocal) {
        currentUserRolesInExternalSystem.put(extAccessUserRole.getName(), extAccessUserRole);
      }

      if (isLoggedInUserRoleAdminofApp) {
        if (deletedRolesByApprover.size() > 0) {
          List<ExternalAccessUserRoleDetail> newUpdatedRoles = new ArrayList<>();
          for (ExternalAccessUserRoleDetail userRole : userRoleListMatchingInExtAuthAndLocal) {
            for (EcompRole role : deletedRolesByApprover) {
              if ((userRole.getName().substring(app.getAuthNamespace().length() + 1))
                  .equals(role.getName())) {
                newUpdatedRoles.add(userRole);
              }
            }
          }
          if (newUpdatedRoles.size() > 0) {
            userRoleListMatchingInExtAuthAndLocal = new ArrayList<>(newUpdatedRoles);
          } else {
            userRoleListMatchingInExtAuthAndLocal = new ArrayList<>();
            currentUserRolesToUpdate = new HashMap<>();

          }

        } else {
          userRoleListMatchingInExtAuthAndLocal = new ArrayList<>();
          currentUserRolesToUpdate = new HashMap<>();

        }
      }

      // Check if user roles does not exists in local but still there in External Central Auth System delete them all
      for (ExternalAccessUserRoleDetail userRole : userRoleListMatchingInExtAuthAndLocal) {
        if (!(currentUserRolesToUpdate
            .containsKey(userRole.getName().substring(app.getAuthNamespace().length() + 1)))) {
          HttpEntity<String> entity = new HttpEntity<>(headers);
          logger.debug(EELFLoggerDelegate.debugLogger,
              "updateUserRolesInExternalSystem: Connecting to external system to DELETE user role {}",
              userRole.getName());
          ResponseEntity<String> deleteResponse = template.exchange(
              SystemProperties
                  .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                  + "userRole/" + name + "/" + userRole.getName(),
              HttpMethod.DELETE, entity, String.class);
          logger.debug(EELFLoggerDelegate.debugLogger,
              "updateUserRolesInExternalSystem: Finished DELETE operation in external system for user role {} and the response is {}",
              userRole.getName(), deleteResponse.getBody());
        }
      }
      // Check if user roles does not exists in External Central Auth System add them all
      for (RoleInAppForUser addUserRole : roleInAppUserNonDupls) {
        if (!(currentUserRolesInExternalSystem
            .containsKey(app.getAuthNamespace() + "." + addUserRole.getRoleName().replaceAll(
                EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS,
                "_")))) {
          ExternalAccessUser extUser = new ExternalAccessUser(name,
              app.getAuthNamespace() + "." + addUserRole.getRoleName().replaceAll(
                  EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS,
                  "_"));
          String formattedUserRole = mapper.writeValueAsString(extUser);
          HttpEntity<String> entity = new HttpEntity<>(formattedUserRole, headers);
          logger.debug(EELFLoggerDelegate.debugLogger,
              "updateUserRolesInExternalSystem: Connecting to external system for user {} and POST {}",
              name, addUserRole.getRoleName());
          ResponseEntity<String> addResponse = template
              .exchange(SystemProperties
                  .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                  + "userRole", HttpMethod.POST, entity, String.class);
          logger.debug(EELFLoggerDelegate.debugLogger,
              "updateUserRolesInExternalSystem: Finished adding user role in external system {} and added user role {}",
              addResponse.getBody(), addUserRole.getRoleName());
          if (addResponse.getStatusCode().value() != 201
              && addResponse.getStatusCode().value() != 404) {
            logger.debug(EELFLoggerDelegate.debugLogger,
                "Finished POST operation in external system but unable to save user role",
                addResponse.getBody(),
                addUserRole.getRoleName());
            throw new Exception(addResponse.getBody());
          }
        }
      }
    } catch (HttpClientErrorException e) {
      logger.error(EELFLoggerDelegate.errorLogger,
          "updateUserRolesInExternalSystem: Failed to add user role for application {} due to {}",
          app.getId(), e);
      if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
        logger.error(EELFLoggerDelegate.errorLogger, "Please enter the valid systemUser",
            orgUserId);
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN,
            "Please enter the valid systemUser");
      }
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        logger.error(EELFLoggerDelegate.errorLogger, "Please enter the valid role");
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Please enter the valid role");
      }
      EPLogUtil.logExternalAuthAccessAlarm(logger, HttpStatus.BAD_REQUEST);
      throw e;
    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger,
          "updateUserRolesInExternalSystem: Failed to add user role for application {} due to {}",
          app.getId(), e);
      EPLogUtil.logExternalAuthAccessAlarm(logger, HttpStatus.BAD_REQUEST);
      throw e;
    }
  }

  private List<ExternalAccessUserRoleDetail> checkIfRoleAreMatchingInUserRoleDetailList(
      List<ExternalAccessUserRoleDetail> userRoleDetailList, FnApp app) {
    Map<String, FnRole> epRoleList = externalAccessRolesService.getAppRoleNamesWithUnderscoreMap(app);
    //Add Account Admin role for partner app to prevent conflict
    if (!PortalConstants.PORTAL_APP_ID.equals(app.getId())) {
      FnRole role = new FnRole();
      role.setRoleName(PortalConstants.ADMIN_ROLE
          .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
      epRoleList.put(role.getRoleName(), role);
    }
    userRoleDetailList.removeIf(
        userRoleDetail -> !epRoleList
            .containsKey(userRoleDetail.getName().substring(app.getAuthNamespace().length() + 1)));
    return userRoleDetailList;
  }

  private List<FnUser> checkIfUserExists(String userParams) {
    return fnUserService.getUserWithOrgUserId(userParams);
  }

  @Transactional
  private void createLocalUserIfNecessary(String userId, boolean isSystemUser) {
    if (StringUtils.isEmpty(userId)) {
      logger.error(EELFLoggerDelegate.errorLogger, "createLocalUserIfNecessary : empty userId!");
      return;
    }
    try {
      List<FnUser> userList = fnUserService.getUserWithOrgUserId(userId);
      if (userList.size() == 0) {
        FnUser client;
        if (!isSystemUser) {
          client = fnUserService.loadUserByUsername(userId);
        } else {
          client = new FnUser();
          client.setOrgUserId(userId);
          client.setIsSystemUser(true);
          client.setFirstName(userId.substring(0, userId.indexOf("@")));
        }
        if (client == null) {
          String msg = "createLocalUserIfNecessary: cannot create user " + userId
              + ", because not found in phonebook";
          logger.error(EELFLoggerDelegate.errorLogger, msg);
        } else {
          client.setLoginId(userId);
          client.setActiveYn(true);
        }
        fnUserService.saveFnUser(client);
      }
    } catch (Exception e) {
      EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
    }

  }

  private FnUser checkIfRemoteUserExits(String userId, FnApp app,
      ApplicationsRestClientService applicationsRestClientService) throws HTTPException {
    FnUser checkRemoteUser = null;
    try {
      checkRemoteUser = getUserFromApp(userId, app, applicationsRestClientService);
    } catch (HTTPException e) {
      // Some apps are returning 400 if user is not found.
      if (e.getResponseCode() == 400) {
        logger.debug(EELFLoggerDelegate.debugLogger,
            "setAppWithUserRoleStateForUser: getuserFromApp threw exception with response code 400; continuing",
            e);
      } else if (e.getResponseCode() == 404) {
        logger.debug(EELFLoggerDelegate.debugLogger,
            "setAppWithUserRoleStateForUser: getuserFromApp threw exception with response code 404; continuing",
            e);
      } else {
        // Other response code, let it come thru.
        throw e;
      }
    }
    return checkRemoteUser;
  }

  private FnUser getUserFromApp(String userId, FnApp app, ApplicationsRestClientService applicationsRestClientService)
      throws HTTPException {
    if (PortalConstants.PORTAL_APP_ID.equals(app.getId())) {
      List<FnUser> userList = fnUserService.getUserWithOrgUserId(userId);
      if (userList != null && !userList.isEmpty()) {
        return userList.get(0);
      } else {
        return null;
      }
    }
    return getUser(userId, app, applicationsRestClientService);
  }

  private boolean applyChangesInUserRolesForAppToEcompDB(RolesInAppForUser rolesInAppForUser,
      boolean externalSystemRequest, String reqType, boolean isSystemUser,
      Set<EcompRole> rolesDeletedByApprover, boolean isLoggedInUserRoleAdminOfApp) throws Exception {
    boolean result = false;
    String userId = rolesInAppForUser.getOrgUserId();
    Long appId = rolesInAppForUser.getAppId();
    synchronized (syncRests) {
      createLocalUserIfNecessary(userId, isSystemUser);

      EcompRole[] userAppRoles = new EcompRole[(int) rolesInAppForUser.getRoles().stream().distinct().count()];
      for (int i = 0;
          i < rolesInAppForUser.getRoles().stream().distinct().count(); i++) {
        RoleInAppForUser roleInAppForUser = rolesInAppForUser.getRoles().get(i);
        EcompRole role = new EcompRole();
        role.setId(roleInAppForUser.getRoleId());
        role.setName(roleInAppForUser.getRoleName());
        userAppRoles[i] = role;
      }
      try {
        EcompRole[] applicationRoles = null;

        if (isLoggedInUserRoleAdminOfApp) {
          List<EcompRole> roles = Arrays.stream(userAppRoles)
              .collect(Collectors.toList());
          List<EcompRole> roles1 = new ArrayList<>(rolesDeletedByApprover);
          roles.addAll(roles1);
          applicationRoles = roles.toArray(new EcompRole[0]);
        }

        syncUserRoles(userId, appId, userAppRoles, externalSystemRequest,
            reqType, isLoggedInUserRoleAdminOfApp, applicationRoles);
        result = true;
      } catch (Exception e) {
        logger.error(EELFLoggerDelegate.errorLogger,
            "applyChangesInUserRolesForAppToEcompDB: failed to syncUserRoles for orgUserId "
                + userId, e);
        if ("DELETE".equals(reqType)) {
          throw new Exception(e.getMessage());
        }
      }
    }
    return result;
  }

  private void syncUserRoles(String userId, Long appId,
      EcompRole[] userAppRoles, Boolean extRequestValue, String reqType, boolean checkIfUserisRoleAdmin,
      EcompRole[] appRoles) throws Exception {

    Transaction transaction = null;
    String roleActive;
    HashMap<Long, EcompRole> newUserAppRolesMap = hashMapFromEcompRoles(userAppRoles);
    List<FnRole> roleInfo = externalAccessRolesService
        .getPortalAppRoleInfo(PortalConstants.ACCOUNT_ADMIN_ROLE_ID);
    FnRole adminRole = new FnRole();
    if (roleInfo.size() > 0) {
      adminRole = roleInfo.get(0);
      logger.debug(EELFLoggerDelegate.debugLogger, "Admin RoleName form DB: " + adminRole.getRoleName());
    }
    try {
      List<FnUser> userList = fnUserService.getUserWithOrgUserId(userId);
      if (userList.size() > 0) {
        FnUser client = userList.get(0);
        roleActive = ("DELETE".equals(reqType)) ? "" : " and role.active = 'Y'";
        List<FnUserRole> userRoles = fnUserRoleService.retrieveByAppIdAndUserId(appId, userId);
        entityManager
            .createQuery("from EPUserApp where app.id=:appId and userId=:userId" + roleActive)
            .setParameter("appId", appId)
            .setParameter("userId", client.getId())
            .getResultList();

        if ("DELETE".equals(reqType)) {
          for (FnUserRole userAppRoleList : userRoles) {
            List<FnRole> rolesList =
                (!userAppRoleList.getRoleId().getRoleName()
                    .equals(adminRole.getRoleName()))
                    ? fnRoleService.retrieveAppRolesByRoleNameAndByAppId
                    (userAppRoleList.getRoleId().getRoleName(), appId)
                    : fnRoleService.retrieveAppRolesWhereAppIdIsNull();
            if (!rolesList.isEmpty()) {
              checkIfRoleInactive(rolesList.get(0));
            }
          }
        }

        if (appRoles != null) {
          List<EcompRole> appRolesList = Arrays.stream(appRoles).collect(Collectors.toList());
          List<FnUserRole> finalUserRolesList = new ArrayList<>();
          if (checkIfUserisRoleAdmin) {
            for (EcompRole role : appRolesList) {
              for (FnUserRole userAppRoleList : userRoles) {
                if (userAppRoleList.getRoleId().getRoleName()
                    .equals(role.getName())) {
                  finalUserRolesList.add(userAppRoleList);
                }

              }
            }
            userRoles = new ArrayList<>(finalUserRolesList);
          }
        }

        for (FnUserRole userRole : userRoles) {
          if (!PortalConstants.ACCOUNT_ADMIN_ROLE_ID.equals(userRole.getRoleId().getRoleId())
              && !PortalConstants.SYS_ADMIN_ROLE_ID
              .equals(userRole.getRoleId().getRoleId())
              && !extRequestValue) {
            syncUserRolesExtension(userRole, appId,
                newUserAppRolesMap);
          } else if (extRequestValue && ("PUT".equals(reqType) || "POST".equals(reqType)
              || "DELETE".equals(reqType))) {
            syncUserRolesExtension(userRole, appId,
                newUserAppRolesMap);
          } else if (extRequestValue && !PortalConstants.ACCOUNT_ADMIN_ROLE_ID
              .equals(userRole.getRoleId().getRoleId())) {
            syncUserRolesExtension(userRole, appId,
                newUserAppRolesMap);
          }
        }

        Collection<EcompRole> newRolesToAdd = newUserAppRolesMap.values();
        if (newRolesToAdd.size() > 0) {
          FnApp app = fnAppService.getById(appId);

          HashMap<Long, FnRole> rolesMap = new HashMap<>();
          if (appId.equals(PortalConstants.PORTAL_APP_ID)) { // local app
            String appIdValue = "";
            if (!extRequestValue) {
              appIdValue = "and id != " + PortalConstants.SYS_ADMIN_ROLE_ID;
            }
            @SuppressWarnings("unchecked")
            List<FnRole> roles = entityManager
                .createQuery(
                    "from " + FnRole.class.getName() + " where appId is null "
                        + appIdValue).getResultList();
            for (FnRole role : roles) {
              role.setAppId(1L);
              rolesMap.put(role.getId(), role);
            }
          } else { // remote app
            @SuppressWarnings("unchecked")
            List<FnRole> roles = entityManager
                .createQuery("from EPRole where appId=:appId")
                .setParameter("appId", appId)
                .getResultList();
            for (FnRole role : roles) {
              if (!extRequestValue && app.getAuthCentral()) {
                rolesMap.put(role.getId(), role);
              } else {
                rolesMap.put(role.getAppRoleId(), role);
              }
            }
          }

          FnRole role;
          for (EcompRole userRole : newRolesToAdd) {
            FnUserRole userApp = new FnUserRole();
            if (("PUT".equals(reqType) || "POST".equals(reqType)) && userRole.getName()
                .equals(adminRole.getRoleName())) {
              role = fnRoleService.getById(PortalConstants.ACCOUNT_ADMIN_ROLE_ID);
              userApp.setRoleId(role);
            } else if ((userRole.getId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID))
                && !extRequestValue) {
              continue;
            } else if ((userRole.getId().equals(PortalConstants.SYS_ADMIN_ROLE_ID)) && app
                .getId().equals(PortalConstants.PORTAL_APP_ID) && !extRequestValue) {
              continue;
            } else {
              userApp.setRoleId(rolesMap.get(userRole.getId()));
            }

            userApp.setUserId(client);
            userApp.setAppId(app);
            fnUserRoleService.saveOne(userApp);
          }

          if (PortalConstants.PORTAL_APP_ID.equals(appId)) {
            /*
             * for local app -- hack - always make sure fn_role
             * table's app_id is null and not 1 for primary app in
             * this case being onap portal app; reason: hibernate
             * is rightly setting this to 1 while persisting to
             * fn_role as per the mapping but SDK role management
             * code expects the app_id to be null as there is no
             * concept of App_id in SDK
             */
            Query query = entityManager.createQuery("update fn_role set app_id = null where app_id = 1 ");
            query.executeUpdate();
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger, "syncUserRoles failed", e);
      EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
      EcompPortalUtils.rollbackTransaction(transaction,
          "Exception occurred in syncUserRoles, Details: " + e.toString());
      if ("DELETE".equals(reqType)) {
        throw new SyncUserRolesException(e.getMessage());
      }
    }
  }

  private static HashMap<Long, EcompRole> hashMapFromEcompRoles(EcompRole[] ecompRoles) {
    HashMap<Long, EcompRole> result = new HashMap<>();
    if (ecompRoles != null) {
      for (EcompRole ecompRole : ecompRoles) {
        if (ecompRole.getId() != null) {
          result.put(ecompRole.getId(), ecompRole);
        }
      }
    }
    return result;
  }

  private void syncUserRolesExtension(FnUserRole userRole, Long appId,
      HashMap<Long, EcompRole> newUserAppRolesMap) {

    Long userAppRoleId;
    if (PortalConstants.PORTAL_APP_ID.equals(appId)) { // local app
      userAppRoleId = userRole.getRoleId().getRoleId();
    } else { // remote app
      userAppRoleId = userRole.getId();
    }

    if (!newUserAppRolesMap.containsKey(userAppRoleId)) {
      fnUserRoleService.deleteById(userRole.getId());
    } else {
      newUserAppRolesMap.remove(userAppRoleId);
    }
  }

  private void checkIfRoleInactive(FnRole epRole) throws Exception {
    if (!epRole.getActiveYn()) {
      throw new Exception(epRole.getRoleName() + " role is unavailable");
    }
  }
}
