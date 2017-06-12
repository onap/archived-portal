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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.PortalConstants;

@Service("epRoleService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPRoleServiceImpl implements EPRoleService {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPRoleServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	@SuppressWarnings("unchecked")
	public List<RoleFunction> getRoleFunctions() {
		// List msgDB = getDataAccessService().getList(Profile.class, null);
		return getDataAccessService().getList(RoleFunction.class, null);
	}

	@SuppressWarnings("unchecked")
	public List<EPRole> getAvailableChildRoles(Long roleId) {
		List<EPRole> availableChildRoles = (List<EPRole>) getDataAccessService().getList(EPRole.class, null);
		if (roleId == null || roleId == 0) {
			return availableChildRoles;
		}

		EPRole currentRole = (EPRole) getDataAccessService().getDomainObject(EPRole.class, roleId, null);
		Set<EPRole> allParentRoles = new TreeSet<EPRole>();
		allParentRoles = getAllParentRolesAsList(currentRole, allParentRoles);

		Iterator<EPRole> availableChildRolesIterator = availableChildRoles.iterator();
		while (availableChildRolesIterator.hasNext()) {
			EPRole role = availableChildRolesIterator.next();
			if (!role.getActive() || allParentRoles.contains(role) || role.getId().equals(roleId)) {
				availableChildRolesIterator.remove();
			}
		}
		return availableChildRoles;
	}

	private Set<EPRole> getAllParentRolesAsList(EPRole role, Set<EPRole> allParentRoles) {
		Set<EPRole> parentRoles = role.getParentRoles();
		allParentRoles.addAll(parentRoles);
		Iterator<EPRole> parentRolesIterator = parentRoles.iterator();
		while (parentRolesIterator.hasNext()) {
			getAllParentRolesAsList(parentRolesIterator.next(), allParentRoles);
		}
		return allParentRoles;
	}

	public RoleFunction getRoleFunction(String code) {
		return (RoleFunction) getDataAccessService().getDomainObject(RoleFunction.class, code, null);
	}

	public void saveRoleFunction(RoleFunction domainRoleFunction) {
		getDataAccessService().saveDomainObject(domainRoleFunction, null);
	}

	public void deleteRoleFunction(RoleFunction domainRoleFunction) {
		getDataAccessService().deleteDomainObject(domainRoleFunction, null);
	}

	public EPRole getRole(Long id) {
		return (EPRole) getDataAccessService().getDomainObject(EPRole.class, id, null);
	}

	// TODO: refactor
	private static final String getAppRoleSqlFormat = "SELECT * FROM fn_role where APP_ID = %s AND APP_ROLE_ID = %s";

	@SuppressWarnings("unchecked")
	public EPRole getRole(Long appId, Long appRoleid) {
		if (appId == null || appRoleid == null) {
			logger.error(EELFLoggerDelegate.errorLogger, String.format(
					"getRole does not support null appId or roleId. appRoleid=%s, appRoleid=%s", appId, appRoleid));
			return null;
		}

		String sql = String.format(getAppRoleSqlFormat, appId, appRoleid);

		List<EPRole> roles = (List<EPRole>) dataAccessService.executeSQLQuery(sql, EPRole.class, null);
		int resultsCount = roles.size();
		if (resultsCount > 1) {
			logger.error(EELFLoggerDelegate.errorLogger,
					String.format(
							"search by appId=%s, appRoleid=%s should have returned 0 or 1 results. Got %d. This is an internal server error.",
							appId, appRoleid, resultsCount));
			logger.error(EELFLoggerDelegate.errorLogger,
					"Trying to recover from duplicates by returning the first search result. This issue should be treated, it is probably not critical because duplicate roles should be similar.");
			return roles.get(0);
		} else if (resultsCount == 1) {
			return roles.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public EPRole getAppRole(String roleName, Long appId) {

		final Map<String, String> params = new HashMap<String, String>();
		final Map<String, String> portalParams = new HashMap<String, String>();
		List<EPRole> roles = null;
		params.put("appId", appId.toString());
		params.put("roleName", roleName);
		portalParams.put("appRoleName", roleName);
		if (appId == 1 || roleName.equals(PortalConstants.ADMIN_ROLE)) {
			roles = (List<EPRole>) dataAccessService.executeNamedQuery("getPortalAppRoles", portalParams, null);
		} else if (appId != 1 && !roleName.equals(PortalConstants.ADMIN_ROLE)) {
			roles = (List<EPRole>) dataAccessService.executeNamedQuery("getAppRoles", params, null);
		}
		int resultsCount = roles.size();
		if (resultsCount > 1) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Trying to recover from duplicates by returning the first search result. This issue should be treated, it is probably not critical because duplicate roles should be similar.");
			return roles.get(0);
		} else if (resultsCount == 1) {
			return roles.get(0);
		}
		return null;
	}

	public void saveRole(EPRole domainRole) {
		getDataAccessService().saveDomainObject(domainRole, null);
	}

	public void deleteRole(EPRole domainRole) {
		getDataAccessService().deleteDomainObject(domainRole, null);
	}

	@SuppressWarnings("unchecked")
	public List<EPRole> getAvailableRoles() {
		return getDataAccessService().getList(EPRole.class, null);
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}
}
