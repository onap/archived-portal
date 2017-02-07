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

import org.openecomp.portalapp.authentication.LoginStrategy;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
import org.openecomp.portalsdk.core.controller.FusionBaseController;
import org.openecomp.portalsdk.core.domain.support.CollaborateList;
import org.openecomp.portalsdk.core.exception.SessionExpiredException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionTimeoutInterceptor extends HandlerInterceptorAdapter {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SessionTimeoutInterceptor.class);

	@Autowired
	private LoginStrategy loginStrategy;

	public SessionTimeoutInterceptor() {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!isHandlerMethod(handler))
			return false;

		HandlerMethod method = (HandlerMethod) handler;

		if (!isFusionController(method.getBean()))
			return false;

		FusionBaseController controller = (FusionBaseController) method.getBean();

		if (!controller.isAccessible()) {
			try {
				EPUser user = EPUserUtils.getUserSession(request);

				if (request.getRequestURI().indexOf("logout.htm") > -1) {
					CollaborateList.delUserName(user.getOrgUserId());
					throw new SessionExpiredException();
				} else {
					resetSessionMaxIdleTimeOut(request);
					CollaborateList.addUserName(user.getOrgUserId());
				}
			} catch (Exception see) {
				logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(see));
				return loginStrategy.login(request, response);
			}

		}
		
		return true;
	}

	private void resetSessionMaxIdleTimeOut(HttpServletRequest request) {
		SessionCookieUtil.resetSessionMaxIdleTimeOut(request);

	}

	private boolean isFusionController(Object controller) {
		if (controller instanceof FusionBaseController)
			return true;
		return false;
	}

	private boolean isHandlerMethod(Object controller) {
		if (controller instanceof HandlerMethod)
			return true;
		return false;
	}
}
