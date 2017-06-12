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

import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.transport.AppsListWithAdminRole;

public interface AdminRolesService {

	public AppsListWithAdminRole getAppsWithAdminRoleStateForUser(String orgUserId);

	public boolean setAppsWithAdminRoleStateForUser(AppsListWithAdminRole newAppsListWithAdminRoles);

	/**
	 * Attention! User roles in ECOMP PORTAL cannot be managed by this function.
	 * @param user
	 * @return 'true' if user has Super Administrator role SYS_ADMIN_ROLE_ID (1 for now) in ECOMP PORTAL, 'false' otherwise
	 */
	public boolean isSuperAdmin(EPUser user);
	
	/**
	 * Attention! User roles in ECOMP PORTAL cannot be managed by this function.
	 * @param user
	 * @return 'true' if user has Account Administrator role ACCOUNT_ADMIN_ROLE_ID (999 for now) for any application except ECOMP Portal, 'false' otherwise
	 */
	public boolean isAccountAdmin(EPUser user);

	/**
	 * Attention! User roles in ECOMP PORTAL cannot be managed by this function.
	 * @param user
	 * @return 'true' if user has any remote(!) role within any application (ECOMP Portal roles are not included), 'false' otherwise
	 */
	public boolean isUser(EPUser user);

	List<EPRole> getRolesByApp(EPUser user, Long appId);
}
