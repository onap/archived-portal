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
 * 
 */
package org.onap.portalapp.portal.utils;

public interface PortalConstants {
	public static final Long PORTAL_APP_ID = 1L;
	public static final Long DEFAULT_NOTIFICATION_CREATOR = 1L;
	public static final String REST_AUX_API = "/auxapi";
	public static final String PORTAL_AUX_API = "/portalApi";
	public static final Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	public static final Long SYS_ADMIN_ROLE_ID = 1L;
	public static final String ADMIN_ROLE = "Account Administrator";
	public static final String PORTAL_ADMIN_ROLE = "System Administrator";
	public static final Integer AUDIT_LOG_COMMENT_SIZE = 990;
}
