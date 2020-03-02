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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.cxf.transport.http.HTTPException;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.ecomp.EPUserAppCatalogRoles;
import org.onap.portal.domain.dto.ecomp.EcompAuditLog;
import org.onap.portal.domain.dto.ecomp.ExternalSystemAccess;
import org.onap.portal.domain.dto.transport.AppNameIdIsAdmin;
import org.onap.portal.domain.dto.transport.AppWithRolesForUser;
import org.onap.portal.domain.dto.transport.AppsListWithAdminRole;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.RoleInAppForUser;
import org.onap.portal.domain.dto.transport.UserApplicationRoles;
import org.onap.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.AdminRolesService;
import org.onap.portal.service.ApplicationsRestClientService;
import org.onap.portal.service.SearchService;
import org.onap.portal.service.userRole.FnUserRoleService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.service.AuditServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class UserRolesController {

    private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRolesController.class);

    private final SearchService searchService;
    private final FnUserService fnUserService;
    private final FnUserRoleService fnUserRoleService;
    private final AdminRolesService adminRolesService;
    private final ApplicationsRestClientService applicationsRestClientService;
    private final AuditServiceImpl auditService = new AuditServiceImpl();

    private static final String FAILURE = "failure";

    @Autowired
    public UserRolesController(SearchService searchService, final FnUserService fnUserService,
        FnUserRoleService fnUserRoleService,
        final AdminRolesService adminRolesService,
        ApplicationsRestClientService applicationsRestClientService) {
        this.searchService = searchService;
        this.fnUserService = fnUserService;
        this.fnUserRoleService = fnUserRoleService;
        this.adminRolesService = adminRolesService;
        this.applicationsRestClientService = applicationsRestClientService;
    }


    @RequestMapping(value = {"/portalApi/queryUsers"}, method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasRole('System_Administrator') and hasRole('Account_Administrator')")
    public String getPhoneBookSearchResult(Principal principal, @RequestParam("search") String searchString,
        HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());

        String searchResult = null;
        if (!adminRolesService.isSuperAdmin(user.getLoginId()) && !adminRolesService
            .isAccountAdmin(user.getId(), user.getOrgUserId(), user.getUserApps())
            && !adminRolesService.isRoleAdmin(user.getId())) {
            EcompPortalUtils.setBadPermissions(user, response, "getPhoneBookSearchResult");
        } else {
            searchString = searchString.trim();
            if (searchString.length() > 2) {
                searchResult = searchService.searchUsersInPhoneBook(searchString);
            } else {
                logger.info(EELFLoggerDelegate.errorLogger,
                    "getPhoneBookSearchResult - too short search string: " + searchString);
            }
        }
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/queryUsers", "result =", searchResult);

        return searchResult;
    }


    @RequestMapping(value = {"/portalApi/adminAppsRoles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(Principal principal,
        @RequestParam("user") String orgUserId, HttpServletResponse response) {

        FnUser user = fnUserService.loadUserByUsername(principal.getName());

        AppsListWithAdminRole result = null;
        if (!adminRolesService.isSuperAdmin(user.getLoginId())) {
            EcompPortalUtils.setBadPermissions(user, response, "getAppsWithAdminRoleStateForUser");
        } else {
            if (EcompPortalUtils.legitimateUserId(orgUserId)) {
                result = adminRolesService.getAppsWithAdminRoleStateForUser(orgUserId);
            } else {
                logger.info(EELFLoggerDelegate.errorLogger,
                    "getAppsWithAdminRoleStateForUser - parms error, no Organization User ID");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        StringBuilder adminAppRoles = new StringBuilder();
        if (result != null) {
            if (!result.getAppsRoles().isEmpty()) {
                adminAppRoles.append("User '").append(result.getOrgUserId())
                    .append("' has admin role to the apps = {");
                for (AppNameIdIsAdmin adminAppRole : result.getAppsRoles()) {
                    if (adminAppRole.getIsAdmin()) {
                        adminAppRoles.append(adminAppRole.getAppName()).append(", ");
                    }
                }
                adminAppRoles.append("}.");
            } else {
                adminAppRoles.append("User '").append(result.getOrgUserId())
                    .append("' has no Apps with Admin Role.");
            }
        } else {
            logger.error(EELFLoggerDelegate.errorLogger,
                "putAppWithUserRoleStateForUser: getAppsWithAdminRoleStateForUser result is null");
        }

        logger.info(EELFLoggerDelegate.errorLogger, adminAppRoles.toString());

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/adminAppsRoles", "get result =", result);

        return result;
    }

    @RequestMapping(value = {"/portalApi/adminAppsRoles"}, method = {
        RequestMethod.PUT}, produces = "application/json")
    public FieldsValidator putAppsWithAdminRoleStateForUser(Principal principal,
        @RequestBody AppsListWithAdminRole newAppsListWithAdminRoles, HttpServletResponse response) {
        // newAppsListWithAdminRoles.appsRoles
        FieldsValidator fieldsValidator = new FieldsValidator();
        StringBuilder newAppRoles = new StringBuilder();
        if (newAppsListWithAdminRoles != null) {
            if (!newAppsListWithAdminRoles.getAppsRoles().isEmpty()) {
                newAppRoles.append("User '").append(newAppsListWithAdminRoles.getOrgUserId())
                    .append("' has admin role to the apps = { ");
                for (AppNameIdIsAdmin adminAppRole : newAppsListWithAdminRoles.getAppsRoles()) {
                    if (adminAppRole.getIsAdmin()) {
                        newAppRoles.append(adminAppRole.getAppName()).append(" ,");
                    }
                }
                newAppRoles.deleteCharAt(newAppRoles.length() - 1);
                newAppRoles.append("}.");
            } else {
                newAppRoles.append("User '").append(newAppsListWithAdminRoles.getOrgUserId())
                    .append("' has no Apps with Admin Role.");
            }
        } else {
            logger.error(EELFLoggerDelegate.errorLogger,
                "putAppWithUserRoleStateForUser: putAppsWithAdminRoleStateForUser result is null");
            fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        logger.info(EELFLoggerDelegate.errorLogger, newAppRoles.toString());

        FnUser user = fnUserService.loadUserByUsername(principal.getName());

        boolean changesApplied = false;

        if (!adminRolesService.isSuperAdmin(user.getLoginId())) {
            EcompPortalUtils.setBadPermissions(user, response, "putAppsWithAdminRoleStateForUser");
        } else {
            changesApplied = adminRolesService.setAppsWithAdminRoleStateForUser(newAppsListWithAdminRoles);
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(user.getId());
            auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN);
            if (newAppsListWithAdminRoles != null) {
                auditLog.setAffectedRecordId(newAppsListWithAdminRoles.getOrgUserId());
            }
            auditLog.setComments(
                EcompPortalUtils
                    .truncateString(newAppRoles.toString(), PortalConstants.AUDIT_LOG_COMMENT_SIZE));
            auditService.logActivity(auditLog, null);

            MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
                EPEELFLoggerAdvice.getCurrentDateTimeUTC());
            MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
                EPEELFLoggerAdvice.getCurrentDateTimeUTC());
            EcompPortalUtils.calculateDateTimeDifferenceForLog(
                MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
            if (newAppsListWithAdminRoles != null) {
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage(
                        "UserRolesController.putAppsWithAdminRoleStateForUser",
                        EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN, user.getOrgUserId(),
                        newAppsListWithAdminRoles.getOrgUserId(), newAppRoles.toString()));
            }
            MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
            MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
            MDC.remove(SystemProperties.MDC_TIMER);
        }
        EcompPortalUtils
            .logAndSerializeObject(logger, "/portalApi/adminAppsRoles", "put result =", changesApplied);

        return fieldsValidator;
    }

    @RequestMapping(value = {"/portalApi/userAppRoles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public List<RoleInAppForUser> getAppRolesForUser(Principal principal,
        @RequestParam("user") String orgUserId,
        @RequestParam("app") Long appid, @RequestParam("externalRequest") Boolean extRequestValue,
        @RequestParam("isSystemUser") Boolean isSystemUser,
        HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        List<RoleInAppForUser> result = null;
        String feErrorString = "";
        if (!adminRolesService.isAccountAdmin(user.getId(), user.getOrgUserId(), user.getUserApps())
            && !adminRolesService.isRoleAdmin(user.getId())) {
            logger.debug(EELFLoggerDelegate.debugLogger,
                "getAppRolesForUser: Accountadminpermissioncheck {}, RoleAdmincheck {}",
                adminRolesService.isAccountAdmin(user.getId(), user.getOrgUserId(), user.getUserApps()),
                adminRolesService.isRoleAdmin(user.getId()));
            EcompPortalUtils.setBadPermissions(user, response, "getAppRolesForUser");
            feErrorString = EcompPortalUtils.getFEErrorString(true, response.getStatus());
        } else {
            if (isSystemUser || EcompPortalUtils.legitimateUserId(orgUserId)) {
                result = adminRolesService.getAppRolesForUser(appid, orgUserId, extRequestValue, user.getId());
                logger.debug(EELFLoggerDelegate.debugLogger, "getAppRolesForUser: result {}, appId {}",
                    result, appid);
                int responseCode = EcompPortalUtils.getExternalAppResponseCode();
                if (responseCode != 0 && responseCode != 200) {
                    // external error
                    response.setStatus(responseCode);
                    feErrorString = EcompPortalUtils.getFEErrorString(false, responseCode);
                } else if (result == null) {
                    // If the result is null, there was an internal onap error
                    // in the service call.
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    feErrorString = EcompPortalUtils.getFEErrorString(true,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                logger.info(EELFLoggerDelegate.errorLogger, "getAppRolesForUser - no Organization User ID");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                feErrorString = EcompPortalUtils.getFEErrorString(true, HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        StringBuilder sbUserApps = new StringBuilder();
        if (result != null && !result.isEmpty()) {
            sbUserApps.append("User '").append(orgUserId).append("' has Roles={");
            for (RoleInAppForUser appRole : result) {
                if (appRole.getIsApplied()) {
                    sbUserApps.append(appRole.getRoleName()).append(", ");
                }
            }
            sbUserApps.append("} assigned to the appId '").append(appid).append("'.");
        } else {
            // Not sure creating an empty object will make any difference
            // but would like to give it a shot for defect #DE221057
            if (result == null) {
                result = new ArrayList<>();
            }
            sbUserApps.append("User '").append(orgUserId).append("' and appid ").append(appid).append(" has no roles");
        }
        logger.info(EELFLoggerDelegate.errorLogger, sbUserApps.toString());

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppRoles", "get result =", result);
        if (!feErrorString.isEmpty()) {
            logger.debug(EELFLoggerDelegate.debugLogger, "LR: FEErrorString to header: " + feErrorString);

            response.addHeader("FEErrorString", feErrorString);
            response.addHeader("Access-Control-Expose-Headers", "FEErrorString");
        }
        return result;
    }


    @RequestMapping(value = {"/portalApi/userAppRoles"}, method = {
        RequestMethod.PUT}, produces = "application/json")
    public PortalRestResponse<String> putAppWithUserRoleStateForUser(Principal principal,
        @RequestBody AppWithRolesForUser newAppRolesForUser, HttpServletResponse response) {
        // FieldsValidator fieldsValidator = new FieldsValidator();
        PortalRestResponse<String> portalResponse = new PortalRestResponse<>();
        StringBuilder sbUserApps = new StringBuilder();
        if (newAppRolesForUser != null) {
            sbUserApps.append("User '").append(newAppRolesForUser.getOrgUserId());
            if (newAppRolesForUser.getAppId() != null && !newAppRolesForUser.getAppRoles().isEmpty()) {
                sbUserApps.append("' has roles = { ");
                for (RoleInAppForUser appRole : newAppRolesForUser.getAppRoles()) {
                    if (appRole.getIsApplied()) {
                        sbUserApps.append(appRole.getRoleName()).append(" ,");
                    }
                }
                sbUserApps.deleteCharAt(sbUserApps.length() - 1);
                sbUserApps.append("} assigned for the app ").append(newAppRolesForUser.getAppId());
            } else {
                sbUserApps.append("' has no roles assigned for app ").append(newAppRolesForUser.getAppId());
            }
        }
        logger.info(EELFLoggerDelegate.applicationLogger, "putAppWithUserRoleStateForUser: {}",
            sbUserApps.toString());

        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        // boolean changesApplied = false;
        ExternalRequestFieldsValidator changesApplied = null;

        if (!adminRolesService.isAccountAdmin(user.getId(), user.getOrgUserId(), user.getUserApps())
            && !adminRolesService.isRoleAdmin(user.getId())) {
            EcompPortalUtils.setBadPermissions(user, response, "putAppWithUserRoleStateForUser");
        } else if (newAppRolesForUser == null) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "putAppWithUserRoleStateForUser: newAppRolesForUser is null");
        } else {
            changesApplied = adminRolesService.setAppWithUserRoleStateForUser(user, newAppRolesForUser);
            try {
                if (changesApplied.isResult()) {
                    logger.info(EELFLoggerDelegate.applicationLogger,
                        "putAppWithUserRoleStateForUser: succeeded for app {}, user {}",
                        newAppRolesForUser.getAppId(),
                        newAppRolesForUser.getAppId());

                    MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
                        EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                    AuditLog auditLog = new AuditLog();
                    auditLog.setUserId(user.getId());
                    auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_UPDATE_USER);
                    auditLog.setAffectedRecordId(newAppRolesForUser.getOrgUserId());
                    auditLog.setComments(EcompPortalUtils.truncateString(sbUserApps.toString(),
                        PortalConstants.AUDIT_LOG_COMMENT_SIZE));
                    auditService.logActivity(auditLog, null);

                    MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
                        EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                    EcompPortalUtils.calculateDateTimeDifferenceForLog(
                        MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                        MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                    logger.info(EELFLoggerDelegate.auditLogger,
                        EPLogUtil.formatAuditLogMessage(
                            "UserRolesController.putAppWithUserRoleStateForUser",
                            EcompAuditLog.CD_ACTIVITY_UPDATE_USER, user.getOrgUserId(),
                            newAppRolesForUser.getOrgUserId(), sbUserApps.toString()));
                    MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                    MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                    MDC.remove(SystemProperties.MDC_TIMER);
                    portalResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);

                }
                if (!changesApplied.isResult()) {
                    throw new Exception(changesApplied.getDetailMessage());
                }

            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "putAppWithUserRoleStateForUser: failed for app {}, user {}",
                    newAppRolesForUser.getAppId(),
                    newAppRolesForUser.getOrgUserId(), e);
                portalResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), null);
            }
        }

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppRoles", "put result =", changesApplied);
        return portalResponse;
    }

    @RequestMapping(value = {"/portalApi/updateRemoteUserProfile"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public PortalRestResponse<String> updateRemoteUserProfile(HttpServletRequest request) {

        String updateRemoteUserFlag = FAILURE;
        try {
            // saveNewUser = userService.saveNewUser(newUser);
            String orgUserId = request.getParameter("loginId");
            long appId = Long.parseLong(request.getParameter("appId"));
            fnUserRoleService.updateRemoteUserProfile(orgUserId, appId);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "updateRemoteUserProfile failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, updateRemoteUserFlag, e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, updateRemoteUserFlag, "");

    }

    @RequestMapping(value = {"/portalApi/app/{appId}/users"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public List<UserApplicationRoles> getUsersFromAppEndpoint(@PathVariable("appId") Long appId) {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "/portalApi/app/{}/users was invoked", appId);
            return fnUserRoleService.getUsersFromAppEndpoint(appId);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getUsersFromAppEndpoint failed", e);
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = {"/portalApi/app/{appId}/roles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public List<EcompRole> testGetRoles(HttpServletRequest request, @PathVariable("appId") Long appId)
        throws HTTPException {
        EcompRole[] appRoles = applicationsRestClientService.get(EcompRole[].class, appId, "/roles");
        List<EcompRole> rolesList = Arrays.asList(appRoles);
        EcompPortalUtils
            .logAndSerializeObject(logger, "/portalApi/app/{appId}/roles", "response for appId=" + appId,
                rolesList);

        return rolesList;
    }


    @RequestMapping(value = {"/portalApi/admin/import/app/{appId}/roles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public List<FnRole> importRolesFromRemoteApplication(@PathVariable("appId") Long appId) throws HTTPException {
        List<FnRole> rolesList = fnUserRoleService.importRolesFromRemoteApplication(appId);
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/admin/import/app/{appId}/roles",
            "response for appId=" + appId, rolesList);

        return rolesList;
    }


    @RequestMapping(value = {"/portalApi/app/{appId}/user/{orgUserId}/roles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public EcompRole testGetRoles(@PathVariable("appId") Long appId,
        @PathVariable("orgUserId") String orgUserId) throws Exception {
        if (!EcompPortalUtils.legitimateUserId(orgUserId)) {
            String msg = "Error /user/<user>/roles not legitimate orgUserId = " + orgUserId;
            logger.error(EELFLoggerDelegate.errorLogger, msg);
            throw new Exception(msg);
        }
        EcompRole[] roles = applicationsRestClientService.get(EcompRole[].class, appId,
            String.format("/user/%s/roles", orgUserId));
        if (roles.length != 1) {
            String msg =
                "Error /user/<user>/roles returned array. expected size 1 recieved size = " + roles.length;
            logger.error(EELFLoggerDelegate.errorLogger, msg);
            throw new Exception(msg);
        }

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/app/{appId}/user/{orgUserId}/roles",
            "response for appId='" + appId + "' and orgUserId='" + orgUserId + "'", roles[0]);
        return roles[0];
    }


    @RequestMapping(value = {"/portalApi/saveUserAppRoles"}, method = {
        RequestMethod.PUT}, produces = "application/json")
    public FieldsValidator putAppWithUserRoleRequest(Principal principal,
        @RequestBody AppWithRolesForUser newAppRolesForUser, HttpServletResponse response) {
        FieldsValidator fieldsValidator = null;
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        try {
            fieldsValidator = fnUserRoleService.putUserAppRolesRequest(newAppRolesForUser, user);
            response.setStatus(0);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "putAppWithUserRoleRequest failed", e);

        }
        // return fieldsValidator;
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/saveUserAppRoles", "PUT result =",
            response.getStatus());
        return fieldsValidator;
    }


    @SuppressWarnings("ConstantConditions")
    @RequestMapping(value = {"/portalApi/appCatalogRoles"}, method = {
        RequestMethod.GET}, produces = "application/json")
    public List<EPUserAppCatalogRoles> getUserAppCatalogRoles(Principal principal,
        @RequestParam("appName") String appName) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        List<EPUserAppCatalogRoles> userAppRoleList = null;
        try {
            userAppRoleList = fnUserRoleService.getUserAppCatalogRoles(user, appName);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "putUserWidgetsSortPref failed", e);

        }
        userAppRoleList.sort(getUserAppCatalogRolesComparator);
        EcompPortalUtils
            .logAndSerializeObject(logger, "/portalApi/userApplicationRoles", "result =", userAppRoleList);

        return userAppRoleList;

    }


    private final Comparator<EPUserAppCatalogRoles> getUserAppCatalogRolesComparator =
        Comparator.comparing(EPUserAppCatalogRoles::getRoleName);

    @RequestMapping(value = "/portalApi/externalRequestAccessSystem", method = RequestMethod.GET,
        produces = "application/json")
    public ExternalSystemAccess readExternalRequestAccess() {
        ExternalSystemAccess result = null;
        try {
            result = fnUserRoleService.getExternalRequestAccess();
            EcompPortalUtils
                .logAndSerializeObject(logger, "/portalApi/externalRequestAccessSystem", "GET result =",
                    result);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "readExternalRequestAccess failed: " + e.getMessage());
        }
        return result;

    }

    @RequestMapping(value = {"/portalApi/checkIfUserIsSuperAdmin"}, method = RequestMethod.GET,
        produces = "application/json")
    public boolean checkIfUserIsSuperAdmin(Principal principal) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());

        boolean isSuperAdmin = false;
        try {
            isSuperAdmin = adminRolesService.isSuperAdmin(user.getLoginId());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "checkIfUserIsSuperAdmin failed: " + e.getMessage());
        }
        return isSuperAdmin;
    }
}
