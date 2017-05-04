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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.PortalAdmin;
import org.openecomp.portalapp.portal.transport.PortalAdminUserRole;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;

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

	@PostConstruct
	private void init() {
		SYS_ADMIN_ROLE_ID = SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID);
		ECOMP_APP_ID = SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID);
	}

	public List<PortalAdmin> getPortalAdmins() {
		try {
			String sql = "SELECT u.user_id, u.first_name, u.last_name, u.login_id "
					+ " FROM fn_user u, fn_user_role ur WHERE u.user_id = ur.user_id AND ur.role_id="
					+ SYS_ADMIN_ROLE_ID;
			logQuery(sql);

			@SuppressWarnings("unchecked")
			List<PortalAdmin> portalAdmins = dataAccessService.executeSQLQuery(sql, PortalAdmin.class, null);
			logger.debug(EELFLoggerDelegate.debugLogger, "getPortalAdmins was successful");
			return portalAdmins;

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"Exception occurred while performing getPortalAdmins operation, Details: "
							+ EcompPortalUtils.getStackTrace(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked") 
	public FieldsValidator createPortalAdmin(String orgUserId) {
		FieldsValidator fieldsValidator = new FieldsValidator(); 
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: createPortalAdmin: test 1"); 
		boolean result = false; 
		EPUser user = null; 
		boolean createNewUser = false; 
		List<EPUser> localUserList = dataAccessService.getList(EPUser.class, " where orgUserId='" + orgUserId + "'",
				null, null); 
		if (localUserList.size() > 0) { 
			user = localUserList.get(0); 
		} else { 
			createNewUser = true; 
		} 
 
		if (user!=null && isLoggedInUserPortalAdmin(user.getId())) { 
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT); 
			logger.error(EELFLoggerDelegate.errorLogger, "User '" + user.getOrgUserId() + "' already has PortalAdmin role assigned."); 
		} 
		else if (user != null || createNewUser) { 
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
	//			logger.debug(EELFLoggerDelegate.debugLogger, "after saving menu object, new id: " + userid); 
 
				transaction.commit(); 
				result = true; 
			} catch (Exception e) { 
				EcompPortalUtils.rollbackTransaction(transaction, 
						"createPortalAdmin rollback, exception = " + e); 
				logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e)); 
			} finally { 
				EcompPortalUtils.closeLocalSession(localSession, "createPortalAdmin"); 
			} 
			if (!result) { 
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: createPortalAdmin: no result. setting httpStatusCode to " 
						+ HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
				logger.error(EELFLoggerDelegate.errorLogger, "PortalAdminServiceImpl.createPortalAdmin: bad request"); 
			} 
		} 
		return fieldsValidator; 
	}
	
	public FieldsValidator deletePortalAdmin(Long userId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		logger.debug(EELFLoggerDelegate.debugLogger, "deletePortalAdmin: test 1");
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;

		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			dataAccessService.deleteDomainObjects(PortalAdminUserRole.class,
					"user_id='" + userId + "' AND role_id='" + SYS_ADMIN_ROLE_ID + "'", null);
			transaction.commit();
			result = true;
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction, "deletePortalAdmin rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "deletePortalAdmin");
		}
		if (result) {
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, "deletePortalAdmin: no result. setting httpStatusCode to "
					+ HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}

	private void logQuery(String sql) {
		logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
	}
	
	private boolean isLoggedInUserPortalAdmin(Long userId) { 
		try { 
			String sql = "SELECT u.user_id, u.first_name, u.last_name, u.login_id " + 
					" FROM fn_user u, fn_user_role ur " + 
					" WHERE u.user_id = ur.user_id " + 
					" AND ur.user_id=" + userId + 
					" AND ur.role_id=" + SYS_ADMIN_ROLE_ID;
 
			logQuery(sql); 
 
			@SuppressWarnings("unchecked") 
			List<PortalAdmin> portalAdmins = dataAccessService.executeSQLQuery(sql, PortalAdmin.class, null); 
			logger.debug(EELFLoggerDelegate.debugLogger, portalAdmins.toString()); 
			if (portalAdmins==null || portalAdmins.size()<=0) { 
				return false; 
			} 
			return true; 
 
		} catch (Exception e) { 
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing isLoggedInUserPortalAdmin operation, Details: " + EcompPortalUtils.getStackTrace(e)); 
			return false; 
		} 
	}
}
