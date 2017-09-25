package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.WidgetCatalogParameter;

public class WidgetCatalogParameterTest {

	public WidgetCatalogParameter mockWidgetCatalogParameter(){
		WidgetCatalogParameter widgetCatalogParameter = new WidgetCatalogParameter();
		
		widgetCatalogParameter.setId((long)1);
		widgetCatalogParameter.setWidgetId((long)1);
		widgetCatalogParameter.setUserId((long)1);
		widgetCatalogParameter.setParamId((long)1);
		widgetCatalogParameter.setUser_value("test");
		
		return widgetCatalogParameter;
	}
	
	@Test
	public void widgetCatalogParameterTest(){
		WidgetCatalogParameter widgetCatalogParameter = mockWidgetCatalogParameter();
		
		assertEquals(widgetCatalogParameter.getId(), new Long(1));
		assertEquals(widgetCatalogParameter.getWidgetId(), new Long(1));
		assertEquals(widgetCatalogParameter.getUserId(), new Long(1));
		assertEquals(widgetCatalogParameter.getParamId(), new Long(1));
		assertEquals(widgetCatalogParameter.getUser_value(), "test");
		assertEquals(widgetCatalogParameter.toString(), "WidgetCatalogParameter [id=1, widgetId=1, userId=1, paramId=1, user_value=test]");
	}
}
