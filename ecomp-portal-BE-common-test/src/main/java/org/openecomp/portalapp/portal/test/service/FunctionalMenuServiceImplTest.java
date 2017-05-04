/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openecomp.portalapp.portal.service.FunctionalMenuService;
import org.openecomp.portalapp.portal.test.framework.ApplicationCommonContextTestSuite;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRole;
import org.openecomp.portalapp.portal.transport.FunctionalMenuRole;
import org.springframework.beans.factory.annotation.Autowired;




public class FunctionalMenuServiceImplTest extends ApplicationCommonContextTestSuite{

	
	 @Autowired
	 FunctionalMenuService functionalMenuService;
		
	
	
	  @Test
	  public void getAppListTestService() throws Exception {
				
		String userId ="guest";
		List<BusinessCardApplicationRole> userAppRolesActualResult = null;
		
		List<BusinessCardApplicationRole> userAppRolesExpectedResult = new ArrayList<BusinessCardApplicationRole>();
		BusinessCardApplicationRole businessCardApplicationRole= new BusinessCardApplicationRole();
		businessCardApplicationRole.setAppName("ECOMP Portal");
		businessCardApplicationRole.setRoleName("System Administrator");	
		userAppRolesExpectedResult.add(businessCardApplicationRole);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		userAppRolesActualResult =functionalMenuService.getUserAppRolesList(userId);
		assertTrue(userAppRolesActualResult.contains(businessCardApplicationRole));
	
  
	 }
	

	  @Test
	  public void getFunctionalMenuRoleTest() throws Exception {
		
		FunctionalMenuRole expectedFunctionalMenuRole = new FunctionalMenuRole();
		
		expectedFunctionalMenuRole.setId(new Integer(99999999)) ;
		expectedFunctionalMenuRole.setMenuId((long) 137);
		expectedFunctionalMenuRole.setAppId(new Integer(456));
		expectedFunctionalMenuRole.setRoleId(new Integer(6214));
		List<FunctionalMenuRole> actualFunctionalMenuRoleList = null;
		actualFunctionalMenuRoleList =  functionalMenuService.getFunctionalMenuRole();
		assertEquals(expectedFunctionalMenuRole.getAppId(),actualFunctionalMenuRoleList.get(actualFunctionalMenuRoleList.size()-1).getAppId());
		assertEquals(expectedFunctionalMenuRole.getMenuId(),actualFunctionalMenuRoleList.get(actualFunctionalMenuRoleList.size()-1).getMenuId());
		assertEquals(expectedFunctionalMenuRole.getId(),actualFunctionalMenuRoleList.get(actualFunctionalMenuRoleList.size()-1).getId());
		assertEquals(expectedFunctionalMenuRole.getRoleId(),actualFunctionalMenuRoleList.get(actualFunctionalMenuRoleList.size()-1).getRoleId());
		
	}
	
}


