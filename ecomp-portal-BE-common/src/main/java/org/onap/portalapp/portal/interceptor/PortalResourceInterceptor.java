/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
 * 
 * Modification Copyright (C) 2018 IBM.
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
package org.onap.portalapp.portal.interceptor;

import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.aaf.cadi.CadiWrap;
import org.onap.portalapp.controller.sessionmgt.SessionCommunicationController;
import org.onap.portalapp.portal.controller.BasicAuthenticationController;
import org.onap.portalapp.portal.controller.ExternalAppsRestfulController;
import org.onap.portalapp.portal.controller.SharedContextRestController;
import org.onap.portalapp.portal.controller.WebAnalyticsExtAppController;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.BasicAuthenticationCredentialService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.service.RemoteWebServiceCallService;
import org.onap.portalapp.service.sessionmgt.ManageService;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.controller.FusionBaseController;
import org.onap.portalsdk.core.exception.UrlAccessRestrictedException;
import org.onap.portalsdk.core.interceptor.ResourceInterceptor;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.listener.PortalTimeoutHandler;
import org.onap.portalsdk.core.onboarding.util.AuthUtil;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

public class PortalResourceInterceptor extends ResourceInterceptor {
	
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalResourceInterceptor.class);

	@Autowired
	private RemoteWebServiceCallService remoteWebServiceCallService;

	@Autowired
	private ManageService manageService;
	
	@Autowired
	AppsCacheService appCacheService;

	@Autowired
	private EPEELFLoggerAdvice epAdvice;
	
	@Autowired
	private AdminRolesService adminRolesService;

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
							EPUser user = (EPUser) request.getSession().getAttribute(
									SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
							//RoleAdmin check is being added because the role belongs to partner application 
							//inorder to access portal api's, bypassing this with isRoleAdmin Check
							if ((EPUserUtils.matchRoleFunctions(portalApiPath, allRoleFunctions)
									&& !EPUserUtils.matchRoleFunctions(portalApiPath, roleFunctions)) && !adminRolesService.isRoleAdmin(user)) {
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
					if (!remoteWebServiceCallService.verifyRESTCredential(secretKey, request.getHeader(EPCommonSystemProperties.UEB_KEY),
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
				if (!remoteWebServiceCallService.verifyAppKeyCredential(request.getHeader(EPCommonSystemProperties.UEB_KEY))) {
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

		final String authHeader = request.getHeader(EPCommonSystemProperties.AUTHORIZATION);
		final String uebkey = request.getHeader(EPCommonSystemProperties.UEB_KEY);
		try{
			CadiWrap wrapReq = (CadiWrap) request;
				logger.debug(EELFLoggerDelegate.debugLogger, "Entering in the loop as the uri contains auxapi : {}");
				String nameSpace=PortalApiProperties.getProperty(PortalApiConstants.AUTH_NAMESPACE);
				logger.debug(EELFLoggerDelegate.debugLogger, "namespace form the portal properties : {}",nameSpace);
				Boolean accessallowed=AuthUtil.isAccessAllowed(request, nameSpace, new HashMap<>());
				logger.debug(EELFLoggerDelegate.debugLogger, "AccessAllowed for the request and namespace : {}",accessallowed);
				if(accessallowed){
					logger.debug(EELFLoggerDelegate.debugLogger, "AccessAllowed is allowed: {}",accessallowed);

					//String[] accountNamePassword = EcompPortalUtils.getUserNamePassword(authHeader);
					//check ueb condition
					if(uebkey !=null && !uebkey.isEmpty())
					{
						EPApp application = appCacheService.getAppFromUeb(uebkey,1);
						if (application == null) {
							throw new Exception("Invalid credentials!");
						}
						else {
							final String appUsername = application.getAppBasicAuthUsername();
							logger.debug(EELFLoggerDelegate.debugLogger, "appUsername : {}",appUsername);

							String[] accountNamePassword = EcompPortalUtils.getUserNamePassword(authHeader);
							logger.debug(EELFLoggerDelegate.debugLogger, "accountNamePassword : {}",accountNamePassword);

							if (accountNamePassword == null || accountNamePassword.length != 2) {
								final String msg = "failed to get username and password from Atuhorization header";
								logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth Username and password failed to get: {}", msg);
								sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
								return false;
							}
							if (appUsername.equals(accountNamePassword[0])) {
								return true;
							}else{
								final String msg = "failed to match the UserName from the application ";
								logger.debug(EELFLoggerDelegate.debugLogger, "failed to match the UserName from the application checkBasicAuth Username and password failed to get: {}", msg);
								sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
								return false;
							}
						}
					}

					return true;	
				}
				if(!accessallowed){
					final String msg = "no authorization found";
					logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth when no accessallowed: {}", msg);
					sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
					return false;
				}
				return false;
			
		}catch(ClassCastException e){
			logger.debug(EELFLoggerDelegate.debugLogger, "Entering in the classcastexception block if the UN is not the mechid : {}");

			String secretKey = null;
			// Unauthorized access due to missing HTTP Authorization request header
			if (authHeader == null) {
				if (remoteWebServiceCallService.verifyRESTCredential(secretKey, request.getHeader(EPCommonSystemProperties.UEB_KEY),
						request.getHeader("username"), request.getHeader("password"))) {
					return true;
				}
				final String msg = "no authorization found";
				logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
				sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
				return false;
			}

			String[] accountNamePassword = EcompPortalUtils.getUserNamePassword(authHeader);
			if (accountNamePassword == null || accountNamePassword.length != 2) {
				final String msg = "failed to get username and password from Atuhorization header";
				logger.debug(EELFLoggerDelegate.debugLogger, "checkBasicAuth: {}", msg);
				sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, msg);
				return false;
			}

			if(uebkey !=null && !uebkey.isEmpty())
			{
				EPApp application = appCacheService.getAppFromUeb(uebkey,1);
				if (application == null) {
					throw new Exception("Invalid credentials!");
				}
				else {
					final String appUsername = application.getAppBasicAuthUsername();
					final String dbDecryptedPwd = CipherUtil.decryptPKC(application.getAppBasicAuthPassword());
					if (appUsername.equals(accountNamePassword[0]) && dbDecryptedPwd.equals(accountNamePassword[1])) {
						return true;
					}
				}
			}

			
			BasicAuthCredentials creds;
			try {
				creds = basicAuthService.getBasicAuthCredentialByUsernameAndPassword(accountNamePassword[0],
						accountNamePassword[1]);
			} catch (Exception e1) {
				logger.error(EELFLoggerDelegate.errorLogger, "checkBasicAuth failed to get credentials", e1);
				final String msg = "Failed while getting basic authentication credential: ";
				sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
				throw e1;
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
		
		}catch (Exception e2) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkBasicAuth failed to get credentials for some other exception", e2);
			final String msg = "Failed while getting basic authentication credential for some other exception: ";
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
			throw e2;
		}
		return true;


}

	@SuppressWarnings("unused")
	private String decrypted(String encrypted) throws Exception {
		String result = "";
		if (encrypted != null && encrypted.length() > 0) {
			try {
				result = CipherUtil.decryptPKC(encrypted, SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	private String encrypted(String decryptedPwd) throws Exception {
		String result = "";
		if (decryptedPwd != null && decryptedPwd.length() > 0) {
			try {
				result = CipherUtil.encryptPKC(decryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword() failed", e);
				throw e;
			}
		}
		return result;
	}

	protected void handleSessionUpdates(HttpServletRequest request) {
		PortalTimeoutHandler.handleSessionUpdatesNative(request, null, null, null, null, manageService);
	}
	
}
