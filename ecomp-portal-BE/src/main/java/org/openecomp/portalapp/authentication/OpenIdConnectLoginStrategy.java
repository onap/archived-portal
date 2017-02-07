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
package org.openecomp.portalapp.authentication;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.model.UserInfo;
import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.util.StringUtils;

public class OpenIdConnectLoginStrategy implements LoginStrategy {
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(OpenIdConnectLoginStrategy.class);
	
	private static final String GLOBAL_LOCATION_KEY = "Location";

	@SuppressWarnings("rawtypes")
	public boolean login(HttpServletRequest request, HttpServletResponse response){
		
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
			EPUserUtils.setUserSession(request, user,  new HashSet(), new HashSet(), SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM));

			logger.info(EELFLoggerDelegate.errorLogger, request.getContextPath());
			SessionCookieUtil.preSetUp(request, response);	
			return true;
		} else {
			// in case authentication cookie is missing, send 401 UNAUTHORIZED to client and it will redirect to Logon
			
			try {
				String authentication = SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM);
				String loginUrl = SystemProperties.getProperty(EPSystemProperties.LOGIN_URL_NO_RET_VAL);
				logger.info(EELFLoggerDelegate.errorLogger, "Authentication Mechanism: '" + authentication + "'.");
				
				if (authentication == null || authentication.equals("") || authentication.trim().equals("OIDC")) {				
				    response.sendRedirect("oid-login");
				} else {
					logger.info(EELFLoggerDelegate.errorLogger, "No cookies are found, redirecting the request to '" + loginUrl + "'.");
					response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			        response.setHeader(GLOBAL_LOCATION_KEY, loginUrl);
			    }
			} catch(Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred in preHandle() while redirecting, Details: " + EcompPortalUtils.getStackTrace(e));
			}
		}
		return false;
	}
}
