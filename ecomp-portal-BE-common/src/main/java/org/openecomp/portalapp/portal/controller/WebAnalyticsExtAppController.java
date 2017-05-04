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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.openecomp.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EcompAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.service.AppsCacheService;
import org.openecomp.portalapp.portal.transport.Analytics;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalAPIResponse;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class WebAnalyticsExtAppController extends EPRestrictedRESTfulBaseController{
	
	private static final String APP_KEY = "uebkey";
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	AppsCacheService appCacheService;
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WebAnalyticsExtAppController.class);

	protected boolean isAuxRESTfulCall() {
		return true;
	}

	/*
	 * Answers requests from partner applications for a file that is expected to
	 * contain javascript to support web analytics.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
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
	 * @param analyticsMap
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/storeAnalytics" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Accepts data from partner applications with web analytics data.", response = PortalAPIResponse.class)
	public PortalAPIResponse storeAnalyticsScript(HttpServletRequest request, @RequestBody Analytics analyticsMap) throws Exception {	
		try{
		MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,EPEELFLoggerAdvice.getCurrentDateTimeUTC());		
		MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,EPEELFLoggerAdvice.getCurrentDateTimeUTC());
		String appName = "";
		try {
			
			String appKeyValue = request.getHeader(APP_KEY);
			if(appKeyValue == null || appKeyValue.equals("")) {
				logger.error(EELFLoggerDelegate.errorLogger, " App Key unavailable; Proceeding with null app name");
			} else {	
				EPApp appRecord = appCacheService.getAppForAnalytics(appKeyValue); 
				if(appRecord == null){
					logger.error(EELFLoggerDelegate.errorLogger, " App could not be found for the key "+ appKeyValue);
				}
				else appName = appRecord.getName();
				
			}
		
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, " Error retrieving Application to capture app name for analytics; Proceeding with empty app name");
		}
		
		
		
		logger.info(EELFLoggerDelegate.auditLogger, EPLogUtil.formatStoreAnalyticsAuditLogMessage(analyticsMap.getUserId(),appName, "WebAnalyticsExtAppController.postWebAnalyticsData", 
				EcompAuditLog.CD_ACTIVITY_STORE_ANALYTICS, analyticsMap.getAction(),analyticsMap.getPage(),analyticsMap.getFunction(),analyticsMap.getType()));			
		MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
		
		PortalAPIResponse response = new PortalAPIResponse(true, "success");
		return response;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "storeAnalytics failed", e);
			PortalAPIResponse response = new PortalAPIResponse(true, "error");
			return response;
		}
	}

}
