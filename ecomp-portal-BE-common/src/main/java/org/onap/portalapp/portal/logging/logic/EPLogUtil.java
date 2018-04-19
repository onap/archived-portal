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
package org.onap.portalapp.portal.logging.logic;

import static com.att.eelf.configuration.Configuration.MDC_ALERT_SEVERITY;

import java.text.MessageFormat;

import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.onap.portalsdk.core.logging.format.ErrorSeverityEnum;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;

public class EPLogUtil {

	// This class has no logger of its own; it uses loggers passed to it.
	private static EELFLogger errorLogger = EELFManager.getInstance().getErrorLogger();

	/**
	 * Formats and writes a message to the error log with the class name and the
	 * specified parameters, using log level info, warn or error appropriate for
	 * the specified severity
	 * 
	 * @param classLogger
	 *            Logger for the class where the error occurred; the logger
	 *            carries the class name.
	 * @param epMessageEnum
	 *            Enum carrying alarm and error severity
	 * @param param
	 *            Values used to build the message.
	 */
	public static void logEcompError(EELFLoggerDelegate classLogger, EPAppMessagesEnum epMessageEnum, String... param) {
		logEcompError(classLogger, epMessageEnum, null, param);
	}

	/**
	 * Formats and writes a message to the error log with the class name and the
	 * specified parameters, using log level info, warn or error appropriate for
	 * the specified severity
	 * 
	 * @param epMessageEnum
	 *            Enum carrying alarm and error severity
	 * @param param
	 *            Values used to build the message.
	 */
	public static void logEcompError(EPAppMessagesEnum epMessageEnum, String... param) {
		try {
			AlarmSeverityEnum alarmSeverityEnum = epMessageEnum.getAlarmSeverity();
			ErrorSeverityEnum errorSeverityEnum = epMessageEnum.getErrorSeverity();

			MDC.put(MDC_ALERT_SEVERITY, alarmSeverityEnum.severity());
			MDC.put("ErrorCode", epMessageEnum.getErrorCode());
			MDC.put("ErrorDescription", epMessageEnum.getErrorDescription());
			MDC.put("ClassName", EPLogUtil.class.getName());

			String resolution = EPLogUtil
					.formatMessage(epMessageEnum.getDetails() + " " + epMessageEnum.getResolution(), (Object[]) param);
			if (errorSeverityEnum == ErrorSeverityEnum.WARN) {
				errorLogger.warn(resolution);
			} else if (errorSeverityEnum == ErrorSeverityEnum.INFO) {
				errorLogger.info(resolution);
			} else {
				errorLogger.error(resolution);
			}
		} catch (Exception e) {
			errorLogger.error("logEcompError failed", e);
		} finally {
			MDC.remove("ErrorCode");
			MDC.remove("ErrorDescription");
			MDC.remove("ClassName");
			MDC.remove(MDC_ALERT_SEVERITY);
		}
	}

	/**
	 * Formats and writes a message to the error log with the class name,
	 * throwable and the specified parameters, using log level info, warn or
	 * error appropriate for the specified severity
	 * 
	 * @param classLogger
	 *            Logger for the class where the error occurred; the logger
	 *            carries the class name.
	 * @param epMessageEnum
	 *            Enum carrying alarm and error severity
	 * @param th
	 *            Throwable; ignored if null
	 * @param param
	 *            Array of Strings used to build the message.
	 */
	@SuppressWarnings("static-access")
	public static void logEcompError(EELFLoggerDelegate classLogger, EPAppMessagesEnum epMessageEnum, Throwable th,
			String... param) {

		AlarmSeverityEnum alarmSeverityEnum = epMessageEnum.getAlarmSeverity();
		ErrorSeverityEnum errorSeverityEnum = epMessageEnum.getErrorSeverity();

		MDC.put(MDC_ALERT_SEVERITY, alarmSeverityEnum.severity());
		MDC.put("ErrorCode", epMessageEnum.getErrorCode());
		MDC.put("ErrorDescription", epMessageEnum.getErrorDescription());

		final String message = EPLogUtil.formatMessage(epMessageEnum.getDetails() + " " + epMessageEnum.getResolution(),
				(Object[]) param);
		if (errorSeverityEnum == ErrorSeverityEnum.INFO) {
			if (th == null)
				classLogger.info(classLogger.errorLogger, message);
			else
				classLogger.info(classLogger.errorLogger, message, th);
		} else if (errorSeverityEnum == ErrorSeverityEnum.WARN) {
			if (th == null)
				classLogger.warn(classLogger.errorLogger, message);
			else
				classLogger.warn(classLogger.errorLogger, message, th);
		} else {
			if (th == null)
				classLogger.error(classLogger.errorLogger, message);
			else
				classLogger.error(classLogger.errorLogger, message, th);
		}

		// Clean up
		MDC.remove(MDC_ALERT_SEVERITY);
		MDC.remove("ErrorCode");
		MDC.remove("ErrorDescription");
	}

	/**
	 * Builds a string using the format and parameters.
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	private static String formatMessage(String message, Object... args) {
		StringBuilder sbFormattedMessage = new StringBuilder();
		if (args != null && args.length > 0 && message != null && message != "") {
			MessageFormat mf = new MessageFormat(message);
			sbFormattedMessage.append(mf.format(args));
		} else {
			sbFormattedMessage.append(message);
		}
		return sbFormattedMessage.toString();
	}

	/**
	 * Builds a comma-separated string of values to document a user action.
	 * 
	 * @param action
	 *            String
	 * @param activity
	 *            String
	 * @param userId
	 *            String
	 * @param affectedId
	 *            String
	 * @param comment
	 *            String
	 * @return Value suitable for writing to the audit log file.
	 */
	public static String formatAuditLogMessage(String action, String activity, String userId, String affectedId,
			String comment) {
		StringBuilder auditLogMsg = new StringBuilder();
		auditLogMsg.append("Click_A:[");
		if (action != null && !action.equals("")) {
			auditLogMsg.append(" Action: ");
			auditLogMsg.append(action);
		}

		if (activity != null && !activity.equals("")) {
			auditLogMsg.append(",Activity CD: ");
			auditLogMsg.append(activity);
		}

		if (userId != null && !userId.equals("")) {
			auditLogMsg.append(",User ID: ");
			auditLogMsg.append(userId);
		}

		if (affectedId != null && !affectedId.equals("")) {
			auditLogMsg.append(",Affected ID: ");
			auditLogMsg.append(affectedId);
		}

		if (comment != null && !comment.equals("")) {
			auditLogMsg.append(",Comment: ");
			auditLogMsg.append(comment);
		}
		auditLogMsg.append("]");
		return auditLogMsg.toString();
	}

	/**
	 * Builds a comma-separated string of values to document a user browser
	 * action.
	 * 
	 * @param orgUserId
	 *            String
	 * @param appName
	 *            String
	 * @param action
	 *            String
	 * @param activity
	 *            String
	 * @param actionLink
	 *            String
	 * @param page
	 *            String
	 * @param function
	 *            String
	 * @param type
	 *            String
	 * @return String value suitable for writing to the audit log file.
	 */
	public static String formatStoreAnalyticsAuditLogMessage(String orgUserId, String appName, String action,
			String activity, String actionLink, String page, String function, String type) {
		StringBuilder auditLogStoreAnalyticsMsg = new StringBuilder();
		auditLogStoreAnalyticsMsg.append("Click_Analytics:[");
		if (orgUserId != null && !orgUserId.equals("")) {
			auditLogStoreAnalyticsMsg.append(" Organization User ID: ");
			auditLogStoreAnalyticsMsg.append(orgUserId);
		}

		if (appName != null && !appName.equals("")) {
			auditLogStoreAnalyticsMsg.append(",AppName: ");
			auditLogStoreAnalyticsMsg.append(appName);
		}

		if (action != null && !action.equals("")) {
			auditLogStoreAnalyticsMsg.append(",Action: ");
			auditLogStoreAnalyticsMsg.append(action);
		}

		if (activity != null && !activity.equals("")) {
			auditLogStoreAnalyticsMsg.append(",Activity: ");
			auditLogStoreAnalyticsMsg.append(activity);
		}

		if (actionLink != null && !actionLink.equals("")) {
			auditLogStoreAnalyticsMsg.append(",ActionLink: ");
			auditLogStoreAnalyticsMsg.append(actionLink);
		}

		if (page != null && !page.equals("")) {
			auditLogStoreAnalyticsMsg.append(",Page: ");
			auditLogStoreAnalyticsMsg.append(page);
		}

		if (function != null && !function.equals("")) {
			auditLogStoreAnalyticsMsg.append(",Function: ");
			auditLogStoreAnalyticsMsg.append(function);
		}

		if (type != null && !type.equals("")) {
			auditLogStoreAnalyticsMsg.append(",Type: ");
			auditLogStoreAnalyticsMsg.append(type);
		}
		auditLogStoreAnalyticsMsg.append("]");
		return auditLogStoreAnalyticsMsg.toString();
	}

	public static void logExternalAuthAccessAlarm(EELFLoggerDelegate logger, HttpStatus res) {
		if (res.equals(HttpStatus.UNAUTHORIZED) || res.equals(HttpStatus.FORBIDDEN)) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.ExternalAuthAccessAuthenticationError);
		} else if (res.equals(HttpStatus.NOT_FOUND) || res.equals(HttpStatus.NOT_ACCEPTABLE)
				|| res.equals(HttpStatus.CONFLICT) || res.equals(HttpStatus.BAD_REQUEST)) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.ExternalAuthAccessConnectionError);
		} else if (!res.equals(HttpStatus.ACCEPTED) && !res.equals(HttpStatus.OK)) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.ExternalAuthAccessGeneralError);
		}
	}

	public static void schedulerAccessAlarm(EELFLoggerDelegate logger, int res) {
		if (res == HttpStatus.UNAUTHORIZED.value() || res == HttpStatus.FORBIDDEN.value()) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.SchedulerAuxAccessAuthenticationError);
		} else if (res == HttpStatus.NOT_FOUND.value() || res == HttpStatus.NOT_ACCEPTABLE.value()
				|| res == HttpStatus.CONFLICT.value() || res == HttpStatus.BAD_REQUEST.value()
				|| res == HttpStatus.REQUEST_TIMEOUT.value()||res==HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.SchedulerAccessConnectionError);
		} else if (res == HttpStatus.PRECONDITION_FAILED.value() || res == HttpStatus.EXPECTATION_FAILED.value()) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.SchedulerInvalidAttributeError);
		} else if (res != HttpStatus.ACCEPTED.value() && res != HttpStatus.OK.value()
				&& res != HttpStatus.NO_CONTENT.value()) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.SchedulerAccessGeneralError);
		} else {
			logger.error(EELFLoggerDelegate.errorLogger, "Other SchedulerErrors failed", res);
		}
	}

}
