package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;

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
