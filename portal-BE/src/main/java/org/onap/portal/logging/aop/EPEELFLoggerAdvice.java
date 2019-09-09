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

package org.onap.portal.logging.aop;

import com.att.eelf.configuration.Configuration;
import java.net.InetAddress;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.service.fn.FnUserService;
import org.onap.portal.service.fn.old.AppsCacheService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.exception.SessionExpiredException;
import org.onap.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.onap.portalsdk.core.logging.format.AuditLogFormatter;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.context.annotation.Configuration
public class EPEELFLoggerAdvice {

	private EELFLoggerDelegate adviceLogger = EELFLoggerDelegate.getLogger(EPEELFLoggerAdvice.class);

	private	final AppsCacheService appCacheService;
	private final FnUserService fnUserService;

	@Autowired
	public EPEELFLoggerAdvice(AppsCacheService appCacheService, FnUserService fnUserService) {
		this.appCacheService = appCacheService;
		this.fnUserService = fnUserService;
	}

	public static String getCurrentDateTimeUTC() {
		SimpleDateFormat ecompLogDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		return ecompLogDateFormat.format(new Date());
	}

	public void loadServletRequestBasedDefaults(Principal principal, HttpServletRequest req, SecurityEventTypeEnum securityEventType) {
		try {
			setHttpRequestBasedDefaultsIntoGlobalLoggingContext(principal, req, securityEventType, req.getServletPath());
		} catch (Exception e) {
			adviceLogger.error(EELFLoggerDelegate.errorLogger, "loadServletRequestBasedDefaults failed", e);
		}
	}

	public Object[] before(Principal principal, SecurityEventTypeEnum securityEventType, Object[] args, Object[] passOnArgs) {
		String className = "";
		if (passOnArgs.length > 0 && passOnArgs[0] != null)
			className = passOnArgs[0].toString();
		String methodName = EPCommonSystemProperties.ECOMP_PORTAL_BE;
		if (passOnArgs.length > 1 && passOnArgs[1] != null)
			methodName = passOnArgs[1].toString();

		MDC.put(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP, getCurrentDateTimeUTC());
		MDC.put(EPCommonSystemProperties.TARGET_ENTITY, EPCommonSystemProperties.ECOMP_PORTAL_BE);
		MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, methodName);
		if (MDC.get(Configuration.MDC_KEY_REQUEST_ID) == null||MDC.get(Configuration.MDC_KEY_REQUEST_ID).isEmpty()){
			String requestId = UUID.randomUUID().toString();
			MDC.put(Configuration.MDC_KEY_REQUEST_ID, requestId);
		}
		MDC.put(EPCommonSystemProperties.PARTNER_NAME, "Unknown");
		MDC.put(Configuration.MDC_SERVICE_NAME, EPCommonSystemProperties.ECOMP_PORTAL_BE);

		if (securityEventType != null) {
			MDC.put(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
					getCurrentDateTimeUTC());
			HttpServletRequest req;
			if (args.length > 0 && args[0] != null && args[0] instanceof HttpServletRequest) {
				req = (HttpServletRequest) args[0];
				this.setHttpRequestBasedDefaultsIntoGlobalLoggingContext(principal, req, securityEventType, methodName);
			}
		}

		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(className);
		logger.debug(EELFLoggerDelegate.debugLogger, "EPEELFLoggerAdvice#before: entering {}", methodName);
		return new Object[] { "" };
	}

	public void after(Principal principal, SecurityEventTypeEnum securityEventType, String statusCode, String responseCode, Object[] args,
			Object[] returnArgs, Object[] passOnArgs) {
		String className = "";
		if (passOnArgs.length > 0 && passOnArgs[0] != null)
			className = passOnArgs[0].toString();
		String methodName =  EPCommonSystemProperties.ECOMP_PORTAL_BE;
		if (passOnArgs.length > 1 && passOnArgs[1] != null)
			methodName = passOnArgs[1].toString();

		if (MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME) == null
				|| "".equals(MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME)))
			MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, methodName);

		if (MDC.get(EPCommonSystemProperties.TARGET_ENTITY) == null
				|| "".equals(MDC.get(EPCommonSystemProperties.TARGET_ENTITY)))
			MDC.put(EPCommonSystemProperties.TARGET_ENTITY, EPCommonSystemProperties.ECOMP_PORTAL_BE);

		if (MDC.get(Configuration.MDC_KEY_REQUEST_ID) == null||MDC.get(Configuration.MDC_KEY_REQUEST_ID).isEmpty()){
			String requestId = UUID.randomUUID().toString();
			MDC.put(Configuration.MDC_KEY_REQUEST_ID, requestId);
		}

		if (MDC.get(EPCommonSystemProperties.PARTNER_NAME) == null|| MDC.get(EPCommonSystemProperties.PARTNER_NAME).isEmpty()){
			MDC.put(EPCommonSystemProperties.PARTNER_NAME, "Unknown");
		}

		MDC.put(Configuration.MDC_SERVICE_NAME, EPCommonSystemProperties.ECOMP_PORTAL_BE);


		MDC.put(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP,
				MDC.get(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP));
		MDC.put(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP, getCurrentDateTimeUTC());
		this.calculateDateTimeDifference(MDC.get(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP),
				MDC.get(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP));

		if (securityEventType != null && args.length > 0 && args[0] != null && args[0] instanceof HttpServletRequest
				&& securityEventType == SecurityEventTypeEnum.INCOMING_REST_MESSAGE
				&& (MDC.get(EPCommonSystemProperties.FULL_URL) == null
						|| MDC.get(EPCommonSystemProperties.FULL_URL).isEmpty())) {
			HttpServletRequest req = (HttpServletRequest) args[0];
			this.setHttpRequestBasedDefaultsIntoGlobalLoggingContext(principal, req, securityEventType, methodName);
		}

		String externalAPIResponseCode = MDC.get(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
		if (externalAPIResponseCode == null || "".equals(externalAPIResponseCode)
				|| externalAPIResponseCode.trim().equalsIgnoreCase("200")) {
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, responseCode);
			MDC.put(EPCommonSystemProperties.STATUS_CODE, statusCode);
		} else {
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, externalAPIResponseCode);
			MDC.put(EPCommonSystemProperties.STATUS_CODE, "ERROR");
		}

		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(className);
		logger.debug(EELFLoggerDelegate.debugLogger, "EPEELFLoggerAdvice#after: finished {}", methodName);

		logger.info(EELFLoggerDelegate.metricsLogger,  methodName + " operation is completed.");

		if (securityEventType != null) {
			MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
					MDC.get(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP));
			MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, getCurrentDateTimeUTC());
			this.calculateDateTimeDifference(MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
					MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));

			this.logSecurityMessage(logger, securityEventType, methodName);

			if (securityEventType != SecurityEventTypeEnum.OUTGOING_REST_MESSAGE
					&& securityEventType != SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
				MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
				MDC.remove(EPCommonSystemProperties.PARTNER_NAME);
				MDC.remove(Configuration.MDC_SERVICE_NAME);
				MDC.remove(EPCommonSystemProperties.MDC_LOGIN_ID);
				MDC.remove(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
			}else{
				MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
				MDC.remove(EPCommonSystemProperties.PARTNER_NAME);
				MDC.remove(Configuration.MDC_SERVICE_NAME);
			}


			MDC.remove(EPCommonSystemProperties.FULL_URL);
			MDC.remove(EPCommonSystemProperties.PROTOCOL);
			MDC.remove(EPCommonSystemProperties.STATUS_CODE);
			MDC.remove(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.RESPONSE_CODE);
		}
		MDC.remove(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.MDC_TIMER);
		MDC.remove(EPCommonSystemProperties.TARGET_ENTITY);
		MDC.remove(EPCommonSystemProperties.TARGET_SERVICE_NAME);

	}

	private void logSecurityMessage(EELFLoggerDelegate logger, SecurityEventTypeEnum securityEventType,
			String restMethod) {
		StringBuilder additionalInfoAppender = new StringBuilder();
		String auditMessage;

		if (securityEventType == SecurityEventTypeEnum.OUTGOING_REST_MESSAGE) {
			additionalInfoAppender.append(String.format("%s '%s' request was initiated.", restMethod,
					MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME)));
		} else if (securityEventType == SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
			additionalInfoAppender.append("LDAP Phonebook search operation is performed.");
		} else {
			additionalInfoAppender.append(String.format("%s request was received.", restMethod));

			if (securityEventType == SecurityEventTypeEnum.FE_LOGIN_ATTEMPT) {
				String loginId;
				String additionalMessage = " Successfully authenticated.";
				loginId = MDC.get(EPCommonSystemProperties.MDC_LOGIN_ID);
				if (loginId == null || "".equals(loginId) || EPCommonSystemProperties.UNKNOWN.equals(loginId)) {
					additionalMessage = " No cookies are found.";
				}
				additionalInfoAppender.append(additionalMessage);
			} else if (securityEventType == SecurityEventTypeEnum.FE_LOGOUT) {
				additionalInfoAppender.append(" User has been successfully logged out.");
			}
		}

		String fullURL = MDC.get(EPCommonSystemProperties.FULL_URL);
		if (fullURL != null && !"".equals(fullURL)) {
			additionalInfoAppender.append(" Request-URL:").append(MDC.get(EPCommonSystemProperties.FULL_URL));
		}

		auditMessage = AuditLogFormatter.getInstance().createMessage(MDC.get(EPCommonSystemProperties.PROTOCOL),
				securityEventType.name(), MDC.get(EPCommonSystemProperties.MDC_LOGIN_ID),
				additionalInfoAppender.toString());

		logger.info(EELFLoggerDelegate.auditLogger, auditMessage);
	}

	private void setHttpRequestBasedDefaultsIntoGlobalLoggingContext(Principal principal, HttpServletRequest req,
			SecurityEventTypeEnum securityEventType, String restMethod) {

		if (req != null) {
			if (securityEventType != SecurityEventTypeEnum.OUTGOING_REST_MESSAGE
					&& securityEventType != SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH
					&& securityEventType != SecurityEventTypeEnum.INCOMING_UEB_MESSAGE) {
				loadRequestId(req);

				loadPartnerName(req);

				loadLoginId(principal, req);

				loadUrlProtocol(req);

				loadServicePath(req, restMethod);

				loadClientAddress(req);

			} else if (securityEventType == SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
				MDC.put(EPCommonSystemProperties.TARGET_ENTITY, "Phonebook");
				MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, "search");
			}
		} else {
			MDC.put(Configuration.MDC_SERVICE_NAME, restMethod);
			MDC.put(EPCommonSystemProperties.PARTNER_NAME, EPCommonSystemProperties.ECOMP_PORTAL_FE);
		}

		MDC.put(Configuration.MDC_SERVICE_INSTANCE_ID, "");
		MDC.put(Configuration.MDC_ALERT_SEVERITY, AlarmSeverityEnum.INFORMATIONAL.severity());
		try {
			MDC.put(Configuration.MDC_SERVER_FQDN, InetAddress.getLocalHost().getCanonicalHostName());
			MDC.put(Configuration.MDC_SERVER_IP_ADDRESS, InetAddress.getLocalHost().getHostAddress());
			MDC.put(Configuration.MDC_INSTANCE_UUID, SystemProperties.getProperty(SystemProperties.INSTANCE_UUID));
		} catch (Exception e) {
			adviceLogger.error(EELFLoggerDelegate.errorLogger,
					"setHttpRequestBasedDefaultsIntoGlobalLoggingContext failed", e);
		}
	}

	private void loadClientAddress(HttpServletRequest req) {
		String clientIPAddress;
		clientIPAddress = req.getHeader("X-FORWARDED-FOR");
		if (clientIPAddress == null) {
			clientIPAddress = req.getRemoteAddr();
		}
		MDC.put(EPCommonSystemProperties.CLIENT_IP_ADDRESS, clientIPAddress);
	}

	private void loadServicePath(HttpServletRequest req, String restMethod) {
		MDC.put(Configuration.MDC_SERVICE_NAME, restMethod);
		String restPath = req.getServletPath();
		if (restPath != null && restPath.trim().length()>0) {

			MDC.put(Configuration.MDC_SERVICE_NAME, restPath);
		}
	}

	private void loadUrlProtocol(HttpServletRequest req) {
		String restURL;
		MDC.put(EPCommonSystemProperties.FULL_URL, EPCommonSystemProperties.UNKNOWN);
		MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTP);
		restURL = UserUtils.getFullURL(req);
		if (restURL.trim().length() > 0) {
			MDC.put(EPCommonSystemProperties.FULL_URL, restURL);
			if (restURL.toLowerCase().contains("https")) {
				MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTPS);
			}
		}
	}

	private void loadRequestId(HttpServletRequest req) {
		String requestId = UserUtils.getRequestId(req);
		if (requestId == null||requestId.trim().length()==0) {
			requestId = UUID.randomUUID().toString();
		}
		MDC.put(Configuration.MDC_KEY_REQUEST_ID, requestId);
	}

	private void loadLoginId(Principal principal, HttpServletRequest req) {
		String loginId = "NoUser";
		try {
			FnUser user = fnUserService.loadUserByUsername(principal.getName());
			loginId = (user != null ? user.getOrgUserId(): loginId);
		} catch (SessionExpiredException se) {
			adviceLogger.debug(EELFLoggerDelegate.debugLogger,
					"setHttpRequestBasedDefaultsIntoGlobalLoggingContext: No user found in session");
		}

		final String nameHeader = req.getHeader(EPCommonSystemProperties.USERNAME);
		if (nameHeader != null) {
			loginId = nameHeader;
		}

		final String authHeader = req.getHeader(EPCommonSystemProperties.AUTHORIZATION);
		if (authHeader != null) {
			String[] accountNamePassword = EcompPortalUtils.getUserNamePassword(authHeader);
			if (accountNamePassword != null && accountNamePassword.length == 2) {
				loginId = accountNamePassword[0];
			}
		}

		MDC.put(EPCommonSystemProperties.MDC_LOGIN_ID, loginId );
	}

	private void loadPartnerName(HttpServletRequest req) {


		// Load user agent into MDC context, if available.
		String accessingClient = req.getHeader(SystemProperties.USERAGENT_NAME);
		accessingClient = (accessingClient == null || accessingClient.trim().length()==0)?"Unknown":accessingClient;
		if (accessingClient != null && accessingClient.trim().length()==0 && (accessingClient.contains("Mozilla")
				|| accessingClient.contains("Chrome") || accessingClient.contains("Safari"))) {
			accessingClient = EPCommonSystemProperties.ECOMP_PORTAL_FE;
		}
		MDC.put(EPCommonSystemProperties.PARTNER_NAME, accessingClient);

		String uebVal = req.getHeader(EPCommonSystemProperties.UEB_KEY);
		if(uebVal != null) {
			FnApp appRecord = appCacheService.getAppFromUeb(uebVal);
			MDC.put(EPCommonSystemProperties.PARTNER_NAME, appRecord.getAppName());
		}


	}

	private void calculateDateTimeDifference(String beginDateTime, String endDateTime) {
		if (beginDateTime != null && endDateTime != null && !beginDateTime.isEmpty()&&!endDateTime.isEmpty()) {
			try {
				SimpleDateFormat ecompLogDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				Date beginDate = ecompLogDateFormat.parse(beginDateTime);
				Date endDate = ecompLogDateFormat.parse(endDateTime);
				String timeDifference = String.format("%d", endDate.getTime() - beginDate.getTime());
				MDC.put(SystemProperties.MDC_TIMER, timeDifference);
			} catch (Exception e) {
				adviceLogger.error(EELFLoggerDelegate.errorLogger, "calculateDateTimeDifference failed", e);
			}
		}
	}

	public String getInternalResponseCode() {
		return MDC.get(EPCommonSystemProperties.RESPONSE_CODE);
	}

}
