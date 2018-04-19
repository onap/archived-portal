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

import org.apache.cxf.transport.http.HTTPException;
import org.onap.portalapp.externalsystemapproval.model.ExternalSystemUser;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.EPUserAppCatalogRoles;
import org.onap.portalapp.portal.domain.ExternalSystemAccess;
import org.onap.portalapp.portal.transport.AppWithRolesForUser;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.RoleInAppForUser;
import org.onap.portalapp.portal.transport.UserApplicationRoles;

public interface UserRolesService {

	/**
	 * Returns list of app roles of a single app
	 * 
	 * @param appId
	 *            ID of row in fn_app
	 * @param userId
	 *            ID of row in fn_user
	 * @param extRequestValue
	 *            set to false if request is from users page otherwise true
	 * @return List<RoleInAppForUser>
	 */
	public List<RoleInAppForUser> getAppRolesForUser(Long appId, String userId, Boolean extRequestValue);

	public boolean setAppWithUserRoleStateForUser(EPUser user, AppWithRolesForUser newAppRolesForUser);

	public List<UserApplicationRoles> getUsersFromAppEndpoint(Long appId) throws HTTPException;

	public List<EPRole> importRolesFromRemoteApplication(Long appId) throws HTTPException;

	/**
	 * Gets entries from the local fn_user_role table for the specified user and
	 * app.
	 * 
	 * @param appId
	 *            ID of row in fn_app
	 * @param userId
	 *            ID of row in fn_user
	 * @return List of EPRole; empty if none found.
	 */
	public List<EPUserApp> getCachedAppRolesForUser(Long appId, Long userId);

	public FieldsValidator putUserAppRolesRequest(AppWithRolesForUser userAppRolesData, EPUser user);

	/**
	 * Save user app roles in the database from the external request
	 * 
	 * @param newAppRolesForUser
	 * 
	 *            contains login id, app name, request id, and list of role
	 *            names
	 * @param reqType
	 * @return if any exceptions, returns detail message and true or false
	 */
	ExternalRequestFieldsValidator setExternalRequestUserAppRole(ExternalSystemUser newAppRolesForUser, String reqType);

	public List<EPUserAppCatalogRoles> getUserAppCatalogRoles(EPUser user, String appName);

	public String updateRemoteUserProfile(String orgUserId, Long appId);

	public ExternalSystemAccess getExternalRequestAccess();
	
	public List<EPUserApp> getEPUserAppList(Long appId, Long userId, Long roleId);

}
