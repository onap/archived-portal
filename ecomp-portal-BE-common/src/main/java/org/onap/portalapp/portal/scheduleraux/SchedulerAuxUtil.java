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
package org.onap.portalapp.portal.scheduleraux;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.glassfish.jersey.client.ClientResponse;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

public class SchedulerAuxUtil {
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerAuxUtil.class);
	
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	public static SchedulerAuxResponseWrapper wrapResponse ( String body, int statusCode ) {
		
		SchedulerAuxResponseWrapper w = new SchedulerAuxResponseWrapper();
		w.setStatus (statusCode);
		w.setEntity(body);
		
		return w;
	}
	
	public static SchedulerAuxResponseWrapper wrapResponse (ClientResponse cres) {	
		String resp_str = "";
		if ( cres != null ) {
			resp_str = cres.readEntity(String.class);
		}
		int statuscode = cres.getStatus();
		SchedulerAuxResponseWrapper w = SchedulerAuxUtil.wrapResponse ( resp_str, statuscode );
		return (w);
	}
	
	public static SchedulerAuxResponseWrapper wrapResponse (RestObject<String> rs) {	
		String resp_str = "";
		int status = 0;
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}
		SchedulerAuxResponseWrapper w = SchedulerAuxUtil.wrapResponse ( resp_str, status );
		return (w);
	}
	
	
}
