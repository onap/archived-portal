/*-
 * ================================================================================
 * eCOMP Portal
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

import org.apache.cxf.transport.http.HTTPException;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.transport.AppWithRolesForUser;
import org.openecomp.portalapp.portal.transport.RoleInAppForUser;
import org.openecomp.portalapp.portal.transport.UserApplicationRoles;

public interface UserRolesService {

	public List<RoleInAppForUser> getAppRolesForUser(Long appId, String userId);

	public boolean setAppWithUserRoleStateForUser(EPUser user, AppWithRolesForUser newAppRolesForUser);

	public List<UserApplicationRoles> getUsersFromAppEndpoint(Long appId) throws HTTPException;

	public List<EPRole> importRolesFromRemoteApplication(Long appId) throws HTTPException;
	
	
	/**
	 * Gets entries from the local fn_user_role table for the specified user and
	 * app.
	 * 
	 * @param appId
	 *            ID of row in fn_app
	 * @param userid
	 *            ID of row in fn_user
	 * @return List of EPRole; empty if none found.
	 */
	public List<EPUserApp> getCachedAppRolesForUser(Long appId, Long userId);
	
	public String updateRemoteUserProfile(String orgUserId, Long appId);


}
