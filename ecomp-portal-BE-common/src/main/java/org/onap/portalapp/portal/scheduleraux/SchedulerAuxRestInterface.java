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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.scheduleraux;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.json.simple.JSONObject;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.scheduler.client.HttpBasicClient;
import org.onap.portalapp.portal.scheduler.policy.rest.RequestDetails;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class SchedulerAuxRestInterface extends SchedulerAuxRestInt implements SchedulerAuxRestInterfaceIfc {

	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerAuxRestInterface.class);

	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	/** The client. */
	private static Client client = null;

	/** The common headers. */
	private MultivaluedHashMap<String, Object> commonHeaders;

	public SchedulerAuxRestInterface() {
		super();
	}

	public void initRestClient() {
		final String methodname = "initRestClient()";
		final String mechId = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_CLIENT_MECHID_VAL);
		final String clientPassword = SchedulerProperties
				.getProperty(SchedulerProperties.SCHEDULERAUX_CLIENT_PASSWORD_VAL);
		final String username = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_USERNAME_VAL);
		final String password = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_PASSWORD_VAL);
		final String environment = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_ENVIRONMENT_VAL);
		final String clientAuth = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_CLIENTAUTH_VAL);
		final String decrypted_client_password = Password.deobfuscate(clientPassword);
		String mechAuthString = mechId + ":" + decrypted_client_password;
		byte[] mechAuthEncBytes = Base64.encodeBase64(mechAuthString.getBytes());
		final String decrypted_password = Password.deobfuscate(password);
		String authString = username + ":" + decrypted_password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authorization = new String(authEncBytes);

		commonHeaders = new MultivaluedHashMap<String, Object>();
		commonHeaders.put("ClientAuth", Collections.singletonList((Object) ("Basic " + clientAuth)));
		commonHeaders.put("Authorization", Collections.singletonList((Object) ("Basic " + authorization)));
		commonHeaders.put("Environment", Collections.singletonList((Object) (environment)));

		if (client == null) {

			try {
				client = HttpBasicClient.getClient();
			} catch (Exception e) {
				logger.debug(EELFLoggerDelegate.debugLogger, " Unable to get the SSL client", methodname);

			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void Get(T t, String sourceId, String path, RestObject<T> restObject) throws Exception {
		String methodName = "Get";

		logger.debug(EELFLoggerDelegate.debugLogger, " start", methodName);

		String url = "";
		restObject.set(t);

		url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_SERVER_URL_VAL) + path;
		logger.debug(EELFLoggerDelegate.debugLogger, " sending request to url: ", dateFormat.format(new Date()),
				methodName, url);

		initRestClient();

		final Response cres = client.target(url).request().accept("application/json").headers(commonHeaders).get();

		int status = cres.getStatus();
		restObject.setStatusCode(status);

		if (status == 200) {
			t = (T) cres.readEntity(t.getClass());
			restObject.set(t);
			logger.debug(EELFLoggerDelegate.debugLogger, " REST api was successfull!", dateFormat.format(new Date()),
					methodName);

		} else {
			throw new Exception(methodName + " with status=" + status + ", url= " + url);
		}

		logger.debug(EELFLoggerDelegate.debugLogger, " received status", methodName, status);

		return;
	}

	@SuppressWarnings("unchecked")
	public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) {

		String methodName = "Delete";
		String url = "";
		Response cres = null;

		logRequest(r);

		try {
			initRestClient();

			url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_SERVER_URL_VAL) + path;
			logger.debug(EELFLoggerDelegate.debugLogger, " methodName sending request to: ",
					dateFormat.format(new Date()), url, methodName);

			cres = client.target(url).request().accept("application/json").headers(commonHeaders)
					// .entity(r)
					.build("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON)).invoke();
			// .method("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON));
			// .delete(Entity.entity(r, MediaType.APPLICATION_JSON));

			int status = cres.getStatus();
			restObject.setStatusCode(status);

			if (status == 404) { // resource not found
				String msg = "Resource does not exist...: " + cres.getStatus();
				logger.debug(EELFLoggerDelegate.debugLogger, "msg", dateFormat.format(new Date()), msg);
			} else if (status == 200 || status == 204) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Resource--deleted:url", dateFormat.format(new Date()),
						url);
			} else if (status == 202) {
				String msg = "Delete in progress: " + status;
				logger.debug(EELFLoggerDelegate.debugLogger, "msg ", dateFormat.format(new Date()), msg);
			} else {
				String msg = "Deleting Resource failed: " + status;
				logger.debug(EELFLoggerDelegate.debugLogger, "msg", dateFormat.format(new Date()), msg);
			}

			try {
				t = (T) cres.readEntity(t.getClass());
				restObject.set(t);
			} catch (HttpClientErrorException e) {
				logger.error(EELFLoggerDelegate.errorLogger,
						" HttpClientErrorException:No response entity, this is probably ok, e=", methodName, e);
				EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "No response entity, this is probably ok, e=", methodName,
						e);
				EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.INTERNAL_SERVER_ERROR.value());

			}

		} catch (HttpClientErrorException e) {
			logger.error(EELFLoggerDelegate.errorLogger, " HttpClientErrorException:Exception with the URL", methodName,
					url, e);
			EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception with the URL ", dateFormat.format(new Date()),
					methodName, url, e);
			EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.INTERNAL_SERVER_ERROR.value());

			throw e;

		}
	}

	@SuppressWarnings("unchecked")
	public <T> void Post(T t, JSONObject requestDetails, String uuid, String path, RestObject<T> restObject)
			throws HttpClientErrorException, Exception {

		String methodName = "Post";
		String url = "";
		Response cres = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "POST policy rest interface");
		// logRequest (requestDetails);
		try {

			initRestClient();

			url = SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_SERVER_URL_VAL) + path;
			logger.debug(EELFLoggerDelegate.debugLogger, " sending request to url= ", methodName, url);
			// Change the content length

			cres = client.target(url).request().accept("application/json").headers(commonHeaders)
					// .header("content-length", 201)
					// .header("X-FromAppId", sourceID)
					.post(Entity.entity(requestDetails, MediaType.APPLICATION_JSON));

			t = (T) cres.readEntity(t.getClass());
			if (t.equals("")) {
				restObject.set(null);
			} else {
				restObject.set(t);
			}

			int status = cres.getStatus();

			restObject.setStatusCode(status);

			if (status >= 200 && status <= 299) {
				logger.debug(EELFLoggerDelegate.debugLogger, " REST api POST was successful!", methodName);

			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "methodname with Status and URL", methodName, status, url);
			}

		} catch (HttpClientErrorException e) {
			String message = String.format(
					" HttpClientErrorException:Exception with the URL . MethodName: %s, Url: %s", methodName,url);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
			EPLogUtil.schedulerAccessAlarm(logger, e.getStatusCode().value());
		} catch (Exception e) {
			String message = String.format(
					" Exception with the URL . MethodName: %s, Url: %s", methodName,url);
			logger.error(EELFLoggerDelegate.errorLogger, message, e);			
			EPLogUtil.schedulerAccessAlarm(logger, HttpStatus.BAD_REQUEST.value());
			throw e;

		}
	}

	public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
		return clazz.newInstance();
	}

	@Override
	public void logRequest(RequestDetails r) {
		// TODO Auto-generated method stub
	}
}