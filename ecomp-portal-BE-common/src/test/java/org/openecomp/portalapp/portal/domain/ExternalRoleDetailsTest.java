package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPAppRoleFunction;
import org.openecomp.portalapp.portal.domain.ExternalRoleDetails;

public class ExternalRoleDetailsTest {

	public ExternalRoleDetails mockExternalRoleDetails(){
		
		List<EPAppRoleFunction> epAppRoleFunctionList = new ArrayList<EPAppRoleFunction>();
		
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setRoleId((long)1);
		epAppRoleFunction.setAppId((long)1);
		epAppRoleFunction.setCode("test");
		
		epAppRoleFunctionList.add(epAppRoleFunction);
		
		ExternalRoleDetails externalRoleDetails = new ExternalRoleDetails();
		
		externalRoleDetails.setName("test");
		externalRoleDetails.setActive(false);
		externalRoleDetails.setPriority(1);
		externalRoleDetails.setAppId((long)1);
		externalRoleDetails.setAppRoleId((long)1);
		externalRoleDetails.setPerms(epAppRoleFunctionList);
		
		return externalRoleDetails;
	}
	
	@Test
	public void externalRoleDetailsTest(){
		
		List<EPAppRoleFunction> epAppRoleFunctionList = new ArrayList<EPAppRoleFunction>();
		
		EPAppRoleFunction epAppRoleFunction = new EPAppRoleFunction();
		epAppRoleFunction.setRoleId((long)1);
		epAppRoleFunction.setAppId((long)1);
		epAppRoleFunction.setCode("test");
		
		epAppRoleFunctionList.add(epAppRoleFunction);
		
		ExternalRoleDetails externalRoleDetails = mockExternalRoleDetails();
		
		assertEquals(externalRoleDetails.getAppId(), new Long(1));
		assertEquals(externalRoleDetails.getAppRoleId(), new Long(1));
		assertEquals(externalRoleDetails.getPriority().toString(), "1");
		assertEquals(externalRoleDetails.getName(), "test");
		assertEquals(externalRoleDetails.getPriority().toString(),  "1");
		assertEquals(externalRoleDetails.getPerms().size(), epAppRoleFunctionList.size());
		
	}
}
