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
package org.openecomp.portalapp.util;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.FusionObject;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.domain.UrlsAccessible;
import org.openecomp.portalsdk.core.exception.SessionExpiredException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuBuilder;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EPUserUtils implements Serializable, FusionObject {
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUserUtils.class);

	public static final String KEY_USER_ROLES_CACHE = "userRoles";

	public static final String WJ_HEADER_USER_NAME = "iv-user";
	public static final String WJ_HEADER_USER_GROUP = "iv-groups";

	private static DataAccessService dataAccessService;

	private static final long serialVersionUID = 1L;

	public static EPUser getUserSession(HttpServletRequest request) {
		HttpSession session = AppUtils.getSession(request);

		if (session == null) {
			throw new SessionExpiredException();
		}

		return (EPUser) session.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
	}

	@SuppressWarnings("rawtypes")
	public static void setUserSession(HttpServletRequest request, EPUser user, Set applicationMenuData,
			Set businessDirectMenuData, String loginMethod) {
		HttpSession session = request.getSession(true);

		EPUserUtils.clearUserSession(request); // let's clear the current user
												// session to avoid any
												// conflicts during the set

		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		getRoleFunctions(request);

		// truncate the role (and therefore the role function) data to save
		// memory in the session
		user.setEPRoles(null);
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_NAME), user.getFullName());
		session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME), "My Portal");
		session.setAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME),
				MenuBuilder.filterMenu(applicationMenuData, request));
		session.setAttribute(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME),
				MenuBuilder.filterMenu(businessDirectMenuData, request));
	}

	public static void clearUserSession(HttpServletRequest request) {
		HttpSession session = AppUtils.getSession(request);

		if (session == null) {
			throw new SessionExpiredException();
		}

		// removes all stored attributes from the current user's session
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));
		session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set getRoleFunctions(HttpServletRequest request) {
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

	@SuppressWarnings("rawtypes")
	public static HashMap getRoles(HttpServletRequest request) {
		HashMap roles = null;

		// HttpSession session = request.getSession();
		HttpSession session = AppUtils.getSession(request);
		roles = (HashMap) session.getAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));

		// if roles are not already cached, let's grab them from the user
		// session
		if (roles == null) {
			EPUser user = getUserSession(request);

			// get all user roles (including the tree of child roles)
			roles = getAllUserRoles(user);

			session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME),
					getAllUserRoles(user));
		}

		return roles;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getAllUserRoles(EPUser user) {
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

		return roles;
	}

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

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static boolean isUrlAccessible(HttpServletRequest request, String currentUrl) {
		boolean isAccessible = false;

		Map params = new HashMap();
		params.put("current_url", currentUrl);

		List list = getDataAccessService().executeNamedQuery("restrictedUrls", params, null);

		// loop through the list of restricted URL's
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				/*
				 * Object[] restrictedUrl = (Object[])list.get(i);
				 * 
				 * String url = (String)restrictedUrl[0]; String functionCd =
				 * (String)restrictedUrl[1];
				 */
				UrlsAccessible urlFunctions = (UrlsAccessible) list.get(i);

				String url = (String) urlFunctions.getUrl();
				String functionCd = (String) urlFunctions.getFunctionCd();

				if (EPUserUtils.isAccessible(request, functionCd)) {
					isAccessible = true;
				}
			}
			return isAccessible;
		}

		return true;
	}

	public static boolean hasRole(HttpServletRequest request, String roleKey) {
		return getRoles(request).keySet().contains(new Long(roleKey));
	}

	public static boolean hasRole(EPUser user, String roleKey) {
		return getAllUserRoles(user).keySet().contains(new Long(roleKey));
	}

	public static boolean isAccessible(HttpServletRequest request, String functionKey) {
		return getRoleFunctions(request).contains(functionKey);
	}

	public static DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	@Autowired
	public void setDataAccessService(DataAccessService dataAccessService) {
		EPUserUtils.dataAccessService = dataAccessService;
	}

	public static int getUserId(HttpServletRequest request) {
		return getUserIdAsLong(request).intValue();
	}

	public static Long getUserIdAsLong(HttpServletRequest request) {
		Long userId = new Long(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID));

		if (request != null) {
			if (getUserSession(request) != null) {
				userId = getUserSession(request).getId();
			}
		}

		return userId;
	}
	
	public static String getRequestId(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();

		String requestId = "";
		try {
			while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			logger.debug(EELFLoggerDelegate.debugLogger, "One header is " + headerName + " : " + request.getHeader(headerName));
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
