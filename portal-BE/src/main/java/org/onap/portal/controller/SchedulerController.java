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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.scheduler.SchedulerProperties;
import org.onap.portal.scheduler.SchedulerRestInterface;
import org.onap.portal.scheduler.SchedulerUtil;
import org.onap.portal.scheduler.restobjects.GetTimeSlotsRestObject;
import org.onap.portal.scheduler.restobjects.PostCreateNewVnfRestObject;
import org.onap.portal.scheduler.restobjects.PostSubmitVnfChangeRestObject;
import org.onap.portal.scheduler.wrapper.GetTimeSlotsWrapper;
import org.onap.portal.scheduler.wrapper.PostCreateNewVnfWrapper;
import org.onap.portal.scheduler.wrapper.PostSubmitVnfChangeTimeSlotsWrapper;
import org.onap.portal.service.AdminRolesService;
import org.onap.portal.utils.EPUserUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@EPAuditLog
@RestController
@EnableAspectJAutoProxy
@RequestMapping(PortalConstants.PORTAL_AUX_API)
public class SchedulerController {

    private static final String USER_IS_UNAUTHORIZED_TO_MAKE_THIS_CALL = "User is unauthorized to make this call";

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerController.class);
    private static final DateFormat requestDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss z");

    private SchedulerRestInterface schedulerRestController;
    private AdminRolesService adminRolesService;

    @Autowired
    public SchedulerController(
        final SchedulerRestInterface schedulerRestController,
        final AdminRolesService adminRolesService) {
        this.schedulerRestController = schedulerRestController;
        this.adminRolesService = adminRolesService;
    }

    @RequestMapping(value = "/get_time_slots/{scheduler_request}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getTimeSlots(HttpServletRequest request,
        @PathVariable("scheduler_request") String schedulerRequest) throws Exception {
        if (checkIfUserISValidToMakeSchedule(request)) {
            try {
                Date startingTime = new Date();
                String startTimeRequest = requestDateFormat.format(startingTime);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "Controller Scheduler GET Timeslots for startTimeRequest: ", startTimeRequest);
                logger.debug(EELFLoggerDelegate.debugLogger, "Original Request = {} ", schedulerRequest);

                String path = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_GET_TIME_SLOTS)
                    + schedulerRequest;

                GetTimeSlotsWrapper schedulerResWrapper = getTimeSlots(path, schedulerRequest);

                Date endTime = new Date();
                String endTimeRequest = requestDateFormat.format(endTime);
                logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler - GET for EndTimeRequest = {}",
                    endTimeRequest);
                return (new ResponseEntity<>(schedulerResWrapper.getResponse(),
                    HttpStatus.valueOf(schedulerResWrapper.getStatus())));
            } catch (Exception e) {
                GetTimeSlotsWrapper schedulerResWrapper = new GetTimeSlotsWrapper();
                schedulerResWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                schedulerResWrapper.setEntity(e.getMessage());
                logger.error(EELFLoggerDelegate.errorLogger, "Exception with getTimeslots", e);
                return (new ResponseEntity<>(schedulerResWrapper.getResponse(),
                    HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } else {
            return (new ResponseEntity<>(USER_IS_UNAUTHORIZED_TO_MAKE_THIS_CALL, HttpStatus.UNAUTHORIZED));
        }
    }

    @RequestMapping(value = "/post_create_new_vnf_change", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> postCreateNewVNFChange(HttpServletRequest request,
        @RequestBody JSONObject schedulerRequest) throws Exception {
        if (checkIfUserISValidToMakeSchedule(request)) {
            try {
                Date startingTime = new Date();
                String startTimeRequest = requestDateFormat.format(startingTime);

                logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler POST : post_create_new_vnf_change",
                    startTimeRequest);

                // Generating uuid
                String uuid = UUID.randomUUID().toString();

                schedulerRequest.put("scheduleId", uuid);
                logger.debug(EELFLoggerDelegate.debugLogger, "UUID = {} ", uuid);

                // adding uuid to the request payload
                schedulerRequest.put("scheduleId", uuid);
                logger.debug(EELFLoggerDelegate.debugLogger, "Original Request = {}", schedulerRequest.toString());

                String path = SchedulerProperties
                    .getProperty(SchedulerProperties.SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL) + uuid;

                PostCreateNewVnfWrapper responseWrapper = postSchedulingRequest(schedulerRequest, path, uuid);

                Date endTime = new Date();
                String endTimeRequest = requestDateFormat.format(endTime);
                logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler - POST= {}", endTimeRequest);

                return new ResponseEntity<>(responseWrapper.getResponse(),
                    HttpStatus.valueOf(responseWrapper.getStatus()));
            } catch (Exception e) {
                PostCreateNewVnfWrapper responseWrapper = new PostCreateNewVnfWrapper();
                responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseWrapper.setEntity(e.getMessage());
                logger.error(EELFLoggerDelegate.errorLogger, "Exception with postCreateNewVNFChange ", e);
                return (new ResponseEntity<>(responseWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));

            }
        } else {
            return (new ResponseEntity<>(USER_IS_UNAUTHORIZED_TO_MAKE_THIS_CALL, HttpStatus.UNAUTHORIZED));
        }
    }

    @RequestMapping(value = "/submit_vnf_change_timeslots", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> postSubmitVnfChangeTimeslots(HttpServletRequest request,
        @RequestBody JSONObject schedulerRequest) throws Exception {
        if (checkIfUserISValidToMakeSchedule(request)) {
            try {
                Date startingTime = new Date();
                String startTimeRequest = requestDateFormat.format(startingTime);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    " Controller Scheduler POST : submit_vnf_change_timeslots = {}",
                    startTimeRequest);

                // Generating uuid
                String uuid = (String) schedulerRequest.get("scheduleId");
                logger.debug(EELFLoggerDelegate.debugLogger, "UUID = {} ", uuid);

                schedulerRequest.remove("scheduleId");
                logger.debug(EELFLoggerDelegate.debugLogger, "Original Request for the schedulerId= {} ",
                    schedulerRequest.toString());

                String path = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_SUBMIT_NEW_VNF_CHANGE)
                    .replace("{scheduleId}", uuid);

                PostSubmitVnfChangeTimeSlotsWrapper responseWrapper = postSubmitSchedulingRequest(schedulerRequest,
                    path,
                    uuid);

                Date endTime = new Date();
                String endTimeRequest = requestDateFormat.format(endTime);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    " Controller Scheduler - POST Submit for end time request= {}",
                    endTimeRequest);

                return (new ResponseEntity<>(responseWrapper.getResponse(),
                    HttpStatus.valueOf(responseWrapper.getStatus())));
            } catch (Exception e) {
                PostSubmitVnfChangeTimeSlotsWrapper responseWrapper = new PostSubmitVnfChangeTimeSlotsWrapper();
                responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseWrapper.setEntity(e.getMessage());
                logger.error(EELFLoggerDelegate.errorLogger, "Exception with Post submit Vnf change Timeslots", e);
                return (new ResponseEntity<>(responseWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));

            }
        } else {
            return (new ResponseEntity<>(USER_IS_UNAUTHORIZED_TO_MAKE_THIS_CALL, HttpStatus.UNAUTHORIZED));
        }
    }

    @RequestMapping(value = "/get_scheduler_constant", method = RequestMethod.GET, produces = "application/json")
    public PortalRestResponse<Map<String, String>> getSchedulerConstant(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        logger.debug(EELFLoggerDelegate.debugLogger, "get scheduler constant");

        PortalRestResponse<Map<String, String>> portalRestResponse;

        if (checkIfUserISValidToMakeSchedule(request)) {
            String errorMsg = " is not defined in property file. Please check the property file and make sure all the schedule constant values are defined";
            HashMap<String, String> constantMap = new HashMap<>();
            constantMap.put(SchedulerProperties.SCHEDULER_DOMAIN_NAME, "domainName");
            constantMap.put(SchedulerProperties.SCHEDULER_SCHEDULE_NAME, "scheduleName");
            constantMap.put(SchedulerProperties.SCHEDULER_WORKFLOW_NAME, "workflowName");
            constantMap.put(SchedulerProperties.SCHEDULER_CALLBACK_URL, "callbackUrl");
            constantMap.put(SchedulerProperties.SCHEDULER_APPROVAL_TYPE, "approvalType");
            constantMap.put(SchedulerProperties.SCHEDULER_APPROVAL_SUBMIT_STATUS, "approvalSubmitStatus");
            constantMap.put(SchedulerProperties.SCHEDULER_APPROVAL_REJECT_STATUS, "approvalRejectStatus");
            constantMap.put(SchedulerProperties.SCHEDULER_POLICY_NAME, "policyName");
            constantMap.put(SchedulerProperties.SCHEDULER_INTERVAL_GET_TIMESLOT_RATE, "intervalRate");
            constantMap.put(SchedulerProperties.SCHEDULER_GROUP_ID, "groupId");
            try {
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : constantMap.entrySet()) {
                    if (SchedulerProperties.containsProperty(entry.getKey())) {
                        map.put(entry.getValue(), SchedulerProperties.getProperty(entry.getKey()));
                    } else {
                        throw new Exception(entry.getKey() + errorMsg);
                    }
                }
                logger.debug(EELFLoggerDelegate.debugLogger, " portalRestResponse - getSchedulerConstant= {}", map);
                portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
                    map);

            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "getSchedulerConstant failed", e);
                portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), null);
            }

        } else {
            logger.error(EELFLoggerDelegate.errorLogger,
                "getSchedulerConstant failed: User unauthorized to make this call");
            portalRestResponse = new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "failed : Unauthorized", null);
        }
        return portalRestResponse;
    }

    private GetTimeSlotsWrapper getTimeSlots(String path, String uuid) throws Exception {

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "Get Time Slots Request START");

            GetTimeSlotsRestObject<String> restObjStr = new GetTimeSlotsRestObject<>();
            String str = "";

            restObjStr.setT(str);

            schedulerRestController.get(str, uuid, path, restObjStr);
            GetTimeSlotsWrapper schedulerRespWrapper = SchedulerUtil.getTimeSlotsWrapResponse(restObjStr);
            logger.debug(EELFLoggerDelegate.debugLogger, "Get Time Slots Request END : Response: {}",
                schedulerRespWrapper.getResponse());
            if (schedulerRespWrapper.getStatus() != 200 && schedulerRespWrapper.getStatus() != 204
                && schedulerRespWrapper.getStatus() != 202) {
                String message = String.format(
                    " getTimeslots Information failed . SchedulerResponseWrapper for gettimeslots: {}",
                    schedulerRespWrapper.getResponse());
                logger.error(EELFLoggerDelegate.errorLogger, message);
                EPLogUtil.schedulerAccessAlarm(logger, schedulerRespWrapper.getStatus());

            }
            return schedulerRespWrapper;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "Get Time Slots Request ERROR : Exception:", e);
            throw e;
        }
    }

    private PostCreateNewVnfWrapper postSchedulingRequest(JSONObject request, String path, String uuid)
        throws Exception {

        try {
					PostCreateNewVnfRestObject<String> restObjStr = new PostCreateNewVnfRestObject<>();
            String str = "";

            restObjStr.setT(str);
            schedulerRestController.post(str, request, path, restObjStr);

            int status = restObjStr.getStatusCode();
            if (status >= 200 && status <= 299) {
                restObjStr.setUuid(uuid);
            }

            PostCreateNewVnfWrapper responseWrapper = SchedulerUtil.postCreateNewVnfWrapResponse(restObjStr);

            logger.debug(EELFLoggerDelegate.debugLogger, " Post Create New Vnf Scheduling Request END : Response = {}",
                responseWrapper.getResponse());
            if (responseWrapper.getStatus() != 200 && responseWrapper.getStatus() != 202
                && responseWrapper.getStatus() != 204) {
                logger.error(EELFLoggerDelegate.errorLogger, "PostCreateNewVnfWrapper Information failed",
                    responseWrapper.getResponse());
                EPLogUtil.schedulerAccessAlarm(logger, responseWrapper.getStatus());

            }
            return responseWrapper;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "PostCreateNewVnfWrapper failed . Post Create New Vnf Scheduling Request ERROR :", e);
            throw e;
        }
    }

    private PostSubmitVnfChangeTimeSlotsWrapper postSubmitSchedulingRequest(JSONObject request, String path,
        String uuid) throws Exception {

        try {
            PostSubmitVnfChangeRestObject<String> restObjStr = new PostSubmitVnfChangeRestObject<>();
            String str = "";

            restObjStr.setT(str);
            schedulerRestController.post(str, request, path, restObjStr);

            int status = restObjStr.getStatusCode();
            if (status >= 200 && status <= 299) {
                status = (status == 204) ? 200 : status;
                restObjStr.setStatusCode(status);
                restObjStr.setUuid(uuid);
            }

            PostSubmitVnfChangeTimeSlotsWrapper responseWrapper = SchedulerUtil
                .postSubmitNewVnfWrapResponse(restObjStr);
            logger.debug(EELFLoggerDelegate.debugLogger, "Post Submit Scheduling Request END : Response = {}",
                responseWrapper.getResponse());
            if (responseWrapper.getStatus() != 200 && responseWrapper.getStatus() != 202
                && responseWrapper.getStatus() != 204) {
                logger.error(EELFLoggerDelegate.errorLogger, "PostCreateNewVnfWrapper Information failed",
                    responseWrapper.getResponse());
                EPLogUtil.schedulerAccessAlarm(logger, responseWrapper.getStatus());

            }
            return responseWrapper;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                " PostCreateNewVnfWrapper failed . Post Submit Scheduling Request ERROR :", e);
            throw e;
        }
    }

    private String getPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String portalApiPath = "";
        if (requestURI != null) {
            String[] uriArray = requestURI.split("/portalApi/");
            if (uriArray.length > 1) {
                portalApiPath = uriArray[1];
            }
        }
        return portalApiPath;
    }

    private boolean checkIfUserISValidToMakeSchedule(HttpServletRequest request) throws Exception {
        FnUser user = EPUserUtils.getUserSession(request);
        String portalApiPath = getPath(request);
        Set<String> functionCodeList = adminRolesService.getAllAppsFunctionsOfUser(user.getId().toString());
        return EPUserUtils.matchRoleFunctions(portalApiPath, functionCodeList);
    }
}
