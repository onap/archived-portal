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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.scheduleraux.RestObject;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxResponseWrapper;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxRestInterfaceFactory;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxRestInterfaceIfc;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxUtil;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle Policy requests.
 */

@RestController
@RequestMapping(PortalConstants.PORTAL_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class SchedulerAuxController extends EPRestrictedBaseController {

	/** The logger. */
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerAuxController.class);

	@RequestMapping(value = "/get_policy", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getPolicyInfo(HttpServletRequest request) throws Exception {
		try {

			logger.debug(EELFLoggerDelegate.debugLogger,
					"SchedulerAux Controller Call Started: " + SchedulerProperties.SCHEDULERAUX_GET_CONFIG_VAL);
			String path = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_GET_CONFIG_VAL);
			SchedulerAuxResponseWrapper policyResWrapper = getPolicyConfig(path);

			logger.debug(EELFLoggerDelegate.debugLogger, "SchedulerAux Request END : Response: ",
					new ResponseEntity<String>(policyResWrapper.getResponse(), HttpStatus.OK).toString());

			return (new ResponseEntity<String>(policyResWrapper.getResponse(),
					HttpStatus.valueOf(policyResWrapper.getStatus())));
		} catch (Exception e) {
			SchedulerAuxResponseWrapper policyResWrapper = new SchedulerAuxResponseWrapper();
			policyResWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			policyResWrapper.setEntity(e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, "Exception with getpolicy ", e);
			return (new ResponseEntity<String>(policyResWrapper.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	protected static SchedulerAuxResponseWrapper getPolicyConfig(String path) throws Exception {
		String methodName = "getPolicyConfig";
		String uuid = UUID.randomUUID().toString();
		logger.debug(EELFLoggerDelegate.debugLogger, "starting getPolicyConfig ");

		try {
			// STARTING REST API CALL AS AN FACTORY INSTACE
			SchedulerAuxRestInterfaceIfc policyRestController = SchedulerAuxRestInterfaceFactory.getInstance();
			JSONObject request = new JSONObject();
			String policyName = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_POLICY_NAME);
			request.put("policyName", policyName);
			RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);
			policyRestController.<String>Post(str, request, uuid, path, restObjStr);
			SchedulerAuxResponseWrapper policyRespWrapper = SchedulerAuxUtil.wrapResponse(restObjStr);
			logger.debug(EELFLoggerDelegate.debugLogger, "Getpolicy Request END : Response: ", methodName,
					policyRespWrapper.getResponse());
			if (policyRespWrapper.getStatus() != 200) {
				String message = String.format(
						" get policy Information failed  . MethodName: %s, PolicyRespWrapperResponse: %s", methodName,
						policyRespWrapper.getResponse());
				logger.error(EELFLoggerDelegate.errorLogger, message);
				EPLogUtil.schedulerAccessAlarm(logger, policyRespWrapper.getStatus());
			}
			return policyRespWrapper;
		} catch (Exception e) {
			String message = String.format(" EXCEPTION in getPolicyConfig  . MethodName: %s and Exception:  %s", methodName, e);
			logger.error(EELFLoggerDelegate.errorLogger, "EXCEPTION in getPolicyConfig", message);
			throw e;
		}
	}
}
