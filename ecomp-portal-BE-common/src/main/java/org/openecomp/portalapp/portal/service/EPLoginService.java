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
package org.openecomp.portalapp.portal.service;

import java.util.HashMap;

import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;

public interface EPLoginService {

	/**
	 * Calls {@link #findUser(EPLoginBean, String, HashMap, boolean)} with the
	 * last parameter set to true.
	 * 
	 * @param bean
	 * @param menuPropertiesFilename
	 * @param additionalParams
	 * @return EPUser object; null on error or if no match.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams) throws Exception;

	/**
	 * Searches the fn_user table for a row that matches information in the
	 * bean. Uses the bean's Organization User ID property if present; if not, uses the bean's
	 * LoginId property and (optionally, depending on matchPassword parameter)
	 * loginPwd property.
	 * 
	 * @param bean
	 *            EPLoginBean
	 * @param menuPropertiesFileName
	 *            Always ignored
	 * @param additionalParams
	 *            Used by DataAccessService when updating a matched user object
	 * @param matchPassword
	 *            If true, the search must match the password
	 * @return EPUser object; null on error or if no match.
	 */
	@SuppressWarnings("rawtypes")
	EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams,
			boolean matchPassword) throws Exception;

	/**
	 * Searches the fn_user table for a row with a value in column login_id that
	 * matches the specified value.
	 * 
	 * @param loginId
	 * @return EPUser object; null on error or if no match.
	 */
	public EPUser findUserWithoutPwd(String loginId);
}
