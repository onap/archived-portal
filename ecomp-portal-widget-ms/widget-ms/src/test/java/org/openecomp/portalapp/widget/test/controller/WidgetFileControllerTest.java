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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.openecomp.portalapp.widget.controller.DatabaseFileUploadController;
import org.openecomp.portalapp.widget.service.impl.StorageServiceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class WidgetFileControllerTest {
	private MockMvc mockMvc;
	
	@Mock
	private StorageServiceImpl storageService;
	
	@InjectMocks
	private DatabaseFileUploadController controller;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void getWidgetMarkup_NoError() throws Exception{
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		Long widgetId = new Long(1);
		mockMvc.perform(MockMvcRequestBuilders.get("/microservices/markup/" + widgetId)).andReturn();;
		Mockito.verify(storageService, times(1)).getWidgetMarkup(storageServiceArg.capture());
		assertEquals(storageServiceArg.getValue(), widgetId);
	}
	
	@Test
	public void getWidgetController_NoError() throws Exception{
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		Long widgetId = new Long(1);
		mockMvc.perform(MockMvcRequestBuilders.get("/microservices/" + widgetId + "/controller.js")).andReturn();;
		Mockito.verify(storageService, times(1)).getWidgetController(storageServiceArg.capture());
		assertEquals(storageServiceArg.getValue(), widgetId);
	}
	
	@Test
	public void getWidgetFramework_NoError() throws Exception{
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		Long widgetId = new Long(1);
		mockMvc.perform(MockMvcRequestBuilders.get("/microservices/" + widgetId + "/framework.js")).andReturn();;
		Mockito.verify(storageService, times(1)).getWidgetFramework(storageServiceArg.capture());
		assertEquals(storageServiceArg.getValue(), widgetId);
	}
	
	@Test
	public void getWidgetCSS_NoError() throws Exception{
		ArgumentCaptor<Long> storageServiceArg = ArgumentCaptor.forClass(Long.class);
		Long widgetId = new Long(1);
		mockMvc.perform(MockMvcRequestBuilders.get("/microservices/" + widgetId + "/styles.css")).andReturn();;
		Mockito.verify(storageService, times(1)).getWidgetCSS(storageServiceArg.capture());
		assertEquals(storageServiceArg.getValue(), widgetId);
	}
	
}
