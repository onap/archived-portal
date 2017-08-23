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
