/*-
 * ================================================================================
 * ECOMP Portal
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
package org.openecomp.portalapp.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.MicroserviceData;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;
import org.openecomp.portalapp.portal.domain.WidgetCatalogParameter;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
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

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceProxyServiceImpl.class);
	private static final String BASIC_AUTH = "Basic Authentication";
	private static final String NO_AUTH = "No Authentication";
	private static final String COOKIE_AUTH = "Cookie based Authentication";
	private static final String QUESTION_MARK = "?";
	private static final String ADD_MARK = "&";

	String whatService = "widgets-service";

	@Autowired
	private ConsulHealthService consulHealthService;

	@Autowired
	MicroserviceService microserviceService;

	@Autowired
	WidgetParameterService widgetParameterService;

	RestTemplate template = new RestTemplate();

	@Override
	public String proxyToDestination(long serviceId, EPUser user, HttpServletRequest request) throws Exception {

		//get the microservice object by the id
		MicroserviceData data = microserviceService.getMicroserviceDataById(serviceId);

		// No such microservice available
		if (data == null) {
			//can we return a better response than null?
			return null;
		}
	
		return authenticateAndRespond(data, request, composeParams(data, user));
	}
	
	@Override
	public String proxyToDestinationByWidgetId(long widgetId, EPUser user, HttpServletRequest request)
			throws Exception {

		String response = null;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ResponseEntity<Long> ans = (ResponseEntity<Long>) template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ consulHealthService.getServiceLocation(whatService,
								SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/parameters/" + widgetId,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), Long.class);
		Long serviceId = ans.getBody();

		// get the microservice object by the id
		MicroserviceData data = microserviceService.getMicroserviceDataById(serviceId);

		// No such microservice available
		if (data == null) {
			return response;
		}

		List<MicroserviceParameter> params = composeParams(data, user);
		for (MicroserviceParameter p : params) {
			WidgetCatalogParameter userValue = widgetParameterService.getUserParamById(widgetId, user.getId(),
					p.getId());
			if (userValue != null)
				p.setPara_value(userValue.getUser_value());
		}
		
		return authenticateAndRespond(data, request, params);

	}

	private String authenticateAndRespond(MicroserviceData data, HttpServletRequest request, List<MicroserviceParameter> params){
		
		String response = null;
		
		if (data.getSecurityType().equals(NO_AUTH)) {
			HttpEntity<String> entity = new HttpEntity<String>(headersForNoAuth());

			String url = microserviceUrlConverter(data, params);
			try {
				logger.debug(EELFLoggerDelegate.debugLogger, "Before making no authentication call: {}", url);
				response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
				logger.debug(EELFLoggerDelegate.debugLogger, "No authentication call response: {}", response);

			} catch (HttpClientErrorException e) {
				throw e;
			}
		} else if (data.getSecurityType().equals(BASIC_AUTH)) {
			// encoding the username and password
			String plainCreds = null;
			try{
				plainCreds = data.getUsername() + ":" + decryptedPassword(data.getPassword());
			}
			catch(Exception e){
				logger.error("problem decrypting password ... check if decryption key is correct in system.properties: ", e);
			}
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			HttpEntity<String> entity = new HttpEntity<String>(headersForBasicAuth(request, base64Creds));

			String url = microserviceUrlConverter(data, params);
			try {
				response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			} catch (HttpClientErrorException e) {
				logger.error("Problem while talking to {1} - message: {2} ", url, e.getMessage());
				throw e;
			}
		} else if (data.getSecurityType().equals(COOKIE_AUTH)) {
			HttpEntity<String> entity = new HttpEntity<String>(headersForCookieAuth(request));
			String url = microserviceUrlConverter(data, params);
			try {
				response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			} catch (HttpClientErrorException e) {
				throw e;
			}
		}
		
		return response;
	}
	
	private String decryptedPassword(String encryptedPwd) throws Exception {
		String result = "";
		if (encryptedPwd != null & encryptedPwd.length() > 0) {
			try {
				result = CipherUtil.decrypt(encryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
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
	
	private HttpHeaders headersForNoAuth(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}
	
	private HttpHeaders headersForBasicAuth(HttpServletRequest request, String base64Creds){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String rawCookie = request.getHeader("Cookie");
		headers.add("Cookie", rawCookie);
		
		return headers;
	}

	private HttpHeaders headersForCookieAuth(HttpServletRequest request){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String rawCookie = request.getHeader("Cookie");
		headers.add("Cookie", rawCookie);
		
		return headers;
	}
	
	private List<MicroserviceParameter> composeParams(MicroserviceData data, EPUser user){
		List<MicroserviceParameter> params = data.getParameterList();
		MicroserviceParameter userId_param = new MicroserviceParameter();
		userId_param.setPara_key("userId");
		userId_param.setPara_value(user.getOrgUserId());
		params.add(userId_param);
		
		return params;
	}
}