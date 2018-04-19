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
package org.onap.portalapp.portal.scheduleraux;

import org.json.simple.JSONObject;
import org.onap.portalapp.portal.scheduler.policy.rest.RequestDetails;

public interface SchedulerAuxRestInterfaceIfc {	
	/**
	 * Inits the rest client.
	 */
	public void initRestClient();
	
	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param sourceId the source id
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Get (T t, String sourceId, String path, RestObject<T> restObject ) throws Exception;
	
	/**
	 * Delete.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param r the r
	 * @param sourceID the source ID
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception;
	
	/**
	 * Post.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param r the r
	 * @param sourceID the source ID
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Post(T t, JSONObject r, String sourceID, String path, RestObject<T> restObject) throws Exception;
	
	/***
	 * Log request.
	 *
	 * @param r the r
	 */
	public void logRequest ( RequestDetails r  );
	
}