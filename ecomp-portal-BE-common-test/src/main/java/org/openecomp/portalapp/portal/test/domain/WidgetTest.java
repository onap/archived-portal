package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.Widget;

public class WidgetTest {

	public Widget mockWidget(){
		
		Widget widget = new Widget();
		widget.setName("test");
		widget.setWidth(100);
		widget.setHeight(100);
		widget.setUrl("test");
		widget.setAppId((long)1);
				
		return widget;
	}
	 
	@Test
	public void widgetTest(){
		Widget widget = mockWidget();
		
		assertEquals(widget.getName(), "test");
		assertTrue(widget.getWidth() == 100);
		assertTrue(widget.getHeight() == 100);
		assertEquals(widget.getUrl(), "test");
		assertEquals(widget.getAppId(), new Long(1)); 
		
		
	}
}
