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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.authentication.LoginStrategy;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.controller.FusionBaseController;
import org.onap.portalsdk.core.domain.support.CollaborateList;
import org.onap.portalsdk.core.exception.SessionExpiredException;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
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

		if (method.getBean() instanceof FusionBaseController) {
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
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "preHandle failed", e);
					return false;
				}

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