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
package org.onap.portalapp.portal.scheduler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.util.Collections;
import java.util.Date;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.security.Password;
import org.json.simple.JSONObject;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.scheduler.client.HttpBasicClient;
import org.onap.portalapp.portal.scheduler.client.HttpsBasicClient;
import org.onap.portalapp.portal.scheduler.restobjects.RestObject;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@SuppressWarnings("MalformedFormatString")
@Service
@NoArgsConstructor
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {
	private static final String APPLICATION_JSON = "application/json";
	private static final String PASSWORD_IS_EMPTY = "Password is Empty";
	private static final String HTTP_CLIENT_ERROR = " HttpClientErrorException: Exception For the POST  ." 
													+ " MethodName: %APPLICATION_JSON, Url: %APPLICATION_JSON";

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
	private static Client client = null;
	private static Gson gson = null;

	private MultivaluedHashMap<String, Object> commonHeaders;

	private static void init() {
		logger.debug(EELFLoggerDelegate.debugLogger, "initializing");
		GsonBuilder builder = new GsonBuilder();

		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(Date.class,
			(JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));

		gson = builder.create();
	}

	public void initRestClient() {
		logger.debug(EELFLoggerDelegate.debugLogger, "Starting to initialize rest client");

		init();

		final String username;
		final String password;

		/* Setting user name based on properties */
		String retrievedUsername = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
		if (retrievedUsername.isEmpty()) {
			username = "";
		} else {
			username = retrievedUsername;
		}

		/* Setting password based on properties */
		String retrievedPassword = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
		if (retrievedPassword.isEmpty()) {
			password = StringUtils.EMPTY;
		} else {
			if (retrievedPassword.contains("OBF:")) {
				password = Password.deobfuscate(retrievedPassword);
			} else {
				password = retrievedPassword;
			}
		}
		try {
			if (StringUtils.isBlank(password)) {
				throw new CredentialException(PASSWORD_IS_EMPTY);
			}
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegate.errorLogger, "Unable to initialize rest client", ex);
		}
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);

		commonHeaders = new MultivaluedHashMap<>();
		commonHeaders.put("Authorization", Collections.singletonList(("Basic " + authStringEnc)));

		try {
			if (!username.isEmpty()) {

				client = HttpBasicClient.getClient();
			} else {

				client = HttpsBasicClient.getClient();
			}
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "Unable to initialize rest client",e.getMessage());

		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Client Initialized");

	}

	@SuppressWarnings("unchecked")
	public <T> void Get(T t, String sourceId, String path,
			org.onap.portalapp.portal.scheduler.restobjects.RestObject<T> restObject) {

		String methodName = "Get";
		String url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;

		logger.debug(EELFLoggerDelegate.debugLogger, "URL FOR GET : ", url);
		try {
			initRestClient();

			final Response cres = client.target(url).request().accept(APPLICATION_JSON).headers(commonHeaders).get();

			int status = cres.getStatus();
			restObject.setStatusCode(status);

			if (cres.getEntity() != null) {
				try {
					String str = (cres).readEntity(String.class);
					if (t.getClass().getName().equals(String.class.getName())) {
						t = (T) str;

					} else {
						t = (T) gson.fromJson(str, t.getClass());
					}

				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
				}
			} else {
				t = null;
				restObject.set(null);
			}

			if ("".equals(t)) {
				restObject.set(null);
			} else {
				restObject.set(t);
			}
		} catch (HttpClientErrorException e) {
			String message = String.format(
					HTTP_CLIENT_ERROR, methodName, url);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
			EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
		} catch (Exception e) {
			String message = String.format("Exception For the POST . MethodName: %APPLICATION_JSON, Url: %APPLICATION_JSON", methodName, url);

			logger.error(EELFLoggerDelegate.errorLogger, message, e);
			EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.INTERNAL_SERVER_ERROR.value());

			throw e;

		}

	}

	@SuppressWarnings("unchecked")
	public <T> void Post(T t, JSONObject requestDetails, String path, RestObject<T> restObject) {

		String methodName = "Post";
		String url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		logger.debug(EELFLoggerDelegate.debugLogger, "URL FOR POST : " + url);

		try {

			initRestClient();

			// Change the content length
			final Response cres = client.target(url).request().accept(APPLICATION_JSON).headers(commonHeaders)
					.post(Entity.entity(requestDetails, MediaType.APPLICATION_JSON));

			if (cres != null && cres.getEntity() != null) {

				try {
					String str = (cres).readEntity(String.class);
					if (t.getClass().getName().equals(String.class.getName())) {
						t = (T) str;

					} else {
						t = (T) gson.fromJson(str, t.getClass());
					}

				} catch (Exception e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
				}
				restObject.set(t);
			} else {
				restObject.set(null);
			}

			int status = cres != null ? cres.getStatus() : 0;
			restObject.setStatusCode(status);

			if (status >= 200 && status <= 299) {
				String message = String.format(" REST api POST was successful!", methodName);
				logger.debug(EELFLoggerDelegate.debugLogger, message);

			} else {
				String message = String.format(" FAILED with http status  . MethodName: %APPLICATION_JSON, Status: %APPLICATION_JSON, Url: %APPLICATION_JSON",
						methodName, status, url);
				logger.debug(EELFLoggerDelegate.debugLogger, message);
			}

		} catch (HttpClientErrorException e) {
			String message = String.format(
					HTTP_CLIENT_ERROR, methodName, url);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
			EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
		} catch (Exception e) {
			String message = String.format(
					HTTP_CLIENT_ERROR, methodName, url);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
			EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.INTERNAL_SERVER_ERROR.value());
			throw e;
		}
	}

	@Override
	public void logRequest(JSONObject requestDetails) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public <T> void Delete(T t, JSONObject requestDetails, String sourceID, String path, RestObject<T> restObject) {

		String methodName = "Delete";
		String url = "";
		Response cres;

		try {
			initRestClient();

			url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;

			cres = client.target(url).request().accept(APPLICATION_JSON).headers(commonHeaders)
					// .entity(r)
					.build("DELETE", Entity.entity(requestDetails, MediaType.APPLICATION_JSON)).invoke();

			int status = cres.getStatus();
			restObject.setStatusCode(status);
			if (cres.getEntity() != null) {
				t = (T) cres.readEntity(t.getClass());
				restObject.set(t);
			}

		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, " HttpClientErrorException:Exception For the Delete",
					methodName, url, e);
			EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception For the Delete", methodName, url, e);
			EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.INTERNAL_SERVER_ERROR.value());
			throw e;
		}
	}

}
