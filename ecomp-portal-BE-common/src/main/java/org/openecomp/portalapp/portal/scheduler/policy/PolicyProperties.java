/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.scheduler.policy;

import org.openecomp.portalsdk.core.util.SystemProperties;


public class PolicyProperties extends SystemProperties {
	
	public static final String POLICY_CLIENTAUTH_VAL =  "policy.ClientAuth";
	
	public static final String POLICY_CLIENT_MECHID_VAL =  "policy.client.mechId";
	
	public static final String POLICY_CLIENT_PASSWORD_VAL =  "policy.client.password";
	
	public static final String POLICY_USERNAME_VAL =  "policy.username";
	
	public static final String POLICY_PASSWORD_VAL =  "policy.password";
	
	public static final String POLICY_AUTHORIZATION_VAL = "policy.Authorization";
	
	public static final String POLICY_SERVER_URL_VAL = "policy.server.url";
	
	public static final String POLICY_ENVIRONMENT_VAL = "policy.environment";
	
	public static final String POLICY_GET_CONFIG_VAL = "policy.get.config"; 
	
}
