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
package org.openecomp.portalapp.portal.utils;

import org.openecomp.portalsdk.core.util.SystemProperties;

public class EPCommonSystemProperties extends SystemProperties {

	public static final String LOGIN_URL_NO_RET_VAL           		= "login_url_no_ret_val";
	public static final String ECOMP_APP_ID                       	= "ecomp_app_id";
	public static final String SYS_ADMIN_ROLE_ID                  	= "sys_admin_role_id";
	public static final String DUBLICATED_FIELD_VALUE_ECOMP_ERROR 	= "1201";
	public static final String ACCOUNT_ADMIN_ROLE_ID              	= "account_admin_role_id";
	public static final String RESTRICTED_APP_ROLE_ID				= "restricted_app_role_id";
	public static final String FE_URL                        	  	= "frontend_url";
	public static final String HEALTH_POLL_INTERVAL_SECONDS       	= "health_poll_interval_seconds";
	public static final String HEALTHFAIL_ALERT_EVERY_X_INTERVALS 	= "health_fail_alert_every_x_intervals";
	public static final String USER_GUIDE_URL                       = "user_guide_link";
	
	public static final String USER_FIRST_NAME 						= "USER_FIRST_NAME";
	public static final String USER_LAST_NAME						= "USER_LAST_NAME";
	public static final String USER_EMAIL 							= "USER_EMAIL";
	public static final String USER_ORG_USERID						= "USER_ORG_USERID";
	
	public static final String USH_TICKET_URL						= "ush_ticket_url"; 

	public static final String EXTERNAL_API_RESPONSE_CODE			= "External_API_ResponseCode";
	public static final String COOKIE_DOMAIN                        = "cookie_domain";

	public static final String FEEDBACK_EMAIL_ADDRESS				= "feedback_email_address";
	public static final String PORTAL_INFO_URL						= "portal_info_url";
	
	public static final String ONLINE_USER_UPDATE_RATE				= "online_user_update_rate";
	public static final String ONLINE_USER_UPDATE_DURATION			= "online_user_update_duration";
	
	public static final String NOTIFICATION_UPDATE_RATE				= "notification_update_rate";
	public static final String NOTIFICATION_UPDATE_DURATION			= "notification_update_duration";
	
	public static final String WINDOW_WIDTH_THRESHOLD_LEFT_MENU		= "window_width_threshold_left_menu";
	public static final String WINDOW_WIDTH_THRESHOLD_RIGHT_MENU	= "window_width_threshold_right_menu";
		
	public static final String AUDITLOG_DEL_DAY_FROM				= "auditlog_del_day_from";
	public static final String AUDITLOG_DELETE_CRON   				= "auditlog_delete_cron";
	
	public static final String AUTH_USER_SERVER 					= "auth_user_server";
	public static final String EXTERNAL_ACCESS_ENABLE 				= "external_access_enable";
	public static final String WIDGET_MS_PROTOCOL 					= "microservices.widget.protocol";

}