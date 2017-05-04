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
package org.openecomp.portalapp.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.exception.SessionExpiredException;
import org.openecomp.portalsdk.core.lm.FusionLicenseManager;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuBuilder;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EPUserUtils {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUserUtils.class);

	private final static Long ACCOUNT_ADMIN_ROLE_ID = 999L;

	public static final String ALL_ROLE_FUNCTIONS = "allRoleFunctions";

	private static DataAccessService dataAccessService;

	/**
	 * Gets the EPUser object from the session.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return EPUser object that was created upon login
	 * @throws SessionExpiredException
	 *             if no session exists.
	 */
	public static EPUser getUserSession(HttpServletRequest request) {
		HttpSession session = AppUtils.getSession(request);
		if (session == null)
			throw new SessionExpiredException();
		return (EPUser) session.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
	}

	/**
	 * Establishes the user's portal session
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param user
	 *            EPUser
	 * @param applicationMenuData
	 *            Menu data
	 * @param businessDirectMenuData
	 *            Menu data
	 * @param loginMethod_ignored
	 *            How the user authenticated; ignored
	 * @param allRoleFunctions
	 *            Set of user's roles
	 */
	@SuppressWarnings("rawtypes")
	public static void setUserSession(HttpServletRequest request, EPUser user, Set applicationMenuData,
			Set businessDirectMenuData, String loginMethod_ignored, List<RoleFunction> allRoleFunctions) {
		HttpSession session = request.getSession(true);

		// clear the current user session to avoid any conflicts
		EPUserUtils.clearUserSession(request);
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		getAllRoleFunctions(allRoleFunctions, session);

		getRoleFunctions(request);

		// truncate the role (and therefore the role function) data to save
		// memory in the session
		user.setEPRoles(null);
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_NAME), user.getFullName());

		ServletContext context = session.getServletContext();
		int licenseVerificationFlag = 3;
		try {
			licenseVerificationFlag = (Integer) context.getAttribute("licenseVerification");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "setUserSession failed to get licenseVerification attribute",
					e);
		}
		switch (licenseVerificationFlag) {
		case FusionLicenseManager.DEVELOPER_LICENSE:
			session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME),
					"My Portal [Development Version]");
			break;
		case FusionLicenseManager.EXPIRED_LICENSE:
			session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME),
					"My Portal [LICENSE EXPIRED]");
			break;
		case FusionLicenseManager.VALID_LICENSE:
			session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME), "My Portal");
			break;
		default:
			session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME),
					"My Portal [INVALID LICENSE]");
			break;
		}

		session.setAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME),
				MenuBuilder.filterMenu(applicationMenuData, request));
		session.setAttribute(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME),
				MenuBuilder.filterMenu(businessDirectMenuData, request));
	}

	/**
	 * Creates a set of role function names and stores the set as a session
	 * attribute.
	 * 
	 * @param allRoleFunctions
	 *            List of role functions.
	 * @param session
	 *            HttpSession
	 */
	private static void getAllRoleFunctions(List<RoleFunction> allRoleFunctions, HttpSession session) {
		if (allRoleFunctions == null)
			return;
		Set<String> roleFnSet = new HashSet<String>();
		for (RoleFunction roleFn : allRoleFunctions)
			roleFnSet.add(roleFn.getCode());
		session.setAttribute(ALL_ROLE_FUNCTIONS, roleFnSet);
	}

	/**
	 * Removes all stored attributes from the user's session
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @throws SessionExpiredException
	 *             if no session exists
	 */
	private static void clearUserSession(HttpServletRequest request) {
		HttpSession session = AppUtils.getSession(request);
		if (session == null)
			throw new SessionExpiredException();

		// removes all stored attributes from the current user's session
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));
	}

	/**
	 * Builds a set of role functions and sets a session attribute with it.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Set of role functions that was built.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Set getRoleFunctions(HttpServletRequest request) {
		HashSet roleFunctions = null;

		HttpSession session = request.getSession();
		roleFunctions = (HashSet) session
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));

		if (roleFunctions == null) {
			HashMap roles = getRoles(request);
			roleFunctions = new HashSet();

			Iterator i = roles.keySet().iterator();

			while (i.hasNext()) {
				Long roleKey = (Long) i.next();
				EPRole role = (EPRole) roles.get(roleKey);

				Iterator j = role.getRoleFunctions().iterator();

				while (j.hasNext()) {
					RoleFunction function = (RoleFunction) j.next();
					roleFunctions.add(function.getCode());
				}
			}

			session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME),
					roleFunctions);
		}

		return roleFunctions;
	}

	/**
	 * Gets role information from the user session, in the cached user object.
	 * As a side effect sets a session variable with the roles.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Map of role ID to role object
	 */
	@SuppressWarnings("rawtypes")
	private static HashMap getRoles(HttpServletRequest request) {
		HashMap roles = null;

		HttpSession session = AppUtils.getSession(request);
		roles = (HashMap) session.getAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));

		// if roles are not already cached, let's grab them from the user
		// session
		if (roles == null) {
			EPUser user = getUserSession(request);

			// get all user roles (including the tree of child roles)
			roles = getAllUserRoles(user);

			session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME), roles);
		}

		return roles;
	}

	/**
	 * Builds a map of role ID to role object.
	 * 
	 * @param user
	 *            EPUser
	 * @return Map of role ID to role object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap getAllUserRoles(EPUser user) {
		HashMap roles = new HashMap();
		Iterator i = user.getEPRoles().iterator();

		while (i.hasNext()) {
			EPRole role = (EPRole) i.next();

			if (role.getActive()) {
				roles.put(role.getId(), role);

				// let's take a recursive trip down the tree to add all child
				// roles
				addChildRoles(role, roles);
			}
		}

		// Additionally; the account admin role is overloaded between ecomp
		// portal and partners; lets also include that
		Iterator<EPUserApp> appRolesIterator = user.getEPUserApps().iterator();
		while (appRolesIterator.hasNext()) {
			EPRole role = (EPRole) appRolesIterator.next().getRole();

			if (role.getActive() && role.getId().equals(ACCOUNT_ADMIN_ROLE_ID)) {
				roles.put(role.getId(), role);

				// let's take a recursive trip down the tree to add all child
				// roles
				addChildRoles(role, roles);
			}
		}

		return roles;
	}

	/**
	 * Adds all child roles of the specified role to the map of roles.
	 * 
	 * @param role
	 *            EPRole
	 * @param roles
	 *            Maps role id to role object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addChildRoles(EPRole role, HashMap roles) {
		Set childRoles = role.getChildRoles();

		if (childRoles != null && childRoles.size() > 0) {
			Iterator j = childRoles.iterator();
			while (j.hasNext()) {
				EPRole childRole = (EPRole) j.next();

				if (childRole.getActive()) {
					roles.put(childRole.getId(), childRole);

					addChildRoles(childRole, roles);
				}
			}
		}

	}

	public static boolean hasRole(EPUser user, String roleKey) {
		return getAllUserRoles(user).keySet().contains(new Long(roleKey));
	}

	public static DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	@Autowired
	public void setDataAccessService(DataAccessService dataAccessService) {
		EPUserUtils.dataAccessService = dataAccessService;
	}

	/**
	 * Gets the user's ID from the user object in the session
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Integer ID of current user
	 */
	public static int getUserId(HttpServletRequest request) {
		return getUserIdAsLong(request).intValue();
	}

	/**
	 * Gets the user's ID from the user object in the session
	 * 
	 * @param request
	 *            HttpServletREquest
	 * @return Long ID of current user
	 */
	public static Long getUserIdAsLong(HttpServletRequest request) {
		Long userId = new Long(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID));
		if (request != null) {
			if (getUserSession(request) != null) {
				userId = getUserSession(request).getId();
			}
		}
		return userId;
	}

	/**
	 * Gets the request ID from the request.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Request ID
	 */
	public static String getRequestId(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();

		String requestId = "";
		try {
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				logger.debug(EELFLoggerDelegate.debugLogger,
						"One header is " + headerName + " : " + request.getHeader(headerName));
				if (headerName.equalsIgnoreCase(SystemProperties.ECOMP_REQUEST_ID)) {
					requestId = request.getHeader(headerName);
					break;
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "HEADER!!!! Exception : " + EcompPortalUtils.getStackTrace(e));
		}

		return (requestId.isEmpty() ? UUID.randomUUID().toString() : requestId);
	}

	/**
	 * Gets the full URL from the request.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Full URL
	 */
	public static String getFullURL(HttpServletRequest request) {
		if (request != null) {
			StringBuffer requestURL = request.getRequestURL();
			String queryString = request.getQueryString();

			if (queryString == null) {
				return requestURL.toString();
			} else {
				return requestURL.append('?').append(queryString).toString();
			}
		}
		return "";
	}

}
