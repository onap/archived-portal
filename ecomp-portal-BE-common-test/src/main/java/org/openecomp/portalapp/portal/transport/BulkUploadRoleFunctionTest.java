package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.BulkUploadRoleFunction;

public class BulkUploadRoleFunctionTest {

	public BulkUploadRoleFunction mockBulkUploadRoleFunction(){
		
		BulkUploadRoleFunction bulkUploadRoleFunction = new BulkUploadRoleFunction();
						
		bulkUploadRoleFunction.setFunctionName("test");
		bulkUploadRoleFunction.setFunctionCd("test");
		
		return bulkUploadRoleFunction;
	}
	
	@Test
	public void bulkUploadRoleFunctionTest(){
		BulkUploadRoleFunction bulkUploadRoleFunction = mockBulkUploadRoleFunction();
		
		BulkUploadRoleFunction bulkUploadRoleFunction1 = new BulkUploadRoleFunction();
		
		bulkUploadRoleFunction1.setFunctionName("test");
		bulkUploadRoleFunction1.setFunctionCd("test");
		
		assertEquals(bulkUploadRoleFunction.getFunctionCd(), "test");
		assertEquals(bulkUploadRoleFunction.getFunctionName(), "test");
		assertEquals(bulkUploadRoleFunction.hashCode(), bulkUploadRoleFunction1.hashCode());
		assertTrue(bulkUploadRoleFunction.equals(bulkUploadRoleFunction1));
		
	}
}
