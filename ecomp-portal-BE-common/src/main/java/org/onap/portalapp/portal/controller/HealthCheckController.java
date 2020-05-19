/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modification Copyright © 2020 IBM.
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.onap.music.main.MusicUtil;
import org.onap.portalapp.controller.EPUnRestrictedBaseController;
import org.onap.portalapp.portal.listener.HealthMonitor;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

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

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HealthCheckController.class);

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

	@GetMapping(value = { "/portalApi/healthCheck" }, produces = "application/json")
	public HealthStatus healthCheck(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "");

		// Return the status as 500 if it suspended due to manual fail over
		if (HealthMonitor.isSuspended()) {
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

//			if (!HealthMonitor.isClusterStatusOk()) {
//				dbInfo.dbClusterStatus = "Problem, check the logs for more details";
//				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
//			} else {
//				dbInfo.dbClusterStatus = statusOk;
//			}

			if (!HealthMonitor.isDbPermissionsOk()) {
				dbInfo.dbPermissions = "Problem, check the logs for more details";
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError);
			} else {
				dbInfo.dbPermissions = statusOk;
			}
			statusCollection.add(dbInfo);

			if(org.onap.portalapp.music.util.MusicUtil.isMusicEnable()){
				HealthStatusInfo CassandraStatusInfo = new HealthStatusInfo("Music-Cassandra");
				//CassandraStatusInfo.hostName = EcompPortalUtils.getMyHostName();
				CassandraStatusInfo.ipAddress = MusicUtil.getMyCassaHost();
				
				if (!HealthMonitor.isCassandraStatusOk()) {
					overallStatus = false;
					CassandraStatusInfo.healthCheckStatus = statusDown;
					CassandraStatusInfo.description = "Check the logs for more details";
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.MusicHealthCheckCassandraError);
				}
				statusCollection.add(CassandraStatusInfo);

				/*
				 * HealthStatusInfo zookeeperStatusInfo = new
				 * HealthStatusInfo("Music-zookeeper"); --zookeeperStatusInfo.hostName =
				 * EcompPortalUtils.getMyHostName(); zookeeperStatusInfo.ipAddress =
				 * MusicUtil.getMyZkHost(); if (!HealthMonitor.isZookeeperStatusOk()) {
				 * overallStatus = false; zookeeperStatusInfo.healthCheckStatus = statusDown;
				 * zookeeperStatusInfo.description = "Check the logs for more details";
				 * EPLogUtil.logEcompError(logger,
				 * EPAppMessagesEnum.MusicHealthCheckZookeeperError); }
				 * statusCollection.add(zookeeperStatusInfo);
				 */
			}

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
			logger.error(EELFLoggerDelegate.errorLogger,	"healthCheck failed", e);
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheck", "GET result =", response.getStatus());

		return healthStatus;
	}

	@GetMapping(value = {
			"/portalApi/healthCheckSuspend" }, produces = "application/json")
	public HealthStatus healthCheckSuspend(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "Suspended for manual failover mechanism");

		HealthMonitor.setSuspended(true);
		healthStatus.statusCode = 200;

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheckSuspend", "GET result =",
				response.getStatus());

		return healthStatus;
	}

	@GetMapping(value = {
			"/portalApi/healthCheckResume" }, produces = "application/json")
	public HealthStatus healthCheckResume(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(500, "Resumed from manual failover mechanism");

		HealthMonitor.setSuspended(false);
		healthStatus.statusCode = 200;
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/healthCheckResume", "GET result =",
				response.getStatus());
		return healthStatus;
	}

	@GetMapping(value = { "/portalApi/ping" }, produces = "application/json")
	public HealthStatus ping(HttpServletRequest request, HttpServletResponse response) {
		HealthStatus healthStatus = new HealthStatus(200, "OK");
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/ping", "GET result =", response.getStatus());

		return healthStatus;
	}
}
