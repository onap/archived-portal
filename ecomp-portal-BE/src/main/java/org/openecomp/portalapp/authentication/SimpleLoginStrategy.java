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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuProperties;
import org.openecomp.portalsdk.core.onboarding.crossapi.ECOMPSSO;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class SimpleLoginStrategy implements LoginStrategy{
	
	@Autowired
	private EPLoginService loginService;

	private static final String GLOBAL_LOCATION_KEY = "Location";
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SimpleLoginStrategy.class);
	
	public boolean login(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("Attempting 'Simple' Login");												
		
		//check both authentication cookie and authentication header
		String  orgUserId = null;
		try{
			 orgUserId = ECOMPSSO.getUserIdFromCookie(request);
		} catch(Exception ex){
			logger.error(EELFLoggerDelegate.errorLogger, "Error getting User ID: '" + ex.getLocalizedMessage() + "'.");
		}
		
		
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
				EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(), commandBean.getBusinessDirectMenu(), "");
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
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred in preHandle() while redirecting, Details: " + EcompPortalUtils.getStackTrace(e));
			}
		}

		return false;

	}
}
