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
package org.onap.portalapp.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.KeyConstants;
import org.onap.portalsdk.core.onboarding.util.KeyProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service("microserviceProxyService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class MicroserviceProxyServiceImpl implements MicroserviceProxyService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceProxyServiceImpl.class);

	private static final String BASIC_AUTH = "Basic Authentication";
	private static final String NO_AUTH = "No Authentication";
	private static final String COOKIE_AUTH = "Cookie based Authentication";
	private static final String QUESTION_MARK = "?";
	private static final String ADD_MARK = "&";

	@Autowired
	private WidgetMService widgetMService;
	@Autowired
	MicroserviceService microserviceService;
	@Autowired
	WidgetParameterService widgetParameterService;

	private String whatService = "widgets-service";

	private RestTemplate template = new RestTemplate();

	@Override
	public String proxyToDestination(long serviceId, EPUser user, HttpServletRequest request) throws Exception {
		// get the microservice object by the id
		MicroserviceData data = microserviceService.getMicroserviceDataById(serviceId);
		// No such microservice available
		if (data == null) {
			// can we return a better response than null?
			return null;
		}
		return authenticateAndRespond(data, request, composeParams(data, user));
	}

	@Override
	public String proxyToDestinationByWidgetId(long widgetId, EPUser user, HttpServletRequest request)
			throws Exception {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ResponseEntity<Long> ans = (ResponseEntity<Long>) template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ widgetMService.getServiceLocation(whatService,
								SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/parameters/" + widgetId,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), Long.class);
		Long serviceId = ans.getBody();
		// get the microservice object by the id
		MicroserviceData data = microserviceService.getMicroserviceDataById(serviceId);
		// No such microservice available
		if (data == null)
			return null;

		List<MicroserviceParameter> params = composeParams(data, user);
		for (MicroserviceParameter p : params) {
			WidgetCatalogParameter userValue = widgetParameterService.getUserParamById(widgetId, user.getId(),
					p.getId());
			if (userValue != null)
				p.setPara_value(userValue.getUser_value());
		}
		return authenticateAndRespond(data, request, params);
	}

	private String authenticateAndRespond(MicroserviceData data, HttpServletRequest request,
			List<MicroserviceParameter> params) throws HttpClientErrorException, IllegalArgumentException {
		String response = null;
		if (data.getSecurityType().equals(NO_AUTH)) {
			HttpEntity<String> entity = new HttpEntity<String>(headersForNoAuth());
			String url = microserviceUrlConverter(data, params);
			logger.debug(EELFLoggerDelegate.debugLogger,
					"authenticateAndRespond: Before making no authentication call: {}", url);
			response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			logger.debug(EELFLoggerDelegate.debugLogger, "authenticateAndRespond: No authentication call response: {}",
					response);
		} else if (data.getSecurityType().equals(BASIC_AUTH)) {
			// encoding the username and password
			String plainCreds = null;
			try {
				plainCreds = data.getUsername() + ":" + decryptedPassword(data.getPassword());
			} catch (Exception e) {
				logger.error("authenticateAndRespond failed to decrypt password", e);
				throw new IllegalArgumentException("Failed to decrypt password", e);
			}
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			HttpEntity<String> entity = new HttpEntity<String>(headersForBasicAuth(request, base64Creds));

			String url = microserviceUrlConverter(data, params);
			try {
				response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			} catch (HttpClientErrorException e) {
				logger.error("authenticateAndRespond failed for basic security url " + url, e);
				throw e;
			}
		} else if (data.getSecurityType().equals(COOKIE_AUTH)) {
			HttpEntity<String> entity = new HttpEntity<String>(headersForCookieAuth(request));
			String url = microserviceUrlConverter(data, params);
			try {
				response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			} catch (HttpClientErrorException e) {
				logger.error("authenticateAndRespond failed for cookie auth url " + url, e);
				throw e;
			}
		}

		return response;
	}

	private String decryptedPassword(String encryptedPwd) throws Exception {
		String result = "";
		if (encryptedPwd != null && encryptedPwd.length() > 0) {
			try {
				result = CipherUtil.decryptPKC(encryptedPwd,
						KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}

		return result;
	}

	private String microserviceUrlConverter(MicroserviceData data, List<MicroserviceParameter> params) {
		String url = data.getUrl();
		for (int i = 0; i < params.size(); i++) {
			if (i == 0) {
				url += QUESTION_MARK;
			}
			url += params.get(i).getPara_key() + "=" + params.get(i).getPara_value();
			if (i != (params.size() - 1)) {
				url += ADD_MARK;
			}
		}

		return url;
	}

	private HttpHeaders headersForNoAuth() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	// TODO: why is this generically named cookie used?
	private final static String Cookie = "Cookie";
	
	private HttpHeaders headersForBasicAuth(HttpServletRequest request, String base64Creds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String rawCookie = request.getHeader(Cookie);
		if (rawCookie != null)
			headers.add(Cookie, rawCookie);
		return headers;
	}

	private HttpHeaders headersForCookieAuth(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String rawCookie = request.getHeader(Cookie);
		if (rawCookie != null)
			headers.add(Cookie, rawCookie);
		return headers;
	}

	private List<MicroserviceParameter> composeParams(MicroserviceData data, EPUser user) {
		List<MicroserviceParameter> params = data.getParameterList();
		MicroserviceParameter userIdParam = new MicroserviceParameter();
		userIdParam.setPara_key("userId");
		userIdParam.setPara_value(user.getOrgUserId());
		params.add(userIdParam);
		return params;
	}
}