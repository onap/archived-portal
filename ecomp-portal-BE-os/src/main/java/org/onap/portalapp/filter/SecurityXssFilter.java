/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portalapp.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.web.filter.OncePerRequestFilter;

public class SecurityXssFilter extends OncePerRequestFilter {

	private EELFLoggerDelegate sxLogger = EELFLoggerDelegate.getLogger(SecurityXssFilter.class);

	private static final String APPLICATION_JSON = "application/json";

	private static final String ERROR_BAD_REQUEST = "{\"error\":\"BAD_REQUEST\"}";

	private SecurityXssValidator validator = SecurityXssValidator.getInstance();

	public class RequestWrapper extends HttpServletRequestWrapper {

		private ByteArrayOutputStream cachedBytes;

		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			if (cachedBytes == null)
				cacheInputStream();

			return new CachedServletInputStream();
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		private void cacheInputStream() throws IOException {
			cachedBytes = new ByteArrayOutputStream();
			IOUtils.copy(super.getInputStream(), cachedBytes);
		}

		public class CachedServletInputStream extends ServletInputStream {
			private ByteArrayInputStream input;

			public CachedServletInputStream() {
				input = new ByteArrayInputStream(cachedBytes.toByteArray());
			}

			@Override
			public int read() throws IOException {
				return input.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// do nothing
			}
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();
		String requestUrl;

		if (queryString == null) {
			requestUrl = requestURL.toString();
		} else {
			requestUrl = requestURL.append('?').append(queryString).toString();
		}

		validateRequest(requestUrl, response);
		StringBuilder headerValues = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			headerValues.append(value);
		}

		validateRequest(headerValues.toString(), response);

		if (validateRequestType(request)) {
			request = new RequestWrapper(request);
			String requestData = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.toString());
			validateRequest(requestData, response);
		}

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			sxLogger.warn(EELFLoggerDelegate.errorLogger, "Handling bad request", e);
			response.sendError(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "Handling bad request");
		}
	}

	private boolean validateRequestType(HttpServletRequest request) {
		return (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT")
				|| request.getMethod().equalsIgnoreCase("DELETE"));
	}
	
	private void validateRequest(String text, HttpServletResponse response) throws IOException {
		try {
			if (StringUtils.isNotBlank(text) && validator.denyXSS(text)) {
				response.setContentType(APPLICATION_JSON);
				response.setStatus(HttpStatus.SC_BAD_REQUEST);
				response.getWriter().write(ERROR_BAD_REQUEST);
				throw new SecurityException(ERROR_BAD_REQUEST);
			}
		} catch (Exception e) {
			sxLogger.error(EELFLoggerDelegate.errorLogger, "doFilterInternal() failed due to BAD_REQUEST", e);
			response.getWriter().close();
		}
	}
}
