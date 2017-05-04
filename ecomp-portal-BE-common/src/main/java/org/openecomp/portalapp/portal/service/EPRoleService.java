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

import java.util.List;

import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalapp.portal.domain.EPRole;


public interface EPRoleService {
	
	// Used by ECOMP. Get cached role by two columns used by ECOMP only. app id, and external app role id.
	EPRole getRole(Long appId, Long appRoleid);
	public void saveRole(EPRole domainRole);
	EPRole getAppRole(String roleName, Long appId);
	public List<RoleFunction> getRoleFunctions();
	
	
}
