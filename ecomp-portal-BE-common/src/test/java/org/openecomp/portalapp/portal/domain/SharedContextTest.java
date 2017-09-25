package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.SharedContext;

public class SharedContextTest {

	public SharedContext mockSharedContext(){
		
		SharedContext sharedContext = new SharedContext();
		sharedContext.setId((long)1);
		sharedContext.setCreate_time(new Date());
		sharedContext.setContext_id("test");
		sharedContext.setCkey("test");
		sharedContext.setCvalue("test");
		
		return sharedContext;
	}
	
	@Test
	public void sharedContextTest(){
		SharedContext sharedContext = mockSharedContext();
		
		assertEquals(sharedContext.getId(), new Long(1));
	//	assertEquals(sharedContext.getCreate_time(), new Date());
		assertEquals(sharedContext.getContext_id(), "test");
		assertEquals(sharedContext.getCkey(), "test");
		assertEquals(sharedContext.getCvalue(), "test");
		
		
	}
}
