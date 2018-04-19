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
package org.onap.portalapp.portal.scheduler;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "${container.classpath:}/WEB-INF/conf/scheduler.properties" })

public class SchedulerProperties {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerProperties.class);

	private static Environment environment;


	public SchedulerProperties() {
	}

	protected Environment getEnvironment() {
		return environment;
	}

	@Autowired
	public void setEnvironment(Environment environment) {
		SchedulerProperties.environment = environment;
	}

	/**
	 * Tests whether a property value is available for the specified key.
	 * 
	 * @param key
	 *            Property key
	 * @return True if the key is known, otherwise false.
	 */
	public static boolean containsProperty(String key) {
		return environment.containsProperty(key);
	}

	/**
	 * Returns the property value associated with the given key (never
	 * {@code null}), after trimming any trailing space.
	 * 
	 * @param key
	 *            Property key
	 * @return Property value; the empty string if the environment was not
	 *         autowired, which should never happen.
	 * @throws IllegalStateException
	 *             if the key cannot be resolved
	 */
	public static String getProperty(String key) {
		String value = "";
		if (environment == null) {
			logger.error(EELFLoggerDelegate.errorLogger, "getProperty: environment is null, should never happen!");
		} else {
			value = environment.getRequiredProperty(key);
			// java.util.Properties preserves trailing space
			if (value != null)
				value = value.trim();
		}
		return value;
	}

	public static final String SCHEDULER_USER_NAME_VAL = "scheduler.user.name";

	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";

	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";

	public static final String SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL = "scheduler.create.new.vnf.change.instance";

	public static final String SCHEDULER_GET_TIME_SLOTS = "scheduler.get.time.slots";

	public static final String SCHEDULER_SUBMIT_NEW_VNF_CHANGE = "scheduler.submit.new.vnf.change";

	public static final String SCHEDULERAUX_CLIENTAUTH_VAL = "scheduleraux.ClientAuth";

	public static final String SCHEDULERAUX_CLIENT_MECHID_VAL = "scheduleraux.client.mechId";

	public static final String SCHEDULERAUX_CLIENT_PASSWORD_VAL = "scheduleraux.client.password";

	public static final String SCHEDULERAUX_USERNAME_VAL = "scheduleraux.username";

	public static final String SCHEDULERAUX_PASSWORD_VAL = "scheduleraux.password";

	public static final String SCHEDULERAUX_AUTHORIZATION_VAL = "scheduleraux.Authorization";

	public static final String SCHEDULERAUX_SERVER_URL_VAL = "scheduleraux.server.url";

	public static final String SCHEDULERAUX_ENVIRONMENT_VAL = "scheduleraux.environment";

	public static final String SCHEDULERAUX_GET_CONFIG_VAL = "scheduleraux.get.config";
	

	/** The Constant VID_TRUSTSTORE_FILENAME. */
	public static final String VID_TRUSTSTORE_FILENAME = "vid.truststore.filename";

	/** The Constant VID_TRUSTSTORE_PASSWD_X. */
	public static final String VID_TRUSTSTORE_PASSWD_X = "vid.truststore.passwd.x";

	/** The Constant FILESEPARATOR. */
	public static final String FILESEPARATOR = (System.getProperty("file.separator") == null) ? "/" : System.getProperty("file.separator");
	
	/** Scheduler UI constant **/
	public static final String SCHEDULER_DOMAIN_NAME = "scheduler.domain.name";

	public static final String SCHEDULER_SCHEDULE_NAME = "scheduler.schedule.name";

	public static final String SCHEDULER_WORKFLOW_NAME = "scheduler.workflow.name";

	public static final String SCHEDULER_CALLBACK_URL = "scheduler.callback.url";
	
	public static final String SCHEDULER_APPROVAL_TYPE = "scheduler.approval.type";

	public static final String SCHEDULER_APPROVAL_SUBMIT_STATUS = "scheduler.approval.submit.status";

	public static final String SCHEDULER_APPROVAL_REJECT_STATUS = "scheduler.approval.reject.status";	
	
	public static final String SCHEDULER_INTERVAL_GET_TIMESLOT_RATE = "scheduler.interval.get.timeslot.rate";	
	
	public static final String SCHEDULER_POLICY_NAME = "scheduler.policy.name";

	public static final String SCHEDULER_GROUP_ID = "scheduler.group.id";	

}
