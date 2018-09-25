/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.portalapp.portal.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.portalapp.music.service.MusicService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;


public final class MusicCookieCsrfTokenRepository implements CsrfTokenRepository {
	static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
	static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	static final String CSRF_PARAMETER_NAME = "_csrf";
	static final String EP_SERVICE = "EPService";
	CookieCsrfTokenRepository cookieRepo = null;
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicCookieCsrfTokenRepository.class);
	public MusicCookieCsrfTokenRepository() {
	}
	
	public MusicCookieCsrfTokenRepository(CookieCsrfTokenRepository _cookieRepo) {
		this();
		cookieRepo = _cookieRepo;
	}

	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		return cookieRepo.generateToken(request) ;
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug(EELFLoggerDelegate.debugLogger, "initialize save csrf token ...");
		cookieRepo.saveToken(token, request, response);
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		logger.debug(EELFLoggerDelegate.debugLogger, "initialize load csrf token ...");
		CsrfToken cookieRepoToken = cookieRepo.loadToken(request);
		if(cookieRepoToken==null){ // if cookieRepo does not has the token, check the cassandra for the values stored by other tomcats
			try { // todo this part of the code needs to be replaced with out depending on EPService cookie
				String sessionId = getSessionIdFromCookie(request);
				if(sessionId == null) 
					return null;
				String token = MusicService.getAttribute(CSRF_COOKIE_NAME, sessionId);
				if (token==null || !StringUtils.hasLength(token)) 
					return null;
				cookieRepoToken = new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAMETER_NAME , token); 
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Error while calling csrf loadToken" , e);
			}
		}
		return cookieRepoToken;
	}

	/**
	 * Factory method to conveniently create an instance that has
	 * {@link #setCookieHttpOnly(boolean)} set to false.
	 *
	 * @return an instance of CookieCsrfTokenRepository with
	 * {@link #setCookieHttpOnly(boolean)} set to false
	 */
	public static MusicCookieCsrfTokenRepository withHttpOnlyFalse() {
		CookieCsrfTokenRepository result = new CookieCsrfTokenRepository();
		result.setCookieHttpOnly(false);
		return new MusicCookieCsrfTokenRepository(result);
	}
	
	private String getSessionIdFromCookie (HttpServletRequest request){
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (EP_SERVICE.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
