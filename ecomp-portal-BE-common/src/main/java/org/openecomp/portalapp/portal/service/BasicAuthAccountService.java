package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;

public interface BasicAuthAccountService {
	
	/**
	 * Saves Basic Authentication account for external systems 
	 * @param BasicAuthCredentials
	 * @return Id of the newly created account
	 */
	Long saveBasicAuthAccount(BasicAuthCredentials newCredential) throws Exception;
	
	/**
	 * Saves Endpoint associated with a Basic Auth account 
	 * @param EPEndpoint
	 * @return Id of the newly created endpoint
	 */
	Long saveEndpoints(EPEndpoint endpoint) throws Exception;
	
	/**
	 * Saves Endpoint associated with a Basic Auth account 
	 * @param accountId, endpointId
	 */
	void saveEndpointAccount(Long accountId, Long endpointId) throws Exception;
	
	/**
	 * Returns list of all  BasicAuthCredentials in the sytem
	 * @return List<BasicAuthCredentials>
	 */
	List<BasicAuthCredentials> getAccountData() throws Exception;
	
	/**
	 * Deletes BasicAuthenticationAccount
	 * @param accountId
	 */
	void deleteEndpointAccout(Long accountId) throws Exception;
	
	/**
	 * Updates BasicAuthenticationAccount
	 * @param accountId, BasicAuthCredentials
	 */
	void updateBasicAuthAccount(Long accountId, BasicAuthCredentials newCredential) throws Exception;
}
