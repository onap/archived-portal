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

import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPEndpoint;

public interface BasicAuthAccountService {

	/**
	 * Saves Basic Authentication account for external systems
	 * 
	 * @param newCredential
	 *            BasicAuthCredentials
	 * @return Id of the newly created account
	 * @throws Exception
	 */
	Long saveBasicAuthAccount(BasicAuthCredentials newCredential) throws Exception;

	/**
	 * Saves Endpoint associated with a Basic Auth account
	 * 
	 * @param endpoint
	 *            EPEndpoint
	 * @return Id of the newly created endpoint
	 * @throws Exception
	 */
	Long saveEndpoints(EPEndpoint endpoint) throws Exception;

	/**
	 * Saves Endpoint associated with a Basic Auth account
	 * 
	 * @param accountId
	 * @param endpointId
	 * @throws Exception
	 */
	void saveEndpointAccount(Long accountId, Long endpointId) throws Exception;

	/**
	 * Returns list of all BasicAuthCredentials in the sytem
	 * 
	 * @return List<BasicAuthCredentials>
	 * @throws Exception
	 */
	List<BasicAuthCredentials> getAccountData() throws Exception;

	/**
	 * Deletes BasicAuthenticationAccount
	 * 
	 * @param accountId
	 * @throws Exception
	 */
	void deleteEndpointAccout(Long accountId) throws Exception;

	/**
	 * Updates BasicAuthenticationAccount
	 * 
	 * @param accountId
	 * @param newCredential
	 *            BasicAuthCredentials
	 * @throws Exception
	 */
	void updateBasicAuthAccount(Long accountId, BasicAuthCredentials newCredential) throws Exception;
}
