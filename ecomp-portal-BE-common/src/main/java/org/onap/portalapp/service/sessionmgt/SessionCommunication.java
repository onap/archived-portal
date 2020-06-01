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
package org.onap.portalapp.service.sessionmgt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.exception.UrlAccessRestrictedException;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.KeyConstants;
import org.onap.portalsdk.core.onboarding.util.KeyProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.att.eelf.configuration.Configuration;

@Service("sessionCommunication")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class SessionCommunication {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SessionCommunication.class);
	@Autowired
	private AppsCacheService appsCacheService;
	
	private static final String BASIC_AUTHENTICATION_HEADER = "Authorization";

	@EPAuditLog
	public String sendGet(OnboardingApp app) throws Exception {
		String appResponse = "";
		String appName = "";
		int responseCode = 0;
		if (app != null && app.name != null && app.name != "") {
			try {
				appName = app.name;
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
				Map<String,String> headers = getHeaders(app);
				appUserName =headers.get("username");
				encriptedPwdDB = headers.get("password");
				
				con.setRequestProperty("username", appUserName);
				con.setRequestProperty("password", encriptedPwdDB);

				
				String encoding = Base64.getEncoder().encodeToString((appUserName + ":" + encriptedPwdDB).getBytes());
				String encodingStr = "Basic " + encoding;
				con.setRequestProperty(BASIC_AUTHENTICATION_HEADER, encodingStr);

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
				logger.error(EELFLoggerDelegate.errorLogger, String.format(
						"SessionCommunication.sendGet received an un-authorized exception. AppName: %s", appName));
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiAuthenticationError, e);
			} catch (Exception e) {
				responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				String message = String.format(
						"SessionCommunication.sendGet encountered an Exception. AppName: %s, Details: %s", appName,
						e.toString());
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHttpConnectionError, e);
				logger.error(EELFLoggerDelegate.errorLogger, message, e);
			} finally {
				EcompPortalUtils.setExternalAppResponseCode(responseCode);
			}
		} else {
			logger.error(EELFLoggerDelegate.errorLogger, "SessionCommunication sendGet: app is null");
		}
		return appResponse;
	}

	@EPAuditLog
	public Boolean pingSession(OnboardingApp app, String sessionTimeoutMap) throws Exception {
		String appName = "";
		int responseCode = 0;
		try {
			if (app == null)
				throw new Exception("SessionCommunication.pingSession: app is null");
			if (app != null && app.name != null && app.name != "") {
				appName = app.name;
			}
			String url = app.restUrl + "/updateSessionTimeOuts";
			String encriptedPwdDB = app.appPassword;
			String appUserName = app.username;

			setLocalMDCContext(app, "/updateSessionTimeOuts", url);

			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("POST");
			con.setConnectTimeout(3000);
			con.setReadTimeout(15000);

			Map<String,String> headers = getHeaders(app);
			appUserName =headers.get("username");
			encriptedPwdDB = headers.get("password");
			
			con.setRequestProperty("username", appUserName);
			con.setRequestProperty("password", encriptedPwdDB);
			
			String encoding = Base64.getEncoder().encodeToString((appUserName + ":" + encriptedPwdDB).getBytes());
			String encodingStr = "Basic " + encoding;
			con.setRequestProperty(BASIC_AUTHENTICATION_HEADER, encodingStr);

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
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiAuthenticationError, e);
		} catch (Exception e) {
			responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			String message = String.format(
					"SessionCommunication.pingSession encountered an Exception. AppName: %s, Details: %s", appName, e.toString());
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHttpConnectionError, e);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
		} finally {
			EcompPortalUtils.setExternalAppResponseCode(responseCode);
		}

		return true;
	}

	@EPAuditLog
	public Boolean timeoutSession(OnboardingApp app, String portalJSessionId) throws Exception {
		String appName = "Unknwon";
		int responseCode = 0;
		if (app != null && app.name != null && app.name != "") {
			try {
				appName = app.name;
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

				Map<String,String> headers = getHeaders(app);
				appUserName =headers.get("username");
				encriptedPwdDB = headers.get("password");
				
				con.setRequestProperty("username", appUserName);
				con.setRequestProperty("password", encriptedPwdDB);
				
				String encoding = Base64.getEncoder().encodeToString((appUserName + ":" + encriptedPwdDB).getBytes());
				String encodingStr = "Basic " + encoding;
				con.setRequestProperty(BASIC_AUTHENTICATION_HEADER, encodingStr);
				
				con.setDoInput(true);
				con.setDoOutput(true);
				con.getOutputStream().flush();
				con.getOutputStream().close();

				responseCode = con.getResponseCode();
				logger.debug(EELFLoggerDelegate.debugLogger, "Response Code : " + responseCode);
			} catch (UrlAccessRestrictedException e) {
				responseCode = HttpServletResponse.SC_UNAUTHORIZED;
				String message = String.format(
						"SessionCommunication.timeoutSession received an un-authorized exception. AppName: %s",
						appName);
				logger.error(EELFLoggerDelegate.errorLogger, message);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiAuthenticationError, e);
			} catch (Exception e) {
				responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				String message = String.format(
						"SessionCommunication.timeoutSession encountered an Exception. AppName: %s, Details: %s", 
						appName, e.toString());
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHttpConnectionError, e);
				logger.error(EELFLoggerDelegate.errorLogger, message, e);
			} finally {
				EcompPortalUtils.setExternalAppResponseCode(responseCode);
			}
		} else {
			logger.error(EELFLoggerDelegate.errorLogger, "SessionCommunication pingSession: app is null");
		}
		return true;
	}

	@EPMetricsLog
	private void setLocalMDCContext(OnboardingApp app, String restPath, String url) {
		setRequestId();
		MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTP);
		if (url != null && url.contains("https")) {
			MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTPS);
		}
		MDC.put(EPCommonSystemProperties.FULL_URL, url);
		MDC.put(EPCommonSystemProperties.TARGET_ENTITY, app.myLoginsAppName);
		MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, restPath);
	}

	/**
	 * Generates request id, service name fields and loads them into MDC, as these
	 * values could be empty as these session timeout requests are generated at
	 * scheduled intervals using quartz scheduler.
	 */
	@EPMetricsLog
	public void setRequestId() {
		String requestId = MDC.get(Configuration.MDC_KEY_REQUEST_ID);
		if (StringUtils.isEmpty(requestId)) {
			MDC.put(Configuration.MDC_KEY_REQUEST_ID, UUID.randomUUID().toString());
		}

		MDC.put(Configuration.MDC_SERVICE_NAME, "/quartz/keepSessionAlive");
		MDC.put(EPCommonSystemProperties.PARTNER_NAME, EPCommonSystemProperties.ECOMP_PORTAL_BE);
	}

	/**
	 * Remove the values from MDC as these requests are executed at regular
	 * intervals based on quartz rather incoming REST API requests.
	 * 
	 * @param bAll
	 */
	@EPMetricsLog
	public void clear(Boolean bAll) {
		MDC.remove(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
		if (bAll) {
			MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
			MDC.remove(Configuration.MDC_SERVICE_NAME);
			MDC.remove(EPCommonSystemProperties.PARTNER_NAME);
		}
	}
	
	public Map<String,String> getHeaders(OnboardingApp app)
	{
		String encriptedPwdDB = "";
		String appUserName = "";

		
		 Map<String,String> headersMap = new HashMap<>();
		EPApp externalApp = null;

		if(app.appPassword.isEmpty() || app.appPassword==null){
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering in the externalApp get app password contains null : {}");
			externalApp = appsCacheService.getApp(1L);
			logger.debug(EELFLoggerDelegate.debugLogger, "external App Information : {}",externalApp);

			String mechidUsername=externalApp.getUsername();
			logger.debug(EELFLoggerDelegate.debugLogger, "external App mechidUsername Information : {}",mechidUsername);

			String password=externalApp.getAppPassword();
			String decreptedexternalAppPwd = StringUtils.EMPTY;
			try {
				/*decreptedexternalAppPwd = CipherUtil.decryptPKC(password,
						SystemProperties.getProperty(SystemProperties.Decryption_Key)); */
				decreptedexternalAppPwd = CipherUtil.decryptPKC(password,
						KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
			} catch (CipherUtilException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "failed to decreptedexternalAppPwd when external app pwd is null", e);
			}
			
			appUserName =mechidUsername;
			encriptedPwdDB = decreptedexternalAppPwd;
		
		}else{
			appUserName = app.username;
			encriptedPwdDB = app.appPassword;
		}
		
		headersMap.put("username", appUserName);
		headersMap.put("password", encriptedPwdDB);
		return headersMap;
	}
}