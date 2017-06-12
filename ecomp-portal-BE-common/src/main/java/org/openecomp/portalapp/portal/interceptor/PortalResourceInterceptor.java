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
 * ================================================================================*/
package org.openecomp.portalapp.portal.interceptor;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.sessionmgt.SessionCommunicationController;
import org.openecomp.portalapp.portal.controller.BasicAuthenticationController;
import org.openecomp.portalapp.portal.controller.ExternalAppsRestfulController;
import org.openecomp.portalapp.portal.controller.SharedContextRestController;
import org.openecomp.portalapp.portal.controller.WebAnalyticsExtAppController;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.service.BasicAuthenticationCredentialService;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.service.RemoteWebServiceCallService;
import org.openecomp.portalapp.service.sessionmgt.ManageService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.controller.FusionBaseController;
import org.openecomp.portalsdk.core.exception.UrlAccessRestrictedException;
import org.openecomp.portalsdk.core.interceptor.ResourceInterceptor;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.listener.PortalTimeoutHandler;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

public class PortalResourceInterceptor extends ResourceInterceptor {
	private static final String APP_KEY = "uebkey";

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalResourceInterceptor.class);

	@Autowired
	private RemoteWebServiceCallService remoteWebServiceCallService;

	@Autowired
	private ManageService manageService;

	@Autowired
	private EPEELFLoggerAdvice epAdvice;

	@Autowired
	private BasicAuthenticationCredentialService basicAuthService;

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;

			/**
			 * These classes provide REST endpoints used by other application
			 * servers, NOT by an end user's browser.
			 */
			if (method.getBean() instanceof FusionBaseController) {
				FusionBaseController controller = (FusionBaseController) method.getBean();
				if (!controller.isAccessible()) {

					// authorize portalApi requests by user role
					String requestURI = request.getRequestURI();
					if (requestURI != null) {
						String[] uriArray = requestURI.split("/portalApi/");
						if (uriArray.length > 1) {
							String portalApiPath = uriArray[1];

							Set<? extends String> roleFunctions = (Set<? extends String>) request.getSession()
									.getAttribute(SystemProperties
											.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));
							Set<? extends String> allRoleFunctions = (Set<? extends String>) request.getSession()
									.getAttribute(EPUserUtils.ALL_ROLE_FUNCTIONS);
							// Defend against code error to avoid throwing NPE
							if (roleFunctions == null || allRoleFunctions == null) {
								logger.error(EELFLoggerDelegate.errorLogger,
										"preHandle: failed to get role functions attribute(s) from session!!");
								EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInitializationError);
								return false;
							}
							// check to see if roleFunctions of the user is in
							// the
							// list of all role functions
							// if not, ignore to prevent restricting every
							// trivial
							// call; otherwise, if it is, then check for the
							// access
							if (matchRoleFunctions(portalApiPath, allRoleFunctions)
									&& !matchRoleFunctions(portalApiPath, roleFunctions)) {
								EPUser user = (EPUser) request.getSession().getAttribute(
										SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
								logger.error(EELFLoggerDelegate.errorLogger,
										"preHandle: User {} not authorized for path {} ", user.getOrgUserId(),
										portalApiPath);
								EcompPortalUtils.setBadPermissions(user, response, portalApiPath);
								EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiAuthenticationError);
								return false;
							} // failed to match

						} // is portalApi

					} // requestURI
				} // instance check
			} // not accessible
			else if (method.getBean() instanceof BasicAuthenticationController) {
				return checkBasicAuth(request, response);
			}
			Object controllerObj = method.getBean();
			if (controllerObj instanceof SessionCommunicationController
					|| controllerObj instanceof SharedContextRestController
					|| controllerObj instanceof ExternalAppsRestfulController) {
				// check user authentication for RESTful calls
				String secretKey = null;
				try {
					epAdvice.loadServletRequestBasedDefaults(request, SecurityEventTypeEnum.INCOMING_REST_MESSAGE);
					if (!remoteWebServiceCallService.verifyRESTCredential(secretKey, request.getHeader(APP_KEY),
							request.getHeader("username"), request.getHeader("password"))) {
						throw new UrlAccessRestrictedException();
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "preHandle: failed to authenticate RESTful service",
							e);
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiAuthenticationError, e);
					throw new UrlAccessRestrictedException();
				}
			}

			if (controllerObj instanceof WebAnalyticsExtAppController) {
				if (!remoteWebServiceCallService.verifyAppKeyCredential(request.getHeader(APP_KEY))) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"preHandle: failed to verify app key for web analytics call");
					throw new UrlAccessRestrictedException();
				}
			}
		}

		handleSessionUpdates(request);
		return true;
	}

	/**
	 * Sets the status code and sends a response. Factors code out of many
	 * methods.
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param statusCode
	 *            HTTP status code like 404
	 * @param message
	 *            Message to send in a JSON error object
	 */
	private void sendErrorResponse(HttpServletResponse response, final int statusCode, final String message)
			throws Exception {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\":\"" + message + "\"}");
		response.getWriter().flush();
	}

	/**
	 * Gets HTTP basic authentication information from the request and checks
	 * whether those credentials are authorized for the request path.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return True if the request is authorized, else false
	 * @throws Exception
	 */
	private boolean checkBasicAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uri = request.getRequestURI().toString();
		uri = uri.substring(uri.indexOf("/", 1));

		final String authHeader = request.getHeader("Authorization");

		// Unauthorized access due to missing HTTP Authorization request header
		if (authHeader == null) {
			final String msg = "no authorization found";
			logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
			return false;
		}

		String[] accountNamePassword = getUserNamePassword(authHeader);
		if (accountNamePassword == null || accountNamePassword.length != 2) {
			final String msg = "failed to get username and password from Atuhorization header";
			logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
			return false;
		}

		BasicAuthCredentials creds;
		try {
			creds = basicAuthService.getBasicAuthCredentialByUsernameAndPassword(accountNamePassword[0],
					encrypted(accountNamePassword[1]));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkBasicAuth failed to get credentials", e);
			final String msg = "Failed while getting basic authentication credential: " + e.toString();
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
			throw e;
		}

		// Unauthorized access due to invalid credentials (username and
		// password)
		if (creds == null || !creds.getUsername().equals(accountNamePassword[0])) {
			final String msg = "Unauthorized: Access denied";
			logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
			return false;
		}

		// Unauthorized access due to inactive account
		if (creds.getIsActive().equals("N")) {
			final String msg = "Unauthorized: The account is inactive";
			logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
			return false;
		}
		boolean isAllowedEp = false;
		for (EPEndpoint ep : creds.getEndpoints()) {
			if (ep.getName().equals(uri)) {
				isAllowedEp = true;
				break;
			}
		}

		// If user doesn't specify any endpoint, allow all endpoints for that
		// account
		if (creds.getEndpoints().size() == 0)
			isAllowedEp = true;

		// Unauthorized access due to the invalid endpoints
		if (!isAllowedEp) {
			final String msg = "Unauthorized: Endpoint access denied";
			logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
			return false;
		}

		// Made it to the end!
		return true;
	}

	private String[] getUserNamePassword(String authValue) {
		String base64Credentials = authValue.substring("Basic".length()).trim();
		String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
		final String[] values = credentials.split(":", 2);
		return values;
	}

	private String decrypted(String encrypted) throws Exception {
		String result = "";
		if (encrypted != null & encrypted.length() > 0) {
			try {
				result = CipherUtil.decrypt(encrypted, SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	private String encrypted(String decryptedPwd) throws Exception {
		String result = "";
		if (decryptedPwd != null & decryptedPwd.length() > 0) {
			try {
				result = CipherUtil.encrypt(decryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword() failed", e);
				throw e;
			}
		}
		return result;
	}

	private Boolean matchRoleFunctions(String portalApiPath, Set<? extends String> roleFunctions) {
		for (String roleFunction : roleFunctions) {
			if (portalApiPath.matches(roleFunction))
				return true;
		}
		return false;

	}

	protected void handleSessionUpdates(HttpServletRequest request) {
		PortalTimeoutHandler.handleSessionUpdatesNative(request, null, null, null, null, manageService);
	}
}
