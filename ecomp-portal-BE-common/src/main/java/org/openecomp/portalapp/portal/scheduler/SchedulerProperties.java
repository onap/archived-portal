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
package org.openecomp.portalapp.portal.scheduler;

import org.openecomp.portalsdk.core.util.SystemProperties;

public class SchedulerProperties extends SystemProperties {

	public static final String SCHEDULER_USER_NAME_VAL = "scheduler.user.name";;

	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";

	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";

	public static final String SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL = "scheduler.create.new.vnf.change.instance";

	public static final String SCHEDULER_GET_TIME_SLOTS = "scheduler.get.time.slots";

	public static final String SCHEDULER_SUBMIT_NEW_VNF_CHANGE = "scheduler.submit.new.vnf.change";

}
