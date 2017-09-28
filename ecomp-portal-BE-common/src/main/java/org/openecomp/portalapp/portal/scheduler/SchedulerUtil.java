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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openecomp.portalapp.portal.scheduler.restobjects.GetTimeSlotsRestObject;
import org.openecomp.portalapp.portal.scheduler.restobjects.PostCreateNewVnfRestObject;
import org.openecomp.portalapp.portal.scheduler.restobjects.PostSubmitVnfChangeRestObject;
import org.openecomp.portalapp.portal.scheduler.wrapper.GetTimeSlotsWrapper;
import org.openecomp.portalapp.portal.scheduler.wrapper.PostCreateNewVnfWrapper;
import org.openecomp.portalapp.portal.scheduler.wrapper.PostSubmitVnfChangeTimeSlotsWrapper;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SchedulerUtil {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerUtil.class);

	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	public static GetTimeSlotsWrapper getTimeSlotsWrapResponse(GetTimeSlotsRestObject<String> rs) {

		String resp_str = "";
		int status = 0;

		if (rs != null) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}

		GetTimeSlotsWrapper w = new GetTimeSlotsWrapper();

		w.setEntity(resp_str);
		w.setStatus(status);

		return (w);
	}

	public static PostSubmitVnfChangeTimeSlotsWrapper postSubmitNewVnfWrapResponse(
			PostSubmitVnfChangeRestObject<String> rs) {

		String resp_str = "";
		int status = 0;
		String uuid = "";

		if (rs != null) {
			resp_str = rs.get();
			status = rs.getStatusCode();
			uuid = rs.getUUID();
		}

		PostSubmitVnfChangeTimeSlotsWrapper w = new PostSubmitVnfChangeTimeSlotsWrapper();

		w.setEntity(resp_str);
		w.setStatus(status);
		w.setUuid(uuid);

		return (w);
	}

	public static PostCreateNewVnfWrapper postCreateNewVnfWrapResponse(PostCreateNewVnfRestObject<String> rs) {

		String resp_str = "";
		int status = 0;
		String uuid = "";

		if (rs != null) {
			resp_str = rs.get();
			status = rs.getStatusCode();
			uuid = rs.getUUID();
		}

		PostCreateNewVnfWrapper w = new PostCreateNewVnfWrapper();

		w.setEntity(resp_str);
		w.setStatus(status);
		w.setUuid(uuid);

		return (w);
	}

	public static <T> String convertPojoToString(T t) throws com.fasterxml.jackson.core.JsonProcessingException {

		String methodName = "convertPojoToString";
		ObjectMapper mapper = new ObjectMapper();
		String r_json_str = "";
		if (t != null) {
			try {
				r_json_str = mapper.writeValueAsString(t);
			} catch (com.fasterxml.jackson.core.JsonProcessingException j) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						dateFormat.format(new Date()) + "<== " + methodName + " Unable to parse object as json");
			}
		}
		return (r_json_str);
	}

}
