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
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.transport.http.HTTPException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.ep.EpUserRolesRequest;
import org.onap.portal.domain.db.ep.EpUserRolesRequestDet;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnFunction;
import org.onap.portal.domain.db.fn.FnMenuFunctional;
import org.onap.portal.domain.db.fn.FnMenuFunctionalRoles;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnRoleFunction;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.model.ExternalSystemRoleApproval;
import org.onap.portal.domain.dto.model.ExternalSystemUser;
import org.onap.portal.domain.dto.transport.AppNameIdIsAdmin;
import org.onap.portal.domain.dto.transport.AppWithRolesForUser;
import org.onap.portal.domain.dto.transport.AppsListWithAdminRole;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.domain.dto.transport.EPUserAppCurrentRoles;
import org.onap.portal.domain.dto.transport.EcompUserAppRoles;
import org.onap.portal.domain.dto.transport.ExternalAccessUser;
import org.onap.portal.domain.dto.transport.ExternalAccessUserRoleDetail;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.domain.dto.transport.ExternalRoleDescription;
import org.onap.portal.domain.dto.transport.RemoteRoleV1;
import org.onap.portal.domain.dto.transport.Role;
import org.onap.portal.domain.dto.transport.RoleInAppForUser;
import org.onap.portal.domain.dto.transport.RolesInAppForUser;
import org.onap.portal.exception.DeleteDomainObjectFailedException;
import org.onap.portal.exception.RoleFunctionException;
import org.onap.portal.exception.SyncUserRolesException;
import org.onap.portal.logging.format.EPAppMessagesEnum;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.app.FnAppService;
import org.onap.portal.service.appFunction.EpAppFunctionService;
import org.onap.portal.service.menuFunctional.FnMenuFunctionalService;
import org.onap.portal.service.menuFunctionalRoles.FnMenuFunctionalRolesService;
import org.onap.portal.service.role.FnRoleService;
import org.onap.portal.service.roleFunction.FnRoleFunctionService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.service.userRole.FnUserRoleService;
import org.onap.portal.service.userRolesRequest.EpUserRolesRequestService;
import org.onap.portal.service.userRolesRequestDet.EpUserRolesRequestDetService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EPUserUtils;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.RoleFunction;
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

    private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AdminRolesService.class);
    private static final Object syncRests = new Object();
    private final RestTemplate template = new RestTemplate();

    private Long SYS_ADMIN_ROLE_ID = 38L;
    private final Long ACCOUNT_ADMIN_ROLE_ID = 999L;
    private final Long ECOMP_APP_ID = 1L;
    private final String ADMIN_ACCOUNT = "Is account admin for user {}";

    private final AppsCacheService appsCacheService;
    private final EntityManager entityManager;
    private final FnUserService fnUserService;
    private final FnRoleService fnRoleService;
    private final FnAppService fnAppService;
    private final FnRoleFunctionService fnRoleFunctionService;
    private final FnMenuFunctionalService fnMenuFunctionalService;
    private final FnUserRoleService fnUserRoleService;
    private final EpAppFunctionService epAppFunctionService;
    private final EcompUserAppRolesService ecompUserAppRolesService;
    private final FnMenuFunctionalRolesService fnMenuFunctionalRolesService;
    private final ApplicationsRestClientService applicationsRestClientService;
    private final EpUserRolesRequestDetService epUserRolesRequestDetService;
    private final ExternalAccessRolesService externalAccessRolesService;
    private final EpUserRolesRequestService epUserRolesRequestService;

    @Autowired
    public AdminRolesService(AppsCacheService appsCacheService,
        final EntityManager entityManager,
        final FnUserService fnUserService, FnRoleService fnRoleService,
        FnAppService fnAppService,
        FnRoleFunctionService fnRoleFunctionService, FnMenuFunctionalService fnMenuFunctionalService,
        final FnUserRoleService fnUserRoleService,
        EpAppFunctionService epAppFunctionService,
        EcompUserAppRolesService ecompUserAppRolesService,
        FnMenuFunctionalRolesService fnMenuFunctionalRolesService,
        ApplicationsRestClientService applicationsRestClientService,
        EpUserRolesRequestDetService epUserRolesRequestDetService,
        ExternalAccessRolesService externalAccessRolesService,
        EpUserRolesRequestService epUserRolesRequestService) {
        this.appsCacheService = appsCacheService;
        this.entityManager = entityManager;
        this.fnUserService = fnUserService;
        this.fnRoleService = fnRoleService;
        this.fnAppService = fnAppService;
        this.fnRoleFunctionService = fnRoleFunctionService;
        this.fnMenuFunctionalService = fnMenuFunctionalService;
        this.fnUserRoleService = fnUserRoleService;
        this.epAppFunctionService = epAppFunctionService;
        this.ecompUserAppRolesService = ecompUserAppRolesService;
        this.fnMenuFunctionalRolesService = fnMenuFunctionalRolesService;
        this.applicationsRestClientService = applicationsRestClientService;
        this.epUserRolesRequestDetService = epUserRolesRequestDetService;
        this.externalAccessRolesService = externalAccessRolesService;
        this.epUserRolesRequestService = epUserRolesRequestService;
    }

    public boolean isSuperAdmin(final String loginId) {
        boolean isSuperAdmin;
        try {
            isSuperAdmin = fnUserRoleService
                .isSuperAdmin(loginId, SYS_ADMIN_ROLE_ID, ECOMP_APP_ID);
        } catch (Exception e) {
            logger.error("isSuperAdmin exception: " + e.toString());
            throw e;
        }
        logger.info("isSuperAdmin " + isSuperAdmin);
        return isSuperAdmin;
    }

    public boolean isAccountAdmin(final long userId, final String orgUserId, final Set<FnUserRole> userApps) {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, userId);
            List<Integer> userAdminApps = getAdminAppsForTheUser(userId);
            logger.debug(EELFLoggerDelegate.debugLogger,
                "Is account admin for userAdminApps() - for user {}, found userAdminAppsSize {}",
                orgUserId, userAdminApps.size());

            for (FnUserRole userApp : userApps) {
                if (userApp.getRoleId().getId().equals(ACCOUNT_ADMIN_ROLE_ID) || (
                    userAdminApps.size() > 1)) {
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "Is account admin for userAdminApps() - for user {}, found Id {}",
                        orgUserId, userApp.getRoleId().getId());
                    return true;
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

    public boolean isUser(final long userId) {
        try {
            FnUser currentUser = fnUserService.getUser(userId).orElseThrow(Exception::new);
            if (currentUser != null && currentUser.getId() != null) {
                for (FnUserRole userApp : currentUser.getUserApps()) {
                    if (!userApp.getFnAppId().getId().equals(ECOMP_APP_ID)) {
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

    public boolean isRoleAdmin(Long userId) {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "Checking if user has isRoleAdmin access");
            List getRoleFuncListOfUser = fnUserRoleService.getRoleFunctionsOfUserforAlltheApplications(userId);
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

    public boolean isAccountAdminOfApplication(Long userId, FnApp app) {
        boolean isApplicationAccountAdmin = false;
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, ADMIN_ACCOUNT, userId);
            List<Integer> userAdminApps = getAdminAppsForTheUser(userId);
            if (!userAdminApps.isEmpty()) {
                isApplicationAccountAdmin = userAdminApps.contains(app.getId());
                logger.debug(EELFLoggerDelegate.debugLogger, "Is account admin for user is true{} ,appId {}", userId,
                    app.getId());
            }
        } catch (Exception e) {
            EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
            logger.error(EELFLoggerDelegate.errorLogger,
                "Exception occurred while executing isAccountAdminOfApplication operation", e);
        }
        logger.debug(EELFLoggerDelegate.debugLogger,
            "In AdminRolesServiceImpl() - isAccountAdminOfApplication = {} and userId ={} ", isApplicationAccountAdmin,
            userId);
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

                boolean checkIfUserisApplicationAccAdmin = isAccountAdminOfApplication(user.getId(),
                    app);
                Set<EcompRole> rolesGotDeletedFromApprover = new TreeSet<>();

                boolean checkIfUserIsOnlyRoleAdmin =
                    isRoleAdmin(user.getId()) && !checkIfUserisApplicationAccAdmin;
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

        List<FnRole> getAppRoles = fnRoleService.getAppRoles(app.getId());
        List<FnApp> appList = new ArrayList<>();
        appList.add(app);
        List<CentralV2Role> roleList = new ArrayList<>();

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
            List<FnRoleFunction> fnRoleFunctions = new ArrayList<>();
            for (DomainVo vo : epRole.getRoleFunctions()) {
                Optional<FnRoleFunction> roleFunction = fnRoleFunctionService.findById(vo.getId());
                roleFunction.ifPresent(fnRoleFunctions::add);
            }
            if (appRole != null) {
                role.setId(appRole.getAppRoleId());
                role.setRoleName(epRole.getName());
                role.setFnRoleFunctions(new HashSet<>(fnRoleFunctions));
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
        return FnRole.builder()
            .id(role.getId())
            .roleName(role.getRoleName())
            .activeYn(role.getActiveYn())
            .priority(role.getPriority())
            .fnRoleFunctions(role.getFnRoleFunctions())
            .childRoles(role.getChildRoles())
            .parentRoles(role.getParentRoles())
            .build();
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
                userAppList = ecompUserAppRolesService.getUserAppExistingRoles(app.getId(), user.getId());
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
                    if (!PortalConstants.ACCOUNT_ADMIN_ROLE_ID.equals(userRole.getRoleId().getId())
                        && !PortalConstants.SYS_ADMIN_ROLE_ID
                        .equals(userRole.getRoleId().getId())
                        && !extRequestValue) {
                        syncUserRolesExtension(userRole, appId,
                            newUserAppRolesMap);
                    } else if (extRequestValue && ("PUT".equals(reqType) || "POST".equals(reqType)
                        || "DELETE".equals(reqType))) {
                        syncUserRolesExtension(userRole, appId,
                            newUserAppRolesMap);
                    } else if (extRequestValue && !PortalConstants.ACCOUNT_ADMIN_ROLE_ID
                        .equals(userRole.getRoleId().getId())) {
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
                        userApp.setFnAppId(app);
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
            userAppRoleId = userRole.getRoleId().getId();
        } else { // remote app
            userAppRoleId = userRole.getId();
        }

        if (!newUserAppRolesMap.containsKey(userAppRoleId)) {
            fnUserRoleService.deleteById(userRole.getId());
        } else {
            newUserAppRolesMap.remove(userAppRoleId);
        }
    }

    private Role fnRoleToRole(final FnRole role) {
        return new Role(null, null, null, null, null, null, null, null, null, role.getRoleName(), null,
            role.getActiveYn(),
            role.getPriority(), role.getFnRoleFunctions(), role.getChildRoles(), role.getParentRoles());
    }

    @SuppressWarnings("unchecked")
    public List<RoleInAppForUser> getAppRolesForUser(Long appId, String orgUserId, Boolean extRequestValue,
        Long userId) {
        List<RoleInAppForUser> rolesInAppForUser = null;
        FnApp app = fnAppService.getById(appId);
        logger.debug(EELFLoggerDelegate.debugLogger, "In getAppRolesForUser() - app = {}", app);
        try {
            // for onap portal app, no need to make a remote call
            List<Role> roleList = new ArrayList<>();
            if (!PortalConstants.PORTAL_APP_ID.equals(appId)) {
                if (app.getAuthCentral()) {
                    List<CentralV2Role> cenRoleList = externalAccessRolesService.getRolesForApp(app.getUebKey());
                    for (CentralV2Role cenRole : cenRoleList) {
                        Role role = new Role();
                        role.setActiveYn(cenRole.isActive());
                        role.setId(cenRole.getId());
                        role.setRoleName(cenRole.getName());
                        role.setPriority(cenRole.getPriority());
                        roleList.add(role);
                    }
                } else {
                    Optional<FnUser> user = fnUserService.getUser(userId);
                    if (user.isPresent()) {
                        roleList = user.get().getFnRoles().stream().map(this::fnRoleToRole)
                            .collect(Collectors.toList());
                    }
                }
                List<Role> activeRoleList = new ArrayList<>();
                for (Role role : roleList) {
                    if (role.getActiveYn()) {
                        if (role.getId() != 1) { // prevent portal admin from being added
                            activeRoleList.add(role);
                        } else if (extRequestValue) {
                            activeRoleList.add(role);
                        }
                    }

                }
                FnUser localUser = getUserFromApp(Long.toString(userId), app, applicationsRestClientService);
                // If localUser does not exists return roles
                Set<FnRole> roleSet = null;
                FnRole[] roleSetList = null;
                if (localUser != null) {
                    roleSet = localUser.getAppEPRoles(app);
                    roleSetList = roleSet.toArray(new FnRole[0]);
                }
                rolesInAppForUser = fnUserRoleService
                    .constructRolesInAppForUserGet(activeRoleList, roleSetList, extRequestValue);
                return rolesInAppForUser;
            }

            EcompRole[] appRoles = null;
            boolean checkIfUserIsApplicationAccAdmin = false;
            List<EcompRole> roles = new ArrayList<>();
            if (app.getAuthCentral()) {
                List<FnRole> applicationRoles = fnRoleService.retrieveActiveRolesOfApplication(app.getId());
                FnApp application = fnAppService.getById(appId);
                checkIfUserIsApplicationAccAdmin = isAccountAdminOfApplication(userId,
                    application);

                List<FnRole> roleSetWithFunctioncds = new ArrayList<>();
                for (FnRole role : applicationRoles) {
                    List<EpAppFunction> cenRoleFuncList = epAppFunctionService
                        .getAppRoleFunctionList(role.getId(), app.getId());
                    for (EpAppFunction roleFunc : cenRoleFuncList) {

                        String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
                        functionCode = EPUserUtils.decodeFunctionCode(functionCode);
                        String type = externalAccessRolesService.getFunctionCodeType(roleFunc.getFunctionCd());
                        String action = externalAccessRolesService.getFunctionCodeAction(roleFunc.getFunctionCd());
                        String name = roleFunc.getFunctionName();

                        FnFunction function = new FnFunction();
                        function.setAction(action);
                        function.setType(type);
                        function.setCode(functionCode);
                        function.setName(name);
                        role.getFnRoleFunctions().add(new FnRoleFunction(role, function));

                    }
                    roleSetWithFunctioncds.add(role);


                }

                for (FnRole role1 : roleSetWithFunctioncds) {
                    EcompRole ecompRole = new EcompRole();
                    ecompRole.setId(role1.getId());
                    ecompRole.setName(role1.getRoleName());
                    ecompRole.setRoleFunctions(role1.getRoleFunctions());
                    roles.add(ecompRole);

                }
                if (checkIfUserIsApplicationAccAdmin) {
                    appRoles = roles.toArray(new EcompRole[roles.size()]);
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "In getAppRolesForUser() If Logged in user checkIfUserisApplicationAccAdmin- appRoles = {}",
                        appRoles);
                } else if (isRoleAdmin(userId) && !checkIfUserIsApplicationAccAdmin) {
                    List<EcompRole> roleAdminAppRoles = new ArrayList<>();
                    List<String> roleAdminAppRolesNames = new ArrayList<>();
                    String QUERY =
                        "select distinct fr.role_name as roleName from fn_user_role fu, ep_app_role_function ep, ep_app_function ea, fn_role fr"
                            + " where fu.role_id = ep.role_id"
                            + " and fu.app_id = ep.app_id"
                            + " and fu.user_id = :userId"
                            + " and fu.role_id = fr.role_id and fr.active_yn='Y'"
                            + " and ea.function_cd = ep.function_cd and ea.function_cd like 'approver|%'"
                            + " and exists"
                            + " ("
                            + " select fa.app_id from fn_user fu, fn_user_role ur, fn_app fa where fu.user_id =:userId and fu.user_id = ur.user_id"
                            + " and ur.app_id = fa.app_id and fa.enabled = 'Y')";
                    List<Tuple> tuples = entityManager.createNativeQuery(QUERY, Tuple.class)
                        .setParameter("userId", userId)
                        .getResultList();
                    List<String> getUserApproverRoles = tuples.stream().map(tuple -> (String) tuple.get("roleName"))
                        .collect(Collectors.toList());

                    List<EcompRole> userapproverRolesList = new ArrayList<>();
                    for (String str : getUserApproverRoles) {
                        EcompRole epRole = roles.stream().filter(x -> str.equals(x.getName())).findAny().orElse(null);
                        if (epRole != null) {
                            userapproverRolesList.add(epRole);
                        }
                    }
                    for (EcompRole role : userapproverRolesList) {

                        List<RoleFunction> roleFunList = new ArrayList<>(role.getRoleFunctions());
                        boolean checkIfFunctionsExits = roleFunList.stream()
                            .anyMatch(roleFunction -> roleFunction.getType().equalsIgnoreCase("Approver"));
                        if (checkIfFunctionsExits) {
                            roleAdminAppRoles.add(role);
                            List<RoleFunction> filteredList = roleFunList.stream()
                                .filter(x -> "Approver".equalsIgnoreCase(x.getType())).collect(Collectors.toList());
                            roleAdminAppRolesNames.addAll(filteredList.stream().map(RoleFunction::getCode)
                                .collect(Collectors.toList()));
                        }
                    }
                    for (String name : roleAdminAppRolesNames) {
                        roles.stream().filter(x -> name.equals(x.getName())).findAny()
                            .ifPresent(roleAdminAppRoles::add);

                    }
                    appRoles = roleAdminAppRoles.toArray(new EcompRole[0]);

                }
            } else {
                appRoles = applicationsRestClientService.get(EcompRole[].class, appId, "/roles");
            }
            // Test this error case, for generating an internal ONAP Portal
            // error
            // EcompRole[] appRoles = null;
            // If there is an exception in the rest client api, then null will
            // be returned.
            if (appRoles != null) {
                if (!app.getAuthCentral()) {
                    syncAppRoles(appId, appRoles);
                }
                EcompRole[] userAppRoles = null;
                try {
                    try {
                        if (app.getAuthCentral()) {
                            List<FnUser> actualUser = fnUserService.getUserWithOrgUserId(Long.toString(userId));
                            List<EPUserAppCurrentRoles> userAppsRolesList = entityManager
                                .createNamedQuery("EPUserAppCurrentRoles")
                                .setParameter("appId", app.getId())
                                .setParameter("userId", actualUser.get(0).getId())
                                .getResultList();
                            List<EcompRole> setUserRoles = new ArrayList<>();
                            for (EPUserAppCurrentRoles role : userAppsRolesList) {
                                logger.debug(EELFLoggerDelegate.debugLogger,
                                    "In getAppRolesForUser() - userAppsRolesList get userRolename = {}",
                                    role.getRoleName());
                                EcompRole ecompRole = new EcompRole();
                                ecompRole.setId(role.getRoleId());
                                ecompRole.setName(role.getRoleName());
                                setUserRoles.add(ecompRole);
                            }

                            boolean checkIfUserisAccAdmin = setUserRoles.stream()
                                .anyMatch(ecompRole -> ecompRole.getId() == 999L);

                            if (!checkIfUserisAccAdmin) {
                                List<EcompRole> appRolesList = Arrays.asList(appRoles);
                                Set<EcompRole> finalUserAppRolesList = new HashSet<>();

                                List<String> roleNames = new ArrayList<>();
                                for (EcompRole role : setUserRoles) {
                                    EcompRole epRole = appRolesList.stream()
                                        .filter(x -> role.getName().equals(x.getName())).findAny().orElse(null);
                                    List<RoleFunction> roleFunList = new ArrayList<>();

                                    if (epRole != null) {
                                        if (epRole.getRoleFunctions().size() > 0) {
                                            roleFunList.addAll(epRole.getRoleFunctions());
                                        }
                                        boolean checkIfFunctionsExits = roleFunList.stream().anyMatch(
                                            roleFunction -> roleFunction.getType().equalsIgnoreCase("Approver"));
                                        if (checkIfFunctionsExits) {
                                            finalUserAppRolesList.add(role);
                                            List<RoleFunction> filteredList = roleFunList.stream()
                                                .filter(x -> "Approver".equalsIgnoreCase(x.getType()))
                                                .collect(Collectors.toList());
                                            roleNames = filteredList.stream().map(RoleFunction::getCode)
                                                .collect(Collectors.toList());
                                        } else {
                                            roleNames.add(epRole.getName());
                                        }
                                    }
                                    for (String name : roleNames) {
                                        EcompRole ecompRole = appRolesList.stream()
                                            .filter(x -> name.equals(x.getName())).findAny().orElse(null);
                                        if (ecompRole != null) {
                                            finalUserAppRolesList.add(ecompRole);
                                        }
                                    }
                                }
                                for (String name : roleNames) {
                                    boolean checkIfFunctionsExits = userAppsRolesList.stream().anyMatch(
                                        role -> role.getRoleName().equalsIgnoreCase(name));
                                    if (checkIfFunctionsExits) {
                                        appRolesList.stream().filter(x -> name.equals(x.getName()))
                                            .findAny().ifPresent(setUserRoles::add);
                                    }
                                }
                                userAppRoles = setUserRoles.toArray(new EcompRole[0]);
                            }
                        } else {
                            userAppRoles = applicationsRestClientService.get(EcompRole[].class, appId,
                                String.format("/user/%s/roles", userId));
                        }
                    } catch (HTTPException e) {
                        // Some apps are returning 400 if user is not found.
                        if (e.getResponseCode() == 400) {
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                "getAppRolesForUser caught exception with response code 400; continuing", e);
                        } else {
                            // Other response code, let it come thru.
                            throw e;
                        }
                    }
                    if (userAppRoles == null) {
                        if (EcompPortalUtils.getExternalAppResponseCode() == 400) {
                            EcompPortalUtils.setExternalAppResponseCode(200);
                            String message = String.format(
                                "getAppRolesForUser: App %s, User %, endpoint /user/{userid}/roles returned 400, "
                                    + "assuming user doesn't exist, app is framework SDK based, and things are ok. "
                                    + "Overriding to 200 until framework SDK returns a useful response.",
                                Long.toString(appId), userId);
                            logger.warn(EELFLoggerDelegate.applicationLogger, message);
                        }
                    }

                    HashMap<Long, EcompRole> appRolesActiveMap = hashMapFromEcompRoles(appRoles);
                    ArrayList<EcompRole> activeRoles = new ArrayList<>();
                    if (userAppRoles != null) {
                        for (EcompRole userAppRole : userAppRoles) {
                            if (appRolesActiveMap.containsKey(userAppRole.getId())) {
                                EcompRole role = new EcompRole();
                                role.setId(userAppRole.getId());
                                role.setName(userAppRole.getName());
                                activeRoles.add(role);
                            }
                        }
                    }
                    EcompRole[] userAppRolesActive = activeRoles.toArray(new EcompRole[0]);

                    boolean checkIfUserisRoleAdmin = isRoleAdmin(userId) && !checkIfUserIsApplicationAccAdmin;

                    // If the remote application isn't down we MUST sync user
                    // roles here in case we have this user here!
                    syncUserRoles(Long.toString(userId), appId, userAppRolesActive, extRequestValue, null,
                        checkIfUserisRoleAdmin,
                        appRoles);
                } catch (Exception e) {
                    // TODO: we may need to check if user exists, maybe remote
                    // app is down.
                    String message = String.format(
                        "getAppRolesForUser: user %s does not exist in remote application %s", userId,
                        Long.toString(appId));
                    logger.error(EELFLoggerDelegate.errorLogger, message, e);
                    userAppRoles = new EcompRole[0];
                }
                rolesInAppForUser = fnUserRoleService.constructRolesInAppForUserGet(appRoles, userAppRoles);
            }
        } catch (Exception e) {
            String message = String.format("getAppRolesForUser: failed for User %s, AppId %s", userId,
                Long.toString(appId));
            logger.error(EELFLoggerDelegate.errorLogger, message, e);
        }
        return rolesInAppForUser;
    }

    private void syncAppRoles(Long appId, EcompRole[] appRoles) throws Exception {
        logger.debug(EELFLoggerDelegate.debugLogger, "entering syncAppRoles for appId: " + appId);
        HashMap<Long, EcompRole> newRolesMap = hashMapFromEcompRoles(appRoles);
        try {
            List<FnRole> currentAppRoles = fnRoleService.retrieveAppRolesByAppId(appId);

            List<FnRole> obsoleteRoles = new ArrayList<>();
            for (FnRole oldAppRole : currentAppRoles) {
                if (oldAppRole.getAppRoleId() != null) {
                    EcompRole role;
                    role = newRolesMap.get(oldAppRole.getAppRoleId());
                    if (role != null) {
                        if (!(role.getName() == null || oldAppRole.getRoleName().equals(role.getName()))) {
                            oldAppRole.setRoleName(role.getName());
                        }
                        oldAppRole.setActiveYn(true);
                        newRolesMap.remove(oldAppRole.getAppRoleId());
                    } else {
                        obsoleteRoles.add(oldAppRole);
                    }
                } else {
                    obsoleteRoles.add(oldAppRole);
                }
            }
            Collection<EcompRole> newRolesToAdd = newRolesMap.values();
            if (obsoleteRoles.size() > 0) {
                logger.debug(EELFLoggerDelegate.debugLogger, "syncAppRoles: we have obsolete roles to delete");
                for (FnRole role : obsoleteRoles) {
                    logger.debug(EELFLoggerDelegate.debugLogger, "syncAppRoles: obsolete role: " + role.toString());
                    Long roleId = role.getId();
                    List<FnUserRole> userRoles = fnUserRoleService.getUserRolesForRoleIdAndAppId(roleId, appId);

                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "syncAppRoles: number of userRoles to delete: " + userRoles.size());
                    for (FnUserRole userRole : userRoles) {
                        logger.debug(EELFLoggerDelegate.debugLogger,
                            "syncAppRoles: about to delete userRole: " + userRole.toString());
                        fnUserRoleService.deleteById(userRole.getId());
                        logger.debug(EELFLoggerDelegate.debugLogger,
                            "syncAppRoles: finished deleting userRole: " + userRole.toString());
                    }
                    List<FnMenuFunctionalRoles> funcMenuRoles = fnMenuFunctionalRolesService.retrieveByroleId(roleId);
                    int numMenuRoles = funcMenuRoles.size();
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "syncAppRoles: number of funcMenuRoles for roleId: " + roleId + ": " + numMenuRoles);
                    for (FnMenuFunctionalRoles funcMenuRole : funcMenuRoles) {
                        Long menuId = funcMenuRole.getMenuId().getMenuId();
                        // If this is the only role for this menu item, then the
                        // app and roles will be gone,
                        // so must null out the url too, to be consistent
                        List<FnMenuFunctionalRoles> funcMenuRoles2 = fnMenuFunctionalRolesService
                            .retrieveByMenuId(menuId);
                        int numMenuRoles2 = funcMenuRoles2.size();
                        logger.debug(EELFLoggerDelegate.debugLogger,
                            "syncAppRoles: number of funcMenuRoles for menuId: " + menuId + ": " + numMenuRoles2);
                        fnMenuFunctionalRolesService.delete(funcMenuRole);

                        if (numMenuRoles2 == 1) {
                            // If this is the only role for this menu item, then
                            // the app and roles will be gone,
                            // so must null out the url too, to be consistent
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                "syncAppRoles: There is exactly 1 menu item for this role, so emptying the url");
                            List<FnMenuFunctional> funcMenuItems = fnMenuFunctionalService.retrieveByMenuId(menuId);
                            if (funcMenuItems.size() > 0) {
                                logger.debug(EELFLoggerDelegate.debugLogger, "got the menu item");
                                FnMenuFunctional funcMenuItem = funcMenuItems.get(0);
                                funcMenuItem.setUrl("");
                            }
                        }
                    }
                    boolean isPortalRequest = true;
                    deleteRoleDependencyRecords(roleId, appId, isPortalRequest);
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "syncAppRoles: about to delete the role: " + role.toString());
                    fnRoleService.delete(role);
                    logger.debug(EELFLoggerDelegate.debugLogger, "syncAppRoles: deleted the role");
                }
            }
            for (EcompRole role : newRolesToAdd) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "syncAppRoles: about to add missing role: " + role.toString());
                FnRole newRole = new FnRole();
                // Attention! All roles from remote application supposed to be
                // active!
                newRole.setActiveYn(true);
                newRole.setRoleName(role.getName());
                newRole.setAppId(appId);
                newRole.setAppRoleId(role.getId());
                fnRoleService.saveOne(newRole);
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "syncAppRoles: about to commit the transaction");
            logger.debug(EELFLoggerDelegate.debugLogger, "syncAppRoles: committed the transaction");
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "syncAppRoles failed", e);
            EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
            throw new Exception(e);
        }
    }

    private void deleteRoleDependencyRecords(Long roleId, Long appId, boolean isPortalRequest)
        throws Exception {
        Session localSession = entityManager.unwrap(Session.class);
        try {
            String sql;
            Query query;
            // It should delete only when it portal's roleId
            if (appId.equals(PortalConstants.PORTAL_APP_ID)) {
                // Delete from fn_role_function
                sql = "DELETE FROM fn_role_function WHERE role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = localSession.createSQLQuery(sql);
                query.executeUpdate();
                // Delete from fn_role_composite
                sql = "DELETE FROM fn_role_composite WHERE parent_role_id=" + roleId + " OR child_role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = localSession.createSQLQuery(sql);
                query.executeUpdate();
            }
            // Delete from ep_app_role_function
            sql = "DELETE FROM ep_app_role_function WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = localSession.createSQLQuery(sql);
            query.executeUpdate();
            // Delete from ep_role_notification
            sql = "DELETE FROM ep_role_notification WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = localSession.createSQLQuery(sql);
            query.executeUpdate();
            // Delete from fn_user_pseudo_role
            sql = "DELETE FROM fn_user_pseudo_role WHERE pseudo_role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = localSession.createSQLQuery(sql);
            query.executeUpdate();
            // Delete form EP_WIDGET_CATALOG_ROLE
            sql = "DELETE FROM EP_WIDGET_CATALOG_ROLE WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = localSession.createSQLQuery(sql);
            query.executeUpdate();
            // Delete form EP_WIDGET_CATALOG_ROLE
            sql = "DELETE FROM ep_user_roles_request_det WHERE requested_role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = localSession.createSQLQuery(sql);
            query.executeUpdate();
            if (!isPortalRequest) {
                // Delete form fn_menu_functional_roles
                sql = "DELETE FROM fn_menu_functional_roles WHERE role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = localSession.createSQLQuery(sql);
                query.executeUpdate();
            }
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleDependeciesRecord: failed ", e);
            throw new DeleteDomainObjectFailedException("delete Failed" + e.getMessage());
        }
    }

    private void checkIfRoleInactive(FnRole epRole) throws Exception {
        if (!epRole.getActiveYn()) {
            throw new Exception(epRole.getRoleName() + " role is unavailable");
        }
    }

    public boolean setAppsWithAdminRoleStateForUser(AppsListWithAdminRole newAppsListWithAdminRoles) {
        boolean result = false;
        // No changes if no new roles list or no userId.
        if (!org.apache.cxf.common.util.StringUtils.isEmpty(newAppsListWithAdminRoles.getOrgUserId())
            && newAppsListWithAdminRoles.getAppsRoles() != null) {
            synchronized (syncRests) {
                List<FnApp> apps = fnAppService.getAppsFullList();
                HashMap<Long, FnApp> enabledApps = new HashMap<>();
                for (FnApp app : apps) {
                    enabledApps.put(app.getId(), app);
                }
                List<AppNameIdIsAdmin> newAppsWhereUserIsAdmin = new ArrayList<>();
                for (AppNameIdIsAdmin adminRole : newAppsListWithAdminRoles.getAppsRoles()) {
                    // user Admin role may be added only for enabled apps
                    if (adminRole.getIsAdmin() && enabledApps.containsKey(adminRole.getId())) {
                        newAppsWhereUserIsAdmin.add(adminRole);
                    }
                }
                FnUser user = null;
                boolean createNewUser = false;
                String orgUserId = newAppsListWithAdminRoles.getOrgUserId().trim();
                List<FnUser> localUserList = fnUserService.getUserWithOrgUserId(orgUserId);
                List<FnUserRole> oldAppsWhereUserIsAdmin = new ArrayList<>();
                if (localUserList.size() > 0) {
                    FnUser tmpUser = localUserList.get(0);
                    oldAppsWhereUserIsAdmin = fnUserRoleService
                        .retrieveByUserIdAndRoleId(tmpUser.getId(), ACCOUNT_ADMIN_ROLE_ID);
                    if (oldAppsWhereUserIsAdmin.size() > 0 || newAppsWhereUserIsAdmin.size() > 0) {
                        user = tmpUser;
                    }
                } else if (newAppsWhereUserIsAdmin.size() > 0) {
                    // we create new user only if he has Admin Role for any App
                    createNewUser = true;
                }
                result = isResult(result, enabledApps, newAppsWhereUserIsAdmin, user, createNewUser, orgUserId,
                    oldAppsWhereUserIsAdmin);
            }
        }

        return result;
    }

    @Transactional
    public boolean isResult(boolean result, HashMap<Long, FnApp> enabledApps,
        List<AppNameIdIsAdmin> newAppsWhereUserIsAdmin, FnUser user, boolean createNewUser, String orgUserId,
        List<FnUserRole> oldAppsWhereUserIsAdmin) {
        if (user != null || createNewUser) {
            if (createNewUser) {
                user = fnUserService.getUserWithOrgUserId(orgUserId).stream().findFirst().get();
                if (user != null) {
                    user.setActiveYn(true);
                }
            }
            for (FnUserRole oldUserApp : oldAppsWhereUserIsAdmin) {
                // user Admin role may be deleted only for enabled
                // apps
                if (enabledApps.containsKey(oldUserApp.getFnAppId())) {
                    fnUserRoleService.saveOne(oldUserApp);
                }
            }
            for (AppNameIdIsAdmin appNameIdIsAdmin : newAppsWhereUserIsAdmin) {
                FnApp app = fnAppService.getById(appNameIdIsAdmin.getId());
                FnRole role = fnRoleService.getById(ACCOUNT_ADMIN_ROLE_ID);
                FnUserRole newUserApp = new FnUserRole();
                newUserApp.setUserId(user);
                newUserApp.setFnAppId(app);
                newUserApp.setRoleId(role);
                fnUserRoleService.saveOne(newUserApp);
            }
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                addAdminRoleInExternalSystem(user, newAppsWhereUserIsAdmin);
                result = true;
            }
        }
        return result;
    }

    public boolean addAdminRoleInExternalSystem(FnUser user, List<AppNameIdIsAdmin> newAppsWhereUserIsAdmin) {
        boolean result = false;
        try {
            // Reset All admin role for centralized applications
            List<FnApp> appList = fnAppService.getCentralizedApps();
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            for (FnApp app : appList) {
                String name = "";
                if (EPCommonSystemProperties
                    .containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
                    name = user.getOrgUserId() + SystemProperties
                        .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
                }
                String extRole = app.getAuthNamespace() + "." + PortalConstants.ADMIN_ROLE.replaceAll(" ", "_");
                HttpEntity<String> entity = new HttpEntity<>(headers);
                logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
                try {
                    ResponseEntity<String> getResponse = template
                        .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                            + "roles/" + extRole, HttpMethod.GET, entity, String.class);

                    if (getResponse.getBody().equals("{}")) {
                        String addDesc = "{\"name\":\"" + extRole + "\"}";
                        HttpEntity<String> roleEntity = new HttpEntity<>(addDesc, headers);
                        template.exchange(
                            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                                + "role",
                            HttpMethod.POST, roleEntity, String.class);
                    } else {
                        try {
                            HttpEntity<String> deleteUserRole = new HttpEntity<>(headers);
                            template.exchange(
                                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                                    + "userRole/" + name + "/" + extRole,
                                HttpMethod.DELETE, deleteUserRole, String.class);
                        } catch (Exception e) {
                            logger.error(EELFLoggerDelegate.errorLogger,
                                " Role not found for this user may be it gets deleted before", e);
                        }
                    }
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
                        logger.debug(EELFLoggerDelegate.debugLogger, "Application Not found for app {}",
                            app.getAuthNamespace(), e.getMessage());
                    } else {
                        logger.error(EELFLoggerDelegate.errorLogger, "Application Not found for app {}",
                            app.getAuthNamespace(), e);
                    }
                }
            }
            for (AppNameIdIsAdmin appNameIdIsAdmin : newAppsWhereUserIsAdmin) {
                FnApp app = fnAppService.getById(appNameIdIsAdmin.getId());
                try {
                    if (app.getAuthCentral()) {
                        String extRole = app.getAuthNamespace() + "." + PortalConstants.ADMIN_ROLE.replaceAll(" ", "_");
                        HttpEntity<String> entity = new HttpEntity<>(headers);
                        String name = "";
                        if (EPCommonSystemProperties
                            .containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
                            name = user.getOrgUserId() + SystemProperties
                                .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
                        }
                        logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to External Access system");
                        ResponseEntity<String> getUserRolesResponse = template.exchange(
                            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                                + "userRoles/user/" + name,
                            HttpMethod.GET, entity, String.class);
                        logger.debug(EELFLoggerDelegate.debugLogger, "Connected to External Access system");
                        if (!getUserRolesResponse.getBody().equals("{}")) {
                            JSONObject jsonObj = new JSONObject(getUserRolesResponse.getBody());
                            JSONArray extRoles = jsonObj.getJSONArray("userRole");
                            final Map<String, JSONObject> extUserRoles = new HashMap<>();
                            for (int i = 0; i < extRoles.length(); i++) {
                                String userRole = extRoles.getJSONObject(i).getString("role");
                                if (userRole.startsWith(app.getAuthNamespace() + ".")
                                    && !userRole.equals(app.getAuthNamespace() + ".admin")
                                    && !userRole.equals(app.getAuthNamespace() + ".owner")) {

                                    extUserRoles.put(userRole, extRoles.getJSONObject(i));
                                }
                            }
                            if (!extUserRoles.containsKey(extRole)) {
                                // Assign with new apps user admin
                                try {
                                    ExternalAccessUser extUser = new ExternalAccessUser(name, extRole);
                                    // Assign user role for an application in external access system
                                    ObjectMapper addUserRoleMapper = new ObjectMapper();
                                    String userRole = addUserRoleMapper.writeValueAsString(extUser);
                                    HttpEntity<String> addUserRole = new HttpEntity<>(userRole, headers);
                                    template.exchange(
                                        SystemProperties.getProperty(
                                            EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole",
                                        HttpMethod.POST, addUserRole, String.class);
                                } catch (Exception e) {
                                    logger.error(EELFLoggerDelegate.errorLogger, "Failed to add user admin role", e);
                                }

                            }
                        }
                    }
                    result = true;
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
                        logger.debug(EELFLoggerDelegate.errorLogger,
                            "Application name space not found in External system for app {} due to bad rquest name space ",
                            app.getAuthNamespace(), e.getMessage());
                    } else {
                        logger.error(EELFLoggerDelegate.errorLogger, "Failed to assign admin role for application {}",
                            app.getAuthNamespace(), e);
                        result = false;
                    }
                }
            }
        } catch (Exception e) {
            result = false;
            logger.error(EELFLoggerDelegate.errorLogger, "Failed to assign admin roles operation", e);
        }
        return result;
    }

    public Set<String> getAllAppsFunctionsOfUser(String OrgUserId) throws RoleFunctionException {
        final String getAllAppsFunctionsOfUser =
            "select distinct ep.function_cd from fn_user_role fu, ep_app_role_function ep, ep_app_function ea, fn_app fa , fn_role fr\n"
                + "         where fu.role_id = ep.role_id \n"
                + "         and fu.app_id = ep.app_id\n"
                + "         and fu.user_id =:userId\n"
                + "         and ea.function_cd = ep.function_cd\n"
                + "            and ((fu.app_id = fa.app_id  and fa.enabled = 'Y' ) or (fa.app_id = 1))\n"
                + "            and fr.role_id = fu.role_id and fr.active_yn='Y' \n"
                + "          union\n"
                + "            select distinct app_r_f.function_cd from ep_app_role_function app_r_f, ep_app_function a_f\n"
                + "         where role_id = 999\n"
                + "         and app_r_f.function_cd = a_f.function_cd\n"
                + "         and exists\n"
                + "         (\n"
                + "         select fa.app_id from fn_user fu, fn_user_role ur, fn_app fa where fu.user_id =:userId and fu.user_id = ur.user_id\n"
                + "         and ur.role_id = 999 and ur.app_id = fa.app_id and fa.enabled = 'Y'\n"
                + "         )";
        List getRoleFuncListOfPortal = entityManager.createNativeQuery(getAllAppsFunctionsOfUser)
            .setParameter("userId", OrgUserId).getResultList();
        Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfPortal);
        Set<String> roleFunSet = getRoleFuncListOfPortalSet.stream().filter(x -> x.contains("|"))
            .collect(Collectors.toSet());
        if (!roleFunSet.isEmpty()) {
            for (String roleFunction : roleFunSet) {
                String roleFun = EcompPortalUtils.getFunctionCode(roleFunction);
                getRoleFuncListOfPortalSet.remove(roleFunction);
                getRoleFuncListOfPortalSet.add(roleFun);
            }
        }

        Set<String> finalRoleFunctionSet = new HashSet<>();
        for (String roleFn : getRoleFuncListOfPortalSet) {
            finalRoleFunctionSet.add(EPUserUtils.decodeFunctionCode(roleFn));
        }

        return finalRoleFunctionSet;
    }

    public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(String orgUserId) {
        AppsListWithAdminRole appsListWithAdminRole = null;

        try {
            List<FnUser> userList = fnUserService.getUserWithOrgUserId(orgUserId);
            HashMap<Long, Long> appsUserAdmin = new HashMap<>();
            if (userList != null && userList.size() > 0) {
                FnUser user = userList.get(0);
                List<FnUserRole> userAppList = new ArrayList<>();
                try {
                    userAppList = fnUserRoleService.retrieveByUserIdAndRoleId(user.getId(), ACCOUNT_ADMIN_ROLE_ID);
                } catch (Exception e) {
                    logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 1 failed", e);
                    EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
                }
                for (FnUserRole userApp : userAppList) {
                    appsUserAdmin.put(userApp.getFnAppId().getId(), userApp.getUserId().getId());
                }
            }

            appsListWithAdminRole = new AppsListWithAdminRole();
            appsListWithAdminRole.setOrgUserId(orgUserId);
            List<FnApp> appsList = new ArrayList<>();
            try {
                appsList = fnAppService.findAll();
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 2 failed", e);
                EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
            }
            for (FnApp app : appsList) {
                AppNameIdIsAdmin appNameIdIsAdmin = new AppNameIdIsAdmin();
                appNameIdIsAdmin.setId(app.getId());
                appNameIdIsAdmin.setAppName(app.getAppName());
                appNameIdIsAdmin.setIsAdmin(appsUserAdmin.containsKey(app.getId()));
                appNameIdIsAdmin.setRestrictedApp(app.isRestrictedApp());
                appsListWithAdminRole.getAppsRoles().add(appNameIdIsAdmin);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getAppsWithAdminRoleStateForUser 3 failed", e);
        }
        return appsListWithAdminRole;
    }

    public ExternalRequestFieldsValidator setExternalRequestUserAppRole(ExternalSystemUser newAppRolesForUser,
        String reqType) {
        boolean result = false;
        boolean externalSystemRequest = true;
        List<FnUser> userInfo = null;
        FnUser user = null;
        List<EpUserRolesRequest> epRequestId = null;
        String orgUserId = "";
        String updateStatus = "";
        String reqMessage = "";
        FnApp app = null;
        if (newAppRolesForUser != null && newAppRolesForUser.getLoginId() != null) {
            orgUserId = newAppRolesForUser.getLoginId().trim();
        }
        String appName = newAppRolesForUser.getApplicationName();
        String logMessage = ("DELETE").equals(reqType) ? "Deleting" : "Assigning/Updating";
        if (orgUserId.length() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            int epRequestIdSize = 0;
            try {
                app = fnAppService.getAppDetail(appName);
                userInfo = checkIfUserExists(orgUserId);
                reqMessage = "Updated Successfully";
                if (!reqType.equals("DELETE") && (userInfo.isEmpty())) {
                    reqMessage = validateNewUser(orgUserId, app);
                }
                if (!userInfo.isEmpty()) {
                    validateExternalRequestFields(app);
                    user = userInfo.get(0);
                    epRequestId = epUserRolesRequestService.userAppRolesRequestList(user.getId(), app.getId());
                    epRequestIdSize = epRequestId.size();
                }
                if (!app.getId().equals(PortalConstants.PORTAL_APP_ID) && !app.getAuthCentral()) {
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "setExternalRequestUserAppRole: Starting GET roles for app {}", app.getId());
                    EcompRole[] appRoles = applicationsRestClientService.get(EcompRole[].class, app.getId(), "/roles");
                    logger.debug(EELFLoggerDelegate.debugLogger,
                        "setExternalRequestUserAppRole: Finshed GET roles for app {} and payload {}", app.getId(),
                        appRoles);
                    if (appRoles.length > 0) {
                        syncAppRoles(app.getId(), appRoles);
                    }
                }
                List<RoleInAppForUser> roleInAppForUserList = roleInAppForUserList(newAppRolesForUser.getRoles(),
                    app.getId(), app.getMlAppName());
                List<EcompUserAppRoles> userRoleList = null;
                if (!userInfo.isEmpty()) {
                    userRoleList = ecompUserAppRolesService.getUserAppExistingRoles(app.getId(), user.getId());
                }
                // Check if list contains just account admin role
                boolean checkIfAdminRoleExists = false;
                if (reqType.equals("DELETE") && userRoleList != null) {
                    checkIfAdminRoleExists = userRoleList.stream()
                        .anyMatch(userRole -> userRole.getRoleId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID));
                } else {
                    checkIfAdminRoleExists = roleInAppForUserList.stream()
                        .anyMatch(roleList -> roleList.getRoleId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID));
                }
                if (app.getAuthCentral()) {
                    try {
                        if (!(app.getId().equals(PortalConstants.PORTAL_APP_ID) && reqType.equals("DELETE"))
                            && ((checkIfAdminRoleExists && roleInAppForUserList.size() > 1)
                            || (!checkIfAdminRoleExists && roleInAppForUserList.size() >= 1))) {
                            List<RoleInAppForUser> remoteUserRoles = new ArrayList<>(roleInAppForUserList);
                            remoteUserRoles.removeIf(role -> {
                                return (role.getRoleId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID));
                            });
                            String orgUserIdNewOrExist = (!userInfo.isEmpty()) ? user.getOrgUserId() : orgUserId;
                            pushRemoteUser(remoteUserRoles, orgUserIdNewOrExist, app, mapper,
                                applicationsRestClientService, true);
                        }
                    } catch (Exception e) {
                        reqMessage = e.getMessage();
                        logger.error(EELFLoggerDelegate.errorLogger,
                            "setExternalRequestUserAppRole: Failed to added remote user", e);
                        throw new Exception(reqMessage);
                    }
                    Set<EcompRole> userRolesInLocalApp = postUsersRolesToLocalApp(roleInAppForUserList);
                    RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(orgUserId, app.getId(),
                        userRolesInLocalApp);
                    List<RoleInAppForUser> roleAppUserList = rolesInAppForUser.getRoles();
                    Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
                    if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                        updateUserRolesInExternalSystem(app, rolesInAppForUser.getOrgUserId(), roleAppUserList,
                            externalSystemRequest, false, rolesGotDeletedByApprover, false);
                    }
                    logger.info(EELFLoggerDelegate.debugLogger,
                        "setExternalRequestUserAppRole: {} user app roles: for app {}, user {}", logMessage,
                        newAppRolesForUser.getApplicationName(), newAppRolesForUser.getLoginId());
                    result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser, externalSystemRequest, reqType,
                        false, rolesGotDeletedByApprover, false);
                } else if (!app.getAuthCentral() && app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                    Set<EcompRole> userRolesInLocalApp = postUsersRolesToLocalApp(roleInAppForUserList);
                    RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(orgUserId, app.getId(),
                        userRolesInLocalApp);
                    Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();

                    result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser, externalSystemRequest, reqType,
                        false, rolesGotDeletedByApprover, false);
                } else {
                    if (!((roleInAppForUserList.size() == 1 || reqType.equals("DELETE")) && checkIfAdminRoleExists)) {
                        FnUser remoteAppUser = null;
                        remoteAppUser = checkIfRemoteUserExits(orgUserId, app, applicationsRestClientService);
                        if (remoteAppUser == null) {
                            addRemoteUser(roleInAppForUserList, orgUserId, app, mapper,
                                applicationsRestClientService);
                            reqMessage = "Saved Successfully";
                        }
                        Set<EcompRole> userRolesInRemoteApp = postUsersRolesToRemoteApp(roleInAppForUserList, mapper,
                            applicationsRestClientService, app.getId(), orgUserId);
                        RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(orgUserId, app.getId(),
                            userRolesInRemoteApp);
                        logger.info(EELFLoggerDelegate.debugLogger,
                            "setExternalRequestUserAppRole: {} user app roles: for app {}, user {}", logMessage,
                            newAppRolesForUser.getApplicationName(), newAppRolesForUser.getLoginId());
                        Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
                        result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser, externalSystemRequest,
                            reqType, false, rolesGotDeletedByApprover, false);
                    } else {
                        if (!(reqType.equals("DELETE")) && userInfo.isEmpty()) {
                            reqMessage = "Saved Successfully";
                        }
                        Set<EcompRole> userRolesInRemoteApp = constructUsersEcompRoles(roleInAppForUserList);
                        RolesInAppForUser rolesInAppForUser = constructRolesInAppForUserUpdate(orgUserId, app.getId(),
                            userRolesInRemoteApp);
                        logger.info(EELFLoggerDelegate.debugLogger,
                            "setExternalRequestUserAppRole: {} user app roles: for app {}, user {}",
                            logMessage, newAppRolesForUser.getApplicationName(), newAppRolesForUser.getLoginId());
                        Set<EcompRole> rolesGotDeletedByApprover = new TreeSet<>();
                        result = applyChangesInUserRolesForAppToEcompDB(rolesInAppForUser, externalSystemRequest,
                            reqType, false, rolesGotDeletedByApprover, false);
                    }
                    if (!result) {
                        reqMessage = "Failed to save the user app role(s)";
                    }
                    if (epRequestIdSize > 0 && !userInfo.isEmpty()) {
                        updateStatus = "C";
                        applyChangesToAppRolesRequest(user.getId(), updateStatus, epRequestId.get(0));
                    }
                }
            } catch (Exception e) {
                String message = String.format(
                    "setExternalRequestUserAppRole: Failed to create user or update user roles for User %s, AppId %s",
                    orgUserId, appName);
                logger.error(EELFLoggerDelegate.errorLogger, message, e);
                result = false;
                reqMessage = e.getMessage();
                if (epRequestIdSize > 0 && userInfo != null && !userInfo.isEmpty()) {
                    updateStatus = "F";
                    applyChangesToAppRolesRequest(user.getId(),
                        updateStatus, epRequestId.get(0));
                }
            }
        }
        return new ExternalRequestFieldsValidator(result, reqMessage);
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

    private List<RoleInAppForUser> roleInAppForUserList(List<ExternalSystemRoleApproval> roleInAppForUserList,
        Long appId, String appName) throws Exception {
        List<RoleInAppForUser> existingUserRoles = new ArrayList<>();
        List<FnRole> existingAppRole;
        for (ExternalSystemRoleApproval roleInAppForUser : roleInAppForUserList) {
            RoleInAppForUser ecompRole = new RoleInAppForUser();
            existingAppRole = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(roleInAppForUser.getRoleName(), appId);
            if (existingAppRole.isEmpty()) {
                logger.error(EELFLoggerDelegate.errorLogger, "roleInAppForUserList failed for the roles {}",
                    roleInAppForUserList);
                throw new Exception("'" + roleInAppForUser.getRoleName() + "'" + " role does not exist for " + appName
                    + " application");
            }
            if (!existingAppRole.get(0).getActiveYn()) {
                logger.error(EELFLoggerDelegate.errorLogger, "roleInAppForUserList failed for the roles {}",
                    roleInAppForUserList);
                throw new Exception(
                    roleInAppForUser.getRoleName() + " role is unavailable for " + appName + " application");
            } else {

                List<FnRole> roleInfo = externalAccessRolesService
                    .getPortalAppRoleInfo(PortalConstants.ACCOUNT_ADMIN_ROLE_ID);
                FnRole adminRole = new FnRole();
                if (roleInfo.size() > 0) {
                    adminRole = roleInfo.get(0);
                    logger.debug(EELFLoggerDelegate.debugLogger, "Admin RoleName form DB: " + adminRole.getRoleName());
                }
                ecompRole.setRoleId(
                    (appId == 1 || roleInAppForUser.getRoleName().equals(adminRole.getRoleName())) ? existingAppRole
                        .get(0)
                        .getId() : existingAppRole.get(0).getAppRoleId());
                ecompRole.setRoleName(roleInAppForUser.getRoleName());
                ecompRole.setIsApplied(true);
                existingUserRoles.add(ecompRole);
            }
        }
        return existingUserRoles;
    }

    private void validateExternalRequestFields(FnApp app) throws Exception {
        if (app == null) {
            throw new Exception("Application does not exist");
        } else if (!app.getEnabled() && !app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
            throw new Exception(app.getMlAppName() + " application is unavailable");
        }
    }

    private String validateNewUser(String orgUserId, FnApp app) throws Exception {
        FnUser epUser = fnUserService.getUserWithOrgUserId(orgUserId).get(0);
        if (epUser == null) {
            throw new Exception("User does not exist");
        } else if (!epUser.getOrgUserId().equals(orgUserId)) {
            throw new Exception("User does not exist");
        } else if (app == null) {
            throw new Exception("Application does not exist");
        }
        return "Saved Successfully";
    }

    private void applyChangesToAppRolesRequest(final Long userId, final String updateStatus,
        final EpUserRolesRequest epUserAppRolesRequest) {
        try {
            epUserAppRolesRequest.setUpdatedDate(LocalDateTime.now());
            epUserAppRolesRequest.setRequestStatus(updateStatus);
            epUserAppRolesRequest.setUserId(fnUserService.getUser(userId).get());
            epUserRolesRequestService.saveOne(epUserAppRolesRequest);
            List<EpUserRolesRequestDet> epUserAppRolessDetailList = epUserRolesRequestDetService
                .appRolesRequestDetailList(epUserAppRolesRequest.getReqId());
            if (epUserAppRolessDetailList.size() > 0) {
                for (EpUserRolesRequestDet epRequestUpdateData : epUserAppRolessDetailList) {
                    epRequestUpdateData.setRequestType(updateStatus);
                    epRequestUpdateData.setReqId(epUserAppRolesRequest);
                    epRequestUpdateData.setReqId(epUserAppRolesRequest);
                    epUserRolesRequestDetService.saveOne(epRequestUpdateData);
                }
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "The request is set to complete");
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "applyChangesToAppRolesRequest failed", e);
        }
    }
}
