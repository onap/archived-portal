package org.openecomp.portalapp.test.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.exceptions.NoHealthyServiceException;

public class NoHealthyServiceExceptionTest {

	public NoHealthyServiceException mockNoHealthyServiceException(){
		NoHealthyServiceException noHealthyServiceException = new NoHealthyServiceException("test");
		
		return noHealthyServiceException;
	}
	
	@Test
	public void noHealthyServiceExceptionTest(){
		NoHealthyServiceException noHealthyServiceException = mockNoHealthyServiceException();
		
	//	assertEquals(noHealthyServiceException, new NoHealthyServiceException("test"));
		assertEquals(noHealthyServiceException.toString(), "NoHealthyServiceException [] test");
	}
}
