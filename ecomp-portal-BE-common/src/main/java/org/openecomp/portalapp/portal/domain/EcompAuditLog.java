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
package org.openecomp.portalapp.portal.domain;

import java.util.Date;

import org.openecomp.portalsdk.core.domain.AuditLog;

public class EcompAuditLog extends AuditLog {

	private static final long serialVersionUID = 7970458389782626231L;
	
	// values of activity_cd column in table fu_lu_activity
	// as loaded by DML sql script
	public static final String CD_ACTIVITY_ADD_CHILD_ROLE = "add_child_role";
	public static final String CD_ACTIVITY_ADD_ROLE = "add_role";
	public static final String CD_ACTIVITY_ADD_ROLE_FUNCTION = "add_role_function";
	public static final String CD_ACTIVITY_ADD_USER_ROLE = "add_user_role";
	public static final String CD_ACTIVITY_APP_ACCESS = "app_access";
	public static final String CD_ACTIVITY_FUNCTIONAL_ACCESS = "functional_access";
	public static final String CD_ACTIVITY_GUEST_LOGIN = "guest_login";
	public static final String CD_ACTIVITY_LOGIN = "login";
	public static final String CD_ACTIVITY_LOGOUT = "logout";
	public static final String CD_ACTIVITY_MOBILE_LOGIN = "mobile_login";
	public static final String CD_ACTIVITY_MOBILE_LOGOUT = "mobile_logout";
	public static final String CD_ACTIVITY_REMOVE_CHILD_ROLE = "remove_child_role";
	public static final String CD_ACTIVITY_REMOVE_ROLE = "remove_role";
	public static final String CD_ACTIVITY_REMOVE_ROLE_FUNCTION = "remove_role_function";
	public static final String CD_ACTIVITY_REMOVE_USER_ROLE = "remove_user_role";
	public static final String CD_ACTIVITY_TAB_ACCESS = "tab_access";
	public static final String CD_ACTIVITY_SEARCH = "search";
	public static final String CD_ACTIVITY_ADD_PORTAL_ADMIN = "apa ";
	public static final String CD_ACTIVITY_DELETE_PORTAL_ADMIN = "dpa";
	public static final String CD_ACTIVITY_UPDATE_USER = "uu ";
	public static final String CD_ACTIVITY_UPDATE_ACCOUNT_ADMIN = "uaa ";
	public static final String CD_ACTIVITY_STORE_ANALYTICS = "store_analytics";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_ADD_ROLE = "eaar";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION = "eaaf";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION = "eauf";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_ROLE_AND_FUNCTION = "eaurf";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE = "eadr";
	public static final String CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION = "eadf";
	/**
	 * Creates a new object with the created field set to the current date-time.
	 */
	public EcompAuditLog() {
		setCreated(new Date());
	}
}
