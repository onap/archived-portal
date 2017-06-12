/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalapp.controller.EPUnRestrictedBaseController;
import org.openecomp.portalapp.portal.listener.HealthMonitor;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import com.google.gson.Gson;

/**
 * This controller processes requests for the health-check feature implemented
 * in the HealthMonitor, which runs in its own thread. These requests do not
 * require any authentication nor an active user session.
 */
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class HealthCheckController extends EPUnRestrictedBaseController {

	private class HealthStatus {
		public int statusCode;
		@SuppressWarnings("unused")
		public String body;

		public HealthStatus(int code, String body) {
			this.statusCode = code;
			this.body = body;
		}
	}

	private class HealthStatusInfo {
		HealthStatusInfo(String healthCheckComponent) {
			this.healthCheckComponent = healthCheckComponent;
			this.healthCheckStatus = statusUp; // Default value
			this.version = "";
			this.description = statusOk; // Default value
			this.hostName = "";
			this.ipAddress = "";
			this.dbClusterStatus = "";
			this.dbPermissions = "";
		}

		@SuppressWarnings("unused")
		public String healthCheckComponent;
		@SuppressWarnings("unused")
		public String healthCheckStatus;
		@SuppressWarnings("unused")
		public String version;
		@SuppressWarnings("unused")
		public String description;
		@SuppressWarnings("unused")
		public String hostName;
		@SuppressWarnings("unused")
		public String ipAddress;
		@SuppressWarnings("unused")
		public String dbClusterStatus;
		@SuppressWarnings("unused")
		public String dbPermissions;
	}

	private final String statusUp = "UP";
	private final String statusDown = "DOWN";
	private final String statusOk = "OK";

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HealthCheckController.class);

	@RequestMapping(value = { "/portalApi/healthCheck" }, method = RequestMethod.GET, produces = "application/json")
	public HealthStatus healthCheck(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "");

		// Return the status as 500 if it suspended due to manual fail over
		if (HealthMonitor.isSuspended) {
			healthStatus.body = "Suspended";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE,
					Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			return healthStatus;
		}

		try {
			boolean overallStatus = true;

			List<HealthStatusInfo> statusCollection = new ArrayList<HealthStatusInfo>();

			HealthStatusInfo beInfo = new HealthStatusInfo("BE");
			beInfo.hostName = EcompPortalUtils.getMyHostName();
			beInfo.ipAddress = EcompPortalUtils.getMyIpAdddress();
			if (!HealthMonitor.isBackEndUp()) {
				overallStatus = false;
				beInfo.healthCheckStatus = statusDown;
				beInfo.description = "Check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckError);
			}
			statusCollection.add(beInfo);

			HealthStatusInfo feInfo = new HealthStatusInfo("FE");
			if (!HealthMonitor.isFrontEndUp()) {
				overallStatus = false;
				feInfo.healthCheckStatus = statusDown;
				feInfo.description = "Check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.FeHealthCheckError);
			}
			statusCollection.add(feInfo);

			HealthStatusInfo dbInfo = new HealthStatusInfo("DB");
			if (!HealthMonitor.isDatabaseUp()) {
				overallStatus = false;
				dbInfo.healthCheckStatus = statusDown;
				dbInfo.description = "Check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
			}

			if (!HealthMonitor.isClusterStatusOk()) {
				dbInfo.dbClusterStatus = "Problem, check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
			} else {
				dbInfo.dbClusterStatus = statusOk;
			}

			if (!HealthMonitor.isDatabasePermissionsOk()) {
				dbInfo.dbPermissions = "Problem, check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
			} else {
				dbInfo.dbPermissions = statusOk;
			}
			statusCollection.add(dbInfo);

			HealthStatusInfo uebInfo = new HealthStatusInfo("UEB");
			if (!HealthMonitor.isUebUp()) {
				// As per test case review meeting, UEB is considered as
				// critical as DB. Hence commenting
				// overallStatus = false;
				uebInfo.healthCheckStatus = statusDown;
				uebInfo.description = "Check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebConnectionError);
			}
			statusCollection.add(uebInfo);

			String json = "";
			try {
				json = new Gson().toJson(statusCollection);
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput);
			}
			logger.info(EELFLoggerDelegate.debugLogger, json);

			if (overallStatus) {
				healthStatus = new HealthStatus(200, json);
			} else {
				healthStatus = new HealthStatus(500, json);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, Integer.toString(healthStatus.statusCode));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing the healthcheck. Details: "
							+ EcompPortalUtils.getStackTrace(e));
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheck", "GET result =", response.getStatus());

		return healthStatus;
	}

	@RequestMapping(value = {
			"/portalApi/healthCheckSuspend" }, method = RequestMethod.GET, produces = "application/json")
	public HealthStatus healthCheckSuspend(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "Suspended for manual failover mechanism");

		HealthMonitor.isSuspended = true;
		healthStatus.statusCode = 200;

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheckSuspend", "GET result =",
				response.getStatus());

		return healthStatus;
	}

	@RequestMapping(value = {
			"/portalApi/healthCheckResume" }, method = RequestMethod.GET, produces = "application/json")
	public HealthStatus healthCheckResume(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "Resumed from manual failover mechanism");

		HealthMonitor.isSuspended = false;
		healthStatus.statusCode = 200;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheckResume", "GET result =",
				response.getStatus());
		return healthStatus;
	}

	@RequestMapping(value = { "/portalApi/ping" }, method = RequestMethod.GET, produces = "application/json")
	public HealthStatus ping(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(200, "OK");
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/ping", "GET result =", response.getStatus());

		return healthStatus;
	}
}
