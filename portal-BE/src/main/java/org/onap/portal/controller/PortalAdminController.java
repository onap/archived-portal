/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.ecomp.EcompAuditLog;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.PortalAdmin;
import org.onap.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.AdminRolesService;
import org.onap.portal.service.PortalAdminService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.validation.DataValidator;
import org.onap.portal.validation.SecureString;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.service.AuditServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@EnableAspectJAutoProxy
public class PortalAdminController {

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalAdminController.class);
    private static final DataValidator DATA_VALIDATOR = new DataValidator();

    private PortalAdminService portalAdminService;
    private final FnUserService fnUserService;
    private AdminRolesService adminRolesService;
    private AuditServiceImpl auditService = new AuditServiceImpl();

    @Autowired
    public PortalAdminController(PortalAdminService portalAdminService,
        FnUserService fnUserService, AdminRolesService adminRolesService) {
        this.portalAdminService = portalAdminService;
        this.fnUserService = fnUserService;
        this.adminRolesService = adminRolesService;
    }

    @RequestMapping(value = {"/portalApi/portalAdmins"}, method = RequestMethod.GET, produces = "application/json")
    public List<PortalAdmin> getPortalAdmins(Principal principal, HttpServletRequest request,
        HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        List<PortalAdmin> portalAdmins = null;
        if (user == null) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.getPortalAdmins, null user");
            EcompPortalUtils.setBadPermissions(user, response, "getPortalAdmins");
        } else if (!adminRolesService.isSuperAdmin(user.getLoginId())) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.getPortalAdmins, bad permissions");
            EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
        } else {
            // return the list of portal admins
            portalAdmins = portalAdminService.getPortalAdmins();
            logger.debug(EELFLoggerDelegate.debugLogger, "portalAdmins: called getPortalAdmins()");
            EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/getPortalAdmins", "result =", portalAdmins);
        }

        return portalAdmins;
    }

    @RequestMapping(value = {"/portalApi/portalAdmin"}, method = RequestMethod.POST)
    public FieldsValidator createPortalAdmin(Principal principal, HttpServletRequest request,
        @RequestBody String userId,
        HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        FieldsValidator fieldsValidator = null;
        if (!DATA_VALIDATOR.isValid(new SecureString(userId))) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.createPortalAdmin not valid userId");
            EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
        } else if (user == null) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.createPortalAdmin, null user");
            EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
        } else if (!adminRolesService.isSuperAdmin(user.getLoginId())) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.createPortalAdmin bad permissions");
            EcompPortalUtils.setBadPermissions(user, response, "createPortalAdmin");
        } else {
            fieldsValidator = portalAdminService.createPortalAdmin(userId);
            int statusCode = fieldsValidator.getHttpStatusCode().intValue();
            response.setStatus(statusCode);
            if (statusCode == 200) {
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_ADD_PORTAL_ADMIN);
                auditLog.setAffectedRecordId(userId);
                try {
                    auditService.logActivity(auditLog, null);
                } catch (Exception e) {
                    logger.error(EELFLoggerDelegate.errorLogger, "createPortalAdmin: failed for save audit log", e);
                }
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("PortalAdminController.createPortalAdmin",
                        EcompAuditLog.CD_ACTIVITY_ADD_PORTAL_ADMIN, user.getOrgUserId(), userId,
                        "A new Portal Admin has been added"));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            }
        }
        EcompPortalUtils.logAndSerializeObject(logger, "/portalAdmin", "POST result =", response.getStatus());

        return fieldsValidator;
    }

    @RequestMapping(value = {"/portalApi/portalAdmin/{userInfo}"}, method = RequestMethod.DELETE)
    public FieldsValidator deletePortalAdmin(Principal principal, HttpServletRequest request,
        @PathVariable("userInfo") String userInfo,
        HttpServletResponse response) {
        if (!DATA_VALIDATOR.isValid(new SecureString(userInfo))) {
            logger.debug(EELFLoggerDelegate.debugLogger, "PortalAdminController.deletePortalAdmin not valid userId");
            return null;
        }
        int userIdIdx = userInfo.indexOf("-");
        Long userId = null;
        String sbcid = null;
        FieldsValidator fieldsValidator = null;
        try {
            if (userIdIdx == -1) {
                logger.error(EELFLoggerDelegate.errorLogger, "deletePortalAdmin missing userId");
                return fieldsValidator;
            } else {
                String userIdStr = userInfo.substring(0, userIdIdx);
                userId = Long.valueOf(userIdStr);
                sbcid = userInfo.substring(userIdIdx + 1, userInfo.length());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deletePortalAdmin error while parsing the userInfo", e);
        }
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        if (!adminRolesService.isSuperAdmin(user.getLoginId())) {
            EcompPortalUtils.setBadPermissions(user, response, "deletePortalAdmin");
        } else {
            fieldsValidator = portalAdminService.deletePortalAdmin(userId);
            int statusCode = fieldsValidator.getHttpStatusCode().intValue();
            response.setStatus(statusCode);
            if (statusCode == 200) {
                AuditLog auditLog = new AuditLog();
                auditLog.setUserId(user.getId());
                auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_DELETE_PORTAL_ADMIN);
                auditLog.setAffectedRecordId(sbcid);
                auditService.logActivity(auditLog, null);
                MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
                EcompPortalUtils.calculateDateTimeDifferenceForLog(
                    MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
                    MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
                logger.info(EELFLoggerDelegate.auditLogger,
                    EPLogUtil.formatAuditLogMessage("PortalAdminController.deletePortalAdmin",
                        EcompAuditLog.CD_ACTIVITY_DELETE_PORTAL_ADMIN, user.getOrgUserId(), sbcid,
                        "A Portal Admin has been deleted"));
                MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
                MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
                MDC.remove(SystemProperties.MDC_TIMER);
            }
        }
        EcompPortalUtils.logAndSerializeObject(logger, "/portalAdmin", "DELETE result =", response.getStatus());

        return fieldsValidator;
    }

    @RequestMapping(value = {
        "/portalApi/adminAppsRoles/{appId}"}, method = RequestMethod.GET, produces = "application/json")
    public List<FnRole> getRolesByApp(Principal principal, HttpServletRequest request,
        @PathVariable("appId") Long appId, HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        List<FnRole> rolesByApp = null;
        try {
            if (user == null) {
                EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
            } else {
                rolesByApp = adminRolesService.getRolesByApp(appId);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRolesByApp failed", e);
        }
        return rolesByApp;
    }
}
