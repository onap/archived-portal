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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EPRoleFunctionServiceImpl implements EPRoleFunctionService {
	@Autowired
	private DataAccessService dataAccessService;
	
	

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleFunction> getRoleFunctions() {
		return getDataAccessService().getList(RoleFunction.class, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getRoleFunctions(HttpServletRequest request, EPUser user) {
		HashSet roleFunctions = null;

		HttpSession session = request.getSession();
		roleFunctions = (HashSet) session
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));

		if (roleFunctions == null) {
			HashMap roles = (HashMap) EPUserUtils.getRoles(request);
			roleFunctions = new HashSet();

			Iterator i = roles.keySet().iterator();

			while (i.hasNext()) {
				Long roleKey = (Long) i.next();
				EPRole role = (EPRole) roles.get(roleKey);

				Iterator j = role.getRoleFunctions().iterator();

				while (j.hasNext()) {
					RoleFunction function = (RoleFunction) j.next();
					roleFunctions.add(function.getCode());
				}
			}

			session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME),
					roleFunctions);
		}

		return roleFunctions;
	}
	
	
	

}
