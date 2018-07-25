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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CentralV2UserTest {
	
	private static final String TEST="test";
	private static final Long ID=1l;
	private static final Date DATE=new Date();
	
	
	@Test
	public void testCentralUser() {
		
		
		CentralV2User user=buildV2User();
		user.setZipCodeSuffix(TEST);
		CentralV2User centrlUser=new CentralV2User();
		centrlUser.setId(user.getId());
		centrlUser.setCreated(user.getCreated());
		centrlUser.setModified(user.getModified());
		centrlUser.setCreatedId(user.getCreatedId());
		centrlUser.setModifiedId(user.getModifiedId());
		centrlUser.setRowNum(user.getRowNum());
		centrlUser.setOrgId(user.getOrgId());
		centrlUser.setManagerId(user.getManagerId());
		centrlUser.setFirstName(user.getFirstName());
		centrlUser.setMiddleInitial(user.getMiddleInitial());
		centrlUser.setLastName(user.getLastName());
		centrlUser.setPhone(user.getPhone());
		centrlUser.setFax(user.getFax());
		centrlUser.setCellular(user.getCellular());
		centrlUser.setEmail(user.getEmail());
		centrlUser.setAddressId(user.getAddressId());
		centrlUser.setAlertMethodCd(user.getAlertMethodCd());
		centrlUser.setHrid(user.getHrid());
		centrlUser.setOrgUserId(user.getOrgUserId());
		centrlUser.setOrgCode(user.getOrgCode());
		centrlUser.setAddress1(user.getAddress1());
		centrlUser.setAddress2(user.getAddress2());
		centrlUser.setCity(user.getCity());
		centrlUser.setState(user.getState());
		centrlUser.setZipCode(user.getZipCode());
		centrlUser.setCountry(user.getCountry());
	
		centrlUser.setOrgManagerUserId(user.getOrgManagerUserId());
		centrlUser.setLocationClli(user.getLocationClli());
		centrlUser.setBusinessCountryCode(user.getBusinessCountryCode());
		centrlUser.setBusinessCountryName(user.getBusinessCountryName());
		centrlUser.setBusinessUnit(user.getBusinessUnit());
		centrlUser.setBusinessUnitName(user.getBusinessUnitName());
		centrlUser.setDepartment(user.getDepartment());
		centrlUser.setDepartmentName(user.getDepartmentName());
		centrlUser.setCompanyCode(user.getCompanyCode());
		centrlUser.setCompany(user.getCompany());
		centrlUser.setZipCode(user.getZipCode());
		centrlUser.setZipCodeSuffix(user.getZipCodeSuffix());
		centrlUser.setJobTitle(user.getJobTitle());
		centrlUser.setCommandChain(user.getCommandChain());
		centrlUser.setSiloStatus(user.getSiloStatus());
		centrlUser.setCostCenter(user.getCostCenter());
		centrlUser.setFinancialLocCode(user.getFinancialLocCode());
		centrlUser.setLoginId(user.getLoginId());
		centrlUser.setLoginPwd(user.getLoginPwd());
		centrlUser.setLastLoginDate(user.getLastLoginDate());
		centrlUser.setActive(user.isActive());
		centrlUser.setInternal(user.isInternal());
		centrlUser.setSelectedProfileId(user.getSelectedProfileId());
		centrlUser.setTimeZoneId(user.getTimeZoneId());
		centrlUser.setOnline(user.isOnline());
		centrlUser.setChatId(user.getChatId());
		centrlUser.setUserApps(user.getUserApps());
		centrlUser.setPseudoRoles(user.getPseudoRoles());
	
		assertEquals(user.hashCode(), centrlUser.hashCode());
		assertTrue(centrlUser.equals(centrlUser));
		assertFalse(user.equals(null));
		assertEquals(ID, user.getId());
		assertEquals(DATE, user.getCreated());
		assertEquals(DATE, user.getModified());
		
		centrlUser.setZipCodeSuffix(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setZipCode(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setUserApps(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setTimeZoneId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setState(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setSiloStatus(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setSelectedProfileId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setRowNum(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setPseudoRoles(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setPhone(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setOrgUserId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setOrgManagerUserId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setOrgId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setOrgCode(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setOnline(false);
		assertFalse(centrlUser.equals(user));
		centrlUser.setModifiedId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setModified(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setMiddleInitial(null);
		assertFalse(centrlUser.equals(user));
		
		centrlUser.setManagerId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setLoginPwd(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setLoginId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setLocationClli(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setLastName(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setLastLoginDate(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setJobTitle(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setInternal(false);
		assertFalse(centrlUser.equals(user));
		centrlUser.setId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setHrid(null);
		assertFalse(centrlUser.equals(user));
		
		centrlUser.setFirstName(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setFinancialLocCode(null);
		assertFalse(centrlUser.equals(user));
		
		centrlUser.setFax(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setEmail(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setDepartmentName(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setDepartment(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCreatedId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCreated(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCountry(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCostCenter(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCompanyCode(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCompany(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCommandChain(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCity(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setChatId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setCellular(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setBusinessUnitName(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setBusinessUnit(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setBusinessCountryName(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setBusinessCountryCode(null);
		assertFalse(centrlUser.equals(user));
		
		centrlUser.setBusinessCountryCode(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setAlertMethodCd(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setAddressId(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setAddress2(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setAddress1(null);
		assertFalse(centrlUser.equals(user));
		centrlUser.setActive(false);
		assertFalse(centrlUser.equals(user));
		
		centrlUser.hashCode();
		
		
	
		
		
	}
	
	public CentralV2User buildV2User(){
		Set<CentralV2UserApp> userApps = new HashSet<CentralV2UserApp>();
		Set<CentralV2Role> pseudoRoles = new HashSet<CentralV2Role>();
		CentralV2User centralV2User = new CentralV2User(ID, DATE, DATE, ID, ID, ID, ID,
				ID, TEST, TEST, TEST, TEST, TEST,
				TEST, TEST, ID, TEST, TEST, TEST,
				TEST, TEST, TEST, TEST, TEST, TEST, TEST,
				TEST, TEST, TEST, TEST,
				TEST, TEST, TEST, TEST, TEST,
				TEST, TEST, TEST, TEST, TEST,
				TEST, TEST, TEST, TEST, null,
				false, false, ID, ID, false, TEST, userApps, pseudoRoles);
		
		return centralV2User;
	}
	
	

}
