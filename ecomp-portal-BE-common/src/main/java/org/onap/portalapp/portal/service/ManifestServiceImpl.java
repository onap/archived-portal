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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

@Service("manifestService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class ManifestServiceImpl implements ManifestService {
	@Autowired
	ServletContext context;

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.ManifestService#getWebappManifest()
	 */
	public Attributes getWebappManifest() throws IOException {
		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ManifestServiceImpl.class);
		// Path to resource on classpath
		final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
		// Manifest is formatted as Java-style properties
		try {
			InputStream inputStream = context.getResourceAsStream(MANIFEST_RESOURCE_PATH);
			Manifest manifest = new Manifest(inputStream);
			inputStream.close();
			return manifest.getMainAttributes();
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getWebappManifest: failed to read/find manifest");
			throw e;
		}
	}
}
