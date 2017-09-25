package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.GetAccessResultId;

public class GetAccessResultIdTest {
	
	public GetAccessResultId mockGetAccessResultId(){
		
		GetAccessResultId getAccessResultIdObj = new GetAccessResultId();
		getAccessResultIdObj.setEcompFunction("test");
		getAccessResultIdObj.setAppName("test");
		getAccessResultIdObj.setRoleName("test");
		
		return getAccessResultIdObj;
	}

	@Test
	public void getAccessResultIdObjTest(){
		GetAccessResultId getAccessResultIdObj = mockGetAccessResultId();
		
		assertEquals(getAccessResultIdObj.getEcompFunction(), "test");
		assertEquals(getAccessResultIdObj.getAppName(), "test");
		assertEquals(getAccessResultIdObj.getRoleName(), "test");
		
	}
}
