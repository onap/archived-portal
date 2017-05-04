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
package org.openecomp.portalapp.portal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

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
