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
package org.openecomp.portalapp.portal.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EcompPortalUtils {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EcompPortalUtils.class);

	/**
	 * @param orgUserId
	 * @return true if orgUserId is not empty and contains only alphanumeric,
	 *         false otherwise
	 */
	public static boolean legitimateUserId(String orgUserId) {
		return orgUserId.matches("^[a-zA-Z0-9]+$");
	}

	/**
	 * Splits the string into a list of tokens using the specified regular
	 * expression
	 * 
	 * @param source
	 * @param regex
	 * @return List of tokens split from the source
	 */
	public static List<String> parsingByRegularExpression(String source, String regex) {
		List<String> tokens = new ArrayList<String>();
		if (source != null && source.length() > 0) {
			String[] parsed = source.split(regex);
			for (String token : parsed) {
				if (token.length() > 0) {
					tokens.add(token);
				}
			}
		}
		return tokens;
	}

	/**
	 * Builds a JSON object with error code and message information.
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @return JSON object as a String
	 */
	public static String jsonErrorMessageResponse(int errorCode, String errorMessage) {
		return "{\"error\":{\"code\":" + errorCode + "," + "\"message\":\"" + errorMessage + "\"}}";
	}

	/**
	 * Builds a JSON object with the specified message
	 * 
	 * @param message
	 * @return JSON object as a String
	 */
	public static String jsonMessageResponse(String message) {
		return String.format("{\"message\":\"%s\"}", message);
	}

	/**
	 * Serializes the specified object as JSON and writes the result to the
	 * debug log. If serialization fails, logs a message to the error logger.
	 * 
	 * @param logger
	 *            Logger for the class where the object was built; the logger
	 *            carries the class name.
	 * @param source
	 *            First portion of the log message
	 * @param msg
	 *            Second portion of the log message
	 * @param obj
	 *            Object to serialize as JSON
	 */
	public static void logAndSerializeObject(EELFLoggerDelegate logger, String source, String msg, Object obj) {
		try {
			String objectAsJson = new ObjectMapper().writeValueAsString(obj);
			logger.debug(EELFLoggerDelegate.debugLogger,
					String.format("source= [%s]; %s [%s];", source, msg, objectAsJson));
		} catch (JsonProcessingException e) {
			logger.warn(EELFLoggerDelegate.errorLogger, "logAndSerializedObject failed to serialize", e);
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "logAndSerializedObject failed", e);
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
		}
	}

	/**
	 * Serializes the specified object as JSON and writes the result to the
	 * debug log. If serialization fails, logs a message to the error logger.
	 * 
	 * @param source
	 *            First portion of the log message
	 * @param msg
	 *            Second portion of the log message
	 * @param obj
	 *            Object to serialize as JSON
	 */
	public static void logAndSerializeObject(String source, String msg, Object obj) {
		logAndSerializeObject(logger, source, msg, obj);
	}

	public static void rollbackTransaction(Transaction transaction, String errorMessage) {
		logger.error(EELFLoggerDelegate.errorLogger, errorMessage);
		try {
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeExecuteRollbackError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing a rollback transaction",
					e);
		}
	}

	public static void closeLocalSession(Session localSession, String errorMessage) {
		logger.error(EELFLoggerDelegate.errorLogger, errorMessage);
		try {
			if (localSession != null) {
				localSession.close();
			}
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoCloseSessionError, e);
			logger.error(EELFLoggerDelegate.errorLogger, errorMessage + ", closeLocalSession exception", e);
		}
	}

	// TODO: GLOBAL_LOGIN_URL is the same as in SessionTimeoutInterceptor.
	// It should be defined in SystemProperties.
	private static final String GLOBAL_LOGIN_URL = "global-login-url";

	/**
	 * Set response status to Unauthorized if user == null and to Forbidden in
	 * all (!) other cases. Logging is not performed if invocator == null
	 * 
	 * @param user
	 * @param response
	 * @param invocator
	 *            - may be null
	 */
	public static void setBadPermissions(EPUser user, HttpServletResponse response, String invocator) {
		if (user == null) {
			String loginUrl = SystemProperties.getProperty(EPCommonSystemProperties.LOGIN_URL_NO_RET_VAL);
			response.setHeader(GLOBAL_LOGIN_URL, loginUrl);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, Integer.toString(HttpServletResponse.SC_UNAUTHORIZED));
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, Integer.toString(HttpServletResponse.SC_FORBIDDEN));
		}
		if (invocator != null) {
			logger.warn(EELFLoggerDelegate.errorLogger,
					invocator + ", permissions problem, response status = " + response.getStatus());
		}
	}

	public static int getExternalAppResponseCode() {
		String responseCode = MDC.get(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
		int responseCodeInt = 0;
		try {
			if (responseCode != null && responseCode != "") {
				responseCodeInt = Integer.valueOf(responseCode);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred in getResponseCode(). Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return responseCodeInt;
	}

	// This method might be just for testing purposes.
	public static void setExternalAppResponseCode(int responseCode) {
		try {
			String responseCodeString = String.valueOf(responseCode);
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE, responseCodeString);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred in setResponseCode(). Details: " + EcompPortalUtils.getStackTrace(e));
		}
	}

	public static String getHTTPStatusString(int httpStatusCode) {
		String httpStatusString = "unknown_error";
		try {
			httpStatusString = org.springframework.http.HttpStatus.valueOf(httpStatusCode).name();
			if (httpStatusString != null) {
				httpStatusString = httpStatusString.toLowerCase();
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred in getHTTPStatusString(). Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return httpStatusString;
	}

	public static String getFEErrorString(Boolean internal, int responseCode) {
		// Return a String like the following:
		// "Internal Ecomp Error: 500 internal_server_error" or
		// "External App Error: 404 not_found"
		// TODO: create our own Ecomp error codes, starting with 1000 and up.
		String responseString = "";
		String internalExternalString = internal ? "Ecomp Error: " : "App Error: ";
		String httpStatusString = "unknown_error";
		try {
			if (responseCode < 1000) {
				httpStatusString = getHTTPStatusString(responseCode);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred in getFEErrorString(). Details: " + EcompPortalUtils.getStackTrace(e));
		}
		responseString = internalExternalString + responseCode + " " + httpStatusString;
		return responseString;
	}

	public static boolean isProductionBuild() {
		boolean productionBuild = true;
		String epVersion = EcompVersion.buildNumber;
		if (epVersion != null) {
			int buildNum = epVersion.lastIndexOf('.');
			if (buildNum > 0) {
				int buildNumber = Integer.parseInt(epVersion.substring(buildNum + 1));
				if (buildNumber < 3000) // Production versions are 3000+, (ie
										// 1.0.3003)
				{
					productionBuild = false;
				}
			}
		}
		return productionBuild;
	}

	private static final Object stackTraceLock = new Object();

	public static String getStackTrace(Throwable t) {
		synchronized (stackTraceLock) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		}
	}

	public static String getMyIpAdddress() {
		InetAddress ip;
		String localIp;
		try {
			ip = InetAddress.getLocalHost();
			localIp = ip.getHostAddress();
		} catch (UnknownHostException e) {
			localIp = "unknown";
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return localIp;
	}

	public static String getMyHostName() {
		InetAddress ip;
		String hostName;
		try {
			ip = InetAddress.getLocalHost();
			hostName = ip.getHostName();
		} catch (UnknownHostException e) {
			hostName = "unknown";
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return hostName;
	}

	/* return a default property if the expected one is not available */
	public static String getPropertyOrDefault(String property, String defaultValue) {
		return ((null == SystemProperties.getProperty(property) || SystemProperties.getProperty(property).equals(""))
				? defaultValue : SystemProperties.getProperty(property));
	}

	/**
	 * Calculates the time duration of a function call for logging purpose. It
	 * stores the result by using "MDC.put(SystemProperties.MDC_TIMER,
	 * timeDifference);" It is important to call
	 * "MDC.remove(SystemProperties.MDC_TIMER);" after this method call to clean
	 * up the record in MDC
	 *
	 * @param beginDateTime
	 *            the given begin time for the call
	 * @param endDateTime
	 *            the given end time for the call
	 * 
	 */
	public static void calculateDateTimeDifferenceForLog(String beginDateTime, String endDateTime) {
		if (beginDateTime != null && endDateTime != null) {
			try {
				SimpleDateFormat ecompLogDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

				Date beginDate = ecompLogDateFormat.parse(beginDateTime);
				Date endDate = ecompLogDateFormat.parse(endDateTime);
				String timeDifference = String.format("%d", endDate.getTime() - beginDate.getTime());
				MDC.put(SystemProperties.MDC_TIMER, timeDifference);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			}
		}
	}
	
	public static String widgetMsProtocol(){
		final String protocol;
		try{
			protocol = SystemProperties.getProperty(EPCommonSystemProperties.WIDGET_MS_PROTOCOL);
			return (protocol == null || protocol.trim().equals("")) ? "https" : protocol ;
		}
		catch(IllegalStateException ese){
			//looks like SystemProperties.getProperty throws IllegalStateException if it cannot find a property you are looking for
			//In order to not break the code if a non-required property is missing from system.properties, returning https as default
			//when this exception is caught.
			return "https";
		}		
	}

}
