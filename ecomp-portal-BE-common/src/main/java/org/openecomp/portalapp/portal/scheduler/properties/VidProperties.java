/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ============LICENSE_END=========================================================
 */

package org.openecomp.portalapp.portal.scheduler.properties;
import org.openecomp.portalsdk.core.util.SystemProperties;
/**
 * The Class VidProperties.
 */
public class VidProperties extends SystemProperties {

	//VID General Properties
	
	/** The Constant VID_TRUSTSTORE_FILENAME. */
	public static final String VID_TRUSTSTORE_FILENAME = "vid.truststore.filename";
	
	/** The Constant VID_TRUSTSTORE_PASSWD_X. */
	public static final String VID_TRUSTSTORE_PASSWD_X = "vid.truststore.passwd.x";
	
	/** The Constant FILESEPARATOR. */
	public static final String FILESEPARATOR = (System.getProperty("file.separator") == null) ? "/" : System.getProperty("file.separator");
	
	
}
