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
package org.openecomp.portalapp.widget.test.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.openecomp.portalapp.widget.controller.WidgetsCatalogController;
import org.openecomp.portalapp.widget.domain.ValidationRespond;
import org.openecomp.portalapp.widget.domain.WidgetCatalog;
import org.openecomp.portalapp.widget.service.StorageService;
import org.openecomp.portalapp.widget.service.WidgetCatalogService;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class WidgetsCatalogControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private WidgetCatalogService widgetService;
	
	@Mock
	private StorageService storageService;
	
	@InjectMocks
	private WidgetsCatalogController controller;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void getWidgetCatalog_ValidAuthorization_NoError() throws Exception {	
		List<WidgetCatalog> list = new ArrayList<WidgetCatalog>();
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");
		list.add(widget);
		Mockito.when(widgetService.getWidgetCatalog()).thenReturn(list);
		
		String security_user = "user";
		String security_pass = "password";
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + security_pass).getBytes()));
		mockMvc.perform(get("/microservices/widgetCatalog/").header("Authorization", basic_auth))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[0].name", is("junit")));
	}
	  
	@Test
	public void getWidgetCatalog_InValidAuthorization_Unauthorized() throws Exception {	

		String security_user = "user";
		String security_pass = "password";
		String wrong_pass = "wrong";
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + wrong_pass).getBytes()));
		mockMvc.perform(get("/microservices/widgetCatalog/").header("Authorization", basic_auth))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void getWidgetCatalog_NoAuthorization_BadRequest() throws Exception {	
		List<WidgetCatalog> list = new ArrayList<WidgetCatalog>();
		WidgetCatalog widget = new WidgetCatalog();
		list.add(widget);
		Mockito.when(widgetService.getWidgetCatalog()).thenReturn(list);
		
		mockMvc.perform(get("/microservices/widgetCatalog/"))
		.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void saveWidgetCatalog_ValidAuthorization_NoError() throws Exception {	
		ValidationRespond respond = new ValidationRespond(true, null);
		Mockito.when(storageService.checkZipFile(any(MultipartFile.class))).thenReturn(respond);
		
		String security_user = "user";
		String security_pass = "password";
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + security_pass).getBytes()));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/microservices/widgetCatalog/").file("file", null)
				.param("widget", "{}")
				.header("Authorization", basic_auth)
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(jsonPath("$.valid", is(true)));
		
		Mockito.verify(widgetService, times(1)).saveWidgetCatalog(any(WidgetCatalog.class));
	}
	
	
	@Test
	public void updateWidgetCatalog_ValidAuthorization_NoError() throws Exception {	
		String security_user = "user"; 
		String security_pass = "password";
		Long widgetId = new Long(1);
		ArgumentCaptor<Long> widgetServiceArg = ArgumentCaptor.forClass(Long.class);
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + security_pass).getBytes()));
		mockMvc.perform(put("/microservices/widgetCatalog/" + widgetId).contentType(MediaType.APPLICATION_JSON).content("{}").header("Authorization", basic_auth));
		
		Mockito.verify(widgetService, times(1)).updateWidgetCatalog(widgetServiceArg.capture(), any(WidgetCatalog.class));
		assertEquals(widgetServiceArg.getValue(), widgetId);
	}
	
	
	@Test
	public void updateWidgetCatalogwithFiles_ValidAuthorization_NoError() throws Exception {
		ValidationRespond respond = new ValidationRespond(true, null);
		Mockito.when(storageService.checkZipFile(any(MultipartFile.class))).thenReturn(respond);
		
		String security_user = "user";
		String security_pass = "password";
		Long widgetId = new Long(1);
		ArgumentCaptor<Long> widgetServiceArg = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + security_pass).getBytes()));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/microservices/widgetCatalog/" + widgetId).file("file", null)
				.param("widget", "{}")
				.header("Authorization", basic_auth)
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(jsonPath("$.valid", is(true)));
		
		Mockito.verify(widgetService, times(1)).updateWidgetCatalog(widgetServiceArg.capture(), any(WidgetCatalog.class));
		assertEquals(widgetServiceArg.getValue(), widgetId);
	}
	
	@Test
	public void deleteOnboardingWidget_ValidAuthorization_NoError() throws Exception {
		
		String security_user = "user";
		String security_pass = "password";
		Long widgetId = new Long(1);
		
		ReflectionTestUtils.setField(controller, "security_user", security_user, String.class);
		ReflectionTestUtils.setField(controller, "security_pass", security_pass, String.class);
		
		String basic_auth = "Basic " + new String(Base64.encodeBase64((security_user + ":" + security_pass).getBytes()));
		mockMvc.perform(MockMvcRequestBuilders.delete("/microservices/widgetCatalog/" + widgetId)
				.header("Authorization", basic_auth));
		ArgumentCaptor<Long> widgetServiceArg = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		
		Mockito.verify(widgetService, times(1)).deleteWidgetCatalog(widgetServiceArg.capture());
		assertEquals(widgetServiceArg.getValue(), widgetId);
		Mockito.verify(storageService, times(1)).deleteWidgetFile(storageServiceArg.capture());
		assertEquals(storageServiceArg.getValue(), widgetId);
	}
	
	
}
