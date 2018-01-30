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
package org.onap.portalapp.portal.utils;

import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({ 
	@PropertySource ("/WEB-INF/conf/system.properties"),
	@PropertySource ("/WEB-INF/conf/sql.properties"),
    @PropertySource ("/WEB-INF/fusion/conf/fusion.properties"),
	@PropertySource (value = "file:${catalina.home}/conf/system.properties", ignoreResourceNotFound = true),
	@PropertySource (value = "file:${catalina.home}/conf/fusion.properties", ignoreResourceNotFound = true)
	})

/**
 * Contains properties specific to the ONAP version of the ECOMP Portal.
 */
public class EPSystemProperties extends EPCommonSystemProperties {
	public static final String CONTACT_US_URL = "contact_us_link";
	public static final String ECOMP_CONTEXT_ROOT = "context_root";

}