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
package org.onap.portalapp.authentication;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.model.UserInfo;
import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public class OpenIdConnectLoginStrategy extends org.onap.portalsdk.core.auth.LoginStrategy implements org.onap.portalapp.authentication.LoginStrategy {
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(OpenIdConnectLoginStrategy.class);
	private static final String GLOBAL_LOCATION_KEY = "Location";

	public boolean login(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		logger.info("Attempting Login");												
		
		//check both authentication cookie and authentication header
		UserInfo  userInfo = (UserInfo) request.getAttribute("userInfo");
				
		if (userInfo != null && !StringUtils.isEmpty(userInfo.getPreferredUsername())) {    												
			//package the userid in the login form for processing
			EPLoginBean commandBean = new EPLoginBean();
			commandBean.setOrgUserId(userInfo.getPreferredUsername());

			EPUser user = new EPUser();
				
			user.setOrgUserId(userInfo.getPreferredUsername());
			user.setEmail(userInfo.getEmail());
			user.setFirstName(userInfo.getName());
			user.setLastName(userInfo.getFamilyName());
			
			//store the currently logged in user's information in the session
			EPUserUtils.setUserSession(request, user,  new HashSet(), new HashSet(),null);

			logger.info(EELFLoggerDelegate.errorLogger, request.getContextPath());
			SessionCookieUtil.preSetUp(request, response);	
			return true;
		} else {
			// in case authentication cookie is missing, send 401 UNAUTHORIZED to client and it will redirect to Logon
			try {
				String authentication = SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM);
				String loginUrl = SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL);
				logger.info(EELFLoggerDelegate.errorLogger, "Authentication Mechanism: '" + authentication + "'.");
				
				if (authentication == null || "".equals(authentication) || "OIDC".equals(authentication.trim())) {
				    response.sendRedirect("oid-login");
				} else {
					logger.info(EELFLoggerDelegate.errorLogger, "No cookies are found, redirecting the request to '" + loginUrl + "'.");
					response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			        response.setHeader(GLOBAL_LOCATION_KEY, loginUrl);
			    }
			} catch(Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "login failed", e);
			}
		}
		return false;
	}

	@Override
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) throws PortalAPIException {
		String message = "Method not implmented; Cannot be called";
		logger.error(EELFLoggerDelegate.errorLogger, message);
		throw new PortalAPIException(message);
	}

	@Override
	public String getUserId(HttpServletRequest request) throws PortalAPIException {
		String message = "Method not implmented; Cannot be called";
		logger.error(EELFLoggerDelegate.errorLogger, message);
		throw new PortalAPIException(message);
	}
}
