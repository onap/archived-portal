package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUser;

public class EPUserTest {

	public EPUser mockEPUser(){
		
		EPUser epUser = new EPUser();
		
		epUser.setId((long)1);
		epUser.setManagerId((long) 1234);
		epUser.setFirstName("test");
		epUser.setLastName("test");
		epUser.setMiddleInitial("test");
		epUser.setPhone("test");
		epUser.setFax("test");
		epUser.setCellular("test");
		epUser.setEmail("test");
		epUser.setAddressId((long) 123); 
		epUser.setAlertMethodCd("test");
		epUser.setHrid("test");
		epUser.setOrgUserId("test");
		epUser.setOrgCode("test");
		epUser.setAddress1("test");
		epUser.setAddress2("test");
		epUser.setCity("test");
		epUser.setState("test");
		epUser.setZipCode("test");
		epUser.setCountry("test");
		epUser.setOrgManagerUserId("test");
		epUser.setLocationClli("test");
		epUser.setBusinessCountryCode("test");
		epUser.setBusinessCountryName("test");
		epUser.setBusinessUnit("test");
		epUser.setBusinessUnitName("test");
		epUser.setDepartment("test");
		epUser.setDepartmentName("test");
		epUser.setCompanyCode("test");
		epUser.setCompany("test");
		epUser.setZipCodeSuffix("test");
		epUser.setJobTitle("test");
		epUser.setCommandChain("test");
		epUser.setSiloStatus("test");
		epUser.setCostCenter("test");
		epUser.setFinancialLocCode("test");
		epUser.setLoginId("test");
		epUser.setLoginPwd("test");
		epUser.setLastLoginDate(new Date());
		epUser.setActive(false);
		epUser.setInternal(false);
		epUser.setSelectedProfileId((long) 12345);
		epUser.setTimeZoneId((long) 12345);
		epUser.setOnline(false);
		epUser.setChatId("test");
		return epUser;
				    
	}
	
	@Test
	public void userTest(){
		EPUser user = mockEPUser();
		
		assertEquals(user.getId(), new Long(1));
		assertEquals(user.getManagerId(), new Long(1234));
		assertEquals(user.getFirstName(), "test");
		assertEquals(user.getLastName(), "test");
		assertEquals(user.getMiddleInitial(), "test");
		assertEquals(user.getPhone(), "test");
		assertEquals(user.getFax(), "test");
		assertEquals(user.getCellular(), "test");		
		assertEquals(user.getEmail(), "test");
		assertEquals(user.getAddressId(), new Long(123) );
		assertEquals(user.getAlertMethodCd(), "test");
		assertEquals(user.getHrid(), "test");
		assertEquals(user.getOrgUserId(), "test");
		assertEquals(user.getOrgCode(), "test");
		assertEquals(user.getAddress1(), "test");
		assertEquals(user.getAddress2(), "test");
		assertEquals(user.getState(), "test");
		assertEquals(user.getZipCode(), "test");
		assertEquals(user.getCountry(), "test");
		assertEquals(user.getOrgManagerUserId(), "test");
		assertEquals(user.getLocationClli(), "test");
		assertEquals(user.getBusinessCountryCode(), "test");
		assertEquals(user.getBusinessCountryName(), "test");
		assertEquals(user.getBusinessUnit(), "test");
		assertEquals(user.getBusinessUnitName(), "test");
		assertEquals(user.getDepartment(), "test");
		assertEquals(user.getDepartmentName(), "test");
		assertEquals(user.getCompanyCode(), "test");
		assertEquals(user.getCompany(), "test");
		assertEquals(user.getZipCodeSuffix(), "test");
		assertEquals(user.getJobTitle(), "test");
		assertEquals(user.getCommandChain(), "test");
		assertEquals(user.getSiloStatus(), "test");
		assertEquals(user.getFinancialLocCode(), "test");
		assertEquals(user.getLoginId(), "test");
		assertEquals(user.getLoginPwd(), "test");
		assertEquals(user.getActive(), false);
		assertEquals(user.getInternal(), false);
		assertEquals(user.getSelectedProfileId(), new Long (12345));
		assertEquals(user.getTimeZoneId(), new Long (12345));
		assertEquals(user.getChatId(), "test");
		
		//assertEquals(user.toString(), "EPUser [orgId=null, managerId=1234, firstName=test, middleInitial=test, lastName=test, phone=test, fax=test, cellular=test, email=test, addressId=123, alertMethodCd=test, hrid=test, orgUserId=test, orgCode=test, address1=test, address2=test, city=test, state=test, zipCode=test, country=test, orgManagerUserId=test, locationClli=test, businessCountryCode=test, businessCountryName=test, businessUnit=test, businessUnitName=test, department=test, departmentName=test, companyCode=test, company=test, zipCodeSuffix=test, jobTitle=test, commandChain=test, siloStatus=test, costCenter=test, financialLocCode=test, loginId=test, loginPwd=test, lastLoginDate=Tue Sep 05 11:04:49 EDT 2017, active=false, internal=false, selectedProfileId=12345, timeZoneId=12345, online=false, chatId=test, isGuest=false, userApps=[], pseudoRoles=[]]");
	}
	
}
