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

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.impl.ResponseImpl;
import org.apache.cxf.transport.http.HTTPException;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.SystemType;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@Service("applicationsRestClientService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ApplicationsRestClientServiceImpl implements ApplicationsRestClientService {

	private static final String PASSWORD_HEADER = "password";

	private static final String APP_USERNAME_HEADER = "username";

	private static final String BASIC_AUTHENTICATION_HEADER = "Authorization";

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ApplicationsRestClientServiceImpl.class);

	@Autowired
	private AppsCacheService appsCacheService;

	Gson gson = null;

	private final ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	private void init() {
		logger.debug(EELFLoggerDelegate.debugLogger, "initializing");
		GsonBuilder builder = new GsonBuilder();

		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});

		gson = builder.create();
	}

	// TODO: do we need to do additional logging for remote API calls?
	private static WebClient createClientForPath(String baseUri, String path) {
		logger.info(EELFLoggerDelegate.debugLogger, "Creating web client for " + baseUri + "   +   " + path);
		WebClient client = WebClient.create(baseUri);
		client.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		client.path(path);
		return client;
	}

	@EPMetricsLog
	private void verifyResponse(Response response) throws HTTPException {
		int status = response.getStatus();
		logger.debug(EELFLoggerDelegate.debugLogger, "http response status=" + status);
		MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE, Integer.toString(status));
		if (!isHttpSuccess(status)) {
			String errMsg = "Failed. Status=" + status + "; [" + response.getStatusInfo().getReasonPhrase().toString()
					+ "]";
			URL url = null;
			try {
				// must not be null to avoid NPE in HTTPException constructor
				url = new URL("http://null");
				if (response.getLocation() != null)
					url = response.getLocation().toURL();
			} catch (MalformedURLException e) {
				// never mind. it is only for the debug message.
				logger.warn(EELFLoggerDelegate.errorLogger, "Failed to build URL", e);
			}
			logger.error(EELFLoggerDelegate.errorLogger, "http response failed. " + errMsg + "; url=" + url);
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeIncorrectHttpStatusError);
			throw new HTTPException(status, errMsg, url);
		}
	}

	private static boolean isHttpSuccess(int status) {
		return status / 100 == 2;
	}
	
	
	private WebClient createClientForApp(long appId, String restPath) {
		return createClientFor(appId, restPath, SystemType.APPLICATION);
	}

	//TODO Need to implement the mylogins once the endpoint is confirmed
	@EPMetricsLog
	private WebClient createClientFor(long appSystemId, String restPath, SystemType type) {
		logger.debug(EELFLoggerDelegate.debugLogger, "creating client for appId=" + appSystemId + "; restPath=" + restPath);
		EPApp externalApp = null;
		
		if(type == SystemType.APPLICATION){
		externalApp = appsCacheService.getApp(appSystemId);
		}else{
			// TO DO 
		}
		
		if (externalApp != null) {
			String appBaseUri = (type == SystemType.APPLICATION) ? externalApp.getAppRestEndpoint() : "";
			String username = (type == SystemType.APPLICATION) ? externalApp.getUsername(): "";
			String encriptedPwd = (type == SystemType.APPLICATION) ? externalApp.getAppPassword(): "";
			String appName = (type == SystemType.APPLICATION) ? externalApp.getName(): "";
			String decreptedAppPwd = StringUtils.EMPTY;

			// Set local context
			MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTP);
			if (appBaseUri != null && appBaseUri.contains("https")) {
				MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTPS);
			}
			MDC.put(EPCommonSystemProperties.FULL_URL, appBaseUri + restPath);
			MDC.put(EPCommonSystemProperties.TARGET_ENTITY, appName);
			MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, restPath);

			try {
				decreptedAppPwd = CipherUtil.decryptPKC(encriptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "createClientFor failed to decrypt", e);
			}
			
			WebClient client = createClientForPath(appBaseUri, restPath);

			// support basic authentication for some partners
			String encoding = Base64.getEncoder().encodeToString((username + ":" + decreptedAppPwd).getBytes());
			String encodingStr = "Basic " + encoding;
			client.header(BASIC_AUTHENTICATION_HEADER, encodingStr);

			// But still keep code downward compatible for non compliant apps
			client.header(APP_USERNAME_HEADER, username);
			client.header(PASSWORD_HEADER, decreptedAppPwd);

			client.header(SystemProperties.ECOMP_REQUEST_ID, MDC.get(MDC_KEY_REQUEST_ID));
			client.header(SystemProperties.USERAGENT_NAME, EPCommonSystemProperties.ECOMP_PORTAL_BE);
			
			logger.debug(EELFLoggerDelegate.debugLogger,
					String.format("App %d found, baseUri=[%s], Headers: [%s=%s, %s=%s, %s=%s]", appSystemId, appBaseUri,
							APP_USERNAME_HEADER, username, PASSWORD_HEADER, encriptedPwd, BASIC_AUTHENTICATION_HEADER, encodingStr));

			return client;
		}
		return null;
	}

	@Override
	public <T> T get(Class<T> clazz, long appId, String restPath) throws HTTPException {
		T t = null;
		Response response = getResponse(appId, restPath);

		if (response != null) {
			verifyResponse(response);
			
			/* It is not recommendable to use the implementation class org.apache.cxf.jaxrs.impl.ResponseImpl in the code, 
			but had to force this in-order to prevent conflict with the ResponseImpl class of Jersey Client which 
			doesn't work as expected. Created Portal-253 for tracking */
			String str = ((ResponseImpl)response).readEntity(String.class);
			
			EcompPortalUtils.logAndSerializeObject(logger, restPath, "GET result =", str);
			try {
				t = gson.fromJson(str, clazz);
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
			}
		}

		return t;
	}
	
	@Override
	public String getIncomingJsonString(long appId, String restPath) throws HTTPException {
		Response response = getResponse(appId, restPath);

		if (response != null) {
			verifyResponse(response);
			
			/* It is not recommendable to use the implementation class org.apache.cxf.jaxrs.impl.ResponseImpl in the code, 
			but had to force this in-order to prevent conflict with the ResponseImpl class of Jersey Client which 
			doesn't work as expected. Created Portal-253 for tracking */
			String incomingJson = ((ResponseImpl)response).readEntity(String.class);
			return incomingJson;
		}
		
		return "";
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.onap.portalapp.portal.service.ApplicationsRestClientService#get(
	 * java.lang.Class, long, java.lang.String, boolean)
	 */
	@Override
	public <T> T get(Class<T> clazz, long appId, String restPath, boolean useJacksonMapper) throws HTTPException {

		if (!useJacksonMapper)
			return get(clazz, appId, restPath);

		T t = null;
		Response response = getResponse(appId, restPath);

		if (response != null) {
			verifyResponse(response);
			String str = ((ResponseImpl)response).readEntity(String.class);
			EcompPortalUtils.logAndSerializeObject(logger, restPath, "GET result =", str);

			try {
				t = mapper.readValue(str, clazz);
			} catch (Exception e) {
				e.printStackTrace();
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
			}
		}

		return t;
	}

	protected Response getResponse(long appId, String restPath) {
		WebClient webClient = null;
		Response response = null;

		webClient = createClientForApp(appId, restPath);
		EcompPortalUtils.logAndSerializeObject(logger, restPath, "GET request =", "no-payload");

		try {
			if (webClient != null) {
				response = webClient.get();
			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"Unable to create the Webclient to make the '" + restPath + "' API call.");
			}
		} catch (Exception e) {
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE,
					Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiGeneralError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while making the GET REST API call", e);
		}
		return response;
	}
	
	
	@Override
	public <T> T post(Class<T> clazz, long appId, Object payload, String restPath, SystemType type) throws HTTPException {
		WebClient client = null;
		Response response = null;
		T t = null;

		client = createClientFor(appId, restPath, type);
		EcompPortalUtils.logAndSerializeObject(logger, restPath, "POST request =", payload);

		try {
			if (client != null) {
				response = client.post(payload);
			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"Unable to create the Webclient to make the '" + restPath + "' API call.");
			}
		} catch (Exception e) {
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE,
					Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiGeneralError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while making the POST REST API call", e);
		}

		if (response != null) {
			verifyResponse(response);

			// String contentType = response.getHeaderString("Content-Type");
			if (clazz != null) {
				String str = ((ResponseImpl)response).readEntity(String.class);
				EcompPortalUtils.logAndSerializeObject(logger, restPath, "POST result =", str);
				try {
					t = gson.fromJson(str, clazz);
				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
				}
			}
		}
		return t;
	}

	@Override
	public <T> T post(Class<T> clazz, long appId, Object payload, String restPath) throws HTTPException {
		return post( clazz, appId, payload, restPath, SystemType.APPLICATION);
	}

	//@Override
	public <T> T postForClass(Class<T> clazz, long appId, Object payload, String restPath, Class<T> forClass) throws HTTPException {
		WebClient client = null;
		Response response = null;
		T t = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "Entering to createClientForApp method for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);
		client = createClientForApp(appId, restPath);
		EcompPortalUtils.logAndSerializeObject(logger, restPath, "POST request =", payload);
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished createClientForApp method for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);
		try {
			if (client != null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Entering to POST for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);
				response = client.post(payload);
				logger.debug(EELFLoggerDelegate.debugLogger, "Finished to POST for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);
			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"Unable to create the Webclient to make the '" + restPath + "' API call.");
			}
		} catch (Exception e) {
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE,
					Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiGeneralError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while making the POST REST API call", e);
		}

		if (response != null) {
			verifyResponse(response);

			// String contentType = response.getHeaderString("Content-Type");
			if (clazz != null) {
				String str = ((ResponseImpl)response).readEntity(String.class);
				EcompPortalUtils.logAndSerializeObject(logger, restPath, "POST result =", str);
				try {
					t = gson.fromJson(str, clazz);
				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
				}
			}
		}
		return t;
	}

	
	@Override
	public <T> T put(Class<T> clazz, long appId, Object payload, String restPath) throws HTTPException {
		WebClient client = null;
		Response response = null;
		T t = null;

		logger.debug(EELFLoggerDelegate.debugLogger, "Entering to createClientForApp method for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);

		client = createClientForApp(appId, restPath);
		EcompPortalUtils.logAndSerializeObject(logger, restPath, "PUT request =", payload);
		
		logger.debug(EELFLoggerDelegate.debugLogger, "Finished createClientForApp method for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);

		try {
			if (client != null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Entering to PUT for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);

				response = client.put(payload);
				
				logger.debug(EELFLoggerDelegate.debugLogger, "Finished to PUT for payload: {} and restPath: {} and appId: {}", payload.toString(), restPath, appId);

			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"Unable to create the Webclient to make the '" + restPath + "' API call.");
			}
		} catch (Exception e) {
			MDC.put(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE,
					Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeRestApiGeneralError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while making the PUT REST API call", e);
		}

		if (response != null) {
			verifyResponse(response);
			String str = ((ResponseImpl)response).readEntity(String.class);
			EcompPortalUtils.logAndSerializeObject(logger, restPath, "PUT result =", str);
			try {
				t = gson.fromJson(str, clazz);
			} catch (Exception e) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
			}
		}
		return t;
	}
}
