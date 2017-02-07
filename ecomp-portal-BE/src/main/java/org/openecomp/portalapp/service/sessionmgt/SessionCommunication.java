/*-
 * ================================================================================
 * eCOMP Portal
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
package org.openecomp.portalapp.service.sessionmgt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.exception.UrlAccessRestrictedException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.slf4j.MDC;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.att.eelf.configuration.Configuration;

@Service("sessionCommunication")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class SessionCommunication {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SessionCommunication.class);
	
	@EPAuditLog
	public String sendGet(OnboardingApp app) throws Exception {
		String appResponse = "";
		String appName = "Unknwon";
		int responseCode = 0;
		
		try {
			if (app != null && app.name != null && app.name != "") {
				appName = app.name;
			}
			String url = app.restUrl + "/sessionTimeOuts";
			String encriptedPwdDB = app.appPassword;
			String appUserName = app.username;

			setLocalMDCContext(app, "/sessionTimeOuts", url);

			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			con.setConnectTimeout(3000);
			con.setReadTimeout(8000);
			// add request header
			con.setRequestProperty("username", appUserName);
			con.setRequestProperty("password", encriptedPwdDB);

			// con.set
			responseCode = con.getResponseCode();
			logger.debug(EELFLoggerDelegate.debugLogger, "Response Code : " + responseCode);
						
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			appResponse = response.toString();
		} catch (UrlAccessRestrictedException e) {
			responseCode = HttpServletResponse.SC_UNAUTHORIZED;
			logger.error(EELFLoggerDelegate.errorLogger, String.format("SessionCommunication.sendGet received an un-authorized exception. AppName: %s", appName));
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeRestApiAuthenticationError);
		} catch (Exception e) {
			responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			String message = String.format(
					"SessionCommunication.sendGet encountered an Exception. AppName: %s, Details: %s", appName,
					EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeHttpConnectionError, e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, message);
		} finally {
			EcompPortalUtils.setExternalAppResponseCode(responseCode);
		}
		return appResponse;
	}

	@EPAuditLog
	public Boolean pingSession(OnboardingApp app, String sessionTimeoutMap) throws Exception {
		String appName = "Unknwon";
		int responseCode = 0;
		try {
			if (app != null && app.name != null && app.name != "") {
				appName = app.name;
			}
			
			String url = app.restUrl + "/updateSessionTimeOuts";
			String encriptedPwdDB = app.appPassword;
			String appUserName = app.username;
			// String decreptedPwd = CipherUtil.decrypt(encriptedPwdDB,
			// SystemProperties.getProperty(SystemProperties.Decryption_Key));

			setLocalMDCContext(app, "/updateSessionTimeOuts", url);

			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("POST");
			con.setConnectTimeout(3000);
			con.setReadTimeout(15000);

			// add request header
			con.setRequestProperty("username", appUserName);
			con.setRequestProperty("password", encriptedPwdDB);

			con.setRequestProperty("sessionMap", sessionTimeoutMap);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.getOutputStream().write(sessionTimeoutMap.getBytes());
			con.getOutputStream().flush();
			con.getOutputStream().close();

			responseCode = con.getResponseCode();
			logger.debug(EELFLoggerDelegate.debugLogger, "Response Code : " + responseCode);
		} catch (UrlAccessRestrictedException e) {
			responseCode = HttpServletResponse.SC_UNAUTHORIZED;
			String message = String.format(
					"SessionCommunication.pingSession received an un-authorized exception. AppName: %s", appName);
			logger.error(EELFLoggerDelegate.errorLogger, message);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeRestApiAuthenticationError);
		} catch (Exception e) {
			responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			String message = String.format(
					"SessionCommunication.pingSession encountered an Exception. AppName: %s, Details: %s", appName,
					EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeHttpConnectionError, e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, message);
		} finally {
			EcompPortalUtils.setExternalAppResponseCode(responseCode);
		}
		
		return true;
	}

	@EPAuditLog
	public Boolean timeoutSession(OnboardingApp app, String portalJSessionId) throws Exception {
		String appName = "Unknwon";
		int responseCode = 0;
		try {
			if (app != null && app.name != null && app.name != "") {
				appName = app.name;
			}
			
			String url = app.restUrl + "/timeoutSession" + "?portalJSessionId=" + portalJSessionId;

			String encriptedPwdDB = app.appPassword;
			String appUserName = app.username;
			// String decreptedPwd = CipherUtil.decrypt(encriptedPwdDB,
			// SystemProperties.getProperty(SystemProperties.Decryption_Key));

			setLocalMDCContext(app, "/timeoutSession", url);

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("POST");
			con.setConnectTimeout(3000);
			con.setReadTimeout(15000);

			// add request header
			con.setRequestProperty("username", appUserName);
			con.setRequestProperty("password", encriptedPwdDB);

			// con.setRequestProperty("portalJSessionId", portalJSessionId);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.getOutputStream().flush();
			con.getOutputStream().close();

			responseCode = con.getResponseCode();
			logger.debug(EELFLoggerDelegate.debugLogger, "Response Code : " + responseCode);
		} catch (UrlAccessRestrictedException e) {
			responseCode = HttpServletResponse.SC_UNAUTHORIZED;
			String message = String.format(
					"SessionCommunication.timeoutSession received an un-authorized exception. AppName: %s", appName);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			logger.error(EELFLoggerDelegate.errorLogger, message);
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeRestApiAuthenticationError);
		} catch (Exception e) {
			responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			String message = String.format(
					"SessionCommunication.timeoutSession encountered an Exception. AppName: %s, Details: %s", appName,
					EcompPortalUtils.getStackTrace(e));
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeHttpConnectionError, e.getMessage());
			logger.error(EELFLoggerDelegate.errorLogger, message);
		} finally {
			EcompPortalUtils.setExternalAppResponseCode(responseCode);
		}
		return true;
	}

	@EPMetricsLog
	private void setLocalMDCContext(OnboardingApp app, String restPath, String url) {
		setRequestId();
		MDC.put(EPSystemProperties.PROTOCOL, EPSystemProperties.HTTP);
		if (url!=null && url.contains("https")) {
			MDC.put(EPSystemProperties.PROTOCOL, EPSystemProperties.HTTPS);
		}
		MDC.put(EPSystemProperties.FULL_URL, url);
		MDC.put(EPSystemProperties.TARGET_ENTITY, app.name);
		MDC.put(EPSystemProperties.TARGET_SERVICE_NAME, restPath);
	}
	
	/**
	 * Generates request id, service name fields and loads them
	 * into MDC, as these values could be empty as these
	 * session timeout requests are generated at 
	 * scheduled intervals using quartz scheduler.
	 */
	@EPMetricsLog
	public void setRequestId() {
		String requestId = MDC.get(Configuration.MDC_KEY_REQUEST_ID);
		if (StringUtils.isEmpty(requestId)) {
			MDC.put(Configuration.MDC_KEY_REQUEST_ID, UUID.randomUUID().toString());
		}
		
		MDC.put(Configuration.MDC_SERVICE_NAME, "/quartz/keepSessionAlive");
		MDC.put(EPSystemProperties.PARTNER_NAME, EPSystemProperties.ECOMP_PORTAL_BE);
	}
	
	/**
	 * Remove the values from MDC as these requests are 
	 * executed at regular intervals based on quartz rather
	 * incoming REST API requests.
	 * @param bAll
	 */
	@EPMetricsLog
	public void clear(Boolean bAll) {
		MDC.remove(EPSystemProperties.EXTERNAL_API_RESPONSE_CODE);
		if (bAll) {
			MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
			MDC.remove(Configuration.MDC_SERVICE_NAME);
			MDC.remove(EPSystemProperties.PARTNER_NAME);
		}
	}
}
