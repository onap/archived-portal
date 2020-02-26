/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (c) 2019 Samsung. All rights reserved.
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.listener.PortalTimeoutHandler;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.web.support.AppUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonSessionCookieUtil {
	
	public static final String EP_SERVICE = "EPService";
	public static Integer cookieMaxAge = -1;
	public static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(CommonSessionCookieUtil.class);
	public static boolean EP_SERVICE_SECURE = false;

	public static void setUpEPServiceCookie(HttpServletRequest request, HttpServletResponse response) throws CipherUtilException, JsonParseException, JsonMappingException, IOException {
		//set up EPService cookie
		EP_SERVICE_SECURE = Boolean.parseBoolean(EPCommonSystemProperties.getProperty(EPCommonSystemProperties.EPSERVICE_COOKIE_SECURE));
		String multifactorauthfrontendurl = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.MULTI_FACTOR_AUTH_FRONTEND_URL);
		String domainName = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.COOKIE_DOMAIN);
		Cookie epCookie = getCookie(request, EP_SERVICE);
		ObjectMapper objectMapper = new ObjectMapper();
		if(epCookie != null) {
			//If cookie already exist then add the current env and jsessionId to this
			String jSessionId = getJessionId(request);
			String jsonValue = URLDecoder.decode(epCookie.getValue(),"UTF-8");
			Map<String,String> valueMap = null;
			// if the value is encoded
			if(jsonValue.startsWith("{")) {
				valueMap = objectMapper.readValue(jsonValue, HashMap.class);
			}else {
				valueMap = new HashMap<>();
			}
			valueMap.put(multifactorauthfrontendurl, CipherUtil.encryptPKC(jSessionId));
			saveOrUpdateEPServiceCookie(response, domainName, objectMapper, valueMap);
		}else {
			//If cookie does not exist create a cookie with current env and jsessionId
		    String jSessionId = getJessionId(request);
		    Map<String,String> valueMap = new HashMap<>();
			valueMap.put(multifactorauthfrontendurl, CipherUtil.encryptPKC(jSessionId));
			saveOrUpdateEPServiceCookie(response, domainName, objectMapper, valueMap);
		}
	}

	private static void saveOrUpdateEPServiceCookie(HttpServletResponse response, String domainName,
			ObjectMapper objectMapper, Map<String, String> valueMap)
			throws UnsupportedEncodingException, JsonProcessingException {
		Cookie cookie1 = new Cookie(EP_SERVICE, URLEncoder.encode(objectMapper.writeValueAsString(valueMap), "UTF-8"));
		cookie1.setMaxAge(cookieMaxAge);
		cookie1.setDomain(domainName);
		cookie1.setPath("/");
		cookie1.setSecure(EP_SERVICE_SECURE);
		response.addCookie(cookie1);
	}
	
	//Get cookie from request object on the basis of cookie name
		private static Cookie getCookie(HttpServletRequest request, String cookieName) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null)
				for (Cookie cookie : cookies)
					if (cookie.getName().equals(cookieName))
						return cookie;

			return null;
		}
		
		protected static void initateSessionMgtHandler(HttpServletRequest request) {
			String jSessionId = getJessionId(request);
			storeMaxInactiveTime(request);
			PortalTimeoutHandler.sessionCreated(jSessionId, jSessionId, AppUtils.getSession(request));
		}
		
		protected static void storeMaxInactiveTime(HttpServletRequest request) {
			HttpSession session = AppUtils.getSession(request);
			if(session.getAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME) == null)
				session.setAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME,session.getMaxInactiveInterval());
		}
		
		public static void resetSessionMaxIdleTimeOut(HttpServletRequest request) {
			try {
				HttpSession session = AppUtils.getSession(request);
				final Object maxIdleAttribute = session.getAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME);
				if(session != null && maxIdleAttribute != null) {
					session.setMaxInactiveInterval(Integer.parseInt(maxIdleAttribute.toString()));
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "resetSessionMaxIdleTimeOut failed", e);
			}
			
		}
		
		public static String getJessionId(HttpServletRequest request){
			return request.getSession().getId();
		}
}