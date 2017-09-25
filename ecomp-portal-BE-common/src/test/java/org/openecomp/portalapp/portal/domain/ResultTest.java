package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.model.Result;

public class ResultTest {
	
	public Result mockResult(){
		
		Result result= new Result("test");
		
		result.setResult("test");
		return result;
	}

	@Test
	public void resultTest(){
		
		Result result = mockResult();		
		result.setResult("test");
		
		assertEquals(result.getResult(), "test");
		
	}
	
				
}
