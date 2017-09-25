package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.externalsystemapproval.model.ExternalSystemRoleApproval;

public class ExternalSystemRoleApprovalTest {

	public ExternalSystemRoleApproval mockExternalSystemRoleApproval(){
		ExternalSystemRoleApproval externalSystemRoleApproval = new ExternalSystemRoleApproval();
		externalSystemRoleApproval.setRoleName("test");
		
		return externalSystemRoleApproval;		
	}
	
	@Test
	public void externalSystemRoleApprovalTest(){
		ExternalSystemRoleApproval externalSystemRoleApproval = mockExternalSystemRoleApproval();
		
		assertEquals(externalSystemRoleApproval.getRoleName(), "test");
	}
}
