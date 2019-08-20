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

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CentralUserTest {

    CentralUser centralUser = new CentralUser.CentralUserBuilder().setId(null).setCreated(null).setModified(null)
            .setCreatedId(null).setModifiedId(null).setRowNum(null).setOrgId(null).setManagerId(null).setFirstName(null)
            .setMiddleInitial(null).setLastName(null).setPhone(null).setFax(null).setCellular(null).setEmail(null)
            .setAddressId(null).setAlertMethodCd(null).setHrid(null).setOrgUserId(null).setOrgCode(null)
            .setAddress1(null).setAddress2(null).setCity(null).setState(null).setZipCode(null).setCountry(null)
            .setOrgManagerUserId(null).setLocationClli(null).setBusinessCountryCode(null).setBusinessCountryName(null)
            .setBusinessUnit(null).setBusinessUnitName(null).setDepartment(null).setDepartmentName(null)
            .setCompanyCode(null).setCompany(null).setZipCodeSuffix(null).setJobTitle(null).setCommandChain(null)
            .setSiloStatus(null).setCostCenter(null).setFinancialLocCode(null).setLoginId(null).setLoginPwd(null)
            .setLastLoginDate(null).setActive(false).setInternal(false).setSelectedProfileId(null).setTimeZoneId(null)
            .setOnline(false).setChatId(null).setUserApps(null).createCentralUser();

	public CentralV2User mockCentralUser(){
		Set<CentralV2UserApp> userApps = new HashSet<CentralV2UserApp>();
		Set<CentralV2Role> pseudoRoles = new HashSet<CentralV2Role>();
        CentralV2User centralV2User = new CentralV2User.CentralV2UserBuilder().setId((long) 1).setCreated(null)
                .setModified(null).setCreatedId((long) 1).setModifiedId((long) 1).setRowNum((long) 1).setOrgId((long) 1)
                .setManagerId((long) 1).setFirstName("test").setMiddleInitial("test").setLastName("test")
                .setPhone("test").setFax("test").setCellular("test").setEmail("test").setAddressId((long) 1)
                .setAlertMethodCd("test").setHrid("test").setOrgUserId("test").setOrgCode("test").setAddress1("test")
                .setAddress2("test").setCity("test").setState("test").setZipCode("test").setCountry("test")
                .setOrgManagerUserId("test").setLocationClli("test").setBusinessCountryCode("test")
                .setBusinessCountryName("test").setBusinessUnit("test").setBusinessUnitName("test")
                .setDepartment("test").setDepartmentName("test").setCompanyCode("test").setCompany("test")
                .setZipCodeSuffix("test").setJobTitle("test").setCommandChain("test").setSiloStatus("test")
                .setCostCenter("test").setFinancialLocCode("test").setLoginId("test").setLoginPwd("test")
                .setLastLoginDate(null).setActive(false).setInternal(false).setSelectedProfileId((long) 1)
                .setTimeZoneId((long) 1).setOnline(false).setChatId("test").setUserApps(userApps)
                .setPseudoRoles(pseudoRoles).createCentralV2User();
		
		return centralV2User;
	}
	
	@Test
	public void centralRoleTest(){
		CentralV2User centralV2User = mockCentralUser();
		
		Set<CentralV2UserApp> userApps = new HashSet<CentralV2UserApp>();
		Set<CentralV2Role> pseudoRoles = new HashSet<CentralV2Role>();
        CentralV2User centralV2User1 = new CentralV2User.CentralV2UserBuilder().setId((long) 1).setCreated(null)
                .setModified(null).setCreatedId((long) 1).setModifiedId((long) 1).setRowNum((long) 1).setOrgId((long) 1)
                .setManagerId((long) 1).setFirstName("test").setMiddleInitial("test").setLastName("test")
                .setPhone("test").setFax("test").setCellular("test").setEmail("test").setAddressId((long) 1)
                .setAlertMethodCd("test").setHrid("test").setOrgUserId("test").setOrgCode("test").setAddress1("test")
                .setAddress2("test").setCity("test").setState("test").setZipCode("test").setCountry("test")
                .setOrgManagerUserId("test").setLocationClli("test").setBusinessCountryCode("test")
                .setBusinessCountryName("test").setBusinessUnit("test").setBusinessUnitName("test")
                .setDepartment("test").setDepartmentName("test").setCompanyCode("test").setCompany("test")
                .setZipCodeSuffix("test").setJobTitle("test").setCommandChain("test").setSiloStatus("test")
                .setCostCenter("test").setFinancialLocCode("test").setLoginId("test").setLoginPwd("test")
                .setLastLoginDate(null).setActive(false).setInternal(false).setSelectedProfileId((long) 1)
                .setTimeZoneId((long) 1).setOnline(false).setChatId("test").setUserApps(userApps)
                .setPseudoRoles(pseudoRoles).createCentralV2User();
		
		
		assertEquals(centralV2User, centralV2User1);
		assertEquals(centralV2User.hashCode(), centralV2User1.hashCode());
		assertTrue(centralV2User.equals(centralV2User1));
	}
	
	@Test
	public void unt_IdTest(){
		Long defaultValue=123L;
		centralUser.setId(defaultValue);
		assertEquals(defaultValue, centralUser.getId());
	}
	
	@Test
	public void unt_createdTest(){
		Date defaultValue=new Date();
		centralUser.setCreated(defaultValue);
		assertEquals(defaultValue, centralUser.getCreated());
	}
	
	@Test
	public void unt_modifiedTest(){
		Date defaultValue=new Date();
		centralUser.setModified(defaultValue);
		assertEquals(defaultValue, centralUser.getModified());
	}
	
	@Test
	public void unt_createdIdTest(){
		Long defaultValue=123L;
		centralUser.setCreatedId(defaultValue);
		assertEquals(defaultValue, centralUser.getCreatedId());
	}
	
	@Test
	public void unt_modifiedIdTest(){
		Long defaultValue=123L;
		centralUser.setModifiedId(defaultValue);
		assertEquals(defaultValue, centralUser.getModifiedId());
	}
	
	@Test
	public void unt_rowNumTest(){
		Long defaultValue=123L;
		centralUser.setRowNum(defaultValue);
		assertEquals(defaultValue, centralUser.getRowNum());
	}
	
	@Test
	public void unt_orgIdTest(){
		Long defaultValue=123L;
		centralUser.setOrgId(defaultValue);
		assertEquals(defaultValue, centralUser.getOrgId());
	}
	
	@Test
	public void unt_managerIdTest(){
		Long defaultValue=123L;
		centralUser.setManagerId(defaultValue);
		assertEquals(defaultValue, centralUser.getManagerId());
	}
	
	@Test
	public void unt_firstNameTest(){
		String defaultValue="test";
		centralUser.setFirstName(defaultValue);
		assertEquals(defaultValue, centralUser.getFirstName());
	}
	
	@Test
	public void unt_middleInitialTest(){
		String defaultValue="test";
		centralUser.setMiddleInitial(defaultValue);
		assertEquals(defaultValue, centralUser.getMiddleInitial());
	}
	
	@Test
	public void unt_lastNameTest(){
		String defaultValue="test";
		centralUser.setLastName(defaultValue);
		assertEquals(defaultValue, centralUser.getLastName());
	}
	
	@Test
	public void unt_phoneTest(){
		String defaultValue="test";
		centralUser.setPhone(defaultValue);
		assertEquals(defaultValue, centralUser.getPhone());
	}
	
	@Test
	public void unt_faxTest(){
		String defaultValue="test";
		centralUser.setFax(defaultValue);
		assertEquals(defaultValue, centralUser.getFax());
	}
	
	@Test
	public void unt_cellularTest(){
		String defaultValue="test";
		centralUser.setCellular(defaultValue);
		assertEquals(defaultValue, centralUser.getCellular());
	}
	
	@Test
	public void unt_emailTest(){
		String defaultValue="test";
		centralUser.setEmail(defaultValue);
		assertEquals(defaultValue, centralUser.getEmail());
	}
	
	@Test
	public void unt_addressTest(){
		Long defaultValue=123L;
		centralUser.setAddressId(defaultValue);
		assertEquals(defaultValue, centralUser.getAddressId());
	}
	
	@Test
	public void unt_alertMethodCdTest(){
		String defaultValue="testAlert";
		centralUser.setAlertMethodCd(defaultValue);
		assertEquals(defaultValue, centralUser.getAlertMethodCd());
	}
	
	@Test
	public void unt_hridTest(){
		String defaultValue="testHrid";
		centralUser.setHrid(defaultValue);
		assertEquals(defaultValue, centralUser.getHrid());
	}
	
	@Test
	public void unt_orgUserIdTest(){
		String defaultValue="testorgUseriD";
		centralUser.setOrgUserId(defaultValue);
		assertEquals(defaultValue, centralUser.getOrgUserId());
	}
	
	@Test
	public void unt_orgCodeTest(){
		String defaultValue="testorgcode";
		centralUser.setOrgCode(defaultValue);
		assertEquals(defaultValue, centralUser.getOrgCode());
	}
	
	@Test
	public void unt_address1Test(){
		String defaultValue="testaddress1";
		centralUser.setAddress1(defaultValue);
		assertEquals(defaultValue, centralUser.getAddress1());
	}
	
	@Test
	public void unt_address2Test(){
		String defaultValue="testaddress2";
		centralUser.setAddress2(defaultValue);
		assertEquals(defaultValue, centralUser.getAddress2());
	}
	
	@Test
	public void unt_cityTest(){
		String defaultValue="testcity";
		centralUser.setCity(defaultValue);
		assertEquals(defaultValue, centralUser.getCity());
	}
	
	@Test
	public void unt_stateTest(){
		String defaultValue="testcity";
		centralUser.setState(defaultValue);
		assertEquals(defaultValue, centralUser.getState());
	}
	
	@Test
	public void unt_zipcodeTest(){
		String defaultValue="testzipcode";
		centralUser.setZipCode(defaultValue);
		assertEquals(defaultValue, centralUser.getZipCode());
	}
	
	@Test
	public void unt_countryTest(){
		String defaultValue="testcountry";
		centralUser.setCountry(defaultValue);
		assertEquals(defaultValue, centralUser.getCountry());
	}
	
	@Test
	public void unt_orgManagerUserIdTest(){
		String defaultValue="testOrgManagerUserId";
		centralUser.setOrgManagerUserId(defaultValue);
		assertEquals(defaultValue, centralUser.getOrgManagerUserId());
	} 
	
	@Test
	public void unt_locationClliTest(){
		String defaultValue="testLocationClli";
		centralUser.setLocationClli(defaultValue);
		assertEquals(defaultValue, centralUser.getLocationClli());
	}
	
	@Test
	public void unt_businessCountryCodeTest(){
		String defaultValue="testBusinessCountryCode";
		centralUser.setBusinessCountryCode(defaultValue);
		assertEquals(defaultValue, centralUser.getBusinessCountryCode());
	}
	
	@Test
	public void unt_businessCountryNameTest(){
		String defaultValue="testBusinessCountryName";
		centralUser.setBusinessCountryName(defaultValue);
		assertEquals(defaultValue, centralUser.getBusinessCountryName());
	}
	
	@Test
	public void unt_businessUnitTest(){
		String defaultValue="testBusinessUnit";
		centralUser.setBusinessUnit(defaultValue);
		assertEquals(defaultValue, centralUser.getBusinessUnit());
	}
	
	@Test
	public void unt_businessUnitNameTest(){
		String defaultValue="testBusinessUnitName";
		centralUser.setBusinessUnitName(defaultValue);
		assertEquals(defaultValue, centralUser.getBusinessUnitName());
	}
	
	@Test
	public void unt_departmentTest(){
		String defaultValue="testdepartment";
		centralUser.setDepartment(defaultValue);
		assertEquals(defaultValue, centralUser.getDepartment());
	}
	
	@Test
	public void unt_departmentNameTest(){
		String defaultValue="testdepartmentName";
		centralUser.setDepartmentName(defaultValue);
		assertEquals(defaultValue, centralUser.getDepartmentName());
	}
	
	@Test
	public void unt_companyCodeTest(){
		String defaultValue="testcompanyCode";
		centralUser.setCompanyCode(defaultValue);
		assertEquals(defaultValue, centralUser.getCompanyCode());
	}
	
	@Test
	public void unt_companyTest(){
		String defaultValue="testcompany";
		centralUser.setCompany(defaultValue);
		assertEquals(defaultValue, centralUser.getCompany());
	}
	
	@Test
	public void unt_zipcodeSuffixTest(){
		String defaultValue="testcompany";
		centralUser.setZipCodeSuffix(defaultValue);
		assertEquals(defaultValue, centralUser.getZipCodeSuffix());
	}
	
	@Test
	public void unt_jobTitleTest(){
		String defaultValue="testjobTitle";
		centralUser.setJobTitle(defaultValue);
		assertEquals(defaultValue, centralUser.getJobTitle());
	}
	
	@Test
	public void unt_commandChainTest(){
		String defaultValue="testcommandChain";
		centralUser.setCommandChain(defaultValue);
		assertEquals(defaultValue, centralUser.getCommandChain());
	}
	
	@Test
	public void unt_siloStatusTest(){
		String defaultValue="testcommandChain";
		centralUser.setSiloStatus(defaultValue);
		assertEquals(defaultValue, centralUser.getSiloStatus());
	}
	
	@Test
	public void unt_costCenterTest(){
		String defaultValue="testcommandChain";
		centralUser.setCostCenter(defaultValue);
		assertEquals(defaultValue, centralUser.getCostCenter());
	}
	
	@Test
	public void unt_financialLocCodeTest(){
		String defaultValue="testcommandChain";
		centralUser.setFinancialLocCode(defaultValue);
		assertEquals(defaultValue, centralUser.getFinancialLocCode());
	}
	
	@Test
	public void unt_loginIdTest(){
		String defaultValue="testcommandChain";
		centralUser.setLoginId(defaultValue);
		assertEquals(defaultValue, centralUser.getLoginId());
	}
		
	@Test
	public void unt_loginPwdTest(){
		String defaultValue="testcommandChain";
		centralUser.setLoginPwd(defaultValue);
		assertEquals(defaultValue, centralUser.getLoginPwd());
	}
	
	@Test
	public void unt_lastLoginDateTest(){
		Date defaultValue=new Date();
		centralUser.setLastLoginDate(defaultValue);
		assertEquals(defaultValue, centralUser.getLastLoginDate());
	}
	
	@Test
	public void unt_activeTest(){
		boolean defaultValue= false;
		centralUser.setActive(defaultValue);
		assertEquals(defaultValue, centralUser.isActive());
	}
	
	@Test
	public void unt_internalTest(){
		boolean defaultValue= false;
		centralUser.setInternal(defaultValue);
		assertEquals(defaultValue, centralUser.isInternal());
	}
	
	@Test
	public void unt_selectedProfileIdTest(){
		Long defaultValue= (long)123;
		centralUser.setSelectedProfileId(defaultValue);
		assertEquals(defaultValue, centralUser.getSelectedProfileId());
	}
	
	@Test
	public void unt_timeZoneIdTest(){
		Long defaultValue= (long)123;
		centralUser.setTimeZoneId(defaultValue);
		assertEquals(defaultValue, centralUser.getTimeZoneId());
	}
	
	@Test
	public void unt_onlineTest(){
		boolean defaultValue= false;
		centralUser.setOnline(defaultValue);
		assertEquals(defaultValue, centralUser.isOnline());
	}
	
	@Test
	public void unt_chatIdTest(){
		String defaultValue= "false";
		centralUser.setChatId(defaultValue);
		assertEquals(defaultValue, centralUser.getChatId());
	}
	
	@Test
	public void unt_userAppsTest(){
		Set<CentralUserApp> userApps= new HashSet<CentralUserApp>();
		centralUser.setUserApps(userApps);
		assertEquals(userApps, centralUser.getUserApps());
	}
	
	@Test
	public void unt_getPseudoRolesTest(){
		Set<CentralRole> centralRoles= new HashSet<CentralRole>();
		centralUser.setPseudoRoles(centralRoles);
		assertEquals(centralRoles, centralUser.getPseudoRoles());
	}
}
