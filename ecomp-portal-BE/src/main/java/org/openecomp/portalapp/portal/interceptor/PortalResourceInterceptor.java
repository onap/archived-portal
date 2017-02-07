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
package org.openecomp.portalapp.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.sessionmgt.SessionCommunicationController;
import org.openecomp.portalapp.portal.controller.ExternalAppsRestfulController;
import org.openecomp.portalapp.portal.controller.SharedContextRestController;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.service.sessionmgt.ManageService;
import org.openecomp.portalapp.service.sessionmgt.RemoteWebServiceCallService;
import org.openecomp.portalsdk.core.exception.UrlAccessRestrictedException;
import org.openecomp.portalsdk.core.interceptor.ResourceInterceptor;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalTimeoutHandler;
import org.openecomp.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PortalResourceInterceptor extends ResourceInterceptor {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalResourceInterceptor.class);

	@Autowired
	public RemoteWebServiceCallService remoteWebServiceCallService;

	@Autowired
	public ManageService manageService;
	
	@Autowired
	private EPEELFLoggerAdvice epAdvice;

	static ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Object controllerObj = method.getBean();
			/**
			 * These classes provide REST endpoints used by other application
			 * servers, NOT by an end user's browser.
			 */
			if (controllerObj instanceof SessionCommunicationController
					|| controllerObj instanceof SharedContextRestController
					|| controllerObj instanceof ExternalAppsRestfulController) {
				// check user authentication for RESTful calls
				String secretKey = null;
				try {
					epAdvice.loadServletRequestBasedDefaults(request, SecurityEventTypeEnum.INCOMING_REST_MESSAGE);
					if (!remoteWebServiceCallService.verifyRESTCredential(secretKey, request.getHeader("uebkey"),
							request.getHeader("username"), request.getHeader("password"))) {
						throw new UrlAccessRestrictedException();
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "Error authenticating RESTful service. Details: " + EcompPortalUtils.getStackTrace(e));
					EPLogUtil.logEcompError(EPAppMessagesEnum.BeRestApiAuthenticationError);
					throw new UrlAccessRestrictedException();
				}
			}
		}

		handleSessionUpdates(request);
		return true;
	}

	protected void handleSessionUpdates(HttpServletRequest request) {
		PortalTimeoutHandler.handleSessionUpdatesNative(request, null, null, null, null, manageService);
	}
}
