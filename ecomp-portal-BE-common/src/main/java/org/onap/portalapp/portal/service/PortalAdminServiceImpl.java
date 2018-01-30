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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.transport.ExternalAccessUser;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.PortalAdmin;
import org.onap.portalapp.portal.transport.PortalAdminUserRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service("portalAdminService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class PortalAdminServiceImpl implements PortalAdminService {	

	private String SYS_ADMIN_ROLE_ID = "1";
	private String ECOMP_APP_ID = "1";

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalAdminServiceImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	SearchService searchService;
	@Autowired
	private EPAppService epAppService;
	
	RestTemplate template = new RestTemplate();
	
	@PostConstruct
	private void init() {
		SYS_ADMIN_ROLE_ID = SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID);
		ECOMP_APP_ID = SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID);
	}

	public List<PortalAdmin> getPortalAdmins() {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("adminRoleId", SYS_ADMIN_ROLE_ID);
			@SuppressWarnings("unchecked")
			List<PortalAdmin> portalAdmins = (List<PortalAdmin>) dataAccessService.executeNamedQuery("getPortalAdmins",
					params, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "getPortalAdmins was successful");
			return portalAdmins;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getPortalAdmins failed", e);
			return null;
		}
	}

	public FieldsValidator createPortalAdmin(String orgUserId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: createPortalAdmin: orgUserId is {}", orgUserId);
		EPUser user = null;
		boolean createNewUser = false;
		List<EPUser> localUserList = getUserListWithOrguseId(orgUserId);
		if (!localUserList.isEmpty()) {
			user = localUserList.get(0);
		} else {
			createNewUser = true;
		}

		if (user != null && isLoggedInUserPortalAdmin(user.getId())) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
			logger.error(EELFLoggerDelegate.errorLogger,
					"User '" + user.getOrgUserId() + "' already has PortalAdmin role assigned.");
		} else if (user != null || createNewUser) {
			Session localSession = null;
			Transaction transaction = null;
			try {
				localSession = sessionFactory.openSession();

				transaction = localSession.beginTransaction();
				if (createNewUser) {
					user = this.searchService.searchUserByUserId(orgUserId);
					if (user != null) {
						// insert the user with active true in order to
						// pass login phase.
						user.setActive(true);
						localSession.save(EPUser.class.getName(), user);
					}
				}
				if (user != null) {
					Long userid = user.getId();
					PortalAdminUserRole userRole = new PortalAdminUserRole();
					userRole.userId = userid;
					userRole.roleId = Long.valueOf(SYS_ADMIN_ROLE_ID);
					userRole.appId = Long.valueOf(ECOMP_APP_ID);

					localSession.save(PortalAdminUserRole.class.getName(), userRole);
				}

				transaction.commit();
				// Add role in the external central auth system
				if(user != null && EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
					 addPortalAdminInExternalCentralAuth(user.getOrgUserId(), PortalConstants.PORTAL_ADMIN_ROLE);
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "createPortalAdmin failed", e);
				EcompPortalUtils.rollbackTransaction(transaction, "createPortalAdmin rollback, exception = " + e.toString());
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "createPortalAdmin");
			}
		}
		return fieldsValidator;
	}

	@SuppressWarnings("unchecked")
	private List<EPUser> getUserListWithOrguseId(String orgUserId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("orgUserId", orgUserId);
		restrictionsList.add(orgUserIdCriterion);
		return (List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null);
	}
	
	private void addPortalAdminInExternalCentralAuth(String loginId, String portalAdminRole) throws Exception{
		try{
			String name = "";
			if (EPCommonSystemProperties.containsProperty(
					EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
				name = loginId + SystemProperties
						.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
			}
			EPApp app = epAppService.getApp(PortalConstants.PORTAL_APP_ID);
			String extRole = app.getNameSpace()+"."+portalAdminRole.replaceAll(" ", "_");
			ObjectMapper addUserRoleMapper = new ObjectMapper();
			ExternalAccessUser extUser = new ExternalAccessUser(name, extRole);
			String userRole = addUserRoleMapper.writeValueAsString(extUser);
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();

			HttpEntity<String> addUserRole = new HttpEntity<>(userRole, headers);
			template.exchange(
					SystemProperties.getProperty(
							EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "userRole",
					HttpMethod.POST, addUserRole, String.class);
		} catch (Exception e) {
			// This happens only if role already exists in external central access system but not in local DB thats where we logging here
			if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Portal Admin role already exists", e.getMessage());
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to add Portal Admin role ", e);
				throw e;
			}
		}
	}

	public FieldsValidator deletePortalAdmin(Long userId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		logger.debug(EELFLoggerDelegate.debugLogger, "deletePortalAdmin: test 1");
		Session localSession = null;
		Transaction transaction = null;

		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			dataAccessService.deleteDomainObjects(PortalAdminUserRole.class,
					"user_id='" + userId + "' AND role_id='" + SYS_ADMIN_ROLE_ID + "'", null);
			transaction.commit();
			if(EcompPortalUtils.checkIfRemoteCentralAccessAllowed()){
				deletePortalAdminInExternalCentralAuth(userId, PortalConstants.PORTAL_ADMIN_ROLE);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "deletePortalAdmin failed", e);
			EcompPortalUtils.rollbackTransaction(transaction, "deletePortalAdmin rollback, exception = " + e.toString());
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "deletePortalAdmin");
		}
		return fieldsValidator;
	}

	
	private void deletePortalAdminInExternalCentralAuth(Long userId, String portalAdminRole) throws Exception{
		try{									
			String name = "";
			List<EPUser> localUserList = getUserListWithUserid(userId);
			if (EPCommonSystemProperties.containsProperty(
					EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
				name = localUserList.get(0).getOrgUserId() + SystemProperties
						.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
			}
			EPApp app = epAppService.getApp(PortalConstants.PORTAL_APP_ID);
			String extRole = app.getNameSpace()+"."+portalAdminRole.replaceAll(" ", "_");
			HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
			HttpEntity<String> addUserRole = new HttpEntity<>(headers);
			template.exchange(
					SystemProperties.getProperty(
							EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
							+ "userRole/"+name+"/"+extRole,
					HttpMethod.DELETE, addUserRole, String.class);
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
				logger.debug(EELFLoggerDelegate.debugLogger, "Portal Admin role already deleted or may not be found", e.getMessage());
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to add Portal Admin role ", e);
				throw e;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<EPUser> getUserListWithUserid(Long userId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion orgUserIdCriterion = Restrictions.eq("id", userId);
		restrictionsList.add(orgUserIdCriterion);
		return (List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null);
	}
	
	private void logQuery(String sql) {
		logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
	}

	private boolean isLoggedInUserPortalAdmin(Long userId) {
		try {
			String sql = "SELECT u.user_id, u.first_name, u.last_name, u.login_id "
					+ " FROM fn_user u, fn_user_role ur " + " WHERE u.user_id = ur.user_id " + " AND ur.user_id="
					+ userId + " AND ur.role_id=" + SYS_ADMIN_ROLE_ID;

			logQuery(sql);

			@SuppressWarnings("unchecked")
			List<PortalAdmin> portalAdmins = dataAccessService.executeSQLQuery(sql, PortalAdmin.class, null);
			logger.debug(EELFLoggerDelegate.debugLogger, portalAdmins.toString());
			if (portalAdmins == null || portalAdmins.size() <= 0) {
				return false;
			}
			return true;

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "isLoggedInUserPortalAdmin failed", e);
			return false;
		}
	}
}
