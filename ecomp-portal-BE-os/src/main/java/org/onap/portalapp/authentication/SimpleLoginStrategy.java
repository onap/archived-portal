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
package org.onap.portalapp.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPRoleFunctionService;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.menu.MenuProperties;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public class SimpleLoginStrategy extends org.onap.portalsdk.core.auth.LoginStrategy implements LoginStrategy{
	
	@Autowired
	private EPLoginService loginService;

	@Autowired
	private EPRoleFunctionService ePRoleFunctionService;
	
	private static final String GLOBAL_LOCATION_KEY = "Location";
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SimpleLoginStrategy.class);
	
	public boolean login(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("Attempting 'Simple' Login");												
		
		//check both authentication cookie and authentication header
		String  orgUserId = SessionCookieUtil.getUserIdFromCookie(request, response);
		
		if (!StringUtils.isEmpty(orgUserId)) {    												
			// package the userid in the login form for processing
			EPLoginBean commandBean = new EPLoginBean();
			commandBean.setOrgUserId(orgUserId);
			commandBean = loginService.findUser(commandBean, (String)request.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY), null);

			 // in case authentication has passed but user is not in the ECOMP data base, return a Guest User to the home page.
			if (commandBean.getUser() == null) {
			}
			else {
				// store the currently logged in user's information in the session
				EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(), commandBean.getBusinessDirectMenu(), "", ePRoleFunctionService);
				logger.info(EELFLoggerDelegate.debugLogger, commandBean.getUser().getOrgUserId() + " exists in the the system.");
			}
			
			logger.info(EELFLoggerDelegate.errorLogger, request.getContextPath());
			SessionCookieUtil.preSetUp(request, response);
			return true;
		} else {
			// in case authentication cookie is missing, send 401 UNAUTHORIZED to client and it will redirect to Logon
			try {
				String authentication = SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM);
				String loginUrl = SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL);
				logger.info(EELFLoggerDelegate.errorLogger, "Authentication Mechanism: '" + authentication + "'.");
				if (authentication == null || authentication.equals("") || authentication.trim().equals("BOTH")) {
				
					logger.info(EELFLoggerDelegate.errorLogger, "No cookies are found, redirecting the request to '" + loginUrl + "'.");
				    response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		            response.setHeader(GLOBAL_LOCATION_KEY, loginUrl); //returnUrl + "/index.htm");
		        }else {
					logger.info(EELFLoggerDelegate.errorLogger, "No cookies are found, redirecting the request to '" + loginUrl + "'.");
					response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			        response.setHeader(GLOBAL_LOCATION_KEY, loginUrl); //returnUrl + "/index.htm");
			    }
			} catch(Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "login failed", e);
			}
		}

		return false;

	}

	@Override
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = "Method not implmented; Cannot be called";
		logger.error(EELFLoggerDelegate.errorLogger, message);
		throw new Exception(message);
	}

	@Override
	public String getUserId(HttpServletRequest request) throws PortalAPIException {
		String message = "Method not implmented; Cannot be called";
		logger.error(EELFLoggerDelegate.errorLogger, message);
		throw new PortalAPIException(message);
	}
}
