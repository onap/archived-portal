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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPException;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/*import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;*/
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.service.ApplicationsRestClientServiceImpl;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.util.SystemType;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.apache.cxf.jaxrs.impl.ResponseImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebClient.class,Object.class,CipherUtil.class})
public class ApplicationsRestClientServiceImplTest {

	@Mock
	private AppsCacheService appsCacheService;
	
	@InjectMocks
	private  ApplicationsRestClientServiceImpl serviceImpl= new ApplicationsRestClientServiceImpl();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void unt_get_failure() throws HTTPException{
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.get(ApplicationsRestClientServiceImpl.class, 12L,"/path" );
		Assert.assertNull(appservice);
		
	}
	
	@Test(expected=ClassCastException.class)
	public void unt_get_successwithException() throws HTTPException{
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.get()).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.get(ApplicationsRestClientServiceImpl.class, 12L,"/path" );
		Assert.assertNull(appservice);
		
	}
	
	@Test(expected=ClassCastException.class)
	public void unt_get_successwithException1() throws HTTPException{
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.get()).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(1);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.get(ApplicationsRestClientServiceImpl.class, 12L,"/path" );
		Assert.assertNull(appservice);
		
	}
	
	@Test
	public void unt_post_failure() throws HTTPException{
		PowerMockito.mockStatic(Object.class);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.post(ApplicationsRestClientServiceImpl.class, 12L, Matchers.any() , "/path",Matchers.any());
		Assert.assertNull(appservice);
		
	}
	
	@Test
	public void unt_post_successwithException() throws HTTPException, CipherUtilException{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
			PowerMockito.when(CipherUtil.decryptPKC(Matchers.anyString())).thenReturn("password1234");
			/*Map<String, String> payload = new HashMap<String, String>();
			payload.put("payload1", Long.toString(1));*/
			List payload=new ArrayList<>();
				payload.add("test");
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.get()).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.post(ApplicationsRestClientServiceImpl.class, 12L,payload,"/path",SystemType.APPLICATION);
		Assert.assertNull(appservice);
		
	}
	
	
	@Test(expected=ClassCastException.class)
	public void unt_post_successwithException2() throws HTTPException, CipherUtilException{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
			PowerMockito.when(CipherUtil.decryptPKC(Matchers.anyString())).thenReturn("password1234");
			/*Map<String, String> payload = new HashMap<String, String>();
			payload.put("payload1", Long.toString(1));*/
			List payload=new ArrayList<>();
				payload.add("test");
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.post(Matchers.any())).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.post(ApplicationsRestClientServiceImpl.class, 12L,payload,"/path",SystemType.APPLICATION);
		Assert.assertNull(appservice);
		
	}
	

	@Test(expected=ClassCastException.class)
	public void unt_post_successwithException3() throws HTTPException, CipherUtilException{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
			PowerMockito.when(CipherUtil.decryptPKC(Matchers.anyString())).thenReturn("password1234");
			/*Map<String, String> payload = new HashMap<String, String>();
			payload.put("payload1", Long.toString(1));*/
			List payload=new ArrayList<>();
				payload.add("test");
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.post(Matchers.any())).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(1);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.post(ApplicationsRestClientServiceImpl.class, 12L,payload,"/path",SystemType.APPLICATION);
		Assert.assertNull(appservice);
		
	}
	
	
	@Test(expected=ClassCastException.class)
	public void unt_get_successwithException3() throws HTTPException{
		PowerMockito.mockStatic(WebClient.class);
		WebClient client=Mockito.mock(WebClient.class);
		 PowerMockito.when(WebClient.create(Matchers.anyString())).thenReturn(client);
		 Response response=Mockito.mock(Response.class);
		Mockito.when(client.get()).thenReturn(response);
		Mockito.when(client.type(MediaType.APPLICATION_JSON)).thenReturn(client);
		Mockito.when(response.getStatus()).thenReturn(1);
		EPApp appTest=new EPApp();
		appTest.setAppRestEndpoint("https");
		appTest.setAppBasicAuthPassword("testPassword");
		Mockito.when(appsCacheService.getApp(Matchers.anyLong())).thenReturn(appTest);
		ApplicationsRestClientServiceImpl appservice=serviceImpl.get(ApplicationsRestClientServiceImpl.class, 12L,"/path" , Matchers.anyBoolean());
		Assert.assertNull(appservice);
		
	}
	

}
