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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuBuilder;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.support.FusionService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eploginService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPLoginServiceImpl extends FusionService implements EPLoginService {
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPLoginServiceImpl.class);

    @SuppressWarnings("unused")
	private MenuBuilder  menuBuilder;
 
    @Autowired
	private DataAccessService  dataAccessService;


    @SuppressWarnings("rawtypes")
	public EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams ) throws Exception {
    	return findUser(bean, menuPropertiesFilename, additionalParams, true);
    }

	@SuppressWarnings("rawtypes")
	public EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams, boolean matchPassword) throws Exception {
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

		if (user != null) {

			// raise an error if the application is locked and the user does not have system administrator privileges
			if (AppUtils.isApplicationLocked() && !EPUserUtils.hasRole(user, SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID))) {
				bean.setLoginErrorMessage(SystemProperties.MESSAGE_KEY_LOGIN_ERROR_APPLICATION_LOCKED);
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeUserAdminPrivilegesInfo, user.getLoginId());
			}

			// raise an error if the user is inactive
			if (!user.getActive()) {
				bean.setLoginErrorMessage(SystemProperties.MESSAGE_KEY_LOGIN_ERROR_USER_INACTIVE);
				EPLogUtil.logEcompError(EPAppMessagesEnum.BeUserInactiveWarning, user.getLoginId());
			}

/*
 * Original SDK from QUANTUM
			boolean hasActiveRole = false;
			Iterator roles = user.getRoles().iterator();
			while (roles.hasNext()) {
				Role role = (Role) roles.next();
				hasActiveRole = true;
				if (role.getActive()) {
					break;
				}
			}
 */

			// raise an error if no active roles exist for the user
			if (!userHasActiveRoles(user.getEPUserApps())) {
				bean.setLoginErrorMessage(SystemProperties.MESSAGE_KEY_LOGIN_ERROR_USER_INACTIVE);
			}

			// only login the user if no errors have occurred
			if (bean.getLoginErrorMessage() == null) {

				// this will be a snapshot of the user's information as retrieved from the database
				userCopy = (EPUser) user.clone();

				// update the last logged in date for the user
				user.setLastLoginDate(new Date());
				getDataAccessService().saveDomainObject(user, additionalParams);

				// update the audit log of the user
				// Check for the client device type and set log attributes appropriately

				// save the above changes to the User and their audit trail

				// create the application menu based on the user's privileges
				Set appMenu = getMenuBuilder().getMenu(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_SET_NAME), dataAccessService);
				bean.setMenu(appMenu != null ? appMenu : new HashSet());
				Set businessDirectMenu = getMenuBuilder().getMenu(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_SET_NAME), dataAccessService);
				bean.setBusinessDirectMenu(businessDirectMenu != null ? businessDirectMenu : new HashSet());

				bean.setUser(userCopy);
			}

		} else {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeUserMissingError, bean.getOrgUserId());
		}
			

		return bean;
	}

	private boolean userHasActiveRoles(Set<EPUserApp> userApps) {
		for (EPUserApp userApp : userApps) {
			if (userApp.getRole().getActive()) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public EPLoginBean findUserWithoutPassword(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams ) throws Exception {
    	return findUser(bean, menuPropertiesFilename, additionalParams, false);
    }
	
    public EPUser findUser(String loginId, String password) {
      List<?>      list     = null;

      StringBuffer criteria = new StringBuffer();
      criteria.append(" where login_id = '").append(loginId).append("'")
              .append(" and login_pwd = '").append(password).append("'");
      
      try {
    	  list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
      } catch (Exception e) {
    	  EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
    	  String stackTrace = EcompPortalUtils.getStackTrace(e);
    	  logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing findUser operation. Details: " + stackTrace);
      }

      return (list == null || list.size() == 0) ? null : (EPUser) list.get(0);
    }
    
    @Override
    public EPUser findUserWithoutPwd(String loginId) {
        List<?>      list     = null;

        StringBuffer criteria = new StringBuffer();
        criteria.append(" where login_id = '").append(loginId).append("'");
        
        try {
        	list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
        } catch (Exception e) {
        	EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
      	  	String stackTrace = EcompPortalUtils.getStackTrace(e);
      	  	String message = "Exception occurred while performing findUser: '" + loginId + "'. Details: ";
      	  	logger.error(EELFLoggerDelegate.errorLogger, message + stackTrace);
        }

        return (list == null || list.size() == 0) ? null : (EPUser)list.get(0);
      }


    public EPUser findUser(EPLoginBean bean) {
      List<?>          list = null;

      StringBuffer criteria = new StringBuffer();
      criteria.append(" where org_user_id = '").append(bean.getOrgUserId()).append("'");
      
      try { 
    	  list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null); 
      } catch (Exception e) {
    	  EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
    	  String stackTrace = EcompPortalUtils.getStackTrace(e);
    	  logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while executing findUser operation. Details: " + stackTrace);
      }

      return (list == null || list.size() == 0) ? null : (EPUser)list.get(0);
    }


    public MenuBuilder getMenuBuilder() {
        return new MenuBuilder();
    }


    public void setMenuBuilder(MenuBuilder menuBuilder) {
        this.menuBuilder = menuBuilder;
    }

    
    public DataAccessService getDataAccessService() {
		return dataAccessService;
	}


	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}


}
