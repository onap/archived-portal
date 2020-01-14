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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.ep.EpNotification;
import org.onap.portal.domain.db.ep.EpRoleNotification;
import org.onap.portal.domain.db.fn.FnMenuFunctionalRoles;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.ecomp.EcompAppRole;
import org.onap.portal.domain.dto.transport.EpNotificationItemVO;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portal.service.EcompAppRoleService;
import org.onap.portal.service.EpNotificationItemVOService;
import org.onap.portal.service.epNotification.EpNotificationService;
import org.onap.portal.service.epRoleNotification.EpRoleNotificationService;
import org.onap.portal.service.epUserNotification.EpUserNotificationService;
import org.onap.portal.service.menuFunctionalRoles.FnMenuFunctionalRolesService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAspectJAutoProxy
@EPAuditLog
public class UserNotificationController {

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserNotificationController.class);

    private final FnMenuFunctionalRolesService functionalMenuService;
    private final FnUserService fnUserService;
    private final EpNotificationService userNotificationService;
    private final EpUserNotificationService epUserNotificationService;
    private final EpRoleNotificationService roleNotificationService;
    private final EcompAppRoleService ecompAppRoleService;
    private final EpNotificationItemVOService epNotificationItemVOService;

    private static final String SUCCESS = "success";
    private static final String FAILURE = "FAILURE";

    @Autowired
    public UserNotificationController(
        final FnMenuFunctionalRolesService functionalMenuService, final FnUserService fnUserService,
        final EpNotificationService epNotificationService,
        final EpUserNotificationService epUserNotificationService,
        final EpRoleNotificationService roleNotificationService,
        final EcompAppRoleService ecompAppRoleService,
        final EpNotificationItemVOService epNotificationItemVOService) {
        this.functionalMenuService = functionalMenuService;
        this.fnUserService = fnUserService;
        this.userNotificationService = epNotificationService;
        this.epUserNotificationService = epUserNotificationService;
        this.roleNotificationService = roleNotificationService;
        this.ecompAppRoleService = ecompAppRoleService;
        this.epNotificationItemVOService = epNotificationItemVOService;
    }

    @RequestMapping(value = {
            "/portalApi/getFunctionalMenuRole" }, method = RequestMethod.GET, produces = "application/json")
    public List<FnMenuFunctionalRoles> getMenuIdRoleId(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        return functionalMenuService.findAll();
    }

    @RequestMapping(value = {
            "/portalApi/getNotifications" }, method = RequestMethod.GET, produces = "application/json")
    public PortalRestResponse<List<EpNotification>> getNotifications(Principal principal, HttpServletRequest request,
            HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        PortalRestResponse<List<EpNotification>> portalRestResponse = null;
        try {
            List<EpNotification> notificationList = userNotificationService.getNotifications(user.getId());
            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS,
                    notificationList);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getAllAppsAndContacts failed", e);
            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), null);
        }
        return portalRestResponse;
    }

    @RequestMapping(value = {
            "/portalApi/getAdminNotifications" }, method = RequestMethod.GET, produces = "application/json")
    public List<EpNotificationItemVO> getAdminNotifications(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        List<EpNotificationItemVO> adminNotificationList = null;
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        adminNotificationList = userNotificationService.getAdminNotificationVOS(user.getId());
        return adminNotificationList;
    }

    @RequestMapping(value = "/portalApi/saveNotification", method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> save(Principal principal, HttpServletRequest request, HttpServletResponse response,
            @RequestBody EpNotification notificationItem) {
        FnUser fnUser = fnUserService.loadUserByUsername(principal.getName());
        if (notificationItem == null || notificationItem.getMsgHeader() == null)
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "Notification Header cannot be null or empty");
        if (notificationItem.getEndTime().compareTo(notificationItem.getStartTime()) < 0) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "End Time should be greater than  start time");
        }

        if (("N".equals(notificationItem.getIsForAllRoles())) && notificationItem.getRoleIds().isEmpty()) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "No Roles Ids Exist for the selected Roles");
        }

        Long creatorId = UserUtils.getUserIdAsLong(request);
        notificationItem.setCreatorId(fnUser);

        //TODO
        // Front-end date picker does not accept a time value, so all
        // values are the start of the chosen day in the local time zone.
        // Move the end time value to the very end of the chosen day.
        // Avoid Calendar.getDefault() which uses the server's locale.
        //Long endTime = notificationItem.getEndTime()
        //endTime += (23 * 3600 + 59 * 60 + 59) * 1000;
        //notificationItem.getEndTime().setTime(endTime);

        try {
            userNotificationService.saveNotification(notificationItem);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveNotification failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
    }

    @RequestMapping(value = {
            "/portalApi/notificationUpdateRate" }, method = RequestMethod.GET, produces = "application/json")
    public PortalRestResponse<Map<String, String>> getNotificationUpdateRate(HttpServletRequest request) {
        try {
            String updateRate = SystemProperties.getProperty(EPCommonSystemProperties.NOTIFICATION_UPDATE_RATE);
            String updateDuration = SystemProperties.getProperty(EPCommonSystemProperties.NOTIFICATION_UPDATE_DURATION);
            Integer rateInMiliSec = Integer.parseInt(updateRate) * 1000;
            Integer durationInMiliSec = Integer.parseInt(updateDuration) * 1000;
            Map<String, String> results = new HashMap<>();
            results.put("updateRate", String.valueOf(rateInMiliSec));
            results.put("updateDuration", String.valueOf(durationInMiliSec));
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, results);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getNotificationUpdateRate failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
        }
    }

    @RequestMapping(value = {
            "/portalApi/notificationRead" }, method = RequestMethod.GET, produces = "application/json")
    public PortalRestResponse<Map<String, String>> notificationRead(
            @RequestParam("notificationId") Long notificationID, HttpServletRequest request) {
        try {
            epUserNotificationService.setNotificationRead(notificationID, UserUtils.getUserId(request));
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, null);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "notificationRead failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
        }
    }

    @RequestMapping(value = {
            "/portalApi/getNotificationHistory" }, method = RequestMethod.GET, produces = "application/json")
    public List<EpNotificationItemVO> getNotificationHistory(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        FnUser user = fnUserService.loadUserByUsername(principal.getName());
        return epNotificationItemVOService.getNotificationHistoryVO(user.getId());
    }

    @RequestMapping(value = { "/portalApi/notificationRole/{notificationId}/roles" }, method = {
            RequestMethod.GET }, produces = "application/json")
    public List<Long> testGetRoles(HttpServletRequest request, @PathVariable("notificationId") Long notificationId) {
        List<EpRoleNotification> notifRoles = roleNotificationService.getNotificationRoles(notificationId);
        ArrayList<Long> rolesList = new ArrayList<>();
        for (EpRoleNotification notifRole : notifRoles) {
            rolesList.add(notifRole.getRoleId().getId());
        }
        return rolesList;
    }

    @RequestMapping(value = { "/portalApi/getNotificationAppRoles" }, method = {
            RequestMethod.GET }, produces = "application/json")
    public List<EcompAppRole> getNotificationAppRoles(HttpServletRequest request, HttpServletResponse response) {
        List<EcompAppRole> epAppRoleList = null;
        try {
            epAppRoleList = ecompAppRoleService.getAppRoleList();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                    "Exception occurred while performing UserNofiticationController.getNotificationAppRoles. Details: ",
                    e);
        }
        return epAppRoleList;
    }

    @RequestMapping(value = {
            "/portalApi/getMessageRecipients" }, method = RequestMethod.GET, produces = "application/json")
    public List<String> getMessageRecipients(@RequestParam("notificationId") Long notificationID) {
        return userNotificationService.getMessageRecipients(notificationID);
    }

}
