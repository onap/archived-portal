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
import java.util.List;

import org.apache.cxf.transport.http.HTTPException;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.transport.RemoteUserWithRoles;
import org.openecomp.portalapp.portal.transport.UserApplicationRoles;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("userRolesService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class UserRolesServiceImpl extends UserRolesCommonServiceImpl implements UserRolesService {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRolesServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	private ApplicationsRestClientService applicationsRestClientService;

	private EPUser getUserFromRemoteApp(String orgUserId, EPApp app,
			ApplicationsRestClientService applicationsRestClientService) throws HTTPException {
		EPUser user = applicationsRestClientService.get(EPUser.class, app.getId(),
				String.format("/user/%s", orgUserId));
		return user;
	}

	private static void createNewUserOnRemoteApp(String orgUserId, EPApp app,
			ApplicationsRestClientService applicationsRestClientService, SearchService searchService,
			ObjectMapper mapper) throws Exception {
		EPUser client = searchService.searchUserByUserId(orgUserId);
		if (client == null) {
			String msg = "cannot create user " + orgUserId + ", because he/she cannot be found in phonebook.";
			logger.error(EELFLoggerDelegate.errorLogger, msg);
			throw new Exception(msg);
		}
		client.setLoginId(orgUserId);
		client.setActive(true);
		// The remote doesn't care about other apps, and this has caused
		// serialization problems - infinite recursion.
		client.getEPUserApps().clear();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String userAsString = mapper.writeValueAsString(client);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"about to post new client to remote application, users json = " + userAsString);
		applicationsRestClientService.post(EPUser.class, app.getId(), userAsString, String.format("/user", orgUserId));
	}

	@Override
	public List<UserApplicationRoles> getUsersFromAppEndpoint(Long appId) throws HTTPException {
		RemoteUserWithRoles[] remoteUsers = applicationsRestClientService.get(RemoteUserWithRoles[].class, appId,
				"/users");
		ArrayList<UserApplicationRoles> userApplicationRoles = new ArrayList<UserApplicationRoles>();
		for (RemoteUserWithRoles remoteUser : remoteUsers) {
			UserApplicationRoles userWithRemoteAppRoles = convertToUserApplicationRoles(appId, remoteUser);
			if (userWithRemoteAppRoles.getRoles() != null && userWithRemoteAppRoles.getRoles().size() > 0) {
				userApplicationRoles.add(userWithRemoteAppRoles);
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "User " + userWithRemoteAppRoles.getOrgUserId()
						+ " doesn't have any roles assigned to any app.");

			}
		}

		return userApplicationRoles;
	}

	public static void persistExternalRoleInEcompDb(EPRole externalAppRole, Long appId, EPRoleService roleService) {
		externalAppRole.setAppId(appId);
		externalAppRole.setAppRoleId(externalAppRole.getId());
		externalAppRole.setId(null); // We will persist a new role, with ecomp
										// role id which will be different than
										// external app role id.

		roleService.saveRole(externalAppRole);
		logger.debug(EELFLoggerDelegate.debugLogger,
				String.format("ECOMP persists role from app:%d, app roleId: %d, roleName: %s", appId,
						externalAppRole.getAppRoleId(), externalAppRole.getName()));
	}

	@Override
	public List<EPUserApp> getCachedAppRolesForUser(Long appId, Long userId) {
		// Find the records for this user-app combo, if any
		String filter = " where user_id = " + Long.toString(userId) + " and app_id = " + Long.toString(appId);
		@SuppressWarnings("unchecked")
		List<EPUserApp> roleList = dataAccessService.getList(EPUserApp.class, filter, null, null);
		logger.debug(EELFLoggerDelegate.debugLogger, "getCachedAppRolesForUser: list size is {}", roleList.size());
		return roleList;
	}

}
