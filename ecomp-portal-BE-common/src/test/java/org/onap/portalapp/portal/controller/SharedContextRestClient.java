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
package org.onap.portalapp.portal.controller;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * Provides reusable features for test cases to get or post from an REST
 * endpoint, allowing use of HTTPS connections to servers that use self-signed
 * certificates.
 */
public class SharedContextRestClient {

	private static final Log logger = LogFactory.getLog(SharedContextRestClient.class);

	/**
	 * Convenience method that builds and sends a GET request using properties
	 * to build the URI and populate header with credentials.
	 * 
	 * @param task
	 *            last component(s) of REST endpoint name; e.g., "get".
	 * @param contextId
	 * @param contextKey
	 * @return JSON string fetched
	 * @throws Exception
	 *             if the HTTP response code is anything other than OK.
	 */
	public static String getJson(final SharedContextTestProperties properties, final String task,
			final String contextId, final String contextKey) throws Exception {
		String requestPath = '/' + properties.getProperty(SharedContextTestProperties.APPNAME) //
				+ '/' + properties.getProperty(SharedContextTestProperties.RESTPATH) //
				+ '/' + task;
		return getJson(properties.getProperty(SharedContextTestProperties.HOSTNAME), //
				properties.getProperty(SharedContextTestProperties.PORT, -1), //
				properties.getProperty(SharedContextTestProperties.SECURE, true), //
				properties.getProperty(SharedContextTestProperties.UEBKEY), //
				properties.getProperty(SharedContextTestProperties.USERNAME), //
				properties.getProperty(SharedContextTestProperties.COUNTERSIGN), requestPath, //
				contextId, //
				contextKey);
	}

	/**
	 * Constructs and sends a GET request using the specified values.
	 * 
	 * @param hostname
	 * @param port
	 *            ignored if negative
	 * @param secure
	 *            If true, uses https; else http.
	 * @param headerUebkey
	 * @param headerUsername
	 * @param headerPassword
	 * @param requestPath
	 *            full path of the REST endpoint
	 * @param contextId
	 * @param contextKey
	 * Ignored if null
	 * @return JSON result
	 */
	public static String getJson(final String hostname, final int port, boolean secure, final String headerUebkey,
			final String headerUsername, final String headerPassword, final String requestPath, final String contextId,
			final String contextKey) throws Exception {

		URIBuilder uriBuilder = new URIBuilder();
		if (secure)
			uriBuilder.setScheme("https");
		else
			uriBuilder.setScheme("http");
		uriBuilder.setHost(hostname);
		if (port > 0)
			uriBuilder.setPort(port);
		uriBuilder.setPath(requestPath);
		uriBuilder.addParameter("context_id", contextId);
		if (contextKey != null)
			uriBuilder.addParameter("ckey", contextKey);
		final URI uri = uriBuilder.build();

		CloseableHttpClient httpClient;
		if (secure) {
			// Tell HttpClient to accept any server certificate for HTTPS.
			// http://stackoverflow.com/questions/24720013/apache-http-client-ssl-certificate-error
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
		} else {
			httpClient = HttpClients.createDefault();
		}

		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("uebkey", headerUebkey);
		httpGet.setHeader("username", headerUsername);
		httpGet.setHeader("password", headerPassword);

		String json = null;
		CloseableHttpResponse response = null;
		try {
			logger.debug("GET from " + uri);
			response = httpClient.execute(httpGet);
			logger.info("Status is " + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK)
				throw new Exception("Status is " + response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				logger.warn("Entity is null!");
			} else {
				// entity content length is never set.
				// this naively tries to read everything.
				json = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			}
		} finally {
			if (response != null)
				response.close();
		}
		return json;
	}

	/**
	 * Convenience method that builds and sends a POST request using properties
	 * to build the URI and populate header with credentials.
	 * 
	 * @param path
	 *            last component(s) of REST endpoint name; e.g., "users" or
	 *            "user/ab1234/roles".
	 * @return JSON string fetched
	 * @throws Exception
	 *             if the HTTP response code is anything other than OK.
	 */
	public static String postJson(final SharedContextTestProperties properties, final String path, final String json)
			throws Exception {
		String requestPath = '/' + properties.getProperty(SharedContextTestProperties.APPNAME) //
				+ '/' + properties.getProperty(SharedContextTestProperties.RESTPATH) //
				+ '/' + path;
		return postJson(properties.getProperty(SharedContextTestProperties.HOSTNAME), //
				properties.getProperty(SharedContextTestProperties.PORT, -1), //
				properties.getProperty(SharedContextTestProperties.SECURE, true), //
				properties.getProperty(SharedContextTestProperties.UEBKEY), //
				properties.getProperty(SharedContextTestProperties.USERNAME), //
				properties.getProperty(SharedContextTestProperties.COUNTERSIGN), //
				requestPath, //
				json);
	}

	/**
	 * Constructs and sends a POST request using the specified values.
	 * 
	 * @param hostname
	 * @param port
	 * @param secure
	 *            If true, uses https; else http.
	 * @param requestPath
	 *            full path of the REST endpoint
	 * @param headerUebkey
	 * @param headerUsername
	 * @param headerPassword
	 * @param json
	 *            Content to post
	 * @return JSON result
	 * @throws Exception
	 */
	public static String postJson(final String hostname, final int port, boolean secure, final String headerUebkey,
			final String headerUsername, final String headerPassword, final String requestPath, final String json)
			throws Exception {

		URIBuilder builder = new URIBuilder();
		if (secure)
			builder.setScheme("https");
		else
			builder.setScheme("http");
		builder.setHost(hostname);
		if (port > 0)
			builder.setPort(port);
		builder.setPath(requestPath);
		final URI uri = builder.build();

		CloseableHttpClient httpClient;
		if (secure) {
			// Tell HttpClient to accept any server certificate for HTTPS.
			// http://stackoverflow.com/questions/24720013/apache-http-client-ssl-certificate-error
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("uebkey", headerUebkey);
		httpPost.setHeader("username", headerUsername);
		httpPost.setHeader("password", headerPassword);

		StringEntity postEntity = new StringEntity(json, ContentType.create("application/json", Consts.UTF_8));
		httpPost.setEntity(postEntity);

		String responseJson = null;
		CloseableHttpResponse response = null;
		try {
			logger.debug("POST to " + uri);
			response = httpClient.execute(httpPost);
			logger.info("Status is " + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK)
				throw new Exception("Status is " + response.getStatusLine().toString());

			HttpEntity entity = response.getEntity();
			if (entity == null) {
				logger.warn("Entity is null!");
			} else {
				long len = entity.getContentLength();
				if (len < 0)
					logger.warn("Content length is -1");
				if (len < 2048) {
					responseJson = EntityUtils.toString(entity);
					logger.debug(responseJson);
				} else {
					logger.warn("Not implemented - stream content");
				}
				EntityUtils.consume(entity);
			}
		} finally {
			if (response != null)
				response.close();
		}
		return responseJson;
	}

}
