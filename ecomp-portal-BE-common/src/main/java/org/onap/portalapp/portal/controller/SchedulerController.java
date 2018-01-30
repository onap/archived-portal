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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.scheduler.SchedulerRestInterface;
import org.onap.portalapp.portal.scheduler.SchedulerUtil;
import org.onap.portalapp.portal.scheduler.restobjects.GetTimeSlotsRestObject;
import org.onap.portalapp.portal.scheduler.restobjects.PostCreateNewVnfRestObject;
import org.onap.portalapp.portal.scheduler.restobjects.PostSubmitVnfChangeRestObject;
import org.onap.portalapp.portal.scheduler.wrapper.GetTimeSlotsWrapper;
import org.onap.portalapp.portal.scheduler.wrapper.PostCreateNewVnfWrapper;
import org.onap.portalapp.portal.scheduler.wrapper.PostSubmitVnfChangeTimeSlotsWrapper;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PortalConstants.PORTAL_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class SchedulerController extends EPRestrictedBaseController {

	@Autowired
	private SchedulerRestInterface schedulerRestController;

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerController.class);

	/** The request date format. */
	public DateFormat requestDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss z");

	@RequestMapping(value = "/get_time_slots/{scheduler_request}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getTimeSlots(HttpServletRequest request,
			@PathVariable("scheduler_request") String scheduler_request) throws Exception {
		try {

			Date startingTime = new Date();
			String startTimeRequest = requestDateFormat.format(startingTime);
			logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler GET Timeslots for startTimeRequest: ",
					startTimeRequest);
			logger.debug(EELFLoggerDelegate.debugLogger, "Original Request : \n ", scheduler_request);
			String path = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_GET_TIME_SLOTS)
					+ scheduler_request;

			GetTimeSlotsWrapper schedulerResWrapper = getTimeSlots(scheduler_request, path, scheduler_request);

			Date endTime = new Date();
			String endTimeRequest = requestDateFormat.format(endTime);
			logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler - GET for EndTimeRequest",
					endTimeRequest);
			return (new ResponseEntity<String>(schedulerResWrapper.getResponse(),
					HttpStatus.valueOf(schedulerResWrapper.getStatus())));
		} catch (Exception e) {
			GetTimeSlotsWrapper schedulerResWrapper=new GetTimeSlotsWrapper();
			schedulerResWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			schedulerResWrapper.setEntity(e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, "Exception with getTimeslots", e);
			return (new ResponseEntity<String>(schedulerResWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));
		}

	}

	protected GetTimeSlotsWrapper getTimeSlots(String request, String path, String uuid) throws Exception {

		try {
			// STARTING REST API CALL AS AN FACTORY INSTACE
			logger.debug(EELFLoggerDelegate.debugLogger, "Get Time Slots Request START");

			GetTimeSlotsRestObject<String> restObjStr = new GetTimeSlotsRestObject<String>();
			String str = new String();

			restObjStr.set(str);

			schedulerRestController.Get(str, uuid, path, restObjStr);
			GetTimeSlotsWrapper schedulerRespWrapper = SchedulerUtil.getTimeSlotsWrapResponse(restObjStr);
			logger.debug(EELFLoggerDelegate.debugLogger, "Get Time Slots Request END : Response: ",
					schedulerRespWrapper.getResponse());
			if (schedulerRespWrapper.getStatus() != 200 && schedulerRespWrapper.getStatus() != 204
					&& schedulerRespWrapper.getStatus() != 202) {
				String message = String.format(
						" getTimeslots Information failed . SchedulerResponseWrapper for gettimeslots: %s", schedulerRespWrapper.getResponse());
				logger.error(EELFLoggerDelegate.errorLogger, message);
				EPLogUtil.schedulerAccessAlarm(logger, schedulerRespWrapper.getStatus());

			}
			return schedulerRespWrapper;

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,  "Get Time Slots Request ERROR : Exception:",e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/post_create_new_vnf_change", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> postCreateNewVNFChange(HttpServletRequest request,
			@RequestBody JSONObject scheduler_request) throws Exception {
		try {
			Date startingTime = new Date();
			String startTimeRequest = requestDateFormat.format(startingTime);

			logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler POST : post_create_new_vnf_change",
					startTimeRequest);

			// Generating uuid
			String uuid = UUID.randomUUID().toString();

			scheduler_request.put("scheduleId", uuid);
			logger.debug(EELFLoggerDelegate.debugLogger, "UUID : ", uuid);

			// adding uuid to the request payload
			scheduler_request.put("scheduleId", uuid);
			logger.debug(EELFLoggerDelegate.debugLogger, "Original Request ", scheduler_request.toString());

			String path = SchedulerProperties
					.getProperty(SchedulerProperties.SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL) + uuid;

			PostCreateNewVnfWrapper responseWrapper = postSchedulingRequest(scheduler_request, path, uuid);

			Date endTime = new Date();
			String endTimeRequest = requestDateFormat.format(endTime);
			logger.debug(EELFLoggerDelegate.debugLogger, "Controller Scheduler - POST", endTimeRequest);

			return new ResponseEntity<String>(responseWrapper.getResponse(),
					HttpStatus.valueOf(responseWrapper.getStatus()));
		} catch (Exception e) {
			PostCreateNewVnfWrapper responseWrapper=new PostCreateNewVnfWrapper();
			responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseWrapper.setEntity(e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, "Exception with postCreateNewVNFChange ", e);
			return (new ResponseEntity<String>(responseWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));

		}

	}

	protected PostCreateNewVnfWrapper postSchedulingRequest(JSONObject request, String path, String uuid)
			throws Exception {

		try {
			// STARTING REST API CALL AS AN FACTORY INSTACE

			PostCreateNewVnfRestObject<String> restObjStr = new PostCreateNewVnfRestObject<String>();
			String str = new String();

			restObjStr.set(str);
			schedulerRestController.<String>Post(str, request, path, restObjStr);

			int status = restObjStr.getStatusCode();
			if (status >= 200 && status <= 299) {
				restObjStr.setUUID(uuid);
			}

			PostCreateNewVnfWrapper responseWrapper = SchedulerUtil.postCreateNewVnfWrapResponse(restObjStr);

			logger.debug(EELFLoggerDelegate.debugLogger, " Post Create New Vnf Scheduling Request END : Response: ",
					responseWrapper.getResponse());
			if (responseWrapper.getStatus() != 200 && responseWrapper.getStatus() != 202 && responseWrapper.getStatus() != 204) {
				logger.error(EELFLoggerDelegate.errorLogger, "PostCreateNewVnfWrapper Information failed", responseWrapper.getResponse());
				EPLogUtil.schedulerAccessAlarm(logger, responseWrapper.getStatus());

			}
			return responseWrapper;

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "PostCreateNewVnfWrapper failed . Post Create New Vnf Scheduling Request ERROR :",e);
			throw e;
		}
	}

	@RequestMapping(value = "/submit_vnf_change_timeslots", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> postSubmitVnfChangeTimeslots(HttpServletRequest request,
			@RequestBody JSONObject scheduler_request) throws Exception {
		try {
			Date startingTime = new Date();
			String startTimeRequest = requestDateFormat.format(startingTime);
			logger.debug(EELFLoggerDelegate.debugLogger, " Controller Scheduler POST : submit_vnf_change_timeslots",
					startTimeRequest);

			// Generating uuid
			String uuid = (String) scheduler_request.get("scheduleId");
			logger.debug(EELFLoggerDelegate.debugLogger, "UUID : ", uuid);

			scheduler_request.remove("scheduleId");
			logger.debug(EELFLoggerDelegate.debugLogger, "Original Request for the schedulerId: ",
					scheduler_request.toString());

			String path = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_SUBMIT_NEW_VNF_CHANGE)
					.replace("{scheduleId}", uuid);

			PostSubmitVnfChangeTimeSlotsWrapper responseWrapper = postSubmitSchedulingRequest(scheduler_request, path,
					uuid);

			Date endTime = new Date();
			String endTimeRequest = requestDateFormat.format(endTime);
			logger.debug(EELFLoggerDelegate.debugLogger, " Controller Scheduler - POST Submit for end time request",
					endTimeRequest);

			return (new ResponseEntity<String>(responseWrapper.getResponse(),HttpStatus.valueOf(responseWrapper.getStatus())));
		} catch (Exception e) {
			PostSubmitVnfChangeTimeSlotsWrapper responseWrapper=new PostSubmitVnfChangeTimeSlotsWrapper();
			responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseWrapper.setEntity(e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, "Exception with Post submit Vnf change Timeslots", e);
			return (new ResponseEntity<String>(responseWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));

		}
	}

	protected PostSubmitVnfChangeTimeSlotsWrapper postSubmitSchedulingRequest(JSONObject request, String path,
			String uuid) throws Exception {

		try {
			// STARTING REST API CALL AS AN FACTORY INSTACE

			PostSubmitVnfChangeRestObject<String> restObjStr = new PostSubmitVnfChangeRestObject<String>();
			String str = new String();

			restObjStr.set(str);
			schedulerRestController.<String>Post(str, request, path, restObjStr);

			int status = restObjStr.getStatusCode();
			if (status >= 200 && status <= 299) {
				status=(status==204)?200:status;
				restObjStr.setStatusCode(status);
				restObjStr.setUUID(uuid);
			}

			PostSubmitVnfChangeTimeSlotsWrapper responseWrapper = SchedulerUtil
					.postSubmitNewVnfWrapResponse(restObjStr);
			logger.debug(EELFLoggerDelegate.debugLogger, "Post Submit Scheduling Request END : Response = ",
					responseWrapper.getResponse());
			if (responseWrapper.getStatus() != 200 && responseWrapper.getStatus() != 202
					&& responseWrapper.getStatus() != 204) {
				logger.error(EELFLoggerDelegate.errorLogger, "PostCreateNewVnfWrapper Information failed", responseWrapper.getResponse());
				EPLogUtil.schedulerAccessAlarm(logger, responseWrapper.getStatus());

			}
			return responseWrapper;

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, " PostCreateNewVnfWrapper failed . Post Submit Scheduling Request ERROR :",e);
			throw e;
		}
	}

	/**
	 * Get Scheduler UI constant values from properties file
	 * 
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@RequestMapping(value = "/get_scheduler_constant", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<Map<String, String>> getSchedulerConstant(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug(EELFLoggerDelegate.debugLogger, "get scheduler constant");

		PortalRestResponse<Map<String, String>> portalRestResponse = null;
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
				if (SchedulerProperties.containsProperty(entry.getKey()))
					map.put(entry.getValue(), SchedulerProperties.getProperty(entry.getKey()));
				else
					throw new Exception(entry.getKey() + errorMsg);
			}
			portalRestResponse = new PortalRestResponse<Map<String, String>>(PortalRestStatusEnum.OK, "success", map);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getSchedulerConstant failed", e);
			portalRestResponse = new PortalRestResponse<Map<String, String>>(PortalRestStatusEnum.ERROR, e.getMessage(),
					null);
		}
		return portalRestResponse;
	}

}
