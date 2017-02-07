/*-
 * ================================================================================
 * eCOMP Portal
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
package org.openecomp.portalapp.kpidash.model;

import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "${container.classpath:}/WEB-INF/conf/kpidash.properties" })

public class KpidashProperties extends SystemProperties {
	public static final String GEO_MAP_URL = "url.sectiona";
	public static final String RCLOUD_A_URL = "url.sectionb";
	public static final String GEO_API_URL = "url.geomapapiurl";
}
