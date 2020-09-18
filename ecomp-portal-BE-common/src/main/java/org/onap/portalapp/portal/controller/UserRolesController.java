/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.cxf.transport.http.HTTPException;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserAppCatalogRoles;
import org.onap.portalapp.portal.domain.EcompAuditLog;
import org.onap.portalapp.portal.domain.ExternalSystemAccess;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.ApplicationsRestClientService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalapp.portal.service.UserRolesService;
import org.onap.portalapp.portal.transport.AppNameIdIsAdmin;
import org.onap.portalapp.portal.transport.AppWithRolesForUser;
import org.onap.portalapp.portal.transport.AppsListWithAdminRole;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.RoleInAppForUser;
import org.onap.portalapp.portal.transport.UserApplicationRoles;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserRolesController extends EPRestrictedBaseController {

    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRolesController.class);

    @Autowired
    private SearchService searchService;
    @Autowired
    private AdminRolesService adminRolesService;
    private @Autowired UserRolesService userRolesService;
    @Autowired
    private ApplicationsRestClientService applicationsRestClientService;
    @Autowired
    private AuditService auditService;
    @Autowired
	private DataAccessService dataAccessService;

    private static final String FAILURE = "failure";

    /**
     * RESTful service method to fetch users in the WebPhone external service
     *
     * @param request HttpServletRequest
     * @param searchString search string
     * @param response HttpServletResponse
     * @return array of found users as json
     */
    @GetMapping(value = { "/portalApi/queryUsers" }, produces = "application/json")
    public String getPhoneBookSearchResult(HttpServletRequest request, @RequestParam("search") String searchString,
            HttpServletResponse response) {
        EPUser user = EPUserUtils.getUserSession(request);
        String searchResult = null;
        if (!adminRolesService.isSuperAdmin(user) && !adminRolesService.isAccountAdmin(user)
                && !adminRolesService.isRoleAdmin(user)) {
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

    /**
     * RESTful service method to fetch applications where user is admin
     *
     * @param request HttpServletRequest
     * @param orgUserId search string
     * @param response HttpServletResponse
     * @return for GET: array of all applications with boolean isAdmin=true/false for each application
     */
    @GetMapping(value = { "/portalApi/adminAppsRoles" }, produces = "application/json")
    public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(HttpServletRequest request,
            @RequestParam("user") String orgUserId, HttpServletResponse response) {

        EPUser user = EPUserUtils.getUserSession(request);
        AppsListWithAdminRole result = null;
        if (!adminRolesService.isSuperAdmin(user)) {
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
            if (!result.appsRoles.isEmpty()) {
                adminAppRoles.append("User '" + result.orgUserId + "' has admin role to the apps = {");
                for (AppNameIdIsAdmin adminAppRole : result.appsRoles) {
                    if (adminAppRole.isAdmin) {
                        adminAppRoles.append(adminAppRole.appName + ", ");
                    }
                }
                adminAppRoles.append("}.");
            } else {
                adminAppRoles.append("User '" + result.orgUserId + "' has no Apps with Admin Role.");
            }
        } else {
            logger.error(EELFLoggerDelegate.errorLogger,
                    "putAppWithUserRoleStateForUser: getAppsWithAdminRoleStateForUser result is null");
        }

        logger.info(EELFLoggerDelegate.errorLogger, adminAppRoles.toString());

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/adminAppsRoles", "get result =", result);

        return result;
    }

    /**
     *
     * @param request HttpServletRequest
     * @param newAppsListWithAdminRoles new apps
     * @param response HttpServletResponse
     * @return FieldsValidator
     */
    @PutMapping(value = { "/portalApi/adminAppsRoles" }, produces = "application/json")
    public FieldsValidator putAppsWithAdminRoleStateForUser(HttpServletRequest request,
            @RequestBody AppsListWithAdminRole newAppsListWithAdminRoles, HttpServletResponse response) {

        // newAppsListWithAdminRoles.appsRoles
        FieldsValidator fieldsValidator = new FieldsValidator();
        StringBuilder newAppRoles = new StringBuilder();
        if (newAppsListWithAdminRoles != null) {
            if (!newAppsListWithAdminRoles.appsRoles.isEmpty()) {
                newAppRoles
                        .append("User '" + newAppsListWithAdminRoles.orgUserId + "' has admin role to the apps = { ");
                for (AppNameIdIsAdmin adminAppRole : newAppsListWithAdminRoles.appsRoles) {
                    if (adminAppRole.isAdmin) {
                        newAppRoles.append(adminAppRole.appName + " ,");
                    }
                }
                newAppRoles.deleteCharAt(newAppRoles.length() - 1);
                newAppRoles.append("}.");
            } else {
                newAppRoles.append("User '" + newAppsListWithAdminRoles.orgUserId + "' has no Apps with Admin Role.");
            }
        } else {
            logger.error(EELFLoggerDelegate.errorLogger,
                    "putAppWithUserRoleStateForUser: putAppsWithAdminRoleStateForUser result is null");
            fieldsValidator.httpStatusCode = (long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        logger.info(EELFLoggerDelegate.errorLogger, newAppRoles.toString());

        EPUser user = EPUserUtils.getUserSession(request);
        boolean changesApplied = false;

        if (!adminRolesService.isSuperAdmin(user)) {
            EcompPortalUtils.setBadPermissions(user, response, "putAppsWithAdminRoleStateForUser");
        } else {
            changesApplied = adminRolesService.setAppsWithAdminRoleStateForUser(newAppsListWithAdminRoles);
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(user.getId());
            auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN);
            if (newAppsListWithAdminRoles != null) {
                auditLog.setAffectedRecordId(newAppsListWithAdminRoles.orgUserId);
            }
            auditLog.setComments(
                    EcompPortalUtils.truncateString(newAppRoles.toString(), PortalConstants.AUDIT_LOG_COMMENT_SIZE));
            auditService.logActivity(auditLog, null);
			String auditMessageInfo = EPLogUtil.formatAuditLogMessage("UserRolesController.putAppsWithAdminRoleStateForUser",
                    EcompAuditLog.CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN, user.getOrgUserId(),
                    newAppsListWithAdminRoles.orgUserId, newAppRoles.toString());		

			EPLogUtil.logAuditMessage(logger, auditMessageInfo);
            
        }
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/adminAppsRoles", "put result =", changesApplied);

        return fieldsValidator;
    }

    /**
     * It returns a list of user app roles for single app
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param orgUserId user ID
     * @param appid application ID
     * @param extRequestValue set to false if request is from users page otherwise true
     * @return List<RoleInAppForUser>
     */
    @GetMapping(value = { "/portalApi/userAppRoles" }, produces = "application/json")
    public List<RoleInAppForUser> getAppRolesForUser(HttpServletRequest request, @RequestParam("user") String orgUserId,
            @RequestParam("app") Long appid, @RequestParam("externalRequest") Boolean extRequestValue,
            @RequestParam("isSystemUser") Boolean isSystemUser,
            HttpServletResponse response) {
        EPUser user = EPUserUtils.getUserSession(request);
        List<RoleInAppForUser> result = null;
        String feErrorString = "";
        if (!adminRolesService.isAccountAdmin(user) && !adminRolesService.isRoleAdmin(user)) {
            logger.debug(EELFLoggerDelegate.debugLogger,
                    "getAppRolesForUser: Accountadminpermissioncheck {}, RoleAdmincheck {}",
                    adminRolesService.isAccountAdmin(user), adminRolesService.isRoleAdmin(user));
            EcompPortalUtils.setBadPermissions(user, response, "getAppRolesForUser");
            feErrorString = EcompPortalUtils.getFEErrorString(true, response.getStatus());
        } else {
        	try {
        		if(orgUserId!=null) {
	        		EPUser localUser  = getUserInfo(orgUserId,applicationsRestClientService);
	        		if(localUser !=null) {
	        			if(localUser.isSystemUser()) {
	        				isSystemUser = true;
	        			}
	        		}
        		}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "isSystemUser update failed", e);
			}
            if ((!isSystemUser && EcompPortalUtils.legitimateUserId(orgUserId)) || isSystemUser) {
                result = userRolesService.getAppRolesForUser(appid, orgUserId, extRequestValue, user);
                logger.debug(EELFLoggerDelegate.debugLogger, "getAppRolesForUser: result {}, appId {}", result, appid);
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
            sbUserApps.append("User '" + orgUserId + "' has Roles={");
            for (RoleInAppForUser appRole : result) {
                if (appRole.isApplied) {
                    sbUserApps.append(appRole.roleName + ", ");
                }
            }
            sbUserApps.append("} assigned to the appId '" + appid + "'.");
        } else {
            // Not sure creating an empty object will make any difference
            // but would like to give it a shot for defect #DE221057
            if (result == null) {
                result = new ArrayList<>();
            }
            sbUserApps.append("User '" + orgUserId + "' and appid " + appid + " has no roles");
        }
        logger.info(EELFLoggerDelegate.errorLogger, sbUserApps.toString());

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppRoles", "get result =", result);
        if (feErrorString != "") {
            logger.debug(EELFLoggerDelegate.debugLogger, "LR: FEErrorString to header: " + feErrorString);

            response.addHeader("FEErrorString", feErrorString);
            response.addHeader("Access-Control-Expose-Headers", "FEErrorString");
        }
        return result;
    }

    @PutMapping(value = { "/portalApi/userAppRoles" }, produces = "application/json")
    public PortalRestResponse<String> putAppWithUserRoleStateForUser(HttpServletRequest request,
            @RequestBody AppWithRolesForUser newAppRolesForUser, HttpServletResponse response) {
        // FieldsValidator fieldsValidator = new FieldsValidator();
        PortalRestResponse<String> portalResponse = new PortalRestResponse<>();
        StringBuilder sbUserApps = new StringBuilder();
        if (newAppRolesForUser != null) {
            sbUserApps.append("User '" + newAppRolesForUser.getOrgUserId());
            if (newAppRolesForUser.getAppId() != null && !newAppRolesForUser.getAppRoles().isEmpty()) {
                sbUserApps.append("' has roles = { ");
                for (RoleInAppForUser appRole : newAppRolesForUser.getAppRoles()) {
                    if (appRole.isApplied) {
                        sbUserApps.append(appRole.roleName + " ,");
                    }
                }
                sbUserApps.deleteCharAt(sbUserApps.length() - 1);
                sbUserApps.append("} assigned for the app " + newAppRolesForUser.getAppId());
            } else {
                sbUserApps.append("' has no roles assigned for app " + newAppRolesForUser.getAppId());
            }
        }
        logger.info(EELFLoggerDelegate.applicationLogger, "putAppWithUserRoleStateForUser: {}", sbUserApps.toString());

        EPUser user = EPUserUtils.getUserSession(request);
        // boolean changesApplied = false;
        ExternalRequestFieldsValidator changesApplied = null;

        if (!adminRolesService.isAccountAdmin(user) && !adminRolesService.isRoleAdmin(user)) {
            EcompPortalUtils.setBadPermissions(user, response, "putAppWithUserRoleStateForUser");
        } else if (newAppRolesForUser == null) {
            logger.error(EELFLoggerDelegate.errorLogger, "putAppWithUserRoleStateForUser: newAppRolesForUser is null");
        } else {
            changesApplied = userRolesService.setAppWithUserRoleStateForUser(user, newAppRolesForUser);
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
        			String auditMessageInfo = EPLogUtil.formatAuditLogMessage("UserRolesController.putAppWithUserRoleStateForUser",
                            EcompAuditLog.CD_ACTIVITY_UPDATE_USER, user.getOrgUserId(),
                            newAppRolesForUser.getOrgUserId(), sbUserApps.toString());		

        			EPLogUtil.logAuditMessage(logger, auditMessageInfo);

        			portalResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", null);

                }
                if (!changesApplied.isResult())
                    throw new Exception(changesApplied.getDetailMessage());

            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                        "putAppWithUserRoleStateForUser: failed for app {}, user {}", newAppRolesForUser.getAppId(),
                        newAppRolesForUser.getOrgUserId(), e);
                portalResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage(), null);
            }
        }

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userAppRoles", "put result =", changesApplied);
        return portalResponse;
    }

    @GetMapping(value = { "/portalApi/updateRemoteUserProfile" }, produces = "application/json")
    public PortalRestResponse<String> updateRemoteUserProfile(HttpServletRequest request,
            HttpServletResponse response) {

        String updateRemoteUserFlag = FAILURE;
        try {
            // saveNewUser = userService.saveNewUser(newUser);
            String orgUserId = request.getParameter("loginId");
            Long appId = Long.parseLong(request.getParameter("appId"));
            userRolesService.updateRemoteUserProfile(orgUserId, appId);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "updateRemoteUserProfile failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, updateRemoteUserFlag, e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, updateRemoteUserFlag, "");

    }

    @GetMapping(value = { "/portalApi/app/{appId}/users" }, produces = "application/json")
    public List<UserApplicationRoles> getUsersFromAppEndpoint(HttpServletRequest request,
            @PathVariable("appId") Long appId) throws HTTPException {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "/portalApi/app/{}/users was invoked", appId);
            return userRolesService.getUsersFromAppEndpoint(appId);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getUsersFromAppEndpoint failed", e);
            return new ArrayList<>();
        }
    }

    @GetMapping(value = { "/portalApi/app/{appId}/roles" }, produces = "application/json")
    public List<EcompRole> testGetRoles(HttpServletRequest request, @PathVariable("appId") Long appId)
            throws HTTPException {
        EcompRole[] appRoles = applicationsRestClientService.get(EcompRole[].class, appId, "/roles");
        List<EcompRole> rolesList = Arrays.asList(appRoles);
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/app/{appId}/roles", "response for appId=" + appId,
                rolesList);

        return rolesList;
    }

    @GetMapping(value = { "/portalApi/admin/import/app/{appId}/roles" }, produces = "application/json")
    public List<EPRole> importRolesFromRemoteApplication(HttpServletRequest request, @PathVariable("appId") Long appId)
            throws HTTPException {
        List<EPRole> rolesList = userRolesService.importRolesFromRemoteApplication(appId);
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/admin/import/app/{appId}/roles",
                "response for appId=" + appId, rolesList);

        return rolesList;
    }

    @GetMapping(value = { "/portalApi/app/{appId}/user/{orgUserId}/roles" }, produces = "application/json")
    public EcompRole testGetRoles(HttpServletRequest request, @PathVariable("appId") Long appId,
            @PathVariable("orgUserId") String orgUserId) throws Exception {
        if (!EcompPortalUtils.legitimateUserId(orgUserId)) {
            String msg = "Error /user/<user>/roles not legitimate orgUserId = " + orgUserId;
            logger.error(EELFLoggerDelegate.errorLogger, msg);
            throw new Exception(msg);
        }
        EcompRole[] roles = applicationsRestClientService.get(EcompRole[].class, appId,
                String.format("/user/%s/roles", orgUserId));
        if (roles.length != 1) {
            String msg = "Error /user/<user>/roles returned array. expected size 1 recieved size = " + roles.length;
            logger.error(EELFLoggerDelegate.errorLogger, msg);
            throw new Exception(msg);
        }

        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/app/{appId}/user/{orgUserId}/roles",
                "response for appId='" + appId + "' and orgUserId='" + orgUserId + "'", roles[0]);
        return roles[0];
    }

    @PutMapping(value = { "/portalApi/saveUserAppRoles" }, produces = "application/json")
    public FieldsValidator putAppWithUserRoleRequest(HttpServletRequest request,
            @RequestBody AppWithRolesForUser newAppRolesForUser, HttpServletResponse response) {
        FieldsValidator fieldsValidator = null;
        try {

            EPUser user = EPUserUtils.getUserSession(request);
            fieldsValidator = userRolesService.putUserAppRolesRequest(newAppRolesForUser, user);
            response.setStatus(fieldsValidator.httpStatusCode.intValue());

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "putAppWithUserRoleRequest failed", e);

        }
        // return fieldsValidator;
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/saveUserAppRoles", "PUT result =",
                response.getStatus());
        return fieldsValidator;
    }

    @GetMapping(value = { "/portalApi/appCatalogRoles" }, produces = "application/json")
    public List<EPUserAppCatalogRoles> getUserAppCatalogRoles(HttpServletRequest request,
            @RequestParam("appName") String appName) {
        EPUser user = EPUserUtils.getUserSession(request);
        List<EPUserAppCatalogRoles> userAppRoleList = null;
        try {
            userAppRoleList = userRolesService.getUserAppCatalogRoles(user, appName);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "putUserWidgetsSortPref failed", e);

        }
        Collections.sort(userAppRoleList, getUserAppCatalogRolesComparator);
        EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/userApplicationRoles", "result =", userAppRoleList);

        return userAppRoleList;

    }

    private Comparator<EPUserAppCatalogRoles> getUserAppCatalogRolesComparator =
            new Comparator<EPUserAppCatalogRoles>() {
                public int compare(EPUserAppCatalogRoles o1, EPUserAppCatalogRoles o2) {
                    return o1.getRolename().compareTo(o2.getRolename());
                }
            };

    @GetMapping(value = "/portalApi/externalRequestAccessSystem",
            produces = "application/json")
    public ExternalSystemAccess readExternalRequestAccess(HttpServletRequest request) {
        ExternalSystemAccess result = null;
        try {
            result = userRolesService.getExternalRequestAccess();
            EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/externalRequestAccessSystem", "GET result =",
                    result);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "readExternalRequestAccess failed: " + e.getMessage());
        }
        return result;
    }

    @GetMapping(value = { "/portalApi/checkIfUserIsSuperAdmin" }, produces = "application/json")
    public boolean checkIfUserIsSuperAdmin(HttpServletRequest request,
            HttpServletResponse response) {
        EPUser user = EPUserUtils.getUserSession(request);
        boolean isSuperAdmin = false;
        try {
            isSuperAdmin = adminRolesService.isSuperAdmin(user);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "checkIfUserIsSuperAdmin failed: " + e.getMessage());
        }
        return isSuperAdmin;
    }
    
    /**
	 * 
	 * @param userId
	 * @param app
	 * @param applicationsRestClientService
	 * @return EPUser
	 * @throws HTTPException
	 */
	protected EPUser getUserInfo(String userId, ApplicationsRestClientService applicationsRestClientService)
			throws HTTPException {
		@SuppressWarnings("unchecked")
		List<EPUser> userList = (List<EPUser>) dataAccessService
				.executeQuery("from EPUser where orgUserId='" + userId + "'", null);
		if (userList != null && !userList.isEmpty())
			return userList.get(0);
		else
			return null;
	}
}
