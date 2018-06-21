/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.portalapp.music.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.portalapp.music.service.MusicService;


public class MusicProperties {
	
	public static final String MUSIC_ENDPOINT = "music.endpoint";
	
	public static final String MUSIC_VERSION = "music.version";
	
	public static final String MUSIC_KEYSPACE = "music.keyspace";
	
	public static final String MUSIC_SESSION_KEYSPACE = "music.session.keyspace";

	public static final String MUSIC_TABLES = "TABLES";
	
	public static final String MUSIC_SESSION_ATTR_TABLES = "music.session.attr.tables";
	
	public static final String MUSIC_SESSION_META_TABLES = "music.session.meta.tables";
	
	public static final String MUSIC_ROWS = "ROW";
	
	public static final String MUSIC_SESSION_ROW = "music.sesion.rows";

	public static final String MUSIC_X_MINOR_VERSION = "music.x.minor.version";
	
	public static final String MUSIC_X_PATCH_VERSION = "music.x.patch.version";
	
	public static final String MUSIC_AID = "AID";
	
	public static final String MUSIC_NS = "music.ns";
	
	public static final String MUSIC_USER_ID = "music.user.id";
	
	public static final String MUSIC_PASSWORD = "music.password";
	
	public static final String MUSIC_CONSISTENCYINFO = "music.consistency.info";
	
	public static final String MUSIC_CONSISTENCYINFO_VALUE = "music.consistency.info.value";
	
	public static final String MUSIC_CACHE = "music.cache";
	
	public static final String MUSIC_SERIALIZE_COMPRESS = "music.serialize.compress";

	public static final String MUSIC_ATOMIC_GET = "music.atomic.get";
		
	public static final String MUSIC_ATOMIC_PUT = "music.atomic.put";
		
	public static final String MUSIC_ATOMIC_POST = "music.atomic.post";
	
	public static final String MUSIC_EXCLUDE_API = "music.exclude.api";
	
	public static final String MUSIC_CLEAN_UP_FREQUENCY = "music.cleanup.frequency";
	
	public static final String MUSIC_CLEAN_UP_THRESHOLD = "music.cleanup.threshold";
	
	public static final String MUSIC_ENABLE = "music.enable";
	
	public static final String SESSION_MAX_INACTIVE_INTERVAL_SECONDS = "music.session.max.inactive.interval.seconds";
	
	public static final String ATTRIBUTE_NAME = "ATTRIBUTE_NAME";
	
	public static final String ATTRIBUTE_BYTES = "ATTRIBUTE_BYTES";
	
	public static final String ATTRIBUTE_CLASS = "ATTRIBUTE_CLASS";
	
	public static final String PRIMARY_ID = "PRIMARY_ID";
	
	public static final String SESSION_ID = "SESSION_ID";
	
	public static final String CREATION_TIME = "CREATION_TIME";
	
	public static final String LAST_ACCESS_TIME = "LAST_ACCESS_TIME";
	
	public static final String MAX_INACTIVE_INTERVAL = "MAX_INACTIVE_INTERVAL";
	
	public static final String EXPIRY_TIME = "EXPIRY_TIME";
	
	public static final String PRINCIPAL_NAME = "PRINCIPAL_NAME";
	
	private MusicProperties(){}

	private static Properties properties;
	
	private static String propertyFileName = "music.properties";

	private static final Object lockObject = new Object();
	
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicProperties.class);

	/**
	 * Gets the property value for the specified key. If a value is found, leading
	 * and trailing space is trimmed.
	 *
	 * @param property
	 *            Property key
	 * @return Value for the named property; null if the property file was not
	 *         loaded or the key was not found.
	 */
	public static String getProperty(String property) {
		if (properties == null) {
			synchronized (lockObject) {
				try {
					if (!initialize()) {
						logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + propertyFileName);
						return null;
					}
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + propertyFileName ,e);
					return null;
				}
			}
		}
		String value = properties.getProperty(property);
		if (value != null)
			value = value.trim();
		return value;
	}

	/**
	 * Reads properties from a portal.properties file on the classpath.
	 * 
	 * Clients do NOT need to call this method. Clients MAY call this method to test
	 * whether the properties file can be loaded successfully.
	 * 
	 * @return True if properties were successfully loaded, else false.
	 * @throws IOException
	 *             On failure
	 */
	private static boolean initialize() throws IOException {
		if (properties != null)
			return true;
		InputStream in = MusicProperties.class.getClassLoader().getResourceAsStream(propertyFileName);
		if (in == null)
			return false;
		properties = new Properties();
		try {
			properties.load(in);
		} finally {
			in.close();
		}
		return true;
	}
}
