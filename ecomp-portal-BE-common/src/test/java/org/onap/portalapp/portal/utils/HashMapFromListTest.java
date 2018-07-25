/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserAppRoles;

public class HashMapFromListTest {
	
	private static final String TEST="test";
	
	@Test
	public void testHashMap() {
		
		List<String> data=new ArrayList<>();
		data.add(TEST);
		
		HashMapFromList<String> map=new HashMapFromList<>();
		map.hashMap(data, TEST);
		
	}
	
	@Test
	public void testHashMapUser() {
		
		List<EPUser> data=new ArrayList<>();
		data.add(buildUser());
		
		HashMapFromList<EPUser> map=new HashMapFromList<>();
		map.hashMap(data, "firstName");
		
	}
	
	@Test
	public void testHashMapUserField() {
		
		List<EPUserAppRoles> data=new ArrayList<>();
		EPUserAppRoles role=new EPUserAppRoles();
		role.setAppId(2l);
		role.setRoleId(3l);
		data.add(role);
		
		HashMapFromList<EPUserAppRoles> map=new HashMapFromList<>();
		map.hashMap(data, "appId");
		
	}
	
	@Test
	public void testHashMapUserFieldException() {
		
		List<EPUserAppRoles> data=new ArrayList<>();
		EPUserAppRoles role=new EPUserAppRoles();
		role.setAppId(null);
		role.setRoleId(3l);
		data.add(role);
		
		HashMapFromList<EPUserAppRoles> map=new HashMapFromList<>();
		map.hashMap(data, "appId");
		
	}

	
	
	private EPUser buildUser() {
	EPUser epUser = new EPUser();
		
		epUser.setId((long)1);
		epUser.setManagerId((long) 1234);
		epUser.setFirstName(TEST);
		epUser.setLastName(TEST);
		epUser.setMiddleInitial(TEST);
		epUser.setPhone(TEST);
		epUser.setFax(TEST);
		epUser.setCellular(TEST);
		epUser.setEmail(TEST);
		epUser.setAddressId((long) 123); 
		epUser.setAlertMethodCd(TEST);
		epUser.setHrid(TEST);
		epUser.setOrgUserId(TEST);
		epUser.setOrgCode(TEST);
		epUser.setAddress1(TEST);
		epUser.setAddress2(TEST);
		epUser.setCity(TEST);
		epUser.setState(TEST);
		epUser.setZipCode(TEST);
		epUser.setCountry(TEST);
		epUser.setOrgManagerUserId(TEST);
		epUser.setLocationClli(TEST);
		epUser.setBusinessCountryCode(TEST);
		epUser.setBusinessCountryName(TEST);
		epUser.setBusinessUnit(TEST);
		epUser.setBusinessUnitName(TEST);
		epUser.setDepartment(TEST);
		epUser.setDepartmentName(TEST);
		epUser.setCompanyCode(TEST);
		epUser.setCompany(TEST);
		epUser.setZipCodeSuffix(TEST);
		epUser.setJobTitle(TEST);
		epUser.setCommandChain(TEST);
		epUser.setSiloStatus(TEST);
		epUser.setCostCenter(TEST);
		epUser.setFinancialLocCode(TEST);
		epUser.setLoginId(TEST);
		epUser.setLoginPwd(TEST);
		epUser.setLastLoginDate(new Date());
		epUser.setActive(false);
		epUser.setInternal(false);
		epUser.setSelectedProfileId((long) 12345);
		epUser.setTimeZoneId((long) 12345);
		epUser.setOnline(false);
		epUser.setChatId(TEST);
		return epUser;
				   
	}
}
