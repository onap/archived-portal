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
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Trivial extension of Properties that populates itself from a known source.
 */
public class SharedContextTestProperties extends Properties {

	private static final long serialVersionUID = -4064100267979036550L;

	// property names
	public static final String HOSTNAME = "hostname";
	public static final String PORT = "port";
	public static final String SECURE = "secure";
	public static final String APPNAME = "appname";
	public static final String RESTPATH = "restpath";
	public static final String UEBKEY = "uebkey";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	/**
	 * Expected on the classpath
	 */
	private static final String propertiesFileName = "shared-context-test.properties";

	/**
	 * Constructor populates itself from properties file found in same package.
	 * 
	 * @throws Exception
	 */
	public SharedContextTestProperties() throws IOException {
		InputStream inStream = getClass().getResourceAsStream(propertiesFileName);
		if (inStream == null)
			throw new IOException("Failed to find file on classpath: " + propertiesFileName);
		super.load(inStream);
		inStream.close();
	}

	public int getProperty(final String name, final int defVal) throws NumberFormatException {
		String prop = getProperty(name);
		if (prop == null)
			return defVal;
		return Integer.parseInt(prop);
	}
	
	public boolean getProperty(final String name, final boolean defVal) {
		String prop = getProperty(name);
		if (prop == null)
			return false;
		return Boolean.parseBoolean(prop);
	}
	
	// Test this class
	public static void main(String[] args) throws Exception {
		SharedContextTestProperties p = new SharedContextTestProperties();
		System.out.println("Property " + SharedContextTestProperties.HOSTNAME + " = "
				+ p.getProperty(SharedContextTestProperties.HOSTNAME));
	}
}
