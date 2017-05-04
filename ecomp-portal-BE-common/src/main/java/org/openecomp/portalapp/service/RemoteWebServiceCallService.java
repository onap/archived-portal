/*-
 * ================================================================================
 * ECOMP Portal
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
package org.openecomp.portalapp.service;

public interface RemoteWebServiceCallService {

	/**
	 * Answers whether the specified credentials match application information
	 * in the database.
	 * 
	 * @param secretKey
	 *            Key used to decrypt passwords; ignored if null.
	 * @param requestUebKey
	 *            UEB key that identifies the application
	 * @param requesUserName
	 *            User name for the application
	 * @param requestPassword
	 *            Password for the application
	 * @return True if the UEB key and the credentials match the database
	 *         entries; else false.
	 * @throws Exception
	 *             If decryption fails.
	 */
	public boolean verifyRESTCredential(String secretKey, String requestUebKey, String requestUserName,
			String requestPassword) throws Exception;
	
	
	
	public boolean verifyAppKeyCredential(String requestUebKey) throws Exception;

}
