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
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests the endpoints exposed by the Shared Context REST controller in Portal
 * Core.
 * 
 * @author clott
 */
public class SharedContextRestControllerTest {

	private final Log logger = LogFactory.getLog(getClass());

	private final SharedContextTestProperties properties;

	private final String ckey = "ckey";
	private final String cvalue = "cvalue";
	
	// Supposed to be a Portal session ID
	private final String cxid = UUID.randomUUID().toString();

	private final String key = "key123";
	private final String value1 = "first value";
	private final String value2 = "second value";

	public SharedContextRestControllerTest() throws IOException {
		properties = new SharedContextTestProperties();
	}

	@SuppressWarnings("unchecked")
	//@Test
	public void test() throws Exception {
		String response = null, val = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseMap, jsonMap;

		logger.info("Get on empty context");
		response = SharedContextRestClient.getJson(properties, "get", cxid, key);
		// Should not exist - just generated the UUID
		Map<String, Object> responseMap1 = mapper.readValue(response, Map.class);
		response = (String) responseMap1.get("response");
		Assert.assertNull(response);

		logger.info("Set a new context");
		response = setContext(cxid, key, value1);
		Assert.assertNotNull(response);
		responseMap = mapper.readValue(response, Map.class);
		String responseValue = (String) responseMap.get("response");
		Assert.assertNotNull(responseValue);
		Assert.assertEquals("added", responseValue);

		logger.info("Get existing context");
		response = SharedContextRestClient.getJson(properties, "get", cxid, key);
		responseMap = mapper.readValue(response, Map.class);
		jsonMap = (Map<String,Object>) responseMap.get("response");
		Assert.assertNotNull(jsonMap);
		val = (String) jsonMap.get(cvalue);
		Assert.assertEquals(val, value1);

		logger.info("Overwrite exiting context");
		response = setContext(cxid, key, value2);
		Assert.assertNotNull(response);
		responseMap = mapper.readValue(response, Map.class);
		response = (String) responseMap.get("response");
		Assert.assertNotNull(responseValue);
		// Assert.assertEquals("replaced", responseValue);

		logger.info("Get existing context to verify overwrite");
		response = SharedContextRestClient.getJson(properties, "get", cxid, key);
		responseMap = mapper.readValue(response, Map.class);
		jsonMap = (Map<String,Object>) responseMap.get("response");
		Assert.assertNotNull(jsonMap);
		val = (String) jsonMap.get(cvalue);
		Assert.assertEquals(val, value2);

		logger.info("Delete one context");
		response = SharedContextRestClient.getJson(properties, "remove", cxid, key);
		responseMap = mapper.readValue(response, Map.class);
		response = (String) responseMap.get("response");
		Assert.assertEquals(response, "removed");

		logger.info("Clear the context");
		response = SharedContextRestClient.getJson(properties, "clear", cxid, null);
		Assert.assertEquals("", response);
	}

	private String setContext(String context, String id, String value) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String,String> stringMap = new HashMap<String,String>();
		stringMap.put("context_id", cxid);
		stringMap.put(ckey, key);
		stringMap.put(cvalue, value2);
		String json = mapper.writeValueAsString(stringMap);
		String response = SharedContextRestClient.postJson(properties, "set", json);
		return response;
	}
}
