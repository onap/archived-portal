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
package org.openecomp.portalapp.portal.test.core;

import java.util.Date;

import org.openecomp.portalapp.portal.domain.EPUser;

public class MockEPUser {

	public EPUser mockEPUser() {

		EPUser ePUser = new EPUser();
		ePUser.setOrgId(null);
		ePUser.setManagerId(null);
		ePUser.setFirstName("test");
		ePUser.setLastName("test");
		ePUser.setMiddleInitial(null);
		ePUser.setPhone(null);
		ePUser.setFax(null);
		ePUser.setCellular(null);
		ePUser.setEmail(null);
		ePUser.setAddressId(null);
		ePUser.setAlertMethodCd(null);
		ePUser.setHrid(null);
		ePUser.setOrgUserId("guest");
		ePUser.setOrgCode(null);
		ePUser.setAddress1(null);
		ePUser.setAddress2(null);
		ePUser.setCity(null);
		ePUser.setState(null);
		ePUser.setZipCode(null);
		ePUser.setCountry(null);
		ePUser.setOrgManagerUserId(null);
		ePUser.setLocationClli(null);
		ePUser.setBusinessCountryCode(null);
		ePUser.setBusinessCountryName(null);
		ePUser.setBusinessUnit(null);
		ePUser.setBusinessUnitName(null);
		ePUser.setDepartment(null);
		ePUser.setDepartmentName(null);
		ePUser.setCompanyCode(null);
		ePUser.setCompany(null);
		ePUser.setZipCodeSuffix(null);
		ePUser.setJobTitle(null);
		ePUser.setCommandChain(null);
		ePUser.setSiloStatus(null);
		ePUser.setCostCenter(null);
		ePUser.setFinancialLocCode(null);

		ePUser.setLoginId(null);
		ePUser.setLoginPwd(null);
		Date date = new Date();
		ePUser.setLastLoginDate(date);
		ePUser.setActive(true);
		ePUser.setInternal(false);
		ePUser.setSelectedProfileId(null);
		ePUser.setTimeZoneId(null);
		ePUser.setOnline(true);
		ePUser.setChatId(null);
		ePUser.setUserApps(null);
		ePUser.setPseudoRoles(null);

		ePUser.setId((long) 99999999);

		return ePUser;

	}
}
