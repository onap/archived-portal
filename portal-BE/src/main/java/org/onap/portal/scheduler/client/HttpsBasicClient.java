/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.portal.scheduler.client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.onap.portal.scheduler.SchedulerProperties;
import org.eclipse.jetty.util.security.Password;
import org.onap.portal.scheduler.util.CustomJacksonJaxBJsonProvider;
import org.onap.portal.utils.DateUtil;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

public class HttpsBasicClient {

    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpsBasicClient.class);

    public static Client getClient() throws Exception {
        String methodName = "getClient";
        ClientConfig config = new ClientConfig();

        SSLContext ctx = null;

        try {

            SimpleDateFormat dateFormat = DateUtil.getDateFormat();
            config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

            String truststore_path = SchedulerProperties.getProperty(SchedulerProperties.VID_TRUSTSTORE_FILENAME);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + " " + methodName + " "
                + "truststore_path=" +
                truststore_path);
            String truststore_password = SchedulerProperties.getProperty(SchedulerProperties.VID_TRUSTSTORE_PASSWD_X);

            String decrypted_truststore_password = Password.deobfuscate(truststore_password);

            File tr = new File(truststore_path);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + " " + methodName + " absolute "
                + "truststore path=" + tr.getAbsolutePath());
            System.setProperty("javax.net.ssl.trustStore", truststore_path);
            System.setProperty("javax.net.ssl.trustStorePassword", decrypted_truststore_password);
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, null, null);

            return ClientBuilder.newBuilder()
                .sslContext(ctx)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }).withConfig(config)
                .build()
                .register(CustomJacksonJaxBJsonProvider.class);

        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up config: exiting");
            e.printStackTrace();
            return null;
        }
    }
}
