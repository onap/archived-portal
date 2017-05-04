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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuBuilder;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.support.FusionService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.AppUtils;
import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.util.EPUserUtils;

@Service("eploginService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPLoginServiceImpl extends FusionService implements EPLoginService {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPLoginServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPLoginService#findUser(org.openecomp.portalapp.command.EPLoginBean, java.lang.String, java.util.HashMap)
	 */
	@SuppressWarnings("rawtypes")
	public EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams)
			throws Exception {
		return findUser(bean, menuPropertiesFilename, additionalParams, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPLoginService#findUser(org.openecomp.portalapp.command.EPLoginBean, java.lang.String, java.util.HashMap, boolean)
	 */
	@SuppressWarnings("rawtypes")
	public EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename_ignored, HashMap additionalParams,
			boolean matchPassword) throws Exception {
		EPUser user = null;
		EPUser userCopy = null;

		if (bean.getOrgUserId() != null) {
			user = (EPUser) findUser(bean);
		} else {
			if (matchPassword)
				user = (EPUser) findUser(bean.getLoginId(), bean.getLoginPwd());
			else
				user = (EPUser) findUserWithoutPwd(bean.getLoginId());
		}

		// run this command to fetch more information from the lazily loaded
		// object

		// This is funny - commenting out the following method call
		// 1. What are we doing with the returned values of the following two
		// methods? Nothing.
		// 2. Use a guest user scenario - user object will be null - clealry,
		// NPE.
		// 3. A check of if(user !=null) is made AFTER these bogus calls :) - If
		// these calls WERE doing anything significat (which they are not),
		// shouln't they have been moved inside that if check?

		// user.getEPUserApps();

		// Comments
		// 1. This method is clearly doing more than 'getting roles' - Not a
		// good name -
		// 2. Also, there is no null check - guest user scenarios will break the
		// code with NPE - added the check - Do not want to remove the call
		// altogether - not sure how it will effect things.

		if (user != null) {
			user.getEPRoles();

			// raise an error if the portal application is locked and the user
			// does not
			// have system administrator privileges
			if (AppUtils.isApplicationLocked()
					&& !EPUserUtils.hasRole(user, SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID))) {
				bean.setLoginErrorMessage(SystemProperties.MESSAGE_KEY_LOGIN_ERROR_APPLICATION_LOCKED);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUserAdminPrivilegesInfo, user.getLoginId());
			}

			// raise an error if the user is inactive
			if (!user.getActive()) {
				bean.setLoginErrorMessage(SystemProperties.MESSAGE_KEY_LOGIN_ERROR_USER_INACTIVE);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUserInactiveWarning, user.getLoginId());
			}

			// only login the user if no errors have occurred
			if (bean.getLoginErrorMessage() == null) {

				// this will be a snapshot of the user's information as
				// retrieved from the database
				userCopy = (EPUser) user.clone();

				// update the last logged in date for the user
				user.setLastLoginDate(new Date());
				getDataAccessService().saveDomainObject(user, additionalParams);

				// create the application menu based on the user's privileges
				MenuBuilder menuBuilder = new MenuBuilder();
				Set appMenu = menuBuilder.getMenu(
						SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_SET_NAME), dataAccessService);
				bean.setMenu(appMenu != null ? appMenu : new HashSet());
				Set businessDirectMenu = menuBuilder.getMenu(
						SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_SET_NAME),
						dataAccessService);
				bean.setBusinessDirectMenu(businessDirectMenu != null ? businessDirectMenu : new HashSet());

				bean.setUser(userCopy);
			}

		} else {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUserMissingError, bean.getOrgUserId());
		}

		return bean;
	}

	/**
	 * Searches the fn_user table for a row that matches the specified login_id
	 * and login_pwd values.
	 * 
	 * @param loginId
	 * @param password
	 * @return EPUser object; null on error or if no match.
	 */
	private EPUser findUser(String loginId, String password) {
		List<?> list = null;

		StringBuffer criteria = new StringBuffer();
		criteria.append(" where login_id = '").append(loginId).append("'").append(" and login_pwd = '").append(password)
				.append("'");

		try {
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "findUser(String) failed on " + loginId, e);
		}

		return (list == null || list.size() == 0) ? null : (EPUser) list.get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPLoginService#findUserWithoutPwd(java.lang.String)
	 */
	@Override
	public EPUser findUserWithoutPwd(String loginId) {
		List<?> list = null;

		StringBuffer criteria = new StringBuffer();
		criteria.append(" where login_id = '").append(loginId).append("'");

		try {
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			String message = "findUserWithoutPwd failed on " + loginId;
			logger.error(EELFLoggerDelegate.errorLogger, message, e);
		}

		return (list == null || list.size() == 0) ? null : (EPUser) list.get(0);
	}

	/**
	 * Searches the fn_user table for a row that matches the value of the bean's
	 * Organization User ID property.
	 * 
	 * @param bean
	 * @return EPUser object; null on error or if no match.
	 */
	private EPUser findUser(EPLoginBean bean) {
		List<?> list = null;

		StringBuffer criteria = new StringBuffer();
		criteria.append(" where orgUserId = '").append(bean.getOrgUserId()).append("'");

		try {
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			logger.error(EELFLoggerDelegate.errorLogger, "findUser(EPLoginBean) failed", e);
		}

		return (list == null || list.size() == 0) ? null : (EPUser) list.get(0);
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

}
