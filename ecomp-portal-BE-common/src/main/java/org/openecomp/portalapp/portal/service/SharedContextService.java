/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.openecomp.portalapp.portal.domain.SharedContext;

/**
 * Defines the methods exposed by the service that manages shared context
 * objects in the database.
 */
public interface SharedContextService {

	/**
	 * Gets all shared context objects for the specified context ID.
	 * 
	 * @param contextId
	 *            SharedContext ID
	 * @return List of SharedContext objects
	 */
	List<SharedContext> getSharedContexts(String contextId);

	/**
	 * Gets the shared context with the specified context ID and key.
	 * 
	 * @param contextId
	 *            Context ID; usually a session ID
	 * @param key
	 *            Key for the key-value pair
	 * @return Value found in the database, null if any parameter is null or no
	 *         shared context exists with that context ID - key pair.
	 */
	SharedContext getSharedContext(String contextId, String key);

	/**
	 * Creates a new shared context entry with the specified context ID, key and
	 * value.
	 * 
	 * @param contextId
	 *            SharedContext ID
	 * @param key
	 *            Key for the key-value pair.
	 * @param value
	 *            Value for the key-value pair.
	 */
	void addSharedContext(String contextId, String key, String value);

	/**
	 * Saves the specified shared context.
	 * 
	 * @param context
	 *            SharedContext object to save.
	 */
	void saveSharedContext(SharedContext context);

	/**
	 * Deletes the specified shared context.
	 * 
	 * @param context
	 *            SharedContext object to delete.
	 */
	void deleteSharedContext(SharedContext context);

	/**
	 * Deletes all shared contexts with the specified context ID.
	 * 
	 * @param contextId
	 *            Context ID; usually a session ID
	 * @return number of shared-context objects deleted
	 */
	int deleteSharedContexts(String contextId);

	/**
	 * Deletes all shared contexts with a creation time that is older than the
	 * specified value.
	 * 
	 * @param ageInSeconds
	 *            Expiration threshold in seconds
	 */
	void expireSharedContexts(int ageInSeconds);

}
