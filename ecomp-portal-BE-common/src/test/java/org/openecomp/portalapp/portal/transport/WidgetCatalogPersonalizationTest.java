package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.WidgetCatalogPersonalization;

public class WidgetCatalogPersonalizationTest {

	public WidgetCatalogPersonalization mockWidgetCatalogPersonalization(){
		WidgetCatalogPersonalization widgetCatalogPersonalization = new WidgetCatalogPersonalization();
		
		widgetCatalogPersonalization.setWidgetId((long)1);
		widgetCatalogPersonalization.setSelect(false);
		return widgetCatalogPersonalization;
	}
	
	@Test
	public void widgetCatalogPersonalizationTest(){
		WidgetCatalogPersonalization widgetCatalogPersonalization = mockWidgetCatalogPersonalization();
		
		assertEquals(widgetCatalogPersonalization.getWidgetId(), new Long(1));
		assertEquals(widgetCatalogPersonalization.getSelect(), false);
	}
}
