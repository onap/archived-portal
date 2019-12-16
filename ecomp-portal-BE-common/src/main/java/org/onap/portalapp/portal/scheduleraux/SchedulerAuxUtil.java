/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

import org.glassfish.jersey.client.ClientResponse;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

public class SchedulerAuxUtil {

    public static SchedulerAuxResponseWrapper wrapResponse(String body, int statusCode) {

        SchedulerAuxResponseWrapper w = new SchedulerAuxResponseWrapper();
        w.setStatus(statusCode);
        w.setEntity(body);

        return w;
    }

    public static SchedulerAuxResponseWrapper wrapResponse(ClientResponse cres) {
        String respStr = "";
        int statuscode = 0;
        if (cres != null) {
            respStr = cres.readEntity(String.class);
            statuscode = cres.getStatus();
        }
        SchedulerAuxResponseWrapper w = SchedulerAuxUtil.wrapResponse(respStr, statuscode);
        return (w);
    }

    public static SchedulerAuxResponseWrapper wrapResponse(RestObject<String> rs) {
        String respStr = "";
        int status = 0;
        if (rs != null) {
            respStr = rs.get();
            status = rs.getStatusCode();
        }
        SchedulerAuxResponseWrapper w = SchedulerAuxUtil.wrapResponse(respStr, status);
        return (w);
    }

}
