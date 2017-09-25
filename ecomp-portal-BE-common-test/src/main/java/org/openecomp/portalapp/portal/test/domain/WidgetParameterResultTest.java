package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.WidgetParameterResult;

public class WidgetParameterResultTest {

	public WidgetParameterResult mockWidgetParameterResult(){
				
		WidgetParameterResult widgetParameterResult = new WidgetParameterResult();
		widgetParameterResult.setParam_id((long)1);
		widgetParameterResult.setParam_key("test");
		widgetParameterResult.setUser_value("test");
		widgetParameterResult.setDefault_value("test");
		
		return widgetParameterResult;
	}
	
	@Test
	public void widgetParameterResultTest(){
		
		WidgetParameterResult widgetParameterResult = mockWidgetParameterResult();
		
		assertEquals(widgetParameterResult.getParam_key(), "test");
		assertEquals(widgetParameterResult.getParam_id(), new Long(1));
		assertEquals(widgetParameterResult.getUser_value(), "test");
		assertEquals(widgetParameterResult.getDefault_value(), "test");
		
		assertEquals("WidgetParameterResult [param_id=1, param_key=test, user_value=test, default_value=test]", widgetParameterResult.toString());
	}
}
