/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2020 IBM
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

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.onap.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EcompAuditLog;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.WidgetMService;
import org.onap.portalapp.portal.transport.Analytics;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.onap.portalsdk.core.util.SystemProperties;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
@NoArgsConstructor
public class WebAnalyticsExtAppController extends EPRestrictedRESTfulBaseController {
	public static final String FEED_ML = "feed.ml";
	private WidgetMService widgetMService;
	private	AppsCacheService appCacheService;

	private static final String MACHINE_LEARNING_SERVICE_CTX = "/ml_api";
	private static final String REGISTER_ACTION = MACHINE_LEARNING_SERVICE_CTX + "/" + "registerAction";
	private static final String APP_KEY = "uebkey";
	private static final String ERROR_MSG = " Error retrieving Application to capture app name for analytics; Proceeding with empty app name";
	private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WebAnalyticsExtAppController.class);
	private final AsyncRestTemplate restTemplate = new AsyncRestTemplate();
	private final SuccessCallback<ResponseEntity<String>> successCallback = arg -> logger.info(EELFLoggerDelegate.debugLogger, arg.getBody());
	private final FailureCallback failureCallback = arg -> logger.error(EELFLoggerDelegate.errorLogger, "storeAuxAnalytics failed", arg);

	@Autowired
	public WebAnalyticsExtAppController(AppsCacheService appCacheService, WidgetMService consulHealthService) {
		this.appCacheService = appCacheService;
		this.widgetMService = consulHealthService;
	}

	/**
	 * Answers requests from partner applications for a file that is expected to
	 * contain javascript to support web analytics.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@ApiOperation(value = "Gets javascript with functions that support gathering and reporting web analytics.", response = String.class)
	@GetMapping(value = { "/analytics" }, produces = "application/javascript")
	public String getAnalyticsScript(HttpServletRequest request) {
		String responseText = "";
		EPApp app = null;
		String version = "";
		try {
			app = getApp(request);
		} catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                    ERROR_MSG, e);
		}
		if (app != null) {
			String restEndPoint = app.getAppRestEndpoint();
			if(restEndPoint.indexOf("/api")!=-1) {
				version = restEndPoint.substring(restEndPoint.indexOf("api"));
			}
		}
		String endPoint = "/storeAnalytics";
		if(StringUtils.isNotBlank(version)) {
			endPoint = version + "/storeAnalytics";
		}

		final String fileName = "analytics.txt";
		try (InputStream analyticsFileStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
			responseText = IOUtils.toString(Objects.requireNonNull(analyticsFileStream), StandardCharsets.UTF_8.name());
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Error reading contents of the file " + fileName, e);
		}

		String feURLContext = SystemProperties.getProperty("frontend_url");
		String feURL = feURLContext.substring(0, feURLContext.lastIndexOf('/'));
		responseText = responseText.replace("PORTAL_ENV_URL", feURL);
		responseText = responseText.replace("$END_POINT", endPoint);
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
	 */
	@PostMapping(value = { "/storeAnalytics" }, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Accepts data from partner applications with web analytics data.", response = PortalAPIResponse.class)
	public PortalAPIResponse storeAnalyticsScript(HttpServletRequest request, @RequestBody Analytics analyticsMap) {
		try {
			MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
			String appName = "";
			try {
				appName = getAppName(request, appName);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
                        ERROR_MSG, e);
			}

			try {
				if(SystemProperties.containsProperty(FEED_ML) && 
						SystemProperties.getProperty(FEED_ML).equals("true")) {
					storeAuxAnalytics(analyticsMap, appName);
				}
				
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger,
                        ERROR_MSG, e);
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

			return new PortalAPIResponse(true, "success");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "storeAnalytics failed", e);
			return new PortalAPIResponse(true, "error");
		}
	}

	private String getAppName(HttpServletRequest request, String appName) {
		
		EPApp appRecord = getApp(request);
		if (appRecord != null) {
			appName = appRecord.getName();
		}
		return appName;
	}
	
	private EPApp getApp(HttpServletRequest request) {
		String appKeyValue = request.getHeader(APP_KEY);
		EPApp appRecord = null;
		if (appKeyValue == null || appKeyValue.equals("")) {
			logger.error(EELFLoggerDelegate.errorLogger, " App Key unavailable; Proceeding with null app name");
		} else {
			 appRecord = appCacheService.getAppFromUeb(appKeyValue);
		}
		return appRecord;
	}

	protected void storeAuxAnalytics(Analytics analyticsMap, String appName) {
		logger.info(EELFLoggerDelegate.debugLogger,
				" Registering an action for recommendation: AppName/Function/UserId " + appName + "/"
						+ analyticsMap.getFunction() + "/" + analyticsMap.getUserid());

		
		Map<String, String> requestMapping = new HashMap<>();
		requestMapping.put("id", analyticsMap.getUserid());
		requestMapping.put("action", appName + "|" + analyticsMap.getFunction());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set your entity to send
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestMapping, headers);

		// send it!
		ListenableFuture<ResponseEntity<String>> out = restTemplate.exchange(
				EcompPortalUtils.widgetMLProtocol() + "://"
						+ widgetMService.getMLServiceLocation()
						+ REGISTER_ACTION,
				HttpMethod.POST, entity, String.class);
		out.addCallback(successCallback, failureCallback);
	}
	
}