package org.onap.portalapp.widget.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.onap.portalapp.widget.excetpion.StorageFileNotFoundException;

public class StorageFileNotFoundExceptionTest {

	@Test
	public void Test1()
	{
		 String s1 = "Value1";
	        String s2 = "value2";
	        try {
	            if (!s1.equalsIgnoreCase(s2)) {
	                throw new StorageFileNotFoundException("org.onap.portalapp.widget.excetpion.StorageFileNotFoundException");
	            }
	        } catch (StorageFileNotFoundException mde) {
	            assertEquals(mde.getMessage(),"org.onap.portalapp.widget.excetpion.StorageFileNotFoundException");
	        }
	}
	
	@Test
	public void Test2()
	{
		 String message = "Exception occured";
		 String s1 = "Value1";
	     String s2 = "value2";
	        try {
	            if (!s1.equalsIgnoreCase(s2)) {
	                throw new StorageFileNotFoundException(message, new Throwable());
	            }
	        } catch (StorageFileNotFoundException mde) {
	            assertEquals("org.onap.portalapp.widget.excetpion.StorageFileNotFoundException", mde.getClass().getName());
	        }
	}
	
}
