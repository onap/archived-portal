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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.DecoderException;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.exceptions.RoleFunctionException;
import org.onap.portalsdk.core.domain.RoleFunction;

public interface EPRoleFunctionService {
	
	/**
	 * Builds a set of role functions and sets a session attribute with it.
	 * 
	 * @return Set of role functions that was built.
	 */
	public List<RoleFunction> getRoleFunctions();

	/**
	 * Builds a set of role functions of user
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param user
	 *            EPUser
	 * @return Set of role functions that was built.
	 * @throws RoleFunctionException 
	 */
	public Set getRoleFunctions(HttpServletRequest request, EPUser user) throws RoleFunctionException;

}
