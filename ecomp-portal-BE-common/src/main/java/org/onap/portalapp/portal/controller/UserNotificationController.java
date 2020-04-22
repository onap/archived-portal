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
package org.onap.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompAppRole;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.UserNotificationService;
import org.onap.portalapp.portal.transport.EpNotificationItem;
import org.onap.portalapp.portal.transport.EpNotificationItemVO;
import org.onap.portalapp.portal.transport.EpRoleNotificationItem;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserNotificationController extends EPRestrictedBaseController {

    @Autowired
    FunctionalMenuService functionalMenuService;

    @Autowired
    UserNotificationService userNotificationService;

    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserNotificationController.class);
    private static final String SUCCESS = "success";
    private static final String FAILURE = "FAILURE";

    @GetMapping(value = {
            "/portalApi/getFunctionalMenuRole" }, produces = "application/json")
    public List<FunctionalMenuRole> getMenuIdRoleId(HttpServletRequest request, HttpServletResponse response) {
        // EPUser user = EPUserUtils.getUserSession(request);
        List<FunctionalMenuRole> menuRoleList = null;
        menuRoleList = functionalMenuService.getFunctionalMenuRole();
        return menuRoleList;
    }

    @GetMapping(value = {
            "/portalApi/getNotifications" }, produces = "application/json")
    public PortalRestResponse<List<EpNotificationItem>> getNotifications(HttpServletRequest request,
            HttpServletResponse response) {
        EPUser user = EPUserUtils.getUserSession(request);
        PortalRestResponse<List<EpNotificationItem>> portalRestResponse = null;
        try {
            List<EpNotificationItem> notificationList = userNotificationService.getNotifications(user.getId());
            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS,
                    notificationList);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getAllAppsAndContacts failed", e);
            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), null);
        }
        return portalRestResponse;
    }

    @GetMapping(value = {
            "/portalApi/getAdminNotifications" }, produces = "application/json")
    public List<EpNotificationItemVO> getAdminNotifications(HttpServletRequest request, HttpServletResponse response) {
        List<EpNotificationItemVO> adminNotificationList = null;
        EPUser user = EPUserUtils.getUserSession(request);
        adminNotificationList = userNotificationService.getAdminNotificationVOS(user.getId());
        return adminNotificationList;
    }

    @PostMapping(value = "/portalApi/saveNotification", produces = "application/json")
    public PortalRestResponse<String> save(HttpServletRequest request, HttpServletResponse response,
            @RequestBody EpNotificationItem notificationItem) {

        if (notificationItem == null || notificationItem.getMsgHeader() == null)
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "Notification Header cannot be null or empty");
        if (notificationItem.getEndTime().compareTo(notificationItem.getStartTime()) < 0) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "End Time should be greater than  start time");
        }

        if ((notificationItem.getIsForAllRoles() == "N") && notificationItem.getRoleIds().isEmpty()) {
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE,
                    "No Roles Ids Exist for the selected Roles");
        }

        Long creatorId = UserUtils.getUserIdAsLong(request);
        notificationItem.setCreatorId(creatorId);

        // Front-end date picker does not accept a time value, so all
        // values are the start of the chosen day in the local time zone.
        // Move the end time value to the very end of the chosen day.
        // Avoid Calendar.getDefault() which uses the server's locale.
        Long endTime = notificationItem.getEndTime().getTime();
        endTime += (23 * 3600 + 59 * 60 + 59) * 1000;
        notificationItem.getEndTime().setTime(endTime);

        try {
            userNotificationService.saveNotification(notificationItem);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveNotification failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
        }
        return new PortalRestResponse<>(PortalRestStatusEnum.OK, "SUCCESS", "");
    }

    @GetMapping(value = {
            "/portalApi/notificationUpdateRate" }, produces = "application/json")
    public PortalRestResponse<Map<String, String>> getNotificationUpdateRate(HttpServletRequest request) {
        try {
            String updateRate = SystemProperties.getProperty(EPCommonSystemProperties.NOTIFICATION_UPDATE_RATE);
            String updateDuration = SystemProperties.getProperty(EPCommonSystemProperties.NOTIFICATION_UPDATE_DURATION);
            Integer rateInMiliSec = Integer.valueOf(updateRate) * 1000;
            Integer durationInMiliSec = Integer.valueOf(updateDuration) * 1000;
            Map<String, String> results = new HashMap<>();
            results.put("updateRate", String.valueOf(rateInMiliSec));
            results.put("updateDuration", String.valueOf(durationInMiliSec));
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, results);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getNotificationUpdateRate failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
        }
    }

    @GetMapping(value = {
            "/portalApi/notificationRead" }, produces = "application/json")
    public PortalRestResponse<Map<String, String>> notificationRead(
            @RequestParam("notificationId") String notificationID, HttpServletRequest request) {
        try {
            userNotificationService.setNotificationRead(Long.parseLong(notificationID), UserUtils.getUserId(request));
            return new PortalRestResponse<>(PortalRestStatusEnum.OK, SUCCESS, null);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "notificationRead failed", e);
            return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
        }
    }

    @GetMapping(value = {
            "/portalApi/getNotificationHistory" }, produces = "application/json")
    public List<EpNotificationItemVO> getNotificationHistory(HttpServletRequest request, HttpServletResponse response) {
        EPUser user = EPUserUtils.getUserSession(request);
        List<EpNotificationItemVO> notificationList = null;
        notificationList = userNotificationService.getNotificationHistoryVO(user.getId());
        return notificationList;
    }

    @GetMapping(value = { "/portalApi/notificationRole/{notificationId}/roles" }, produces = "application/json")
    public List<Integer> testGetRoles(HttpServletRequest request, @PathVariable("notificationId") Long notificationId) {
        List<EpRoleNotificationItem> notifRoles = userNotificationService.getNotificationRoles(notificationId);
        ArrayList<Integer> rolesList = new ArrayList<>();
        for (EpRoleNotificationItem notifRole : notifRoles) {
            rolesList.add(notifRole.roleId);
        }
        return rolesList;
    }

    @GetMapping(value = { "/portalApi/getNotificationAppRoles" }, produces = "application/json")
    public List<EcompAppRole> getNotificationAppRoles(HttpServletRequest request, HttpServletResponse response) {
        List<EcompAppRole> epAppRoleList = null;
        try {
            epAppRoleList = userNotificationService.getAppRoleList();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                    "Exception occurred while performing UserNofiticationController.getNotificationAppRoles. Details: ",
                    e);
        }
        return epAppRoleList;
    }

    @GetMapping(value = {
            "/portalApi/getMessageRecipients" }, produces = "application/json")
    public List<String> getMessageRecipients(@RequestParam("notificationId") Long notificationID) {
        // EPUser user = EPUserUtils.getUserSession(request);
        List<String> messageUserRecipients = null;
        messageUserRecipients = userNotificationService.getMessageRecipients(notificationID);
        return messageUserRecipients;
    }

}
