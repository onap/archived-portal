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
package org.openecomp.portalsdk.core.lm;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalsdk.core.util.SystemProperties;
//import org.openecomp.portalapp.lm.FusionLicenseManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class FusionLicenseManagerUtils {
	
	@Autowired
	private FusionLicenseManager licenseManager;

	@Autowired
	private EPCommonSystemProperties sysProps;
	static {
	}
		
	public int verifyLicense(ServletContext context) {
		if(sysProps == null) {
			try {
				sysProps = new EPCommonSystemProperties();
				sysProps.setServletContext(context);
//				sysProps.load();
//				licenseManager = new FusionLicenseManagerImpl(new LicenseableClassImpl());
				System.out.println(licenseManager);
				licenseManager.installLicense();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
		
//		WebApplicationContext ctx =RequestContextUtils.getWebApplicationContext(request);
//		int flag = ((FusionLicenseManager)ctx.getBean("fusionLicenseManager")).verifyLicense(request);
//		logger.debug("****************FLAG ******************** " + flag);
//		return flag;
//		return 2;
		return licenseManager.verifyLicense(context);
	}

	public static Date getLicenseExpiryDate(HttpServletRequest request) {
		WebApplicationContext ctx =RequestContextUtils.getWebApplicationContext(request);
		return ((FusionLicenseManager)ctx.getBean("fusionLicenseManager")).getExpiredDate();
	}
}
