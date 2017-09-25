package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPEndpointAccount;

public class EPEndpointAccountTest {

	public EPEndpointAccount mockEPEndpointAccount(){
		EPEndpointAccount epEndpointAccount = new EPEndpointAccount();
		
		epEndpointAccount.setEp_id((long)1);
		epEndpointAccount.setAccount_id((long)1);
		epEndpointAccount.setId((long)1);
		
		return epEndpointAccount;
	}
	
	@Test
	public void epEndpointAccount(){
		EPEndpointAccount epEndpointAccount = mockEPEndpointAccount();
		assertEquals(epEndpointAccount.getEp_id(), new Long(1));
		assertEquals(epEndpointAccount.getAccount_id(), new Long(1));
		assertEquals(epEndpointAccount.getId(), new Long(1));
		
		assertEquals(epEndpointAccount.toString(), "EPEndpointAccount [id=1, ep_id=1, account_id=1]");
	}
}
