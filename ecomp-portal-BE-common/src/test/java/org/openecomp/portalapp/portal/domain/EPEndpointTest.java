package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPEndpoint;

public class EPEndpointTest {

	public EPEndpoint mockEPEndpoint(){
		EPEndpoint epEndpoint = new EPEndpoint();
		
		epEndpoint.setId((long)1);
		epEndpoint.setName("test");
		
		return epEndpoint;
	}
	
	@Test
	public void epEndpointTest(){
		EPEndpoint epEndpoint = mockEPEndpoint();
		
		assertEquals(epEndpoint.getId(), new Long(1));
		assertEquals(epEndpoint.getName(), "test");
		assertEquals("EPEndpoint [id=1, name=test]", epEndpoint.toString());
	}
}
