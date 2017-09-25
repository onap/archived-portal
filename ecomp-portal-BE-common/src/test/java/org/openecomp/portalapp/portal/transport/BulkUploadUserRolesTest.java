package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.BulkUploadUserRoles;

public class BulkUploadUserRolesTest {

	public BulkUploadUserRoles mockBulkUploadUserRoles(){
		BulkUploadUserRoles bulkUploadUserRoles = new BulkUploadUserRoles();
				
		bulkUploadUserRoles.setRoleName("test");
		bulkUploadUserRoles.setOrgUserId("test");
		bulkUploadUserRoles.setAppNameSpace("test");
		
		return bulkUploadUserRoles;
	}
	
	@Test
	public void bulkUploadUserRolesTest(){
		BulkUploadUserRoles bulkUploadUserRoles = mockBulkUploadUserRoles();
		
		BulkUploadUserRoles bulkUploadUserRoles1 = new BulkUploadUserRoles();
		
		bulkUploadUserRoles1.setRoleName("test");
		bulkUploadUserRoles1.setOrgUserId("test");
		bulkUploadUserRoles1.setAppNameSpace("test");
		
		assertEquals(bulkUploadUserRoles.getRoleName(), "test");
		assertEquals(bulkUploadUserRoles.getOrgUserId(), "test");
		assertEquals(bulkUploadUserRoles.getAppNameSpace(), "test");
		
		assertEquals(bulkUploadUserRoles.hashCode(), bulkUploadUserRoles1.hashCode());
		assertTrue(bulkUploadUserRoles.equals(bulkUploadUserRoles1));
	}
}
