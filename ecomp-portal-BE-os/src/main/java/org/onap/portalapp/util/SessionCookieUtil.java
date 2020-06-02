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
package org.onap.portalapp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.KeyConstants;
import org.onap.portalsdk.core.onboarding.util.KeyProperties;
import org.onap.portalsdk.core.util.SystemProperties;

public class SessionCookieUtil extends CommonSessionCookieUtil{
	
	//private static final String JSESSIONID = "JSESSIONID";
	private static final String USER_ID = "UserId";
	
	public static void preSetUp(HttpServletRequest request,
			HttpServletResponse response) {
		initateSessionMgtHandler(request);
		//set up EPService cookie
		setUpEPServiceCookie(request, response);
	}

	public static void setUpEPServiceCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String jSessionId = getJessionId(request);
		Cookie cookie1 = new Cookie(EP_SERVICE, jSessionId);
		cookie1.setSecure(true);
		cookie1.setMaxAge(cookieMaxAge);
		cookie1.setDomain(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.COOKIE_DOMAIN));
		cookie1.setPath("/");
		response.addCookie(cookie1);
	}
	
	public static void setUpUserIdCookie(HttpServletRequest request,
			HttpServletResponse response,String userId) throws Exception {
		logger.info("************** session cookie util set up UserId cookie begins");
		userId = CipherUtil.encrypt(userId,
				KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
		Cookie cookie1 = new Cookie(USER_ID, userId);
		cookie1.setSecure(true);
		cookie1.setMaxAge(cookieMaxAge);
		cookie1.setDomain(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.COOKIE_DOMAIN));
		cookie1.setPath("/");
		response.addCookie(cookie1);
		logger.info("************** session cookie util set up EP cookie completed");
	}
	
	public static String getUserIdFromCookie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userId = "";
		Cookie[] cookies = request.getCookies();
		Cookie userIdcookie = null;
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(USER_ID))
					userIdcookie = cookie;
		if(userIdcookie!=null){
			userId = CipherUtil.decrypt(userIdcookie.getValue(),
					KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
		}
		
		logger.info("************** session cookie util set up EP cookie completed");
		return userId;
	}
}
