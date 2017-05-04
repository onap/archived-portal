/*-
 * ================================================================================
 * ECOMP Portal SDK
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

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPException;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.SystemType;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
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
			String decreptedAppPwd = "";

			// Set local context
			MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTP);
			if (appBaseUri != null && appBaseUri.contains("https")) {
				MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTPS);
			}
			MDC.put(EPCommonSystemProperties.FULL_URL, appBaseUri + restPath);
			MDC.put(EPCommonSystemProperties.TARGET_ENTITY, appName);
			MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, restPath);

			try {
				decreptedAppPwd = CipherUtil.decrypt(encriptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Unable to decrypt App name = " + externalApp,
						EcompPortalUtils.getStackTrace(e));
				logger.error(EELFLoggerDelegate.errorLogger, "Unable to decrypt App name = " + externalApp,
						EcompPortalUtils.getStackTrace(e));
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
			String str = response.readEntity(String.class);
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
			String incomingJson = response.readEntity(String.class);
			return incomingJson;
		}
		
		return "";
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openecomp.portalapp.portal.service.ApplicationsRestClientService#get(
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
			String str = response.readEntity(String.class);
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
				String str = response.readEntity(String.class);
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

		client = createClientForApp(appId, restPath);
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
				String str = response.readEntity(String.class);
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

		client = createClientForApp(appId, restPath);
		EcompPortalUtils.logAndSerializeObject(logger, restPath, "PUT request =", payload);

		try {
			if (client != null) {
				response = client.put(payload);
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
			String str = response.readEntity(String.class);
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
