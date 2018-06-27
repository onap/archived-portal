package org.onap.portalapp.portal.controller;
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
 * 
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.core.command.assertion.AssertEquals;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.SharedContextRestClient;
import org.onap.portalapp.portal.controller.SharedContextTestProperties;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.SharedContext;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests the endpoints exposed by the Shared Context controller in Portal.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SharedContext.class,EPCommonSystemProperties.class})

public class SharedContextRestControllerTest {
	
	@Mock
	SharedContextService contextService;

	@InjectMocks
	SharedContextRestController sharedContextRestController=new SharedContextRestController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	
	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	private final Log logger = LogFactory.getLog(getClass());

	//private final SharedContextTestProperties properties;

	private final String ckey = "ckey";
	private final String cvalue = "cvalue";
	
	// Supposed to be a Portal session ID
	private final String cxid = UUID.randomUUID().toString();

	private final String key = "key123";
	private final String value1 = "first value";
	private final String value2 = "second value";

	/*public SharedContextRestControllerTest() throws IOException {
		properties = new SharedContextTestProperties();
	}*/

	@SuppressWarnings("unchecked")
	//@Test
	/*public void test() throws Exception {
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
	}*/
	
	@Test
	public void getContextTest() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(), Matchers.any())).thenReturn(sharedContext);
		String result = sharedContextRestController.getContext(mockedRequest, "12","test");
		assertNotNull(result);
	}
	
	@Test
	public void getContextTestWithContextNull() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(), Matchers.any())).thenReturn(null);
		String result = sharedContextRestController.getContext(mockedRequest, "12","test");
		assertNotNull(result);
	}
	
	@Test(expected=Exception.class)
	public void getContextTestWithException() throws Exception{
		sharedContextRestController.getContext(mockedRequest, null,null);
	}
	
	@Test(expected=Exception.class)
	public void getUserContextTest() throws Exception{
		sharedContextRestController.getUserContext(mockedRequest, null);
	}
	
	@Test
	public void getUserContextTestWithContext() throws Exception{
		PowerMockito.mock(SharedContext.class);
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		List<SharedContext> listSharedContext = new ArrayList<SharedContext>();
		listSharedContext.add(sharedContext);
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);
		String response=sharedContextRestController.getUserContext(mockedRequest, "12");
		assertNotNull(response);
	}
	
	@Test
	public void checkContextTest() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);
		String response=sharedContextRestController.checkContext(mockedRequest, "12","test");
		assertNotNull(response);
	}
	
	@Test(expected=Exception.class)
	public void checkContextTestWithContextIdNull() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);
		sharedContextRestController.checkContext(mockedRequest, null,null);
	}
	
	@Test
	public void removeContextTest() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);

		//Mockito.when(contextService.deleteSharedContext(sharedContext));
		String actual=sharedContextRestController.removeContext(mockedRequest, "12","test");
		assertNotNull(actual);

	}
	
	@Test(expected=Exception.class)
	public void removeContextTestWithContextNull() throws Exception{
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);

		//Mockito.when(contextService.deleteSharedContext(sharedContext));
		String actual=sharedContextRestController.removeContext(mockedRequest, null,null);
		assertNotNull(actual);

	}
	
	@Test(expected=Exception.class)
	public void clearContextTestwithContextIdNull() throws Exception{
		
		Mockito.when(contextService.deleteSharedContexts(Matchers.any())).thenReturn(12);

		String actual=sharedContextRestController.clearContext(mockedRequest,null);
		assertNotNull(actual);

	}
	
	@Test
	public void clearContextTest() throws Exception{
		
		Mockito.when(contextService.deleteSharedContexts(Matchers.any())).thenReturn(12);

		String actual=sharedContextRestController.clearContext(mockedRequest,"12");
		assertNotNull(actual);

	}
	
	@Test
	public void setContextTest() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> userData = new HashMap<String, Object>();
		userData.put("context_id", "test_contextId");
		userData.put("ckey", "test_ckey");
		userData.put("cvalue", "test_cvalue");
		//String testUserJson=Matchers.anyString();
		JSONObject testUserJson = new JSONObject();
		testUserJson.put("context_id", "test1ContextId");
		testUserJson.put("ckey", "testCkey");
		testUserJson.put("cvalue", "testCValue");
		Map<String, Object> userData1 = mapper.readValue(testUserJson.toString(), Map.class);
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(sharedContext);
		// Mockito.when(mapper.readValue("true", Map.class)).thenReturn(userData);
		String actual=sharedContextRestController.setContext(mockedRequest,testUserJson.toString());

	}
	
	@Test(expected=Exception.class)
	public void setContextTestWithContextNull() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> userData = new HashMap<String, Object>();
		userData.put("context_id", "test_contextId");
		userData.put("ckey", "test_ckey");
		userData.put("cvalue", "test_cvalue");
		//String testUserJson=Matchers.anyString();
		JSONObject testUserJson = new JSONObject();
		testUserJson.put("context_id", "test1ContextId");
		testUserJson.put("ckey", "testCkey");
		testUserJson.put("cvalue", "testCValue");
		Map<String, Object> userData1 = mapper.readValue(testUserJson.toString(), Map.class);
		SharedContext sharedContext=new SharedContext();
		sharedContext.setContext_id("test_contextid");
		sharedContext.setCkey("test_ckey");
		Mockito.when(contextService.getSharedContext(Matchers.any(),Matchers.any())).thenReturn(null);
		Mockito.when(userData1.get(ckey)).thenReturn(null);
		Mockito.when(userData1.get(cxid)).thenReturn(null);

		// Mockito.when(mapper.readValue("true", Map.class)).thenReturn(userData);
		String actual=sharedContextRestController.setContext(mockedRequest,testUserJson.toString());

	}
}
