/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.controller;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.openecomp.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EcompAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.service.AppsCacheService;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.transport.Analytics;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class WebAnalyticsExtAppController extends EPRestrictedRESTfulBaseController {

	@Autowired
	private ConsulHealthService consulHealthService;

	private static final String MACHINE_LEARNING_SERVICE_CTX = "/ml_api";
	private static final String REGISTER_ACTION = MACHINE_LEARNING_SERVICE_CTX + "/" + "registerAction";
	private static final String CONSUL_ML_SERVICE_ID = "machine-learning";
	private static final String APP_KEY = "uebkey";
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WebAnalyticsExtAppController.class);
	private AsyncRestTemplate restTemplate = new AsyncRestTemplate();


	@Autowired
	AuditService auditService;

	@Autowired
	AppsCacheService appCacheService;

	SuccessCallback<ResponseEntity<String>> successCallback = new SuccessCallback<ResponseEntity<String>>() {
		@Override
		public void onSuccess(ResponseEntity<String> arg) {
			logger.info(EELFLoggerDelegate.debugLogger, arg.getBody());
		}
	};

	FailureCallback failureCallback = new FailureCallback() {
		@Override
		public void onFailure(Throwable arg) {
			logger.error(EELFLoggerDelegate.errorLogger, "storeAuxAnalytics failed", arg);
		}
	};

	protected boolean isAuxRESTfulCall() {
		return true;
	}

	/**
	 * Answers requests from partner applications for a file that is expected to
	 * contain javascript to support web analytics.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws Exception
	 *             on failure
	 */
	@ApiOperation(value = "Gets javascript with functions that support gathering and reporting web analytics.", response = String.class)
	@RequestMapping(value = { "/analytics" }, method = RequestMethod.GET, produces = "application/javascript")
	public String getAnalyticsScript(HttpServletRequest request) throws Exception {
		String responseText = "";
		final String fileName = "analytics.txt";
		InputStream analyticsFileStream = null;
		try {
			analyticsFileStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
			responseText = IOUtils.toString(analyticsFileStream, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Error reading contents of the file " + fileName, e);
		} finally {
			if (analyticsFileStream != null)
				analyticsFileStream.close();
		}

		String feURLContext = SystemProperties.getProperty("frontend_url");
		String feURL = feURLContext.substring(0, feURLContext.lastIndexOf('/'));

		responseText = responseText.replace("PORTAL_ENV_URL", feURL);
		return responseText;
	}

	/**
	 * Accepts data from partner applications with web analytics data.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param analyticsMap
	 *            Analytics
	 * @return PortalAPIResponse
	 * @throws Exception
	 *             on failure
	 */
	@RequestMapping(value = { "/storeAnalytics" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Accepts data from partner applications with web analytics data.", response = PortalAPIResponse.class)
	public PortalAPIResponse storeAnalyticsScript(HttpServletRequest request, @RequestBody Analytics analyticsMap)
			throws Exception {
		try {
			MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
			String appName = "";
			try {
				appName = getAppName(request, appName);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						" Error retrieving Application to capture app name for analytics; Proceeding with empty app name");
			}

			try {
				storeAuxAnalytics(analyticsMap, appName);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						" Error retrieving Application to capture app name for analytics; Proceeding with empty app name");
			}

			MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());

			EcompPortalUtils.calculateDateTimeDifferenceForLog(
					MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
					MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
			logger.info(EELFLoggerDelegate.auditLogger,
					EPLogUtil.formatStoreAnalyticsAuditLogMessage(analyticsMap.getUserid(), appName,
							"WebAnalyticsExtAppController.postWebAnalyticsData",
							EcompAuditLog.CD_ACTIVITY_STORE_ANALYTICS, analyticsMap.getAction(), analyticsMap.getPage(),
							analyticsMap.getFunction(), analyticsMap.getType()));

			MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
			MDC.remove(SystemProperties.MDC_TIMER);

			PortalAPIResponse response = new PortalAPIResponse(true, "success");
			return response;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "storeAnalytics failed", e);
			PortalAPIResponse response = new PortalAPIResponse(true, "error");
			return response;
		}
	}

	protected String getAppName(HttpServletRequest request, String appName) {
		String appKeyValue = request.getHeader(APP_KEY);
		if (appKeyValue == null || appKeyValue.equals("")) {
			logger.error(EELFLoggerDelegate.errorLogger, " App Key unavailable; Proceeding with null app name");
		} else {
			EPApp appRecord = appCacheService.getAppForAnalytics(appKeyValue);
			if (appRecord == null) {
				logger.error(EELFLoggerDelegate.errorLogger, " App could not be found for the key " + appKeyValue);
			} else
				appName = appRecord.getName();

		}
		return appName;
	}

	protected void storeAuxAnalytics(Analytics analyticsMap, String appName) {
		logger.info(EELFLoggerDelegate.debugLogger,
				" Registering an action for recommendation: AppName/Function/UserId " + appName + "/"
						+ analyticsMap.getFunction() + "/" + analyticsMap.getUserid());

		Map<String, String> requestMapping = new HashMap<String, String>();
		requestMapping.put("id", analyticsMap.getUserid());
		requestMapping.put("action", appName + "|" + analyticsMap.getFunction());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set your entity to send
		HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(requestMapping, headers);

		// send it!
		ListenableFuture<ResponseEntity<String>> out = restTemplate.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ consulHealthService.getServiceLocation(CONSUL_ML_SERVICE_ID,
								SystemProperties.getProperty("microservices.m-learn.local.port"))
						+ REGISTER_ACTION,
				HttpMethod.POST, entity, String.class);
		out.addCallback(successCallback, failureCallback);
	}

}
