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
package org.openecomp.portalapp.portal.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EcompPortalUtils {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EcompPortalUtils.class);

	// TODO: GLOBAL_LOGIN_URL is the same as in SessionTimeoutInterceptor.
	// It should be defined in SystemProperties.
	private static final String GLOBAL_LOGIN_URL = "global-login-url";

	/**
	 * @param orgUserId
	 *            User ID to validate
	 * @return true if orgUserId is not empty and contains only alphanumeric, false
	 *         otherwise
	 */
	public static boolean legitimateUserId(String orgUserId) {
		return orgUserId.matches("^[a-zA-Z0-9]+$");
	}

	/**
	 * Splits the string into a list of tokens using the specified regular
	 * expression
	 * 
	 * @param source
	 *            String to split
	 * @param regex
	 *            tokens
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
	 *            error code
	 * @param errorMessage
	 *            message
	 * @return JSON object as a String
	 */
	public static String jsonErrorMessageResponse(int errorCode, String errorMessage) {
		return "{\"error\":{\"code\":" + errorCode + "," + "\"message\":\"" + errorMessage + "\"}}";
	}

	/**
	 * Builds a JSON object with the specified message
	 * 
	 * @param message
	 *            Message to embed
	 * @return JSON object as a String
	 */
	public static String jsonMessageResponse(String message) {
		return String.format("{\"message\":\"%s\"}", message);
	}

	/**
	 * Serializes the specified object as JSON and writes the result to the debug
	 * log. If serialization fails, logs a message to the error logger.
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
	 * Serializes the specified object as JSON and writes the result to the debug
	 * log. If serialization fails, logs a message to the error logger.
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

	/**
	 * Set response status to Unauthorized if user == null and to Forbidden in all
	 * (!) other cases. Logging is not performed if invocator == null
	 * 
	 * @param user
	 *            User object
	 * @param response
	 *            HttpServletResponse
	 * @param invocator
	 *            may be null
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
			logger.error(EELFLoggerDelegate.errorLogger, "getExternalAppResponseCode failed", e);
		}
		return responseCodeInt;
	}

	// This method might be just for testing purposes.
	public static void setExternalAppResponseCode(int responseCode) {
		try {
			String responseCodeString = String.valueOf(responseCode);
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE, responseCodeString);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "setExternalAppResponseCode failed", e);
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
			logger.error(EELFLoggerDelegate.errorLogger, "getHTTPStatusString failed", e);
		}
		return httpStatusString;
	}

	public static String getFEErrorString(Boolean internal, int responseCode) {
		// Return a String like the following:
		// "Internal Ecomp Error: 500 internal_server_error" or
		// "External App Error: 404 not_found"
		// TODO: create our own Ecomp error codes, starting with 1000 and up.
		String internalExternalString = internal ? "Ecomp Error: " : "App Error: ";
		String httpStatusString = "unknown_error";
		try {
			if (responseCode < 1000) {
				httpStatusString = getHTTPStatusString(responseCode);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getFEErrorString failed", e);
		}
		String responseString = internalExternalString + responseCode + " " + httpStatusString;
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

	public static String getMyIpAdddress() {
		InetAddress ip;
		String localIp;
		try {
			ip = InetAddress.getLocalHost();
			localIp = ip.getHostAddress();
		} catch (UnknownHostException e) {
			localIp = "unknown";
			logger.error(EELFLoggerDelegate.errorLogger, "getMyIpAdddress failed ", e);
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
			logger.error(EELFLoggerDelegate.errorLogger, "getMyHostName failed", e);
		}
		return hostName;
	}

	/**
	 * Returns a default property if the expected one is not available
	 * 
	 * @param property
	 *            Key
	 * @param defaultValue
	 *            default Value
	 * @return Default value if property is not defined or yields the empty string;
	 *         else the property value.
	 */
	public static String getPropertyOrDefault(String property, String defaultValue) {
		if (!SystemProperties.containsProperty(property))
			return defaultValue;
		String value = SystemProperties.getProperty(property);
		if (value == null || "".equals(value))
			return defaultValue;
		return value;
	}

	/**
	 * Calculates the time duration of a function call for logging purpose. It
	 * stores the result by using "MDC.put(SystemProperties.MDC_TIMER,
	 * timeDifference);" It is important to call
	 * "MDC.remove(SystemProperties.MDC_TIMER);" after this method call to clean up
	 * the record in MDC
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
				logger.error(EELFLoggerDelegate.errorLogger, "calculateDateTimeDifferenceForLog failed", e);
			}
		}
	}

	/**
	 * Answers the protocol to use.
	 * 
	 * @return Protocol name from property file; defaults to https.
	 */
	public static String widgetMsProtocol() {
		return getPropertyOrDefault(EPCommonSystemProperties.WIDGET_MS_PROTOCOL, "https");
	}

	/**
	 * Answers the host to use.
	 * 
	 * @return Host name from property file; defaults to localhost.
	 */
	public static String localOrDockerHost() {
		return getPropertyOrDefault(EPCommonSystemProperties.WIDGET_MS_HOSTNAME, "localhost");
	}

	/**
	 * It returns headers where username and password of external central auth is
	 * encoded to base64
	 * 
	 * @return header which contains external central auth username and password
	 *         base64 encoded
	 * @throws Exception
	 *             if unable to decrypt the password
	 */
	public static HttpHeaders base64encodeKeyForAAFBasicAuth() throws Exception {
		String userName = "";
		String decryptedPass = "";
		if (EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_AUTH_USER_NAME)
				&& EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_AUTH_PASSWORD)) {
			decryptedPass = SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_AUTH_PASSWORD);
			userName = SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_AUTH_USER_NAME);
		}
		String decPass = decrypted(decryptedPass);
		String usernamePass = userName + ":" + decPass;
		String encToBase64 = String.valueOf((DatatypeConverter.printBase64Binary(usernamePass.getBytes())));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encToBase64);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private static String decrypted(String encrypted) throws Exception {
		String result = "";
		if (encrypted != null && encrypted.length() > 0) {
			try {
				result = CipherUtil.decrypt(encrypted, SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	public static String truncateString(String originString, int size){
		if(originString.length()>=size){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(originString);
			stringBuilder.setLength(size);
			stringBuilder.append("...");
			return stringBuilder.toString();
		}
		return originString;
	}
}
