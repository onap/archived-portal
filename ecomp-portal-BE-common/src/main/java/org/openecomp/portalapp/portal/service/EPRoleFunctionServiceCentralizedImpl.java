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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class EPRoleFunctionServiceCentralizedImpl implements EPRoleFunctionService{

	
	@Autowired
	private DataAccessService dataAccessService;
	
	@Autowired
	private  SessionFactory sessionFactory;
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<RoleFunction> getRoleFunctions() {
		List<CentralRoleFunction> getRoleFuncList = null;
		List<RoleFunction> getRoleFuncListOfPortal = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", (long) 1);
		//Sync all functions from external system into Ecomp portal DB
		getRoleFuncList = dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null);
		for(CentralRoleFunction roleFunction : getRoleFuncList)
		{
			RoleFunction roleFun = new RoleFunction();
			roleFun.setCode(roleFunction.getCode());
			roleFun.setName(roleFunction.getName());
			getRoleFuncListOfPortal.add(roleFun);
		}
		return getRoleFuncListOfPortal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getRoleFunctions(HttpServletRequest request, EPUser user) {
		HttpSession session = request.getSession();
		String userId = user.getId().toString();
		final Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		@SuppressWarnings("unchecked")
		List getRoleFuncListOfPortal =   dataAccessService.executeNamedQuery("getRoleFunctionsOfUser", params, null);
		Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfPortal);	
		session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME),
				getRoleFuncListOfPortalSet);
		return getRoleFuncListOfPortalSet;
				
	}

	
	
}
